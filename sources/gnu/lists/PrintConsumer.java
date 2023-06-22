package gnu.lists;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class PrintConsumer extends PrintWriter implements Appendable, Consumer {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PrintConsumer(Consumer out, boolean autoFlush) {
        super(out instanceof Writer ? (Writer) out : new ConsumerWriter(out), autoFlush);
    }

    public PrintConsumer(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintConsumer(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public PrintConsumer(Writer out) {
        super(out);
    }

    /* access modifiers changed from: protected */
    public void startNumber() {
    }

    /* access modifiers changed from: protected */
    public void endNumber() {
    }

    public PrintConsumer append(char c) {
        print(c);
        return this;
    }

    public PrintConsumer append(CharSequence csq) {
        if (csq == null) {
            csq = "null";
        }
        append(csq, 0, csq.length());
        return this;
    }

    public PrintConsumer append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        for (int i = start; i < end; i++) {
            append(csq.charAt(i));
        }
        return this;
    }

    public void write(CharSequence csq, int start, int end) {
        if (csq instanceof String) {
            write((String) csq, start, end);
            return;
        }
        for (int i = start; i < end; i++) {
            write((int) csq.charAt(i));
        }
    }

    public void writeBoolean(boolean v) {
        print(v);
    }

    public void writeFloat(float v) {
        startNumber();
        print(v);
        endNumber();
    }

    public void writeDouble(double v) {
        startNumber();
        print(v);
        endNumber();
    }

    public void writeInt(int v) {
        startNumber();
        print(v);
        endNumber();
    }

    public void writeLong(long v) {
        startNumber();
        print(v);
        endNumber();
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startElement(Object type) {
    }

    public void endElement() {
    }

    public void startAttribute(Object attrType) {
    }

    public void endAttribute() {
    }

    public void writeObject(Object v) {
        print(v);
    }

    public boolean ignoring() {
        return false;
    }
}
