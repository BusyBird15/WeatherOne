package gnu.expr;

import gnu.mapping.EnvironmentKey;
import gnu.mapping.KeyPair;
import gnu.mapping.Symbol;
import gnu.text.SourceLocator;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;

public class FindCapturedVars extends ExpExpVisitor<Void> {
    int backJumpPossible = 0;
    ModuleExp currentModule = null;
    Hashtable unknownDecls = null;

    public static void findCapturedVars(Expression exp, Compilation comp) {
        FindCapturedVars visitor = new FindCapturedVars();
        visitor.setContext(comp);
        exp.visit(visitor, null);
    }

    /* access modifiers changed from: protected */
    public Expression visitApplyExp(ApplyExp exp, Void ignored) {
        int oldBackJumpPossible = this.backJumpPossible;
        boolean skipFunc = false;
        boolean skipArgs = false;
        if ((exp.func instanceof ReferenceExp) && Compilation.defaultCallConvention <= 1) {
            Declaration decl = Declaration.followAliases(((ReferenceExp) exp.func).binding);
            if (decl != null && (decl.context instanceof ModuleExp) && !decl.isPublic() && !decl.getFlag(4096)) {
                Expression value = decl.getValue();
                if ((value instanceof LambdaExp) && !((LambdaExp) value).getNeedsClosureEnv()) {
                    skipFunc = true;
                }
            }
        } else if ((exp.func instanceof QuoteExp) && exp.getArgCount() > 0) {
            Object val = ((QuoteExp) exp.func).getValue();
            Expression arg0 = exp.getArg(0);
            if ((val instanceof PrimProcedure) && (arg0 instanceof ReferenceExp)) {
                PrimProcedure primProcedure = (PrimProcedure) val;
                Declaration decl2 = Declaration.followAliases(((ReferenceExp) arg0).binding);
                if (decl2 != null && (decl2.context instanceof ModuleExp) && !decl2.getFlag(4096)) {
                    Expression value2 = decl2.getValue();
                    if (value2 instanceof ClassExp) {
                        Expression[] args = exp.getArgs();
                        if (!((LambdaExp) value2).getNeedsClosureEnv()) {
                            exp.nextCall = decl2.firstCall;
                            decl2.firstCall = exp;
                            for (int i = 1; i < args.length; i++) {
                                args[i].visit(this, ignored);
                            }
                            skipArgs = true;
                            skipFunc = true;
                        }
                    }
                }
            }
        }
        if (!skipFunc) {
            exp.func = (Expression) exp.func.visit(this, ignored);
        }
        if (this.exitValue == null && !skipArgs) {
            exp.args = visitExps(exp.args, ignored);
        }
        if (this.backJumpPossible > oldBackJumpPossible) {
            exp.setFlag(8);
        }
        return exp;
    }

    public void visitDefaultArgs(LambdaExp exp, Void ignored) {
        if (exp.defaultArgs != null) {
            super.visitDefaultArgs(exp, ignored);
            for (Declaration param = exp.firstDecl(); param != null; param = param.nextDecl()) {
                if (!param.isSimple()) {
                    exp.setFlag(true, 512);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public Expression visitClassExp(ClassExp exp, Void ignored) {
        Expression ret = (Expression) super.visitClassExp(exp, ignored);
        if (!exp.explicitInit && !exp.instanceType.isInterface()) {
            Compilation.getConstructor(exp.instanceType, exp);
        } else if (exp.getNeedsClosureEnv()) {
            for (LambdaExp child = exp.firstChild; child != null; child = child.nextSibling) {
                if ("*init*".equals(child.getName())) {
                    child.setNeedsStaticLink(true);
                }
            }
        }
        if (exp.isSimple() && exp.getNeedsClosureEnv() && exp.nameDecl != null && exp.nameDecl.getType() == Compilation.typeClass) {
            exp.nameDecl.setType(Compilation.typeClassType);
        }
        return ret;
    }

    /* access modifiers changed from: protected */
    public Expression visitModuleExp(ModuleExp exp, Void ignored) {
        ModuleExp saveModule = this.currentModule;
        Hashtable saveDecls = this.unknownDecls;
        this.currentModule = exp;
        this.unknownDecls = null;
        try {
            return visitLambdaExp((LambdaExp) exp, ignored);
        } finally {
            this.currentModule = saveModule;
            this.unknownDecls = saveDecls;
        }
    }

    /* access modifiers changed from: package-private */
    public void maybeWarnNoDeclarationSeen(Object name, Compilation comp, SourceLocator location) {
        if (comp.warnUndefinedVariable()) {
            comp.error('w', "no declaration seen for " + name, location);
        }
    }

    /* access modifiers changed from: protected */
    public Expression visitFluidLetExp(FluidLetExp exp, Void ignored) {
        for (Declaration decl = exp.firstDecl(); decl != null; decl = decl.nextDecl()) {
            if (decl.base == null) {
                Object name = decl.getSymbol();
                Declaration bind = allocUnboundDecl(name, false);
                maybeWarnNoDeclarationSeen(name, this.comp, exp);
                capture(bind);
                decl.base = bind;
            }
        }
        return (Expression) super.visitLetExp(exp, ignored);
    }

    /* access modifiers changed from: protected */
    public Expression visitLetExp(LetExp exp, Void ignored) {
        if (exp.body instanceof BeginExp) {
            Expression[] inits = exp.inits;
            int len = inits.length;
            Expression[] exps = ((BeginExp) exp.body).exps;
            int init_index = 0;
            Declaration decl = exp.firstDecl();
            for (int begin_index = 0; begin_index < exps.length && init_index < len; begin_index++) {
                Expression st = exps[begin_index];
                if (st instanceof SetExp) {
                    SetExp set = (SetExp) st;
                    if (set.binding == decl && inits[init_index] == QuoteExp.nullExp && set.isDefining()) {
                        Expression new_value = set.new_value;
                        if (((new_value instanceof QuoteExp) || (new_value instanceof LambdaExp)) && decl.getValue() == new_value) {
                            inits[init_index] = new_value;
                            exps[begin_index] = QuoteExp.voidExp;
                        }
                        init_index++;
                        decl = decl.nextDecl();
                    }
                }
            }
        }
        return (Expression) super.visitLetExp(exp, ignored);
    }

    static Expression checkInlineable(LambdaExp current, Set<LambdaExp> seen) {
        if (current.returnContinuation == LambdaExp.unknownContinuation) {
            return current.returnContinuation;
        }
        if (seen.contains(current)) {
            return current.returnContinuation;
        }
        if (current.getCanRead() || current.isClassMethod() || current.min_args != current.max_args) {
            current.returnContinuation = LambdaExp.unknownContinuation;
            return LambdaExp.unknownContinuation;
        }
        seen.add(current);
        Expression r = current.returnContinuation;
        if (current.tailCallers != null) {
            for (LambdaExp p : current.tailCallers) {
                Expression t = checkInlineable(p, seen);
                if (t == LambdaExp.unknownContinuation) {
                    if (r == null || r == p.body) {
                        r = p.body;
                        current.inlineHome = p;
                    } else {
                        current.returnContinuation = LambdaExp.unknownContinuation;
                        return t;
                    }
                } else if (r == null) {
                    r = t;
                    if (current.inlineHome == null) {
                        if (!current.nestedIn(p)) {
                            p = p.inlineHome;
                        }
                        current.inlineHome = p;
                    }
                } else if ((t != null && r != t) || current.getFlag(32)) {
                    current.returnContinuation = LambdaExp.unknownContinuation;
                    return LambdaExp.unknownContinuation;
                }
            }
        }
        return r;
    }

    /* access modifiers changed from: protected */
    public Expression visitLambdaExp(LambdaExp exp, Void ignored) {
        if (checkInlineable(exp, new LinkedHashSet<>()) != LambdaExp.unknownContinuation && (!(exp.outer instanceof ModuleExp) || exp.nameDecl == null)) {
            exp.setInlineOnly(true);
            this.backJumpPossible++;
        }
        return (Expression) super.visitLambdaExp(exp, ignored);
    }

    public void capture(Declaration decl) {
        LambdaExp declValue;
        if (!decl.getCanRead() && !decl.getCanCall()) {
            return;
        }
        if (decl.field != null && decl.field.getStaticFlag()) {
            return;
        }
        if (!this.comp.immediate || !decl.hasConstantValue()) {
            LambdaExp curLambda = getCurrentLambda();
            ScopeExp sc = decl.getContext();
            if (sc == null) {
                throw new Error("null context for " + decl + " curL:" + curLambda);
            }
            LambdaExp declLambda = sc.currentLambda();
            LambdaExp oldParent = null;
            LambdaExp chain = null;
            while (curLambda != declLambda && curLambda.getInlineOnly()) {
                LambdaExp curParent = curLambda.outerLambda();
                if (curParent != oldParent) {
                    chain = curParent.firstChild;
                    oldParent = curParent;
                }
                if (chain == null || curLambda.inlineHome == null) {
                    curLambda.setCanCall(false);
                    return;
                } else {
                    curLambda = curLambda.getCaller();
                    chain = chain.nextSibling;
                }
            }
            if (this.comp.usingCPStyle()) {
                if (curLambda instanceof ModuleExp) {
                    return;
                }
            } else if (curLambda == declLambda) {
                return;
            }
            Expression value = decl.getValue();
            if (value == null || !(value instanceof LambdaExp)) {
                declValue = null;
            } else {
                declValue = (LambdaExp) value;
                if (declValue.getInlineOnly()) {
                    return;
                }
                if (declValue.isHandlingTailCalls()) {
                    declValue = null;
                } else if (declValue == curLambda && !decl.getCanRead()) {
                    return;
                }
            }
            if (decl.getFlag(65536)) {
                LambdaExp parent = curLambda;
                while (true) {
                    if (parent != declLambda) {
                        if (parent.nameDecl != null && parent.nameDecl.getFlag(2048)) {
                            decl.setFlag(2048);
                            break;
                        }
                        parent = parent.outerLambda();
                    } else {
                        break;
                    }
                }
            }
            if (decl.base != null) {
                decl.base.setCanRead(true);
                capture(decl.base);
            } else if (decl.getCanRead() || decl.getCanCall() || declValue == null) {
                if (!decl.isStatic()) {
                    LambdaExp heapLambda = curLambda;
                    if (!decl.isFluid()) {
                        heapLambda.setImportsLexVars();
                    }
                    LambdaExp outer = heapLambda.outerLambda();
                    while (outer != declLambda && outer != null) {
                        LambdaExp heapLambda2 = outer;
                        if (!decl.getCanRead() && declValue == outer) {
                            break;
                        }
                        Declaration heapDecl = heapLambda2.nameDecl;
                        if (heapDecl != null && heapDecl.getFlag(2048)) {
                            this.comp.error('e', "static " + heapLambda2.getName() + " references non-static " + decl.getName());
                        }
                        if ((heapLambda2 instanceof ClassExp) && heapLambda2.getName() != null && ((ClassExp) heapLambda2).isSimple()) {
                            this.comp.error('w', heapLambda2.nameDecl, "simple class ", " requiring lexical link (because of reference to " + decl.getName() + ") - use define-class instead");
                        }
                        heapLambda2.setNeedsStaticLink();
                        outer = heapLambda2.outerLambda();
                    }
                }
                if (declLambda == null) {
                    System.err.println("null declLambda for " + decl + " curL:" + curLambda);
                    for (ScopeExp c = decl.context; c != null; c = c.outer) {
                        System.err.println("- context:" + c);
                    }
                }
                declLambda.capture(decl);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Declaration allocUnboundDecl(Object name, boolean function) {
        Declaration decl;
        Object key = name;
        if (function && (name instanceof Symbol)) {
            if (!getCompilation().getLanguage().hasSeparateFunctionNamespace()) {
                function = false;
            } else {
                key = new KeyPair((Symbol) name, EnvironmentKey.FUNCTION);
            }
        }
        if (this.unknownDecls == null) {
            this.unknownDecls = new Hashtable(100);
            decl = null;
        } else {
            decl = (Declaration) this.unknownDecls.get(key);
        }
        if (decl == null) {
            decl = this.currentModule.addDeclaration(name);
            decl.setSimple(false);
            decl.setPrivate(true);
            if (function) {
                decl.setProcedureDecl(true);
            }
            if (this.currentModule.isStatic()) {
                decl.setFlag(2048);
            }
            decl.setCanRead(true);
            decl.setCanWrite(true);
            decl.setFlag(327680);
            decl.setIndirectBinding(true);
            this.unknownDecls.put(key, decl);
        }
        return decl;
    }

    /* access modifiers changed from: protected */
    public Expression visitReferenceExp(ReferenceExp exp, Void ignored) {
        Declaration decl = exp.getBinding();
        if (decl == null) {
            decl = allocUnboundDecl(exp.getSymbol(), exp.isProcedureName());
            exp.setBinding(decl);
        }
        if (decl.getFlag(65536) && this.comp.resolve(exp.getSymbol(), exp.isProcedureName()) == null) {
            maybeWarnNoDeclarationSeen(exp.getSymbol(), this.comp, exp);
        }
        capture(exp.contextDecl(), decl);
        return exp;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r1 = (gnu.expr.ReferenceExp) r5.value;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void capture(gnu.expr.Declaration r4, gnu.expr.Declaration r5) {
        /*
            r3 = this;
            boolean r2 = r5.isAlias()
            if (r2 == 0) goto L_0x0024
            gnu.expr.Expression r2 = r5.value
            boolean r2 = r2 instanceof gnu.expr.ReferenceExp
            if (r2 == 0) goto L_0x0024
            gnu.expr.Expression r1 = r5.value
            gnu.expr.ReferenceExp r1 = (gnu.expr.ReferenceExp) r1
            gnu.expr.Declaration r0 = r1.binding
            if (r0 == 0) goto L_0x0024
            if (r4 == 0) goto L_0x001c
            boolean r2 = r0.needsContext()
            if (r2 != 0) goto L_0x0024
        L_0x001c:
            gnu.expr.Declaration r2 = r1.contextDecl()
            r3.capture(r2, r0)
        L_0x0023:
            return
        L_0x0024:
            boolean r2 = r5.isFluid()
            if (r2 == 0) goto L_0x0033
            gnu.expr.ScopeExp r2 = r5.context
            boolean r2 = r2 instanceof gnu.expr.FluidLetExp
            if (r2 == 0) goto L_0x0033
            gnu.expr.Declaration r5 = r5.base
            goto L_0x0024
        L_0x0033:
            if (r4 == 0) goto L_0x003f
            boolean r2 = r5.needsContext()
            if (r2 == 0) goto L_0x003f
            r3.capture(r4)
            goto L_0x0023
        L_0x003f:
            r3.capture(r5)
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.FindCapturedVars.capture(gnu.expr.Declaration, gnu.expr.Declaration):void");
    }

    /* access modifiers changed from: protected */
    public Expression visitThisExp(ThisExp exp, Void ignored) {
        if (!exp.isForContext()) {
            return visitReferenceExp((ReferenceExp) exp, ignored);
        }
        getCurrentLambda().setImportsLexVars();
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitSetExp(SetExp exp, Void ignored) {
        Declaration decl = exp.binding;
        if (decl == null) {
            decl = allocUnboundDecl(exp.getSymbol(), exp.isFuncDef());
            exp.binding = decl;
        }
        if (!decl.ignorable()) {
            if (!exp.isDefining()) {
                decl = Declaration.followAliases(decl);
            }
            capture(exp.contextDecl(), decl);
        }
        return (Expression) super.visitSetExp(exp, ignored);
    }
}
