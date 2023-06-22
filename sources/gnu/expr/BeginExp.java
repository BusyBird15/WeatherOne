package gnu.expr;

import gnu.bytecode.Type;
import gnu.lists.Consumer;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.OutPort;
import java.util.Vector;

public class BeginExp extends Expression {
    Vector compileOptions;
    Expression[] exps;
    int length;

    public BeginExp() {
    }

    public BeginExp(Expression[] ex) {
        this.exps = ex;
        this.length = ex.length;
    }

    public BeginExp(Expression exp0, Expression exp1) {
        this.exps = new Expression[2];
        this.exps[0] = exp0;
        this.exps[1] = exp1;
        this.length = 2;
    }

    public static final Expression canonicalize(Expression exp) {
        if (!(exp instanceof BeginExp)) {
            return exp;
        }
        BeginExp bexp = (BeginExp) exp;
        if (bexp.compileOptions != null) {
            return exp;
        }
        int len = bexp.length;
        if (len == 0) {
            return QuoteExp.voidExp;
        }
        if (len == 1) {
            return canonicalize(bexp.exps[0]);
        }
        return exp;
    }

    public static final Expression canonicalize(Expression[] exps2) {
        int len = exps2.length;
        if (len == 0) {
            return QuoteExp.voidExp;
        }
        if (len == 1) {
            return canonicalize(exps2[0]);
        }
        return new BeginExp(exps2);
    }

    public final void add(Expression exp) {
        if (this.exps == null) {
            this.exps = new Expression[8];
        }
        if (this.length == this.exps.length) {
            Expression[] ex = new Expression[(this.length * 2)];
            System.arraycopy(this.exps, 0, ex, 0, this.length);
            this.exps = ex;
        }
        Expression[] expressionArr = this.exps;
        int i = this.length;
        this.length = i + 1;
        expressionArr[i] = exp;
    }

    public final Expression[] getExpressions() {
        return this.exps;
    }

    public final int getExpressionCount() {
        return this.length;
    }

    public final void setExpressions(Expression[] exps2) {
        this.exps = exps2;
        this.length = exps2.length;
    }

    public void setCompileOptions(Vector options) {
        this.compileOptions = options;
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        return false;
    }

    public void apply(CallContext ctx) throws Throwable {
        int n = this.length;
        Consumer consumerSave = ctx.consumer;
        ctx.consumer = VoidConsumer.instance;
        int i = 0;
        while (i < n - 1) {
            try {
                this.exps[i].eval(ctx);
                i++;
            } catch (Throwable th) {
                ctx.consumer = consumerSave;
                throw th;
            }
        }
        ctx.consumer = consumerSave;
        this.exps[i].apply(ctx);
    }

    public void pushOptions(Compilation comp) {
        if (this.compileOptions != null && comp != null) {
            comp.currentOptions.pushOptionValues(this.compileOptions);
        }
    }

    public void popOptions(Compilation comp) {
        if (this.compileOptions != null && comp != null) {
            comp.currentOptions.popOptionValues(this.compileOptions);
        }
    }

    public void compile(Compilation comp, Target target) {
        pushOptions(comp);
        try {
            int n = this.length;
            int i = 0;
            while (i < n - 1) {
                this.exps[i].compileWithPosition(comp, Target.Ignore);
                i++;
            }
            this.exps[i].compileWithPosition(comp, target);
        } finally {
            popOptions(comp);
        }
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        pushOptions(visitor.comp);
        try {
            return visitor.visitBeginExp(this, d);
        } finally {
            popOptions(visitor.comp);
        }
    }

    /* access modifiers changed from: protected */
    public <R, D> void visitChildren(ExpVisitor<R, D> visitor, D d) {
        this.exps = visitor.visitExps(this.exps, this.length, d);
    }

    public void print(OutPort out) {
        out.startLogicalBlock("(Begin", ")", 2);
        out.writeSpaceFill();
        printLineColumn(out);
        if (this.compileOptions != null) {
            out.writeSpaceFill();
            out.startLogicalBlock("(CompileOptions", ")", 2);
            int sizeOptions = this.compileOptions.size();
            for (int i = 0; i < sizeOptions; i += 3) {
                Object key = this.compileOptions.elementAt(i);
                Object value = this.compileOptions.elementAt(i + 2);
                out.writeSpaceFill();
                out.startLogicalBlock("", "", 2);
                out.print(key);
                out.print(':');
                out.writeSpaceLinear();
                out.print(value);
                out.endLogicalBlock("");
            }
            out.endLogicalBlock(")");
        }
        int n = this.length;
        for (int i2 = 0; i2 < n; i2++) {
            out.writeSpaceLinear();
            this.exps[i2].print(out);
        }
        out.endLogicalBlock(")");
    }

    public Type getType() {
        return this.exps[this.length - 1].getType();
    }
}
