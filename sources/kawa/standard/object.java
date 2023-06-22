package kawa.standard;

import gnu.bytecode.Type;
import gnu.expr.ClassExp;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.Keyword;
import gnu.expr.LambdaExp;
import gnu.expr.ObjectExp;
import gnu.expr.ReferenceExp;
import gnu.expr.ScopeExp;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Namespace;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import java.util.Vector;
import kawa.lang.Lambda;
import kawa.lang.Syntax;
import kawa.lang.SyntaxForm;
import kawa.lang.Translator;

public class object extends Syntax {
    public static final Keyword accessKeyword = Keyword.make("access");
    public static final Keyword allocationKeyword = Keyword.make("allocation");
    public static final Keyword classNameKeyword = Keyword.make("class-name");
    static final Symbol coloncolon = Namespace.EmptyNamespace.getSymbol("::");
    static final Keyword initKeyword = Keyword.make("init");
    static final Keyword init_formKeyword = Keyword.make("init-form");
    static final Keyword init_keywordKeyword = Keyword.make("init-keyword");
    static final Keyword init_valueKeyword = Keyword.make("init-value");
    static final Keyword initformKeyword = Keyword.make("initform");
    public static final Keyword interfaceKeyword = Keyword.make("interface");
    public static final object objectSyntax = new object(SchemeCompilation.lambda);
    public static final Keyword throwsKeyword = Keyword.make("throws");
    static final Keyword typeKeyword = Keyword.make("type");
    Lambda lambda;

    static {
        objectSyntax.setName("object");
    }

    public object(Lambda lambda2) {
        this.lambda = lambda2;
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        if (!(form.getCdr() instanceof Pair)) {
            return tr.syntaxError("missing superclass specification in object");
        }
        Pair pair = (Pair) form.getCdr();
        ObjectExp oexp = new ObjectExp();
        if (pair.getCar() instanceof FString) {
            if (!(pair.getCdr() instanceof Pair)) {
                return tr.syntaxError("missing superclass specification after object class name");
            }
            pair = (Pair) pair.getCdr();
        }
        Object[] saved = scanClassDef(pair, oexp, tr);
        if (saved == null) {
            return oexp;
        }
        rewriteClassDef(saved, tr);
        return oexp;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:127:0x0327, code lost:
        r16 = null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object[] scanClassDef(gnu.lists.Pair r43, gnu.expr.ClassExp r44, kawa.lang.Translator r45) {
        /*
            r42 = this;
            r45.mustCompileHere()
            java.lang.Object r39 = r43.getCar()
            java.lang.Object r18 = r43.getCdr()
            r17 = 0
            r27 = 0
            r25 = 0
            r6 = 0
            java.util.Vector r21 = new java.util.Vector
            r5 = 20
            r0 = r21
            r0.<init>(r5)
            r31 = r18
        L_0x001e:
            gnu.lists.LList r5 = gnu.lists.LList.Empty
            r0 = r31
            if (r0 == r5) goto L_0x042c
        L_0x0024:
            r0 = r31
            boolean r5 = r0 instanceof kawa.lang.SyntaxForm
            if (r5 == 0) goto L_0x0031
            kawa.lang.SyntaxForm r31 = (kawa.lang.SyntaxForm) r31
            java.lang.Object r31 = r31.getDatum()
            goto L_0x0024
        L_0x0031:
            r0 = r31
            boolean r5 = r0 instanceof gnu.lists.Pair
            if (r5 != 0) goto L_0x0043
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "object member not a list"
            r0 = r45
            r0.error(r5, r8)
            r33 = 0
        L_0x0042:
            return r33
        L_0x0043:
            r43 = r31
            gnu.lists.Pair r43 = (gnu.lists.Pair) r43
            java.lang.Object r32 = r43.getCar()
        L_0x004b:
            r0 = r32
            boolean r5 = r0 instanceof kawa.lang.SyntaxForm
            if (r5 == 0) goto L_0x0058
            kawa.lang.SyntaxForm r32 = (kawa.lang.SyntaxForm) r32
            java.lang.Object r32 = r32.getDatum()
            goto L_0x004b
        L_0x0058:
            java.lang.Object r31 = r43.getCdr()
            r0 = r45
            r1 = r43
            java.lang.Object r34 = r0.pushPositionOf(r1)
            r0 = r32
            boolean r5 = r0 instanceof gnu.expr.Keyword
            if (r5 == 0) goto L_0x011a
        L_0x006a:
            r0 = r31
            boolean r5 = r0 instanceof kawa.lang.SyntaxForm
            if (r5 == 0) goto L_0x0077
            kawa.lang.SyntaxForm r31 = (kawa.lang.SyntaxForm) r31
            java.lang.Object r31 = r31.getDatum()
            goto L_0x006a
        L_0x0077:
            r0 = r31
            boolean r5 = r0 instanceof gnu.lists.Pair
            if (r5 == 0) goto L_0x011a
            gnu.expr.Keyword r5 = interfaceKeyword
            r0 = r32
            if (r0 != r5) goto L_0x00b0
            r5 = r31
            gnu.lists.Pair r5 = (gnu.lists.Pair) r5
            java.lang.Object r41 = r5.getCar()
            java.lang.Boolean r5 = java.lang.Boolean.FALSE
            r0 = r41
            if (r0 != r5) goto L_0x00a7
            r5 = 65536(0x10000, float:9.18355E-41)
            r0 = r44
            r0.setFlag(r5)
        L_0x0098:
            gnu.lists.Pair r31 = (gnu.lists.Pair) r31
            java.lang.Object r31 = r31.getCdr()
            r0 = r45
            r1 = r34
            r0.popPositionOf(r1)
            goto L_0x001e
        L_0x00a7:
            r5 = 32768(0x8000, float:4.5918E-41)
            r0 = r44
            r0.setFlag(r5)
            goto L_0x0098
        L_0x00b0:
            gnu.expr.Keyword r5 = classNameKeyword
            r0 = r32
            if (r0 != r5) goto L_0x00d2
            if (r17 == 0) goto L_0x00c1
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "duplicate class-name specifiers"
            r0 = r45
            r0.error(r5, r8)
        L_0x00c1:
            r17 = r31
            gnu.lists.Pair r31 = (gnu.lists.Pair) r31
            java.lang.Object r31 = r31.getCdr()
            r0 = r45
            r1 = r34
            r0.popPositionOf(r1)
            goto L_0x001e
        L_0x00d2:
            gnu.expr.Keyword r5 = accessKeyword
            r0 = r32
            if (r0 != r5) goto L_0x011a
            r0 = r45
            r1 = r31
            java.lang.Object r35 = r0.pushPositionOf(r1)
            r5 = r31
            gnu.lists.Pair r5 = (gnu.lists.Pair) r5
            java.lang.Object r5 = r5.getCar()
            r8 = 25820135424(0x603000000, double:1.2756841884E-313)
            java.lang.String r10 = "class"
            r11 = r45
            long r6 = addAccessFlags(r5, r6, r8, r10, r11)
            r0 = r44
            gnu.expr.Declaration r5 = r0.nameDecl
            if (r5 != 0) goto L_0x0104
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "access specifier for anonymous class"
            r0 = r45
            r0.error(r5, r8)
        L_0x0104:
            r0 = r45
            r1 = r35
            r0.popPositionOf(r1)
            gnu.lists.Pair r31 = (gnu.lists.Pair) r31
            java.lang.Object r31 = r31.getCdr()
            r0 = r45
            r1 = r34
            r0.popPositionOf(r1)
            goto L_0x001e
        L_0x011a:
            r0 = r32
            boolean r5 = r0 instanceof gnu.lists.Pair
            if (r5 != 0) goto L_0x012d
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "object member not a list"
            r0 = r45
            r0.error(r5, r8)
            r33 = 0
            goto L_0x0042
        L_0x012d:
            r43 = r32
            gnu.lists.Pair r43 = (gnu.lists.Pair) r43
            java.lang.Object r32 = r43.getCar()
        L_0x0135:
            r0 = r32
            boolean r5 = r0 instanceof kawa.lang.SyntaxForm
            if (r5 == 0) goto L_0x0142
            kawa.lang.SyntaxForm r32 = (kawa.lang.SyntaxForm) r32
            java.lang.Object r32 = r32.getDatum()
            goto L_0x0135
        L_0x0142:
            r0 = r32
            boolean r5 = r0 instanceof java.lang.String
            if (r5 != 0) goto L_0x0154
            r0 = r32
            boolean r5 = r0 instanceof gnu.mapping.Symbol
            if (r5 != 0) goto L_0x0154
            r0 = r32
            boolean r5 = r0 instanceof gnu.expr.Keyword
            if (r5 == 0) goto L_0x03d7
        L_0x0154:
            r40 = 0
            r38 = r32
            r4 = 0
            r10 = 0
            r0 = r38
            boolean r5 = r0 instanceof gnu.expr.Keyword
            if (r5 == 0) goto L_0x0180
            r19 = 0
            r16 = r43
        L_0x0165:
            r30 = 0
            r37 = 0
            r20 = 0
        L_0x016b:
            gnu.lists.LList r5 = gnu.lists.LList.Empty
            r0 = r16
            if (r0 == r5) goto L_0x0329
            r5 = r16
        L_0x0173:
            boolean r8 = r5 instanceof kawa.lang.SyntaxForm
            if (r8 == 0) goto L_0x01a2
            kawa.lang.SyntaxForm r5 = (kawa.lang.SyntaxForm) r5
            java.lang.Object r16 = r5.getDatum()
            r5 = r16
            goto L_0x0173
        L_0x0180:
            r0 = r44
            r1 = r38
            gnu.expr.Declaration r19 = r0.addDeclaration((java.lang.Object) r1)
            r5 = 0
            r0 = r19
            r0.setSimple(r5)
            r12 = 1048576(0x100000, double:5.180654E-318)
            r0 = r19
            r0.setFlag(r12)
            r0 = r19
            r1 = r43
            kawa.lang.Translator.setLine((gnu.expr.Declaration) r0, (java.lang.Object) r1)
            java.lang.Object r16 = r43.getCdr()
            goto L_0x0165
        L_0x01a2:
            r43 = r5
            gnu.lists.Pair r43 = (gnu.lists.Pair) r43
            r24 = r43
            java.lang.Object r23 = r43.getCar()
        L_0x01ac:
            r0 = r23
            boolean r5 = r0 instanceof kawa.lang.SyntaxForm
            if (r5 == 0) goto L_0x01b9
            kawa.lang.SyntaxForm r23 = (kawa.lang.SyntaxForm) r23
            java.lang.Object r23 = r23.getDatum()
            goto L_0x01ac
        L_0x01b9:
            r0 = r45
            r1 = r43
            java.lang.Object r35 = r0.pushPositionOf(r1)
            java.lang.Object r16 = r43.getCdr()
            gnu.mapping.Symbol r5 = coloncolon
            r0 = r23
            if (r0 == r5) goto L_0x01d1
            r0 = r23
            boolean r5 = r0 instanceof gnu.expr.Keyword
            if (r5 == 0) goto L_0x02f5
        L_0x01d1:
            r0 = r16
            boolean r5 = r0 instanceof gnu.lists.Pair
            if (r5 == 0) goto L_0x02f5
            int r30 = r30 + 1
            r43 = r16
            gnu.lists.Pair r43 = (gnu.lists.Pair) r43
            java.lang.Object r9 = r43.getCar()
            java.lang.Object r16 = r43.getCdr()
            gnu.mapping.Symbol r5 = coloncolon
            r0 = r23
            if (r0 == r5) goto L_0x01f1
            gnu.expr.Keyword r5 = typeKeyword
            r0 = r23
            if (r0 != r5) goto L_0x01fc
        L_0x01f1:
            r40 = r43
        L_0x01f3:
            r0 = r45
            r1 = r35
            r0.popPositionOf(r1)
            goto L_0x016b
        L_0x01fc:
            gnu.expr.Keyword r5 = allocationKeyword
            r0 = r23
            if (r0 != r5) goto L_0x0252
            if (r4 == 0) goto L_0x020d
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "duplicate allocation: specification"
            r0 = r45
            r0.error(r5, r8)
        L_0x020d:
            java.lang.String r5 = "class"
            r0 = r45
            boolean r5 = matches(r9, r5, r0)
            if (r5 != 0) goto L_0x0221
            java.lang.String r5 = "static"
            r0 = r45
            boolean r5 = matches(r9, r5, r0)
            if (r5 == 0) goto L_0x0224
        L_0x0221:
            r4 = 2048(0x800, float:2.87E-42)
            goto L_0x01f3
        L_0x0224:
            java.lang.String r5 = "instance"
            r0 = r45
            boolean r5 = matches(r9, r5, r0)
            if (r5 == 0) goto L_0x0231
            r4 = 4096(0x1000, float:5.74E-42)
            goto L_0x01f3
        L_0x0231:
            r5 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r12 = "unknown allocation kind '"
            java.lang.StringBuilder r8 = r8.append(r12)
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r12 = "'"
            java.lang.StringBuilder r8 = r8.append(r12)
            java.lang.String r8 = r8.toString()
            r0 = r45
            r0.error(r5, r8)
            goto L_0x01f3
        L_0x0252:
            gnu.expr.Keyword r5 = initKeyword
            r0 = r23
            if (r0 == r5) goto L_0x026a
            gnu.expr.Keyword r5 = initformKeyword
            r0 = r23
            if (r0 == r5) goto L_0x026a
            gnu.expr.Keyword r5 = init_formKeyword
            r0 = r23
            if (r0 == r5) goto L_0x026a
            gnu.expr.Keyword r5 = init_valueKeyword
            r0 = r23
            if (r0 != r5) goto L_0x0281
        L_0x026a:
            if (r37 == 0) goto L_0x0275
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "duplicate initialization"
            r0 = r45
            r0.error(r5, r8)
        L_0x0275:
            r37 = 1
            gnu.expr.Keyword r5 = initKeyword
            r0 = r23
            if (r0 == r5) goto L_0x01f3
            r20 = r43
            goto L_0x01f3
        L_0x0281:
            gnu.expr.Keyword r5 = init_keywordKeyword
            r0 = r23
            if (r0 != r5) goto L_0x02ad
            boolean r5 = r9 instanceof gnu.expr.Keyword
            if (r5 != 0) goto L_0x0296
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "invalid 'init-keyword' - not a keyword"
            r0 = r45
            r0.error(r5, r8)
            goto L_0x01f3
        L_0x0296:
            gnu.expr.Keyword r9 = (gnu.expr.Keyword) r9
            java.lang.String r5 = r9.getName()
            java.lang.String r8 = r38.toString()
            if (r5 == r8) goto L_0x01f3
            r5 = 119(0x77, float:1.67E-43)
            java.lang.String r8 = "init-keyword option ignored"
            r0 = r45
            r0.error(r5, r8)
            goto L_0x01f3
        L_0x02ad:
            gnu.expr.Keyword r5 = accessKeyword
            r0 = r23
            if (r0 != r5) goto L_0x02d1
            r0 = r45
            r1 = r43
            java.lang.Object r36 = r0.pushPositionOf(r1)
            r12 = 32463912960(0x78f000000, double:1.6039304123E-313)
            java.lang.String r14 = "field"
            r15 = r45
            long r10 = addAccessFlags(r9, r10, r12, r14, r15)
            r0 = r45
            r1 = r36
            r0.popPositionOf(r1)
            goto L_0x01f3
        L_0x02d1:
            r5 = 119(0x77, float:1.67E-43)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r12 = "unknown slot keyword '"
            java.lang.StringBuilder r8 = r8.append(r12)
            r0 = r23
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.String r12 = "'"
            java.lang.StringBuilder r8 = r8.append(r12)
            java.lang.String r8 = r8.toString()
            r0 = r45
            r0.error(r5, r8)
            goto L_0x01f3
        L_0x02f5:
            gnu.lists.LList r5 = gnu.lists.LList.Empty
            r0 = r16
            if (r0 != r5) goto L_0x0303
            if (r37 != 0) goto L_0x0303
            r20 = r24
            r37 = 1
            goto L_0x01f3
        L_0x0303:
            r0 = r16
            boolean r5 = r0 instanceof gnu.lists.Pair
            if (r5 == 0) goto L_0x0327
            if (r30 != 0) goto L_0x0327
            if (r37 != 0) goto L_0x0327
            if (r40 != 0) goto L_0x0327
            r43 = r16
            gnu.lists.Pair r43 = (gnu.lists.Pair) r43
            java.lang.Object r5 = r43.getCdr()
            gnu.lists.LList r8 = gnu.lists.LList.Empty
            if (r5 != r8) goto L_0x0327
            r40 = r24
            r20 = r43
            java.lang.Object r16 = r43.getCdr()
            r37 = 1
            goto L_0x01f3
        L_0x0327:
            r16 = 0
        L_0x0329:
            gnu.lists.LList r5 = gnu.lists.LList.Empty
            r0 = r16
            if (r0 == r5) goto L_0x036c
            r8 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r12 = "invalid argument list for slot '"
            java.lang.StringBuilder r5 = r5.append(r12)
            r0 = r38
            java.lang.StringBuilder r5 = r5.append(r0)
            r12 = 39
            java.lang.StringBuilder r5 = r5.append(r12)
            java.lang.String r12 = " args:"
            java.lang.StringBuilder r12 = r5.append(r12)
            if (r16 != 0) goto L_0x0363
            java.lang.String r5 = "null"
        L_0x0352:
            java.lang.StringBuilder r5 = r12.append(r5)
            java.lang.String r5 = r5.toString()
            r0 = r45
            r0.error(r8, r5)
            r33 = 0
            goto L_0x0042
        L_0x0363:
            java.lang.Class r5 = r16.getClass()
            java.lang.String r5 = r5.getName()
            goto L_0x0352
        L_0x036c:
            if (r37 == 0) goto L_0x0384
            r5 = 2048(0x800, float:2.87E-42)
            if (r4 != r5) goto L_0x0395
            r22 = 1
        L_0x0374:
            if (r19 == 0) goto L_0x0398
            r5 = r19
        L_0x0378:
            r0 = r21
            r0.addElement(r5)
            r0 = r21
            r1 = r20
            r0.addElement(r1)
        L_0x0384:
            if (r19 != 0) goto L_0x03a0
            if (r37 != 0) goto L_0x03ce
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "missing field name"
            r0 = r45
            r0.error(r5, r8)
            r33 = 0
            goto L_0x0042
        L_0x0395:
            r22 = 0
            goto L_0x0374
        L_0x0398:
            if (r22 == 0) goto L_0x039d
            java.lang.Boolean r5 = java.lang.Boolean.TRUE
            goto L_0x0378
        L_0x039d:
            java.lang.Boolean r5 = java.lang.Boolean.FALSE
            goto L_0x0378
        L_0x03a0:
            if (r40 == 0) goto L_0x03af
            r0 = r45
            r1 = r40
            gnu.bytecode.Type r5 = r0.exp2Type(r1)
            r0 = r19
            r0.setType(r5)
        L_0x03af:
            if (r4 == 0) goto L_0x03b7
            long r12 = (long) r4
            r0 = r19
            r0.setFlag(r12)
        L_0x03b7:
            r12 = 0
            int r5 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r5 == 0) goto L_0x03c2
            r0 = r19
            r0.setFlag(r10)
        L_0x03c2:
            r5 = 1
            r0 = r19
            r0.setCanRead(r5)
            r5 = 1
            r0 = r19
            r0.setCanWrite(r5)
        L_0x03ce:
            r0 = r45
            r1 = r34
            r0.popPositionOf(r1)
            goto L_0x001e
        L_0x03d7:
            r0 = r32
            boolean r5 = r0 instanceof gnu.lists.Pair
            if (r5 == 0) goto L_0x0422
            r29 = r32
            gnu.lists.Pair r29 = (gnu.lists.Pair) r29
            java.lang.Object r28 = r29.getCar()
            r0 = r28
            boolean r5 = r0 instanceof java.lang.String
            if (r5 != 0) goto L_0x03fe
            r0 = r28
            boolean r5 = r0 instanceof gnu.mapping.Symbol
            if (r5 != 0) goto L_0x03fe
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "missing method name"
            r0 = r45
            r0.error(r5, r8)
            r33 = 0
            goto L_0x0042
        L_0x03fe:
            gnu.expr.LambdaExp r26 = new gnu.expr.LambdaExp
            r26.<init>()
            r0 = r44
            r1 = r26
            r2 = r28
            gnu.expr.Declaration r19 = r0.addMethod(r1, r2)
            r0 = r19
            r1 = r29
            kawa.lang.Translator.setLine((gnu.expr.Declaration) r0, (java.lang.Object) r1)
            if (r25 != 0) goto L_0x041b
            r27 = r26
        L_0x0418:
            r25 = r26
            goto L_0x03ce
        L_0x041b:
            r0 = r26
            r1 = r25
            r1.nextSibling = r0
            goto L_0x0418
        L_0x0422:
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r8 = "invalid field/method definition"
            r0 = r45
            r0.error(r5, r8)
            goto L_0x03ce
        L_0x042c:
            r12 = 0
            int r5 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
            if (r5 == 0) goto L_0x0439
            r0 = r44
            gnu.expr.Declaration r5 = r0.nameDecl
            r5.setFlag(r6)
        L_0x0439:
            r5 = 6
            java.lang.Object[] r0 = new java.lang.Object[r5]
            r33 = r0
            r5 = 0
            r33[r5] = r44
            r5 = 1
            r33[r5] = r18
            r5 = 2
            r33[r5] = r21
            r5 = 3
            r33[r5] = r27
            r5 = 4
            r33[r5] = r39
            r5 = 5
            r33[r5] = r17
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.object.scanClassDef(gnu.lists.Pair, gnu.expr.ClassExp, kawa.lang.Translator):java.lang.Object[]");
    }

    public void rewriteClassDef(Object[] saved, Translator tr) {
        Declaration decl;
        ClassExp oexp = saved[0];
        Object components = saved[1];
        Vector inits = saved[2];
        LambdaExp method_list = saved[3];
        Object superlist = saved[4];
        Object classNamePair = saved[5];
        oexp.firstChild = method_list;
        int num_supers = Translator.listLength(superlist);
        if (num_supers < 0) {
            tr.error('e', "object superclass specification not a list");
            num_supers = 0;
        }
        Expression[] supers = new Expression[num_supers];
        for (int i = 0; i < num_supers; i++) {
            while (superlist instanceof SyntaxForm) {
                superlist = ((SyntaxForm) superlist).getDatum();
            }
            Pair superpair = (Pair) superlist;
            supers[i] = tr.rewrite_car(superpair, false);
            if ((supers[i] instanceof ReferenceExp) && (decl = Declaration.followAliases(((ReferenceExp) supers[i]).getBinding())) != null) {
                Expression svalue = decl.getValue();
                if (svalue instanceof ClassExp) {
                    ((ClassExp) svalue).setFlag(131072);
                }
            }
            superlist = superpair.getCdr();
        }
        if (classNamePair != null) {
            Object classNameVal = tr.rewrite_car((Pair) classNamePair, false).valueIfConstant();
            if (classNameVal instanceof CharSequence) {
                String classNameSpecifier = classNameVal.toString();
                if (classNameSpecifier.length() > 0) {
                    oexp.classNameSpecifier = classNameSpecifier;
                }
            }
            Object savedPos = tr.pushPositionOf(classNamePair);
            tr.error('e', "class-name specifier must be a non-empty string literal");
            tr.popPositionOf(savedPos);
        }
        oexp.supers = supers;
        oexp.setTypes(tr);
        int len = inits.size();
        for (int i2 = 0; i2 < len; i2 += 2) {
            Object init = inits.elementAt(i2 + 1);
            if (init != null) {
                rewriteInit(inits.elementAt(i2), oexp, (Pair) init, tr, (SyntaxForm) null);
            }
        }
        tr.push((ScopeExp) oexp);
        LambdaExp meth = method_list;
        int init_index = 0;
        SyntaxForm componentsSyntax = null;
        Object obj = components;
        while (obj != LList.Empty) {
            while (obj instanceof SyntaxForm) {
                componentsSyntax = (SyntaxForm) obj;
                obj = componentsSyntax.getDatum();
            }
            Pair pair = (Pair) obj;
            Object savedPos1 = tr.pushPositionOf(pair);
            Object pair_car = pair.getCar();
            SyntaxForm memberSyntax = componentsSyntax;
            while (pair_car instanceof SyntaxForm) {
                memberSyntax = (SyntaxForm) pair_car;
                pair_car = memberSyntax.getDatum();
            }
            try {
                obj = pair.getCdr();
                if (!(pair_car instanceof Keyword) || !(obj instanceof Pair)) {
                    Pair pair2 = (Pair) pair_car;
                    Object pair_car2 = pair2.getCar();
                    SyntaxForm memberCarSyntax = memberSyntax;
                    while (pair_car2 instanceof SyntaxForm) {
                        memberCarSyntax = (SyntaxForm) pair_car2;
                        pair_car2 = memberCarSyntax.getDatum();
                    }
                    if ((pair_car2 instanceof String) || (pair_car2 instanceof Symbol) || (pair_car2 instanceof Keyword)) {
                        Object type = null;
                        int nKeywords = 0;
                        Object args = pair_car2 instanceof Keyword ? pair2 : pair2.getCdr();
                        Pair initPair = null;
                        SyntaxForm initSyntax = null;
                        while (true) {
                            if (args == LList.Empty) {
                                break;
                            }
                            while (args instanceof SyntaxForm) {
                                memberSyntax = (SyntaxForm) args;
                                args = memberSyntax.getDatum();
                            }
                            Pair pair3 = (Pair) args;
                            Object key = pair3.getCar();
                            while (key instanceof SyntaxForm) {
                                key = ((SyntaxForm) key).getDatum();
                            }
                            Object savedPos2 = tr.pushPositionOf(pair3);
                            args = pair3.getCdr();
                            if ((key == coloncolon || (key instanceof Keyword)) && (args instanceof Pair)) {
                                nKeywords++;
                                Pair pair4 = (Pair) args;
                                Object value = pair4.getCar();
                                args = pair4.getCdr();
                                if (key == coloncolon || key == typeKeyword) {
                                    type = value;
                                } else if (key == initKeyword || key == initformKeyword || key == init_formKeyword || key == init_valueKeyword) {
                                    initPair = pair4;
                                    initSyntax = memberSyntax;
                                }
                            } else if (args != LList.Empty || initPair != null) {
                                if (!(args instanceof Pair) || nKeywords != 0 || initPair != null || type != null) {
                                    break;
                                }
                                Pair pair5 = (Pair) args;
                                if (pair5.getCdr() != LList.Empty) {
                                    break;
                                }
                                type = key;
                                initPair = pair5;
                                initSyntax = memberSyntax;
                                args = pair5.getCdr();
                            } else {
                                initPair = pair3;
                                initSyntax = memberSyntax;
                            }
                            tr.popPositionOf(savedPos2);
                        }
                        if (initPair != null) {
                            int init_index2 = init_index + 1;
                            try {
                                Object d = inits.elementAt(init_index);
                                if (d instanceof Declaration) {
                                    boolean flag = ((Declaration) d).getFlag(2048);
                                } else if (d == Boolean.TRUE) {
                                }
                                init_index = init_index2 + 1;
                                if (inits.elementAt(init_index2) == null) {
                                    rewriteInit(d, oexp, initPair, tr, initSyntax);
                                }
                            } catch (Throwable th) {
                                th = th;
                                int i3 = init_index2;
                                tr.popPositionOf(savedPos1);
                                throw th;
                            }
                        }
                    } else if (pair_car2 instanceof Pair) {
                        ScopeExp save_scope = tr.currentScope();
                        if (memberSyntax != null) {
                            tr.setCurrentScope(memberSyntax.getScope());
                        }
                        if ("*init*".equals(meth.getName())) {
                            meth.setReturnType(Type.voidType);
                        }
                        Translator.setLine((Expression) meth, (Object) pair2);
                        LambdaExp saveLambda = tr.curMethodLambda;
                        tr.curMethodLambda = meth;
                        this.lambda.rewrite(meth, ((Pair) pair_car2).getCdr(), pair2.getCdr(), tr, (memberCarSyntax == null || (memberSyntax != null && memberCarSyntax.getScope() == memberSyntax.getScope())) ? null : memberCarSyntax.getScope());
                        tr.curMethodLambda = saveLambda;
                        if (memberSyntax != null) {
                            tr.setCurrentScope(save_scope);
                        }
                        meth = meth.nextSibling;
                    } else {
                        tr.syntaxError("invalid field/method definition");
                    }
                    tr.popPositionOf(savedPos1);
                } else {
                    obj = ((Pair) obj).getCdr();
                    tr.popPositionOf(savedPos1);
                }
            } catch (Throwable th2) {
                th = th2;
                tr.popPositionOf(savedPos1);
                throw th;
            }
        }
        if (oexp.initMethod != null) {
            oexp.initMethod.outer = oexp;
        }
        if (oexp.clinitMethod != null) {
            oexp.clinitMethod.outer = oexp;
        }
        tr.pop(oexp);
        oexp.declareParts(tr);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: gnu.expr.ApplyExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: gnu.expr.ApplyExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: gnu.expr.SetExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: gnu.expr.ApplyExp} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void rewriteInit(java.lang.Object r11, gnu.expr.ClassExp r12, gnu.lists.Pair r13, kawa.lang.Translator r14, kawa.lang.SyntaxForm r15) {
        /*
            r10 = 0
            r7 = 1
            boolean r6 = r11 instanceof gnu.expr.Declaration
            if (r6 == 0) goto L_0x0061
            r6 = r11
            gnu.expr.Declaration r6 = (gnu.expr.Declaration) r6
            r8 = 2048(0x800, double:1.0118E-320)
            boolean r3 = r6.getFlag(r8)
        L_0x000f:
            if (r3 == 0) goto L_0x0069
            gnu.expr.LambdaExp r1 = r12.clinitMethod
        L_0x0013:
            if (r1 != 0) goto L_0x0036
            gnu.expr.LambdaExp r1 = new gnu.expr.LambdaExp
            gnu.expr.BeginExp r6 = new gnu.expr.BeginExp
            r6.<init>()
            r1.<init>((gnu.expr.Expression) r6)
            r1.setClassMethod(r7)
            gnu.bytecode.PrimType r6 = gnu.bytecode.Type.voidType
            r1.setReturnType(r6)
            if (r3 == 0) goto L_0x006c
            java.lang.String r6 = "$clinit$"
            r1.setName(r6)
            r12.clinitMethod = r1
        L_0x0030:
            gnu.expr.LambdaExp r6 = r12.firstChild
            r1.nextSibling = r6
            r12.firstChild = r1
        L_0x0036:
            r14.push((gnu.expr.ScopeExp) r1)
            gnu.expr.LambdaExp r4 = r14.curMethodLambda
            r14.curMethodLambda = r1
            gnu.expr.Expression r2 = r14.rewrite_car((gnu.lists.Pair) r13, (kawa.lang.SyntaxForm) r15)
            boolean r6 = r11 instanceof gnu.expr.Declaration
            if (r6 == 0) goto L_0x007e
            r0 = r11
            gnu.expr.Declaration r0 = (gnu.expr.Declaration) r0
            gnu.expr.SetExp r5 = new gnu.expr.SetExp
            r5.<init>((gnu.expr.Declaration) r0, (gnu.expr.Expression) r2)
            r5.setLocation(r0)
            r0.noteValue(r10)
            r2 = r5
        L_0x0054:
            gnu.expr.Expression r6 = r1.body
            gnu.expr.BeginExp r6 = (gnu.expr.BeginExp) r6
            r6.add(r2)
            r14.curMethodLambda = r4
            r14.pop(r1)
            return
        L_0x0061:
            java.lang.Boolean r6 = java.lang.Boolean.TRUE
            if (r11 != r6) goto L_0x0067
            r3 = r7
            goto L_0x000f
        L_0x0067:
            r3 = 0
            goto L_0x000f
        L_0x0069:
            gnu.expr.LambdaExp r1 = r12.initMethod
            goto L_0x0013
        L_0x006c:
            java.lang.String r6 = "$finit$"
            r1.setName(r6)
            r12.initMethod = r1
            gnu.expr.Declaration r6 = new gnu.expr.Declaration
            java.lang.String r7 = gnu.expr.ThisExp.THIS_NAME
            r6.<init>((java.lang.Object) r7)
            r1.add(r10, r6)
            goto L_0x0030
        L_0x007e:
            gnu.expr.QuoteExp r6 = new gnu.expr.QuoteExp
            gnu.bytecode.PrimType r7 = gnu.bytecode.Type.voidType
            r6.<init>(r7)
            gnu.expr.ApplyExp r2 = gnu.expr.Compilation.makeCoercion((gnu.expr.Expression) r2, (gnu.expr.Expression) r6)
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.object.rewriteInit(java.lang.Object, gnu.expr.ClassExp, gnu.lists.Pair, kawa.lang.Translator, kawa.lang.SyntaxForm):void");
    }

    static boolean matches(Object exp, String tag, Translator tr) {
        String value;
        if (exp instanceof Keyword) {
            value = ((Keyword) exp).getName();
        } else if (exp instanceof FString) {
            value = ((FString) exp).toString();
        } else if (!(exp instanceof Pair)) {
            return false;
        } else {
            Object qvalue = tr.matchQuoted((Pair) exp);
            if (!(qvalue instanceof SimpleSymbol)) {
                return false;
            }
            value = qvalue.toString();
        }
        if (tag == null || tag.equals(value)) {
            return true;
        }
        return false;
    }

    static long addAccessFlags(Object value, long previous, long allowed, String kind, Translator tr) {
        long flags = matchAccess(value, tr);
        if (flags == 0) {
            tr.error('e', "unknown access specifier " + value);
        } else if (((-1 ^ allowed) & flags) != 0) {
            tr.error('e', "invalid " + kind + " access specifier " + value);
        } else if ((previous & flags) != 0) {
            tr.error('w', "duplicate " + kind + " access specifiers " + value);
        }
        return previous | flags;
    }

    static long matchAccess(Object value, Translator tr) {
        while (value instanceof SyntaxForm) {
            value = ((SyntaxForm) value).getDatum();
        }
        if (value instanceof Pair) {
            Pair pair = (Pair) value;
            value = tr.matchQuoted((Pair) value);
            if (value instanceof Pair) {
                return matchAccess2((Pair) value, tr);
            }
        }
        return matchAccess1(value, tr);
    }

    private static long matchAccess2(Pair pair, Translator tr) {
        long icar = matchAccess1(pair.getCar(), tr);
        Object cdr = pair.getCdr();
        if (cdr == LList.Empty || icar == 0) {
            return icar;
        }
        if (cdr instanceof Pair) {
            long icdr = matchAccess2((Pair) cdr, tr);
            if (icdr != 0) {
                return icar | icdr;
            }
        }
        return 0;
    }

    private static long matchAccess1(Object value, Translator tr) {
        if (value instanceof Keyword) {
            value = ((Keyword) value).getName();
        } else if (value instanceof FString) {
            value = ((FString) value).toString();
        } else if (value instanceof SimpleSymbol) {
            value = value.toString();
        }
        if ("private".equals(value)) {
            return 16777216;
        }
        if ("protected".equals(value)) {
            return 33554432;
        }
        if ("public".equals(value)) {
            return 67108864;
        }
        if ("package".equals(value)) {
            return 134217728;
        }
        if ("volatile".equals(value)) {
            return Declaration.VOLATILE_ACCESS;
        }
        if ("transient".equals(value)) {
            return Declaration.TRANSIENT_ACCESS;
        }
        if ("enum".equals(value)) {
            return Declaration.ENUM_ACCESS;
        }
        if ("final".equals(value)) {
            return Declaration.FINAL_ACCESS;
        }
        return 0;
    }
}
