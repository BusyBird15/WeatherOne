package com.google.appinventor.components.runtime.repackaged.org.json;

import com.google.appinventor.components.runtime.Component;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class JSONObject {
    public static final Object NULL = new Null((AnonymousClass1) null);
    private final Map map;

    /* renamed from: com.google.appinventor.components.runtime.repackaged.org.json.JSONObject$1  reason: invalid class name */
    static class AnonymousClass1 {
    }

    private static final class Null {
        private Null() {
        }

        Null(AnonymousClass1 x0) {
            this();
        }

        /* access modifiers changed from: protected */
        public final Object clone() {
            return this;
        }

        public boolean equals(Object object) {
            return object == null || object == this;
        }

        public String toString() {
            return "null";
        }
    }

    public JSONObject() {
        this.map = new HashMap();
    }

    public JSONObject(JSONObject jo, String[] names) {
        this();
        for (int i = 0; i < names.length; i++) {
            try {
                putOnce(names[i], jo.opt(names[i]));
            } catch (Exception e) {
            }
        }
    }

    public JSONObject(JSONTokener x) throws JSONException {
        this();
        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        while (true) {
            switch (x.nextClean()) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    String key = x.nextValue().toString();
                    if (x.nextClean() != ':') {
                        throw x.syntaxError("Expected a ':' after a key");
                    }
                    putOnce(key, x.nextValue());
                    switch (x.nextClean()) {
                        case ',':
                        case ';':
                            if (x.nextClean() != '}') {
                                x.back();
                            } else {
                                return;
                            }
                        case '}':
                            return;
                        default:
                            throw x.syntaxError("Expected a ',' or '}'");
                    }
            }
        }
    }

    public JSONObject(Map map2) {
        this.map = new HashMap();
        if (map2 != null) {
            for (Map.Entry e : map2.entrySet()) {
                Object value = e.getValue();
                if (value != null) {
                    this.map.put(e.getKey(), wrap(value));
                }
            }
        }
    }

    public JSONObject(Object bean) {
        this();
        populateMap(bean);
    }

    public JSONObject(Object object, String[] names) {
        this();
        Class c = object.getClass();
        for (String name : names) {
            try {
                putOpt(name, c.getField(name).get(object));
            } catch (Exception e) {
            }
        }
    }

    public JSONObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONObject(String baseName, Locale locale) throws JSONException {
        this();
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());
        Enumeration keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String nextElement = keys.nextElement();
            if (nextElement instanceof String) {
                String[] path = nextElement.split("\\.");
                int last = path.length - 1;
                JSONObject target = this;
                for (int i = 0; i < last; i++) {
                    String segment = path[i];
                    JSONObject nextTarget = target.optJSONObject(segment);
                    if (nextTarget == null) {
                        nextTarget = new JSONObject();
                        target.put(segment, (Object) nextTarget);
                    }
                    target = nextTarget;
                }
                target.put(path[last], (Object) bundle.getString(nextElement));
            }
        }
    }

    public JSONObject accumulate(String key, Object value) throws JSONException {
        testValidity(value);
        Object object = opt(key);
        if (object == null) {
            if (value instanceof JSONArray) {
                value = new JSONArray().put(value);
            }
            put(key, value);
        } else if (object instanceof JSONArray) {
            ((JSONArray) object).put(value);
        } else {
            put(key, (Object) new JSONArray().put(object).put(value));
        }
        return this;
    }

    public JSONObject append(String key, Object value) throws JSONException {
        testValidity(value);
        Object object = opt(key);
        if (object == null) {
            put(key, (Object) new JSONArray().put(value));
        } else if (object instanceof JSONArray) {
            put(key, (Object) ((JSONArray) object).put(value));
        } else {
            throw new JSONException(new StringBuffer().append("JSONObject[").append(key).append("] is not a JSONArray.").toString());
        }
        return this;
    }

    public static String doubleToString(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String string = Double.toString(d);
        if (string.indexOf(46) <= 0 || string.indexOf(101) >= 0 || string.indexOf(69) >= 0) {
            return string;
        }
        while (string.endsWith(Component.TYPEFACE_DEFAULT)) {
            string = string.substring(0, string.length() - 1);
        }
        if (string.endsWith(".")) {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }

    public Object get(String key) throws JSONException {
        if (key == null) {
            throw new JSONException("Null key.");
        }
        Object object = opt(key);
        if (object != null) {
            return object;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] not found.").toString());
    }

    public boolean getBoolean(String key) throws JSONException {
        Object object = get(key);
        if (object.equals(Boolean.FALSE) || ((object instanceof String) && ((String) object).equalsIgnoreCase("false"))) {
            return false;
        }
        if (object.equals(Boolean.TRUE) || ((object instanceof String) && ((String) object).equalsIgnoreCase("true"))) {
            return true;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a Boolean.").toString());
    }

    public double getDouble(String key) throws JSONException {
        Object object = get(key);
        try {
            return object instanceof Number ? ((Number) object).doubleValue() : Double.parseDouble((String) object);
        } catch (Exception e) {
            throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a number.").toString());
        }
    }

    public int getInt(String key) throws JSONException {
        Object object = get(key);
        try {
            return object instanceof Number ? ((Number) object).intValue() : Integer.parseInt((String) object);
        } catch (Exception e) {
            throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not an int.").toString());
        }
    }

    public JSONArray getJSONArray(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a JSONArray.").toString());
    }

    public JSONObject getJSONObject(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a JSONObject.").toString());
    }

    public long getLong(String key) throws JSONException {
        Object object = get(key);
        try {
            return object instanceof Number ? ((Number) object).longValue() : Long.parseLong((String) object);
        } catch (Exception e) {
            throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] is not a long.").toString());
        }
    }

    public static String[] getNames(JSONObject jo) {
        int length = jo.length();
        if (length == 0) {
            return null;
        }
        Iterator iterator = jo.keys();
        String[] names = new String[length];
        int i = 0;
        while (iterator.hasNext()) {
            names[i] = (String) iterator.next();
            i++;
        }
        return names;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0004, code lost:
        r0 = r6.getClass().getFields();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] getNames(java.lang.Object r6) {
        /*
            r4 = 0
            if (r6 != 0) goto L_0x0004
        L_0x0003:
            return r4
        L_0x0004:
            java.lang.Class r2 = r6.getClass()
            java.lang.reflect.Field[] r0 = r2.getFields()
            int r3 = r0.length
            if (r3 == 0) goto L_0x0003
            java.lang.String[] r4 = new java.lang.String[r3]
            r1 = 0
        L_0x0012:
            if (r1 >= r3) goto L_0x0003
            r5 = r0[r1]
            java.lang.String r5 = r5.getName()
            r4[r1] = r5
            int r1 = r1 + 1
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.repackaged.org.json.JSONObject.getNames(java.lang.Object):java.lang.String[]");
    }

    public String getString(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof String) {
            return (String) object;
        }
        throw new JSONException(new StringBuffer().append("JSONObject[").append(quote(key)).append("] not a string.").toString());
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public JSONObject increment(String key) throws JSONException {
        Object value = opt(key);
        if (value == null) {
            put(key, 1);
        } else if (value instanceof Integer) {
            put(key, ((Integer) value).intValue() + 1);
        } else if (value instanceof Long) {
            put(key, ((Long) value).longValue() + 1);
        } else if (value instanceof Double) {
            put(key, ((Double) value).doubleValue() + 1.0d);
        } else if (value instanceof Float) {
            put(key, (double) (((Float) value).floatValue() + 1.0f));
        } else {
            throw new JSONException(new StringBuffer().append("Unable to increment [").append(quote(key)).append("].").toString());
        }
        return this;
    }

    public boolean isNull(String key) {
        return NULL.equals(opt(key));
    }

    public Iterator keys() {
        return keySet().iterator();
    }

    public Set keySet() {
        return this.map.keySet();
    }

    public int length() {
        return this.map.size();
    }

    public JSONArray names() {
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()) {
            ja.put(keys.next());
        }
        if (ja.length() == 0) {
            return null;
        }
        return ja;
    }

    public static String numberToString(Number number) throws JSONException {
        if (number == null) {
            throw new JSONException("Null pointer");
        }
        testValidity(number);
        String string = number.toString();
        if (string.indexOf(46) <= 0 || string.indexOf(101) >= 0 || string.indexOf(69) >= 0) {
            return string;
        }
        while (string.endsWith(Component.TYPEFACE_DEFAULT)) {
            string = string.substring(0, string.length() - 1);
        }
        if (string.endsWith(".")) {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }

    public Object opt(String key) {
        if (key == null) {
            return null;
        }
        return this.map.get(key);
    }

    public boolean optBoolean(String key) {
        return optBoolean(key, false);
    }

    public boolean optBoolean(String key, boolean defaultValue) {
        try {
            return getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double optDouble(String key) {
        return optDouble(key, Double.NaN);
    }

    public double optDouble(String key, double defaultValue) {
        try {
            return getDouble(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int optInt(String key) {
        return optInt(key, 0);
    }

    public int optInt(String key, int defaultValue) {
        try {
            return getInt(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public JSONArray optJSONArray(String key) {
        Object o = opt(key);
        if (o instanceof JSONArray) {
            return (JSONArray) o;
        }
        return null;
    }

    public JSONObject optJSONObject(String key) {
        Object object = opt(key);
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        return null;
    }

    public long optLong(String key) {
        return optLong(key, 0);
    }

    public long optLong(String key, long defaultValue) {
        try {
            return getLong(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public String optString(String key) {
        return optString(key, "");
    }

    public String optString(String key, String defaultValue) {
        Object object = opt(key);
        return NULL.equals(object) ? defaultValue : object.toString();
    }

    private void populateMap(Object bean) {
        boolean includeSuperClass = false;
        Class klass = bean.getClass();
        if (klass.getClassLoader() != null) {
            includeSuperClass = true;
        }
        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            try {
                Method method = methods[i];
                if (Modifier.isPublic(method.getModifiers())) {
                    String name = method.getName();
                    String key = "";
                    if (name.startsWith("get")) {
                        if ("getClass".equals(name) || "getDeclaringClass".equals(name)) {
                            key = "";
                        } else {
                            key = name.substring(3);
                        }
                    } else if (name.startsWith("is")) {
                        key = name.substring(2);
                    }
                    if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (key.length() == 1) {
                            key = key.toLowerCase();
                        } else if (!Character.isUpperCase(key.charAt(1))) {
                            key = new StringBuffer().append(key.substring(0, 1).toLowerCase()).append(key.substring(1)).toString();
                        }
                        Object result = method.invoke(bean, (Object[]) null);
                        if (result != null) {
                            this.map.put(key, wrap(result));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public JSONObject put(String key, boolean value) throws JSONException {
        put(key, (Object) value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONObject put(String key, Collection value) throws JSONException {
        put(key, (Object) new JSONArray(value));
        return this;
    }

    public JSONObject put(String key, double value) throws JSONException {
        put(key, (Object) new Double(value));
        return this;
    }

    public JSONObject put(String key, int value) throws JSONException {
        put(key, (Object) new Integer(value));
        return this;
    }

    public JSONObject put(String key, long value) throws JSONException {
        put(key, (Object) new Long(value));
        return this;
    }

    public JSONObject put(String key, Map value) throws JSONException {
        put(key, (Object) new JSONObject(value));
        return this;
    }

    public JSONObject put(String key, Object value) throws JSONException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.map.put(key, value);
        } else {
            remove(key);
        }
        return this;
    }

    public JSONObject putOnce(String key, Object value) throws JSONException {
        if (!(key == null || value == null)) {
            if (opt(key) != null) {
                throw new JSONException(new StringBuffer().append("Duplicate key \"").append(key).append("\"").toString());
            }
            put(key, value);
        }
        return this;
    }

    public JSONObject putOpt(String key, Object value) throws JSONException {
        if (!(key == null || value == null)) {
            put(key, value);
        }
        return this;
    }

    public static String quote(String string) {
        String str;
        StringWriter sw = new StringWriter();
        synchronized (sw.getBuffer()) {
            try {
                str = quote(string, sw).toString();
            } catch (IOException e) {
                str = "";
            }
        }
        return str;
    }

    public static Writer quote(String string, Writer w) throws IOException {
        if (string == null || string.length() == 0) {
            w.write("\"\"");
        } else {
            char c = 0;
            int len = string.length();
            w.write(34);
            for (int i = 0; i < len; i++) {
                char b = c;
                c = string.charAt(i);
                switch (c) {
                    case 8:
                        w.write("\\b");
                        break;
                    case 9:
                        w.write("\\t");
                        break;
                    case 10:
                        w.write("\\n");
                        break;
                    case 12:
                        w.write("\\f");
                        break;
                    case 13:
                        w.write("\\r");
                        break;
                    case '\"':
                    case '\\':
                        w.write(92);
                        w.write(c);
                        break;
                    case '/':
                        if (b == '<') {
                            w.write(92);
                        }
                        w.write(c);
                        break;
                    default:
                        if (c >= ' ' && ((c < 128 || c >= 160) && (c < 8192 || c >= 8448))) {
                            w.write(c);
                            break;
                        } else {
                            w.write("\\u");
                            String hhhh = Integer.toHexString(c);
                            w.write("0000", 0, 4 - hhhh.length());
                            w.write(hhhh);
                            break;
                        }
                }
            }
            w.write(34);
        }
        return w;
    }

    public Object remove(String key) {
        return this.map.remove(key);
    }

    public static Object stringToValue(String string) {
        if (string.equals("")) {
            return string;
        }
        if (string.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (string.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (string.equalsIgnoreCase("null")) {
            return NULL;
        }
        char b = string.charAt(0);
        if ((b < '0' || b > '9') && b != '-') {
            return string;
        }
        try {
            if (string.indexOf(46) > -1 || string.indexOf(101) > -1 || string.indexOf(69) > -1) {
                Double d = Double.valueOf(string);
                if (d.isInfinite() || d.isNaN()) {
                    return string;
                }
                return d;
            }
            Long myLong = new Long(string);
            if (string.equals(myLong.toString())) {
                return myLong.longValue() == ((long) myLong.intValue()) ? new Integer(myLong.intValue()) : myLong;
            }
            return string;
        } catch (Exception e) {
            return string;
        }
    }

    public static void testValidity(Object o) throws JSONException {
        if (o == null) {
            return;
        }
        if (o instanceof Double) {
            if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        } else if (!(o instanceof Float)) {
        } else {
            if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
    }

    public JSONArray toJSONArray(JSONArray names) throws JSONException {
        if (names == null || names.length() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        for (int i = 0; i < names.length(); i++) {
            ja.put(opt(names.getString(i)));
        }
        return ja;
    }

    public String toString() {
        try {
            return toString(0);
        } catch (Exception e) {
            return null;
        }
    }

    public String toString(int indentFactor) throws JSONException {
        String obj;
        StringWriter w = new StringWriter();
        synchronized (w.getBuffer()) {
            obj = write(w, indentFactor, 0).toString();
        }
        return obj;
    }

    public static String valueToString(Object value) throws JSONException {
        if (value == null || value.equals((Object) null)) {
            return "null";
        }
        if (value instanceof JSONString) {
            try {
                String object = ((JSONString) value).toJSONString();
                if (object instanceof String) {
                    return object;
                }
                throw new JSONException(new StringBuffer().append("Bad value from toJSONString: ").append(object).toString());
            } catch (Exception e) {
                throw new JSONException((Throwable) e);
            }
        } else if (value instanceof Number) {
            return numberToString((Number) value);
        } else {
            if ((value instanceof Boolean) || (value instanceof JSONObject) || (value instanceof JSONArray)) {
                return value.toString();
            }
            if (value instanceof Map) {
                return new JSONObject((Map) value).toString();
            }
            if (value instanceof Collection) {
                return new JSONArray((Collection) value).toString();
            }
            if (value.getClass().isArray()) {
                return new JSONArray(value).toString();
            }
            return quote(value.toString());
        }
    }

    public static Object wrap(Object object) {
        if (object == null) {
            try {
                return NULL;
            } catch (Exception e) {
                return null;
            }
        } else if ((object instanceof JSONObject) || (object instanceof JSONArray) || NULL.equals(object) || (object instanceof JSONString) || (object instanceof Byte) || (object instanceof Character) || (object instanceof Short) || (object instanceof Integer) || (object instanceof Long) || (object instanceof Boolean) || (object instanceof Float) || (object instanceof Double) || (object instanceof String)) {
            return object;
        } else {
            if (object instanceof Collection) {
                return new JSONArray((Collection) object);
            }
            if (object.getClass().isArray()) {
                return new JSONArray(object);
            }
            if (object instanceof Map) {
                return new JSONObject((Map) object);
            }
            Package objectPackage = object.getClass().getPackage();
            String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
            if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
                return object.toString();
            }
            return new JSONObject(object);
        }
    }

    public Writer write(Writer writer) throws JSONException {
        return write(writer, 0, 0);
    }

    static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
        if (value == null || value.equals((Object) null)) {
            writer.write("null");
        } else if (value instanceof JSONObject) {
            ((JSONObject) value).write(writer, indentFactor, indent);
        } else if (value instanceof JSONArray) {
            ((JSONArray) value).write(writer, indentFactor, indent);
        } else if (value instanceof Map) {
            new JSONObject((Map) value).write(writer, indentFactor, indent);
        } else if (value instanceof Collection) {
            new JSONArray((Collection) value).write(writer, indentFactor, indent);
        } else if (value.getClass().isArray()) {
            new JSONArray(value).write(writer, indentFactor, indent);
        } else if (value instanceof Number) {
            writer.write(numberToString((Number) value));
        } else if (value instanceof Boolean) {
            writer.write(value.toString());
        } else if (value instanceof JSONString) {
            try {
                String o = ((JSONString) value).toJSONString();
                writer.write(o != null ? o.toString() : quote(value.toString()));
            } catch (Exception e) {
                throw new JSONException((Throwable) e);
            }
        } else {
            quote(value.toString(), writer);
        }
        return writer;
    }

    static final void indent(Writer writer, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            writer.write(32);
        }
    }

    /* access modifiers changed from: package-private */
    public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
        boolean commanate = false;
        try {
            int length = length();
            Iterator keys = keys();
            writer.write(123);
            if (length == 1) {
                Object key = keys.next();
                writer.write(quote(key.toString()));
                writer.write(58);
                if (indentFactor > 0) {
                    writer.write(32);
                }
                writeValue(writer, this.map.get(key), indentFactor, indent);
            } else if (length != 0) {
                int newindent = indent + indentFactor;
                while (keys.hasNext()) {
                    Object key2 = keys.next();
                    if (commanate) {
                        writer.write(44);
                    }
                    if (indentFactor > 0) {
                        writer.write(10);
                    }
                    indent(writer, newindent);
                    writer.write(quote(key2.toString()));
                    writer.write(58);
                    if (indentFactor > 0) {
                        writer.write(32);
                    }
                    writeValue(writer, this.map.get(key2), indentFactor, newindent);
                    commanate = true;
                }
                if (indentFactor > 0) {
                    writer.write(10);
                }
                indent(writer, indent);
            }
            writer.write(125);
            return writer;
        } catch (IOException exception) {
            throw new JSONException((Throwable) exception);
        }
    }
}
