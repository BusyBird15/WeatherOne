package com.google.appinventor.components.runtime;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;

@SimpleObject
public abstract class SingleValueSensor extends AndroidNonvisibleComponent implements OnPauseListener, OnResumeListener, SensorComponent, SensorEventListener, Deleteable {
    private static final int DEFAULT_REFRESH_TIME = 1000;
    protected boolean enabled = true;
    protected int refreshTime = 1000;
    private Sensor sensor;
    protected final SensorManager sensorManager;
    protected int sensorType;
    protected float value;

    /* access modifiers changed from: protected */
    public abstract void onValueChanged(float f);

    public SingleValueSensor(ComponentContainer container, int sensorType2) {
        super(container.$form());
        this.sensorType = sensorType2;
        this.form.registerForOnResume(this);
        this.form.registerForOnPause(this);
        this.sensorManager = (SensorManager) container.$context().getSystemService("sensor");
        this.sensor = this.sensorManager.getDefaultSensor(sensorType2);
        startListening();
    }

    /* access modifiers changed from: protected */
    public void startListening() {
        if (Build.VERSION.SDK_INT >= 9) {
            this.sensorManager.registerListener(this, this.sensor, this.refreshTime * 1000);
            return;
        }
        this.sensorManager.registerListener(this, this.sensor, 2);
    }

    /* access modifiers changed from: protected */
    public void stopListening() {
        this.sensorManager.unregisterListener(this);
    }

    @SimpleProperty(description = "Specifies whether or not the device has the hardware to support the %type% component.")
    public boolean Available() {
        return isAvailable();
    }

    @SimpleProperty(description = "If enabled, then device will listen for changes.")
    public boolean Enabled() {
        return this.enabled;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void Enabled(boolean enabled2) {
        setEnabled(enabled2);
    }

    @SimpleProperty(description = "The requested minimum time in milliseconds between changes in readings being reported. Android is not guaranteed to honor the request. Setting this property has no effect on pre-Gingerbread devices.")
    public int RefreshTime() {
        return this.refreshTime;
    }

    @DesignerProperty(defaultValue = "1000", editorType = "non_negative_integer")
    @SimpleProperty
    public void RefreshTime(int time) {
        this.refreshTime = time;
        if (this.enabled) {
            stopListening();
            startListening();
        }
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.enabled && sensorEvent.sensor.getType() == this.sensorType) {
            this.value = sensorEvent.values[0];
            onValueChanged(this.value);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAvailable() {
        return this.sensorManager.getSensorList(this.sensorType).size() > 0;
    }

    /* access modifiers changed from: protected */
    public void setEnabled(boolean enabled2) {
        if (this.enabled != enabled2) {
            this.enabled = enabled2;
            if (enabled2) {
                startListening();
            } else {
                stopListening();
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor2, int accuracy) {
    }

    public void onPause() {
        if (this.enabled) {
            stopListening();
        }
    }

    public void onResume() {
        if (this.enabled) {
            startListening();
        }
    }

    public void onDelete() {
        if (this.enabled) {
            stopListening();
        }
    }

    /* access modifiers changed from: protected */
    public float getValue() {
        return this.value;
    }
}
