package com.google.appinventor.components.runtime.util;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.appinventor.components.runtime.collect.Lists;
import com.google.appinventor.components.runtime.errors.DispatchableError;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import gnu.lists.FString;
import gnu.lists.LList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class YailDictionary extends LinkedHashMap<Object, Object> implements YailObject<YailList> {
    public static final Object ALL = new Object() {
        public String toString() {
            return "ALL_ITEMS";
        }
    };
    private static final String LOG_TAG = "YailDictionary";

    public YailDictionary() {
    }

    public YailDictionary(Map<Object, Object> prevMap) {
        super(prevMap);
    }

    public static YailDictionary makeDictionary() {
        return new YailDictionary();
    }

    public static YailDictionary makeDictionary(Map<Object, Object> prevMap) {
        return new YailDictionary(prevMap);
    }

    public static YailDictionary makeDictionary(Object... keysAndValues) {
        if (keysAndValues.length % 2 == 1) {
            throw new IllegalArgumentException("Expected an even number of key-value entries.");
        }
        YailDictionary dict = new YailDictionary();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            dict.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return dict;
    }

    public static YailDictionary makeDictionary(List<YailList> pairs) {
        Map<Object, Object> map = new LinkedHashMap<>();
        for (YailList currentYailList : pairs) {
            map.put(currentYailList.getObject(0), currentYailList.getObject(1));
        }
        return new YailDictionary(map);
    }

    private static Boolean isAlist(YailList yailList) {
        boolean hadPair = false;
        Iterator it = ((LList) yailList.getCdr()).iterator();
        while (it.hasNext()) {
            Object currentPair = it.next();
            if (!(currentPair instanceof YailList)) {
                return false;
            }
            if (((YailList) currentPair).size() != 2) {
                return false;
            }
            hadPair = true;
        }
        return Boolean.valueOf(hadPair);
    }

    public static YailDictionary alistToDict(YailList alist) {
        LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
        Iterator it = ((LList) alist.getCdr()).iterator();
        while (it.hasNext()) {
            YailList currentPair = (YailList) it.next();
            Object currentKey = currentPair.getObject(0);
            Object currentValue = currentPair.getObject(1);
            if ((currentValue instanceof YailList) && isAlist((YailList) currentValue).booleanValue()) {
                map.put(currentKey, alistToDict((YailList) currentValue));
            } else if (currentValue instanceof YailList) {
                map.put(currentKey, checkList((YailList) currentValue));
            } else {
                map.put(currentKey, currentValue);
            }
        }
        return new YailDictionary(map);
    }

    private static YailList checkList(YailList list) {
        Object[] checked = new Object[list.size()];
        int i = 0;
        Iterator<?> it = list.iterator();
        it.next();
        boolean processed = false;
        while (it.hasNext()) {
            Object next = it.next();
            if (!(next instanceof YailList)) {
                checked[i] = next;
            } else if (isAlist((YailList) next).booleanValue()) {
                checked[i] = alistToDict((YailList) next);
                processed = true;
            } else {
                checked[i] = checkList((YailList) next);
                if (checked[i] != next) {
                    processed = true;
                }
            }
            i++;
        }
        if (processed) {
            return YailList.makeList((Object[]) checked);
        }
        return list;
    }

    private static YailList checkListForDicts(YailList list) {
        List<Object> copy = new ArrayList<>();
        Iterator it = ((LList) list.getCdr()).iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof YailDictionary) {
                copy.add(dictToAlist((YailDictionary) o));
            } else if (o instanceof YailList) {
                copy.add(checkListForDicts((YailList) o));
            } else {
                copy.add(o);
            }
        }
        return YailList.makeList((List) copy);
    }

    public static YailList dictToAlist(YailDictionary dict) {
        List<Object> list = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : dict.entrySet()) {
            list.add(YailList.makeList(new Object[]{entry.getKey(), entry.getValue()}));
        }
        return YailList.makeList((List) list);
    }

    public void setPair(YailList pair) {
        put(pair.getObject(0), pair.getObject(1));
    }

    private Object getFromList(List<?> target, Object currentKey) {
        int offset = target instanceof YailList ? 0 : 1;
        try {
            if (currentKey instanceof FString) {
                return target.get(Integer.parseInt(currentKey.toString()) - offset);
            }
            if (currentKey instanceof String) {
                return target.get(Integer.parseInt((String) currentKey) - offset);
            }
            if (currentKey instanceof Number) {
                return target.get(((Number) currentKey).intValue() - offset);
            }
            return null;
        } catch (NumberFormatException e) {
            Log.w(LOG_TAG, "Unable to parse key as integer: " + currentKey, e);
            throw new YailRuntimeError("Unable to parse key as integer: " + currentKey, "NumberParseException");
        } catch (IndexOutOfBoundsException e2) {
            Log.w(LOG_TAG, "Requested too large of an index: " + currentKey, e2);
            throw new YailRuntimeError("Requested too large of an index: " + currentKey, "IndexOutOfBoundsException");
        }
    }

    public Object getObjectAtKeyPath(List<?> keysOrIndices) {
        Object obj;
        Object target = this;
        for (Object currentKey : keysOrIndices) {
            if (target instanceof Map) {
                obj = ((Map) target).get(currentKey);
            } else if ((target instanceof YailList) && isAlist((YailList) target).booleanValue()) {
                obj = alistToDict((YailList) target).get(currentKey);
            } else if (!(target instanceof List)) {
                return null;
            } else {
                obj = getFromList((List) target, currentKey);
            }
            target = obj;
        }
        return target;
    }

    private static Collection<Object> allOf(Map<Object, Object> map) {
        return map.values();
    }

    private static Collection<Object> allOf(List<Object> list) {
        if (!(list instanceof YailList)) {
            return list;
        }
        if (!isAlist((YailList) list).booleanValue()) {
            return (Collection) ((YailList) list).getCdr();
        }
        ArrayList<Object> result = new ArrayList<>();
        Iterator it = ((LList) ((YailList) list).getCdr()).iterator();
        while (it.hasNext()) {
            result.add(((YailList) it.next()).getObject(1));
        }
        return result;
    }

    private static Collection<Object> allOf(Object object) {
        if (object instanceof Map) {
            return allOf((Map<Object, Object>) (Map) object);
        }
        if (object instanceof List) {
            return allOf((List<Object>) (List) object);
        }
        return Collections.emptyList();
    }

    private static Object alistLookup(YailList alist, Object target) {
        Iterator it = ((LList) alist.getCdr()).iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (!(o instanceof YailList)) {
                return null;
            }
            if (((YailList) o).getObject(0).equals(target)) {
                return ((YailList) o).getObject(1);
            }
        }
        return null;
    }

    private static <T> List<Object> walkKeyPath(Object root, List<T> keysOrIndices, List<Object> result) {
        if (keysOrIndices.isEmpty()) {
            if (root != null) {
                result.add(root);
            }
        } else if (root != null) {
            Object currentKey = keysOrIndices.get(0);
            List<T> childKeys = keysOrIndices.subList(1, keysOrIndices.size());
            if (currentKey == ALL) {
                for (Object child : allOf(root)) {
                    walkKeyPath(child, childKeys, result);
                }
            } else if (root instanceof Map) {
                walkKeyPath(((Map) root).get(currentKey), childKeys, result);
            } else if ((root instanceof YailList) && isAlist((YailList) root).booleanValue()) {
                Object value = alistLookup((YailList) root, currentKey);
                if (value != null) {
                    walkKeyPath(value, childKeys, result);
                }
            } else if (root instanceof List) {
                try {
                    walkKeyPath(((List) root).get(keyToIndex((List) root, currentKey)), childKeys, result);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    public static <T> List<Object> walkKeyPath(YailObject<?> object, List<T> keysOrIndices) {
        return walkKeyPath(object, keysOrIndices, new ArrayList());
    }

    private static int keyToIndex(List<?> target, Object key) {
        int offset;
        int index;
        if (target instanceof YailList) {
            offset = 0;
        } else {
            offset = 1;
        }
        if (key instanceof Number) {
            index = ((Number) key).intValue();
        } else {
            try {
                index = Integer.parseInt(key.toString());
            } catch (NumberFormatException e) {
                throw new DispatchableError(ErrorMessages.ERROR_NUMBER_FORMAT_EXCEPTION, key.toString());
            }
        }
        int index2 = index - offset;
        if (index2 >= 0 && index2 < (target.size() + 1) - offset) {
            return index2;
        }
        try {
            throw new DispatchableError(ErrorMessages.ERROR_INDEX_MISSING_IN_LIST, Integer.valueOf(index2 + offset), JsonUtil.getJsonRepresentation(target));
        } catch (JSONException e2) {
            Log.e(LOG_TAG, "Unable to serialize object as JSON", e2);
            throw new YailRuntimeError(e2.getMessage(), "JSON Error");
        }
    }

    private Object lookupTargetForKey(Object target, Object key) {
        String simpleName;
        if (target instanceof YailDictionary) {
            return ((YailDictionary) target).get(key);
        }
        if (target instanceof List) {
            return ((List) target).get(keyToIndex((List) target, key));
        }
        Object[] objArr = new Object[1];
        if (target == null) {
            simpleName = "null";
        } else {
            simpleName = target.getClass().getSimpleName();
        }
        objArr[0] = simpleName;
        throw new DispatchableError(ErrorMessages.ERROR_INVALID_VALUE_IN_PATH, objArr);
    }

    public void setValueForKeyPath(List<?> keys, Object value) {
        Iterator<?> it = keys.iterator();
        if (!keys.isEmpty()) {
            YailDictionary yailDictionary = this;
            while (it.hasNext()) {
                Object key = it.next();
                if (it.hasNext()) {
                    yailDictionary = lookupTargetForKey(yailDictionary, key);
                } else if (yailDictionary instanceof YailDictionary) {
                    yailDictionary.put(key, value);
                } else if (yailDictionary instanceof YailList) {
                    ((LList) yailDictionary).getIterator(keyToIndex((List) yailDictionary, key)).set(value);
                } else if (yailDictionary instanceof List) {
                    ((List) yailDictionary).set(keyToIndex((List) yailDictionary, key), value);
                } else {
                    throw new DispatchableError(ErrorMessages.ERROR_INVALID_VALUE_IN_PATH);
                }
            }
        }
    }

    public boolean containsKey(Object key) {
        if (key instanceof FString) {
            return super.containsKey(key.toString());
        }
        return super.containsKey(key);
    }

    public boolean containsValue(Object value) {
        if (value instanceof FString) {
            return super.containsValue(value.toString());
        }
        return super.containsValue(value);
    }

    public Object get(Object key) {
        if (key instanceof FString) {
            return super.get(key.toString());
        }
        return super.get(key);
    }

    public Object put(Object key, Object value) {
        if (key instanceof FString) {
            key = key.toString();
        }
        if (value instanceof FString) {
            value = value.toString();
        }
        return super.put(key, value);
    }

    public Object remove(Object key) {
        if (key instanceof FString) {
            return super.remove(key.toString());
        }
        return super.remove(key);
    }

    public String toString() {
        try {
            return JsonUtil.getJsonRepresentation(this);
        } catch (JSONException e) {
            throw new YailRuntimeError(e.getMessage(), "JSON Error");
        }
    }

    public Object getObject(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        int i = index;
        for (Map.Entry<Object, Object> e : entrySet()) {
            if (i == 0) {
                return Lists.newArrayList(e.getKey(), e.getValue());
            }
            i--;
        }
        throw new IndexOutOfBoundsException();
    }

    @NonNull
    public Iterator<YailList> iterator() {
        return new DictIterator(entrySet().iterator());
    }

    private static class DictIterator implements Iterator<YailList> {
        final Iterator<Map.Entry<Object, Object>> it;

        DictIterator(Iterator<Map.Entry<Object, Object>> it2) {
            this.it = it2;
        }

        public boolean hasNext() {
            return this.it.hasNext();
        }

        public YailList next() {
            Map.Entry<Object, Object> e = this.it.next();
            return YailList.makeList(new Object[]{e.getKey(), e.getValue()});
        }

        public void remove() {
            this.it.remove();
        }
    }
}
