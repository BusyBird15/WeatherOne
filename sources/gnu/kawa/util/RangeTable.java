package gnu.kawa.util;

import java.util.Hashtable;

public class RangeTable implements Cloneable {
    Hashtable hash = new Hashtable(200);
    Object[] index = new Object[128];

    public Object lookup(int key, Object defaultValue) {
        if ((key & 127) == key) {
            return this.index[key];
        }
        return this.hash.get(new Integer(key));
    }

    public void set(int lo, int hi, Object value) {
        if (lo <= hi) {
            int i = lo;
            while (true) {
                if ((i & 127) == i) {
                    this.index[i] = value;
                } else {
                    this.hash.put(new Integer(i), value);
                }
                if (i != hi) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void set(int key, Object value) {
        set(key, key, value);
    }

    public void remove(int lo, int hi) {
        if (lo <= hi) {
            int i = lo;
            while (true) {
                if ((i & 127) == i) {
                    this.index[i] = null;
                } else {
                    this.hash.remove(new Integer(i));
                }
                if (i != hi) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public void remove(int key) {
        remove(key, key);
    }

    public RangeTable copy() {
        RangeTable copy = new RangeTable();
        copy.index = (Object[]) this.index.clone();
        copy.hash = (Hashtable) this.hash.clone();
        return copy;
    }

    public Object clone() {
        return copy();
    }
}
