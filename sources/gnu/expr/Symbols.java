package gnu.expr;

import gnu.lists.Consumer;
import gnu.mapping.OutPort;
import gnu.mapping.SimpleSymbol;

public class Symbols {
    private static int gensym_counter;

    private Symbols() {
    }

    static synchronized int generateInt() {
        int i;
        synchronized (Symbols.class) {
            i = gensym_counter + 1;
            gensym_counter = i;
        }
        return i;
    }

    public static final SimpleSymbol gentemp() {
        return SimpleSymbol.valueOf("GS." + Integer.toString(generateInt()));
    }

    public static String make(String name) {
        return name.intern();
    }

    public static final String intern(String name) {
        return make(name);
    }

    public static void print(String name, Consumer out) {
        if ((out instanceof OutPort) && ((OutPort) out).printReadable) {
            int len = name.length();
            for (int i = 0; i < len; i++) {
                char ch = name.charAt(i);
                if (!(Character.isLowerCase(ch) || ch == '!' || ch == '$' || ch == '%' || ch == '&' || ch == '*' || ch == '/' || ch == ':' || ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '~' || ch == '_' || ch == '^' || (((ch == '+' || ch == '-') && (i > 0 || len == 1)) || ((Character.isDigit(ch) && i > 0) || (ch == '.' && (i == 0 || name.charAt(i - 1) == '.')))))) {
                    out.write(92);
                }
                out.write((int) ch);
            }
            return;
        }
        out.write(name);
    }
}
