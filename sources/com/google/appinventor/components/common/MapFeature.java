package com.google.appinventor.components.common;

import com.google.appinventor.components.runtime.util.MapFactory;
import java.util.HashMap;
import java.util.Map;

public enum MapFeature implements OptionList<String> {
    Circle(MapFactory.MapFeatureType.TYPE_CIRCLE),
    LineString(MapFactory.MapFeatureType.TYPE_LINESTRING),
    Marker(MapFactory.MapFeatureType.TYPE_MARKER),
    Polygon(MapFactory.MapFeatureType.TYPE_POLYGON),
    Rectangle(MapFactory.MapFeatureType.TYPE_RECTANGLE);
    
    private static final Map<String, MapFeature> lookup = null;
    private final String value;

    static {
        int i;
        lookup = new HashMap();
        for (MapFeature feat : values()) {
            lookup.put(feat.toUnderlyingValue(), feat);
        }
    }

    private MapFeature(String feat) {
        this.value = feat;
    }

    public String toUnderlyingValue() {
        return this.value;
    }

    public static MapFeature fromUnderlyingValue(String feat) {
        return lookup.get(feat);
    }
}
