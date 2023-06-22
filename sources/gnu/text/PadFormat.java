package gnu.text;

import java.io.IOException;
import java.io.Writer;
import java.text.FieldPosition;
import java.text.Format;
import java.text.MessageFormat;

public class PadFormat extends ReportFormat {
    Format fmt;
    int minWidth;
    char padChar;
    int where;

    public PadFormat(Format fmt2, int minWidth2, char padChar2, int where2) {
        this.fmt = fmt2;
        this.minWidth = minWidth2;
        this.padChar = padChar2;
        this.where = where2;
    }

    public PadFormat(Format fmt2, int minWidth2) {
        this(fmt2, minWidth2, ' ', 100);
    }

    public int format(Object[] args, int start, Writer dst, FieldPosition fpos) throws IOException {
        return format(this.fmt, args, start, dst, this.padChar, this.minWidth, 1, 0, this.where, fpos);
    }

    public static int padNeeded(int actualWidth, int minWidth2, int colInc, int minPad) {
        int total = actualWidth + minPad;
        if (colInc <= 1) {
            colInc = minWidth2 - total;
        }
        while (total < minWidth2) {
            total += colInc;
        }
        return total - actualWidth;
    }

    public static int format(Format fmt2, Object[] args, int start, Writer dst, char padChar2, int minWidth2, int colInc, int minPad, int where2, FieldPosition fpos) throws IOException {
        int start2;
        StringBuffer tbuf = new StringBuffer(200);
        if (fmt2 instanceof ReportFormat) {
            start2 = ((ReportFormat) fmt2).format(args, start, tbuf, fpos);
        } else if (fmt2 instanceof MessageFormat) {
            fmt2.format(args, tbuf, fpos);
            start2 = args.length;
        } else {
            fmt2.format(args[start], tbuf, fpos);
            start2 = start + 1;
        }
        int len = tbuf.length();
        int pad = padNeeded(len, minWidth2, colInc, minPad);
        int prefix = 0;
        String text = tbuf.toString();
        if (pad > 0) {
            if (where2 == -1) {
                if (len > 0) {
                    char ch = text.charAt(0);
                    if (ch == '-' || ch == '+') {
                        prefix = 0 + 1;
                        dst.write(ch);
                    }
                    if (len - prefix > 2 && text.charAt(prefix) == '0') {
                        dst.write(48);
                        prefix++;
                        char ch2 = text.charAt(prefix);
                        if (ch2 == 'x' || ch2 == 'X') {
                            prefix++;
                            dst.write(ch2);
                        }
                    }
                    if (prefix > 0) {
                        text = text.substring(prefix);
                    }
                }
                where2 = 0;
            }
            int padAfter = (pad * where2) / 100;
            int padBefore = pad - padAfter;
            while (true) {
                padBefore--;
                if (padBefore < 0) {
                    break;
                }
                dst.write(padChar2);
            }
            dst.write(text);
            while (true) {
                padAfter--;
                if (padAfter < 0) {
                    break;
                }
                dst.write(padChar2);
            }
        } else {
            dst.write(text);
        }
        return start2;
    }
}
