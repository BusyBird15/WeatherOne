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
import com.google.appinventor.components.common.UltrasonicSensorUnit;
import com.google.appinventor.components.runtime.util.ErrorMessages;

@SimpleObject
@DesignerComponent(category = ComponentCategory.LEGOMINDSTORMS, description = "A component that provides a high-level interface to an ultrasonic sensor on a LEGO MINDSTORMS EV3 robot.", iconName = "images/legoMindstormsEv3.png", nonVisible = true, version = 2)
public class Ev3UltrasonicSensor extends LegoMindstormsEv3Sensor implements Deleteable {
    private static final int DEFAULT_BOTTOM_OF_RANGE = 30;
    private static final int DEFAULT_TOP_OF_RANGE = 90;
    private static final int DELAY_MILLISECONDS = 50;
    private static final int SENSOR_TYPE = 30;
    /* access modifiers changed from: private */
    public boolean aboveRangeEventEnabled;
    /* access modifiers changed from: private */
    public boolean belowRangeEventEnabled;
    /* access modifiers changed from: private */
    public int bottomOfRange;
    /* access modifiers changed from: private */
    public Handler eventHandler = new Handler();
    private UltrasonicSensorUnit mode = UltrasonicSensorUnit.Centimeters;
    /* access modifiers changed from: private */
    public double previousDistance = -1.0d;
    private final Runnable sensorValueChecker = new Runnable() {
        public void run() {
            if (Ev3UltrasonicSensor.this.bluetooth != null && Ev3UltrasonicSensor.this.bluetooth.IsConnected()) {
                double currentDistance = Ev3UltrasonicSensor.this.getDistance("");
                if (Ev3UltrasonicSensor.this.previousDistance < 0.0d) {
                    double unused = Ev3UltrasonicSensor.this.previousDistance = currentDistance;
                    Ev3UltrasonicSensor.this.eventHandler.postDelayed(this, 50);
                    return;
                }
                if (currentDistance < ((double) Ev3UltrasonicSensor.this.bottomOfRange)) {
                    if (Ev3UltrasonicSensor.this.belowRangeEventEnabled && Ev3UltrasonicSensor.this.previousDistance >= ((double) Ev3UltrasonicSensor.this.bottomOfRange)) {
                        Ev3UltrasonicSensor.this.BelowRange();
                    }
                } else if (currentDistance > ((double) Ev3UltrasonicSensor.this.topOfRange)) {
                    if (Ev3UltrasonicSensor.this.aboveRangeEventEnabled && Ev3UltrasonicSensor.this.previousDistance <= ((double) Ev3UltrasonicSensor.this.topOfRange)) {
                        Ev3UltrasonicSensor.this.AboveRange();
                    }
                } else if (Ev3UltrasonicSensor.this.withinRangeEventEnabled && (Ev3UltrasonicSensor.this.previousDistance < ((double) Ev3UltrasonicSensor.this.bottomOfRange) || Ev3UltrasonicSensor.this.previousDistance > ((double) Ev3UltrasonicSensor.this.topOfRange))) {
                    Ev3UltrasonicSensor.this.WithinRange();
                }
                double unused2 = Ev3UltrasonicSensor.this.previousDistance = currentDistance;
            }
            Ev3UltrasonicSensor.this.eventHandler.postDelayed(this, 50);
        }
    };
    /* access modifiers changed from: private */
    public int topOfRange;
    /* access modifiers changed from: private */
    public boolean withinRangeEventEnabled;

    public Ev3UltrasonicSensor(ComponentContainer container) {
        super(container, "Ev3UltrasonicSensor");
        this.eventHandler.post(this.sensorValueChecker);
        TopOfRange(90);
        BottomOfRange(30);
        BelowRangeEventEnabled(false);
        AboveRangeEventEnabled(false);
        WithinRangeEventEnabled(false);
        UnitAbstract(UltrasonicSensorUnit.Centimeters);
    }

    @SimpleFunction(description = "Returns the current distance in centimeters as a value between 0 and 254, or -1 if the distance can not be read.")
    public double GetDistance() {
        return getDistance("GetDistance");
    }

    /* access modifiers changed from: private */
    public double getDistance(String functionName) {
        double distance = readInputSI(functionName, 0, this.sensorPortNumber, 30, this.mode.toInt().intValue());
        if (distance == 255.0d) {
            return -1.0d;
        }
        return distance;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The bottom of the range used for the BelowRange, WithinRange, and AboveRange events.")
    public int BottomOfRange() {
        return this.bottomOfRange;
    }

    @DesignerProperty(defaultValue = "30", editorType = "non_negative_integer")
    @SimpleProperty
    public void BottomOfRange(int bottomOfRange2) {
        this.bottomOfRange = bottomOfRange2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The top of the range used for the BelowRange, WithinRange, and AboveRange events.")
    public int TopOfRange() {
        return this.topOfRange;
    }

    @DesignerProperty(defaultValue = "90", editorType = "non_negative_integer")
    @SimpleProperty
    public void TopOfRange(int topOfRange2) {
        this.topOfRange = topOfRange2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the BelowRange event should fire when the distance goes below the BottomOfRange.")
    public boolean BelowRangeEventEnabled() {
        return this.belowRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void BelowRangeEventEnabled(boolean enabled) {
        this.belowRangeEventEnabled = enabled;
    }

    @SimpleEvent(description = "Called when the detected distance has gone below the range.")
    public void BelowRange() {
        EventDispatcher.dispatchEvent(this, "BelowRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the WithinRange event should fire when the distance goes between the BottomOfRange and the TopOfRange.")
    public boolean WithinRangeEventEnabled() {
        return this.withinRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void WithinRangeEventEnabled(boolean enabled) {
        this.withinRangeEventEnabled = enabled;
    }

    @SimpleEvent(description = "Called when the detected distance has gone within the range.")
    public void WithinRange() {
        EventDispatcher.dispatchEvent(this, "WithinRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the AboveRange event should fire when the distance goes above the TopOfRange.")
    public boolean AboveRangeEventEnabled() {
        return this.aboveRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void AboveRangeEventEnabled(boolean enabled) {
        this.aboveRangeEventEnabled = enabled;
    }

    @SimpleEvent(description = "Called when the detected distance has gone above the range.")
    public void AboveRange() {
        EventDispatcher.dispatchEvent(this, "AboveRange", new Object[0]);
    }

    @DesignerProperty(defaultValue = "cm", editorType = "lego_ev3_ultrasonic_sensor_mode")
    @SimpleProperty
    public void Unit(@Options(UltrasonicSensorUnit.class) String unitName) {
        UltrasonicSensorUnit unit = UltrasonicSensorUnit.fromUnderlyingValue(unitName);
        if (unit == null) {
            this.form.dispatchErrorOccurredEvent(this, "Unit", ErrorMessages.ERROR_EV3_ILLEGAL_ARGUMENT, unitName);
            return;
        }
        setMode(unit);
    }

    public void UnitAbstract(UltrasonicSensorUnit unit) {
        setMode(unit);
    }

    public UltrasonicSensorUnit UnitAbstract() {
        return this.mode;
    }

    @Options(UltrasonicSensorUnit.class)
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The distance unit, which can be either \"cm\" or \"inch\".")
    public String Unit() {
        return this.mode.toUnderlyingValue();
    }

    @Deprecated
    @SimpleFunction(description = "Measure the distance in centimeters.")
    public void SetCmUnit() {
        setMode(UltrasonicSensorUnit.Centimeters);
    }

    @Deprecated
    @SimpleFunction(description = "Measure the distance in inches.")
    public void SetInchUnit() {
        setMode(UltrasonicSensorUnit.Inches);
    }

    private void setMode(UltrasonicSensorUnit newMode) {
        this.previousDistance = -1.0d;
        this.mode = newMode;
    }

    public void onDelete() {
        this.eventHandler.removeCallbacks(this.sensorValueChecker);
        super.onDelete();
    }
}
