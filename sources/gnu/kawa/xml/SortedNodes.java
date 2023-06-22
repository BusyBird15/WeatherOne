package gnu.kawa.xml;

import gnu.lists.AbstractSequence;

public class SortedNodes extends Nodes {
    int nesting = 0;

    /* access modifiers changed from: package-private */
    public int compareIndex(int index, AbstractSequence seq2, int ipos2) {
        if (this.data[index] == 61711) {
            return AbstractSequence.compare((AbstractSequence) this.objects[getIntN(index + 1)], getIntN(index + 3), seq2, ipos2);
        }
        throw new RuntimeException("invalid kind of value to compare");
    }

    /* access modifiers changed from: package-private */
    public int find(int start, int count, AbstractSequence seq, int ipos) {
        int lo = 0;
        int hi = count;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = compareIndex((mid * 5) + start, seq, ipos);
            if (cmp == 0) {
                return -1;
            }
            if (cmp > 0) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return (lo * 5) + start;
    }

    public void writePosition(AbstractSequence seq, int ipos) {
        int i;
        if (this.count > 0) {
            int lastIndex = this.gapStart - 5;
            int cmp = compareIndex(lastIndex, seq, ipos);
            if (cmp < 0) {
                int i2 = this.gapEnd;
                int i3 = find(i2, (this.data.length - i2) / 5, seq, ipos);
                if (i3 >= 0) {
                    int delta = i3 - this.gapEnd;
                    if (delta > 0) {
                        System.arraycopy(this.data, this.gapEnd, this.data, this.gapStart, delta);
                        this.gapEnd = i3;
                        this.gapStart += delta;
                    }
                } else {
                    return;
                }
            } else if (cmp != 0 && (i = find(0, lastIndex / 5, seq, ipos)) >= 0) {
                int delta2 = this.gapStart - i;
                if (delta2 > 0) {
                    System.arraycopy(this.data, i, this.data, this.gapEnd - delta2, delta2);
                    this.gapStart = i;
                    this.gapEnd -= delta2;
                }
            } else {
                return;
            }
        }
        super.writePosition(seq, ipos);
    }
}
