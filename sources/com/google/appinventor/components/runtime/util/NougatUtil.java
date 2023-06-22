package com.google.appinventor.components.runtime.util;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.google.appinventor.components.runtime.Form;
import java.io.File;

public final class NougatUtil {
    private static final String LOG_TAG = NougatUtil.class.getSimpleName();

    private NougatUtil() {
    }

    public static Uri getPackageUri(Form form, File apk) {
        if (Build.VERSION.SDK_INT < 24) {
            return Uri.fromFile(apk);
        }
        String packageName = form.$context().getPackageName();
        Log.d(LOG_TAG, "packageName = " + packageName);
        return FileProvider.getUriForFile(form.$context(), packageName + ".provider", apk);
    }
}
