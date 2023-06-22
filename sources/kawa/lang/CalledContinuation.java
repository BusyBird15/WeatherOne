package kawa.lang;

import gnu.mapping.CallContext;

public class CalledContinuation extends RuntimeException {
    public Continuation continuation;
    public CallContext ctx;
    public Object[] values;

    CalledContinuation(Object[] values2, Continuation continuation2, CallContext ctx2) {
        super("call/cc called");
        this.values = values2;
        this.continuation = continuation2;
        this.ctx = ctx2;
    }
}
