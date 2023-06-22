package com.google.appinventor.components.runtime.repackaged.org.json;

public class HTTPTokener extends JSONTokener {
    public HTTPTokener(String string) {
        super(string);
    }

    public String nextToken() throws JSONException {
        char c;
        StringBuffer sb = new StringBuffer();
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == '\"' || c == '\'') {
            char q = c;
            while (true) {
                char c2 = next();
                if (c2 < ' ') {
                    throw syntaxError("Unterminated string.");
                } else if (c2 == q) {
                    return sb.toString();
                } else {
                    sb.append(c2);
                }
            }
        } else {
            while (c != 0 && !Character.isWhitespace(c)) {
                sb.append(c);
                c = next();
            }
            return sb.toString();
        }
    }
}
