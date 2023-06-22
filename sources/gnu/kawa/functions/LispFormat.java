package gnu.kawa.functions;

import gnu.lists.Pair;
import gnu.lists.Sequence;
import gnu.text.CompoundFormat;
import gnu.text.ReportFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Vector;

public class LispFormat extends CompoundFormat {
    public static final String paramFromCount = "<from count>";
    public static final String paramFromList = "<from list>";
    public static final String paramUnspecified = "<unspecified>";

    /* JADX WARNING: Code restructure failed: missing block: B:129:0x0344, code lost:
        throw new java.text.ParseException("saw ~) without matching ~(", r29);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x03d2, code lost:
        throw new java.text.ParseException("saw ~} without matching ~{", r29);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x0466, code lost:
        throw new java.text.ParseException("saw ~> without matching ~<", r29);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x0618, code lost:
        throw new java.text.ParseException("saw ~] without matching ~[", r29);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public LispFormat(char[] r49, int r50, int r51) throws java.text.ParseException {
        /*
            r48 = this;
            r11 = 0
            r12 = 0
            r0 = r48
            r0.<init>(r11, r12)
            r46 = -1
            r23 = 0
            java.lang.StringBuffer r33 = new java.lang.StringBuffer
            r11 = 100
            r0 = r33
            r0.<init>(r11)
            java.util.Stack r44 = new java.util.Stack
            r44.<init>()
            int r32 = r50 + r51
            r28 = r50
            r29 = r28
        L_0x001f:
            r0 = r29
            r1 = r32
            if (r0 >= r1) goto L_0x002b
            char r11 = r49[r29]
            r12 = 126(0x7e, float:1.77E-43)
            if (r11 != r12) goto L_0x0043
        L_0x002b:
            int r11 = r33.length()
            if (r11 <= 0) goto L_0x0043
            gnu.text.LiteralFormat r11 = new gnu.text.LiteralFormat
            r0 = r33
            r11.<init>((java.lang.StringBuffer) r0)
            r0 = r44
            r0.push(r11)
            r11 = 0
            r0 = r33
            r0.setLength(r11)
        L_0x0043:
            r0 = r29
            r1 = r32
            if (r0 < r1) goto L_0x0055
            r0 = r29
            r1 = r32
            if (r0 <= r1) goto L_0x078f
            java.lang.IndexOutOfBoundsException r11 = new java.lang.IndexOutOfBoundsException
            r11.<init>()
            throw r11
        L_0x0055:
            int r28 = r29 + 1
            char r21 = r49[r29]
            r11 = 126(0x7e, float:1.77E-43)
            r0 = r21
            if (r0 == r11) goto L_0x0069
            r0 = r33
            r1 = r21
            r0.append(r1)
            r29 = r28
            goto L_0x001f
        L_0x0069:
            int r43 = r44.size()
            int r29 = r28 + 1
            char r21 = r49[r28]
        L_0x0071:
            r11 = 35
            r0 = r21
            if (r0 != r11) goto L_0x00a1
            java.lang.String r11 = "<from count>"
            r0 = r44
            r0.push(r11)
            int r28 = r29 + 1
            char r21 = r49[r29]
            r29 = r28
        L_0x0084:
            r11 = 44
            r0 = r21
            if (r0 == r11) goto L_0x0145
            r28 = r29
        L_0x008c:
            r42 = 0
            r41 = 0
            r29 = r28
        L_0x0092:
            r11 = 58
            r0 = r21
            if (r0 != r11) goto L_0x014d
            r42 = 1
        L_0x009a:
            int r28 = r29 + 1
            char r21 = r49[r29]
            r29 = r28
            goto L_0x0092
        L_0x00a1:
            r11 = 118(0x76, float:1.65E-43)
            r0 = r21
            if (r0 == r11) goto L_0x00ad
            r11 = 86
            r0 = r21
            if (r0 != r11) goto L_0x00bb
        L_0x00ad:
            java.lang.String r11 = "<from list>"
            r0 = r44
            r0.push(r11)
            int r28 = r29 + 1
            char r21 = r49[r29]
            r29 = r28
            goto L_0x0084
        L_0x00bb:
            r11 = 45
            r0 = r21
            if (r0 == r11) goto L_0x00cb
            r11 = 10
            r0 = r21
            int r11 = java.lang.Character.digit(r0, r11)
            if (r11 < 0) goto L_0x011d
        L_0x00cb:
            r11 = 45
            r0 = r21
            if (r0 != r11) goto L_0x0100
            r34 = 1
        L_0x00d3:
            if (r34 == 0) goto L_0x07c3
            int r28 = r29 + 1
            char r21 = r49[r29]
        L_0x00d9:
            r47 = 0
            r45 = r28
        L_0x00dd:
            r11 = 10
            r0 = r21
            int r26 = java.lang.Character.digit(r0, r11)
            if (r26 >= 0) goto L_0x0103
            int r11 = r28 - r45
            r12 = 8
            if (r11 >= r12) goto L_0x010e
            if (r34 == 0) goto L_0x00f4
            r0 = r47
            int r0 = -r0
            r47 = r0
        L_0x00f4:
            gnu.math.IntNum r11 = gnu.math.IntNum.make((int) r47)
        L_0x00f8:
            r0 = r44
            r0.push(r11)
            r29 = r28
            goto L_0x0084
        L_0x0100:
            r34 = 0
            goto L_0x00d3
        L_0x0103:
            int r11 = r47 * 10
            int r47 = r11 + r26
            int r29 = r28 + 1
            char r21 = r49[r28]
            r28 = r29
            goto L_0x00dd
        L_0x010e:
            int r11 = r28 - r45
            r12 = 10
            r0 = r49
            r1 = r45
            r2 = r34
            gnu.math.IntNum r11 = gnu.math.IntNum.valueOf(r0, r1, r11, r12, r2)
            goto L_0x00f8
        L_0x011d:
            r11 = 39
            r0 = r21
            if (r0 != r11) goto L_0x0136
            int r28 = r29 + 1
            char r11 = r49[r29]
            gnu.text.Char r11 = gnu.text.Char.make(r11)
            r0 = r44
            r0.push(r11)
            int r29 = r28 + 1
            char r21 = r49[r28]
            goto L_0x0084
        L_0x0136:
            r11 = 44
            r0 = r21
            if (r0 != r11) goto L_0x07bf
            java.lang.String r11 = "<unspecified>"
            r0 = r44
            r0.push(r11)
            goto L_0x0084
        L_0x0145:
            int r28 = r29 + 1
            char r21 = r49[r29]
            r29 = r28
            goto L_0x0071
        L_0x014d:
            r11 = 64
            r0 = r21
            if (r0 != r11) goto L_0x0157
            r41 = 1
            goto L_0x009a
        L_0x0157:
            char r21 = java.lang.Character.toUpperCase(r21)
            int r11 = r44.size()
            int r36 = r11 - r43
            switch(r21) {
                case 10: goto L_0x0686;
                case 33: goto L_0x06a7;
                case 36: goto L_0x01f6;
                case 37: goto L_0x0775;
                case 38: goto L_0x06d4;
                case 40: goto L_0x02f7;
                case 41: goto L_0x032d;
                case 42: goto L_0x02e4;
                case 59: goto L_0x0591;
                case 60: goto L_0x0403;
                case 62: goto L_0x044f;
                case 63: goto L_0x036a;
                case 65: goto L_0x0287;
                case 66: goto L_0x0181;
                case 67: goto L_0x02ca;
                case 68: goto L_0x0181;
                case 69: goto L_0x01f6;
                case 70: goto L_0x01f6;
                case 71: goto L_0x01f6;
                case 73: goto L_0x06e5;
                case 79: goto L_0x0181;
                case 80: goto L_0x01ed;
                case 82: goto L_0x0181;
                case 83: goto L_0x0287;
                case 84: goto L_0x06ad;
                case 87: goto L_0x0287;
                case 88: goto L_0x0181;
                case 89: goto L_0x0287;
                case 91: goto L_0x053e;
                case 93: goto L_0x0601;
                case 94: goto L_0x0661;
                case 95: goto L_0x06ff;
                case 123: goto L_0x0383;
                case 124: goto L_0x0740;
                case 125: goto L_0x03bb;
                case 126: goto L_0x0735;
                default: goto L_0x0164;
            }
        L_0x0164:
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r15 = "unrecognized format specifier ~"
            java.lang.StringBuilder r12 = r12.append(r15)
            r0 = r21
            java.lang.StringBuilder r12 = r12.append(r0)
            java.lang.String r12 = r12.toString()
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x0181:
            r18 = r43
            r11 = 82
            r0 = r21
            if (r0 != r11) goto L_0x01d0
            int r19 = r18 + 1
            r0 = r44
            r1 = r18
            int r4 = getParam(r0, r1)
            r18 = r19
        L_0x0195:
            r0 = r44
            r1 = r18
            int r5 = getParam(r0, r1)
            int r11 = r18 + 1
            r0 = r44
            int r6 = getParam(r0, r11)
            int r11 = r18 + 2
            r0 = r44
            int r7 = getParam(r0, r11)
            int r11 = r18 + 3
            r0 = r44
            int r8 = getParam(r0, r11)
            r9 = 0
            if (r42 == 0) goto L_0x01ba
            r9 = r9 | 1
        L_0x01ba:
            if (r41 == 0) goto L_0x01be
            r9 = r9 | 2
        L_0x01be:
            java.text.Format r10 = gnu.kawa.functions.IntegerFormat.getInstance(r4, r5, r6, r7, r8, r9)
        L_0x01c2:
            r0 = r44
            r1 = r43
            r0.setSize(r1)
            r0 = r44
            r0.push(r10)
            goto L_0x001f
        L_0x01d0:
            r11 = 68
            r0 = r21
            if (r0 != r11) goto L_0x01d9
            r4 = 10
            goto L_0x0195
        L_0x01d9:
            r11 = 79
            r0 = r21
            if (r0 != r11) goto L_0x01e2
            r4 = 8
            goto L_0x0195
        L_0x01e2:
            r11 = 88
            r0 = r21
            if (r0 != r11) goto L_0x01eb
            r4 = 16
            goto L_0x0195
        L_0x01eb:
            r4 = 2
            goto L_0x0195
        L_0x01ed:
            r0 = r42
            r1 = r41
            gnu.kawa.functions.LispPluralFormat r10 = gnu.kawa.functions.LispPluralFormat.getInstance(r0, r1)
            goto L_0x01c2
        L_0x01f6:
            gnu.kawa.functions.LispRealFormat r25 = new gnu.kawa.functions.LispRealFormat
            r25.<init>()
            r0 = r21
            r1 = r25
            r1.op = r0
            r0 = r44
            r1 = r43
            int r11 = getParam(r0, r1)
            r0 = r25
            r0.arg1 = r11
            int r11 = r43 + 1
            r0 = r44
            int r11 = getParam(r0, r11)
            r0 = r25
            r0.arg2 = r11
            int r11 = r43 + 2
            r0 = r44
            int r11 = getParam(r0, r11)
            r0 = r25
            r0.arg3 = r11
            int r11 = r43 + 3
            r0 = r44
            int r11 = getParam(r0, r11)
            r0 = r25
            r0.arg4 = r11
            r11 = 36
            r0 = r21
            if (r0 == r11) goto L_0x0267
            int r11 = r43 + 4
            r0 = r44
            int r11 = getParam(r0, r11)
            r0 = r25
            r0.arg5 = r11
            r11 = 69
            r0 = r21
            if (r0 == r11) goto L_0x024f
            r11 = 71
            r0 = r21
            if (r0 != r11) goto L_0x0267
        L_0x024f:
            int r11 = r43 + 5
            r0 = r44
            int r11 = getParam(r0, r11)
            r0 = r25
            r0.arg6 = r11
            int r11 = r43 + 6
            r0 = r44
            int r11 = getParam(r0, r11)
            r0 = r25
            r0.arg7 = r11
        L_0x0267:
            r0 = r41
            r1 = r25
            r1.showPlus = r0
            r0 = r42
            r1 = r25
            r1.internalPad = r0
            r0 = r25
            int r11 = r0.argsUsed
            if (r11 != 0) goto L_0x0283
            r11 = 0
            r12 = 0
            r0 = r25
            java.text.Format r10 = r0.resolve(r11, r12)
            goto L_0x01c2
        L_0x0283:
            r10 = r25
            goto L_0x01c2
        L_0x0287:
            r11 = 65
            r0 = r21
            if (r0 == r11) goto L_0x02c5
            r11 = 1
        L_0x028e:
            gnu.kawa.functions.ObjectFormat r27 = gnu.kawa.functions.ObjectFormat.getInstance(r11)
            if (r36 <= 0) goto L_0x07bb
            r0 = r44
            r1 = r43
            int r5 = getParam(r0, r1)
            int r11 = r43 + 1
            r0 = r44
            int r13 = getParam(r0, r11)
            int r11 = r43 + 2
            r0 = r44
            int r14 = getParam(r0, r11)
            int r11 = r43 + 3
            r0 = r44
            int r6 = getParam(r0, r11)
            gnu.kawa.functions.LispObjectFormat r10 = new gnu.kawa.functions.LispObjectFormat
            r11 = r27
            gnu.text.ReportFormat r11 = (gnu.text.ReportFormat) r11
            if (r41 == 0) goto L_0x02c7
            r16 = 0
        L_0x02be:
            r12 = r5
            r15 = r6
            r10.<init>(r11, r12, r13, r14, r15, r16)
            goto L_0x01c2
        L_0x02c5:
            r11 = 0
            goto L_0x028e
        L_0x02c7:
            r16 = 100
            goto L_0x02be
        L_0x02ca:
            if (r36 <= 0) goto L_0x02e1
            r0 = r44
            r1 = r43
            int r22 = getParam(r0, r1)
        L_0x02d4:
            r11 = 1
            r0 = r22
            r1 = r41
            r2 = r42
            gnu.kawa.functions.LispCharacterFormat r10 = gnu.kawa.functions.LispCharacterFormat.getInstance(r0, r11, r1, r2)
            goto L_0x01c2
        L_0x02e1:
            r22 = -1610612736(0xffffffffa0000000, float:-1.0842022E-19)
            goto L_0x02d4
        L_0x02e4:
            gnu.kawa.functions.LispRepositionFormat r10 = new gnu.kawa.functions.LispRepositionFormat
            r0 = r44
            r1 = r43
            int r11 = getParam(r0, r1)
            r0 = r42
            r1 = r41
            r10.<init>(r11, r0, r1)
            goto L_0x01c2
        L_0x02f7:
            if (r42 == 0) goto L_0x0325
            if (r41 == 0) goto L_0x0322
            r21 = 85
        L_0x02fd:
            gnu.text.CaseConvertFormat r20 = new gnu.text.CaseConvertFormat
            r11 = 0
            r0 = r20
            r1 = r21
            r0.<init>(r11, r1)
            r0 = r44
            r1 = r43
            r0.setSize(r1)
            r0 = r44
            r1 = r20
            r0.push(r1)
            gnu.math.IntNum r11 = gnu.math.IntNum.make((int) r46)
            r0 = r44
            r0.push(r11)
            r46 = r43
            goto L_0x001f
        L_0x0322:
            r21 = 67
            goto L_0x02fd
        L_0x0325:
            if (r41 == 0) goto L_0x032a
            r21 = 84
            goto L_0x02fd
        L_0x032a:
            r21 = 76
            goto L_0x02fd
        L_0x032d:
            if (r46 < 0) goto L_0x033b
            r0 = r44
            r1 = r46
            java.lang.Object r11 = r0.elementAt(r1)
            boolean r11 = r11 instanceof gnu.text.CaseConvertFormat
            if (r11 != 0) goto L_0x0345
        L_0x033b:
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "saw ~) without matching ~("
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x0345:
            r0 = r44
            r1 = r46
            java.lang.Object r20 = r0.elementAt(r1)
            gnu.text.CaseConvertFormat r20 = (gnu.text.CaseConvertFormat) r20
            int r11 = r46 + 2
            r0 = r44
            r1 = r43
            java.text.Format r11 = popFormats(r0, r11, r1)
            r0 = r20
            r0.setBaseFormat(r11)
            java.lang.Object r11 = r44.pop()
            gnu.math.IntNum r11 = (gnu.math.IntNum) r11
            int r46 = r11.intValue()
            goto L_0x001f
        L_0x036a:
            gnu.kawa.functions.LispIterationFormat r31 = new gnu.kawa.functions.LispIterationFormat
            r31.<init>()
            r0 = r41
            r1 = r31
            r1.seenAt = r0
            r11 = 1
            r0 = r31
            r0.maxIterations = r11
            r11 = 1
            r0 = r31
            r0.atLeastOnce = r11
            r10 = r31
            goto L_0x01c2
        L_0x0383:
            gnu.kawa.functions.LispIterationFormat r31 = new gnu.kawa.functions.LispIterationFormat
            r31.<init>()
            r0 = r41
            r1 = r31
            r1.seenAt = r0
            r0 = r42
            r1 = r31
            r1.seenColon = r0
            r0 = r44
            r1 = r43
            int r11 = getParam(r0, r1)
            r0 = r31
            r0.maxIterations = r11
            r0 = r44
            r1 = r43
            r0.setSize(r1)
            r0 = r44
            r1 = r31
            r0.push(r1)
            gnu.math.IntNum r11 = gnu.math.IntNum.make((int) r46)
            r0 = r44
            r0.push(r11)
            r46 = r43
            goto L_0x001f
        L_0x03bb:
            if (r46 < 0) goto L_0x03c9
            r0 = r44
            r1 = r46
            java.lang.Object r11 = r0.elementAt(r1)
            boolean r11 = r11 instanceof gnu.kawa.functions.LispIterationFormat
            if (r11 != 0) goto L_0x03d3
        L_0x03c9:
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "saw ~} without matching ~{"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x03d3:
            r0 = r44
            r1 = r46
            java.lang.Object r31 = r0.elementAt(r1)
            gnu.kawa.functions.LispIterationFormat r31 = (gnu.kawa.functions.LispIterationFormat) r31
            r0 = r42
            r1 = r31
            r1.atLeastOnce = r0
            int r11 = r46 + 2
            r0 = r43
            if (r0 <= r11) goto L_0x03f7
            int r11 = r46 + 2
            r0 = r44
            r1 = r43
            java.text.Format r11 = popFormats(r0, r11, r1)
            r0 = r31
            r0.body = r11
        L_0x03f7:
            java.lang.Object r11 = r44.pop()
            gnu.math.IntNum r11 = (gnu.math.IntNum) r11
            int r46 = r11.intValue()
            goto L_0x001f
        L_0x0403:
            gnu.kawa.functions.LispPrettyFormat r40 = new gnu.kawa.functions.LispPrettyFormat
            r40.<init>()
            r0 = r41
            r1 = r40
            r1.seenAt = r0
            if (r42 == 0) goto L_0x0442
            java.lang.String r11 = "("
            r0 = r40
            r0.prefix = r11
            java.lang.String r11 = ")"
            r0 = r40
            r0.suffix = r11
        L_0x041c:
            r0 = r44
            r1 = r43
            r0.setSize(r1)
            r0 = r44
            r1 = r40
            r0.push(r1)
            gnu.math.IntNum r11 = gnu.math.IntNum.make((int) r46)
            r0 = r44
            r0.push(r11)
            gnu.math.IntNum r11 = gnu.math.IntNum.make((int) r23)
            r0 = r44
            r0.push(r11)
            r46 = r43
            r23 = 0
            goto L_0x001f
        L_0x0442:
            java.lang.String r11 = ""
            r0 = r40
            r0.prefix = r11
            java.lang.String r11 = ""
            r0 = r40
            r0.suffix = r11
            goto L_0x041c
        L_0x044f:
            if (r46 < 0) goto L_0x045d
            r0 = r44
            r1 = r46
            java.lang.Object r11 = r0.elementAt(r1)
            boolean r11 = r11 instanceof gnu.kawa.functions.LispPrettyFormat
            if (r11 != 0) goto L_0x0467
        L_0x045d:
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "saw ~> without matching ~<"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x0467:
            int r11 = r46 + 3
            int r11 = r11 + r23
            r0 = r44
            r1 = r43
            java.text.Format r10 = popFormats(r0, r11, r1)
            r0 = r44
            r0.push(r10)
            r0 = r44
            r1 = r46
            java.lang.Object r40 = r0.elementAt(r1)
            gnu.kawa.functions.LispPrettyFormat r40 = (gnu.kawa.functions.LispPrettyFormat) r40
            int r11 = r46 + 3
            int r12 = r44.size()
            r0 = r44
            java.text.Format[] r11 = getFormats(r0, r11, r12)
            r0 = r40
            r0.segments = r11
            int r11 = r46 + 3
            r0 = r44
            r0.setSize(r11)
            java.lang.Object r11 = r44.pop()
            gnu.math.IntNum r11 = (gnu.math.IntNum) r11
            int r46 = r11.intValue()
            java.lang.Object r11 = r44.pop()
            gnu.math.IntNum r11 = (gnu.math.IntNum) r11
            int r46 = r11.intValue()
            if (r42 == 0) goto L_0x0534
            r0 = r40
            java.text.Format[] r11 = r0.segments
            int r0 = r11.length
            r35 = r0
            r11 = 3
            r0 = r35
            if (r0 <= r11) goto L_0x04c5
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "too many segments in Logical Block format"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x04c5:
            r11 = 2
            r0 = r35
            if (r0 < r11) goto L_0x0515
            r0 = r40
            java.text.Format[] r11 = r0.segments
            r12 = 0
            r11 = r11[r12]
            boolean r11 = r11 instanceof gnu.text.LiteralFormat
            if (r11 != 0) goto L_0x04df
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "prefix segment is not literal"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x04df:
            r0 = r40
            java.text.Format[] r11 = r0.segments
            r12 = 0
            r11 = r11[r12]
            gnu.text.LiteralFormat r11 = (gnu.text.LiteralFormat) r11
            java.lang.String r11 = r11.content()
            r0 = r40
            r0.prefix = r11
            r0 = r40
            java.text.Format[] r11 = r0.segments
            r12 = 1
            r11 = r11[r12]
            r0 = r40
            r0.body = r11
        L_0x04fb:
            r11 = 3
            r0 = r35
            if (r0 < r11) goto L_0x001f
            r0 = r40
            java.text.Format[] r11 = r0.segments
            r12 = 2
            r11 = r11[r12]
            boolean r11 = r11 instanceof gnu.text.LiteralFormat
            if (r11 != 0) goto L_0x0521
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "suffix segment is not literal"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x0515:
            r0 = r40
            java.text.Format[] r11 = r0.segments
            r12 = 0
            r11 = r11[r12]
            r0 = r40
            r0.body = r11
            goto L_0x04fb
        L_0x0521:
            r0 = r40
            java.text.Format[] r11 = r0.segments
            r12 = 2
            r11 = r11[r12]
            gnu.text.LiteralFormat r11 = (gnu.text.LiteralFormat) r11
            java.lang.String r11 = r11.content()
            r0 = r40
            r0.suffix = r11
            goto L_0x001f
        L_0x0534:
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "not implemented: justfication i.e. ~<...~>"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x053e:
            gnu.kawa.functions.LispChoiceFormat r17 = new gnu.kawa.functions.LispChoiceFormat
            r17.<init>()
            r0 = r44
            r1 = r43
            int r11 = getParam(r0, r1)
            r0 = r17
            r0.param = r11
            r0 = r17
            int r11 = r0.param
            r12 = -1073741824(0xffffffffc0000000, float:-2.0)
            if (r11 != r12) goto L_0x055d
            r11 = -1610612736(0xffffffffa0000000, float:-1.0842022E-19)
            r0 = r17
            r0.param = r11
        L_0x055d:
            if (r42 == 0) goto L_0x0564
            r11 = 1
            r0 = r17
            r0.testBoolean = r11
        L_0x0564:
            if (r41 == 0) goto L_0x056b
            r11 = 1
            r0 = r17
            r0.skipIfFalse = r11
        L_0x056b:
            r0 = r44
            r1 = r43
            r0.setSize(r1)
            r0 = r44
            r1 = r17
            r0.push(r1)
            gnu.math.IntNum r11 = gnu.math.IntNum.make((int) r46)
            r0 = r44
            r0.push(r11)
            gnu.math.IntNum r11 = gnu.math.IntNum.make((int) r23)
            r0 = r44
            r0.push(r11)
            r46 = r43
            r23 = 0
            goto L_0x001f
        L_0x0591:
            if (r46 < 0) goto L_0x05f7
            r0 = r44
            r1 = r46
            java.lang.Object r11 = r0.elementAt(r1)
            boolean r11 = r11 instanceof gnu.kawa.functions.LispChoiceFormat
            if (r11 == 0) goto L_0x05c5
            r0 = r44
            r1 = r46
            java.lang.Object r17 = r0.elementAt(r1)
            gnu.kawa.functions.LispChoiceFormat r17 = (gnu.kawa.functions.LispChoiceFormat) r17
            if (r42 == 0) goto L_0x05b0
            r11 = 1
            r0 = r17
            r0.lastIsDefault = r11
        L_0x05b0:
            int r11 = r46 + 3
            int r11 = r11 + r23
            r0 = r44
            r1 = r43
            java.text.Format r10 = popFormats(r0, r11, r1)
            r0 = r44
            r0.push(r10)
            int r23 = r23 + 1
            goto L_0x001f
        L_0x05c5:
            r0 = r44
            r1 = r46
            java.lang.Object r11 = r0.elementAt(r1)
            boolean r11 = r11 instanceof gnu.kawa.functions.LispPrettyFormat
            if (r11 == 0) goto L_0x05f7
            r0 = r44
            r1 = r46
            java.lang.Object r40 = r0.elementAt(r1)
            gnu.kawa.functions.LispPrettyFormat r40 = (gnu.kawa.functions.LispPrettyFormat) r40
            if (r41 == 0) goto L_0x05e2
            r11 = 1
            r0 = r40
            r0.perLine = r11
        L_0x05e2:
            int r11 = r46 + 3
            int r11 = r11 + r23
            r0 = r44
            r1 = r43
            java.text.Format r10 = popFormats(r0, r11, r1)
            r0 = r44
            r0.push(r10)
            int r23 = r23 + 1
            goto L_0x001f
        L_0x05f7:
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "saw ~; without matching ~[ or ~<"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x0601:
            if (r46 < 0) goto L_0x060f
            r0 = r44
            r1 = r46
            java.lang.Object r11 = r0.elementAt(r1)
            boolean r11 = r11 instanceof gnu.kawa.functions.LispChoiceFormat
            if (r11 != 0) goto L_0x0619
        L_0x060f:
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "saw ~] without matching ~["
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x0619:
            int r11 = r46 + 3
            int r11 = r11 + r23
            r0 = r44
            r1 = r43
            java.text.Format r10 = popFormats(r0, r11, r1)
            r0 = r44
            r0.push(r10)
            r0 = r44
            r1 = r46
            java.lang.Object r17 = r0.elementAt(r1)
            gnu.kawa.functions.LispChoiceFormat r17 = (gnu.kawa.functions.LispChoiceFormat) r17
            int r11 = r46 + 3
            int r12 = r44.size()
            r0 = r44
            java.text.Format[] r11 = getFormats(r0, r11, r12)
            r0 = r17
            r0.choices = r11
            int r11 = r46 + 3
            r0 = r44
            r0.setSize(r11)
            java.lang.Object r11 = r44.pop()
            gnu.math.IntNum r11 = (gnu.math.IntNum) r11
            int r23 = r11.intValue()
            java.lang.Object r11 = r44.pop()
            gnu.math.IntNum r11 = (gnu.math.IntNum) r11
            int r46 = r11.intValue()
            goto L_0x001f
        L_0x0661:
            r0 = r44
            r1 = r43
            int r37 = getParam(r0, r1)
            int r11 = r43 + 1
            r0 = r44
            int r38 = getParam(r0, r11)
            int r11 = r43 + 2
            r0 = r44
            int r39 = getParam(r0, r11)
            gnu.kawa.functions.LispEscapeFormat r10 = new gnu.kawa.functions.LispEscapeFormat
            r0 = r37
            r1 = r38
            r2 = r39
            r10.<init>(r0, r1, r2)
            goto L_0x01c2
        L_0x0686:
            if (r41 == 0) goto L_0x068f
            r0 = r33
            r1 = r21
            r0.append(r1)
        L_0x068f:
            if (r42 != 0) goto L_0x001f
        L_0x0691:
            r0 = r29
            r1 = r32
            if (r0 >= r1) goto L_0x001f
            int r28 = r29 + 1
            char r21 = r49[r29]
            boolean r11 = java.lang.Character.isWhitespace(r21)
            if (r11 != 0) goto L_0x07b7
            int r28 = r28 + -1
            r29 = r28
            goto L_0x001f
        L_0x06a7:
            gnu.text.FlushFormat r10 = gnu.text.FlushFormat.getInstance()
            goto L_0x01c2
        L_0x06ad:
            r0 = r44
            r1 = r43
            int r37 = getParam(r0, r1)
            int r11 = r43 + 1
            r0 = r44
            int r38 = getParam(r0, r11)
            int r11 = r43 + 2
            r0 = r44
            int r39 = getParam(r0, r11)
            gnu.kawa.functions.LispTabulateFormat r10 = new gnu.kawa.functions.LispTabulateFormat
            r0 = r37
            r1 = r38
            r2 = r39
            r3 = r41
            r10.<init>(r0, r1, r2, r3)
            goto L_0x01c2
        L_0x06d4:
            r0 = r44
            r1 = r43
            int r37 = getParam(r0, r1)
            gnu.kawa.functions.LispFreshlineFormat r10 = new gnu.kawa.functions.LispFreshlineFormat
            r0 = r37
            r10.<init>(r0)
            goto L_0x01c2
        L_0x06e5:
            r0 = r44
            r1 = r43
            int r37 = getParam(r0, r1)
            r11 = -1073741824(0xffffffffc0000000, float:-2.0)
            r0 = r37
            if (r0 != r11) goto L_0x06f5
            r37 = 0
        L_0x06f5:
            r0 = r37
            r1 = r42
            gnu.kawa.functions.LispIndentFormat r10 = gnu.kawa.functions.LispIndentFormat.getInstance(r0, r1)
            goto L_0x01c2
        L_0x06ff:
            r0 = r44
            r1 = r43
            int r37 = getParam(r0, r1)
            r11 = -1073741824(0xffffffffc0000000, float:-2.0)
            r0 = r37
            if (r0 != r11) goto L_0x070f
            r37 = 1
        L_0x070f:
            if (r42 == 0) goto L_0x0725
            if (r41 == 0) goto L_0x0725
            r22 = 10
        L_0x0715:
            if (r41 == 0) goto L_0x0728
            if (r42 == 0) goto L_0x0728
            r30 = 82
        L_0x071b:
            r0 = r37
            r1 = r30
            gnu.kawa.functions.LispNewlineFormat r10 = gnu.kawa.functions.LispNewlineFormat.getInstance(r0, r1)
            goto L_0x01c2
        L_0x0725:
            r22 = 32
            goto L_0x0715
        L_0x0728:
            if (r41 == 0) goto L_0x072d
            r30 = 77
            goto L_0x071b
        L_0x072d:
            if (r42 == 0) goto L_0x0732
            r30 = 70
            goto L_0x071b
        L_0x0732:
            r30 = 78
            goto L_0x071b
        L_0x0735:
            if (r36 != 0) goto L_0x0740
            r0 = r33
            r1 = r21
            r0.append(r1)
            goto L_0x001f
        L_0x0740:
            r0 = r44
            r1 = r43
            int r24 = getParam(r0, r1)
            r11 = -1073741824(0xffffffffc0000000, float:-2.0)
            r0 = r24
            if (r0 != r11) goto L_0x0750
            r24 = 1
        L_0x0750:
            int r11 = r43 + 1
            r0 = r44
            int r22 = getParam(r0, r11)
            r11 = -1073741824(0xffffffffc0000000, float:-2.0)
            r0 = r22
            if (r0 != r11) goto L_0x0766
            r11 = 124(0x7c, float:1.74E-43)
            r0 = r21
            if (r0 != r11) goto L_0x0772
            r22 = 12
        L_0x0766:
            r11 = 0
            r12 = 0
            r0 = r22
            r1 = r24
            gnu.kawa.functions.LispCharacterFormat r10 = gnu.kawa.functions.LispCharacterFormat.getInstance(r0, r1, r11, r12)
            goto L_0x01c2
        L_0x0772:
            r22 = 126(0x7e, float:1.77E-43)
            goto L_0x0766
        L_0x0775:
            r0 = r44
            r1 = r43
            int r24 = getParam(r0, r1)
            r11 = -1073741824(0xffffffffc0000000, float:-2.0)
            r0 = r24
            if (r0 != r11) goto L_0x0785
            r24 = 1
        L_0x0785:
            r11 = 76
            r0 = r24
            gnu.kawa.functions.LispNewlineFormat r10 = gnu.kawa.functions.LispNewlineFormat.getInstance(r0, r11)
            goto L_0x01c2
        L_0x078f:
            if (r46 < 0) goto L_0x079b
            java.text.ParseException r11 = new java.text.ParseException
            java.lang.String r12 = "missing ~] or ~}"
            r0 = r29
            r11.<init>(r12, r0)
            throw r11
        L_0x079b:
            int r11 = r44.size()
            r0 = r48
            r0.length = r11
            r0 = r48
            int r11 = r0.length
            java.text.Format[] r11 = new java.text.Format[r11]
            r0 = r48
            r0.formats = r11
            r0 = r48
            java.text.Format[] r11 = r0.formats
            r0 = r44
            r0.copyInto(r11)
            return
        L_0x07b7:
            r29 = r28
            goto L_0x0691
        L_0x07bb:
            r10 = r27
            goto L_0x01c2
        L_0x07bf:
            r28 = r29
            goto L_0x008c
        L_0x07c3:
            r28 = r29
            goto L_0x00d9
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.functions.LispFormat.<init>(char[], int, int):void");
    }

    static Format[] getFormats(Vector vector, int start, int end) {
        Format[] f = new Format[(end - start)];
        for (int i = start; i < end; i++) {
            f[i - start] = (Format) vector.elementAt(i);
        }
        return f;
    }

    static Format popFormats(Vector vector, int start, int end) {
        Format f;
        if (end == start + 1) {
            f = (Format) vector.elementAt(start);
        } else {
            f = new CompoundFormat(getFormats(vector, start, end));
        }
        vector.setSize(start);
        return f;
    }

    public LispFormat(String str) throws ParseException {
        this(str.toCharArray());
    }

    public LispFormat(char[] format) throws ParseException {
        this(format, 0, format.length);
    }

    public static int getParam(Vector vec, int index) {
        if (index >= vec.size()) {
            return -1073741824;
        }
        Object arg = vec.elementAt(index);
        if (arg == paramFromList) {
            return -1610612736;
        }
        if (arg == paramFromCount) {
            return ReportFormat.PARAM_FROM_COUNT;
        }
        if (arg != paramUnspecified) {
            return getParam(arg, -1073741824);
        }
        return -1073741824;
    }

    public static Object[] asArray(Object arg) {
        if (arg instanceof Object[]) {
            return (Object[]) arg;
        }
        if (!(arg instanceof Sequence)) {
            return null;
        }
        int count = ((Sequence) arg).size();
        Object[] arr = new Object[count];
        int i = 0;
        while (arg instanceof Pair) {
            Pair pair = (Pair) arg;
            arr[i] = pair.getCar();
            arg = pair.getCdr();
            i++;
        }
        if (i < count) {
            if (!(arg instanceof Sequence)) {
                return null;
            }
            int npairs = i;
            Sequence seq = (Sequence) arg;
            while (i < count) {
                arr[i] = seq.get(npairs + i);
                i++;
            }
        }
        return arr;
    }
}
