package kawa.standard;

import kawa.lang.Syntax;

public class module_name extends Syntax {
    public static final module_name module_name = new module_name();

    static {
        module_name.setName("module-name");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scanForm(gnu.lists.Pair r18, gnu.expr.ScopeExp r19, kawa.lang.Translator r20) {
        /*
            r17 = this;
            java.lang.Object r6 = r18.getCdr()
            r11 = 0
        L_0x0005:
            boolean r14 = r6 instanceof kawa.lang.SyntaxForm
            if (r14 == 0) goto L_0x0011
            r11 = r6
            kawa.lang.SyntaxForm r11 = (kawa.lang.SyntaxForm) r11
            java.lang.Object r6 = r11.getDatum()
            goto L_0x0005
        L_0x0011:
            boolean r14 = r6 instanceof gnu.lists.Pair
            if (r14 == 0) goto L_0x0027
            gnu.lists.Pair r6 = (gnu.lists.Pair) r6
            java.lang.Object r2 = r6.getCar()
        L_0x001b:
            boolean r14 = r2 instanceof kawa.lang.SyntaxForm
            if (r14 == 0) goto L_0x0029
            r11 = r2
            kawa.lang.SyntaxForm r11 = (kawa.lang.SyntaxForm) r11
            java.lang.Object r2 = r11.getDatum()
            goto L_0x001b
        L_0x0027:
            r2 = 0
            goto L_0x001b
        L_0x0029:
            r10 = 0
            r5 = 0
            r4 = 0
            boolean r14 = r2 instanceof gnu.lists.Pair
            if (r14 == 0) goto L_0x006f
            r13 = r2
            gnu.lists.Pair r13 = (gnu.lists.Pair) r13
            java.lang.Object r14 = r13.getCar()
            java.lang.String r15 = "quote"
            if (r14 != r15) goto L_0x006f
            java.lang.Object r2 = r13.getCdr()
            boolean r14 = r2 instanceof gnu.lists.Pair
            if (r14 == 0) goto L_0x0056
            r13 = r2
            gnu.lists.Pair r13 = (gnu.lists.Pair) r13
            java.lang.Object r14 = r13.getCdr()
            gnu.lists.LList r15 = gnu.lists.LList.Empty
            if (r14 != r15) goto L_0x0056
            java.lang.Object r14 = r13.getCar()
            boolean r14 = r14 instanceof java.lang.String
            if (r14 != 0) goto L_0x0068
        L_0x0056:
            java.lang.String r5 = "invalid quoted symbol for 'module-name'"
        L_0x0058:
            if (r5 == 0) goto L_0x00b1
            r0 = r20
            java.util.Stack r14 = r0.formStack
            r0 = r20
            gnu.expr.Expression r15 = r0.syntaxError(r5)
            r14.add(r15)
        L_0x0067:
            return
        L_0x0068:
            java.lang.Object r10 = r13.getCar()
            java.lang.String r10 = (java.lang.String) r10
            goto L_0x0058
        L_0x006f:
            boolean r14 = r2 instanceof gnu.lists.FString
            if (r14 != 0) goto L_0x0077
            boolean r14 = r2 instanceof java.lang.String
            if (r14 == 0) goto L_0x007c
        L_0x0077:
            java.lang.String r10 = r2.toString()
            goto L_0x0058
        L_0x007c:
            boolean r14 = r2 instanceof gnu.mapping.Symbol
            if (r14 == 0) goto L_0x00ae
            java.lang.String r10 = r2.toString()
            int r8 = r10.length()
            r14 = 2
            if (r8 <= r14) goto L_0x00a5
            r14 = 0
            char r14 = r10.charAt(r14)
            r15 = 60
            if (r14 != r15) goto L_0x00a5
            int r14 = r8 + -1
            char r14 = r10.charAt(r14)
            r15 = 62
            if (r14 != r15) goto L_0x00a5
            r14 = 1
            int r15 = r8 + -1
            java.lang.String r10 = r10.substring(r14, r15)
        L_0x00a5:
            r0 = r20
            r1 = r19
            gnu.expr.Declaration r4 = r0.define(r2, r11, r1)
            goto L_0x0058
        L_0x00ae:
            java.lang.String r5 = "un-implemented expression in module-name"
            goto L_0x0058
        L_0x00b1:
            r14 = 46
            int r7 = r10.lastIndexOf(r14)
            r3 = r10
            if (r7 < 0) goto L_0x010f
            r14 = 0
            int r15 = r7 + 1
            java.lang.String r14 = r10.substring(r14, r15)
            r0 = r20
            r0.classPrefix = r14
        L_0x00c5:
            gnu.expr.ModuleExp r9 = r20.getModule()
            r0 = r20
            gnu.bytecode.ClassType r14 = r0.mainClass
            if (r14 != 0) goto L_0x013e
            gnu.bytecode.ClassType r14 = new gnu.bytecode.ClassType
            r14.<init>(r3)
            r0 = r20
            r0.mainClass = r14
        L_0x00d8:
            r0 = r20
            gnu.bytecode.ClassType r14 = r0.mainClass
            r9.setType(r14)
            r9.setName(r10)
            if (r4 == 0) goto L_0x010a
            gnu.expr.QuoteExp r14 = new gnu.expr.QuoteExp
            r0 = r20
            gnu.bytecode.ClassType r15 = r0.mainClass
            gnu.bytecode.ClassType r16 = gnu.expr.Compilation.typeClass
            r14.<init>(r15, r16)
            r4.noteValue(r14)
            r14 = 16793600(0x1004000, double:8.297141E-317)
            r4.setFlag(r14)
            gnu.expr.ScopeExp r14 = r9.outer
            if (r14 != 0) goto L_0x0101
            r14 = 2048(0x800, double:1.0118E-320)
            r4.setFlag(r14)
        L_0x0101:
            r14 = 1
            r4.setPrivate(r14)
            gnu.bytecode.ClassType r14 = gnu.expr.Compilation.typeClass
            r4.setType(r14)
        L_0x010a:
            r20.mustCompileHere()
            goto L_0x0067
        L_0x010f:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r0 = r20
            java.lang.String r15 = r0.classPrefix
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.StringBuilder r14 = r14.append(r10)
            java.lang.String r10 = r14.toString()
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            r0 = r20
            java.lang.String r15 = r0.classPrefix
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = gnu.expr.Compilation.mangleName(r10)
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r3 = r14.toString()
            goto L_0x00c5
        L_0x013e:
            r0 = r20
            gnu.bytecode.ClassType r14 = r0.mainClass
            java.lang.String r12 = r14.getName()
            if (r12 != 0) goto L_0x0150
            r0 = r20
            gnu.bytecode.ClassType r14 = r0.mainClass
            r14.setName(r3)
            goto L_0x00d8
        L_0x0150:
            boolean r14 = r12.equals(r3)
            if (r14 != 0) goto L_0x00d8
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "duplicate module-name: old name: "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.StringBuilder r14 = r14.append(r12)
            java.lang.String r14 = r14.toString()
            r0 = r20
            r0.syntaxError(r14)
            goto L_0x00d8
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.module_name.scanForm(gnu.lists.Pair, gnu.expr.ScopeExp, kawa.lang.Translator):void");
    }
}
