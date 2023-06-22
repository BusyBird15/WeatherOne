package gnu.math;

public class BitOps {
    static final byte[] bit4_count = {0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4};

    private BitOps() {
    }

    public static boolean bitValue(IntNum x, int bitno) {
        int i = x.ival;
        if (x.words != null) {
            int wordno = bitno >> 5;
            if (wordno >= i) {
                if (x.words[i - 1] >= 0) {
                    return false;
                }
                return true;
            } else if (((x.words[wordno] >> bitno) & 1) == 0) {
                return false;
            } else {
                return true;
            }
        } else if (bitno >= 32) {
            if (i < 0) {
                return true;
            }
            return false;
        } else if (((i >> bitno) & 1) == 0) {
            return false;
        } else {
            return true;
        }
    }

    static int[] dataBufferFor(IntNum x, int bitno) {
        int[] data;
        int i;
        int i2 = x.ival;
        int nwords = (bitno + 1) >> 5;
        if (x.words == null) {
            if (nwords == 0) {
                nwords = 1;
            }
            data = new int[nwords];
            data[0] = i2;
            if (i2 < 0) {
                for (int j = 1; j < nwords; j++) {
                    data[j] = -1;
                }
            }
        } else {
            int nwords2 = (bitno + 1) >> 5;
            if (nwords2 > i2) {
                i = nwords2;
            } else {
                i = i2;
            }
            data = new int[i];
            int j2 = i2;
            while (true) {
                j2--;
                if (j2 < 0) {
                    break;
                }
                data[j2] = x.words[j2];
            }
            if (data[i2 - 1] < 0) {
                for (int j3 = i2; j3 < nwords2; j3++) {
                    data[j3] = -1;
                }
            }
        }
        return data;
    }

    public static IntNum setBitValue(IntNum x, int bitno, int newValue) {
        int oldValue;
        int i = 31;
        int newValue2 = newValue & 1;
        int i2 = x.ival;
        if (x.words == null) {
            if (bitno < 31) {
                i = bitno;
            }
            if (((i2 >> i) & 1) == newValue2) {
                return x;
            }
            if (bitno < 63) {
                return IntNum.make(((long) (1 << bitno)) ^ ((long) i2));
            }
        } else {
            int wordno = bitno >> 5;
            if (wordno >= i2) {
                oldValue = x.words[i2 + -1] < 0 ? 1 : 0;
            } else {
                oldValue = (x.words[wordno] >> bitno) & 1;
            }
            if (oldValue == newValue2) {
                return x;
            }
        }
        int[] data = dataBufferFor(x, bitno);
        int i3 = bitno >> 5;
        data[i3] = (1 << (bitno & 31)) ^ data[i3];
        return IntNum.make(data, data.length);
    }

    public static boolean test(IntNum x, int y) {
        boolean z = false;
        if (x.words != null) {
            if (y < 0 || (x.words[0] & y) != 0) {
                z = true;
            }
            return z;
        } else if ((x.ival & y) != 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean test(IntNum x, IntNum y) {
        if (y.words == null) {
            return test(x, y.ival);
        }
        if (x.words == null) {
            return test(y, x.ival);
        }
        if (x.ival < y.ival) {
            IntNum temp = x;
            x = y;
            y = temp;
        }
        for (int i = 0; i < y.ival; i++) {
            if ((x.words[i] & y.words[i]) != 0) {
                return true;
            }
        }
        return y.isNegative();
    }

    public static IntNum and(IntNum x, int y) {
        if (x.words == null) {
            return IntNum.make(x.ival & y);
        }
        if (y >= 0) {
            return IntNum.make(x.words[0] & y);
        }
        int len = x.ival;
        int[] words = new int[len];
        words[0] = x.words[0] & y;
        while (true) {
            len--;
            if (len <= 0) {
                return IntNum.make(words, x.ival);
            }
            words[len] = x.words[len];
        }
    }

    public static IntNum and(IntNum x, IntNum y) {
        if (y.words == null) {
            return and(x, y.ival);
        }
        if (x.words == null) {
            return and(y, x.ival);
        }
        if (x.ival < y.ival) {
            IntNum temp = x;
            x = y;
            y = temp;
        }
        int len = y.isNegative() ? x.ival : y.ival;
        int[] words = new int[len];
        int i = 0;
        while (i < y.ival) {
            words[i] = x.words[i] & y.words[i];
            i++;
        }
        while (i < len) {
            words[i] = x.words[i];
            i++;
        }
        return IntNum.make(words, len);
    }

    public static IntNum ior(IntNum x, IntNum y) {
        return bitOp(7, x, y);
    }

    public static IntNum xor(IntNum x, IntNum y) {
        return bitOp(6, x, y);
    }

    public static IntNum not(IntNum x) {
        return bitOp(12, x, IntNum.zero());
    }

    public static int swappedOp(int op) {
        return "\u0000\u0001\u0004\u0005\u0002\u0003\u0006\u0007\b\t\f\r\n\u000b\u000e\u000f".charAt(op);
    }

    public static IntNum bitOp(int op, IntNum x, IntNum y) {
        switch (op) {
            case 0:
                return IntNum.zero();
            case 1:
                return and(x, y);
            case 3:
                return x;
            case 5:
                return y;
            case 15:
                return IntNum.minusOne();
            default:
                IntNum result = new IntNum();
                setBitOp(result, op, x, y);
                return result.canonicalize();
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0063, code lost:
        if ((r2 + 1) < r9) goto L_0x0052;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0065, code lost:
        if (r8 >= 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0067, code lost:
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x007c, code lost:
        if ((r2 + 1) < r9) goto L_0x0069;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x007e, code lost:
        if (r8 < 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0080, code lost:
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0099, code lost:
        if ((r2 + 1) < r9) goto L_0x0086;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x009b, code lost:
        if (r8 >= 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x009d, code lost:
        r0 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00af, code lost:
        if ((r2 + 1) < r9) goto L_0x009f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00c4, code lost:
        if ((r2 + 1) < r9) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00c6, code lost:
        if (r8 >= 0) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c8, code lost:
        r0 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00cb, code lost:
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00de, code lost:
        if ((r2 + 1) < r9) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00e0, code lost:
        if (r8 < 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00e2, code lost:
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00f8, code lost:
        if ((r2 + 1) < r9) goto L_0x00e5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00fa, code lost:
        if (r8 < 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00fc, code lost:
        r0 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0112, code lost:
        if ((r2 + 1) < r9) goto L_0x00ff;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0114, code lost:
        if (r8 < 0) goto L_0x0119;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x0116, code lost:
        r0 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x0119, code lost:
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x012c, code lost:
        if ((r2 + 1) < r9) goto L_0x011b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0143, code lost:
        if ((r2 + 1) < r9) goto L_0x0130;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0145, code lost:
        if (r8 >= 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0147, code lost:
        r0 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0163, code lost:
        if ((r2 + 1) < r9) goto L_0x0150;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0165, code lost:
        if (r8 < 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0167, code lost:
        r0 = 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x017d, code lost:
        if ((r2 + 1) < r9) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x017f, code lost:
        if (r8 >= 0) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0181, code lost:
        r0 = 2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setBitOp(gnu.math.IntNum r12, int r13, gnu.math.IntNum r14, gnu.math.IntNum r15) {
        /*
            int[] r10 = r15.words
            if (r10 != 0) goto L_0x002d
        L_0x0004:
            int[] r10 = r15.words
            if (r10 != 0) goto L_0x003f
            int r8 = r15.ival
            r9 = 1
        L_0x000b:
            int[] r10 = r14.words
            if (r10 != 0) goto L_0x0047
            int r6 = r14.ival
            r7 = 1
        L_0x0012:
            r10 = 1
            if (r7 <= r10) goto L_0x0018
            r12.realloc(r7)
        L_0x0018:
            int[] r5 = r12.words
            r1 = 0
            r0 = 0
            switch(r13) {
                case 0: goto L_0x004f;
                case 1: goto L_0x01cf;
                case 2: goto L_0x01cc;
                case 3: goto L_0x0082;
                case 4: goto L_0x01c9;
                case 5: goto L_0x01c6;
                case 6: goto L_0x01c3;
                case 7: goto L_0x01c0;
                case 8: goto L_0x01bd;
                case 9: goto L_0x01ba;
                case 10: goto L_0x01b7;
                case 11: goto L_0x01b5;
                case 12: goto L_0x014a;
                case 13: goto L_0x01b3;
                case 14: goto L_0x01b1;
                default: goto L_0x001f;
            }
        L_0x001f:
            r3 = -1
            r2 = r1
        L_0x0021:
            int r10 = r2 + 1
            if (r10 != r7) goto L_0x0026
            r0 = 0
        L_0x0026:
            switch(r0) {
                case 0: goto L_0x0184;
                case 1: goto L_0x0193;
                case 2: goto L_0x01a1;
                default: goto L_0x0029;
            }
        L_0x0029:
            r1 = r2
        L_0x002a:
            r12.ival = r1
        L_0x002c:
            return
        L_0x002d:
            int[] r10 = r14.words
            if (r10 == 0) goto L_0x0037
            int r10 = r14.ival
            int r11 = r15.ival
            if (r10 >= r11) goto L_0x0004
        L_0x0037:
            r4 = r14
            r14 = r15
            r15 = r4
            int r13 = swappedOp(r13)
            goto L_0x0004
        L_0x003f:
            int[] r10 = r15.words
            r11 = 0
            r8 = r10[r11]
            int r9 = r15.ival
            goto L_0x000b
        L_0x0047:
            int[] r10 = r14.words
            r11 = 0
            r6 = r10[r11]
            int r7 = r14.ival
            goto L_0x0012
        L_0x004f:
            r3 = 0
            r2 = r1
            goto L_0x0021
        L_0x0052:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x005f:
            r3 = r6 & r8
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x0052
            if (r8 >= 0) goto L_0x0021
            r0 = 1
            goto L_0x0021
        L_0x0069:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x0076:
            r10 = r8 ^ -1
            r3 = r6 & r10
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x0069
            if (r8 < 0) goto L_0x0021
            r0 = 1
            goto L_0x0021
        L_0x0082:
            r3 = r6
            r0 = 1
            r2 = r1
            goto L_0x0021
        L_0x0086:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x0093:
            r10 = r6 ^ -1
            r3 = r10 & r8
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x0086
            if (r8 >= 0) goto L_0x0021
            r0 = 2
            goto L_0x0021
        L_0x009f:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x00ac:
            r3 = r8
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x009f
            goto L_0x0021
        L_0x00b3:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x00c0:
            r3 = r6 ^ r8
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x00b3
            if (r8 >= 0) goto L_0x00cb
            r0 = 2
        L_0x00c9:
            goto L_0x0021
        L_0x00cb:
            r0 = 1
            goto L_0x00c9
        L_0x00cd:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x00da:
            r3 = r6 | r8
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x00cd
            if (r8 < 0) goto L_0x0021
            r0 = 1
            goto L_0x0021
        L_0x00e5:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x00f2:
            r10 = r6 | r8
            r3 = r10 ^ -1
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x00e5
            if (r8 < 0) goto L_0x0021
            r0 = 2
            goto L_0x0021
        L_0x00ff:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x010c:
            r10 = r6 ^ r8
            r3 = r10 ^ -1
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x00ff
            if (r8 < 0) goto L_0x0119
            r0 = 2
        L_0x0117:
            goto L_0x0021
        L_0x0119:
            r0 = 1
            goto L_0x0117
        L_0x011b:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x0128:
            r3 = r8 ^ -1
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x011b
            goto L_0x0021
        L_0x0130:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x013d:
            r10 = r8 ^ -1
            r3 = r6 | r10
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x0130
            if (r8 >= 0) goto L_0x0021
            r0 = 1
            goto L_0x0021
        L_0x014a:
            r3 = r6 ^ -1
            r0 = 2
            r2 = r1
            goto L_0x0021
        L_0x0150:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x015d:
            r10 = r6 ^ -1
            r3 = r10 | r8
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x0150
            if (r8 < 0) goto L_0x0021
            r0 = 2
            goto L_0x0021
        L_0x016a:
            int r1 = r2 + 1
            r5[r2] = r3
            int[] r10 = r14.words
            r6 = r10[r1]
            int[] r10 = r15.words
            r8 = r10[r1]
            r2 = r1
        L_0x0177:
            r10 = r6 & r8
            r3 = r10 ^ -1
            int r10 = r2 + 1
            if (r10 < r9) goto L_0x016a
            if (r8 >= 0) goto L_0x0021
            r0 = 2
            goto L_0x0021
        L_0x0184:
            if (r2 != 0) goto L_0x018d
            if (r5 != 0) goto L_0x018d
            r12.ival = r3
            r1 = r2
            goto L_0x002c
        L_0x018d:
            int r1 = r2 + 1
            r5[r2] = r3
            goto L_0x002a
        L_0x0193:
            r5[r2] = r3
            r1 = r2
        L_0x0196:
            int r1 = r1 + 1
            if (r1 >= r7) goto L_0x002a
            int[] r10 = r14.words
            r10 = r10[r1]
            r5[r1] = r10
            goto L_0x0196
        L_0x01a1:
            r5[r2] = r3
            r1 = r2
        L_0x01a4:
            int r1 = r1 + 1
            if (r1 >= r7) goto L_0x002a
            int[] r10 = r14.words
            r10 = r10[r1]
            r10 = r10 ^ -1
            r5[r1] = r10
            goto L_0x01a4
        L_0x01b1:
            r2 = r1
            goto L_0x0177
        L_0x01b3:
            r2 = r1
            goto L_0x015d
        L_0x01b5:
            r2 = r1
            goto L_0x013d
        L_0x01b7:
            r2 = r1
            goto L_0x0128
        L_0x01ba:
            r2 = r1
            goto L_0x010c
        L_0x01bd:
            r2 = r1
            goto L_0x00f2
        L_0x01c0:
            r2 = r1
            goto L_0x00da
        L_0x01c3:
            r2 = r1
            goto L_0x00c0
        L_0x01c6:
            r2 = r1
            goto L_0x00ac
        L_0x01c9:
            r2 = r1
            goto L_0x0093
        L_0x01cc:
            r2 = r1
            goto L_0x0076
        L_0x01cf:
            r2 = r1
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.math.BitOps.setBitOp(gnu.math.IntNum, int, gnu.math.IntNum, gnu.math.IntNum):void");
    }

    public static IntNum extract(IntNum x, int startBit, int endBit) {
        int x_len;
        long l;
        int i;
        if (endBit < 32) {
            return IntNum.make((((-1 << endBit) ^ -1) & (x.words == null ? x.ival : x.words[0])) >> startBit);
        }
        if (x.words != null) {
            x_len = x.ival;
        } else if (x.ival >= 0) {
            return IntNum.make(startBit >= 31 ? 0 : x.ival >> startBit);
        } else {
            x_len = 1;
        }
        boolean neg = x.isNegative();
        if (endBit > x_len * 32) {
            endBit = x_len * 32;
            if (!neg && startBit == 0) {
                return x;
            }
        } else {
            x_len = (endBit + 31) >> 5;
        }
        int length = endBit - startBit;
        if (length < 64) {
            if (x.words == null) {
                int i2 = x.ival;
                if (startBit >= 32) {
                    i = 31;
                } else {
                    i = startBit;
                }
                l = (long) (i2 >> i);
            } else {
                l = MPN.rshift_long(x.words, x_len, startBit);
            }
            return IntNum.make(((-1 << length) ^ -1) & l);
        }
        int startWord = startBit >> 5;
        int[] buf = new int[(((endBit >> 5) + 1) - startWord)];
        if (x.words == null) {
            buf[0] = startBit >= 32 ? -1 : x.ival >> startBit;
        } else {
            MPN.rshift0(buf, x.words, startWord, x_len - startWord, startBit & 31);
        }
        int x_len2 = length >> 5;
        buf[x_len2] = buf[x_len2] & ((-1 << length) ^ -1);
        return IntNum.make(buf, x_len2 + 1);
    }

    public static int lowestBitSet(int i) {
        if (i == 0) {
            return -1;
        }
        int index = 0;
        while ((i & 255) == 0) {
            i >>>= 8;
            index += 8;
        }
        while ((i & 3) == 0) {
            i >>>= 2;
            index += 2;
        }
        if ((i & 1) == 0) {
            return index + 1;
        }
        return index;
    }

    public static int lowestBitSet(IntNum x) {
        int[] x_words = x.words;
        if (x_words == null) {
            return lowestBitSet(x.ival);
        }
        int x_len = x.ival;
        while (0 < x_len) {
            int b = lowestBitSet(x_words[0]);
            if (b >= 0) {
                return b + 0;
            }
        }
        return -1;
    }

    public static int bitCount(int i) {
        int count = 0;
        while (i != 0) {
            count += bit4_count[i & 15];
            i >>>= 4;
        }
        return count;
    }

    public static int bitCount(int[] x, int len) {
        int count = 0;
        while (true) {
            len--;
            if (len < 0) {
                return count;
            }
            count += bitCount(x[len]);
        }
    }

    public static int bitCount(IntNum x) {
        int x_len;
        int i;
        int[] x_words = x.words;
        if (x_words == null) {
            x_len = 1;
            i = bitCount(x.ival);
        } else {
            x_len = x.ival;
            i = bitCount(x_words, x_len);
        }
        return x.isNegative() ? (x_len * 32) - i : i;
    }

    public static IntNum reverseBits(IntNum x, int start, int end) {
        int wi;
        int ival = x.ival;
        if (x.words != null || end >= 63) {
            int[] data = dataBufferFor(x, end - 1);
            int i = start;
            for (int j = end - 1; i < j; j--) {
                int ii = i >> 5;
                int jj = j >> 5;
                int wi2 = data[ii];
                int biti = (wi2 >> i) & 1;
                if (ii == jj) {
                    wi = (biti << j) | ((int) (((long) wi2) & (((1 << i) | (1 << j)) ^ -1))) | (((wi2 >> j) & 1) << i);
                } else {
                    int wj = data[jj];
                    wi = (wi2 & ((1 << (i & 31)) ^ -1)) | (((wj >> (j & 31)) & 1) << (i & 31));
                    data[jj] = (wj & ((1 << (j & 31)) ^ -1)) | (biti << (j & 31));
                }
                data[ii] = wi;
                i++;
            }
            return IntNum.make(data, data.length);
        }
        long w = (long) ival;
        int i2 = start;
        for (int j2 = end - 1; i2 < j2; j2--) {
            w = (((w >> i2) & 1) << j2) | (w & (((1 << i2) | (1 << j2)) ^ -1)) | (((w >> j2) & 1) << i2);
            i2++;
        }
        return IntNum.make(w);
    }
}
