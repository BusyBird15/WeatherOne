package gnu.lists;

public class GeneralArray extends AbstractSequence implements Array {
    static final int[] zeros = new int[8];
    SimpleVector base;
    int[] dimensions;
    int[] lowBounds;
    int offset;
    boolean simple = true;
    int[] strides;

    public static Array makeSimple(int[] lowBounds2, int[] dimensions2, SimpleVector base2) {
        int d = dimensions2.length;
        if (lowBounds2 == null) {
            lowBounds2 = zeros;
            if (d > lowBounds2.length) {
                lowBounds2 = new int[d];
            }
        }
        if (d == 1 && lowBounds2[0] == 0) {
            return base2;
        }
        GeneralArray array = new GeneralArray();
        int[] strides2 = new int[d];
        int n = 1;
        int i = d;
        while (true) {
            i--;
            if (i >= 0) {
                strides2[i] = n;
                n *= dimensions2[i];
            } else {
                array.strides = strides2;
                array.dimensions = dimensions2;
                array.lowBounds = lowBounds2;
                array.base = base2;
                return array;
            }
        }
    }

    public GeneralArray() {
    }

    public GeneralArray(int[] dimensions2) {
        int total = 1;
        int rank = dimensions2.length;
        if (rank <= zeros.length) {
            this.lowBounds = zeros;
        } else {
            this.lowBounds = new int[rank];
        }
        int[] strides2 = new int[rank];
        int i = rank;
        while (true) {
            i--;
            if (i >= 0) {
                strides2[i] = total;
                total *= dimensions2[i];
            } else {
                this.base = new FVector(total);
                this.dimensions = dimensions2;
                this.offset = 0;
                return;
            }
        }
    }

    public int rank() {
        return this.dimensions.length;
    }

    public int getEffectiveIndex(int[] indexes) {
        int index;
        int result = this.offset;
        int i = this.dimensions.length;
        while (true) {
            i--;
            if (i < 0) {
                return result;
            }
            int index2 = indexes[i];
            int low = this.lowBounds[i];
            if (index2 >= low && (index = index2 - low) < this.dimensions[i]) {
                result += this.strides[i] * index;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public Object get(int index) {
        return getRowMajor(index);
    }

    public int createPos(int index, boolean isAfter) {
        int total = this.offset;
        int i = this.dimensions.length;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            int dim = this.dimensions[i];
            int cur = index % dim;
            index /= dim;
            total += this.strides[i] * cur;
        }
        return (isAfter ? 1 : 0) | (total << 1);
    }

    public Object getRowMajor(int index) {
        if (this.simple) {
            return this.base.get(index);
        }
        int total = this.offset;
        int i = this.dimensions.length;
        while (true) {
            i--;
            if (i < 0) {
                return this.base.get(total);
            }
            int dim = this.dimensions[i];
            int cur = index % dim;
            index /= dim;
            total += this.strides[i] * cur;
        }
    }

    public Object get(int[] indexes) {
        return this.base.get(getEffectiveIndex(indexes));
    }

    public Object set(int[] indexes, Object value) {
        return this.base.set(getEffectiveIndex(indexes), value);
    }

    public int size() {
        int total = 1;
        int i = this.dimensions.length;
        while (true) {
            i--;
            if (i < 0) {
                return total;
            }
            total *= this.dimensions[i];
        }
    }

    public int getLowBound(int dim) {
        return this.lowBounds[dim];
    }

    public int getSize(int dim) {
        return this.dimensions[dim];
    }

    public Array transpose(int[] lowBounds2, int[] dimensions2, int offset0, int[] factors) {
        GeneralArray array = (dimensions2.length == 1 && lowBounds2[0] == 0) ? new GeneralArray1() : new GeneralArray();
        array.offset = offset0;
        array.strides = factors;
        array.dimensions = dimensions2;
        array.lowBounds = lowBounds2;
        array.base = this.base;
        array.simple = false;
        return array;
    }

    public static void toString(Array array, StringBuffer sbuf) {
        sbuf.append("#<array");
        int r = array.rank();
        for (int i = 0; i < r; i++) {
            sbuf.append(' ');
            int lo = array.getLowBound(i);
            int sz = array.getSize(i);
            if (lo != 0) {
                sbuf.append(lo);
                sbuf.append(':');
            }
            sbuf.append(lo + sz);
        }
        sbuf.append('>');
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        toString(this, sbuf);
        return sbuf.toString();
    }
}
