package kawa.lang;

import gnu.lists.Consumer;
import gnu.lists.Pair;
import gnu.text.Printable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class PairPat extends Pattern implements Printable, Externalizable {
    Pattern car;
    private int car_count;
    Pattern cdr;
    private int cdr_count;

    public PairPat() {
    }

    public PairPat(Pattern car2, Pattern cdr2) {
        this.car = car2;
        this.cdr = cdr2;
        this.car_count = car2.varCount();
        this.cdr_count = cdr2.varCount();
    }

    public static PairPat make(Pattern car2, Pattern cdr2) {
        return new PairPat(car2, cdr2);
    }

    public boolean match(Object obj, Object[] vars, int start_vars) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        if (!this.car.match(pair.getCar(), vars, start_vars) || !this.cdr.match(pair.getCdr(), vars, this.car_count + start_vars)) {
            return false;
        }
        return true;
    }

    public void print(Consumer out) {
        out.write("#<pair-pattern car: ");
        this.car.print(out);
        out.write(" cdr: ");
        this.cdr.print(out);
        out.write(62);
    }

    public int varCount() {
        return this.car_count + this.cdr_count;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.car);
        out.writeObject(this.cdr);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.car = (Pattern) in.readObject();
        this.cdr = (Pattern) in.readObject();
    }
}
