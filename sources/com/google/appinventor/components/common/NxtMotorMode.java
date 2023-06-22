package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum NxtMotorMode implements OptionList<Integer> {
    On(1),
    Brake(2),
    Regulated(4),
    Coast(0);
    
    private static final Map<Integer, NxtMotorMode> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (NxtMotorMode mode : values()) {
            lookup.put(mode.toUnderlyingValue(), mode);
        }
    }

    private NxtMotorMode(int mode) {
        this.value = mode;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static NxtMotorMode fromUnderlyingValue(Integer mode) {
        return lookup.get(mode);
    }
}
