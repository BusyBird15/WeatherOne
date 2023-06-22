package xyz.kumaraswamy.itoo;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Data {
    private final File filesDir;

    public Data(Context context) {
        this.filesDir = context.getFilesDir();
    }

    public Data(Context context, String str) {
        this.filesDir = new File(context.getFilesDir(), "/" + str + "/");
        this.filesDir.mkdirs();
    }

    public Data put(String str, String str2) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.filesDir, str));
        fileOutputStream.write(str2.getBytes());
        fileOutputStream.close();
        return this;
    }

    public String get(String str) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(this.filesDir, str));
        byte[] bArr = new byte[fileInputStream.available()];
        fileInputStream.read(bArr);
        return new String(bArr);
    }

    public void delete(String str) {
        File file = new File(this.filesDir, str);
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean exists(String str) {
        return new File(this.filesDir, str).exists();
    }
}
