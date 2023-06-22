package gnu.xml;

import gnu.lists.Consumer;
import gnu.text.LineBufferedReader;
import gnu.text.LineInputStreamReader;
import gnu.text.Path;
import gnu.text.SourceMessages;
import java.io.IOException;
import java.io.InputStream;

public class XMLParser {
    private static final int ATTRIBUTE_SEEN_EQ_STATE = 11;
    private static final int ATTRIBUTE_SEEN_NAME_STATE = 8;
    static final String BAD_ENCODING_SYNTAX = "bad 'encoding' declaration";
    static final String BAD_STANDALONE_SYNTAX = "bad 'standalone' declaration";
    private static final int BEGIN_ELEMENT_STATE = 2;
    private static final int DOCTYPE_NAME_SEEN_STATE = 16;
    private static final int DOCTYPE_SEEN_STATE = 13;
    private static final int END_ELEMENT_STATE = 4;
    private static final int EXPECT_NAME_MODIFIER = 1;
    private static final int EXPECT_RIGHT_STATE = 27;
    private static final int INIT_LEFT_QUEST_STATE = 30;
    private static final int INIT_LEFT_STATE = 34;
    private static final int INIT_STATE = 0;
    private static final int INIT_TEXT_STATE = 31;
    private static final int INVALID_VERSION_DECL = 35;
    private static final int MAYBE_ATTRIBUTE_STATE = 10;
    private static final int PREV_WAS_CR_STATE = 28;
    private static final int SAW_AMP_SHARP_STATE = 26;
    private static final int SAW_AMP_STATE = 25;
    private static final int SAW_ENTITY_REF = 6;
    private static final int SAW_EOF_ERROR = 37;
    private static final int SAW_ERROR = 36;
    private static final int SAW_LEFT_EXCL_MINUS_STATE = 22;
    private static final int SAW_LEFT_EXCL_STATE = 20;
    private static final int SAW_LEFT_QUEST_STATE = 21;
    private static final int SAW_LEFT_SLASH_STATE = 19;
    private static final int SAW_LEFT_STATE = 14;
    private static final int SKIP_SPACES_MODIFIER = 2;
    private static final int TEXT_STATE = 1;

    public static void parse(Object uri, SourceMessages messages, Consumer out) throws IOException {
        parse(Path.openInputStream(uri), uri, messages, out);
    }

    public static LineInputStreamReader XMLStreamReader(InputStream strm) throws IOException {
        int b4 = -1;
        LineInputStreamReader in = new LineInputStreamReader(strm);
        int b1 = in.getByte();
        int b2 = b1 < 0 ? -1 : in.getByte();
        int b3 = b2 < 0 ? -1 : in.getByte();
        if (b1 == 239 && b2 == 187 && b3 == 191) {
            in.resetStart(3);
            in.setCharset("UTF-8");
        } else if (b1 == 255 && b2 == 254 && b3 != 0) {
            in.resetStart(2);
            in.setCharset("UTF-16LE");
        } else if (b1 == 254 && b2 == 255 && b3 != 0) {
            in.resetStart(2);
            in.setCharset("UTF-16BE");
        } else {
            if (b3 >= 0) {
                b4 = in.getByte();
            }
            if (b1 == 76 && b2 == 111 && b3 == 167 && b4 == 148) {
                throw new RuntimeException("XMLParser: EBCDIC encodings not supported");
            }
            in.resetStart(0);
            if ((b1 == 60 && ((b2 == 63 && b3 == 120 && b4 == 109) || (b2 == 0 && b3 == 63 && b4 == 0))) || (b1 == 0 && b2 == 60 && b3 == 0 && b4 == 63)) {
                char[] buffer = in.buffer;
                if (buffer == null) {
                    buffer = new char[8192];
                    in.buffer = buffer;
                }
                int pos = 0;
                int quote = 0;
                while (true) {
                    int b = in.getByte();
                    if (b != 0) {
                        if (b < 0) {
                            break;
                        }
                        int pos2 = pos + 1;
                        buffer[pos] = (char) (b & 255);
                        if (quote == 0) {
                            if (b == 62) {
                                pos = pos2;
                                break;
                            } else if (b == 39 || b == 34) {
                                quote = b;
                            }
                        } else if (b == quote) {
                            quote = 0;
                        }
                        pos = pos2;
                    }
                }
                in.pos = 0;
                in.limit = pos;
            } else {
                in.setCharset("UTF-8");
            }
        }
        in.setKeepFullLines(false);
        return in;
    }

    public static void parse(InputStream strm, Object uri, SourceMessages messages, Consumer out) throws IOException {
        LineInputStreamReader in = XMLStreamReader(strm);
        if (uri != null) {
            in.setName(uri);
        }
        parse((LineBufferedReader) in, messages, out);
        in.close();
    }

    public static void parse(LineBufferedReader in, SourceMessages messages, Consumer out) throws IOException {
        XMLFilter filter = new XMLFilter(out);
        filter.setMessages(messages);
        filter.setSourceLocator(in);
        filter.startDocument();
        Path uri = in.getPath();
        if (uri != null) {
            filter.writeDocumentUri(uri);
        }
        parse(in, filter);
        filter.endDocument();
    }

    public static void parse(LineBufferedReader in, SourceMessages messages, XMLFilter filter) throws IOException {
        filter.setMessages(messages);
        filter.setSourceLocator(in);
        filter.startDocument();
        Path uri = in.getPath();
        if (uri != null) {
            filter.writeDocumentUri(uri);
        }
        parse(in, filter);
        filter.endDocument();
        in.close();
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:160:0x023d, code lost:
        if (r5 != 0) goto L_0x0878;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x0243, code lost:
        if (r22 != 8) goto L_0x024d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:163:0x0245, code lost:
        r17 = "missing or invalid attribute name";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:164:0x0247, code lost:
        r22 = 36;
        r18 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:166:0x0250, code lost:
        if (r22 == 2) goto L_0x0257;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0255, code lost:
        if (r22 != 4) goto L_0x025a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:169:0x0257, code lost:
        r17 = "missing or invalid element name";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0053, code lost:
        r26.pos = r18;
        r27.error('e', r17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:170:0x025a, code lost:
        r17 = "missing or invalid name";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x025f, code lost:
        if (r9 != 'x') goto L_0x0287;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x0261, code lost:
        if (r6 != 0) goto L_0x0287;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x0263, code lost:
        r6 = 16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x0269, code lost:
        if (r19 >= r16) goto L_0x0023;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x0273, code lost:
        if (r9 != ';') goto L_0x025d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0062, code lost:
        r19 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:180:0x0275, code lost:
        r26.pos = r19;
        r27.emitCharacterReference(r5, r3, r4, (r19 - 1) - r4);
        r22 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x0289, code lost:
        if (r5 < 134217728) goto L_0x029e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:183:0x028b, code lost:
        r26.pos = r19;
        r27.error('e', "invalid character reference");
        r22 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x029e, code lost:
        if (r6 != 0) goto L_0x02ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:0x02a0, code lost:
        r8 = 10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x02a2, code lost:
        r11 = java.lang.Character.digit(r9, r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x02a6, code lost:
        if (r11 < 0) goto L_0x028b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x02a8, code lost:
        r5 = (r5 * r8) + r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x02ad, code lost:
        r8 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0068, code lost:
        if (r19 < r16) goto L_0x006d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006a, code lost:
        r18 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006d, code lost:
        r18 = r19 + 1;
        r9 = r3[r19];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0073, code lost:
        if (r9 != '>') goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0075, code lost:
        r22 = 1;
        r19 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0021, code lost:
        r19 = r18;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:414:0x05f2, code lost:
        if (r19 >= r16) goto L_0x0023;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:417:0x05fc, code lost:
        if (r9 != '>') goto L_0x0692;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:418:0x05fe, code lost:
        r5 = (r19 - 1) - r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:419:0x0603, code lost:
        if (r5 < 4) goto L_0x0638;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:421:0x0609, code lost:
        if (r3[r4] != '-') goto L_0x0638;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:423:0x0611, code lost:
        if (r3[r4 + 1] != '-') goto L_0x0638;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:425:0x0619, code lost:
        if (r3[r19 - 2] != '-') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:427:0x0621, code lost:
        if (r3[r19 - 3] != '-') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:428:0x0623, code lost:
        r26.pos = r19;
        r27.commentFromParser(r3, r4 + 2, r5 - 4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:429:0x0632, code lost:
        r4 = r16;
        r22 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:431:0x0639, code lost:
        if (r5 < 6) goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:433:0x063f, code lost:
        if (r3[r4] != '[') goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:435:0x0647, code lost:
        if (r3[r4 + 1] != 'C') goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:437:0x064f, code lost:
        if (r3[r4 + 2] != 'D') goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:439:0x0657, code lost:
        if (r3[r4 + 3] != 'A') goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:441:0x065f, code lost:
        if (r3[r4 + 4] != 'T') goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:443:0x0667, code lost:
        if (r3[r4 + 5] != 'A') goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:445:0x066f, code lost:
        if (r3[r4 + 6] != '[') goto L_0x0632;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:447:0x0677, code lost:
        if (r3[r19 - 2] != ']') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:449:0x067f, code lost:
        if (r3[r19 - 3] != ']') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:450:0x0681, code lost:
        r26.pos = r19;
        r27.writeCDATA(r3, r4 + 7, (r19 - 10) - r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:452:0x0696, code lost:
        if (r19 != (r4 + 7)) goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:454:0x069c, code lost:
        if (r3[r4] != 'D') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:456:0x06a4, code lost:
        if (r3[r4 + 1] != 'O') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:458:0x06ac, code lost:
        if (r3[r4 + 2] != 'C') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:460:0x06b4, code lost:
        if (r3[r4 + 3] != 'T') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:462:0x06bc, code lost:
        if (r3[r4 + 4] != 'Y') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:464:0x06c4, code lost:
        if (r3[r4 + 5] != 'P') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:466:0x06c8, code lost:
        if (r9 != 'E') goto L_0x05ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:467:0x06ca, code lost:
        r4 = r16;
        r22 = 15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:569:0x0878, code lost:
        r18 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:666:?, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:544:0x0806  */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x0029  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void parse(gnu.text.LineBufferedReader r26, gnu.xml.XMLFilter r27) {
        /*
            r0 = r26
            char[] r3 = r0.buffer
            r0 = r26
            int r0 = r0.pos
            r18 = r0
            r0 = r26
            int r0 = r0.limit
            r16 = r0
            r22 = 0
            r23 = 60
            r10 = 14
            r9 = 32
            r5 = 0
            r6 = -1
            r17 = 0
            r4 = r16
        L_0x001e:
            switch(r22) {
                case 0: goto L_0x002e;
                case 1: goto L_0x008a;
                case 2: goto L_0x030b;
                case 3: goto L_0x01a8;
                case 4: goto L_0x07e5;
                case 5: goto L_0x01a8;
                case 6: goto L_0x02c3;
                case 7: goto L_0x01a8;
                case 8: goto L_0x076f;
                case 9: goto L_0x01a8;
                case 10: goto L_0x0734;
                case 11: goto L_0x07ab;
                case 12: goto L_0x017c;
                case 13: goto L_0x06d0;
                case 14: goto L_0x02e3;
                case 15: goto L_0x017c;
                case 16: goto L_0x06d6;
                case 17: goto L_0x01a8;
                case 18: goto L_0x0021;
                case 19: goto L_0x07df;
                case 20: goto L_0x087c;
                case 21: goto L_0x031c;
                case 22: goto L_0x0021;
                case 23: goto L_0x017c;
                case 24: goto L_0x01a8;
                case 25: goto L_0x02af;
                case 26: goto L_0x0880;
                case 27: goto L_0x07f6;
                case 28: goto L_0x0151;
                case 29: goto L_0x017c;
                case 30: goto L_0x031c;
                case 31: goto L_0x0035;
                case 32: goto L_0x017c;
                case 33: goto L_0x01a8;
                case 34: goto L_0x0041;
                case 35: goto L_0x004f;
                case 36: goto L_0x0053;
                case 37: goto L_0x007a;
                default: goto L_0x0021;
            }
        L_0x0021:
            r19 = r18
        L_0x0023:
            r0 = r19
            r1 = r16
            if (r0 >= r1) goto L_0x0806
            int r18 = r19 + 1
            char r9 = r3[r19]
            goto L_0x001e
        L_0x002e:
            r22 = 1
            r22 = 31
            r19 = r18
            goto L_0x0023
        L_0x0035:
            r2 = 60
            if (r9 != r2) goto L_0x003e
            r22 = 34
            r19 = r18
            goto L_0x0023
        L_0x003e:
            r22 = 1
            goto L_0x001e
        L_0x0041:
            r2 = 63
            if (r9 != r2) goto L_0x004c
            r4 = r18
            r22 = 33
            r19 = r18
            goto L_0x0023
        L_0x004c:
            r22 = 14
            goto L_0x001e
        L_0x004f:
            r18 = r6
            java.lang.String r17 = "invalid xml version specifier"
        L_0x0053:
            r0 = r18
            r1 = r26
            r1.pos = r0
            r2 = 101(0x65, float:1.42E-43)
            r0 = r27
            r1 = r17
            r0.error(r2, r1)
        L_0x0062:
            r19 = r18
            r0 = r19
            r1 = r16
            if (r0 < r1) goto L_0x006d
            r18 = r19
        L_0x006c:
            return
        L_0x006d:
            int r18 = r19 + 1
            char r9 = r3[r19]
            r2 = 62
            if (r9 != r2) goto L_0x0062
            r22 = 1
            r19 = r18
            goto L_0x0023
        L_0x007a:
            r0 = r18
            r1 = r26
            r1.pos = r0
            r2 = 102(0x66, float:1.43E-43)
            java.lang.String r7 = "unexpected end-of-file"
            r0 = r27
            r0.error(r2, r7)
            goto L_0x006c
        L_0x008a:
            int r4 = r18 + -1
            r5 = r18
            r19 = r18
        L_0x0090:
            r0 = r23
            if (r9 != r0) goto L_0x00ac
            r22 = r10
            r18 = r19
        L_0x0098:
            int r5 = r18 - r5
            if (r5 <= 0) goto L_0x00a7
            r0 = r18
            r1 = r26
            r1.pos = r0
            r0 = r27
            r0.textFromParser(r3, r4, r5)
        L_0x00a7:
            int r4 = r3.length
            r19 = r18
            goto L_0x0023
        L_0x00ac:
            r2 = 38
            if (r9 != r2) goto L_0x00b5
            r22 = 25
            r18 = r19
            goto L_0x0098
        L_0x00b5:
            r2 = 13
            if (r9 != r2) goto L_0x0115
            int r5 = r19 - r5
            r0 = r19
            r1 = r26
            r1.pos = r0
            if (r5 <= 0) goto L_0x00c8
            r0 = r27
            r0.textFromParser(r3, r4, r5)
        L_0x00c8:
            r0 = r19
            r1 = r16
            if (r0 >= r1) goto L_0x010e
            char r9 = r3[r19]
            r2 = 10
            if (r9 != r2) goto L_0x00ef
            r4 = r19
            int r18 = r19 + 1
            r5 = r18
        L_0x00da:
            r2 = 1
            r0 = r26
            r1 = r18
            r0.incrLineNumber(r2, r1)
            r19 = r18
        L_0x00e4:
            r0 = r19
            r1 = r16
            if (r0 != r1) goto L_0x0149
            int r5 = r5 + -1
            r18 = r19
            goto L_0x0098
        L_0x00ef:
            r27.linefeedFromParser()
            r2 = 133(0x85, float:1.86E-43)
            if (r9 != r2) goto L_0x00fd
            int r18 = r19 + 1
            r4 = r19
            int r5 = r18 + 1
            goto L_0x00da
        L_0x00fd:
            r2 = 1
            r0 = r26
            r1 = r19
            r0.incrLineNumber(r2, r1)
            r4 = r19
            int r18 = r19 + 1
            r5 = r18
            r19 = r18
            goto L_0x0090
        L_0x010e:
            r27.linefeedFromParser()
            r22 = 28
            goto L_0x0023
        L_0x0115:
            r2 = 133(0x85, float:1.86E-43)
            if (r9 == r2) goto L_0x011d
            r2 = 8232(0x2028, float:1.1535E-41)
            if (r9 != r2) goto L_0x013c
        L_0x011d:
            int r5 = r19 - r5
            int r2 = r19 + -1
            r0 = r26
            r0.pos = r2
            if (r5 <= 0) goto L_0x012c
            r0 = r27
            r0.textFromParser(r3, r4, r5)
        L_0x012c:
            r27.linefeedFromParser()
            r2 = 1
            r0 = r26
            r1 = r19
            r0.incrLineNumber(r2, r1)
            int r5 = r19 + 1
            r4 = r19
            goto L_0x00e4
        L_0x013c:
            r2 = 10
            if (r9 != r2) goto L_0x00e4
            r2 = 1
            r0 = r26
            r1 = r19
            r0.incrLineNumber(r2, r1)
            goto L_0x00e4
        L_0x0149:
            int r18 = r19 + 1
            char r9 = r3[r19]
            r19 = r18
            goto L_0x0090
        L_0x0151:
            r22 = 1
            r2 = 10
            if (r9 != r2) goto L_0x016d
            r2 = 1
            r7 = r2
        L_0x0159:
            r2 = 133(0x85, float:1.86E-43)
            if (r9 != r2) goto L_0x0170
            r2 = 1
        L_0x015e:
            r2 = r2 | r7
            if (r2 == 0) goto L_0x0172
            r2 = 1
            r0 = r26
            r1 = r18
            r0.incrLineNumber(r2, r1)
            r19 = r18
            goto L_0x0023
        L_0x016d:
            r2 = 0
            r7 = r2
            goto L_0x0159
        L_0x0170:
            r2 = 0
            goto L_0x015e
        L_0x0172:
            r2 = 1
            int r7 = r18 + -1
            r0 = r26
            r0.incrLineNumber(r2, r7)
            goto L_0x001e
        L_0x017c:
            r2 = 32
            if (r9 == r2) goto L_0x0021
            r2 = 9
            if (r9 != r2) goto L_0x0188
            r19 = r18
            goto L_0x0023
        L_0x0188:
            r2 = 10
            if (r9 == r2) goto L_0x0198
            r2 = 13
            if (r9 == r2) goto L_0x0198
            r2 = 133(0x85, float:1.86E-43)
            if (r9 == r2) goto L_0x0198
            r2 = 8232(0x2028, float:1.1535E-41)
            if (r9 != r2) goto L_0x01a4
        L_0x0198:
            r2 = 1
            r0 = r26
            r1 = r18
            r0.incrLineNumber(r2, r1)
            r19 = r18
            goto L_0x0023
        L_0x01a4:
            int r22 = r22 + -2
            goto L_0x001e
        L_0x01a8:
            int r5 = r4 + 1
            r19 = r18
        L_0x01ac:
            r2 = 97
            if (r9 < r2) goto L_0x01b4
            r2 = 122(0x7a, float:1.71E-43)
            if (r9 <= r2) goto L_0x022b
        L_0x01b4:
            r2 = 65
            if (r9 < r2) goto L_0x01bc
            r2 = 90
            if (r9 <= r2) goto L_0x022b
        L_0x01bc:
            r2 = 95
            if (r9 == r2) goto L_0x022b
            r2 = 58
            if (r9 == r2) goto L_0x022b
            r2 = 192(0xc0, float:2.69E-43)
            if (r9 < r2) goto L_0x0203
            r2 = 767(0x2ff, float:1.075E-42)
            if (r9 <= r2) goto L_0x022b
            r2 = 880(0x370, float:1.233E-42)
            if (r9 < r2) goto L_0x0203
            r2 = 8191(0x1fff, float:1.1478E-41)
            if (r9 > r2) goto L_0x01d8
            r2 = 894(0x37e, float:1.253E-42)
            if (r9 != r2) goto L_0x022b
        L_0x01d8:
            r2 = 8204(0x200c, float:1.1496E-41)
            if (r9 < r2) goto L_0x0203
            r2 = 8205(0x200d, float:1.1498E-41)
            if (r9 <= r2) goto L_0x022b
            r2 = 8304(0x2070, float:1.1636E-41)
            if (r9 < r2) goto L_0x01e8
            r2 = 8591(0x218f, float:1.2039E-41)
            if (r9 <= r2) goto L_0x022b
        L_0x01e8:
            r2 = 11264(0x2c00, float:1.5784E-41)
            if (r9 < r2) goto L_0x01f0
            r2 = 12271(0x2fef, float:1.7195E-41)
            if (r9 <= r2) goto L_0x022b
        L_0x01f0:
            r2 = 12289(0x3001, float:1.722E-41)
            if (r9 < r2) goto L_0x01f9
            r2 = 55295(0xd7ff, float:7.7485E-41)
            if (r9 <= r2) goto L_0x022b
        L_0x01f9:
            r2 = 63744(0xf900, float:8.9324E-41)
            if (r9 < r2) goto L_0x0203
            r2 = 65533(0xfffd, float:9.1831E-41)
            if (r9 <= r2) goto L_0x022b
        L_0x0203:
            r0 = r19
            if (r0 <= r5) goto L_0x020f
            r2 = 48
            if (r9 < r2) goto L_0x020f
            r2 = 57
            if (r9 <= r2) goto L_0x022b
        L_0x020f:
            r2 = 46
            if (r9 == r2) goto L_0x022b
            r2 = 45
            if (r9 == r2) goto L_0x022b
            r2 = 183(0xb7, float:2.56E-43)
            if (r9 == r2) goto L_0x022b
            r2 = 768(0x300, float:1.076E-42)
            if (r9 <= r2) goto L_0x0239
            r2 = 879(0x36f, float:1.232E-42)
            if (r9 <= r2) goto L_0x022b
            r2 = 8255(0x203f, float:1.1568E-41)
            if (r9 < r2) goto L_0x0239
            r2 = 8256(0x2040, float:1.1569E-41)
            if (r9 > r2) goto L_0x0239
        L_0x022b:
            r0 = r19
            r1 = r16
            if (r0 >= r1) goto L_0x0023
            int r18 = r19 + 1
            char r9 = r3[r19]
            r19 = r18
            goto L_0x01ac
        L_0x0239:
            int r22 = r22 + -1
            int r5 = r19 - r5
            if (r5 != 0) goto L_0x0878
            r2 = 8
            r0 = r22
            if (r0 != r2) goto L_0x024d
            java.lang.String r17 = "missing or invalid attribute name"
        L_0x0247:
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x024d:
            r2 = 2
            r0 = r22
            if (r0 == r2) goto L_0x0257
            r2 = 4
            r0 = r22
            if (r0 != r2) goto L_0x025a
        L_0x0257:
            java.lang.String r17 = "missing or invalid element name"
            goto L_0x0247
        L_0x025a:
            java.lang.String r17 = "missing or invalid name"
            goto L_0x0247
        L_0x025d:
            r2 = 120(0x78, float:1.68E-43)
            if (r9 != r2) goto L_0x0287
            if (r6 != 0) goto L_0x0287
            r6 = 16
        L_0x0265:
            r0 = r19
            r1 = r16
            if (r0 >= r1) goto L_0x0023
            int r18 = r19 + 1
            char r9 = r3[r19]
            r19 = r18
        L_0x0271:
            r2 = 59
            if (r9 != r2) goto L_0x025d
            r0 = r19
            r1 = r26
            r1.pos = r0
            int r2 = r19 + -1
            int r2 = r2 - r4
            r0 = r27
            r0.emitCharacterReference(r5, r3, r4, r2)
            r22 = 1
            goto L_0x0023
        L_0x0287:
            r2 = 134217728(0x8000000, float:3.85186E-34)
            if (r5 < r2) goto L_0x029e
        L_0x028b:
            r0 = r19
            r1 = r26
            r1.pos = r0
            r2 = 101(0x65, float:1.42E-43)
            java.lang.String r7 = "invalid character reference"
            r0 = r27
            r0.error(r2, r7)
            r22 = 1
            goto L_0x0023
        L_0x029e:
            if (r6 != 0) goto L_0x02ad
            r8 = 10
        L_0x02a2:
            int r11 = java.lang.Character.digit(r9, r8)
            if (r11 < 0) goto L_0x028b
            int r2 = r5 * r8
            int r5 = r2 + r11
            goto L_0x0265
        L_0x02ad:
            r8 = r6
            goto L_0x02a2
        L_0x02af:
            r2 = 35
            if (r9 != r2) goto L_0x02bd
            r22 = 26
            r4 = r18
            r5 = 0
            r6 = 0
            r19 = r18
            goto L_0x0023
        L_0x02bd:
            int r4 = r18 + -1
            r22 = 7
            goto L_0x001e
        L_0x02c3:
            r0 = r18
            r1 = r26
            r1.pos = r0
            r2 = 59
            if (r9 == r2) goto L_0x02d6
            r2 = 119(0x77, float:1.67E-43)
            java.lang.String r7 = "missing ';'"
            r0 = r27
            r0.error(r2, r7)
        L_0x02d6:
            r0 = r27
            r0.emitEntityReference(r3, r4, r5)
            r4 = r16
            r22 = 1
            r19 = r18
            goto L_0x0023
        L_0x02e3:
            r2 = 47
            if (r9 != r2) goto L_0x02ed
            r22 = 19
            r19 = r18
            goto L_0x0023
        L_0x02ed:
            r2 = 63
            if (r9 != r2) goto L_0x02f9
            r4 = r18
            r22 = 24
            r19 = r18
            goto L_0x0023
        L_0x02f9:
            r2 = 33
            if (r9 != r2) goto L_0x0305
            r22 = 20
            r4 = r18
            r19 = r18
            goto L_0x0023
        L_0x0305:
            int r4 = r18 + -1
            r22 = 3
            goto L_0x001e
        L_0x030b:
            int r2 = r18 - r5
            r0 = r26
            r0.pos = r2
            r0 = r27
            r0.emitStartElement(r3, r4, r5)
            r22 = 12
            r4 = r16
            goto L_0x001e
        L_0x031c:
            if (r6 >= 0) goto L_0x0874
            int r6 = r18 + -1
            r19 = r18
        L_0x0322:
            r2 = 62
            if (r9 != r2) goto L_0x05e0
            int r13 = r19 + -2
            char r2 = r3[r13]
            r7 = 63
            if (r2 != r7) goto L_0x05e0
            if (r13 < r6) goto L_0x05e0
            r0 = r19
            r1 = r26
            r1.pos = r0
            r2 = 3
            if (r5 != r2) goto L_0x05d2
            char r2 = r3[r4]
            r7 = 120(0x78, float:1.68E-43)
            if (r2 != r7) goto L_0x05d2
            int r2 = r4 + 1
            char r2 = r3[r2]
            r7 = 109(0x6d, float:1.53E-43)
            if (r2 != r7) goto L_0x05d2
            int r2 = r4 + 2
            char r2 = r3[r2]
            r7 = 108(0x6c, float:1.51E-43)
            if (r2 != r7) goto L_0x05d2
            r2 = 30
            r0 = r22
            if (r0 != r2) goto L_0x05ca
            int r2 = r6 + 7
            if (r13 <= r2) goto L_0x038f
            char r2 = r3[r6]
            r7 = 118(0x76, float:1.65E-43)
            if (r2 != r7) goto L_0x038f
            int r2 = r6 + 1
            char r2 = r3[r2]
            r7 = 101(0x65, float:1.42E-43)
            if (r2 != r7) goto L_0x038f
            int r2 = r6 + 2
            char r2 = r3[r2]
            r7 = 114(0x72, float:1.6E-43)
            if (r2 != r7) goto L_0x038f
            int r2 = r6 + 3
            char r2 = r3[r2]
            r7 = 115(0x73, float:1.61E-43)
            if (r2 != r7) goto L_0x038f
            int r2 = r6 + 4
            char r2 = r3[r2]
            r7 = 105(0x69, float:1.47E-43)
            if (r2 != r7) goto L_0x038f
            int r2 = r6 + 5
            char r2 = r3[r2]
            r7 = 111(0x6f, float:1.56E-43)
            if (r2 != r7) goto L_0x038f
            int r2 = r6 + 6
            char r2 = r3[r2]
            r7 = 110(0x6e, float:1.54E-43)
            if (r2 == r7) goto L_0x0397
        L_0x038f:
            r18 = r6
            java.lang.String r17 = "xml declaration without version"
            r22 = 36
            goto L_0x001e
        L_0x0397:
            int r6 = r6 + 7
            char r9 = r3[r6]
        L_0x039b:
            boolean r2 = java.lang.Character.isWhitespace(r9)
            if (r2 == 0) goto L_0x03a8
            int r6 = r6 + 1
            if (r6 >= r13) goto L_0x03a8
            char r9 = r3[r6]
            goto L_0x039b
        L_0x03a8:
            r2 = 61
            if (r9 == r2) goto L_0x03b2
            r22 = 35
            r18 = r19
            goto L_0x001e
        L_0x03b2:
            int r6 = r6 + 1
            char r9 = r3[r6]
        L_0x03b6:
            boolean r2 = java.lang.Character.isWhitespace(r9)
            if (r2 == 0) goto L_0x03c3
            int r6 = r6 + 1
            if (r6 >= r13) goto L_0x03c3
            char r9 = r3[r6]
            goto L_0x03b6
        L_0x03c3:
            r2 = 39
            if (r9 == r2) goto L_0x03d1
            r2 = 34
            if (r9 == r2) goto L_0x03d1
            r22 = 35
            r18 = r19
            goto L_0x001e
        L_0x03d1:
            r20 = r9
            int r6 = r6 + 1
            r15 = r6
        L_0x03d6:
            if (r15 != r13) goto L_0x03de
            r22 = 35
            r18 = r19
            goto L_0x001e
        L_0x03de:
            char r9 = r3[r15]
            r0 = r20
            if (r9 != r0) goto L_0x0411
            int r2 = r6 + 3
            if (r15 != r2) goto L_0x03fe
            char r2 = r3[r6]
            r7 = 49
            if (r2 != r7) goto L_0x03fe
            int r2 = r6 + 1
            char r2 = r3[r2]
            r7 = 46
            if (r2 != r7) goto L_0x03fe
            int r2 = r6 + 2
            char r9 = r3[r2]
            r2 = 48
            if (r9 == r2) goto L_0x0402
        L_0x03fe:
            r2 = 49
            if (r9 != r2) goto L_0x0414
        L_0x0402:
            int r6 = r15 + 1
        L_0x0404:
            if (r6 >= r13) goto L_0x041a
            char r2 = r3[r6]
            boolean r2 = java.lang.Character.isWhitespace(r2)
            if (r2 == 0) goto L_0x041a
            int r6 = r6 + 1
            goto L_0x0404
        L_0x0411:
            int r15 = r15 + 1
            goto L_0x03d6
        L_0x0414:
            r22 = 35
            r18 = r19
            goto L_0x001e
        L_0x041a:
            int r2 = r6 + 7
            if (r13 <= r2) goto L_0x04d5
            char r2 = r3[r6]
            r7 = 101(0x65, float:1.42E-43)
            if (r2 != r7) goto L_0x04d5
            int r2 = r6 + 1
            char r2 = r3[r2]
            r7 = 110(0x6e, float:1.54E-43)
            if (r2 != r7) goto L_0x04d5
            int r2 = r6 + 2
            char r2 = r3[r2]
            r7 = 99
            if (r2 != r7) goto L_0x04d5
            int r2 = r6 + 3
            char r2 = r3[r2]
            r7 = 111(0x6f, float:1.56E-43)
            if (r2 != r7) goto L_0x04d5
            int r2 = r6 + 4
            char r2 = r3[r2]
            r7 = 100
            if (r2 != r7) goto L_0x04d5
            int r2 = r6 + 5
            char r2 = r3[r2]
            r7 = 105(0x69, float:1.47E-43)
            if (r2 != r7) goto L_0x04d5
            int r2 = r6 + 6
            char r2 = r3[r2]
            r7 = 110(0x6e, float:1.54E-43)
            if (r2 != r7) goto L_0x04d5
            int r2 = r6 + 7
            char r2 = r3[r2]
            r7 = 103(0x67, float:1.44E-43)
            if (r2 != r7) goto L_0x04d5
            int r6 = r6 + 8
            char r9 = r3[r6]
        L_0x0460:
            boolean r2 = java.lang.Character.isWhitespace(r9)
            if (r2 == 0) goto L_0x046d
            int r6 = r6 + 1
            if (r6 >= r13) goto L_0x046d
            char r9 = r3[r6]
            goto L_0x0460
        L_0x046d:
            r2 = 61
            if (r9 == r2) goto L_0x0479
            java.lang.String r17 = "bad 'encoding' declaration"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x0479:
            int r6 = r6 + 1
            char r9 = r3[r6]
        L_0x047d:
            boolean r2 = java.lang.Character.isWhitespace(r9)
            if (r2 == 0) goto L_0x048a
            int r6 = r6 + 1
            if (r6 >= r13) goto L_0x048a
            char r9 = r3[r6]
            goto L_0x047d
        L_0x048a:
            r2 = 39
            if (r9 == r2) goto L_0x049a
            r2 = 34
            if (r9 == r2) goto L_0x049a
            java.lang.String r17 = "bad 'encoding' declaration"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x049a:
            r20 = r9
            int r6 = r6 + 1
            r15 = r6
        L_0x049f:
            if (r15 != r13) goto L_0x04a9
            java.lang.String r17 = "bad 'encoding' declaration"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x04a9:
            char r9 = r3[r15]
            r0 = r20
            if (r9 != r0) goto L_0x04d2
            java.lang.String r12 = new java.lang.String
            int r2 = r15 - r6
            r12.<init>(r3, r6, r2)
            r0 = r26
            boolean r2 = r0 instanceof gnu.text.LineInputStreamReader
            if (r2 == 0) goto L_0x04c3
            r2 = r26
            gnu.text.LineInputStreamReader r2 = (gnu.text.LineInputStreamReader) r2
            r2.setCharset((java.lang.String) r12)
        L_0x04c3:
            int r6 = r15 + 1
        L_0x04c5:
            if (r6 >= r13) goto L_0x04d5
            char r2 = r3[r6]
            boolean r2 = java.lang.Character.isWhitespace(r2)
            if (r2 == 0) goto L_0x04d5
            int r6 = r6 + 1
            goto L_0x04c5
        L_0x04d2:
            int r15 = r15 + 1
            goto L_0x049f
        L_0x04d5:
            int r2 = r6 + 9
            if (r13 <= r2) goto L_0x05c0
            char r2 = r3[r6]
            r7 = 115(0x73, float:1.61E-43)
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 1
            char r2 = r3[r2]
            r7 = 116(0x74, float:1.63E-43)
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 2
            char r2 = r3[r2]
            r7 = 97
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 3
            char r2 = r3[r2]
            r7 = 110(0x6e, float:1.54E-43)
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 4
            char r2 = r3[r2]
            r7 = 100
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 5
            char r2 = r3[r2]
            r7 = 97
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 6
            char r2 = r3[r2]
            r7 = 108(0x6c, float:1.51E-43)
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 7
            char r2 = r3[r2]
            r7 = 111(0x6f, float:1.56E-43)
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 8
            char r2 = r3[r2]
            r7 = 110(0x6e, float:1.54E-43)
            if (r2 != r7) goto L_0x05c0
            int r2 = r6 + 9
            char r2 = r3[r2]
            r7 = 101(0x65, float:1.42E-43)
            if (r2 != r7) goto L_0x05c0
            int r6 = r6 + 10
            char r9 = r3[r6]
        L_0x052b:
            boolean r2 = java.lang.Character.isWhitespace(r9)
            if (r2 == 0) goto L_0x0538
            int r6 = r6 + 1
            if (r6 >= r13) goto L_0x0538
            char r9 = r3[r6]
            goto L_0x052b
        L_0x0538:
            r2 = 61
            if (r9 == r2) goto L_0x0544
            java.lang.String r17 = "bad 'standalone' declaration"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x0544:
            int r6 = r6 + 1
            char r9 = r3[r6]
        L_0x0548:
            boolean r2 = java.lang.Character.isWhitespace(r9)
            if (r2 == 0) goto L_0x0555
            int r6 = r6 + 1
            if (r6 >= r13) goto L_0x0555
            char r9 = r3[r6]
            goto L_0x0548
        L_0x0555:
            r2 = 39
            if (r9 == r2) goto L_0x0565
            r2 = 34
            if (r9 == r2) goto L_0x0565
            java.lang.String r17 = "bad 'standalone' declaration"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x0565:
            r20 = r9
            int r6 = r6 + 1
            r15 = r6
        L_0x056a:
            if (r15 != r13) goto L_0x0574
            java.lang.String r17 = "bad 'standalone' declaration"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x0574:
            char r9 = r3[r15]
            r0 = r20
            if (r9 != r0) goto L_0x05a3
            int r2 = r6 + 3
            if (r15 != r2) goto L_0x05a6
            char r2 = r3[r6]
            r7 = 121(0x79, float:1.7E-43)
            if (r2 != r7) goto L_0x05a6
            int r2 = r6 + 1
            char r2 = r3[r2]
            r7 = 101(0x65, float:1.42E-43)
            if (r2 != r7) goto L_0x05a6
            int r2 = r6 + 2
            char r2 = r3[r2]
            r7 = 115(0x73, float:1.61E-43)
            if (r2 != r7) goto L_0x05a6
        L_0x0594:
            int r6 = r15 + 1
        L_0x0596:
            if (r6 >= r13) goto L_0x05c0
            char r2 = r3[r6]
            boolean r2 = java.lang.Character.isWhitespace(r2)
            if (r2 == 0) goto L_0x05c0
            int r6 = r6 + 1
            goto L_0x0596
        L_0x05a3:
            int r15 = r15 + 1
            goto L_0x056a
        L_0x05a6:
            int r2 = r6 + 2
            if (r15 != r2) goto L_0x05b8
            char r2 = r3[r6]
            r7 = 110(0x6e, float:1.54E-43)
            if (r2 != r7) goto L_0x05b8
            int r2 = r6 + 1
            char r2 = r3[r2]
            r7 = 111(0x6f, float:1.56E-43)
            if (r2 == r7) goto L_0x0594
        L_0x05b8:
            java.lang.String r17 = "bad 'standalone' declaration"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x05c0:
            if (r13 == r6) goto L_0x05d9
            java.lang.String r17 = "junk at end of xml declaration"
            r18 = r6
            r22 = 36
            goto L_0x001e
        L_0x05ca:
            java.lang.String r17 = "<?xml must be at start of file"
            r22 = 36
            r18 = r19
            goto L_0x001e
        L_0x05d2:
            int r7 = r13 - r6
            r2 = r27
            r2.processingInstructionFromParser(r3, r4, r5, r6, r7)
        L_0x05d9:
            r4 = r16
            r6 = -1
            r22 = 1
            goto L_0x0023
        L_0x05e0:
            r0 = r19
            r1 = r16
            if (r0 >= r1) goto L_0x0023
            int r18 = r19 + 1
            char r9 = r3[r19]
            r19 = r18
            goto L_0x0322
        L_0x05ee:
            r0 = r19
            r1 = r16
            if (r0 >= r1) goto L_0x0023
            int r18 = r19 + 1
            char r9 = r3[r19]
            r19 = r18
        L_0x05fa:
            r2 = 62
            if (r9 != r2) goto L_0x0692
            int r2 = r19 + -1
            int r5 = r2 - r4
            r2 = 4
            if (r5 < r2) goto L_0x0638
            char r2 = r3[r4]
            r7 = 45
            if (r2 != r7) goto L_0x0638
            int r2 = r4 + 1
            char r2 = r3[r2]
            r7 = 45
            if (r2 != r7) goto L_0x0638
            int r2 = r19 + -2
            char r2 = r3[r2]
            r7 = 45
            if (r2 != r7) goto L_0x05ee
            int r2 = r19 + -3
            char r2 = r3[r2]
            r7 = 45
            if (r2 != r7) goto L_0x05ee
            r0 = r19
            r1 = r26
            r1.pos = r0
            int r2 = r4 + 2
            int r7 = r5 + -4
            r0 = r27
            r0.commentFromParser(r3, r2, r7)
        L_0x0632:
            r4 = r16
            r22 = 1
            goto L_0x0023
        L_0x0638:
            r2 = 6
            if (r5 < r2) goto L_0x0632
            char r2 = r3[r4]
            r7 = 91
            if (r2 != r7) goto L_0x0632
            int r2 = r4 + 1
            char r2 = r3[r2]
            r7 = 67
            if (r2 != r7) goto L_0x0632
            int r2 = r4 + 2
            char r2 = r3[r2]
            r7 = 68
            if (r2 != r7) goto L_0x0632
            int r2 = r4 + 3
            char r2 = r3[r2]
            r7 = 65
            if (r2 != r7) goto L_0x0632
            int r2 = r4 + 4
            char r2 = r3[r2]
            r7 = 84
            if (r2 != r7) goto L_0x0632
            int r2 = r4 + 5
            char r2 = r3[r2]
            r7 = 65
            if (r2 != r7) goto L_0x0632
            int r2 = r4 + 6
            char r2 = r3[r2]
            r7 = 91
            if (r2 != r7) goto L_0x0632
            int r2 = r19 + -2
            char r2 = r3[r2]
            r7 = 93
            if (r2 != r7) goto L_0x05ee
            int r2 = r19 + -3
            char r2 = r3[r2]
            r7 = 93
            if (r2 != r7) goto L_0x05ee
            r0 = r19
            r1 = r26
            r1.pos = r0
            int r2 = r4 + 7
            int r7 = r19 + -10
            int r7 = r7 - r4
            r0 = r27
            r0.writeCDATA(r3, r2, r7)
            goto L_0x0632
        L_0x0692:
            int r2 = r4 + 7
            r0 = r19
            if (r0 != r2) goto L_0x05ee
            char r2 = r3[r4]
            r7 = 68
            if (r2 != r7) goto L_0x05ee
            int r2 = r4 + 1
            char r2 = r3[r2]
            r7 = 79
            if (r2 != r7) goto L_0x05ee
            int r2 = r4 + 2
            char r2 = r3[r2]
            r7 = 67
            if (r2 != r7) goto L_0x05ee
            int r2 = r4 + 3
            char r2 = r3[r2]
            r7 = 84
            if (r2 != r7) goto L_0x05ee
            int r2 = r4 + 4
            char r2 = r3[r2]
            r7 = 89
            if (r2 != r7) goto L_0x05ee
            int r2 = r4 + 5
            char r2 = r3[r2]
            r7 = 80
            if (r2 != r7) goto L_0x05ee
            r2 = 69
            if (r9 != r2) goto L_0x05ee
            r4 = r16
            r22 = 15
            goto L_0x0023
        L_0x06d0:
            r22 = 17
            int r4 = r18 + -1
            goto L_0x001e
        L_0x06d6:
            if (r6 >= 0) goto L_0x0870
            int r6 = r18 + -1
            int r6 = r6 - r4
            int r6 = r6 << 1
            r23 = 0
            r19 = r18
        L_0x06e1:
            r2 = 39
            if (r9 == r2) goto L_0x06e9
            r2 = 34
            if (r9 != r2) goto L_0x0701
        L_0x06e9:
            if (r23 != 0) goto L_0x06fa
            r23 = r9
        L_0x06ed:
            r0 = r19
            r1 = r16
            if (r0 >= r1) goto L_0x0023
            int r18 = r19 + 1
            char r9 = r3[r19]
            r19 = r18
            goto L_0x06e1
        L_0x06fa:
            r0 = r23
            if (r0 != r9) goto L_0x06ed
            r23 = 0
            goto L_0x06ed
        L_0x0701:
            if (r23 != 0) goto L_0x06ed
            r2 = 91
            if (r9 != r2) goto L_0x070a
            r6 = r6 | 1
            goto L_0x06ed
        L_0x070a:
            r2 = 93
            if (r9 != r2) goto L_0x0711
            r6 = r6 & -2
            goto L_0x06ed
        L_0x0711:
            r2 = 62
            if (r9 != r2) goto L_0x06ed
            r2 = r6 & 1
            if (r2 != 0) goto L_0x06ed
            r0 = r19
            r1 = r26
            r1.pos = r0
            int r6 = r6 >> 1
            int r6 = r6 + r4
            int r2 = r19 + -1
            int r7 = r2 - r6
            r2 = r27
            r2.emitDoctypeDecl(r3, r4, r5, r6, r7)
            r23 = 60
            r4 = r16
            r6 = -1
            r22 = 1
            goto L_0x0023
        L_0x0734:
            r23 = 60
            r10 = 14
            r2 = 47
            if (r9 != r2) goto L_0x0756
            r0 = r18
            r1 = r26
            r1.pos = r0
            r27.emitEndAttributes()
            r2 = 0
            r7 = 0
            r25 = 0
            r0 = r27
            r1 = r25
            r0.emitEndElement(r2, r7, r1)
            r22 = 27
            r19 = r18
            goto L_0x0023
        L_0x0756:
            r2 = 62
            if (r9 != r2) goto L_0x0769
            r0 = r18
            r1 = r26
            r1.pos = r0
            r27.emitEndAttributes()
            r22 = 1
            r19 = r18
            goto L_0x0023
        L_0x0769:
            int r4 = r18 + -1
            r22 = 9
            goto L_0x001e
        L_0x076f:
            r2 = 32
            if (r9 == r2) goto L_0x0021
            r2 = 9
            if (r9 == r2) goto L_0x0021
            r2 = 13
            if (r9 == r2) goto L_0x0021
            r2 = 10
            if (r9 == r2) goto L_0x0021
            r2 = 133(0x85, float:1.86E-43)
            if (r9 == r2) goto L_0x0021
            r2 = 8232(0x2028, float:1.1535E-41)
            if (r9 != r2) goto L_0x078b
            r19 = r18
            goto L_0x0023
        L_0x078b:
            int r2 = r18 - r5
            r0 = r26
            r0.pos = r2
            r0 = r27
            r0.emitStartAttribute(r3, r4, r5)
            r4 = r16
            r2 = 61
            if (r9 != r2) goto L_0x07a2
            r22 = 11
            r19 = r18
            goto L_0x0023
        L_0x07a2:
            r27.emitEndAttributes()
            java.lang.String r17 = "missing or misplaced '=' after attribute name"
            r22 = 36
            goto L_0x001e
        L_0x07ab:
            r2 = 39
            if (r9 == r2) goto L_0x07b3
            r2 = 34
            if (r9 != r2) goto L_0x07bd
        L_0x07b3:
            r23 = r9
            r10 = 12
            r22 = 1
            r19 = r18
            goto L_0x0023
        L_0x07bd:
            r2 = 32
            if (r9 == r2) goto L_0x0021
            r2 = 9
            if (r9 == r2) goto L_0x0021
            r2 = 13
            if (r9 == r2) goto L_0x0021
            r2 = 10
            if (r9 == r2) goto L_0x0021
            r2 = 133(0x85, float:1.86E-43)
            if (r9 == r2) goto L_0x0021
            r2 = 8232(0x2028, float:1.1535E-41)
            if (r9 != r2) goto L_0x07d9
            r19 = r18
            goto L_0x0023
        L_0x07d9:
            java.lang.String r17 = "missing or unquoted attribute value"
            r22 = 36
            goto L_0x001e
        L_0x07df:
            int r4 = r18 + -1
            r22 = 5
            goto L_0x001e
        L_0x07e5:
            r0 = r18
            r1 = r26
            r1.pos = r0
            r0 = r27
            r0.emitEndElement(r3, r4, r5)
            r4 = r16
            r22 = 29
            goto L_0x001e
        L_0x07f6:
            r2 = 62
            if (r9 == r2) goto L_0x0800
            java.lang.String r17 = "missing '>'"
            r22 = 36
            goto L_0x001e
        L_0x0800:
            r22 = 1
            r19 = r18
            goto L_0x0023
        L_0x0806:
            int r21 = r19 - r4
            if (r21 <= 0) goto L_0x0815
            r0 = r26
            r0.pos = r4     // Catch:{ IOException -> 0x0862 }
            int r2 = r21 + 1
            r0 = r26
            r0.mark(r2)     // Catch:{ IOException -> 0x0862 }
        L_0x0815:
            r0 = r19
            r1 = r26
            r1.pos = r0     // Catch:{ IOException -> 0x0862 }
            int r24 = r26.read()     // Catch:{ IOException -> 0x0862 }
            if (r24 >= 0) goto L_0x0836
            r2 = 1
            r0 = r22
            if (r0 == r2) goto L_0x082c
            r2 = 28
            r0 = r22
            if (r0 != r2) goto L_0x0830
        L_0x082c:
            r18 = r19
            goto L_0x006c
        L_0x0830:
            r22 = 37
            r18 = r19
            goto L_0x001e
        L_0x0836:
            if (r21 <= 0) goto L_0x085e
            r26.reset()     // Catch:{ IOException -> 0x0862 }
            r0 = r26
            r1 = r21
            r0.skip(r1)     // Catch:{ IOException -> 0x0862 }
        L_0x0842:
            r0 = r26
            int r0 = r0.pos
            r18 = r0
            r0 = r26
            char[] r3 = r0.buffer
            r0 = r26
            int r0 = r0.limit
            r16 = r0
            if (r21 <= 0) goto L_0x086d
            int r4 = r18 - r21
        L_0x0856:
            int r19 = r18 + 1
            char r9 = r3[r18]
            r18 = r19
            goto L_0x001e
        L_0x085e:
            r26.unread_quick()     // Catch:{ IOException -> 0x0862 }
            goto L_0x0842
        L_0x0862:
            r14 = move-exception
            java.lang.RuntimeException r2 = new java.lang.RuntimeException
            java.lang.String r7 = r14.getMessage()
            r2.<init>(r7)
            throw r2
        L_0x086d:
            r4 = r16
            goto L_0x0856
        L_0x0870:
            r19 = r18
            goto L_0x06e1
        L_0x0874:
            r19 = r18
            goto L_0x0322
        L_0x0878:
            r18 = r19
            goto L_0x001e
        L_0x087c:
            r19 = r18
            goto L_0x05fa
        L_0x0880:
            r19 = r18
            goto L_0x0271
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xml.XMLParser.parse(gnu.text.LineBufferedReader, gnu.xml.XMLFilter):void");
    }
}
