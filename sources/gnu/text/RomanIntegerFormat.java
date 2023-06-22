package gnu.text;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class RomanIntegerFormat extends NumberFormat {
    static final String codes = "IVXLCDM";
    private static RomanIntegerFormat newRoman;
    private static RomanIntegerFormat oldRoman;
    public boolean oldStyle;

    public RomanIntegerFormat(boolean oldStyle2) {
        this.oldStyle = oldStyle2;
    }

    public RomanIntegerFormat() {
    }

    public static RomanIntegerFormat getInstance(boolean oldStyle2) {
        if (oldStyle2) {
            if (oldRoman == null) {
                oldRoman = new RomanIntegerFormat(true);
            }
            return oldRoman;
        }
        if (newRoman == null) {
            newRoman = new RomanIntegerFormat(false);
        }
        return newRoman;
    }

    public static String format(int num, boolean oldStyle2) {
        if (num <= 0 || num >= 4999) {
            return Integer.toString(num);
        }
        StringBuffer sbuf = new StringBuffer(20);
        int unit = 1000;
        for (int power = 3; power >= 0; power--) {
            int digit = num / unit;
            num -= digit * unit;
            if (digit != 0) {
                if (oldStyle2 || !(digit == 4 || digit == 9)) {
                    int rest = digit;
                    if (rest >= 5) {
                        sbuf.append(codes.charAt((power * 2) + 1));
                        rest -= 5;
                    }
                    while (true) {
                        rest--;
                        if (rest < 0) {
                            break;
                        }
                        sbuf.append(codes.charAt(power * 2));
                    }
                } else {
                    sbuf.append(codes.charAt(power * 2));
                    sbuf.append(codes.charAt((power * 2) + ((digit + 1) / 5)));
                }
            }
            unit /= 10;
        }
        return sbuf.toString();
    }

    public static String format(int num) {
        return format(num, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.StringBuffer format(long r12, java.lang.StringBuffer r14, java.text.FieldPosition r15) {
        /*
            r11 = this;
            r6 = 0
            int r6 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
            if (r6 <= 0) goto L_0x0030
            boolean r6 = r11.oldStyle
            if (r6 == 0) goto L_0x002d
            r6 = 4999(0x1387, float:7.005E-42)
        L_0x000c:
            long r6 = (long) r6
            int r6 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
            if (r6 >= 0) goto L_0x0030
            int r6 = (int) r12
            boolean r7 = r11.oldStyle
            java.lang.String r2 = format(r6, r7)
        L_0x0018:
            if (r15 == 0) goto L_0x0044
            r4 = 1
            int r1 = r2.length()
            r0 = r1
        L_0x0021:
            int r0 = r0 + -1
            if (r0 <= 0) goto L_0x0035
            r6 = 10
            long r6 = r6 * r4
            r8 = 9
            long r4 = r6 + r8
            goto L_0x0021
        L_0x002d:
            r6 = 3999(0xf9f, float:5.604E-42)
            goto L_0x000c
        L_0x0030:
            java.lang.String r2 = java.lang.Long.toString(r12)
            goto L_0x0018
        L_0x0035:
            java.lang.StringBuffer r3 = new java.lang.StringBuffer
            r3.<init>(r1)
            java.text.DecimalFormat r6 = new java.text.DecimalFormat
            java.lang.String r7 = "0"
            r6.<init>(r7)
            r6.format(r4, r3, r15)
        L_0x0044:
            r14.append(r2)
            return r14
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.RomanIntegerFormat.format(long, java.lang.StringBuffer, java.text.FieldPosition):java.lang.StringBuffer");
    }

    public StringBuffer format(double num, StringBuffer sbuf, FieldPosition fpos) {
        long inum = (long) num;
        if (((double) inum) == num) {
            return format(inum, sbuf, fpos);
        }
        sbuf.append(Double.toString(num));
        return sbuf;
    }

    public Number parse(String text, ParsePosition status) {
        throw new Error("RomanIntegerFormat.parseObject - not implemented");
    }
}
