package com.sunny.CustomWebView;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import androidx.core.app.NotificationCompat;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.OnDestroyListener;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SimpleObject(external = true)
@DesignerComponent(androidMinSdk = 21, category = ComponentCategory.EXTENSION, description = "Helper class of CustomWebView extension for downloading files <br> Developed by Sunny Gupta", helpUrl = "https://github.com/vknow360/CustomWebView", iconName = "https://res.cloudinary.com/andromedaviewflyvipul/image/upload/c_scale,h_20,w_20/v1571472765/ktvu4bapylsvnykoyhdm.png", nonVisible = true, version = 1, versionName = "1.2")
public class DownloadHelper extends AndroidNonvisibleComponent implements OnDestroyListener {
    public BroadcastReceiver completed = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getLongExtra("extra_download_id", -1) == DownloadHelper.this.lastRequestId) {
                DownloadHelper.this.DownloadCompleted();
            }
        }
    };
    private final Context context;
    /* access modifiers changed from: private */
    public final DownloadManager downloadManager;
    /* access modifiers changed from: private */
    public boolean isCancelled = false;
    /* access modifiers changed from: private */
    public long lastRequestId;
    private int nVisibility = 1;

    public DownloadHelper(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.context = componentContainer.$context();
        this.downloadManager = (DownloadManager) this.context.getSystemService("download");
        this.context.registerReceiver(this.completed, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
    }

    @SimpleFunction(description = "Cancels the current download request")
    public void Cancel() {
        this.downloadManager.remove(new long[]{this.lastRequestId});
        this.isCancelled = true;
        DownloadFailed();
    }

    @SimpleFunction(description = "Downloads the given file")
    public void Download(String str, String str2, String str3, String str4) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setMimeType(str2);
        request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(str));
        request.setDescription("Downloading file...");
        request.setTitle(str3);
        request.setNotificationVisibility(this.nVisibility);
        request.setTitle(str3);
        if (str4.startsWith("~")) {
            request.setDestinationInExternalFilesDir(this.context, str4.substring(1), str3);
        } else {
            request.setDestinationInExternalPublicDir(str4, str3);
        }
        this.lastRequestId = this.downloadManager.enqueue(request);
        DownloadStarted(this.lastRequestId);
        this.isCancelled = false;
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                boolean z = true;
                while (z) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(new long[]{DownloadHelper.this.lastRequestId});
                    Cursor query2 = DownloadHelper.this.downloadManager.query(query);
                    if (query2.moveToFirst()) {
                        int i = query2.getInt(query2.getColumnIndex(NotificationCompat.CATEGORY_STATUS));
                        if (i == 16 || DownloadHelper.this.isCancelled) {
                            boolean unused = DownloadHelper.this.isCancelled = true;
                            DownloadHelper.this.form.runOnUiThread(new Runnable() {
                                public void run() {
                                    DownloadHelper.this.DownloadFailed();
                                }
                            });
                            z = false;
                        } else {
                            int i2 = query2.getInt(query2.getColumnIndex("bytes_so_far"));
                            int i3 = query2.getInt(query2.getColumnIndex("total_size"));
                            query2.close();
                            if (i == 8) {
                                z = false;
                            }
                            final int i4 = (int) ((((long) i2) * 100) / ((long) i3));
                            DownloadHelper.this.form.runOnUiThread(new Runnable() {
                                public void run() {
                                    DownloadHelper.this.DownloadProgressChanged(i4);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @SimpleEvent(description = "Event invoked when downloading gets completed")
    public void DownloadCompleted() {
        EventDispatcher.dispatchEvent(this, "DownloadCompleted", new Object[0]);
    }

    @SimpleEvent(description = "Event invoked when downloading gets failed")
    public void DownloadFailed() {
        this.lastRequestId = 0;
        EventDispatcher.dispatchEvent(this, "DownloadFailed", new Object[0]);
    }

    @SimpleEvent(description = "Event invoked when downloading progress changes")
    public void DownloadProgressChanged(int i) {
        EventDispatcher.dispatchEvent(this, "DownloadProgressChanged", Integer.valueOf(i));
    }

    @SimpleEvent(description = "Event invoked when downloading starts")
    public void DownloadStarted(long j) {
        EventDispatcher.dispatchEvent(this, "DownloadStarted", Long.valueOf(j));
    }

    @SimpleFunction(description = "Tries to get file size")
    public void GetFileSize(final String str) {
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                final long[] jArr = new long[1];
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    httpURLConnection.getResponseCode();
                    jArr[0] = httpURLConnection.getContentLengthLong();
                } catch (IOException e) {
                    e.printStackTrace();
                    jArr[0] = -1;
                }
                DownloadHelper.this.form.runOnUiThread(new Runnable() {
                    public void run() {
                        DownloadHelper.this.GotFileSize(jArr[0]);
                    }
                });
            }
        });
    }

    @SimpleFunction
    public String GetMimeType(long j) {
        return this.downloadManager.getMimeTypeForDownloadedFile(j);
    }

    @SimpleFunction
    public String GetUriString(long j) {
        try {
            return this.downloadManager.getUriForDownloadedFile(j).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @SimpleEvent(description = "Event invoked after getting file size")
    public void GotFileSize(long j) {
        EventDispatcher.dispatchEvent(this, "GotFileSize", Long.valueOf(j));
    }

    @SimpleFunction(description = "Returns guessed file name")
    public String GuessFileName(String str, String str2, String str3) {
        return URLUtil.guessFileName(str, str3, str2);
    }

    @SimpleProperty(description = "Sets download notification visibility")
    public void NotificationVisibility(int i) {
        this.nVisibility = i;
    }

    public void onDestroy() {
        this.context.unregisterReceiver(this.completed);
    }
}
