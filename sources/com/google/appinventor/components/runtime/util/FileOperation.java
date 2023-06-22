package com.google.appinventor.components.runtime.util;

import android.util.Log;
import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.PermissionResultHandler;
import com.google.appinventor.components.runtime.errors.StopBlocksExecution;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public abstract class FileOperation implements Runnable, PermissionResultHandler {
    /* access modifiers changed from: private */
    public static final String LOG_TAG = FileOperation.class.getSimpleName();
    protected volatile boolean askedForPermission = false;
    protected final boolean async;
    protected final Component component;
    protected final Form form;
    protected volatile boolean hasPermission = false;
    protected final String method;

    public interface FileInvocation {
        void call(ScopedFile[] scopedFileArr) throws IOException;
    }

    public abstract List<String> getPermissions();

    /* access modifiers changed from: protected */
    public abstract boolean needsExternalStorage();

    /* access modifiers changed from: protected */
    public abstract boolean needsPermission();

    /* access modifiers changed from: protected */
    public abstract void performOperation();

    FileOperation(Form form2, Component component2, String method2, boolean async2) {
        this.form = form2;
        this.component = component2;
        this.method = method2;
        this.async = async2;
    }

    public final void run() {
        List<String> neededPermissions = getNeededPermissions();
        if (AsynchUtil.isUiThread()) {
            if (needsExternalStorage()) {
                FileUtil.checkExternalStorageWriteable();
            }
            if (neededPermissions.isEmpty()) {
                this.hasPermission = true;
                if (this.async) {
                    AsynchUtil.runAsynchronously(this);
                } else {
                    performOperation();
                }
            } else if (!this.hasPermission) {
                if (this.askedForPermission) {
                    this.form.dispatchPermissionDeniedEvent(this.component, this.method, neededPermissions.get(0));
                } else {
                    this.askedForPermission = true;
                    this.form.askPermission(new BulkPermissionRequest(this.component, this.method, (String[]) neededPermissions.toArray(new String[0])) {
                        public void onGranted() {
                            FileOperation.this.hasPermission = true;
                            FileOperation.this.run();
                        }
                    });
                }
                throw new StopBlocksExecution();
            } else if (this.async) {
                AsynchUtil.runAsynchronously(this);
            } else {
                performOperation();
            }
        } else if (!neededPermissions.isEmpty()) {
            this.hasPermission = false;
            this.askedForPermission = false;
            this.form.runOnUiThread(this);
        } else {
            performOperation();
        }
    }

    public void HandlePermissionResponse(String permission, boolean granted) {
        this.askedForPermission = true;
        this.hasPermission = granted;
        run();
    }

    /* access modifiers changed from: protected */
    public void reportError(final int errorNumber, final Object... messageArgs) {
        this.form.runOnUiThread(new Runnable() {
            public void run() {
                FileOperation.this.form.dispatchErrorOccurredEvent(FileOperation.this.component, FileOperation.this.method, errorNumber, messageArgs);
            }
        });
    }

    private List<String> getNeededPermissions() {
        if (this.hasPermission) {
            return Collections.emptyList();
        }
        List<String> permissions = getPermissions();
        Set<String> result = new HashSet<>();
        for (String permission : permissions) {
            if (this.form.isDeniedPermission(permission)) {
                result.add(permission);
            }
        }
        return new ArrayList(result);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean askPermission = true;
        private boolean async = true;
        /* access modifiers changed from: private */
        public final List<FileInvocation> commands = new ArrayList();
        private Component component;
        private Form form;
        private String method;
        /* access modifiers changed from: private */
        public final Set<String> neededPermissions = new HashSet();
        /* access modifiers changed from: private */
        public boolean needsExternalStorage = false;
        /* access modifiers changed from: private */
        public final LinkedHashMap<ScopedFile, FileAccessMode> scopedFiles = new LinkedHashMap<>();

        public Builder() {
        }

        public Builder(Form form2, Component component2, String method2) {
            this.form = form2;
            this.component = component2;
            this.method = method2;
        }

        public Builder setForm(Form form2) {
            this.form = form2;
            return this;
        }

        public Builder setComponent(Component component2) {
            this.component = component2;
            return this;
        }

        public Builder setMethod(String method2) {
            this.method = method2;
            return this;
        }

        public Builder addFile(FileScope scope, String fileName, FileAccessMode accessMode) {
            ScopedFile file = new ScopedFile(scope, fileName);
            if (file.getScope() != FileScope.Asset || accessMode == FileAccessMode.READ) {
                this.scopedFiles.put(file, accessMode);
                String resolvedFileName = FileUtil.resolveFileName(this.form, fileName, scope);
                Log.d(FileOperation.LOG_TAG, this.method + " resolved " + resolvedFileName);
                this.needsExternalStorage |= FileUtil.needsPermission(this.form, resolvedFileName);
                String permission = FileUtil.getNeededPermission(this.form, resolvedFileName, accessMode);
                if (permission != null) {
                    this.neededPermissions.add(permission);
                }
                return this;
            }
            this.form.dispatchErrorOccurredEvent(this.component, this.method, ErrorMessages.ERROR_CANNOT_WRITE_ASSET, file.getFileName());
            throw new StopBlocksExecution();
        }

        public Builder addCommand(FileInvocation command) {
            this.commands.add(command);
            return this;
        }

        public Builder setAsynchronous(boolean async2) {
            this.async = async2;
            return this;
        }

        public Builder setAskPermission(boolean askPermission2) {
            this.askPermission = askPermission2;
            return this;
        }

        public FileOperation build() {
            return new FileOperation(this.form, this.component, this.method, this.async) {
                public List<String> getPermissions() {
                    return new ArrayList(Builder.this.neededPermissions);
                }

                /* access modifiers changed from: protected */
                public void performOperation() {
                    if (Builder.this.askPermission && Builder.this.neededPermissions.size() > 0) {
                        Iterator<String> i = Builder.this.neededPermissions.iterator();
                        while (i.hasNext()) {
                            if (!this.form.isDeniedPermission(i.next())) {
                                i.remove();
                            }
                        }
                        if (needsPermission()) {
                            Log.d(FileOperation.LOG_TAG, this.method + " need permissions: " + Builder.this.neededPermissions);
                            this.form.askPermission(new BulkPermissionRequest(this.component, this.method, (String[]) Builder.this.neededPermissions.toArray(new String[0])) {
                                public void onGranted() {
                                }
                            });
                            throw new StopBlocksExecution();
                        }
                    }
                    ScopedFile[] filesArray = (ScopedFile[]) Builder.this.scopedFiles.keySet().toArray(new ScopedFile[0]);
                    for (FileInvocation command : Builder.this.commands) {
                        try {
                            command.call(filesArray);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                /* access modifiers changed from: protected */
                public boolean needsPermission() {
                    return !Builder.this.neededPermissions.isEmpty();
                }

                /* access modifiers changed from: protected */
                public boolean needsExternalStorage() {
                    return Builder.this.needsExternalStorage;
                }
            };
        }
    }
}
