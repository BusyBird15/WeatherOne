package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum UltrasonicSensorUnit implements OptionList<String> {
    Centimeters("cm", 0),
    Inches("inch", 1);
    
    private static final Map<String, UltrasonicSensorUnit> lookup = null;
    private int intValue;
    private String value;

    static {
        int i;
        lookup = new HashMap();
        for (UltrasonicSensorUnit mode : values()) {
            lookup.put(mode.toUnderlyingValue(), mode);
        }
    }

    private UltrasonicSensorUnit(String mode, int intMode) {
        this.value = mode;
        this.intValue = intMode;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public Integer toInt() {
        return Integer.valueOf(this.intValue);
    }

    public static UltrasonicSensorUnit fromUnderlyingValue(String mode) {
        return lookup.get(mode);
    }
}
