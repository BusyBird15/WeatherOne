package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum VerticalAlignment implements OptionList<Integer> {
    Top(1),
    Center(2),
    Bottom(3);
    
    private static final Map<Integer, VerticalAlignment> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (VerticalAlignment alignment : values()) {
            lookup.put(alignment.toUnderlyingValue(), alignment);
        }
    }

    private VerticalAlignment(int value2) {
        this.value = value2;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static VerticalAlignment fromUnderlyingValue(Integer alignment) {
        return lookup.get(alignment);
    }
}
