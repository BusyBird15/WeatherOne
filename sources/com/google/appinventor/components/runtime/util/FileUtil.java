package com.google.appinventor.components.runtime.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.errors.RuntimeError;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressLint({"InlinedApi"})
public class FileUtil {
    private static final String DIRECTORY_DOWNLOADS = "Downloads";
    private static final String DIRECTORY_PICTURES = "Pictures";
    private static final String DIRECTORY_RECORDINGS = "Recordings";
    private static final String DOCUMENT_DIRECTORY = "My Documents/";
    private static final String FILENAME_PREFIX = "app_inventor_";
    private static final String LOG_TAG = FileUtil.class.getSimpleName();
    public static final int MIN_SDK_FOR_APP_SPECIFIC_DIRS = 29;

    private FileUtil() {
    }

    public static String getFileUrl(String localFileName) {
        return new File(localFileName).toURI().toString();
    }

    @Deprecated
    public static byte[] readFile(String inputFileName) throws IOException {
        Log.w(LOG_TAG, "Calling deprecated function readFile", new IllegalAccessException());
        return readFile(Form.getActiveForm(), inputFileName);
    }

    public static byte[] readFile(Form form, String inputFileName) throws IOException {
        InputStream inputStream;
        if (inputFileName.startsWith("file://")) {
            inputFileName = inputFileName.substring(7);
        }
        InputStream inputStream2 = null;
        try {
            if (inputFileName.startsWith("/android_asset/")) {
                inputStream = form.openAsset(inputFileName.substring(inputFileName.lastIndexOf(47) + 1));
            } else if (!new File(inputFileName).isFile()) {
                throw new FileNotFoundException("Cannot find file: " + inputFileName);
            } else {
                inputStream = openFile(form, inputFileName);
            }
            return IOUtils.readStream(inputStream2);
        } finally {
            IOUtils.closeQuietly(LOG_TAG, inputStream2);
        }
    }

    @Deprecated
    public static FileInputStream openFile(String fileName) throws IOException, PermissionException {
        Log.w(LOG_TAG, "Calling deprecated function openFile", new IllegalAccessException());
        return openFile(Form.getActiveForm(), fileName);
    }

    public static FileInputStream openFile(Form form, String fileName) throws IOException, PermissionException {
        String fileUri;
        if (fileName.startsWith("/")) {
            fileUri = "file://" + fileName;
        } else {
            fileUri = fileName;
        }
        if (needsReadPermission(form, fileUri)) {
            form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
        }
        return new FileInputStream(fileName);
    }

    @Deprecated
    public static FileInputStream openFile(File file) throws IOException, PermissionException {
        Log.w(LOG_TAG, "Calling deprecated function openFile", new IllegalAccessException());
        return openFile(Form.getActiveForm(), file.getAbsolutePath());
    }

    public static FileInputStream openFile(Form form, File file) throws IOException, PermissionException {
        return openFile(form, file.getAbsolutePath());
    }

    @Deprecated
    public static FileInputStream openFile(URI fileUri) throws IOException, PermissionException {
        Log.w(LOG_TAG, "Calling deprecated function openFile", new IllegalAccessException());
        return openFile(Form.getActiveForm(), fileUri);
    }

    public static FileInputStream openFile(Form form, URI fileUri) throws IOException, PermissionException {
        if (needsPermission(form, fileUri.toString())) {
            form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
        }
        return new FileInputStream(new File(fileUri));
    }

    public static String downloadUrlToFile(String url, String outputFileName) throws IOException {
        InputStream in = new URL(url).openStream();
        try {
            return writeStreamToFile(in, outputFileName);
        } finally {
            in.close();
        }
    }

    public static String writeFile(byte[] array, String outputFileName) throws IOException {
        InputStream in = new ByteArrayInputStream(array);
        try {
            return writeStreamToFile(in, outputFileName);
        } finally {
            in.close();
        }
    }

    public static String copyFile(String inputFileName, String outputFileName) throws IOException {
        InputStream in = new FileInputStream(inputFileName);
        try {
            return writeStreamToFile(in, outputFileName);
        } finally {
            in.close();
        }
    }

    public static boolean copyFile(Form form, ScopedFile src, ScopedFile dest) throws IOException {
        if (Build.VERSION.SDK_INT < 24 || src.getScope() == FileScope.Shared || dest.getScope() == FileScope.Shared) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = openForReading(form, src);
                out = openForWriting(form, src);
                byte[] buffer = new byte[4096];
                while (true) {
                    int read = in.read(buffer);
                    if (read <= 0) {
                        break;
                    }
                    out.write(buffer, 0, read);
                }
            } finally {
                IOUtils.closeQuietly(LOG_TAG, in);
                IOUtils.closeQuietly(LOG_TAG, out);
            }
        } else {
            Files.copy(Paths.get(src.resolve(form).toURI()), Paths.get(dest.resolve(form).toURI()), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
        }
        return true;
    }

    public static String writeStreamToFile(InputStream in, String outputFileName) throws IOException {
        File file = new File(outputFileName);
        file.getParentFile().mkdirs();
        OutputStream out = new FileOutputStream(file);
        try {
            copy(in, out);
            return file.toURI().toString();
        } finally {
            out.flush();
            out.close();
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        OutputStream out2 = new BufferedOutputStream(out, 4096);
        InputStream in2 = new BufferedInputStream(in, 4096);
        while (true) {
            int b = in2.read();
            if (b == -1) {
                out2.flush();
                return;
            }
            out2.write(b);
        }
    }

    @Deprecated
    public static File getPictureFile(String extension) throws IOException, FileException {
        Log.w(LOG_TAG, "Calling deprecated function getPictureFile", new IllegalAccessException());
        return getPictureFile(Form.getActiveForm(), extension);
    }

    public static File getPictureFile(Form form, String extension) throws IOException, FileException {
        return getFile(form, DIRECTORY_PICTURES, extension);
    }

    public static ScopedFile getScopedPictureFile(Form form, String extension) {
        return getScopedFile(form, DIRECTORY_PICTURES, extension);
    }

    @Deprecated
    public static File getRecordingFile(String extension) throws IOException, FileException {
        return getRecordingFile(Form.getActiveForm(), extension);
    }

    public static File getRecordingFile(Form form, String extension) throws IOException, FileException {
        return getFile(form, DIRECTORY_RECORDINGS, extension);
    }

    @Deprecated
    public static File getDownloadFile(String extension) throws IOException, FileException {
        Log.w(LOG_TAG, "Calling deprecated function getDownloadFile", new IllegalAccessException());
        return getDownloadFile(Form.getActiveForm(), extension);
    }

    public static File getDownloadFile(Form form, String extension) throws IOException, FileException {
        return getFile(form, DIRECTORY_DOWNLOADS, extension);
    }

    private static File getFile(Form form, String category, String extension) throws IOException, FileException {
        File target = getExternalFile(form, DOCUMENT_DIRECTORY + category + "/" + FILENAME_PREFIX + System.currentTimeMillis() + "." + extension);
        File parent = target.getParentFile();
        if (parent.exists() || parent.mkdirs()) {
            return target;
        }
        throw new IOException("Unable to create directory: " + parent.getAbsolutePath());
    }

    private static ScopedFile getScopedFile(Form form, String category, String extension) {
        String fullPath;
        FileScope scope = form.DefaultFileScope();
        if (scope == FileScope.Legacy) {
            fullPath = "/My Documents/" + category;
        } else {
            fullPath = category;
            if (scope == FileScope.Asset) {
                scope = FileScope.Private;
            }
        }
        return new ScopedFile(scope, fullPath + "/app_inventor_" + System.currentTimeMillis() + "." + extension);
    }

    @Deprecated
    public static File getExternalFile(String fileName) throws IOException, FileException, SecurityException {
        return getExternalFile(Form.getActiveForm(), fileName);
    }

    public static File getExternalFile(Form form, String fileName) throws FileException {
        if (form.DefaultFileScope() == FileScope.Legacy && !fileName.startsWith("/")) {
            fileName = "/" + fileName;
        }
        String uri = resolveFileName(form, fileName, form.DefaultFileScope());
        if (isExternalStorageUri(form, uri)) {
            checkExternalStorageWriteable();
        }
        if (needsPermission(form, uri)) {
            form.assertPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        }
        return new File(URI.create(uri));
    }

    public static File getExternalFile(Form form, String fileName, boolean mkdirs, boolean overwrite) throws IOException, FileException {
        File file = getExternalFile(form, fileName);
        File directory = file.getParentFile();
        if (mkdirs && !directory.exists() && !directory.mkdirs()) {
            throw new IOException("Unable to create directory " + directory.getAbsolutePath());
        } else if (!overwrite || !file.exists() || file.delete()) {
            return file;
        } else {
            throw new IOException("Cannot overwrite existing file " + file.getAbsolutePath());
        }
    }

    public static File getExternalFile(Form form, String fileName, FileScope scope) throws FileException, PermissionException {
        return new File(URI.create(resolveFileName(form, fileName, scope)));
    }

    public static File getExternalFile(Form form, String fileName, FileScope scope, FileAccessMode accessMode, boolean mkdirs) throws IOException, FileException, PermissionException {
        File file = getExternalFile(form, fileName, scope);
        if (mkdirs && accessMode != FileAccessMode.READ) {
            File directory = file.getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Unable to create directory " + directory.getAbsolutePath());
            }
        }
        return file;
    }

    public static void checkExternalStorageWriteable() throws FileException {
        String state = Environment.getExternalStorageState();
        if (!"mounted".equals(state)) {
            if ("mounted_ro".equals(state)) {
                throw new FileException(ErrorMessages.ERROR_MEDIA_EXTERNAL_STORAGE_READONLY);
            }
            throw new FileException(ErrorMessages.ERROR_MEDIA_EXTERNAL_STORAGE_NOT_AVAILABLE);
        }
    }

    public static String resolveFileName(Form form, String fileName, FileScope scope) {
        File path;
        if (fileName.startsWith("//")) {
            path = new File(form.getAssetPath(fileName.substring(2)).substring(7));
        } else if (scope == FileScope.App && Build.VERSION.SDK_INT >= 8) {
            path = new File(form.getExternalFilesDir(""), fileName);
        } else if (scope == FileScope.Asset) {
            path = new File(form.getAssetPath(fileName).substring(7));
        } else if (scope == FileScope.Cache) {
            path = new File(form.getCachePath(fileName).substring(7));
        } else if ((scope == FileScope.Legacy || scope == null) && fileName.startsWith("/")) {
            path = new File(QUtil.getExternalStorageDir(form, false, true), fileName.substring(1));
        } else if (scope == FileScope.Private) {
            path = new File(form.getPrivatePath(fileName).substring(7));
        } else if (scope == FileScope.Shared) {
            path = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
        } else if (!fileName.startsWith("/")) {
            path = new File(form.getPrivatePath(fileName).substring(7));
        } else {
            path = getExternalFile(form, fileName.substring(1), scope);
        }
        return Uri.fromFile(path).toString();
    }

    public static String resolveFileName(Form form, ScopedFile file) {
        return resolveFileName(form, file.getFileName(), file.getScope());
    }

    public static boolean needsPermission(Form form, String fileUri) {
        if (isAssetUri(form, fileUri) || isPrivateUri(form, fileUri)) {
            return false;
        }
        if (!isAppSpecificExternalUri(form, fileUri)) {
            return isExternalStorageUri(form, fileUri);
        }
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        return false;
    }

    public static boolean needsReadPermission(Form form, String fileUri) {
        return needsPermission(form, fileUri);
    }

    public static boolean needsReadPermission(ScopedFile scopedFile) {
        switch (scopedFile.getScope()) {
            case Legacy:
            case Shared:
                return true;
            default:
                return false;
        }
    }

    public static boolean needsWritePermission(Form form, String fileUri) {
        if (Build.VERSION.SDK_INT >= 30) {
            return false;
        }
        return needsPermission(form, fileUri);
    }

    public static boolean needsWritePermission(ScopedFile scopedFile) {
        return needsWritePermission(scopedFile.getScope());
    }

    public static boolean needsWritePermission(FileScope scope) {
        switch (scope) {
            case Legacy:
            case Shared:
                if (Build.VERSION.SDK_INT < 30) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    public static boolean needsExternalStorage(Form form, ScopedFile scopedFile) {
        return isExternalStorageUri(form, resolveFileName(form, scopedFile.getFileName(), scopedFile.getScope()));
    }

    public static boolean isAssetUri(Form form, String fileUri) {
        return fileUri.startsWith(form.getAssetPath(""));
    }

    public static boolean isPrivateUri(Form form, String fileUri) {
        return fileUri.startsWith(form.getPrivatePath(""));
    }

    public static boolean isAppSpecificExternalUri(Form form, String fileUri) {
        if (Build.VERSION.SDK_INT < 8) {
            return false;
        }
        return fileUri.startsWith("file://" + form.getExternalFilesDir("").getAbsolutePath());
    }

    public static boolean isExternalStorageUri(Form form, String fileUri) {
        if (fileUri.startsWith("file:///sdcard/") || fileUri.startsWith("file:///storage") || fileUri.startsWith("file://" + Environment.getExternalStorageDirectory().getAbsolutePath()) || fileUri.startsWith("file://" + form.getExternalFilesDir("").getAbsolutePath())) {
            return true;
        }
        return false;
    }

    public static class FileException extends RuntimeError {
        private final int msgNumber;

        public FileException(int errorMsgNumber) {
            this.msgNumber = errorMsgNumber;
        }

        public int getErrorMessageNumber() {
            return this.msgNumber;
        }
    }

    public static String getNeededPermission(Form form, String path, FileAccessMode mode) {
        if (path == null) {
            throw new NullPointerException("path cannot be null");
        } else if (path.startsWith("file:") || path.startsWith("/")) {
            if (path.startsWith("/")) {
                path = "file://" + path;
            }
            if (!isExternalStorageUri(form, path)) {
                return null;
            }
            if (isAppSpecificExternalUri(form, path) && Build.VERSION.SDK_INT >= 19) {
                return null;
            }
            if (mode == FileAccessMode.READ) {
                return "android.permission.READ_EXTERNAL_STORAGE";
            }
            return "android.permission.WRITE_EXTERNAL_STORAGE";
        } else if (path.contains(":")) {
            return null;
        } else {
            throw new IllegalArgumentException("path cannot be relative");
        }
    }

    /* JADX INFO: finally extract failed */
    public static boolean moveFile(Form form, ScopedFile src, ScopedFile dest) throws IOException {
        if (Build.VERSION.SDK_INT < 26 || src.getScope() == FileScope.Shared || dest.getScope() == FileScope.Shared) {
            byte[] buffer = new byte[4096];
            try {
                InputStream in = openForReading(form, src);
                OutputStream out = openForWriting(form, dest);
                while (true) {
                    int read = in.read(buffer);
                    if (read <= 0) {
                        break;
                    }
                    out.write(buffer, 0, read);
                }
                IOUtils.closeQuietly(LOG_TAG, in);
                IOUtils.closeQuietly(LOG_TAG, out);
                File original = src.resolve(form);
                File copy = dest.resolve(form);
                if (original.delete()) {
                    return true;
                }
                if (copy.delete()) {
                    return false;
                }
                throw new IOException("Unable to delete fresh file");
            } catch (Throwable th) {
                IOUtils.closeQuietly(LOG_TAG, (Closeable) null);
                IOUtils.closeQuietly(LOG_TAG, (Closeable) null);
                throw th;
            }
        } else {
            Files.move(Paths.get(src.resolve(form).toURI()), Paths.get(dest.resolve(form).toURI()), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            return true;
        }
    }

    public static boolean removeDirectory(File directory, boolean recursive) throws IOException {
        boolean delete;
        if (directory == null) {
            throw new NullPointerException();
        } else if (!directory.isDirectory()) {
            throw new IllegalArgumentException();
        } else {
            File[] files = directory.listFiles();
            if (files == null) {
                return directory.delete();
            }
            if (!recursive && files.length > 0) {
                return false;
            }
            boolean success = true;
            for (File child : files) {
                if (child.isDirectory()) {
                    delete = removeDirectory(directory, recursive);
                } else {
                    delete = child.delete();
                }
                success &= delete;
            }
            if (!success || !directory.delete()) {
                return false;
            }
            return true;
        }
    }

    public static InputStream openForReading(Form form, ScopedFile file) throws IOException {
        switch (file.getScope()) {
            case Legacy:
                return new FileInputStream(new File(Environment.getExternalStorageDirectory(), file.getFileName()));
            case Shared:
                String[] parts = file.getFileName().split("/", 2);
                Uri contentUri = getContentUriForPath(parts[0]);
                String[] projection = {"_id", "_display_name"};
                Cursor cursor = null;
                try {
                    cursor = form.getContentResolver().query(contentUri, projection, "_display_name = ?", new String[]{parts[1]}, (String) null);
                    int idColumn = cursor.getColumnIndexOrThrow("_id");
                    if (!cursor.moveToFirst()) {
                        IOUtils.closeQuietly(LOG_TAG, cursor);
                        break;
                    } else {
                        return form.getContentResolver().openInputStream(ContentUris.withAppendedId(contentUri, cursor.getLong(idColumn)));
                    }
                } finally {
                    IOUtils.closeQuietly(LOG_TAG, cursor);
                }
            case Asset:
                return form.openAsset(file.getFileName());
            case App:
                if (Build.VERSION.SDK_INT < 8) {
                    return new FileInputStream(new File(Environment.getExternalStorageDirectory(), file.getFileName()));
                }
                return new FileInputStream(new File(form.getExternalFilesDir(""), file.getFileName()));
            case Cache:
                return new FileInputStream(new File(URI.create(form.getCachePath(file.getFileName()))));
            case Private:
                return new FileInputStream(new File(URI.create(form.getPrivatePath(file.getFileName()))));
        }
        throw new IOException("Unsupported file scope: " + file.getScope());
    }

    public static OutputStream openForWriting(Form form, ScopedFile file) throws IOException {
        switch (file.getScope()) {
            case Legacy:
                return new FileOutputStream(new File(Environment.getExternalStorageDirectory(), file.getFileName()));
            case Shared:
                String[] parts = file.getFileName().split("/", 2);
                ContentValues values = new ContentValues();
                values.put("_display_name", parts[1]);
                values.put("mime_type", "");
                values.put("relative_path", parts[0]);
                ContentResolver resolver = form.getContentResolver();
                Uri contentUri = getContentUriForPath(parts[0]);
                if (contentUri == null) {
                    throw new IOException("Unrecognized shared folder: " + parts[0]);
                }
                Uri uri = resolver.insert(contentUri, values);
                if (uri == null) {
                    throw new IOException("Unable to insert MediaStore entry for shared content");
                }
                OutputStream out = resolver.openOutputStream(uri);
                if (out != null) {
                    return out;
                }
                throw new IOException("Unable to open stream for writing");
            case Asset:
                throw new IOException("Assets are read-only.");
            case App:
                if (Build.VERSION.SDK_INT < 8) {
                    return new FileOutputStream(new File(Environment.getExternalStorageDirectory(), file.getFileName()));
                }
                return new FileOutputStream(new File(form.getExternalFilesDir(""), file.getFileName()));
            case Cache:
                return new FileOutputStream(new File(URI.create(form.getCachePath(file.getFileName()))));
            case Private:
                return new FileOutputStream(new File(URI.create(form.getPrivatePath(file.getFileName()))));
            default:
                throw new IOException("Unsupported file scope: " + file.getScope());
        }
    }

    public static List<String> listDirectory(Form form, ScopedFile file) throws IOException {
        switch (file.getScope()) {
            case Legacy:
            case App:
            case Cache:
            case Private:
                break;
            case Shared:
                String filename = file.getFileName();
                if (filename.startsWith("/")) {
                    filename = filename.substring(1);
                }
                String[] parts = filename.split("/", 2);
                ContentResolver resolver = form.getContentResolver();
                Uri contentUri = getContentUriForPath(parts[0]);
                if (contentUri == null) {
                    contentUri = MediaStore.Files.getContentUri("external");
                }
                Cursor cursor = null;
                try {
                    String dataColumnName = Build.VERSION.SDK_INT < 29 ? "_data" : "relative_path";
                    cursor = resolver.query(contentUri, new String[]{"_display_name", dataColumnName}, (String) null, (String[]) null, (String) null);
                    int nameColumn = cursor.getColumnIndex("_display_name");
                    int pathColumn = cursor.getColumnIndex(dataColumnName);
                    ArrayList arrayList = new ArrayList();
                    String rootPath = QUtil.getExternalStoragePath(form, false, true) + "/";
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(nameColumn);
                        String path = cursor.getString(pathColumn);
                        if (Build.VERSION.SDK_INT < 29) {
                            arrayList.add(path.replace(rootPath, ""));
                        } else {
                            arrayList.add(path + name);
                        }
                    }
                    return arrayList;
                } finally {
                    IOUtils.closeQuietly(LOG_TAG, cursor);
                }
            case Asset:
                if (!form.isRepl()) {
                    String[] files = form.getAssets().list(file.getFileName());
                    if (files != null) {
                        return Arrays.asList(files);
                    }
                    return Collections.emptyList();
                }
                break;
            default:
                throw new IOException("Unsupported file scope: " + file.getScope());
        }
        String[] files2 = new File(URI.create(resolveFileName(form, file.getFileName(), file.getScope()))).list();
        if (files2 != null) {
            return Arrays.asList(files2);
        }
        return null;
    }

    private static Uri getContentUriForPath(String path) {
        if ("DCIM".equals(path) || DIRECTORY_PICTURES.equals(path) || "Screenshots".equals(path)) {
            if (Build.VERSION.SDK_INT >= 29) {
                return MediaStore.Images.Media.getContentUri("external");
            }
            return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("Videos".equals(path) || "Movies".equals(path)) {
            if (Build.VERSION.SDK_INT >= 29) {
                return MediaStore.Video.Media.getContentUri("external");
            }
            return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("Audio".equals(path) || "Music".equals(path)) {
            if (Build.VERSION.SDK_INT >= 29) {
                return MediaStore.Audio.Media.getContentUri("external");
            }
            return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else if (Build.VERSION.SDK_INT >= 30 && ("Download".equals(path) || DIRECTORY_DOWNLOADS.equals(path))) {
            return MediaStore.Downloads.getContentUri("external");
        } else {
            if (Build.VERSION.SDK_INT >= 11) {
                return MediaStore.Files.getContentUri("external");
            }
            return null;
        }
    }
}
