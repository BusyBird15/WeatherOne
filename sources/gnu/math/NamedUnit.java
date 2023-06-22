package gnu.math;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

public class NamedUnit extends Unit implements Externalizable {
    Unit base;
    NamedUnit chain;
    String name;
    double scale;

    public NamedUnit() {
    }

    public NamedUnit(String name2, DQuantity value) {
        this.name = name2.intern();
        this.scale = value.factor;
        this.base = value.unt;
        init();
    }

    public NamedUnit(String name2, double factor, Unit base2) {
        this.name = name2;
        this.base = base2;
        this.scale = factor;
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.factor = this.scale * this.base.factor;
        this.dims = this.base.dims;
        this.name = this.name.intern();
        int index = (Integer.MAX_VALUE & this.name.hashCode()) % table.length;
        this.chain = table[index];
        table[index] = this;
    }

    public String getName() {
        return this.name;
    }

    public static NamedUnit lookup(String name2) {
        String name3 = name2.intern();
        for (NamedUnit unit = table[(Integer.MAX_VALUE & name3.hashCode()) % table.length]; unit != null; unit = unit.chain) {
            if (unit.name == name3) {
                return unit;
            }
        }
        return null;
    }

    public static NamedUnit lookup(String name2, double scale2, Unit base2) {
        String name3 = name2.intern();
        for (NamedUnit unit = table[(Integer.MAX_VALUE & name3.hashCode()) % table.length]; unit != null; unit = unit.chain) {
            if (unit.name == name3 && unit.scale == scale2 && unit.base == base2) {
                return unit;
            }
        }
        return null;
    }

    public static NamedUnit make(String name2, double scale2, Unit base2) {
        NamedUnit old = lookup(name2, scale2, base2);
        return old == null ? new NamedUnit(name2, scale2, base2) : old;
    }

    public static NamedUnit make(String name2, Quantity value) {
        double scale2;
        if (value instanceof DQuantity) {
            scale2 = ((DQuantity) value).factor;
        } else if (value.imValue() != 0.0d) {
            throw new ArithmeticException("defining " + name2 + " using complex value");
        } else {
            scale2 = value.re().doubleValue();
        }
        Unit base2 = value.unit();
        NamedUnit old = lookup(name2, scale2, base2);
        if (old == null) {
            return new NamedUnit(name2, scale2, base2);
        }
        return old;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.name);
        out.writeDouble(this.scale);
        out.writeObject(this.base);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name = in.readUTF();
        this.scale = in.readDouble();
        this.base = (Unit) in.readObject();
    }

    public Object readResolve() throws ObjectStreamException {
        NamedUnit unit = lookup(this.name, this.scale, this.base);
        if (unit != null) {
            return unit;
        }
        init();
        return this;
    }
}
