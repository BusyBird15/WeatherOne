package com.google.appinventor.components.runtime;

import android.util.Log;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.util.BluetoothReflection;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.Ev3Constants;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.YailList;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@SimpleObject
public abstract class BluetoothConnectionBase extends AndroidNonvisibleComponent implements Component, OnDestroyListener, Deleteable {
    private final List<BluetoothConnectionListener> bluetoothConnectionListeners;
    private ByteOrder byteOrder;
    private Object connectedBluetoothSocket;
    private byte delimiter;
    protected boolean disconnectOnError;
    private String encoding;
    private InputStream inputStream;
    protected final String logTag;
    private OutputStream outputStream;
    private final int sdkLevel;
    protected boolean secure;

    protected BluetoothConnectionBase(ComponentContainer container, String logTag2) {
        this(container.$form(), logTag2, SdkLevel.getLevel());
        this.form.registerForOnDestroy(this);
    }

    private BluetoothConnectionBase(Form form, String logTag2, int sdkLevel2) {
        super(form);
        this.bluetoothConnectionListeners = new ArrayList();
        this.logTag = logTag2;
        this.sdkLevel = sdkLevel2;
        this.disconnectOnError = false;
        HighByteFirst(false);
        CharacterEncoding("UTF-8");
        DelimiterByte(0);
        Secure(true);
    }

    protected BluetoothConnectionBase(OutputStream outputStream2, InputStream inputStream2) {
        this((Form) null, (String) null, 7);
        this.connectedBluetoothSocket = "Not Null";
        this.outputStream = outputStream2;
        this.inputStream = inputStream2;
    }

    /* access modifiers changed from: package-private */
    public void addBluetoothConnectionListener(BluetoothConnectionListener listener) {
        this.bluetoothConnectionListeners.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void removeBluetoothConnectionListener(BluetoothConnectionListener listener) {
        this.bluetoothConnectionListeners.remove(listener);
    }

    private void fireAfterConnectEvent() {
        for (BluetoothConnectionListener listener : this.bluetoothConnectionListeners) {
            listener.afterConnect(this);
        }
    }

    private void fireBeforeDisconnectEvent() {
        for (BluetoothConnectionListener listener : this.bluetoothConnectionListeners) {
            listener.beforeDisconnect(this);
        }
    }

    public final void Initialize() {
    }

    @SimpleEvent(description = "The BluetoothError event is no longer used. Please use the Screen.ErrorOccurred event instead.", userVisible = false)
    public void BluetoothError(String functionName, String message) {
    }

    /* access modifiers changed from: protected */
    public void bluetoothError(String functionName, int errorNumber, Object... messageArgs) {
        this.form.dispatchErrorOccurredEvent(this, functionName, errorNumber, messageArgs);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether Bluetooth is available on the device")
    public boolean Available() {
        if (BluetoothReflection.getBluetoothAdapter() != null) {
            return true;
        }
        return false;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether Bluetooth is enabled")
    public boolean Enabled() {
        Object bluetoothAdapter = BluetoothReflection.getBluetoothAdapter();
        if (bluetoothAdapter == null || !BluetoothReflection.isBluetoothEnabled(bluetoothAdapter)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public final void setConnection(Object bluetoothSocket) throws IOException {
        this.connectedBluetoothSocket = bluetoothSocket;
        this.inputStream = new BufferedInputStream(BluetoothReflection.getInputStream(this.connectedBluetoothSocket));
        this.outputStream = new BufferedOutputStream(BluetoothReflection.getOutputStream(this.connectedBluetoothSocket));
        fireAfterConnectEvent();
    }

    @SimpleFunction(description = "Disconnect from the connected Bluetooth device.")
    public final void Disconnect() {
        if (this.connectedBluetoothSocket != null) {
            fireBeforeDisconnectEvent();
            try {
                BluetoothReflection.closeBluetoothSocket(this.connectedBluetoothSocket);
                Log.i(this.logTag, "Disconnected from Bluetooth device.");
            } catch (IOException e) {
                Log.w(this.logTag, "Error while disconnecting: " + e.getMessage());
            }
            this.connectedBluetoothSocket = null;
        }
        this.inputStream = null;
        this.outputStream = null;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "On devices with API level 14 (LEVEL_ICE_CREAM_SANDWICH) or higher, this property returned is accurate. But on old devices with API level lower than 14, it may not return the current state of connection(e.g., it might be disconnected but you may not know until you attempt to read/write the socket.")
    public final boolean IsConnected() {
        if (this.sdkLevel >= 14) {
            if (this.connectedBluetoothSocket == null || !BluetoothReflection.isBluetoothSocketConnected(this.connectedBluetoothSocket)) {
                return false;
            }
            return true;
        } else if (this.connectedBluetoothSocket == null) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public boolean DisconnectOnError() {
        return this.disconnectOnError;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether to invoke SSP (Simple Secure Pairing), which is supported on devices with Bluetooth v2.1 or higher. When working with embedded Bluetooth devices, this property may need to be set to False. For Android 2.0-2.2, this property setting will be ignored.")
    public boolean Secure() {
        return this.secure;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void Secure(boolean secure2) {
        this.secure = secure2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean HighByteFirst() {
        return this.byteOrder == ByteOrder.BIG_ENDIAN;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void HighByteFirst(boolean highByteFirst) {
        this.byteOrder = highByteFirst ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
    }

    @DesignerProperty(defaultValue = "UTF-8", editorType = "string")
    @SimpleProperty
    public void CharacterEncoding(String encoding2) {
        try {
            "check".getBytes(encoding2);
            this.encoding = encoding2;
        } catch (UnsupportedEncodingException e) {
            bluetoothError("CharacterEncoding", ErrorMessages.ERROR_BLUETOOTH_UNSUPPORTED_ENCODING, encoding2);
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String CharacterEncoding() {
        return this.encoding;
    }

    @DesignerProperty(defaultValue = "0", editorType = "non_negative_integer")
    @SimpleProperty
    public void DelimiterByte(int number) {
        int n = number;
        byte b = (byte) n;
        int n2 = n >> 8;
        if (n2 == 0 || n2 == -1) {
            this.delimiter = b;
            return;
        }
        bluetoothError("DelimiterByte", 511, Integer.valueOf(number));
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public int DelimiterByte() {
        return this.delimiter;
    }

    @SimpleFunction(description = "Send text to the connected Bluetooth device.")
    public void SendText(String text) {
        byte[] bytes;
        try {
            bytes = text.getBytes(this.encoding);
        } catch (UnsupportedEncodingException e) {
            Log.w(this.logTag, "UnsupportedEncodingException: " + e.getMessage());
            bytes = text.getBytes();
        }
        write("SendText", bytes);
    }

    @SimpleFunction(description = "Send a 1-byte number to the connected Bluetooth device.")
    public void Send1ByteNumber(String number) {
        try {
            int n = Integer.decode(number).intValue();
            byte b = (byte) n;
            int n2 = n >> 8;
            if (n2 == 0 || n2 == -1) {
                write("Send1ByteNumber", b);
                return;
            }
            bluetoothError("Send1ByteNumber", 511, number);
        } catch (NumberFormatException e) {
            bluetoothError("Send1ByteNumber", ErrorMessages.ERROR_BLUETOOTH_COULD_NOT_DECODE, number);
        }
    }

    @SimpleFunction(description = "Send a 2-byte number to the connected Bluetooth device.")
    public void Send2ByteNumber(String number) {
        int n;
        try {
            int n2 = Integer.decode(number).intValue();
            byte[] bytes = new byte[2];
            if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                bytes[1] = (byte) (n2 & 255);
                n = n2 >> 8;
                bytes[0] = (byte) (n & 255);
            } else {
                bytes[0] = (byte) (n2 & 255);
                n = n2 >> 8;
                bytes[1] = (byte) (n & 255);
            }
            int n3 = n >> 8;
            if (n3 == 0 || n3 == -1) {
                write("Send2ByteNumber", bytes);
                return;
            }
            bluetoothError("Send2ByteNumber", 512, number, 2);
        } catch (NumberFormatException e) {
            bluetoothError("Send2ByteNumber", ErrorMessages.ERROR_BLUETOOTH_COULD_NOT_DECODE, number);
        }
    }

    @SimpleFunction(description = "Send a 4-byte number to the connected Bluetooth device.")
    public void Send4ByteNumber(String number) {
        long n;
        try {
            long n2 = Long.decode(number).longValue();
            byte[] bytes = new byte[4];
            if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
                bytes[3] = (byte) ((int) (n2 & 255));
                long n3 = n2 >> 8;
                bytes[2] = (byte) ((int) (n3 & 255));
                long n4 = n3 >> 8;
                bytes[1] = (byte) ((int) (n4 & 255));
                n = n4 >> 8;
                bytes[0] = (byte) ((int) (n & 255));
            } else {
                bytes[0] = (byte) ((int) (n2 & 255));
                long n5 = n2 >> 8;
                bytes[1] = (byte) ((int) (n5 & 255));
                long n6 = n5 >> 8;
                bytes[2] = (byte) ((int) (n6 & 255));
                n = n6 >> 8;
                bytes[3] = (byte) ((int) (n & 255));
            }
            long n7 = n >> 8;
            if (n7 == 0 || n7 == -1) {
                write("Send4ByteNumber", bytes);
                return;
            }
            bluetoothError("Send4ByteNumber", 512, number, 4);
        } catch (NumberFormatException e) {
            bluetoothError("Send4ByteNumber", ErrorMessages.ERROR_BLUETOOTH_COULD_NOT_DECODE, number);
        }
    }

    @SimpleFunction(description = "Send a list of byte values to the connected Bluetooth device.")
    public void SendBytes(YailList list) {
        Object[] array = list.toArray();
        byte[] bytes = new byte[array.length];
        int i = 0;
        while (i < array.length) {
            try {
                int n = Integer.decode(array[i].toString()).intValue();
                bytes[i] = (byte) (n & 255);
                int n2 = n >> 8;
                if (n2 == 0 || n2 == -1) {
                    i++;
                } else {
                    bluetoothError("SendBytes", ErrorMessages.ERROR_BLUETOOTH_COULD_NOT_FIT_ELEMENT_IN_BYTE, Integer.valueOf(i + 1));
                    return;
                }
            } catch (NumberFormatException e) {
                bluetoothError("SendBytes", 513, Integer.valueOf(i + 1));
                return;
            }
        }
        write("SendBytes", bytes);
    }

    /* access modifiers changed from: protected */
    public void write(String functionName, byte b) {
        if (!IsConnected()) {
            bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_CONNECTED_TO_DEVICE, new Object[0]);
            return;
        }
        try {
            this.outputStream.write(b);
            this.outputStream.flush();
        } catch (IOException e) {
            Log.e(this.logTag, "IO Exception during Writing" + e.getMessage());
            if (this.disconnectOnError) {
                Disconnect();
            }
            bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_WRITE, e.getMessage());
        }
    }

    /* access modifiers changed from: protected */
    public void write(String functionName, byte[] bytes) {
        if (!IsConnected()) {
            bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_CONNECTED_TO_DEVICE, new Object[0]);
            return;
        }
        try {
            this.outputStream.write(bytes);
            this.outputStream.flush();
        } catch (IOException e) {
            Log.e(this.logTag, "IO Exception during Writing" + e.getMessage());
            if (this.disconnectOnError) {
                Disconnect();
            }
            bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_WRITE, e.getMessage());
        }
    }

    @SimpleFunction(description = "Returns an estimate of the number of bytes that can be received without blocking")
    public int BytesAvailableToReceive() {
        if (!IsConnected()) {
            bluetoothError("BytesAvailableToReceive", ErrorMessages.ERROR_BLUETOOTH_NOT_CONNECTED_TO_DEVICE, new Object[0]);
            return 0;
        }
        try {
            return this.inputStream.available();
        } catch (IOException e) {
            Log.e(this.logTag, "IO Exception during Getting Receive Availability " + e.getMessage());
            if (this.disconnectOnError) {
                Disconnect();
            }
            bluetoothError("BytesAvailableToReceive", ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_READ, e.getMessage());
            return 0;
        }
    }

    @SimpleFunction(description = "Receive text from the connected Bluetooth device. If numberOfBytes is less than 0, read until a delimiter byte value is received.")
    public String ReceiveText(int numberOfBytes) {
        byte[] bytes = read("ReceiveText", numberOfBytes);
        if (numberOfBytes >= 0) {
            return new String(bytes, this.encoding);
        }
        try {
            return new String(bytes, 0, bytes.length - 1, this.encoding);
        } catch (UnsupportedEncodingException e) {
            Log.w(this.logTag, "UnsupportedEncodingException: " + e.getMessage());
            return new String(bytes);
        }
    }

    @SimpleFunction(description = "Receive a signed 1-byte number from the connected Bluetooth device.")
    public int ReceiveSigned1ByteNumber() {
        byte[] bytes = read("ReceiveSigned1ByteNumber", 1);
        if (bytes.length != 1) {
            return 0;
        }
        return bytes[0];
    }

    @SimpleFunction(description = "Receive an unsigned 1-byte number from the connected Bluetooth device.")
    public int ReceiveUnsigned1ByteNumber() {
        byte[] bytes = read("ReceiveUnsigned1ByteNumber", 1);
        if (bytes.length != 1) {
            return 0;
        }
        return bytes[0] & Ev3Constants.Opcode.TST;
    }

    @SimpleFunction(description = "Receive a signed 2-byte number from the connected Bluetooth device.")
    public int ReceiveSigned2ByteNumber() {
        byte[] bytes = read("ReceiveSigned2ByteNumber", 2);
        if (bytes.length != 2) {
            return 0;
        }
        if (this.byteOrder != ByteOrder.BIG_ENDIAN) {
            return (bytes[0] & Ev3Constants.Opcode.TST) | (bytes[1] << 8);
        }
        return (bytes[0] << 8) | (bytes[1] & Ev3Constants.Opcode.TST);
    }

    @SimpleFunction(description = "Receive a unsigned 2-byte number from the connected Bluetooth device.")
    public int ReceiveUnsigned2ByteNumber() {
        byte[] bytes = read("ReceiveUnsigned2ByteNumber", 2);
        if (bytes.length != 2) {
            return 0;
        }
        if (this.byteOrder != ByteOrder.BIG_ENDIAN) {
            return (bytes[0] & Ev3Constants.Opcode.TST) | ((bytes[1] & Ev3Constants.Opcode.TST) << 8);
        }
        return ((bytes[0] & Ev3Constants.Opcode.TST) << 8) | (bytes[1] & Ev3Constants.Opcode.TST);
    }

    @SimpleFunction(description = "Receive a signed 4-byte number from the connected Bluetooth device.")
    public long ReceiveSigned4ByteNumber() {
        byte[] bytes = read("ReceiveSigned4ByteNumber", 4);
        if (bytes.length != 4) {
            return 0;
        }
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            return (long) ((bytes[3] & Ev3Constants.Opcode.TST) | ((bytes[2] & Ev3Constants.Opcode.TST) << 8) | ((bytes[1] & Ev3Constants.Opcode.TST) << 16) | (bytes[0] << 24));
        }
        return (long) ((bytes[0] & Ev3Constants.Opcode.TST) | ((bytes[1] & Ev3Constants.Opcode.TST) << 8) | ((bytes[2] & Ev3Constants.Opcode.TST) << 16) | (bytes[3] << 24));
    }

    @SimpleFunction(description = "Receive a unsigned 4-byte number from the connected Bluetooth device.")
    public long ReceiveUnsigned4ByteNumber() {
        byte[] bytes = read("ReceiveUnsigned4ByteNumber", 4);
        if (bytes.length != 4) {
            return 0;
        }
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            return (((long) bytes[3]) & 255) | ((((long) bytes[2]) & 255) << 8) | ((((long) bytes[1]) & 255) << 16) | ((((long) bytes[0]) & 255) << 24);
        }
        return (((long) bytes[0]) & 255) | ((((long) bytes[1]) & 255) << 8) | ((((long) bytes[2]) & 255) << 16) | ((((long) bytes[3]) & 255) << 24);
    }

    @SimpleFunction(description = "Receive multiple signed byte values from the connected Bluetooth device. If numberOfBytes is less than 0, read until a delimiter byte value is received.")
    public List<Integer> ReceiveSignedBytes(int numberOfBytes) {
        byte[] bytes = read("ReceiveSignedBytes", numberOfBytes);
        List<Integer> list = new ArrayList<>();
        for (byte n : bytes) {
            list.add(Integer.valueOf(n));
        }
        return list;
    }

    @SimpleFunction(description = "Receive multiple unsigned byte values from the connected Bluetooth device. If numberOfBytes is less than 0, read until a delimiter byte value is received.")
    public List<Integer> ReceiveUnsignedBytes(int numberOfBytes) {
        byte[] bytes = read("ReceiveUnsignedBytes", numberOfBytes);
        List<Integer> list = new ArrayList<>();
        for (byte b : bytes) {
            list.add(Integer.valueOf(b & 255));
        }
        return list;
    }

    /* access modifiers changed from: protected */
    public final byte[] read(String functionName, int numberOfBytes) {
        if (!IsConnected()) {
            bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_NOT_CONNECTED_TO_DEVICE, new Object[0]);
            return new byte[0];
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        if (numberOfBytes >= 0) {
            byte[] bytes = new byte[numberOfBytes];
            int totalBytesRead = 0;
            while (true) {
                if (totalBytesRead >= numberOfBytes) {
                    break;
                }
                try {
                    int numBytesRead = this.inputStream.read(bytes, totalBytesRead, bytes.length - totalBytesRead);
                    if (numBytesRead == -1) {
                        bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_END_OF_STREAM, new Object[0]);
                        break;
                    }
                    totalBytesRead += numBytesRead;
                } catch (IOException e) {
                    Log.e(this.logTag, "IO Exception during Reading " + e.getMessage());
                    if (this.disconnectOnError) {
                        Disconnect();
                    }
                    bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_READ, e.getMessage());
                }
            }
            buffer.write(bytes, 0, totalBytesRead);
        } else {
            while (true) {
                try {
                    int value = this.inputStream.read();
                    if (value != -1) {
                        buffer.write(value);
                        if (value == this.delimiter) {
                            break;
                        }
                    } else {
                        bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_END_OF_STREAM, new Object[0]);
                        break;
                    }
                } catch (IOException e2) {
                    Log.e(this.logTag, "IO Exception during Reading " + e2.getMessage());
                    if (this.disconnectOnError) {
                        Disconnect();
                    }
                    bluetoothError(functionName, ErrorMessages.ERROR_BLUETOOTH_UNABLE_TO_READ, e2.getMessage());
                }
            }
        }
        return buffer.toByteArray();
    }

    public void onDestroy() {
        prepareToDie();
    }

    public void onDelete() {
        prepareToDie();
    }

    private void prepareToDie() {
        if (this.connectedBluetoothSocket != null) {
            Disconnect();
        }
    }
}
