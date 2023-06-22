package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum Sensitivity implements OptionList<Integer> {
    Weak(1),
    Moderate(2),
    Strong(3);
    
    private static final Map<Integer, Sensitivity> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (Sensitivity sensitivity : values()) {
            lookup.put(sensitivity.toUnderlyingValue(), sensitivity);
        }
    }

    private Sensitivity(int sensitivity) {
        this.value = sensitivity;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static Sensitivity fromUnderlyingValue(Integer sensitivity) {
        return lookup.get(sensitivity);
    }
}
