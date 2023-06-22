package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum TransportMethod implements OptionList<String> {
    Foot("foot-walking"),
    Car("driving-car"),
    Bicycle("cycling-regular"),
    Wheelchair("wheelchair");
    
    private static final Map<String, TransportMethod> lookup = null;
    private final String value;

    static {
        int i;
        lookup = new HashMap();
        for (TransportMethod method : values()) {
            lookup.put(method.toUnderlyingValue(), method);
        }
    }

    private TransportMethod(String value2) {
        this.value = value2;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public static TransportMethod fromUnderlyingValue(String method) {
        return lookup.get(method);
    }
}
