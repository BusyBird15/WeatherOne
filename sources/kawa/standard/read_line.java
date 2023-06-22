package kawa.standard;

public class read_line {
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002e, code lost:
        if (r18 == "trim") goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0034, code lost:
        if (r18 != "peek") goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003a, code lost:
        if (r18 != "peek") goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003c, code lost:
        r4 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003f, code lost:
        if (r2 != 10) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0041, code lost:
        r4 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0042, code lost:
        r17.pos = r10 + r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0052, code lost:
        if ((r10 + 1) >= r8) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005c, code lost:
        if (r1[r10 + 1] != 10) goto L_0x0060;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005e, code lost:
        r4 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0060, code lost:
        r4 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0066, code lost:
        if (r18 != "concat") goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006a, code lost:
        if (r2 != 10) goto L_0x0074;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x006c, code lost:
        r10 = r10 + 1;
        r17.pos = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
        return new gnu.lists.FString(r1, r13, r10 - r13);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object apply(gnu.text.LineBufferedReader r17, java.lang.String r18) throws java.io.IOException {
        /*
            int r2 = r17.read()
            if (r2 >= 0) goto L_0x0009
            java.lang.Object r3 = gnu.expr.Special.eof
        L_0x0008:
            return r3
        L_0x0009:
            r0 = r17
            int r15 = r0.pos
            int r13 = r15 + -1
            r10 = r13
            r0 = r17
            int r8 = r0.limit
            r0 = r17
            char[] r1 = r0.buffer
            r4 = -1
            r11 = r10
        L_0x001a:
            if (r11 >= r8) goto L_0x0073
            int r10 = r11 + 1
            char r2 = r1[r11]
            r15 = 13
            if (r2 == r15) goto L_0x0028
            r15 = 10
            if (r2 != r15) goto L_0x00fb
        L_0x0028:
            int r10 = r10 + -1
            java.lang.String r15 = "trim"
            r0 = r18
            if (r0 == r15) goto L_0x0036
            java.lang.String r15 = "peek"
            r0 = r18
            if (r0 != r15) goto L_0x0062
        L_0x0036:
            java.lang.String r15 = "peek"
            r0 = r18
            if (r0 != r15) goto L_0x003d
            r4 = 0
        L_0x003d:
            r15 = 10
            if (r2 != r15) goto L_0x0050
            r4 = 1
        L_0x0042:
            int r15 = r10 + r4
            r0 = r17
            r0.pos = r15
        L_0x0048:
            gnu.lists.FString r3 = new gnu.lists.FString
            int r15 = r10 - r13
            r3.<init>((char[]) r1, (int) r13, (int) r15)
            goto L_0x0008
        L_0x0050:
            int r15 = r10 + 1
            if (r15 >= r8) goto L_0x0074
            int r15 = r10 + 1
            char r15 = r1[r15]
            r16 = 10
            r0 = r16
            if (r15 != r0) goto L_0x0060
            r4 = 2
        L_0x005f:
            goto L_0x0042
        L_0x0060:
            r4 = 1
            goto L_0x005f
        L_0x0062:
            java.lang.String r15 = "concat"
            r0 = r18
            if (r0 != r15) goto L_0x0074
            r15 = 10
            if (r2 != r15) goto L_0x0074
            int r10 = r10 + 1
            r0 = r17
            r0.pos = r10
            goto L_0x0048
        L_0x0073:
            r10 = r11
        L_0x0074:
            java.lang.StringBuffer r12 = new java.lang.StringBuffer
            r15 = 100
            r12.<init>(r15)
            if (r10 <= r13) goto L_0x0082
            int r15 = r10 - r13
            r12.append(r1, r13, r15)
        L_0x0082:
            r0 = r17
            r0.pos = r10
            java.lang.String r15 = "peek"
            r0 = r18
            if (r0 != r15) goto L_0x00c3
            r9 = 80
        L_0x008e:
            r0 = r17
            r0.readLine(r12, r9)
            int r7 = r12.length()
            java.lang.String r15 = "split"
            r0 = r18
            if (r0 != r15) goto L_0x00a0
            if (r7 != 0) goto L_0x00d5
            r4 = 0
        L_0x00a0:
            gnu.lists.FString r3 = new gnu.lists.FString
            r15 = 0
            r3.<init>((java.lang.StringBuffer) r12, (int) r15, (int) r7)
            java.lang.String r15 = "split"
            r0 = r18
            if (r0 != r15) goto L_0x0008
            gnu.lists.FString r5 = new gnu.lists.FString
            int r15 = r7 - r4
            r5.<init>((java.lang.StringBuffer) r12, (int) r15, (int) r4)
            r15 = 2
            java.lang.Object[] r14 = new java.lang.Object[r15]
            r15 = 0
            r14[r15] = r3
            r15 = 1
            r14[r15] = r5
            gnu.mapping.Values r3 = new gnu.mapping.Values
            r3.<init>(r14)
            goto L_0x0008
        L_0x00c3:
            java.lang.String r15 = "concat"
            r0 = r18
            if (r0 == r15) goto L_0x00cf
            java.lang.String r15 = "split"
            r0 = r18
            if (r0 != r15) goto L_0x00d2
        L_0x00cf:
            r9 = 65
            goto L_0x008e
        L_0x00d2:
            r9 = 73
            goto L_0x008e
        L_0x00d5:
            int r15 = r7 + -1
            char r6 = r12.charAt(r15)
            r15 = 13
            if (r6 != r15) goto L_0x00e2
            r4 = 1
        L_0x00e0:
            int r7 = r7 - r4
            goto L_0x00a0
        L_0x00e2:
            r15 = 10
            if (r6 == r15) goto L_0x00e8
            r4 = 0
            goto L_0x00e0
        L_0x00e8:
            r15 = 2
            if (r6 <= r15) goto L_0x00f9
            int r15 = r7 + -2
            char r15 = r12.charAt(r15)
            r16 = 13
            r0 = r16
            if (r15 != r0) goto L_0x00f9
            r4 = 2
            goto L_0x00e0
        L_0x00f9:
            r4 = 1
            goto L_0x00e0
        L_0x00fb:
            r11 = r10
            goto L_0x001a
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.read_line.apply(gnu.text.LineBufferedReader, java.lang.String):java.lang.Object");
    }
}
