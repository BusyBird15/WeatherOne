package gnu.expr;

public abstract class ExpExpVisitor<D> extends ExpVisitor<Expression, D> {
    /* access modifiers changed from: protected */
    public Expression update(Expression exp, Expression r) {
        return r;
    }

    /* access modifiers changed from: protected */
    public Expression defaultValue(Expression r, D d) {
        return r;
    }
}
