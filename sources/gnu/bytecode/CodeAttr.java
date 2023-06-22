package gnu.bytecode;

import androidx.core.internal.view.SupportMenu;
import com.google.appinventor.components.runtime.util.Ev3Constants;
import com.google.appinventor.components.runtime.util.FullScreenVideoUtil;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeAttr extends Attribute implements AttrContainer {
    public static final int DONT_USE_JSR = 2;
    static final int FIXUP_CASE = 3;
    static final int FIXUP_DEFINE = 1;
    static final int FIXUP_DELETE3 = 8;
    static final int FIXUP_GOTO = 4;
    static final int FIXUP_JSR = 5;
    static final int FIXUP_LINE_NUMBER = 15;
    static final int FIXUP_LINE_PC = 14;
    static final int FIXUP_MOVE = 9;
    static final int FIXUP_MOVE_TO_END = 10;
    static final int FIXUP_NONE = 0;
    static final int FIXUP_SWITCH = 2;
    static final int FIXUP_TRANSFER = 6;
    static final int FIXUP_TRANSFER2 = 7;
    static final int FIXUP_TRY = 11;
    static final int FIXUP_TRY_END = 12;
    static final int FIXUP_TRY_HANDLER = 13;
    public static final int GENERATE_STACK_MAP_TABLE = 1;
    public static boolean instructionLineMode = false;
    int PC;
    int SP;
    Attribute attributes;
    byte[] code;
    ExitableBlock currentExitableBlock;
    short[] exception_table;
    int exception_table_length;
    int exitableBlockLevel;
    int fixup_count;
    Label[] fixup_labels;
    int[] fixup_offsets;
    int flags;
    IfState if_stack;
    LineNumbersAttr lines;
    Type[] local_types;
    public LocalVarsAttr locals;
    private int max_locals;
    private int max_stack;
    Label previousLabel;
    SourceDebugExtAttr sourceDbgExt;
    public StackMapTableAttr stackMap;
    public Type[] stack_types;
    TryState try_stack;
    private boolean unreachable_here;
    boolean[] varsSetInCurrentBlock;

    public final Attribute getAttributes() {
        return this.attributes;
    }

    public final void setAttributes(Attribute attributes2) {
        this.attributes = attributes2;
    }

    /* access modifiers changed from: package-private */
    public boolean useJsr() {
        return (this.flags & 2) == 0;
    }

    public final void fixupChain(Label here, Label target) {
        fixupAdd(9, 0, target);
        here.defineRaw(this);
    }

    public final void fixupAdd(int kind, Label label) {
        fixupAdd(kind, this.PC, label);
    }

    /* access modifiers changed from: package-private */
    public final void fixupAdd(int kind, int offset, Label label) {
        if (!(label == null || kind == 1 || kind == 0 || kind == 2 || kind == 11)) {
            label.needsStackMapEntry = true;
        }
        int count = this.fixup_count;
        if (count == 0) {
            this.fixup_offsets = new int[30];
            this.fixup_labels = new Label[30];
        } else if (this.fixup_count == this.fixup_offsets.length) {
            int new_length = count * 2;
            Label[] new_labels = new Label[new_length];
            System.arraycopy(this.fixup_labels, 0, new_labels, 0, count);
            this.fixup_labels = new_labels;
            int[] new_offsets = new int[new_length];
            System.arraycopy(this.fixup_offsets, 0, new_offsets, 0, count);
            this.fixup_offsets = new_offsets;
        }
        this.fixup_offsets[count] = (offset << 4) | kind;
        this.fixup_labels[count] = label;
        this.fixup_count = count + 1;
    }

    private final int fixupOffset(int index) {
        return this.fixup_offsets[index] >> 4;
    }

    private final int fixupKind(int index) {
        return this.fixup_offsets[index] & 15;
    }

    public final Method getMethod() {
        return (Method) getContainer();
    }

    public final int getPC() {
        return this.PC;
    }

    public final int getSP() {
        return this.SP;
    }

    public final ConstantPool getConstants() {
        return getMethod().classfile.constants;
    }

    public final boolean reachableHere() {
        return !this.unreachable_here;
    }

    public final void setReachable(boolean val) {
        this.unreachable_here = !val;
    }

    public final void setUnreachable() {
        this.unreachable_here = true;
    }

    public int getMaxStack() {
        return this.max_stack;
    }

    public int getMaxLocals() {
        return this.max_locals;
    }

    public void setMaxStack(int n) {
        this.max_stack = n;
    }

    public void setMaxLocals(int n) {
        this.max_locals = n;
    }

    public byte[] getCode() {
        return this.code;
    }

    public void setCode(byte[] code2) {
        this.code = code2;
        this.PC = code2.length;
    }

    public void setCodeLength(int len) {
        this.PC = len;
    }

    public int getCodeLength() {
        return this.PC;
    }

    public CodeAttr(Method meth) {
        super("Code");
        addToFrontOf(meth);
        meth.code = this;
        if (meth.getDeclaringClass().getClassfileMajorVersion() >= 50) {
            this.flags |= 3;
        }
    }

    public final void reserve(int bytes) {
        if (this.code == null) {
            this.code = new byte[(bytes + 100)];
        } else if (this.PC + bytes > this.code.length) {
            byte[] new_code = new byte[((this.code.length * 2) + bytes)];
            System.arraycopy(this.code, 0, new_code, 0, this.PC);
            this.code = new_code;
        }
    }

    /* access modifiers changed from: package-private */
    public byte invert_opcode(byte opcode) {
        int iopcode = opcode & 255;
        if ((iopcode >= 153 && iopcode <= 166) || (iopcode >= 198 && iopcode <= 199)) {
            return (byte) (iopcode ^ 1);
        }
        throw new Error("unknown opcode to invert_opcode");
    }

    public final void put1(int i) {
        byte[] bArr = this.code;
        int i2 = this.PC;
        this.PC = i2 + 1;
        bArr[i2] = (byte) i;
        this.unreachable_here = false;
    }

    public final void put2(int i) {
        byte[] bArr = this.code;
        int i2 = this.PC;
        this.PC = i2 + 1;
        bArr[i2] = (byte) (i >> 8);
        byte[] bArr2 = this.code;
        int i3 = this.PC;
        this.PC = i3 + 1;
        bArr2[i3] = (byte) i;
        this.unreachable_here = false;
    }

    public final void put4(int i) {
        byte[] bArr = this.code;
        int i2 = this.PC;
        this.PC = i2 + 1;
        bArr[i2] = (byte) (i >> 24);
        byte[] bArr2 = this.code;
        int i3 = this.PC;
        this.PC = i3 + 1;
        bArr2[i3] = (byte) (i >> 16);
        byte[] bArr3 = this.code;
        int i4 = this.PC;
        this.PC = i4 + 1;
        bArr3[i4] = (byte) (i >> 8);
        byte[] bArr4 = this.code;
        int i5 = this.PC;
        this.PC = i5 + 1;
        bArr4[i5] = (byte) i;
        this.unreachable_here = false;
    }

    public final void putIndex2(CpoolEntry cnst) {
        put2(cnst.index);
    }

    public final void putLineNumber(String filename, int linenumber) {
        if (filename != null) {
            getMethod().classfile.setSourceFile(filename);
        }
        putLineNumber(linenumber);
    }

    public final void putLineNumber(int linenumber) {
        if (this.sourceDbgExt != null) {
            linenumber = this.sourceDbgExt.fixLine(linenumber);
        }
        fixupAdd(14, (Label) null);
        fixupAdd(15, linenumber, (Label) null);
    }

    /* access modifiers changed from: package-private */
    public void noteParamTypes() {
        Method method = getMethod();
        int offset = 0;
        if ((method.access_flags & 8) == 0) {
            Type type = method.classfile;
            if ("<init>".equals(method.getName()) && !"java.lang.Object".equals(type.getName())) {
                type = UninitializedType.uninitializedThis((ClassType) type);
            }
            noteVarType(0, type);
            offset = 0 + 1;
        }
        int arg_count = method.arg_types.length;
        int i = 0;
        int offset2 = offset;
        while (i < arg_count) {
            Type type2 = method.arg_types[i];
            int offset3 = offset2 + 1;
            noteVarType(offset2, type2);
            int size = type2.getSizeInWords();
            while (true) {
                size--;
                if (size <= 0) {
                    break;
                }
                offset3++;
            }
            i++;
            offset2 = offset3;
        }
        if ((this.flags & 1) != 0) {
            this.stackMap = new StackMapTableAttr();
            int[] encodedLocals = new int[(offset2 + 20)];
            int i2 = 0;
            int count = 0;
            while (i2 < offset2) {
                int encoded = this.stackMap.encodeVerificationType(this.local_types[i2], this);
                int count2 = count + 1;
                encodedLocals[count] = encoded;
                int tag = encoded & 255;
                if (tag == 3 || tag == 4) {
                    i2++;
                }
                i2++;
                count = count2;
            }
            this.stackMap.encodedLocals = encodedLocals;
            this.stackMap.countLocals = count;
            this.stackMap.encodedStack = new int[10];
            this.stackMap.countStack = 0;
        }
    }

    public void noteVarType(int offset, Type type) {
        Type prev;
        int size = type.getSizeInWords();
        if (this.local_types == null) {
            this.local_types = new Type[(offset + size + 20)];
        } else if (offset + size > this.local_types.length) {
            Type[] new_array = new Type[((offset + size) * 2)];
            System.arraycopy(this.local_types, 0, new_array, 0, this.local_types.length);
            this.local_types = new_array;
        }
        this.local_types[offset] = type;
        if (this.varsSetInCurrentBlock == null) {
            this.varsSetInCurrentBlock = new boolean[this.local_types.length];
        } else if (this.varsSetInCurrentBlock.length <= offset) {
            boolean[] tmp = new boolean[this.local_types.length];
            System.arraycopy(this.varsSetInCurrentBlock, 0, tmp, 0, this.varsSetInCurrentBlock.length);
            this.varsSetInCurrentBlock = tmp;
        }
        this.varsSetInCurrentBlock[offset] = true;
        if (offset > 0 && (prev = this.local_types[offset - 1]) != null && prev.getSizeInWords() == 2) {
            this.local_types[offset - 1] = null;
        }
        while (true) {
            size--;
            if (size > 0) {
                offset++;
                this.local_types[offset] = null;
            } else {
                return;
            }
        }
    }

    public final void setTypes(Label label) {
        setTypes(label.localTypes, label.stackTypes);
    }

    public final void setTypes(Type[] labelLocals, Type[] labelStack) {
        int usedStack = labelStack.length;
        int usedLocals = labelLocals.length;
        if (this.local_types != null) {
            if (usedLocals > 0) {
                System.arraycopy(labelLocals, 0, this.local_types, 0, usedLocals);
            }
            for (int i = usedLocals; i < this.local_types.length; i++) {
                this.local_types[i] = null;
            }
        }
        if (this.stack_types == null || usedStack > this.stack_types.length) {
            this.stack_types = new Type[usedStack];
        } else {
            for (int i2 = usedStack; i2 < this.stack_types.length; i2++) {
                this.stack_types[i2] = null;
            }
        }
        System.arraycopy(labelStack, 0, this.stack_types, 0, usedStack);
        this.SP = usedStack;
    }

    public final void pushType(Type type) {
        if (type.size == 0) {
            throw new Error("pushing void type onto stack");
        }
        if (this.stack_types == null || this.stack_types.length == 0) {
            this.stack_types = new Type[20];
        } else if (this.SP + 1 >= this.stack_types.length) {
            Type[] new_array = new Type[(this.stack_types.length * 2)];
            System.arraycopy(this.stack_types, 0, new_array, 0, this.SP);
            this.stack_types = new_array;
        }
        if (type.size == 8) {
            Type[] typeArr = this.stack_types;
            int i = this.SP;
            this.SP = i + 1;
            typeArr[i] = Type.voidType;
        }
        Type[] typeArr2 = this.stack_types;
        int i2 = this.SP;
        this.SP = i2 + 1;
        typeArr2[i2] = type;
        if (this.SP > this.max_stack) {
            this.max_stack = this.SP;
        }
    }

    public final Type popType() {
        if (this.SP <= 0) {
            throw new Error("popType called with empty stack " + getMethod());
        }
        Type[] typeArr = this.stack_types;
        int i = this.SP - 1;
        this.SP = i;
        Type type = typeArr[i];
        if (type.size != 8 || popType().isVoid()) {
            return type;
        }
        throw new Error("missing void type on stack");
    }

    public final Type topType() {
        return this.stack_types[this.SP - 1];
    }

    public void emitPop(int nvalues) {
        while (nvalues > 0) {
            reserve(1);
            if (popType().size > 4) {
                put1(88);
            } else if (nvalues > 1) {
                if (popType().size > 4) {
                    put1(87);
                    reserve(1);
                }
                put1(88);
                nvalues--;
            } else {
                put1(87);
            }
            nvalues--;
        }
    }

    public Label getLabel() {
        Label label = new Label();
        label.defineRaw(this);
        return label;
    }

    public void emitSwap() {
        reserve(1);
        Type type1 = popType();
        Type type2 = popType();
        if (type1.size > 4 || type2.size > 4) {
            pushType(type2);
            pushType(type1);
            emitDupX();
            emitPop(1);
            return;
        }
        pushType(type1);
        put1(95);
        pushType(type2);
    }

    public void emitDup() {
        reserve(1);
        Type type = topType();
        put1(type.size <= 4 ? 89 : 92);
        pushType(type);
    }

    public void emitDupX() {
        reserve(1);
        Type type = popType();
        Type skipedType = popType();
        if (skipedType.size <= 4) {
            put1(type.size <= 4 ? 90 : 93);
        } else {
            put1(type.size <= 4 ? 91 : 94);
        }
        pushType(type);
        pushType(skipedType);
        pushType(type);
    }

    public void emitDup(int size, int offset) {
        int kind;
        if (size != 0) {
            reserve(1);
            Type copied1 = popType();
            Type copied2 = null;
            if (size == 1) {
                if (copied1.size > 4) {
                    throw new Error("using dup for 2-word type");
                }
            } else if (size != 2) {
                throw new Error("invalid size to emitDup");
            } else if (copied1.size <= 4) {
                copied2 = popType();
                if (copied2.size > 4) {
                    throw new Error("dup will cause invalid types on stack");
                }
            }
            Type skipped1 = null;
            Type skipped2 = null;
            if (offset == 0) {
                kind = size == 1 ? 89 : 92;
            } else if (offset == 1) {
                kind = size == 1 ? 90 : 93;
                skipped1 = popType();
                if (skipped1.size > 4) {
                    throw new Error("dup will cause invalid types on stack");
                }
            } else if (offset == 2) {
                kind = size == 1 ? 91 : 94;
                skipped1 = popType();
                if (skipped1.size <= 4) {
                    skipped2 = popType();
                    if (skipped2.size > 4) {
                        throw new Error("dup will cause invalid types on stack");
                    }
                }
            } else {
                throw new Error("emitDup:  invalid offset");
            }
            put1(kind);
            if (copied2 != null) {
                pushType(copied2);
            }
            pushType(copied1);
            if (skipped2 != null) {
                pushType(skipped2);
            }
            if (skipped1 != null) {
                pushType(skipped1);
            }
            if (copied2 != null) {
                pushType(copied2);
            }
            pushType(copied1);
        }
    }

    public void emitDup(int size) {
        emitDup(size, 0);
    }

    public void emitDup(Type type) {
        emitDup(type.size > 4 ? 2 : 1, 0);
    }

    public void enterScope(Scope scope) {
        scope.setStartPC(this);
        this.locals.enterScope(scope);
    }

    public Scope pushScope() {
        Scope scope = new Scope();
        if (this.locals == null) {
            this.locals = new LocalVarsAttr(getMethod());
        }
        enterScope(scope);
        if (this.locals.parameter_scope == null) {
            this.locals.parameter_scope = scope;
        }
        return scope;
    }

    public Scope getCurrentScope() {
        return this.locals.current_scope;
    }

    public Scope popScope() {
        Scope scope = this.locals.current_scope;
        this.locals.current_scope = scope.parent;
        scope.freeLocals(this);
        scope.end = getLabel();
        return scope;
    }

    public Variable getArg(int index) {
        return this.locals.parameter_scope.getVariable(index);
    }

    public Variable lookup(String name) {
        for (Scope scope = this.locals.current_scope; scope != null; scope = scope.parent) {
            Variable var = scope.lookup(name);
            if (var != null) {
                return var;
            }
        }
        return null;
    }

    public Variable addLocal(Type type) {
        return this.locals.current_scope.addVariable(this, type, (String) null);
    }

    public Variable addLocal(Type type, String name) {
        return this.locals.current_scope.addVariable(this, type, name);
    }

    public void addParamLocals() {
        Method method = getMethod();
        if ((method.access_flags & 8) == 0) {
            addLocal(method.classfile).setParameter(true);
        }
        for (Type addLocal : method.arg_types) {
            addLocal(addLocal).setParameter(true);
        }
    }

    public final void emitPushConstant(int val, Type type) {
        switch (type.getSignature().charAt(0)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                emitPushInt(val);
                return;
            case 'D':
                emitPushDouble((double) val);
                return;
            case 'F':
                emitPushFloat((float) val);
                return;
            case 'J':
                emitPushLong((long) val);
                return;
            default:
                throw new Error("bad type to emitPushConstant");
        }
    }

    public final void emitPushConstant(CpoolEntry cnst) {
        reserve(3);
        int index = cnst.index;
        if (cnst instanceof CpoolValue2) {
            put1(20);
            put2(index);
        } else if (index < 256) {
            put1(18);
            put1(index);
        } else {
            put1(19);
            put2(index);
        }
    }

    public final void emitPushInt(int i) {
        reserve(3);
        if (i >= -1 && i <= 5) {
            put1(i + 3);
        } else if (i >= -128 && i < 128) {
            put1(16);
            put1(i);
        } else if (i < -32768 || i >= 32768) {
            emitPushConstant(getConstants().addInt(i));
        } else {
            put1(17);
            put2(i);
        }
        pushType(Type.intType);
    }

    public void emitPushLong(long i) {
        if (i == 0 || i == 1) {
            reserve(1);
            put1(((int) i) + 9);
        } else if (((long) ((int) i)) == i) {
            emitPushInt((int) i);
            reserve(1);
            popType();
            put1(133);
        } else {
            emitPushConstant(getConstants().addLong(i));
        }
        pushType(Type.longType);
    }

    public void emitPushFloat(float x) {
        int xi = (int) x;
        if (((float) xi) != x || xi < -128 || xi >= 128) {
            emitPushConstant(getConstants().addFloat(x));
        } else if (xi < 0 || xi > 2) {
            emitPushInt(xi);
            reserve(1);
            popType();
            put1(134);
        } else {
            reserve(1);
            put1(xi + 11);
            if (xi == 0 && Float.floatToIntBits(x) != 0) {
                reserve(1);
                put1(118);
            }
        }
        pushType(Type.floatType);
    }

    public void emitPushDouble(double x) {
        int xi = (int) x;
        if (((double) xi) != x || xi < -128 || xi >= 128) {
            emitPushConstant(getConstants().addDouble(x));
        } else if (xi == 0 || xi == 1) {
            reserve(1);
            put1(xi + 14);
            if (xi == 0 && Double.doubleToLongBits(x) != 0) {
                reserve(1);
                put1(119);
            }
        } else {
            emitPushInt(xi);
            reserve(1);
            popType();
            put1(135);
        }
        pushType(Type.doubleType);
    }

    public static final String calculateSplit(String str) {
        int strLength = str.length();
        StringBuffer sbuf = new StringBuffer(20);
        int segmentStart = 0;
        int byteLength = 0;
        for (int i = 0; i < strLength; i++) {
            char ch = str.charAt(i);
            int bytes = ch >= 2048 ? 3 : (ch >= 128 || ch == 0) ? 2 : 1;
            if (byteLength + bytes > 65535) {
                sbuf.append((char) (i - segmentStart));
                segmentStart = i;
                byteLength = 0;
            }
            byteLength += bytes;
        }
        sbuf.append((char) (strLength - segmentStart));
        return sbuf.toString();
    }

    public final void emitPushString(String str) {
        if (str == null) {
            emitPushNull();
            return;
        }
        int length = str.length();
        String segments = calculateSplit(str);
        int numSegments = segments.length();
        if (numSegments <= 1) {
            emitPushConstant(getConstants().addString(str));
            pushType(Type.javalangStringType);
            return;
        }
        if (numSegments == 2) {
            int firstSegment = segments.charAt(0);
            emitPushString(str.substring(0, firstSegment));
            emitPushString(str.substring(firstSegment));
            emitInvokeVirtual(Type.javalangStringType.getDeclaredMethod("concat", 1));
        } else {
            ClassType sbufType = ClassType.make("java.lang.StringBuffer");
            emitNew(sbufType);
            emitDup((Type) sbufType);
            emitPushInt(length);
            emitInvokeSpecial(sbufType.getDeclaredMethod("<init>", new Type[]{Type.intType}));
            Method appendMethod = sbufType.getDeclaredMethod("append", new Type[]{Type.javalangStringType});
            int segStart = 0;
            for (int seg = 0; seg < numSegments; seg++) {
                emitDup((Type) sbufType);
                int segEnd = segStart + segments.charAt(seg);
                emitPushString(str.substring(segStart, segEnd));
                emitInvokeVirtual(appendMethod);
                segStart = segEnd;
            }
            emitInvokeVirtual(Type.toString_method);
        }
        if (str == str.intern()) {
            emitInvokeVirtual(Type.javalangStringType.getDeclaredMethod("intern", 0));
        }
    }

    public final void emitPushClass(ObjectType ctype) {
        emitPushConstant(getConstants().addClass(ctype));
        pushType(Type.javalangClassType);
    }

    public void emitPushNull() {
        reserve(1);
        put1(1);
        pushType(Type.nullType);
    }

    public void emitPushDefaultValue(Type type) {
        Type type2 = type.getImplementationType();
        if (type2 instanceof PrimType) {
            emitPushConstant(0, type2);
        } else {
            emitPushNull();
        }
    }

    public void emitStoreDefaultValue(Variable var) {
        emitPushDefaultValue(var.getType());
        emitStore(var);
    }

    public final void emitPushThis() {
        emitLoad(this.locals.used[0]);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void emitPushPrimArray(java.lang.Object r17, gnu.bytecode.ArrayType r18) {
        /*
            r16 = this;
            gnu.bytecode.Type r4 = r18.getComponentType()
            int r7 = java.lang.reflect.Array.getLength(r17)
            r0 = r16
            r0.emitPushInt(r7)
            r0 = r16
            r0.emitNewArray((gnu.bytecode.Type) r4)
            java.lang.String r11 = r4.getSignature()
            r12 = 0
            char r10 = r11.charAt(r12)
            r6 = 0
        L_0x001c:
            if (r6 >= r7) goto L_0x00df
            r8 = 0
            r5 = 0
            r2 = 0
            switch(r10) {
                case 66: goto L_0x007c;
                case 67: goto L_0x006c;
                case 68: goto L_0x00b2;
                case 70: goto L_0x00a2;
                case 73: goto L_0x004c;
                case 74: goto L_0x003d;
                case 83: goto L_0x005c;
                case 90: goto L_0x008c;
                default: goto L_0x0026;
            }
        L_0x0026:
            r0 = r16
            r1 = r18
            r0.emitDup((gnu.bytecode.Type) r1)
            r0 = r16
            r0.emitPushInt(r6)
            switch(r10) {
                case 66: goto L_0x00c2;
                case 67: goto L_0x00c2;
                case 68: goto L_0x00d8;
                case 70: goto L_0x00d1;
                case 73: goto L_0x00c2;
                case 74: goto L_0x00ca;
                case 83: goto L_0x00c2;
                case 90: goto L_0x00c2;
                default: goto L_0x0035;
            }
        L_0x0035:
            r0 = r16
            r0.emitArrayStore(r4)
        L_0x003a:
            int r6 = r6 + 1
            goto L_0x001c
        L_0x003d:
            r11 = r17
            long[] r11 = (long[]) r11
            long[] r11 = (long[]) r11
            r8 = r11[r6]
            r12 = 0
            int r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x004c:
            r11 = r17
            int[] r11 = (int[]) r11
            int[] r11 = (int[]) r11
            r11 = r11[r6]
            long r8 = (long) r11
            r12 = 0
            int r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x005c:
            r11 = r17
            short[] r11 = (short[]) r11
            short[] r11 = (short[]) r11
            short r11 = r11[r6]
            long r8 = (long) r11
            r12 = 0
            int r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x006c:
            r11 = r17
            char[] r11 = (char[]) r11
            char[] r11 = (char[]) r11
            char r11 = r11[r6]
            long r8 = (long) r11
            r12 = 0
            int r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x007c:
            r11 = r17
            byte[] r11 = (byte[]) r11
            byte[] r11 = (byte[]) r11
            byte r11 = r11[r6]
            long r8 = (long) r11
            r12 = 0
            int r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x008c:
            r11 = r17
            boolean[] r11 = (boolean[]) r11
            boolean[] r11 = (boolean[]) r11
            boolean r11 = r11[r6]
            if (r11 == 0) goto L_0x009f
            r8 = 1
        L_0x0098:
            r12 = 0
            int r11 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x009f:
            r8 = 0
            goto L_0x0098
        L_0x00a2:
            r11 = r17
            float[] r11 = (float[]) r11
            float[] r11 = (float[]) r11
            r5 = r11[r6]
            double r12 = (double) r5
            r14 = 0
            int r11 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x00b2:
            r11 = r17
            double[] r11 = (double[]) r11
            double[] r11 = (double[]) r11
            r2 = r11[r6]
            r12 = 0
            int r11 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1))
            if (r11 != 0) goto L_0x0026
            goto L_0x003a
        L_0x00c2:
            int r11 = (int) r8
            r0 = r16
            r0.emitPushInt(r11)
            goto L_0x0035
        L_0x00ca:
            r0 = r16
            r0.emitPushLong(r8)
            goto L_0x0035
        L_0x00d1:
            r0 = r16
            r0.emitPushFloat(r5)
            goto L_0x0035
        L_0x00d8:
            r0 = r16
            r0.emitPushDouble(r2)
            goto L_0x0035
        L_0x00df:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.bytecode.CodeAttr.emitPushPrimArray(java.lang.Object, gnu.bytecode.ArrayType):void");
    }

    /* access modifiers changed from: package-private */
    public void emitNewArray(int type_code) {
        reserve(2);
        put1(188);
        put1(type_code);
    }

    public final void emitArrayLength() {
        if (!(popType() instanceof ArrayType)) {
            throw new Error("non-array type in emitArrayLength");
        }
        reserve(1);
        put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SEEK);
        pushType(Type.intType);
    }

    private int adjustTypedOp(char sig) {
        switch (sig) {
            case 'B':
            case 'Z':
                return 5;
            case 'C':
                return 6;
            case 'D':
                return 3;
            case 'F':
                return 2;
            case 'I':
                return 0;
            case 'J':
                return 1;
            case 'S':
                return 7;
            default:
                return 4;
        }
    }

    private int adjustTypedOp(Type type) {
        return adjustTypedOp(type.getSignature().charAt(0));
    }

    private void emitTypedOp(int op, Type type) {
        reserve(1);
        put1(adjustTypedOp(type) + op);
    }

    private void emitTypedOp(int op, char sig) {
        reserve(1);
        put1(adjustTypedOp(sig) + op);
    }

    public void emitArrayStore(Type element_type) {
        popType();
        popType();
        popType();
        emitTypedOp(79, element_type);
    }

    public void emitArrayStore() {
        popType();
        popType();
        emitTypedOp(79, ((ArrayType) popType().getImplementationType()).getComponentType());
    }

    public void emitArrayLoad(Type element_type) {
        popType();
        popType();
        emitTypedOp(46, element_type);
        pushType(element_type);
    }

    public void emitArrayLoad() {
        popType();
        Type elementType = ((ArrayType) popType().getImplementationType()).getComponentType();
        emitTypedOp(46, elementType);
        pushType(elementType);
    }

    public void emitNew(ClassType type) {
        reserve(3);
        Label label = new Label(this);
        label.defineRaw(this);
        put1(187);
        putIndex2(getConstants().addClass((ObjectType) type));
        pushType(new UninitializedType(type, label));
    }

    public void emitNewArray(Type element_type, int dims) {
        int code2;
        if (popType().promote() != Type.intType) {
            throw new Error("non-int dim. spec. in emitNewArray");
        }
        if (element_type instanceof PrimType) {
            switch (element_type.getSignature().charAt(0)) {
                case 'B':
                    code2 = 8;
                    break;
                case 'C':
                    code2 = 5;
                    break;
                case 'D':
                    code2 = 7;
                    break;
                case 'F':
                    code2 = 6;
                    break;
                case 'I':
                    code2 = 10;
                    break;
                case 'J':
                    code2 = 11;
                    break;
                case 'S':
                    code2 = 9;
                    break;
                case 'Z':
                    code2 = 4;
                    break;
                default:
                    throw new Error("bad PrimType in emitNewArray");
            }
            emitNewArray(code2);
        } else if (element_type instanceof ObjectType) {
            reserve(3);
            put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_DIALOG_FLAG);
            putIndex2(getConstants().addClass((ObjectType) element_type));
        } else if (element_type instanceof ArrayType) {
            reserve(4);
            put1(197);
            putIndex2(getConstants().addClass((ObjectType) new ArrayType(element_type)));
            if (dims < 1 || dims > 255) {
                throw new Error("dims out of range in emitNewArray");
            }
            put1(dims);
            do {
                dims--;
                if (dims > 0) {
                }
            } while (popType().promote() == Type.intType);
            throw new Error("non-int dim. spec. in emitNewArray");
        } else {
            throw new Error("unimplemented type in emitNewArray");
        }
        pushType(new ArrayType(element_type));
    }

    public void emitNewArray(Type element_type) {
        emitNewArray(element_type, 1);
    }

    private void emitBinop(int base_code) {
        Type type2 = popType().promote();
        Type type1_raw = popType();
        Type type1 = type1_raw.promote();
        if (type1 != type2 || !(type1 instanceof PrimType)) {
            throw new Error("non-matching or bad types in binary operation");
        }
        emitTypedOp(base_code, type1);
        pushType(type1_raw);
    }

    private void emitBinop(int base_code, char sig) {
        popType();
        popType();
        emitTypedOp(base_code, sig);
        pushType(Type.signatureToPrimitive(sig));
    }

    public void emitBinop(int base_code, Type type) {
        popType();
        popType();
        emitTypedOp(base_code, type);
        pushType(type);
    }

    public final void emitAdd(char sig) {
        emitBinop(96, sig);
    }

    public final void emitAdd(PrimType type) {
        emitBinop(96, (Type) type);
    }

    public final void emitAdd() {
        emitBinop(96);
    }

    public final void emitSub(char sig) {
        emitBinop(100, sig);
    }

    public final void emitSub(PrimType type) {
        emitBinop(100, (Type) type);
    }

    public final void emitSub() {
        emitBinop(100);
    }

    public final void emitMul() {
        emitBinop(104);
    }

    public final void emitDiv() {
        emitBinop(108);
    }

    public final void emitRem() {
        emitBinop(112);
    }

    public final void emitAnd() {
        emitBinop(126);
    }

    public final void emitIOr() {
        emitBinop(128);
    }

    public final void emitXOr() {
        emitBinop(130);
    }

    public final void emitShl() {
        emitShift(120);
    }

    public final void emitShr() {
        emitShift(122);
    }

    public final void emitUshr() {
        emitShift(124);
    }

    private void emitShift(int base_code) {
        Type type2 = popType().promote();
        Type type1_raw = popType();
        Type type1 = type1_raw.promote();
        if (type1 != Type.intType && type1 != Type.longType) {
            throw new Error("the value shifted must be an int or a long");
        } else if (type2 != Type.intType) {
            throw new Error("the amount of shift must be an int");
        } else {
            emitTypedOp(base_code, type1);
            pushType(type1_raw);
        }
    }

    public final void emitNot(Type type) {
        emitPushConstant(1, type);
        emitAdd();
        emitPushConstant(1, type);
        emitAnd();
    }

    public void emitPrimop(int opcode, int arg_count, Type retType) {
        reserve(1);
        while (true) {
            arg_count--;
            if (arg_count >= 0) {
                popType();
            } else {
                put1(opcode);
                pushType(retType);
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void emitMaybeWide(int opcode, int index) {
        if (index >= 256) {
            put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_DURATION);
            put1(opcode);
            put2(index);
            return;
        }
        put1(opcode);
        put1(index);
    }

    public final void emitLoad(Variable var) {
        if (var.dead()) {
            throw new Error("attempting to push dead variable");
        }
        int offset = var.offset;
        if (offset < 0 || !var.isSimple()) {
            throw new Error("attempting to load from unassigned variable " + var + " simple:" + var.isSimple() + ", offset: " + offset);
        }
        Type type = var.getType().promote();
        reserve(4);
        int kind = adjustTypedOp(type);
        if (offset <= 3) {
            put1((kind * 4) + 26 + offset);
        } else {
            emitMaybeWide(kind + 21, offset);
        }
        pushType(var.getType());
    }

    public void emitStore(Variable var) {
        int offset = var.offset;
        if (offset < 0 || !var.isSimple()) {
            throw new Error("attempting to store in unassigned " + var + " simple:" + var.isSimple() + ", offset: " + offset);
        }
        Type type = var.getType().promote();
        noteVarType(offset, type);
        reserve(4);
        popType();
        int kind = adjustTypedOp(type);
        if (offset <= 3) {
            put1((kind * 4) + 59 + offset);
        } else {
            emitMaybeWide(kind + 54, offset);
        }
    }

    public void emitInc(Variable var, short inc) {
        if (var.dead()) {
            throw new Error("attempting to increment dead variable");
        }
        int offset = var.offset;
        if (offset < 0 || !var.isSimple()) {
            throw new Error("attempting to increment unassigned variable" + var.getName() + " simple:" + var.isSimple() + ", offset: " + offset);
        }
        reserve(6);
        if (var.getType().getImplementationType().promote() != Type.intType) {
            throw new Error("attempting to increment non-int variable");
        }
        if (offset > 255 || inc > 255 || inc < -256) {
            put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_DURATION);
            put1(132);
            put2(offset);
            put2(inc);
            return;
        }
        put1(132);
        put1(offset);
        put1(inc);
    }

    private final void emitFieldop(Field field, int opcode) {
        reserve(3);
        put1(opcode);
        putIndex2(getConstants().addFieldRef(field));
    }

    public final void emitGetStatic(Field field) {
        pushType(field.type);
        emitFieldop(field, 178);
    }

    public final void emitGetField(Field field) {
        popType();
        pushType(field.type);
        emitFieldop(field, 180);
    }

    public final void emitPutStatic(Field field) {
        popType();
        emitFieldop(field, 179);
    }

    public final void emitPutField(Field field) {
        popType();
        popType();
        emitFieldop(field, 181);
    }

    private int words(Type[] types) {
        int res = 0;
        int i = types.length;
        while (true) {
            i--;
            if (i < 0) {
                return res;
            }
            if (types[i].size > 4) {
                res += 2;
            } else {
                res++;
            }
        }
    }

    public void emitInvokeMethod(Method method, int opcode) {
        boolean is_invokestatic;
        boolean is_init;
        Type t;
        boolean z = true;
        reserve(opcode == 185 ? 5 : 3);
        int arg_count = method.arg_types.length;
        if (opcode == 184) {
            is_invokestatic = true;
        } else {
            is_invokestatic = false;
        }
        if (opcode != 183 || !"<init>".equals(method.getName())) {
            is_init = false;
        } else {
            is_init = true;
        }
        if ((method.access_flags & 8) == 0) {
            z = false;
        }
        if (is_invokestatic != z) {
            throw new Error("emitInvokeXxx static flag mis-match method.flags=" + method.access_flags);
        }
        if (!is_invokestatic && !is_init) {
            arg_count++;
        }
        put1(opcode);
        putIndex2(getConstants().addMethodRef(method));
        if (opcode == 185) {
            put1(words(method.arg_types) + 1);
            put1(0);
        }
        do {
            arg_count--;
            if (arg_count >= 0) {
                t = popType();
            } else {
                if (is_init) {
                    Type t2 = popType();
                    if (t2 instanceof UninitializedType) {
                        ClassType ctype = ((UninitializedType) t2).ctype;
                        for (int i = 0; i < this.SP; i++) {
                            if (this.stack_types[i] == t2) {
                                this.stack_types[i] = ctype;
                            }
                        }
                        Variable[] used = this.locals.used;
                        int i2 = used == null ? 0 : used.length;
                        while (true) {
                            i2--;
                            if (i2 < 0) {
                                break;
                            }
                            Variable var = used[i2];
                            if (var != null && var.type == t2) {
                                var.type = ctype;
                            }
                        }
                        int i3 = this.local_types == null ? 0 : this.local_types.length;
                        while (true) {
                            i3--;
                            if (i3 < 0) {
                                break;
                            } else if (this.local_types[i3] == t2) {
                                this.local_types[i3] = ctype;
                            }
                        }
                    } else {
                        throw new Error("calling <init> on already-initialized object");
                    }
                }
                if (method.return_type.size != 0) {
                    pushType(method.return_type);
                    return;
                }
                return;
            }
        } while (!(t instanceof UninitializedType));
        throw new Error("passing " + t + " as parameter");
    }

    public void emitInvoke(Method method) {
        int opcode;
        if ((method.access_flags & 8) != 0) {
            opcode = 184;
        } else if (method.classfile.isInterface()) {
            opcode = 185;
        } else if ("<init>".equals(method.getName())) {
            opcode = 183;
        } else {
            opcode = 182;
        }
        emitInvokeMethod(method, opcode);
    }

    public void emitInvokeVirtual(Method method) {
        emitInvokeMethod(method, 182);
    }

    public void emitInvokeSpecial(Method method) {
        emitInvokeMethod(method, 183);
    }

    public void emitInvokeStatic(Method method) {
        emitInvokeMethod(method, 184);
    }

    public void emitInvokeInterface(Method method) {
        emitInvokeMethod(method, 185);
    }

    /* access modifiers changed from: package-private */
    public final void emitTransfer(Label label, int opcode) {
        label.setTypes(this);
        fixupAdd(6, label);
        put1(opcode);
        this.PC += 2;
    }

    public final void emitGoto(Label label) {
        label.setTypes(this);
        fixupAdd(4, label);
        reserve(3);
        put1(167);
        this.PC += 2;
        setUnreachable();
    }

    public final void emitJsr(Label label) {
        fixupAdd(5, label);
        reserve(3);
        put1(168);
        this.PC += 2;
    }

    public ExitableBlock startExitableBlock(Type resultType, boolean runFinallyBlocks) {
        ExitableBlock bl = new ExitableBlock(resultType, this, runFinallyBlocks);
        bl.outer = this.currentExitableBlock;
        this.currentExitableBlock = bl;
        return bl;
    }

    public void endExitableBlock() {
        ExitableBlock bl = this.currentExitableBlock;
        bl.finish();
        this.currentExitableBlock = bl.outer;
    }

    public final void emitGotoIfCompare1(Label label, int opcode) {
        popType();
        reserve(3);
        emitTransfer(label, opcode);
    }

    public final void emitGotoIfIntEqZero(Label label) {
        emitGotoIfCompare1(label, 153);
    }

    public final void emitGotoIfIntNeZero(Label label) {
        emitGotoIfCompare1(label, 154);
    }

    public final void emitGotoIfIntLtZero(Label label) {
        emitGotoIfCompare1(label, 155);
    }

    public final void emitGotoIfIntGeZero(Label label) {
        emitGotoIfCompare1(label, 156);
    }

    public final void emitGotoIfIntGtZero(Label label) {
        emitGotoIfCompare1(label, 157);
    }

    public final void emitGotoIfIntLeZero(Label label) {
        emitGotoIfCompare1(label, 158);
    }

    public final void emitGotoIfCompare2(Label label, int logop) {
        boolean cmpg = false;
        if (logop < 153 || logop > 158) {
            throw new Error("emitGotoIfCompare2: logop must be one of ifeq...ifle");
        }
        Type type2 = popType().promote();
        Type type1 = popType().promote();
        reserve(4);
        char sig1 = type1.getSignature().charAt(0);
        char sig2 = type2.getSignature().charAt(0);
        if (logop == 155 || logop == 158) {
            cmpg = true;
        }
        if (sig1 == 'I' && sig2 == 'I') {
            logop += 6;
        } else if (sig1 == 'J' && sig2 == 'J') {
            put1(148);
        } else if (sig1 == 'F' && sig2 == 'F') {
            put1(cmpg ? 149 : 150);
        } else if (sig1 == 'D' && sig2 == 'D') {
            put1(cmpg ? 151 : 152);
        } else if ((sig1 == 'L' || sig1 == '[') && ((sig2 == 'L' || sig2 == '[') && logop <= 154)) {
            logop += 12;
        } else {
            throw new Error("invalid types to emitGotoIfCompare2");
        }
        emitTransfer(label, logop);
    }

    public final void emitGotoIfEq(Label label, boolean invert) {
        emitGotoIfCompare2(label, invert ? 154 : 153);
    }

    public final void emitGotoIfEq(Label label) {
        emitGotoIfCompare2(label, 153);
    }

    public final void emitGotoIfNE(Label label) {
        emitGotoIfCompare2(label, 154);
    }

    public final void emitGotoIfLt(Label label) {
        emitGotoIfCompare2(label, 155);
    }

    public final void emitGotoIfGe(Label label) {
        emitGotoIfCompare2(label, 156);
    }

    public final void emitGotoIfGt(Label label) {
        emitGotoIfCompare2(label, 157);
    }

    public final void emitGotoIfLe(Label label) {
        emitGotoIfCompare2(label, 158);
    }

    public final void emitIfCompare1(int opcode) {
        IfState new_if = new IfState(this);
        if (popType().promote() != Type.intType) {
            throw new Error("non-int type to emitIfCompare1");
        }
        reserve(3);
        emitTransfer(new_if.end_label, opcode);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfIntNotZero() {
        emitIfCompare1(153);
    }

    public final void emitIfIntEqZero() {
        emitIfCompare1(154);
    }

    public final void emitIfIntLEqZero() {
        emitIfCompare1(157);
    }

    public final void emitIfRefCompare1(int opcode) {
        IfState new_if = new IfState(this);
        if (!(popType() instanceof ObjectType)) {
            throw new Error("non-ref type to emitIfRefCompare1");
        }
        reserve(3);
        emitTransfer(new_if.end_label, opcode);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfNotNull() {
        emitIfRefCompare1(198);
    }

    public final void emitIfNull() {
        emitIfRefCompare1(199);
    }

    public final void emitIfIntCompare(int opcode) {
        IfState new_if = new IfState(this);
        popType();
        popType();
        reserve(3);
        emitTransfer(new_if.end_label, opcode);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfIntLt() {
        emitIfIntCompare(162);
    }

    public final void emitIfNEq() {
        IfState new_if = new IfState(this);
        emitGotoIfEq(new_if.end_label);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfEq() {
        IfState new_if = new IfState(this);
        emitGotoIfNE(new_if.end_label);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfLt() {
        IfState new_if = new IfState(this);
        emitGotoIfGe(new_if.end_label);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfGe() {
        IfState new_if = new IfState(this);
        emitGotoIfLt(new_if.end_label);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfGt() {
        IfState new_if = new IfState(this);
        emitGotoIfLe(new_if.end_label);
        new_if.start_stack_size = this.SP;
    }

    public final void emitIfLe() {
        IfState new_if = new IfState(this);
        emitGotoIfGt(new_if.end_label);
        new_if.start_stack_size = this.SP;
    }

    public void emitRet(Variable var) {
        int offset = var.offset;
        if (offset < 256) {
            reserve(2);
            put1(169);
            put1(offset);
            return;
        }
        reserve(4);
        put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_DURATION);
        put1(169);
        put2(offset);
    }

    public final void emitThen() {
        this.if_stack.start_stack_size = this.SP;
    }

    public final void emitIfThen() {
        new IfState(this, (Label) null);
    }

    public final void emitElse() {
        Label else_label = this.if_stack.end_label;
        if (reachableHere()) {
            Label end_label = new Label(this);
            this.if_stack.end_label = end_label;
            int growth = this.SP - this.if_stack.start_stack_size;
            this.if_stack.stack_growth = growth;
            if (growth > 0) {
                this.if_stack.then_stacked_types = new Type[growth];
                System.arraycopy(this.stack_types, this.if_stack.start_stack_size, this.if_stack.then_stacked_types, 0, growth);
            } else {
                this.if_stack.then_stacked_types = new Type[0];
            }
            emitGoto(end_label);
        } else {
            this.if_stack.end_label = null;
        }
        while (this.SP > this.if_stack.start_stack_size) {
            popType();
        }
        this.SP = this.if_stack.start_stack_size;
        if (else_label != null) {
            else_label.define(this);
        }
        this.if_stack.doing_else = true;
    }

    public final void emitFi() {
        boolean make_unreachable = false;
        if (!this.if_stack.doing_else) {
            if (reachableHere() && this.SP != this.if_stack.start_stack_size) {
                throw new Error("at PC " + this.PC + " then clause grows stack with no else clause");
            }
        } else if (this.if_stack.then_stacked_types != null) {
            int then_clause_stack_size = this.if_stack.start_stack_size + this.if_stack.stack_growth;
            if (!reachableHere()) {
                if (this.if_stack.stack_growth > 0) {
                    System.arraycopy(this.if_stack.then_stacked_types, 0, this.stack_types, this.if_stack.start_stack_size, this.if_stack.stack_growth);
                }
                this.SP = then_clause_stack_size;
            } else if (this.SP != then_clause_stack_size) {
                throw new Error("at PC " + this.PC + ": SP at end of 'then' was " + then_clause_stack_size + " while SP at end of 'else' was " + this.SP);
            }
        } else if (this.unreachable_here) {
            make_unreachable = true;
        }
        if (this.if_stack.end_label != null) {
            this.if_stack.end_label.define(this);
        }
        if (make_unreachable) {
            setUnreachable();
        }
        this.if_stack = this.if_stack.previous;
    }

    public final void emitConvert(Type from, Type to) {
        String to_sig = to.getSignature();
        String from_sig = from.getSignature();
        int op = -1;
        if (to_sig.length() == 1 || from_sig.length() == 1) {
            char to_sig0 = to_sig.charAt(0);
            char from_sig0 = from_sig.charAt(0);
            if (from_sig0 != to_sig0) {
                if (from.size < 4) {
                    from_sig0 = Access.INNERCLASS_CONTEXT;
                }
                if (to.size < 4) {
                    emitConvert(from, Type.intType);
                    from_sig0 = Access.INNERCLASS_CONTEXT;
                }
                if (from_sig0 != to_sig0) {
                    switch (from_sig0) {
                        case 'D':
                            switch (to_sig0) {
                                case 'F':
                                    op = 144;
                                    break;
                                case 'I':
                                    op = 142;
                                    break;
                                case 'J':
                                    op = 143;
                                    break;
                            }
                        case 'F':
                            switch (to_sig0) {
                                case 'D':
                                    op = 141;
                                    break;
                                case 'I':
                                    op = 139;
                                    break;
                                case 'J':
                                    op = 140;
                                    break;
                            }
                        case 'I':
                            switch (to_sig0) {
                                case 'B':
                                    op = 145;
                                    break;
                                case 'C':
                                    op = 146;
                                    break;
                                case 'D':
                                    op = 135;
                                    break;
                                case 'F':
                                    op = 134;
                                    break;
                                case 'J':
                                    op = 133;
                                    break;
                                case 'S':
                                    op = 147;
                                    break;
                            }
                        case 'J':
                            switch (to_sig0) {
                                case 'D':
                                    op = 138;
                                    break;
                                case 'F':
                                    op = 137;
                                    break;
                                case 'I':
                                    op = 136;
                                    break;
                            }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        if (op < 0) {
            throw new Error("unsupported CodeAttr.emitConvert");
        }
        reserve(1);
        popType();
        put1(op);
        pushType(to);
    }

    private void emitCheckcast(Type type, int opcode) {
        reserve(3);
        popType();
        put1(opcode);
        if (type instanceof ObjectType) {
            putIndex2(getConstants().addClass((ObjectType) type));
            return;
        }
        throw new Error("unimplemented type " + type + " in emitCheckcast/emitInstanceof");
    }

    public static boolean castNeeded(Type top, Type required) {
        if (top instanceof UninitializedType) {
            top = ((UninitializedType) top).getImplementationType();
        }
        while (top != required) {
            if ((required instanceof ClassType) && (top instanceof ClassType) && ((ClassType) top).isSubclass((ClassType) required)) {
                return false;
            }
            if (!(required instanceof ArrayType) || !(top instanceof ArrayType)) {
                return true;
            }
            required = ((ArrayType) required).getComponentType();
            top = ((ArrayType) top).getComponentType();
        }
        return false;
    }

    public void emitCheckcast(Type type) {
        if (castNeeded(topType(), type)) {
            emitCheckcast(type, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE);
            pushType(type);
        }
    }

    public void emitInstanceof(Type type) {
        emitCheckcast(type, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_STOP);
        pushType(Type.booleanType);
    }

    public final void emitThrow() {
        popType();
        reserve(1);
        put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PLAY);
        setUnreachable();
    }

    public final void emitMonitorEnter() {
        popType();
        reserve(1);
        put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SOURCE);
    }

    public final void emitMonitorExit() {
        popType();
        reserve(1);
        put1(FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_FULLSCREEN);
    }

    public final void emitReturn() {
        if (this.try_stack != null) {
            new Error();
        }
        emitRawReturn();
    }

    /* access modifiers changed from: package-private */
    public final void emitRawReturn() {
        if (getMethod().getReturnType().size == 0) {
            reserve(1);
            put1(177);
        } else {
            emitTypedOp(172, popType().promote());
        }
        setUnreachable();
    }

    public void addHandler(int start_pc, int end_pc, int handler_pc, int catch_type) {
        int index = this.exception_table_length * 4;
        if (this.exception_table == null) {
            this.exception_table = new short[20];
        } else if (this.exception_table.length <= index) {
            short[] new_table = new short[(this.exception_table.length * 2)];
            System.arraycopy(this.exception_table, 0, new_table, 0, index);
            this.exception_table = new_table;
        }
        int index2 = index + 1;
        this.exception_table[index] = (short) start_pc;
        int index3 = index2 + 1;
        this.exception_table[index2] = (short) end_pc;
        int index4 = index3 + 1;
        this.exception_table[index3] = (short) handler_pc;
        int i = index4 + 1;
        this.exception_table[index4] = (short) catch_type;
        this.exception_table_length++;
    }

    public void addHandler(Label start_try, Label end_try, ClassType catch_type) {
        int catch_type_index;
        Type handler_class;
        ConstantPool constants = getConstants();
        if (catch_type == null) {
            catch_type_index = 0;
        } else {
            catch_type_index = constants.addClass((ObjectType) catch_type).index;
        }
        fixupAdd(11, start_try);
        fixupAdd(12, catch_type_index, end_try);
        Label handler = new Label();
        handler.localTypes = start_try.localTypes;
        handler.stackTypes = new Type[1];
        if (catch_type == null) {
            handler_class = Type.javalangThrowableType;
        } else {
            handler_class = catch_type;
        }
        handler.stackTypes[0] = handler_class;
        setTypes(handler);
        fixupAdd(13, 0, handler);
    }

    public void emitWithCleanupStart() {
        int savedSP = this.SP;
        this.SP = 0;
        emitTryStart(false, (Type) null);
        this.SP = savedSP;
    }

    public void emitWithCleanupCatch(Variable catchVar) {
        Type[] savedTypes;
        emitTryEnd();
        if (this.SP > 0) {
            savedTypes = new Type[this.SP];
            System.arraycopy(this.stack_types, 0, savedTypes, 0, this.SP);
            this.SP = 0;
        } else {
            savedTypes = null;
        }
        this.try_stack.savedTypes = savedTypes;
        this.try_stack.saved_result = catchVar;
        int i = this.SP;
        emitCatchStart(catchVar);
    }

    public void emitWithCleanupDone() {
        Variable catchVar = this.try_stack.saved_result;
        this.try_stack.saved_result = null;
        if (catchVar != null) {
            emitLoad(catchVar);
        }
        emitThrow();
        emitCatchEnd();
        Type[] savedTypes = this.try_stack.savedTypes;
        emitTryCatchEnd();
        if (savedTypes != null) {
            this.SP = savedTypes.length;
            if (this.SP >= this.stack_types.length) {
                this.stack_types = savedTypes;
            } else {
                System.arraycopy(savedTypes, 0, this.stack_types, 0, this.SP);
            }
        } else {
            this.SP = 0;
        }
    }

    public void emitTryStart(boolean has_finally, Type result_type) {
        Type[] startLocals;
        if (result_type != null && result_type.isVoid()) {
            result_type = null;
        }
        Variable[] savedStack = null;
        if (result_type != null || this.SP > 0) {
            pushScope();
        }
        if (this.SP > 0) {
            savedStack = new Variable[this.SP];
            int i = 0;
            while (this.SP > 0) {
                Variable var = addLocal(topType());
                emitStore(var);
                savedStack[i] = var;
                i++;
            }
        }
        TryState try_state = new TryState(this);
        try_state.savedStack = savedStack;
        int usedLocals = this.local_types == null ? 0 : this.local_types.length;
        while (usedLocals > 0 && this.local_types[usedLocals - 1] == null) {
            usedLocals--;
        }
        if (usedLocals == 0) {
            startLocals = Type.typeArray0;
        } else {
            startLocals = new Type[usedLocals];
            System.arraycopy(this.local_types, 0, startLocals, 0, usedLocals);
        }
        try_state.start_try.localTypes = startLocals;
        if (result_type != null) {
            try_state.saved_result = addLocal(result_type);
        }
        if (has_finally) {
            try_state.finally_subr = new Label();
        }
    }

    public void emitTryEnd() {
        emitTryEnd(false);
    }

    private void emitTryEnd(boolean fromFinally) {
        if (!this.try_stack.tryClauseDone) {
            this.try_stack.tryClauseDone = true;
            if (this.try_stack.finally_subr != null) {
                this.try_stack.exception = addLocal(Type.javalangThrowableType);
            }
            gotoFinallyOrEnd(fromFinally);
            this.try_stack.end_try = getLabel();
        }
    }

    public void emitCatchStart(Variable var) {
        emitTryEnd(false);
        setTypes(this.try_stack.start_try.localTypes, Type.typeArray0);
        if (this.try_stack.try_type != null) {
            emitCatchEnd();
        }
        ClassType type = var == null ? null : (ClassType) var.getType();
        this.try_stack.try_type = type;
        addHandler(this.try_stack.start_try, this.try_stack.end_try, type);
        if (var != null) {
            emitStore(var);
        }
    }

    public void emitCatchEnd() {
        gotoFinallyOrEnd(false);
        this.try_stack.try_type = null;
    }

    private void gotoFinallyOrEnd(boolean fromFinally) {
        if (reachableHere()) {
            if (this.try_stack.saved_result != null) {
                emitStore(this.try_stack.saved_result);
            }
            if (this.try_stack.end_label == null) {
                this.try_stack.end_label = new Label();
            }
            if (this.try_stack.finally_subr == null || useJsr()) {
                if (this.try_stack.finally_subr != null) {
                    emitJsr(this.try_stack.finally_subr);
                }
                emitGoto(this.try_stack.end_label);
                return;
            }
            if (this.try_stack.exitCases != null) {
                emitPushInt(0);
            }
            emitPushNull();
            if (!fromFinally) {
                emitGoto(this.try_stack.finally_subr);
            }
        }
    }

    public void emitFinallyStart() {
        emitTryEnd(true);
        if (this.try_stack.try_type != null) {
            emitCatchEnd();
        }
        this.try_stack.end_try = getLabel();
        pushScope();
        if (useJsr()) {
            this.SP = 0;
            emitCatchStart((Variable) null);
            emitStore(this.try_stack.exception);
            emitJsr(this.try_stack.finally_subr);
            emitLoad(this.try_stack.exception);
            emitThrow();
        } else {
            setUnreachable();
            int fragment_cookie = beginFragment(new Label(this));
            addHandler(this.try_stack.start_try, this.try_stack.end_try, Type.javalangThrowableType);
            if (this.try_stack.saved_result != null) {
                emitStoreDefaultValue(this.try_stack.saved_result);
            }
            if (this.try_stack.exitCases != null) {
                emitPushInt(-1);
                emitSwap();
            }
            emitGoto(this.try_stack.finally_subr);
            endFragment(fragment_cookie);
        }
        this.try_stack.finally_subr.define(this);
        if (useJsr()) {
            Type ret_addr_type = Type.objectType;
            this.try_stack.finally_ret_addr = addLocal(ret_addr_type);
            pushType(ret_addr_type);
            emitStore(this.try_stack.finally_ret_addr);
        }
    }

    public void emitFinallyEnd() {
        if (useJsr()) {
            emitRet(this.try_stack.finally_ret_addr);
        } else if (this.try_stack.end_label == null && this.try_stack.exitCases == null) {
            emitThrow();
        } else {
            emitStore(this.try_stack.exception);
            emitLoad(this.try_stack.exception);
            emitIfNotNull();
            emitLoad(this.try_stack.exception);
            emitThrow();
            emitElse();
            ExitableBlock exit = this.try_stack.exitCases;
            if (exit != null) {
                SwitchState sw = startSwitch();
                while (exit != null) {
                    ExitableBlock next = exit.nextCase;
                    exit.nextCase = null;
                    exit.currentTryState = null;
                    TryState nextTry = TryState.outerHandler(this.try_stack.previous, exit.initialTryState);
                    if (nextTry == exit.initialTryState) {
                        sw.addCaseGoto(exit.switchCase, this, exit.endLabel);
                    } else {
                        sw.addCase(exit.switchCase, this);
                        exit.exit(nextTry);
                    }
                    exit = next;
                }
                this.try_stack.exitCases = null;
                sw.addDefault(this);
                sw.finish(this);
            }
            emitFi();
            setUnreachable();
        }
        popScope();
        this.try_stack.finally_subr = null;
    }

    public void emitTryCatchEnd() {
        if (this.try_stack.finally_subr != null) {
            emitFinallyEnd();
        }
        Variable[] vars = this.try_stack.savedStack;
        if (this.try_stack.end_label == null) {
            setUnreachable();
        } else {
            setTypes(this.try_stack.start_try.localTypes, Type.typeArray0);
            this.try_stack.end_label.define(this);
            if (vars != null) {
                int i = vars.length;
                while (true) {
                    i--;
                    if (i < 0) {
                        break;
                    }
                    Variable v = vars[i];
                    if (v != null) {
                        emitLoad(v);
                    }
                }
            }
            if (this.try_stack.saved_result != null) {
                emitLoad(this.try_stack.saved_result);
            }
        }
        if (!(this.try_stack.saved_result == null && vars == null)) {
            popScope();
        }
        this.try_stack = this.try_stack.previous;
    }

    public final TryState getCurrentTry() {
        return this.try_stack;
    }

    public final boolean isInTry() {
        return this.try_stack != null;
    }

    public SwitchState startSwitch() {
        SwitchState sw = new SwitchState(this);
        sw.switchValuePushed(this);
        return sw;
    }

    public void emitTailCall(boolean pop_args, Scope scope) {
        int arg_slots;
        int i;
        int i2;
        if (pop_args) {
            Method meth = getMethod();
            if ((meth.access_flags & 8) != 0) {
                arg_slots = 0;
            } else {
                arg_slots = 1;
            }
            int i3 = meth.arg_types.length;
            while (true) {
                i3--;
                if (i3 < 0) {
                    break;
                }
                if (meth.arg_types[i3].size > 4) {
                    i2 = 2;
                } else {
                    i2 = 1;
                }
                arg_slots += i2;
            }
            int i4 = meth.arg_types.length;
            while (true) {
                i4--;
                if (i4 < 0) {
                    break;
                }
                if (meth.arg_types[i4].size > 4) {
                    i = 2;
                } else {
                    i = 1;
                }
                arg_slots -= i;
                emitStore(this.locals.used[arg_slots]);
            }
        }
        emitGoto(scope.start);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x02db, code lost:
        r25 = mergeLabels(r25, r10);
        r21 = r22;
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x02e7, code lost:
        r21 = r22 + 3;
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x02eb, code lost:
        r5 = r10.position - r14;
        r13 = r14 + 1;
        r12[r14] = r32.code[r22];
        r14 = r13 + 1;
        r12[r13] = (byte) (r5 >> 8);
        r13 = r14 + 1;
        r12[r14] = (byte) (r5 & 255);
        r21 = r22 + 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0318, code lost:
        r5 = r10.position - r14;
        r23 = r32.code[r22];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x032a, code lost:
        if (r9 != 6) goto L_0x037d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x032c, code lost:
        r13 = r14 + 1;
        r12[r14] = invert_opcode(r23);
        r14 = r13 + 1;
        r12[r13] = 0;
        r13 = r14 + 1;
        r12[r14] = 8;
        r23 = com.google.appinventor.components.runtime.util.Ev3Constants.Opcode.READ8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x0346, code lost:
        r14 = r13 + 1;
        r12[r13] = r23;
        r13 = r14 + 1;
        r12[r14] = (byte) (r5 >> 24);
        r14 = r13 + 1;
        r12[r13] = (byte) (r5 >> 16);
        r13 = r14 + 1;
        r12[r14] = (byte) (r5 >> 8);
        r12[r13] = (byte) (r5 & 255);
        r21 = r22 + 3;
        r13 = r13 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x037d, code lost:
        r23 = (byte) (r23 + com.google.appinventor.components.runtime.util.Ev3Constants.Opcode.OR16);
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0386, code lost:
        r24 = (3 - r14) & 3;
        r28 = r14;
        r13 = r14 + 1;
        r21 = r22 + 1;
        r12[r14] = r32.code[r22];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x039b, code lost:
        r14 = r13;
        r24 = r24 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x039d, code lost:
        if (r24 < 0) goto L_0x03e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x039f, code lost:
        r13 = r14 + 1;
        r12[r14] = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x03a7, code lost:
        r5 = r32.fixup_labels[r16].position - r28;
        r13 = r14 + 1;
        r12[r14] = (byte) (r5 >> 24);
        r14 = r13 + 1;
        r12[r13] = (byte) (r5 >> 16);
        r13 = r14 + 1;
        r12[r14] = (byte) (r5 >> 8);
        r14 = r13 + 1;
        r12[r13] = (byte) (r5 & 255);
        r21 = r22 + 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x03e8, code lost:
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x03f2, code lost:
        if (r16 >= r32.fixup_count) goto L_0x02b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x0404, code lost:
        if (fixupKind(r16 + 1) != 3) goto L_0x02b6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0406, code lost:
        r16 = r16 + 1;
        r19 = fixupOffset(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x0413, code lost:
        r22 = r21;
        r14 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x0417, code lost:
        if (r22 >= r19) goto L_0x03a7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x0419, code lost:
        r13 = r14 + 1;
        r21 = r22 + 1;
        r12[r14] = r32.code[r22];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x042b, code lost:
        r10 = r32.fixup_labels[r16 + 2];
        r6 = fixupOffset(r16 + 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x0445, code lost:
        if (r32.stackMap == null) goto L_0x044f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x0447, code lost:
        r25 = mergeLabels(r25, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x044f, code lost:
        addHandler(r32.fixup_labels[r16].position, r32.fixup_labels[r16 + 1].position, r14, r6);
        r16 = r16 + 2;
        r21 = r22;
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x0483, code lost:
        if (r32.lines != null) goto L_0x0494;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x0485, code lost:
        r32.lines = new gnu.bytecode.LineNumbersAttr(r32);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x0494, code lost:
        r16 = r16 + 1;
        r11 = fixupOffset(r16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x04a0, code lost:
        if (r11 == r26) goto L_0x04ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x04a2, code lost:
        r32.lines.put(r11, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x04ad, code lost:
        r26 = r11;
        r21 = r22;
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x04b4, code lost:
        if (r10 != null) goto L_0x04df;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x04b6, code lost:
        if (r15 == r14) goto L_0x04fd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x04de, code lost:
        throw new java.lang.Error("PC confusion new_pc:" + r14 + " new_size:" + r15);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x04df, code lost:
        r16 = r10.first_fixup;
        r21 = fixupOffset(r16);
        r17 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x04f3, code lost:
        if (r10.position == r14) goto L_0x051f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x04fc, code lost:
        throw new java.lang.Error("bad pc");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:140:0x04fd, code lost:
        r32.PC = r15;
        r32.code = r12;
        r32.fixup_count = 0;
        r32.fixup_labels = null;
        r32.fixup_offsets = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:141:0x051f, code lost:
        r22 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0526, code lost:
        r21 = r22;
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00d0, code lost:
        if ((r7 + 1) < r32.fixup_count) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00d2, code lost:
        r4 = r32.PC;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00d6, code lost:
        r32.fixup_offsets[r7] = (r4 << 4) | 9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00e2, code lost:
        if (r10 != null) goto L_0x0142;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e4, code lost:
        r15 = r32.PC;
        r5 = 0;
        r7 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00f2, code lost:
        if (r7 >= r32.fixup_count) goto L_0x0225;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00f4, code lost:
        r19 = r32.fixup_offsets[r7];
        r9 = r19 & 15;
        r10 = r32.fixup_labels[r7];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0106, code lost:
        if (r10 == null) goto L_0x0150;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x010c, code lost:
        if (r10.position >= 0) goto L_0x0150;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0128, code lost:
        throw new java.lang.Error("undefined label " + r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0129, code lost:
        r4 = fixupOffset(r32.fixup_labels[r7 + 1].first_fixup);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0142, code lost:
        r7 = r10.first_fixup;
        r5 = (r4 + r5) - fixupOffset(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0150, code lost:
        if (r10 == null) goto L_0x01ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0156, code lost:
        if (r9 < 4) goto L_0x01ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x015c, code lost:
        if (r9 > 7) goto L_0x01ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x016e, code lost:
        if ((r10.first_fixup + 1) >= r32.fixup_count) goto L_0x01ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0192, code lost:
        if (r32.fixup_offsets[r10.first_fixup + 1] != ((r32.fixup_offsets[r10.first_fixup] & 15) | 4)) goto L_0x01ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0194, code lost:
        r10 = r32.fixup_labels[r10.first_fixup + 1];
        r32.fixup_labels[r7] = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x01ab, code lost:
        r19 = r19 >> 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x01ad, code lost:
        switch(r9) {
            case 0: goto L_0x01ca;
            case 1: goto L_0x01d6;
            case 2: goto L_0x01dd;
            case 3: goto L_0x01ca;
            case 4: goto L_0x01e8;
            case 5: goto L_0x01e8;
            case 6: goto L_0x01e8;
            case 7: goto L_0x01b0;
            case 8: goto L_0x01d1;
            case 9: goto L_0x0223;
            case 10: goto L_0x01b0;
            case 11: goto L_0x01b8;
            case 12: goto L_0x01b0;
            case 13: goto L_0x01b0;
            case 14: goto L_0x01ce;
            default: goto L_0x01b0;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x01b7, code lost:
        throw new java.lang.Error("unexpected fixup");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x01b8, code lost:
        r7 = r7 + 2;
        r32.fixup_labels[r7].position = r19 + r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x01ca, code lost:
        r7 = r7 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x01ce, code lost:
        r7 = r7 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x01d1, code lost:
        r5 = r5 - 3;
        r15 = r15 - 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x01d6, code lost:
        r10.position = r19 + r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x01dd, code lost:
        r24 = (3 - (r19 + r5)) & 3;
        r5 = r5 + r24;
        r15 = r15 + r24;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x01e8, code lost:
        r27 = r10.position - (r19 + r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x01f9, code lost:
        if (((short) r27) != r27) goto L_0x0208;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x01fb, code lost:
        r32.fixup_offsets[r7] = (r19 << 4) | 7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x020c, code lost:
        if (r9 != 6) goto L_0x021d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x020e, code lost:
        r29 = 5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0210, code lost:
        r5 = r5 + r29;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0216, code lost:
        if (r9 != 6) goto L_0x0220;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0218, code lost:
        r29 = 5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x021a, code lost:
        r15 = r15 + r29;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x021d, code lost:
        r29 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0220, code lost:
        r29 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0223, code lost:
        if (r10 != null) goto L_0x0257;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0225, code lost:
        r12 = new byte[r15];
        r26 = -1;
        r16 = 0;
        r17 = fixupOffset(0);
        r25 = null;
        r22 = 0;
        r14 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0243, code lost:
        if (r22 >= r17) goto L_0x0265;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0245, code lost:
        r12[r14] = r32.code[r22];
        r22 = r22 + 1;
        r14 = r14 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0257, code lost:
        r7 = r10.first_fixup;
        r5 = (r19 + r5) - fixupOffset(r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0265, code lost:
        r9 = r32.fixup_offsets[r16] & 15;
        r10 = r32.fixup_labels[r16];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0277, code lost:
        if (r25 == null) goto L_0x0294;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0281, code lost:
        if (r25.position >= r14) goto L_0x0294;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0283, code lost:
        r32.stackMap.emitStackMapEntry(r25, r32);
        r25 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0294, code lost:
        if (r25 == null) goto L_0x02a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x029e, code lost:
        if (r25.position <= r14) goto L_0x02a8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x02a7, code lost:
        throw new java.lang.Error("labels out of order");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x02a8, code lost:
        switch(r9) {
            case 0: goto L_0x02b3;
            case 1: goto L_0x02c5;
            case 2: goto L_0x0386;
            case 3: goto L_0x02ab;
            case 4: goto L_0x0318;
            case 5: goto L_0x0318;
            case 6: goto L_0x0318;
            case 7: goto L_0x02eb;
            case 8: goto L_0x02e7;
            case 9: goto L_0x04b4;
            case 10: goto L_0x02ab;
            case 11: goto L_0x042b;
            case 12: goto L_0x02ab;
            case 13: goto L_0x02ab;
            case 14: goto L_0x047d;
            default: goto L_0x02ab;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0041, code lost:
        r7 = r7 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x02b2, code lost:
        throw new java.lang.Error("unexpected fixup");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x02b3, code lost:
        r21 = r22;
        r13 = r14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x02b6, code lost:
        r16 = r16 + 1;
        r17 = fixupOffset(r16);
        r22 = r21;
        r14 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x02cb, code lost:
        if (r32.stackMap == null) goto L_0x0526;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x02cd, code lost:
        if (r10 == null) goto L_0x0526;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x02d3, code lost:
        if (r10.stackTypes == null) goto L_0x0526;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x02d9, code lost:
        if (r10.needsStackMapEntry == false) goto L_0x0526;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void processFixups() {
        /*
            r32 = this;
            r0 = r32
            int r0 = r0.fixup_count
            r29 = r0
            if (r29 > 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            r5 = 0
            r0 = r32
            int r8 = r0.fixup_count
            r29 = 9
            r30 = 0
            r31 = 0
            r0 = r32
            r1 = r29
            r2 = r30
            r3 = r31
            r0.fixupAdd(r1, r2, r3)
            r7 = 0
        L_0x0020:
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r29 = r0
            r19 = r29[r7]
            r9 = r19 & 15
            int r19 = r19 >> 4
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r10 = r29[r7]
            switch(r9) {
                case 0: goto L_0x0041;
                case 1: goto L_0x0047;
                case 2: goto L_0x0052;
                case 3: goto L_0x0041;
                case 4: goto L_0x0055;
                case 5: goto L_0x008c;
                case 6: goto L_0x009e;
                case 7: goto L_0x0037;
                case 8: goto L_0x0041;
                case 9: goto L_0x00c4;
                case 10: goto L_0x00b0;
                case 11: goto L_0x003f;
                case 12: goto L_0x0037;
                case 13: goto L_0x0037;
                case 14: goto L_0x0044;
                default: goto L_0x0037;
            }
        L_0x0037:
            java.lang.Error r29 = new java.lang.Error
            java.lang.String r30 = "unexpected fixup"
            r29.<init>(r30)
            throw r29
        L_0x003f:
            int r7 = r7 + 2
        L_0x0041:
            int r7 = r7 + 1
            goto L_0x0020
        L_0x0044:
            int r7 = r7 + 1
            goto L_0x0041
        L_0x0047:
            int r0 = r10.position
            r29 = r0
            int r29 = r29 + r5
            r0 = r29
            r10.position = r0
            goto L_0x0041
        L_0x0052:
            int r5 = r5 + 3
            goto L_0x0041
        L_0x0055:
            int r0 = r10.first_fixup
            r29 = r0
            int r30 = r7 + 1
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x008c
            int r29 = r7 + 1
            r0 = r32
            r1 = r29
            int r29 = r0.fixupOffset(r1)
            int r30 = r19 + 3
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x008c
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r29 = r0
            int r30 = r19 << 4
            r30 = r30 | 8
            r29[r7] = r30
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r30 = 0
            r29[r7] = r30
            int r5 = r5 + -3
            goto L_0x0041
        L_0x008c:
            r0 = r32
            int r0 = r0.PC
            r29 = r0
            r30 = 32768(0x8000, float:4.5918E-41)
            r0 = r29
            r1 = r30
            if (r0 < r1) goto L_0x0041
            int r5 = r5 + 2
            goto L_0x0041
        L_0x009e:
            r0 = r32
            int r0 = r0.PC
            r29 = r0
            r30 = 32768(0x8000, float:4.5918E-41)
            r0 = r29
            r1 = r30
            if (r0 < r1) goto L_0x0041
            int r5 = r5 + 5
            goto L_0x0041
        L_0x00b0:
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r30 = r0
            int r31 = r7 + 1
            r30 = r30[r31]
            r29[r8] = r30
            r8 = r19
        L_0x00c4:
            int r29 = r7 + 1
            r0 = r32
            int r0 = r0.fixup_count
            r30 = r0
            r0 = r29
            r1 = r30
            if (r0 < r1) goto L_0x0129
            r0 = r32
            int r4 = r0.PC
        L_0x00d6:
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r29 = r0
            int r30 = r4 << 4
            r30 = r30 | 9
            r29[r7] = r30
            if (r10 != 0) goto L_0x0142
            r0 = r32
            int r15 = r0.PC
            r5 = 0
            r7 = 0
        L_0x00ea:
            r0 = r32
            int r0 = r0.fixup_count
            r29 = r0
            r0 = r29
            if (r7 >= r0) goto L_0x0225
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r29 = r0
            r19 = r29[r7]
            r9 = r19 & 15
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r10 = r29[r7]
            if (r10 == 0) goto L_0x0150
            int r0 = r10.position
            r29 = r0
            if (r29 >= 0) goto L_0x0150
            java.lang.Error r29 = new java.lang.Error
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            r30.<init>()
            java.lang.String r31 = "undefined label "
            java.lang.StringBuilder r30 = r30.append(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r10)
            java.lang.String r30 = r30.toString()
            r29.<init>(r30)
            throw r29
        L_0x0129:
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            int r30 = r7 + 1
            r29 = r29[r30]
            r0 = r29
            int r0 = r0.first_fixup
            r29 = r0
            r0 = r32
            r1 = r29
            int r4 = r0.fixupOffset(r1)
            goto L_0x00d6
        L_0x0142:
            int r7 = r10.first_fixup
            r0 = r32
            int r18 = r0.fixupOffset(r7)
            int r29 = r4 + r5
            int r5 = r29 - r18
            goto L_0x0020
        L_0x0150:
            if (r10 == 0) goto L_0x01ab
            r29 = 4
            r0 = r29
            if (r9 < r0) goto L_0x01ab
            r29 = 7
            r0 = r29
            if (r9 > r0) goto L_0x01ab
            int r0 = r10.first_fixup
            r29 = r0
            int r29 = r29 + 1
            r0 = r32
            int r0 = r0.fixup_count
            r30 = r0
            r0 = r29
            r1 = r30
            if (r0 >= r1) goto L_0x01ab
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r29 = r0
            int r0 = r10.first_fixup
            r30 = r0
            int r30 = r30 + 1
            r29 = r29[r30]
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r30 = r0
            int r0 = r10.first_fixup
            r31 = r0
            r30 = r30[r31]
            r30 = r30 & 15
            r30 = r30 | 4
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x01ab
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            int r0 = r10.first_fixup
            r30 = r0
            int r30 = r30 + 1
            r10 = r29[r30]
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r29[r7] = r10
            goto L_0x0150
        L_0x01ab:
            int r19 = r19 >> 4
            switch(r9) {
                case 0: goto L_0x01ca;
                case 1: goto L_0x01d6;
                case 2: goto L_0x01dd;
                case 3: goto L_0x01ca;
                case 4: goto L_0x01e8;
                case 5: goto L_0x01e8;
                case 6: goto L_0x01e8;
                case 7: goto L_0x01b0;
                case 8: goto L_0x01d1;
                case 9: goto L_0x0223;
                case 10: goto L_0x01b0;
                case 11: goto L_0x01b8;
                case 12: goto L_0x01b0;
                case 13: goto L_0x01b0;
                case 14: goto L_0x01ce;
                default: goto L_0x01b0;
            }
        L_0x01b0:
            java.lang.Error r29 = new java.lang.Error
            java.lang.String r30 = "unexpected fixup"
            r29.<init>(r30)
            throw r29
        L_0x01b8:
            int r7 = r7 + 2
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r29 = r29[r7]
            int r30 = r19 + r5
            r0 = r30
            r1 = r29
            r1.position = r0
        L_0x01ca:
            int r7 = r7 + 1
            goto L_0x00ea
        L_0x01ce:
            int r7 = r7 + 1
            goto L_0x01ca
        L_0x01d1:
            int r5 = r5 + -3
            int r15 = r15 + -3
            goto L_0x01ca
        L_0x01d6:
            int r29 = r19 + r5
            r0 = r29
            r10.position = r0
            goto L_0x01ca
        L_0x01dd:
            int r29 = r19 + r5
            int r29 = 3 - r29
            r24 = r29 & 3
            int r5 = r5 + r24
            int r15 = r15 + r24
            goto L_0x01ca
        L_0x01e8:
            int r0 = r10.position
            r29 = r0
            int r30 = r19 + r5
            int r27 = r29 - r30
            r0 = r27
            short r0 = (short) r0
            r29 = r0
            r0 = r29
            r1 = r27
            if (r0 != r1) goto L_0x0208
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r29 = r0
            int r30 = r19 << 4
            r30 = r30 | 7
            r29[r7] = r30
            goto L_0x01ca
        L_0x0208:
            r29 = 6
            r0 = r29
            if (r9 != r0) goto L_0x021d
            r29 = 5
        L_0x0210:
            int r5 = r5 + r29
            r29 = 6
            r0 = r29
            if (r9 != r0) goto L_0x0220
            r29 = 5
        L_0x021a:
            int r15 = r15 + r29
            goto L_0x01ca
        L_0x021d:
            r29 = 2
            goto L_0x0210
        L_0x0220:
            r29 = 2
            goto L_0x021a
        L_0x0223:
            if (r10 != 0) goto L_0x0257
        L_0x0225:
            byte[] r12 = new byte[r15]
            r26 = -1
            r13 = 0
            r16 = 0
            r29 = 0
            r0 = r32
            r1 = r29
            int r17 = r0.fixupOffset(r1)
            r20 = -1
            r25 = 0
            r21 = 0
            r22 = r21
            r14 = r13
        L_0x023f:
            r0 = r22
            r1 = r17
            if (r0 >= r1) goto L_0x0265
            int r13 = r14 + 1
            r0 = r32
            byte[] r0 = r0.code
            r29 = r0
            int r21 = r22 + 1
            byte r29 = r29[r22]
            r12[r14] = r29
            r22 = r21
            r14 = r13
            goto L_0x023f
        L_0x0257:
            int r7 = r10.first_fixup
            r0 = r32
            int r18 = r0.fixupOffset(r7)
            int r29 = r19 + r5
            int r5 = r29 - r18
            goto L_0x00ea
        L_0x0265:
            r0 = r32
            int[] r0 = r0.fixup_offsets
            r29 = r0
            r29 = r29[r16]
            r9 = r29 & 15
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r10 = r29[r16]
            if (r25 == 0) goto L_0x0294
            r0 = r25
            int r0 = r0.position
            r29 = r0
            r0 = r29
            if (r0 >= r14) goto L_0x0294
            r0 = r32
            gnu.bytecode.StackMapTableAttr r0 = r0.stackMap
            r29 = r0
            r0 = r29
            r1 = r25
            r2 = r32
            r0.emitStackMapEntry(r1, r2)
            r25 = 0
        L_0x0294:
            if (r25 == 0) goto L_0x02a8
            r0 = r25
            int r0 = r0.position
            r29 = r0
            r0 = r29
            if (r0 <= r14) goto L_0x02a8
            java.lang.Error r29 = new java.lang.Error
            java.lang.String r30 = "labels out of order"
            r29.<init>(r30)
            throw r29
        L_0x02a8:
            switch(r9) {
                case 0: goto L_0x02b3;
                case 1: goto L_0x02c5;
                case 2: goto L_0x0386;
                case 3: goto L_0x02ab;
                case 4: goto L_0x0318;
                case 5: goto L_0x0318;
                case 6: goto L_0x0318;
                case 7: goto L_0x02eb;
                case 8: goto L_0x02e7;
                case 9: goto L_0x04b4;
                case 10: goto L_0x02ab;
                case 11: goto L_0x042b;
                case 12: goto L_0x02ab;
                case 13: goto L_0x02ab;
                case 14: goto L_0x047d;
                default: goto L_0x02ab;
            }
        L_0x02ab:
            java.lang.Error r29 = new java.lang.Error
            java.lang.String r30 = "unexpected fixup"
            r29.<init>(r30)
            throw r29
        L_0x02b3:
            r21 = r22
            r13 = r14
        L_0x02b6:
            int r16 = r16 + 1
            r0 = r32
            r1 = r16
            int r17 = r0.fixupOffset(r1)
            r22 = r21
            r14 = r13
            goto L_0x023f
        L_0x02c5:
            r0 = r32
            gnu.bytecode.StackMapTableAttr r0 = r0.stackMap
            r29 = r0
            if (r29 == 0) goto L_0x0526
            if (r10 == 0) goto L_0x0526
            gnu.bytecode.Type[] r0 = r10.stackTypes
            r29 = r0
            if (r29 == 0) goto L_0x0526
            boolean r0 = r10.needsStackMapEntry
            r29 = r0
            if (r29 == 0) goto L_0x0526
            r0 = r32
            r1 = r25
            gnu.bytecode.Label r25 = r0.mergeLabels(r1, r10)
            r21 = r22
            r13 = r14
            goto L_0x02b6
        L_0x02e7:
            int r21 = r22 + 3
            r13 = r14
            goto L_0x02b6
        L_0x02eb:
            int r0 = r10.position
            r29 = r0
            int r5 = r29 - r14
            int r13 = r14 + 1
            r0 = r32
            byte[] r0 = r0.code
            r29 = r0
            byte r29 = r29[r22]
            r12[r14] = r29
            int r14 = r13 + 1
            int r29 = r5 >> 8
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r13] = r29
            int r13 = r14 + 1
            r0 = r5 & 255(0xff, float:3.57E-43)
            r29 = r0
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r14] = r29
            int r21 = r22 + 3
            goto L_0x02b6
        L_0x0318:
            int r0 = r10.position
            r29 = r0
            int r5 = r29 - r14
            r0 = r32
            byte[] r0 = r0.code
            r29 = r0
            byte r23 = r29[r22]
            r29 = 6
            r0 = r29
            if (r9 != r0) goto L_0x037d
            r0 = r32
            r1 = r23
            byte r23 = r0.invert_opcode(r1)
            int r13 = r14 + 1
            r12[r14] = r23
            int r14 = r13 + 1
            r29 = 0
            r12[r13] = r29
            int r13 = r14 + 1
            r29 = 8
            r12[r14] = r29
            r23 = -56
        L_0x0346:
            int r14 = r13 + 1
            r12[r13] = r23
            int r13 = r14 + 1
            int r29 = r5 >> 24
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r14] = r29
            int r14 = r13 + 1
            int r29 = r5 >> 16
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r13] = r29
            int r13 = r14 + 1
            int r29 = r5 >> 8
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r14] = r29
            int r14 = r13 + 1
            r0 = r5 & 255(0xff, float:3.57E-43)
            r29 = r0
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r13] = r29
            int r21 = r22 + 3
            r13 = r14
            goto L_0x02b6
        L_0x037d:
            int r29 = r23 + 33
            r0 = r29
            byte r0 = (byte) r0
            r23 = r0
            r13 = r14
            goto L_0x0346
        L_0x0386:
            int r29 = 3 - r14
            r24 = r29 & 3
            r28 = r14
            int r13 = r14 + 1
            r0 = r32
            byte[] r0 = r0.code
            r29 = r0
            int r21 = r22 + 1
            byte r29 = r29[r22]
            r12[r14] = r29
            r14 = r13
        L_0x039b:
            int r24 = r24 + -1
            if (r24 < 0) goto L_0x0523
            int r13 = r14 + 1
            r29 = 0
            r12[r14] = r29
            r14 = r13
            goto L_0x039b
        L_0x03a7:
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r29 = r29[r16]
            r0 = r29
            int r0 = r0.position
            r29 = r0
            int r5 = r29 - r28
            int r13 = r14 + 1
            int r29 = r5 >> 24
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r14] = r29
            int r14 = r13 + 1
            int r29 = r5 >> 16
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r13] = r29
            int r13 = r14 + 1
            int r29 = r5 >> 8
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r14] = r29
            int r14 = r13 + 1
            r0 = r5 & 255(0xff, float:3.57E-43)
            r29 = r0
            r0 = r29
            byte r0 = (byte) r0
            r29 = r0
            r12[r13] = r29
            int r21 = r22 + 4
            r13 = r14
        L_0x03e8:
            r0 = r32
            int r0 = r0.fixup_count
            r29 = r0
            r0 = r16
            r1 = r29
            if (r0 >= r1) goto L_0x02b6
            int r29 = r16 + 1
            r0 = r32
            r1 = r29
            int r29 = r0.fixupKind(r1)
            r30 = 3
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x02b6
            int r16 = r16 + 1
            r0 = r32
            r1 = r16
            int r19 = r0.fixupOffset(r1)
            r22 = r21
            r14 = r13
        L_0x0413:
            r0 = r22
            r1 = r19
            if (r0 >= r1) goto L_0x03a7
            int r13 = r14 + 1
            r0 = r32
            byte[] r0 = r0.code
            r29 = r0
            int r21 = r22 + 1
            byte r29 = r29[r22]
            r12[r14] = r29
            r22 = r21
            r14 = r13
            goto L_0x0413
        L_0x042b:
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            int r30 = r16 + 2
            r10 = r29[r30]
            int r29 = r16 + 1
            r0 = r32
            r1 = r29
            int r6 = r0.fixupOffset(r1)
            r0 = r32
            gnu.bytecode.StackMapTableAttr r0 = r0.stackMap
            r29 = r0
            if (r29 == 0) goto L_0x044f
            r0 = r32
            r1 = r25
            gnu.bytecode.Label r25 = r0.mergeLabels(r1, r10)
        L_0x044f:
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r29 = r0
            r29 = r29[r16]
            r0 = r29
            int r0 = r0.position
            r29 = r0
            r0 = r32
            gnu.bytecode.Label[] r0 = r0.fixup_labels
            r30 = r0
            int r31 = r16 + 1
            r30 = r30[r31]
            r0 = r30
            int r0 = r0.position
            r30 = r0
            r0 = r32
            r1 = r29
            r2 = r30
            r0.addHandler(r1, r2, r14, r6)
            int r16 = r16 + 2
            r21 = r22
            r13 = r14
            goto L_0x02b6
        L_0x047d:
            r0 = r32
            gnu.bytecode.LineNumbersAttr r0 = r0.lines
            r29 = r0
            if (r29 != 0) goto L_0x0494
            gnu.bytecode.LineNumbersAttr r29 = new gnu.bytecode.LineNumbersAttr
            r0 = r29
            r1 = r32
            r0.<init>(r1)
            r0 = r29
            r1 = r32
            r1.lines = r0
        L_0x0494:
            int r16 = r16 + 1
            r0 = r32
            r1 = r16
            int r11 = r0.fixupOffset(r1)
            r0 = r26
            if (r11 == r0) goto L_0x04ad
            r0 = r32
            gnu.bytecode.LineNumbersAttr r0 = r0.lines
            r29 = r0
            r0 = r29
            r0.put(r11, r14)
        L_0x04ad:
            r26 = r11
            r21 = r22
            r13 = r14
            goto L_0x02b6
        L_0x04b4:
            if (r10 != 0) goto L_0x04df
            if (r15 == r14) goto L_0x04fd
            java.lang.Error r29 = new java.lang.Error
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            r30.<init>()
            java.lang.String r31 = "PC confusion new_pc:"
            java.lang.StringBuilder r30 = r30.append(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r14)
            java.lang.String r31 = " new_size:"
            java.lang.StringBuilder r30 = r30.append(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r15)
            java.lang.String r30 = r30.toString()
            r29.<init>(r30)
            throw r29
        L_0x04df:
            int r0 = r10.first_fixup
            r16 = r0
            r0 = r32
            r1 = r16
            int r21 = r0.fixupOffset(r1)
            r17 = r21
            int r0 = r10.position
            r29 = r0
            r0 = r29
            if (r0 == r14) goto L_0x051f
            java.lang.Error r29 = new java.lang.Error
            java.lang.String r30 = "bad pc"
            r29.<init>(r30)
            throw r29
        L_0x04fd:
            r0 = r32
            r0.PC = r15
            r0 = r32
            r0.code = r12
            r29 = 0
            r0 = r29
            r1 = r32
            r1.fixup_count = r0
            r29 = 0
            r0 = r29
            r1 = r32
            r1.fixup_labels = r0
            r29 = 0
            r0 = r29
            r1 = r32
            r1.fixup_offsets = r0
            goto L_0x0008
        L_0x051f:
            r22 = r21
            goto L_0x023f
        L_0x0523:
            r13 = r14
            goto L_0x03e8
        L_0x0526:
            r21 = r22
            r13 = r14
            goto L_0x02b6
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.bytecode.CodeAttr.processFixups():void");
    }

    private Label mergeLabels(Label oldLabel, Label newLabel) {
        if (oldLabel != null) {
            newLabel.setTypes(oldLabel);
        }
        return newLabel;
    }

    public void assignConstants(ClassType cl) {
        if (this.locals != null && this.locals.container == null && !this.locals.isEmpty()) {
            this.locals.addToFrontOf(this);
        }
        processFixups();
        if (this.stackMap != null && this.stackMap.numEntries > 0) {
            this.stackMap.addToFrontOf(this);
        }
        if (instructionLineMode) {
            if (this.lines == null) {
                this.lines = new LineNumbersAttr(this);
            }
            this.lines.linenumber_count = 0;
            int codeLen = getCodeLength();
            for (int i = 0; i < codeLen; i++) {
                this.lines.put(i, i);
            }
        }
        super.assignConstants(cl);
        Attribute.assignConstants(this, cl);
    }

    public final int getLength() {
        return getCodeLength() + 12 + (this.exception_table_length * 8) + Attribute.getLengthAll(this);
    }

    public void write(DataOutputStream dstr) throws IOException {
        dstr.writeShort(this.max_stack);
        dstr.writeShort(this.max_locals);
        dstr.writeInt(this.PC);
        dstr.write(this.code, 0, this.PC);
        dstr.writeShort(this.exception_table_length);
        int count = this.exception_table_length;
        int i = 0;
        while (true) {
            count--;
            if (count >= 0) {
                dstr.writeShort(this.exception_table[i]);
                dstr.writeShort(this.exception_table[i + 1]);
                dstr.writeShort(this.exception_table[i + 2]);
                dstr.writeShort(this.exception_table[i + 3]);
                i += 4;
            } else {
                Attribute.writeAll(this, dstr);
                return;
            }
        }
    }

    public void print(ClassTypeWriter dst) {
        dst.print("Attribute \"");
        dst.print(getName());
        dst.print("\", length:");
        dst.print(getLength());
        dst.print(", max_stack:");
        dst.print(this.max_stack);
        dst.print(", max_locals:");
        dst.print(this.max_locals);
        dst.print(", code_length:");
        int length = getCodeLength();
        dst.println(length);
        disAssemble(dst, 0, length);
        if (this.exception_table_length > 0) {
            dst.print("Exceptions (count: ");
            dst.print(this.exception_table_length);
            dst.println("):");
            int count = this.exception_table_length;
            int i = 0;
            while (true) {
                count--;
                if (count < 0) {
                    break;
                }
                dst.print("  start: ");
                dst.print(this.exception_table[i] & 65535);
                dst.print(", end: ");
                dst.print(this.exception_table[i + 1] & 65535);
                dst.print(", handler: ");
                dst.print(this.exception_table[i + 2] & 65535);
                dst.print(", type: ");
                int catch_type_index = this.exception_table[i + 3] & SupportMenu.USER_MASK;
                if (catch_type_index == 0) {
                    dst.print("0 /* finally */");
                } else {
                    dst.printOptionalIndex(catch_type_index);
                    dst.printConstantTersely(catch_type_index, 7);
                }
                dst.println();
                i += 4;
            }
        }
        dst.printAttributes(this);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v8, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v9, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void disAssemble(gnu.bytecode.ClassTypeWriter r29, int r30, int r31) {
        /*
            r28 = this;
            r25 = 0
            r10 = r30
            r11 = r10
        L_0x0005:
            r0 = r31
            if (r11 >= r0) goto L_0x0707
            int r10 = r11 + 1
            r18 = r11
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            byte r26 = r26[r18]
            r0 = r26
            r0 = r0 & 255(0xff, float:3.57E-43)
            r19 = r0
            java.lang.String r21 = java.lang.Integer.toString(r18)
            r20 = 0
            int r13 = r21.length()
        L_0x0025:
            int r13 = r13 + 1
            r26 = 3
            r0 = r26
            if (r13 > r0) goto L_0x0037
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            goto L_0x0025
        L_0x0037:
            r0 = r29
            r1 = r21
            r0.print(r1)
            java.lang.String r26 = ": "
            r0 = r29
            r1 = r26
            r0.print(r1)
            r26 = 120(0x78, float:1.68E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0224
            r26 = 87
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x01e5
            r26 = 3
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0090
            java.lang.String r26 = "nop;aconst_null;iconst_m1;"
            r0 = r28
            r1 = r26
            r2 = r19
            r3 = r29
            r0.print(r1, r2, r3)
            r11 = r10
        L_0x006d:
            if (r20 <= 0) goto L_0x0708
            r26 = 1
            r0 = r20
            r1 = r26
            if (r0 != r1) goto L_0x06fd
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            int r10 = r11 + 1
            byte r26 = r26[r11]
            r0 = r26
            r12 = r0 & 255(0xff, float:3.57E-43)
        L_0x0085:
            r0 = r29
            r0.printConstantOperand(r12)
        L_0x008a:
            r29.println()
            r11 = r10
            goto L_0x0005
        L_0x0090:
            r26 = 9
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x00ac
            java.lang.String r26 = "iconst_"
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r19 + -3
            r0 = r29
            r1 = r26
            r0.print(r1)
            r11 = r10
            goto L_0x006d
        L_0x00ac:
            r26 = 16
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x00eb
            r26 = 11
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x00d9
            r22 = 108(0x6c, float:1.51E-43)
            int r19 = r19 + -9
        L_0x00c0:
            r0 = r29
            r1 = r22
            r0.print(r1)
            java.lang.String r26 = "const_"
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r1 = r19
            r0.print(r1)
            r11 = r10
            goto L_0x006d
        L_0x00d9:
            r26 = 14
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x00e6
            r22 = 102(0x66, float:1.43E-43)
            int r19 = r19 + -11
            goto L_0x00c0
        L_0x00e6:
            r22 = 100
            int r19 = r19 + -14
            goto L_0x00c0
        L_0x00eb:
            r26 = 21
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0150
            r26 = 18
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0131
            java.lang.String r26 = "bipush ;sipush ;"
            int r27 = r19 + -16
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r26 = 16
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x0125
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            int r11 = r10 + 1
            byte r6 = r26[r10]
            r10 = r11
        L_0x011d:
            r0 = r29
            r0.print(r6)
            r11 = r10
            goto L_0x006d
        L_0x0125:
            r0 = r28
            int r26 = r0.readUnsignedShort(r10)
            r0 = r26
            short r6 = (short) r0
            int r10 = r10 + 2
            goto L_0x011d
        L_0x0131:
            r26 = 18
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x014d
            r20 = 1
        L_0x013b:
            java.lang.String r26 = "ldc;ldc_w;ldc2_w;"
            int r27 = r19 + -18
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r11 = r10
            goto L_0x006d
        L_0x014d:
            r20 = 2
            goto L_0x013b
        L_0x0150:
            r26 = 54
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x019d
            java.lang.String r14 = "load"
        L_0x015a:
            r26 = 26
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x01a2
            r12 = -1
            int r19 = r19 + -21
        L_0x0165:
            java.lang.String r26 = "ilfdabcs"
            r0 = r26
            r1 = r19
            char r26 = r0.charAt(r1)
            r0 = r29
            r1 = r26
            r0.print(r1)
            r26 = -2
            r0 = r26
            if (r12 != r0) goto L_0x0185
            r26 = 97
            r0 = r29
            r1 = r26
            r0.write(r1)
        L_0x0185:
            r0 = r29
            r0.print(r14)
            if (r12 < 0) goto L_0x01b5
            r26 = 95
            r0 = r29
            r1 = r26
            r0.write(r1)
            r0 = r29
            r0.print(r12)
        L_0x019a:
            r11 = r10
            goto L_0x006d
        L_0x019d:
            java.lang.String r14 = "store"
            int r19 = r19 + -33
            goto L_0x015a
        L_0x01a2:
            r26 = 46
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x01b1
            int r19 = r19 + -26
            int r12 = r19 % 4
            int r19 = r19 >> 2
            goto L_0x0165
        L_0x01b1:
            r12 = -2
            int r19 = r19 + -46
            goto L_0x0165
        L_0x01b5:
            r26 = -1
            r0 = r26
            if (r12 != r0) goto L_0x019a
            if (r25 == 0) goto L_0x01d6
            r0 = r28
            int r12 = r0.readUnsignedShort(r10)
            int r10 = r10 + 2
        L_0x01c5:
            r25 = 0
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r0.print(r12)
            goto L_0x019a
        L_0x01d6:
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            byte r26 = r26[r10]
            r0 = r26
            r12 = r0 & 255(0xff, float:3.57E-43)
            int r10 = r10 + 1
            goto L_0x01c5
        L_0x01e5:
            r26 = 96
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x01ff
            java.lang.String r26 = "pop;pop2;dup;dup_x1;dup_x2;dup2;dup2_x1;dup2_x2;swap;"
            int r27 = r19 + -87
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r11 = r10
            goto L_0x006d
        L_0x01ff:
            java.lang.String r26 = "ilfda"
            int r27 = r19 + -96
            int r27 = r27 % 4
            char r26 = r26.charAt(r27)
            r0 = r29
            r1 = r26
            r0.print(r1)
            java.lang.String r26 = "add;sub;mul;div;rem;neg;"
            int r27 = r19 + -96
            int r27 = r27 >> 2
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r11 = r10
            goto L_0x006d
        L_0x0224:
            r26 = 170(0xaa, float:2.38E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x03db
            r26 = 132(0x84, float:1.85E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0258
            r26 = r19 & 1
            if (r26 != 0) goto L_0x0255
            r26 = 105(0x69, float:1.47E-43)
        L_0x023a:
            r0 = r29
            r1 = r26
            r0.print(r1)
            java.lang.String r26 = "shl;shr;ushr;and;or;xor;"
            int r27 = r19 + -120
            int r27 = r27 >> 1
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r11 = r10
            goto L_0x006d
        L_0x0255:
            r26 = 108(0x6c, float:1.51E-43)
            goto L_0x023a
        L_0x0258:
            r26 = 132(0x84, float:1.85E-43)
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x02bc
            java.lang.String r26 = "iinc"
            r0 = r29
            r1 = r26
            r0.print(r1)
            if (r25 != 0) goto L_0x02a6
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            int r11 = r10 + 1
            byte r26 = r26[r10]
            r0 = r26
            r0 = r0 & 255(0xff, float:3.57E-43)
            r24 = r0
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            int r10 = r11 + 1
            byte r6 = r26[r11]
        L_0x0285:
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r1 = r24
            r0.print(r1)
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r0.print(r6)
            r11 = r10
            goto L_0x006d
        L_0x02a6:
            r0 = r28
            int r24 = r0.readUnsignedShort(r10)
            int r10 = r10 + 2
            r0 = r28
            int r26 = r0.readUnsignedShort(r10)
            r0 = r26
            short r6 = (short) r0
            int r10 = r10 + 2
            r25 = 0
            goto L_0x0285
        L_0x02bc:
            r26 = 148(0x94, float:2.07E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x02f8
            java.lang.String r26 = "ilfdi"
            r0 = r19
            int r0 = r0 + -133
            r27 = r0
            int r27 = r27 / 3
            char r26 = r26.charAt(r27)
            r0 = r29
            r1 = r26
            r0.print(r1)
            r26 = 50
            r0 = r29
            r1 = r26
            r0.print(r1)
            java.lang.String r26 = "lfdifdildilfbcs"
            r0 = r19
            int r0 = r0 + -133
            r27 = r0
            char r26 = r26.charAt(r27)
            r0 = r29
            r1 = r26
            r0.print(r1)
            r11 = r10
            goto L_0x006d
        L_0x02f8:
            r26 = 153(0x99, float:2.14E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0316
            java.lang.String r26 = "lcmp;fcmpl;fcmpg;dcmpl;dcmpg;"
            r0 = r19
            int r0 = r0 + -148
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r11 = r10
            goto L_0x006d
        L_0x0316:
            r26 = 169(0xa9, float:2.37E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x03af
            r26 = 159(0x9f, float:2.23E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0362
            java.lang.String r26 = "if"
            r0 = r29
            r1 = r26
            r0.print(r1)
            java.lang.String r26 = "eq;ne;lt;ge;gt;le;"
            r0 = r19
            int r0 = r0 + -153
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
        L_0x0342:
            r0 = r28
            int r26 = r0.readUnsignedShort(r10)
            r0 = r26
            short r7 = (short) r0
            int r10 = r10 + 2
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r18 + r7
            r0 = r29
            r1 = r26
            r0.print(r1)
            r11 = r10
            goto L_0x006d
        L_0x0362:
            r26 = 167(0xa7, float:2.34E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x039b
            r26 = 165(0xa5, float:2.31E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x038f
            java.lang.String r26 = "if_icmp"
            r0 = r29
            r1 = r26
            r0.print(r1)
        L_0x037b:
            java.lang.String r26 = "eq;ne;lt;ge;gt;le;"
            r0 = r19
            int r0 = r0 + -159
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            goto L_0x0342
        L_0x038f:
            java.lang.String r26 = "if_acmp"
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r19 = r19 + -6
            goto L_0x037b
        L_0x039b:
            java.lang.String r26 = "goto;jsr;"
            r0 = r19
            int r0 = r0 + -167
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            goto L_0x0342
        L_0x03af:
            java.lang.String r26 = "ret "
            r0 = r29
            r1 = r26
            r0.print(r1)
            if (r25 == 0) goto L_0x03cc
            r0 = r28
            int r12 = r0.readUnsignedShort(r10)
            int r12 = r12 + 2
        L_0x03c2:
            r25 = 0
            r0 = r29
            r0.print(r12)
            r11 = r10
            goto L_0x006d
        L_0x03cc:
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            byte r26 = r26[r10]
            r0 = r26
            r12 = r0 & 255(0xff, float:3.57E-43)
            int r10 = r10 + 1
            goto L_0x03c2
        L_0x03db:
            r26 = 172(0xac, float:2.41E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x04e6
            r0 = r28
            int r0 = r0.fixup_count
            r26 = r0
            if (r26 > 0) goto L_0x03ef
            int r26 = r10 + 3
            r10 = r26 & -4
        L_0x03ef:
            r0 = r28
            int r5 = r0.readInt(r10)
            int r10 = r10 + 4
            r26 = 170(0xaa, float:2.38E-43)
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x0476
            java.lang.String r26 = "tableswitch"
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r28
            int r15 = r0.readInt(r10)
            int r10 = r10 + 4
            r0 = r28
            int r9 = r0.readInt(r10)
            int r10 = r10 + 4
            java.lang.String r26 = " low: "
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r0.print(r15)
            java.lang.String r26 = " high: "
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r0.print(r9)
            java.lang.String r26 = " default: "
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r18 + r5
            r0 = r29
            r1 = r26
            r0.print(r1)
        L_0x0446:
            if (r15 > r9) goto L_0x04e3
            r0 = r28
            int r5 = r0.readInt(r10)
            int r10 = r10 + 4
            r29.println()
            java.lang.String r26 = "  "
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r0.print(r15)
            java.lang.String r26 = ": "
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r18 + r5
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r15 = r15 + 1
            goto L_0x0446
        L_0x0476:
            java.lang.String r26 = "lookupswitch"
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r28
            int r17 = r0.readInt(r10)
            int r10 = r10 + 4
            java.lang.String r26 = " npairs: "
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r1 = r17
            r0.print(r1)
            java.lang.String r26 = " default: "
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r18 + r5
            r0 = r29
            r1 = r26
            r0.print(r1)
        L_0x04a9:
            int r17 = r17 + -1
            if (r17 < 0) goto L_0x04e3
            r0 = r28
            int r16 = r0.readInt(r10)
            int r10 = r10 + 4
            r0 = r28
            int r5 = r0.readInt(r10)
            int r10 = r10 + 4
            r29.println()
            java.lang.String r26 = "  "
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r1 = r16
            r0.print(r1)
            java.lang.String r26 = ": "
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r18 + r5
            r0 = r29
            r1 = r26
            r0.print(r1)
            goto L_0x04a9
        L_0x04e3:
            r11 = r10
            goto L_0x006d
        L_0x04e6:
            r26 = 178(0xb2, float:2.5E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0515
            r26 = 177(0xb1, float:2.48E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0509
            java.lang.String r26 = "ilfda"
            r0 = r19
            int r0 = r0 + -172
            r27 = r0
            char r26 = r26.charAt(r27)
            r0 = r29
            r1 = r26
            r0.print(r1)
        L_0x0509:
            java.lang.String r26 = "return"
            r0 = r29
            r1 = r26
            r0.print(r1)
            r11 = r10
            goto L_0x006d
        L_0x0515:
            r26 = 182(0xb6, float:2.55E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x0535
            java.lang.String r26 = "getstatic;putstatic;getfield;putfield;"
            r0 = r19
            int r0 = r0 + -178
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r20 = 2
            r11 = r10
            goto L_0x006d
        L_0x0535:
            r26 = 185(0xb9, float:2.59E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x055e
            java.lang.String r26 = "invoke"
            r0 = r29
            r1 = r26
            r0.print(r1)
            java.lang.String r26 = "virtual;special;static;"
            r0 = r19
            int r0 = r0 + -182
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r20 = 2
            r11 = r10
            goto L_0x006d
        L_0x055e:
            r26 = 185(0xb9, float:2.59E-43)
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x05a9
            java.lang.String r26 = "invokeinterface ("
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r28
            int r12 = r0.readUnsignedShort(r10)
            int r10 = r10 + 2
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            byte r26 = r26[r10]
            r0 = r26
            r4 = r0 & 255(0xff, float:3.57E-43)
            int r10 = r10 + 2
            java.lang.StringBuilder r26 = new java.lang.StringBuilder
            r26.<init>()
            r0 = r26
            java.lang.StringBuilder r26 = r0.append(r4)
            java.lang.String r27 = " args)"
            java.lang.StringBuilder r26 = r26.append(r27)
            java.lang.String r26 = r26.toString()
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r0.printConstantOperand(r12)
            r11 = r10
            goto L_0x006d
        L_0x05a9:
            r26 = 196(0xc4, float:2.75E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x062e
            java.lang.String r26 = "186;new;newarray;anewarray;arraylength;athrow;checkcast;instanceof;monitorenter;monitorexit;"
            r0 = r19
            int r0 = r0 + -186
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r26 = 187(0xbb, float:2.62E-43)
            r0 = r19
            r1 = r26
            if (r0 == r1) goto L_0x05e4
            r26 = 189(0xbd, float:2.65E-43)
            r0 = r19
            r1 = r26
            if (r0 == r1) goto L_0x05e4
            r26 = 192(0xc0, float:2.69E-43)
            r0 = r19
            r1 = r26
            if (r0 == r1) goto L_0x05e4
            r26 = 193(0xc1, float:2.7E-43)
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x05e9
        L_0x05e4:
            r20 = 2
            r11 = r10
            goto L_0x006d
        L_0x05e9:
            r26 = 188(0xbc, float:2.63E-43)
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x06fa
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            int r11 = r10 + 1
            byte r23 = r26[r10]
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            r26 = 4
            r0 = r23
            r1 = r26
            if (r0 < r1) goto L_0x0625
            r26 = 11
            r0 = r23
            r1 = r26
            if (r0 > r1) goto L_0x0625
            java.lang.String r26 = "boolean;char;float;double;byte;short;int;long;"
            int r27 = r23 + -4
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            goto L_0x006d
        L_0x0625:
            r0 = r29
            r1 = r23
            r0.print(r1)
            goto L_0x006d
        L_0x062e:
            r26 = 196(0xc4, float:2.75E-43)
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x0644
            java.lang.String r26 = "wide"
            r0 = r29
            r1 = r26
            r0.print(r1)
            r25 = 1
            r11 = r10
            goto L_0x006d
        L_0x0644:
            r26 = 197(0xc5, float:2.76E-43)
            r0 = r19
            r1 = r26
            if (r0 != r1) goto L_0x0680
            java.lang.String r26 = "multianewarray"
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r28
            int r12 = r0.readUnsignedShort(r10)
            int r10 = r10 + 2
            r0 = r29
            r0.printConstantOperand(r12)
            r0 = r28
            byte[] r0 = r0.code
            r26 = r0
            int r11 = r10 + 1
            byte r26 = r26[r10]
            r0 = r26
            r8 = r0 & 255(0xff, float:3.57E-43)
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            r0 = r29
            r0.print(r8)
            goto L_0x006d
        L_0x0680:
            r26 = 200(0xc8, float:2.8E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x06bb
            java.lang.String r26 = "ifnull;ifnonnull;"
            r0 = r19
            int r0 = r0 + -198
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r0 = r28
            int r26 = r0.readUnsignedShort(r10)
            r0 = r26
            short r7 = (short) r0
            int r10 = r10 + 2
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r18 + r7
            r0 = r29
            r1 = r26
            r0.print(r1)
            r11 = r10
            goto L_0x006d
        L_0x06bb:
            r26 = 202(0xca, float:2.83E-43)
            r0 = r19
            r1 = r26
            if (r0 >= r1) goto L_0x06f3
            java.lang.String r26 = "goto_w;jsr_w;"
            r0 = r19
            int r0 = r0 + -200
            r27 = r0
            r0 = r28
            r1 = r26
            r2 = r27
            r3 = r29
            r0.print(r1, r2, r3)
            r0 = r28
            int r7 = r0.readInt(r10)
            int r10 = r10 + 4
            r26 = 32
            r0 = r29
            r1 = r26
            r0.print(r1)
            int r26 = r18 + r7
            r0 = r29
            r1 = r26
            r0.print(r1)
            r11 = r10
            goto L_0x006d
        L_0x06f3:
            r0 = r29
            r1 = r19
            r0.print(r1)
        L_0x06fa:
            r11 = r10
            goto L_0x006d
        L_0x06fd:
            r0 = r28
            int r12 = r0.readUnsignedShort(r11)
            int r10 = r11 + 2
            goto L_0x0085
        L_0x0707:
            return
        L_0x0708:
            r10 = r11
            goto L_0x008a
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.bytecode.CodeAttr.disAssemble(gnu.bytecode.ClassTypeWriter, int, int):void");
    }

    private int readUnsignedShort(int offset) {
        return ((this.code[offset] & Ev3Constants.Opcode.TST) << 8) | (this.code[offset + 1] & Ev3Constants.Opcode.TST);
    }

    private int readInt(int offset) {
        return (readUnsignedShort(offset) << 16) | readUnsignedShort(offset + 2);
    }

    private void print(String str, int i, PrintWriter dst) {
        int last = 0;
        int pos = -1;
        while (i >= 0) {
            last = pos + 1;
            pos = str.indexOf(59, last);
            i--;
        }
        dst.write(str, last, pos - last);
    }

    public int beginFragment(Label after) {
        return beginFragment(new Label(), after);
    }

    public int beginFragment(Label start, Label after) {
        int i = this.fixup_count;
        fixupAdd(10, after);
        start.define(this);
        return i;
    }

    public void endFragment(int cookie) {
        this.fixup_offsets[cookie] = (this.fixup_count << 4) | 10;
        Label after = this.fixup_labels[cookie];
        fixupAdd(9, 0, (Label) null);
        after.define(this);
    }
}
