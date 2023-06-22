package kawa.lang;

import gnu.bytecode.ClassType;
import gnu.bytecode.Method;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.QuoteExp;
import gnu.kawa.lispexpr.LispLanguage;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Namespace;
import gnu.mapping.Symbol;
import java.util.IdentityHashMap;

public class Quote extends Syntax {
    private static final Object CYCLE = new String("(cycle)");
    protected static final int QUOTE_DEPTH = -1;
    private static final Object WORKING = new String("(working)");
    static final Method appendMethod = quoteType.getDeclaredMethod("append$V", 1);
    static final Method consXMethod = quoteType.getDeclaredMethod("consX$V", 1);
    static final Method makePairMethod = Compilation.typePair.getDeclaredMethod("make", 2);
    static final Method makeVectorMethod = ClassType.make("gnu.lists.FVector").getDeclaredMethod("make", 1);
    public static final Quote plainQuote = new Quote(LispLanguage.quote_sym, false);
    public static final Quote quasiQuote = new Quote(LispLanguage.quasiquote_sym, true);
    static final ClassType quoteType = ClassType.make("kawa.lang.Quote");
    static final Method vectorAppendMethod = ClassType.make("kawa.standard.vector_append").getDeclaredMethod("apply$V", 1);
    protected boolean isQuasi;

    public Quote(String name, boolean isQuasi2) {
        super(name);
        this.isQuasi = isQuasi2;
    }

    /* access modifiers changed from: protected */
    public Object expand(Object template, int depth, Translator tr) {
        return expand(template, depth, (SyntaxForm) null, new IdentityHashMap(), tr);
    }

    public static Object quote(Object obj, Translator tr) {
        return plainQuote.expand(obj, -1, tr);
    }

    public static Object quote(Object obj) {
        return plainQuote.expand(obj, -1, (Translator) Compilation.getCurrent());
    }

    /* access modifiers changed from: protected */
    public Expression coerceExpression(Object val, Translator tr) {
        return val instanceof Expression ? (Expression) val : leaf(val, tr);
    }

    /* access modifiers changed from: protected */
    public Expression leaf(Object val, Translator tr) {
        return new QuoteExp(val);
    }

    /* access modifiers changed from: protected */
    public boolean expandColonForms() {
        return true;
    }

    public static Symbol makeSymbol(Namespace ns, Object local) {
        String name;
        if (local instanceof CharSequence) {
            name = ((CharSequence) local).toString();
        } else {
            name = (String) local;
        }
        return ns.getSymbol(name.intern());
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:100:0x034b, code lost:
        r19 = r18 + 1;
        r27[r18] = r22;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0353, code lost:
        if (r22.getCdr() != r4) goto L_0x0370;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0357, code lost:
        if ((r3 instanceof gnu.expr.Expression) == false) goto L_0x0379;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0359, code lost:
        r31 = gnu.lists.LList.Empty;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x035b, code lost:
        r18 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x035d, code lost:
        r18 = r18 - 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x035f, code lost:
        if (r18 < 0) goto L_0x037c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x0361, code lost:
        r22 = r27[r18];
        r31 = kawa.lang.Translator.makePair(r22, r22.getCar(), r31);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x0370, code lost:
        r22 = (gnu.lists.Pair) r22.getCdr();
        r18 = r19;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0379, code lost:
        r31 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x037e, code lost:
        if ((r3 instanceof gnu.expr.Expression) == false) goto L_0x03bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x0380, code lost:
        r11 = new gnu.expr.Expression[2];
        r11[1] = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x038b, code lost:
        if (r18 != 1) goto L_0x03a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x038d, code lost:
        r11[0] = leaf(r39.getCar(), r43);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:0x03a5, code lost:
        r11[0] = leaf(r31, r43);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:?, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:?, code lost:
        return new gnu.expr.ApplyExp(makePairMethod, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:?, code lost:
        return new gnu.expr.ApplyExp(appendMethod, r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:?, code lost:
        return r31;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0075, code lost:
        if (r39 != r4) goto L_0x0327;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x0327, code lost:
        r22 = r39;
        r27 = new gnu.lists.Pair[20];
        r18 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0336, code lost:
        if (r18 < r27.length) goto L_0x034b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x0338, code lost:
        r36 = new gnu.lists.Pair[(r18 * 2)];
        java.lang.System.arraycopy(r27, 0, r36, 0, r18);
        r27 = r36;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object expand_pair(gnu.lists.Pair r39, int r40, kawa.lang.SyntaxForm r41, java.lang.Object r42, kawa.lang.Translator r43) {
        /*
            r38 = this;
            r25 = r39
        L_0x0002:
            r30 = r25
            boolean r3 = r38.expandColonForms()
            if (r3 == 0) goto L_0x0129
            r0 = r25
            r1 = r39
            if (r0 != r1) goto L_0x0129
            java.lang.Object r3 = r25.getCar()
            gnu.mapping.Symbol r5 = gnu.kawa.lispexpr.LispLanguage.lookup_sym
            r0 = r43
            r1 = r41
            boolean r3 = r0.matches((java.lang.Object) r3, (kawa.lang.SyntaxForm) r1, (gnu.mapping.Symbol) r5)
            if (r3 == 0) goto L_0x0129
            java.lang.Object r3 = r25.getCdr()
            boolean r3 = r3 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x0129
            java.lang.Object r23 = r25.getCdr()
            gnu.lists.Pair r23 = (gnu.lists.Pair) r23
            r0 = r23
            boolean r3 = r0 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x0129
            java.lang.Object r24 = r23.getCdr()
            gnu.lists.Pair r24 = (gnu.lists.Pair) r24
            r0 = r24
            boolean r3 = r0 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x0129
            java.lang.Object r3 = r24.getCdr()
            gnu.lists.LList r5 = gnu.lists.LList.Empty
            if (r3 != r5) goto L_0x0129
            r3 = 0
            r0 = r43
            r1 = r23
            gnu.expr.Expression r28 = r0.rewrite_car((gnu.lists.Pair) r1, (boolean) r3)
            r3 = 0
            r0 = r43
            r1 = r24
            gnu.expr.Expression r29 = r0.rewrite_car((gnu.lists.Pair) r1, (boolean) r3)
            r0 = r43
            r1 = r28
            gnu.mapping.Namespace r20 = r0.namespaceResolvePrefix(r1)
            r0 = r43
            r1 = r20
            r2 = r29
            gnu.mapping.Symbol r35 = r0.namespaceResolve((gnu.mapping.Namespace) r1, (gnu.expr.Expression) r2)
            if (r35 == 0) goto L_0x0078
            r13 = r35
            r4 = r30
            r3 = r13
        L_0x0073:
            r0 = r39
            if (r0 != r4) goto L_0x0327
        L_0x0077:
            return r3
        L_0x0078:
            if (r20 == 0) goto L_0x009e
            r3 = 1
            r0 = r40
            if (r0 != r3) goto L_0x009e
            gnu.expr.ApplyExp r13 = new gnu.expr.ApplyExp
            gnu.bytecode.ClassType r3 = quoteType
            java.lang.String r5 = "makeSymbol"
            r6 = 2
            gnu.bytecode.Method r3 = r3.getDeclaredMethod((java.lang.String) r5, (int) r6)
            r5 = 2
            gnu.expr.Expression[] r5 = new gnu.expr.Expression[r5]
            r6 = 0
            gnu.expr.QuoteExp r7 = gnu.expr.QuoteExp.getInstance(r20)
            r5[r6] = r7
            r6 = 1
            r5[r6] = r29
            r13.<init>((gnu.bytecode.Method) r3, (gnu.expr.Expression[]) r5)
            r4 = r30
            r3 = r13
            goto L_0x0073
        L_0x009e:
            r0 = r28
            boolean r3 = r0 instanceof gnu.expr.ReferenceExp
            if (r3 == 0) goto L_0x00dd
            r0 = r29
            boolean r3 = r0 instanceof gnu.expr.QuoteExp
            if (r3 == 0) goto L_0x00dd
            gnu.mapping.Environment r3 = r43.getGlobalEnvironment()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            gnu.expr.ReferenceExp r28 = (gnu.expr.ReferenceExp) r28
            java.lang.String r6 = r28.getName()
            java.lang.StringBuilder r5 = r5.append(r6)
            r6 = 58
            java.lang.StringBuilder r5 = r5.append(r6)
            gnu.expr.QuoteExp r29 = (gnu.expr.QuoteExp) r29
            java.lang.Object r6 = r29.getValue()
            java.lang.String r6 = r6.toString()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            gnu.mapping.Symbol r13 = r3.getSymbol(r5)
            r4 = r30
            r3 = r13
            goto L_0x0073
        L_0x00dd:
            java.lang.String r14 = gnu.kawa.functions.CompileNamedPart.combineName(r28, r29)
            if (r14 == 0) goto L_0x00ef
            gnu.mapping.Environment r3 = r43.getGlobalEnvironment()
            gnu.mapping.Symbol r13 = r3.getSymbol(r14)
            r4 = r30
            r3 = r13
            goto L_0x0073
        L_0x00ef:
            r0 = r43
            r1 = r25
            java.lang.Object r32 = r0.pushPositionOf(r1)
            r3 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "'"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.Object r6 = r23.getCar()
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r6 = "' is not a valid prefix"
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.String r5 = r5.toString()
            r0 = r43
            r0.error(r3, r5)
            r0 = r43
            r1 = r32
            r0.popPositionOf(r1)
            r13 = r35
            r4 = r30
            r3 = r13
            goto L_0x0073
        L_0x0129:
            if (r40 >= 0) goto L_0x014b
        L_0x012b:
            r3 = 1
            r0 = r40
            if (r0 != r3) goto L_0x02a6
            java.lang.Object r3 = r25.getCar()
            boolean r3 = r3 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x02a6
            java.lang.Object r15 = r25.getCar()
            r34 = r41
        L_0x013e:
            boolean r3 = r15 instanceof kawa.lang.SyntaxForm
            if (r3 == 0) goto L_0x01f1
            r34 = r15
            kawa.lang.SyntaxForm r34 = (kawa.lang.SyntaxForm) r34
            java.lang.Object r15 = r34.getDatum()
            goto L_0x013e
        L_0x014b:
            java.lang.Object r3 = r25.getCar()
            java.lang.String r5 = "quasiquote"
            r0 = r43
            r1 = r41
            boolean r3 = r0.matches((java.lang.Object) r3, (kawa.lang.SyntaxForm) r1, (java.lang.String) r5)
            if (r3 == 0) goto L_0x015e
            int r40 = r40 + 1
            goto L_0x012b
        L_0x015e:
            java.lang.Object r3 = r25.getCar()
            java.lang.String r5 = "unquote"
            r0 = r43
            r1 = r41
            boolean r3 = r0.matches((java.lang.Object) r3, (kawa.lang.SyntaxForm) r1, (java.lang.String) r5)
            if (r3 == 0) goto L_0x01bc
            int r40 = r40 + -1
            java.lang.Object r3 = r25.getCdr()
            boolean r3 = r3 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x0186
            java.lang.Object r26 = r25.getCdr()
            gnu.lists.Pair r26 = (gnu.lists.Pair) r26
            java.lang.Object r3 = r26.getCdr()
            gnu.lists.LList r5 = gnu.lists.LList.Empty
            if (r3 == r5) goto L_0x01ab
        L_0x0186:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "invalid used of "
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.Object r5 = r25.getCar()
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r5 = " in quasiquote template"
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r3 = r3.toString()
            r0 = r43
            gnu.expr.Expression r3 = r0.syntaxError(r3)
            goto L_0x0077
        L_0x01ab:
            if (r40 != 0) goto L_0x012b
            r0 = r43
            r1 = r26
            r2 = r41
            gnu.expr.Expression r13 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            r4 = r30
            r3 = r13
            goto L_0x0073
        L_0x01bc:
            java.lang.Object r3 = r25.getCar()
            java.lang.String r5 = "unquote-splicing"
            r0 = r43
            r1 = r41
            boolean r3 = r0.matches((java.lang.Object) r3, (kawa.lang.SyntaxForm) r1, (java.lang.String) r5)
            if (r3 == 0) goto L_0x012b
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "invalid used of "
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.Object r5 = r25.getCar()
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r5 = " in quasiquote template"
            java.lang.StringBuilder r3 = r3.append(r5)
            java.lang.String r3 = r3.toString()
            r0 = r43
            gnu.expr.Expression r3 = r0.syntaxError(r3)
            goto L_0x0077
        L_0x01f1:
            r33 = -1
            boolean r3 = r15 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x020e
            r3 = r15
            gnu.lists.Pair r3 = (gnu.lists.Pair) r3
            java.lang.Object r21 = r3.getCar()
            java.lang.String r3 = "unquote"
            r0 = r43
            r1 = r21
            r2 = r34
            boolean r3 = r0.matches((java.lang.Object) r1, (kawa.lang.SyntaxForm) r2, (java.lang.String) r3)
            if (r3 == 0) goto L_0x026d
            r33 = 0
        L_0x020e:
            if (r33 < 0) goto L_0x02a6
            gnu.lists.Pair r15 = (gnu.lists.Pair) r15
            java.lang.Object r15 = r15.getCdr()
            java.util.Vector r37 = new java.util.Vector
            r37.<init>()
            r13 = 0
        L_0x021c:
            boolean r3 = r15 instanceof kawa.lang.SyntaxForm
            if (r3 == 0) goto L_0x0228
            r34 = r15
            kawa.lang.SyntaxForm r34 = (kawa.lang.SyntaxForm) r34
            java.lang.Object r15 = r34.getDatum()
        L_0x0228:
            gnu.lists.LList r3 = gnu.lists.LList.Empty
            if (r15 != r3) goto L_0x027e
            int r3 = r37.size()
            int r17 = r3 + 1
            java.lang.Object r4 = r25.getCdr()
            r5 = 1
            r3 = r38
            r6 = r41
            r7 = r42
            r8 = r43
            java.lang.Object r13 = r3.expand(r4, r5, r6, r7, r8)
            r3 = 1
            r0 = r17
            if (r0 <= r3) goto L_0x0268
            r0 = r17
            gnu.expr.Expression[] r11 = new gnu.expr.Expression[r0]
            r0 = r37
            r0.copyInto(r11)
            int r3 = r17 + -1
            r0 = r38
            r1 = r43
            gnu.expr.Expression r5 = r0.coerceExpression(r13, r1)
            r11[r3] = r5
            if (r33 != 0) goto L_0x02a3
            gnu.bytecode.Method r16 = consXMethod
        L_0x0261:
            gnu.expr.ApplyExp r13 = new gnu.expr.ApplyExp
            r0 = r16
            r13.<init>((gnu.bytecode.Method) r0, (gnu.expr.Expression[]) r11)
        L_0x0268:
            r4 = r25
            r3 = r13
            goto L_0x0073
        L_0x026d:
            java.lang.String r3 = "unquote-splicing"
            r0 = r43
            r1 = r21
            r2 = r34
            boolean r3 = r0.matches((java.lang.Object) r1, (kawa.lang.SyntaxForm) r2, (java.lang.String) r3)
            if (r3 == 0) goto L_0x020e
            r33 = 1
            goto L_0x020e
        L_0x027e:
            boolean r3 = r15 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x0299
            r3 = r15
            gnu.lists.Pair r3 = (gnu.lists.Pair) r3
            r0 = r43
            r1 = r34
            gnu.expr.Expression r3 = r0.rewrite_car((gnu.lists.Pair) r3, (kawa.lang.SyntaxForm) r1)
            r0 = r37
            r0.addElement(r3)
            gnu.lists.Pair r15 = (gnu.lists.Pair) r15
            java.lang.Object r15 = r15.getCdr()
            goto L_0x021c
        L_0x0299:
            java.lang.String r3 = "improper list argument to unquote"
            r0 = r43
            gnu.expr.Expression r3 = r0.syntaxError(r3)
            goto L_0x0077
        L_0x02a3:
            gnu.bytecode.Method r16 = appendMethod
            goto L_0x0261
        L_0x02a6:
            java.lang.Object r4 = r25.getCar()
            r3 = r38
            r5 = r40
            r6 = r41
            r7 = r42
            r8 = r43
            java.lang.Object r12 = r3.expand(r4, r5, r6, r7, r8)
            java.lang.Object r3 = r25.getCar()
            if (r12 != r3) goto L_0x02dd
            java.lang.Object r4 = r25.getCdr()
            boolean r3 = r4 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x02cc
            r25 = r4
            gnu.lists.Pair r25 = (gnu.lists.Pair) r25
            goto L_0x0002
        L_0x02cc:
            r3 = r38
            r5 = r40
            r6 = r41
            r7 = r42
            r8 = r43
            java.lang.Object r13 = r3.expand(r4, r5, r6, r7, r8)
            r3 = r13
            goto L_0x0073
        L_0x02dd:
            java.lang.Object r6 = r25.getCdr()
            r5 = r38
            r7 = r40
            r8 = r41
            r9 = r42
            r10 = r43
            java.lang.Object r13 = r5.expand(r6, r7, r8, r9, r10)
            boolean r3 = r12 instanceof gnu.expr.Expression
            if (r3 != 0) goto L_0x02f7
            boolean r3 = r13 instanceof gnu.expr.Expression
            if (r3 == 0) goto L_0x031c
        L_0x02f7:
            r3 = 2
            gnu.expr.Expression[] r11 = new gnu.expr.Expression[r3]
            r3 = 0
            r0 = r38
            r1 = r43
            gnu.expr.Expression r5 = r0.coerceExpression(r12, r1)
            r11[r3] = r5
            r3 = 1
            r0 = r38
            r1 = r43
            gnu.expr.Expression r5 = r0.coerceExpression(r13, r1)
            r11[r3] = r5
            gnu.expr.ApplyExp r13 = new gnu.expr.ApplyExp
            gnu.bytecode.Method r3 = makePairMethod
            r13.<init>((gnu.bytecode.Method) r3, (gnu.expr.Expression[]) r11)
            r4 = r30
            r3 = r13
            goto L_0x0073
        L_0x031c:
            r0 = r25
            gnu.lists.Pair r13 = kawa.lang.Translator.makePair(r0, r12, r13)
            r4 = r30
            r3 = r13
            goto L_0x0073
        L_0x0327:
            r22 = r39
            r5 = 20
            gnu.lists.Pair[] r0 = new gnu.lists.Pair[r5]
            r27 = r0
            r18 = 0
        L_0x0331:
            r0 = r27
            int r5 = r0.length
            r0 = r18
            if (r0 < r5) goto L_0x034b
            int r5 = r18 * 2
            gnu.lists.Pair[] r0 = new gnu.lists.Pair[r5]
            r36 = r0
            r5 = 0
            r6 = 0
            r0 = r27
            r1 = r36
            r2 = r18
            java.lang.System.arraycopy(r0, r5, r1, r6, r2)
            r27 = r36
        L_0x034b:
            int r19 = r18 + 1
            r27[r18] = r22
            java.lang.Object r5 = r22.getCdr()
            if (r5 != r4) goto L_0x0370
            boolean r5 = r3 instanceof gnu.expr.Expression
            if (r5 == 0) goto L_0x0379
            gnu.lists.LList r31 = gnu.lists.LList.Empty
        L_0x035b:
            r18 = r19
        L_0x035d:
            int r18 = r18 + -1
            if (r18 < 0) goto L_0x037c
            r22 = r27[r18]
            java.lang.Object r5 = r22.getCar()
            r0 = r22
            r1 = r31
            gnu.lists.Pair r31 = kawa.lang.Translator.makePair(r0, r5, r1)
            goto L_0x035d
        L_0x0370:
            java.lang.Object r22 = r22.getCdr()
            gnu.lists.Pair r22 = (gnu.lists.Pair) r22
            r18 = r19
            goto L_0x0331
        L_0x0379:
            r31 = r3
            goto L_0x035b
        L_0x037c:
            boolean r5 = r3 instanceof gnu.expr.Expression
            if (r5 == 0) goto L_0x03bb
            r5 = 2
            gnu.expr.Expression[] r11 = new gnu.expr.Expression[r5]
            r5 = 1
            gnu.expr.Expression r3 = (gnu.expr.Expression) r3
            r11[r5] = r3
            r3 = 1
            r0 = r18
            if (r0 != r3) goto L_0x03a5
            r3 = 0
            java.lang.Object r5 = r39.getCar()
            r0 = r38
            r1 = r43
            gnu.expr.Expression r5 = r0.leaf(r5, r1)
            r11[r3] = r5
            gnu.expr.ApplyExp r3 = new gnu.expr.ApplyExp
            gnu.bytecode.Method r5 = makePairMethod
            r3.<init>((gnu.bytecode.Method) r5, (gnu.expr.Expression[]) r11)
            goto L_0x0077
        L_0x03a5:
            r3 = 0
            r0 = r38
            r1 = r31
            r2 = r43
            gnu.expr.Expression r5 = r0.leaf(r1, r2)
            r11[r3] = r5
            gnu.expr.ApplyExp r3 = new gnu.expr.ApplyExp
            gnu.bytecode.Method r5 = appendMethod
            r3.<init>((gnu.bytecode.Method) r5, (gnu.expr.Expression[]) r11)
            goto L_0x0077
        L_0x03bb:
            r3 = r31
            goto L_0x0077
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Quote.expand_pair(gnu.lists.Pair, int, kawa.lang.SyntaxForm, java.lang.Object, kawa.lang.Translator):java.lang.Object");
    }

    /* JADX WARNING: type inference failed for: r19v4 */
    /* JADX WARNING: type inference failed for: r19v5 */
    /* JADX WARNING: type inference failed for: r0v21, types: [gnu.expr.ApplyExp] */
    /* JADX WARNING: type inference failed for: r19v8 */
    /* JADX WARNING: type inference failed for: r0v22, types: [gnu.lists.FVector] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0109  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x010b A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object expand(java.lang.Object r23, int r24, kawa.lang.SyntaxForm r25, java.lang.Object r26, kawa.lang.Translator r27) {
        /*
            r22 = this;
            r13 = r26
            java.util.IdentityHashMap r13 = (java.util.IdentityHashMap) r13
            r0 = r23
            java.lang.Object r16 = r13.get(r0)
            java.lang.Object r3 = WORKING
            r0 = r16
            if (r0 != r3) goto L_0x0018
            java.lang.Object r3 = CYCLE
            r0 = r23
            r13.put(r0, r3)
        L_0x0017:
            return r16
        L_0x0018:
            java.lang.Object r3 = CYCLE
            r0 = r16
            if (r0 == r3) goto L_0x0017
            if (r16 != 0) goto L_0x0017
            r0 = r23
            boolean r3 = r0 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x005b
            r4 = r23
            gnu.lists.Pair r4 = (gnu.lists.Pair) r4
            r3 = r22
            r5 = r24
            r6 = r25
            r7 = r26
            r8 = r27
            java.lang.Object r19 = r3.expand_pair(r4, r5, r6, r7, r8)
            r16 = r19
        L_0x003a:
            r0 = r23
            r1 = r16
            if (r0 == r1) goto L_0x0053
            r0 = r23
            java.lang.Object r3 = r13.get(r0)
            java.lang.Object r6 = CYCLE
            if (r3 != r6) goto L_0x0053
            r3 = 101(0x65, float:1.42E-43)
            java.lang.String r6 = "cycle in non-literal data"
            r0 = r27
            r0.error(r3, r6)
        L_0x0053:
            r0 = r23
            r1 = r16
            r13.put(r0, r1)
            goto L_0x0017
        L_0x005b:
            r0 = r23
            boolean r3 = r0 instanceof kawa.lang.SyntaxForm
            if (r3 == 0) goto L_0x007a
            r25 = r23
            kawa.lang.SyntaxForm r25 = (kawa.lang.SyntaxForm) r25
            java.lang.Object r4 = r25.getDatum()
            r3 = r22
            r5 = r24
            r6 = r25
            r7 = r26
            r8 = r27
            java.lang.Object r19 = r3.expand(r4, r5, r6, r7, r8)
            r16 = r19
            goto L_0x003a
        L_0x007a:
            r0 = r23
            boolean r3 = r0 instanceof gnu.lists.FVector
            if (r3 == 0) goto L_0x01a8
            r21 = r23
            gnu.lists.FVector r21 = (gnu.lists.FVector) r21
            int r15 = r21.size()
            java.lang.Object[] r11 = new java.lang.Object[r15]
            byte[] r0 = new byte[r15]
            r20 = r0
            r14 = 0
            r12 = 0
        L_0x0090:
            if (r12 >= r15) goto L_0x0132
            r0 = r21
            java.lang.Object r4 = r0.get(r12)
            r5 = r24
            boolean r3 = r4 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x010e
            r3 = -1
            r0 = r24
            if (r0 <= r3) goto L_0x010e
            r17 = r4
            gnu.lists.Pair r17 = (gnu.lists.Pair) r17
            java.lang.Object r3 = r17.getCar()
            java.lang.String r6 = "unquote-splicing"
            r0 = r27
            r1 = r25
            boolean r3 = r0.matches((java.lang.Object) r3, (kawa.lang.SyntaxForm) r1, (java.lang.String) r6)
            if (r3 == 0) goto L_0x010e
            int r5 = r5 + -1
            if (r5 != 0) goto L_0x010e
            java.lang.Object r3 = r17.getCdr()
            boolean r3 = r3 instanceof gnu.lists.Pair
            if (r3 == 0) goto L_0x00d1
            java.lang.Object r18 = r17.getCdr()
            gnu.lists.Pair r18 = (gnu.lists.Pair) r18
            java.lang.Object r3 = r18.getCdr()
            gnu.lists.LList r6 = gnu.lists.LList.Empty
            if (r3 == r6) goto L_0x00f6
        L_0x00d1:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r6 = "invalid used of "
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.Object r6 = r17.getCar()
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r6 = " in quasiquote template"
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r3 = r3.toString()
            r0 = r27
            gnu.expr.Expression r16 = r0.syntaxError(r3)
            goto L_0x0017
        L_0x00f6:
            r0 = r27
            r1 = r18
            r2 = r25
            gnu.expr.Expression r3 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            r11[r12] = r3
            r3 = 3
            r20[r12] = r3
        L_0x0105:
            byte r3 = r20[r12]
            if (r3 <= r14) goto L_0x010b
            byte r14 = r20[r12]
        L_0x010b:
            int r12 = r12 + 1
            goto L_0x0090
        L_0x010e:
            r3 = r22
            r6 = r25
            r7 = r26
            r8 = r27
            java.lang.Object r3 = r3.expand(r4, r5, r6, r7, r8)
            r11[r12] = r3
            r3 = r11[r12]
            if (r3 != r4) goto L_0x0124
            r3 = 0
            r20[r12] = r3
            goto L_0x0105
        L_0x0124:
            r3 = r11[r12]
            boolean r3 = r3 instanceof gnu.expr.Expression
            if (r3 == 0) goto L_0x012e
            r3 = 2
            r20[r12] = r3
            goto L_0x0105
        L_0x012e:
            r3 = 1
            r20[r12] = r3
            goto L_0x0105
        L_0x0132:
            if (r14 != 0) goto L_0x013a
            r19 = r21
        L_0x0136:
            r16 = r19
            goto L_0x003a
        L_0x013a:
            r3 = 1
            if (r14 != r3) goto L_0x0145
            gnu.lists.FVector r19 = new gnu.lists.FVector
            r0 = r19
            r0.<init>((java.lang.Object[]) r11)
            goto L_0x0136
        L_0x0145:
            gnu.expr.Expression[] r10 = new gnu.expr.Expression[r15]
            r12 = 0
        L_0x0148:
            if (r12 >= r15) goto L_0x0196
            byte r3 = r20[r12]
            r6 = 3
            if (r3 != r6) goto L_0x0158
            r3 = r11[r12]
            gnu.expr.Expression r3 = (gnu.expr.Expression) r3
            r10[r12] = r3
        L_0x0155:
            int r12 = r12 + 1
            goto L_0x0148
        L_0x0158:
            r3 = 3
            if (r14 >= r3) goto L_0x0168
            r3 = r11[r12]
            r0 = r22
            r1 = r27
            gnu.expr.Expression r3 = r0.coerceExpression(r3, r1)
            r10[r12] = r3
            goto L_0x0155
        L_0x0168:
            byte r3 = r20[r12]
            r6 = 2
            if (r3 >= r6) goto L_0x0185
            r3 = 1
            java.lang.Object[] r9 = new java.lang.Object[r3]
            r3 = 0
            r6 = r11[r12]
            r9[r3] = r6
            gnu.lists.FVector r3 = new gnu.lists.FVector
            r3.<init>((java.lang.Object[]) r9)
            r0 = r22
            r1 = r27
            gnu.expr.Expression r3 = r0.leaf(r3, r1)
            r10[r12] = r3
            goto L_0x0155
        L_0x0185:
            r3 = 1
            gnu.expr.Expression[] r9 = new gnu.expr.Expression[r3]
            r6 = 0
            r3 = r11[r12]
            gnu.expr.Expression r3 = (gnu.expr.Expression) r3
            r9[r6] = r3
            gnu.expr.ApplyExp r3 = makeInvokeMakeVector(r9)
            r10[r12] = r3
            goto L_0x0155
        L_0x0196:
            r3 = 3
            if (r14 >= r3) goto L_0x019e
            gnu.expr.ApplyExp r19 = makeInvokeMakeVector(r10)
            goto L_0x0136
        L_0x019e:
            gnu.expr.ApplyExp r19 = new gnu.expr.ApplyExp
            gnu.bytecode.Method r3 = vectorAppendMethod
            r0 = r19
            r0.<init>((gnu.bytecode.Method) r3, (gnu.expr.Expression[]) r10)
            goto L_0x0136
        L_0x01a8:
            r19 = r23
            r16 = r19
            goto L_0x003a
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Quote.expand(java.lang.Object, int, kawa.lang.SyntaxForm, java.lang.Object, kawa.lang.Translator):java.lang.Object");
    }

    private static ApplyExp makeInvokeMakeVector(Expression[] args) {
        return new ApplyExp(makeVectorMethod, args);
    }

    public Expression rewrite(Object obj, Translator tr) {
        if (obj instanceof Pair) {
            Pair pair = (Pair) obj;
            if (pair.getCdr() == LList.Empty) {
                return coerceExpression(expand(pair.getCar(), this.isQuasi ? 1 : -1, tr), tr);
            }
        }
        return tr.syntaxError("wrong number of arguments to quote");
    }

    public static Object consX$V(Object[] args) {
        return LList.consX(args);
    }

    public static Object append$V(Object[] args) {
        Pair pair;
        int count = args.length;
        if (count == 0) {
            return LList.Empty;
        }
        Pair result = args[count - 1];
        int i = count - 1;
        while (true) {
            Pair pair2 = result;
            i--;
            if (i < 0) {
                return pair2;
            }
            Object list = args[i];
            Pair last = null;
            SyntaxForm syntax = null;
            result = null;
            while (true) {
                if (list instanceof SyntaxForm) {
                    syntax = (SyntaxForm) list;
                    list = syntax.getDatum();
                } else if (list == LList.Empty) {
                    break;
                } else {
                    Pair list_pair = (Pair) list;
                    Object car = list_pair.getCar();
                    if (syntax != null && !(car instanceof SyntaxForm)) {
                        car = SyntaxForms.makeForm(car, syntax.getScope());
                    }
                    Pair new_pair = new Pair(car, (Object) null);
                    if (last == null) {
                        pair = new_pair;
                    } else {
                        last.setCdr(new_pair);
                        pair = result;
                    }
                    last = new_pair;
                    list = list_pair.getCdr();
                    result = pair;
                }
            }
            if (last != null) {
                last.setCdr(pair2);
            } else {
                result = pair2;
            }
        }
    }
}
