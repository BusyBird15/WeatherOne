package kawa.standard;

import gnu.bytecode.ClassType;
import gnu.bytecode.Method;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.LetExp;
import gnu.expr.PrimProcedure;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.ScopeExp;
import gnu.lists.Pair;
import gnu.math.IntNum;
import kawa.lang.Syntax;
import kawa.lang.SyntaxForm;
import kawa.lang.SyntaxPattern;
import kawa.lang.Translator;

public class syntax_case extends Syntax {
    public static final syntax_case syntax_case = new syntax_case();
    PrimProcedure call_error;

    static {
        syntax_case.setName("syntax-case");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: gnu.lists.LList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: gnu.lists.LList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v14, resolved type: gnu.lists.LList} */
    /* JADX WARNING: type inference failed for: r17v2, types: [gnu.expr.Expression] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression rewriteClauses(java.lang.Object r29, kawa.standard.syntax_case_work r30, kawa.lang.Translator r31) {
        /*
            r28 = this;
            gnu.expr.Language r13 = r31.getLanguage()
            gnu.lists.LList r25 = gnu.lists.LList.Empty
            r0 = r29
            r1 = r25
            if (r0 != r1) goto L_0x0077
            r25 = 2
            r0 = r25
            gnu.expr.Expression[] r4 = new gnu.expr.Expression[r0]
            r25 = 0
            gnu.expr.QuoteExp r26 = new gnu.expr.QuoteExp
            java.lang.String r27 = "syntax-case"
            r26.<init>(r27)
            r4[r25] = r26
            r25 = 1
            gnu.expr.ReferenceExp r26 = new gnu.expr.ReferenceExp
            r0 = r30
            gnu.expr.Declaration r0 = r0.inputExpression
            r27 = r0
            r26.<init>((gnu.expr.Declaration) r27)
            r4[r25] = r26
            r0 = r28
            gnu.expr.PrimProcedure r0 = r0.call_error
            r25 = r0
            if (r25 != 0) goto L_0x0069
            java.lang.String r25 = "kawa.standard.syntax_case"
            gnu.bytecode.ClassType r7 = gnu.bytecode.ClassType.make(r25)
            r25 = 2
            r0 = r25
            gnu.bytecode.Type[] r5 = new gnu.bytecode.Type[r0]
            r25 = 0
            gnu.bytecode.ClassType r26 = gnu.expr.Compilation.javaStringType
            r5[r25] = r26
            r25 = 1
            gnu.bytecode.ClassType r26 = gnu.bytecode.Type.objectType
            r5[r25] = r26
            java.lang.String r25 = "error"
            gnu.bytecode.ClassType r26 = gnu.bytecode.Type.objectType
            r27 = 9
            r0 = r25
            r1 = r26
            r2 = r27
            gnu.bytecode.Method r14 = r7.addMethod((java.lang.String) r0, (gnu.bytecode.Type[]) r5, (gnu.bytecode.Type) r1, (int) r2)
            gnu.expr.PrimProcedure r25 = new gnu.expr.PrimProcedure
            r0 = r25
            r0.<init>((gnu.bytecode.Method) r14, (gnu.expr.Language) r13)
            r0 = r25
            r1 = r28
            r1.call_error = r0
        L_0x0069:
            gnu.expr.ApplyExp r6 = new gnu.expr.ApplyExp
            r0 = r28
            gnu.expr.PrimProcedure r0 = r0.call_error
            r25 = r0
            r0 = r25
            r6.<init>((gnu.mapping.Procedure) r0, (gnu.expr.Expression[]) r4)
        L_0x0076:
            return r6
        L_0x0077:
            r0 = r31
            r1 = r29
            java.lang.Object r20 = r0.pushPositionOf(r1)
            r0 = r29
            boolean r0 = r0 instanceof gnu.lists.Pair     // Catch:{ all -> 0x018e }
            r25 = r0
            if (r25 == 0) goto L_0x0097
            r0 = r29
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ all -> 0x018e }
            r25 = r0
            java.lang.Object r8 = r25.getCar()     // Catch:{ all -> 0x018e }
            boolean r0 = r8 instanceof gnu.lists.Pair     // Catch:{ all -> 0x018e }
            r25 = r0
            if (r25 != 0) goto L_0x00a9
        L_0x0097:
            java.lang.String r25 = "syntax-case:  bad clause list"
            r0 = r31
            r1 = r25
            gnu.expr.Expression r6 = r0.syntaxError(r1)     // Catch:{ all -> 0x018e }
            r0 = r31
            r1 = r20
            r0.popPositionOf(r1)
            goto L_0x0076
        L_0x00a9:
            r0 = r8
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ all -> 0x018e }
            r18 = r0
            kawa.lang.PatternScope r9 = kawa.lang.PatternScope.push(r31)     // Catch:{ all -> 0x018e }
            r0 = r31
            gnu.expr.Declaration r0 = r0.matchArray     // Catch:{ all -> 0x018e }
            r25 = r0
            r0 = r25
            r9.matchArray = r0     // Catch:{ all -> 0x018e }
            r0 = r31
            r0.push((gnu.expr.ScopeExp) r9)     // Catch:{ all -> 0x018e }
            r21 = 0
            java.lang.Object r22 = r18.getCdr()     // Catch:{ all -> 0x018e }
        L_0x00c7:
            r0 = r22
            boolean r0 = r0 instanceof kawa.lang.SyntaxForm     // Catch:{ all -> 0x018e }
            r25 = r0
            if (r25 == 0) goto L_0x00da
            r0 = r22
            kawa.lang.SyntaxForm r0 = (kawa.lang.SyntaxForm) r0     // Catch:{ all -> 0x018e }
            r21 = r0
            java.lang.Object r22 = r21.getDatum()     // Catch:{ all -> 0x018e }
            goto L_0x00c7
        L_0x00da:
            r0 = r22
            boolean r0 = r0 instanceof gnu.lists.Pair     // Catch:{ all -> 0x018e }
            r25 = r0
            if (r25 != 0) goto L_0x00f4
            java.lang.String r25 = "missing syntax-case output expression"
            r0 = r31
            r1 = r25
            gnu.expr.Expression r6 = r0.syntaxError(r1)     // Catch:{ all -> 0x018e }
            r0 = r31
            r1 = r20
            r0.popPositionOf(r1)
            goto L_0x0076
        L_0x00f4:
            java.util.Vector r0 = r9.pattern_names     // Catch:{ all -> 0x018e }
            r25 = r0
            int r16 = r25.size()     // Catch:{ all -> 0x018e }
            kawa.lang.SyntaxPattern r19 = new kawa.lang.SyntaxPattern     // Catch:{ all -> 0x018e }
            java.lang.Object r25 = r18.getCar()     // Catch:{ all -> 0x018e }
            r0 = r30
            java.lang.Object[] r0 = r0.literal_identifiers     // Catch:{ all -> 0x018e }
            r26 = r0
            r0 = r19
            r1 = r25
            r2 = r26
            r3 = r31
            r0.<init>((java.lang.Object) r1, (java.lang.Object[]) r2, (kawa.lang.Translator) r3)     // Catch:{ all -> 0x018e }
            int r24 = r19.varCount()     // Catch:{ all -> 0x018e }
            r0 = r30
            int r0 = r0.maxVars     // Catch:{ all -> 0x018e }
            r25 = r0
            r0 = r24
            r1 = r25
            if (r0 <= r1) goto L_0x0129
            r0 = r24
            r1 = r30
            r1.maxVars = r0     // Catch:{ all -> 0x018e }
        L_0x0129:
            gnu.expr.BlockExp r6 = new gnu.expr.BlockExp     // Catch:{ all -> 0x018e }
            r6.<init>()     // Catch:{ all -> 0x018e }
            r25 = 4
            r0 = r25
            gnu.expr.Expression[] r4 = new gnu.expr.Expression[r0]     // Catch:{ all -> 0x018e }
            r25 = 0
            gnu.expr.QuoteExp r26 = new gnu.expr.QuoteExp     // Catch:{ all -> 0x018e }
            r0 = r26
            r1 = r19
            r0.<init>(r1)     // Catch:{ all -> 0x018e }
            r4[r25] = r26     // Catch:{ all -> 0x018e }
            r25 = 1
            gnu.expr.ReferenceExp r26 = new gnu.expr.ReferenceExp     // Catch:{ all -> 0x018e }
            r0 = r30
            gnu.expr.Declaration r0 = r0.inputExpression     // Catch:{ all -> 0x018e }
            r27 = r0
            r26.<init>((gnu.expr.Declaration) r27)     // Catch:{ all -> 0x018e }
            r4[r25] = r26     // Catch:{ all -> 0x018e }
            r25 = 2
            gnu.expr.ReferenceExp r26 = new gnu.expr.ReferenceExp     // Catch:{ all -> 0x018e }
            r0 = r31
            gnu.expr.Declaration r0 = r0.matchArray     // Catch:{ all -> 0x018e }
            r27 = r0
            r26.<init>((gnu.expr.Declaration) r27)     // Catch:{ all -> 0x018e }
            r4[r25] = r26     // Catch:{ all -> 0x018e }
            r25 = 3
            gnu.expr.QuoteExp r26 = new gnu.expr.QuoteExp     // Catch:{ all -> 0x018e }
            gnu.math.IntNum r27 = gnu.math.IntNum.zero()     // Catch:{ all -> 0x018e }
            r26.<init>(r27)     // Catch:{ all -> 0x018e }
            r4[r25] = r26     // Catch:{ all -> 0x018e }
            gnu.expr.ApplyExp r23 = new gnu.expr.ApplyExp     // Catch:{ all -> 0x018e }
            gnu.expr.PrimProcedure r25 = new gnu.expr.PrimProcedure     // Catch:{ all -> 0x018e }
            gnu.bytecode.Method r26 = kawa.lang.Pattern.matchPatternMethod     // Catch:{ all -> 0x018e }
            r0 = r25
            r1 = r26
            r0.<init>((gnu.bytecode.Method) r1, (gnu.expr.Language) r13)     // Catch:{ all -> 0x018e }
            r0 = r23
            r1 = r25
            r0.<init>((gnu.mapping.Procedure) r1, (gnu.expr.Expression[]) r4)     // Catch:{ all -> 0x018e }
            int r15 = r24 - r16
            gnu.expr.Expression[] r12 = new gnu.expr.Expression[r15]     // Catch:{ all -> 0x018e }
            r11 = r15
        L_0x0185:
            int r11 = r11 + -1
            if (r11 < 0) goto L_0x0197
            gnu.expr.QuoteExp r25 = gnu.expr.QuoteExp.undefined_exp     // Catch:{ all -> 0x018e }
            r12[r11] = r25     // Catch:{ all -> 0x018e }
            goto L_0x0185
        L_0x018e:
            r25 = move-exception
            r0 = r31
            r1 = r20
            r0.popPositionOf(r1)
            throw r25
        L_0x0197:
            r9.inits = r12     // Catch:{ all -> 0x018e }
            r0 = r22
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ all -> 0x018e }
            r18 = r0
            java.lang.Object r25 = r18.getCdr()     // Catch:{ all -> 0x018e }
            gnu.lists.LList r26 = gnu.lists.LList.Empty     // Catch:{ all -> 0x018e }
            r0 = r25
            r1 = r26
            if (r0 != r1) goto L_0x01f6
            r0 = r31
            r1 = r18
            r2 = r21
            gnu.expr.Expression r17 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)     // Catch:{ all -> 0x018e }
        L_0x01b5:
            r0 = r17
            r9.setBody(r0)     // Catch:{ all -> 0x018e }
            r0 = r31
            r0.pop(r9)     // Catch:{ all -> 0x018e }
            kawa.lang.PatternScope.pop(r31)     // Catch:{ all -> 0x018e }
            gnu.expr.IfExp r25 = new gnu.expr.IfExp     // Catch:{ all -> 0x018e }
            gnu.expr.ExitExp r26 = new gnu.expr.ExitExp     // Catch:{ all -> 0x018e }
            r0 = r26
            r0.<init>(r6)     // Catch:{ all -> 0x018e }
            r0 = r25
            r1 = r23
            r2 = r26
            r0.<init>(r1, r9, r2)     // Catch:{ all -> 0x018e }
            gnu.lists.Pair r29 = (gnu.lists.Pair) r29     // Catch:{ all -> 0x018e }
            java.lang.Object r26 = r29.getCdr()     // Catch:{ all -> 0x018e }
            r0 = r28
            r1 = r26
            r2 = r30
            r3 = r31
            gnu.expr.Expression r26 = r0.rewriteClauses(r1, r2, r3)     // Catch:{ all -> 0x018e }
            r0 = r25
            r1 = r26
            r6.setBody(r0, r1)     // Catch:{ all -> 0x018e }
            r0 = r31
            r1 = r20
            r0.popPositionOf(r1)
            goto L_0x0076
        L_0x01f6:
            r0 = r31
            r1 = r18
            r2 = r21
            gnu.expr.Expression r10 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)     // Catch:{ all -> 0x018e }
            java.lang.Object r25 = r18.getCdr()     // Catch:{ all -> 0x018e }
            r0 = r25
            boolean r0 = r0 instanceof gnu.lists.Pair     // Catch:{ all -> 0x018e }
            r25 = r0
            if (r25 == 0) goto L_0x021e
            java.lang.Object r18 = r18.getCdr()     // Catch:{ all -> 0x018e }
            gnu.lists.Pair r18 = (gnu.lists.Pair) r18     // Catch:{ all -> 0x018e }
            java.lang.Object r25 = r18.getCdr()     // Catch:{ all -> 0x018e }
            gnu.lists.LList r26 = gnu.lists.LList.Empty     // Catch:{ all -> 0x018e }
            r0 = r25
            r1 = r26
            if (r0 == r1) goto L_0x0231
        L_0x021e:
            java.lang.String r25 = "syntax-case:  bad clause"
            r0 = r31
            r1 = r25
            gnu.expr.Expression r6 = r0.syntaxError(r1)     // Catch:{ all -> 0x018e }
            r0 = r31
            r1 = r20
            r0.popPositionOf(r1)
            goto L_0x0076
        L_0x0231:
            gnu.expr.IfExp r17 = new gnu.expr.IfExp     // Catch:{ all -> 0x018e }
            r0 = r31
            r1 = r18
            r2 = r21
            gnu.expr.Expression r25 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)     // Catch:{ all -> 0x018e }
            gnu.expr.ExitExp r26 = new gnu.expr.ExitExp     // Catch:{ all -> 0x018e }
            r0 = r26
            r0.<init>(r6)     // Catch:{ all -> 0x018e }
            r0 = r17
            r1 = r25
            r2 = r26
            r0.<init>(r10, r1, r2)     // Catch:{ all -> 0x018e }
            goto L_0x01b5
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.syntax_case.rewriteClauses(java.lang.Object, kawa.standard.syntax_case_work, kawa.lang.Translator):gnu.expr.Expression");
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        syntax_case_work work = new syntax_case_work();
        Object obj = form.getCdr();
        if (!(obj instanceof Pair) || !(((Pair) obj).getCdr() instanceof Pair)) {
            return tr.syntaxError("insufficiant arguments to syntax-case");
        }
        Expression[] linits = new Expression[2];
        LetExp let = new LetExp(linits);
        work.inputExpression = let.addDeclaration((Object) null);
        Declaration matchArrayOuter = tr.matchArray;
        Declaration matchArray = let.addDeclaration((Object) null);
        matchArray.setType(Compilation.objArrayType);
        matchArray.setCanRead(true);
        tr.matchArray = matchArray;
        work.inputExpression.setCanRead(true);
        tr.push((ScopeExp) let);
        Pair form2 = (Pair) obj;
        linits[0] = tr.rewrite(form2.getCar());
        work.inputExpression.noteValue(linits[0]);
        Pair form3 = (Pair) form2.getCdr();
        work.literal_identifiers = SyntaxPattern.getLiteralsList(form3.getCar(), (SyntaxForm) null, tr);
        let.body = rewriteClauses(form3.getCdr(), work, tr);
        tr.pop(let);
        Method allocVars = ClassType.make("kawa.lang.SyntaxPattern").getDeclaredMethod("allocVars", 2);
        Expression[] args = new Expression[2];
        args[0] = new QuoteExp(IntNum.make(work.maxVars));
        if (matchArrayOuter == null) {
            args[1] = QuoteExp.nullExp;
        } else {
            args[1] = new ReferenceExp(matchArrayOuter);
        }
        linits[1] = new ApplyExp(allocVars, args);
        matchArray.noteValue(linits[1]);
        tr.matchArray = matchArrayOuter;
        return let;
    }

    public static Object error(String kind, Object arg) {
        Translator tr = (Translator) Compilation.getCurrent();
        if (tr == null) {
            throw new RuntimeException("no match in syntax-case");
        }
        Syntax syntax = tr.getCurrentSyntax();
        return tr.syntaxError("no matching case while expanding " + (syntax == null ? "some syntax" : syntax.getName()));
    }
}
