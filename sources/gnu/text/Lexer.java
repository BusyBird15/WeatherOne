package gnu.text;

import androidx.appcompat.widget.ActivityChooserView;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class Lexer extends Reader {
    protected boolean interactive;
    SourceMessages messages = null;
    protected int nesting;
    protected LineBufferedReader port;
    private int saveTokenBufferLength = -1;
    public char[] tokenBuffer = new char[100];
    public int tokenBufferLength = 0;

    public Lexer(LineBufferedReader port2) {
        this.port = port2;
    }

    public Lexer(LineBufferedReader port2, SourceMessages messages2) {
        this.port = port2;
        this.messages = messages2;
    }

    public char pushNesting(char promptChar) {
        this.nesting++;
        LineBufferedReader port2 = getPort();
        char save = port2.readState;
        port2.readState = promptChar;
        return save;
    }

    public void popNesting(char save) {
        getPort().readState = save;
        this.nesting--;
    }

    public final LineBufferedReader getPort() {
        return this.port;
    }

    public void close() throws IOException {
        this.port.close();
    }

    public int read() throws IOException {
        return this.port.read();
    }

    public int readUnicodeChar() throws IOException {
        int next;
        int c = this.port.read();
        if (c < 55296 || c >= 56319 || (next = this.port.read()) < 56320 || next > 57343) {
            return c;
        }
        return ((c - 55296) << 10) + (c - 56320) + 65536;
    }

    public int read(char[] buf, int offset, int length) throws IOException {
        return this.port.read(buf, offset, length);
    }

    public void unread(int ch) throws IOException {
        if (ch >= 0) {
            this.port.unread();
        }
    }

    public int peek() throws IOException {
        return this.port.peek();
    }

    public void skip() throws IOException {
        this.port.skip();
    }

    /* access modifiers changed from: protected */
    public void unread() throws IOException {
        this.port.unread();
    }

    /* access modifiers changed from: protected */
    public void unread_quick() throws IOException {
        this.port.unread_quick();
    }

    public boolean checkNext(char ch) throws IOException {
        int r = this.port.read();
        if (r == ch) {
            return true;
        }
        if (r >= 0) {
            this.port.unread_quick();
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void skip_quick() throws IOException {
        this.port.skip_quick();
    }

    public SourceMessages getMessages() {
        return this.messages;
    }

    public void setMessages(SourceMessages messages2) {
        this.messages = messages2;
    }

    public boolean checkErrors(PrintWriter out, int max) {
        return this.messages != null && this.messages.checkErrors(out, max);
    }

    public SourceError getErrors() {
        if (this.messages == null) {
            return null;
        }
        return this.messages.getErrors();
    }

    public boolean seenErrors() {
        return this.messages != null && this.messages.seenErrors();
    }

    public void clearErrors() {
        if (this.messages != null) {
            this.messages.clearErrors();
        }
    }

    public void error(char severity, String filename, int line, int column, String message) {
        if (this.messages == null) {
            this.messages = new SourceMessages();
        }
        this.messages.error(severity, filename, line, column, message);
    }

    public void error(char severity, String message) {
        int line = this.port.getLineNumber();
        int column = this.port.getColumnNumber();
        error(severity, this.port.getName(), line + 1, column >= 0 ? column + 1 : 0, message);
    }

    public void error(String message) {
        error('e', message);
    }

    public void fatal(String message) throws SyntaxException {
        error('f', message);
        throw new SyntaxException(this.messages);
    }

    public void eofError(String msg) throws SyntaxException {
        fatal(msg);
    }

    public void eofError(String message, int startLine, int startColumn) throws SyntaxException {
        error('f', this.port.getName(), startLine, startColumn, message);
        throw new SyntaxException(this.messages);
    }

    public int readOptionalExponent() throws IOException {
        int c;
        int value;
        int sign = read();
        boolean overflow = false;
        if (sign == 43 || sign == 45) {
            c = read();
        } else {
            c = sign;
            sign = 0;
        }
        if (c >= 0 && (value = Character.digit((char) c, 10)) >= 0) {
            while (true) {
                c = read();
                int d = Character.digit((char) c, 10);
                if (d < 0) {
                    break;
                }
                if (value > 214748363) {
                    overflow = true;
                }
                value = (value * 10) + d;
            }
        } else {
            if (sign != 0) {
                error("exponent sign not followed by digit");
            }
            value = 1;
        }
        if (c >= 0) {
            unread(c);
        }
        if (sign == 45) {
            value = -value;
        }
        if (!overflow) {
            return value;
        }
        if (sign == 45) {
            return Integer.MIN_VALUE;
        }
        return ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0016, code lost:
        r3 = r1 - 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean readDelimited(java.lang.String r9) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r8 = this;
            r5 = 0
            r8.tokenBufferLength = r5
            int r1 = r9.length()
            int r6 = r1 + -1
            char r4 = r9.charAt(r6)
        L_0x000d:
            int r0 = r8.read()
            if (r0 >= 0) goto L_0x0014
        L_0x0013:
            return r5
        L_0x0014:
            if (r0 != r4) goto L_0x0032
            int r6 = r8.tokenBufferLength
            int r3 = r1 + -1
            int r2 = r6 - r3
            if (r2 < 0) goto L_0x0032
        L_0x001e:
            if (r3 != 0) goto L_0x0024
            r8.tokenBufferLength = r2
            r5 = 1
            goto L_0x0013
        L_0x0024:
            int r3 = r3 + -1
            char[] r6 = r8.tokenBuffer
            int r7 = r2 + r3
            char r6 = r6[r7]
            char r7 = r9.charAt(r3)
            if (r6 == r7) goto L_0x001e
        L_0x0032:
            char r6 = (char) r0
            r8.tokenBufferAppend(r6)
            goto L_0x000d
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.Lexer.readDelimited(java.lang.String):boolean");
    }

    public static long readDigitsInBuffer(LineBufferedReader port2, int radix) {
        long ival = 0;
        boolean overflow = false;
        long max_val = Long.MAX_VALUE / ((long) radix);
        int i = port2.pos;
        if (i >= port2.limit) {
            return 0;
        }
        do {
            int dval = Character.digit(port2.buffer[i], radix);
            if (dval < 0) {
                break;
            }
            if (ival > max_val) {
                overflow = true;
            } else {
                ival = (((long) radix) * ival) + ((long) dval);
            }
            if (ival < 0) {
                overflow = true;
            }
            i++;
        } while (i < port2.limit);
        port2.pos = i;
        if (overflow) {
            return -1;
        }
        return ival;
    }

    public String getName() {
        return this.port.getName();
    }

    public int getLineNumber() {
        return this.port.getLineNumber();
    }

    public int getColumnNumber() {
        return this.port.getColumnNumber();
    }

    public boolean isInteractive() {
        return this.interactive;
    }

    public void setInteractive(boolean v) {
        this.interactive = v;
    }

    public void tokenBufferAppend(int ch) {
        if (ch >= 65536) {
            tokenBufferAppend(((ch - 65536) >> 10) + 55296);
            ch = (ch & 1023) + 56320;
        }
        int len = this.tokenBufferLength;
        char[] buffer = this.tokenBuffer;
        if (len == this.tokenBuffer.length) {
            this.tokenBuffer = new char[(len * 2)];
            System.arraycopy(buffer, 0, this.tokenBuffer, 0, len);
            buffer = this.tokenBuffer;
        }
        buffer[len] = (char) ch;
        this.tokenBufferLength = len + 1;
    }

    public String tokenBufferString() {
        return new String(this.tokenBuffer, 0, this.tokenBufferLength);
    }

    public void mark() throws IOException {
        if (this.saveTokenBufferLength >= 0) {
            throw new Error("internal error: recursive call to mark not allowed");
        }
        this.port.mark(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        this.saveTokenBufferLength = this.tokenBufferLength;
    }

    public void reset() throws IOException {
        if (this.saveTokenBufferLength < 0) {
            throw new Error("internal error: reset called without prior mark");
        }
        this.port.reset();
        this.saveTokenBufferLength = -1;
    }
}
