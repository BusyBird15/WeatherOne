package com.google.appinventor.components.runtime;

import android.util.Log;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.FileScope;
import com.google.appinventor.components.runtime.errors.StopBlocksExecution;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileStreamReadOperation;
import com.google.appinventor.components.runtime.util.IOUtils;
import java.io.FileNotFoundException;
import java.io.IOException;

@SimpleObject
public abstract class FileBase extends AndroidNonvisibleComponent implements Component {
    protected static final String LOG_TAG = "FileComponent";
    protected FileScope scope = FileScope.App;

    /* access modifiers changed from: protected */
    public abstract void afterRead(String str);

    protected FileBase(ComponentContainer container) {
        super(container.$form());
        DefaultScope(FileScope.App);
    }

    @DesignerProperty(defaultValue = "App", editorType = "file_scope")
    @SimpleProperty(userVisible = false)
    public void DefaultScope(FileScope scope2) {
        this.scope = scope2;
    }

    @Deprecated
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void LegacyMode(boolean legacy) {
        this.scope = legacy ? FileScope.Legacy : FileScope.App;
    }

    @Deprecated
    @SimpleProperty(description = "Allows app to access files from the root of the external storage directory (legacy mode).")
    public boolean LegacyMode() {
        return this.scope == FileScope.Legacy;
    }

    /* access modifiers changed from: protected */
    public void readFromFile(String fileName) {
        try {
            new FileStreamReadOperation(this.form, this, "ReadFrom", fileName, this.scope, true) {
                public boolean process(String contents) {
                    FileBase.this.afterRead(IOUtils.normalizeNewLines(contents));
                    return true;
                }

                public void onError(IOException e) {
                    if (e instanceof FileNotFoundException) {
                        Log.e(FileBase.LOG_TAG, "FileNotFoundException", e);
                        this.form.dispatchErrorOccurredEvent(FileBase.this, "ReadFrom", ErrorMessages.ERROR_CANNOT_FIND_FILE, this.fileName);
                        return;
                    }
                    Log.e(FileBase.LOG_TAG, "IOException", e);
                    this.form.dispatchErrorOccurredEvent(FileBase.this, "ReadFrom", ErrorMessages.ERROR_CANNOT_READ_FILE, this.fileName);
                }
            }.run();
        } catch (StopBlocksExecution e) {
        }
    }
}
