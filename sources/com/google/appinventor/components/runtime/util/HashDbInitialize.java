package com.google.appinventor.components.runtime.util;

import android.provider.BaseColumns;

public class HashDbInitialize {

    public static class HashTable implements BaseColumns {
        public static final String COLUMN_1_NAME = "fileName";
        public static final String COLUMN_2_NAME = "hashFile";
        public static final String COLUMN_3_NAME = "timeStamp";
        public static final String TABLE_NAME = "HashDatabase";
    }

    private HashDbInitialize() {
    }
}
