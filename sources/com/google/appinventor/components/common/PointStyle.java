package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum PointStyle implements OptionList<Integer> {
    Circle(0),
    Square(1),
    Triangle(2),
    Cross(3),
    X(4);
    
    private static final Map<Integer, PointStyle> LOOKUP = null;
    private final int value;

    static {
        int i;
        LOOKUP = new HashMap();
        for (PointStyle style : values()) {
            LOOKUP.put(style.toUnderlyingValue(), style);
        }
    }

    private PointStyle(int value2) {
        this.value = value2;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static PointStyle fromUnderlyingValue(Integer value2) {
        return LOOKUP.get(value2);
    }

    public static PointStyle fromUnderlyingValue(String value2) {
        return fromUnderlyingValue(Integer.valueOf(Integer.parseInt(value2)));
    }
}
