package gnu.commonlisp.lang;

import gnu.expr.Declaration;
import gnu.expr.ModuleExp;
import gnu.expr.ScopeExp;
import gnu.lists.Pair;
import gnu.mapping.Symbol;
import java.util.Vector;
import kawa.lang.Lambda;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class defun extends Syntax {
    Lambda lambdaSyntax;

    public defun(Lambda lambdaSyntax2) {
        this.lambdaSyntax = lambdaSyntax2;
    }

    public boolean scanForDefinitions(Pair st, Vector forms, ScopeExp defs, Translator tr) {
        if (st.getCdr() instanceof Pair) {
            Pair p = (Pair) st.getCdr();
            if ((p.getCar() instanceof String) || (p.getCar() instanceof Symbol)) {
                Object sym = p.getCar();
                Declaration decl = defs.lookup(sym);
                if (decl == null) {
                    decl = new Declaration(sym);
                    decl.setProcedureDecl(true);
                    defs.addDeclaration(decl);
                } else {
                    tr.error('w', "duplicate declaration for `" + sym + "'");
                }
                if (defs instanceof ModuleExp) {
                    decl.setCanRead(true);
                }
                forms.addElement(Translator.makePair(st, this, Translator.makePair(p, decl, p.getCdr())));
                return true;
            }
        }
        return super.scanForDefinitions(st, forms, defs, tr);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: gnu.expr.Declaration} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression rewriteForm(gnu.lists.Pair r16, kawa.lang.Translator r17) {
        /*
            r15 = this;
            java.lang.Object r9 = r16.getCdr()
            r8 = 0
            r14 = 0
            r7 = 0
            boolean r1 = r9 instanceof gnu.lists.Pair
            if (r1 == 0) goto L_0x007f
            r10 = r9
            gnu.lists.Pair r10 = (gnu.lists.Pair) r10
            java.lang.Object r11 = r10.getCar()
            boolean r1 = r11 instanceof gnu.mapping.Symbol
            if (r1 != 0) goto L_0x001a
            boolean r1 = r11 instanceof java.lang.String
            if (r1 == 0) goto L_0x0073
        L_0x001a:
            java.lang.String r8 = r11.toString()
        L_0x001e:
            if (r8 == 0) goto L_0x007f
            java.lang.Object r1 = r10.getCdr()
            boolean r1 = r1 instanceof gnu.lists.Pair
            if (r1 == 0) goto L_0x007f
            java.lang.Object r12 = r10.getCdr()
            gnu.lists.Pair r12 = (gnu.lists.Pair) r12
            gnu.expr.LambdaExp r2 = new gnu.expr.LambdaExp
            r2.<init>()
            kawa.lang.Lambda r1 = r15.lambdaSyntax
            java.lang.Object r3 = r12.getCar()
            java.lang.Object r4 = r12.getCdr()
            r6 = 0
            r5 = r17
            r1.rewrite(r2, r3, r4, r5, r6)
            r2.setSymbol(r8)
            boolean r1 = r12 instanceof gnu.lists.PairWithPosition
            if (r1 == 0) goto L_0x004f
            gnu.lists.PairWithPosition r12 = (gnu.lists.PairWithPosition) r12
            r2.setLocation(r12)
        L_0x004f:
            r14 = r2
            gnu.expr.SetExp r13 = new gnu.expr.SetExp
            r13.<init>((java.lang.Object) r8, (gnu.expr.Expression) r14)
            r1 = 1
            r13.setDefining(r1)
            r1 = 1
            r13.setFuncDef(r1)
            if (r7 == 0) goto L_0x0072
            r13.setBinding(r7)
            gnu.expr.ScopeExp r1 = r7.context
            boolean r1 = r1 instanceof gnu.expr.ModuleExp
            if (r1 == 0) goto L_0x006f
            boolean r1 = r7.getCanWrite()
            if (r1 == 0) goto L_0x006f
            r14 = 0
        L_0x006f:
            r7.noteValue(r14)
        L_0x0072:
            return r13
        L_0x0073:
            boolean r1 = r11 instanceof gnu.expr.Declaration
            if (r1 == 0) goto L_0x001e
            r7 = r11
            gnu.expr.Declaration r7 = (gnu.expr.Declaration) r7
            java.lang.Object r8 = r7.getSymbol()
            goto L_0x001e
        L_0x007f:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r3 = "invalid syntax for "
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r3 = r15.getName()
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r0 = r17
            gnu.expr.Expression r13 = r0.syntaxError(r1)
            goto L_0x0072
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.commonlisp.lang.defun.rewriteForm(gnu.lists.Pair, kawa.lang.Translator):gnu.expr.Expression");
    }
}
