package gnu.expr;

import gnu.bytecode.CodeAttr;
import gnu.bytecode.Type;
import gnu.mapping.CallContext;
import gnu.mapping.OutPort;

public class TryExp extends Expression {
    CatchClause catch_clauses;
    Expression finally_clause;
    Expression try_clause;

    public final CatchClause getCatchClauses() {
        return this.catch_clauses;
    }

    public final Expression getFinallyClause() {
        return this.finally_clause;
    }

    public final void setCatchClauses(CatchClause catch_clauses2) {
        this.catch_clauses = catch_clauses2;
    }

    public TryExp(Expression try_clause2, Expression finally_clause2) {
        this.try_clause = try_clause2;
        this.finally_clause = finally_clause2;
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        return false;
    }

    public void apply(CallContext ctx) throws Throwable {
        try {
            this.try_clause.apply(ctx);
            ctx.runUntilDone();
            if (this.finally_clause != null) {
                this.finally_clause.eval(ctx);
            }
        } catch (Throwable th) {
            if (this.finally_clause != null) {
                this.finally_clause.eval(ctx);
            }
            throw th;
        }
    }

    public void compile(Compilation comp, Target target) {
        Target ttarg;
        CodeAttr code = comp.getCode();
        boolean has_finally = this.finally_clause != null;
        if ((target instanceof StackTarget) || (target instanceof ConsumerTarget) || (target instanceof IgnoreTarget) || ((target instanceof ConditionalTarget) && this.finally_clause == null)) {
            ttarg = target;
        } else {
            ttarg = Target.pushValue(target.getType());
        }
        code.emitTryStart(has_finally, ttarg instanceof StackTarget ? ttarg.getType() : null);
        this.try_clause.compileWithPosition(comp, ttarg);
        for (CatchClause catch_clause = this.catch_clauses; catch_clause != null; catch_clause = catch_clause.getNext()) {
            catch_clause.compile(comp, ttarg);
        }
        if (this.finally_clause != null) {
            code.emitFinallyStart();
            this.finally_clause.compileWithPosition(comp, Target.Ignore);
            code.emitFinallyEnd();
        }
        code.emitTryCatchEnd();
        if (ttarg != target) {
            target.compileFromStack(comp, target.getType());
        }
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        return visitor.visitTryExp(this, d);
    }

    /* access modifiers changed from: protected */
    public <R, D> void visitChildren(ExpVisitor<R, D> visitor, D d) {
        this.try_clause = visitor.visitAndUpdate(this.try_clause, d);
        CatchClause catch_clause = this.catch_clauses;
        while (visitor.exitValue == null && catch_clause != null) {
            visitor.visit(catch_clause, d);
            catch_clause = catch_clause.getNext();
        }
        if (visitor.exitValue == null && this.finally_clause != null) {
            this.finally_clause = visitor.visitAndUpdate(this.finally_clause, d);
        }
    }

    public Type getType() {
        if (this.catch_clauses == null) {
            return this.try_clause.getType();
        }
        return super.getType();
    }

    public void print(OutPort ps) {
        ps.startLogicalBlock("(Try", ")", 2);
        ps.writeSpaceFill();
        this.try_clause.print(ps);
        for (CatchClause catch_clause = this.catch_clauses; catch_clause != null; catch_clause = catch_clause.getNext()) {
            catch_clause.print(ps);
        }
        if (this.finally_clause != null) {
            ps.writeSpaceLinear();
            ps.print(" finally: ");
            this.finally_clause.print(ps);
        }
        ps.endLogicalBlock(")");
    }
}
