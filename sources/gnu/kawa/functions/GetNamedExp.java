package gnu.kawa.functions;

import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.InlineCalls;
import gnu.expr.PrimProcedure;
import gnu.expr.ReferenceExp;
import gnu.mapping.Procedure;

/* compiled from: CompileNamedPart */
class GetNamedExp extends ApplyExp {
    static final Declaration castDecl = Declaration.getDeclarationFromStatic("gnu.kawa.functions.Convert", "as");
    static final Declaration fieldDecl = Declaration.getDeclarationFromStatic("gnu.kawa.reflect.SlotGet", "field");
    static final Declaration instanceOfDecl = Declaration.getDeclarationFromStatic("kawa.standard.Scheme", "instanceOf");
    static final Declaration invokeDecl = Declaration.getDeclarationFromStatic("gnu.kawa.reflect.Invoke", "invoke");
    static final Declaration invokeStaticDecl = Declaration.getDeclarationFromStatic("gnu.kawa.reflect.Invoke", "invokeStatic");
    static final Declaration makeDecl = Declaration.getDeclarationFromStatic("gnu.kawa.reflect.Invoke", "make");
    static final Declaration staticFieldDecl = Declaration.getDeclarationFromStatic("gnu.kawa.reflect.SlotGet", "staticField");
    public String combinedName;
    char kind;
    PrimProcedure[] methods;

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = gnu.mapping.Environment.getCurrent();
        r2 = r0.getSymbol(r6.combinedName);
        r3 = gnu.mapping.Location.UNBOUND;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void apply(gnu.mapping.CallContext r7) throws java.lang.Throwable {
        /*
            r6 = this;
            java.lang.String r5 = r6.combinedName
            if (r5 == 0) goto L_0x001b
            gnu.mapping.Environment r0 = gnu.mapping.Environment.getCurrent()
            java.lang.String r5 = r6.combinedName
            gnu.mapping.Symbol r2 = r0.getSymbol(r5)
            java.lang.String r3 = gnu.mapping.Location.UNBOUND
            r1 = 0
            java.lang.Object r4 = r0.get(r2, r1, r3)
            if (r4 == r3) goto L_0x001b
            r7.writeValue(r4)
        L_0x001a:
            return
        L_0x001b:
            super.apply(r7)
            goto L_0x001a
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.functions.GetNamedExp.apply(gnu.mapping.CallContext):void");
    }

    public GetNamedExp(Expression[] args) {
        super((Procedure) GetNamedPart.getNamedPart, args);
    }

    /* access modifiers changed from: protected */
    public GetNamedExp setProcedureKind(char kind2) {
        this.type = Compilation.typeProcedure;
        this.kind = kind2;
        return this;
    }

    public Expression validateApply(ApplyExp exp, InlineCalls visitor, Type required, Declaration decl) {
        Declaration decl2;
        Expression[] xargs;
        Expression[] pargs = getArgs();
        Expression context = pargs[0];
        Expression[] args = exp.getArgs();
        switch (this.kind) {
            case 'C':
                decl2 = castDecl;
                xargs = new Expression[(args.length + 1)];
                System.arraycopy(args, 1, xargs, 2, args.length - 1);
                xargs[0] = context;
                xargs[1] = args[0];
                break;
            case 'I':
                decl2 = instanceOfDecl;
                xargs = new Expression[(args.length + 1)];
                System.arraycopy(args, 1, xargs, 2, args.length - 1);
                xargs[0] = args[0];
                xargs[1] = context;
                break;
            case 'M':
                decl2 = invokeDecl;
                xargs = new Expression[(args.length + 2)];
                xargs[0] = pargs[0];
                xargs[1] = pargs[1];
                System.arraycopy(args, 0, xargs, 2, args.length);
                break;
            case 'N':
                decl2 = makeDecl;
                xargs = new Expression[(args.length + 1)];
                System.arraycopy(args, 0, xargs, 1, args.length);
                xargs[0] = context;
                break;
            case 'S':
                decl2 = invokeStaticDecl;
                xargs = new Expression[(args.length + 2)];
                xargs[0] = context;
                xargs[1] = pargs[1];
                System.arraycopy(args, 0, xargs, 2, args.length);
                break;
            default:
                return exp;
        }
        ApplyExp result = new ApplyExp((Expression) new ReferenceExp(decl2), xargs);
        result.setLine((Expression) exp);
        return visitor.visit((Expression) result, required);
    }

    public boolean side_effects() {
        if (this.kind == 'S' || this.kind == 'N' || this.kind == 'C' || this.kind == 'I') {
            return false;
        }
        if (this.kind == 'M') {
            return getArgs()[0].side_effects();
        }
        return true;
    }
}
