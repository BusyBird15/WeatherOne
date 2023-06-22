package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;

@SimpleObject
@DesignerComponent(category = ComponentCategory.SENSORS, description = "A sensor component that can measure the ambient (external) temperature. Most Android devices do not have this sensor.", iconName = "images/thermometer.png", nonVisible = true, version = 1)
public class Thermometer extends SingleValueSensor {
    public Thermometer(ComponentContainer container) {
        super(container.$form(), 13);
    }

    /* access modifiers changed from: protected */
    public void onValueChanged(float value) {
        TemperatureChanged(value);
    }

    @SimpleEvent(description = "Called when a change is detected in the temperature (in degrees Celsius).")
    public void TemperatureChanged(float temperature) {
        EventDispatcher.dispatchEvent(this, "TemperatureChanged", Float.valueOf(temperature));
    }

    @SimpleProperty(description = "The temperature in degrees Celsius, if the sensor is available and enabled")
    public float Temperature() {
        return getValue();
    }
}
