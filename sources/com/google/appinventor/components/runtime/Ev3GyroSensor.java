package com.google.appinventor.components.runtime;

import android.os.Handler;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.GyroSensorMode;
import com.google.appinventor.components.runtime.util.ErrorMessages;

@SimpleObject
@DesignerComponent(category = ComponentCategory.LEGOMINDSTORMS, description = "A component that provides a high-level interface to a gyro sensor on a LEGO MINDSTORMS EV3 robot.", iconName = "images/legoMindstormsEv3.png", nonVisible = true, version = 2)
public class Ev3GyroSensor extends LegoMindstormsEv3Sensor implements Deleteable {
    private static final int DELAY_MILLISECONDS = 50;
    private static final int SENSOR_TYPE = 32;
    /* access modifiers changed from: private */
    public Handler eventHandler = new Handler();
    /* access modifiers changed from: private */
    public GyroSensorMode mode = GyroSensorMode.Angle;
    /* access modifiers changed from: private */
    public double previousValue = -1.0d;
    private boolean sensorValueChangedEventEnabled = false;
    private final Runnable sensorValueChecker = new Runnable() {
        public void run() {
            if (Ev3GyroSensor.this.bluetooth != null && Ev3GyroSensor.this.bluetooth.IsConnected()) {
                double currentValue = Ev3GyroSensor.this.getSensorValue("");
                if (Ev3GyroSensor.this.previousValue < 0.0d) {
                    double unused = Ev3GyroSensor.this.previousValue = currentValue;
                    Ev3GyroSensor.this.eventHandler.postDelayed(this, 50);
                    return;
                }
                if ((Ev3GyroSensor.this.mode == GyroSensorMode.Rate && Math.abs(currentValue) >= 1.0d) || (Ev3GyroSensor.this.mode == GyroSensorMode.Angle && Math.abs(currentValue - Ev3GyroSensor.this.previousValue) >= 1.0d)) {
                    Ev3GyroSensor.this.SensorValueChanged(currentValue);
                }
                double unused2 = Ev3GyroSensor.this.previousValue = currentValue;
            }
            Ev3GyroSensor.this.eventHandler.postDelayed(this, 50);
        }
    };

    public Ev3GyroSensor(ComponentContainer container) {
        super(container, "Ev3GyroSensor");
        this.eventHandler.post(this.sensorValueChecker);
        ModeAbstract(GyroSensorMode.Angle);
        SensorValueChangedEventEnabled(false);
    }

    @SimpleFunction(description = "Returns the current angle or rotation speed based on current mode, or -1 if the value cannot be read from sensor.")
    public double GetSensorValue() {
        return getSensorValue("");
    }

    @DesignerProperty(defaultValue = "angle", editorType = "lego_ev3_gyro_sensor_mode")
    @SimpleProperty
    public void Mode(@Options(GyroSensorMode.class) String modeName) {
        GyroSensorMode gyroMode = GyroSensorMode.fromUnderlyingValue(modeName);
        if (gyroMode == null) {
            this.form.dispatchErrorOccurredEvent(this, "Mode", ErrorMessages.ERROR_EV3_ILLEGAL_ARGUMENT, modeName);
            return;
        }
        setMode(gyroMode);
    }

    public void ModeAbstract(GyroSensorMode mode2) {
        setMode(mode2);
    }

    public GyroSensorMode ModeAbstract() {
        return this.mode;
    }

    @Options(GyroSensorMode.class)
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The sensor mode can be a text constant of either \"rate\" or \"angle\", which correspond to SetAngleMode or SetRateMode respectively.")
    public String Mode() {
        return this.mode.toUnderlyingValue();
    }

    @Deprecated
    @SimpleFunction(description = "Measures the orientation of the sensor.")
    public void SetAngleMode() {
        setMode(GyroSensorMode.Angle);
    }

    @Deprecated
    @SimpleFunction(description = "Measures the angular velocity of the sensor.")
    public void SetRateMode() {
        setMode(GyroSensorMode.Rate);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the SensorValueChanged event should fire when the sensor value changed.")
    public boolean SensorValueChangedEventEnabled() {
        return this.sensorValueChangedEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void SensorValueChangedEventEnabled(boolean enabled) {
        this.sensorValueChangedEventEnabled = enabled;
    }

    @SimpleEvent(description = "Called then the sensor value changed.")
    public void SensorValueChanged(double sensorValue) {
        EventDispatcher.dispatchEvent(this, "SensorValueChanged", Double.valueOf(sensorValue));
    }

    /* access modifiers changed from: private */
    public double getSensorValue(String functionName) {
        return readInputSI(functionName, 0, this.sensorPortNumber, 32, this.mode.toInt().intValue());
    }

    private void setMode(GyroSensorMode newMode) {
        if (newMode != this.mode) {
            this.previousValue = -1.0d;
        }
        this.mode = newMode;
    }

    public void onDelete() {
        super.onDelete();
    }
}
