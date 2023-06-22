package com.google.appinventor.components.runtime.util;

import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.appinventor.components.runtime.ReplForm;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RetValManager {
    private static final String LOG_TAG = "RetValManager";
    private static final long TENSECONDS = 10000;
    private static ArrayList<JSONObject> currentArray = new ArrayList<>(10);
    private static final Object semaphore = new Object();

    private RetValManager() {
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void appendReturnValue(java.lang.String r6, java.lang.String r7, java.lang.String r8) {
        /*
            java.lang.Object r4 = semaphore
            monitor-enter(r4)
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x003e }
            r1.<init>()     // Catch:{ all -> 0x003e }
            java.lang.String r3 = "status"
            r1.put(r3, r7)     // Catch:{ JSONException -> 0x0034 }
            java.lang.String r3 = "type"
            java.lang.String r5 = "return"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0034 }
            java.lang.String r3 = "value"
            r1.put(r3, r8)     // Catch:{ JSONException -> 0x0034 }
            java.lang.String r3 = "blockid"
            r1.put(r3, r6)     // Catch:{ JSONException -> 0x0034 }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x003e }
            boolean r2 = r3.isEmpty()     // Catch:{ all -> 0x003e }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x003e }
            r3.add(r1)     // Catch:{ all -> 0x003e }
            boolean r3 = com.google.appinventor.components.runtime.PhoneStatus.getUseWebRTC()     // Catch:{ all -> 0x003e }
            if (r3 == 0) goto L_0x0041
            webRTCsendCurrent()     // Catch:{ all -> 0x003e }
        L_0x0032:
            monitor-exit(r4)     // Catch:{ all -> 0x003e }
        L_0x0033:
            return
        L_0x0034:
            r0 = move-exception
            java.lang.String r3 = "RetValManager"
            java.lang.String r5 = "Error building retval"
            android.util.Log.e(r3, r5, r0)     // Catch:{ all -> 0x003e }
            monitor-exit(r4)     // Catch:{ all -> 0x003e }
            goto L_0x0033
        L_0x003e:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003e }
            throw r3
        L_0x0041:
            if (r2 == 0) goto L_0x0032
            java.lang.Object r3 = semaphore     // Catch:{ all -> 0x003e }
            r3.notifyAll()     // Catch:{ all -> 0x003e }
            goto L_0x0032
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.RetValManager.appendReturnValue(java.lang.String, java.lang.String, java.lang.String):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void sendError(java.lang.String r6) {
        /*
            java.lang.Object r4 = semaphore
            monitor-enter(r4)
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x003b }
            r1.<init>()     // Catch:{ all -> 0x003b }
            java.lang.String r3 = "status"
            java.lang.String r5 = "OK"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0031 }
            java.lang.String r3 = "type"
            java.lang.String r5 = "error"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0031 }
            java.lang.String r3 = "value"
            r1.put(r3, r6)     // Catch:{ JSONException -> 0x0031 }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x003b }
            boolean r2 = r3.isEmpty()     // Catch:{ all -> 0x003b }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x003b }
            r3.add(r1)     // Catch:{ all -> 0x003b }
            boolean r3 = com.google.appinventor.components.runtime.PhoneStatus.getUseWebRTC()     // Catch:{ all -> 0x003b }
            if (r3 == 0) goto L_0x003e
            webRTCsendCurrent()     // Catch:{ all -> 0x003b }
        L_0x002f:
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
        L_0x0030:
            return
        L_0x0031:
            r0 = move-exception
            java.lang.String r3 = "RetValManager"
            java.lang.String r5 = "Error building retval"
            android.util.Log.e(r3, r5, r0)     // Catch:{ all -> 0x003b }
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            goto L_0x0030
        L_0x003b:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x003b }
            throw r3
        L_0x003e:
            if (r2 == 0) goto L_0x002f
            java.lang.Object r3 = semaphore     // Catch:{ all -> 0x003b }
            r3.notifyAll()     // Catch:{ all -> 0x003b }
            goto L_0x002f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.RetValManager.sendError(java.lang.String):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void pushScreen(java.lang.String r6, java.lang.Object r7) {
        /*
            java.lang.Object r4 = semaphore
            monitor-enter(r4)
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x0046 }
            r1.<init>()     // Catch:{ all -> 0x0046 }
            java.lang.String r3 = "status"
            java.lang.String r5 = "OK"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x003c }
            java.lang.String r3 = "type"
            java.lang.String r5 = "pushScreen"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x003c }
            java.lang.String r3 = "screen"
            r1.put(r3, r6)     // Catch:{ JSONException -> 0x003c }
            if (r7 == 0) goto L_0x0026
            java.lang.String r3 = "value"
            java.lang.String r5 = r7.toString()     // Catch:{ JSONException -> 0x003c }
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x003c }
        L_0x0026:
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0046 }
            boolean r2 = r3.isEmpty()     // Catch:{ all -> 0x0046 }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0046 }
            r3.add(r1)     // Catch:{ all -> 0x0046 }
            boolean r3 = com.google.appinventor.components.runtime.PhoneStatus.getUseWebRTC()     // Catch:{ all -> 0x0046 }
            if (r3 == 0) goto L_0x0049
            webRTCsendCurrent()     // Catch:{ all -> 0x0046 }
        L_0x003a:
            monitor-exit(r4)     // Catch:{ all -> 0x0046 }
        L_0x003b:
            return
        L_0x003c:
            r0 = move-exception
            java.lang.String r3 = "RetValManager"
            java.lang.String r5 = "Error building retval"
            android.util.Log.e(r3, r5, r0)     // Catch:{ all -> 0x0046 }
            monitor-exit(r4)     // Catch:{ all -> 0x0046 }
            goto L_0x003b
        L_0x0046:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0046 }
            throw r3
        L_0x0049:
            if (r2 == 0) goto L_0x003a
            java.lang.Object r3 = semaphore     // Catch:{ all -> 0x0046 }
            r3.notifyAll()     // Catch:{ all -> 0x0046 }
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.RetValManager.pushScreen(java.lang.String, java.lang.Object):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void popScreen(java.lang.String r6) {
        /*
            java.lang.Object r4 = semaphore
            monitor-enter(r4)
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x0041 }
            r1.<init>()     // Catch:{ all -> 0x0041 }
            java.lang.String r3 = "status"
            java.lang.String r5 = "OK"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0037 }
            java.lang.String r3 = "type"
            java.lang.String r5 = "popScreen"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0037 }
            if (r6 == 0) goto L_0x0021
            java.lang.String r3 = "value"
            java.lang.String r5 = r6.toString()     // Catch:{ JSONException -> 0x0037 }
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0037 }
        L_0x0021:
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0041 }
            boolean r2 = r3.isEmpty()     // Catch:{ all -> 0x0041 }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0041 }
            r3.add(r1)     // Catch:{ all -> 0x0041 }
            boolean r3 = com.google.appinventor.components.runtime.PhoneStatus.getUseWebRTC()     // Catch:{ all -> 0x0041 }
            if (r3 == 0) goto L_0x0044
            webRTCsendCurrent()     // Catch:{ all -> 0x0041 }
        L_0x0035:
            monitor-exit(r4)     // Catch:{ all -> 0x0041 }
        L_0x0036:
            return
        L_0x0037:
            r0 = move-exception
            java.lang.String r3 = "RetValManager"
            java.lang.String r5 = "Error building retval"
            android.util.Log.e(r3, r5, r0)     // Catch:{ all -> 0x0041 }
            monitor-exit(r4)     // Catch:{ all -> 0x0041 }
            goto L_0x0036
        L_0x0041:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0041 }
            throw r3
        L_0x0044:
            if (r2 == 0) goto L_0x0035
            java.lang.Object r3 = semaphore     // Catch:{ all -> 0x0041 }
            r3.notifyAll()     // Catch:{ all -> 0x0041 }
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.RetValManager.popScreen(java.lang.String):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void assetTransferred(java.lang.String r6) {
        /*
            java.lang.Object r4 = semaphore
            monitor-enter(r4)
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x0041 }
            r1.<init>()     // Catch:{ all -> 0x0041 }
            java.lang.String r3 = "status"
            java.lang.String r5 = "OK"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0037 }
            java.lang.String r3 = "type"
            java.lang.String r5 = "assetTransferred"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0037 }
            if (r6 == 0) goto L_0x0021
            java.lang.String r3 = "value"
            java.lang.String r5 = r6.toString()     // Catch:{ JSONException -> 0x0037 }
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x0037 }
        L_0x0021:
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0041 }
            boolean r2 = r3.isEmpty()     // Catch:{ all -> 0x0041 }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0041 }
            r3.add(r1)     // Catch:{ all -> 0x0041 }
            boolean r3 = com.google.appinventor.components.runtime.PhoneStatus.getUseWebRTC()     // Catch:{ all -> 0x0041 }
            if (r3 == 0) goto L_0x0044
            webRTCsendCurrent()     // Catch:{ all -> 0x0041 }
        L_0x0035:
            monitor-exit(r4)     // Catch:{ all -> 0x0041 }
        L_0x0036:
            return
        L_0x0037:
            r0 = move-exception
            java.lang.String r3 = "RetValManager"
            java.lang.String r5 = "Error building retval"
            android.util.Log.e(r3, r5, r0)     // Catch:{ all -> 0x0041 }
            monitor-exit(r4)     // Catch:{ all -> 0x0041 }
            goto L_0x0036
        L_0x0041:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0041 }
            throw r3
        L_0x0044:
            if (r2 == 0) goto L_0x0035
            java.lang.Object r3 = semaphore     // Catch:{ all -> 0x0041 }
            r3.notifyAll()     // Catch:{ all -> 0x0041 }
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.RetValManager.assetTransferred(java.lang.String):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void extensionsLoaded() {
        /*
            java.lang.Object r4 = semaphore
            monitor-enter(r4)
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ all -> 0x0036 }
            r1.<init>()     // Catch:{ all -> 0x0036 }
            java.lang.String r3 = "status"
            java.lang.String r5 = "OK"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x002c }
            java.lang.String r3 = "type"
            java.lang.String r5 = "extensionsLoaded"
            r1.put(r3, r5)     // Catch:{ JSONException -> 0x002c }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0036 }
            boolean r2 = r3.isEmpty()     // Catch:{ all -> 0x0036 }
            java.util.ArrayList<org.json.JSONObject> r3 = currentArray     // Catch:{ all -> 0x0036 }
            r3.add(r1)     // Catch:{ all -> 0x0036 }
            boolean r3 = com.google.appinventor.components.runtime.PhoneStatus.getUseWebRTC()     // Catch:{ all -> 0x0036 }
            if (r3 == 0) goto L_0x0039
            webRTCsendCurrent()     // Catch:{ all -> 0x0036 }
        L_0x002a:
            monitor-exit(r4)     // Catch:{ all -> 0x0036 }
        L_0x002b:
            return
        L_0x002c:
            r0 = move-exception
            java.lang.String r3 = "RetValManager"
            java.lang.String r5 = "Error building retval"
            android.util.Log.e(r3, r5, r0)     // Catch:{ all -> 0x0036 }
            monitor-exit(r4)     // Catch:{ all -> 0x0036 }
            goto L_0x002b
        L_0x0036:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0036 }
            throw r3
        L_0x0039:
            if (r2 == 0) goto L_0x002a
            java.lang.Object r3 = semaphore     // Catch:{ all -> 0x0036 }
            r3.notifyAll()     // Catch:{ all -> 0x0036 }
            goto L_0x002a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.RetValManager.extensionsLoaded():void");
    }

    public static String fetch(boolean block) {
        String str;
        long startTime = System.currentTimeMillis();
        synchronized (semaphore) {
            while (currentArray.isEmpty() && block && System.currentTimeMillis() - startTime <= 9900) {
                try {
                    semaphore.wait(TENSECONDS);
                } catch (InterruptedException e) {
                }
            }
            JSONArray arrayoutput = new JSONArray(currentArray);
            JSONObject output = new JSONObject();
            try {
                output.put(NotificationCompat.CATEGORY_STATUS, "OK");
                output.put("values", arrayoutput);
                currentArray.clear();
                str = output.toString();
            } catch (JSONException e2) {
                Log.e(LOG_TAG, "Error fetching retvals", e2);
                str = "{\"status\" : \"BAD\", \"message\" : \"Failure in RetValManager\"}";
            }
        }
        return str;
    }

    private static void webRTCsendCurrent() {
        try {
            JSONObject output = new JSONObject();
            output.put(NotificationCompat.CATEGORY_STATUS, "OK");
            output.put("values", new JSONArray(currentArray));
            ReplForm.returnRetvals(output.toString());
            currentArray.clear();
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error building retval", e);
        }
    }
}
