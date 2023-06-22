package gnu.expr;

import gnu.bytecode.Type;

public class FindTailCalls extends ExpExpVisitor<Expression> {
    public static void findTailCalls(Expression exp, Compilation comp) {
        FindTailCalls visitor = new FindTailCalls();
        visitor.setContext(comp);
        visitor.visit(exp, exp);
    }

    /* access modifiers changed from: protected */
    public Expression visitExpression(Expression exp, Expression returnContinuation) {
        return (Expression) super.visitExpression(exp, exp);
    }

    public Expression[] visitExps(Expression[] exps) {
        int n = exps.length;
        for (int i = 0; i < n; i++) {
            Expression expi = exps[i];
            exps[i] = (Expression) visit(expi, expi);
        }
        return exps;
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [gnu.expr.Expression] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression visitApplyExp(gnu.expr.ApplyExp r11, gnu.expr.Expression r12) {
        /*
            r10 = this;
            r8 = 0
            r7 = 1
            gnu.expr.LambdaExp r9 = r10.currentLambda
            gnu.expr.Expression r9 = r9.body
            if (r12 != r9) goto L_0x005c
            r3 = r7
        L_0x0009:
            if (r3 == 0) goto L_0x000e
            r11.setTailCall(r7)
        L_0x000e:
            gnu.expr.LambdaExp r9 = r10.currentLambda
            r11.context = r9
            r5 = 0
            r4 = 0
            gnu.expr.Expression r9 = r11.func
            boolean r9 = r9 instanceof gnu.expr.ReferenceExp
            if (r9 == 0) goto L_0x005e
            gnu.expr.Expression r2 = r11.func
            gnu.expr.ReferenceExp r2 = (gnu.expr.ReferenceExp) r2
            gnu.expr.Declaration r7 = r2.binding
            gnu.expr.Declaration r0 = gnu.expr.Declaration.followAliases(r7)
            if (r0 == 0) goto L_0x004d
            r8 = 2048(0x800, double:1.0118E-320)
            boolean r7 = r0.getFlag(r8)
            if (r7 != 0) goto L_0x0034
            gnu.expr.ApplyExp r7 = r0.firstCall
            r11.nextCall = r7
            r0.firstCall = r11
        L_0x0034:
            gnu.expr.Compilation r1 = r10.getCompilation()
            r0.setCanCall()
            boolean r7 = r1.mustCompile
            if (r7 != 0) goto L_0x0042
            r0.setCanRead()
        L_0x0042:
            gnu.expr.Expression r6 = r0.getValue()
            boolean r7 = r6 instanceof gnu.expr.LambdaExp
            if (r7 == 0) goto L_0x004d
            r5 = r6
            gnu.expr.LambdaExp r5 = (gnu.expr.LambdaExp) r5
        L_0x004d:
            if (r5 == 0) goto L_0x0053
            gnu.expr.Expression r7 = r5.returnContinuation
            if (r7 != r12) goto L_0x0094
        L_0x0053:
            gnu.expr.Expression[] r7 = r11.args
            gnu.expr.Expression[] r7 = r10.visitExps(r7)
            r11.args = r7
            return r11
        L_0x005c:
            r3 = r8
            goto L_0x0009
        L_0x005e:
            gnu.expr.Expression r9 = r11.func
            boolean r9 = r9 instanceof gnu.expr.LambdaExp
            if (r9 == 0) goto L_0x0075
            gnu.expr.Expression r9 = r11.func
            boolean r9 = r9 instanceof gnu.expr.ClassExp
            if (r9 != 0) goto L_0x0075
            gnu.expr.Expression r5 = r11.func
            gnu.expr.LambdaExp r5 = (gnu.expr.LambdaExp) r5
            r10.visitLambdaExp((gnu.expr.LambdaExp) r5, (boolean) r8)
            r5.setCanCall(r7)
            goto L_0x004d
        L_0x0075:
            gnu.expr.Expression r7 = r11.func
            boolean r7 = r7 instanceof gnu.expr.QuoteExp
            if (r7 == 0) goto L_0x0089
            gnu.expr.Expression r7 = r11.func
            gnu.expr.QuoteExp r7 = (gnu.expr.QuoteExp) r7
            java.lang.Object r7 = r7.getValue()
            gnu.kawa.functions.AppendValues r8 = gnu.kawa.functions.AppendValues.appendValues
            if (r7 != r8) goto L_0x0089
            r4 = 1
            goto L_0x004d
        L_0x0089:
            gnu.expr.Expression r7 = r11.func
            gnu.expr.Expression r8 = r11.func
            gnu.expr.Expression r7 = r10.visitExpression((gnu.expr.Expression) r7, (gnu.expr.Expression) r8)
            r11.func = r7
            goto L_0x004d
        L_0x0094:
            gnu.expr.LambdaExp r7 = r10.currentLambda
            if (r5 != r7) goto L_0x009a
            if (r3 != 0) goto L_0x0053
        L_0x009a:
            if (r3 == 0) goto L_0x00af
            java.util.Set<gnu.expr.LambdaExp> r7 = r5.tailCallers
            if (r7 != 0) goto L_0x00a7
            java.util.HashSet r7 = new java.util.HashSet
            r7.<init>()
            r5.tailCallers = r7
        L_0x00a7:
            java.util.Set<gnu.expr.LambdaExp> r7 = r5.tailCallers
            gnu.expr.LambdaExp r8 = r10.currentLambda
            r7.add(r8)
            goto L_0x0053
        L_0x00af:
            gnu.expr.Expression r7 = r5.returnContinuation
            if (r7 != 0) goto L_0x00ba
            r5.returnContinuation = r12
            gnu.expr.LambdaExp r7 = r10.currentLambda
            r5.inlineHome = r7
            goto L_0x0053
        L_0x00ba:
            gnu.expr.ApplyExp r7 = gnu.expr.LambdaExp.unknownContinuation
            r5.returnContinuation = r7
            r7 = 0
            r5.inlineHome = r7
            goto L_0x0053
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.FindTailCalls.visitApplyExp(gnu.expr.ApplyExp, gnu.expr.Expression):gnu.expr.Expression");
    }

    /* access modifiers changed from: protected */
    public Expression visitBlockExp(BlockExp exp, Expression returnContinuation) {
        exp.body = (Expression) exp.body.visit(this, returnContinuation);
        if (exp.exitBody != null) {
            exp.exitBody = (Expression) exp.exitBody.visit(this, exp.exitBody);
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitBeginExp(BeginExp exp, Expression returnContinuation) {
        int n = exp.length - 1;
        int i = 0;
        while (i <= n) {
            exp.exps[i] = (Expression) exp.exps[i].visit(this, i == n ? returnContinuation : exp.exps[i]);
            i++;
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitFluidLetExp(FluidLetExp exp, Expression returnContinuation) {
        for (Declaration decl = exp.firstDecl(); decl != null; decl = decl.nextDecl()) {
            decl.setCanRead(true);
            if (decl.base != null) {
                decl.base.setCanRead(true);
            }
        }
        visitLetDecls(exp);
        exp.body = (Expression) exp.body.visit(this, exp.body);
        postVisitDecls(exp);
        return exp;
    }

    /* access modifiers changed from: package-private */
    public void visitLetDecls(LetExp exp) {
        Declaration decl = exp.firstDecl();
        int n = exp.inits.length;
        int i = 0;
        while (i < n) {
            Expression init = visitSetExp(decl, exp.inits[i]);
            if (init == QuoteExp.undefined_exp) {
                Expression value = decl.getValue();
                if ((value instanceof LambdaExp) || (value != init && (value instanceof QuoteExp))) {
                    init = value;
                }
            }
            exp.inits[i] = init;
            i++;
            decl = decl.nextDecl();
        }
    }

    /* access modifiers changed from: protected */
    public Expression visitLetExp(LetExp exp, Expression returnContinuation) {
        visitLetDecls(exp);
        exp.body = (Expression) exp.body.visit(this, returnContinuation);
        postVisitDecls(exp);
        return exp;
    }

    public void postVisitDecls(ScopeExp exp) {
        Declaration context;
        for (Declaration decl = exp.firstDecl(); decl != null; decl = decl.nextDecl()) {
            Expression value = decl.getValue();
            if (value instanceof LambdaExp) {
                LambdaExp lexp = (LambdaExp) value;
                if (decl.getCanRead()) {
                    lexp.setCanRead(true);
                }
                if (decl.getCanCall()) {
                    lexp.setCanCall(true);
                }
            }
            if (decl.getFlag(1024) && (value instanceof ReferenceExp) && (context = ((ReferenceExp) value).contextDecl()) != null && context.isPrivate()) {
                context.setFlag(524288);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Expression visitIfExp(IfExp exp, Expression returnContinuation) {
        exp.test = (Expression) exp.test.visit(this, exp.test);
        exp.then_clause = (Expression) exp.then_clause.visit(this, returnContinuation);
        Expression else_clause = exp.else_clause;
        if (else_clause != null) {
            exp.else_clause = (Expression) else_clause.visit(this, returnContinuation);
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitLambdaExp(LambdaExp exp, Expression returnContinuation) {
        visitLambdaExp(exp, true);
        return exp;
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public final void visitLambdaExp(LambdaExp exp, boolean canRead) {
        LambdaExp parent = this.currentLambda;
        this.currentLambda = exp;
        if (canRead) {
            exp.setCanRead(true);
        }
        try {
            if (exp.defaultArgs != null) {
                exp.defaultArgs = visitExps(exp.defaultArgs);
            }
            if (this.exitValue == null && exp.body != null) {
                exp.body = (Expression) exp.body.visit(this, exp.getInlineOnly() ? exp : exp.body);
            }
            this.currentLambda = parent;
            postVisitDecls(exp);
        } catch (Throwable th) {
            this.currentLambda = parent;
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public Expression visitClassExp(ClassExp exp, Expression returnContinuation) {
        LambdaExp parent = this.currentLambda;
        this.currentLambda = exp;
        try {
            for (LambdaExp child = exp.firstChild; child != null && this.exitValue == null; child = child.nextSibling) {
                visitLambdaExp(child, false);
            }
            return exp;
        } finally {
            this.currentLambda = parent;
        }
    }

    /* access modifiers changed from: protected */
    public Expression visitReferenceExp(ReferenceExp exp, Expression returnContinuation) {
        Declaration decl = Declaration.followAliases(exp.binding);
        if (decl != null) {
            Type type = decl.type;
            if (type != null && type.isVoid()) {
                return QuoteExp.voidExp;
            }
            decl.setCanRead(true);
        }
        Declaration ctx = exp.contextDecl();
        if (ctx == null) {
            return exp;
        }
        ctx.setCanRead(true);
        return exp;
    }

    /* access modifiers changed from: package-private */
    public final Expression visitSetExp(Declaration decl, Expression value) {
        if (decl == null || decl.getValue() != value || !(value instanceof LambdaExp) || (value instanceof ClassExp) || decl.isPublic()) {
            return (Expression) value.visit(this, value);
        }
        LambdaExp lexp = (LambdaExp) value;
        visitLambdaExp(lexp, false);
        return lexp;
    }

    /* access modifiers changed from: protected */
    public Expression visitSetExp(SetExp exp, Expression returnContinuation) {
        Declaration decl = exp.binding;
        if (decl != null && decl.isAlias()) {
            if (exp.isDefining()) {
                exp.new_value = (Expression) exp.new_value.visit(this, exp.new_value);
                return exp;
            }
            decl = Declaration.followAliases(decl);
        }
        Declaration ctx = exp.contextDecl();
        if (ctx != null) {
            ctx.setCanRead(true);
        }
        Expression value = visitSetExp(decl, exp.new_value);
        if (decl != null && (decl.context instanceof LetExp) && value == decl.getValue() && ((value instanceof LambdaExp) || (value instanceof QuoteExp))) {
            return QuoteExp.voidExp;
        }
        exp.new_value = value;
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitTryExp(TryExp exp, Expression returnContinuation) {
        exp.try_clause = (Expression) exp.try_clause.visit(this, exp.finally_clause == null ? returnContinuation : exp.try_clause);
        CatchClause catch_clause = exp.catch_clauses;
        while (this.exitValue == null && catch_clause != null) {
            catch_clause.body = (Expression) catch_clause.body.visit(this, exp.finally_clause == null ? returnContinuation : catch_clause.body);
            catch_clause = catch_clause.getNext();
        }
        Expression finally_clause = exp.finally_clause;
        if (finally_clause != null) {
            exp.finally_clause = (Expression) finally_clause.visit(this, finally_clause);
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitSynchronizedExp(SynchronizedExp exp, Expression returnContinuation) {
        exp.object = (Expression) exp.object.visit(this, exp.object);
        exp.body = (Expression) exp.body.visit(this, exp.body);
        return exp;
    }
}
