package gnu.kawa.functions;

import gnu.text.ReportFormat;
import java.io.IOException;
import java.io.Writer;
import java.text.FieldPosition;
import java.text.Format;

/* compiled from: LispFormat */
class LispIterationFormat extends ReportFormat {
    boolean atLeastOnce;
    Format body;
    int maxIterations;
    boolean seenAt;
    boolean seenColon;

    LispIterationFormat() {
    }

    public static int format(Format body2, int maxIterations2, Object[] args, int start, Writer dst, boolean seenColon2, boolean atLeastOnce2) throws IOException {
        int i = 0;
        while (true) {
            if (i == maxIterations2 && maxIterations2 != -1) {
                return start;
            }
            if (start == args.length && (i > 0 || !atLeastOnce2)) {
                return start;
            }
            if (seenColon2) {
                Object[] curArr = LispFormat.asArray(args[start]);
                if (curArr == null) {
                }
                start++;
                if (ReportFormat.resultCode(ReportFormat.format(body2, curArr, 0, dst, (FieldPosition) null)) == 242) {
                    return start;
                }
            } else {
                start = ReportFormat.format(body2, args, start, dst, (FieldPosition) null);
                if (start < 0) {
                    return ReportFormat.nextArg(start);
                }
            }
            i++;
        }
    }

    public int format(Object[] args, int start, Writer dst, FieldPosition fpos) throws IOException {
        int maxIterations2 = getParam(this.maxIterations, -1, args, start);
        if (this.maxIterations == -1610612736) {
            start++;
        }
        Format body2 = this.body;
        if (body2 == null) {
            int start2 = start + 1;
            Format body3 = args[start];
            if (body3 instanceof Format) {
                body2 = body3;
                start = start2;
            } else {
                try {
                    body2 = new LispFormat(body3.toString());
                    start = start2;
                } catch (Exception e) {
                    print(dst, "<invalid argument for \"~{~}\" format>");
                    int i = start2;
                    return args.length;
                }
            }
        }
        if (this.seenAt) {
            return format(body2, maxIterations2, args, start, dst, this.seenColon, this.atLeastOnce);
        }
        Object arg = args[start];
        Object[] curArgs = LispFormat.asArray(arg);
        if (curArgs == null) {
            dst.write("{" + arg + "}".toString());
        } else {
            format(body2, maxIterations2, curArgs, 0, dst, this.seenColon, this.atLeastOnce);
        }
        return start + 1;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("LispIterationFormat[");
        sbuf.append(this.body);
        sbuf.append("]");
        return sbuf.toString();
    }
}
