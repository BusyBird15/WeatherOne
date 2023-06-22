package com.google.appinventor.components.runtime.errors;

public class PermissionException extends RuntimeException {
    private final String permissionNeeded;

    public PermissionException(String permissionNeeded2) {
        this.permissionNeeded = permissionNeeded2;
    }

    public String getPermissionNeeded() {
        return this.permissionNeeded;
    }

    public String getMessage() {
        return "Unable to complete the operation because the user denied permission: " + this.permissionNeeded;
    }
}
