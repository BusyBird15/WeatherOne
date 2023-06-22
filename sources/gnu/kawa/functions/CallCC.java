package gnu.kawa.functions;

import androidx.fragment.app.FragmentTransaction;
import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.Inlineable;
import gnu.expr.Target;
import gnu.mapping.CallContext;
import gnu.mapping.MethodProc;
import gnu.mapping.Procedure;
import kawa.lang.Continuation;

public class CallCC extends MethodProc implements Inlineable {
    public static final CallCC callcc = new CallCC();

    CallCC() {
        setProperty(Procedure.validateApplyKey, "gnu.kawa.functions.CompileMisc:validateApplyCallCC");
    }

    public int numArgs() {
        return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
    }

    public int match1(Object proc, CallContext ctx) {
        if (!(proc instanceof Procedure)) {
            return MethodProc.NO_MATCH_BAD_TYPE;
        }
        return super.match1(proc, ctx);
    }

    public void apply(CallContext ctx) throws Throwable {
        Continuation cont = new Continuation(ctx);
        ((Procedure) ctx.value1).check1(cont, ctx);
        Procedure proc = ctx.proc;
        ctx.proc = null;
        try {
            proc.apply(ctx);
            ctx.runUntilDone();
            cont.invoked = true;
        } catch (Throwable ex) {
            Continuation.handleException$X(ex, cont, ctx);
        }
    }

    public void compile(ApplyExp exp, Compilation comp, Target target) {
        CompileMisc.compileCallCC(exp, comp, target, this);
    }

    public Type getReturnType(Expression[] args) {
        return Type.pointer_type;
    }
}
