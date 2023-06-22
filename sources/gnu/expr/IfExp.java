package gnu.expr;

import gnu.bytecode.CodeAttr;
import gnu.bytecode.Label;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.mapping.CallContext;
import gnu.mapping.OutPort;
import gnu.mapping.Values;

public class IfExp extends Expression {
    Expression else_clause;
    Expression test;
    Expression then_clause;

    public IfExp(Expression i, Expression t, Expression e) {
        this.test = i;
        this.then_clause = t;
        this.else_clause = e;
    }

    public Expression getTest() {
        return this.test;
    }

    public Expression getThenClause() {
        return this.then_clause;
    }

    public Expression getElseClause() {
        return this.else_clause;
    }

    /* access modifiers changed from: protected */
    public final Language getLanguage() {
        return Language.getDefaultLanguage();
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        return false;
    }

    public void apply(CallContext ctx) throws Throwable {
        if (getLanguage().isTrue(this.test.eval(ctx))) {
            this.then_clause.apply(ctx);
        } else if (this.else_clause != null) {
            this.else_clause.apply(ctx);
        }
    }

    /* access modifiers changed from: package-private */
    public Expression select(boolean truth) {
        if (truth) {
            return this.then_clause;
        }
        return this.else_clause == null ? QuoteExp.voidExp : this.else_clause;
    }

    public void compile(Compilation comp, Target target) {
        compile(this.test, this.then_clause, this.else_clause == null ? QuoteExp.voidExp : this.else_clause, comp, target);
    }

    public static void compile(Expression test2, Expression then_clause2, Expression else_clause2, Compilation comp, Target target) {
        boolean falseInherited;
        boolean trueInherited;
        Label trueLabel;
        Language language = comp.getLanguage();
        CodeAttr code = comp.getCode();
        Label falseLabel = null;
        if (!(target instanceof ConditionalTarget) || !(else_clause2 instanceof QuoteExp)) {
            if ((else_clause2 instanceof ExitExp) && (((ExitExp) else_clause2).result instanceof QuoteExp)) {
                BlockExp block = ((ExitExp) else_clause2).block;
                if ((block.exitTarget instanceof IgnoreTarget) && (falseLabel = block.exitableBlock.exitIsGoto()) != null) {
                    falseInherited = true;
                }
            }
            falseInherited = false;
        } else {
            falseInherited = true;
            if (language.isTrue(((QuoteExp) else_clause2).getValue())) {
                falseLabel = ((ConditionalTarget) target).ifTrue;
            } else {
                falseLabel = ((ConditionalTarget) target).ifFalse;
            }
        }
        if (falseLabel == null) {
            falseLabel = new Label(code);
        }
        if (test2 != then_clause2 || !(target instanceof ConditionalTarget) || !(then_clause2 instanceof ReferenceExp)) {
            trueInherited = false;
            trueLabel = new Label(code);
        } else {
            trueInherited = true;
            trueLabel = ((ConditionalTarget) target).ifTrue;
        }
        ConditionalTarget ctarget = new ConditionalTarget(trueLabel, falseLabel, language);
        if (trueInherited) {
            ctarget.trueBranchComesFirst = false;
        }
        test2.compile(comp, (Target) ctarget);
        code.emitIfThen();
        if (!trueInherited) {
            trueLabel.define(code);
            Variable callContextSave = comp.callContextVar;
            then_clause2.compileWithPosition(comp, target);
            comp.callContextVar = callContextSave;
        }
        if (!falseInherited) {
            code.emitElse();
            falseLabel.define(code);
            Variable callContextSave2 = comp.callContextVar;
            if (else_clause2 == null) {
                comp.compileConstant(Values.empty, target);
            } else {
                else_clause2.compileWithPosition(comp, target);
            }
            comp.callContextVar = callContextSave2;
        } else {
            code.setUnreachable();
        }
        code.emitFi();
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        return visitor.visitIfExp(this, d);
    }

    /* access modifiers changed from: protected */
    public <R, D> void visitChildren(ExpVisitor<R, D> visitor, D d) {
        this.test = visitor.visitAndUpdate(this.test, d);
        if (visitor.exitValue == null) {
            this.then_clause = visitor.visitAndUpdate(this.then_clause, d);
        }
        if (visitor.exitValue == null && this.else_clause != null) {
            this.else_clause = visitor.visitAndUpdate(this.else_clause, d);
        }
    }

    public Type getType() {
        return Language.unionType(this.then_clause.getType(), this.else_clause == null ? Type.voidType : this.else_clause.getType());
    }

    public void print(OutPort out) {
        out.startLogicalBlock("(If ", false, ")");
        out.setIndentation(-2, false);
        this.test.print(out);
        out.writeSpaceLinear();
        this.then_clause.print(out);
        if (this.else_clause != null) {
            out.writeSpaceLinear();
            this.else_clause.print(out);
        }
        out.endLogicalBlock(")");
    }
}
