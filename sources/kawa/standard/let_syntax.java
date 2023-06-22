package kawa.standard;

import gnu.expr.Declaration;
import gnu.expr.LetExp;
import gnu.expr.ScopeExp;
import java.util.Stack;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class let_syntax extends Syntax {
    public static final let_syntax let_syntax = new let_syntax(false, "let-syntax");
    public static final let_syntax letrec_syntax = new let_syntax(true, "letrec-syntax");
    boolean recursive;

    public let_syntax(boolean recursive2, String name) {
        super(name);
        this.recursive = recursive2;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v14, resolved type: gnu.lists.LList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r23v2, resolved type: kawa.lang.SyntaxForm} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression rewrite(java.lang.Object r36, kawa.lang.Translator r37) {
        /*
            r35 = this;
            r0 = r36
            boolean r0 = r0 instanceof gnu.lists.Pair
            r33 = r0
            if (r33 != 0) goto L_0x0013
            java.lang.String r33 = "missing let-syntax arguments"
            r0 = r37
            r1 = r33
            gnu.expr.Expression r27 = r0.syntaxError(r1)
        L_0x0012:
            return r27
        L_0x0013:
            r24 = r36
            gnu.lists.Pair r24 = (gnu.lists.Pair) r24
            java.lang.Object r10 = r24.getCar()
            java.lang.Object r11 = r24.getCdr()
            int r13 = kawa.lang.Translator.listLength(r10)
            if (r13 >= 0) goto L_0x0030
            java.lang.String r33 = "bindings not a proper list"
            r0 = r37
            r1 = r33
            gnu.expr.Expression r27 = r0.syntaxError(r1)
            goto L_0x0012
        L_0x0030:
            r25 = 0
            r26 = 0
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r13]
            r16 = r0
            gnu.expr.Declaration[] r14 = new gnu.expr.Declaration[r13]
            kawa.lang.Macro[] r0 = new kawa.lang.Macro[r13]
            r21 = r0
            gnu.lists.Pair[] r0 = new gnu.lists.Pair[r13]
            r31 = r0
            kawa.lang.SyntaxForm[] r0 = new kawa.lang.SyntaxForm[r13]
            r30 = r0
            gnu.expr.LetExp r17 = new gnu.expr.LetExp
            r0 = r17
            r1 = r16
            r0.<init>(r1)
            r18 = 0
            r15 = 0
        L_0x0052:
            if (r15 >= r13) goto L_0x01d0
        L_0x0054:
            boolean r0 = r10 instanceof kawa.lang.SyntaxForm
            r33 = r0
            if (r33 == 0) goto L_0x0063
            r18 = r10
            kawa.lang.SyntaxForm r18 = (kawa.lang.SyntaxForm) r18
            java.lang.Object r10 = r18.getDatum()
            goto L_0x0054
        L_0x0063:
            r8 = r18
            r5 = r10
            gnu.lists.Pair r5 = (gnu.lists.Pair) r5
            java.lang.Object r6 = r5.getCar()
            boolean r0 = r6 instanceof kawa.lang.SyntaxForm
            r33 = r0
            if (r33 == 0) goto L_0x0079
            r8 = r6
            kawa.lang.SyntaxForm r8 = (kawa.lang.SyntaxForm) r8
            java.lang.Object r6 = r8.getDatum()
        L_0x0079:
            boolean r0 = r6 instanceof gnu.lists.Pair
            r33 = r0
            if (r33 != 0) goto L_0x00a0
            java.lang.StringBuilder r33 = new java.lang.StringBuilder
            r33.<init>()
            java.lang.String r34 = r35.getName()
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r34 = " binding is not a pair"
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r33 = r33.toString()
            r0 = r37
            r1 = r33
            gnu.expr.Expression r27 = r0.syntaxError(r1)
            goto L_0x0012
        L_0x00a0:
            r7 = r6
            gnu.lists.Pair r7 = (gnu.lists.Pair) r7
            java.lang.Object r22 = r7.getCar()
            r23 = r8
        L_0x00a9:
            r0 = r22
            boolean r0 = r0 instanceof kawa.lang.SyntaxForm
            r33 = r0
            if (r33 == 0) goto L_0x00ba
            r23 = r22
            kawa.lang.SyntaxForm r23 = (kawa.lang.SyntaxForm) r23
            java.lang.Object r22 = r23.getDatum()
            goto L_0x00a9
        L_0x00ba:
            r0 = r22
            boolean r0 = r0 instanceof java.lang.String
            r33 = r0
            if (r33 != 0) goto L_0x00f1
            r0 = r22
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r33 = r0
            if (r33 != 0) goto L_0x00f1
            java.lang.StringBuilder r33 = new java.lang.StringBuilder
            r33.<init>()
            java.lang.String r34 = "variable in "
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r34 = r35.getName()
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r34 = " binding is not a symbol"
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r33 = r33.toString()
            r0 = r37
            r1 = r33
            gnu.expr.Expression r27 = r0.syntaxError(r1)
            goto L_0x0012
        L_0x00f1:
            java.lang.Object r9 = r7.getCdr()
        L_0x00f5:
            boolean r0 = r9 instanceof kawa.lang.SyntaxForm
            r33 = r0
            if (r33 == 0) goto L_0x0103
            r8 = r9
            kawa.lang.SyntaxForm r8 = (kawa.lang.SyntaxForm) r8
            java.lang.Object r9 = r8.getDatum()
            goto L_0x00f5
        L_0x0103:
            boolean r0 = r9 instanceof gnu.lists.Pair
            r33 = r0
            if (r33 != 0) goto L_0x0138
            java.lang.StringBuilder r33 = new java.lang.StringBuilder
            r33.<init>()
            java.lang.String r34 = r35.getName()
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r34 = " has no value for '"
            java.lang.StringBuilder r33 = r33.append(r34)
            r0 = r33
            r1 = r22
            java.lang.StringBuilder r33 = r0.append(r1)
            java.lang.String r34 = "'"
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r33 = r33.toString()
            r0 = r37
            r1 = r33
            gnu.expr.Expression r27 = r0.syntaxError(r1)
            goto L_0x0012
        L_0x0138:
            r7 = r9
            gnu.lists.Pair r7 = (gnu.lists.Pair) r7
            java.lang.Object r33 = r7.getCdr()
            gnu.lists.LList r34 = gnu.lists.LList.Empty
            r0 = r33
            r1 = r34
            if (r0 == r1) goto L_0x016e
            java.lang.StringBuilder r33 = new java.lang.StringBuilder
            r33.<init>()
            java.lang.String r34 = "let binding for '"
            java.lang.StringBuilder r33 = r33.append(r34)
            r0 = r33
            r1 = r22
            java.lang.StringBuilder r33 = r0.append(r1)
            java.lang.String r34 = "' is improper list"
            java.lang.StringBuilder r33 = r33.append(r34)
            java.lang.String r33 = r33.toString()
            r0 = r37
            r1 = r33
            gnu.expr.Expression r27 = r0.syntaxError(r1)
            goto L_0x0012
        L_0x016e:
            gnu.expr.Declaration r12 = new gnu.expr.Declaration
            r0 = r22
            r12.<init>((java.lang.Object) r0)
            kawa.lang.Macro r20 = kawa.lang.Macro.make(r12)
            r21[r15] = r20
            r31[r15] = r7
            r30[r15] = r8
            r0 = r17
            r0.addDeclaration((gnu.expr.Declaration) r12)
            if (r23 != 0) goto L_0x01bb
            r29 = 0
        L_0x0188:
            if (r29 == 0) goto L_0x01a0
            r0 = r37
            r1 = r29
            gnu.expr.Declaration r4 = r0.makeRenamedAlias(r12, r1)
            if (r25 != 0) goto L_0x0199
            java.util.Stack r25 = new java.util.Stack
            r25.<init>()
        L_0x0199:
            r0 = r25
            r0.push(r4)
            int r26 = r26 + 1
        L_0x01a0:
            if (r8 == 0) goto L_0x01c0
            kawa.lang.TemplateScope r33 = r8.getScope()
        L_0x01a6:
            r0 = r20
            r1 = r33
            r0.setCapturedScope(r1)
            r14[r15] = r12
            gnu.expr.QuoteExp r33 = gnu.expr.QuoteExp.nullExp
            r16[r15] = r33
            java.lang.Object r10 = r5.getCdr()
            int r15 = r15 + 1
            goto L_0x0052
        L_0x01bb:
            kawa.lang.TemplateScope r29 = r23.getScope()
            goto L_0x0188
        L_0x01c0:
            r0 = r35
            boolean r0 = r0.recursive
            r33 = r0
            if (r33 == 0) goto L_0x01cb
            r33 = r17
            goto L_0x01a6
        L_0x01cb:
            gnu.expr.ScopeExp r33 = r37.currentScope()
            goto L_0x01a6
        L_0x01d0:
            r0 = r35
            boolean r0 = r0.recursive
            r33 = r0
            if (r33 == 0) goto L_0x01e3
            r0 = r35
            r1 = r17
            r2 = r37
            r3 = r25
            r0.push(r1, r2, r3)
        L_0x01e3:
            r0 = r37
            kawa.lang.Macro r0 = r0.currentMacroDefinition
            r28 = r0
            r15 = 0
        L_0x01ea:
            if (r15 >= r13) goto L_0x0238
            r20 = r21[r15]
            r0 = r20
            r1 = r37
            r1.currentMacroDefinition = r0
            r33 = r31[r15]
            r34 = r30[r15]
            r0 = r37
            r1 = r33
            r2 = r34
            gnu.expr.Expression r32 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            r16[r15] = r32
            r12 = r14[r15]
            r0 = r32
            r1 = r20
            r1.expander = r0
            gnu.expr.QuoteExp r33 = new gnu.expr.QuoteExp
            r0 = r33
            r1 = r20
            r0.<init>(r1)
            r0 = r33
            r12.noteValue(r0)
            r0 = r32
            boolean r0 = r0 instanceof gnu.expr.LambdaExp
            r33 = r0
            if (r33 == 0) goto L_0x0235
            r19 = r32
            gnu.expr.LambdaExp r19 = (gnu.expr.LambdaExp) r19
            r0 = r19
            r0.nameDecl = r12
            java.lang.Object r33 = r12.getSymbol()
            r0 = r19
            r1 = r33
            r0.setSymbol(r1)
        L_0x0235:
            int r15 = r15 + 1
            goto L_0x01ea
        L_0x0238:
            r0 = r28
            r1 = r37
            r1.currentMacroDefinition = r0
            r0 = r35
            boolean r0 = r0.recursive
            r33 = r0
            if (r33 != 0) goto L_0x0251
            r0 = r35
            r1 = r17
            r2 = r37
            r3 = r25
            r0.push(r1, r2, r3)
        L_0x0251:
            r0 = r37
            gnu.expr.Expression r27 = r0.rewrite_body(r11)
            r0 = r37
            r1 = r17
            r0.pop(r1)
            r0 = r37
            r1 = r26
            r0.popRenamedAlias(r1)
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.let_syntax.rewrite(java.lang.Object, kawa.lang.Translator):gnu.expr.Expression");
    }

    private void push(LetExp let, Translator tr, Stack renamedAliases) {
        tr.push((ScopeExp) let);
        if (renamedAliases != null) {
            int i = renamedAliases.size();
            while (true) {
                i--;
                if (i >= 0) {
                    tr.pushRenamedAlias((Declaration) renamedAliases.pop());
                } else {
                    return;
                }
            }
        }
    }
}
