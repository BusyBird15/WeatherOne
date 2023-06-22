package com.google.appinventor.components.runtime.util;

import android.os.Build;
import android.os.Environment;
import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Form;

public class RUtil {
    public static boolean needsFilePermission(Form form, String path, FileScope fileScope) {
        if (fileScope != null) {
            switch (fileScope) {
                case App:
                    if (Build.VERSION.SDK_INT >= 19) {
                        return false;
                    }
                    return true;
                case Asset:
                    if (!form.isRepl() || Build.VERSION.SDK_INT >= 19) {
                        return false;
                    }
                    return true;
                case Shared:
                    return true;
                default:
                    return false;
            }
        } else if (path.startsWith("//")) {
            return false;
        } else {
            if (!path.startsWith("/") && !path.startsWith("file:")) {
                return false;
            }
            if (Build.VERSION.SDK_INT < 8) {
                String fpath = path;
                if (path.startsWith("file:")) {
                    fpath = path.substring(5);
                }
                return fpath.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath());
            } else if (!FileUtil.isExternalStorageUri(form, path) || FileUtil.isAppSpecificExternalUri(form, path)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
