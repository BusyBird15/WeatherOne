package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum LineType implements OptionList<Integer> {
    Linear(0),
    Curved(1),
    Stepped(2);
    
    private static final Map<Integer, LineType> LOOKUP = null;
    private final int value;

    static {
        int i;
        LOOKUP = new HashMap();
        for (LineType type : values()) {
            LOOKUP.put(type.toUnderlyingValue(), type);
        }
    }

    private LineType(int value2) {
        this.value = value2;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static LineType fromUnderlyingValue(Integer value2) {
        return LOOKUP.get(value2);
    }

    public static LineType fromUnderlyingValue(String value2) {
        return fromUnderlyingValue(Integer.valueOf(Integer.parseInt(value2)));
    }
}
