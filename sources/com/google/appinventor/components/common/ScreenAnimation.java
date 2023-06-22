package com.google.appinventor.components.common;

import java.util.HashMap;
import java.util.Map;

public enum ScreenAnimation implements OptionList<String> {
    Default("default"),
    Fade("fade"),
    Zoom("zoom"),
    SlideHorizontal("slidehorizontal"),
    SlideVertical("slidevertical"),
    None("none");
    
    private static final Map<String, ScreenAnimation> lookup = null;
    private final String value;

    static {
        int i;
        lookup = new HashMap();
        for (ScreenAnimation anim : values()) {
            lookup.put(anim.toUnderlyingValue(), anim);
        }
    }

    private ScreenAnimation(String anim) {
        this.value = anim;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public static ScreenAnimation fromUnderlyingValue(String anim) {
        return lookup.get(anim);
    }
}
