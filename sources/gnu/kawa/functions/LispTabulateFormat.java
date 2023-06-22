package gnu.kawa.functions;

import gnu.mapping.OutPort;
import gnu.text.ReportFormat;
import java.io.IOException;
import java.io.Writer;
import java.text.FieldPosition;

/* compiled from: LispFormat */
class LispTabulateFormat extends ReportFormat {
    int colinc;
    int colnum;
    int padChar;
    boolean relative;

    public LispTabulateFormat(int colnum2, int colinc2, int padChar2, boolean relative2) {
        this.colnum = colnum2;
        this.colinc = colinc2;
        this.relative = relative2;
        this.padChar = padChar2;
    }

    public int format(Object[] args, int start, Writer dst, FieldPosition fpos) throws IOException {
        int spaces;
        int colnum2 = getParam(this.colnum, 1, args, start);
        if (this.colnum == -1610612736) {
            start++;
        }
        int colinc2 = getParam(this.colinc, 1, args, start);
        if (this.colinc == -1610612736) {
            start++;
        }
        char padChar2 = getParam(this.padChar, ' ', args, start);
        if (this.padChar == -1610612736) {
            start++;
        }
        int column = -1;
        if (dst instanceof OutPort) {
            column = ((OutPort) dst).getColumnNumber();
        }
        if (column >= 0) {
            spaces = !this.relative ? column < colnum2 ? colnum2 - column : colinc2 <= 0 ? 0 : colinc2 - ((column - colnum2) % colinc2) : (colnum2 + colinc2) - ((column + colnum2) % colinc2);
        } else {
            spaces = this.relative ? colnum2 : 2;
        }
        while (true) {
            spaces--;
            if (spaces < 0) {
                return start;
            }
            dst.write(padChar2);
        }
    }
}
