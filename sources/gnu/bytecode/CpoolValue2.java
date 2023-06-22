package gnu.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;

public class CpoolValue2 extends CpoolEntry {
    int tag;
    long value;

    CpoolValue2(int tag2) {
        this.tag = tag2;
    }

    CpoolValue2(ConstantPool cpool, int tag2, int hash, long value2) {
        super(cpool, hash);
        this.tag = tag2;
        this.value = value2;
        cpool.count++;
    }

    public int getTag() {
        return this.tag;
    }

    public final long getValue() {
        return this.value;
    }

    static int hashCode(long val) {
        return (int) val;
    }

    public int hashCode() {
        if (this.hash == 0) {
            this.hash = hashCode(this.value);
        }
        return this.hash;
    }

    /* access modifiers changed from: package-private */
    public void write(DataOutputStream dstr) throws IOException {
        dstr.writeByte(this.tag);
        dstr.writeLong(this.value);
    }

    public void print(ClassTypeWriter dst, int verbosity) {
        if (this.tag == 5) {
            if (verbosity > 0) {
                dst.print("Long ");
            }
            dst.print(this.value);
            if (verbosity > 1 && this.value != 0) {
                dst.print("=0x");
                dst.print(Long.toHexString(this.value));
                return;
            }
            return;
        }
        if (verbosity > 0) {
            dst.print("Double ");
        }
        dst.print(Double.longBitsToDouble(this.value));
        if (verbosity > 1) {
            dst.print("=0x");
            dst.print(Long.toHexString(this.value));
        }
    }
}
