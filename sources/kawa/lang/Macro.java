package kawa.lang;

import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.ModuleExp;
import gnu.expr.ModuleInfo;
import gnu.expr.QuoteExp;
import gnu.expr.ScopeExp;
import gnu.lists.Consumer;
import gnu.lists.Pair;
import gnu.mapping.Procedure;
import gnu.text.Printable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Macro extends Syntax implements Printable, Externalizable {
    private ScopeExp capturedScope;
    public Object expander;
    private boolean hygienic = true;
    Object instance;

    public ScopeExp getCapturedScope() {
        if (this.capturedScope == null) {
            if (this.instance instanceof ModuleExp) {
                this.capturedScope = (ModuleExp) this.instance;
            } else if (this.instance != null) {
                this.capturedScope = ModuleInfo.findFromInstance(this.instance).getModuleExp();
            }
        }
        return this.capturedScope;
    }

    public void setCapturedScope(ScopeExp scope) {
        this.capturedScope = scope;
    }

    public static Macro make(Declaration decl) {
        Macro mac = new Macro(decl.getSymbol());
        decl.setSyntax();
        mac.capturedScope = decl.context;
        return mac;
    }

    public static Macro makeNonHygienic(Object name, Procedure expander2) {
        Macro mac = new Macro(name, expander2);
        mac.hygienic = false;
        return mac;
    }

    public static Macro makeNonHygienic(Object name, Procedure expander2, Object instance2) {
        Macro mac = new Macro(name, expander2);
        mac.hygienic = false;
        mac.instance = instance2;
        return mac;
    }

    public static Macro make(Object name, Procedure expander2) {
        return new Macro(name, expander2);
    }

    public static Macro make(Object name, Procedure expander2, Object instance2) {
        Macro mac = new Macro(name, expander2);
        mac.instance = instance2;
        return mac;
    }

    public final boolean isHygienic() {
        return this.hygienic;
    }

    public final void setHygienic(boolean hygienic2) {
        this.hygienic = hygienic2;
    }

    public Macro() {
    }

    public Macro(Macro old) {
        this.name = old.name;
        this.expander = old.expander;
        this.hygienic = old.hygienic;
    }

    public Macro(Object name, Procedure expander2) {
        super(name);
        this.expander = new QuoteExp(expander2);
    }

    public Macro(Object name) {
        super(name);
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        return tr.rewrite(expand(form, tr));
    }

    public Expression rewriteForm(Object form, Translator tr) {
        return tr.rewrite(expand(form, tr));
    }

    public String toString() {
        return "#<macro " + getName() + '>';
    }

    public void print(Consumer out) {
        out.write("#<macro ");
        out.write(getName());
        out.write(62);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object expand(java.lang.Object r15, kawa.lang.Translator r16) {
        /*
            r14 = this;
            java.lang.Object r3 = r14.expander     // Catch:{ Throwable -> 0x0061 }
            boolean r11 = r3 instanceof gnu.mapping.Procedure     // Catch:{ Throwable -> 0x0061 }
            if (r11 == 0) goto L_0x0036
            boolean r11 = r3 instanceof gnu.expr.Expression     // Catch:{ Throwable -> 0x0061 }
            if (r11 != 0) goto L_0x0036
            r0 = r3
            gnu.mapping.Procedure r0 = (gnu.mapping.Procedure) r0     // Catch:{ Throwable -> 0x0061 }
            r8 = r0
        L_0x000e:
            boolean r11 = r14.hygienic     // Catch:{ Throwable -> 0x0061 }
            if (r11 != 0) goto L_0x00cd
            java.lang.Object r15 = kawa.lang.Quote.quote(r15, r16)     // Catch:{ Throwable -> 0x0061 }
            int r5 = kawa.lang.Translator.listLength(r15)     // Catch:{ Throwable -> 0x0061 }
            if (r5 > 0) goto L_0x008a
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0061 }
            r11.<init>()     // Catch:{ Throwable -> 0x0061 }
            java.lang.String r12 = "invalid macro argument list to "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Throwable -> 0x0061 }
            java.lang.StringBuilder r11 = r11.append(r14)     // Catch:{ Throwable -> 0x0061 }
            java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x0061 }
            r0 = r16
            gnu.expr.Expression r9 = r0.syntaxError(r11)     // Catch:{ Throwable -> 0x0061 }
        L_0x0035:
            return r9
        L_0x0036:
            boolean r11 = r3 instanceof gnu.expr.Expression     // Catch:{ Throwable -> 0x0061 }
            if (r11 != 0) goto L_0x004e
            r0 = r16
            kawa.lang.Macro r10 = r0.currentMacroDefinition     // Catch:{ Throwable -> 0x0061 }
            r0 = r16
            r0.currentMacroDefinition = r14     // Catch:{ Throwable -> 0x0061 }
            r0 = r16
            gnu.expr.Expression r3 = r0.rewrite(r3)     // Catch:{ all -> 0x005b }
            r14.expander = r3     // Catch:{ all -> 0x005b }
            r0 = r16
            r0.currentMacroDefinition = r10     // Catch:{ Throwable -> 0x0061 }
        L_0x004e:
            gnu.expr.Expression r3 = (gnu.expr.Expression) r3     // Catch:{ Throwable -> 0x0061 }
            gnu.mapping.Environment r11 = r16.getGlobalEnvironment()     // Catch:{ Throwable -> 0x0061 }
            java.lang.Object r8 = r3.eval((gnu.mapping.Environment) r11)     // Catch:{ Throwable -> 0x0061 }
            gnu.mapping.Procedure r8 = (gnu.mapping.Procedure) r8     // Catch:{ Throwable -> 0x0061 }
            goto L_0x000e
        L_0x005b:
            r11 = move-exception
            r0 = r16
            r0.currentMacroDefinition = r10     // Catch:{ Throwable -> 0x0061 }
            throw r11     // Catch:{ Throwable -> 0x0061 }
        L_0x0061:
            r2 = move-exception
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "evaluating syntax transformer '"
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = r14.getName()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = "' threw "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r2)
            java.lang.String r11 = r11.toString()
            r0 = r16
            gnu.expr.Expression r9 = r0.syntaxError(r11)
            goto L_0x0035
        L_0x008a:
            int r11 = r5 + -1
            java.lang.Object[] r1 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x0061 }
            r4 = 0
        L_0x008f:
            if (r4 >= r5) goto L_0x00a6
            r0 = r15
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ Throwable -> 0x0061 }
            r7 = r0
            if (r4 <= 0) goto L_0x009f
            int r11 = r4 + -1
            java.lang.Object r12 = r7.getCar()     // Catch:{ Throwable -> 0x0061 }
            r1[r11] = r12     // Catch:{ Throwable -> 0x0061 }
        L_0x009f:
            java.lang.Object r15 = r7.getCdr()     // Catch:{ Throwable -> 0x0061 }
            int r4 = r4 + 1
            goto L_0x008f
        L_0x00a6:
            java.lang.Object r9 = r8.applyN(r1)     // Catch:{ Throwable -> 0x0061 }
        L_0x00aa:
            boolean r11 = r15 instanceof gnu.lists.PairWithPosition     // Catch:{ Throwable -> 0x0061 }
            if (r11 == 0) goto L_0x0035
            boolean r11 = r9 instanceof gnu.lists.Pair     // Catch:{ Throwable -> 0x0061 }
            if (r11 == 0) goto L_0x0035
            boolean r11 = r9 instanceof gnu.lists.PairWithPosition     // Catch:{ Throwable -> 0x0061 }
            if (r11 != 0) goto L_0x0035
            r0 = r9
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ Throwable -> 0x0061 }
            r6 = r0
            gnu.lists.PairWithPosition r9 = new gnu.lists.PairWithPosition     // Catch:{ Throwable -> 0x0061 }
            r0 = r15
            gnu.lists.PairWithPosition r0 = (gnu.lists.PairWithPosition) r0     // Catch:{ Throwable -> 0x0061 }
            r11 = r0
            java.lang.Object r12 = r6.getCar()     // Catch:{ Throwable -> 0x0061 }
            java.lang.Object r13 = r6.getCdr()     // Catch:{ Throwable -> 0x0061 }
            r9.<init>(r11, r12, r13)     // Catch:{ Throwable -> 0x0061 }
            goto L_0x0035
        L_0x00cd:
            java.lang.Object r9 = r8.apply1(r15)     // Catch:{ Throwable -> 0x0061 }
            goto L_0x00aa
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Macro.expand(java.lang.Object, kawa.lang.Translator):java.lang.Object");
    }

    public void scanForm(Pair st, ScopeExp defs, Translator tr) {
        String save_filename = tr.getFileName();
        int save_line = tr.getLineNumber();
        int save_column = tr.getColumnNumber();
        Syntax saveSyntax = tr.currentSyntax;
        try {
            tr.setLine((Object) st);
            tr.currentSyntax = this;
            tr.scanForm(expand(st, tr), defs);
        } finally {
            tr.setLine(save_filename, save_line, save_column);
            tr.currentSyntax = saveSyntax;
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getName());
        out.writeObject(((QuoteExp) this.expander).getValue());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setName((String) in.readObject());
        this.expander = new QuoteExp(in.readObject());
    }
}
