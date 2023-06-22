package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.NxtSensorPort;
import com.google.appinventor.components.runtime.util.ErrorMessages;

@SimpleObject
public abstract class LegoMindstormsNxtSensor extends LegoMindstormsNxtBase {
    protected NxtSensorPort port;

    public abstract void SensorPort(String str);

    /* access modifiers changed from: protected */
    public abstract void initializeSensor(String str);

    static class SensorValue<T> {
        final boolean valid;
        final T value;

        SensorValue(boolean valid2, T value2) {
            this.valid = valid2;
            this.value = value2;
        }
    }

    protected LegoMindstormsNxtSensor(ComponentContainer container, String logTag) {
        super(container, logTag);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The sensor port that the sensor is connected to.", userVisible = false)
    public String SensorPort() {
        return this.port.toUnderlyingValue();
    }

    /* access modifiers changed from: protected */
    public final void setSensorPort(String sensorPortLetter) {
        NxtSensorPort port2 = NxtSensorPort.fromUnderlyingValue(sensorPortLetter);
        if (port2 == null) {
            this.form.dispatchErrorOccurredEvent(this, "SensorPort", ErrorMessages.ERROR_NXT_INVALID_SENSOR_PORT, sensorPortLetter);
            return;
        }
        this.port = port2;
        if (this.bluetooth != null && this.bluetooth.IsConnected()) {
            initializeSensor("SensorPort");
        }
    }

    public void afterConnect(BluetoothConnectionBase bluetoothConnection) {
        initializeSensor("Connect");
    }
}
