package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum FileScope implements OptionList<String> {
    App,
    Asset,
    Cache,
    Legacy,
    Private,
    Shared;
    
    private static final Map<String, FileScope> LOOKUP = null;

    static {
        int i;
        LOOKUP = new HashMap();
        for (FileScope scope : values()) {
            LOOKUP.put(scope.toUnderlyingValue(), scope);
        }
    }

    public static FileScope fromUnderlyingValue(String scope) {
        return LOOKUP.get(scope);
    }

    public String toUnderlyingValue() {
        return name();
    }
}
