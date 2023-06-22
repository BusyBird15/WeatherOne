package com.google.appinventor.components.runtime.util;

import android.util.Log;
import com.google.appinventor.components.runtime.repackaged.org.json.HTTP;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class IOUtils {
    private static final int BUFFER_LENGTH = 4096;

    public static void closeQuietly(String tag, Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.w(tag, "Failed to close resource", e);
            }
        }
    }

    public static byte[] readStream(InputStream fis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        while (true) {
            int read = fis.read(buffer);
            if (read <= 0) {
                return baos.toByteArray();
            }
            baos.write(buffer, 0, read);
        }
    }

    public static String readStreamAsString(InputStream fis, String encoding) throws IOException {
        return new String(readStream(fis), encoding);
    }

    public static String readStreamAsString(InputStream fis) throws IOException {
        return readStreamAsString(fis, "UTF-8");
    }

    public static String readReader(InputStreamReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[4096];
        while (true) {
            int read = reader.read(buffer);
            if (read <= 0) {
                return sb.toString();
            }
            sb.append(buffer, 0, read);
        }
    }

    public static String normalizeNewLines(String s) {
        return s.replaceAll(HTTP.CRLF, "\n");
    }

    public static void mkdirs(File file) throws IOException {
        File directory = file.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Unable to create directory for " + file);
        }
    }
}
