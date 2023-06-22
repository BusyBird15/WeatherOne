package com.google.appinventor.components.runtime;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.BulkPermissionRequest;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.NougatUtil;
import com.google.appinventor.components.runtime.util.ScopedFile;
import java.io.File;
import java.net.URI;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MEDIA, description = "A component to take a picture using the device's camera. After the picture is taken, the name of the file on the phone containing the picture is available as an argument to the AfterPicture event. The file name can be used, for example, to set the Picture property of an Image component.", iconName = "images/camera.png", nonVisible = true, version = 3)
@UsesPermissions({"android.permission.CAMERA"})
public class Camera extends AndroidNonvisibleComponent implements ActivityResultListener, Component {
    private static final String CAMERA_INTENT = "android.media.action.IMAGE_CAPTURE";
    private static final String CAMERA_OUTPUT = "output";
    private final ComponentContainer container;
    /* access modifiers changed from: private */
    public boolean havePermission = false;
    private Uri imageFile;
    private int requestCode;
    private boolean useFront;

    public Camera(ComponentContainer container2) {
        super(container2.$form());
        this.container = container2;
        UseFront(false);
    }

    public void Initialize() {
        boolean z;
        boolean z2 = true;
        if (!this.form.isDeniedPermission("android.permission.CAMERA")) {
            z = true;
        } else {
            z = false;
        }
        this.havePermission = z;
        if (FileUtil.needsWritePermission(this.form.DefaultFileScope())) {
            boolean z3 = this.havePermission;
            if (this.form.isDeniedPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
                z2 = false;
            }
            this.havePermission = z3 & z2;
        }
    }

    @Deprecated
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean UseFront() {
        return this.useFront;
    }

    @Deprecated
    @SimpleProperty(description = "Specifies whether the front-facing camera should be used (when available). If the device does not have a front-facing camera, this option will be ignored and the camera will open normally.")
    public void UseFront(boolean front) {
        this.useFront = front;
    }

    @SimpleFunction
    public void TakePicture() {
        ScopedFile target = FileUtil.getScopedPictureFile(this.form, "jpg");
        if (!this.havePermission) {
            this.form.askPermission(new BulkPermissionRequest(this, "TakePicture", FileUtil.needsWritePermission(target) ? new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"} : new String[]{"android.permission.CAMERA"}) {
                public void onGranted() {
                    boolean unused = Camera.this.havePermission = true;
                    Camera.this.TakePicture();
                }
            });
            return;
        }
        String state = Environment.getExternalStorageState();
        if (!FileUtil.needsExternalStorage(this.form, target) || "mounted".equals(state)) {
            Log.i("CameraComponent", "External storage is available and writable");
            capturePicture(target);
        } else if ("mounted_ro".equals(state)) {
            this.form.dispatchErrorOccurredEvent(this, "TakePicture", ErrorMessages.ERROR_MEDIA_EXTERNAL_STORAGE_READONLY, new Object[0]);
        } else {
            this.form.dispatchErrorOccurredEvent(this, "TakePicture", ErrorMessages.ERROR_MEDIA_EXTERNAL_STORAGE_NOT_AVAILABLE, new Object[0]);
        }
    }

    private void capturePicture(ScopedFile target) {
        Uri imageUri;
        File path = new File(URI.create(FileUtil.resolveFileName(this.form, target.getFileName(), target.getScope())));
        File directory = path.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        this.imageFile = Uri.fromFile(path);
        ContentValues values = new ContentValues();
        values.put("_data", this.imageFile.getPath());
        values.put("mime_type", "image/jpeg");
        values.put("title", this.imageFile.getLastPathSegment());
        if (this.requestCode == 0) {
            this.requestCode = this.form.registerForActivityResult(this);
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = this.container.$context().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        } else {
            imageUri = NougatUtil.getPackageUri(this.form, path);
        }
        Intent intent = new Intent(CAMERA_INTENT);
        intent.putExtra(CAMERA_OUTPUT, imageUri);
        if (this.useFront) {
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        }
        this.container.$context().startActivityForResult(intent, this.requestCode);
    }

    public void resultReturned(int requestCode2, int resultCode, Intent data) {
        Log.i("CameraComponent", "Returning result. Request code = " + requestCode2 + ", result code = " + resultCode);
        if (requestCode2 == this.requestCode && resultCode == -1) {
            File image = new File(this.imageFile.getPath());
            if (image.length() != 0) {
                scanFileToAdd(image);
                AfterPicture(this.imageFile.toString());
                return;
            }
            deleteFile(this.imageFile);
            if (data == null || data.getData() == null) {
                Log.i("CameraComponent", "Couldn't find an image file from the Camera result");
                this.form.dispatchErrorOccurredEvent(this, "TakePicture", ErrorMessages.ERROR_CAMERA_NO_IMAGE_RETURNED, new Object[0]);
                return;
            }
            Uri tryImageUri = data.getData();
            Log.i("CameraComponent", "Calling Camera.AfterPicture with image path " + tryImageUri.toString());
            AfterPicture(tryImageUri.toString());
            return;
        }
        deleteFile(this.imageFile);
    }

    private void scanFileToAdd(File image) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(NougatUtil.getPackageUri(this.form, image));
        this.container.$context().getApplicationContext().sendBroadcast(mediaScanIntent);
    }

    private void deleteFile(Uri fileUri) {
        try {
            if (new File(fileUri.getPath()).delete()) {
                Log.i("CameraComponent", "Deleted file " + fileUri.toString());
            } else {
                Log.i("CameraComponent", "Could not delete file " + fileUri.toString());
            }
        } catch (SecurityException e) {
            Log.i("CameraComponent", "Got security exception trying to delete file " + fileUri.toString());
        }
    }

    @SimpleEvent
    public void AfterPicture(String image) {
        EventDispatcher.dispatchEvent(this, "AfterPicture", image);
    }
}
