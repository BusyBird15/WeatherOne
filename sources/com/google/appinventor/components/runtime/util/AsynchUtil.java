package com.google.appinventor.components.runtime.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;

public class AsynchUtil {
    private static final String LOG_TAG = AsynchUtil.class.getSimpleName();

    public static void runAsynchronously(Runnable call) {
        new Thread(call).start();
    }

    public static void runAsynchronously(final Handler androidUIHandler, final Runnable call, final Runnable callback) {
        new Thread(new Runnable() {
            public void run() {
                call.run();
                if (callback != null) {
                    androidUIHandler.post(new Runnable() {
                        public void run() {
                            callback.run();
                        }
                    });
                }
            }
        }).start();
    }

    public static boolean isUiThread() {
        return Looper.getMainLooper().equals(Looper.myLooper());
    }

    public static <T> void finish(Synchronizer<T> result, Continuation<T> continuation) {
        Log.d(LOG_TAG, "Waiting for synchronizer result");
        result.waitfor();
        if (result.getThrowable() == null) {
            continuation.call(result.getResult());
            return;
        }
        Throwable e = result.getThrowable();
        if (e instanceof RuntimeException) {
            throw ((RuntimeException) e);
        }
        throw new YailRuntimeError(e.toString(), e.getClass().getSimpleName());
    }
}
