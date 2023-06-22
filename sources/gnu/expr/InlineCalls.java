package gnu.expr;

import gnu.bytecode.ClassType;
import gnu.bytecode.Method;
import gnu.bytecode.PrimType;
import gnu.bytecode.Type;
import gnu.kawa.reflect.CompileReflect;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.util.IdentityHashTable;
import gnu.mapping.Procedure;
import gnu.mapping.Values;
import gnu.math.IntNum;

public class InlineCalls extends ExpExpVisitor<Type> {
    private static Class[] inlinerMethodArgTypes;

    public static Expression inlineCalls(Expression exp, Compilation comp) {
        return new InlineCalls(comp).visit(exp, (Type) null);
    }

    public InlineCalls(Compilation comp) {
        setContext(comp);
    }

    public Expression visit(Expression exp, Type required) {
        if (!exp.getFlag(1)) {
            exp.setFlag(1);
            exp = (Expression) super.visit(exp, required);
            exp.setFlag(1);
        }
        return checkType(exp, required);
    }

    public Expression checkType(Expression exp, Type required) {
        Expression converted;
        Method amethod;
        boolean incompatible = true;
        Type expType = exp.getType();
        if (!(required instanceof ClassType) || !((ClassType) required).isInterface() || !expType.isSubtype(Compilation.typeProcedure) || expType.isSubtype(required)) {
            if (expType == Type.toStringType) {
                expType = Type.javalangStringType;
            }
            if (required == null || required.compare(expType) != -3) {
                incompatible = false;
            }
            if (incompatible && (required instanceof TypeValue) && (converted = ((TypeValue) required).convertValue(exp)) != null) {
                return converted;
            }
        } else if (!(exp instanceof LambdaExp) || (amethod = ((ClassType) required).checkSingleAbstractMethod()) == null) {
            incompatible = true;
        } else {
            LambdaExp lexp = (LambdaExp) exp;
            ObjectExp oexp = new ObjectExp();
            oexp.setLocation(exp);
            oexp.supers = new Expression[]{new QuoteExp(required)};
            oexp.setTypes(getCompilation());
            String mname = amethod.getName();
            oexp.addMethod(lexp, mname);
            Declaration addDeclaration = oexp.addDeclaration(mname, Compilation.typeProcedure);
            oexp.firstChild = lexp;
            oexp.declareParts(this.comp);
            return visit((Expression) oexp, required);
        }
        if (incompatible) {
            Language language = this.comp.getLanguage();
            this.comp.error('w', "type " + language.formatType(expType) + " is incompatible with required type " + language.formatType(required), exp);
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitApplyExp(ApplyExp exp, Type required) {
        Expression func = exp.func;
        if (func instanceof LambdaExp) {
            LambdaExp lambdaExp = (LambdaExp) func;
            Expression inlined = inlineCall((LambdaExp) func, exp.args, false);
            if (inlined != null) {
                return visit(inlined, required);
            }
        }
        Expression func2 = visit(func, (Type) null);
        exp.func = func2;
        return func2.validateApply(exp, this, required, (Declaration) null);
    }

    public final Expression visitApplyOnly(ApplyExp exp, Type required) {
        return exp.func.validateApply(exp, this, required, (Declaration) null);
    }

    public static Integer checkIntValue(Expression exp) {
        if (exp instanceof QuoteExp) {
            QuoteExp qarg = (QuoteExp) exp;
            Object value = qarg.getValue();
            if (!qarg.isExplicitlyTyped() && (value instanceof IntNum)) {
                IntNum ivalue = (IntNum) value;
                if (ivalue.inIntRange()) {
                    return Integer.valueOf(ivalue.intValue());
                }
            }
        }
        return null;
    }

    public static Long checkLongValue(Expression exp) {
        if (exp instanceof QuoteExp) {
            QuoteExp qarg = (QuoteExp) exp;
            Object value = qarg.getValue();
            if (!qarg.isExplicitlyTyped() && (value instanceof IntNum)) {
                IntNum ivalue = (IntNum) value;
                if (ivalue.inLongRange()) {
                    return Long.valueOf(ivalue.longValue());
                }
            }
        }
        return null;
    }

    public QuoteExp fixIntValue(Expression exp) {
        Integer ival = checkIntValue(exp);
        if (ival != null) {
            return new QuoteExp(ival, this.comp.getLanguage().getTypeFor(Integer.TYPE));
        }
        return null;
    }

    public QuoteExp fixLongValue(Expression exp) {
        Long ival = checkLongValue(exp);
        if (ival != null) {
            return new QuoteExp(ival, this.comp.getLanguage().getTypeFor(Long.TYPE));
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Expression visitQuoteExp(QuoteExp exp, Type required) {
        Object value;
        if (exp.getRawType() != null || exp.isSharedConstant() || (value = exp.getValue()) == null) {
            return exp;
        }
        Type vtype = this.comp.getLanguage().getTypeFor((Class) value.getClass());
        if (vtype == Type.toStringType) {
            vtype = Type.javalangStringType;
        }
        exp.type = vtype;
        if (!(required instanceof PrimType)) {
            return exp;
        }
        char sig1 = required.getSignature().charAt(0);
        QuoteExp ret = sig1 == 'I' ? fixIntValue(exp) : sig1 == 'J' ? fixLongValue(exp) : null;
        if (ret != null) {
            return ret;
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitReferenceExp(ReferenceExp exp, Type required) {
        Declaration decl = exp.getBinding();
        if (decl != null && decl.field == null && !decl.getCanWrite()) {
            Expression dval = decl.getValue();
            if ((dval instanceof QuoteExp) && dval != QuoteExp.undefined_exp) {
                return visitQuoteExp((QuoteExp) dval, required);
            }
            if ((dval instanceof ReferenceExp) && !decl.isAlias()) {
                ReferenceExp rval = (ReferenceExp) dval;
                Declaration rdecl = rval.getBinding();
                Type dtype = decl.getType();
                if (rdecl != null && !rdecl.getCanWrite() && ((dtype == null || dtype == Type.pointer_type || dtype == rdecl.getType()) && !rval.getDontDereference())) {
                    return visitReferenceExp(rval, required);
                }
            }
            if (!exp.isProcedureName() && (decl.flags & 1048704) == 1048704) {
                this.comp.error('e', "unimplemented: reference to method " + decl.getName() + " as variable");
                this.comp.error('e', decl, "here is the definition of ", "");
            }
        }
        return (Expression) super.visitReferenceExp(exp, required);
    }

    /* access modifiers changed from: protected */
    public Expression visitIfExp(IfExp exp, Type required) {
        Declaration decl;
        Expression test = (Expression) exp.test.visit(this, null);
        if ((test instanceof ReferenceExp) && (decl = ((ReferenceExp) test).getBinding()) != null) {
            Expression value = decl.getValue();
            if ((value instanceof QuoteExp) && value != QuoteExp.undefined_exp) {
                test = value;
            }
        }
        exp.test = test;
        if (this.exitValue == null) {
            exp.then_clause = visit(exp.then_clause, required);
        }
        if (this.exitValue == null && exp.else_clause != null) {
            exp.else_clause = visit(exp.else_clause, required);
        }
        if (test instanceof QuoteExp) {
            return exp.select(this.comp.getLanguage().isTrue(((QuoteExp) test).getValue()));
        }
        if (!test.getType().isVoid()) {
            return exp;
        }
        boolean truth = this.comp.getLanguage().isTrue(Values.empty);
        this.comp.error('w', "void-valued condition is always " + truth);
        return new BeginExp(test, exp.select(truth));
    }

    /* access modifiers changed from: protected */
    public Expression visitBeginExp(BeginExp exp, Type required) {
        Type type;
        int last = exp.length - 1;
        for (int i = 0; i <= last; i++) {
            Expression[] expressionArr = exp.exps;
            Expression expression = exp.exps[i];
            if (i < last) {
                type = null;
            } else {
                type = required;
            }
            expressionArr[i] = visit(expression, type);
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitScopeExp(ScopeExp exp, Type required) {
        Type type;
        exp.visitChildren(this, null);
        visitDeclarationTypes(exp);
        for (Declaration decl = exp.firstDecl(); decl != null; decl = decl.nextDecl()) {
            if (decl.type == null) {
                Expression val = decl.getValue();
                decl.type = Type.objectType;
                if (val == null || val == QuoteExp.undefined_exp) {
                    type = Type.objectType;
                } else {
                    type = val.getType();
                }
                decl.setType(type);
            }
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitLetExp(LetExp exp, Type required) {
        ReferenceExp ref;
        Declaration d;
        Declaration decl = exp.firstDecl();
        int n = exp.inits.length;
        int i = 0;
        while (i < n) {
            Expression init0 = exp.inits[i];
            boolean typeSpecified = decl.getFlag(8192);
            Expression init = visit(init0, (!typeSpecified || init0 == QuoteExp.undefined_exp) ? null : decl.getType());
            exp.inits[i] = init;
            if (decl.value == init0) {
                Expression dvalue = init;
                decl.value = init;
                if (!typeSpecified) {
                    decl.setType(dvalue.getType());
                }
            }
            i++;
            decl = decl.nextDecl();
        }
        if (this.exitValue == null) {
            exp.body = visit(exp.body, required);
        }
        if (!(exp.body instanceof ReferenceExp) || (d = ref.getBinding()) == null || d.context != exp || (ref = (ReferenceExp) exp.body).getDontDereference() || n != 1) {
            return exp;
        }
        Expression init2 = exp.inits[0];
        Expression texp = d.getTypeExp();
        if (texp == QuoteExp.classObjectExp) {
            return init2;
        }
        return visitApplyOnly(Compilation.makeCoercion(init2, texp), (Type) null);
    }

    /* access modifiers changed from: protected */
    public Expression visitLambdaExp(LambdaExp exp, Type required) {
        Declaration firstDecl = exp.firstDecl();
        if (firstDecl != null && firstDecl.isThisParameter() && !exp.isClassMethod() && firstDecl.type == null) {
            firstDecl.setType(this.comp.mainClass);
        }
        return visitScopeExp((ScopeExp) exp, required);
    }

    /* access modifiers changed from: protected */
    public Expression visitTryExp(TryExp exp, Type required) {
        if (exp.getCatchClauses() == null && exp.getFinallyClause() == null) {
            return visit(exp.try_clause, required);
        }
        return (Expression) super.visitTryExp(exp, required);
    }

    /* access modifiers changed from: protected */
    public Expression visitSetExpValue(Expression new_value, Type required, Declaration decl) {
        return visit(new_value, (decl == null || decl.isAlias()) ? null : decl.type);
    }

    /* access modifiers changed from: protected */
    public Expression visitSetExp(SetExp exp, Type required) {
        Declaration decl = exp.getBinding();
        super.visitSetExp(exp, required);
        if (!exp.isDefining() && decl != null && (decl.flags & 1048704) == 1048704) {
            this.comp.error('e', "can't assign to method " + decl.getName(), exp);
        }
        if (decl != null && decl.getFlag(8192) && CompileReflect.checkKnownClass(decl.getType(), this.comp) < 0) {
            decl.setType(Type.errorType);
        }
        return exp;
    }

    private static synchronized Class[] getInlinerMethodArgTypes() throws Exception {
        Class[] t;
        synchronized (InlineCalls.class) {
            t = inlinerMethodArgTypes;
            if (t == null) {
                t = new Class[]{Class.forName("gnu.expr.ApplyExp"), Class.forName("gnu.expr.InlineCalls"), Class.forName("gnu.bytecode.Type"), Class.forName("gnu.mapping.Procedure")};
                inlinerMethodArgTypes = t;
            }
        }
        return t;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0062, code lost:
        if (r5 == null) goto L_0x00b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r9 = new java.lang.Object[]{r15, r14, r16, r17};
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0075, code lost:
        if ((r5 instanceof gnu.mapping.Procedure) == false) goto L_0x00b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b8, code lost:
        if ((r5 instanceof java.lang.reflect.Method) == false) goto L_0x00b4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        return (gnu.expr.Expression) ((gnu.mapping.Procedure) r5).applyN(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        return (gnu.expr.Expression) ((java.lang.reflect.Method) r5).invoke((java.lang.Object) null, r9);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression maybeInline(gnu.expr.ApplyExp r15, gnu.bytecode.Type r16, gnu.mapping.Procedure r17) {
        /*
            r14 = this;
            monitor-enter(r17)     // Catch:{ Throwable -> 0x0083 }
            gnu.mapping.Symbol r10 = gnu.mapping.Procedure.validateApplyKey     // Catch:{ all -> 0x0080 }
            r11 = 0
            r0 = r17
            java.lang.Object r5 = r0.getProperty(r10, r11)     // Catch:{ all -> 0x0080 }
            boolean r10 = r5 instanceof java.lang.String     // Catch:{ all -> 0x0080 }
            if (r10 == 0) goto L_0x0061
            r0 = r5
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ all -> 0x0080 }
            r6 = r0
            r10 = 58
            int r3 = r6.indexOf(r10)     // Catch:{ all -> 0x0080 }
            r7 = 0
            if (r3 <= 0) goto L_0x003b
            r10 = 0
            java.lang.String r2 = r6.substring(r10, r3)     // Catch:{ all -> 0x0080 }
            int r10 = r3 + 1
            java.lang.String r8 = r6.substring(r10)     // Catch:{ all -> 0x0080 }
            r10 = 1
            java.lang.Class r11 = r17.getClass()     // Catch:{ all -> 0x0080 }
            java.lang.ClassLoader r11 = r11.getClassLoader()     // Catch:{ all -> 0x0080 }
            java.lang.Class r1 = java.lang.Class.forName(r2, r10, r11)     // Catch:{ all -> 0x0080 }
            java.lang.Class[] r10 = getInlinerMethodArgTypes()     // Catch:{ all -> 0x0080 }
            java.lang.reflect.Method r7 = r1.getDeclaredMethod(r8, r10)     // Catch:{ all -> 0x0080 }
        L_0x003b:
            if (r7 != 0) goto L_0x0060
            r10 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0080 }
            r11.<init>()     // Catch:{ all -> 0x0080 }
            java.lang.String r12 = "inliner property string for "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x0080 }
            r0 = r17
            java.lang.StringBuilder r11 = r11.append(r0)     // Catch:{ all -> 0x0080 }
            java.lang.String r12 = " is not of the form CLASS:METHOD"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x0080 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0080 }
            r14.error(r10, r11)     // Catch:{ all -> 0x0080 }
            r10 = 0
            monitor-exit(r17)     // Catch:{ all -> 0x0080 }
        L_0x005f:
            return r10
        L_0x0060:
            r5 = r7
        L_0x0061:
            monitor-exit(r17)     // Catch:{ all -> 0x0080 }
            if (r5 == 0) goto L_0x00b4
            r10 = 4
            java.lang.Object[] r9 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x0083 }
            r10 = 0
            r9[r10] = r15     // Catch:{ Throwable -> 0x0083 }
            r10 = 1
            r9[r10] = r14     // Catch:{ Throwable -> 0x0083 }
            r10 = 2
            r9[r10] = r16     // Catch:{ Throwable -> 0x0083 }
            r10 = 3
            r9[r10] = r17     // Catch:{ Throwable -> 0x0083 }
            boolean r10 = r5 instanceof gnu.mapping.Procedure     // Catch:{ Throwable -> 0x0083 }
            if (r10 == 0) goto L_0x00b6
            gnu.mapping.Procedure r5 = (gnu.mapping.Procedure) r5     // Catch:{ Throwable -> 0x0083 }
            java.lang.Object r10 = r5.applyN(r9)     // Catch:{ Throwable -> 0x0083 }
            gnu.expr.Expression r10 = (gnu.expr.Expression) r10     // Catch:{ Throwable -> 0x0083 }
            goto L_0x005f
        L_0x0080:
            r10 = move-exception
            monitor-exit(r17)     // Catch:{ all -> 0x0080 }
            throw r10     // Catch:{ Throwable -> 0x0083 }
        L_0x0083:
            r4 = move-exception
            boolean r10 = r4 instanceof java.lang.reflect.InvocationTargetException
            if (r10 == 0) goto L_0x008e
            java.lang.reflect.InvocationTargetException r4 = (java.lang.reflect.InvocationTargetException) r4
            java.lang.Throwable r4 = r4.getTargetException()
        L_0x008e:
            gnu.text.SourceMessages r10 = r14.messages
            r11 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "caught exception in inliner for "
            java.lang.StringBuilder r12 = r12.append(r13)
            r0 = r17
            java.lang.StringBuilder r12 = r12.append(r0)
            java.lang.String r13 = " - "
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r4)
            java.lang.String r12 = r12.toString()
            r10.error((char) r11, (java.lang.String) r12, (java.lang.Throwable) r4)
        L_0x00b4:
            r10 = 0
            goto L_0x005f
        L_0x00b6:
            boolean r10 = r5 instanceof java.lang.reflect.Method     // Catch:{ Throwable -> 0x0083 }
            if (r10 == 0) goto L_0x00b4
            java.lang.reflect.Method r5 = (java.lang.reflect.Method) r5     // Catch:{ Throwable -> 0x0083 }
            r10 = 0
            java.lang.Object r10 = r5.invoke(r10, r9)     // Catch:{ Throwable -> 0x0083 }
            gnu.expr.Expression r10 = (gnu.expr.Expression) r10     // Catch:{ Throwable -> 0x0083 }
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.InlineCalls.maybeInline(gnu.expr.ApplyExp, gnu.bytecode.Type, gnu.mapping.Procedure):gnu.expr.Expression");
    }

    public static Expression inlineCall(LambdaExp lexp, Expression[] args, boolean makeCopy) {
        IdentityHashTable mapper;
        Expression[] cargs;
        if (lexp.keywords != null || (lexp.nameDecl != null && !makeCopy)) {
            return null;
        }
        boolean varArgs = lexp.max_args < 0;
        if ((lexp.min_args != lexp.max_args || lexp.min_args != args.length) && (!varArgs || lexp.min_args != 0)) {
            return null;
        }
        Declaration prev = null;
        int i = 0;
        if (makeCopy) {
            mapper = new IdentityHashTable();
            cargs = Expression.deepCopy(args, mapper);
            if (cargs == null && args != null) {
                return null;
            }
        } else {
            mapper = null;
            cargs = args;
        }
        if (varArgs) {
            Expression[] xargs = new Expression[(args.length + 1)];
            xargs[0] = QuoteExp.getInstance(lexp.firstDecl().type);
            System.arraycopy(args, 0, xargs, 1, args.length);
            cargs = new Expression[]{new ApplyExp((Procedure) Invoke.make, xargs)};
        }
        LetExp let = new LetExp(cargs);
        Declaration param = lexp.firstDecl();
        while (param != null) {
            Declaration next = param.nextDecl();
            if (makeCopy) {
                Declaration ldecl = let.addDeclaration(param.symbol, param.type);
                if (param.typeExp != null) {
                    ldecl.typeExp = Expression.deepCopy(param.typeExp);
                    if (ldecl.typeExp == null) {
                        return null;
                    }
                }
                mapper.put(param, ldecl);
            } else {
                lexp.remove(prev, param);
                let.add(prev, param);
            }
            if (!varArgs && !param.getCanWrite()) {
                param.setValue(cargs[i]);
            }
            prev = param;
            param = next;
            i++;
        }
        Expression body = lexp.body;
        if (makeCopy && (body = Expression.deepCopy(body, mapper)) == null && lexp.body != null) {
            return null;
        }
        let.body = body;
        return let;
    }
}
