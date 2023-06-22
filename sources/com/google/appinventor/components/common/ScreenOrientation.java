package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum ScreenOrientation implements OptionList<String> {
    Unspecified("unspecified", 4),
    Landscape("landscape", 0),
    Portrait("portrait", 1),
    Sensor("sensor", 4),
    User("user", 2),
    Behind("behind", 3),
    NoSensor("nosensor", 5),
    FullSensor("fullSensor", 10),
    ReverseLandscape("reverseLandscape", 8),
    ReversePortrait("reversePortrait", 9),
    SensorLandscape("sensorLandscape", 6),
    SensorPortrait("sensorPortrait", 7);
    
    private static final Map<String, ScreenOrientation> lookup = null;
    private int orientationConst;
    private String value;

    static {
        int i;
        lookup = new HashMap();
        for (ScreenOrientation orientation : values()) {
            lookup.put(orientation.toUnderlyingValue().toLowerCase(), orientation);
        }
    }

    private ScreenOrientation(String val, int orientation) {
        this.value = val;
        this.orientationConst = orientation;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public int getOrientationConstant() {
        return this.orientationConst;
    }

    public static ScreenOrientation fromUnderlyingValue(String orientation) {
        return lookup.get(orientation.toLowerCase());
    }
}
