package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum NxtRunState implements OptionList<Integer> {
    Disabled(0),
    Running(32),
    RampUp(16),
    RampDown(64);
    
    private static final Map<Integer, NxtRunState> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (NxtRunState state : values()) {
            lookup.put(state.toUnderlyingValue(), state);
        }
    }

    private NxtRunState(int state) {
        this.value = state;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static NxtRunState fromUnderlyingValue(Integer state) {
        return lookup.get(state);
    }
}
