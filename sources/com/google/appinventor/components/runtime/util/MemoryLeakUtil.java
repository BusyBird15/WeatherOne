package com.google.appinventor.components.runtime.util;

import android.util.Log;
import com.google.appinventor.components.runtime.collect.Maps;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryLeakUtil {
    private static final String LOG_TAG = "MemoryLeakUtil";
    private static final Map<String, WeakReference<Object>> TRACKED_OBJECTS = Maps.newTreeMap();
    private static final AtomicInteger prefixGenerator = new AtomicInteger(0);

    private MemoryLeakUtil() {
    }

    public static String trackObject(String tag, Object object) {
        String key;
        if (tag == null) {
            key = prefixGenerator.incrementAndGet() + "_";
        } else {
            key = prefixGenerator.incrementAndGet() + "_" + tag;
        }
        TRACKED_OBJECTS.put(key, new WeakReference(object));
        return key;
    }

    public static boolean isTrackedObjectCollected(String key, boolean stopTrackingIfCollected) {
        System.gc();
        WeakReference<Object> ref = TRACKED_OBJECTS.get(key);
        if (ref != null) {
            Object o = ref.get();
            Log.i(LOG_TAG, "Object with tag " + key.substring(key.indexOf("_") + 1) + " has " + (o != null ? "not " : "") + "been garbage collected.");
            if (stopTrackingIfCollected && o == null) {
                TRACKED_OBJECTS.remove(key);
            }
            if (o == null) {
                return true;
            }
            return false;
        }
        throw new IllegalArgumentException("key not found");
    }

    public static void checkAllTrackedObjects(boolean verbose, boolean stopTrackingCollectedObjects) {
        String str;
        Log.i(LOG_TAG, "Checking Tracked Objects ----------------------------------------");
        System.gc();
        int countRemaining = 0;
        int countCollected = 0;
        Iterator<Map.Entry<String, WeakReference<Object>>> it = TRACKED_OBJECTS.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, WeakReference<Object>> entry = it.next();
            String key = entry.getKey();
            Object o = entry.getValue().get();
            if (o != null) {
                countRemaining++;
            } else {
                countCollected++;
                if (stopTrackingCollectedObjects) {
                    it.remove();
                }
            }
            if (verbose) {
                StringBuilder append = new StringBuilder().append("Object with tag ").append(key.substring(key.indexOf("_") + 1)).append(" has ");
                if (o != null) {
                    str = "not ";
                } else {
                    str = "";
                }
                Log.i(LOG_TAG, append.append(str).append("been garbage collected.").toString());
            }
        }
        Log.i(LOG_TAG, "summary: collected " + countCollected);
        Log.i(LOG_TAG, "summary: remaining " + countRemaining);
        Log.i(LOG_TAG, "-----------------------------------------------------------------");
    }
}
