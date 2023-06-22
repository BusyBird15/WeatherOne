package gnu.kawa.lispexpr;

import androidx.core.internal.view.SupportMenu;
import gnu.bytecode.Access;
import gnu.expr.Keyword;
import gnu.expr.QuoteExp;
import gnu.expr.Special;
import gnu.kawa.util.GeneralHashTable;
import gnu.lists.F32Vector;
import gnu.lists.F64Vector;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.lists.S16Vector;
import gnu.lists.S32Vector;
import gnu.lists.S64Vector;
import gnu.lists.S8Vector;
import gnu.lists.Sequence;
import gnu.lists.SimpleVector;
import gnu.lists.U16Vector;
import gnu.lists.U32Vector;
import gnu.lists.U64Vector;
import gnu.lists.U8Vector;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.math.IntNum;
import gnu.text.Char;
import gnu.text.Lexer;
import gnu.text.LineBufferedReader;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import java.io.IOException;

public class LispReader extends Lexer {
    static final int SCM_COMPLEX = 1;
    public static final int SCM_NUMBERS = 1;
    public static final char TOKEN_ESCAPE_CHAR = '￿';
    protected boolean seenEscapes;
    GeneralHashTable<Integer, Object> sharedStructureTable;

    public LispReader(LineBufferedReader port) {
        super(port);
    }

    public LispReader(LineBufferedReader port, SourceMessages messages) {
        super(port, messages);
    }

    public final void readNestedComment(char c1, char c2) throws IOException, SyntaxException {
        int commentNesting = 1;
        int startLine = this.port.getLineNumber();
        int startColumn = this.port.getColumnNumber();
        do {
            int c = read();
            if (c == 124) {
                c = read();
                if (c == c1) {
                    commentNesting--;
                }
            } else if (c == c1 && (c = read()) == c2) {
                commentNesting++;
            }
            if (c < 0) {
                eofError("unexpected end-of-file in " + c1 + c2 + " comment starting here", startLine + 1, startColumn - 1);
                return;
            }
        } while (commentNesting > 0);
    }

    static char getReadCase() {
        try {
            char read_case = Environment.getCurrent().get("symbol-read-case", (Object) "P").toString().charAt(0);
            if (read_case == 'P') {
                return read_case;
            }
            if (read_case == 'u') {
                return 'U';
            }
            if (read_case == 'd' || read_case == 'l' || read_case == 'L') {
                return 'D';
            }
            if (read_case == 'i') {
                return Access.INNERCLASS_CONTEXT;
            }
            return read_case;
        } catch (Exception e) {
            return 'P';
        }
    }

    public Object readValues(int ch, ReadTable rtable) throws IOException, SyntaxException {
        return readValues(ch, rtable.lookup(ch), rtable);
    }

    public Object readValues(int ch, ReadTableEntry entry, ReadTable rtable) throws IOException, SyntaxException {
        int startPos = this.tokenBufferLength;
        this.seenEscapes = false;
        switch (entry.getKind()) {
            case 0:
                String err = "invalid character #\\" + ((char) ch);
                if (this.interactive) {
                    fatal(err);
                } else {
                    error(err);
                }
                return Values.empty;
            case 1:
                return Values.empty;
            case 5:
            case 6:
                return entry.read(this, ch, -1);
            default:
                return readAndHandleToken(ch, startPos, rtable);
        }
    }

    /* access modifiers changed from: protected */
    public Object readAndHandleToken(int ch, int startPos, ReadTable rtable) throws IOException, SyntaxException {
        int j;
        Object value;
        readToken(ch, getReadCase(), rtable);
        int endPos = this.tokenBufferLength;
        if (!this.seenEscapes && (value = parseNumber(this.tokenBuffer, startPos, endPos - startPos, 0, 0, 1)) != null && !(value instanceof String)) {
            return value;
        }
        char readCase = getReadCase();
        if (readCase == 'I') {
            int upperCount = 0;
            int lowerCount = 0;
            int i = startPos;
            while (i < endPos) {
                char ci = this.tokenBuffer[i];
                if (ci == 65535) {
                    i++;
                } else if (Character.isLowerCase(ci)) {
                    lowerCount++;
                } else if (Character.isUpperCase(ci)) {
                    upperCount++;
                }
                i++;
            }
            if (lowerCount == 0) {
                readCase = 'D';
            } else if (upperCount == 0) {
                readCase = 'U';
            } else {
                readCase = 'P';
            }
        }
        boolean handleUri = endPos >= startPos + 2 && this.tokenBuffer[endPos + -1] == '}' && this.tokenBuffer[endPos + -2] != 65535 && peek() == 58;
        int packageMarker = -1;
        int lbrace = -1;
        int rbrace = -1;
        int braceNesting = 0;
        int i2 = startPos;
        int j2 = startPos;
        while (i2 < endPos) {
            char ci2 = this.tokenBuffer[i2];
            if (ci2 == 65535) {
                i2++;
                if (i2 < endPos) {
                    j = j2 + 1;
                    this.tokenBuffer[j2] = this.tokenBuffer[i2];
                } else {
                    j = j2;
                }
            } else {
                if (handleUri) {
                    if (ci2 == '{') {
                        if (lbrace < 0) {
                            lbrace = j2;
                        } else if (braceNesting == 0) {
                        }
                        braceNesting++;
                    } else if (ci2 == '}') {
                        braceNesting--;
                        if (braceNesting >= 0) {
                            if (braceNesting == 0) {
                                if (rbrace < 0) {
                                    rbrace = j2;
                                }
                            }
                        }
                    }
                }
                if (braceNesting <= 0) {
                    if (ci2 == ':') {
                        packageMarker = packageMarker >= 0 ? -1 : j2;
                    } else if (readCase == 'U') {
                        ci2 = Character.toUpperCase(ci2);
                    } else if (readCase == 'D') {
                        ci2 = Character.toLowerCase(ci2);
                    }
                }
                j = j2 + 1;
                this.tokenBuffer[j2] = ci2;
            }
            i2++;
            j2 = j;
        }
        int endPos2 = j2;
        int len = endPos2 - startPos;
        if (lbrace >= 0 && rbrace > lbrace) {
            String prefix = lbrace > 0 ? new String(this.tokenBuffer, startPos, lbrace - startPos) : null;
            int lbrace2 = lbrace + 1;
            String str = new String(this.tokenBuffer, lbrace2, rbrace - lbrace2);
            int ch2 = read();
            int ch3 = read();
            Object rightOperand = readValues(ch3, rtable.lookup(ch3), rtable);
            if (!(rightOperand instanceof SimpleSymbol)) {
                error("expected identifier in symbol after '{URI}:'");
            }
            return Symbol.valueOf(rightOperand.toString(), str, prefix);
        } else if (rtable.initialColonIsKeyword && packageMarker == startPos && len > 1) {
            int startPos2 = startPos + 1;
            return Keyword.make(new String(this.tokenBuffer, startPos2, endPos2 - startPos2).intern());
        } else if (!rtable.finalColonIsKeyword || packageMarker != endPos2 - 1 || (len <= 1 && !this.seenEscapes)) {
            return rtable.makeSymbol(new String(this.tokenBuffer, startPos, len));
        } else {
            return Keyword.make(new String(this.tokenBuffer, startPos, len - 1).intern());
        }
    }

    /* access modifiers changed from: package-private */
    public void readToken(int ch, char readCase, ReadTable rtable) throws IOException, SyntaxException {
        boolean inEscapes = false;
        int braceNesting = 0;
        while (true) {
            if (ch < 0) {
                if (inEscapes) {
                    eofError("unexpected EOF between escapes");
                } else {
                    return;
                }
            }
            ReadTableEntry entry = rtable.lookup(ch);
            int kind = entry.getKind();
            if (kind != 0) {
                if (ch == rtable.postfixLookupOperator && !inEscapes) {
                    int next = this.port.peek();
                    if (next == rtable.postfixLookupOperator) {
                        unread(ch);
                        return;
                    } else if (validPostfixLookupStart(next, rtable)) {
                        kind = 5;
                    }
                }
                if (kind == 3) {
                    int ch2 = read();
                    if (ch2 < 0) {
                        eofError("unexpected EOF after single escape");
                    }
                    if (rtable.hexEscapeAfterBackslash && (ch2 == 120 || ch2 == 88)) {
                        ch2 = readHexEscape();
                    }
                    tokenBufferAppend(SupportMenu.USER_MASK);
                    tokenBufferAppend(ch2);
                    this.seenEscapes = true;
                } else if (kind == 4) {
                    inEscapes = !inEscapes;
                    this.seenEscapes = true;
                } else if (inEscapes) {
                    tokenBufferAppend(SupportMenu.USER_MASK);
                    tokenBufferAppend(ch);
                } else {
                    switch (kind) {
                        case 1:
                            unread(ch);
                            return;
                        case 2:
                            if (ch == 123 && entry == ReadTableEntry.brace) {
                                braceNesting++;
                                break;
                            }
                        case 4:
                            inEscapes = true;
                            this.seenEscapes = true;
                            continue;
                        case 5:
                            unread(ch);
                            return;
                        case 6:
                            break;
                    }
                    tokenBufferAppend(ch);
                }
            } else if (inEscapes) {
                tokenBufferAppend(SupportMenu.USER_MASK);
                tokenBufferAppend(ch);
            } else if (ch != 125 || braceNesting - 1 < 0) {
                unread(ch);
            } else {
                tokenBufferAppend(ch);
            }
            ch = read();
        }
        unread(ch);
    }

    public Object readObject() throws IOException, SyntaxException {
        int line;
        int column;
        Object value;
        char saveReadState = ((InPort) this.port).readState;
        int startPos = this.tokenBufferLength;
        ((InPort) this.port).readState = ' ';
        try {
            ReadTable rtable = ReadTable.getCurrent();
            do {
                line = this.port.getLineNumber();
                column = this.port.getColumnNumber();
                int ch = this.port.read();
                if (ch < 0) {
                    Object obj = Sequence.eofValue;
                    this.tokenBufferLength = startPos;
                    ((InPort) this.port).readState = saveReadState;
                    return obj;
                }
                value = readValues(ch, rtable);
            } while (value == Values.empty);
            Object handlePostfix = handlePostfix(value, rtable, line, column);
            this.tokenBufferLength = startPos;
            ((InPort) this.port).readState = saveReadState;
            return handlePostfix;
        } catch (Throwable th) {
            Throwable th2 = th;
            this.tokenBufferLength = startPos;
            ((InPort) this.port).readState = saveReadState;
            throw th2;
        }
    }

    /* access modifiers changed from: protected */
    public boolean validPostfixLookupStart(int ch, ReadTable rtable) throws IOException {
        if (ch < 0 || ch == 58 || ch == rtable.postfixLookupOperator) {
            return false;
        }
        if (ch == 44) {
            return true;
        }
        int kind = rtable.lookup(ch).getKind();
        if (kind == 2 || kind == 6 || kind == 4 || kind == 3) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public Object handlePostfix(Object value, ReadTable rtable, int line, int column) throws IOException, SyntaxException {
        if (value == QuoteExp.voidExp) {
            value = Values.empty;
        }
        while (true) {
            int ch = this.port.peek();
            if (ch < 0 || ch != rtable.postfixLookupOperator) {
                break;
            }
            this.port.read();
            if (!validPostfixLookupStart(this.port.peek(), rtable)) {
                unread();
                break;
            }
            int ch2 = this.port.read();
            value = PairWithPosition.make(LispLanguage.lookup_sym, LList.list2(value, LList.list2(rtable.makeSymbol(LispLanguage.quasiquote_sym), readValues(ch2, rtable.lookup(ch2), rtable))), this.port.getName(), line + 1, column + 1);
        }
        return value;
    }

    private boolean isPotentialNumber(char[] buffer, int start, int end) {
        boolean z = true;
        int sawDigits = 0;
        for (int i = start; i < end; i++) {
            char ch = buffer[i];
            if (Character.isDigit(ch)) {
                sawDigits++;
            } else if (ch == '-' || ch == '+') {
                if (i + 1 == end) {
                    return false;
                }
            } else if (ch == '#') {
                return true;
            } else {
                if (Character.isLetter(ch) || ch == '/' || ch == '_' || ch == '^') {
                    if (i == start) {
                        return false;
                    }
                } else if (ch != '.') {
                    return false;
                }
            }
        }
        if (sawDigits <= 0) {
            z = false;
        }
        return z;
    }

    public static Object parseNumber(CharSequence str, int radix) {
        char[] buf;
        if (str instanceof FString) {
            buf = ((FString) str).data;
        } else {
            buf = str.toString().toCharArray();
        }
        return parseNumber(buf, 0, str.length(), 0, radix, 1);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r39v0, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r39v1, resolved type: gnu.math.DFloNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r39v2, resolved type: gnu.math.DFloNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r39v3, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: type inference failed for: r38v2, types: [gnu.math.RatNum] */
    /* JADX WARNING: type inference failed for: r38v3, types: [gnu.math.RatNum] */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x01aa, code lost:
        r34 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x01ac, code lost:
        if (r6 >= 0) goto L_0x0511;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x01ae, code lost:
        if (r50 == false) goto L_0x01de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x01b4, code lost:
        if ((r13 + 4) >= r26) goto L_0x01de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x01bc, code lost:
        if (r54[r13 + 3] != '.') goto L_0x01de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x01c4, code lost:
        if (r54[r13 + 4] != '0') goto L_0x01de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x01ca, code lost:
        if (r54[r13] != 'i') goto L_0x0296;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x01d2, code lost:
        if (r54[r13 + 1] != 'n') goto L_0x0296;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x01da, code lost:
        if (r54[r13 + 2] != 'f') goto L_0x0296;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x01dc, code lost:
        r34 = 'i';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x01de, code lost:
        if (r34 != 0) goto L_0x02b0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x01e0, code lost:
        return "no digits";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x029a, code lost:
        if (r54[r13] != 'n') goto L_0x01de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:184:0x02a2, code lost:
        if (r54[r13 + 1] != 'a') goto L_0x01de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:186:0x02aa, code lost:
        if (r54[r13 + 2] != 'n') goto L_0x01de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:187:0x02ac, code lost:
        r34 = 'n';
     */
    /* JADX WARNING: Code restructure failed: missing block: B:188:0x02b0, code lost:
        r43 = r13 + 5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x02b4, code lost:
        if (r30 != false) goto L_0x02b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x02b6, code lost:
        if (r52 == false) goto L_0x02b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:0x02bc, code lost:
        if (r57 == 'i') goto L_0x02cc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:194:0x02c2, code lost:
        if (r57 == 'I') goto L_0x02cc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:196:0x02c8, code lost:
        if (r57 != ' ') goto L_0x0328;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:0x02ca, code lost:
        if (r30 == false) goto L_0x0328;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:198:0x02cc, code lost:
        r33 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x02ce, code lost:
        r27 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x02d2, code lost:
        if (r34 == 0) goto L_0x032e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x02da, code lost:
        if (r34 != 'i') goto L_0x032b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:203:0x02dc, code lost:
        r22 = Double.POSITIVE_INFINITY;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:205:0x02e0, code lost:
        if (r9 == false) goto L_0x02e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:0x02e2, code lost:
        r22 = -r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:207:0x02e7, code lost:
        r0 = new gnu.math.DFloNum(r22);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:209:0x02f2, code lost:
        if (r57 == 'e') goto L_0x02fa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:210:0x02f4, code lost:
        r38 = r38;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:211:0x02f8, code lost:
        if (r57 != 'E') goto L_0x02fe;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:212:0x02fa, code lost:
        r38 = r38.toExact();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:214:0x0302, code lost:
        if (r43 >= r26) goto L_0x04d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:215:0x0304, code lost:
        r13 = r43 + 1;
        r18 = r54[r43];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:216:0x030c, code lost:
        if (r18 != '@') goto L_0x042c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:217:0x030e, code lost:
        r4 = parseNumber(r54, r13, r26 - r13, r57, 10, r59);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:218:0x031e, code lost:
        if ((r4 instanceof java.lang.String) != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:220:0x0322, code lost:
        if ((r4 instanceof gnu.math.RealNum) != false) goto L_0x0409;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:221:0x0324, code lost:
        return "invalid complex polar constant";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:222:0x0328, code lost:
        r33 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:223:0x032b, code lost:
        r22 = Double.NaN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:224:0x032e, code lost:
        if (r29 >= 0) goto L_0x0332;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:225:0x0330, code lost:
        if (r21 < 0) goto L_0x039b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:227:0x0334, code lost:
        if (r6 <= r21) goto L_0x033a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:228:0x0336, code lost:
        if (r21 < 0) goto L_0x033a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:229:0x0338, code lost:
        r6 = r21;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:230:0x033a, code lost:
        if (r40 == null) goto L_0x0342;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:231:0x033c, code lost:
        r13 = r43;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x0342, code lost:
        r0 = new java.lang.String(r54, r6, r43 - r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:233:0x034d, code lost:
        if (r29 < 0) goto L_0x0385;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:0x034f, code lost:
        r27 = java.lang.Character.toLowerCase(r54[r29]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:235:0x0359, code lost:
        if (r27 == 'e') goto L_0x0385;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:236:0x035b, code lost:
        r44 = r29 - r6;
        r51 = r0.substring(0, r44) + 'e' + r0.substring(r44 + 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x0385, code lost:
        r22 = gnu.lists.Convert.parseDouble(r51);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:238:0x038b, code lost:
        if (r9 == false) goto L_0x0392;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:239:0x038d, code lost:
        r22 = -r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x0392, code lost:
        r0 = new gnu.math.DFloNum(r22);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:241:0x039b, code lost:
        r35 = valueOf(r54, r6, r43 - r6, r58, r9, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:242:0x03a5, code lost:
        if (r40 != null) goto L_0x03c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:243:0x03a7, code lost:
        r39 = r35;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:244:0x03ab, code lost:
        if (r33 == false) goto L_0x050d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:246:0x03b1, code lost:
        if (r39.isExact() == false) goto L_0x050d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:248:0x03b5, code lost:
        if (r41 == false) goto L_0x0404;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:250:0x03bb, code lost:
        if (r39.isZero() == false) goto L_0x0404;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:251:0x03bd, code lost:
        r14 = -0.0d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:252:0x03bf, code lost:
        r0 = new gnu.math.DFloNum(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:254:0x03ca, code lost:
        if (r35.isZero() == false) goto L_0x03f9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:0x03cc, code lost:
        r42 = r40.isZero();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:256:0x03d0, code lost:
        if (r33 == false) goto L_0x03e8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:258:0x03d4, code lost:
        if (r42 == false) goto L_0x03e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:259:0x03d6, code lost:
        r14 = Double.NaN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:260:0x03d8, code lost:
        r0 = new gnu.math.DFloNum(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:261:0x03dd, code lost:
        r39 = r38;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:262:0x03e0, code lost:
        if (r41 == false) goto L_0x03e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:263:0x03e2, code lost:
        r14 = Double.NEGATIVE_INFINITY;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:264:0x03e5, code lost:
        r14 = Double.POSITIVE_INFINITY;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:265:0x03e8, code lost:
        if (r42 == false) goto L_0x03f0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:266:0x03ea, code lost:
        r13 = r43;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:267:0x03f0, code lost:
        r38 = gnu.math.RatNum.make(r40, r35);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:268:0x03f9, code lost:
        r39 = gnu.math.RatNum.make(r40, r35);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:269:0x0404, code lost:
        r14 = r39.doubleValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:270:0x0409, code lost:
        r46 = (gnu.math.RealNum) r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:271:0x0411, code lost:
        if (r38.isZero() == false) goto L_0x0422;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:273:0x0417, code lost:
        if (r46.isExact() != false) goto L_0x0422;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:277:0x0430, code lost:
        if (r18 == '-') goto L_0x0438;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:279:0x0436, code lost:
        if (r18 != '+') goto L_0x0493;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:280:0x0438, code lost:
        r13 = r13 - 1;
        r32 = parseNumber(r54, r13, r26 - r13, r57, 10, r59);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:281:0x044c, code lost:
        if ((r32 instanceof java.lang.String) == false) goto L_0x0452;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:284:0x0456, code lost:
        if ((r32 instanceof gnu.math.Complex) != false) goto L_0x0475;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:286:0x0475, code lost:
        r19 = (gnu.math.Complex) r32;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:287:0x0481, code lost:
        if (r19.re().isZero() != false) goto L_0x0487;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:288:0x0483, code lost:
        return "invalid numeric constant";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:290:0x0493, code lost:
        r36 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:292:0x0499, code lost:
        if (java.lang.Character.isLetter(r18) != false) goto L_0x04ba;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:293:0x049b, code lost:
        r13 = r13 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:295:0x04a0, code lost:
        if (r36 != 1) goto L_0x04d3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:296:0x04a2, code lost:
        r45 = r54[r13 - 1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:297:0x04aa, code lost:
        if (r45 == 'i') goto L_0x04b2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:299:0x04b0, code lost:
        if (r45 != 'I') goto L_0x04d3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:301:0x04b4, code lost:
        if (r13 >= r26) goto L_0x04c7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:302:0x04b6, code lost:
        return "junk after imaginary suffix 'i'";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:303:0x04ba, code lost:
        r36 = r36 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:304:0x04be, code lost:
        if (r13 == r26) goto L_0x049d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:305:0x04c0, code lost:
        r18 = r54[r13];
        r13 = r13 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:307:0x04d3, code lost:
        return "excess junk after number";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:309:0x04db, code lost:
        if ((r38 instanceof gnu.math.DFloNum) == false) goto L_0x04ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:310:0x04dd, code lost:
        if (r27 <= 0) goto L_0x04ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:312:0x04e3, code lost:
        if (r27 == 'e') goto L_0x04ec;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:313:0x04e5, code lost:
        r22 = r38.doubleValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:314:0x04e9, code lost:
        switch(r27) {
            case 100: goto L_0x04fd;
            case 102: goto L_0x04f2;
            case 108: goto L_0x0505;
            case 115: goto L_0x04f2;
            default: goto L_0x04ec;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:315:0x04ec, code lost:
        r13 = r43;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:316:0x04f2, code lost:
        r13 = r43;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:317:0x04fd, code lost:
        r13 = r43;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:318:0x0505, code lost:
        r13 = r43;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:319:0x050d, code lost:
        r38 = r39;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:320:0x0511, code lost:
        r43 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:358:?, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:359:?, code lost:
        return "floating-point number after fraction symbol '/'";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:360:?, code lost:
        return "0/0 is undefined";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:361:?, code lost:
        return new gnu.math.DFloNum(0.0d);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:362:?, code lost:
        return gnu.math.Complex.polar(r38, r46);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:363:?, code lost:
        return r32;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:364:?, code lost:
        return "invalid numeric constant (" + r32 + ")";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:365:?, code lost:
        return gnu.math.Complex.make(r38, r19.im());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:366:?, code lost:
        return gnu.math.Complex.make((gnu.math.RealNum) gnu.math.IntNum.zero(), r38);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:367:?, code lost:
        return r38;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:368:?, code lost:
        return java.lang.Float.valueOf((float) r22);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:369:?, code lost:
        return java.lang.Double.valueOf(r22);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:370:?, code lost:
        return java.math.BigDecimal.valueOf(r22);
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object parseNumber(char[] r54, int r55, int r56, char r57, int r58, int r59) {
        /*
            int r26 = r55 + r56
            r13 = r55
            r0 = r26
            if (r13 < r0) goto L_0x000b
            java.lang.String r4 = "no digits"
        L_0x000a:
            return r4
        L_0x000b:
            int r43 = r13 + 1
            char r18 = r54[r13]
        L_0x000f:
            r5 = 35
            r0 = r18
            if (r0 != r5) goto L_0x00d5
            r0 = r43
            r1 = r26
            if (r0 < r1) goto L_0x0020
            java.lang.String r4 = "no digits"
            r13 = r43
            goto L_0x000a
        L_0x0020:
            int r13 = r43 + 1
            char r18 = r54[r43]
            switch(r18) {
                case 66: goto L_0x0044;
                case 68: goto L_0x0062;
                case 69: goto L_0x0076;
                case 73: goto L_0x0076;
                case 79: goto L_0x0058;
                case 88: goto L_0x006c;
                case 98: goto L_0x0044;
                case 100: goto L_0x0062;
                case 101: goto L_0x0076;
                case 105: goto L_0x0076;
                case 111: goto L_0x0058;
                case 120: goto L_0x006c;
                default: goto L_0x0027;
            }
        L_0x0027:
            r53 = 0
        L_0x0029:
            r5 = 10
            r0 = r18
            int r24 = java.lang.Character.digit(r0, r5)
            if (r24 >= 0) goto L_0x0089
            r5 = 82
            r0 = r18
            if (r0 == r5) goto L_0x003f
            r5 = 114(0x72, float:1.6E-43)
            r0 = r18
            if (r0 != r5) goto L_0x00b0
        L_0x003f:
            if (r58 == 0) goto L_0x009c
            java.lang.String r4 = "duplicate radix specifier"
            goto L_0x000a
        L_0x0044:
            if (r58 == 0) goto L_0x0049
            java.lang.String r4 = "duplicate radix specifier"
            goto L_0x000a
        L_0x0049:
            r58 = 2
            r43 = r13
        L_0x004d:
            r0 = r43
            r1 = r26
            if (r0 < r1) goto L_0x00cd
            java.lang.String r4 = "no digits"
            r13 = r43
            goto L_0x000a
        L_0x0058:
            if (r58 == 0) goto L_0x005d
            java.lang.String r4 = "duplicate radix specifier"
            goto L_0x000a
        L_0x005d:
            r58 = 8
            r43 = r13
            goto L_0x004d
        L_0x0062:
            if (r58 == 0) goto L_0x0067
            java.lang.String r4 = "duplicate radix specifier"
            goto L_0x000a
        L_0x0067:
            r58 = 10
            r43 = r13
            goto L_0x004d
        L_0x006c:
            if (r58 == 0) goto L_0x0071
            java.lang.String r4 = "duplicate radix specifier"
            goto L_0x000a
        L_0x0071:
            r58 = 16
            r43 = r13
            goto L_0x004d
        L_0x0076:
            if (r57 == 0) goto L_0x0084
            r5 = 32
            r0 = r57
            if (r0 != r5) goto L_0x0081
            java.lang.String r4 = "non-prefix exactness specifier"
            goto L_0x000a
        L_0x0081:
            java.lang.String r4 = "duplicate exactness specifier"
            goto L_0x000a
        L_0x0084:
            r57 = r18
            r43 = r13
            goto L_0x004d
        L_0x0089:
            int r5 = r53 * 10
            int r53 = r5 + r24
            r0 = r26
            if (r13 < r0) goto L_0x0095
            java.lang.String r4 = "missing letter after '#'"
            goto L_0x000a
        L_0x0095:
            int r43 = r13 + 1
            char r18 = r54[r13]
            r13 = r43
            goto L_0x0029
        L_0x009c:
            r5 = 2
            r0 = r53
            if (r0 < r5) goto L_0x00a7
            r5 = 35
            r0 = r53
            if (r0 <= r5) goto L_0x00ab
        L_0x00a7:
            java.lang.String r4 = "invalid radix specifier"
            goto L_0x000a
        L_0x00ab:
            r58 = r53
            r43 = r13
            goto L_0x004d
        L_0x00b0:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "unknown modifier '#"
            java.lang.StringBuilder r5 = r5.append(r7)
            r0 = r18
            java.lang.StringBuilder r5 = r5.append(r0)
            r7 = 39
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r4 = r5.toString()
            goto L_0x000a
        L_0x00cd:
            int r13 = r43 + 1
            char r18 = r54[r43]
            r43 = r13
            goto L_0x000f
        L_0x00d5:
            if (r57 != 0) goto L_0x00d9
            r57 = 32
        L_0x00d9:
            if (r58 != 0) goto L_0x00e3
            r31 = r56
        L_0x00dd:
            int r31 = r31 + -1
            if (r31 >= 0) goto L_0x0108
            r58 = 10
        L_0x00e3:
            r5 = 45
            r0 = r18
            if (r0 != r5) goto L_0x0113
            r9 = 1
        L_0x00ea:
            r41 = r9
            r5 = 45
            r0 = r18
            if (r0 == r5) goto L_0x00f8
            r5 = 43
            r0 = r18
            if (r0 != r5) goto L_0x0115
        L_0x00f8:
            r50 = 1
        L_0x00fa:
            if (r50 == 0) goto L_0x0515
            r0 = r43
            r1 = r26
            if (r0 < r1) goto L_0x0118
            java.lang.String r4 = "no digits following sign"
            r13 = r43
            goto L_0x000a
        L_0x0108:
            int r5 = r55 + r31
            char r5 = r54[r5]
            r7 = 46
            if (r5 != r7) goto L_0x00dd
            r58 = 10
            goto L_0x00e3
        L_0x0113:
            r9 = 0
            goto L_0x00ea
        L_0x0115:
            r50 = 0
            goto L_0x00fa
        L_0x0118:
            int r13 = r43 + 1
            char r18 = r54[r43]
        L_0x011c:
            r5 = 105(0x69, float:1.47E-43)
            r0 = r18
            if (r0 == r5) goto L_0x0128
            r5 = 73
            r0 = r18
            if (r0 != r5) goto L_0x0174
        L_0x0128:
            r0 = r26
            if (r13 != r0) goto L_0x0174
            int r5 = r13 + -2
            r0 = r55
            if (r0 != r5) goto L_0x0174
            r5 = r59 & 1
            if (r5 == 0) goto L_0x0174
            char r49 = r54[r55]
            r5 = 43
            r0 = r49
            if (r0 == r5) goto L_0x0148
            r5 = 45
            r0 = r49
            if (r0 == r5) goto L_0x0148
            java.lang.String r4 = "no digits"
            goto L_0x000a
        L_0x0148:
            r5 = 105(0x69, float:1.47E-43)
            r0 = r57
            if (r0 == r5) goto L_0x0154
            r5 = 73
            r0 = r57
            if (r0 != r5) goto L_0x0166
        L_0x0154:
            gnu.math.DComplex r4 = new gnu.math.DComplex
            r16 = 0
            if (r9 == 0) goto L_0x0163
            r14 = -4616189618054758400(0xbff0000000000000, double:-1.0)
        L_0x015c:
            r0 = r16
            r4.<init>(r0, r14)
            goto L_0x000a
        L_0x0163:
            r14 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            goto L_0x015c
        L_0x0166:
            if (r9 == 0) goto L_0x016f
            gnu.math.CComplex r5 = gnu.math.Complex.imMinusOne()
        L_0x016c:
            r4 = r5
            goto L_0x000a
        L_0x016f:
            gnu.math.CComplex r5 = gnu.math.Complex.imOne()
            goto L_0x016c
        L_0x0174:
            int r48 = r13 + -1
            r30 = 0
            r29 = -1
            r6 = -1
            r21 = -1
            r20 = 0
            r52 = 0
            r40 = 0
            r10 = 0
        L_0x0185:
            r0 = r18
            r1 = r58
            int r25 = java.lang.Character.digit(r0, r1)
            if (r25 < 0) goto L_0x01e4
            if (r30 == 0) goto L_0x0197
            if (r21 >= 0) goto L_0x0197
            java.lang.String r4 = "digit after '#' in number"
            goto L_0x000a
        L_0x0197:
            if (r6 >= 0) goto L_0x019b
            int r6 = r13 + -1
        L_0x019b:
            r0 = r58
            long r14 = (long) r0
            long r14 = r14 * r10
            r0 = r25
            long r0 = (long) r0
            r16 = r0
            long r10 = r14 + r16
        L_0x01a6:
            r0 = r26
            if (r13 != r0) goto L_0x028e
        L_0x01aa:
            r34 = 0
            if (r6 >= 0) goto L_0x0511
            if (r50 == 0) goto L_0x01de
            int r5 = r13 + 4
            r0 = r26
            if (r5 >= r0) goto L_0x01de
            int r5 = r13 + 3
            char r5 = r54[r5]
            r7 = 46
            if (r5 != r7) goto L_0x01de
            int r5 = r13 + 4
            char r5 = r54[r5]
            r7 = 48
            if (r5 != r7) goto L_0x01de
            char r5 = r54[r13]
            r7 = 105(0x69, float:1.47E-43)
            if (r5 != r7) goto L_0x0296
            int r5 = r13 + 1
            char r5 = r54[r5]
            r7 = 110(0x6e, float:1.54E-43)
            if (r5 != r7) goto L_0x0296
            int r5 = r13 + 2
            char r5 = r54[r5]
            r7 = 102(0x66, float:1.43E-43)
            if (r5 != r7) goto L_0x0296
            r34 = 105(0x69, float:1.47E-43)
        L_0x01de:
            if (r34 != 0) goto L_0x02b0
            java.lang.String r4 = "no digits"
            goto L_0x000a
        L_0x01e4:
            switch(r18) {
                case 46: goto L_0x01ea;
                case 47: goto L_0x0266;
                case 68: goto L_0x01fd;
                case 69: goto L_0x01fd;
                case 70: goto L_0x01fd;
                case 76: goto L_0x01fd;
                case 83: goto L_0x01fd;
                case 100: goto L_0x01fd;
                case 101: goto L_0x01fd;
                case 102: goto L_0x01fd;
                case 108: goto L_0x01fd;
                case 115: goto L_0x01fd;
                default: goto L_0x01e7;
            }
        L_0x01e7:
            int r13 = r13 + -1
            goto L_0x01aa
        L_0x01ea:
            if (r21 < 0) goto L_0x01f0
            java.lang.String r4 = "duplicate '.' in number"
            goto L_0x000a
        L_0x01f0:
            r5 = 10
            r0 = r58
            if (r0 == r5) goto L_0x01fa
            java.lang.String r4 = "'.' in non-decimal number"
            goto L_0x000a
        L_0x01fa:
            int r21 = r13 + -1
            goto L_0x01a6
        L_0x01fd:
            r0 = r26
            if (r13 == r0) goto L_0x0207
            r5 = 10
            r0 = r58
            if (r0 == r5) goto L_0x020a
        L_0x0207:
            int r13 = r13 + -1
            goto L_0x01aa
        L_0x020a:
            char r37 = r54[r13]
            int r28 = r13 + -1
            r5 = 43
            r0 = r37
            if (r0 == r5) goto L_0x021a
            r5 = 45
            r0 = r37
            if (r0 != r5) goto L_0x022e
        L_0x021a:
            int r13 = r13 + 1
            r0 = r26
            if (r13 >= r0) goto L_0x022a
            char r5 = r54[r13]
            r7 = 10
            int r5 = java.lang.Character.digit(r5, r7)
            if (r5 >= 0) goto L_0x023c
        L_0x022a:
            java.lang.String r4 = "missing exponent digits"
            goto L_0x000a
        L_0x022e:
            r5 = 10
            r0 = r37
            int r5 = java.lang.Character.digit(r0, r5)
            if (r5 >= 0) goto L_0x023c
            int r13 = r13 + -1
            goto L_0x01aa
        L_0x023c:
            if (r29 < 0) goto L_0x0242
            java.lang.String r4 = "duplicate exponent"
            goto L_0x000a
        L_0x0242:
            r5 = 10
            r0 = r58
            if (r0 == r5) goto L_0x024c
            java.lang.String r4 = "exponent in non-decimal number"
            goto L_0x000a
        L_0x024c:
            if (r6 >= 0) goto L_0x0252
            java.lang.String r4 = "mantissa with no digits"
            goto L_0x000a
        L_0x0252:
            r29 = r28
        L_0x0254:
            int r13 = r13 + 1
            r0 = r26
            if (r13 >= r0) goto L_0x01aa
            char r5 = r54[r13]
            r7 = 10
            int r5 = java.lang.Character.digit(r5, r7)
            if (r5 >= 0) goto L_0x0254
            goto L_0x01aa
        L_0x0266:
            if (r40 == 0) goto L_0x026c
            java.lang.String r4 = "multiple fraction symbol '/'"
            goto L_0x000a
        L_0x026c:
            if (r6 >= 0) goto L_0x0272
            java.lang.String r4 = "no digits before fraction symbol '/'"
            goto L_0x000a
        L_0x0272:
            if (r29 >= 0) goto L_0x0276
            if (r21 < 0) goto L_0x027a
        L_0x0276:
            java.lang.String r4 = "fraction symbol '/' following exponent or '.'"
            goto L_0x000a
        L_0x027a:
            int r7 = r13 - r6
            r5 = r54
            r8 = r58
            gnu.math.IntNum r40 = valueOf(r5, r6, r7, r8, r9, r10)
            r6 = -1
            r10 = 0
            r9 = 0
            r30 = 0
            r52 = 0
            goto L_0x01a6
        L_0x028e:
            int r43 = r13 + 1
            char r18 = r54[r13]
            r13 = r43
            goto L_0x0185
        L_0x0296:
            char r5 = r54[r13]
            r7 = 110(0x6e, float:1.54E-43)
            if (r5 != r7) goto L_0x01de
            int r5 = r13 + 1
            char r5 = r54[r5]
            r7 = 97
            if (r5 != r7) goto L_0x01de
            int r5 = r13 + 2
            char r5 = r54[r5]
            r7 = 110(0x6e, float:1.54E-43)
            if (r5 != r7) goto L_0x01de
            r34 = 110(0x6e, float:1.54E-43)
            goto L_0x01de
        L_0x02b0:
            int r13 = r13 + 5
            r43 = r13
        L_0x02b4:
            if (r30 != 0) goto L_0x02b8
            if (r52 == 0) goto L_0x02b8
        L_0x02b8:
            r5 = 105(0x69, float:1.47E-43)
            r0 = r57
            if (r0 == r5) goto L_0x02cc
            r5 = 73
            r0 = r57
            if (r0 == r5) goto L_0x02cc
            r5 = 32
            r0 = r57
            if (r0 != r5) goto L_0x0328
            if (r30 == 0) goto L_0x0328
        L_0x02cc:
            r33 = 1
        L_0x02ce:
            r38 = 0
            r27 = 0
            if (r34 == 0) goto L_0x032e
            r33 = 1
            r5 = 105(0x69, float:1.47E-43)
            r0 = r34
            if (r0 != r5) goto L_0x032b
            r22 = 9218868437227405312(0x7ff0000000000000, double:Infinity)
        L_0x02de:
            gnu.math.DFloNum r38 = new gnu.math.DFloNum
            if (r9 == 0) goto L_0x02e7
            r0 = r22
            double r0 = -r0
            r22 = r0
        L_0x02e7:
            r0 = r38
            r1 = r22
            r0.<init>((double) r1)
        L_0x02ee:
            r5 = 101(0x65, float:1.42E-43)
            r0 = r57
            if (r0 == r5) goto L_0x02fa
            r5 = 69
            r0 = r57
            if (r0 != r5) goto L_0x02fe
        L_0x02fa:
            gnu.math.RatNum r38 = r38.toExact()
        L_0x02fe:
            r0 = r43
            r1 = r26
            if (r0 >= r1) goto L_0x04d7
            int r13 = r43 + 1
            char r18 = r54[r43]
            r5 = 64
            r0 = r18
            if (r0 != r5) goto L_0x042c
            int r14 = r26 - r13
            r16 = 10
            r12 = r54
            r15 = r57
            r17 = r59
            java.lang.Object r4 = parseNumber(r12, r13, r14, r15, r16, r17)
            boolean r5 = r4 instanceof java.lang.String
            if (r5 != 0) goto L_0x000a
            boolean r5 = r4 instanceof gnu.math.RealNum
            if (r5 != 0) goto L_0x0409
            java.lang.String r4 = "invalid complex polar constant"
            goto L_0x000a
        L_0x0328:
            r33 = 0
            goto L_0x02ce
        L_0x032b:
            r22 = 9221120237041090560(0x7ff8000000000000, double:NaN)
            goto L_0x02de
        L_0x032e:
            if (r29 >= 0) goto L_0x0332
            if (r21 < 0) goto L_0x039b
        L_0x0332:
            r0 = r21
            if (r6 <= r0) goto L_0x033a
            if (r21 < 0) goto L_0x033a
            r6 = r21
        L_0x033a:
            if (r40 == 0) goto L_0x0342
            java.lang.String r4 = "floating-point number after fraction symbol '/'"
            r13 = r43
            goto L_0x000a
        L_0x0342:
            java.lang.String r51 = new java.lang.String
            int r5 = r43 - r6
            r0 = r51
            r1 = r54
            r0.<init>(r1, r6, r5)
            if (r29 < 0) goto L_0x0385
            char r5 = r54[r29]
            char r27 = java.lang.Character.toLowerCase(r5)
            r5 = 101(0x65, float:1.42E-43)
            r0 = r27
            if (r0 == r5) goto L_0x0385
            int r44 = r29 - r6
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r7 = 0
            r0 = r51
            r1 = r44
            java.lang.String r7 = r0.substring(r7, r1)
            java.lang.StringBuilder r5 = r5.append(r7)
            r7 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r5 = r5.append(r7)
            int r7 = r44 + 1
            r0 = r51
            java.lang.String r7 = r0.substring(r7)
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r51 = r5.toString()
        L_0x0385:
            double r22 = gnu.lists.Convert.parseDouble(r51)
            gnu.math.DFloNum r38 = new gnu.math.DFloNum
            if (r9 == 0) goto L_0x0392
            r0 = r22
            double r0 = -r0
            r22 = r0
        L_0x0392:
            r0 = r38
            r1 = r22
            r0.<init>((double) r1)
            goto L_0x02ee
        L_0x039b:
            int r7 = r43 - r6
            r5 = r54
            r8 = r58
            gnu.math.IntNum r35 = valueOf(r5, r6, r7, r8, r9, r10)
            if (r40 != 0) goto L_0x03c6
            r38 = r35
            r39 = r38
        L_0x03ab:
            if (r33 == 0) goto L_0x050d
            boolean r5 = r39.isExact()
            if (r5 == 0) goto L_0x050d
            gnu.math.DFloNum r38 = new gnu.math.DFloNum
            if (r41 == 0) goto L_0x0404
            boolean r5 = r39.isZero()
            if (r5 == 0) goto L_0x0404
            r14 = -9223372036854775808
        L_0x03bf:
            r0 = r38
            r0.<init>((double) r14)
            goto L_0x02ee
        L_0x03c6:
            boolean r5 = r35.isZero()
            if (r5 == 0) goto L_0x03f9
            boolean r42 = r40.isZero()
            if (r33 == 0) goto L_0x03e8
            gnu.math.DFloNum r38 = new gnu.math.DFloNum
            if (r42 == 0) goto L_0x03e0
            r14 = 9221120237041090560(0x7ff8000000000000, double:NaN)
        L_0x03d8:
            r0 = r38
            r0.<init>((double) r14)
        L_0x03dd:
            r39 = r38
            goto L_0x03ab
        L_0x03e0:
            if (r41 == 0) goto L_0x03e5
            r14 = -4503599627370496(0xfff0000000000000, double:-Infinity)
            goto L_0x03d8
        L_0x03e5:
            r14 = 9218868437227405312(0x7ff0000000000000, double:Infinity)
            goto L_0x03d8
        L_0x03e8:
            if (r42 == 0) goto L_0x03f0
            java.lang.String r4 = "0/0 is undefined"
            r13 = r43
            goto L_0x000a
        L_0x03f0:
            r0 = r40
            r1 = r35
            gnu.math.RatNum r38 = gnu.math.RatNum.make(r0, r1)
            goto L_0x03dd
        L_0x03f9:
            r0 = r40
            r1 = r35
            gnu.math.RatNum r38 = gnu.math.RatNum.make(r0, r1)
            r39 = r38
            goto L_0x03ab
        L_0x0404:
            double r14 = r39.doubleValue()
            goto L_0x03bf
        L_0x0409:
            r46 = r4
            gnu.math.RealNum r46 = (gnu.math.RealNum) r46
            boolean r5 = r38.isZero()
            if (r5 == 0) goto L_0x0422
            boolean r5 = r46.isExact()
            if (r5 != 0) goto L_0x0422
            gnu.math.DFloNum r4 = new gnu.math.DFloNum
            r14 = 0
            r4.<init>((double) r14)
            goto L_0x000a
        L_0x0422:
            r0 = r38
            r1 = r46
            gnu.math.DComplex r4 = gnu.math.Complex.polar((gnu.math.RealNum) r0, (gnu.math.RealNum) r1)
            goto L_0x000a
        L_0x042c:
            r5 = 45
            r0 = r18
            if (r0 == r5) goto L_0x0438
            r5 = 43
            r0 = r18
            if (r0 != r5) goto L_0x0493
        L_0x0438:
            int r13 = r13 + -1
            int r14 = r26 - r13
            r16 = 10
            r12 = r54
            r15 = r57
            r17 = r59
            java.lang.Object r32 = parseNumber(r12, r13, r14, r15, r16, r17)
            r0 = r32
            boolean r5 = r0 instanceof java.lang.String
            if (r5 == 0) goto L_0x0452
            r4 = r32
            goto L_0x000a
        L_0x0452:
            r0 = r32
            boolean r5 = r0 instanceof gnu.math.Complex
            if (r5 != 0) goto L_0x0475
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "invalid numeric constant ("
            java.lang.StringBuilder r5 = r5.append(r7)
            r0 = r32
            java.lang.StringBuilder r5 = r5.append(r0)
            java.lang.String r7 = ")"
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r4 = r5.toString()
            goto L_0x000a
        L_0x0475:
            r19 = r32
            gnu.math.Complex r19 = (gnu.math.Complex) r19
            gnu.math.RealNum r47 = r19.re()
            boolean r5 = r47.isZero()
            if (r5 != 0) goto L_0x0487
            java.lang.String r4 = "invalid numeric constant"
            goto L_0x000a
        L_0x0487:
            gnu.math.RealNum r5 = r19.im()
            r0 = r38
            gnu.math.Complex r4 = gnu.math.Complex.make((gnu.math.RealNum) r0, (gnu.math.RealNum) r5)
            goto L_0x000a
        L_0x0493:
            r36 = 0
        L_0x0495:
            boolean r5 = java.lang.Character.isLetter(r18)
            if (r5 != 0) goto L_0x04ba
            int r13 = r13 + -1
        L_0x049d:
            r5 = 1
            r0 = r36
            if (r0 != r5) goto L_0x04d3
            int r5 = r13 + -1
            char r45 = r54[r5]
            r5 = 105(0x69, float:1.47E-43)
            r0 = r45
            if (r0 == r5) goto L_0x04b2
            r5 = 73
            r0 = r45
            if (r0 != r5) goto L_0x04d3
        L_0x04b2:
            r0 = r26
            if (r13 >= r0) goto L_0x04c7
            java.lang.String r4 = "junk after imaginary suffix 'i'"
            goto L_0x000a
        L_0x04ba:
            int r36 = r36 + 1
            r0 = r26
            if (r13 == r0) goto L_0x049d
            int r43 = r13 + 1
            char r18 = r54[r13]
            r13 = r43
            goto L_0x0495
        L_0x04c7:
            gnu.math.IntNum r5 = gnu.math.IntNum.zero()
            r0 = r38
            gnu.math.Complex r4 = gnu.math.Complex.make((gnu.math.RealNum) r5, (gnu.math.RealNum) r0)
            goto L_0x000a
        L_0x04d3:
            java.lang.String r4 = "excess junk after number"
            goto L_0x000a
        L_0x04d7:
            r0 = r38
            boolean r5 = r0 instanceof gnu.math.DFloNum
            if (r5 == 0) goto L_0x04ec
            if (r27 <= 0) goto L_0x04ec
            r5 = 101(0x65, float:1.42E-43)
            r0 = r27
            if (r0 == r5) goto L_0x04ec
            double r22 = r38.doubleValue()
            switch(r27) {
                case 100: goto L_0x04fd;
                case 102: goto L_0x04f2;
                case 108: goto L_0x0505;
                case 115: goto L_0x04f2;
                default: goto L_0x04ec;
            }
        L_0x04ec:
            r13 = r43
            r4 = r38
            goto L_0x000a
        L_0x04f2:
            r0 = r22
            float r5 = (float) r0
            java.lang.Float r4 = java.lang.Float.valueOf(r5)
            r13 = r43
            goto L_0x000a
        L_0x04fd:
            java.lang.Double r4 = java.lang.Double.valueOf(r22)
            r13 = r43
            goto L_0x000a
        L_0x0505:
            java.math.BigDecimal r4 = java.math.BigDecimal.valueOf(r22)
            r13 = r43
            goto L_0x000a
        L_0x050d:
            r38 = r39
            goto L_0x02ee
        L_0x0511:
            r43 = r13
            goto L_0x02b4
        L_0x0515:
            r13 = r43
            goto L_0x011c
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.lispexpr.LispReader.parseNumber(char[], int, int, char, int, int):java.lang.Object");
    }

    private static IntNum valueOf(char[] buffer, int digits_start, int number_of_digits, int radix, boolean negative, long lvalue) {
        if (number_of_digits + radix > 28) {
            return IntNum.valueOf(buffer, digits_start, number_of_digits, radix, negative);
        }
        if (negative) {
            lvalue = -lvalue;
        }
        return IntNum.make(lvalue);
    }

    public int readEscape() throws IOException, SyntaxException {
        int c = read();
        if (c >= 0) {
            return readEscape(c);
        }
        eofError("unexpected EOF in character literal");
        return -1;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        eofError("unexpected EOF in literal");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0097, code lost:
        r11 = read();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x009d, code lost:
        if (r11 != 92) goto L_0x00a3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x009f, code lost:
        r11 = readEscape();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00a3, code lost:
        if (r11 != 63) goto L_0x00a9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00a5, code lost:
        return 127;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
        return r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:?, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
        return r11 & 159;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int readEscape(int r11) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r10 = this;
            r9 = 32
            r8 = 9
            r5 = -1
            r4 = 63
            r7 = 10
            char r6 = (char) r11
            switch(r6) {
                case 9: goto L_0x0030;
                case 10: goto L_0x0030;
                case 13: goto L_0x0030;
                case 32: goto L_0x0030;
                case 34: goto L_0x0026;
                case 48: goto L_0x00ad;
                case 49: goto L_0x00ad;
                case 50: goto L_0x00ad;
                case 51: goto L_0x00ad;
                case 52: goto L_0x00ad;
                case 53: goto L_0x00ad;
                case 54: goto L_0x00ad;
                case 55: goto L_0x00ad;
                case 67: goto L_0x0088;
                case 77: goto L_0x006b;
                case 88: goto L_0x00f2;
                case 92: goto L_0x0029;
                case 94: goto L_0x0097;
                case 97: goto L_0x000f;
                case 98: goto L_0x0011;
                case 101: goto L_0x0023;
                case 102: goto L_0x001d;
                case 110: goto L_0x0017;
                case 114: goto L_0x0020;
                case 116: goto L_0x0014;
                case 117: goto L_0x00ce;
                case 118: goto L_0x001a;
                case 120: goto L_0x00f2;
                default: goto L_0x000d;
            }
        L_0x000d:
            r4 = r11
        L_0x000e:
            return r4
        L_0x000f:
            r11 = 7
            goto L_0x000d
        L_0x0011:
            r11 = 8
            goto L_0x000d
        L_0x0014:
            r11 = 9
            goto L_0x000d
        L_0x0017:
            r11 = 10
            goto L_0x000d
        L_0x001a:
            r11 = 11
            goto L_0x000d
        L_0x001d:
            r11 = 12
            goto L_0x000d
        L_0x0020:
            r11 = 13
            goto L_0x000d
        L_0x0023:
            r11 = 27
            goto L_0x000d
        L_0x0026:
            r11 = 34
            goto L_0x000d
        L_0x0029:
            r11 = 92
            goto L_0x000d
        L_0x002c:
            int r11 = r10.read()
        L_0x0030:
            if (r11 >= 0) goto L_0x0039
            java.lang.String r4 = "unexpected EOF in literal"
            r10.eofError(r4)
            r4 = r5
            goto L_0x000e
        L_0x0039:
            if (r11 != r7) goto L_0x004a
        L_0x003b:
            if (r11 != r7) goto L_0x000d
        L_0x003d:
            int r11 = r10.read()
            if (r11 >= 0) goto L_0x0062
            java.lang.String r4 = "unexpected EOF in literal"
            r10.eofError(r4)
            r4 = r5
            goto L_0x000e
        L_0x004a:
            r4 = 13
            if (r11 != r4) goto L_0x005a
            int r4 = r10.peek()
            if (r4 != r7) goto L_0x0057
            r10.skip()
        L_0x0057:
            r11 = 10
            goto L_0x003b
        L_0x005a:
            if (r11 == r9) goto L_0x002c
            if (r11 == r8) goto L_0x002c
            r10.unread(r11)
            goto L_0x003b
        L_0x0062:
            if (r11 == r9) goto L_0x003d
            if (r11 == r8) goto L_0x003d
            r10.unread(r11)
            r4 = -2
            goto L_0x000e
        L_0x006b:
            int r11 = r10.read()
            r5 = 45
            if (r11 == r5) goto L_0x0079
            java.lang.String r5 = "Invalid escape character syntax"
            r10.error(r5)
            goto L_0x000e
        L_0x0079:
            int r11 = r10.read()
            r4 = 92
            if (r11 != r4) goto L_0x0085
            int r11 = r10.readEscape()
        L_0x0085:
            r4 = r11 | 128(0x80, float:1.794E-43)
            goto L_0x000e
        L_0x0088:
            int r11 = r10.read()
            r5 = 45
            if (r11 == r5) goto L_0x0097
            java.lang.String r5 = "Invalid escape character syntax"
            r10.error(r5)
            goto L_0x000e
        L_0x0097:
            int r11 = r10.read()
            r5 = 92
            if (r11 != r5) goto L_0x00a3
            int r11 = r10.readEscape()
        L_0x00a3:
            if (r11 != r4) goto L_0x00a9
            r4 = 127(0x7f, float:1.78E-43)
            goto L_0x000e
        L_0x00a9:
            r4 = r11 & 159(0x9f, float:2.23E-43)
            goto L_0x000e
        L_0x00ad:
            int r11 = r11 + -48
            r0 = 0
        L_0x00b0:
            int r0 = r0 + 1
            r4 = 3
            if (r0 >= r4) goto L_0x000d
            int r1 = r10.read()
            char r4 = (char) r1
            r5 = 8
            int r3 = java.lang.Character.digit(r4, r5)
            if (r3 < 0) goto L_0x00c7
            int r4 = r11 << 3
            int r11 = r4 + r3
            goto L_0x00b0
        L_0x00c7:
            if (r1 < 0) goto L_0x000d
            r10.unread(r1)
            goto L_0x000d
        L_0x00ce:
            r11 = 0
            r2 = 4
        L_0x00d0:
            int r2 = r2 + -1
            if (r2 < 0) goto L_0x000d
            int r1 = r10.read()
            if (r1 >= 0) goto L_0x00df
            java.lang.String r4 = "premature EOF in \\u escape"
            r10.eofError(r4)
        L_0x00df:
            char r4 = (char) r1
            r5 = 16
            int r3 = java.lang.Character.digit(r4, r5)
            if (r3 >= 0) goto L_0x00ed
            java.lang.String r4 = "non-hex character following \\u"
            r10.error(r4)
        L_0x00ed:
            int r4 = r11 * 16
            int r11 = r4 + r3
            goto L_0x00d0
        L_0x00f2:
            int r4 = r10.readHexEscape()
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.lispexpr.LispReader.readEscape(int):int");
    }

    public int readHexEscape() throws IOException, SyntaxException {
        int d;
        int c = 0;
        while (true) {
            d = read();
            int v = Character.digit((char) d, 16);
            if (v < 0) {
                break;
            }
            c = (c << 4) + v;
        }
        if (d != 59 && d >= 0) {
            unread(d);
        }
        return c;
    }

    public final Object readObject(int c) throws IOException, SyntaxException {
        unread(c);
        return readObject();
    }

    public Object readCommand() throws IOException, SyntaxException {
        return readObject();
    }

    /* access modifiers changed from: protected */
    public Object makeNil() {
        return LList.Empty;
    }

    /* access modifiers changed from: protected */
    public Pair makePair(Object car, int line, int column) {
        return makePair(car, LList.Empty, line, column);
    }

    /* access modifiers changed from: protected */
    public Pair makePair(Object car, Object cdr, int line, int column) {
        String pname = this.port.getName();
        if (pname == null || line < 0) {
            return Pair.make(car, cdr);
        }
        return PairWithPosition.make(car, cdr, pname, line + 1, column + 1);
    }

    /* access modifiers changed from: protected */
    public void setCdr(Object pair, Object cdr) {
        ((Pair) pair).setCdrBackdoor(cdr);
    }

    public static Object readNumberWithRadix(int previous, LispReader reader, int radix) throws IOException, SyntaxException {
        int startPos = reader.tokenBufferLength - previous;
        reader.readToken(reader.read(), 'P', ReadTable.getCurrent());
        int endPos = reader.tokenBufferLength;
        if (startPos == endPos) {
            reader.error("missing numeric token");
            return IntNum.zero();
        }
        Object result = parseNumber(reader.tokenBuffer, startPos, endPos - startPos, 0, radix, 0);
        if (result instanceof String) {
            reader.error((String) result);
            return IntNum.zero();
        } else if (result != null) {
            return result;
        } else {
            reader.error("invalid numeric constant");
            return IntNum.zero();
        }
    }

    public static Object readCharacter(LispReader reader) throws IOException, SyntaxException {
        int ch = reader.read();
        if (ch < 0) {
            reader.eofError("unexpected EOF in character literal");
        }
        int startPos = reader.tokenBufferLength;
        reader.tokenBufferAppend(ch);
        reader.readToken(reader.read(), 'D', ReadTable.getCurrent());
        char[] tokenBuffer = reader.tokenBuffer;
        int length = reader.tokenBufferLength - startPos;
        if (length == 1) {
            return Char.make(tokenBuffer[startPos]);
        }
        String name = new String(tokenBuffer, startPos, length);
        int ch2 = Char.nameToChar(name);
        if (ch2 >= 0) {
            return Char.make(ch2);
        }
        char ch3 = tokenBuffer[startPos];
        if (ch3 == 'x' || ch3 == 'X') {
            int value = 0;
            int i = 1;
            while (i != length) {
                int v = Character.digit(tokenBuffer[startPos + i], 16);
                if (v >= 0 && (value = (value * 16) + v) <= 1114111) {
                    i++;
                }
            }
            return Char.make(value);
        }
        int ch4 = Character.digit(ch3, 8);
        if (ch4 >= 0) {
            int value2 = ch4;
            int i2 = 1;
            while (i2 != length) {
                int ch5 = Character.digit(tokenBuffer[startPos + i2], 8);
                if (ch5 >= 0) {
                    value2 = (value2 * 8) + ch5;
                    i2++;
                }
            }
            return Char.make(value2);
        }
        reader.error("unknown character name: " + name);
        return Char.make(63);
    }

    public static Object readSpecial(LispReader reader) throws IOException, SyntaxException {
        int ch = reader.read();
        if (ch < 0) {
            reader.eofError("unexpected EOF in #! special form");
        }
        if (ch == 47 && reader.getLineNumber() == 0 && reader.getColumnNumber() == 3) {
            ReaderIgnoreRestOfLine.getInstance().read(reader, 35, 1);
            return Values.empty;
        }
        int startPos = reader.tokenBufferLength;
        reader.tokenBufferAppend(ch);
        reader.readToken(reader.read(), 'D', ReadTable.getCurrent());
        String name = new String(reader.tokenBuffer, startPos, reader.tokenBufferLength - startPos);
        if (name.equals("optional")) {
            return Special.optional;
        }
        if (name.equals("rest")) {
            return Special.rest;
        }
        if (name.equals("key")) {
            return Special.key;
        }
        if (name.equals("eof")) {
            return Special.eof;
        }
        if (name.equals("void")) {
            return QuoteExp.voidExp;
        }
        if (name.equals("default")) {
            return Special.dfault;
        }
        if (name.equals("undefined")) {
            return Special.undefined;
        }
        if (name.equals("abstract")) {
            return Special.abstractSpecial;
        }
        if (name.equals("null")) {
            return null;
        }
        reader.error("unknown named constant #!" + name);
        return null;
    }

    public static SimpleVector readSimpleVector(LispReader reader, char kind) throws IOException, SyntaxException {
        int ch;
        int size = 0;
        while (true) {
            ch = reader.read();
            if (ch < 0) {
                reader.eofError("unexpected EOF reading uniform vector");
            }
            int digit = Character.digit((char) ch, 10);
            if (digit < 0) {
                break;
            }
            size = (size * 10) + digit;
        }
        if ((size == 8 || size == 16 || size == 32 || size == 64) && ((kind != 'F' || size >= 32) && ch == 40)) {
            Object list = ReaderParens.readList(reader, 40, -1, 41);
            if (LList.listLength(list, false) < 0) {
                reader.error("invalid elements in uniform vector syntax");
                return null;
            }
            Sequence q = (Sequence) list;
            switch (kind) {
                case 'F':
                    switch (size) {
                        case 32:
                            return new F32Vector(q);
                        case 64:
                            return new F64Vector(q);
                    }
                case 'S':
                    break;
                case 'U':
                    break;
                default:
                    return null;
            }
            switch (size) {
                case 8:
                    return new S8Vector(q);
                case 16:
                    return new S16Vector(q);
                case 32:
                    return new S32Vector(q);
                case 64:
                    return new S64Vector(q);
            }
            switch (size) {
                case 8:
                    return new U8Vector(q);
                case 16:
                    return new U16Vector(q);
                case 32:
                    return new U32Vector(q);
                case 64:
                    return new U64Vector(q);
                default:
                    return null;
            }
        } else {
            reader.error("invalid uniform vector syntax");
            return null;
        }
    }
}
