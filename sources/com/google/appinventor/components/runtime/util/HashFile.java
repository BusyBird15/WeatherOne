package com.google.appinventor.components.runtime.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HashFile {
    private String fileName;
    private String hash;
    private String timestamp;

    public HashFile() {
    }

    public HashFile(String fileName2, String hash2, Date time) {
        this.fileName = fileName2;
        this.hash = hash2;
        this.timestamp = formatTimestamp(time);
    }

    public HashFile(String fileName2, String hash2, String timestamp2) {
        this.fileName = fileName2;
        this.hash = hash2;
        this.timestamp = timestamp2;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName2) {
        this.fileName = fileName2;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash2) {
        this.hash = hash2;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestampInDb(Date time) {
        this.timestamp = formatTimestamp(time);
    }

    public void setTimestamp(String timestamp2) {
        this.timestamp = timestamp2;
    }

    public String formatTimestamp(Date timestamp2) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(timestamp2);
    }
}
