package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum MapType implements OptionList<Integer> {
    Road(1),
    Aerial(2),
    Terrain(3);
    
    private static final Map<Integer, MapType> lookup = null;
    private final Integer value;

    static {
        int i;
        lookup = new HashMap();
        for (MapType type : values()) {
            lookup.put(type.toUnderlyingValue(), type);
        }
    }

    private MapType(Integer value2) {
        this.value = value2;
    }

    public Integer toUnderlyingValue() {
        return this.value;
    }

    public static MapType fromUnderlyingValue(Integer type) {
        return lookup.get(type);
    }
}
