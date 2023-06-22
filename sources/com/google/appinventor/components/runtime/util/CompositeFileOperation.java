package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class CompositeFileOperation extends FileOperation implements Iterable<FileOperand> {
    private final List<FileOperand> files = new ArrayList();
    private boolean needsExternalStorage = false;
    private final Set<String> permissions = new HashSet();

    /* access modifiers changed from: protected */
    public abstract void performOperation();

    public static class FileOperand {
        /* access modifiers changed from: private */
        public final String file;
        private final FileAccessMode mode;

        FileOperand(String file2, FileAccessMode mode2) {
            this.file = file2;
            this.mode = mode2;
        }

        public String getFile() {
            return this.file;
        }

        public FileAccessMode getMode() {
            return this.mode;
        }
    }

    public CompositeFileOperation(Form form, Component component, String method, boolean async) {
        super(form, component, method, async);
    }

    public void addFile(FileScope scope, String fileName, FileAccessMode mode) {
        FileOperand operand = new FileOperand(FileUtil.resolveFileName(this.form, fileName, scope), mode);
        this.files.add(operand);
        this.permissions.add(FileUtil.getNeededPermission(this.form, fileName, mode));
        this.needsExternalStorage |= FileUtil.isExternalStorageUri(this.form, operand.file);
    }

    public List<String> getPermissions() {
        return new ArrayList(this.permissions);
    }

    /* access modifiers changed from: protected */
    public boolean needsExternalStorage() {
        return this.needsExternalStorage;
    }

    public Iterator<FileOperand> iterator() {
        return this.files.iterator();
    }
}
