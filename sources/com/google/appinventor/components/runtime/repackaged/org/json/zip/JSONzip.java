package com.google.appinventor.components.runtime.repackaged.org.json.zip;

import androidx.core.view.InputDeviceCompat;
import com.google.appinventor.components.runtime.util.Ev3Constants;

public abstract class JSONzip implements None, PostMortem {
    public static final byte[] bcd = {Ev3Constants.Opcode.MOVE8_8, Ev3Constants.Opcode.MOVE8_16, Ev3Constants.Opcode.MOVE8_32, Ev3Constants.Opcode.MOVE8_F, Ev3Constants.Opcode.MOVE16_8, Ev3Constants.Opcode.MOVE16_16, Ev3Constants.Opcode.MOVE16_32, Ev3Constants.Opcode.MOVE16_F, Ev3Constants.Opcode.MOVE32_8, Ev3Constants.Opcode.MOVE32_16, Ev3Constants.Opcode.RL32, Ev3Constants.Opcode.RL16, 43, Ev3Constants.Opcode.CP_LT16};
    public static final int end = 256;
    public static final int endOfNumber = bcd.length;
    public static final long int14 = 16384;
    public static final long int4 = 16;
    public static final long int7 = 128;
    public static final int maxSubstringLength = 10;
    public static final int minSubstringLength = 3;
    public static final boolean probe = false;
    public static final int substringLimit = 40;
    public static final int[] twos = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536};
    public static final int zipArrayString = 6;
    public static final int zipArrayValue = 7;
    public static final int zipEmptyArray = 1;
    public static final int zipEmptyObject = 0;
    public static final int zipFalse = 3;
    public static final int zipNull = 4;
    public static final int zipObject = 5;
    public static final int zipTrue = 2;
    protected final Huff namehuff = new Huff(InputDeviceCompat.SOURCE_KEYBOARD);
    protected final MapKeep namekeep = new MapKeep(9);
    protected final MapKeep stringkeep = new MapKeep(11);
    protected final Huff substringhuff = new Huff(InputDeviceCompat.SOURCE_KEYBOARD);
    protected final TrieKeep substringkeep = new TrieKeep(12);
    protected final MapKeep values = new MapKeep(10);

    protected JSONzip() {
        this.namehuff.tick(32, 125);
        this.namehuff.tick(97, 122);
        this.namehuff.tick(256);
        this.namehuff.tick(256);
        this.substringhuff.tick(32, 125);
        this.substringhuff.tick(97, 122);
        this.substringhuff.tick(256);
        this.substringhuff.tick(256);
    }

    /* access modifiers changed from: protected */
    public void begin() {
        this.namehuff.generate();
        this.substringhuff.generate();
    }

    static void log() {
        log("\n");
    }

    static void log(int integer) {
        log(new StringBuffer().append(integer).append(" ").toString());
    }

    static void log(int integer, int width) {
        log(new StringBuffer().append(integer).append(":").append(width).append(" ").toString());
    }

    static void log(String string) {
        System.out.print(string);
    }

    static void logchar(int integer, int width) {
        if (integer <= 32 || integer > 125) {
            log(integer, width);
        } else {
            log(new StringBuffer().append("'").append((char) integer).append("':").append(width).append(" ").toString());
        }
    }

    public boolean postMortem(PostMortem pm) {
        JSONzip that = (JSONzip) pm;
        return this.namehuff.postMortem((PostMortem) that.namehuff) && this.namekeep.postMortem(that.namekeep) && this.stringkeep.postMortem(that.stringkeep) && this.substringhuff.postMortem((PostMortem) that.substringhuff) && this.substringkeep.postMortem(that.substringkeep) && this.values.postMortem(that.values);
    }
}
