package gnu.kawa.functions;

import gnu.lists.FString;
import gnu.mapping.CharArrayOutPort;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure;
import gnu.mapping.ProcedureN;
import gnu.mapping.Values;
import gnu.text.ReportFormat;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.text.FieldPosition;
import java.text.MessageFormat;

public class Format extends ProcedureN {
    public static final Format format = new Format();

    static {
        format.setName("format");
        format.setProperty(Procedure.validateApplyKey, "gnu.kawa.functions.CompileMisc:validateApplyFormat");
    }

    public static void format(Writer dst, Object[] args, int arg_offset) {
        int arg_offset2 = arg_offset + 1;
        Object format2 = args[arg_offset];
        Object[] vals = new Object[(args.length - arg_offset2)];
        System.arraycopy(args, arg_offset2, vals, 0, vals.length);
        formatToWriter(dst, format2, vals);
    }

    public static void formatToWriter(Writer dst, Object format2, Object... vals) {
        if (dst == null) {
            dst = OutPort.outDefault();
        }
        try {
            if (format2 instanceof MessageFormat) {
                dst.write(((MessageFormat) format2).format(vals));
                return;
            }
            if (!(format2 instanceof ReportFormat)) {
                format2 = ParseFormat.parseFormat.apply1(format2);
            }
            ((ReportFormat) format2).format(vals, 0, dst, (FieldPosition) null);
        } catch (IOException ex) {
            throw new RuntimeException("Error in format: " + ex);
        }
    }

    public static void formatToOutputStream(OutputStream dst, Object format2, Object... vals) {
        OutPort port = new OutPort(dst);
        format(port, format2, vals);
        port.closeThis();
    }

    public static String formatToString(int arg_offset, Object... args) {
        CharArrayOutPort port = new CharArrayOutPort();
        format(port, args, arg_offset);
        String str = port.toString();
        port.close();
        return str;
    }

    public static FString formatToFString(char style, Object fmt, Object[] args) {
        ReportFormat rfmt = ParseFormat.asFormat(fmt, style);
        CharArrayOutPort port = new CharArrayOutPort();
        try {
            rfmt.format(args, 0, (Writer) port, (FieldPosition) null);
            char[] chars = port.toCharArray();
            port.close();
            return new FString(chars);
        } catch (IOException ex) {
            throw new RuntimeException("Error in format: " + ex);
        }
    }

    public Object applyN(Object[] args) {
        return format(args);
    }

    public static Object format(Object... args) {
        Object port_arg = args[0];
        if (port_arg == Boolean.TRUE) {
            format(OutPort.outDefault(), args, 1);
            return Values.empty;
        } else if (port_arg == Boolean.FALSE) {
            return formatToString(1, args);
        } else {
            if ((port_arg instanceof MessageFormat) || (port_arg instanceof CharSequence) || (port_arg instanceof ReportFormat)) {
                return formatToString(0, args);
            }
            if (port_arg instanceof Writer) {
                format((Writer) port_arg, args, 1);
                return Values.empty;
            } else if (port_arg instanceof OutputStream) {
                formatToOutputStream((OutputStream) port_arg, args[1], drop2(args));
                return Values.empty;
            } else {
                throw new RuntimeException("bad first argument to format");
            }
        }
    }

    static Object[] drop2(Object[] vals) {
        int xlen = vals.length - 2;
        Object[] xvals = new Object[xlen];
        System.arraycopy(vals, 2, xvals, 0, xlen);
        return xvals;
    }
}
