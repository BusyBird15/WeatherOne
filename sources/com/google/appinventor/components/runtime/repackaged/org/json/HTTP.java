package com.google.appinventor.components.runtime.repackaged.org.json;

import java.util.Iterator;

public class HTTP {
    public static final String CRLF = "\r\n";

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        HTTPTokener x = new HTTPTokener(string);
        String token = x.nextToken();
        if (token.toUpperCase().startsWith("HTTP")) {
            jo.put("HTTP-Version", (Object) token);
            jo.put("Status-Code", (Object) x.nextToken());
            jo.put("Reason-Phrase", (Object) x.nextTo(0));
            x.next();
        } else {
            jo.put("Method", (Object) token);
            jo.put("Request-URI", (Object) x.nextToken());
            jo.put("HTTP-Version", (Object) x.nextToken());
        }
        while (x.more()) {
            String name = x.nextTo(':');
            x.next(':');
            jo.put(name, (Object) x.nextTo(0));
            x.next();
        }
        return jo;
    }

    public static String toString(JSONObject jo) throws JSONException {
        Iterator keys = jo.keys();
        StringBuffer sb = new StringBuffer();
        if (jo.has("Status-Code") && jo.has("Reason-Phrase")) {
            sb.append(jo.getString("HTTP-Version"));
            sb.append(' ');
            sb.append(jo.getString("Status-Code"));
            sb.append(' ');
            sb.append(jo.getString("Reason-Phrase"));
        } else if (!jo.has("Method") || !jo.has("Request-URI")) {
            throw new JSONException("Not enough material for an HTTP header.");
        } else {
            sb.append(jo.getString("Method"));
            sb.append(' ');
            sb.append('\"');
            sb.append(jo.getString("Request-URI"));
            sb.append('\"');
            sb.append(' ');
            sb.append(jo.getString("HTTP-Version"));
        }
        sb.append(CRLF);
        while (keys.hasNext()) {
            String string = keys.next().toString();
            if (!"HTTP-Version".equals(string) && !"Status-Code".equals(string) && !"Reason-Phrase".equals(string) && !"Method".equals(string) && !"Request-URI".equals(string) && !jo.isNull(string)) {
                sb.append(string);
                sb.append(": ");
                sb.append(jo.getString(string));
                sb.append(CRLF);
            }
        }
        sb.append(CRLF);
        return sb.toString();
    }
}
