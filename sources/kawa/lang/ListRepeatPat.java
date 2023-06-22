package kawa.lang;

import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.text.Printable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ListRepeatPat extends Pattern implements Printable, Externalizable {
    Pattern element_pattern;

    public ListRepeatPat() {
    }

    public ListRepeatPat(Pattern element_pattern2) {
        this.element_pattern = element_pattern2;
    }

    public static ListRepeatPat make(Pattern element_pattern2) {
        return new ListRepeatPat(element_pattern2);
    }

    public void print(Consumer out) {
        out.write("#<list-repeat-pattern ");
        this.element_pattern.print(out);
        out.write(62);
    }

    public boolean match(Object obj, Object[] vars, int start_vars) {
        int length = LList.listLength(obj, false);
        if (length < 0) {
            return false;
        }
        int var_count = this.element_pattern.varCount();
        int i = var_count;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            vars[start_vars + i] = new Object[length];
        }
        Object[] element_vars = new Object[var_count];
        for (int j = 0; j < length; j++) {
            Pair pair = (Pair) obj;
            if (!this.element_pattern.match(pair.getCar(), element_vars, 0)) {
                return false;
            }
            for (int i2 = 0; i2 < var_count; i2++) {
                vars[start_vars + i2][j] = element_vars[i2];
            }
            obj = pair.getCdr();
        }
        return true;
    }

    public int varCount() {
        return this.element_pattern.varCount();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.element_pattern);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.element_pattern = (Pattern) in.readObject();
    }
}
