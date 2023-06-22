package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum StartedStatus implements OptionList<Integer> {
    Incoming(1),
    Outgoing(2);
    
    private static final Map<Integer, StartedStatus> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (StartedStatus status : values()) {
            lookup.put(status.toUnderlyingValue(), status);
        }
    }

    private StartedStatus(int status) {
        this.value = status;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static StartedStatus fromUnderlyingValue(Integer status) {
        return lookup.get(status);
    }
}
