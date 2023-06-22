package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BulkPermissionRequest {
    private String caller;
    private String[] permissions;
    private Component source;

    public abstract void onGranted();

    protected BulkPermissionRequest(Component source2, String caller2, String... permissions2) {
        this.source = source2;
        this.caller = caller2;
        this.permissions = permissions2;
    }

    public void onDenied(String[] permissions2) {
        Form form = (Form) this.source.getDispatchDelegate();
        for (String permission : permissions2) {
            form.dispatchPermissionDeniedEvent(this.source, this.caller, permission);
        }
    }

    public final List<String> getPermissions() {
        List<String> result = new ArrayList<>(this.permissions.length);
        Collections.addAll(result, this.permissions);
        return result;
    }
}
