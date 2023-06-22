package gnu.lists;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class U32Vector extends SimpleVector implements Externalizable, Comparable {
    int[] data;

    public U32Vector() {
        this.data = S32Vector.empty;
    }

    public U32Vector(int size, int value) {
        int[] array = new int[size];
        this.data = array;
        this.size = size;
        while (true) {
            size--;
            if (size >= 0) {
                array[size] = value;
            } else {
                return;
            }
        }
    }

    public U32Vector(int size) {
        this.data = new int[size];
        this.size = size;
    }

    public U32Vector(int[] data2) {
        this.data = data2;
        this.size = data2.length;
    }

    public U32Vector(Sequence seq) {
        this.data = new int[seq.size()];
        addAll(seq);
    }

    public int getBufferLength() {
        return this.data.length;
    }

    public void setBufferLength(int length) {
        int oldLength = this.data.length;
        if (oldLength != length) {
            int[] tmp = new int[length];
            int[] iArr = this.data;
            if (oldLength >= length) {
                oldLength = length;
            }
            System.arraycopy(iArr, 0, tmp, 0, oldLength);
            this.data = tmp;
        }
    }

    /* access modifiers changed from: protected */
    public Object getBuffer() {
        return this.data;
    }

    public final int intAtBuffer(int index) {
        return this.data[index];
    }

    public final long longAtBuffer(int index) {
        return ((long) this.data[index]) & 4294967295L;
    }

    public final long longAt(int index) {
        if (index <= this.size) {
            return longAtBuffer(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public final Object get(int index) {
        if (index <= this.size) {
            return Convert.toObjectUnsigned(this.data[index]);
        }
        throw new IndexOutOfBoundsException();
    }

    public final Object getBuffer(int index) {
        return Convert.toObjectUnsigned(this.data[index]);
    }

    public Object setBuffer(int index, Object value) {
        int old = this.data[index];
        this.data[index] = Convert.toIntUnsigned(value);
        return Convert.toObjectUnsigned(old);
    }

    public final void setIntAt(int index, int value) {
        if (index > this.size) {
            throw new IndexOutOfBoundsException();
        }
        this.data[index] = value;
    }

    public final void setIntAtBuffer(int index, int value) {
        this.data[index] = value;
    }

    /* access modifiers changed from: protected */
    public void clearBuffer(int start, int count) {
        while (true) {
            int start2 = start;
            count--;
            if (count >= 0) {
                start = start2 + 1;
                this.data[start2] = 0;
            } else {
                return;
            }
        }
    }

    public int getElementKind() {
        return 19;
    }

    public String getTag() {
        return "u32";
    }

    public boolean consumeNext(int ipos, Consumer out) {
        int index = ipos >>> 1;
        if (index >= this.size) {
            return false;
        }
        out.writeInt(this.data[index]);
        return true;
    }

    public void consumePosRange(int iposStart, int iposEnd, Consumer out) {
        if (!out.ignoring()) {
            int end = iposEnd >>> 1;
            if (end > this.size) {
                end = this.size;
            }
            for (int i = iposStart >>> 1; i < end; i++) {
                out.writeInt(this.data[i]);
            }
        }
    }

    public int compareTo(Object obj) {
        return compareToLong(this, (U32Vector) obj);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        int size = this.size;
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeInt(this.data[i]);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        int[] data2 = new int[size];
        for (int i = 0; i < size; i++) {
            data2[i] = in.readInt();
        }
        this.data = data2;
        this.size = size;
    }
}
