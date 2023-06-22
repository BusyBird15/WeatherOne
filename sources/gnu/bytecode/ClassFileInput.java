package gnu.bytecode;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ClassFileInput extends DataInputStream {
    ClassType ctype;
    InputStream str;

    public ClassFileInput(InputStream str2) throws IOException {
        super(str2);
    }

    public ClassFileInput(ClassType ctype2, InputStream str2) throws IOException, ClassFormatError {
        super(str2);
        this.ctype = ctype2;
        if (!readHeader()) {
            throw new ClassFormatError("invalid magic number");
        }
        ctype2.constants = readConstants();
        readClassInfo();
        readFields();
        readMethods();
        readAttributes(ctype2);
    }

    public static ClassType readClassType(InputStream str2) throws IOException, ClassFormatError {
        ClassType ctype2 = new ClassType();
        new ClassFileInput(ctype2, str2);
        return ctype2;
    }

    public boolean readHeader() throws IOException {
        if (readInt() != -889275714) {
            return false;
        }
        readFormatVersion();
        return true;
    }

    public void readFormatVersion() throws IOException {
        this.ctype.classfileFormatVersion = (65536 * readUnsignedShort()) + readUnsignedShort();
    }

    public ConstantPool readConstants() throws IOException {
        return new ConstantPool(this);
    }

    public void readClassInfo() throws IOException {
        this.ctype.access_flags = readUnsignedShort();
        this.ctype.thisClassIndex = readUnsignedShort();
        String name = getClassConstant(this.ctype.thisClassIndex).name.string;
        this.ctype.this_name = name.replace('/', '.');
        this.ctype.setSignature("L" + name + ";");
        this.ctype.superClassIndex = readUnsignedShort();
        if (this.ctype.superClassIndex == 0) {
            this.ctype.setSuper((ClassType) null);
        } else {
            this.ctype.setSuper(getClassConstant(this.ctype.superClassIndex).name.string.replace('/', '.'));
        }
        int nInterfaces = readUnsignedShort();
        if (nInterfaces > 0) {
            this.ctype.interfaces = new ClassType[nInterfaces];
            this.ctype.interfaceIndexes = new int[nInterfaces];
            for (int i = 0; i < nInterfaces; i++) {
                int index = readUnsignedShort();
                this.ctype.interfaceIndexes[i] = index;
                this.ctype.interfaces[i] = ClassType.make(((CpoolClass) this.ctype.constants.getForced(index, 7)).name.string.replace('/', '.'));
            }
        }
    }

    public int readAttributes(AttrContainer container) throws IOException {
        int count = readUnsignedShort();
        Attribute last = container.getAttributes();
        for (int i = 0; i < count; i++) {
            if (last != null) {
                while (true) {
                    Attribute next = last.getNext();
                    if (next == null) {
                        break;
                    }
                    last = next;
                }
            }
            int index = readUnsignedShort();
            CpoolUtf8 nameConstant = (CpoolUtf8) this.ctype.constants.getForced(index, 1);
            int length = readInt();
            nameConstant.intern();
            Attribute attr = readAttribute(nameConstant.string, length, container);
            if (attr != null) {
                if (attr.getNameIndex() == 0) {
                    attr.setNameIndex(index);
                }
                if (last == null) {
                    container.setAttributes(attr);
                } else {
                    if (container.getAttributes() == attr) {
                        container.setAttributes(attr.getNext());
                        attr.setNext((Attribute) null);
                    }
                    last.setNext(attr);
                }
                last = attr;
            }
        }
        return count;
    }

    public final void skipAttribute(int length) throws IOException {
        int read = 0;
        while (read < length) {
            int skipped = (int) skip((long) (length - read));
            if (skipped == 0) {
                if (read() < 0) {
                    throw new EOFException("EOF while reading class files attributes");
                }
                skipped = 1;
            }
            read += skipped;
        }
    }

    public Attribute readAttribute(String name, int length, AttrContainer container) throws IOException {
        if (name == "SourceFile" && (container instanceof ClassType)) {
            return new SourceFileAttr(readUnsignedShort(), (ClassType) container);
        }
        if (name == "Code" && (container instanceof Method)) {
            CodeAttr code = new CodeAttr((Method) container);
            code.fixup_count = -1;
            code.setMaxStack(readUnsignedShort());
            code.setMaxLocals(readUnsignedShort());
            byte[] insns = new byte[readInt()];
            readFully(insns);
            code.setCode(insns);
            int exception_table_length = readUnsignedShort();
            for (int i = 0; i < exception_table_length; i++) {
                code.addHandler(readUnsignedShort(), readUnsignedShort(), readUnsignedShort(), readUnsignedShort());
            }
            readAttributes(code);
            return code;
        } else if (name == "LineNumberTable" && (container instanceof CodeAttr)) {
            int count = readUnsignedShort() * 2;
            short[] numbers = new short[count];
            for (int i2 = 0; i2 < count; i2++) {
                numbers[i2] = readShort();
            }
            return new LineNumbersAttr(numbers, (CodeAttr) container);
        } else if (name == "LocalVariableTable" && (container instanceof CodeAttr)) {
            CodeAttr code2 = (CodeAttr) container;
            LocalVarsAttr attr = new LocalVarsAttr(code2);
            Method method = attr.getMethod();
            if (attr.parameter_scope == null) {
                attr.parameter_scope = method.pushScope();
            }
            Scope scope = attr.parameter_scope;
            if (scope.end == null) {
                scope.end = new Label(code2.PC);
            }
            ConstantPool constants = method.getConstants();
            int count2 = readUnsignedShort();
            int prev_start = scope.start.position;
            int prev_end = scope.end.position;
            for (int i3 = 0; i3 < count2; i3++) {
                Variable var = new Variable();
                int start_pc = readUnsignedShort();
                int end_pc = start_pc + readUnsignedShort();
                if (start_pc != prev_start || end_pc != prev_end) {
                    while (scope.parent != null && (start_pc < scope.start.position || end_pc > scope.end.position)) {
                        scope = scope.parent;
                    }
                    scope = new Scope(new Label(start_pc), new Label(end_pc));
                    scope.linkChild(scope);
                    prev_start = start_pc;
                    prev_end = end_pc;
                }
                scope.addVariable(var);
                var.setName(readUnsignedShort(), constants);
                var.setSignature(readUnsignedShort(), constants);
                var.offset = readUnsignedShort();
            }
            return attr;
        } else if (name == "Signature" && (container instanceof Member)) {
            return new SignatureAttr(readUnsignedShort(), (Member) container);
        } else {
            if (name == "StackMapTable" && (container instanceof CodeAttr)) {
                byte[] data = new byte[length];
                readFully(data, 0, length);
                return new StackMapTableAttr(data, (CodeAttr) container);
            } else if ((name == "RuntimeVisibleAnnotations" || name == "RuntimeInvisibleAnnotations") && ((container instanceof Field) || (container instanceof Method) || (container instanceof ClassType))) {
                byte[] data2 = new byte[length];
                readFully(data2, 0, length);
                return new RuntimeAnnotationsAttr(name, data2, container);
            } else if (name == "ConstantValue" && (container instanceof Field)) {
                return new ConstantValueAttr(readUnsignedShort());
            } else {
                if (name == "InnerClasses" && (container instanceof ClassType)) {
                    int count3 = readUnsignedShort() * 4;
                    short[] data3 = new short[count3];
                    for (int i4 = 0; i4 < count3; i4++) {
                        data3[i4] = readShort();
                    }
                    return new InnerClassesAttr(data3, (ClassType) container);
                } else if (name == "EnclosingMethod" && (container instanceof ClassType)) {
                    return new EnclosingMethodAttr(readUnsignedShort(), readUnsignedShort(), (ClassType) container);
                } else {
                    if (name == "Exceptions" && (container instanceof Method)) {
                        Method meth = (Method) container;
                        int count4 = readUnsignedShort();
                        short[] exn_indices = new short[count4];
                        for (int i5 = 0; i5 < count4; i5++) {
                            exn_indices[i5] = readShort();
                        }
                        meth.setExceptions(exn_indices);
                        return meth.getExceptionAttr();
                    } else if (name != "SourceDebugExtension" || !(container instanceof ClassType)) {
                        byte[] data4 = new byte[length];
                        readFully(data4, 0, length);
                        return new MiscAttr(name, data4);
                    } else {
                        SourceDebugExtAttr attr2 = new SourceDebugExtAttr((ClassType) container);
                        byte[] data5 = new byte[length];
                        readFully(data5, 0, length);
                        attr2.data = data5;
                        attr2.dlength = length;
                        return attr2;
                    }
                }
            }
        }
    }

    public void readFields() throws IOException {
        int nFields = readUnsignedShort();
        ConstantPool constants = this.ctype.constants;
        for (int i = 0; i < nFields; i++) {
            int flags = readUnsignedShort();
            int nameIndex = readUnsignedShort();
            int descriptorIndex = readUnsignedShort();
            Field fld = this.ctype.addField();
            fld.setName(nameIndex, constants);
            fld.setSignature(descriptorIndex, constants);
            fld.flags = flags;
            readAttributes(fld);
        }
    }

    public void readMethods() throws IOException {
        int nMethods = readUnsignedShort();
        for (int i = 0; i < nMethods; i++) {
            int flags = readUnsignedShort();
            int nameIndex = readUnsignedShort();
            int descriptorIndex = readUnsignedShort();
            Method meth = this.ctype.addMethod((String) null, flags);
            meth.setName(nameIndex);
            meth.setSignature(descriptorIndex);
            readAttributes(meth);
        }
    }

    /* access modifiers changed from: package-private */
    public CpoolClass getClassConstant(int index) {
        return (CpoolClass) this.ctype.constants.getForced(index, 7);
    }
}
