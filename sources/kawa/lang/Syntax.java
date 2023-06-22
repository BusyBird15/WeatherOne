package kawa.lang;

import gnu.expr.ErrorExp;
import gnu.expr.Expression;
import gnu.expr.ScopeExp;
import gnu.lists.Consumer;
import gnu.lists.Pair;
import gnu.mapping.Named;
import gnu.mapping.Symbol;
import gnu.text.Printable;
import java.util.Vector;

public abstract class Syntax implements Printable, Named {
    Object name;

    public final String getName() {
        if (this.name == null) {
            return null;
        }
        return this.name instanceof Symbol ? ((Symbol) this.name).getName() : this.name.toString();
    }

    public Object getSymbol() {
        return this.name;
    }

    public void setName(Object name2) {
        this.name = name2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public Syntax() {
    }

    public Syntax(Object name2) {
        setName(name2);
    }

    public Expression rewrite(Object obj, Translator tr) {
        throw new InternalError("rewrite method not defined");
    }

    public Expression rewriteForm(Object form, Translator tr) {
        if (form instanceof Pair) {
            return rewriteForm((Pair) form, tr);
        }
        return tr.syntaxError("non-list form for " + this);
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        return rewrite(form.getCdr(), tr);
    }

    public void scanForm(Pair st, ScopeExp defs, Translator tr) {
        if (!scanForDefinitions(st, tr.formStack, defs, tr)) {
            tr.formStack.add(new ErrorExp("syntax error expanding " + this));
        }
    }

    public boolean scanForDefinitions(Pair st, Vector forms, ScopeExp defs, Translator tr) {
        forms.addElement(st);
        return true;
    }

    public void print(Consumer out) {
        out.write("#<syntax ");
        String name2 = getName();
        if (name2 == null) {
            name2 = "<unnamed>";
        }
        out.write(name2);
        out.write(62);
    }
}
