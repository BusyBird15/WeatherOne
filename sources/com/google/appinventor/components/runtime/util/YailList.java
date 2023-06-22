package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.math.IntNum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.json.JSONException;

public class YailList extends Pair implements YailObject {
    public YailList() {
        super(YailConstants.YAIL_HEADER, LList.Empty);
    }

    private YailList(Object cdrval) {
        super(YailConstants.YAIL_HEADER, cdrval);
    }

    public static YailList makeEmptyList() {
        return new YailList();
    }

    public static YailList makeList(Object[] objects) {
        return new YailList(Pair.makeList(objects, 0));
    }

    public static YailList makeList(List vals) {
        return new YailList(Pair.makeList(vals));
    }

    public static YailList makeList(Collection vals) {
        return new YailList(Pair.makeList(new ArrayList(vals)));
    }

    public static YailList makeList(Set vals) {
        return new YailList(Pair.makeList(new ArrayList(vals)));
    }

    public Object[] toArray() {
        if (this.cdr instanceof Pair) {
            return ((Pair) this.cdr).toArray();
        }
        if (this.cdr instanceof LList) {
            return ((LList) this.cdr).toArray();
        }
        throw new YailRuntimeError("YailList cannot be represented as an array", "YailList Error.");
    }

    public String[] toStringArray() {
        int size = size();
        String[] objects = new String[size];
        for (int i = 1; i <= size; i++) {
            objects[i - 1] = YailListElementToString(get(i));
        }
        return objects;
    }

    public static String YailListElementToString(Object element) {
        if (element instanceof IntNum) {
            return ((IntNum) element).toString(10);
        }
        if (element instanceof Long) {
            return Long.toString(((Long) element).longValue());
        }
        if (Number.class.isInstance(element)) {
            return YailNumberToString.format(((Number) element).doubleValue());
        }
        return String.valueOf(element);
    }

    public String toJSONString() {
        try {
            StringBuilder json = new StringBuilder();
            String separator = "";
            json.append('[');
            int size = size();
            for (int i = 1; i <= size; i++) {
                json.append(separator).append(JsonUtil.getJsonRepresentation(get(i)));
                separator = ",";
            }
            json.append(']');
            return json.toString();
        } catch (JSONException e) {
            throw new YailRuntimeError("List failed to convert to JSON.", "JSON Creation Error.");
        }
    }

    public int size() {
        return super.size() - 1;
    }

    public String toString() {
        if (this.cdr instanceof Pair) {
            return ((Pair) this.cdr).toString();
        }
        if (this.cdr instanceof LList) {
            return ((LList) this.cdr).toString();
        }
        throw new RuntimeException("YailList cannot be represented as a String");
    }

    public String getString(int index) {
        return get(index + 1).toString();
    }

    public Object getObject(int index) {
        return get(index + 1);
    }
}
