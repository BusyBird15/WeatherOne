package gnu.lists;

import androidx.appcompat.widget.ActivityChooserView;
import gnu.kawa.lispexpr.LispReader;
import gnu.text.Char;
import java.io.PrintWriter;

public class TreeList extends AbstractSequence implements Appendable, XConsumer, PositionConsumer, Consumable {
    protected static final int BEGIN_ATTRIBUTE_LONG = 61705;
    public static final int BEGIN_ATTRIBUTE_LONG_SIZE = 5;
    protected static final int BEGIN_DOCUMENT = 61712;
    protected static final int BEGIN_ELEMENT_LONG = 61704;
    protected static final int BEGIN_ELEMENT_SHORT = 40960;
    protected static final int BEGIN_ELEMENT_SHORT_INDEX_MAX = 4095;
    public static final int BEGIN_ENTITY = 61714;
    public static final int BEGIN_ENTITY_SIZE = 5;
    static final char BOOL_FALSE = '';
    static final char BOOL_TRUE = '';
    static final int BYTE_PREFIX = 61440;
    static final int CDATA_SECTION = 61717;
    static final int CHAR_FOLLOWS = 61702;
    static final int COMMENT = 61719;
    protected static final int DOCUMENT_URI = 61720;
    static final int DOUBLE_FOLLOWS = 61701;
    static final int END_ATTRIBUTE = 61706;
    public static final int END_ATTRIBUTE_SIZE = 1;
    protected static final int END_DOCUMENT = 61713;
    protected static final int END_ELEMENT_LONG = 61708;
    protected static final int END_ELEMENT_SHORT = 61707;
    protected static final int END_ENTITY = 61715;
    static final int FLOAT_FOLLOWS = 61700;
    public static final int INT_FOLLOWS = 61698;
    static final int INT_SHORT_ZERO = 49152;
    static final int JOINER = 61718;
    static final int LONG_FOLLOWS = 61699;
    public static final int MAX_CHAR_SHORT = 40959;
    static final int MAX_INT_SHORT = 8191;
    static final int MIN_INT_SHORT = -4096;
    static final char OBJECT_REF_FOLLOWS = '';
    static final int OBJECT_REF_SHORT = 57344;
    static final int OBJECT_REF_SHORT_INDEX_MAX = 4095;
    protected static final char POSITION_PAIR_FOLLOWS = '';
    static final char POSITION_REF_FOLLOWS = '';
    protected static final int PROCESSING_INSTRUCTION = 61716;
    public int attrStart;
    int currentParent;
    public char[] data;
    public int docStart;
    public int gapEnd;
    public int gapStart;
    public Object[] objects;
    public int oindex;

    public TreeList() {
        this.currentParent = -1;
        resizeObjects();
        this.gapEnd = 200;
        this.data = new char[this.gapEnd];
    }

    public TreeList(TreeList list, int startPosition, int endPosition) {
        this();
        list.consumeIRange(startPosition, endPosition, this);
    }

    public TreeList(TreeList list) {
        this(list, 0, list.data.length);
    }

    public void clear() {
        this.gapStart = 0;
        this.gapEnd = this.data.length;
        this.attrStart = 0;
        if (this.gapEnd > 1500) {
            this.gapEnd = 200;
            this.data = new char[this.gapEnd];
        }
        this.objects = null;
        this.oindex = 0;
        resizeObjects();
    }

    public void ensureSpace(int needed) {
        int avail = this.gapEnd - this.gapStart;
        if (needed > avail) {
            int oldSize = this.data.length;
            int neededSize = (oldSize - avail) + needed;
            int newSize = oldSize * 2;
            if (newSize < neededSize) {
                newSize = neededSize;
            }
            char[] tmp = new char[newSize];
            if (this.gapStart > 0) {
                System.arraycopy(this.data, 0, tmp, 0, this.gapStart);
            }
            int afterGap = oldSize - this.gapEnd;
            if (afterGap > 0) {
                System.arraycopy(this.data, this.gapEnd, tmp, newSize - afterGap, afterGap);
            }
            this.gapEnd = newSize - afterGap;
            this.data = tmp;
        }
    }

    public final void resizeObjects() {
        Object[] tmp;
        if (this.objects == null) {
            tmp = new Object[100];
        } else {
            int oldLength = this.objects.length;
            tmp = new Object[(oldLength * 2)];
            System.arraycopy(this.objects, 0, tmp, 0, oldLength);
        }
        this.objects = tmp;
    }

    public int find(Object arg1) {
        if (this.oindex == this.objects.length) {
            resizeObjects();
        }
        this.objects[this.oindex] = arg1;
        int i = this.oindex;
        this.oindex = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public final int getIntN(int index) {
        return (this.data[index] << 16) | (this.data[index + 1] & LispReader.TOKEN_ESCAPE_CHAR);
    }

    /* access modifiers changed from: protected */
    public final long getLongN(int index) {
        char[] data2 = this.data;
        return ((((long) data2[index]) & 65535) << 48) | ((((long) data2[index + 1]) & 65535) << 32) | ((((long) data2[index + 2]) & 65535) << 16) | (((long) data2[index + 3]) & 65535);
    }

    public final void setIntN(int index, int i) {
        this.data[index] = (char) (i >> 16);
        this.data[index + 1] = (char) i;
    }

    public void consume(SeqPosition position) {
        ensureSpace(3);
        int index = find(position.copy());
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = POSITION_REF_FOLLOWS;
        setIntN(this.gapStart, index);
        this.gapStart += 2;
    }

    public void writePosition(AbstractSequence seq, int ipos) {
        ensureSpace(5);
        this.data[this.gapStart] = POSITION_PAIR_FOLLOWS;
        setIntN(this.gapStart + 1, find(seq));
        setIntN(this.gapStart + 3, ipos);
        this.gapStart += 5;
    }

    public void writeObject(Object v) {
        ensureSpace(3);
        int index = find(v);
        if (index < 4096) {
            char[] cArr = this.data;
            int i = this.gapStart;
            this.gapStart = i + 1;
            cArr[i] = (char) (OBJECT_REF_SHORT | index);
            return;
        }
        char[] cArr2 = this.data;
        int i2 = this.gapStart;
        this.gapStart = i2 + 1;
        cArr2[i2] = OBJECT_REF_FOLLOWS;
        setIntN(this.gapStart, index);
        this.gapStart += 2;
    }

    public void writeDocumentUri(Object uri) {
        ensureSpace(3);
        int index = find(uri);
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = 61720;
        setIntN(this.gapStart, index);
        this.gapStart += 2;
    }

    public void writeComment(char[] chars, int offset, int length) {
        ensureSpace(length + 3);
        int i = this.gapStart;
        int i2 = i + 1;
        this.data[i] = 61719;
        setIntN(i2, length);
        int i3 = i2 + 2;
        System.arraycopy(chars, offset, this.data, i3, length);
        this.gapStart = i3 + length;
    }

    public void writeComment(String comment, int offset, int length) {
        ensureSpace(length + 3);
        int i = this.gapStart;
        int i2 = i + 1;
        this.data[i] = 61719;
        setIntN(i2, length);
        int i3 = i2 + 2;
        comment.getChars(offset, offset + length, this.data, i3);
        this.gapStart = i3 + length;
    }

    public void writeProcessingInstruction(String target, char[] content, int offset, int length) {
        ensureSpace(length + 5);
        int i = this.gapStart;
        int i2 = i + 1;
        this.data[i] = 61716;
        setIntN(i2, find(target));
        setIntN(i2 + 2, length);
        int i3 = i2 + 4;
        System.arraycopy(content, offset, this.data, i3, length);
        this.gapStart = i3 + length;
    }

    public void writeProcessingInstruction(String target, String content, int offset, int length) {
        ensureSpace(length + 5);
        int i = this.gapStart;
        int i2 = i + 1;
        this.data[i] = 61716;
        setIntN(i2, find(target));
        setIntN(i2 + 2, length);
        int i3 = i2 + 4;
        content.getChars(offset, offset + length, this.data, i3);
        this.gapStart = i3 + length;
    }

    public void startElement(Object type) {
        startElement(find(type));
    }

    public void startDocument() {
        int i = -1;
        ensureSpace(6);
        this.gapEnd--;
        int p = this.gapStart;
        this.data[p] = 61712;
        if (this.docStart != 0) {
            throw new Error("nested document");
        }
        this.docStart = p + 1;
        setIntN(p + 1, this.gapEnd - this.data.length);
        int i2 = p + 3;
        if (this.currentParent != -1) {
            i = this.currentParent - p;
        }
        setIntN(i2, i);
        this.currentParent = p;
        this.gapStart = p + 5;
        this.currentParent = p;
        this.data[this.gapEnd] = 61713;
    }

    public void endDocument() {
        if (this.data[this.gapEnd] == END_DOCUMENT && this.docStart > 0 && this.data[this.currentParent] == BEGIN_DOCUMENT) {
            this.gapEnd++;
            setIntN(this.docStart, (this.gapStart - this.docStart) + 1);
            this.docStart = 0;
            char[] cArr = this.data;
            int i = this.gapStart;
            this.gapStart = i + 1;
            cArr[i] = 61713;
            int parent = getIntN(this.currentParent + 3);
            if (parent < -1) {
                parent += this.currentParent;
            }
            this.currentParent = parent;
            return;
        }
        throw new Error("unexpected endDocument");
    }

    public void beginEntity(Object base) {
        int i = -1;
        if (this.gapStart == 0) {
            ensureSpace(6);
            this.gapEnd--;
            int p = this.gapStart;
            this.data[p] = 61714;
            setIntN(p + 1, find(base));
            int i2 = p + 3;
            if (this.currentParent != -1) {
                i = this.currentParent - p;
            }
            setIntN(i2, i);
            this.gapStart = p + 5;
            this.currentParent = p;
            this.data[this.gapEnd] = 61715;
        }
    }

    public void endEntity() {
        if (this.gapEnd + 1 != this.data.length || this.data[this.gapEnd] != END_ENTITY) {
            return;
        }
        if (this.data[this.currentParent] != 61714) {
            throw new Error("unexpected endEntity");
        }
        this.gapEnd++;
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = 61715;
        int parent = getIntN(this.currentParent + 3);
        if (parent < -1) {
            parent += this.currentParent;
        }
        this.currentParent = parent;
    }

    public void startElement(int index) {
        ensureSpace(10);
        this.gapEnd -= 7;
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = 61704;
        setIntN(this.gapStart, this.gapEnd - this.data.length);
        this.gapStart += 2;
        this.data[this.gapEnd] = 61708;
        setIntN(this.gapEnd + 1, index);
        setIntN(this.gapEnd + 3, this.gapStart - 3);
        setIntN(this.gapEnd + 5, this.currentParent);
        this.currentParent = this.gapStart - 3;
    }

    public void setElementName(int elementIndex, int nameIndex) {
        if (this.data[elementIndex] == BEGIN_ELEMENT_LONG) {
            int j = getIntN(elementIndex + 1);
            if (j < 0) {
                elementIndex = this.data.length;
            }
            elementIndex += j;
        }
        if (elementIndex < this.gapEnd) {
            throw new Error("setElementName before gapEnd");
        }
        setIntN(elementIndex + 1, nameIndex);
    }

    public void endElement() {
        if (this.data[this.gapEnd] != END_ELEMENT_LONG) {
            throw new Error("unexpected endElement");
        }
        int index = getIntN(this.gapEnd + 1);
        int begin = getIntN(this.gapEnd + 3);
        int parent = getIntN(this.gapEnd + 5);
        this.currentParent = parent;
        this.gapEnd += 7;
        int offset = this.gapStart - begin;
        int parentOffset = begin - parent;
        if (index >= 4095 || offset >= 65536 || parentOffset >= 65536) {
            this.data[begin] = 61704;
            setIntN(begin + 1, offset);
            this.data[this.gapStart] = 61708;
            setIntN(this.gapStart + 1, index);
            setIntN(this.gapStart + 3, -offset);
            if (parent >= this.gapStart || begin <= this.gapStart) {
                parent -= this.gapStart;
            }
            setIntN(this.gapStart + 5, parent);
            this.gapStart += 7;
            return;
        }
        this.data[begin] = (char) (BEGIN_ELEMENT_SHORT | index);
        this.data[begin + 1] = (char) offset;
        this.data[begin + 2] = (char) parentOffset;
        this.data[this.gapStart] = 61707;
        this.data[this.gapStart + 1] = (char) offset;
        this.gapStart += 2;
    }

    public void startAttribute(Object attrType) {
        startAttribute(find(attrType));
    }

    public void startAttribute(int index) {
        ensureSpace(6);
        this.gapEnd--;
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = 61705;
        if (this.attrStart != 0) {
            throw new Error("nested attribute");
        }
        this.attrStart = this.gapStart;
        setIntN(this.gapStart, index);
        setIntN(this.gapStart + 2, this.gapEnd - this.data.length);
        this.gapStart += 4;
        this.data[this.gapEnd] = 61706;
    }

    public void setAttributeName(int attrIndex, int nameIndex) {
        setIntN(attrIndex + 1, nameIndex);
    }

    public void endAttribute() {
        if (this.attrStart > 0) {
            if (this.data[this.gapEnd] != END_ATTRIBUTE) {
                throw new Error("unexpected endAttribute");
            }
            this.gapEnd++;
            setIntN(this.attrStart + 2, (this.gapStart - this.attrStart) + 1);
            this.attrStart = 0;
            char[] cArr = this.data;
            int i = this.gapStart;
            this.gapStart = i + 1;
            cArr[i] = 61706;
        }
    }

    public Consumer append(char c) {
        write((int) c);
        return this;
    }

    public void write(int c) {
        ensureSpace(3);
        if (c <= 40959) {
            char[] cArr = this.data;
            int i = this.gapStart;
            this.gapStart = i + 1;
            cArr[i] = (char) c;
        } else if (c < 65536) {
            char[] cArr2 = this.data;
            int i2 = this.gapStart;
            this.gapStart = i2 + 1;
            cArr2[i2] = 61702;
            char[] cArr3 = this.data;
            int i3 = this.gapStart;
            this.gapStart = i3 + 1;
            cArr3[i3] = (char) c;
        } else {
            Char.print(c, this);
        }
    }

    public void writeBoolean(boolean v) {
        ensureSpace(1);
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = v ? BOOL_TRUE : BOOL_FALSE;
    }

    public void writeByte(int v) {
        ensureSpace(1);
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = (char) (BYTE_PREFIX + (v & 255));
    }

    public void writeInt(int v) {
        ensureSpace(3);
        if (v < MIN_INT_SHORT || v > MAX_INT_SHORT) {
            char[] cArr = this.data;
            int i = this.gapStart;
            this.gapStart = i + 1;
            cArr[i] = 61698;
            setIntN(this.gapStart, v);
            this.gapStart += 2;
            return;
        }
        char[] cArr2 = this.data;
        int i2 = this.gapStart;
        this.gapStart = i2 + 1;
        cArr2[i2] = (char) (INT_SHORT_ZERO + v);
    }

    public void writeLong(long v) {
        ensureSpace(5);
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = 61699;
        char[] cArr2 = this.data;
        int i2 = this.gapStart;
        this.gapStart = i2 + 1;
        cArr2[i2] = (char) ((int) (v >>> 48));
        char[] cArr3 = this.data;
        int i3 = this.gapStart;
        this.gapStart = i3 + 1;
        cArr3[i3] = (char) ((int) (v >>> 32));
        char[] cArr4 = this.data;
        int i4 = this.gapStart;
        this.gapStart = i4 + 1;
        cArr4[i4] = (char) ((int) (v >>> 16));
        char[] cArr5 = this.data;
        int i5 = this.gapStart;
        this.gapStart = i5 + 1;
        cArr5[i5] = (char) ((int) v);
    }

    public void writeFloat(float v) {
        ensureSpace(3);
        int i = Float.floatToIntBits(v);
        char[] cArr = this.data;
        int i2 = this.gapStart;
        this.gapStart = i2 + 1;
        cArr[i2] = 61700;
        char[] cArr2 = this.data;
        int i3 = this.gapStart;
        this.gapStart = i3 + 1;
        cArr2[i3] = (char) (i >>> 16);
        char[] cArr3 = this.data;
        int i4 = this.gapStart;
        this.gapStart = i4 + 1;
        cArr3[i4] = (char) i;
    }

    public void writeDouble(double v) {
        ensureSpace(5);
        long l = Double.doubleToLongBits(v);
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = 61701;
        char[] cArr2 = this.data;
        int i2 = this.gapStart;
        this.gapStart = i2 + 1;
        cArr2[i2] = (char) ((int) (l >>> 48));
        char[] cArr3 = this.data;
        int i3 = this.gapStart;
        this.gapStart = i3 + 1;
        cArr3[i3] = (char) ((int) (l >>> 32));
        char[] cArr4 = this.data;
        int i4 = this.gapStart;
        this.gapStart = i4 + 1;
        cArr4[i4] = (char) ((int) (l >>> 16));
        char[] cArr5 = this.data;
        int i5 = this.gapStart;
        this.gapStart = i5 + 1;
        cArr5[i5] = (char) ((int) l);
    }

    public boolean ignoring() {
        return false;
    }

    public void writeJoiner() {
        ensureSpace(1);
        char[] cArr = this.data;
        int i = this.gapStart;
        this.gapStart = i + 1;
        cArr[i] = 61718;
    }

    public void write(char[] buf, int off, int len) {
        if (len == 0) {
            writeJoiner();
        }
        ensureSpace(len);
        int off2 = off;
        while (len > 0) {
            int off3 = off2 + 1;
            char ch = buf[off2];
            len--;
            if (ch <= 40959) {
                char[] cArr = this.data;
                int i = this.gapStart;
                this.gapStart = i + 1;
                cArr[i] = ch;
            } else {
                write((int) ch);
                ensureSpace(len);
            }
            off2 = off3;
        }
    }

    public void write(String str) {
        write((CharSequence) str, 0, str.length());
    }

    public void write(CharSequence str, int start, int length) {
        if (length == 0) {
            writeJoiner();
        }
        ensureSpace(length);
        int start2 = start;
        while (length > 0) {
            int start3 = start2 + 1;
            char ch = str.charAt(start2);
            length--;
            if (ch <= 40959) {
                char[] cArr = this.data;
                int i = this.gapStart;
                this.gapStart = i + 1;
                cArr[i] = ch;
            } else {
                write((int) ch);
                ensureSpace(length);
            }
            start2 = start3;
        }
    }

    public void writeCDATA(char[] chars, int offset, int length) {
        ensureSpace(length + 3);
        int i = this.gapStart;
        int i2 = i + 1;
        this.data[i] = 61717;
        setIntN(i2, length);
        int i3 = i2 + 2;
        System.arraycopy(chars, offset, this.data, i3, length);
        this.gapStart = i3 + length;
    }

    public Consumer append(CharSequence csq) {
        if (csq == null) {
            csq = "null";
        }
        return append(csq, 0, csq.length());
    }

    public Consumer append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        for (int i = start; i < end; i++) {
            append(csq.charAt(i));
        }
        return this;
    }

    public boolean isEmpty() {
        int pos;
        if (this.gapStart == 0) {
            pos = this.gapEnd;
        } else {
            pos = 0;
        }
        if (pos == this.data.length) {
            return true;
        }
        return false;
    }

    public int size() {
        int size = 0;
        int i = 0;
        while (true) {
            i = nextPos(i);
            if (i == 0) {
                return size;
            }
            size++;
        }
    }

    public int createPos(int index, boolean isAfter) {
        return createRelativePos(0, index, isAfter);
    }

    public final int posToDataIndex(int ipos) {
        if (ipos == -1) {
            return this.data.length;
        }
        int index = ipos >>> 1;
        if ((ipos & 1) != 0) {
            index--;
        }
        if (index >= this.gapStart) {
            index += this.gapEnd - this.gapStart;
        }
        if ((ipos & 1) == 0) {
            return index;
        }
        int index2 = nextDataIndex(index);
        if (index2 < 0) {
            return this.data.length;
        }
        if (index2 == this.gapStart) {
            return index2 + (this.gapEnd - this.gapStart);
        }
        return index2;
    }

    public int firstChildPos(int ipos) {
        int index = gotoChildrenStart(posToDataIndex(ipos));
        if (index < 0) {
            return 0;
        }
        return index << 1;
    }

    public final int gotoChildrenStart(int index) {
        int index2;
        int index3;
        if (index == this.data.length) {
            return -1;
        }
        char datum = this.data[index];
        if ((datum >= BEGIN_ELEMENT_SHORT && datum <= 45055) || datum == BEGIN_ELEMENT_LONG) {
            index2 = index + 3;
        } else if (datum != BEGIN_DOCUMENT && datum != 61714) {
            return -1;
        } else {
            index2 = index + 5;
        }
        while (true) {
            if (index2 >= this.gapStart) {
                index2 += this.gapEnd - this.gapStart;
            }
            char datum2 = this.data[index2];
            if (datum2 == BEGIN_ATTRIBUTE_LONG) {
                int end = getIntN(index2 + 3);
                if (end < 0) {
                    index2 = this.data.length;
                }
                index3 = index2 + end;
            } else if (datum2 == END_ATTRIBUTE || datum2 == JOINER) {
                index3 = index2 + 1;
            } else if (datum2 != DOCUMENT_URI) {
                return index2;
            } else {
                index3 = index2 + 3;
            }
        }
    }

    public int parentPos(int ipos) {
        int index = posToDataIndex(ipos);
        do {
            index = parentOrEntityI(index);
            if (index == -1) {
                return -1;
            }
        } while (this.data[index] == 61714);
        return index << 1;
    }

    public int parentOrEntityPos(int ipos) {
        int index = parentOrEntityI(posToDataIndex(ipos));
        if (index < 0) {
            return -1;
        }
        return index << 1;
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 150 */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x006f, code lost:
        if (r8 < 0) goto L_0x0006;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int parentOrEntityI(int r8) {
        /*
            r7 = this;
            r4 = -1
            char[] r5 = r7.data
            int r5 = r5.length
            if (r8 != r5) goto L_0x0007
        L_0x0006:
            return r4
        L_0x0007:
            char[] r5 = r7.data
            char r1 = r5[r8]
            r5 = 61712(0xf110, float:8.6477E-41)
            if (r1 == r5) goto L_0x0015
            r5 = 61714(0xf112, float:8.648E-41)
            if (r1 != r5) goto L_0x0022
        L_0x0015:
            int r5 = r8 + 3
            int r3 = r7.getIntN(r5)
            if (r3 < r4) goto L_0x001f
            r4 = r3
            goto L_0x0006
        L_0x001f:
            int r4 = r8 + r3
            goto L_0x0006
        L_0x0022:
            r5 = 40960(0xa000, float:5.7397E-41)
            if (r1 < r5) goto L_0x0037
            r5 = 45055(0xafff, float:6.3136E-41)
            if (r1 > r5) goto L_0x0037
            char[] r5 = r7.data
            int r6 = r8 + 2
            char r3 = r5[r6]
            if (r3 == 0) goto L_0x0006
            int r4 = r8 - r3
            goto L_0x0006
        L_0x0037:
            r5 = 61704(0xf108, float:8.6466E-41)
            if (r1 != r5) goto L_0x0059
            int r5 = r8 + 1
            int r2 = r7.getIntN(r5)
            if (r2 >= 0) goto L_0x0055
            char[] r5 = r7.data
            int r5 = r5.length
        L_0x0047:
            int r2 = r2 + r5
            int r5 = r2 + 5
            int r3 = r7.getIntN(r5)
            if (r3 == 0) goto L_0x0006
            if (r3 >= 0) goto L_0x0053
            int r3 = r3 + r2
        L_0x0053:
            r4 = r3
            goto L_0x0006
        L_0x0055:
            r5 = r8
            goto L_0x0047
        L_0x0057:
            int r8 = r8 + 1
        L_0x0059:
            int r5 = r7.gapStart
            if (r8 != r5) goto L_0x005f
            int r8 = r7.gapEnd
        L_0x005f:
            char[] r5 = r7.data
            int r5 = r5.length
            if (r8 == r5) goto L_0x0006
            char[] r5 = r7.data
            char r1 = r5[r8]
            switch(r1) {
                case 61706: goto L_0x0057;
                case 61707: goto L_0x0072;
                case 61708: goto L_0x007b;
                case 61709: goto L_0x006b;
                case 61710: goto L_0x006b;
                case 61711: goto L_0x006b;
                case 61712: goto L_0x006b;
                case 61713: goto L_0x0006;
                default: goto L_0x006b;
            }
        L_0x006b:
            int r8 = r7.nextDataIndex(r8)
            if (r8 >= 0) goto L_0x0059
            goto L_0x0006
        L_0x0072:
            char[] r4 = r7.data
            int r5 = r8 + 1
            char r4 = r4[r5]
            int r4 = r8 - r4
            goto L_0x0006
        L_0x007b:
            int r4 = r8 + 3
            int r0 = r7.getIntN(r4)
            if (r0 >= 0) goto L_0x0084
            int r0 = r0 + r8
        L_0x0084:
            r4 = r0
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.lists.TreeList.parentOrEntityI(int):int");
    }

    public int getAttributeCount(int parent) {
        int n = 0;
        int attr = firstAttributePos(parent);
        while (attr != 0 && getNextKind(attr) == 35) {
            n++;
            attr = nextPos(attr);
        }
        return n;
    }

    public boolean gotoAttributesStart(TreePosition pos) {
        int index = gotoAttributesStart(pos.ipos >> 1);
        if (index < 0) {
            return false;
        }
        pos.push(this, index << 1);
        return true;
    }

    public int firstAttributePos(int ipos) {
        int index = gotoAttributesStart(posToDataIndex(ipos));
        if (index < 0) {
            return 0;
        }
        return index << 1;
    }

    public int gotoAttributesStart(int index) {
        if (index >= this.gapStart) {
            index += this.gapEnd - this.gapStart;
        }
        if (index == this.data.length) {
            return -1;
        }
        char datum = this.data[index];
        if ((datum < BEGIN_ELEMENT_SHORT || datum > 45055) && datum != BEGIN_ELEMENT_LONG) {
            return -1;
        }
        return index + 3;
    }

    public Object get(int index) {
        int i = 0;
        do {
            index--;
            if (index < 0) {
                return getPosNext(i);
            }
            i = nextPos(i);
        } while (i != 0);
        throw new IndexOutOfBoundsException();
    }

    public boolean consumeNext(int ipos, Consumer out) {
        if (!hasNext(ipos)) {
            return false;
        }
        int start = posToDataIndex(ipos);
        int end = nextNodeIndex(start, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        if (end == start) {
            end = nextDataIndex(start);
        }
        if (end >= 0) {
            consumeIRange(start, end, out);
        }
        return true;
    }

    public void consumePosRange(int startPos, int endPos, Consumer out) {
        consumeIRange(posToDataIndex(startPos), posToDataIndex(endPos), out);
    }

    public int consumeIRange(int startPosition, int endPosition, Consumer out) {
        int limit;
        int pos = startPosition;
        if (startPosition > this.gapStart || endPosition <= this.gapStart) {
            limit = endPosition;
        } else {
            limit = this.gapStart;
        }
        while (true) {
            if (pos >= limit) {
                if (pos != this.gapStart || endPosition <= this.gapEnd) {
                    return pos;
                }
                pos = this.gapEnd;
                limit = endPosition;
            }
            int pos2 = pos + 1;
            char datum = this.data[pos];
            if (datum <= 40959) {
                int start = pos2 - 1;
                int lim = limit;
                while (true) {
                    if (pos2 >= lim) {
                        pos = pos2;
                    } else {
                        int pos3 = pos2 + 1;
                        if (this.data[pos2] > 40959) {
                            pos = pos3 - 1;
                        } else {
                            pos2 = pos3;
                        }
                    }
                }
                out.write(this.data, start, pos - start);
            } else if (datum >= OBJECT_REF_SHORT && datum <= 61439) {
                out.writeObject(this.objects[datum - OBJECT_REF_SHORT]);
                pos = pos2;
            } else if (datum >= BEGIN_ELEMENT_SHORT && datum <= 45055) {
                out.startElement(this.objects[datum - BEGIN_ELEMENT_SHORT]);
                pos = pos2 + 2;
            } else if (datum < 45056 || datum > 57343) {
                switch (datum) {
                    case 61696:
                    case 61697:
                        out.writeBoolean(datum != 61696);
                        pos = pos2;
                        break;
                    case INT_FOLLOWS /*61698*/:
                        out.writeInt(getIntN(pos2));
                        pos = pos2 + 2;
                        break;
                    case LONG_FOLLOWS /*61699*/:
                        out.writeLong(getLongN(pos2));
                        pos = pos2 + 4;
                        break;
                    case FLOAT_FOLLOWS /*61700*/:
                        out.writeFloat(Float.intBitsToFloat(getIntN(pos2)));
                        pos = pos2 + 2;
                        break;
                    case DOUBLE_FOLLOWS /*61701*/:
                        out.writeDouble(Double.longBitsToDouble(getLongN(pos2)));
                        pos = pos2 + 4;
                        break;
                    case CHAR_FOLLOWS /*61702*/:
                        out.write(this.data, pos2, (datum + 1) - CHAR_FOLLOWS);
                        pos = pos2 + 1;
                        break;
                    case BEGIN_ELEMENT_LONG /*61704*/:
                        int index = getIntN(pos2);
                        pos = pos2 + 2;
                        out.startElement(this.objects[getIntN(index + (index >= 0 ? pos2 - 1 : this.data.length) + 1)]);
                        break;
                    case BEGIN_ATTRIBUTE_LONG /*61705*/:
                        out.startAttribute(this.objects[getIntN(pos2)]);
                        pos = pos2 + 4;
                        break;
                    case END_ATTRIBUTE /*61706*/:
                        out.endAttribute();
                        pos = pos2;
                        break;
                    case END_ELEMENT_SHORT /*61707*/:
                        pos = pos2 + 1;
                        out.endElement();
                        break;
                    case END_ELEMENT_LONG /*61708*/:
                        int intN = getIntN(pos2);
                        out.endElement();
                        pos = pos2 + 6;
                        break;
                    case 61710:
                        if (out instanceof PositionConsumer) {
                            ((PositionConsumer) out).consume((SeqPosition) this.objects[getIntN(pos2)]);
                            pos = pos2 + 2;
                            break;
                        }
                    case 61709:
                        out.writeObject(this.objects[getIntN(pos2)]);
                        pos = pos2 + 2;
                        break;
                    case 61711:
                        AbstractSequence seq = (AbstractSequence) this.objects[getIntN(pos2)];
                        int ipos = getIntN(pos2 + 2);
                        if (out instanceof PositionConsumer) {
                            ((PositionConsumer) out).writePosition(seq, ipos);
                        } else {
                            out.writeObject(seq.getIteratorAtPos(ipos));
                        }
                        pos = pos2 + 4;
                        break;
                    case BEGIN_DOCUMENT /*61712*/:
                        out.startDocument();
                        pos = pos2 + 4;
                        break;
                    case END_DOCUMENT /*61713*/:
                        out.endDocument();
                        pos = pos2;
                        break;
                    case BEGIN_ENTITY /*61714*/:
                        if (out instanceof TreeList) {
                            ((TreeList) out).beginEntity(this.objects[getIntN(pos2)]);
                        }
                        pos = pos2 + 4;
                        break;
                    case END_ENTITY /*61715*/:
                        if (!(out instanceof TreeList)) {
                            pos = pos2;
                            break;
                        } else {
                            ((TreeList) out).endEntity();
                            pos = pos2;
                            break;
                        }
                    case PROCESSING_INSTRUCTION /*61716*/:
                        String target = (String) this.objects[getIntN(pos2)];
                        int length = getIntN(pos2 + 2);
                        int pos4 = pos2 + 4;
                        if (out instanceof XConsumer) {
                            ((XConsumer) out).writeProcessingInstruction(target, this.data, pos4, length);
                        }
                        pos = pos4 + length;
                        break;
                    case CDATA_SECTION /*61717*/:
                        int length2 = getIntN(pos2);
                        int pos5 = pos2 + 2;
                        if (out instanceof XConsumer) {
                            ((XConsumer) out).writeCDATA(this.data, pos5, length2);
                        } else {
                            out.write(this.data, pos5, length2);
                        }
                        pos = pos5 + length2;
                        break;
                    case JOINER /*61718*/:
                        out.write("");
                        pos = pos2;
                        break;
                    case COMMENT /*61719*/:
                        int length3 = getIntN(pos2);
                        int pos6 = pos2 + 2;
                        if (out instanceof XConsumer) {
                            ((XConsumer) out).writeComment(this.data, pos6, length3);
                        }
                        pos = pos6 + length3;
                        break;
                    case DOCUMENT_URI /*61720*/:
                        if (out instanceof TreeList) {
                            ((TreeList) out).writeDocumentUri(this.objects[getIntN(pos2)]);
                        }
                        pos = pos2 + 2;
                        break;
                    default:
                        throw new Error("unknown code:" + datum);
                }
            } else {
                out.writeInt(datum - INT_SHORT_ZERO);
                pos = pos2;
            }
        }
        return pos;
    }

    public void toString(String sep, StringBuffer sbuf) {
        int index;
        int pos;
        int pos2 = 0;
        int limit = this.gapStart;
        boolean seen = false;
        boolean inStartTag = false;
        while (true) {
            if (pos2 < limit || (pos2 == this.gapStart && (pos2 = this.gapEnd) != (limit = this.data.length))) {
                int pos3 = pos2 + 1;
                char datum = this.data[pos2];
                if (datum <= 40959) {
                    int start = pos3 - 1;
                    int lim = limit;
                    while (true) {
                        if (pos3 >= lim) {
                            pos2 = pos3;
                        } else {
                            int pos4 = pos3 + 1;
                            if (this.data[pos3] > 40959) {
                                pos2 = pos4 - 1;
                            } else {
                                pos3 = pos4;
                            }
                        }
                    }
                    if (inStartTag) {
                        sbuf.append('>');
                        inStartTag = false;
                    }
                    sbuf.append(this.data, start, pos2 - start);
                    seen = false;
                } else if (datum >= OBJECT_REF_SHORT && datum <= 61439) {
                    if (inStartTag) {
                        sbuf.append('>');
                        inStartTag = false;
                    }
                    if (seen) {
                        sbuf.append(sep);
                    } else {
                        seen = true;
                    }
                    sbuf.append(this.objects[datum - OBJECT_REF_SHORT]);
                    pos2 = pos3;
                } else if (datum >= BEGIN_ELEMENT_SHORT && datum <= 45055) {
                    if (inStartTag) {
                        sbuf.append('>');
                    }
                    int index2 = datum - BEGIN_ELEMENT_SHORT;
                    if (seen) {
                        sbuf.append(sep);
                    }
                    sbuf.append('<');
                    sbuf.append(this.objects[index2].toString());
                    pos2 = pos3 + 2;
                    seen = false;
                    inStartTag = true;
                } else if (datum < 45056 || datum > 57343) {
                    switch (datum) {
                        case 61696:
                        case 61697:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            if (seen) {
                                sbuf.append(sep);
                            } else {
                                seen = true;
                            }
                            sbuf.append(datum != 61696);
                            pos2 = pos3;
                            break;
                        case INT_FOLLOWS /*61698*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            if (seen) {
                                sbuf.append(sep);
                            } else {
                                seen = true;
                            }
                            sbuf.append(getIntN(pos3));
                            pos2 = pos3 + 2;
                            break;
                        case LONG_FOLLOWS /*61699*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            if (seen) {
                                sbuf.append(sep);
                            } else {
                                seen = true;
                            }
                            sbuf.append(getLongN(pos3));
                            pos2 = pos3 + 4;
                            break;
                        case FLOAT_FOLLOWS /*61700*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            if (seen) {
                                sbuf.append(sep);
                            } else {
                                seen = true;
                            }
                            sbuf.append(Float.intBitsToFloat(getIntN(pos3)));
                            pos2 = pos3 + 2;
                            break;
                        case DOUBLE_FOLLOWS /*61701*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            if (seen) {
                                sbuf.append(sep);
                            } else {
                                seen = true;
                            }
                            sbuf.append(Double.longBitsToDouble(getLongN(pos3)));
                            pos2 = pos3 + 4;
                            break;
                        case CHAR_FOLLOWS /*61702*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            sbuf.append(this.data, pos3, (datum + 1) - CHAR_FOLLOWS);
                            seen = false;
                            pos2 = pos3 + 1;
                            break;
                        case BEGIN_ELEMENT_LONG /*61704*/:
                            int index3 = getIntN(pos3);
                            pos2 = pos3 + 2;
                            int index4 = getIntN(index3 + (index3 >= 0 ? pos3 - 1 : this.data.length) + 1);
                            if (inStartTag) {
                                sbuf.append('>');
                            } else if (seen) {
                                sbuf.append(sep);
                            }
                            sbuf.append('<');
                            sbuf.append(this.objects[index4]);
                            seen = false;
                            inStartTag = true;
                            break;
                        case BEGIN_ATTRIBUTE_LONG /*61705*/:
                            int index5 = getIntN(pos3);
                            sbuf.append(' ');
                            sbuf.append(this.objects[index5]);
                            sbuf.append("=\"");
                            inStartTag = false;
                            pos2 = pos3 + 4;
                            break;
                        case END_ATTRIBUTE /*61706*/:
                            sbuf.append('\"');
                            inStartTag = true;
                            seen = false;
                            pos2 = pos3;
                            break;
                        case END_ELEMENT_SHORT /*61707*/:
                        case END_ELEMENT_LONG /*61708*/:
                            if (datum == END_ELEMENT_SHORT) {
                                pos = pos3 + 1;
                                index = this.data[(pos - 2) - this.data[pos3]] - BEGIN_ELEMENT_SHORT;
                            } else {
                                index = getIntN(pos3);
                                pos = pos3 + 6;
                            }
                            if (inStartTag) {
                                sbuf.append("/>");
                            } else {
                                sbuf.append("</");
                                sbuf.append(this.objects[index]);
                                sbuf.append('>');
                            }
                            inStartTag = false;
                            seen = true;
                            break;
                        case 61709:
                        case 61710:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            if (seen) {
                                sbuf.append(sep);
                            } else {
                                seen = true;
                            }
                            sbuf.append(this.objects[getIntN(pos3)]);
                            pos2 = pos3 + 2;
                            break;
                        case 61711:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            if (seen) {
                                sbuf.append(sep);
                            } else {
                                seen = true;
                            }
                            sbuf.append(((AbstractSequence) this.objects[getIntN(pos3)]).getIteratorAtPos(getIntN(pos3 + 2)));
                            pos2 = pos3 + 4;
                            break;
                        case BEGIN_DOCUMENT /*61712*/:
                        case BEGIN_ENTITY /*61714*/:
                            pos2 = pos3 + 4;
                            break;
                        case END_DOCUMENT /*61713*/:
                        case END_ENTITY /*61715*/:
                            pos2 = pos3;
                            break;
                        case PROCESSING_INSTRUCTION /*61716*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            sbuf.append("<?");
                            int pos5 = pos3 + 2;
                            sbuf.append(this.objects[getIntN(pos3)]);
                            int index6 = getIntN(pos5);
                            int pos6 = pos5 + 2;
                            if (index6 > 0) {
                                sbuf.append(' ');
                                sbuf.append(this.data, pos6, index6);
                                pos6 += index6;
                            }
                            sbuf.append("?>");
                            break;
                        case CDATA_SECTION /*61717*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            int index7 = getIntN(pos3);
                            int pos7 = pos3 + 2;
                            sbuf.append("<![CDATA[");
                            sbuf.append(this.data, pos7, index7);
                            sbuf.append("]]>");
                            pos2 = pos7 + index7;
                            break;
                        case JOINER /*61718*/:
                            pos2 = pos3;
                            break;
                        case COMMENT /*61719*/:
                            if (inStartTag) {
                                sbuf.append('>');
                                inStartTag = false;
                            }
                            int index8 = getIntN(pos3);
                            int pos8 = pos3 + 2;
                            sbuf.append("<!--");
                            sbuf.append(this.data, pos8, index8);
                            sbuf.append("-->");
                            pos2 = pos8 + index8;
                            break;
                        case DOCUMENT_URI /*61720*/:
                            pos2 = pos3 + 2;
                            break;
                        default:
                            throw new Error("unknown code:" + datum);
                    }
                } else {
                    if (inStartTag) {
                        sbuf.append('>');
                        inStartTag = false;
                    }
                    if (seen) {
                        sbuf.append(sep);
                    } else {
                        seen = true;
                    }
                    sbuf.append(datum - INT_SHORT_ZERO);
                    pos2 = pos3;
                }
            } else {
                return;
            }
        }
    }

    public boolean hasNext(int ipos) {
        char ch;
        int index = posToDataIndex(ipos);
        if (index == this.data.length || (ch = this.data[index]) == END_ATTRIBUTE || ch == END_ELEMENT_SHORT || ch == END_ELEMENT_LONG || ch == END_DOCUMENT) {
            return false;
        }
        return true;
    }

    public int getNextKind(int ipos) {
        return getNextKindI(posToDataIndex(ipos));
    }

    public int getNextKindI(int index) {
        if (index == this.data.length) {
            return 0;
        }
        char datum = this.data[index];
        if (datum <= 40959) {
            return 29;
        }
        if (datum >= OBJECT_REF_SHORT && datum <= 61439) {
            return 32;
        }
        if (datum >= BEGIN_ELEMENT_SHORT && datum <= 45055) {
            return 33;
        }
        if ((65280 & datum) == BYTE_PREFIX) {
            return 28;
        }
        if (datum >= 45056 && datum <= 57343) {
            return 22;
        }
        switch (datum) {
            case 61696:
            case 61697:
                return 27;
            case INT_FOLLOWS /*61698*/:
                return 22;
            case LONG_FOLLOWS /*61699*/:
                return 24;
            case FLOAT_FOLLOWS /*61700*/:
                return 25;
            case DOUBLE_FOLLOWS /*61701*/:
                return 26;
            case CHAR_FOLLOWS /*61702*/:
            case BEGIN_DOCUMENT /*61712*/:
                return 34;
            case BEGIN_ELEMENT_LONG /*61704*/:
                return 33;
            case BEGIN_ATTRIBUTE_LONG /*61705*/:
                return 35;
            case END_ATTRIBUTE /*61706*/:
            case END_ELEMENT_SHORT /*61707*/:
            case END_ELEMENT_LONG /*61708*/:
            case END_DOCUMENT /*61713*/:
            case END_ENTITY /*61715*/:
                return 0;
            case BEGIN_ENTITY /*61714*/:
                return getNextKind((index + 5) << 1);
            case PROCESSING_INSTRUCTION /*61716*/:
                return 37;
            case CDATA_SECTION /*61717*/:
                return 31;
            case COMMENT /*61719*/:
                return 36;
            default:
                return 32;
        }
    }

    public Object getNextTypeObject(int ipos) {
        int index;
        int index2 = posToDataIndex(ipos);
        while (index2 != this.data.length) {
            char datum = this.data[index2];
            if (datum != 61714) {
                if (datum >= BEGIN_ELEMENT_SHORT && datum <= 45055) {
                    index = datum - BEGIN_ELEMENT_SHORT;
                } else if (datum == BEGIN_ELEMENT_LONG) {
                    int j = getIntN(index2 + 1);
                    if (j < 0) {
                        index2 = this.data.length;
                    }
                    index = getIntN(j + index2 + 1);
                } else if (datum == BEGIN_ATTRIBUTE_LONG) {
                    index = getIntN(index2 + 1);
                } else if (datum != PROCESSING_INSTRUCTION) {
                    return null;
                } else {
                    index = getIntN(index2 + 1);
                }
                if (index >= 0) {
                    return this.objects[index];
                }
                return null;
            }
            index2 += 5;
        }
        return null;
    }

    public String getNextTypeName(int ipos) {
        Object type = getNextTypeObject(ipos);
        if (type == null) {
            return null;
        }
        return type.toString();
    }

    public Object getPosPrevious(int ipos) {
        if ((ipos & 1) == 0 || ipos == -1) {
            return super.getPosPrevious(ipos);
        }
        return getPosNext(ipos - 3);
    }

    private Object copyToList(int startPosition, int endPosition) {
        return new TreeList(this, startPosition, endPosition);
    }

    public int getPosNextInt(int ipos) {
        int index = posToDataIndex(ipos);
        if (index < this.data.length) {
            char datum = this.data[index];
            if (datum >= 45056 && datum <= 57343) {
                return datum - INT_SHORT_ZERO;
            }
            if (datum == 61698) {
                return getIntN(index + 1);
            }
        }
        return ((Number) getPosNext(ipos)).intValue();
    }

    public Object getPosNext(int ipos) {
        int i;
        int i2;
        int index = posToDataIndex(ipos);
        if (index == this.data.length) {
            return Sequence.eofValue;
        }
        char datum = this.data[index];
        if (datum <= 40959) {
            return Convert.toObject(datum);
        }
        if (datum >= OBJECT_REF_SHORT && datum <= 61439) {
            return this.objects[datum - OBJECT_REF_SHORT];
        }
        if (datum >= BEGIN_ELEMENT_SHORT && datum <= 45055) {
            return copyToList(index, this.data[index + 1] + index + 2);
        }
        if (datum >= 45056 && datum <= 57343) {
            return Convert.toObject(datum - INT_SHORT_ZERO);
        }
        switch (datum) {
            case 61696:
            case 61697:
                return Convert.toObject(datum != 61696);
            case INT_FOLLOWS /*61698*/:
                return Convert.toObject(getIntN(index + 1));
            case LONG_FOLLOWS /*61699*/:
                return Convert.toObject(getLongN(index + 1));
            case FLOAT_FOLLOWS /*61700*/:
                return Convert.toObject(Float.intBitsToFloat(getIntN(index + 1)));
            case DOUBLE_FOLLOWS /*61701*/:
                return Convert.toObject(Double.longBitsToDouble(getLongN(index + 1)));
            case CHAR_FOLLOWS /*61702*/:
                return Convert.toObject(this.data[index + 1]);
            case BEGIN_ELEMENT_LONG /*61704*/:
                int end_offset = getIntN(index + 1);
                if (end_offset < 0) {
                    i = this.data.length;
                } else {
                    i = index;
                }
                return copyToList(index, end_offset + i + 7);
            case BEGIN_ATTRIBUTE_LONG /*61705*/:
                int end_offset2 = getIntN(index + 3);
                if (end_offset2 < 0) {
                    i2 = this.data.length;
                } else {
                    i2 = index;
                }
                return copyToList(index, end_offset2 + i2 + 1);
            case END_ATTRIBUTE /*61706*/:
            case END_ELEMENT_SHORT /*61707*/:
            case END_ELEMENT_LONG /*61708*/:
            case END_DOCUMENT /*61713*/:
                return Sequence.eofValue;
            case 61709:
            case 61710:
                return this.objects[getIntN(index + 1)];
            case 61711:
                return ((AbstractSequence) this.objects[getIntN(index + 1)]).getIteratorAtPos(getIntN(index + 3));
            case BEGIN_DOCUMENT /*61712*/:
                int end_offset3 = getIntN(index + 1);
                return copyToList(index, end_offset3 + (end_offset3 < 0 ? this.data.length : index) + 1);
            case JOINER /*61718*/:
                return "";
            default:
                throw unsupported("getPosNext, code=" + Integer.toHexString(datum));
        }
    }

    public void stringValue(int startIndex, int endIndex, StringBuffer sbuf) {
        int index = startIndex;
        while (index < endIndex && index >= 0) {
            index = stringValue(false, index, sbuf);
        }
    }

    public int stringValue(int index, StringBuffer sbuf) {
        int next = nextNodeIndex(index, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        if (next <= index) {
            return stringValue(false, index, sbuf);
        }
        stringValue(index, next, sbuf);
        return index;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00b7, code lost:
        r5 = getIntN(r14);
        r14 = r14 + 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00bd, code lost:
        if (r13 == false) goto L_0x00c4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00c2, code lost:
        if (r0 != CDATA_SECTION) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00c4, code lost:
        r15.append(r12.data, r14, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
        return r14 + r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int stringValue(boolean r13, int r14, java.lang.StringBuffer r15) {
        /*
            r12 = this;
            r8 = 0
            r1 = 0
            int r9 = r12.gapStart
            if (r14 < r9) goto L_0x000c
            int r9 = r12.gapEnd
            int r10 = r12.gapStart
            int r9 = r9 - r10
            int r14 = r14 + r9
        L_0x000c:
            char[] r9 = r12.data
            int r9 = r9.length
            if (r14 != r9) goto L_0x0013
            r9 = -1
        L_0x0012:
            return r9
        L_0x0013:
            char[] r9 = r12.data
            char r0 = r9[r14]
            int r14 = r14 + 1
            r7 = 0
            r9 = 40959(0x9fff, float:5.7396E-41)
            if (r0 > r9) goto L_0x0024
            r15.append(r0)
            r9 = r14
            goto L_0x0012
        L_0x0024:
            r9 = 57344(0xe000, float:8.0356E-41)
            if (r0 < r9) goto L_0x004f
            r9 = 61439(0xefff, float:8.6094E-41)
            if (r0 > r9) goto L_0x004f
            if (r7 == 0) goto L_0x0035
            r9 = 32
            r15.append(r9)
        L_0x0035:
            java.lang.Object[] r9 = r12.objects
            r10 = 57344(0xe000, float:8.0356E-41)
            int r10 = r0 - r10
            r8 = r9[r10]
            r7 = 0
        L_0x003f:
            if (r8 == 0) goto L_0x0044
            r15.append(r8)
        L_0x0044:
            if (r1 <= 0) goto L_0x004d
        L_0x0046:
            r9 = 1
            int r1 = r12.stringValue((boolean) r9, (int) r1, (java.lang.StringBuffer) r15)
            if (r1 >= 0) goto L_0x0046
        L_0x004d:
            r9 = r14
            goto L_0x0012
        L_0x004f:
            r9 = 40960(0xa000, float:5.7397E-41)
            if (r0 < r9) goto L_0x0063
            r9 = 45055(0xafff, float:6.3136E-41)
            if (r0 > r9) goto L_0x0063
            int r1 = r14 + 2
            char[] r9 = r12.data
            char r9 = r9[r14]
            int r9 = r9 + r14
            int r14 = r9 + 1
            goto L_0x003f
        L_0x0063:
            r9 = 65280(0xff00, float:9.1477E-41)
            r9 = r9 & r0
            r10 = 61440(0xf000, float:8.6096E-41)
            if (r9 != r10) goto L_0x0073
            r9 = r0 & 255(0xff, float:3.57E-43)
            r15.append(r9)
            r9 = r14
            goto L_0x0012
        L_0x0073:
            r9 = 45056(0xb000, float:6.3137E-41)
            if (r0 < r9) goto L_0x0087
            r9 = 57343(0xdfff, float:8.0355E-41)
            if (r0 > r9) goto L_0x0087
            r9 = 49152(0xc000, float:6.8877E-41)
            int r9 = r0 - r9
            r15.append(r9)
            r9 = r14
            goto L_0x0012
        L_0x0087:
            switch(r0) {
                case 61696: goto L_0x00cd;
                case 61697: goto L_0x00cd;
                case 61698: goto L_0x00e3;
                case 61699: goto L_0x00f6;
                case 61700: goto L_0x0109;
                case 61701: goto L_0x0120;
                case 61702: goto L_0x0137;
                case 61703: goto L_0x008a;
                case 61704: goto L_0x014d;
                case 61705: goto L_0x0167;
                case 61706: goto L_0x0164;
                case 61707: goto L_0x0164;
                case 61708: goto L_0x0164;
                case 61709: goto L_0x008a;
                case 61710: goto L_0x008a;
                case 61711: goto L_0x017b;
                case 61712: goto L_0x0143;
                case 61713: goto L_0x0164;
                case 61714: goto L_0x0143;
                case 61715: goto L_0x0164;
                case 61716: goto L_0x00b5;
                case 61717: goto L_0x00b7;
                case 61718: goto L_0x0161;
                case 61719: goto L_0x00b7;
                case 61720: goto L_0x00b1;
                default: goto L_0x008a;
            }
        L_0x008a:
            java.lang.Error r9 = new java.lang.Error
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "unimplemented: "
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = java.lang.Integer.toHexString(r0)
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r11 = " at:"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r14)
            java.lang.String r10 = r10.toString()
            r9.<init>(r10)
            throw r9
        L_0x00b1:
            int r9 = r14 + 2
            goto L_0x0012
        L_0x00b5:
            int r14 = r14 + 2
        L_0x00b7:
            int r5 = r12.getIntN(r14)
            int r14 = r14 + 2
            if (r13 == 0) goto L_0x00c4
            r9 = 61717(0xf115, float:8.6484E-41)
            if (r0 != r9) goto L_0x00c9
        L_0x00c4:
            char[] r9 = r12.data
            r15.append(r9, r14, r5)
        L_0x00c9:
            int r9 = r14 + r5
            goto L_0x0012
        L_0x00cd:
            if (r7 == 0) goto L_0x00d4
            r9 = 32
            r15.append(r9)
        L_0x00d4:
            r9 = 61696(0xf100, float:8.6455E-41)
            if (r0 == r9) goto L_0x00e1
            r9 = 1
        L_0x00da:
            r15.append(r9)
            r7 = 1
            r9 = r14
            goto L_0x0012
        L_0x00e1:
            r9 = 0
            goto L_0x00da
        L_0x00e3:
            if (r7 == 0) goto L_0x00ea
            r9 = 32
            r15.append(r9)
        L_0x00ea:
            int r9 = r12.getIntN(r14)
            r15.append(r9)
            r7 = 1
            int r9 = r14 + 2
            goto L_0x0012
        L_0x00f6:
            if (r7 == 0) goto L_0x00fd
            r9 = 32
            r15.append(r9)
        L_0x00fd:
            long r10 = r12.getLongN(r14)
            r15.append(r10)
            r7 = 1
            int r9 = r14 + 4
            goto L_0x0012
        L_0x0109:
            if (r7 == 0) goto L_0x0110
            r9 = 32
            r15.append(r9)
        L_0x0110:
            int r9 = r12.getIntN(r14)
            float r9 = java.lang.Float.intBitsToFloat(r9)
            r15.append(r9)
            r7 = 1
            int r9 = r14 + 2
            goto L_0x0012
        L_0x0120:
            if (r7 == 0) goto L_0x0127
            r9 = 32
            r15.append(r9)
        L_0x0127:
            long r10 = r12.getLongN(r14)
            double r10 = java.lang.Double.longBitsToDouble(r10)
            r15.append(r10)
            r7 = 1
            int r9 = r14 + 4
            goto L_0x0012
        L_0x0137:
            r7 = 0
            char[] r9 = r12.data
            char r9 = r9[r14]
            r15.append(r9)
            int r9 = r14 + 1
            goto L_0x0012
        L_0x0143:
            int r1 = r14 + 4
            int r9 = r14 + -1
            int r14 = r12.nextDataIndex(r9)
            goto L_0x003f
        L_0x014d:
            r7 = 0
            int r1 = r14 + 2
            int r4 = r12.getIntN(r14)
            if (r4 >= 0) goto L_0x015e
            char[] r9 = r12.data
            int r9 = r9.length
        L_0x0159:
            int r4 = r4 + r9
            int r14 = r4 + 7
            goto L_0x003f
        L_0x015e:
            int r9 = r14 + -1
            goto L_0x0159
        L_0x0161:
            r7 = 0
            goto L_0x003f
        L_0x0164:
            r9 = -1
            goto L_0x0012
        L_0x0167:
            if (r13 != 0) goto L_0x016b
            int r1 = r14 + 4
        L_0x016b:
            int r9 = r14 + 2
            int r2 = r12.getIntN(r9)
            if (r2 >= 0) goto L_0x0178
            char[] r9 = r12.data
            int r9 = r9.length
            int r14 = r9 + 1
        L_0x0178:
            int r14 = r14 + r2
            goto L_0x003f
        L_0x017b:
            java.lang.Object[] r9 = r12.objects
            int r10 = r12.getIntN(r14)
            r6 = r9[r10]
            gnu.lists.AbstractSequence r6 = (gnu.lists.AbstractSequence) r6
            int r9 = r14 + 2
            int r3 = r12.getIntN(r9)
            gnu.lists.TreeList r6 = (gnu.lists.TreeList) r6
            int r9 = r3 >> 1
            r6.stringValue((boolean) r13, (int) r9, (java.lang.StringBuffer) r15)
            int r14 = r14 + 4
            goto L_0x003f
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.lists.TreeList.stringValue(boolean, int, java.lang.StringBuffer):int");
    }

    public int createRelativePos(int istart, int offset, boolean isAfter) {
        if (isAfter) {
            if (offset == 0) {
                if ((istart & 1) != 0) {
                    return istart;
                }
                if (istart == 0) {
                    return 1;
                }
            }
            offset--;
        }
        if (offset < 0) {
            throw unsupported("backwards createRelativePos");
        }
        int pos = posToDataIndex(istart);
        do {
            offset--;
            if (offset >= 0) {
                pos = nextDataIndex(pos);
            } else {
                if (pos >= this.gapEnd) {
                    pos -= this.gapEnd - this.gapStart;
                }
                return isAfter ? ((pos + 1) << 1) | 1 : pos << 1;
            }
        } while (pos >= 0);
        throw new IndexOutOfBoundsException();
    }

    public final int nextNodeIndex(int pos, int limit) {
        if ((Integer.MIN_VALUE | limit) == -1) {
            limit = this.data.length;
        }
        while (true) {
            if (pos == this.gapStart) {
                pos = this.gapEnd;
            }
            if (pos < limit) {
                char datum = this.data[pos];
                if (datum <= 40959 || ((datum >= OBJECT_REF_SHORT && datum <= 61439) || ((datum >= 45056 && datum <= 57343) || (65280 & datum) == BYTE_PREFIX))) {
                    pos++;
                } else if (datum < BEGIN_ELEMENT_SHORT || datum > 45055) {
                    switch (datum) {
                        case BEGIN_ELEMENT_LONG /*61704*/:
                        case BEGIN_ATTRIBUTE_LONG /*61705*/:
                        case END_ATTRIBUTE /*61706*/:
                        case END_ELEMENT_SHORT /*61707*/:
                        case END_ELEMENT_LONG /*61708*/:
                        case BEGIN_DOCUMENT /*61712*/:
                        case END_DOCUMENT /*61713*/:
                        case END_ENTITY /*61715*/:
                        case PROCESSING_INSTRUCTION /*61716*/:
                        case COMMENT /*61719*/:
                            break;
                        case BEGIN_ENTITY /*61714*/:
                            pos += 5;
                            continue;
                        case JOINER /*61718*/:
                            pos++;
                            continue;
                        case DOCUMENT_URI /*61720*/:
                            pos += 3;
                            continue;
                        default:
                            pos = nextDataIndex(pos);
                            continue;
                    }
                }
            }
        }
        return pos;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int nextMatching(int r15, gnu.lists.ItemPredicate r16, int r17, boolean r18) {
        /*
            r14 = this;
            int r10 = r14.posToDataIndex(r15)
            r0 = r17
            int r7 = r14.posToDataIndex(r0)
            r9 = r10
            r0 = r16
            boolean r11 = r0 instanceof gnu.lists.NodePredicate
            if (r11 == 0) goto L_0x0015
            int r9 = r14.nextNodeIndex(r9, r7)
        L_0x0015:
            r1 = 0
            r0 = r16
            boolean r11 = r0 instanceof gnu.lists.ElementPredicate
            if (r11 == 0) goto L_0x002c
            r3 = 1
            r2 = 1
            r4 = 0
        L_0x001f:
            int r11 = r14.gapStart
            if (r9 != r11) goto L_0x0025
            int r9 = r14.gapEnd
        L_0x0025:
            if (r9 < r7) goto L_0x003a
            r11 = -1
            if (r7 == r11) goto L_0x003a
            r11 = 0
        L_0x002b:
            return r11
        L_0x002c:
            r0 = r16
            boolean r11 = r0 instanceof gnu.lists.AttributePredicate
            if (r11 == 0) goto L_0x0036
            r3 = 1
            r2 = 0
            r4 = 0
            goto L_0x001f
        L_0x0036:
            r3 = 1
            r2 = 1
            r4 = 1
            goto L_0x001f
        L_0x003a:
            char[] r11 = r14.data
            char r5 = r11[r9]
            r11 = 40959(0x9fff, float:5.7396E-41)
            if (r5 <= r11) goto L_0x0057
            r11 = 57344(0xe000, float:8.0356E-41)
            if (r5 < r11) goto L_0x004d
            r11 = 61439(0xefff, float:8.6094E-41)
            if (r5 <= r11) goto L_0x0057
        L_0x004d:
            r11 = 45056(0xb000, float:6.3137E-41)
            if (r5 < r11) goto L_0x0074
            r11 = 57343(0xdfff, float:8.0355E-41)
            if (r5 > r11) goto L_0x0074
        L_0x0057:
            if (r4 == 0) goto L_0x0070
            int r11 = r9 << 1
            r0 = r16
            boolean r11 = r0.isInstancePos(r14, r11)
            if (r11 == 0) goto L_0x0070
            int r11 = r14.gapEnd
            if (r9 < r11) goto L_0x006d
            int r11 = r14.gapEnd
            int r12 = r14.gapStart
            int r11 = r11 - r12
            int r9 = r9 - r11
        L_0x006d:
            int r11 = r9 << 1
            goto L_0x002b
        L_0x0070:
            int r8 = r9 + 1
        L_0x0072:
            r9 = r8
            goto L_0x001f
        L_0x0074:
            switch(r5) {
                case 61696: goto L_0x00e9;
                case 61697: goto L_0x00e9;
                case 61698: goto L_0x00ab;
                case 61699: goto L_0x00f1;
                case 61700: goto L_0x00ab;
                case 61701: goto L_0x00f1;
                case 61702: goto L_0x00b0;
                case 61703: goto L_0x0077;
                case 61704: goto L_0x011f;
                case 61705: goto L_0x00d0;
                case 61706: goto L_0x00c8;
                case 61707: goto L_0x00b3;
                case 61708: goto L_0x00c0;
                case 61709: goto L_0x00ab;
                case 61710: goto L_0x00ab;
                case 61711: goto L_0x00bb;
                case 61712: goto L_0x00a3;
                case 61713: goto L_0x00c8;
                case 61714: goto L_0x00a8;
                case 61715: goto L_0x00cd;
                case 61716: goto L_0x00f6;
                case 61717: goto L_0x0111;
                case 61718: goto L_0x00ee;
                case 61719: goto L_0x0103;
                case 61720: goto L_0x00a0;
                default: goto L_0x0077;
            }
        L_0x0077:
            r11 = 40960(0xa000, float:5.7397E-41)
            if (r5 < r11) goto L_0x0143
            r11 = 45055(0xafff, float:6.3136E-41)
            if (r5 > r11) goto L_0x0143
            if (r18 == 0) goto L_0x0138
            int r8 = r9 + 3
        L_0x0085:
            if (r2 == 0) goto L_0x0072
        L_0x0087:
            if (r9 <= r10) goto L_0x0072
            int r11 = r9 << 1
            r0 = r16
            boolean r11 = r0.isInstancePos(r14, r11)
            if (r11 == 0) goto L_0x0072
            int r11 = r14.gapEnd
            if (r9 < r11) goto L_0x009d
            int r11 = r14.gapEnd
            int r12 = r14.gapStart
            int r11 = r11 - r12
            int r9 = r9 - r11
        L_0x009d:
            int r11 = r9 << 1
            goto L_0x002b
        L_0x00a0:
            int r8 = r9 + 3
            goto L_0x0072
        L_0x00a3:
            int r8 = r9 + 5
            if (r3 == 0) goto L_0x0072
            goto L_0x0087
        L_0x00a8:
            int r8 = r9 + 5
            goto L_0x0072
        L_0x00ab:
            int r8 = r9 + 3
            if (r4 == 0) goto L_0x0072
            goto L_0x0087
        L_0x00b0:
            int r8 = r9 + 2
            goto L_0x0072
        L_0x00b3:
            if (r18 != 0) goto L_0x00b8
            r11 = 0
            goto L_0x002b
        L_0x00b8:
            int r8 = r9 + 2
            goto L_0x0072
        L_0x00bb:
            int r8 = r9 + 5
            if (r4 == 0) goto L_0x0072
            goto L_0x0087
        L_0x00c0:
            if (r18 != 0) goto L_0x00c5
            r11 = 0
            goto L_0x002b
        L_0x00c5:
            int r8 = r9 + 7
            goto L_0x0072
        L_0x00c8:
            if (r18 != 0) goto L_0x00cd
            r11 = 0
            goto L_0x002b
        L_0x00cd:
            int r8 = r9 + 1
            goto L_0x0072
        L_0x00d0:
            if (r3 == 0) goto L_0x00e6
            int r11 = r9 + 3
            int r6 = r14.getIntN(r11)
            int r12 = r6 + 1
            if (r6 >= 0) goto L_0x00e4
            char[] r11 = r14.data
            int r11 = r11.length
        L_0x00df:
            int r8 = r12 + r11
        L_0x00e1:
            if (r1 == 0) goto L_0x0072
            goto L_0x0087
        L_0x00e4:
            r11 = r9
            goto L_0x00df
        L_0x00e6:
            int r8 = r9 + 5
            goto L_0x00e1
        L_0x00e9:
            int r8 = r9 + 1
            if (r4 == 0) goto L_0x0072
            goto L_0x0087
        L_0x00ee:
            int r8 = r9 + 1
            goto L_0x0072
        L_0x00f1:
            int r8 = r9 + 5
            if (r4 == 0) goto L_0x0072
            goto L_0x0087
        L_0x00f6:
            int r11 = r9 + 5
            int r12 = r9 + 3
            int r12 = r14.getIntN(r12)
            int r8 = r11 + r12
            if (r3 == 0) goto L_0x0072
            goto L_0x0087
        L_0x0103:
            int r11 = r9 + 3
            int r12 = r9 + 1
            int r12 = r14.getIntN(r12)
            int r8 = r11 + r12
            if (r3 == 0) goto L_0x0072
            goto L_0x0087
        L_0x0111:
            int r11 = r9 + 3
            int r12 = r9 + 1
            int r12 = r14.getIntN(r12)
            int r8 = r11 + r12
            if (r4 == 0) goto L_0x0072
            goto L_0x0087
        L_0x011f:
            if (r18 == 0) goto L_0x0127
            int r8 = r9 + 3
        L_0x0123:
            if (r2 == 0) goto L_0x0072
            goto L_0x0087
        L_0x0127:
            int r11 = r9 + 1
            int r6 = r14.getIntN(r11)
            if (r6 >= 0) goto L_0x0136
            char[] r11 = r14.data
            int r11 = r11.length
        L_0x0132:
            int r11 = r11 + r6
            int r8 = r11 + 7
            goto L_0x0123
        L_0x0136:
            r11 = r9
            goto L_0x0132
        L_0x0138:
            char[] r11 = r14.data
            int r12 = r9 + 1
            char r11 = r11[r12]
            int r11 = r11 + r9
            int r8 = r11 + 2
            goto L_0x0085
        L_0x0143:
            java.lang.Error r11 = new java.lang.Error
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "unknown code:"
            java.lang.StringBuilder r12 = r12.append(r13)
            java.lang.StringBuilder r12 = r12.append(r5)
            java.lang.String r12 = r12.toString()
            r11.<init>(r12)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.lists.TreeList.nextMatching(int, gnu.lists.ItemPredicate, int, boolean):int");
    }

    public int nextPos(int position) {
        int index = posToDataIndex(position);
        if (index == this.data.length) {
            return 0;
        }
        if (index >= this.gapEnd) {
            index -= this.gapEnd - this.gapStart;
        }
        return (index << 1) + 3;
    }

    public final int nextDataIndex(int pos) {
        int pos2;
        if (pos == this.gapStart) {
            pos = this.gapEnd;
        }
        if (pos == this.data.length) {
            return -1;
        }
        int pos3 = pos + 1;
        char datum = this.data[pos];
        if (datum <= 40959 || ((datum >= OBJECT_REF_SHORT && datum <= 61439) || (datum >= 45056 && datum <= 57343))) {
            int i = pos3;
            return pos3;
        } else if (datum < BEGIN_ELEMENT_SHORT || datum > 45055) {
            switch (datum) {
                case 61696:
                case 61697:
                case JOINER /*61718*/:
                    int i2 = pos3;
                    return pos3;
                case INT_FOLLOWS /*61698*/:
                case FLOAT_FOLLOWS /*61700*/:
                case 61709:
                case 61710:
                    int i3 = pos3;
                    return pos3 + 2;
                case LONG_FOLLOWS /*61699*/:
                case DOUBLE_FOLLOWS /*61701*/:
                    int i4 = pos3;
                    return pos3 + 4;
                case CHAR_FOLLOWS /*61702*/:
                    int i5 = pos3;
                    return pos3 + 1;
                case BEGIN_ELEMENT_LONG /*61704*/:
                    int j = getIntN(pos3);
                    int i6 = pos3;
                    return j + (j < 0 ? this.data.length : pos3 - 1) + 7;
                case BEGIN_ATTRIBUTE_LONG /*61705*/:
                    int j2 = getIntN(pos3 + 2);
                    int i7 = pos3;
                    return j2 + (j2 < 0 ? this.data.length : pos3 - 1) + 1;
                case END_ATTRIBUTE /*61706*/:
                case END_ELEMENT_SHORT /*61707*/:
                case END_ELEMENT_LONG /*61708*/:
                case END_DOCUMENT /*61713*/:
                case END_ENTITY /*61715*/:
                    return -1;
                case 61711:
                    int i8 = pos3;
                    return pos3 + 4;
                case BEGIN_DOCUMENT /*61712*/:
                    int j3 = getIntN(pos3);
                    int i9 = pos3;
                    return j3 + (j3 < 0 ? this.data.length : pos3 - 1) + 1;
                case BEGIN_ENTITY /*61714*/:
                    int j4 = pos3 + 4;
                    while (true) {
                        if (j4 == this.gapStart) {
                            j4 = this.gapEnd;
                        }
                        if (j4 == this.data.length) {
                            int i10 = pos3;
                            return -1;
                        } else if (this.data[j4] == END_ENTITY) {
                            int i11 = pos3;
                            return j4 + 1;
                        } else {
                            j4 = nextDataIndex(j4);
                        }
                    }
                case PROCESSING_INSTRUCTION /*61716*/:
                    pos2 = pos3 + 2;
                    break;
                case CDATA_SECTION /*61717*/:
                case COMMENT /*61719*/:
                    pos2 = pos3;
                    break;
                default:
                    throw new Error("unknown code:" + Integer.toHexString(datum));
            }
            return pos2 + 2 + getIntN(pos2);
        } else {
            int i12 = pos3;
            return this.data[pos3] + pos3 + 1;
        }
    }

    public Object documentUriOfPos(int pos) {
        int index = posToDataIndex(pos);
        if (index == this.data.length || this.data[index] != BEGIN_DOCUMENT) {
            return null;
        }
        int next = index + 5;
        if (next == this.gapStart) {
            next = this.gapEnd;
        }
        if (next >= this.data.length || this.data[next] != DOCUMENT_URI) {
            return null;
        }
        return this.objects[getIntN(next + 1)];
    }

    public int compare(int ipos1, int ipos2) {
        int i1 = posToDataIndex(ipos1);
        int i2 = posToDataIndex(ipos2);
        if (i1 < i2) {
            return -1;
        }
        return i1 > i2 ? 1 : 0;
    }

    /* access modifiers changed from: protected */
    public int getIndexDifference(int ipos1, int ipos0) {
        int i0 = posToDataIndex(ipos0);
        int i1 = posToDataIndex(ipos1);
        boolean negate = false;
        if (i0 > i1) {
            negate = true;
            int i = i1;
            i1 = i0;
            i0 = i;
        }
        int i2 = 0;
        while (i0 < i1) {
            i0 = nextDataIndex(i0);
            i2++;
        }
        return negate ? -i2 : i2;
    }

    public int hashCode() {
        return System.identityHashCode(this);
    }

    public void consume(Consumer out) {
        consumeIRange(0, this.data.length, out);
    }

    public void statistics() {
        PrintWriter out = new PrintWriter(System.out);
        statistics(out);
        out.flush();
    }

    public void statistics(PrintWriter out) {
        out.print("data array length: ");
        out.println(this.data.length);
        out.print("data array gap: ");
        out.println(this.gapEnd - this.gapStart);
        out.print("object array length: ");
        out.println(this.objects.length);
    }

    public void dump() {
        PrintWriter out = new PrintWriter(System.out);
        dump(out);
        out.flush();
    }

    public void dump(PrintWriter out) {
        out.println(getClass().getName() + " @" + Integer.toHexString(System.identityHashCode(this)) + " gapStart:" + this.gapStart + " gapEnd:" + this.gapEnd + " length:" + this.data.length);
        dump(out, 0, this.data.length);
    }

    public void dump(PrintWriter out, int start, int limit) {
        int i;
        int toskip = 0;
        int i2 = start;
        while (i2 < limit) {
            if (i2 < this.gapStart || i2 >= this.gapEnd) {
                char ch = this.data[i2];
                out.print("" + i2 + ": 0x" + Integer.toHexString(ch) + '=' + ((short) ch));
                toskip--;
                if (toskip < 0) {
                    if (ch <= 40959) {
                        if (ch >= ' ' && ch < 127) {
                            out.print("='" + ((char) ch) + "'");
                        } else if (ch == 10) {
                            out.print("='\\n'");
                        } else {
                            out.print("='\\u" + Integer.toHexString(ch) + "'");
                        }
                    } else if (ch >= OBJECT_REF_SHORT && ch <= 61439) {
                        int ch2 = ch - OBJECT_REF_SHORT;
                        Object obj = this.objects[ch2];
                        out.print("=Object#" + ch2 + '=' + obj + ':' + obj.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(obj)));
                    } else if (ch >= BEGIN_ELEMENT_SHORT && ch <= 45055) {
                        int ch3 = ch - BEGIN_ELEMENT_SHORT;
                        out.print("=BEGIN_ELEMENT_SHORT end:" + (this.data[i2 + 1] + i2) + " index#" + ch3 + "=<" + this.objects[ch3] + '>');
                        toskip = 2;
                    } else if (ch < 45056 || ch > 57343) {
                        switch (ch) {
                            case 61696:
                                out.print("= false");
                                break;
                            case 61697:
                                out.print("= true");
                                break;
                            case INT_FOLLOWS /*61698*/:
                                out.print("=INT_FOLLOWS value:" + getIntN(i2 + 1));
                                toskip = 2;
                                break;
                            case LONG_FOLLOWS /*61699*/:
                                out.print("=LONG_FOLLOWS value:" + getLongN(i2 + 1));
                                toskip = 4;
                                break;
                            case FLOAT_FOLLOWS /*61700*/:
                                out.write("=FLOAT_FOLLOWS value:" + Float.intBitsToFloat(getIntN(i2 + 1)));
                                toskip = 2;
                                break;
                            case DOUBLE_FOLLOWS /*61701*/:
                                out.print("=DOUBLE_FOLLOWS value:" + Double.longBitsToDouble(getLongN(i2 + 1)));
                                toskip = 4;
                                break;
                            case CHAR_FOLLOWS /*61702*/:
                                out.print("=CHAR_FOLLOWS");
                                toskip = 1;
                                break;
                            case BEGIN_ELEMENT_LONG /*61704*/:
                                int j = getIntN(i2 + 1);
                                int j2 = j + (j < 0 ? this.data.length : i2);
                                out.print("=BEGIN_ELEMENT_LONG end:");
                                out.print(j2);
                                int j3 = getIntN(j2 + 1);
                                out.print(" -> #");
                                out.print(j3);
                                if (j3 < 0 || j3 + 1 >= this.objects.length) {
                                    out.print("=<out-of-bounds>");
                                } else {
                                    out.print("=<" + this.objects[j3] + '>');
                                }
                                toskip = 2;
                                break;
                            case BEGIN_ATTRIBUTE_LONG /*61705*/:
                                int j4 = getIntN(i2 + 1);
                                out.print("=BEGIN_ATTRIBUTE name:" + j4 + "=" + this.objects[j4]);
                                int j5 = getIntN(i2 + 3);
                                if (j5 < 0) {
                                    i = this.data.length;
                                } else {
                                    i = i2;
                                }
                                out.print(" end:" + (j5 + i));
                                toskip = 4;
                                break;
                            case END_ATTRIBUTE /*61706*/:
                                out.print("=END_ATTRIBUTE");
                                break;
                            case END_ELEMENT_SHORT /*61707*/:
                                out.print("=END_ELEMENT_SHORT begin:");
                                int j6 = i2 - this.data[i2 + 1];
                                out.print(j6);
                                int j7 = this.data[j6] - BEGIN_ELEMENT_SHORT;
                                out.print(" -> #");
                                out.print(j7);
                                out.print("=<");
                                out.print(this.objects[j7]);
                                out.print('>');
                                toskip = 1;
                                break;
                            case END_ELEMENT_LONG /*61708*/:
                                int j8 = getIntN(i2 + 1);
                                out.print("=END_ELEMENT_LONG name:" + j8 + "=<" + this.objects[j8] + '>');
                                int j9 = getIntN(i2 + 3);
                                if (j9 < 0) {
                                    j9 += i2;
                                }
                                out.print(" begin:" + j9);
                                int j10 = getIntN(i2 + 5);
                                if (j10 < 0) {
                                    j10 += i2;
                                }
                                out.print(" parent:" + j10);
                                toskip = 6;
                                break;
                            case 61709:
                            case 61710:
                                toskip = 2;
                                break;
                            case 61711:
                                out.print("=POSITION_PAIR_FOLLOWS seq:");
                                int j11 = getIntN(i2 + 1);
                                out.print(j11);
                                out.print('=');
                                Object seq = this.objects[j11];
                                out.print(seq == null ? null : seq.getClass().getName());
                                out.print('@');
                                if (seq == null) {
                                    out.print("null");
                                } else {
                                    out.print(Integer.toHexString(System.identityHashCode(seq)));
                                }
                                out.print(" ipos:");
                                out.print(getIntN(i2 + 3));
                                toskip = 4;
                                break;
                            case BEGIN_DOCUMENT /*61712*/:
                                int j12 = getIntN(i2 + 1);
                                int length = j12 < 0 ? this.data.length : i2;
                                out.print("=BEGIN_DOCUMENT end:");
                                out.print(j12 + length);
                                out.print(" parent:");
                                out.print(getIntN(i2 + 3));
                                toskip = 4;
                                break;
                            case END_DOCUMENT /*61713*/:
                                out.print("=END_DOCUMENT");
                                break;
                            case BEGIN_ENTITY /*61714*/:
                                int j13 = getIntN(i2 + 1);
                                out.print("=BEGIN_ENTITY base:");
                                out.print(j13);
                                out.print(" parent:");
                                out.print(getIntN(i2 + 3));
                                toskip = 4;
                                break;
                            case END_ENTITY /*61715*/:
                                out.print("=END_ENTITY");
                                break;
                            case PROCESSING_INSTRUCTION /*61716*/:
                                out.print("=PROCESSING_INSTRUCTION: ");
                                out.print(this.objects[getIntN(i2 + 1)]);
                                out.print(" '");
                                int j14 = getIntN(i2 + 3);
                                out.write(this.data, i2 + 5, j14);
                                out.print('\'');
                                toskip = j14 + 4;
                                break;
                            case CDATA_SECTION /*61717*/:
                                out.print("=CDATA: '");
                                int j15 = getIntN(i2 + 1);
                                out.write(this.data, i2 + 3, j15);
                                out.print('\'');
                                toskip = j15 + 2;
                                break;
                            case JOINER /*61718*/:
                                out.print("= joiner");
                                break;
                            case COMMENT /*61719*/:
                                out.print("=COMMENT: '");
                                int j16 = getIntN(i2 + 1);
                                out.write(this.data, i2 + 3, j16);
                                out.print('\'');
                                toskip = j16 + 2;
                                break;
                            case DOCUMENT_URI /*61720*/:
                                out.print("=DOCUMENT_URI: ");
                                out.print(this.objects[getIntN(i2 + 1)]);
                                toskip = 2;
                                break;
                        }
                    } else {
                        out.print("= INT_SHORT:" + (ch - INT_SHORT_ZERO));
                    }
                }
                out.println();
                if (1 != 0 && toskip > 0) {
                    i2 += toskip;
                    toskip = 0;
                }
            }
            i2++;
        }
    }
}
