package gnu.bytecode;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class Variable extends Location implements Enumeration {
    private static final int LIVE_FLAG = 4;
    private static final int PARAMETER_FLAG = 2;
    private static final int SIMPLE_FLAG = 1;
    static final int UNASSIGNED = -1;
    private int flags = 1;
    Variable next;
    int offset = -1;
    Scope scope;

    public final Variable nextVar() {
        return this.next;
    }

    public final boolean hasMoreElements() {
        return this.next != null;
    }

    public Object nextElement() {
        if (this.next != null) {
            return this.next;
        }
        throw new NoSuchElementException("Variable enumeration");
    }

    public Variable() {
    }

    public Variable(String name) {
        setName(name);
    }

    public Variable(String name, Type type) {
        setName(name);
        setType(type);
    }

    public final boolean isAssigned() {
        return this.offset != -1;
    }

    public final boolean dead() {
        return (this.flags & 4) == 0;
    }

    private void setFlag(boolean setting, int flag) {
        if (setting) {
            this.flags |= flag;
        } else {
            this.flags &= flag ^ -1;
        }
    }

    public final boolean isSimple() {
        return (this.flags & 1) != 0;
    }

    public final void setSimple(boolean simple) {
        setFlag(simple, 1);
    }

    public final boolean isParameter() {
        return (this.flags & 2) != 0;
    }

    public final void setParameter(boolean parameter) {
        setFlag(parameter, 2);
    }

    public boolean reserveLocal(int varIndex, CodeAttr code) {
        int size = getType().getSizeInWords();
        if (code.locals.used == null) {
            code.locals.used = new Variable[(size + 20)];
        } else if (code.getMaxLocals() + size >= code.locals.used.length) {
            Variable[] new_locals = new Variable[((code.locals.used.length * 2) + size)];
            System.arraycopy(code.locals.used, 0, new_locals, 0, code.getMaxLocals());
            code.locals.used = new_locals;
        }
        for (int j = 0; j < size; j++) {
            if (code.locals.used[varIndex + j] != null) {
                return false;
            }
        }
        for (int j2 = 0; j2 < size; j2++) {
            code.locals.used[varIndex + j2] = this;
        }
        if (varIndex + size > code.getMaxLocals()) {
            code.setMaxLocals(varIndex + size);
        }
        this.offset = varIndex;
        this.flags |= 4;
        if (this.offset == 0 && "<init>".equals(code.getMethod().getName())) {
            setType(code.local_types[0]);
        }
        return true;
    }

    public void allocateLocal(CodeAttr code) {
        if (this.offset == -1) {
            for (int i = 0; !reserveLocal(i, code); i++) {
            }
        }
    }

    public void freeLocal(CodeAttr code) {
        this.flags &= -5;
        int vnum = this.offset + (getType().size > 4 ? 2 : 1);
        while (true) {
            vnum--;
            if (vnum >= this.offset) {
                code.locals.used[vnum] = null;
                Type[] local_types = code.local_types;
                if (local_types != null) {
                    local_types[vnum] = null;
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0012, code lost:
        r1 = (r3 = r2.start).position;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0016, code lost:
        r0 = r2.end;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldEmit() {
        /*
            r5 = this;
            gnu.bytecode.Scope r2 = r5.scope
            boolean r4 = r5.isSimple()
            if (r4 == 0) goto L_0x0020
            java.lang.String r4 = r5.name
            if (r4 == 0) goto L_0x0020
            if (r2 == 0) goto L_0x0020
            gnu.bytecode.Label r3 = r2.start
            if (r3 == 0) goto L_0x0020
            int r1 = r3.position
            if (r1 < 0) goto L_0x0020
            gnu.bytecode.Label r0 = r2.end
            if (r0 == 0) goto L_0x0020
            int r4 = r0.position
            if (r4 <= r1) goto L_0x0020
            r4 = 1
        L_0x001f:
            return r4
        L_0x0020:
            r4 = 0
            goto L_0x001f
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.bytecode.Variable.shouldEmit():boolean");
    }

    public String toString() {
        return "Variable[" + getName() + " offset:" + this.offset + ']';
    }
}
