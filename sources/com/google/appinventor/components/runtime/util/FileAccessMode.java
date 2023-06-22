package com.google.appinventor.components.runtime.util;

import android.annotation.SuppressLint;

@SuppressLint({"InlinedApi"})
public enum FileAccessMode {
    READ("android.permission.READ_EXTERNAL_STORAGE"),
    WRITE("android.permission.WRITE_EXTERNAL_STORAGE"),
    APPEND("android.permission.WRITE_EXTERNAL_STORAGE");
    
    private final String permission;

    private FileAccessMode(String permission2) {
        this.permission = permission2;
    }

    public String getPermission() {
        return this.permission;
    }
}
