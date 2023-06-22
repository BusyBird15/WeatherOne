package com.google.appinventor.components.runtime.multidex;

import android.app.Application;
import android.content.Context;

public class MultiDexApplication extends Application {
    public static boolean installed = false;

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this, true);
    }
}
