package kawa.standard;

import gnu.expr.Expression;
import gnu.lists.Pair;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class fluid_let extends Syntax {
    public static final fluid_let fluid_let = new fluid_let();
    Expression defaultInit;
    boolean star;

    static {
        fluid_let.setName("fluid-set");
    }

    public fluid_let(boolean star2, Expression defaultInit2) {
        this.star = star2;
        this.defaultInit = defaultInit2;
    }

    public fluid_let() {
        this.star = false;
    }

    public Expression rewrite(Object obj, Translator tr) {
        if (!(obj instanceof Pair)) {
            return tr.syntaxError("missing let arguments");
        }
        Pair pair = (Pair) obj;
        return rewrite(pair.getCar(), pair.getCdr(), tr);
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00f7, code lost:
        r9 = r18.syntaxError("invalid " + getName() + " syntax");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0119, code lost:
        r18.popPositionOf(r11);
        r9 = r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression rewrite(java.lang.Object r16, java.lang.Object r17, kawa.lang.Translator r18) {
        /*
            r15 = this;
            boolean r13 = r15.star
            if (r13 == 0) goto L_0x006a
            r5 = 1
        L_0x0005:
            gnu.expr.Expression[] r8 = new gnu.expr.Expression[r5]
            gnu.expr.FluidLetExp r9 = new gnu.expr.FluidLetExp
            r9.<init>(r8)
            r7 = 0
        L_0x000d:
            if (r7 >= r5) goto L_0x0127
            r2 = r16
            gnu.lists.Pair r2 = (gnu.lists.Pair) r2
            r0 = r18
            java.lang.Object r11 = r0.pushPositionOf(r2)
            java.lang.Object r10 = r2.getCar()     // Catch:{ all -> 0x0120 }
            boolean r13 = r10 instanceof java.lang.String     // Catch:{ all -> 0x0120 }
            if (r13 != 0) goto L_0x0025
            boolean r13 = r10 instanceof gnu.mapping.Symbol     // Catch:{ all -> 0x0120 }
            if (r13 == 0) goto L_0x006f
        L_0x0025:
            gnu.expr.Expression r12 = r15.defaultInit     // Catch:{ all -> 0x0120 }
        L_0x0027:
            gnu.expr.Declaration r4 = r9.addDeclaration((java.lang.Object) r10)     // Catch:{ all -> 0x0120 }
            r0 = r18
            gnu.expr.NameLookup r13 = r0.lexical     // Catch:{ all -> 0x0120 }
            r14 = 0
            gnu.expr.Declaration r6 = r13.lookup((java.lang.Object) r10, (boolean) r14)     // Catch:{ all -> 0x0120 }
            if (r6 == 0) goto L_0x0045
            r0 = r18
            r6.maybeIndirectBinding(r0)     // Catch:{ all -> 0x0120 }
            r4.base = r6     // Catch:{ all -> 0x0120 }
            r13 = 1
            r6.setFluid(r13)     // Catch:{ all -> 0x0120 }
            r13 = 1
            r6.setCanWrite(r13)     // Catch:{ all -> 0x0120 }
        L_0x0045:
            r13 = 1
            r4.setCanWrite(r13)     // Catch:{ all -> 0x0120 }
            r13 = 1
            r4.setFluid(r13)     // Catch:{ all -> 0x0120 }
            r13 = 1
            r4.setIndirectBinding(r13)     // Catch:{ all -> 0x0120 }
            if (r12 != 0) goto L_0x0058
            gnu.expr.ReferenceExp r12 = new gnu.expr.ReferenceExp     // Catch:{ all -> 0x0120 }
            r12.<init>((java.lang.Object) r10)     // Catch:{ all -> 0x0120 }
        L_0x0058:
            r8[r7] = r12     // Catch:{ all -> 0x0120 }
            r13 = 0
            r4.noteValue(r13)     // Catch:{ all -> 0x0120 }
            java.lang.Object r16 = r2.getCdr()     // Catch:{ all -> 0x0120 }
            r0 = r18
            r0.popPositionOf(r11)
            int r7 = r7 + 1
            goto L_0x000d
        L_0x006a:
            int r5 = gnu.lists.LList.length(r16)
            goto L_0x0005
        L_0x006f:
            boolean r13 = r10 instanceof gnu.lists.Pair     // Catch:{ all -> 0x0120 }
            if (r13 == 0) goto L_0x00f7
            r0 = r10
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ all -> 0x0120 }
            r3 = r0
            java.lang.Object r13 = r3.getCar()     // Catch:{ all -> 0x0120 }
            boolean r13 = r13 instanceof java.lang.String     // Catch:{ all -> 0x0120 }
            if (r13 != 0) goto L_0x008f
            java.lang.Object r13 = r3.getCar()     // Catch:{ all -> 0x0120 }
            boolean r13 = r13 instanceof gnu.mapping.Symbol     // Catch:{ all -> 0x0120 }
            if (r13 != 0) goto L_0x008f
            java.lang.Object r13 = r3.getCar()     // Catch:{ all -> 0x0120 }
            boolean r13 = r13 instanceof kawa.lang.SyntaxForm     // Catch:{ all -> 0x0120 }
            if (r13 == 0) goto L_0x00f7
        L_0x008f:
            java.lang.Object r10 = r3.getCar()     // Catch:{ all -> 0x0120 }
            boolean r13 = r10 instanceof kawa.lang.SyntaxForm     // Catch:{ all -> 0x0120 }
            if (r13 == 0) goto L_0x009d
            kawa.lang.SyntaxForm r10 = (kawa.lang.SyntaxForm) r10     // Catch:{ all -> 0x0120 }
            java.lang.Object r10 = r10.getDatum()     // Catch:{ all -> 0x0120 }
        L_0x009d:
            java.lang.Object r13 = r3.getCdr()     // Catch:{ all -> 0x0120 }
            gnu.lists.LList r14 = gnu.lists.LList.Empty     // Catch:{ all -> 0x0120 }
            if (r13 != r14) goto L_0x00a8
            gnu.expr.Expression r12 = r15.defaultInit     // Catch:{ all -> 0x0120 }
            goto L_0x0027
        L_0x00a8:
            java.lang.Object r13 = r3.getCdr()     // Catch:{ all -> 0x0120 }
            boolean r13 = r13 instanceof gnu.lists.Pair     // Catch:{ all -> 0x0120 }
            if (r13 == 0) goto L_0x00be
            java.lang.Object r3 = r3.getCdr()     // Catch:{ all -> 0x0120 }
            gnu.lists.Pair r3 = (gnu.lists.Pair) r3     // Catch:{ all -> 0x0120 }
            java.lang.Object r13 = r3.getCdr()     // Catch:{ all -> 0x0120 }
            gnu.lists.LList r14 = gnu.lists.LList.Empty     // Catch:{ all -> 0x0120 }
            if (r13 == r14) goto L_0x00eb
        L_0x00be:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0120 }
            r13.<init>()     // Catch:{ all -> 0x0120 }
            java.lang.String r14 = "bad syntax for value of "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0120 }
            java.lang.StringBuilder r13 = r13.append(r10)     // Catch:{ all -> 0x0120 }
            java.lang.String r14 = " in "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0120 }
            java.lang.String r14 = r15.getName()     // Catch:{ all -> 0x0120 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0120 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0120 }
            r0 = r18
            gnu.expr.Expression r9 = r0.syntaxError(r13)     // Catch:{ all -> 0x0120 }
            r0 = r18
            r0.popPositionOf(r11)
        L_0x00ea:
            return r9
        L_0x00eb:
            java.lang.Object r13 = r3.getCar()     // Catch:{ all -> 0x0120 }
            r0 = r18
            gnu.expr.Expression r12 = r0.rewrite(r13)     // Catch:{ all -> 0x0120 }
            goto L_0x0027
        L_0x00f7:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x0120 }
            r13.<init>()     // Catch:{ all -> 0x0120 }
            java.lang.String r14 = "invalid "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0120 }
            java.lang.String r14 = r15.getName()     // Catch:{ all -> 0x0120 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0120 }
            java.lang.String r14 = " syntax"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ all -> 0x0120 }
            java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x0120 }
            r0 = r18
            gnu.expr.Expression r9 = r0.syntaxError(r13)     // Catch:{ all -> 0x0120 }
            r0 = r18
            r0.popPositionOf(r11)
            goto L_0x00ea
        L_0x0120:
            r13 = move-exception
            r0 = r18
            r0.popPositionOf(r11)
            throw r13
        L_0x0127:
            r0 = r18
            r0.push((gnu.expr.ScopeExp) r9)
            boolean r13 = r15.star
            if (r13 == 0) goto L_0x0142
            gnu.lists.LList r13 = gnu.lists.LList.Empty
            r0 = r16
            if (r0 == r13) goto L_0x0142
            gnu.expr.Expression r13 = r15.rewrite(r16, r17, r18)
            r9.body = r13
        L_0x013c:
            r0 = r18
            r0.pop(r9)
            goto L_0x00ea
        L_0x0142:
            r0 = r18
            r1 = r17
            gnu.expr.Expression r13 = r0.rewrite_body(r1)
            r9.body = r13
            goto L_0x013c
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.fluid_let.rewrite(java.lang.Object, java.lang.Object, kawa.lang.Translator):gnu.expr.Expression");
    }
}
