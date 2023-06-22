package gnu.mapping;

import gnu.lists.AbstractFormat;
import gnu.lists.Consumable;
import gnu.lists.Consumer;
import gnu.lists.PrintConsumer;
import gnu.text.Path;
import gnu.text.PrettyWriter;
import gnu.text.Printable;
import gnu.text.WriterManager;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.NumberFormat;

public class OutPort extends PrintConsumer implements Printable {
    private static OutPort errInitial = new OutPort(new LogWriter(new OutputStreamWriter(System.err)), true, true, Path.valueOf("/dev/stderr"));
    public static final ThreadLocation errLocation = new ThreadLocation("err-default");
    static Writer logFile;
    static OutPort outInitial = new OutPort(new LogWriter(new BufferedWriter(new OutputStreamWriter(System.out))), true, true, Path.valueOf("/dev/stdout"));
    public static final ThreadLocation outLocation = new ThreadLocation("out-default");
    private Writer base;
    protected PrettyWriter bout;
    NumberFormat numberFormat;
    public AbstractFormat objectFormat;
    Path path;
    public boolean printReadable;
    protected Object unregisterRef;

    protected OutPort(Writer base2, PrettyWriter out, boolean autoflush) {
        super((Writer) out, autoflush);
        this.bout = out;
        this.base = base2;
        if (closeOnExit()) {
            this.unregisterRef = WriterManager.instance.register(out);
        }
    }

    protected OutPort(OutPort out, boolean autoflush) {
        this((Writer) out, out.bout, autoflush);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    protected OutPort(Writer out, boolean autoflush) {
        this(out, out instanceof OutPort ? ((OutPort) out).bout : new PrettyWriter(out, true), autoflush);
    }

    public OutPort(Writer base2, boolean printPretty, boolean autoflush) {
        this(base2, new PrettyWriter(base2, printPretty), autoflush);
    }

    public OutPort(Writer base2, boolean printPretty, boolean autoflush, Path path2) {
        this(base2, new PrettyWriter(base2, printPretty), autoflush);
        this.path = path2;
    }

    public OutPort(OutputStream out) {
        this(out, (Path) null);
    }

    public OutPort(OutputStream out, Path path2) {
        this((Writer) new OutputStreamWriter(out), true, path2);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public OutPort(Writer out) {
        this(out, out instanceof OutPort ? ((OutPort) out).bout : new PrettyWriter(out, false), false);
    }

    public OutPort(Writer base2, Path path2) {
        this(base2, false, false);
        this.path = path2;
    }

    public OutPort(Writer base2, boolean autoflush, Path path2) {
        this(base2, false, autoflush);
        this.path = path2;
    }

    static {
        outLocation.setGlobal(outInitial);
        errLocation.setGlobal(errInitial);
    }

    public static OutPort outDefault() {
        return (OutPort) outLocation.get();
    }

    public static void setOutDefault(OutPort o) {
        outLocation.set(o);
    }

    public static OutPort errDefault() {
        return (OutPort) errLocation.get();
    }

    public static void setErrDefault(OutPort e) {
        errLocation.set(e);
    }

    public static OutPort openFile(Object fname) throws IOException {
        Writer wr;
        Object conv = Environment.user().get((Object) "port-char-encoding");
        Path path2 = Path.valueOf(fname);
        OutputStream strm = new BufferedOutputStream(path2.openOutputStream());
        if (conv == null || conv == Boolean.TRUE) {
            wr = new OutputStreamWriter(strm);
        } else {
            if (conv == Boolean.FALSE) {
                conv = "8859_1";
            }
            wr = new OutputStreamWriter(strm, conv.toString());
        }
        return new OutPort(wr, path2);
    }

    public void echo(char[] buf, int off, int len) throws IOException {
        if (this.base instanceof LogWriter) {
            ((LogWriter) this.base).echo(buf, off, len);
        }
    }

    public static void closeLogFile() throws IOException {
        if (logFile != null) {
            logFile.close();
            logFile = null;
        }
        if (outInitial.base instanceof LogWriter) {
            ((LogWriter) outInitial.base).setLogFile((Writer) null);
        }
        if (errInitial.base instanceof LogWriter) {
            ((LogWriter) errInitial.base).setLogFile((Writer) null);
        }
    }

    public static void setLogFile(String name) throws IOException {
        if (logFile != null) {
            closeLogFile();
        }
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(name)));
        if (outInitial.base instanceof LogWriter) {
            ((LogWriter) outInitial.base).setLogFile(logFile);
        }
        if (errInitial.base instanceof LogWriter) {
            ((LogWriter) errInitial.base).setLogFile(logFile);
        }
    }

    protected static final boolean isWordChar(char ch) {
        return Character.isJavaIdentifierPart(ch) || ch == '-' || ch == '+';
    }

    public void print(int v) {
        if (this.numberFormat == null) {
            super.print(v);
        } else {
            print(this.numberFormat.format((long) v));
        }
    }

    public void print(long v) {
        if (this.numberFormat == null) {
            super.print(v);
        } else {
            print(this.numberFormat.format(v));
        }
    }

    public void print(double v) {
        if (this.numberFormat == null) {
            super.print(v);
        } else {
            print(this.numberFormat.format(v));
        }
    }

    public void print(float v) {
        if (this.numberFormat == null) {
            super.print(v);
        } else {
            print(this.numberFormat.format((double) v));
        }
    }

    public void print(boolean v) {
        if (this.objectFormat == null) {
            super.print(v);
        } else {
            this.objectFormat.writeBoolean(v, this);
        }
    }

    public void print(String v) {
        if (v == null) {
            v = "(null)";
        }
        write(v);
    }

    public void print(Object v) {
        if (this.objectFormat != null) {
            this.objectFormat.writeObject(v, (PrintConsumer) this);
        } else if (v instanceof Consumable) {
            ((Consumable) v).consume(this);
        } else {
            if (v == null) {
                v = "null";
            }
            super.print(v);
        }
    }

    public void print(Consumer out) {
        out.write("#<output-port");
        if (this.path != null) {
            out.write(32);
            out.write(this.path.toString());
        }
        out.write(62);
    }

    public void startElement(Object type) {
        if (this.objectFormat != null) {
            this.objectFormat.startElement(type, this);
            return;
        }
        print('(');
        print(type);
    }

    public void endElement() {
        if (this.objectFormat != null) {
            this.objectFormat.endElement(this);
        } else {
            print(')');
        }
    }

    public void startAttribute(Object attrType) {
        if (this.objectFormat != null) {
            this.objectFormat.startAttribute(attrType, this);
            return;
        }
        print(' ');
        print(attrType);
        print(": ");
    }

    public void endAttribute() {
        if (this.objectFormat != null) {
            this.objectFormat.endAttribute(this);
        } else {
            print(' ');
        }
    }

    public void writeWordEnd() {
        this.bout.writeWordEnd();
    }

    public void writeWordStart() {
        this.bout.writeWordStart();
    }

    public void freshLine() {
        if (this.bout.getColumnNumber() != 0) {
            println();
        }
    }

    public int getColumnNumber() {
        return this.bout.getColumnNumber();
    }

    public void setColumnNumber(int column) {
        this.bout.setColumnNumber(column);
    }

    public void clearBuffer() {
        this.bout.clearBuffer();
    }

    public void closeThis() {
        try {
            if (!(this.base instanceof OutPort) || ((OutPort) this.base).bout != this.bout) {
                this.bout.closeThis();
            }
        } catch (IOException e) {
            setError();
        }
        WriterManager.instance.unregister(this.unregisterRef);
    }

    public void close() {
        try {
            if (!(this.base instanceof OutPort) || ((OutPort) this.base).bout != this.bout) {
                this.out.close();
                WriterManager.instance.unregister(this.unregisterRef);
            }
            this.base.close();
            WriterManager.instance.unregister(this.unregisterRef);
        } catch (IOException e) {
            setError();
        }
    }

    /* access modifiers changed from: protected */
    public boolean closeOnExit() {
        return true;
    }

    public static void runCleanups() {
        WriterManager.instance.run();
    }

    public void startLogicalBlock(String prefix, boolean perLine, String suffix) {
        this.bout.startLogicalBlock(prefix, perLine, suffix);
    }

    public void startLogicalBlock(String prefix, String suffix, int indent) {
        this.bout.startLogicalBlock(prefix, false, suffix);
        PrettyWriter prettyWriter = this.bout;
        if (prefix != null) {
            indent -= prefix.length();
        }
        prettyWriter.addIndentation(indent, false);
    }

    public void endLogicalBlock(String suffix) {
        this.bout.endLogicalBlock(suffix);
    }

    public void writeBreak(int kind) {
        this.bout.writeBreak(kind);
    }

    public void writeSpaceLinear() {
        write(32);
        writeBreak(78);
    }

    public void writeBreakLinear() {
        writeBreak(78);
    }

    public void writeSpaceFill() {
        write(32);
        writeBreak(70);
    }

    public void writeBreakFill() {
        writeBreak(70);
    }

    public void setIndentation(int amount, boolean current) {
        this.bout.addIndentation(amount, current);
    }
}
