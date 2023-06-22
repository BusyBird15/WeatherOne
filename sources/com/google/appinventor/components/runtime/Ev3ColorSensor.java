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
import com.google.appinventor.components.common.ColorSensorMode;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ErrorMessages;

@SimpleObject
@DesignerComponent(category = ComponentCategory.LEGOMINDSTORMS, description = "A component that provides a high-level interface to a color sensor on a LEGO MINDSTORMS EV3 robot.", iconName = "images/legoMindstormsEv3.png", nonVisible = true, version = 2)
public class Ev3ColorSensor extends LegoMindstormsEv3Sensor implements Deleteable {
    private static final int DEFAULT_BOTTOM_OF_RANGE = 30;
    private static final int DEFAULT_TOP_OF_RANGE = 60;
    private static final int DELAY_MILLISECONDS = 50;
    private static final int SENSOR_TYPE = 29;
    /* access modifiers changed from: private */
    public boolean aboveRangeEventEnabled;
    /* access modifiers changed from: private */
    public boolean belowRangeEventEnabled;
    /* access modifiers changed from: private */
    public int bottomOfRange;
    /* access modifiers changed from: private */
    public boolean colorChangedEventEnabled;
    /* access modifiers changed from: private */
    public Handler eventHandler = new Handler();
    /* access modifiers changed from: private */
    public ColorSensorMode mode = ColorSensorMode.Reflected;
    /* access modifiers changed from: private */
    public int previousColor = -1;
    /* access modifiers changed from: private */
    public int previousLightLevel = 0;
    private final Runnable sensorValueChecker = new Runnable() {
        public void run() {
            if (Ev3ColorSensor.this.bluetooth != null && Ev3ColorSensor.this.bluetooth.IsConnected()) {
                if (Ev3ColorSensor.this.mode == ColorSensorMode.Color) {
                    int currentColor = Ev3ColorSensor.this.getSensorValue("");
                    if (Ev3ColorSensor.this.previousColor < 0) {
                        int unused = Ev3ColorSensor.this.previousColor = currentColor;
                        Ev3ColorSensor.this.eventHandler.postDelayed(this, 50);
                        return;
                    }
                    if (currentColor != Ev3ColorSensor.this.previousColor && Ev3ColorSensor.this.colorChangedEventEnabled) {
                        Ev3ColorSensor.this.ColorChanged(currentColor, Ev3ColorSensor.this.toColorName(currentColor));
                    }
                    int unused2 = Ev3ColorSensor.this.previousColor = currentColor;
                } else {
                    int currentLightLevel = Ev3ColorSensor.this.getSensorValue("");
                    if (Ev3ColorSensor.this.previousLightLevel < 0) {
                        int unused3 = Ev3ColorSensor.this.previousLightLevel = currentLightLevel;
                        Ev3ColorSensor.this.eventHandler.postDelayed(this, 50);
                        return;
                    }
                    if (currentLightLevel < Ev3ColorSensor.this.bottomOfRange) {
                        if (Ev3ColorSensor.this.belowRangeEventEnabled && Ev3ColorSensor.this.previousLightLevel >= Ev3ColorSensor.this.bottomOfRange) {
                            Ev3ColorSensor.this.BelowRange();
                        }
                    } else if (currentLightLevel > Ev3ColorSensor.this.topOfRange) {
                        if (Ev3ColorSensor.this.aboveRangeEventEnabled && Ev3ColorSensor.this.previousLightLevel <= Ev3ColorSensor.this.topOfRange) {
                            Ev3ColorSensor.this.AboveRange();
                        }
                    } else if (Ev3ColorSensor.this.withinRangeEventEnabled && (Ev3ColorSensor.this.previousLightLevel < Ev3ColorSensor.this.bottomOfRange || Ev3ColorSensor.this.previousLightLevel > Ev3ColorSensor.this.topOfRange)) {
                        Ev3ColorSensor.this.WithinRange();
                    }
                    int unused4 = Ev3ColorSensor.this.previousLightLevel = currentLightLevel;
                }
            }
            Ev3ColorSensor.this.eventHandler.postDelayed(this, 50);
        }
    };
    /* access modifiers changed from: private */
    public int topOfRange;
    /* access modifiers changed from: private */
    public boolean withinRangeEventEnabled;

    public Ev3ColorSensor(ComponentContainer container) {
        super(container, "Ev3ColorSensor");
        this.eventHandler.post(this.sensorValueChecker);
        TopOfRange(60);
        BottomOfRange(30);
        BelowRangeEventEnabled(false);
        AboveRangeEventEnabled(false);
        WithinRangeEventEnabled(false);
        ColorChangedEventEnabled(false);
        ModeAbstract(ColorSensorMode.Reflected);
    }

    @SimpleFunction(description = "It returns the light level in percentage, or -1 when the light level cannot be read.")
    public int GetLightLevel() {
        if (this.mode == ColorSensorMode.Color) {
            return -1;
        }
        return getSensorValue("GetLightLevel");
    }

    @SimpleFunction(description = "It returns the color code from 0 to 7 corresponding to no color, black, blue, green, yellow, red, white and brown.")
    public int GetColorCode() {
        if (this.mode != ColorSensorMode.Color) {
            return 0;
        }
        return getSensorValue("GetColorCode");
    }

    @SimpleFunction(description = "Return the color name in one of \"No Color\", \"Black\", \"Blue\", \"Green\", \"Yellow\", \"Red\", \"White\", \"Brown\".")
    public String GetColorName() {
        if (this.mode != ColorSensorMode.Color) {
            return "No Color";
        }
        return toColorName(getSensorValue("GetColorName"));
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

    @DesignerProperty(defaultValue = "60", editorType = "non_negative_integer")
    @SimpleProperty
    public void TopOfRange(int topOfRange2) {
        this.topOfRange = topOfRange2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the BelowRange event should fire when the light level goes below the BottomOfRange.")
    public boolean BelowRangeEventEnabled() {
        return this.belowRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void BelowRangeEventEnabled(boolean enabled) {
        this.belowRangeEventEnabled = enabled;
    }

    @SimpleEvent(description = "Light level has gone below the range.")
    public void BelowRange() {
        EventDispatcher.dispatchEvent(this, "BelowRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the WithinRange event should fire when the light level goes between the BottomOfRange and the TopOfRange.")
    public boolean WithinRangeEventEnabled() {
        return this.withinRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void WithinRangeEventEnabled(boolean enabled) {
        this.withinRangeEventEnabled = enabled;
    }

    @SimpleEvent(description = "Light level has gone within the range.")
    public void WithinRange() {
        EventDispatcher.dispatchEvent(this, "WithinRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the AboveRange event should fire when the light level goes above the TopOfRange.")
    public boolean AboveRangeEventEnabled() {
        return this.aboveRangeEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void AboveRangeEventEnabled(boolean enabled) {
        this.aboveRangeEventEnabled = enabled;
    }

    @SimpleEvent(description = "Light level has gone above the range.")
    public void AboveRange() {
        EventDispatcher.dispatchEvent(this, "AboveRange", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the ColorChanged event should fire when the Mode property is set to \"color\" and the detected color changes.")
    public boolean ColorChangedEventEnabled() {
        return this.colorChangedEventEnabled;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void ColorChangedEventEnabled(boolean enabled) {
        this.colorChangedEventEnabled = enabled;
    }

    @SimpleEvent(description = "Called when the detected color has changed. The ColorChanged event will occur if the Mode property is set to \"color\" and the ColorChangedEventEnabled property is set to True.")
    public void ColorChanged(int colorCode, String colorName) {
        EventDispatcher.dispatchEvent(this, "ColorChanged", Integer.valueOf(colorCode), colorName);
    }

    /* access modifiers changed from: private */
    public int getSensorValue(String functionName) {
        int level = readInputPercentage(functionName, 0, this.sensorPortNumber, 29, this.mode.toInt().intValue());
        if (this.mode != ColorSensorMode.Color) {
            return level;
        }
        switch (level) {
            case 12:
                return 1;
            case 25:
                return 2;
            case 37:
                return 3;
            case 50:
                return 4;
            case 62:
                return 5;
            case 75:
                return 6;
            case 87:
                return 7;
            default:
                return 0;
        }
    }

    /* access modifiers changed from: private */
    public String toColorName(int colorCode) {
        if (this.mode != ColorSensorMode.Color) {
            return "No Color";
        }
        switch (colorCode) {
            case 0:
                return "No Color";
            case 1:
                return "Black";
            case 2:
                return "Blue";
            case 3:
                return "Green";
            case 4:
                return "Yellow";
            case 5:
                return "Red";
            case 6:
                return "White";
            case 7:
                return "Brown";
            default:
                return "No Color";
        }
    }

    @DesignerProperty(defaultValue = "reflected", editorType = "lego_ev3_color_sensor_mode")
    @SimpleProperty
    public void Mode(@Options(ColorSensorMode.class) String modeName) {
        ColorSensorMode mode2 = ColorSensorMode.fromUnderlyingValue(modeName);
        if (mode2 == null) {
            this.form.dispatchErrorOccurredEvent(this, "Mode", ErrorMessages.ERROR_EV3_ILLEGAL_ARGUMENT, modeName);
            return;
        }
        setMode(mode2);
    }

    public void ModeAbstract(ColorSensorMode mode2) {
        setMode(mode2);
    }

    public ColorSensorMode ModeAbstract() {
        return this.mode;
    }

    @Options(ColorSensorMode.class)
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Get the current sensor mode.")
    public String Mode() {
        return this.mode.toUnderlyingValue();
    }

    @Deprecated
    @SimpleFunction(description = "Enter the color detection mode.")
    public void SetColorMode() {
        setMode(ColorSensorMode.Color);
    }

    @Deprecated
    @SimpleFunction(description = "Make the sensor read the light level with reflected light.")
    public void SetReflectedMode() {
        setMode(ColorSensorMode.Reflected);
    }

    @Deprecated
    @SimpleFunction(description = "Make the sensor read the light level without reflected light.")
    public void SetAmbientMode() {
        setMode(ColorSensorMode.Ambient);
    }

    private void setMode(ColorSensorMode newMode) {
        this.previousColor = -1;
        this.previousLightLevel = -1;
        this.mode = newMode;
    }

    public void onDelete() {
        this.eventHandler.removeCallbacks(this.sensorValueChecker);
        super.onDelete();
    }
}
