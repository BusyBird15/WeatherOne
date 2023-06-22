package kawa.standard;

import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.LetExp;
import gnu.expr.ScopeExp;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Symbol;
import java.util.Stack;
import kawa.lang.Syntax;
import kawa.lang.SyntaxForm;
import kawa.lang.TemplateScope;
import kawa.lang.Translator;

public class let extends Syntax {
    public static final let let = new let();

    static {
        let.setName("let");
    }

    public Expression rewrite(Object obj, Translator tr) {
        TemplateScope templateScope;
        Pair init;
        if (!(obj instanceof Pair)) {
            return tr.syntaxError("missing let arguments");
        }
        Pair pair = (Pair) obj;
        Object bindings = pair.getCar();
        Object body = pair.getCdr();
        int decl_count = Translator.listLength(bindings);
        if (decl_count < 0) {
            return tr.syntaxError("bindings not a proper list");
        }
        Expression[] inits = new Expression[decl_count];
        LetExp letExp = new LetExp(inits);
        Stack renamedAliases = null;
        int renamedAliasesCount = 0;
        SyntaxForm syntaxRest = null;
        for (int i = 0; i < decl_count; i++) {
            while (bindings instanceof SyntaxForm) {
                syntaxRest = (SyntaxForm) bindings;
                bindings = syntaxRest.getDatum();
            }
            Pair bind_pair = (Pair) bindings;
            Object bind_pair_car = bind_pair.getCar();
            SyntaxForm syntax = syntaxRest;
            boolean z = bind_pair_car instanceof SyntaxForm;
            Object bind_pair_car2 = bind_pair_car;
            if (z) {
                syntax = bind_pair_car;
                bind_pair_car2 = syntax.getDatum();
            }
            if (!(bind_pair_car2 instanceof Pair)) {
                return tr.syntaxError("let binding is not a pair:" + bind_pair_car2);
            }
            Pair binding = (Pair) bind_pair_car2;
            Object name = binding.getCar();
            if (name instanceof SyntaxForm) {
                SyntaxForm sf = (SyntaxForm) name;
                name = sf.getDatum();
                templateScope = sf.getScope();
            } else {
                templateScope = syntax == null ? null : syntax.getScope();
            }
            Object name2 = tr.namespaceResolve(name);
            if (!(name2 instanceof Symbol)) {
                return tr.syntaxError("variable " + name2 + " in let binding is not a symbol: " + obj);
            }
            Declaration decl = letExp.addDeclaration(name2);
            decl.setFlag(262144);
            if (templateScope != null) {
                Declaration alias = tr.makeRenamedAlias(decl, templateScope);
                if (renamedAliases == null) {
                    renamedAliases = new Stack();
                }
                renamedAliases.push(alias);
                renamedAliasesCount++;
            }
            Object binding_cdr = binding.getCdr();
            while (binding_cdr instanceof SyntaxForm) {
                syntax = (SyntaxForm) binding_cdr;
                binding_cdr = syntax.getDatum();
            }
            if (!(binding_cdr instanceof Pair)) {
                return tr.syntaxError("let has no value for '" + name2 + "'");
            }
            Pair binding2 = (Pair) binding_cdr;
            Object binding_cdr2 = binding2.getCdr();
            while (binding_cdr2 instanceof SyntaxForm) {
                syntax = (SyntaxForm) binding_cdr2;
                binding_cdr2 = syntax.getDatum();
            }
            Object binding_cdr3 = binding_cdr2;
            if (tr.matches(binding2.getCar(), "::")) {
                if (!(binding_cdr2 instanceof Pair) || (binding2 = (Pair) binding_cdr2).getCdr() == LList.Empty) {
                    return tr.syntaxError("missing type after '::' in let");
                }
                Object binding_cdr4 = binding2.getCdr();
                while (binding_cdr4 instanceof SyntaxForm) {
                    syntax = (SyntaxForm) binding_cdr4;
                    binding_cdr4 = syntax.getDatum();
                }
                binding_cdr3 = binding_cdr4;
            }
            if (binding_cdr3 == LList.Empty) {
                init = binding2;
            } else if (!(binding_cdr3 instanceof Pair)) {
                return tr.syntaxError("let binding for '" + name2 + "' is improper list");
            } else {
                decl.setType(tr.exp2Type(binding2));
                decl.setFlag(8192);
                init = (Pair) binding_cdr3;
            }
            inits[i] = tr.rewrite_car(init, syntax);
            if (init.getCdr() != LList.Empty) {
                return tr.syntaxError("junk after declaration of " + name2);
            }
            decl.noteValue(inits[i]);
            bindings = bind_pair.getCdr();
        }
        int i2 = renamedAliasesCount;
        while (true) {
            i2--;
            if (i2 >= 0) {
                tr.pushRenamedAlias((Declaration) renamedAliases.pop());
            } else {
                tr.push((ScopeExp) letExp);
                letExp.body = tr.rewrite_body(body);
                tr.pop(letExp);
                tr.popRenamedAlias(renamedAliasesCount);
                return letExp;
            }
        }
    }
}
