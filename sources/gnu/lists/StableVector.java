package gnu.lists;

public class StableVector extends GapVector {
    static final int END_POSITION = 1;
    protected static final int FREE_POSITION = -2;
    static final int START_POSITION = 0;
    protected int free;
    protected int[] positions;

    /* access modifiers changed from: protected */
    public void chainFreelist() {
        this.free = -1;
        int i = this.positions.length;
        while (true) {
            i--;
            if (i <= 1) {
                return;
            }
            if (this.positions[i] == -2) {
                this.positions[i] = this.free;
                this.free = i;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void unchainFreelist() {
        int i = this.free;
        while (i >= 0) {
            int next = this.positions[i];
            this.positions[i] = -2;
            i = next;
        }
        this.free = -2;
    }

    public int startPos() {
        return 0;
    }

    public int endPos() {
        return 1;
    }

    public StableVector(SimpleVector base) {
        super(base);
        this.positions = new int[16];
        this.positions[0] = 0;
        this.positions[1] = (base.getBufferLength() << 1) | 1;
        this.free = -1;
        int i = this.positions.length;
        while (true) {
            i--;
            if (i > 1) {
                this.positions[i] = this.free;
                this.free = i;
            } else {
                return;
            }
        }
    }

    protected StableVector() {
    }

    /* access modifiers changed from: protected */
    public int allocPositionIndex() {
        if (this.free == -2) {
            chainFreelist();
        }
        if (this.free < 0) {
            int oldLength = this.positions.length;
            int[] tmp = new int[(oldLength * 2)];
            System.arraycopy(this.positions, 0, tmp, 0, oldLength);
            int i = oldLength * 2;
            while (true) {
                i--;
                if (i < oldLength) {
                    break;
                }
                tmp[i] = this.free;
                this.free = i;
            }
            this.positions = tmp;
        }
        int pos = this.free;
        this.free = this.positions[this.free];
        return pos;
    }

    public int createPos(int index, boolean isAfter) {
        int i = 1;
        if (index == 0 && !isAfter) {
            return 0;
        }
        if (isAfter && index == size()) {
            return 1;
        }
        if (index > this.gapStart || (index == this.gapStart && isAfter)) {
            index += this.gapEnd - this.gapStart;
        }
        int ipos = allocPositionIndex();
        int[] iArr = this.positions;
        int i2 = index << 1;
        if (!isAfter) {
            i = 0;
        }
        iArr[ipos] = i | i2;
        return ipos;
    }

    /* access modifiers changed from: protected */
    public boolean isAfterPos(int ipos) {
        return (this.positions[ipos] & 1) != 0;
    }

    public boolean hasNext(int ipos) {
        int index = this.positions[ipos] >>> 1;
        if (index >= this.gapStart) {
            index += this.gapEnd - this.gapStart;
        }
        return index < this.base.getBufferLength();
    }

    public int nextPos(int ipos) {
        int ppos = this.positions[ipos];
        int index = ppos >>> 1;
        if (index >= this.gapStart) {
            index += this.gapEnd - this.gapStart;
        }
        if (index >= this.base.getBufferLength()) {
            releasePos(ipos);
            return 0;
        }
        if (ipos == 0) {
            ipos = createPos(0, true);
        }
        this.positions[ipos] = ppos | 1;
        return ipos;
    }

    public int nextIndex(int ipos) {
        int index = this.positions[ipos] >>> 1;
        if (index > this.gapStart) {
            return index - (this.gapEnd - this.gapStart);
        }
        return index;
    }

    public void releasePos(int ipos) {
        if (ipos >= 2) {
            if (this.free == -2) {
                chainFreelist();
            }
            this.positions[ipos] = this.free;
            this.free = ipos;
        }
    }

    public int copyPos(int ipos) {
        if (ipos <= 1) {
            return ipos;
        }
        int i = allocPositionIndex();
        this.positions[i] = this.positions[ipos];
        return i;
    }

    public void fillPosRange(int fromPos, int toPos, Object value) {
        fillPosRange(this.positions[fromPos], this.positions[toPos], value);
    }

    /* access modifiers changed from: protected */
    public void shiftGap(int newGapStart) {
        int low;
        int high;
        int adjust;
        int oldGapStart = this.gapStart;
        int delta = newGapStart - oldGapStart;
        if (delta > 0) {
            int low2 = this.gapEnd;
            int high2 = low2 + delta;
            adjust = (oldGapStart - low2) << 1;
            low = low2 << 1;
            high = (high2 << 1) - 1;
        } else if (newGapStart != oldGapStart) {
            low = (newGapStart << 1) + 1;
            high = oldGapStart << 1;
            adjust = (this.gapEnd - oldGapStart) << 1;
        } else {
            return;
        }
        super.shiftGap(newGapStart);
        adjustPositions(low, high, adjust);
    }

    /* access modifiers changed from: protected */
    public void gapReserve(int where, int needed) {
        int oldGapEnd = this.gapEnd;
        int oldGapStart = this.gapStart;
        if (needed > oldGapEnd - oldGapStart) {
            int oldLength = this.base.size;
            super.gapReserve(where, needed);
            int newLength = this.base.size;
            if (where == oldGapStart) {
                adjustPositions(oldGapEnd << 1, (newLength << 1) | 1, (newLength - oldLength) << 1);
                return;
            }
            adjustPositions(oldGapEnd << 1, (oldLength << 1) | 1, (oldGapStart - oldGapEnd) << 1);
            adjustPositions(this.gapStart << 1, (newLength << 1) | 1, (this.gapEnd - this.gapStart) << 1);
        } else if (where != this.gapStart) {
            shiftGap(where);
        }
    }

    /* access modifiers changed from: protected */
    public void adjustPositions(int low, int high, int delta) {
        int index;
        if (this.free >= -1) {
            unchainFreelist();
        }
        int low2 = low ^ Integer.MIN_VALUE;
        int high2 = high ^ Integer.MIN_VALUE;
        int i = this.positions.length;
        while (true) {
            i--;
            if (i > 0) {
                int pos = this.positions[i];
                if (pos != -2 && (index = pos ^ Integer.MIN_VALUE) >= low2 && index <= high2) {
                    this.positions[i] = pos + delta;
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public int addPos(int ipos, Object value) {
        int ppos = this.positions[ipos];
        int index = ppos >>> 1;
        if (index >= this.gapStart) {
            index += this.gapEnd - this.gapStart;
        }
        if ((ppos & 1) == 0) {
            if (ipos == 0) {
                ipos = createPos(0, true);
            } else {
                this.positions[ipos] = ppos | 1;
            }
        }
        add(index, value);
        return ipos;
    }

    /* access modifiers changed from: protected */
    public void removePosRange(int ipos0, int ipos1) {
        super.removePosRange(this.positions[ipos0], this.positions[ipos1]);
        int low = this.gapStart;
        int high = this.gapEnd;
        if (this.free >= -1) {
            unchainFreelist();
        }
        int i = this.positions.length;
        while (true) {
            i--;
            if (i > 0) {
                int pos = this.positions[i];
                if (pos != -2) {
                    int index = pos >> 1;
                    if ((pos & 1) != 0) {
                        if (index >= low && index < high) {
                            this.positions[i] = (this.gapEnd << 1) | 1;
                        }
                    } else if (index > low && index <= high) {
                        this.positions[i] = this.gapStart << 1;
                    }
                }
            } else {
                return;
            }
        }
    }

    public void consumePosRange(int iposStart, int iposEnd, Consumer out) {
        super.consumePosRange(this.positions[iposStart], this.positions[iposEnd], out);
    }
}
