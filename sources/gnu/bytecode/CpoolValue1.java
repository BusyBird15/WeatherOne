package gnu.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;

public class CpoolValue1 extends CpoolEntry {
    int tag;
    int value;

    CpoolValue1(int tag2) {
        this.tag = tag2;
    }

    CpoolValue1(ConstantPool cpool, int tag2, int hash, int value2) {
        super(cpool, hash);
        this.tag = tag2;
        this.value = value2;
    }

    public int getTag() {
        return this.tag;
    }

    public final int getValue() {
        return this.value;
    }

    static int hashCode(int val) {
        return val;
    }

    public int hashCode() {
        if (this.hash == 0) {
            this.hash = this.value;
        }
        return this.hash;
    }

    /* access modifiers changed from: package-private */
    public void write(DataOutputStream dstr) throws IOException {
        dstr.writeByte(this.tag);
        dstr.writeInt(this.value);
    }

    public void print(ClassTypeWriter dst, int verbosity) {
        if (this.tag == 3) {
            if (verbosity > 0) {
                dst.print("Integer ");
            }
            dst.print(this.value);
            if (verbosity > 1 && this.value != 0) {
                dst.print("=0x");
                dst.print(Integer.toHexString(this.value));
                return;
            }
            return;
        }
        if (verbosity > 0) {
            dst.print("Float ");
        }
        dst.print(Float.intBitsToFloat(this.value));
        if (verbosity > 1) {
            dst.print("=0x");
            dst.print(Integer.toHexString(this.value));
        }
    }
}
