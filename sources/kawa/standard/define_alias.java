package kawa.standard;

import com.google.appinventor.components.runtime.repackaged.org.json.zip.JSONzip;
import gnu.bytecode.ClassType;
import gnu.expr.ClassExp;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.ModuleExp;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.ScopeExp;
import gnu.expr.SetExp;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Symbol;
import java.util.Vector;
import kawa.lang.Syntax;
import kawa.lang.SyntaxForm;
import kawa.lang.Translator;

public class define_alias extends Syntax {
    public static final define_alias define_alias = new define_alias();

    static {
        define_alias.setName("define-alias");
    }

    public boolean scanForDefinitions(Pair st, Vector forms, ScopeExp defs, Translator tr) {
        Object formCdr = st.getCdr();
        SyntaxForm formSyntax = null;
        while (formCdr instanceof SyntaxForm) {
            formSyntax = (SyntaxForm) formCdr;
            formCdr = formSyntax.getDatum();
        }
        if (formCdr instanceof Pair) {
            Pair p = (Pair) formCdr;
            SyntaxForm nameSyntax = formSyntax;
            Object name = p.getCar();
            while (name instanceof SyntaxForm) {
                nameSyntax = (SyntaxForm) name;
                name = nameSyntax.getDatum();
            }
            Object formCdr2 = p.getCdr();
            while (formCdr2 instanceof SyntaxForm) {
                formSyntax = (SyntaxForm) formCdr2;
                formCdr2 = formSyntax.getDatum();
            }
            if (((name instanceof String) || (name instanceof Symbol)) && (formCdr2 instanceof Pair)) {
                Pair p2 = (Pair) formCdr2;
                if (p2.getCdr() == LList.Empty) {
                    Declaration decl = tr.define(name, nameSyntax, defs);
                    decl.setIndirectBinding(true);
                    decl.setAlias(true);
                    Expression arg = tr.rewrite_car(p2, formSyntax);
                    if (arg instanceof ReferenceExp) {
                        ReferenceExp rarg = (ReferenceExp) arg;
                        Declaration d = Declaration.followAliases(rarg.getBinding());
                        if (d != null) {
                            Expression dval = d.getValue();
                            if ((dval instanceof ClassExp) || (dval instanceof ModuleExp)) {
                                decl.setIndirectBinding(false);
                                decl.setFlag(JSONzip.int14);
                            }
                        }
                        rarg.setDontDereference(true);
                    } else if (arg instanceof QuoteExp) {
                        decl.setIndirectBinding(false);
                        decl.setFlag(JSONzip.int14);
                    } else {
                        arg = location.rewrite(arg, tr);
                        decl.setType(ClassType.make("gnu.mapping.Location"));
                    }
                    tr.mustCompileHere();
                    tr.push(decl);
                    SetExp sexp = new SetExp(decl, arg);
                    tr.setLineOf(sexp);
                    decl.noteValue(arg);
                    sexp.setDefining(true);
                    forms.addElement(sexp);
                    return true;
                }
            }
        }
        tr.error('e', "invalid syntax for define-alias");
        return false;
    }

    public Expression rewrite(Object obj, Translator tr) {
        return tr.syntaxError("define-alias is only allowed in a <body>");
    }
}
