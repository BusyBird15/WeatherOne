package gnu.kawa.functions;

import gnu.lists.AbstractFormat;
import gnu.lists.Consumer;
import gnu.mapping.OutPort;
import gnu.text.ReportFormat;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.FieldPosition;
import java.text.ParsePosition;
import kawa.standard.Scheme;

public class ObjectFormat extends ReportFormat {
    private static ObjectFormat plainFormat;
    private static ObjectFormat readableFormat;
    int maxChars;
    boolean readable;

    public static ObjectFormat getInstance(boolean readable2) {
        if (readable2) {
            if (readableFormat == null) {
                readableFormat = new ObjectFormat(true);
            }
            return readableFormat;
        }
        if (plainFormat == null) {
            plainFormat = new ObjectFormat(false);
        }
        return plainFormat;
    }

    public ObjectFormat(boolean readable2) {
        this.readable = readable2;
        this.maxChars = -1073741824;
    }

    public ObjectFormat(boolean readable2, int maxChars2) {
        this.readable = readable2;
        this.maxChars = maxChars2;
    }

    public int format(Object[] args, int start, Writer dst, FieldPosition fpos) throws IOException {
        int maxChars2 = getParam(this.maxChars, -1, args, start);
        if (this.maxChars == -1610612736) {
            start++;
        }
        return format(args, start, dst, maxChars2, this.readable);
    }

    private static void print(Object obj, OutPort out, boolean readable2) {
        boolean saveReadable = out.printReadable;
        AbstractFormat saveFormat = out.objectFormat;
        try {
            out.printReadable = readable2;
            AbstractFormat format = readable2 ? Scheme.writeFormat : Scheme.displayFormat;
            out.objectFormat = format;
            format.writeObject(obj, (Consumer) out);
        } finally {
            out.printReadable = saveReadable;
            out.objectFormat = saveFormat;
        }
    }

    public static boolean format(Object arg, Writer dst, int maxChars2, boolean readable2) throws IOException {
        if (maxChars2 < 0 && (dst instanceof OutPort)) {
            print(arg, (OutPort) dst, readable2);
            return true;
        } else if (maxChars2 >= 0 || !(dst instanceof CharArrayWriter)) {
            CharArrayWriter wr = new CharArrayWriter();
            OutPort oport = new OutPort((Writer) wr);
            print(arg, oport, readable2);
            oport.close();
            int len = wr.size();
            if (maxChars2 < 0 || len <= maxChars2) {
                wr.writeTo(dst);
                return true;
            }
            dst.write(wr.toCharArray(), 0, maxChars2);
            return false;
        } else {
            OutPort oport2 = new OutPort(dst);
            print(arg, oport2, readable2);
            oport2.close();
            return true;
        }
    }

    public static int format(Object[] args, int start, Writer dst, int maxChars2, boolean readable2) throws IOException {
        String arg;
        if (start >= args.length) {
            arg = "#<missing format argument>";
            start--;
            readable2 = false;
            maxChars2 = -1;
        } else {
            arg = args[start];
        }
        format((Object) arg, dst, maxChars2, readable2);
        return start + 1;
    }

    public Object parseObject(String text, ParsePosition status) {
        throw new RuntimeException("ObjectFormat.parseObject - not implemented");
    }
}
