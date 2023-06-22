package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;

@SimpleObject
@DesignerComponent(category = ComponentCategory.SENSORS, description = "A sensor component that can measure the ambient air pressure.", iconName = "images/barometer.png", nonVisible = true, version = 1)
public class Barometer extends SingleValueSensor {
    public Barometer(ComponentContainer container) {
        super(container.$form(), 6);
    }

    /* access modifiers changed from: protected */
    public void onValueChanged(float value) {
        AirPressureChanged(value);
    }

    @SimpleEvent
    public void AirPressureChanged(float pressure) {
        EventDispatcher.dispatchEvent(this, "AirPressureChanged", Float.valueOf(pressure));
    }

    @SimpleProperty(description = "The air pressure in hPa (millibar), if the sensor is available and enabled.")
    public float AirPressure() {
        return getValue();
    }
}
