package com.google.appinventor.components.runtime.repackaged.org.json.zip;

import com.google.appinventor.components.runtime.repackaged.org.json.Kim;
import java.util.HashMap;

class MapKeep extends Keep {
    private Object[] list = new Object[this.capacity];
    private HashMap map = new HashMap(this.capacity);

    public MapKeep(int bits) {
        super(bits);
    }

    private void compact() {
        int to = 0;
        for (int from = 0; from < this.capacity; from++) {
            Object key = this.list[from];
            long usage = age(this.uses[from]);
            if (usage > 0) {
                this.uses[to] = usage;
                this.list[to] = key;
                this.map.put(key, new Integer(to));
                to++;
            } else {
                this.map.remove(key);
            }
        }
        if (to < this.capacity) {
            this.length = to;
        } else {
            this.map.clear();
            this.length = 0;
        }
        this.power = 0;
    }

    public int find(Object key) {
        Object o = this.map.get(key);
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        }
        return -1;
    }

    public boolean postMortem(PostMortem pm) {
        boolean b;
        MapKeep that = (MapKeep) pm;
        if (this.length != that.length) {
            JSONzip.log(new StringBuffer().append(this.length).append(" <> ").append(that.length).toString());
            return false;
        }
        for (int i = 0; i < this.length; i++) {
            if (this.list[i] instanceof Kim) {
                b = ((Kim) this.list[i]).equals(that.list[i]);
            } else {
                Object o = this.list[i];
                Object q = that.list[i];
                if (o instanceof Number) {
                    o = o.toString();
                }
                if (q instanceof Number) {
                    q = q.toString();
                }
                b = o.equals(q);
            }
            if (!b) {
                JSONzip.log(new StringBuffer().append("\n[").append(i).append("]\n ").append(this.list[i]).append("\n ").append(that.list[i]).append("\n ").append(this.uses[i]).append("\n ").append(that.uses[i]).toString());
                return false;
            }
        }
        return true;
    }

    public void register(Object value) {
        if (this.length >= this.capacity) {
            compact();
        }
        this.list[this.length] = value;
        this.map.put(value, new Integer(this.length));
        this.uses[this.length] = 1;
        this.length++;
    }

    public Object value(int integer) {
        return this.list[integer];
    }
}
