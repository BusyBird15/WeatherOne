package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum GyroSensorMode implements OptionList<String> {
    Angle("angle", 0),
    Rate("rate", 1);
    
    private static final Map<String, GyroSensorMode> lookup = null;
    private final int intValue;
    private final String value;

    static {
        int i;
        lookup = new HashMap();
        for (GyroSensorMode mode : values()) {
            lookup.put(mode.toUnderlyingValue(), mode);
        }
    }

    private GyroSensorMode(String mode, int intMode) {
        this.value = mode;
        this.intValue = intMode;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public Integer toInt() {
        return Integer.valueOf(this.intValue);
    }

    public static GyroSensorMode fromUnderlyingValue(String mode) {
        return lookup.get(mode);
    }
}
