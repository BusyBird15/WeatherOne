package gnu.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class LineBufferedReader extends Reader {
    public static final int BUFFER_SIZE = 8192;
    private static final int CONVERT_CR = 1;
    private static final int DONT_KEEP_FULL_LINES = 8;
    private static final int PREV_WAS_CR = 4;
    private static final int USER_BUFFER = 2;
    public char[] buffer;
    private int flags;
    int highestPos;
    protected Reader in;
    public int limit;
    protected int lineNumber;
    private int lineStartPos;
    protected int markPos;
    Path path;
    public int pos;
    protected int readAheadLimit = 0;
    public char readState = 10;

    public void close() throws IOException {
        this.in.close();
    }

    public char getReadState() {
        return this.readState;
    }

    public void setKeepFullLines(boolean keep) {
        if (keep) {
            this.flags &= -9;
        } else {
            this.flags |= 8;
        }
    }

    public final boolean getConvertCR() {
        return (this.flags & 1) != 0;
    }

    public final void setConvertCR(boolean convertCR) {
        if (convertCR) {
            this.flags |= 1;
        } else {
            this.flags &= -2;
        }
    }

    public LineBufferedReader(InputStream in2) {
        this.in = new InputStreamReader(in2);
    }

    public LineBufferedReader(Reader in2) {
        this.in = in2;
    }

    public void lineStart(boolean revisited) throws IOException {
    }

    public int fill(int len) throws IOException {
        return this.in.read(this.buffer, this.pos, len);
    }

    private void clearMark() {
        int i = 0;
        this.readAheadLimit = 0;
        if (this.lineStartPos >= 0) {
            i = this.lineStartPos;
        }
        while (true) {
            i++;
            if (i < this.pos) {
                char ch = this.buffer[i - 1];
                if (ch == 10 || (ch == 13 && (!getConvertCR() || this.buffer[i] != 10))) {
                    this.lineNumber++;
                    this.lineStartPos = i;
                }
            } else {
                return;
            }
        }
    }

    public void setBuffer(char[] buffer2) throws IOException {
        if (buffer2 == null) {
            if (this.buffer != null) {
                char[] buffer3 = new char[this.buffer.length];
                System.arraycopy(this.buffer, 0, buffer3, 0, this.buffer.length);
                this.buffer = buffer3;
            }
            this.flags &= -3;
        } else if (this.limit - this.pos > buffer2.length) {
            throw new IOException("setBuffer - too short");
        } else {
            this.flags |= 2;
            reserve(buffer2, 0);
        }
    }

    private void reserve(char[] buffer2, int reserve) throws IOException {
        int saveStart;
        int reserve2 = reserve + this.limit;
        if (reserve2 <= buffer2.length) {
            saveStart = 0;
        } else {
            saveStart = this.pos;
            if (this.readAheadLimit > 0 && this.markPos < this.pos) {
                if (this.pos - this.markPos > this.readAheadLimit || ((this.flags & 2) != 0 && reserve2 - this.markPos > buffer2.length)) {
                    clearMark();
                } else {
                    saveStart = this.markPos;
                }
            }
            int reserve3 = reserve2 - buffer2.length;
            if (reserve3 > saveStart || (saveStart > this.lineStartPos && (this.flags & 8) == 0)) {
                if (reserve3 <= this.lineStartPos && saveStart > this.lineStartPos) {
                    saveStart = this.lineStartPos;
                } else if ((this.flags & 2) != 0) {
                    saveStart -= (saveStart - reserve3) >> 2;
                } else {
                    if (this.lineStartPos >= 0) {
                        saveStart = this.lineStartPos;
                    }
                    buffer2 = new char[(buffer2.length * 2)];
                }
            }
            this.lineStartPos -= saveStart;
            this.limit -= saveStart;
            this.markPos -= saveStart;
            this.pos -= saveStart;
            this.highestPos -= saveStart;
        }
        if (this.limit > 0) {
            System.arraycopy(this.buffer, saveStart, buffer2, 0, this.limit);
        }
        this.buffer = buffer2;
    }

    public int read() throws IOException {
        char prev;
        if (this.pos > 0) {
            prev = this.buffer[this.pos - 1];
        } else if ((this.flags & 4) != 0) {
            prev = 13;
        } else if (this.lineStartPos >= 0) {
            prev = 10;
        } else {
            prev = 0;
        }
        if (prev == 13 || prev == 10) {
            if (this.lineStartPos < this.pos && (this.readAheadLimit == 0 || this.pos <= this.markPos)) {
                this.lineStartPos = this.pos;
                this.lineNumber++;
            }
            boolean revisited = this.pos < this.highestPos;
            if (prev != 10 || (this.pos > 1 ? this.buffer[this.pos - 2] != 13 : (this.flags & 4) == 0)) {
                lineStart(revisited);
            }
            if (!revisited) {
                this.highestPos = this.pos + 1;
            }
        }
        if (this.pos >= this.limit) {
            if (this.buffer == null) {
                this.buffer = new char[8192];
            } else if (this.limit == this.buffer.length) {
                reserve(this.buffer, 1);
            }
            if (this.pos == 0) {
                if (prev == 13) {
                    this.flags |= 4;
                } else {
                    this.flags &= -5;
                }
            }
            int readCount = fill(this.buffer.length - this.pos);
            if (readCount <= 0) {
                return -1;
            }
            this.limit += readCount;
        }
        char[] cArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        char ch = cArr[i];
        if (ch == 10) {
            if (prev != 13) {
                return ch;
            }
            if (this.lineStartPos == this.pos - 1) {
                this.lineNumber--;
                this.lineStartPos--;
            }
            if (getConvertCR()) {
                return read();
            }
            return ch;
        } else if (ch != 13 || !getConvertCR()) {
            return ch;
        } else {
            return 10;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v12, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v13, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v14, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v15, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v16, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v17, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v18, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v19, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v20, resolved type: char} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(char[] r10, int r11, int r12) throws java.io.IOException {
        /*
            r9 = this;
            r8 = 13
            r7 = 10
            int r5 = r9.pos
            int r6 = r9.limit
            if (r5 < r6) goto L_0x0024
            r0 = 0
        L_0x000b:
            r4 = r12
            r2 = r11
        L_0x000d:
            if (r4 <= 0) goto L_0x007b
            int r5 = r9.pos
            int r6 = r9.limit
            if (r5 >= r6) goto L_0x0019
            if (r0 == r7) goto L_0x0019
            if (r0 != r8) goto L_0x0056
        L_0x0019:
            int r5 = r9.pos
            int r6 = r9.limit
            if (r5 < r6) goto L_0x0040
            if (r4 >= r12) goto L_0x0040
            int r5 = r12 - r4
        L_0x0023:
            return r5
        L_0x0024:
            int r5 = r9.pos
            if (r5 <= 0) goto L_0x0031
            char[] r5 = r9.buffer
            int r6 = r9.pos
            int r6 = r6 + -1
            char r0 = r5[r6]
            goto L_0x000b
        L_0x0031:
            int r5 = r9.flags
            r5 = r5 & 4
            if (r5 != 0) goto L_0x003b
            int r5 = r9.lineStartPos
            if (r5 < 0) goto L_0x003e
        L_0x003b:
            r0 = 10
            goto L_0x000b
        L_0x003e:
            r0 = 0
            goto L_0x000b
        L_0x0040:
            int r0 = r9.read()
            if (r0 >= 0) goto L_0x004d
            int r12 = r12 - r4
            if (r12 > 0) goto L_0x004b
            r5 = -1
            goto L_0x0023
        L_0x004b:
            r5 = r12
            goto L_0x0023
        L_0x004d:
            int r11 = r2 + 1
            char r5 = (char) r0
            r10[r2] = r5
            int r4 = r4 + -1
            r2 = r11
            goto L_0x000d
        L_0x0056:
            int r3 = r9.pos
            int r1 = r9.limit
            int r5 = r1 - r3
            if (r4 >= r5) goto L_0x0060
            int r1 = r3 + r4
        L_0x0060:
            if (r3 >= r1) goto L_0x006a
            char[] r5 = r9.buffer
            char r0 = r5[r3]
            if (r0 == r7) goto L_0x006a
            if (r0 != r8) goto L_0x0072
        L_0x006a:
            int r5 = r9.pos
            int r5 = r3 - r5
            int r4 = r4 - r5
            r9.pos = r3
            goto L_0x000d
        L_0x0072:
            int r11 = r2 + 1
            char r5 = (char) r0
            r10[r2] = r5
            int r3 = r3 + 1
            r2 = r11
            goto L_0x0060
        L_0x007b:
            r5 = r12
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.LineBufferedReader.read(char[], int, int):int");
    }

    public Path getPath() {
        return this.path;
    }

    public void setPath(Path path2) {
        this.path = path2;
    }

    public String getName() {
        if (this.path == null) {
            return null;
        }
        return this.path.toString();
    }

    public void setName(Object name) {
        setPath(Path.valueOf(name));
    }

    public int getLineNumber() {
        int lineno = this.lineNumber;
        if (this.readAheadLimit != 0) {
            return lineno + countLines(this.buffer, this.lineStartPos < 0 ? 0 : this.lineStartPos, this.pos);
        } else if (this.pos <= 0 || this.pos <= this.lineStartPos) {
            return lineno;
        } else {
            char prev = this.buffer[this.pos - 1];
            if (prev == 10 || prev == 13) {
                return lineno + 1;
            }
            return lineno;
        }
    }

    public void setLineNumber(int lineNumber2) {
        this.lineNumber += lineNumber2 - getLineNumber();
    }

    public void incrLineNumber(int lineDelta, int lineStartPos2) {
        this.lineNumber += lineDelta;
        this.lineStartPos = lineStartPos2;
    }

    public int getColumnNumber() {
        char prev;
        int start = 0;
        if (this.pos > 0 && ((prev = this.buffer[this.pos - 1]) == 10 || prev == 13)) {
            return 0;
        }
        if (this.readAheadLimit <= 0) {
            return this.pos - this.lineStartPos;
        }
        if (this.lineStartPos >= 0) {
            start = this.lineStartPos;
        }
        int i = start;
        while (i < this.pos) {
            int i2 = i + 1;
            char ch = this.buffer[i];
            if (ch == 10 || ch == 13) {
                start = i2;
            }
            i = i2;
        }
        int col = this.pos - start;
        if (this.lineStartPos < 0) {
            col -= this.lineStartPos;
        }
        return col;
    }

    public boolean markSupported() {
        return true;
    }

    public synchronized void mark(int readAheadLimit2) {
        if (this.readAheadLimit > 0) {
            clearMark();
        }
        this.readAheadLimit = readAheadLimit2;
        this.markPos = this.pos;
    }

    public void reset() throws IOException {
        if (this.readAheadLimit <= 0) {
            throw new IOException("mark invalid");
        }
        if (this.pos > this.highestPos) {
            this.highestPos = this.pos;
        }
        this.pos = this.markPos;
        this.readAheadLimit = 0;
    }

    public void readLine(StringBuffer sbuf, char mode) throws IOException {
        while (read() >= 0) {
            int start = this.pos - 1;
            this.pos = start;
            while (this.pos < this.limit) {
                char[] cArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                char ch = cArr[i];
                if (ch != 13) {
                    if (ch == 10) {
                    }
                }
                sbuf.append(this.buffer, start, (this.pos - 1) - start);
                if (mode == 'P') {
                    this.pos--;
                    return;
                } else if (!getConvertCR() && ch != 10) {
                    if (mode != 'I') {
                        sbuf.append(13);
                    }
                    int ch2 = read();
                    if (ch2 == 10) {
                        if (mode != 'I') {
                            sbuf.append(10);
                            return;
                        }
                        return;
                    } else if (ch2 >= 0) {
                        unread_quick();
                        return;
                    } else {
                        return;
                    }
                } else if (mode != 'I') {
                    sbuf.append(10);
                    return;
                } else {
                    return;
                }
            }
            sbuf.append(this.buffer, start, this.pos - start);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        r1 = r9.pos - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002f, code lost:
        if (r0 == 10) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0035, code lost:
        if (getConvertCR() != false) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003b, code lost:
        if (r9.pos < r9.limit) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003d, code lost:
        r9.pos--;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0062, code lost:
        if (r9.buffer[r9.pos] != 10) goto L_0x006a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0064, code lost:
        r9.pos++;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return new java.lang.String(r9.buffer, r3, r1 - r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String readLine() throws java.io.IOException {
        /*
            r9 = this;
            r8 = 13
            r7 = 10
            int r0 = r9.read()
            if (r0 >= 0) goto L_0x000c
            r4 = 0
        L_0x000b:
            return r4
        L_0x000c:
            if (r0 == r8) goto L_0x0010
            if (r0 != r7) goto L_0x0013
        L_0x0010:
            java.lang.String r4 = ""
            goto L_0x000b
        L_0x0013:
            int r4 = r9.pos
            int r3 = r4 + -1
        L_0x0017:
            int r4 = r9.pos
            int r5 = r9.limit
            if (r4 >= r5) goto L_0x0043
            char[] r4 = r9.buffer
            int r5 = r9.pos
            int r6 = r5 + 1
            r9.pos = r6
            char r0 = r4[r5]
            if (r0 == r8) goto L_0x002b
            if (r0 != r7) goto L_0x0017
        L_0x002b:
            int r4 = r9.pos
            int r1 = r4 + -1
            if (r0 == r7) goto L_0x006a
            boolean r4 = r9.getConvertCR()
            if (r4 != 0) goto L_0x006a
            int r4 = r9.pos
            int r5 = r9.limit
            if (r4 < r5) goto L_0x005c
            int r4 = r9.pos
            int r4 = r4 + -1
            r9.pos = r4
        L_0x0043:
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r4 = 100
            r2.<init>(r4)
            char[] r4 = r9.buffer
            int r5 = r9.pos
            int r5 = r5 - r3
            r2.append(r4, r3, r5)
            r4 = 73
            r9.readLine(r2, r4)
            java.lang.String r4 = r2.toString()
            goto L_0x000b
        L_0x005c:
            char[] r4 = r9.buffer
            int r5 = r9.pos
            char r4 = r4[r5]
            if (r4 != r7) goto L_0x006a
            int r4 = r9.pos
            int r4 = r4 + 1
            r9.pos = r4
        L_0x006a:
            java.lang.String r4 = new java.lang.String
            char[] r5 = r9.buffer
            int r6 = r1 - r3
            r4.<init>(r5, r3, r6)
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.LineBufferedReader.readLine():java.lang.String");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v11, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v12, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v13, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v14, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v15, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v16, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v17, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v18, resolved type: char} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v19, resolved type: char} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int skip(int r9) throws java.io.IOException {
        /*
            r8 = this;
            r7 = 13
            r6 = 10
            if (r9 >= 0) goto L_0x0015
            int r3 = -r9
        L_0x0007:
            if (r3 <= 0) goto L_0x0013
            int r4 = r8.pos
            if (r4 <= 0) goto L_0x0013
            r8.unread()
            int r3 = r3 + -1
            goto L_0x0007
        L_0x0013:
            int r9 = r9 + r3
        L_0x0014:
            return r9
        L_0x0015:
            r3 = r9
            int r4 = r8.pos
            int r5 = r8.limit
            if (r4 < r5) goto L_0x0031
            r0 = 0
        L_0x001d:
            if (r3 <= 0) goto L_0x0014
            if (r0 == r6) goto L_0x0029
            if (r0 == r7) goto L_0x0029
            int r4 = r8.pos
            int r5 = r8.limit
            if (r4 < r5) goto L_0x0050
        L_0x0029:
            int r0 = r8.read()
            if (r0 >= 0) goto L_0x004d
            int r9 = r9 - r3
            goto L_0x0014
        L_0x0031:
            int r4 = r8.pos
            if (r4 <= 0) goto L_0x003e
            char[] r4 = r8.buffer
            int r5 = r8.pos
            int r5 = r5 + -1
            char r0 = r4[r5]
            goto L_0x001d
        L_0x003e:
            int r4 = r8.flags
            r4 = r4 & 4
            if (r4 != 0) goto L_0x0048
            int r4 = r8.lineStartPos
            if (r4 < 0) goto L_0x004b
        L_0x0048:
            r0 = 10
            goto L_0x001d
        L_0x004b:
            r0 = 0
            goto L_0x001d
        L_0x004d:
            int r3 = r3 + -1
            goto L_0x001d
        L_0x0050:
            int r2 = r8.pos
            int r1 = r8.limit
            int r4 = r1 - r2
            if (r3 >= r4) goto L_0x005a
            int r1 = r2 + r3
        L_0x005a:
            if (r2 >= r1) goto L_0x0064
            char[] r4 = r8.buffer
            char r0 = r4[r2]
            if (r0 == r6) goto L_0x0064
            if (r0 != r7) goto L_0x006c
        L_0x0064:
            int r4 = r8.pos
            int r4 = r2 - r4
            int r3 = r3 - r4
            r8.pos = r2
            goto L_0x001d
        L_0x006c:
            int r2 = r2 + 1
            goto L_0x005a
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.LineBufferedReader.skip(int):int");
    }

    public boolean ready() throws IOException {
        return this.pos < this.limit || this.in.ready();
    }

    public final void skip_quick() throws IOException {
        this.pos++;
    }

    public void skip() throws IOException {
        read();
    }

    static int countLines(char[] buffer2, int start, int limit2) {
        int count = 0;
        char prev = 0;
        for (int i = start; i < limit2; i++) {
            char ch = buffer2[i];
            if ((ch == 10 && prev != 13) || ch == 13) {
                count++;
            }
            prev = ch;
        }
        return count;
    }

    public void skipRestOfLine() throws IOException {
        int c;
        do {
            c = read();
            if (c >= 0) {
                if (c == 13) {
                    int c2 = read();
                    if (c2 >= 0 && c2 != 10) {
                        unread();
                        return;
                    }
                    return;
                }
            } else {
                return;
            }
        } while (c != 10);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0056, code lost:
        r1 = r1 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unread() throws java.io.IOException {
        /*
            r6 = this;
            r5 = 13
            r4 = 10
            int r2 = r6.pos
            if (r2 != 0) goto L_0x0010
            java.io.IOException r2 = new java.io.IOException
            java.lang.String r3 = "unread too much"
            r2.<init>(r3)
            throw r2
        L_0x0010:
            int r2 = r6.pos
            int r2 = r2 + -1
            r6.pos = r2
            char[] r2 = r6.buffer
            int r3 = r6.pos
            char r0 = r2[r3]
            if (r0 == r4) goto L_0x0020
            if (r0 != r5) goto L_0x005a
        L_0x0020:
            int r2 = r6.pos
            if (r2 <= 0) goto L_0x003c
            if (r0 != r4) goto L_0x003c
            boolean r2 = r6.getConvertCR()
            if (r2 == 0) goto L_0x003c
            char[] r2 = r6.buffer
            int r3 = r6.pos
            int r3 = r3 + -1
            char r2 = r2[r3]
            if (r2 != r5) goto L_0x003c
            int r2 = r6.pos
            int r2 = r2 + -1
            r6.pos = r2
        L_0x003c:
            int r2 = r6.pos
            int r3 = r6.lineStartPos
            if (r2 >= r3) goto L_0x005a
            int r2 = r6.lineNumber
            int r2 = r2 + -1
            r6.lineNumber = r2
            int r1 = r6.pos
        L_0x004a:
            if (r1 <= 0) goto L_0x0058
            char[] r2 = r6.buffer
            int r1 = r1 + -1
            char r0 = r2[r1]
            if (r0 == r5) goto L_0x0056
            if (r0 != r4) goto L_0x004a
        L_0x0056:
            int r1 = r1 + 1
        L_0x0058:
            r6.lineStartPos = r1
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.LineBufferedReader.unread():void");
    }

    public void unread_quick() {
        this.pos--;
    }

    public int peek() throws IOException {
        char ch;
        if (this.pos >= this.limit || this.pos <= 0 || (ch = this.buffer[this.pos - 1]) == 10 || ch == 13) {
            int c = read();
            if (c >= 0) {
                unread_quick();
            }
            return c;
        }
        char ch2 = this.buffer[this.pos];
        if (ch2 != 13 || !getConvertCR()) {
            return ch2;
        }
        return 10;
    }
}
