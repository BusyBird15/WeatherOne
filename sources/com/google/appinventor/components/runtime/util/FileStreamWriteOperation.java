package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public abstract class FileStreamWriteOperation extends FileWriteOperation {
    private static final String LOG_TAG = FileStreamWriteOperation.class.getSimpleName();

    /* access modifiers changed from: protected */
    public abstract boolean process(OutputStreamWriter outputStreamWriter) throws IOException;

    public FileStreamWriteOperation(Form form, Component component, String method, String fileName, FileScope scope, boolean append, boolean async) {
        super(form, component, method, fileName, scope, append, async);
    }

    /* access modifiers changed from: protected */
    public final boolean process(OutputStream out) throws IOException {
        OutputStreamWriter writer = null;
        try {
            OutputStreamWriter writer2 = new OutputStreamWriter(out);
            try {
                boolean close = process(writer2);
                if (close) {
                    IOUtils.closeQuietly(LOG_TAG, writer2);
                }
                return close;
            } catch (Throwable th) {
                th = th;
                writer = writer2;
            }
        } catch (Throwable th2) {
            th = th2;
            if (1 != 0) {
                IOUtils.closeQuietly(LOG_TAG, writer);
            }
            throw th;
        }
    }
}
