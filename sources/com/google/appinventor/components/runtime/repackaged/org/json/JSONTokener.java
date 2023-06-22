package com.google.appinventor.components.runtime.repackaged.org.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener {
    private long character;
    private boolean eof;
    private long index;
    private long line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;

    public JSONTokener(Reader reader2) {
        this.reader = !reader2.markSupported() ? new BufferedReader(reader2) : reader2;
        this.eof = false;
        this.usePrevious = false;
        this.previous = 0;
        this.index = 0;
        this.character = 1;
        this.line = 1;
    }

    public JSONTokener(InputStream inputStream) throws JSONException {
        this((Reader) new InputStreamReader(inputStream));
    }

    public JSONTokener(String s) {
        this((Reader) new StringReader(s));
    }

    public void back() throws JSONException {
        if (this.usePrevious || this.index <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.index--;
        this.character--;
        this.usePrevious = true;
        this.eof = false;
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 'W';
    }

    public boolean end() {
        return this.eof && !this.usePrevious;
    }

    public boolean more() throws JSONException {
        next();
        if (end()) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        int c;
        long j = 0;
        if (this.usePrevious) {
            this.usePrevious = false;
            c = this.previous;
        } else {
            try {
                c = this.reader.read();
                if (c <= 0) {
                    this.eof = true;
                    c = 0;
                }
            } catch (IOException exception) {
                throw new JSONException((Throwable) exception);
            }
        }
        this.index++;
        if (this.previous == 13) {
            this.line++;
            if (c != 10) {
                j = 1;
            }
            this.character = j;
        } else if (c == 10) {
            this.line = 1 + this.line;
            this.character = 0;
        } else {
            this.character++;
        }
        this.previous = (char) c;
        return this.previous;
    }

    public char next(char c) throws JSONException {
        char n = next();
        if (n == c) {
            return n;
        }
        throw syntaxError(new StringBuffer().append("Expected '").append(c).append("' and instead saw '").append(n).append("'").toString());
    }

    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        char[] chars = new char[n];
        for (int pos = 0; pos < n; pos++) {
            chars[pos] = next();
            if (end()) {
                throw syntaxError("Substring bounds error");
            }
        }
        return new String(chars);
    }

    /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    public char nextClean() throws com.google.appinventor.components.runtime.repackaged.org.json.JSONException {
        /*
            r2 = this;
        L_0x0000:
            char r0 = r2.next()
            if (r0 == 0) goto L_0x000a
            r1 = 32
            if (r0 <= r1) goto L_0x0000
        L_0x000a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.repackaged.org.json.JSONTokener.nextClean():char");
    }

    public String nextString(char quote) throws JSONException {
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = next();
            switch (c) {
                case 0:
                case 10:
                case 13:
                    throw syntaxError("Unterminated string");
                case '\\':
                    char c2 = next();
                    switch (c2) {
                        case '\"':
                        case '\'':
                        case '/':
                        case '\\':
                            sb.append(c2);
                            break;
                        case 'b':
                            sb.append(8);
                            break;
                        case 'f':
                            sb.append(12);
                            break;
                        case 'n':
                            sb.append(10);
                            break;
                        case 'r':
                            sb.append(13);
                            break;
                        case 't':
                            sb.append(9);
                            break;
                        case 'u':
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        default:
                            throw syntaxError("Illegal escape.");
                    }
                default:
                    if (c != quote) {
                        sb.append(c);
                        break;
                    } else {
                        return sb.toString();
                    }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x0017  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(char r4) throws com.google.appinventor.components.runtime.repackaged.org.json.JSONException {
        /*
            r3 = this;
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
        L_0x0005:
            char r0 = r3.next()
            if (r0 == r4) goto L_0x0015
            if (r0 == 0) goto L_0x0015
            r2 = 10
            if (r0 == r2) goto L_0x0015
            r2 = 13
            if (r0 != r2) goto L_0x0023
        L_0x0015:
            if (r0 == 0) goto L_0x001a
            r3.back()
        L_0x001a:
            java.lang.String r2 = r1.toString()
            java.lang.String r2 = r2.trim()
            return r2
        L_0x0023:
            r1.append(r0)
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.repackaged.org.json.JSONTokener.nextTo(char):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String nextTo(java.lang.String r4) throws com.google.appinventor.components.runtime.repackaged.org.json.JSONException {
        /*
            r3 = this;
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
        L_0x0005:
            char r0 = r3.next()
            int r2 = r4.indexOf(r0)
            if (r2 >= 0) goto L_0x0019
            if (r0 == 0) goto L_0x0019
            r2 = 10
            if (r0 == r2) goto L_0x0019
            r2 = 13
            if (r0 != r2) goto L_0x0027
        L_0x0019:
            if (r0 == 0) goto L_0x001e
            r3.back()
        L_0x001e:
            java.lang.String r2 = r1.toString()
            java.lang.String r2 = r2.trim()
            return r2
        L_0x0027:
            r1.append(r0)
            goto L_0x0005
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.repackaged.org.json.JSONTokener.nextTo(java.lang.String):java.lang.String");
    }

    public Object nextValue() throws JSONException {
        char c = nextClean();
        switch (c) {
            case '\"':
            case '\'':
                return nextString(c);
            case '[':
                back();
                return new JSONArray(this);
            case '{':
                back();
                return new JSONObject(this);
            default:
                StringBuffer sb = new StringBuffer();
                while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
                    sb.append(c);
                    c = next();
                }
                back();
                String string = sb.toString().trim();
                if (!"".equals(string)) {
                    return JSONObject.stringToValue(string);
                }
                throw syntaxError("Missing value");
        }
    }

    public char skipTo(char to) throws JSONException {
        char c;
        try {
            long startIndex = this.index;
            long startCharacter = this.character;
            long startLine = this.line;
            this.reader.mark(1000000);
            while (true) {
                c = next();
                if (c != 0) {
                    if (c == to) {
                        back();
                        break;
                    }
                } else {
                    this.reader.reset();
                    this.index = startIndex;
                    this.character = startCharacter;
                    this.line = startLine;
                    break;
                }
            }
            return c;
        } catch (IOException exc) {
            throw new JSONException((Throwable) exc);
        }
    }

    public JSONException syntaxError(String message) {
        return new JSONException(new StringBuffer().append(message).append(toString()).toString());
    }

    public String toString() {
        return new StringBuffer().append(" at ").append(this.index).append(" [character ").append(this.character).append(" line ").append(this.line).append("]").toString();
    }
}
