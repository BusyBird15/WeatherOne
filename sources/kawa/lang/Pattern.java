package kawa.lang;

import gnu.bytecode.ClassType;
import gnu.bytecode.Method;
import gnu.bytecode.Type;
import gnu.expr.Compilation;
import gnu.text.Printable;

public abstract class Pattern implements Printable {
    private static Type[] matchArgs = {Type.pointer_type, Compilation.objArrayType, Type.intType};
    public static final Method matchPatternMethod = typePattern.addMethod("match", matchArgs, (Type) Type.booleanType, 1);
    public static ClassType typePattern = ClassType.make("kawa.lang.Pattern");

    public abstract boolean match(Object obj, Object[] objArr, int i);

    public abstract int varCount();

    public Object[] match(Object obj) {
        Object[] vars = new Object[varCount()];
        if (match(obj, vars, 0)) {
            return vars;
        }
        return null;
    }
}
