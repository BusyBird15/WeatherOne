package com.google.appinventor.components.runtime.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;

public final class HashDatabase extends SQLiteOpenHelper {
    private static final String[] COLUMNS = {"fileName", "hashFile", "timeStamp"};
    public static final String DATABASE_NAME = "hashTable.db";
    public static final int DATABASE_VERSION = 1;
    private static final String KEY_HASH = "hashFile";
    private static final String KEY_NAME = "fileName";
    private static final String KEY_TIMESTAMP = "timeStamp";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE HashDatabase (fileName TEXT,hashFile TEXT,timeStamp TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS HashDatabase";
    private static final String TABLE_NAME = "HashDatabase";

    private static class ExternalContext extends ContextWrapper {
        Context base;

        public ExternalContext(Context base2) {
            super(base2);
            this.base = base2;
        }

        public File getDatabasePath(String name) {
            String dbfile = QUtil.getReplDatabasePath(this.base, true) + File.separator + name;
            if (!dbfile.endsWith(".db")) {
                dbfile = dbfile + ".db";
            }
            File result = new File(dbfile);
            if (!result.getParentFile().exists()) {
                result.getParentFile().mkdirs();
            }
            return result;
        }
    }

    public HashDatabase(Context context) {
        super(new ExternalContext(context), DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void deleteOne(HashFile hashFile) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("HashDatabase", "fileName = ?", new String[]{hashFile.getFileName()});
        db.close();
    }

    public HashFile getHashFile(String fileName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("HashDatabase", COLUMNS, " fileName = ?", new String[]{fileName}, (String) null, (String) null, (String) null, (String) null);
        Log.d("Database", cursor.toString());
        if (cursor == null || cursor.getCount() < 1) {
            cursor.close();
            db.close();
            return null;
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        HashFile hashFile = new HashFile();
        hashFile.setFileName(cursor.getString(0));
        hashFile.setHash(cursor.getString(1));
        hashFile.setTimestamp(cursor.getString(2));
        cursor.close();
        db.close();
        return hashFile;
    }

    public void insertHashFile(HashFile hashFile) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fileName", hashFile.getFileName());
        values.put("hashFile", hashFile.getHash());
        values.put("timeStamp", hashFile.getTimestamp());
        db.insert("HashDatabase", (String) null, values);
        db.close();
    }

    public int updateHashFile(HashFile hashFile) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fileName", hashFile.getFileName());
        values.put("hashFile", hashFile.getHash());
        values.put("timeStamp", hashFile.getTimestamp());
        int i = db.update("HashDatabase", values, "fileName = ?", new String[]{hashFile.getFileName()});
        db.close();
        return i;
    }
}
