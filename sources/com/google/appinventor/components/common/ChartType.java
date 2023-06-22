package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum ChartType implements OptionList<Integer> {
    Line(0),
    Scatter(1),
    Area(2),
    Bar(3),
    Pie(4);
    
    private static final Map<Integer, ChartType> LOOKUP = null;
    private final int value;

    static {
        int i;
        LOOKUP = new HashMap();
        for (ChartType type : values()) {
            LOOKUP.put(type.toUnderlyingValue(), type);
        }
    }

    private ChartType(int value2) {
        this.value = value2;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static ChartType fromUnderlyingValue(Integer type) {
        return LOOKUP.get(type);
    }

    public static ChartType fromUnderlyingValue(String type) {
        return fromUnderlyingValue(Integer.valueOf(Integer.parseInt(type)));
    }
}
