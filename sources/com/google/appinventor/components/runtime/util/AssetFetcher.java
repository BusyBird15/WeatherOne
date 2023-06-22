package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ReplForm;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;

public class AssetFetcher {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = AssetFetcher.class.getSimpleName();
    private static ExecutorService background = Executors.newSingleThreadExecutor();
    private static Context context = ReplForm.getActiveForm();
    private static HashDatabase db = new HashDatabase(context);
    private static volatile boolean inError = false;
    private static final Object semaphore = new Object();

    private AssetFetcher() {
    }

    public static void fetchAssets(final String cookieValue, final String projectId, final String uri, final String asset) {
        background.submit(new Runnable() {
            public void run() {
                if (AssetFetcher.getFile(uri + "/ode/download/file/" + projectId + "/" + asset, cookieValue, asset, 0) != null) {
                    RetValManager.assetTransferred(asset);
                }
            }
        });
    }

    public static void upgradeCompanion(final String cookieValue, final String inputUri) {
        background.submit(new Runnable() {
            public void run() {
                String[] parts = inputUri.split("/", 0);
                File assetFile = AssetFetcher.getFile(inputUri, cookieValue, parts[parts.length - 1], 0);
                if (assetFile != null) {
                    try {
                        Form form = Form.getActiveForm();
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setDataAndType(NougatUtil.getPackageUri(form, assetFile), "application/vnd.android.package-archive");
                        intent.setFlags(1);
                        form.startActivity(intent);
                    } catch (Exception e) {
                        Log.e(AssetFetcher.LOG_TAG, "ERROR_UNABLE_TO_GET", e);
                        RetValManager.sendError("Unable to Install new Companion Package.");
                    }
                }
            }
        });
    }

    public static void loadExtensions(String jsonString) {
        Log.d(LOG_TAG, "loadExtensions called jsonString = " + jsonString);
        try {
            ReplForm form = (ReplForm) Form.getActiveForm();
            JSONArray array = new JSONArray(jsonString);
            List<String> extensionsToLoad = new ArrayList<>();
            if (array.length() == 0) {
                Log.d(LOG_TAG, "loadExtensions: No Extensions");
                RetValManager.extensionsLoaded();
                return;
            }
            int i = 0;
            while (i < array.length()) {
                String extensionName = array.optString(i);
                if (extensionName != null) {
                    Log.d(LOG_TAG, "loadExtensions, extensionName = " + extensionName);
                    extensionsToLoad.add(extensionName);
                    i++;
                } else {
                    Log.e(LOG_TAG, "extensionName was null");
                    return;
                }
            }
            try {
                form.loadComponents(extensionsToLoad);
                RetValManager.extensionsLoaded();
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error in form.loadComponents", e);
            }
        } catch (JSONException e2) {
            Log.e(LOG_TAG, "JSON Exception parsing extension string", e2);
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File getFile(java.lang.String r22, java.lang.String r23, java.lang.String r24, int r25) {
        /*
            com.google.appinventor.components.runtime.Form r10 = com.google.appinventor.components.runtime.Form.getActiveForm()
            r19 = 1
            r0 = r25
            r1 = r19
            if (r0 <= r1) goto L_0x002e
            java.lang.Object r20 = semaphore
            monitor-enter(r20)
            boolean r19 = inError     // Catch:{ all -> 0x002b }
            if (r19 == 0) goto L_0x0016
            r14 = 0
            monitor-exit(r20)     // Catch:{ all -> 0x002b }
        L_0x0015:
            return r14
        L_0x0016:
            r19 = 1
            inError = r19     // Catch:{ all -> 0x002b }
            com.google.appinventor.components.runtime.util.AssetFetcher$3 r19 = new com.google.appinventor.components.runtime.util.AssetFetcher$3     // Catch:{ all -> 0x002b }
            r0 = r19
            r1 = r22
            r0.<init>(r1)     // Catch:{ all -> 0x002b }
            r0 = r19
            r10.runOnUiThread(r0)     // Catch:{ all -> 0x002b }
            r14 = 0
            monitor-exit(r20)     // Catch:{ all -> 0x002b }
            goto L_0x0015
        L_0x002b:
            r19 = move-exception
            monitor-exit(r20)     // Catch:{ all -> 0x002b }
            throw r19
        L_0x002e:
            r16 = 0
            java.io.File r14 = new java.io.File
            r19 = 1
            r0 = r19
            java.lang.String r19 = com.google.appinventor.components.runtime.util.QUtil.getReplAssetPath(r10, r0)
            java.lang.String r20 = "assets/"
            int r20 = r20.length()
            r0 = r24
            r1 = r20
            java.lang.String r20 = r0.substring(r1)
            r0 = r19
            r1 = r20
            r14.<init>(r0, r1)
            java.lang.String r19 = LOG_TAG
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "target file = "
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r14)
            java.lang.String r20 = r20.toString()
            android.util.Log.d(r19, r20)
            r9 = 0
            r7 = 0
            java.net.URL r18 = new java.net.URL     // Catch:{ Exception -> 0x0125 }
            r0 = r18
            r1 = r22
            r0.<init>(r1)     // Catch:{ Exception -> 0x0125 }
            java.net.URLConnection r5 = r18.openConnection()     // Catch:{ Exception -> 0x0125 }
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch:{ Exception -> 0x0125 }
            if (r5 == 0) goto L_0x01b6
            java.lang.String r19 = "Cookie"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0125 }
            r20.<init>()     // Catch:{ Exception -> 0x0125 }
            java.lang.String r21 = "AppInventor = "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0125 }
            r0 = r20
            r1 = r23
            java.lang.StringBuilder r20 = r0.append(r1)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0125 }
            r0 = r19
            r1 = r20
            r5.addRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0125 }
            com.google.appinventor.components.runtime.util.HashDatabase r19 = db     // Catch:{ Exception -> 0x0125 }
            r0 = r19
            r1 = r24
            com.google.appinventor.components.runtime.util.HashFile r11 = r0.getHashFile(r1)     // Catch:{ Exception -> 0x0125 }
            if (r11 == 0) goto L_0x00b5
            java.lang.String r19 = "If-None-Match"
            java.lang.String r20 = r11.getHash()     // Catch:{ Exception -> 0x0125 }
            r0 = r19
            r1 = r20
            r5.addRequestProperty(r0, r1)     // Catch:{ Exception -> 0x0125 }
        L_0x00b5:
            java.lang.String r19 = "GET"
            r0 = r19
            r5.setRequestMethod(r0)     // Catch:{ Exception -> 0x0125 }
            int r16 = r5.getResponseCode()     // Catch:{ Exception -> 0x0125 }
            java.lang.String r19 = LOG_TAG     // Catch:{ Exception -> 0x0125 }
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0125 }
            r20.<init>()     // Catch:{ Exception -> 0x0125 }
            java.lang.String r21 = "asset = "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0125 }
            r0 = r20
            r1 = r24
            java.lang.StringBuilder r20 = r0.append(r1)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r21 = " responseCode = "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0125 }
            r0 = r20
            r1 = r16
            java.lang.StringBuilder r20 = r0.append(r1)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0125 }
            android.util.Log.d(r19, r20)     // Catch:{ Exception -> 0x0125 }
            java.io.File r15 = r14.getParentFile()     // Catch:{ Exception -> 0x0125 }
            java.lang.String r19 = "ETag"
            r0 = r19
            java.lang.String r9 = r5.getHeaderField(r0)     // Catch:{ Exception -> 0x0125 }
            r19 = 304(0x130, float:4.26E-43)
            r0 = r16
            r1 = r19
            if (r0 == r1) goto L_0x0015
            boolean r19 = r15.exists()     // Catch:{ Exception -> 0x0125 }
            if (r19 != 0) goto L_0x0156
            boolean r19 = r15.mkdirs()     // Catch:{ Exception -> 0x0125 }
            if (r19 != 0) goto L_0x0156
            java.io.IOException r19 = new java.io.IOException     // Catch:{ Exception -> 0x0125 }
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0125 }
            r20.<init>()     // Catch:{ Exception -> 0x0125 }
            java.lang.String r21 = "Unable to create assets directory "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0125 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r15)     // Catch:{ Exception -> 0x0125 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0125 }
            r19.<init>(r20)     // Catch:{ Exception -> 0x0125 }
            throw r19     // Catch:{ Exception -> 0x0125 }
        L_0x0125:
            r6 = move-exception
            java.lang.String r19 = LOG_TAG
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "Exception while fetching "
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r20
            r1 = r22
            java.lang.StringBuilder r20 = r0.append(r1)
            java.lang.String r20 = r20.toString()
            r0 = r19
            r1 = r20
            android.util.Log.e(r0, r1, r6)
            int r19 = r25 + 1
            r0 = r22
            r1 = r23
            r2 = r24
            r3 = r19
            java.io.File r14 = getFile(r0, r1, r2, r3)
            goto L_0x0015
        L_0x0156:
            java.io.BufferedInputStream r12 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x0125 }
            java.io.InputStream r19 = r5.getInputStream()     // Catch:{ Exception -> 0x0125 }
            r20 = 4096(0x1000, float:5.74E-42)
            r0 = r19
            r1 = r20
            r12.<init>(r0, r1)     // Catch:{ Exception -> 0x0125 }
            java.io.BufferedOutputStream r13 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x0125 }
            java.io.FileOutputStream r19 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0125 }
            r0 = r19
            r0.<init>(r14)     // Catch:{ Exception -> 0x0125 }
            r20 = 4096(0x1000, float:5.74E-42)
            r0 = r19
            r1 = r20
            r13.<init>(r0, r1)     // Catch:{ Exception -> 0x0125 }
        L_0x0177:
            int r4 = r12.read()     // Catch:{ IOException -> 0x01a0 }
            r19 = -1
            r0 = r19
            if (r4 != r0) goto L_0x019c
            r13.flush()     // Catch:{ IOException -> 0x01a0 }
            r13.close()     // Catch:{ Exception -> 0x0125 }
        L_0x0187:
            r5.disconnect()     // Catch:{ Exception -> 0x0125 }
        L_0x018a:
            if (r7 == 0) goto L_0x01b8
            int r19 = r25 + 1
            r0 = r22
            r1 = r23
            r2 = r24
            r3 = r19
            java.io.File r14 = getFile(r0, r1, r2, r3)     // Catch:{ Exception -> 0x0125 }
            goto L_0x0015
        L_0x019c:
            r13.write(r4)     // Catch:{ IOException -> 0x01a0 }
            goto L_0x0177
        L_0x01a0:
            r6 = move-exception
            java.lang.String r19 = LOG_TAG     // Catch:{ all -> 0x01b1 }
            java.lang.String r20 = "copying assets"
            r0 = r19
            r1 = r20
            android.util.Log.e(r0, r1, r6)     // Catch:{ all -> 0x01b1 }
            r7 = 1
            r13.close()     // Catch:{ Exception -> 0x0125 }
            goto L_0x0187
        L_0x01b1:
            r19 = move-exception
            r13.close()     // Catch:{ Exception -> 0x0125 }
            throw r19     // Catch:{ Exception -> 0x0125 }
        L_0x01b6:
            r7 = 1
            goto L_0x018a
        L_0x01b8:
            r19 = 200(0xc8, float:2.8E-43)
            r0 = r16
            r1 = r19
            if (r0 != r1) goto L_0x01ec
            java.util.Date r17 = new java.util.Date
            r17.<init>()
            com.google.appinventor.components.runtime.util.HashFile r8 = new com.google.appinventor.components.runtime.util.HashFile
            r0 = r24
            r1 = r17
            r8.<init>((java.lang.String) r0, (java.lang.String) r9, (java.util.Date) r1)
            com.google.appinventor.components.runtime.util.HashDatabase r19 = db
            r0 = r19
            r1 = r24
            com.google.appinventor.components.runtime.util.HashFile r19 = r0.getHashFile(r1)
            if (r19 != 0) goto L_0x01e3
            com.google.appinventor.components.runtime.util.HashDatabase r19 = db
            r0 = r19
            r0.insertHashFile(r8)
            goto L_0x0015
        L_0x01e3:
            com.google.appinventor.components.runtime.util.HashDatabase r19 = db
            r0 = r19
            r0.updateHashFile(r8)
            goto L_0x0015
        L_0x01ec:
            r14 = 0
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.AssetFetcher.getFile(java.lang.String, java.lang.String, java.lang.String, int):java.io.File");
    }

    private static String byteArray2Hex(byte[] hash) {
        Formatter formatter = new Formatter();
        int length = hash.length;
        for (int i = 0; i < length; i++) {
            formatter.format("%02x", new Object[]{Byte.valueOf(hash[i])});
        }
        return formatter.toString();
    }
}
