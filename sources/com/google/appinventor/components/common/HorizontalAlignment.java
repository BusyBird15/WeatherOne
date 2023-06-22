package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum HorizontalAlignment implements OptionList<Integer> {
    Left(1),
    Center(3),
    Right(2);
    
    private static final Map<Integer, HorizontalAlignment> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (HorizontalAlignment alignment : values()) {
            lookup.put(alignment.toUnderlyingValue(), alignment);
        }
    }

    private HorizontalAlignment(int value2) {
        this.value = value2;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static HorizontalAlignment fromUnderlyingValue(Integer alignment) {
        return lookup.get(alignment);
    }
}
