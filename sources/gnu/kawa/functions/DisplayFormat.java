package gnu.kawa.functions;

import gnu.bytecode.Access;
import gnu.expr.Keyword;
import gnu.kawa.xml.UntypedAtomic;
import gnu.lists.AbstractFormat;
import gnu.lists.Array;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Namespace;
import gnu.mapping.OutPort;
import gnu.mapping.Symbol;
import gnu.mapping.ThreadLocation;
import gnu.mapping.Values;
import gnu.math.IntNum;
import gnu.text.Char;
import java.util.regex.Pattern;

public class DisplayFormat extends AbstractFormat {
    public static final ThreadLocation outBase = new ThreadLocation("out-base");
    public static final ThreadLocation outRadix = new ThreadLocation("out-radix");
    static Pattern r5rsIdentifierMinusInteriorColons = Pattern.compile("(([a-zA-Z]|[!$%&*/:<=>?^_~])([a-zA-Z]|[!$%&*/<=>?^_~]|[0-9]|([-+.@]))*[:]?)|([-+]|[.][.][.])");
    char language;
    boolean readable;

    static {
        outBase.setGlobal(IntNum.ten());
    }

    public DisplayFormat(boolean readable2, char language2) {
        this.readable = readable2;
        this.language = language2;
    }

    public static DisplayFormat getEmacsLispFormat(boolean readable2) {
        return new DisplayFormat(readable2, 'E');
    }

    public static DisplayFormat getCommonLispFormat(boolean readable2) {
        return new DisplayFormat(readable2, Access.CLASS_CONTEXT);
    }

    public static DisplayFormat getSchemeFormat(boolean readable2) {
        return new DisplayFormat(readable2, 'S');
    }

    public boolean getReadableOutput() {
        return this.readable;
    }

    public void writeBoolean(boolean v, Consumer out) {
        write(this.language == 'S' ? v ? "#t" : "#f" : v ? "t" : "nil", out);
    }

    public void write(int v, Consumer out) {
        if (!getReadableOutput()) {
            Char.print(v, out);
        } else if (this.language != 'E' || v <= 32) {
            write(Char.toScmReadableString(v), out);
        } else {
            out.write(63);
            Char.print(v, out);
        }
    }

    public void writeList(LList value, OutPort out) {
        Object obj = value;
        out.startLogicalBlock("(", false, ")");
        while (obj instanceof Pair) {
            if (obj != value) {
                out.writeSpaceFill();
            }
            Pair pair = (Pair) obj;
            writeObject(pair.getCar(), out);
            obj = pair.getCdr();
        }
        if (obj != LList.Empty) {
            out.writeSpaceFill();
            out.write(". ");
            writeObject(LList.checkNonList(obj), out);
        }
        out.endLogicalBlock(")");
    }

    public void writeObject(Object obj, Consumer out) {
        boolean space = false;
        if ((out instanceof OutPort) && !(obj instanceof UntypedAtomic) && !(obj instanceof Values) && (getReadableOutput() || (!(obj instanceof Char) && !(obj instanceof CharSequence) && !(obj instanceof Character)))) {
            ((OutPort) out).writeWordStart();
            space = true;
        }
        writeObjectRaw(obj, out);
        if (space) {
            ((OutPort) out).writeWordEnd();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: gnu.mapping.Values} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v36, resolved type: gnu.kawa.xml.XmlNamespace} */
    /* JADX WARNING: type inference failed for: r30v2, types: [java.io.Writer] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeObjectRaw(java.lang.Object r29, gnu.lists.Consumer r30) {
        /*
            r28 = this;
            r0 = r29
            boolean r0 = r0 instanceof java.lang.Boolean
            r24 = r0
            if (r24 == 0) goto L_0x0018
            java.lang.Boolean r29 = (java.lang.Boolean) r29
            boolean r24 = r29.booleanValue()
            r0 = r28
            r1 = r24
            r2 = r30
            r0.writeBoolean(r1, r2)
        L_0x0017:
            return
        L_0x0018:
            r0 = r29
            boolean r0 = r0 instanceof gnu.text.Char
            r24 = r0
            if (r24 == 0) goto L_0x0030
            gnu.text.Char r29 = (gnu.text.Char) r29
            int r24 = r29.intValue()
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write(r1, r2)
            goto L_0x0017
        L_0x0030:
            r0 = r29
            boolean r0 = r0 instanceof java.lang.Character
            r24 = r0
            if (r24 == 0) goto L_0x0048
            java.lang.Character r29 = (java.lang.Character) r29
            char r24 = r29.charValue()
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write(r1, r2)
            goto L_0x0017
        L_0x0048:
            r0 = r29
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r24 = r0
            if (r24 == 0) goto L_0x008b
            r19 = r29
            gnu.mapping.Symbol r19 = (gnu.mapping.Symbol) r19
            gnu.mapping.Namespace r24 = r19.getNamespace()
            gnu.kawa.xml.XmlNamespace r25 = gnu.kawa.xml.XmlNamespace.HTML
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x0079
            java.lang.String r24 = "html:"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            java.lang.String r24 = r19.getLocalPart()
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x0017
        L_0x0079:
            r0 = r28
            boolean r0 = r0.readable
            r24 = r0
            r0 = r28
            r1 = r19
            r2 = r30
            r3 = r24
            r0.writeSymbol((gnu.mapping.Symbol) r1, (gnu.lists.Consumer) r2, (boolean) r3)
            goto L_0x0017
        L_0x008b:
            r0 = r29
            boolean r0 = r0 instanceof java.net.URI
            r24 = r0
            if (r24 == 0) goto L_0x00ca
            boolean r24 = r28.getReadableOutput()
            if (r24 == 0) goto L_0x00ca
            r0 = r30
            boolean r0 = r0 instanceof java.io.PrintWriter
            r24 = r0
            if (r24 == 0) goto L_0x00ca
            java.lang.String r24 = "#,(URI "
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            java.lang.String r25 = r29.toString()
            r24 = r30
            java.io.PrintWriter r24 = (java.io.PrintWriter) r24
            r26 = 1
            r0 = r25
            r1 = r24
            r2 = r26
            gnu.lists.Strings.printQuoted(r0, r1, r2)
            r24 = 41
            r0 = r30
            r1 = r24
            r0.write((int) r1)
            goto L_0x0017
        L_0x00ca:
            r0 = r29
            boolean r0 = r0 instanceof java.lang.CharSequence
            r24 = r0
            if (r24 == 0) goto L_0x013a
            r18 = r29
            java.lang.CharSequence r18 = (java.lang.CharSequence) r18
            boolean r24 = r28.getReadableOutput()
            if (r24 == 0) goto L_0x00f3
            r0 = r30
            boolean r0 = r0 instanceof java.io.PrintWriter
            r24 = r0
            if (r24 == 0) goto L_0x00f3
            java.io.PrintWriter r30 = (java.io.PrintWriter) r30
            r24 = 1
            r0 = r18
            r1 = r30
            r2 = r24
            gnu.lists.Strings.printQuoted(r0, r1, r2)
            goto L_0x0017
        L_0x00f3:
            r0 = r29
            boolean r0 = r0 instanceof java.lang.String
            r24 = r0
            if (r24 == 0) goto L_0x0106
            java.lang.String r29 = (java.lang.String) r29
            r0 = r30
            r1 = r29
            r0.write((java.lang.String) r1)
            goto L_0x0017
        L_0x0106:
            r0 = r29
            boolean r0 = r0 instanceof gnu.lists.CharSeq
            r24 = r0
            if (r24 == 0) goto L_0x0123
            r15 = r29
            gnu.lists.CharSeq r15 = (gnu.lists.CharSeq) r15
            r24 = 0
            int r25 = r15.size()
            r0 = r24
            r1 = r25
            r2 = r30
            r15.consume(r0, r1, r2)
            goto L_0x0017
        L_0x0123:
            int r13 = r18.length()
            r11 = 0
        L_0x0128:
            if (r11 >= r13) goto L_0x0017
            r0 = r18
            char r24 = r0.charAt(r11)
            r0 = r30
            r1 = r24
            r0.write((int) r1)
            int r11 = r11 + 1
            goto L_0x0128
        L_0x013a:
            r0 = r29
            boolean r0 = r0 instanceof gnu.lists.LList
            r24 = r0
            if (r24 == 0) goto L_0x0153
            r0 = r30
            boolean r0 = r0 instanceof gnu.mapping.OutPort
            r24 = r0
            if (r24 == 0) goto L_0x0153
            gnu.lists.LList r29 = (gnu.lists.LList) r29
            gnu.mapping.OutPort r30 = (gnu.mapping.OutPort) r30
            r28.writeList(r29, r30)
            goto L_0x0017
        L_0x0153:
            r0 = r29
            boolean r0 = r0 instanceof gnu.lists.SimpleVector
            r24 = r0
            if (r24 == 0) goto L_0x01fc
            r21 = r29
            gnu.lists.SimpleVector r21 = (gnu.lists.SimpleVector) r21
            java.lang.String r20 = r21.getTag()
            r0 = r28
            char r0 = r0.language
            r24 = r0
            r25 = 69
            r0 = r24
            r1 = r25
            if (r0 != r1) goto L_0x01c1
            java.lang.String r17 = "["
            java.lang.String r9 = "]"
        L_0x0175:
            r0 = r30
            boolean r0 = r0 instanceof gnu.mapping.OutPort
            r24 = r0
            if (r24 == 0) goto L_0x01e6
            r24 = r30
            gnu.mapping.OutPort r24 = (gnu.mapping.OutPort) r24
            r25 = 0
            r0 = r24
            r1 = r17
            r2 = r25
            r0.startLogicalBlock((java.lang.String) r1, (boolean) r2, (java.lang.String) r9)
        L_0x018c:
            int r24 = r21.size()
            int r10 = r24 << 1
            r12 = 0
        L_0x0193:
            if (r12 >= r10) goto L_0x01b0
            if (r12 <= 0) goto L_0x01a6
            r0 = r30
            boolean r0 = r0 instanceof gnu.mapping.OutPort
            r24 = r0
            if (r24 == 0) goto L_0x01a6
            r24 = r30
            gnu.mapping.OutPort r24 = (gnu.mapping.OutPort) r24
            r24.writeSpaceFill()
        L_0x01a6:
            r0 = r21
            r1 = r30
            boolean r24 = r0.consumeNext(r12, r1)
            if (r24 != 0) goto L_0x01f0
        L_0x01b0:
            r0 = r30
            boolean r0 = r0 instanceof gnu.mapping.OutPort
            r24 = r0
            if (r24 == 0) goto L_0x01f3
            gnu.mapping.OutPort r30 = (gnu.mapping.OutPort) r30
            r0 = r30
            r0.endLogicalBlock(r9)
            goto L_0x0017
        L_0x01c1:
            if (r20 != 0) goto L_0x01c8
            java.lang.String r17 = "#("
        L_0x01c5:
            java.lang.String r9 = ")"
            goto L_0x0175
        L_0x01c8:
            java.lang.StringBuilder r24 = new java.lang.StringBuilder
            r24.<init>()
            java.lang.String r25 = "#"
            java.lang.StringBuilder r24 = r24.append(r25)
            r0 = r24
            r1 = r20
            java.lang.StringBuilder r24 = r0.append(r1)
            java.lang.String r25 = "("
            java.lang.StringBuilder r24 = r24.append(r25)
            java.lang.String r17 = r24.toString()
            goto L_0x01c5
        L_0x01e6:
            r0 = r28
            r1 = r17
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x018c
        L_0x01f0:
            int r12 = r12 + 2
            goto L_0x0193
        L_0x01f3:
            r0 = r28
            r1 = r30
            r0.write((java.lang.String) r9, (gnu.lists.Consumer) r1)
            goto L_0x0017
        L_0x01fc:
            r0 = r29
            boolean r0 = r0 instanceof gnu.lists.Array
            r24 = r0
            if (r24 == 0) goto L_0x0219
            gnu.lists.Array r29 = (gnu.lists.Array) r29
            r24 = 0
            r25 = 0
            r0 = r28
            r1 = r29
            r2 = r24
            r3 = r25
            r4 = r30
            r0.write(r1, r2, r3, r4)
            goto L_0x0017
        L_0x0219:
            r0 = r29
            boolean r0 = r0 instanceof gnu.kawa.xml.KNode
            r24 = r0
            if (r24 == 0) goto L_0x025d
            boolean r24 = r28.getReadableOutput()
            if (r24 == 0) goto L_0x0232
            java.lang.String r24 = "#"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
        L_0x0232:
            r0 = r30
            boolean r0 = r0 instanceof java.io.Writer
            r24 = r0
            if (r24 == 0) goto L_0x0253
            java.io.Writer r30 = (java.io.Writer) r30
            r22 = r30
        L_0x023e:
            gnu.xml.XMLPrinter r23 = new gnu.xml.XMLPrinter
            r0 = r23
            r1 = r22
            r0.<init>((java.io.Writer) r1)
            r0 = r23
            r1 = r29
            r0.writeObject(r1)
            r23.closeThis()
            goto L_0x0017
        L_0x0253:
            gnu.lists.ConsumerWriter r22 = new gnu.lists.ConsumerWriter
            r0 = r22
            r1 = r30
            r0.<init>(r1)
            goto L_0x023e
        L_0x025d:
            gnu.mapping.Values r24 = gnu.mapping.Values.empty
            r0 = r29
            r1 = r24
            if (r0 != r1) goto L_0x0278
            boolean r24 = r28.getReadableOutput()
            if (r24 == 0) goto L_0x0278
            java.lang.String r24 = "#!void"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x0017
        L_0x0278:
            r0 = r29
            boolean r0 = r0 instanceof gnu.lists.Consumable
            r24 = r0
            if (r24 == 0) goto L_0x0287
            gnu.lists.Consumable r29 = (gnu.lists.Consumable) r29
            r29.consume(r30)
            goto L_0x0017
        L_0x0287:
            r0 = r29
            boolean r0 = r0 instanceof gnu.text.Printable
            r24 = r0
            if (r24 == 0) goto L_0x0296
            gnu.text.Printable r29 = (gnu.text.Printable) r29
            r29.print(r30)
            goto L_0x0017
        L_0x0296:
            r0 = r29
            boolean r0 = r0 instanceof gnu.math.RatNum
            r24 = r0
            if (r24 == 0) goto L_0x037a
            r6 = 10
            r16 = 0
            gnu.mapping.ThreadLocation r24 = outBase
            r25 = 0
            java.lang.Object r7 = r24.get(r25)
            gnu.mapping.ThreadLocation r24 = outRadix
            r25 = 0
            java.lang.Object r14 = r24.get(r25)
            if (r14 == 0) goto L_0x02c8
            java.lang.Boolean r24 = java.lang.Boolean.TRUE
            r0 = r24
            if (r14 == r0) goto L_0x02c6
            java.lang.String r24 = "yes"
            java.lang.String r25 = r14.toString()
            boolean r24 = r24.equals(r25)
            if (r24 == 0) goto L_0x02c8
        L_0x02c6:
            r16 = 1
        L_0x02c8:
            boolean r0 = r7 instanceof java.lang.Number
            r24 = r0
            if (r24 == 0) goto L_0x0317
            r24 = r7
            gnu.math.IntNum r24 = (gnu.math.IntNum) r24
            int r6 = r24.intValue()
        L_0x02d6:
            r24 = r29
            gnu.math.RatNum r24 = (gnu.math.RatNum) r24
            r0 = r24
            java.lang.String r5 = r0.toString(r6)
            if (r16 == 0) goto L_0x02f3
            r24 = 16
            r0 = r24
            if (r6 != r0) goto L_0x0322
            java.lang.String r24 = "#x"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
        L_0x02f3:
            r0 = r28
            r1 = r30
            r0.write((java.lang.String) r5, (gnu.lists.Consumer) r1)
            if (r16 == 0) goto L_0x0017
            r24 = 10
            r0 = r24
            if (r6 != r0) goto L_0x0017
            r0 = r29
            boolean r0 = r0 instanceof gnu.math.IntNum
            r24 = r0
            if (r24 == 0) goto L_0x0017
            java.lang.String r24 = "."
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x0017
        L_0x0317:
            if (r7 == 0) goto L_0x02d6
            java.lang.String r24 = r7.toString()
            int r6 = java.lang.Integer.parseInt(r24)
            goto L_0x02d6
        L_0x0322:
            r24 = 8
            r0 = r24
            if (r6 != r0) goto L_0x0334
            java.lang.String r24 = "#o"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x02f3
        L_0x0334:
            r24 = 2
            r0 = r24
            if (r6 != r0) goto L_0x0346
            java.lang.String r24 = "#b"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x02f3
        L_0x0346:
            r24 = 10
            r0 = r24
            if (r6 != r0) goto L_0x0354
            r0 = r29
            boolean r0 = r0 instanceof gnu.math.IntNum
            r24 = r0
            if (r24 != 0) goto L_0x02f3
        L_0x0354:
            java.lang.StringBuilder r24 = new java.lang.StringBuilder
            r24.<init>()
            java.lang.String r25 = "#"
            java.lang.StringBuilder r24 = r24.append(r25)
            r0 = r24
            java.lang.StringBuilder r24 = r0.append(r7)
            java.lang.String r25 = "r"
            java.lang.StringBuilder r24 = r24.append(r25)
            java.lang.String r24 = r24.toString()
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x02f3
        L_0x037a:
            r0 = r29
            boolean r0 = r0 instanceof java.lang.Enum
            r24 = r0
            if (r24 == 0) goto L_0x03b5
            boolean r24 = r28.getReadableOutput()
            if (r24 == 0) goto L_0x03b5
            java.lang.Class r24 = r29.getClass()
            java.lang.String r24 = r24.getName()
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            java.lang.String r24 = ":"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            java.lang.Enum r29 = (java.lang.Enum) r29
            java.lang.String r24 = r29.name()
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x0017
        L_0x03b5:
            if (r29 != 0) goto L_0x03c7
            r5 = 0
        L_0x03b8:
            if (r5 != 0) goto L_0x044f
            java.lang.String r24 = "#!null"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x0017
        L_0x03c7:
            java.lang.Class r8 = r29.getClass()
            boolean r24 = r8.isArray()
            if (r24 == 0) goto L_0x0449
            int r13 = java.lang.reflect.Array.getLength(r29)
            r0 = r30
            boolean r0 = r0 instanceof gnu.mapping.OutPort
            r24 = r0
            if (r24 == 0) goto L_0x041b
            r24 = r30
            gnu.mapping.OutPort r24 = (gnu.mapping.OutPort) r24
            java.lang.String r25 = "["
            r26 = 0
            java.lang.String r27 = "]"
            r24.startLogicalBlock((java.lang.String) r25, (boolean) r26, (java.lang.String) r27)
        L_0x03ea:
            r11 = 0
        L_0x03eb:
            if (r11 >= r13) goto L_0x0427
            if (r11 <= 0) goto L_0x0409
            java.lang.String r24 = " "
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            r0 = r30
            boolean r0 = r0 instanceof gnu.mapping.OutPort
            r24 = r0
            if (r24 == 0) goto L_0x0409
            r24 = r30
            gnu.mapping.OutPort r24 = (gnu.mapping.OutPort) r24
            r24.writeBreakFill()
        L_0x0409:
            r0 = r29
            java.lang.Object r24 = java.lang.reflect.Array.get(r0, r11)
            r0 = r28
            r1 = r24
            r2 = r30
            r0.writeObject(r1, r2)
            int r11 = r11 + 1
            goto L_0x03eb
        L_0x041b:
            java.lang.String r24 = "["
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x03ea
        L_0x0427:
            r0 = r30
            boolean r0 = r0 instanceof gnu.mapping.OutPort
            r24 = r0
            if (r24 == 0) goto L_0x043c
            gnu.mapping.OutPort r30 = (gnu.mapping.OutPort) r30
            java.lang.String r24 = "]"
            r0 = r30
            r1 = r24
            r0.endLogicalBlock(r1)
            goto L_0x0017
        L_0x043c:
            java.lang.String r24 = "]"
            r0 = r28
            r1 = r24
            r2 = r30
            r0.write((java.lang.String) r1, (gnu.lists.Consumer) r2)
            goto L_0x0017
        L_0x0449:
            java.lang.String r5 = r29.toString()
            goto L_0x03b8
        L_0x044f:
            r0 = r28
            r1 = r30
            r0.write((java.lang.String) r5, (gnu.lists.Consumer) r1)
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.functions.DisplayFormat.writeObjectRaw(java.lang.Object, gnu.lists.Consumer):void");
    }

    /* access modifiers changed from: package-private */
    public int write(Array array, int index, int level, Consumer out) {
        int step;
        int rank = array.rank();
        int count = 0;
        String start = level > 0 ? "(" : rank == 1 ? "#(" : "#" + rank + "a(";
        if (out instanceof OutPort) {
            ((OutPort) out).startLogicalBlock(start, false, ")");
        } else {
            write(start, out);
        }
        if (rank > 0) {
            int size = array.getSize(level);
            int level2 = level + 1;
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    write(" ", out);
                    if (out instanceof OutPort) {
                        ((OutPort) out).writeBreakFill();
                    }
                }
                if (level2 == rank) {
                    writeObject(array.getRowMajor(index), out);
                    step = 1;
                } else {
                    step = write(array, index, level2, out);
                }
                index += step;
                count += step;
            }
        }
        if (out instanceof OutPort) {
            ((OutPort) out).endLogicalBlock(")");
        } else {
            write(")", out);
        }
        return count;
    }

    /* access modifiers changed from: package-private */
    public void writeSymbol(Symbol sym, Consumer out, boolean readable2) {
        boolean hasUri;
        boolean hasPrefix = true;
        String prefix = sym.getPrefix();
        Namespace namespace = sym.getNamespace();
        String uri = namespace == null ? null : namespace.getName();
        if (uri == null || uri.length() <= 0) {
            hasUri = false;
        } else {
            hasUri = true;
        }
        if (prefix == null || prefix.length() <= 0) {
            hasPrefix = false;
        }
        boolean suffixColon = false;
        if (namespace == Keyword.keywordNamespace) {
            if (this.language == 'C' || this.language == 'E') {
                out.write(58);
            } else {
                suffixColon = true;
            }
        } else if (hasPrefix || hasUri) {
            if (hasPrefix) {
                writeSymbol(prefix, out, readable2);
            }
            if (hasUri && (readable2 || !hasPrefix)) {
                out.write(123);
                out.write(uri);
                out.write(125);
            }
            out.write(58);
        }
        writeSymbol(sym.getName(), out, readable2);
        if (suffixColon) {
            out.write(58);
        }
    }

    /* access modifiers changed from: package-private */
    public void writeSymbol(String sym, Consumer out, boolean readable2) {
        if (!readable2 || r5rsIdentifierMinusInteriorColons.matcher(sym).matches()) {
            write(sym, out);
            return;
        }
        int len = sym.length();
        if (len == 0) {
            write("||", out);
            return;
        }
        boolean inVerticalBars = false;
        for (int i = 0; i < len; i++) {
            char ch = sym.charAt(i);
            if (ch == '|') {
                write(inVerticalBars ? "|\\" : "\\", out);
                inVerticalBars = false;
            } else if (!inVerticalBars) {
                out.write(124);
                inVerticalBars = true;
            }
            out.write((int) ch);
        }
        if (inVerticalBars) {
            out.write(124);
        }
    }
}
