package kawa.lang;

import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.text.ReportFormat;

public class ListPat extends Pattern {
    Object default_value;
    int max_length;
    int min_length;

    public ListPat(int len) {
        this.min_length = len;
        this.max_length = len;
    }

    public ListPat(int min, int max) {
        this.min_length = min;
        this.max_length = max;
    }

    public ListPat(int min, int max, Object default_val) {
        this.min_length = min;
        this.max_length = max;
        this.default_value = default_val;
    }

    public static boolean match(int min, int max, Object default_val, Object obj, Object[] vars, int start_vars) {
        int i = 0;
        while (true) {
            if (i >= max) {
                break;
            } else if (obj instanceof Pair) {
                Pair p = (Pair) obj;
                vars[start_vars + i] = p.getCar();
                obj = p.getCdr();
                i++;
            } else if (i < min) {
                return false;
            }
        }
        if (i == max && obj != LList.Empty) {
            return false;
        }
        while (i < max) {
            vars[start_vars + i] = default_val;
            i++;
        }
        return true;
    }

    public static Object[] match(int min, int max, Object default_val, Object obj) {
        Object[] vars = new Object[max];
        if (match(min, max, default_val, obj, vars, 0)) {
            return vars;
        }
        return null;
    }

    public boolean match(Object obj, Object[] vars, int start_vars) {
        return match(this.min_length, this.max_length, this.default_value, obj, vars, start_vars);
    }

    public int varCount() {
        return this.max_length;
    }

    public void print(Consumer out) {
        out.write("#<list-pattern min:");
        out.write(Integer.toString(this.min_length));
        out.write(" max:");
        out.write(Integer.toString(this.max_length));
        out.write(" default:");
        ReportFormat.print(this.default_value, out);
        out.write(62);
    }
}
