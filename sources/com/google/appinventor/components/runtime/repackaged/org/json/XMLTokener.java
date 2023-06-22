package com.google.appinventor.components.runtime.repackaged.org.json;

import java.util.HashMap;

public class XMLTokener extends JSONTokener {
    public static final HashMap entity = new HashMap(8);

    static {
        entity.put("amp", XML.AMP);
        entity.put("apos", XML.APOS);
        entity.put("gt", XML.GT);
        entity.put("lt", XML.LT);
        entity.put("quot", XML.QUOT);
    }

    public XMLTokener(String s) {
        super(s);
    }

    public String nextCDATA() throws JSONException {
        StringBuffer sb = new StringBuffer();
        while (true) {
            char c = next();
            if (end()) {
                throw syntaxError("Unclosed CDATA");
            }
            sb.append(c);
            int i = sb.length() - 3;
            if (i >= 0 && sb.charAt(i) == ']' && sb.charAt(i + 1) == ']' && sb.charAt(i + 2) == '>') {
                sb.setLength(i);
                return sb.toString();
            }
        }
    }

    public Object nextContent() throws JSONException {
        char c;
        do {
            c = next();
        } while (Character.isWhitespace(c));
        if (c == 0) {
            return null;
        }
        if (c == '<') {
            return XML.LT;
        }
        StringBuffer sb = new StringBuffer();
        while (c != '<' && c != 0) {
            if (c == '&') {
                sb.append(nextEntity(c));
            } else {
                sb.append(c);
            }
            c = next();
        }
        back();
        return sb.toString().trim();
    }

    public Object nextEntity(char ampersand) throws JSONException {
        char c;
        StringBuffer sb = new StringBuffer();
        while (true) {
            c = next();
            if (!Character.isLetterOrDigit(c) && c != '#') {
                break;
            }
            sb.append(Character.toLowerCase(c));
        }
        if (c == ';') {
            String string = sb.toString();
            Object object = entity.get(string);
            return object != null ? object : new StringBuffer().append(ampersand).append(string).append(";").toString();
        }
        throw syntaxError(new StringBuffer().append("Missing ';' in XML entity: &").append(sb).toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x000d A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object nextMeta() throws com.google.appinventor.components.runtime.repackaged.org.json.JSONException {
        /*
            r3 = this;
        L_0x0000:
            char r0 = r3.next()
            boolean r2 = java.lang.Character.isWhitespace(r0)
            if (r2 != 0) goto L_0x0000
            switch(r0) {
                case 0: goto L_0x001a;
                case 33: goto L_0x002d;
                case 34: goto L_0x0033;
                case 39: goto L_0x0033;
                case 47: goto L_0x0027;
                case 60: goto L_0x0021;
                case 61: goto L_0x002a;
                case 62: goto L_0x0024;
                case 63: goto L_0x0030;
                default: goto L_0x000d;
            }
        L_0x000d:
            char r0 = r3.next()
            boolean r2 = java.lang.Character.isWhitespace(r0)
            if (r2 == 0) goto L_0x0046
            java.lang.Boolean r2 = java.lang.Boolean.TRUE
        L_0x0019:
            return r2
        L_0x001a:
            java.lang.String r2 = "Misshaped meta tag"
            com.google.appinventor.components.runtime.repackaged.org.json.JSONException r2 = r3.syntaxError(r2)
            throw r2
        L_0x0021:
            java.lang.Character r2 = com.google.appinventor.components.runtime.repackaged.org.json.XML.LT
            goto L_0x0019
        L_0x0024:
            java.lang.Character r2 = com.google.appinventor.components.runtime.repackaged.org.json.XML.GT
            goto L_0x0019
        L_0x0027:
            java.lang.Character r2 = com.google.appinventor.components.runtime.repackaged.org.json.XML.SLASH
            goto L_0x0019
        L_0x002a:
            java.lang.Character r2 = com.google.appinventor.components.runtime.repackaged.org.json.XML.EQ
            goto L_0x0019
        L_0x002d:
            java.lang.Character r2 = com.google.appinventor.components.runtime.repackaged.org.json.XML.BANG
            goto L_0x0019
        L_0x0030:
            java.lang.Character r2 = com.google.appinventor.components.runtime.repackaged.org.json.XML.QUEST
            goto L_0x0019
        L_0x0033:
            r1 = r0
        L_0x0034:
            char r0 = r3.next()
            if (r0 != 0) goto L_0x0041
            java.lang.String r2 = "Unterminated string"
            com.google.appinventor.components.runtime.repackaged.org.json.JSONException r2 = r3.syntaxError(r2)
            throw r2
        L_0x0041:
            if (r0 != r1) goto L_0x0034
            java.lang.Boolean r2 = java.lang.Boolean.TRUE
            goto L_0x0019
        L_0x0046:
            switch(r0) {
                case 0: goto L_0x004a;
                case 33: goto L_0x004a;
                case 34: goto L_0x004a;
                case 39: goto L_0x004a;
                case 47: goto L_0x004a;
                case 60: goto L_0x004a;
                case 61: goto L_0x004a;
                case 62: goto L_0x004a;
                case 63: goto L_0x004a;
                default: goto L_0x0049;
            }
        L_0x0049:
            goto L_0x000d
        L_0x004a:
            r3.back()
            java.lang.Boolean r2 = java.lang.Boolean.TRUE
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.repackaged.org.json.XMLTokener.nextMeta():java.lang.Object");
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0012 A[LOOP_START, PHI: r0 
      PHI: (r0v2 'c' char) = (r0v0 'c' char), (r0v3 'c' char) binds: [B:3:0x000d, B:27:0x006b] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object nextToken() throws com.google.appinventor.components.runtime.repackaged.org.json.JSONException {
        /*
            r4 = this;
        L_0x0000:
            char r0 = r4.next()
            boolean r3 = java.lang.Character.isWhitespace(r0)
            if (r3 != 0) goto L_0x0000
            switch(r0) {
                case 0: goto L_0x0024;
                case 33: goto L_0x003b;
                case 34: goto L_0x0041;
                case 39: goto L_0x0041;
                case 47: goto L_0x0035;
                case 60: goto L_0x002b;
                case 61: goto L_0x0038;
                case 62: goto L_0x0032;
                case 63: goto L_0x003e;
                default: goto L_0x000d;
            }
        L_0x000d:
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
        L_0x0012:
            r2.append(r0)
            char r0 = r4.next()
            boolean r3 = java.lang.Character.isWhitespace(r0)
            if (r3 == 0) goto L_0x006b
            java.lang.String r3 = r2.toString()
        L_0x0023:
            return r3
        L_0x0024:
            java.lang.String r3 = "Misshaped element"
            com.google.appinventor.components.runtime.repackaged.org.json.JSONException r3 = r4.syntaxError(r3)
            throw r3
        L_0x002b:
            java.lang.String r3 = "Misplaced '<'"
            com.google.appinventor.components.runtime.repackaged.org.json.JSONException r3 = r4.syntaxError(r3)
            throw r3
        L_0x0032:
            java.lang.Character r3 = com.google.appinventor.components.runtime.repackaged.org.json.XML.GT
            goto L_0x0023
        L_0x0035:
            java.lang.Character r3 = com.google.appinventor.components.runtime.repackaged.org.json.XML.SLASH
            goto L_0x0023
        L_0x0038:
            java.lang.Character r3 = com.google.appinventor.components.runtime.repackaged.org.json.XML.EQ
            goto L_0x0023
        L_0x003b:
            java.lang.Character r3 = com.google.appinventor.components.runtime.repackaged.org.json.XML.BANG
            goto L_0x0023
        L_0x003e:
            java.lang.Character r3 = com.google.appinventor.components.runtime.repackaged.org.json.XML.QUEST
            goto L_0x0023
        L_0x0041:
            r1 = r0
            java.lang.StringBuffer r2 = new java.lang.StringBuffer
            r2.<init>()
        L_0x0047:
            char r0 = r4.next()
            if (r0 != 0) goto L_0x0054
            java.lang.String r3 = "Unterminated string"
            com.google.appinventor.components.runtime.repackaged.org.json.JSONException r3 = r4.syntaxError(r3)
            throw r3
        L_0x0054:
            if (r0 != r1) goto L_0x005b
            java.lang.String r3 = r2.toString()
            goto L_0x0023
        L_0x005b:
            r3 = 38
            if (r0 != r3) goto L_0x0067
            java.lang.Object r3 = r4.nextEntity(r0)
            r2.append(r3)
            goto L_0x0047
        L_0x0067:
            r2.append(r0)
            goto L_0x0047
        L_0x006b:
            switch(r0) {
                case 0: goto L_0x006f;
                case 33: goto L_0x0074;
                case 34: goto L_0x007c;
                case 39: goto L_0x007c;
                case 47: goto L_0x0074;
                case 60: goto L_0x007c;
                case 61: goto L_0x0074;
                case 62: goto L_0x0074;
                case 63: goto L_0x0074;
                case 91: goto L_0x0074;
                case 93: goto L_0x0074;
                default: goto L_0x006e;
            }
        L_0x006e:
            goto L_0x0012
        L_0x006f:
            java.lang.String r3 = r2.toString()
            goto L_0x0023
        L_0x0074:
            r4.back()
            java.lang.String r3 = r2.toString()
            goto L_0x0023
        L_0x007c:
            java.lang.String r3 = "Bad character in a name"
            com.google.appinventor.components.runtime.repackaged.org.json.JSONException r3 = r4.syntaxError(r3)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.repackaged.org.json.XMLTokener.nextToken():java.lang.Object");
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 127 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean skipPast(java.lang.String r11) throws com.google.appinventor.components.runtime.repackaged.org.json.JSONException {
        /*
            r10 = this;
            r7 = 0
            r6 = 0
            int r5 = r11.length()
            char[] r2 = new char[r5]
            r3 = 0
        L_0x0009:
            if (r3 >= r5) goto L_0x001e
            char r1 = r10.next()
            if (r1 != 0) goto L_0x0012
        L_0x0011:
            return r7
        L_0x0012:
            r2[r3] = r1
            int r3 = r3 + 1
            goto L_0x0009
        L_0x0017:
            r2[r6] = r1
            int r6 = r6 + 1
            if (r6 < r5) goto L_0x001e
            int r6 = r6 - r5
        L_0x001e:
            r4 = r6
            r0 = 1
            r3 = 0
        L_0x0021:
            if (r3 >= r5) goto L_0x002c
            char r8 = r2[r4]
            char r9 = r11.charAt(r3)
            if (r8 == r9) goto L_0x0030
            r0 = 0
        L_0x002c:
            if (r0 == 0) goto L_0x0038
            r7 = 1
            goto L_0x0011
        L_0x0030:
            int r4 = r4 + 1
            if (r4 < r5) goto L_0x0035
            int r4 = r4 - r5
        L_0x0035:
            int r3 = r3 + 1
            goto L_0x0021
        L_0x0038:
            char r1 = r10.next()
            if (r1 != 0) goto L_0x0017
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.repackaged.org.json.XMLTokener.skipPast(java.lang.String):boolean");
    }
}
