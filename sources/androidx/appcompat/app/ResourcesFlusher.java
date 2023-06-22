package androidx.appcompat.app;

import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import java.lang.reflect.Field;

class ResourcesFlusher {
    private static final String TAG = "ResourcesFlusher";
    private static Field sDrawableCacheField;
    private static boolean sDrawableCacheFieldFetched;
    private static Field sResourcesImplField;
    private static boolean sResourcesImplFieldFetched;
    private static Class sThemedResourceCacheClazz;
    private static boolean sThemedResourceCacheClazzFetched;
    private static Field sThemedResourceCache_mUnthemedEntriesField;
    private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;

    static void flush(@NonNull Resources resources) {
        if (Build.VERSION.SDK_INT < 28) {
            if (Build.VERSION.SDK_INT >= 24) {
                flushNougats(resources);
            } else if (Build.VERSION.SDK_INT >= 23) {
                flushMarshmallows(resources);
            } else if (Build.VERSION.SDK_INT >= 21) {
                flushLollipops(resources);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.util.Map} */
    /* JADX WARNING: Multi-variable type inference failed */
    @androidx.annotation.RequiresApi(21)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void flushLollipops(@androidx.annotation.NonNull android.content.res.Resources r6) {
        /*
            r5 = 1
            boolean r3 = sDrawableCacheFieldFetched
            if (r3 != 0) goto L_0x0017
            java.lang.Class<android.content.res.Resources> r3 = android.content.res.Resources.class
            java.lang.String r4 = "mDrawableCache"
            java.lang.reflect.Field r3 = r3.getDeclaredField(r4)     // Catch:{ NoSuchFieldException -> 0x002c }
            sDrawableCacheField = r3     // Catch:{ NoSuchFieldException -> 0x002c }
            java.lang.reflect.Field r3 = sDrawableCacheField     // Catch:{ NoSuchFieldException -> 0x002c }
            r4 = 1
            r3.setAccessible(r4)     // Catch:{ NoSuchFieldException -> 0x002c }
        L_0x0015:
            sDrawableCacheFieldFetched = r5
        L_0x0017:
            java.lang.reflect.Field r3 = sDrawableCacheField
            if (r3 == 0) goto L_0x002b
            r1 = 0
            java.lang.reflect.Field r3 = sDrawableCacheField     // Catch:{ IllegalAccessException -> 0x0035 }
            java.lang.Object r3 = r3.get(r6)     // Catch:{ IllegalAccessException -> 0x0035 }
            r0 = r3
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ IllegalAccessException -> 0x0035 }
            r1 = r0
        L_0x0026:
            if (r1 == 0) goto L_0x002b
            r1.clear()
        L_0x002b:
            return
        L_0x002c:
            r2 = move-exception
            java.lang.String r3 = "ResourcesFlusher"
            java.lang.String r4 = "Could not retrieve Resources#mDrawableCache field"
            android.util.Log.e(r3, r4, r2)
            goto L_0x0015
        L_0x0035:
            r2 = move-exception
            java.lang.String r3 = "ResourcesFlusher"
            java.lang.String r4 = "Could not retrieve value from Resources#mDrawableCache"
            android.util.Log.e(r3, r4, r2)
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.ResourcesFlusher.flushLollipops(android.content.res.Resources):void");
    }

    @RequiresApi(23)
    private static void flushMarshmallows(@NonNull Resources resources) {
        if (!sDrawableCacheFieldFetched) {
            try {
                sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
                sDrawableCacheField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Could not retrieve Resources#mDrawableCache field", e);
            }
            sDrawableCacheFieldFetched = true;
        }
        Object drawableCache = null;
        if (sDrawableCacheField != null) {
            try {
                drawableCache = sDrawableCacheField.get(resources);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "Could not retrieve value from Resources#mDrawableCache", e2);
            }
        }
        if (drawableCache != null) {
            flushThemedResourcesCache(drawableCache);
        }
    }

    @RequiresApi(24)
    private static void flushNougats(@NonNull Resources resources) {
        if (!sResourcesImplFieldFetched) {
            try {
                sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
                sResourcesImplField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Could not retrieve Resources#mResourcesImpl field", e);
            }
            sResourcesImplFieldFetched = true;
        }
        if (sResourcesImplField != null) {
            Object resourcesImpl = null;
            try {
                resourcesImpl = sResourcesImplField.get(resources);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "Could not retrieve value from Resources#mResourcesImpl", e2);
            }
            if (resourcesImpl != null) {
                if (!sDrawableCacheFieldFetched) {
                    try {
                        sDrawableCacheField = resourcesImpl.getClass().getDeclaredField("mDrawableCache");
                        sDrawableCacheField.setAccessible(true);
                    } catch (NoSuchFieldException e3) {
                        Log.e(TAG, "Could not retrieve ResourcesImpl#mDrawableCache field", e3);
                    }
                    sDrawableCacheFieldFetched = true;
                }
                Object drawableCache = null;
                if (sDrawableCacheField != null) {
                    try {
                        drawableCache = sDrawableCacheField.get(resourcesImpl);
                    } catch (IllegalAccessException e4) {
                        Log.e(TAG, "Could not retrieve value from ResourcesImpl#mDrawableCache", e4);
                    }
                }
                if (drawableCache != null) {
                    flushThemedResourcesCache(drawableCache);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.util.LongSparseArray} */
    /* JADX WARNING: Multi-variable type inference failed */
    @androidx.annotation.RequiresApi(16)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void flushThemedResourcesCache(@androidx.annotation.NonNull java.lang.Object r7) {
        /*
            r6 = 1
            boolean r4 = sThemedResourceCacheClazzFetched
            if (r4 != 0) goto L_0x000f
            java.lang.String r4 = "android.content.res.ThemedResourceCache"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ ClassNotFoundException -> 0x0014 }
            sThemedResourceCacheClazz = r4     // Catch:{ ClassNotFoundException -> 0x0014 }
        L_0x000d:
            sThemedResourceCacheClazzFetched = r6
        L_0x000f:
            java.lang.Class r4 = sThemedResourceCacheClazz
            if (r4 != 0) goto L_0x001d
        L_0x0013:
            return
        L_0x0014:
            r1 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r5 = "Could not find ThemedResourceCache class"
            android.util.Log.e(r4, r5, r1)
            goto L_0x000d
        L_0x001d:
            boolean r4 = sThemedResourceCache_mUnthemedEntriesFieldFetched
            if (r4 != 0) goto L_0x0033
            java.lang.Class r4 = sThemedResourceCacheClazz     // Catch:{ NoSuchFieldException -> 0x0048 }
            java.lang.String r5 = "mUnthemedEntries"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ NoSuchFieldException -> 0x0048 }
            sThemedResourceCache_mUnthemedEntriesField = r4     // Catch:{ NoSuchFieldException -> 0x0048 }
            java.lang.reflect.Field r4 = sThemedResourceCache_mUnthemedEntriesField     // Catch:{ NoSuchFieldException -> 0x0048 }
            r5 = 1
            r4.setAccessible(r5)     // Catch:{ NoSuchFieldException -> 0x0048 }
        L_0x0031:
            sThemedResourceCache_mUnthemedEntriesFieldFetched = r6
        L_0x0033:
            java.lang.reflect.Field r4 = sThemedResourceCache_mUnthemedEntriesField
            if (r4 == 0) goto L_0x0013
            r3 = 0
            java.lang.reflect.Field r4 = sThemedResourceCache_mUnthemedEntriesField     // Catch:{ IllegalAccessException -> 0x0051 }
            java.lang.Object r4 = r4.get(r7)     // Catch:{ IllegalAccessException -> 0x0051 }
            r0 = r4
            android.util.LongSparseArray r0 = (android.util.LongSparseArray) r0     // Catch:{ IllegalAccessException -> 0x0051 }
            r3 = r0
        L_0x0042:
            if (r3 == 0) goto L_0x0013
            r3.clear()
            goto L_0x0013
        L_0x0048:
            r2 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r5 = "Could not retrieve ThemedResourceCache#mUnthemedEntries field"
            android.util.Log.e(r4, r5, r2)
            goto L_0x0031
        L_0x0051:
            r1 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r5 = "Could not retrieve value from ThemedResourceCache#mUnthemedEntries"
            android.util.Log.e(r4, r5, r1)
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.ResourcesFlusher.flushThemedResourcesCache(java.lang.Object):void");
    }

    private ResourcesFlusher() {
    }
}
