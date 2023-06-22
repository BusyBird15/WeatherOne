package gnu.kawa.xml;

public class Base64Binary extends BinaryObject {
    public static final String ENCODING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public Base64Binary(byte[] data) {
        this.data = data;
    }

    public static Base64Binary valueOf(String str) {
        return new Base64Binary(str);
    }

    public Base64Binary(String str) {
        int blen;
        int v;
        int len = str.length();
        int blen2 = 0;
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            if (!Character.isWhitespace(ch) && ch != '=') {
                blen2++;
            }
        }
        byte[] bytes = new byte[((blen2 * 3) / 4)];
        int value = 0;
        int buffered = 0;
        int padding = 0;
        int i2 = 0;
        int blen3 = 0;
        while (i2 < len) {
            char ch2 = str.charAt(i2);
            if (ch2 >= 'A' && ch2 <= 'Z') {
                v = ch2 - 'A';
            } else if (ch2 >= 'a' && ch2 <= 'z') {
                v = (ch2 - 'a') + 26;
            } else if (ch2 >= '0' && ch2 <= '9') {
                v = (ch2 - '0') + 52;
            } else if (ch2 == '+') {
                v = 62;
            } else if (ch2 == '/') {
                v = 63;
            } else {
                if (Character.isWhitespace(ch2)) {
                    blen = blen3;
                } else if (ch2 == '=') {
                    padding++;
                    blen = blen3;
                } else {
                    v = -1;
                }
                i2++;
                blen3 = blen;
            }
            if (v < 0 || padding > 0) {
                throw new IllegalArgumentException("illegal character in base64Binary string at position " + i2);
            }
            value = (value << 6) + v;
            buffered++;
            if (buffered == 4) {
                int blen4 = blen3 + 1;
                bytes[blen3] = (byte) (value >> 16);
                int blen5 = blen4 + 1;
                bytes[blen4] = (byte) (value >> 8);
                blen = blen5 + 1;
                bytes[blen5] = (byte) value;
                buffered = 0;
            } else {
                blen = blen3;
            }
            i2++;
            blen3 = blen;
        }
        if (buffered + padding <= 0 ? blen3 != bytes.length : !(buffered + padding == 4 && (((1 << padding) - 1) & value) == 0 && (blen3 + 3) - padding == bytes.length)) {
            throw new IllegalArgumentException();
        }
        switch (padding) {
            case 1:
                int blen6 = blen3 + 1;
                bytes[blen3] = (byte) (value << 10);
                bytes[blen6] = (byte) (value >> 2);
                int i3 = blen6 + 1;
                break;
            case 2:
                int blen7 = blen3 + 1;
                bytes[blen3] = (byte) (value >> 4);
                break;
            default:
                int i4 = blen3;
                break;
        }
        this.data = bytes;
    }

    public StringBuffer toString(StringBuffer sbuf) {
        byte[] bb = this.data;
        int len = bb.length;
        int value = 0;
        int i = 0;
        while (i < len) {
            value = (value << 8) | (bb[i] & 255);
            i++;
            if (i % 3 == 0) {
                sbuf.append(ENCODING.charAt((value >> 18) & 63));
                sbuf.append(ENCODING.charAt((value >> 12) & 63));
                sbuf.append(ENCODING.charAt((value >> 6) & 63));
                sbuf.append(ENCODING.charAt(value & 63));
            }
        }
        switch (len % 3) {
            case 1:
                sbuf.append(ENCODING.charAt((value >> 2) & 63));
                sbuf.append(ENCODING.charAt((value << 4) & 63));
                sbuf.append("==");
                break;
            case 2:
                sbuf.append(ENCODING.charAt((value >> 10) & 63));
                sbuf.append(ENCODING.charAt((value >> 4) & 63));
                sbuf.append(ENCODING.charAt((value << 2) & 63));
                sbuf.append('=');
                break;
        }
        return sbuf;
    }

    public String toString() {
        return toString(new StringBuffer()).toString();
    }
}
