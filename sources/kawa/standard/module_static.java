package kawa.standard;

import gnu.expr.Expression;
import gnu.lists.Pair;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class module_static extends Syntax {
    public static final module_static module_static = new module_static();

    static {
        module_static.setName("module-static");
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0063  */
    /* JADX WARNING: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanForDefinitions(gnu.lists.Pair r12, java.util.Vector r13, gnu.expr.ScopeExp r14, kawa.lang.Translator r15) {
        /*
            r11 = this;
            r4 = 1
            r5 = 0
            r10 = 65536(0x10000, float:9.18355E-41)
            r9 = 32768(0x8000, float:4.5918E-41)
            r8 = 101(0x65, float:1.42E-43)
            java.lang.Object r1 = r12.getCdr()
            boolean r6 = r14 instanceof gnu.expr.ModuleExp
            if (r6 != 0) goto L_0x0032
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "'"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = r11.getName()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "' not at module level"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r15.error(r8, r5)
        L_0x0031:
            return r4
        L_0x0032:
            r2 = r14
            gnu.expr.ModuleExp r2 = (gnu.expr.ModuleExp) r2
            boolean r6 = r1 instanceof gnu.lists.Pair
            if (r6 == 0) goto L_0x006d
            r12 = r1
            gnu.lists.Pair r12 = (gnu.lists.Pair) r12
            java.lang.Object r6 = r12.getCdr()
            gnu.lists.LList r7 = gnu.lists.LList.Empty
            if (r6 != r7) goto L_0x006d
            java.lang.Object r6 = r12.getCar()
            boolean r6 = r6 instanceof java.lang.Boolean
            if (r6 == 0) goto L_0x006d
            java.lang.Object r5 = r12.getCar()
            java.lang.Boolean r6 = java.lang.Boolean.FALSE
            if (r5 != r6) goto L_0x0069
            r2.setFlag(r10)
        L_0x0057:
            boolean r5 = r2.getFlag(r10)
            if (r5 == 0) goto L_0x0031
            boolean r5 = r2.getFlag(r9)
            if (r5 == 0) goto L_0x0031
            java.lang.String r5 = "inconsistent module-static specifiers"
            r15.error(r8, r5)
            goto L_0x0031
        L_0x0069:
            r2.setFlag(r9)
            goto L_0x0057
        L_0x006d:
            boolean r6 = r1 instanceof gnu.lists.Pair
            if (r6 == 0) goto L_0x00e0
            r12 = r1
            gnu.lists.Pair r12 = (gnu.lists.Pair) r12
            java.lang.Object r6 = r12.getCdr()
            gnu.lists.LList r7 = gnu.lists.LList.Empty
            if (r6 != r7) goto L_0x00e0
            java.lang.Object r6 = r12.getCar()
            boolean r6 = r6 instanceof gnu.lists.Pair
            if (r6 == 0) goto L_0x00e0
            java.lang.Object r12 = r12.getCar()
            gnu.lists.Pair r12 = (gnu.lists.Pair) r12
            java.lang.Object r6 = r12.getCar()
            java.lang.String r7 = "quote"
            boolean r6 = r15.matches(r6, r7)
            if (r6 == 0) goto L_0x00e0
            java.lang.Object r12 = r12.getCdr()
            gnu.lists.Pair r12 = (gnu.lists.Pair) r12
            gnu.lists.LList r6 = gnu.lists.LList.Empty
            if (r12 == r6) goto L_0x00bd
            java.lang.Object r6 = r12.getCar()
            boolean r6 = r6 instanceof gnu.mapping.SimpleSymbol
            if (r6 == 0) goto L_0x00bd
            java.lang.Object r6 = r12.getCar()
            java.lang.String r6 = r6.toString()
            java.lang.String r7 = "init-run"
            if (r6 != r7) goto L_0x00bd
            r2.setFlag(r9)
            r5 = 262144(0x40000, float:3.67342E-40)
            r2.setFlag(r5)
            goto L_0x0057
        L_0x00bd:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "invalid quoted symbol for '"
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r6 = r11.getName()
            java.lang.StringBuilder r4 = r4.append(r6)
            r6 = 39
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r4 = r4.toString()
            r15.error(r8, r4)
            r4 = r5
            goto L_0x0031
        L_0x00e0:
            r2.setFlag(r10)
        L_0x00e3:
            gnu.lists.LList r6 = gnu.lists.LList.Empty
            if (r1 == r6) goto L_0x0057
            boolean r6 = r1 instanceof gnu.lists.Pair
            if (r6 == 0) goto L_0x00f6
            r12 = r1
            gnu.lists.Pair r12 = (gnu.lists.Pair) r12
            java.lang.Object r6 = r12.getCar()
            boolean r6 = r6 instanceof gnu.mapping.Symbol
            if (r6 != 0) goto L_0x0119
        L_0x00f6:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "invalid syntax in '"
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r6 = r11.getName()
            java.lang.StringBuilder r4 = r4.append(r6)
            r6 = 39
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r4 = r4.toString()
            r15.error(r8, r4)
            r4 = r5
            goto L_0x0031
        L_0x0119:
            java.lang.Object r3 = r12.getCar()
            gnu.mapping.Symbol r3 = (gnu.mapping.Symbol) r3
            gnu.expr.Declaration r0 = r14.getNoDefine(r3)
            r6 = 512(0x200, double:2.53E-321)
            boolean r6 = r0.getFlag(r6)
            if (r6 == 0) goto L_0x012e
            kawa.lang.Translator.setLine((gnu.expr.Declaration) r0, (java.lang.Object) r12)
        L_0x012e:
            r6 = 2048(0x800, double:1.0118E-320)
            r0.setFlag(r6)
            java.lang.Object r1 = r12.getCdr()
            goto L_0x00e3
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.module_static.scanForDefinitions(gnu.lists.Pair, java.util.Vector, gnu.expr.ScopeExp, kawa.lang.Translator):boolean");
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        return null;
    }
}
