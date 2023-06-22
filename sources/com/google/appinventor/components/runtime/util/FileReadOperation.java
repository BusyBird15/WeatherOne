package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import java.io.IOException;
import java.io.InputStream;

public class FileReadOperation extends FileStreamOperation<InputStream> {
    public FileReadOperation(Form form, Component component, String method, String fileName, FileScope scope, boolean async) {
        super(form, component, method, fileName, scope, FileAccessMode.READ, async);
    }

    /* access modifiers changed from: protected */
    public boolean process(InputStream stream) throws IOException {
        return process(IOUtils.readStream(stream));
    }

    public boolean process(byte[] contents) {
        return true;
    }

    /* access modifiers changed from: protected */
    public InputStream openFile() throws IOException {
        return FileUtil.openForReading(this.form, this.scopedFile);
    }
}
