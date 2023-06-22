package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum NxtRegulationMode implements OptionList<Integer> {
    Disabled(0),
    Speed(1),
    Synchronization(2);
    
    private static final Map<Integer, NxtRegulationMode> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (NxtRegulationMode mode : values()) {
            lookup.put(mode.toUnderlyingValue(), mode);
        }
    }

    private NxtRegulationMode(int mode) {
        this.value = mode;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static NxtRegulationMode fromUnderlyingValue(Integer mode) {
        return lookup.get(mode);
    }
}
