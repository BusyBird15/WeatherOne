package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum NxtSensorType implements OptionList<Integer> {
    NoSensor(0),
    Touch(1),
    LightOn(5),
    LightOff(6),
    SoundDB(7),
    SoundDBA(8),
    ColorFull(13),
    ColorRed(14),
    ColorGreen(15),
    ColorBlue(16),
    ColorNone(17),
    Digital12C(10),
    Digital12C9V(11),
    RcxTemperature(2),
    RcxLight(3),
    RcxAngle(4);
    
    private static final Map<Integer, NxtSensorType> lookup = null;
    private final int value;

    static {
        int i;
        lookup = new HashMap();
        for (NxtSensorType type : values()) {
            lookup.put(type.toUnderlyingValue(), type);
        }
    }

    private NxtSensorType(int type) {
        this.value = type;
    }

    public Integer toUnderlyingValue() {
        return Integer.valueOf(this.value);
    }

    public static NxtSensorType fromUnderlyingValue(Integer type) {
        return lookup.get(type);
    }
}
