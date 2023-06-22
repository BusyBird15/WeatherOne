package gnu.lists;

import java.util.Collection;

public abstract class SimpleVector extends AbstractSequence implements Sequence, Array {
    public int size;

    /* access modifiers changed from: protected */
    public abstract void clearBuffer(int i, int i2);

    /* access modifiers changed from: protected */
    public abstract Object getBuffer();

    /* access modifiers changed from: protected */
    public abstract Object getBuffer(int i);

    public abstract int getBufferLength();

    /* access modifiers changed from: protected */
    public abstract Object setBuffer(int i, Object obj);

    public abstract void setBufferLength(int i);

    public final int size() {
        return this.size;
    }

    public void setSize(int size2) {
        int newLength = 16;
        int oldSize = this.size;
        this.size = size2;
        if (size2 < oldSize) {
            clearBuffer(size2, oldSize - size2);
            return;
        }
        int oldLength = getBufferLength();
        if (size2 > oldLength) {
            if (oldLength >= 16) {
                newLength = oldLength * 2;
            }
            if (size2 <= newLength) {
                size2 = newLength;
            }
            setBufferLength(size2);
        }
    }

    /* access modifiers changed from: protected */
    public void resizeShift(int oldGapStart, int oldGapEnd, int newGapStart, int newGapEnd) {
        int newGapSize = newGapEnd - newGapStart;
        int oldLength = getBufferLength();
        int newLength = (oldLength - (oldGapEnd - oldGapStart)) + newGapSize;
        if (newLength > oldLength) {
            setBufferLength(newLength);
            this.size = newLength;
        }
        int gapDelta = oldGapStart - newGapStart;
        if (gapDelta >= 0) {
            int endLength = oldLength - oldGapEnd;
            shift(oldGapEnd, newLength - endLength, endLength);
            if (gapDelta > 0) {
                shift(newGapStart, newGapEnd, gapDelta);
            }
        } else {
            int endLength2 = newLength - newGapEnd;
            shift(oldLength - endLength2, newGapEnd, endLength2);
            shift(oldGapEnd, oldGapStart, newGapStart - oldGapStart);
        }
        clearBuffer(newGapStart, newGapSize);
    }

    /* access modifiers changed from: protected */
    public boolean isAfterPos(int ipos) {
        return (ipos & 1) != 0;
    }

    /* access modifiers changed from: protected */
    public int nextIndex(int ipos) {
        return ipos == -1 ? this.size : ipos >>> 1;
    }

    public int createPos(int index, boolean isAfter) {
        return (isAfter ? 1 : 0) | (index << 1);
    }

    public int nextPos(int ipos) {
        int index;
        if (ipos == -1 || (index = ipos >>> 1) == this.size) {
            return 0;
        }
        return (index << 1) + 3;
    }

    public Object get(int index) {
        if (index < this.size) {
            return getBuffer(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public Object getPosNext(int ipos) {
        int index = ipos >>> 1;
        return index >= this.size ? eofValue : getBuffer(index);
    }

    public int intAtBuffer(int index) {
        return Convert.toInt(getBuffer(index));
    }

    public int intAt(int index) {
        if (index < this.size) {
            return intAtBuffer(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public long longAt(int index) {
        if (index < this.size) {
            return longAtBuffer(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public long longAtBuffer(int index) {
        return Convert.toLong(getBuffer(index));
    }

    public Object getRowMajor(int i) {
        return get(i);
    }

    public Object set(int index, Object value) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        Object old = getBuffer(index);
        setBuffer(index, value);
        return old;
    }

    public void fill(Object value) {
        int i = this.size;
        while (true) {
            i--;
            if (i >= 0) {
                setBuffer(i, value);
            } else {
                return;
            }
        }
    }

    public void fillPosRange(int fromPos, int toPos, Object value) {
        int j = toPos == -1 ? this.size : toPos >>> 1;
        for (int i = fromPos == -1 ? this.size : fromPos >>> 1; i < j; i++) {
            setBuffer(i, value);
        }
    }

    public void fill(int fromIndex, int toIndex, Object value) {
        if (fromIndex < 0 || toIndex > this.size) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = fromIndex; i < toIndex; i++) {
            setBuffer(i, value);
        }
    }

    public void shift(int srcStart, int dstStart, int count) {
        Object data = getBuffer();
        System.arraycopy(data, srcStart, data, dstStart, count);
    }

    public boolean add(Object o) {
        add(this.size, o);
        return true;
    }

    /* access modifiers changed from: protected */
    public int addPos(int ipos, Object value) {
        int index = ipos >>> 1;
        add(index, value);
        return (index << 1) + 3;
    }

    public void add(int index, Object o) {
        int i = 16;
        int newSize = this.size + 1;
        this.size = newSize;
        int length = getBufferLength();
        if (newSize > length) {
            if (length >= 16) {
                i = length * 2;
            }
            setBufferLength(i);
        }
        this.size = newSize;
        if (this.size != index) {
            shift(index, index + 1, this.size - index);
        }
        set(index, o);
    }

    public boolean addAll(int index, Collection c) {
        boolean changed = false;
        int count = c.size();
        setSize(this.size + count);
        shift(index, index + count, (this.size - count) - index);
        for (Object obj : c) {
            set(index, obj);
            changed = true;
            index++;
        }
        return changed;
    }

    /* access modifiers changed from: protected */
    public void removePosRange(int ipos0, int ipos1) {
        int ipos02 = ipos0 >>> 1;
        int ipos12 = ipos1 >>> 1;
        if (ipos02 < ipos12) {
            if (ipos12 > this.size) {
                ipos12 = this.size;
            }
            shift(ipos12, ipos02, this.size - ipos12);
            int count = ipos12 - ipos02;
            this.size -= count;
            clearBuffer(this.size, count);
        }
    }

    public void removePos(int ipos, int count) {
        int ipos0;
        int ipos1;
        int index = ipos >>> 1;
        if (index > this.size) {
            index = this.size;
        }
        if (count >= 0) {
            ipos0 = index;
            ipos1 = index + count;
        } else {
            ipos0 = index + count;
            ipos1 = index;
            count = -count;
        }
        if (ipos0 < 0 || ipos1 >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        shift(ipos1, ipos0, this.size - ipos1);
        this.size -= count;
        clearBuffer(this.size, count);
    }

    public Object remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        Object result = get(index);
        shift(index + 1, index, 1);
        this.size--;
        clearBuffer(this.size, 1);
        return result;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index < 0) {
            return false;
        }
        shift(index + 1, index, 1);
        this.size--;
        clearBuffer(this.size, 1);
        return true;
    }

    public boolean removeAll(Collection c) {
        boolean changed = false;
        int j = 0;
        for (int i = 0; i < this.size; i++) {
            Object value = get(i);
            if (c.contains(value)) {
                changed = true;
            } else {
                if (changed) {
                    set(j, value);
                }
                j++;
            }
        }
        setSize(j);
        return changed;
    }

    public boolean retainAll(Collection c) {
        boolean changed = false;
        int j = 0;
        for (int i = 0; i < this.size; i++) {
            Object value = get(i);
            if (!c.contains(value)) {
                changed = true;
            } else {
                if (changed) {
                    set(j, value);
                }
                j++;
            }
        }
        setSize(j);
        return changed;
    }

    public void clear() {
        setSize(0);
    }

    public String getTag() {
        return null;
    }

    protected static int compareToInt(SimpleVector v1, SimpleVector v2) {
        int n;
        int n1 = v1.size;
        int n2 = v2.size;
        if (n1 > n2) {
            n = n2;
        } else {
            n = n1;
        }
        for (int i = 0; i < n; i++) {
            int i1 = v1.intAtBuffer(i);
            int i2 = v2.intAtBuffer(i);
            if (11 != i2) {
                return i1 > i2 ? 1 : -1;
            }
        }
        return n1 - n2;
    }

    protected static int compareToLong(SimpleVector v1, SimpleVector v2) {
        int n;
        int n1 = v1.size;
        int n2 = v2.size;
        if (n1 > n2) {
            n = n2;
        } else {
            n = n1;
        }
        for (int i = 0; i < n; i++) {
            long i1 = v1.longAtBuffer(i);
            long i2 = v2.longAtBuffer(i);
            if (i1 != i2) {
                return i1 > i2 ? 1 : -1;
            }
        }
        return n1 - n2;
    }

    public void consume(int start, int length, Consumer out) {
        consumePosRange(start << 1, (start + length) << 1, out);
    }

    public boolean consumeNext(int ipos, Consumer out) {
        int index = ipos >>> 1;
        if (index >= this.size) {
            return false;
        }
        out.writeObject(getBuffer(index));
        return true;
    }

    public void consumePosRange(int iposStart, int iposEnd, Consumer out) {
        if (!out.ignoring()) {
            int end = iposEnd >>> 1;
            if (end > this.size) {
                end = this.size;
            }
            for (int i = iposStart >>> 1; i < end; i++) {
                out.writeObject(getBuffer(i));
            }
        }
    }

    public int getNextKind(int ipos) {
        if (hasNext(ipos)) {
            return getElementKind();
        }
        return 0;
    }

    public int getElementKind() {
        return 32;
    }

    public Array transpose(int[] lowBounds, int[] dimensions, int offset0, int[] factors) {
        GeneralArray array = new GeneralArray();
        array.strides = factors;
        array.dimensions = dimensions;
        array.lowBounds = lowBounds;
        array.offset = offset0;
        array.base = this;
        array.simple = false;
        return array;
    }
}
