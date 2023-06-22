package gnu.bytecode;

public class RuntimeAnnotationsAttr extends MiscAttr {
    int numEntries = u2(0);

    public RuntimeAnnotationsAttr(String name, byte[] data, AttrContainer container) {
        super(name, data, 0, data.length);
        addToFrontOf(container);
    }

    public void print(ClassTypeWriter dst) {
        dst.print("Attribute \"");
        dst.print(getName());
        dst.print("\", length:");
        dst.print(getLength());
        dst.print(", number of entries: ");
        dst.println(this.numEntries);
        int saveOffset = this.offset;
        this.offset = saveOffset + 2;
        for (int i = 0; i < this.numEntries; i++) {
            printAnnotation(2, dst);
        }
        this.offset = saveOffset;
    }

    public void printAnnotation(int indentation, ClassTypeWriter dst) {
        int type_index = u2();
        dst.printSpaces(indentation);
        dst.printOptionalIndex(type_index);
        dst.print('@');
        dst.printContantUtf8AsClass(type_index);
        int num_element_value_pairs = u2();
        dst.println();
        int indentation2 = indentation + 2;
        for (int i = 0; i < num_element_value_pairs; i++) {
            int element_name_index = u2();
            dst.printSpaces(indentation2);
            dst.printOptionalIndex(element_name_index);
            dst.printConstantTersely(element_name_index, 1);
            dst.print(" => ");
            printAnnotationElementValue(indentation2, dst);
            dst.println();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002d, code lost:
        if (r5 != 0) goto L_0x0030;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002f, code lost:
        r5 = 5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0030, code lost:
        if (r5 != 0) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
        r5 = 6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0033, code lost:
        if (r5 != 0) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0035, code lost:
        r5 = 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0036, code lost:
        if (r5 != 0) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
        r5 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0039, code lost:
        r3 = u2();
        r4 = r15.getCpoolEntry(r3);
        r15.printOptionalIndex(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0046, code lost:
        if (r8 != 90) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0048, code lost:
        if (r4 == null) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004f, code lost:
        if (r4.getTag() != 3) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0051, code lost:
        r0 = (gnu.bytecode.CpoolValue1) r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0056, code lost:
        if (r0.value == 0) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005a, code lost:
        if (r0.value != 1) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x005e, code lost:
        if (r0.value != 0) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0060, code lost:
        r10 = "false";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0062, code lost:
        r15.print(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0066, code lost:
        r10 = "true";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0069, code lost:
        r15.printConstantTersely(r3, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void printAnnotationElementValue(int r14, gnu.bytecode.ClassTypeWriter r15) {
        /*
            r13 = this;
            r12 = 1
            int r8 = r13.u1()
            int r10 = r15.flags
            r10 = r10 & 8
            if (r10 == 0) goto L_0x0021
            java.lang.String r10 = "[kind:"
            r15.print(r10)
            r10 = 65
            if (r8 < r10) goto L_0x0026
            r10 = 122(0x7a, float:1.71E-43)
            if (r8 > r10) goto L_0x0026
            char r10 = (char) r8
            r15.print(r10)
        L_0x001c:
            java.lang.String r10 = "] "
            r15.print(r10)
        L_0x0021:
            r5 = 0
            switch(r8) {
                case 64: goto L_0x00b4;
                case 66: goto L_0x002a;
                case 67: goto L_0x002a;
                case 68: goto L_0x0030;
                case 70: goto L_0x0033;
                case 73: goto L_0x002a;
                case 74: goto L_0x002d;
                case 83: goto L_0x002a;
                case 90: goto L_0x002a;
                case 91: goto L_0x00c3;
                case 99: goto L_0x00a8;
                case 101: goto L_0x006d;
                case 115: goto L_0x0036;
                default: goto L_0x0025;
            }
        L_0x0025:
            return
        L_0x0026:
            r15.print(r8)
            goto L_0x001c
        L_0x002a:
            if (r5 != 0) goto L_0x002d
            r5 = 3
        L_0x002d:
            if (r5 != 0) goto L_0x0030
            r5 = 5
        L_0x0030:
            if (r5 != 0) goto L_0x0033
            r5 = 6
        L_0x0033:
            if (r5 != 0) goto L_0x0036
            r5 = 4
        L_0x0036:
            if (r5 != 0) goto L_0x0039
            r5 = 1
        L_0x0039:
            int r3 = r13.u2()
            gnu.bytecode.CpoolEntry r4 = r15.getCpoolEntry(r3)
            r15.printOptionalIndex((gnu.bytecode.CpoolEntry) r4)
            r10 = 90
            if (r8 != r10) goto L_0x0069
            if (r4 == 0) goto L_0x0069
            int r10 = r4.getTag()
            r11 = 3
            if (r10 != r11) goto L_0x0069
            r0 = r4
            gnu.bytecode.CpoolValue1 r0 = (gnu.bytecode.CpoolValue1) r0
            int r10 = r0.value
            if (r10 == 0) goto L_0x005c
            int r10 = r0.value
            if (r10 != r12) goto L_0x0069
        L_0x005c:
            int r10 = r0.value
            if (r10 != 0) goto L_0x0066
            java.lang.String r10 = "false"
        L_0x0062:
            r15.print(r10)
            goto L_0x0025
        L_0x0066:
            java.lang.String r10 = "true"
            goto L_0x0062
        L_0x0069:
            r15.printConstantTersely((int) r3, (int) r5)
            goto L_0x0025
        L_0x006d:
            int r9 = r13.u2()
            int r2 = r13.u2()
            java.lang.String r10 = "enum["
            r15.print(r10)
            int r10 = r15.flags
            r10 = r10 & 8
            if (r10 == 0) goto L_0x0085
            java.lang.String r10 = "type:"
            r15.print(r10)
        L_0x0085:
            r15.printOptionalIndex((int) r9)
            r15.printContantUtf8AsClass(r9)
            int r10 = r15.flags
            r10 = r10 & 8
            if (r10 == 0) goto L_0x00a2
            java.lang.String r10 = " value:"
            r15.print(r10)
        L_0x0096:
            r15.printOptionalIndex((int) r2)
            r15.printConstantTersely((int) r2, (int) r12)
            java.lang.String r10 = "]"
            r15.print(r10)
            goto L_0x0025
        L_0x00a2:
            r10 = 32
            r15.print(r10)
            goto L_0x0096
        L_0x00a8:
            int r1 = r13.u2()
            r15.printOptionalIndex((int) r1)
            r15.printContantUtf8AsClass(r1)
            goto L_0x0025
        L_0x00b4:
            r15.println()
            int r10 = r14 + 2
            r15.printSpaces(r10)
            int r10 = r14 + 2
            r13.printAnnotation(r10, r15)
            goto L_0x0025
        L_0x00c3:
            int r7 = r13.u2()
            java.lang.String r10 = "array length:"
            r15.print(r10)
            r15.print(r7)
            r6 = 0
        L_0x00d0:
            if (r6 >= r7) goto L_0x0025
            r15.println()
            int r10 = r14 + 2
            r15.printSpaces(r10)
            r15.print(r6)
            java.lang.String r10 = ": "
            r15.print(r10)
            int r10 = r14 + 2
            r13.printAnnotationElementValue(r10, r15)
            int r6 = r6 + 1
            goto L_0x00d0
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.bytecode.RuntimeAnnotationsAttr.printAnnotationElementValue(int, gnu.bytecode.ClassTypeWriter):void");
    }
}
