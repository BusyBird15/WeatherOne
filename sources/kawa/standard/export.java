package kawa.standard;

import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.ModuleExp;
import gnu.expr.ScopeExp;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Symbol;
import java.util.Vector;
import kawa.lang.Syntax;
import kawa.lang.SyntaxForm;
import kawa.lang.Translator;

public class export extends Syntax {
    public static final export export = new export();
    public static final export module_export = new export();

    static {
        module_export.setName("module-export");
        module_export.setName("export");
    }

    public boolean scanForDefinitions(Pair st, Vector forms, ScopeExp defs, Translator tr) {
        Object list = st.getCdr();
        Object savePos = tr.pushPositionOf(st);
        try {
            if (defs instanceof ModuleExp) {
                ((ModuleExp) defs).setFlag(16384);
                SyntaxForm restSyntax = null;
                while (list != LList.Empty) {
                    tr.pushPositionOf(list);
                    while (list instanceof SyntaxForm) {
                        restSyntax = (SyntaxForm) list;
                        list = restSyntax.getDatum();
                    }
                    SyntaxForm nameSyntax = restSyntax;
                    if (list instanceof Pair) {
                        Pair st2 = (Pair) list;
                        Object symbol = st2.getCar();
                        while (symbol instanceof SyntaxForm) {
                            nameSyntax = (SyntaxForm) symbol;
                            symbol = nameSyntax.getDatum();
                        }
                        boolean z = symbol instanceof String;
                        Object symbol2 = symbol;
                        if (z) {
                            String str = (String) symbol;
                            symbol2 = symbol;
                            if (str.startsWith("namespace:")) {
                                tr.error('w', "'namespace:' prefix ignored");
                                symbol2 = str.substring(10).intern();
                            }
                        }
                        if ((symbol2 instanceof String) || (symbol2 instanceof Symbol)) {
                            if (nameSyntax != null) {
                            }
                            Declaration decl = defs.getNoDefine(symbol2);
                            if (decl.getFlag(512)) {
                                Translator.setLine(decl, (Object) st2);
                            }
                            decl.setFlag(1024);
                            list = st2.getCdr();
                        }
                    }
                    tr.error('e', "invalid syntax in '" + getName() + '\'');
                    return false;
                }
                tr.popPositionOf(savePos);
                return true;
            }
            tr.error('e', "'" + getName() + "' not at module level");
            tr.popPositionOf(savePos);
            return true;
        } finally {
            tr.popPositionOf(savePos);
        }
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        return null;
    }
}
