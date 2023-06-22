package com.google.appinventor.components.runtime.util;

import android.util.Log;
import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import java.io.File;
import java.util.Collections;
import java.util.List;

public abstract class SingleFileOperation extends FileOperation {
    private static final String LOG_TAG = FileOperation.class.getSimpleName();
    protected final FileAccessMode accessMode;
    protected final File file;
    protected final String fileName;
    protected final String resolvedPath;
    protected final FileScope scope;
    protected final ScopedFile scopedFile;

    /* access modifiers changed from: protected */
    public abstract void processFile(ScopedFile scopedFile2);

    protected SingleFileOperation(Form form, Component component, String method, String fileName2, FileScope scope2, FileAccessMode accessMode2, boolean async) {
        super(form, component, method, async);
        this.scope = scope2;
        this.accessMode = accessMode2;
        this.fileName = fileName2;
        this.scopedFile = new ScopedFile(scope2, fileName2);
        this.file = this.scopedFile.resolve(form);
        this.resolvedPath = this.file.getAbsolutePath();
        Log.d(LOG_TAG, "resolvedPath = " + this.resolvedPath);
    }

    protected SingleFileOperation(Form form, Component component, String method, ScopedFile file2, FileAccessMode accessMode2, boolean async) {
        super(form, component, method, async);
        this.scope = file2.getScope();
        this.accessMode = accessMode2;
        this.fileName = file2.getFileName();
        this.scopedFile = file2;
        this.file = this.scopedFile.resolve(form);
        this.resolvedPath = this.file.getAbsolutePath();
        Log.d(LOG_TAG, "resolvedPath = " + this.resolvedPath);
    }

    protected SingleFileOperation(Form form, Component component, String method, String fileName2, FileScope scope2, FileAccessMode accessMode2) {
        this(form, component, method, fileName2, scope2, accessMode2, true);
    }

    public List<String> getPermissions() {
        String permission = FileUtil.getNeededPermission(this.form, this.resolvedPath, this.accessMode);
        if (permission == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(permission);
    }

    public final File getFile() {
        return this.file;
    }

    public final ScopedFile getScopedFile() {
        return this.scopedFile;
    }

    public final boolean isAsset() {
        return this.fileName.startsWith("//") || this.scope == FileScope.Asset;
    }

    public final FileScope getScope() {
        return this.scope;
    }

    /* access modifiers changed from: protected */
    public void performOperation() {
        processFile(this.scopedFile);
    }

    /* access modifiers changed from: protected */
    public boolean needsExternalStorage() {
        return FileUtil.isExternalStorageUri(this.form, this.resolvedPath);
    }

    /* access modifiers changed from: protected */
    public final boolean needsPermission() {
        return FileUtil.needsPermission(this.form, this.resolvedPath);
    }
}
