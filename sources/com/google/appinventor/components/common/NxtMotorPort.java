package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum NxtMotorPort implements OptionList<String> {
    PortA("A", 0),
    PortB("B", 1),
    PortC("C", 2);
    
    private static final Map<String, NxtMotorPort> lookup = null;
    private final int intValue;
    private final String value;

    static {
        int i;
        lookup = new HashMap();
        for (NxtMotorPort port : values()) {
            String str = port.toUnderlyingValue();
            lookup.put(str, port);
            lookup.put(str.toLowerCase(), port);
        }
    }

    private NxtMotorPort(String port, int intPort) {
        this.value = port;
        this.intValue = intPort;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public Integer toInt() {
        return Integer.valueOf(this.intValue);
    }

    public static NxtMotorPort fromUnderlyingValue(String port) {
        return lookup.get(port);
    }
}
