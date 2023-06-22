package gnu.kawa.reflect;

import gnu.bytecode.ClassType;
import gnu.bytecode.Filter;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;

/* compiled from: ClassMethods */
class MethodFilter implements Filter {
    ClassType caller;
    int modifiers;
    int modmask;
    String name;
    int nlen;
    ObjectType receiver;

    public MethodFilter(String name2, int modifiers2, int modmask2, ClassType caller2, ObjectType receiver2) {
        this.name = name2;
        this.nlen = name2.length();
        this.modifiers = modifiers2;
        this.modmask = modmask2;
        this.caller = caller2;
        this.receiver = receiver2;
    }

    public boolean select(Object value) {
        boolean z;
        char c;
        Method method = (Method) value;
        String mname = method.getName();
        int mmods = method.getModifiers();
        if ((this.modmask & mmods) != this.modifiers || (mmods & 4096) != 0 || !mname.startsWith(this.name)) {
            return false;
        }
        int mlen = mname.length();
        if (mlen != this.nlen && ((mlen != this.nlen + 2 || mname.charAt(this.nlen) != '$' || ((c = mname.charAt(this.nlen + 1)) != 'V' && c != 'X')) && (mlen != this.nlen + 4 || !mname.endsWith("$V$X")))) {
            return false;
        }
        ClassType declaring = this.receiver instanceof ClassType ? (ClassType) this.receiver : method.getDeclaringClass();
        if (this.caller == null || this.caller.isAccessible(declaring, this.receiver, method.getModifiers())) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }
}
