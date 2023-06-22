package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.util.MapFactory;

@SimpleObject
public abstract class MapFeatureBaseWithFill extends MapFeatureBase implements MapFactory.HasFill {
    private int fillColor = -65536;
    private float fillOpacity = 1.0f;

    public MapFeatureBaseWithFill(MapFactory.MapFeatureContainer container, MapFactory.MapFeatureVisitor<Double> distanceComputation) {
        super(container, distanceComputation);
        FillColor(-65536);
        FillOpacity(1.0f);
    }

    @DesignerProperty(defaultValue = "&HFFFF0000", editorType = "color")
    @SimpleProperty
    public void FillColor(int argb) {
        this.fillColor = argb;
        this.map.getController().updateFeatureFill(this);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The paint color used to fill in the %type%.")
    public int FillColor() {
        return this.fillColor;
    }

    @DesignerProperty(defaultValue = "1.0", editorType = "float")
    @SimpleProperty
    public void FillOpacity(float opacity) {
        this.fillOpacity = opacity;
        this.fillColor = (this.fillColor & 16777215) | (Math.round(255.0f * opacity) << 24);
        this.map.getController().updateFeatureFill(this);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The opacity of the interior of the map feature.")
    public float FillOpacity() {
        return this.fillOpacity;
    }
}
