package gnu.kawa.lispexpr;

public class ReaderIgnoreRestOfLine extends ReadTableEntry {
    static ReaderIgnoreRestOfLine instance = new ReaderIgnoreRestOfLine();

    public static ReaderIgnoreRestOfLine getInstance() {
        return instance;
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object read(gnu.text.Lexer r2, int r3, int r4) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r1 = this;
        L_0x0000:
            int r3 = r2.read()
            if (r3 >= 0) goto L_0x0009
            java.lang.Object r0 = gnu.lists.Sequence.eofValue
        L_0x0008:
            return r0
        L_0x0009:
            r0 = 10
            if (r3 == r0) goto L_0x0011
            r0 = 13
            if (r3 != r0) goto L_0x0000
        L_0x0011:
            gnu.mapping.Values r0 = gnu.mapping.Values.empty
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.lispexpr.ReaderIgnoreRestOfLine.read(gnu.text.Lexer, int, int):java.lang.Object");
    }
}
