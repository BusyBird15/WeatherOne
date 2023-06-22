package gnu.expr;

public class ResolveNames extends ExpExpVisitor<Void> {
    protected NameLookup lookup;

    public ResolveNames() {
    }

    public ResolveNames(Compilation comp) {
        setContext(comp);
        this.lookup = comp.lexical;
    }

    public void resolveModule(ModuleExp exp) {
        Compilation saveComp = Compilation.setSaveCurrent(this.comp);
        try {
            push(exp);
            exp.visitChildren(this, null);
        } finally {
            Compilation.restoreCurrent(saveComp);
        }
    }

    /* access modifiers changed from: protected */
    public void push(ScopeExp exp) {
        this.lookup.push(exp);
    }

    /* access modifiers changed from: protected */
    public Expression visitScopeExp(ScopeExp exp, Void ignored) {
        visitDeclarationTypes(exp);
        push(exp);
        exp.visitChildren(this, ignored);
        this.lookup.pop(exp);
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitLetExp(LetExp exp, Void ignored) {
        visitDeclarationTypes(exp);
        exp.visitInitializers(this, ignored);
        push(exp);
        exp.body = (Expression) visit(exp.body, ignored);
        this.lookup.pop((ScopeExp) exp);
        return exp;
    }

    public Declaration lookup(Expression exp, Object symbol, boolean function) {
        return this.lookup.lookup(symbol, function);
    }

    /* access modifiers changed from: protected */
    public Expression visitReferenceExp(ReferenceExp exp, Void ignored) {
        Declaration decl;
        if (exp.getBinding() == null && (decl = lookup(exp, exp.getSymbol(), exp.isProcedureName())) != null) {
            exp.setBinding(decl);
        }
        return exp;
    }

    /* access modifiers changed from: protected */
    public Expression visitSetExp(SetExp exp, Void ignored) {
        if (exp.binding == null) {
            Declaration decl = lookup(exp, exp.getSymbol(), exp.isFuncDef());
            if (decl != null) {
                decl.setCanWrite(true);
            }
            exp.binding = decl;
        }
        return (Expression) super.visitSetExp(exp, ignored);
    }
}
