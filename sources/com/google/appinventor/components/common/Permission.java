package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum Permission implements OptionList<String> {
    CoarseLocation("ACCESS_COARSE_LOCATION"),
    FineLocation("ACCESS_FINE_LOCATION"),
    MockLocation("ACCESS_MOCK_LOCATION"),
    LocationExtraCommands("ACCESS_LOCATION_EXTRA_COMMANDS"),
    ReadExternalStorage("READ_EXTERNAL_STORAGE"),
    WriteExternalStorage("WRITE_EXTERNAL_STORAGE"),
    Camera("CAMERA"),
    Audio("RECORD_AUDIO"),
    Vibrate("VIBRATE"),
    Internet("INTERNET"),
    NearFieldCommunication("NFC"),
    Bluetooth("BLUETOOTH"),
    BluetoothAdmin("BLUETOOTH_ADMIN"),
    WifiState("ACCESS_WIFI_STATE"),
    NetworkState("ACCESS_NETWORK_STATE"),
    AccountManager("ACCOUNT_MANAGER"),
    ManageAccounts("MANAGE_ACCOUNTS"),
    GetAccounts("GET_ACCOUNTS"),
    ReadContacts("READ_CONTACTS"),
    UseCredentials("USE_CREDENTIALS"),
    BluetoothAdvertise("BLUETOOTH_ADVERTISE"),
    BluetoothConnect("BLUETOOTH_CONNECT"),
    BluetoothScan("BLUETOOTH_SCAN");
    
    private static final Map<String, Permission> lookup = null;
    private final String value;

    static {
        int i;
        lookup = new HashMap();
        for (Permission perm : values()) {
            lookup.put(perm.toUnderlyingValue(), perm);
        }
    }

    private Permission(String perm) {
        this.value = perm;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public static Permission fromUnderlyingValue(String perm) {
        return lookup.get(perm);
    }
}
