package gnu.kawa.functions;

import gnu.mapping.Procedure1;
import gnu.text.LineBufferedReader;
import gnu.text.ReportFormat;
import java.io.IOException;
import java.text.ParseException;

public class ParseFormat extends Procedure1 {
    public static final int PARAM_FROM_LIST = -1610612736;
    public static final int PARAM_UNSPECIFIED = -1073741824;
    public static final int SEEN_HASH = 16;
    public static final int SEEN_MINUS = 1;
    public static final int SEEN_PLUS = 2;
    public static final int SEEN_SPACE = 4;
    public static final int SEEN_ZERO = 8;
    public static final ParseFormat parseFormat = new ParseFormat(false);
    boolean emacsStyle = true;

    public ParseFormat(boolean emacsStyle2) {
        this.emacsStyle = emacsStyle2;
    }

    public ReportFormat parseFormat(LineBufferedReader fmt) throws ParseException, IOException {
        return parseFormat(fmt, this.emacsStyle ? '?' : '~');
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r23v0, resolved type: java.text.Format} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r23v1, resolved type: java.text.Format} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r23v2, resolved type: java.text.Format} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r23v3, resolved type: java.text.Format} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v3, resolved type: gnu.text.PadFormat} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r23v4, resolved type: java.text.Format} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v21, resolved type: gnu.text.PadFormat} */
    /* JADX WARNING: type inference failed for: r22v4 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static gnu.text.ReportFormat parseFormat(gnu.text.LineBufferedReader r31, char r32) throws java.text.ParseException, java.io.IOException {
        /*
            java.lang.StringBuffer r19 = new java.lang.StringBuffer
            r5 = 100
            r0 = r19
            r0.<init>(r5)
            r26 = 0
            java.util.Vector r24 = new java.util.Vector
            r24.<init>()
        L_0x0010:
            int r15 = r31.read()
            if (r15 < 0) goto L_0x0030
            r0 = r32
            if (r15 == r0) goto L_0x0021
            char r5 = (char) r15
            r0 = r19
            r0.append(r5)
            goto L_0x0010
        L_0x0021:
            int r15 = r31.read()
            r0 = r32
            if (r15 != r0) goto L_0x0030
            char r5 = (char) r15
            r0 = r19
            r0.append(r5)
            goto L_0x0010
        L_0x0030:
            int r25 = r19.length()
            if (r25 <= 0) goto L_0x0059
            r0 = r25
            char[] r0 = new char[r0]
            r29 = r0
            r5 = 0
            r6 = 0
            r0 = r19
            r1 = r25
            r2 = r29
            r0.getChars(r5, r1, r2, r6)
            r5 = 0
            r0 = r19
            r0.setLength(r5)
            gnu.text.LiteralFormat r5 = new gnu.text.LiteralFormat
            r0 = r29
            r5.<init>((char[]) r0)
            r0 = r24
            r0.addElement(r5)
        L_0x0059:
            if (r15 >= 0) goto L_0x0074
            int r20 = r24.size()
            r5 = 1
            r0 = r20
            if (r0 != r5) goto L_0x01d7
            r5 = 0
            r0 = r24
            java.lang.Object r17 = r0.elementAt(r5)
            r0 = r17
            boolean r5 = r0 instanceof gnu.text.ReportFormat
            if (r5 == 0) goto L_0x01d7
            gnu.text.ReportFormat r17 = (gnu.text.ReportFormat) r17
        L_0x0073:
            return r17
        L_0x0074:
            r5 = 36
            if (r15 != r5) goto L_0x00a1
            int r15 = r31.read()
            char r5 = (char) r15
            r6 = 10
            int r26 = java.lang.Character.digit(r5, r6)
            if (r26 >= 0) goto L_0x0092
            java.text.ParseException r5 = new java.text.ParseException
            java.lang.String r6 = "missing number (position) after '%$'"
            r7 = -1
            r5.<init>(r6, r7)
            throw r5
        L_0x008e:
            int r5 = r26 * 10
            int r26 = r5 + r16
        L_0x0092:
            int r15 = r31.read()
            char r5 = (char) r15
            r6 = 10
            int r16 = java.lang.Character.digit(r5, r6)
            if (r16 >= 0) goto L_0x008e
            int r26 = r26 + -1
        L_0x00a1:
            r21 = 0
        L_0x00a3:
            char r5 = (char) r15
            switch(r5) {
                case 32: goto L_0x00fa;
                case 35: goto L_0x0100;
                case 43: goto L_0x00f7;
                case 45: goto L_0x00f0;
                case 48: goto L_0x00fd;
                default: goto L_0x00a7;
            }
        L_0x00a7:
            r10 = -1073741824(0xffffffffc0000000, float:-2.0)
            char r5 = (char) r15
            r6 = 10
            int r16 = java.lang.Character.digit(r5, r6)
            if (r16 < 0) goto L_0x0108
            r10 = r16
        L_0x00b4:
            int r15 = r31.read()
            char r5 = (char) r15
            r6 = 10
            int r16 = java.lang.Character.digit(r5, r6)
            if (r16 >= 0) goto L_0x0103
        L_0x00c1:
            r4 = -1073741824(0xffffffffc0000000, float:-2.0)
            r5 = 46
            if (r15 != r5) goto L_0x00cd
            r5 = 42
            if (r15 != r5) goto L_0x010f
            r4 = -1610612736(0xffffffffa0000000, float:-1.0842022E-19)
        L_0x00cd:
            switch(r15) {
                case 83: goto L_0x0122;
                case 88: goto L_0x0156;
                case 100: goto L_0x0156;
                case 101: goto L_0x01bb;
                case 102: goto L_0x01bb;
                case 103: goto L_0x01bb;
                case 105: goto L_0x0156;
                case 111: goto L_0x0156;
                case 115: goto L_0x0122;
                case 120: goto L_0x0156;
                default: goto L_0x00d0;
            }
        L_0x00d0:
            java.text.ParseException r5 = new java.text.ParseException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "unknown format character '"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r15)
            java.lang.String r7 = "'"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r7 = -1
            r5.<init>(r6, r7)
            throw r5
        L_0x00f0:
            r21 = r21 | 1
        L_0x00f2:
            int r15 = r31.read()
            goto L_0x00a3
        L_0x00f7:
            r21 = r21 | 2
            goto L_0x00f2
        L_0x00fa:
            r21 = r21 | 4
            goto L_0x00f2
        L_0x00fd:
            r21 = r21 | 8
            goto L_0x00f2
        L_0x0100:
            r21 = r21 | 16
            goto L_0x00f2
        L_0x0103:
            int r5 = r10 * 10
            int r10 = r5 + r16
            goto L_0x00b4
        L_0x0108:
            r5 = 42
            if (r15 != r5) goto L_0x00c1
            r10 = -1610612736(0xffffffffa0000000, float:-1.0842022E-19)
            goto L_0x00c1
        L_0x010f:
            r4 = 0
        L_0x0110:
            int r15 = r31.read()
            char r5 = (char) r15
            r6 = 10
            int r16 = java.lang.Character.digit(r5, r6)
            if (r16 < 0) goto L_0x00cd
            int r5 = r4 * 10
            int r4 = r5 + r16
            goto L_0x0110
        L_0x0122:
            gnu.kawa.functions.ObjectFormat r22 = new gnu.kawa.functions.ObjectFormat
            r5 = 83
            if (r15 != r5) goto L_0x0154
            r5 = 1
        L_0x0129:
            r0 = r22
            r0.<init>(r5, r4)
            r23 = r22
        L_0x0130:
            if (r10 <= 0) goto L_0x01eb
            r5 = r21 & 8
            if (r5 == 0) goto L_0x01c7
            r11 = 48
        L_0x0138:
            r5 = r21 & 1
            if (r5 == 0) goto L_0x01cb
            r30 = 100
        L_0x013e:
            gnu.text.PadFormat r22 = new gnu.text.PadFormat
            r0 = r22
            r1 = r23
            r2 = r30
            r0.<init>(r1, r10, r11, r2)
        L_0x0149:
            r0 = r24
            r1 = r22
            r0.addElement(r1)
            int r26 = r26 + 1
            goto L_0x0010
        L_0x0154:
            r5 = 0
            goto L_0x0129
        L_0x0156:
            r8 = 0
            r5 = 100
            if (r15 == r5) goto L_0x015f
            r5 = 105(0x69, float:1.47E-43)
            if (r15 != r5) goto L_0x019a
        L_0x015f:
            r3 = 10
        L_0x0161:
            r28 = 0
            r27 = 0
            r5 = r21 & 9
            r6 = 8
            if (r5 != r6) goto L_0x01aa
            r11 = 48
        L_0x016d:
            r5 = r21 & 16
            if (r5 == 0) goto L_0x0173
            r8 = r8 | 8
        L_0x0173:
            r5 = r21 & 2
            if (r5 == 0) goto L_0x0179
            r8 = r8 | 2
        L_0x0179:
            r5 = r21 & 1
            if (r5 == 0) goto L_0x017f
            r8 = r8 | 16
        L_0x017f:
            r5 = r21 & 4
            if (r5 == 0) goto L_0x0185
            r8 = r8 | 4
        L_0x0185:
            r5 = -1073741824(0xffffffffc0000000, float:-2.0)
            if (r4 == r5) goto L_0x01ad
            r21 = r21 & -9
            r8 = r8 | 64
            r5 = 48
            r6 = -1073741824(0xffffffffc0000000, float:-2.0)
            r7 = -1073741824(0xffffffffc0000000, float:-2.0)
            java.text.Format r22 = gnu.kawa.functions.IntegerFormat.getInstance(r3, r4, r5, r6, r7, r8)
            r23 = r22
            goto L_0x0130
        L_0x019a:
            r5 = 111(0x6f, float:1.56E-43)
            if (r15 != r5) goto L_0x01a1
            r3 = 8
            goto L_0x0161
        L_0x01a1:
            r3 = 16
            r5 = 88
            if (r15 != r5) goto L_0x0161
            r8 = 32
            goto L_0x0161
        L_0x01aa:
            r11 = 32
            goto L_0x016d
        L_0x01ad:
            r12 = -1073741824(0xffffffffc0000000, float:-2.0)
            r13 = -1073741824(0xffffffffc0000000, float:-2.0)
            r9 = r3
            r14 = r8
            java.text.Format r22 = gnu.kawa.functions.IntegerFormat.getInstance(r9, r10, r11, r12, r13, r14)
            r23 = r22
            goto L_0x0130
        L_0x01bb:
            gnu.kawa.functions.ObjectFormat r22 = new gnu.kawa.functions.ObjectFormat
            r5 = 0
            r0 = r22
            r0.<init>(r5)
            r23 = r22
            goto L_0x0130
        L_0x01c7:
            r11 = 32
            goto L_0x0138
        L_0x01cb:
            r5 = 48
            if (r11 != r5) goto L_0x01d3
            r30 = -1
            goto L_0x013e
        L_0x01d3:
            r30 = 0
            goto L_0x013e
        L_0x01d7:
            r0 = r20
            java.text.Format[] r0 = new java.text.Format[r0]
            r18 = r0
            r0 = r24
            r1 = r18
            r0.copyInto(r1)
            gnu.text.CompoundFormat r17 = new gnu.text.CompoundFormat
            r17.<init>(r18)
            goto L_0x0073
        L_0x01eb:
            r22 = r23
            goto L_0x0149
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.functions.ParseFormat.parseFormat(gnu.text.LineBufferedReader, char):gnu.text.ReportFormat");
    }

    public Object apply1(Object arg) {
        return asFormat(arg, this.emacsStyle ? '?' : '~');
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static gnu.text.ReportFormat asFormat(java.lang.Object r7, char r8) {
        /*
            boolean r4 = r7 instanceof gnu.text.ReportFormat     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            if (r4 == 0) goto L_0x0007
            gnu.text.ReportFormat r7 = (gnu.text.ReportFormat) r7     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
        L_0x0006:
            return r7
        L_0x0007:
            r4 = 126(0x7e, float:1.77E-43)
            if (r8 != r4) goto L_0x0016
            gnu.kawa.functions.LispFormat r4 = new gnu.kawa.functions.LispFormat     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            java.lang.String r5 = r7.toString()     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            r4.<init>((java.lang.String) r5)     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            r7 = r4
            goto L_0x0006
        L_0x0016:
            boolean r4 = r7 instanceof gnu.lists.FString     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            if (r4 == 0) goto L_0x004f
            r0 = r7
            gnu.lists.FString r0 = (gnu.lists.FString) r0     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            r3 = r0
            gnu.mapping.CharArrayInPort r2 = new gnu.mapping.CharArrayInPort     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            char[] r4 = r3.data     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            int r5 = r3.size     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            r2.<init>(r4, r5)     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
        L_0x0027:
            gnu.text.ReportFormat r7 = parseFormat(r2, r8)     // Catch:{ all -> 0x0059 }
            r2.close()     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            goto L_0x0006
        L_0x002f:
            r1 = move-exception
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Error parsing format ("
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r1)
            java.lang.String r6 = ")"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        L_0x004f:
            gnu.mapping.CharArrayInPort r2 = new gnu.mapping.CharArrayInPort     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            java.lang.String r4 = r7.toString()     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            r2.<init>((java.lang.String) r4)     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            goto L_0x0027
        L_0x0059:
            r4 = move-exception
            r2.close()     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
            throw r4     // Catch:{ IOException -> 0x002f, ParseException -> 0x005e, IndexOutOfBoundsException -> 0x007e }
        L_0x005e:
            r1 = move-exception
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Invalid format ("
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r1)
            java.lang.String r6 = ")"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        L_0x007e:
            r1 = move-exception
            java.lang.RuntimeException r4 = new java.lang.RuntimeException
            java.lang.String r5 = "End while parsing format"
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.functions.ParseFormat.asFormat(java.lang.Object, char):gnu.text.ReportFormat");
    }
}
