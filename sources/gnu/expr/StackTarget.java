package gnu.expr;

import gnu.bytecode.ArrayType;
import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.PrimType;
import gnu.bytecode.Type;
import gnu.kawa.reflect.OccurrenceType;
import gnu.mapping.Values;

public class StackTarget extends Target {
    Type type;

    public StackTarget(Type type2) {
        this.type = type2;
    }

    public Type getType() {
        return this.type;
    }

    public static Target getInstance(Type type2) {
        if (type2.isVoid()) {
            return Target.Ignore;
        }
        return type2 == Type.pointer_type ? Target.pushObject : new StackTarget(type2);
    }

    /* access modifiers changed from: protected */
    public boolean compileFromStack0(Compilation comp, Type stackType) {
        return compileFromStack0(comp, stackType, this.type);
    }

    static boolean compileFromStack0(Compilation comp, Type stackType, Type type2) {
        if (type2 == stackType) {
            return true;
        }
        CodeAttr code = comp.getCode();
        if (stackType.isVoid()) {
            comp.compileConstant(Values.empty);
            stackType = Type.pointer_type;
        } else if ((stackType instanceof PrimType) && (type2 instanceof PrimType)) {
            code.emitConvert(stackType, type2);
            return true;
        }
        if (!(stackType instanceof ArrayType)) {
            type2.emitConvertFromPrimitive(stackType, code);
            stackType = code.topType();
        } else if (type2 == Type.pointer_type || "java.lang.Cloneable".equals(type2.getName())) {
            return true;
        }
        if (CodeAttr.castNeeded(stackType.getImplementationType(), type2.getImplementationType())) {
            return false;
        }
        return true;
    }

    public static void convert(Compilation comp, Type stackType, Type targetType) {
        if (!compileFromStack0(comp, stackType, targetType)) {
            emitCoerceFromObject(targetType, comp);
        }
    }

    protected static void emitCoerceFromObject(Type type2, Compilation comp) {
        CodeAttr code = comp.getCode();
        if (type2 instanceof OccurrenceType) {
            comp.compileConstant(type2, Target.pushObject);
            code.emitSwap();
            code.emitInvokeVirtual(ClassType.make("gnu.bytecode.Type").getDeclaredMethod("coerceFromObject", 1));
            return;
        }
        comp.usedClass(type2);
        type2.emitCoerceFromObject(code);
    }

    public void compileFromStack(Compilation comp, Type stackType) {
        if (!compileFromStack0(comp, stackType)) {
            emitCoerceFromObject(this.type, comp);
        }
    }
}
