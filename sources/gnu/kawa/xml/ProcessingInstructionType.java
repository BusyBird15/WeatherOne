package gnu.kawa.xml;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Method;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.expr.Compilation;
import gnu.expr.TypeValue;
import gnu.lists.AbstractSequence;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ProcessingInstructionType extends NodeType implements TypeValue, Externalizable {
    static final Method coerceMethod = typeProcessingInstructionType.getDeclaredMethod("coerce", 2);
    static final Method coerceOrNullMethod = typeProcessingInstructionType.getDeclaredMethod("coerceOrNull", 2);
    public static final ProcessingInstructionType piNodeTest = new ProcessingInstructionType((String) null);
    public static final ClassType typeProcessingInstructionType = ClassType.make("gnu.kawa.xml.ProcessingInstructionType");
    String target;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ProcessingInstructionType(String target2) {
        super(target2 == null ? "processing-instruction()" : "processing-instruction(" + target2 + ")");
        this.target = target2;
    }

    public static ProcessingInstructionType getInstance(String target2) {
        return target2 == null ? piNodeTest : new ProcessingInstructionType(target2);
    }

    public Type getImplementationType() {
        return ClassType.make("gnu.kawa.xml.KProcessingInstruction");
    }

    public void emitCoerceFromObject(CodeAttr code) {
        code.emitPushString(this.target);
        code.emitInvokeStatic(coerceMethod);
    }

    public Object coerceFromObject(Object obj) {
        return coerce(obj, this.target);
    }

    public boolean isInstancePos(AbstractSequence seq, int ipos) {
        int kind = seq.getNextKind(ipos);
        if (kind == 37) {
            if (this.target == null || this.target.equals(seq.getNextTypeObject(ipos))) {
                return true;
            }
            return false;
        } else if (kind == 32) {
            return isInstance(seq.getPosNext(ipos));
        } else {
            return false;
        }
    }

    public boolean isInstance(Object obj) {
        return coerceOrNull(obj, this.target) != null;
    }

    public static KProcessingInstruction coerceOrNull(Object obj, String target2) {
        KProcessingInstruction pos = (KProcessingInstruction) NodeType.coerceOrNull(obj, 32);
        if (pos == null || (target2 != null && !target2.equals(pos.getTarget()))) {
            return null;
        }
        return pos;
    }

    public static KProcessingInstruction coerce(Object obj, String target2) {
        KProcessingInstruction pos = coerceOrNull(obj, target2);
        if (pos != null) {
            return pos;
        }
        throw new ClassCastException();
    }

    /* access modifiers changed from: protected */
    public void emitCoerceOrNullMethod(Variable incoming, Compilation comp) {
        CodeAttr code = comp.getCode();
        if (incoming != null) {
            code.emitLoad(incoming);
        }
        code.emitPushString(this.target);
        code.emitInvokeStatic(coerceOrNullMethod);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.target);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.target = (String) in.readObject();
    }

    public String toString() {
        return "ProcessingInstructionType " + (this.target == null ? "*" : this.target);
    }
}
