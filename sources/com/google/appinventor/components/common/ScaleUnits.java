package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum ScaleUnits implements OptionList<Integer> {
    Metric(1),
    Imperial(2);
    
    private static final Map<Integer, ScaleUnits> lookup = null;
    private final Integer value;

    static {
        int i;
        lookup = new HashMap();
        for (ScaleUnits unit : values()) {
            lookup.put(unit.toUnderlyingValue(), unit);
        }
    }

    private ScaleUnits(Integer value2) {
        this.value = value2;
    }

    public Integer toUnderlyingValue() {
        return this.value;
    }

    public static ScaleUnits fromUnderlyingValue(Integer unit) {
        return lookup.get(unit);
    }
}
