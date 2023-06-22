package kawa;

import java.io.Writer;
import javax.swing.text.AttributeSet;

/* compiled from: ReplPaneOutPort */
class TextPaneWriter extends Writer {
    ReplDocument document;
    String str = "";
    AttributeSet style;

    public TextPaneWriter(ReplDocument document2, AttributeSet style2) {
        this.document = document2;
        this.style = style2;
    }

    public synchronized void write(int x) {
        this.str += ((char) x);
        if (x == 10) {
            flush();
        }
    }

    public void write(String str2) {
        this.document.write(str2, this.style);
    }

    public synchronized void write(char[] data, int off, int len) {
        flush();
        if (len != 0) {
            write(new String(data, off, len));
        }
    }

    public synchronized void flush() {
        String s = this.str;
        if (!s.equals("")) {
            this.str = "";
            write(s);
        }
    }

    public void close() {
        flush();
    }
}
