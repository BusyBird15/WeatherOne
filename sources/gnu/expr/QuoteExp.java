package gnu.expr;

import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.kawa.util.IdentityHashTable;
import gnu.lists.AbstractFormat;
import gnu.mapping.CallContext;
import gnu.mapping.MethodProc;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure;
import gnu.mapping.Values;
import gnu.mapping.WrongArguments;
import gnu.text.SourceLocator;

public class QuoteExp extends Expression {
    public static final int EXPLICITLY_TYPED = 2;
    public static final int SHARED_CONSTANT = 4;
    public static QuoteExp abstractExp = makeShared(Special.abstractSpecial);
    public static final QuoteExp classObjectExp = makeShared(Type.objectType);
    public static QuoteExp falseExp = makeShared(Boolean.FALSE);
    public static QuoteExp nullExp = makeShared((Object) null, Type.nullType);
    public static QuoteExp trueExp = makeShared(Boolean.TRUE);
    public static QuoteExp undefined_exp = makeShared(Special.undefined);
    public static QuoteExp voidExp = makeShared(Values.empty, Type.voidType);
    protected Type type;
    Object value;

    public final Object getValue() {
        return this.value;
    }

    public final Object valueIfConstant() {
        return this.value;
    }

    public final Type getRawType() {
        return this.type;
    }

    public final Type getType() {
        if (this.type != null) {
            return this.type;
        }
        if (this.value == Values.empty) {
            return Type.voidType;
        }
        if (this.value == null) {
            return Type.nullType;
        }
        if (this == undefined_exp) {
            return Type.pointer_type;
        }
        return Type.make(this.value.getClass());
    }

    public void setType(Type type2) {
        this.type = type2;
        setFlag(2);
    }

    public boolean isExplicitlyTyped() {
        return getFlag(2);
    }

    public boolean isSharedConstant() {
        return getFlag(4);
    }

    public static QuoteExp getInstance(Object value2) {
        return getInstance(value2, (SourceLocator) null);
    }

    public static QuoteExp getInstance(Object value2, SourceLocator position) {
        if (value2 == null) {
            return nullExp;
        }
        if (value2 == Type.pointer_type) {
            return classObjectExp;
        }
        if (value2 == Special.undefined) {
            return undefined_exp;
        }
        if (value2 == Values.empty) {
            return voidExp;
        }
        if (value2 instanceof Boolean) {
            return ((Boolean) value2).booleanValue() ? trueExp : falseExp;
        }
        QuoteExp q = new QuoteExp(value2);
        if (position != null) {
            q.setLocation(position);
        }
        return q;
    }

    static QuoteExp makeShared(Object value2) {
        QuoteExp exp = new QuoteExp(value2);
        exp.setFlag(4);
        return exp;
    }

    static QuoteExp makeShared(Object value2, Type type2) {
        QuoteExp exp = new QuoteExp(value2, type2);
        exp.setFlag(4);
        return exp;
    }

    public QuoteExp(Object val) {
        this.value = val;
    }

    public QuoteExp(Object val, Type type2) {
        this.value = val;
        setType(type2);
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        return false;
    }

    public void apply(CallContext ctx) {
        ctx.writeValue(this.value);
    }

    public void compile(Compilation comp, Target target) {
        if (this.type == null || this.type == Type.pointer_type || (target instanceof IgnoreTarget) || ((this.type instanceof ObjectType) && this.type.isInstance(this.value))) {
            comp.compileConstant(this.value, target);
            return;
        }
        comp.compileConstant(this.value, StackTarget.getInstance(this.type));
        target.compileFromStack(comp, this.type);
    }

    public Expression deepCopy(IdentityHashTable mapper) {
        return this;
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        return visitor.visitQuoteExp(this, d);
    }

    public Expression validateApply(ApplyExp exp, InlineCalls visitor, Type required, Declaration decl) {
        ApplyExp nexp;
        Expression e;
        if (this == undefined_exp) {
            return exp;
        }
        Object fval = getValue();
        if (!(fval instanceof Procedure)) {
            return visitor.noteError((decl == null || fval == null) ? "called value is not a procedure" : "calling " + decl.getName() + " which is a " + fval.getClass().getName());
        }
        Procedure proc = (Procedure) fval;
        int nargs = exp.getArgCount();
        String msg = WrongArguments.checkArgCount(proc, nargs);
        if (msg != null) {
            return visitor.noteError(msg);
        }
        Expression inlined = visitor.maybeInline(exp, required, proc);
        if (inlined != null) {
            return inlined;
        }
        Expression[] args = exp.args;
        MethodProc asMProc = proc instanceof MethodProc ? (MethodProc) proc : null;
        for (int i = 0; i < nargs; i++) {
            Type ptype = asMProc != null ? asMProc.getParameterType(i) : null;
            if (i == nargs - 1 && ptype != null && asMProc.maxArgs() < 0 && i == asMProc.minArgs()) {
                ptype = null;
            }
            args[i] = visitor.visit(args[i], ptype);
        }
        if (exp.getFlag(4) && (e = exp.inlineIfConstant(proc, visitor)) != exp) {
            return visitor.visit(e, required);
        }
        Compilation comp = visitor.getCompilation();
        if (!comp.inlineOk(proc)) {
            return exp;
        }
        if (ApplyExp.asInlineable(proc) == null) {
            PrimProcedure mproc = PrimProcedure.getMethodFor(proc, decl, exp.args, comp.getLanguage());
            if (mproc == null) {
                return exp;
            }
            if (mproc.getStaticFlag() || decl == null) {
                nexp = new ApplyExp((Procedure) mproc, exp.args);
            } else if (decl.base == null) {
                return exp;
            } else {
                Expression[] margs = new Expression[(nargs + 1)];
                System.arraycopy(exp.getArgs(), 0, margs, 1, nargs);
                margs[0] = new ReferenceExp(decl.base);
                nexp = new ApplyExp((Procedure) mproc, margs);
            }
            return nexp.setLine((Expression) exp);
        } else if (exp.getFunction() != this) {
            return new ApplyExp((Expression) this, exp.getArgs()).setLine((Expression) exp);
        } else {
            return exp;
        }
    }

    public boolean side_effects() {
        return false;
    }

    public String toString() {
        return "QuoteExp[" + this.value + "]";
    }

    /* JADX INFO: finally extract failed */
    public void print(OutPort out) {
        out.startLogicalBlock("(Quote", ")", 2);
        out.writeSpaceLinear();
        Object val = this.value;
        if (val instanceof Expression) {
            val = val.toString();
        }
        AbstractFormat saveFormat = out.objectFormat;
        try {
            out.objectFormat = Language.getDefaultLanguage().getFormat(true);
            out.print(val);
            if (this.type != null) {
                out.print(" ::");
                out.print(this.type.getName());
            }
            out.objectFormat = saveFormat;
            out.endLogicalBlock(")");
        } catch (Throwable th) {
            out.objectFormat = saveFormat;
            throw th;
        }
    }
}
