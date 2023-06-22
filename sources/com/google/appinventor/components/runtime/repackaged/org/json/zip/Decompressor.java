package com.google.appinventor.components.runtime.repackaged.org.json.zip;

import com.google.appinventor.components.runtime.repackaged.org.json.JSONArray;
import com.google.appinventor.components.runtime.repackaged.org.json.JSONException;
import com.google.appinventor.components.runtime.repackaged.org.json.JSONObject;
import com.google.appinventor.components.runtime.repackaged.org.json.Kim;
import java.io.UnsupportedEncodingException;

public class Decompressor extends JSONzip {
    BitReader bitreader;

    public Decompressor(BitReader bitreader2) {
        this.bitreader = bitreader2;
    }

    private boolean bit() throws JSONException {
        try {
            return this.bitreader.bit();
        } catch (Throwable e) {
            throw new JSONException(e);
        }
    }

    private Object getAndTick(Keep keep, BitReader bitreader2) throws JSONException {
        try {
            int integer = bitreader2.read(keep.bitsize());
            Object value = keep.value(integer);
            if (integer >= keep.length) {
                throw new JSONException("Deep error.");
            }
            keep.tick(integer);
            return value;
        } catch (Throwable e) {
            throw new JSONException(e);
        }
    }

    public boolean pad(int factor) throws JSONException {
        try {
            return this.bitreader.pad(factor);
        } catch (Throwable e) {
            throw new JSONException(e);
        }
    }

    private int read(int width) throws JSONException {
        try {
            return this.bitreader.read(width);
        } catch (Throwable e) {
            throw new JSONException(e);
        }
    }

    private JSONArray readArray(boolean stringy) throws JSONException {
        JSONArray jsonarray = new JSONArray();
        jsonarray.put(stringy ? readString() : readValue());
        while (true) {
            if (bit()) {
                jsonarray.put(stringy ? readString() : readValue());
            } else if (!bit()) {
                return jsonarray;
            } else {
                jsonarray.put(stringy ? readValue() : readString());
            }
        }
    }

    private Object readJSON() throws JSONException {
        switch (read(3)) {
            case 0:
                return new JSONObject();
            case 1:
                return new JSONArray();
            case 2:
                return Boolean.TRUE;
            case 3:
                return Boolean.FALSE;
            case 5:
                return readObject();
            case 6:
                return readArray(true);
            case 7:
                return readArray(false);
            default:
                return JSONObject.NULL;
        }
    }

    private String readName() throws JSONException {
        byte[] bytes = new byte[65536];
        int length = 0;
        if (bit()) {
            return getAndTick(this.namekeep, this.bitreader).toString();
        }
        while (true) {
            int c = this.namehuff.read(this.bitreader);
            if (c == 256) {
                break;
            }
            bytes[length] = (byte) c;
            length++;
        }
        if (length == 0) {
            return "";
        }
        Kim kim = new Kim(bytes, length);
        this.namekeep.register(kim);
        return kim.toString();
    }

    private JSONObject readObject() throws JSONException {
        JSONObject jsonobject = new JSONObject();
        do {
            jsonobject.put(readName(), !bit() ? readString() : readValue());
        } while (bit());
        return jsonobject;
    }

    private String readString() throws JSONException {
        int thru = 0;
        int previousFrom = -1;
        int previousThru = 0;
        if (bit()) {
            return getAndTick(this.stringkeep, this.bitreader).toString();
        }
        byte[] bytes = new byte[65536];
        boolean one = bit();
        this.substringkeep.reserve();
        while (true) {
            if (one) {
                int from = thru;
                thru = ((Kim) getAndTick(this.substringkeep, this.bitreader)).copy(bytes, from);
                if (previousFrom != -1) {
                    this.substringkeep.registerOne(new Kim(bytes, previousFrom, previousThru + 1));
                }
                previousFrom = from;
                previousThru = thru;
                one = bit();
            } else {
                while (true) {
                    int c = this.substringhuff.read(this.bitreader);
                    if (c == 256) {
                        break;
                    }
                    bytes[thru] = (byte) c;
                    thru++;
                    if (previousFrom != -1) {
                        this.substringkeep.registerOne(new Kim(bytes, previousFrom, previousThru + 1));
                        previousFrom = -1;
                    }
                }
                if (!bit()) {
                    break;
                }
                one = true;
            }
        }
        if (thru == 0) {
            return "";
        }
        Kim kim = new Kim(bytes, thru);
        this.stringkeep.register(kim);
        this.substringkeep.registerMany(kim);
        return kim.toString();
    }

    private Object readValue() throws JSONException {
        int i = 4;
        switch (read(2)) {
            case 0:
                if (bit()) {
                    i = !bit() ? 7 : 14;
                }
                return new Integer(read(i));
            case 1:
                byte[] bytes = new byte[256];
                int length = 0;
                while (true) {
                    int c = read(4);
                    if (c == endOfNumber) {
                        try {
                            Object value = JSONObject.stringToValue(new String(bytes, 0, length, "US-ASCII"));
                            this.values.register(value);
                            return value;
                        } catch (UnsupportedEncodingException e) {
                            throw new JSONException((Throwable) e);
                        }
                    } else {
                        bytes[length] = bcd[c];
                        length++;
                    }
                }
            case 2:
                return getAndTick(this.values, this.bitreader);
            case 3:
                return readJSON();
            default:
                throw new JSONException("Impossible.");
        }
    }

    public Object unzip() throws JSONException {
        begin();
        return readJSON();
    }
}
