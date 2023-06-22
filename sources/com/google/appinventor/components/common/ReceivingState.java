package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum ReceivingState implements OptionList<Integer> {
    Off(1),
    Foreground(2),
    Always(3);
    
    private static final Map<Integer, ReceivingState> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (ReceivingState status : values()) {
            lookup.put(status.toUnderlyingValue(), status);
        }
    }

    private ReceivingState(int status) {
        this.value = status;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static ReceivingState fromUnderlyingValue(Integer status) {
        return lookup.get(status);
    }
}
