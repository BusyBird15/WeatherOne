package com.google.appinventor.components.runtime.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.provider.Contacts;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;
import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ReplForm;
import com.google.appinventor.components.runtime.errors.PermissionException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MediaUtil {
    private static final String LOG_TAG = "MediaUtil";
    private static ConcurrentHashMap<String, String> pathCache = new ConcurrentHashMap<>(2);
    private static final Map<String, File> tempFileMap = new HashMap();

    private enum MediaSource {
        ASSET,
        REPL_ASSET,
        SDCARD,
        FILE_URL,
        URL,
        CONTENT_URI,
        CONTACT_URI
    }

    private MediaUtil() {
    }

    static String fileUrlToFilePath(String mediaPath) throws IOException {
        try {
            return new File(new URL(mediaPath).toURI()).getAbsolutePath();
        } catch (IllegalArgumentException e) {
            throw new IOException("Unable to determine file path of file url " + mediaPath);
        } catch (Exception e2) {
            throw new IOException("Unable to determine file path of file url " + mediaPath);
        }
    }

    @SuppressLint({"SdCardPath"})
    private static MediaSource determineMediaSource(Form form, String mediaPath) {
        if (mediaPath.startsWith(QUtil.getExternalStoragePath(form)) || mediaPath.startsWith("/sdcard/")) {
            return MediaSource.SDCARD;
        }
        if (mediaPath.startsWith("content://contacts/")) {
            return MediaSource.CONTACT_URI;
        }
        if (mediaPath.startsWith("content://")) {
            return MediaSource.CONTENT_URI;
        }
        try {
            new URL(mediaPath);
            if (mediaPath.startsWith("file:")) {
                return MediaSource.FILE_URL;
            }
            return MediaSource.URL;
        } catch (MalformedURLException e) {
            if (!(form instanceof ReplForm)) {
                return MediaSource.ASSET;
            }
            if (((ReplForm) form).isAssetsLoaded()) {
                return MediaSource.REPL_ASSET;
            }
            return MediaSource.ASSET;
        }
    }

    @SuppressLint({"SdCardPath"})
    @Deprecated
    public static boolean isExternalFileUrl(String mediaPath) {
        Log.w(LOG_TAG, "Calling deprecated version of isExternalFileUrl", new IllegalAccessException());
        return mediaPath.startsWith(new StringBuilder().append("file://").append(QUtil.getExternalStoragePath(Form.getActiveForm())).toString()) || mediaPath.startsWith("file:///sdcard/");
    }

    @SuppressLint({"SdCardPath"})
    public static boolean isExternalFileUrl(Context context, String mediaPath) {
        if (Build.VERSION.SDK_INT >= 29) {
            return false;
        }
        if (mediaPath.startsWith("file://" + QUtil.getExternalStorageDir(context)) || mediaPath.startsWith("file:///sdcard")) {
            return true;
        }
        return false;
    }

    @SuppressLint({"SdCardPath"})
    @Deprecated
    public static boolean isExternalFile(String mediaPath) {
        Log.w(LOG_TAG, "Calling deprecated version of isExternalFile", new IllegalAccessException());
        return mediaPath.startsWith(QUtil.getExternalStoragePath(Form.getActiveForm())) || mediaPath.startsWith("/sdcard/") || isExternalFileUrl(Form.getActiveForm(), mediaPath);
    }

    @SuppressLint({"SdCardPath"})
    public static boolean isExternalFile(Context context, String mediaPath) {
        if (Build.VERSION.SDK_INT >= 29) {
            return false;
        }
        if (mediaPath.startsWith(QUtil.getExternalStoragePath(context)) || mediaPath.startsWith("/sdcard/") || isExternalFileUrl(context, mediaPath)) {
            return true;
        }
        return false;
    }

    private static String findCaseinsensitivePath(Form form, String mediaPath) throws IOException {
        if (!pathCache.containsKey(mediaPath)) {
            String newPath = findCaseinsensitivePathWithoutCache(form, mediaPath);
            if (newPath == null) {
                return null;
            }
            pathCache.put(mediaPath, newPath);
        }
        return pathCache.get(mediaPath);
    }

    private static String findCaseinsensitivePathWithoutCache(Form form, String mediaPath) throws IOException {
        String[] mediaPathlist = form.getAssets().list("");
        int l = Array.getLength(mediaPathlist);
        for (int i = 0; i < l; i++) {
            String temp = mediaPathlist[i];
            if (temp.equalsIgnoreCase(mediaPath)) {
                return temp;
            }
        }
        return null;
    }

    private static InputStream getAssetsIgnoreCaseInputStream(Form form, String mediaPath) throws IOException {
        try {
            return form.getAssets().open(mediaPath);
        } catch (IOException e) {
            String path = findCaseinsensitivePath(form, mediaPath);
            if (path != null) {
                return form.getAssets().open(path);
            }
            throw e;
        }
    }

    /* access modifiers changed from: private */
    public static InputStream openMedia(Form form, String mediaPath, MediaSource mediaSource) throws IOException {
        InputStream is;
        switch (mediaSource) {
            case ASSET:
                return getAssetsIgnoreCaseInputStream(form, mediaPath);
            case REPL_ASSET:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                return new FileInputStream(new File(URI.create(form.getAssetPath(mediaPath))));
            case SDCARD:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                return new FileInputStream(mediaPath);
            case FILE_URL:
                if (isExternalFileUrl(form, mediaPath) && RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                    break;
                }
            case URL:
                break;
            case CONTENT_URI:
                return form.getContentResolver().openInputStream(Uri.parse(mediaPath));
            case CONTACT_URI:
                if (SdkLevel.getLevel() >= 12) {
                    is = HoneycombMR1Util.openContactPhotoInputStreamHelper(form.getContentResolver(), Uri.parse(mediaPath));
                } else {
                    is = Contacts.People.openContactPhotoInputStream(form.getContentResolver(), Uri.parse(mediaPath));
                }
                if (is != null) {
                    return is;
                }
                throw new IOException("Unable to open contact photo " + mediaPath + ".");
            default:
                throw new IOException("Unable to open media " + mediaPath + ".");
        }
        return new URL(mediaPath).openStream();
    }

    public static InputStream openMedia(Form form, String mediaPath) throws IOException {
        return openMedia(form, mediaPath, determineMediaSource(form, mediaPath));
    }

    public static File copyMediaToTempFile(Form form, String mediaPath) throws IOException {
        return copyMediaToTempFile(form, mediaPath, determineMediaSource(form, mediaPath));
    }

    private static File copyMediaToTempFile(Form form, String mediaPath, MediaSource mediaSource) throws IOException {
        InputStream in = openMedia(form, mediaPath, mediaSource);
        File file = null;
        try {
            file = File.createTempFile("AI_Media_", (String) null);
            file.deleteOnExit();
            FileUtil.writeStreamToFile(in, file.getAbsolutePath());
            in.close();
            return file;
        } catch (IOException e) {
            if (file != null) {
                Log.e(LOG_TAG, "Could not copy media " + mediaPath + " to temp file " + file.getAbsolutePath());
                file.delete();
            } else {
                Log.e(LOG_TAG, "Could not copy media " + mediaPath + " to temp file.");
            }
            throw e;
        } catch (Throwable th) {
            in.close();
            throw th;
        }
    }

    private static File cacheMediaTempFile(Form form, String mediaPath, MediaSource mediaSource) throws IOException {
        File tempFile = tempFileMap.get(mediaPath);
        if (tempFile != null && tempFile.exists()) {
            return tempFile;
        }
        Log.i(LOG_TAG, "Copying media " + mediaPath + " to temp file...");
        File tempFile2 = copyMediaToTempFile(form, mediaPath, mediaSource);
        Log.i(LOG_TAG, "Finished copying media " + mediaPath + " to temp file " + tempFile2.getAbsolutePath());
        tempFileMap.put(mediaPath, tempFile2);
        return tempFile2;
    }

    public static BitmapDrawable getBitmapDrawable(Form form, String mediaPath) throws IOException {
        if (mediaPath == null || mediaPath.length() == 0) {
            return null;
        }
        final Synchronizer<BitmapDrawable> syncer = new Synchronizer<>();
        getBitmapDrawableAsync(form, mediaPath, new AsyncCallbackPair<BitmapDrawable>() {
            public void onFailure(String message) {
                syncer.error(message);
            }

            public void onSuccess(BitmapDrawable result) {
                syncer.wakeup(result);
            }
        });
        syncer.waitfor();
        BitmapDrawable result = syncer.getResult();
        if (result != null) {
            return result;
        }
        String error = syncer.getError();
        if (error.startsWith("PERMISSION_DENIED:")) {
            throw new PermissionException(error.split(":")[1]);
        }
        throw new IOException(error);
    }

    public static void getBitmapDrawableAsync(Form form, String mediaPath, AsyncCallbackPair<BitmapDrawable> continuation) {
        getBitmapDrawableAsync(form, mediaPath, -1, -1, continuation);
    }

    public static void getBitmapDrawableAsync(Form form, String mediaPath, int desiredWidth, int desiredHeight, AsyncCallbackPair<BitmapDrawable> continuation) {
        if (mediaPath == null || mediaPath.length() == 0) {
            continuation.onSuccess(null);
            return;
        }
        final MediaSource mediaSource = determineMediaSource(form, mediaPath);
        final String str = mediaPath;
        final Form form2 = form;
        final AsyncCallbackPair<BitmapDrawable> asyncCallbackPair = continuation;
        final int i = desiredWidth;
        final int i2 = desiredHeight;
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                int intrinsicWidth;
                int intrinsicHeight;
                Log.d(MediaUtil.LOG_TAG, "mediaPath = " + str);
                InputStream is = null;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[4096];
                try {
                    is = MediaUtil.openMedia(form2, str, mediaSource);
                    while (true) {
                        int read = is.read(buf);
                        if (read <= 0) {
                            break;
                        }
                        bos.write(buf, 0, read);
                    }
                    byte[] buf2 = bos.toByteArray();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e);
                        }
                    }
                    try {
                        bos.close();
                    } catch (IOException e2) {
                    }
                    ByteArrayInputStream bis = new ByteArrayInputStream(buf2);
                    try {
                        bis.mark(buf2.length);
                        BitmapFactory.Options options = MediaUtil.getBitmapOptions(form2, bis, str);
                        bis.reset();
                        BitmapDrawable originalBitmapDrawable = new BitmapDrawable(form2.getResources(), MediaUtil.decodeStream(bis, (Rect) null, options));
                        originalBitmapDrawable.setTargetDensity(form2.getResources().getDisplayMetrics());
                        if ((i > 0 && i2 >= 0) || (options.inSampleSize == 1 && form2.deviceDensity() != 1.0f)) {
                            float deviceDensity = form2.deviceDensity();
                            if (i > 0) {
                                intrinsicWidth = i;
                            } else {
                                intrinsicWidth = originalBitmapDrawable.getIntrinsicWidth();
                            }
                            int scaledWidth = (int) (((float) intrinsicWidth) * deviceDensity);
                            float deviceDensity2 = form2.deviceDensity();
                            if (i2 > 0) {
                                intrinsicHeight = i2;
                            } else {
                                intrinsicHeight = originalBitmapDrawable.getIntrinsicHeight();
                            }
                            Log.d(MediaUtil.LOG_TAG, "form.deviceDensity() = " + form2.deviceDensity());
                            Log.d(MediaUtil.LOG_TAG, "originalBitmapDrawable.getIntrinsicWidth() = " + originalBitmapDrawable.getIntrinsicWidth());
                            Log.d(MediaUtil.LOG_TAG, "originalBitmapDrawable.getIntrinsicHeight() = " + originalBitmapDrawable.getIntrinsicHeight());
                            BitmapDrawable scaledBitmapDrawable = new BitmapDrawable(form2.getResources(), Bitmap.createScaledBitmap(originalBitmapDrawable.getBitmap(), scaledWidth, (int) (((float) intrinsicHeight) * deviceDensity2), false));
                            scaledBitmapDrawable.setTargetDensity(form2.getResources().getDisplayMetrics());
                            System.gc();
                            asyncCallbackPair.onSuccess(scaledBitmapDrawable);
                            if (bis != null) {
                                try {
                                    bis.close();
                                } catch (IOException e3) {
                                    Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e3);
                                }
                            }
                        } else {
                            asyncCallbackPair.onSuccess(originalBitmapDrawable);
                            if (bis != null) {
                                try {
                                    bis.close();
                                } catch (IOException e4) {
                                    Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e4);
                                }
                            }
                        }
                    } catch (Exception e5) {
                        Log.w(MediaUtil.LOG_TAG, "Exception while loading media.", e5);
                        asyncCallbackPair.onFailure(e5.getMessage());
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (IOException e6) {
                                Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e6);
                            }
                        }
                    } catch (Throwable th) {
                        if (bis != null) {
                            try {
                                bis.close();
                            } catch (IOException e7) {
                                Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e7);
                            }
                        }
                        throw th;
                    }
                } catch (PermissionException e8) {
                    asyncCallbackPair.onFailure("PERMISSION_DENIED:" + e8.getPermissionNeeded());
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e9) {
                            Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e9);
                        }
                    }
                    try {
                        bos.close();
                    } catch (IOException e10) {
                    }
                } catch (IOException e11) {
                    if (mediaSource == MediaSource.CONTACT_URI) {
                        asyncCallbackPair.onSuccess(new BitmapDrawable(form2.getResources(), BitmapFactory.decodeResource(form2.getResources(), 17301606, (BitmapFactory.Options) null)));
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e12) {
                                Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e12);
                            }
                        }
                        try {
                            bos.close();
                        } catch (IOException e13) {
                        }
                        return;
                    }
                    Log.d(MediaUtil.LOG_TAG, "IOException reading file.", e11);
                    asyncCallbackPair.onFailure(e11.getMessage());
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e14) {
                            Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e14);
                        }
                    }
                    try {
                        bos.close();
                    } catch (IOException e15) {
                    }
                } catch (Throwable th2) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e16) {
                            Log.w(MediaUtil.LOG_TAG, "Unexpected error on close", e16);
                        }
                    }
                    try {
                        bos.close();
                    } catch (IOException e17) {
                    }
                    throw th2;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public static Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        return BitmapFactory.decodeStream(new FlushedInputStream(is), outPadding, opts);
    }

    private static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0;
            while (totalBytesSkipped < n) {
                long bytesSkipped = this.in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0) {
                    if (read() < 0) {
                        break;
                    }
                    bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    /* access modifiers changed from: private */
    public static BitmapFactory.Options getBitmapOptions(Form form, InputStream is, String mediaPath) {
        int maxWidth;
        int maxHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeStream(is, (Rect) null, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        Display display = ((WindowManager) form.getSystemService("window")).getDefaultDisplay();
        if (Form.getCompatibilityMode()) {
            maxWidth = 720;
            maxHeight = 840;
        } else {
            maxWidth = (int) (((float) display.getWidth()) / form.deviceDensity());
            maxHeight = (int) (((float) display.getHeight()) / form.deviceDensity());
        }
        int sampleSize = 1;
        while (imageWidth / sampleSize > maxWidth && imageHeight / sampleSize > maxHeight) {
            sampleSize *= 2;
        }
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        Log.d(LOG_TAG, "getBitmapOptions: sampleSize = " + sampleSize + " mediaPath = " + mediaPath + " maxWidth = " + maxWidth + " maxHeight = " + maxHeight + " display width = " + display.getWidth() + " display height = " + display.getHeight());
        options2.inSampleSize = sampleSize;
        return options2;
    }

    private static AssetFileDescriptor getAssetsIgnoreCaseAfd(Form form, String mediaPath) throws IOException {
        try {
            return form.getAssets().openFd(mediaPath);
        } catch (IOException e) {
            String path = findCaseinsensitivePath(form, mediaPath);
            if (path != null) {
                return form.getAssets().openFd(path);
            }
            throw e;
        }
    }

    public static int loadSoundPool(SoundPool soundPool, Form form, String mediaPath) throws IOException {
        MediaSource mediaSource = determineMediaSource(form, mediaPath);
        switch (mediaSource) {
            case ASSET:
                return soundPool.load(getAssetsIgnoreCaseAfd(form, mediaPath), 1);
            case REPL_ASSET:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                return soundPool.load(fileUrlToFilePath(form.getAssetPath(mediaPath)), 1);
            case SDCARD:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                return soundPool.load(mediaPath, 1);
            case FILE_URL:
                if (isExternalFileUrl(form, mediaPath) || RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                return soundPool.load(fileUrlToFilePath(mediaPath), 1);
            case URL:
            case CONTENT_URI:
                return soundPool.load(cacheMediaTempFile(form, mediaPath, mediaSource).getAbsolutePath(), 1);
            case CONTACT_URI:
                throw new IOException("Unable to load audio for contact " + mediaPath + ".");
            default:
                throw new IOException("Unable to load audio " + mediaPath + ".");
        }
    }

    public static void loadMediaPlayer(MediaPlayer mediaPlayer, Form form, String mediaPath) throws IOException {
        switch (determineMediaSource(form, mediaPath)) {
            case ASSET:
                AssetFileDescriptor afd = getAssetsIgnoreCaseAfd(form, mediaPath);
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    return;
                } finally {
                    afd.close();
                }
            case REPL_ASSET:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                mediaPlayer.setDataSource(fileUrlToFilePath(form.getAssetPath(mediaPath)));
                return;
            case SDCARD:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                mediaPlayer.setDataSource(mediaPath);
                return;
            case FILE_URL:
                if (isExternalFileUrl(form, mediaPath) || RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                mediaPlayer.setDataSource(fileUrlToFilePath(mediaPath));
                return;
            case URL:
                mediaPlayer.setDataSource(mediaPath);
                return;
            case CONTENT_URI:
                mediaPlayer.setDataSource(form, Uri.parse(mediaPath));
                return;
            case CONTACT_URI:
                throw new IOException("Unable to load audio or video for contact " + mediaPath + ".");
            default:
                throw new IOException("Unable to load audio or video " + mediaPath + ".");
        }
    }

    public static void loadVideoView(VideoView videoView, Form form, String mediaPath) throws IOException {
        MediaSource mediaSource = determineMediaSource(form, mediaPath);
        switch (mediaSource) {
            case ASSET:
            case URL:
                videoView.setVideoPath(cacheMediaTempFile(form, mediaPath, mediaSource).getAbsolutePath());
                return;
            case REPL_ASSET:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                videoView.setVideoPath(fileUrlToFilePath(form.getAssetPath(mediaPath)));
                return;
            case SDCARD:
                if (RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                videoView.setVideoPath(mediaPath);
                return;
            case FILE_URL:
                if (isExternalFileUrl(form, mediaPath) || RUtil.needsFilePermission(form, mediaPath, (FileScope) null)) {
                    form.assertPermission("android.permission.READ_EXTERNAL_STORAGE");
                }
                videoView.setVideoPath(fileUrlToFilePath(mediaPath));
                return;
            case CONTENT_URI:
                videoView.setVideoURI(Uri.parse(mediaPath));
                return;
            case CONTACT_URI:
                throw new IOException("Unable to load video for contact " + mediaPath + ".");
            default:
                throw new IOException("Unable to load video " + mediaPath + ".");
        }
    }
}
