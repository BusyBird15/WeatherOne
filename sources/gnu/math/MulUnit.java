package gnu.math;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

class MulUnit extends Unit implements Externalizable {
    MulUnit next;
    int power1;
    int power2;
    Unit unit1;
    Unit unit2;

    MulUnit(Unit unit12, int power12, Unit unit22, int power22) {
        this.unit1 = unit12;
        this.unit2 = unit22;
        this.power1 = power12;
        this.power2 = power22;
        this.dims = Dimensions.product(unit12.dims, power12, unit22.dims, power22);
        if (power12 == 1) {
            this.factor = unit12.factor;
        } else {
            this.factor = Math.pow(unit12.factor, (double) power12);
        }
        if (power22 >= 0) {
            int i = power22;
            while (true) {
                i--;
                if (i < 0) {
                    break;
                }
                this.factor *= unit22.factor;
            }
        } else {
            int i2 = -power22;
            while (true) {
                i2--;
                if (i2 < 0) {
                    break;
                }
                this.factor /= unit22.factor;
            }
        }
        this.next = unit12.products;
        unit12.products = this;
    }

    MulUnit(Unit unit12, Unit unit22, int power22) {
        this(unit12, 1, unit22, power22);
    }

    public String toString() {
        StringBuffer str = new StringBuffer(60);
        str.append(this.unit1);
        if (this.power1 != 1) {
            str.append('^');
            str.append(this.power1);
        }
        if (this.power2 != 0) {
            str.append('*');
            str.append(this.unit2);
            if (this.power2 != 1) {
                str.append('^');
                str.append(this.power2);
            }
        }
        return str.toString();
    }

    public Unit sqrt() {
        if ((this.power1 & 1) == 0 && (this.power2 & 1) == 0) {
            return times(this.unit1, this.power1 >> 1, this.unit2, this.power2 >> 1);
        }
        return super.sqrt();
    }

    static MulUnit lookup(Unit unit12, int power12, Unit unit22, int power22) {
        for (MulUnit u = unit12.products; u != null; u = u.next) {
            if (u.unit1 == unit12 && u.unit2 == unit22 && u.power1 == power12 && u.power2 == power22) {
                return u;
            }
        }
        return null;
    }

    public static MulUnit make(Unit unit12, int power12, Unit unit22, int power22) {
        MulUnit u = lookup(unit12, power12, unit22, power22);
        return u != null ? u : new MulUnit(unit12, power12, unit22, power22);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.unit1);
        out.writeInt(this.power1);
        out.writeObject(this.unit2);
        out.writeInt(this.power2);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.unit1 = (Unit) in.readObject();
        this.power1 = in.readInt();
        this.unit2 = (Unit) in.readObject();
        this.power2 = in.readInt();
    }

    public Object readResolve() throws ObjectStreamException {
        MulUnit u = lookup(this.unit1, this.power1, this.unit2, this.power2);
        return u != null ? u : this;
    }
}
