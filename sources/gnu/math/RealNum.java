package gnu.math;

import com.google.appinventor.components.runtime.Component;
import java.math.BigDecimal;

public abstract class RealNum extends Complex implements Comparable {
    public abstract Numeric add(Object obj, int i);

    public abstract Numeric div(Object obj);

    public abstract boolean isNegative();

    public abstract Numeric mul(Object obj);

    public abstract int sign();

    public final RealNum re() {
        return this;
    }

    public final RealNum im() {
        return IntNum.zero();
    }

    public static RealNum asRealNumOrNull(Object value) {
        if (value instanceof RealNum) {
            return (RealNum) value;
        }
        if ((value instanceof Float) || (value instanceof Double)) {
            return new DFloNum(((Number) value).doubleValue());
        }
        return RatNum.asRatNumOrNull(value);
    }

    public RealNum max(RealNum x) {
        RealNum result;
        boolean exact = isExact() && x.isExact();
        if (grt(x)) {
            result = this;
        } else {
            result = x;
        }
        if (exact || !result.isExact()) {
            return result;
        }
        return new DFloNum(result.doubleValue());
    }

    public RealNum min(RealNum x) {
        RealNum result;
        boolean exact = isExact() && x.isExact();
        if (grt(x)) {
            result = x;
        } else {
            result = this;
        }
        if (exact || !result.isExact()) {
            return result;
        }
        return new DFloNum(result.doubleValue());
    }

    public static RealNum add(RealNum x, RealNum y, int k) {
        return (RealNum) x.add(y, k);
    }

    public static RealNum times(RealNum x, RealNum y) {
        return (RealNum) x.mul(y);
    }

    public static RealNum divide(RealNum x, RealNum y) {
        return (RealNum) x.div(y);
    }

    public Numeric abs() {
        return isNegative() ? neg() : this;
    }

    public RealNum rneg() {
        return (RealNum) neg();
    }

    public boolean isZero() {
        return sign() == 0;
    }

    public RatNum toExact() {
        return DFloNum.toExact(doubleValue());
    }

    public RealNum toInexact() {
        if (isExact()) {
            return new DFloNum(doubleValue());
        }
        return this;
    }

    public static double toInt(double d, int rounding_mode) {
        switch (rounding_mode) {
            case 1:
                return Math.floor(d);
            case 2:
                return Math.ceil(d);
            case 3:
                return d < 0.0d ? Math.ceil(d) : Math.floor(d);
            case 4:
                return Math.rint(d);
            default:
                return d;
        }
    }

    public RealNum toInt(int rounding_mode) {
        return new DFloNum(toInt(doubleValue(), rounding_mode));
    }

    public IntNum toExactInt(int rounding_mode) {
        return toExactInt(doubleValue(), rounding_mode);
    }

    public static IntNum toExactInt(double value, int rounding_mode) {
        return toExactInt(toInt(value, rounding_mode));
    }

    public static IntNum toExactInt(double value) {
        long bits;
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            throw new ArithmeticException("cannot convert " + value + " to exact integer");
        }
        long bits2 = Double.doubleToLongBits(value);
        boolean neg = bits2 < 0;
        int exp = ((int) (bits2 >> 52)) & 2047;
        long bits3 = bits2 & 4503599627370495L;
        if (exp == 0) {
            bits = bits3 << 1;
        } else {
            bits = bits3 | 4503599627370496L;
        }
        if (exp <= 1075) {
            int rshift = 1075 - exp;
            if (rshift > 53) {
                return IntNum.zero();
            }
            long bits4 = bits >> rshift;
            return IntNum.make(neg ? -bits4 : bits4);
        }
        return IntNum.shift(IntNum.make(neg ? -bits : bits), exp - 1075);
    }

    public Complex exp() {
        return new DFloNum(Math.exp(doubleValue()));
    }

    public Complex log() {
        double x = doubleValue();
        if (x < 0.0d) {
            return DComplex.log(x, 0.0d);
        }
        return new DFloNum(Math.log(x));
    }

    public final Complex sin() {
        return new DFloNum(Math.sin(doubleValue()));
    }

    public final Complex sqrt() {
        double d = doubleValue();
        if (d >= 0.0d) {
            return new DFloNum(Math.sqrt(d));
        }
        return DComplex.sqrt(d, 0.0d);
    }

    public static IntNum toScaledInt(double f, int k) {
        return toScaledInt(DFloNum.toExact(f), k);
    }

    public static IntNum toScaledInt(RatNum r, int k) {
        if (k != 0) {
            IntNum power = IntNum.power(IntNum.ten(), k < 0 ? -k : k);
            IntNum num = r.numerator();
            IntNum den = r.denominator();
            if (k >= 0) {
                num = IntNum.times(num, power);
            } else {
                den = IntNum.times(den, power);
            }
            r = RatNum.make(num, den);
        }
        return r.toExactInt(4);
    }

    public IntNum toScaledInt(int k) {
        return toScaledInt(toExact(), k);
    }

    public int compareTo(Object o) {
        return compare(o);
    }

    public BigDecimal asBigDecimal() {
        return new BigDecimal(doubleValue());
    }

    public static String toStringScientific(float d) {
        return toStringScientific(Float.toString(d));
    }

    public static String toStringScientific(double d) {
        return toStringScientific(Double.toString(d));
    }

    public static String toStringScientific(String dstr) {
        if (dstr.indexOf(69) >= 0) {
            return dstr;
        }
        int len = dstr.length();
        char ch = dstr.charAt(len - 1);
        if (ch == 'y' || ch == 'N') {
            return dstr;
        }
        StringBuffer sbuf = new StringBuffer(len + 10);
        int exp = toStringScientific(dstr, sbuf);
        sbuf.append('E');
        sbuf.append(exp);
        return sbuf.toString();
    }

    public static int toStringScientific(String dstr, StringBuffer sbuf) {
        int pos;
        int exp;
        char ch;
        int pos2;
        char ch2;
        boolean neg = dstr.charAt(0) == '-';
        if (neg) {
            sbuf.append('-');
        }
        int pos3 = neg ? 1 : 0;
        int len = dstr.length();
        if (dstr.charAt(pos3) == '0') {
            int start = pos3;
            int pos4 = pos3;
            while (true) {
                if (pos4 == len) {
                    sbuf.append(Component.TYPEFACE_DEFAULT);
                    exp = 0;
                    int i = pos4;
                    break;
                }
                pos2 = pos4 + 1;
                ch2 = dstr.charAt(pos4);
                if (ch2 < '0' || ch2 > '9' || (ch2 == '0' && pos2 != len)) {
                    pos4 = pos2;
                }
            }
            sbuf.append(ch2);
            sbuf.append('.');
            exp = ch2 == '0' ? 0 : (start - pos2) + 2;
            if (pos2 == len) {
                sbuf.append('0');
            } else {
                pos = pos2;
                while (pos < len) {
                    sbuf.append(dstr.charAt(pos));
                    pos++;
                }
            }
        } else {
            exp = ((len - (neg ? 2 : 1)) - len) + dstr.indexOf(46);
            int pos5 = pos3 + 1;
            sbuf.append(dstr.charAt(pos3));
            sbuf.append('.');
            while (pos < len) {
                int pos6 = pos + 1;
                char ch3 = dstr.charAt(pos);
                if (ch3 != '.') {
                    sbuf.append(ch3);
                }
                pos5 = pos6;
            }
        }
        int pos7 = sbuf.length();
        int slen = -1;
        while (true) {
            pos7--;
            ch = sbuf.charAt(pos7);
            if (ch != '0') {
                break;
            }
            slen = pos7;
        }
        if (ch == '.') {
            slen = pos7 + 2;
        }
        if (slen >= 0) {
            sbuf.setLength(slen);
        }
        return exp;
    }

    public static String toStringDecimal(String dstr) {
        boolean neg;
        boolean z;
        int indexE = dstr.indexOf(69);
        if (indexE < 0) {
            return dstr;
        }
        int len = dstr.length();
        char ch = dstr.charAt(len - 1);
        if (ch == 'y' || ch == 'N') {
            return dstr;
        }
        StringBuffer sbuf = new StringBuffer(len + 10);
        if (dstr.charAt(0) == '-') {
            neg = true;
        } else {
            neg = false;
        }
        if (dstr.charAt(indexE + 1) != '-') {
            throw new Error("not implemented: toStringDecimal given non-negative exponent: " + dstr);
        }
        int exp = 0;
        for (int pos = indexE + 2; pos < len; pos++) {
            exp = (exp * 10) + (dstr.charAt(pos) - '0');
        }
        if (neg) {
            sbuf.append('-');
        }
        sbuf.append("0.");
        while (true) {
            exp--;
            if (exp <= 0) {
                break;
            }
            sbuf.append('0');
        }
        int pos2 = 0;
        while (true) {
            int pos3 = pos2 + 1;
            char ch2 = dstr.charAt(pos2);
            if (ch2 == 'E') {
                return sbuf.toString();
            }
            if (ch2 != '-') {
                z = true;
            } else {
                z = false;
            }
            if ((!(ch2 != '.') || !z) || (ch2 == '0' && pos3 >= indexE)) {
                pos2 = pos3;
            } else {
                sbuf.append(ch2);
                pos2 = pos3;
            }
        }
    }
}
