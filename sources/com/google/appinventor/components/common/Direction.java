package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum Direction implements OptionList<Integer> {
    North(1),
    Northeast(2),
    East(3),
    Southeast(4),
    South(-1),
    Southwest(-2),
    West(-3),
    Northwest(-4);
    
    private static final Map<Integer, Direction> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (Direction dir : values()) {
            lookup.put(dir.toUnderlyingValue(), dir);
        }
    }

    private Direction(int dir) {
        this.value = dir;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static Direction fromUnderlyingValue(Integer dir) {
        return lookup.get(dir);
    }
}
