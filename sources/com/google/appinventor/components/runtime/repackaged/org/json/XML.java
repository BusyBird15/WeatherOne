package com.google.appinventor.components.runtime.repackaged.org.json;

import java.util.Iterator;

public class XML {
    public static final Character AMP = new Character('&');
    public static final Character APOS = new Character('\'');
    public static final Character BANG = new Character('!');
    public static final Character EQ = new Character('=');
    public static final Character GT = new Character('>');
    public static final Character LT = new Character('<');
    public static final Character QUEST = new Character('?');
    public static final Character QUOT = new Character('\"');
    public static final Character SLASH = new Character('/');

    public static String escape(String string) {
        StringBuffer sb = new StringBuffer();
        int length = string.length();
        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static void noSpace(String string) throws JSONException {
        int length = string.length();
        if (length == 0) {
            throw new JSONException("Empty string.");
        }
        for (int i = 0; i < length; i++) {
            if (Character.isWhitespace(string.charAt(i))) {
                throw new JSONException(new StringBuffer().append("'").append(string).append("' contains a space character.").toString());
            }
        }
    }

    private static boolean parse(XMLTokener x, JSONObject context, String name) throws JSONException {
        Object obj;
        Object token = x.nextToken();
        if (token == BANG) {
            char c = x.next();
            if (c == '-') {
                if (x.next() == '-') {
                    x.skipPast("-->");
                    return false;
                }
                x.back();
            } else if (c == '[') {
                if (!"CDATA".equals(x.nextToken()) || x.next() != '[') {
                    throw x.syntaxError("Expected 'CDATA['");
                }
                String string = x.nextCDATA();
                if (string.length() <= 0) {
                    return false;
                }
                context.accumulate("content", string);
                return false;
            }
            int i = 1;
            do {
                Object token2 = x.nextMeta();
                if (token2 == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                } else if (token2 == LT) {
                    i++;
                    continue;
                } else if (token2 == GT) {
                    i--;
                    continue;
                } else {
                    continue;
                }
            } while (i > 0);
            return false;
        } else if (token == QUEST) {
            x.skipPast("?>");
            return false;
        } else if (token == SLASH) {
            Object token3 = x.nextToken();
            if (name == null) {
                throw x.syntaxError(new StringBuffer().append("Mismatched close tag ").append(token3).toString());
            } else if (!token3.equals(name)) {
                throw x.syntaxError(new StringBuffer().append("Mismatched ").append(name).append(" and ").append(token3).toString());
            } else if (x.nextToken() == GT) {
                return true;
            } else {
                throw x.syntaxError("Misshaped close tag");
            }
        } else if (token instanceof Character) {
            throw x.syntaxError("Misshaped tag");
        } else {
            String tagName = (String) token;
            Object token4 = null;
            JSONObject jsonobject = new JSONObject();
            while (true) {
                if (token4 == null) {
                    obj = x.nextToken();
                } else {
                    obj = token4;
                }
                if (obj instanceof String) {
                    String string2 = (String) obj;
                    token4 = x.nextToken();
                    if (token4 == EQ) {
                        Object token5 = x.nextToken();
                        if (!(token5 instanceof String)) {
                            throw x.syntaxError("Missing value");
                        }
                        jsonobject.accumulate(string2, stringToValue((String) token5));
                        token4 = null;
                    } else {
                        jsonobject.accumulate(string2, "");
                    }
                } else if (obj == SLASH) {
                    if (x.nextToken() != GT) {
                        throw x.syntaxError("Misshaped tag");
                    } else if (jsonobject.length() > 0) {
                        context.accumulate(tagName, jsonobject);
                        return false;
                    } else {
                        context.accumulate(tagName, "");
                        return false;
                    }
                } else if (obj == GT) {
                    while (true) {
                        Object token6 = x.nextContent();
                        if (token6 == null) {
                            if (tagName == null) {
                                return false;
                            }
                            throw x.syntaxError(new StringBuffer().append("Unclosed tag ").append(tagName).toString());
                        } else if (token6 instanceof String) {
                            String string3 = (String) token6;
                            if (string3.length() > 0) {
                                jsonobject.accumulate("content", stringToValue(string3));
                            }
                        } else if (token6 == LT && parse(x, jsonobject, tagName)) {
                            if (jsonobject.length() == 0) {
                                context.accumulate(tagName, "");
                                return false;
                            } else if (jsonobject.length() != 1 || jsonobject.opt("content") == null) {
                                context.accumulate(tagName, jsonobject);
                                return false;
                            } else {
                                context.accumulate(tagName, jsonobject.opt("content"));
                                return false;
                            }
                        }
                    }
                } else {
                    throw x.syntaxError("Misshaped tag");
                }
            }
        }
    }

    public static Object stringToValue(String string) {
        if ("true".equalsIgnoreCase(string)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(string)) {
            return Boolean.FALSE;
        }
        if ("null".equalsIgnoreCase(string)) {
            return JSONObject.NULL;
        }
        try {
            char initial = string.charAt(0);
            if (initial == '-' || (initial >= '0' && initial <= '9')) {
                Long value = new Long(string);
                if (value.toString().equals(string)) {
                    return value;
                }
            }
        } catch (Exception e) {
            try {
                Double value2 = new Double(string);
                if (value2.toString().equals(string)) {
                    return value2;
                }
            } catch (Exception e2) {
            }
        }
        return string;
    }

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        XMLTokener x = new XMLTokener(string);
        while (x.more() && x.skipPast("<")) {
            parse(x, jo, (String) null);
        }
        return jo;
    }

    public static String toString(Object object) throws JSONException {
        return toString(object, (String) null);
    }

    public static String toString(Object object, String tagName) throws JSONException {
        String str;
        StringBuffer sb = new StringBuffer();
        if (object instanceof JSONObject) {
            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }
            JSONObject jo = (JSONObject) object;
            Iterator keys = jo.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                Object value = jo.opt(key);
                if (value == null) {
                    value = "";
                }
                if (value instanceof String) {
                    String str2 = (String) value;
                }
                if ("content".equals(key)) {
                    if (value instanceof JSONArray) {
                        JSONArray ja = (JSONArray) value;
                        int length = ja.length();
                        for (int i = 0; i < length; i++) {
                            if (i > 0) {
                                sb.append(10);
                            }
                            sb.append(escape(ja.get(i).toString()));
                        }
                    } else {
                        sb.append(escape(value.toString()));
                    }
                } else if (value instanceof JSONArray) {
                    JSONArray ja2 = (JSONArray) value;
                    int length2 = ja2.length();
                    for (int i2 = 0; i2 < length2; i2++) {
                        Object value2 = ja2.get(i2);
                        if (value2 instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(toString(value2));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                        } else {
                            sb.append(toString(value2, key));
                        }
                    }
                } else if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");
                } else {
                    sb.append(toString(value, key));
                }
            }
            if (tagName != null) {
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();
        }
        if (object.getClass().isArray()) {
            object = new JSONArray(object);
        }
        if (object instanceof JSONArray) {
            JSONArray ja3 = (JSONArray) object;
            int length3 = ja3.length();
            for (int i3 = 0; i3 < length3; i3++) {
                Object opt = ja3.opt(i3);
                if (tagName == null) {
                    str = "array";
                } else {
                    str = tagName;
                }
                sb.append(toString(opt, str));
            }
            return sb.toString();
        }
        String string = object == null ? "null" : escape(object.toString());
        if (tagName == null) {
            return new StringBuffer().append("\"").append(string).append("\"").toString();
        }
        return string.length() == 0 ? new StringBuffer().append("<").append(tagName).append("/>").toString() : new StringBuffer().append("<").append(tagName).append(">").append(string).append("</").append(tagName).append(">").toString();
    }
}
