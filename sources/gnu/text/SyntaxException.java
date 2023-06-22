package gnu.text;

import java.io.PrintWriter;

public class SyntaxException extends Exception {
    String header;
    public int maxToPrint = 10;
    SourceMessages messages;

    public SyntaxException(SourceMessages messages2) {
        this.messages = messages2;
    }

    public SyntaxException(String header2, SourceMessages messages2) {
        this.header = header2;
        this.messages = messages2;
    }

    public final String getHeader() {
        return this.header;
    }

    public final void setHeader(String header2) {
        this.header = header2;
    }

    public SourceMessages getMessages() {
        return this.messages;
    }

    public void printAll(PrintWriter out, int max) {
        if (this.header != null) {
            out.println(this.header);
        }
        this.messages.printAll(out, max);
    }

    public void clear() {
        this.messages.clear();
    }

    public String getMessage() {
        StringBuffer buffer = new StringBuffer();
        if (this.header != null) {
            buffer.append(this.header);
        }
        int max = this.maxToPrint;
        for (SourceError err = this.messages.firstError; err != null; err = err.next) {
            max--;
            if (max < 0) {
                break;
            }
            buffer.append(10);
            buffer.append(err);
        }
        return buffer.toString();
    }
}
