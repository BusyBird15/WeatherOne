package gnu.xml;

import gnu.kawa.xml.KNode;
import gnu.lists.Consumer;
import gnu.lists.TreeList;
import gnu.mapping.Values;
import gnu.math.DFloNum;
import java.math.BigDecimal;

public class TextUtils {
    public static String asString(Object node) {
        if (node == Values.empty || node == null) {
            return "";
        }
        if (node instanceof Values) {
            throw new ClassCastException();
        }
        StringBuffer sbuf = new StringBuffer(100);
        stringValue(node, sbuf);
        return sbuf.toString();
    }

    public static String stringValue(Object node) {
        StringBuffer sbuf = new StringBuffer(100);
        if (node instanceof Values) {
            TreeList tlist = (TreeList) node;
            int index = 0;
            while (true) {
                int kind = tlist.getNextKind(index);
                if (kind == 0) {
                    break;
                }
                if (kind == 32) {
                    stringValue(tlist.getPosNext(index), sbuf);
                } else {
                    tlist.stringValue(tlist.posToDataIndex(index), sbuf);
                }
                index = tlist.nextPos(index);
            }
        } else {
            stringValue(node, sbuf);
        }
        return sbuf.toString();
    }

    public static void stringValue(Object node, StringBuffer sbuf) {
        if (node instanceof KNode) {
            KNode pos = (KNode) node;
            NodeTree tlist = (NodeTree) pos.sequence;
            tlist.stringValue(tlist.posToDataIndex(pos.ipos), sbuf);
            return;
        }
        if (node instanceof BigDecimal) {
            node = XMLPrinter.formatDecimal((BigDecimal) node);
        } else if ((node instanceof Double) || (node instanceof DFloNum)) {
            node = XMLPrinter.formatDouble(((Number) node).doubleValue());
        } else if (node instanceof Float) {
            node = XMLPrinter.formatFloat(((Number) node).floatValue());
        }
        if (node != null && node != Values.empty) {
            sbuf.append(node);
        }
    }

    public static void textValue(Object arg, Consumer out) {
        String str;
        if (arg == null) {
            return;
        }
        if (!(arg instanceof Values) || !((Values) arg).isEmpty()) {
            if (arg instanceof String) {
                str = (String) arg;
            } else {
                StringBuffer sbuf = new StringBuffer();
                if (arg instanceof Values) {
                    Object[] vals = ((Values) arg).getValues();
                    for (int i = 0; i < vals.length; i++) {
                        if (i > 0) {
                            sbuf.append(' ');
                        }
                        stringValue(vals[i], sbuf);
                    }
                } else {
                    stringValue(arg, sbuf);
                }
                str = sbuf.toString();
            }
            out.write(str);
        }
    }

    public static String replaceWhitespace(String str, boolean collapse) {
        int prevSpace;
        int prevSpace2;
        int isSpace;
        StringBuilder sbuf = null;
        int len = str.length();
        if (collapse) {
            prevSpace = 1;
        } else {
            prevSpace = 0;
        }
        int i = 0;
        while (i < len) {
            int i2 = i + 1;
            char ch = str.charAt(i);
            if (ch == ' ') {
                isSpace = 1;
            } else {
                isSpace = (ch == 9 || ch == 13 || ch == 10) ? 2 : 0;
            }
            if (sbuf == null && (isSpace == 2 || ((isSpace == 1 && prevSpace2 > 0 && collapse) || (isSpace == 1 && i2 == len && collapse)))) {
                sbuf = new StringBuilder();
                int k = prevSpace2 > 0 ? i2 - 2 : i2 - 1;
                for (int j = 0; j < k; j++) {
                    sbuf.append(str.charAt(j));
                }
                ch = ' ';
            }
            if (collapse) {
                if (prevSpace2 > 0 && isSpace == 0) {
                    if (sbuf != null && sbuf.length() > 0) {
                        sbuf.append(' ');
                    }
                    prevSpace2 = 0;
                } else if (isSpace == 2 || (isSpace == 1 && prevSpace2 > 0)) {
                    prevSpace2 = 2;
                } else if (isSpace > 0) {
                    prevSpace2 = 1;
                } else {
                    prevSpace2 = 0;
                }
                if (prevSpace2 > 0) {
                    i = i2;
                }
            }
            if (sbuf != null) {
                sbuf.append(ch);
            }
            i = i2;
        }
        if (sbuf != null) {
            return sbuf.toString();
        }
        return str;
    }
}
