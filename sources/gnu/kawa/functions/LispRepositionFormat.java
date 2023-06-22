package gnu.kawa.functions;

import gnu.text.ReportFormat;
import java.io.IOException;
import java.io.Writer;
import java.text.FieldPosition;

/* compiled from: LispFormat */
class LispRepositionFormat extends ReportFormat {
    boolean absolute;
    boolean backwards;
    int count;

    public LispRepositionFormat(int count2, boolean backwards2, boolean absolute2) {
        this.count = count2;
        this.backwards = backwards2;
        this.absolute = absolute2;
    }

    public int format(Object[] args, int start, Writer dst, FieldPosition fpos) throws IOException {
        int count2 = getParam(this.count, this.absolute ? 0 : 1, args, start);
        if (!this.absolute) {
            if (this.backwards) {
                count2 = -count2;
            }
            count2 += start;
        }
        if (count2 < 0) {
            return 0;
        }
        return count2 > args.length ? args.length : count2;
    }
}
