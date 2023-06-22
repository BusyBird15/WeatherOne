package gnu.xquery.util;

import gnu.kawa.functions.Arithmetic;
import gnu.kawa.functions.NumberCompare;
import gnu.kawa.xml.KNode;
import gnu.kawa.xml.UntypedAtomic;
import gnu.kawa.xml.XDataType;
import gnu.lists.Sequence;
import gnu.lists.TreeList;
import gnu.mapping.Values;
import gnu.xml.TextUtils;

public class MinMax {
    public static Object min(Object arg, NamedCollator collation) {
        return minMax(arg, false, collation);
    }

    public static Object max(Object arg, NamedCollator collation) {
        return minMax(arg, true, collation);
    }

    public static Object minMax(Object arg, boolean returnMax, NamedCollator collation) {
        int code;
        boolean castNeeded;
        int flags = 16;
        if (arg instanceof Values) {
            TreeList tlist = (TreeList) arg;
            int pos = 0;
            if (!returnMax) {
                flags = 4;
            }
            Object cur = tlist.getPosNext(0);
            if (cur == Sequence.eofValue) {
                return Values.empty;
            }
            Object convert = convert(cur);
            while (true) {
                pos = tlist.nextPos(pos);
                Object cur2 = tlist.getPosNext(pos);
                if (cur2 == Sequence.eofValue) {
                    return convert;
                }
                Object cur3 = convert(cur2);
                if ((convert instanceof Number) || (cur3 instanceof Number)) {
                    int code1 = Arithmetic.classifyValue(convert);
                    int code2 = Arithmetic.classifyValue(cur3);
                    int rcode = NumberCompare.compare(convert, code1, cur3, code2, false);
                    if (rcode == -3) {
                        throw new IllegalArgumentException("values cannot be compared");
                    }
                    if (code1 < code2) {
                        code = code2;
                    } else {
                        code = code1;
                    }
                    if (rcode == -2) {
                        convert = NumberValue.NaN;
                        castNeeded = true;
                    } else if (!NumberCompare.checkCompareCode(rcode, flags)) {
                        if (code != code2) {
                            castNeeded = true;
                        } else {
                            castNeeded = false;
                        }
                        convert = cur3;
                    } else {
                        castNeeded = code != code1;
                    }
                    if (castNeeded) {
                        convert = Arithmetic.convert(convert, code);
                    }
                } else if (!Compare.atomicCompare(flags, convert, cur3, collation)) {
                    convert = cur3;
                }
            }
        } else {
            Object arg2 = convert(arg);
            Compare.atomicCompare(16, arg2, arg2, collation);
            return arg2;
        }
    }

    static Object convert(Object arg) {
        Object arg2 = KNode.atomicValue(arg);
        if (arg2 instanceof UntypedAtomic) {
            return (Double) XDataType.doubleType.valueOf(TextUtils.stringValue(arg2));
        }
        return arg2;
    }
}
