package gnu.kawa.functions;

import gnu.expr.Language;
import gnu.mapping.Procedure2;

public class IsEqual extends Procedure2 {
    Language language;

    public IsEqual(Language language2, String name) {
        this.language = language2;
        setName(name);
    }

    public static boolean numberEquals(Number num1, Number num2) {
        boolean exact1 = Arithmetic.isExact(num1);
        boolean exact2 = Arithmetic.isExact(num2);
        if (!exact1 || !exact2) {
            return exact1 == exact2 && num1.equals(num2);
        }
        return NumberCompare.$Eq(num1, num2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
        return apply(r14, r15);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean apply(java.lang.Object r18, java.lang.Object r19) {
        /*
            r0 = r18
            r1 = r19
            if (r0 != r1) goto L_0x0009
            r16 = 1
        L_0x0008:
            return r16
        L_0x0009:
            if (r18 == 0) goto L_0x000d
            if (r19 != 0) goto L_0x0010
        L_0x000d:
            r16 = 0
            goto L_0x0008
        L_0x0010:
            r0 = r18
            boolean r0 = r0 instanceof java.lang.Number
            r16 = r0
            if (r16 == 0) goto L_0x0029
            r0 = r19
            boolean r0 = r0 instanceof java.lang.Number
            r16 = r0
            if (r16 == 0) goto L_0x0029
            java.lang.Number r18 = (java.lang.Number) r18
            java.lang.Number r19 = (java.lang.Number) r19
            boolean r16 = numberEquals(r18, r19)
            goto L_0x0008
        L_0x0029:
            r0 = r18
            boolean r0 = r0 instanceof java.lang.CharSequence
            r16 = r0
            if (r16 == 0) goto L_0x006a
            r0 = r19
            boolean r0 = r0 instanceof java.lang.CharSequence
            r16 = r0
            if (r16 != 0) goto L_0x003c
            r16 = 0
            goto L_0x0008
        L_0x003c:
            r10 = r18
            java.lang.CharSequence r10 = (java.lang.CharSequence) r10
            r11 = r19
            java.lang.CharSequence r11 = (java.lang.CharSequence) r11
            int r5 = r10.length()
            int r6 = r11.length()
            if (r5 == r6) goto L_0x0051
            r16 = 0
            goto L_0x0008
        L_0x0051:
            r4 = r5
        L_0x0052:
            int r4 = r4 + -1
            if (r4 < 0) goto L_0x0067
            char r16 = r10.charAt(r4)
            char r17 = r11.charAt(r4)
            r0 = r16
            r1 = r17
            if (r0 == r1) goto L_0x0052
            r16 = 0
            goto L_0x0008
        L_0x0067:
            r16 = 1
            goto L_0x0008
        L_0x006a:
            r0 = r18
            boolean r0 = r0 instanceof gnu.lists.FVector
            r16 = r0
            if (r16 == 0) goto L_0x00b4
            r0 = r19
            boolean r0 = r0 instanceof gnu.lists.FVector
            r16 = r0
            if (r16 != 0) goto L_0x007d
            r16 = 0
            goto L_0x0008
        L_0x007d:
            r12 = r18
            gnu.lists.FVector r12 = (gnu.lists.FVector) r12
            r13 = r19
            gnu.lists.FVector r13 = (gnu.lists.FVector) r13
            int r7 = r12.size
            java.lang.Object[] r0 = r13.data
            r16 = r0
            if (r16 == 0) goto L_0x0095
            int r0 = r13.size
            r16 = r0
            r0 = r16
            if (r0 == r7) goto L_0x0099
        L_0x0095:
            r16 = 0
            goto L_0x0008
        L_0x0099:
            java.lang.Object[] r2 = r12.data
            java.lang.Object[] r3 = r13.data
            r4 = r7
        L_0x009e:
            int r4 = r4 + -1
            if (r4 < 0) goto L_0x00b0
            r16 = r2[r4]
            r17 = r3[r4]
            boolean r16 = apply(r16, r17)
            if (r16 != 0) goto L_0x009e
            r16 = 0
            goto L_0x0008
        L_0x00b0:
            r16 = 1
            goto L_0x0008
        L_0x00b4:
            r0 = r18
            boolean r0 = r0 instanceof gnu.lists.LList
            r16 = r0
            if (r16 == 0) goto L_0x0119
            r0 = r18
            boolean r0 = r0 instanceof gnu.lists.Pair
            r16 = r0
            if (r16 == 0) goto L_0x00cc
            r0 = r19
            boolean r0 = r0 instanceof gnu.lists.Pair
            r16 = r0
            if (r16 != 0) goto L_0x00d0
        L_0x00cc:
            r16 = 0
            goto L_0x0008
        L_0x00d0:
            r8 = r18
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8
            r9 = r19
            gnu.lists.Pair r9 = (gnu.lists.Pair) r9
        L_0x00d8:
            java.lang.Object r14 = r8.getCar()
            java.lang.Object r15 = r9.getCar()
            boolean r16 = apply(r14, r15)
            if (r16 != 0) goto L_0x00ea
            r16 = 0
            goto L_0x0008
        L_0x00ea:
            java.lang.Object r14 = r8.getCdr()
            java.lang.Object r15 = r9.getCdr()
            if (r14 != r15) goto L_0x00f8
            r16 = 1
            goto L_0x0008
        L_0x00f8:
            if (r14 == 0) goto L_0x00fc
            if (r15 != 0) goto L_0x0100
        L_0x00fc:
            r16 = 0
            goto L_0x0008
        L_0x0100:
            boolean r0 = r14 instanceof gnu.lists.Pair
            r16 = r0
            if (r16 == 0) goto L_0x010c
            boolean r0 = r15 instanceof gnu.lists.Pair
            r16 = r0
            if (r16 != 0) goto L_0x0112
        L_0x010c:
            boolean r16 = apply(r14, r15)
            goto L_0x0008
        L_0x0112:
            r8 = r14
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8
            r9 = r15
            gnu.lists.Pair r9 = (gnu.lists.Pair) r9
            goto L_0x00d8
        L_0x0119:
            boolean r16 = r18.equals(r19)
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.functions.IsEqual.apply(java.lang.Object, java.lang.Object):boolean");
    }

    public Object apply2(Object arg1, Object arg2) {
        return this.language.booleanObject(apply(arg1, arg2));
    }
}
