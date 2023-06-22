package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.util.Ev3Constants;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;

public class Ev3BinaryParser {
    private static byte PRIMPAR_1_BYTE = 1;
    private static byte PRIMPAR_2_BYTES = 2;
    private static byte PRIMPAR_4_BYTES = 3;
    private static byte PRIMPAR_ADDR = 8;
    private static byte PRIMPAR_BYTES = 7;
    private static byte PRIMPAR_CONST = 0;
    private static byte PRIMPAR_CONST_SIGN = 32;
    private static byte PRIMPAR_GLOBAL = 32;
    private static byte PRIMPAR_HANDLE = 16;
    private static byte PRIMPAR_INDEX = 31;
    private static byte PRIMPAR_LOCAL = 0;
    private static byte PRIMPAR_LONG = Byte.MIN_VALUE;
    private static byte PRIMPAR_SHORT = 0;
    private static byte PRIMPAR_STRING = 4;
    private static byte PRIMPAR_STRING_OLD = 0;
    private static byte PRIMPAR_VALUE = Ev3Constants.Opcode.MOVEF_F;
    private static byte PRIMPAR_VARIABEL = Ev3Constants.Opcode.JR;

    private static class FormatLiteral {
        public int size;
        public char symbol;

        public FormatLiteral(char symbol2, int size2) {
            this.symbol = symbol2;
            this.size = size2;
        }
    }

    public static byte[] pack(String format, Object... values) throws IllegalArgumentException {
        String[] formatTokens = format.split("(?<=\\D)");
        FormatLiteral[] literals = new FormatLiteral[formatTokens.length];
        int index = 0;
        int bufferCapacity = 0;
        for (int i = 0; i < formatTokens.length; i++) {
            String token = formatTokens[i];
            char symbol = token.charAt(token.length() - 1);
            int size = 1;
            boolean sizeSpecified = false;
            if (token.length() != 1) {
                size = Integer.parseInt(token.substring(0, token.length() - 1));
                sizeSpecified = true;
                if (size < 1) {
                    throw new IllegalArgumentException("Illegal format string");
                }
            }
            switch (symbol) {
                case 'B':
                    bufferCapacity += size;
                    index++;
                    break;
                case 'F':
                    bufferCapacity += size * 4;
                    index++;
                    break;
                case 'H':
                    bufferCapacity += size * 2;
                    index++;
                    break;
                case 'I':
                    bufferCapacity += size * 4;
                    index++;
                    break;
                case 'L':
                    bufferCapacity += size * 8;
                    index++;
                    break;
                case 'S':
                    if (!sizeSpecified) {
                        bufferCapacity += values[index].length() + 1;
                        index++;
                        break;
                    } else {
                        throw new IllegalArgumentException("Illegal format string");
                    }
                case 'b':
                    bufferCapacity += size;
                    index += size;
                    break;
                case 'f':
                    bufferCapacity += size * 4;
                    index += size;
                    break;
                case 'h':
                    bufferCapacity += size * 2;
                    index += size;
                    break;
                case 'i':
                    bufferCapacity += size * 4;
                    index += size;
                    break;
                case 'l':
                    bufferCapacity += size * 8;
                    index += size;
                    break;
                case 's':
                    if (size == values[index].length()) {
                        bufferCapacity += size;
                        index++;
                        break;
                    } else {
                        throw new IllegalArgumentException("Illegal format string");
                    }
                case 'x':
                    bufferCapacity += size;
                    break;
                default:
                    throw new IllegalArgumentException("Illegal format string");
            }
            literals[i] = new FormatLiteral(symbol, size);
        }
        if (index != values.length) {
            throw new IllegalArgumentException("Illegal format string");
        }
        int index2 = 0;
        ByteBuffer buffer = ByteBuffer.allocate(bufferCapacity);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int length = literals.length;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= length) {
                return buffer.array();
            }
            FormatLiteral literal = literals[i3];
            switch (literal.symbol) {
                case 'B':
                    buffer.put(values[index2]);
                    index2++;
                    break;
                case 'F':
                    for (int i4 = 0; i4 < literal.size; i4++) {
                        buffer.putFloat(values[index2][i4]);
                    }
                    index2++;
                    break;
                case 'H':
                    for (int i5 = 0; i5 < literal.size; i5++) {
                        buffer.putShort(values[index2][i5]);
                    }
                    index2++;
                    break;
                case 'I':
                    for (int i6 = 0; i6 < literal.size; i6++) {
                        buffer.putInt(values[index2][i6]);
                    }
                    index2++;
                    break;
                case 'L':
                    for (int i7 = 0; i7 < literal.size; i7++) {
                        buffer.putLong(values[index2][i7]);
                    }
                    index2++;
                    break;
                case 'S':
                    try {
                        buffer.put(values[index2].getBytes("US-ASCII"));
                        buffer.put((byte) 0);
                        index2++;
                        break;
                    } catch (UnsupportedEncodingException e) {
                        throw new IllegalArgumentException();
                    }
                case 'b':
                    for (int i8 = 0; i8 < literal.size; i8++) {
                        buffer.put(values[index2].byteValue());
                        index2++;
                    }
                    break;
                case 'f':
                    for (int i9 = 0; i9 < literal.size; i9++) {
                        buffer.putFloat(values[index2].floatValue());
                        index2++;
                    }
                    break;
                case 'h':
                    for (int i10 = 0; i10 < literal.size; i10++) {
                        buffer.putShort(values[index2].shortValue());
                        index2++;
                    }
                    break;
                case 'i':
                    for (int i11 = 0; i11 < literal.size; i11++) {
                        buffer.putInt(values[index2].intValue());
                        index2++;
                    }
                    break;
                case 'l':
                    for (int i12 = 0; i12 < literal.size; i12++) {
                        buffer.putLong(values[index2].longValue());
                        index2++;
                    }
                    break;
                case 's':
                    try {
                        buffer.put(values[index2].getBytes("US-ASCII"));
                        index2++;
                        break;
                    } catch (UnsupportedEncodingException e2) {
                        throw new IllegalArgumentException();
                    }
                case 'x':
                    for (int i13 = 0; i13 < literal.size; i13++) {
                        buffer.put((byte) 0);
                    }
                    break;
            }
            i2 = i3 + 1;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00a8, code lost:
        r21 = r21 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object[] unpack(java.lang.String r26, byte[] r27) throws java.lang.IllegalArgumentException {
        /*
            java.lang.String r21 = "(?<=\\D)"
            r0 = r26
            r1 = r21
            java.lang.String[] r11 = r0.split(r1)
            java.util.ArrayList r8 = new java.util.ArrayList
            r8.<init>()
            java.nio.ByteBuffer r5 = java.nio.ByteBuffer.wrap(r27)
            java.nio.ByteOrder r21 = java.nio.ByteOrder.LITTLE_ENDIAN
            r0 = r21
            r5.order(r0)
            int r0 = r11.length
            r22 = r0
            r21 = 0
        L_0x001f:
            r0 = r21
            r1 = r22
            if (r0 >= r1) goto L_0x01c6
            r20 = r11[r21]
            r17 = 0
            r16 = 1
            int r23 = r20.length()
            int r23 = r23 + -1
            r0 = r20
            r1 = r23
            char r19 = r0.charAt(r1)
            int r23 = r20.length()
            r24 = 1
            r0 = r23
            r1 = r24
            if (r0 <= r1) goto L_0x006d
            r17 = 1
            r23 = 0
            int r24 = r20.length()
            int r24 = r24 + -1
            r0 = r20
            r1 = r23
            r2 = r24
            java.lang.String r23 = r0.substring(r1, r2)
            int r16 = java.lang.Integer.parseInt(r23)
            r23 = 1
            r0 = r16
            r1 = r23
            if (r0 >= r1) goto L_0x006d
            java.lang.IllegalArgumentException r21 = new java.lang.IllegalArgumentException
            java.lang.String r22 = "Illegal format string"
            r21.<init>(r22)
            throw r21
        L_0x006d:
            switch(r19) {
                case 36: goto L_0x01ae;
                case 66: goto L_0x0098;
                case 70: goto L_0x0146;
                case 72: goto L_0x00c1;
                case 73: goto L_0x00ef;
                case 76: goto L_0x011a;
                case 83: goto L_0x0183;
                case 98: goto L_0x0083;
                case 102: goto L_0x0131;
                case 104: goto L_0x00ac;
                case 105: goto L_0x00da;
                case 108: goto L_0x0105;
                case 115: goto L_0x015d;
                case 120: goto L_0x0078;
                default: goto L_0x0070;
            }
        L_0x0070:
            java.lang.IllegalArgumentException r21 = new java.lang.IllegalArgumentException
            java.lang.String r22 = "Illegal format string"
            r21.<init>(r22)
            throw r21
        L_0x0078:
            r12 = 0
        L_0x0079:
            r0 = r16
            if (r12 >= r0) goto L_0x00a8
            r5.get()
            int r12 = r12 + 1
            goto L_0x0079
        L_0x0083:
            r12 = 0
        L_0x0084:
            r0 = r16
            if (r12 >= r0) goto L_0x00a8
            byte r23 = r5.get()
            java.lang.Byte r23 = java.lang.Byte.valueOf(r23)
            r0 = r23
            r8.add(r0)
            int r12 = r12 + 1
            goto L_0x0084
        L_0x0098:
            r0 = r16
            byte[] r6 = new byte[r0]
            r23 = 0
            r0 = r23
            r1 = r16
            r5.get(r6, r0, r1)
            r8.add(r6)
        L_0x00a8:
            int r21 = r21 + 1
            goto L_0x001f
        L_0x00ac:
            r12 = 0
        L_0x00ad:
            r0 = r16
            if (r12 >= r0) goto L_0x00a8
            short r23 = r5.getShort()
            java.lang.Short r23 = java.lang.Short.valueOf(r23)
            r0 = r23
            r8.add(r0)
            int r12 = r12 + 1
            goto L_0x00ad
        L_0x00c1:
            r0 = r16
            short[] r15 = new short[r0]
            r12 = 0
        L_0x00c6:
            r0 = r16
            if (r12 >= r0) goto L_0x00d6
            short r23 = r5.getShort()
            r15[r12] = r23
            int r23 = r12 + 1
            r0 = r23
            short r12 = (short) r0
            goto L_0x00c6
        L_0x00d6:
            r8.add(r15)
            goto L_0x00a8
        L_0x00da:
            r12 = 0
        L_0x00db:
            r0 = r16
            if (r12 >= r0) goto L_0x00a8
            int r23 = r5.getInt()
            java.lang.Integer r23 = java.lang.Integer.valueOf(r23)
            r0 = r23
            r8.add(r0)
            int r12 = r12 + 1
            goto L_0x00db
        L_0x00ef:
            r0 = r16
            int[] r13 = new int[r0]
            r12 = 0
        L_0x00f4:
            r0 = r16
            if (r12 >= r0) goto L_0x0101
            int r23 = r5.getInt()
            r13[r12] = r23
            int r12 = r12 + 1
            goto L_0x00f4
        L_0x0101:
            r8.add(r13)
            goto L_0x00a8
        L_0x0105:
            r12 = 0
        L_0x0106:
            r0 = r16
            if (r12 >= r0) goto L_0x00a8
            long r24 = r5.getLong()
            java.lang.Long r23 = java.lang.Long.valueOf(r24)
            r0 = r23
            r8.add(r0)
            int r12 = r12 + 1
            goto L_0x0106
        L_0x011a:
            r0 = r16
            long[] r14 = new long[r0]
            r12 = 0
        L_0x011f:
            r0 = r16
            if (r12 >= r0) goto L_0x012c
            long r24 = r5.getLong()
            r14[r12] = r24
            int r12 = r12 + 1
            goto L_0x011f
        L_0x012c:
            r8.add(r14)
            goto L_0x00a8
        L_0x0131:
            r12 = 0
        L_0x0132:
            r0 = r16
            if (r12 >= r0) goto L_0x00a8
            float r23 = r5.getFloat()
            java.lang.Float r23 = java.lang.Float.valueOf(r23)
            r0 = r23
            r8.add(r0)
            int r12 = r12 + 1
            goto L_0x0132
        L_0x0146:
            r0 = r16
            float[] r10 = new float[r0]
            r12 = 0
        L_0x014b:
            r0 = r16
            if (r12 >= r0) goto L_0x0158
            float r23 = r5.getFloat()
            r10[r12] = r23
            int r12 = r12 + 1
            goto L_0x014b
        L_0x0158:
            r8.add(r10)
            goto L_0x00a8
        L_0x015d:
            r0 = r16
            byte[] r7 = new byte[r0]
            r23 = 0
            r0 = r23
            r1 = r16
            r5.get(r7, r0, r1)
            java.lang.String r23 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x017c }
            java.lang.String r24 = "US-ASCII"
            r0 = r23
            r1 = r24
            r0.<init>(r7, r1)     // Catch:{ UnsupportedEncodingException -> 0x017c }
            r0 = r23
            r8.add(r0)     // Catch:{ UnsupportedEncodingException -> 0x017c }
            goto L_0x00a8
        L_0x017c:
            r9 = move-exception
            java.lang.IllegalArgumentException r21 = new java.lang.IllegalArgumentException
            r21.<init>()
            throw r21
        L_0x0183:
            if (r17 == 0) goto L_0x018d
            java.lang.IllegalArgumentException r21 = new java.lang.IllegalArgumentException
            java.lang.String r22 = "Illegal format string"
            r21.<init>(r22)
            throw r21
        L_0x018d:
            java.lang.StringBuffer r18 = new java.lang.StringBuffer
            r18.<init>()
        L_0x0192:
            byte r4 = r5.get()
            if (r4 == 0) goto L_0x01a3
            char r0 = (char) r4
            r23 = r0
            r0 = r18
            r1 = r23
            r0.append(r1)
            goto L_0x0192
        L_0x01a3:
            java.lang.String r23 = r18.toString()
            r0 = r23
            r8.add(r0)
            goto L_0x00a8
        L_0x01ae:
            if (r17 == 0) goto L_0x01b8
            java.lang.IllegalArgumentException r21 = new java.lang.IllegalArgumentException
            java.lang.String r22 = "Illegal format string"
            r21.<init>(r22)
            throw r21
        L_0x01b8:
            boolean r21 = r5.hasRemaining()
            if (r21 == 0) goto L_0x0070
            java.lang.IllegalArgumentException r21 = new java.lang.IllegalArgumentException
            java.lang.String r22 = "Illegal format string"
            r21.<init>(r22)
            throw r21
        L_0x01c6:
            java.lang.Object[] r21 = r8.toArray()
            return r21
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.Ev3BinaryParser.unpack(java.lang.String, byte[]):java.lang.Object[]");
    }

    public static byte[] encodeLC0(byte v) {
        if (v < -31 || v > 31) {
            throw new IllegalArgumentException("Encoded value must be in range [0, 127]");
        }
        return new byte[]{(byte) (PRIMPAR_VALUE & v)};
    }

    public static byte[] encodeLC1(byte v) {
        return new byte[]{(byte) (((byte) (PRIMPAR_LONG | PRIMPAR_CONST)) | PRIMPAR_1_BYTE), (byte) (v & Ev3Constants.Opcode.TST)};
    }

    public static byte[] encodeLC2(short v) {
        return new byte[]{(byte) (((byte) (PRIMPAR_LONG | PRIMPAR_CONST)) | PRIMPAR_2_BYTES), (byte) (v & 255), (byte) ((v >>> 8) & 255)};
    }

    public static byte[] encodeLC4(int v) {
        return new byte[]{(byte) (((byte) (PRIMPAR_LONG | PRIMPAR_CONST)) | PRIMPAR_4_BYTES), (byte) (v & 255), (byte) ((v >>> 8) & 255), (byte) ((v >>> 16) & 255), (byte) ((v >>> 24) & 255)};
    }

    public static byte[] encodeLV0(int i) {
        return new byte[]{(byte) ((PRIMPAR_INDEX & i) | PRIMPAR_SHORT | PRIMPAR_VARIABEL | PRIMPAR_LOCAL)};
    }

    public static byte[] encodeLV1(int i) {
        return new byte[]{(byte) (PRIMPAR_LONG | PRIMPAR_VARIABEL | PRIMPAR_LOCAL | PRIMPAR_1_BYTE), (byte) (i & 255)};
    }

    public static byte[] encodeLV2(int i) {
        return new byte[]{(byte) (PRIMPAR_LONG | PRIMPAR_VARIABEL | PRIMPAR_LOCAL | PRIMPAR_2_BYTES), (byte) (i & 255), (byte) ((i >>> 8) & 255)};
    }

    public static byte[] encodeLV4(int i) {
        return new byte[]{(byte) (PRIMPAR_LONG | PRIMPAR_VARIABEL | PRIMPAR_LOCAL | PRIMPAR_4_BYTES), (byte) (i & 255), (byte) ((i >>> 8) & 255), (byte) ((i >>> 16) & 255), (byte) ((i >>> 24) & 255)};
    }

    public static byte[] encodeGV0(int i) {
        return new byte[]{(byte) ((PRIMPAR_INDEX & i) | PRIMPAR_SHORT | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL)};
    }

    public static byte[] encodeGV1(int i) {
        return new byte[]{(byte) (PRIMPAR_LONG | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL | PRIMPAR_1_BYTE), (byte) (i & 255)};
    }

    public static byte[] encodeGV2(int i) {
        return new byte[]{(byte) (PRIMPAR_LONG | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL | PRIMPAR_2_BYTES), (byte) (i & 255), (byte) ((i >>> 8) & 255)};
    }

    public static byte[] encodeGV4(int i) {
        return new byte[]{(byte) (PRIMPAR_LONG | PRIMPAR_VARIABEL | PRIMPAR_GLOBAL | PRIMPAR_4_BYTES), (byte) (i & 255), (byte) ((i >>> 8) & 255), (byte) ((i >>> 16) & 255), (byte) ((i >>> 24) & 255)};
    }

    public static byte[] encodeSystemCommand(byte command, boolean needReply, Object... parameters) {
        int bufferCapacity = 2;
        for (Object obj : parameters) {
            if (obj instanceof Byte) {
                bufferCapacity++;
            } else if (obj instanceof Short) {
                bufferCapacity += 2;
            } else if (obj instanceof Integer) {
                bufferCapacity += 4;
            } else if (obj instanceof String) {
                bufferCapacity += ((String) obj).length() + 1;
            } else {
                throw new IllegalArgumentException("Parameters should be one of the class types: Byte, Short, Integer, String");
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(bufferCapacity);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(needReply ? (byte) 1 : -127);
        buffer.put(command);
        for (Object obj2 : parameters) {
            if (obj2 instanceof Byte) {
                buffer.put(((Byte) obj2).byteValue());
            } else if (obj2 instanceof Short) {
                buffer.putShort(((Short) obj2).shortValue());
            } else if (obj2 instanceof Integer) {
                buffer.putInt(((Integer) obj2).intValue());
            } else if (obj2 instanceof String) {
                try {
                    buffer.put(((String) obj2).getBytes("US-ASCII"));
                    buffer.put((byte) 0);
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalArgumentException("Non-ASCII string encoding is not supported");
                }
            } else {
                throw new IllegalArgumentException("Parameters should be one of the class types: Byte, Short, Integer, String");
            }
        }
        return buffer.array();
    }

    public static byte[] encodeDirectCommand(byte opcode, boolean needReply, int globalAllocation, int localAllocation, String paramFormat, Object... parameters) {
        if (globalAllocation < 0 || globalAllocation > 1023 || localAllocation < 0 || localAllocation > 63 || paramFormat.length() != parameters.length) {
            throw new IllegalArgumentException();
        }
        ArrayList<byte[]> payloads = new ArrayList<>();
        for (int i = 0; i < paramFormat.length(); i++) {
            char letter = paramFormat.charAt(i);
            Byte b = parameters[i];
            switch (letter) {
                case 'c':
                    if (b instanceof Byte) {
                        if (b.byteValue() <= 31 && b.byteValue() >= -31) {
                            payloads.add(encodeLC0(b.byteValue()));
                            break;
                        } else {
                            payloads.add(encodeLC1(b.byteValue()));
                            break;
                        }
                    } else if (b instanceof Short) {
                        payloads.add(encodeLC2(((Short) b).shortValue()));
                        break;
                    } else if (b instanceof Integer) {
                        payloads.add(encodeLC4(((Integer) b).intValue()));
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                case 'g':
                    if (b instanceof Byte) {
                        if (b.byteValue() <= 31 && b.byteValue() >= -31) {
                            payloads.add(encodeGV0(b.byteValue()));
                            break;
                        } else {
                            payloads.add(encodeGV1(b.byteValue()));
                            break;
                        }
                    } else if (b instanceof Short) {
                        payloads.add(encodeGV2(((Short) b).shortValue()));
                        break;
                    } else if (b instanceof Integer) {
                        payloads.add(encodeGV4(((Integer) b).intValue()));
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                case 'l':
                    if (b instanceof Byte) {
                        if (b.byteValue() <= 31 && b.byteValue() >= -31) {
                            payloads.add(encodeLV0(b.byteValue()));
                            break;
                        } else {
                            payloads.add(encodeLV1(b.byteValue()));
                            break;
                        }
                    } else if (b instanceof Short) {
                        payloads.add(encodeLV2(((Short) b).shortValue()));
                        break;
                    } else if (b instanceof Integer) {
                        payloads.add(encodeLV4(((Integer) b).intValue()));
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                case 's':
                    if (!(b instanceof String)) {
                        throw new IllegalArgumentException();
                    }
                    try {
                        payloads.add((((String) b) + 0).getBytes("US-ASCII"));
                        break;
                    } catch (UnsupportedEncodingException e) {
                        throw new IllegalArgumentException();
                    }
                default:
                    throw new IllegalArgumentException("Illegal format string");
            }
        }
        int bufferCapacity = 4;
        Iterator<byte[]> it = payloads.iterator();
        while (it.hasNext()) {
            bufferCapacity += it.next().length;
        }
        ByteBuffer buffer = ByteBuffer.allocate(bufferCapacity);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(needReply ? 0 : Byte.MIN_VALUE);
        buffer.put(new byte[]{(byte) (globalAllocation & 255), (byte) (((globalAllocation >>> 8) & 3) | (localAllocation << 2))});
        buffer.put(opcode);
        Iterator<byte[]> it2 = payloads.iterator();
        while (it2.hasNext()) {
            buffer.put(it2.next());
        }
        return buffer.array();
    }
}
