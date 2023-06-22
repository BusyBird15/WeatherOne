package gnu.mapping;

import gnu.bytecode.Type;

public class WrongType extends WrappedException {
    public static final int ARG_CAST = -4;
    public static final int ARG_DESCRIPTION = -3;
    public static final int ARG_UNKNOWN = -1;
    public static final int ARG_VARNAME = -2;
    public Object argValue;
    public Object expectedType;
    public int number;
    public Procedure proc;
    public String procname;

    public WrongType(String name, int n, String u) {
        super((String) null, (Throwable) null);
        this.procname = name;
        this.number = n;
        this.expectedType = u;
    }

    public WrongType(Procedure proc2, int n, ClassCastException ex) {
        super((Throwable) ex);
        this.proc = proc2;
        this.procname = proc2.getName();
        this.number = n;
    }

    public WrongType(ClassCastException ex, Procedure proc2, int n, Object argValue2) {
        this(proc2, n, ex);
        this.argValue = argValue2;
    }

    public WrongType(Procedure proc2, int n, Object argValue2) {
        this.proc = proc2;
        this.procname = proc2.getName();
        this.number = n;
        this.argValue = argValue2;
    }

    public WrongType(Procedure proc2, int n, Object argValue2, Type expectedType2) {
        this.proc = proc2;
        this.procname = proc2.getName();
        this.number = n;
        this.argValue = argValue2;
        this.expectedType = expectedType2;
    }

    public WrongType(int n, Object argValue2, Type expectedType2) {
        this.number = n;
        this.argValue = argValue2;
        this.expectedType = expectedType2;
    }

    public WrongType(Procedure proc2, int n, Object argValue2, String expectedType2) {
        this(proc2.getName(), n, argValue2, expectedType2);
        this.proc = proc2;
    }

    public WrongType(String procName, int n, Object argValue2, String expectedType2) {
        this.procname = procName;
        this.number = n;
        this.argValue = argValue2;
        this.expectedType = expectedType2;
    }

    public WrongType(String procname2, int n, ClassCastException ex) {
        super((Throwable) ex);
        this.procname = procname2;
        this.number = n;
    }

    public WrongType(ClassCastException ex, String procname2, int n, Object argValue2) {
        this(procname2, n, ex);
        this.argValue = argValue2;
    }

    public static WrongType make(ClassCastException ex, Procedure proc2, int n) {
        return new WrongType(proc2, n, ex);
    }

    public static WrongType make(ClassCastException ex, String procname2, int n) {
        return new WrongType(procname2, n, ex);
    }

    public static WrongType make(ClassCastException ex, Procedure proc2, int n, Object argValue2) {
        WrongType wex = new WrongType(proc2, n, ex);
        wex.argValue = argValue2;
        return wex;
    }

    public static WrongType make(ClassCastException ex, String procname2, int n, Object argValue2) {
        WrongType wex = new WrongType(procname2, n, ex);
        wex.argValue = argValue2;
        return wex;
    }

    public String getMessage() {
        String msg;
        StringBuffer sbuf = new StringBuffer(100);
        if (this.number == -3) {
            sbuf.append(this.procname);
        } else if (this.number == -4 || this.number == -2) {
            sbuf.append("Value");
        } else {
            sbuf.append("Argument ");
            if (this.number > 0) {
                sbuf.append('#');
                sbuf.append(this.number);
            }
        }
        if (this.argValue != null) {
            sbuf.append(" (");
            String argString = this.argValue.toString();
            if (argString.length() > 50) {
                sbuf.append(argString.substring(0, 47));
                sbuf.append("...");
            } else {
                sbuf.append(argString);
            }
            sbuf.append(")");
        }
        if (!(this.procname == null || this.number == -3)) {
            sbuf.append(this.number == -2 ? " for variable '" : " to '");
            sbuf.append(this.procname);
            sbuf.append("'");
        }
        sbuf.append(" has wrong type");
        if (this.argValue != null) {
            sbuf.append(" (");
            sbuf.append(this.argValue.getClass().getName());
            sbuf.append(")");
        }
        Object expectType = this.expectedType;
        if (expectType == null && this.number > 0 && (this.proc instanceof MethodProc)) {
            expectType = ((MethodProc) this.proc).getParameterType(this.number - 1);
        }
        if (!(expectType == null || expectType == Type.pointer_type)) {
            sbuf.append(" (expected: ");
            if (expectType instanceof Type) {
                expectType = ((Type) expectType).getName();
            } else if (expectType instanceof Class) {
                expectType = ((Class) expectType).getName();
            }
            sbuf.append(expectType);
            sbuf.append(")");
        }
        Throwable ex = getCause();
        if (!(ex == null || (msg = ex.getMessage()) == null)) {
            sbuf.append(" (");
            sbuf.append(msg);
            sbuf.append(')');
        }
        return sbuf.toString();
    }
}
