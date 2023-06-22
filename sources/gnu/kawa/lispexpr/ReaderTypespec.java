package gnu.kawa.lispexpr;

public class ReaderTypespec extends ReadTableEntry {
    public int getKind() {
        return 6;
    }

    /* JADX WARNING: type inference failed for: r8v14, types: [char[]] */
    /* JADX WARNING: type inference failed for: r1v7, types: [char] */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0057, code lost:
        if (1 != 1) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0060, code lost:
        if (0 != 0) goto L_0x0062;
     */
    /* JADX WARNING: Incorrect type for immutable var: ssa=char, code=int, for r1v7, types: [char] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object read(gnu.text.Lexer r12, int r13, int r14) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r11 = this;
            int r7 = r12.tokenBufferLength
            gnu.text.LineBufferedReader r3 = r12.getPort()
            gnu.kawa.lispexpr.ReadTable r5 = gnu.kawa.lispexpr.ReadTable.getCurrent()
            r6 = 0
            r12.tokenBufferAppend(r13)
            r1 = r13
            boolean r8 = r3 instanceof gnu.mapping.InPort
            if (r8 == 0) goto L_0x001e
            r8 = r3
            gnu.mapping.InPort r8 = (gnu.mapping.InPort) r8
            char r6 = r8.readState
            r8 = r3
            gnu.mapping.InPort r8 = (gnu.mapping.InPort) r8
            char r9 = (char) r13
            r8.readState = r9
        L_0x001e:
            r2 = 0
        L_0x001f:
            r4 = r1
            int r8 = r3.pos     // Catch:{ all -> 0x0071 }
            int r9 = r3.limit     // Catch:{ all -> 0x0071 }
            if (r8 >= r9) goto L_0x0045
            r8 = 10
            if (r4 == r8) goto L_0x0045
            char[] r8 = r3.buffer     // Catch:{ all -> 0x0071 }
            int r9 = r3.pos     // Catch:{ all -> 0x0071 }
            int r10 = r9 + 1
            r3.pos = r10     // Catch:{ all -> 0x0071 }
            char r1 = r8[r9]     // Catch:{ all -> 0x0071 }
        L_0x0034:
            r8 = 92
            if (r1 != r8) goto L_0x004f
            boolean r8 = r12 instanceof gnu.kawa.lispexpr.LispReader     // Catch:{ all -> 0x0071 }
            if (r8 == 0) goto L_0x004a
            r0 = r12
            gnu.kawa.lispexpr.LispReader r0 = (gnu.kawa.lispexpr.LispReader) r0     // Catch:{ all -> 0x0071 }
            r8 = r0
            int r1 = r8.readEscape()     // Catch:{ all -> 0x0071 }
            goto L_0x001f
        L_0x0045:
            int r1 = r3.read()     // Catch:{ all -> 0x0071 }
            goto L_0x0034
        L_0x004a:
            int r1 = r3.read()     // Catch:{ all -> 0x0071 }
            goto L_0x001f
        L_0x004f:
            if (r2 != 0) goto L_0x0059
            r8 = 91
            if (r1 != r8) goto L_0x0059
            r8 = 1
            r2 = 1
            if (r8 == r2) goto L_0x006d
        L_0x0059:
            if (r2 == 0) goto L_0x0062
            r8 = 93
            if (r1 != r8) goto L_0x0062
            r2 = 0
            if (r2 == 0) goto L_0x006d
        L_0x0062:
            gnu.kawa.lispexpr.ReadTableEntry r8 = r5.lookup(r1)     // Catch:{ all -> 0x0071 }
            int r8 = r8.getKind()     // Catch:{ all -> 0x0071 }
            r9 = 2
            if (r8 != r9) goto L_0x007d
        L_0x006d:
            r12.tokenBufferAppend(r1)     // Catch:{ all -> 0x0071 }
            goto L_0x001f
        L_0x0071:
            r8 = move-exception
            r12.tokenBufferLength = r7
            boolean r9 = r3 instanceof gnu.mapping.InPort
            if (r9 == 0) goto L_0x007c
            gnu.mapping.InPort r3 = (gnu.mapping.InPort) r3
            r3.readState = r6
        L_0x007c:
            throw r8
        L_0x007d:
            r12.unread(r1)     // Catch:{ all -> 0x0071 }
            java.lang.String r8 = new java.lang.String     // Catch:{ all -> 0x0071 }
            char[] r9 = r12.tokenBuffer     // Catch:{ all -> 0x0071 }
            int r10 = r12.tokenBufferLength     // Catch:{ all -> 0x0071 }
            int r10 = r10 - r7
            r8.<init>(r9, r7, r10)     // Catch:{ all -> 0x0071 }
            java.lang.String r8 = r8.intern()     // Catch:{ all -> 0x0071 }
            r12.tokenBufferLength = r7
            boolean r9 = r3 instanceof gnu.mapping.InPort
            if (r9 == 0) goto L_0x0098
            gnu.mapping.InPort r3 = (gnu.mapping.InPort) r3
            r3.readState = r6
        L_0x0098:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.lispexpr.ReaderTypespec.read(gnu.text.Lexer, int, int):java.lang.Object");
    }
}
