package com.google.appinventor.components.runtime.util;

import android.os.Build;
import com.google.appinventor.components.runtime.BluetoothClient;
import com.google.appinventor.components.runtime.BluetoothServer;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.PermissionResultHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SUtil {
    public static boolean requestPermissionsForScanning(Form form, BluetoothClient client, String caller, PermissionResultHandler continuation) {
        List<String> permsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 31) {
            permsNeeded.add("android.permission.BLUETOOTH_SCAN");
        } else {
            permsNeeded.add("android.permission.BLUETOOTH");
            permsNeeded.add("android.permission.BLUETOOTH_ADMIN");
        }
        if (!client.NoLocationNeeded() && form.doesAppDeclarePermission("android.permission.ACCESS_FINE_LOCATION")) {
            permsNeeded.add("android.permission.ACCESS_FINE_LOCATION");
        }
        return performRequest(form, client, caller, permsNeeded, continuation);
    }

    public static boolean requestPermissionsForConnecting(Form form, BluetoothClient client, String caller, PermissionResultHandler continuation) {
        return requestPermissionsForS("android.permission.BLUETOOTH_CONNECT", form, (Component) client, caller, continuation);
    }

    public static boolean requestPermissionsForAdvertising(Form form, BluetoothServer server, String caller, PermissionResultHandler continuation) {
        return requestPermissionsForS("android.permission.BLUETOOTH_ADVERTISE", form, (Component) server, caller, continuation);
    }

    public static boolean requestPermissionsForS(String sdk31Permission, Form form, Component source, String caller, PermissionResultHandler continuation) {
        return requestPermissionsForS(new String[]{sdk31Permission}, form, source, caller, continuation);
    }

    public static boolean requestPermissionsForS(String[] sdk31Permissions, Form form, Component source, String caller, PermissionResultHandler continuation) {
        List<String> permsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 31) {
            Collections.addAll(permsNeeded, sdk31Permissions);
        } else {
            permsNeeded.add("android.permission.BLUETOOTH");
            permsNeeded.add("android.permission.BLUETOOTH_ADMIN");
        }
        return performRequest(form, source, caller, permsNeeded, continuation);
    }

    private static boolean performRequest(Form form, Component source, String caller, List<String> permsNeeded, PermissionResultHandler continuation) {
        boolean ready = true;
        Iterator<String> it = permsNeeded.iterator();
        while (true) {
            if (it.hasNext()) {
                if (form.isDeniedPermission(it.next())) {
                    ready = false;
                    break;
                }
            } else {
                break;
            }
        }
        if (!ready) {
            final PermissionResultHandler permissionResultHandler = continuation;
            final List<String> list = permsNeeded;
            form.askPermission(new BulkPermissionRequest(source, caller, (String[]) permsNeeded.toArray(new String[0])) {
                public void onGranted() {
                    permissionResultHandler.HandlePermissionResponse((String) list.get(0), true);
                }
            });
        }
        return !ready;
    }
}
