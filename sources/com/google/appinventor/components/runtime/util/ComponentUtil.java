package com.google.appinventor.components.runtime.util;

import gnu.mapping.Environment;
import gnu.mapping.LocationEnumeration;
import java.util.ArrayList;
import java.util.List;

public final class ComponentUtil {
    private ComponentUtil() {
    }

    public static List<Object> filterComponentsOfType(Environment env, String type) {
        List<Object> components = new ArrayList<>();
        LocationEnumeration iterator = env.enumerateAllLocations();
        while (iterator.hasNext()) {
            Object maybeComponent = iterator.next().get();
            if (maybeComponent.getClass().getName().equals(type)) {
                components.add(maybeComponent);
            }
        }
        return components;
    }
}
