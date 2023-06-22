package gnu.text;

import java.io.PrintStream;
import java.io.PrintWriter;

public class SourceError implements SourceLocator {
    public String code;
    public int column;
    public Throwable fakeException;
    public String filename;
    public int line;
    public String message;
    public SourceError next;
    public char severity;

    public SourceError(char severity2, String filename2, int line2, int column2, String message2) {
        this.severity = severity2;
        this.filename = filename2;
        this.line = line2;
        this.column = column2;
        this.message = message2;
    }

    public SourceError(char severity2, SourceLocator location, String message2) {
        this(severity2, location.getFileName(), location.getLineNumber(), location.getColumnNumber(), message2);
    }

    public SourceError(LineBufferedReader port, char severity2, String message2) {
        this(severity2, port.getName(), port.getLineNumber() + 1, port.getColumnNumber(), message2);
        if (this.column >= 0) {
            this.column++;
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.filename == null ? "<unknown>" : this.filename);
        if (this.line > 0 || this.column > 0) {
            buffer.append(':');
            buffer.append(this.line);
            if (this.column > 0) {
                buffer.append(':');
                buffer.append(this.column);
            }
        }
        buffer.append(": ");
        if (this.severity == 'w') {
            buffer.append("warning - ");
        }
        buffer.append(this.message);
        if (this.code != null) {
            buffer.append(" [");
            buffer.append(this.code);
            buffer.append("]");
        }
        if (this.fakeException != null) {
            StackTraceElement[] stackTrace = this.fakeException.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                buffer.append("\n");
                buffer.append("    ");
                buffer.append(stackTraceElement.toString());
            }
        }
        return buffer.toString();
    }

    public void print(PrintWriter out) {
        out.print(this);
    }

    public void println(PrintWriter out) {
        String line2 = toString();
        while (true) {
            int nl = line2.indexOf(10);
            if (nl < 0) {
                out.println(line2);
                return;
            } else {
                out.println(line2.substring(0, nl));
                line2 = line2.substring(nl + 1);
            }
        }
    }

    public void println(PrintStream out) {
        String line2 = toString();
        while (true) {
            int nl = line2.indexOf(10);
            if (nl < 0) {
                out.println(line2);
                return;
            } else {
                out.println(line2.substring(0, nl));
                line2 = line2.substring(nl + 1);
            }
        }
    }

    public int getLineNumber() {
        if (this.line == 0) {
            return -1;
        }
        return this.line;
    }

    public int getColumnNumber() {
        if (this.column == 0) {
            return -1;
        }
        return this.column;
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return this.filename;
    }

    public String getFileName() {
        return this.filename;
    }

    public boolean isStableSourceLocation() {
        return true;
    }
}
