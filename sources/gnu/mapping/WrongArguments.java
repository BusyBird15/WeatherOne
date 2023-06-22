package gnu.mapping;

public class WrongArguments extends IllegalArgumentException {
    public int number;
    Procedure proc;
    public String procname;
    public String usage;

    public static String checkArgCount(Procedure proc2, int argCount) {
        int num = proc2.numArgs();
        int min = num & 4095;
        int max = num >> 12;
        String pname = proc2.getName();
        if (pname == null) {
            pname = proc2.getClass().getName();
        }
        return checkArgCount(pname, min, max, argCount);
    }

    public static String checkArgCount(String pname, int min, int max, int argCount) {
        boolean tooMany;
        if (argCount < min) {
            tooMany = false;
        } else if (max < 0 || argCount <= max) {
            return null;
        } else {
            tooMany = true;
        }
        StringBuffer buf = new StringBuffer(100);
        buf.append("call to ");
        if (pname == null) {
            buf.append("unnamed procedure");
        } else {
            buf.append('\'');
            buf.append(pname);
            buf.append('\'');
        }
        buf.append(tooMany ? " has too many" : " has too few");
        buf.append(" arguments (");
        buf.append(argCount);
        if (min == max) {
            buf.append("; must be ");
            buf.append(min);
        } else {
            buf.append("; min=");
            buf.append(min);
            if (max >= 0) {
                buf.append(", max=");
                buf.append(max);
            }
        }
        buf.append(')');
        return buf.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = checkArgCount(r3.proc, r3.number);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getMessage() {
        /*
            r3 = this;
            gnu.mapping.Procedure r1 = r3.proc
            if (r1 == 0) goto L_0x000f
            gnu.mapping.Procedure r1 = r3.proc
            int r2 = r3.number
            java.lang.String r0 = checkArgCount(r1, r2)
            if (r0 == 0) goto L_0x000f
        L_0x000e:
            return r0
        L_0x000f:
            java.lang.String r0 = super.getMessage()
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.mapping.WrongArguments.getMessage():java.lang.String");
    }

    public WrongArguments(Procedure proc2, int argCount) {
        this.proc = proc2;
        this.number = argCount;
    }

    public WrongArguments(String name, int n, String u) {
        this.procname = name;
        this.number = n;
        this.usage = u;
    }
}
