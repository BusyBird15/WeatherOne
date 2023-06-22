package gnu.mapping;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LocationEnumeration implements Iterator<Location>, Enumeration<Location> {
    NamedLocation[] bindings;
    SimpleEnvironment env;
    int index;
    LocationEnumeration inherited;
    NamedLocation nextLoc;
    NamedLocation prevLoc;

    public LocationEnumeration(NamedLocation[] bindings2, int count) {
        this.bindings = bindings2;
        this.index = count;
    }

    public LocationEnumeration(SimpleEnvironment env2) {
        this(env2.table, 1 << env2.log2Size);
    }

    public boolean hasMoreElements() {
        return this.env.hasMoreElements(this);
    }

    public Location nextElement() {
        return nextLocation();
    }

    public Location nextLocation() {
        NamedLocation first;
        if (this.nextLoc != null || hasMoreElements()) {
            NamedLocation namedLocation = this.prevLoc;
            if (this.prevLoc == null && this.nextLoc != (first = this.bindings[this.index])) {
                this.prevLoc = first;
            }
            while (this.prevLoc != null && this.prevLoc.next != this.nextLoc) {
                this.prevLoc = this.prevLoc.next;
            }
            Location r = this.nextLoc;
            this.nextLoc = this.nextLoc.next;
            return r;
        }
        throw new NoSuchElementException();
    }

    public boolean hasNext() {
        return hasMoreElements();
    }

    public Location next() {
        return nextElement();
    }

    public void remove() {
        NamedLocation curLoc = this.prevLoc != null ? this.prevLoc.next : this.bindings[this.index];
        if (curLoc == null || curLoc.next != this.nextLoc) {
            throw new IllegalStateException();
        }
        curLoc.next = null;
        if (this.prevLoc != null) {
            this.prevLoc.next = this.nextLoc;
        } else {
            this.bindings[this.index] = this.nextLoc;
        }
        SimpleEnvironment simpleEnvironment = this.env;
        simpleEnvironment.num_bindings--;
    }
}
