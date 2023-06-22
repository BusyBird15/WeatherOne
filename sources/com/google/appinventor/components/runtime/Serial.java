package com.google.appinventor.components.runtime;

import android.content.Context;
import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.physicaloid.lib.Physicaloid;
import java.io.UnsupportedEncodingException;

@SimpleObject
@DesignerComponent(androidMinSdk = 12, category = ComponentCategory.CONNECTIVITY, description = "Serial component which can be used to connect to devices like Arduino", iconName = "images/arduino.png", nonVisible = true, version = 1)
@UsesLibraries(libraries = "physicaloid.jar")
public class Serial extends AndroidNonvisibleComponent implements Component {
    private static final String LOG_TAG = "Serial Component";
    private int baudRate = 9600;
    private int bytes = 256;
    private Context context;
    private Physicaloid mPhysicaloid;

    public Serial(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        Log.d(LOG_TAG, "Created");
    }

    @SimpleFunction(description = "Initializes serial connection.")
    public void InitializeSerial() {
        this.mPhysicaloid = new Physicaloid(this.context);
        BaudRate(this.baudRate);
        Log.d(LOG_TAG, "Initialized");
    }

    @SimpleFunction(description = "Opens serial connection. Returns true when opened.")
    public boolean OpenSerial() {
        Log.d(LOG_TAG, "Opening connection");
        if (this.mPhysicaloid != null) {
            return this.mPhysicaloid.open();
        }
        this.form.dispatchErrorOccurredEvent(this, "OpenSerial", ErrorMessages.ERROR_SERIAL_NOT_INITIALIZED, new Object[0]);
        return false;
    }

    @SimpleFunction(description = "Closes serial connection. Returns true when closed.")
    public boolean CloseSerial() {
        Log.d(LOG_TAG, "Closing connection");
        if (this.mPhysicaloid != null) {
            return this.mPhysicaloid.close();
        }
        this.form.dispatchErrorOccurredEvent(this, "CloseSerial", ErrorMessages.ERROR_SERIAL_NOT_INITIALIZED, new Object[0]);
        return false;
    }

    @SimpleFunction(description = "Reads data from serial.")
    public String ReadSerial() {
        if (this.mPhysicaloid == null) {
            this.form.dispatchErrorOccurredEvent(this, "ReadSerial", ErrorMessages.ERROR_SERIAL_NOT_INITIALIZED, new Object[0]);
            return "";
        }
        byte[] buf = new byte[this.bytes];
        if (this.mPhysicaloid.read(buf) <= 0) {
            return "";
        }
        try {
            return new String(buf, "UTF-8");
        } catch (UnsupportedEncodingException mEr) {
            Log.e(LOG_TAG, mEr.getMessage());
            return "";
        }
    }

    @SimpleFunction(description = "Writes given data to serial.")
    public void WriteSerial(String data) {
        if (!data.isEmpty() && this.mPhysicaloid != null) {
            if (this.mPhysicaloid.write(data.getBytes()) == -1) {
                this.form.dispatchErrorOccurredEvent(this, "WriteSerial", ErrorMessages.ERROR_SERIAL_WRITING, new Object[0]);
            }
        } else if (this.mPhysicaloid == null) {
            this.form.dispatchErrorOccurredEvent(this, "WriteSerial", ErrorMessages.ERROR_SERIAL_NOT_INITIALIZED, new Object[0]);
        }
    }

    @SimpleFunction(description = "Writes given data to serial, and appends a new line at the end.")
    public void PrintSerial(String data) {
        if (!data.isEmpty()) {
            WriteSerial(data + "\n");
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns true when the Serial connection is open.")
    public boolean IsOpen() {
        if (this.mPhysicaloid != null) {
            return this.mPhysicaloid.isOpened();
        }
        this.form.dispatchErrorOccurredEvent(this, "IsOpen", ErrorMessages.ERROR_SERIAL_NOT_INITIALIZED, new Object[0]);
        return false;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns true when the Serial has been initialized.")
    public boolean IsInitialized() {
        return this.mPhysicaloid != null;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the current baud rate")
    public int BaudRate() {
        return this.baudRate;
    }

    @DesignerProperty(defaultValue = "9600", editorType = "integer")
    @SimpleProperty
    public void BaudRate(int baudRate2) {
        this.baudRate = baudRate2;
        Log.d(LOG_TAG, "Baud Rate: " + baudRate2);
        if (this.mPhysicaloid != null) {
            this.mPhysicaloid.setBaudrate(baudRate2);
        } else {
            Log.w(LOG_TAG, "Could not set Serial Baud Rate to " + baudRate2 + ". Just saved, not applied to serial! Maybe you forgot to initialize it?");
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the buffer size in bytes")
    public int BufferSize() {
        return this.bytes;
    }

    @DesignerProperty(defaultValue = "256", editorType = "integer")
    @SimpleProperty
    public void BufferSize(int bytes2) {
        this.bytes = bytes2;
        Log.d(LOG_TAG, "Buffer Size: " + bytes2);
    }
}
