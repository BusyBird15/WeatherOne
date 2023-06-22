package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum EndedStatus implements OptionList<Integer> {
    IncomingRejected(1),
    IncomingEnded(2),
    OutgoingEnded(3);
    
    private static final Map<Integer, EndedStatus> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (EndedStatus status : values()) {
            lookup.put(status.toUnderlyingValue(), status);
        }
    }

    private EndedStatus(int status) {
        this.value = status;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static EndedStatus fromUnderlyingValue(Integer status) {
        return lookup.get(status);
    }
}
