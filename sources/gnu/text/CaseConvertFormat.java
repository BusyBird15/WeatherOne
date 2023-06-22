package gnu.text;

import java.io.IOException;
import java.io.Writer;
import java.text.FieldPosition;
import java.text.Format;

public class CaseConvertFormat extends ReportFormat {
    Format baseFormat;
    char code;

    public CaseConvertFormat(Format baseFormat2, char action) {
        this.baseFormat = baseFormat2;
        this.code = action;
    }

    public Format getBaseFormat() {
        return this.baseFormat;
    }

    public void setBaseFormat(Format baseFormat2) {
        this.baseFormat = baseFormat2;
    }

    public int format(Object[] args, int start, Writer dst, FieldPosition fpos) throws IOException {
        char ch;
        StringBuffer sbuf = new StringBuffer(100);
        int result = format(this.baseFormat, args, start, sbuf, fpos);
        int len = sbuf.length();
        char prev = ' ';
        for (int i = 0; i < len; i++) {
            char ch2 = sbuf.charAt(i);
            if (this.code == 'U') {
                ch = Character.toUpperCase(ch2);
            } else if (!(this.code == 'T' && i == 0) && (this.code != 'C' || Character.isLetterOrDigit(prev))) {
                ch = Character.toLowerCase(ch2);
            } else {
                ch = Character.toTitleCase(ch2);
            }
            prev = ch;
            dst.write(ch);
        }
        return result;
    }
}
