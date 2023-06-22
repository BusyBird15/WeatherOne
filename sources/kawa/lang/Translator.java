package kawa.lang;

import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.ErrorExp;
import gnu.expr.Expression;
import gnu.expr.Keyword;
import gnu.expr.LambdaExp;
import gnu.expr.Language;
import gnu.expr.LetExp;
import gnu.expr.ModuleExp;
import gnu.expr.ModuleInfo;
import gnu.expr.NameLookup;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.ScopeExp;
import gnu.expr.Special;
import gnu.kawa.functions.AppendValues;
import gnu.kawa.functions.CompileNamedPart;
import gnu.kawa.lispexpr.LispLanguage;
import gnu.kawa.reflect.StaticFieldLocation;
import gnu.kawa.xml.MakeAttribute;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.mapping.Environment;
import gnu.mapping.EnvironmentKey;
import gnu.mapping.Namespace;
import gnu.mapping.Procedure;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.text.SourceLocator;
import gnu.text.SourceMessages;
import gnu.xml.NamespaceBinding;
import java.util.Stack;
import java.util.Vector;
import kawa.standard.begin;
import kawa.standard.require;

public class Translator extends Compilation {
    private static Expression errorExp = new ErrorExp("unknown syntax error");
    public static final Declaration getNamedPartDecl = Declaration.getDeclarationFromStatic("gnu.kawa.functions.GetNamedPart", "getNamedPart");
    public LambdaExp curMethodLambda;
    public Macro currentMacroDefinition;
    Syntax currentSyntax;
    private Environment env = Environment.getCurrent();
    public int firstForm;
    public Stack formStack = new Stack();
    Declaration macroContext;
    public Declaration matchArray;
    Vector notedAccess;
    public PatternScope patternScope;
    public Object pendingForm;
    PairWithPosition positionPair;
    Stack renamedAliasStack;
    public Declaration templateScopeDecl;
    public NamespaceBinding xmlElementNamespaces = NamespaceBinding.predefinedXML;

    static {
        LispLanguage.getNamedPartLocation.setDeclaration(getNamedPartDecl);
    }

    public Translator(Language language, SourceMessages messages, NameLookup lexical) {
        super(language, messages, lexical);
    }

    public final Environment getGlobalEnvironment() {
        return this.env;
    }

    public Expression parse(Object input) {
        return rewrite(input);
    }

    /* JADX INFO: finally extract failed */
    public final Expression rewrite_car(Pair pair, SyntaxForm syntax) {
        if (syntax == null || syntax.getScope() == this.current_scope || (pair.getCar() instanceof SyntaxForm)) {
            return rewrite_car(pair, false);
        }
        ScopeExp save_scope = this.current_scope;
        try {
            setCurrentScope(syntax.getScope());
            Expression rewrite_car = rewrite_car(pair, false);
            setCurrentScope(save_scope);
            return rewrite_car;
        } catch (Throwable th) {
            setCurrentScope(save_scope);
            throw th;
        }
    }

    public final Expression rewrite_car(Pair pair, boolean function) {
        Object car = pair.getCar();
        if (pair instanceof PairWithPosition) {
            return rewrite_with_position(car, function, (PairWithPosition) pair);
        }
        return rewrite(car, function);
    }

    public Syntax getCurrentSyntax() {
        return this.currentSyntax;
    }

    /* access modifiers changed from: package-private */
    public Expression apply_rewrite(Syntax syntax, Pair form) {
        Expression expression = errorExp;
        Syntax saveSyntax = this.currentSyntax;
        this.currentSyntax = syntax;
        try {
            return syntax.rewriteForm(form, this);
        } finally {
            this.currentSyntax = saveSyntax;
        }
    }

    static ReferenceExp getOriginalRef(Declaration decl) {
        if (decl != null && decl.isAlias() && !decl.isIndirectBinding()) {
            Expression value = decl.getValue();
            if (value instanceof ReferenceExp) {
                return (ReferenceExp) value;
            }
        }
        return null;
    }

    public final boolean selfEvaluatingSymbol(Object obj) {
        return ((LispLanguage) getLanguage()).selfEvaluatingSymbol(obj);
    }

    public final boolean matches(Object form, String literal) {
        return matches(form, (SyntaxForm) null, literal);
    }

    public boolean matches(Object form, SyntaxForm syntax, String literal) {
        ReferenceExp rexp;
        if (syntax != null) {
        }
        if (form instanceof SyntaxForm) {
            form = ((SyntaxForm) form).getDatum();
        }
        if ((form instanceof SimpleSymbol) && !selfEvaluatingSymbol(form) && (rexp = getOriginalRef(this.lexical.lookup(form, -1))) != null) {
            form = rexp.getSymbol();
        }
        return (form instanceof SimpleSymbol) && ((Symbol) form).getLocalPart() == literal;
    }

    public boolean matches(Object form, SyntaxForm syntax, Symbol literal) {
        ReferenceExp rexp;
        if (syntax != null) {
        }
        if (form instanceof SyntaxForm) {
            form = ((SyntaxForm) form).getDatum();
        }
        if ((form instanceof SimpleSymbol) && !selfEvaluatingSymbol(form) && (rexp = getOriginalRef(this.lexical.lookup(form, -1))) != null) {
            form = rexp.getSymbol();
        }
        return form == literal;
    }

    public Object matchQuoted(Pair pair) {
        if (matches(pair.getCar(), LispLanguage.quote_sym) && (pair.getCdr() instanceof Pair)) {
            Pair pair2 = (Pair) pair.getCdr();
            if (pair2.getCdr() == LList.Empty) {
                return pair2.getCar();
            }
        }
        return null;
    }

    public Declaration lookup(Object name, int namespace) {
        Declaration decl = this.lexical.lookup(name, namespace);
        return (decl == null || !getLanguage().hasNamespace(decl, namespace)) ? currentModule().lookup(name, getLanguage(), namespace) : decl;
    }

    public Declaration lookupGlobal(Object name) {
        return lookupGlobal(name, -1);
    }

    public Declaration lookupGlobal(Object name, int namespace) {
        ModuleExp module = currentModule();
        Declaration decl = module.lookup(name, getLanguage(), namespace);
        if (decl != null) {
            return decl;
        }
        Declaration decl2 = module.getNoDefine(name);
        decl2.setIndirectBinding(true);
        return decl2;
    }

    /* access modifiers changed from: package-private */
    public Syntax check_if_Syntax(Declaration decl) {
        Declaration d = Declaration.followAliases(decl);
        Object obj = null;
        Expression dval = d.getValue();
        if (dval != null && d.getFlag(32768)) {
            try {
                if (decl.getValue() instanceof ReferenceExp) {
                    Declaration context = ((ReferenceExp) decl.getValue()).contextDecl();
                    if (context != null) {
                        this.macroContext = context;
                    } else if (this.current_scope instanceof TemplateScope) {
                        this.macroContext = ((TemplateScope) this.current_scope).macroContext;
                    }
                } else if (this.current_scope instanceof TemplateScope) {
                    this.macroContext = ((TemplateScope) this.current_scope).macroContext;
                }
                obj = dval.eval(this.env);
            } catch (Throwable ex) {
                ex.printStackTrace();
                error('e', "unable to evaluate macro for " + decl.getSymbol());
            }
        } else if (decl.getFlag(32768) && !decl.needsContext()) {
            obj = StaticFieldLocation.make(decl).get((Object) null);
        }
        if (obj instanceof Syntax) {
            return (Syntax) obj;
        }
        return null;
    }

    public Expression rewrite_pair(Pair p, boolean function) {
        Symbol symbol;
        Expression func = rewrite_car(p, true);
        if (func instanceof QuoteExp) {
            Object proc = func.valueIfConstant();
            if (proc instanceof Syntax) {
                return apply_rewrite((Syntax) proc, p);
            }
        }
        if (func instanceof ReferenceExp) {
            ReferenceExp ref = (ReferenceExp) func;
            Declaration decl = ref.getBinding();
            if (decl == null) {
                Object sym = ref.getSymbol();
                if (!(sym instanceof Symbol) || selfEvaluatingSymbol(sym)) {
                    symbol = this.env.getSymbol(sym.toString());
                } else {
                    symbol = (Symbol) sym;
                    String name = symbol.getName();
                }
                Object proc2 = this.env.get(symbol, getLanguage().hasSeparateFunctionNamespace() ? EnvironmentKey.FUNCTION : null, (Object) null);
                if (proc2 instanceof Syntax) {
                    return apply_rewrite((Syntax) proc2, p);
                }
                if (proc2 instanceof AutoloadProcedure) {
                    try {
                        Object proc3 = ((AutoloadProcedure) proc2).getLoaded();
                    } catch (RuntimeException e) {
                    }
                }
            } else {
                Declaration saveContext = this.macroContext;
                Syntax syntax = check_if_Syntax(decl);
                if (syntax != null) {
                    Expression apply_rewrite = apply_rewrite(syntax, p);
                    this.macroContext = saveContext;
                    return apply_rewrite;
                }
            }
            ref.setProcedureName(true);
            if (getLanguage().hasSeparateFunctionNamespace()) {
                func.setFlag(8);
            }
        }
        Object cdr = p.getCdr();
        int cdr_length = listLength(cdr);
        if (cdr_length == -1) {
            return syntaxError("circular list is not allowed after " + p.getCar());
        }
        if (cdr_length < 0) {
            return syntaxError("dotted list [" + cdr + "] is not allowed after " + p.getCar());
        }
        boolean mapKeywordsToAttributes = false;
        Stack vec = new Stack();
        ScopeExp save_scope = this.current_scope;
        int i = 0;
        while (i < cdr_length) {
            if (cdr instanceof SyntaxForm) {
                SyntaxForm sf = (SyntaxForm) cdr;
                cdr = sf.getDatum();
                setCurrentScope(sf.getScope());
            }
            Pair cdr_pair = (Pair) cdr;
            Expression arg = rewrite_car(cdr_pair, false);
            i++;
            if (mapKeywordsToAttributes) {
                if ((i & 1) == 0) {
                    arg = new ApplyExp((Procedure) MakeAttribute.makeAttribute, (Expression) vec.pop(), arg);
                } else {
                    if (arg instanceof QuoteExp) {
                        Object value = ((QuoteExp) arg).getValue();
                        if ((value instanceof Keyword) && i < cdr_length) {
                            arg = new QuoteExp(((Keyword) value).asSymbol());
                        }
                    }
                    mapKeywordsToAttributes = false;
                }
            }
            vec.addElement(arg);
            cdr = cdr_pair.getCdr();
        }
        Expression[] args = new Expression[vec.size()];
        vec.copyInto(args);
        if (save_scope != this.current_scope) {
            setCurrentScope(save_scope);
        }
        if (!(func instanceof ReferenceExp) || ((ReferenceExp) func).getBinding() != getNamedPartDecl) {
            return ((LispLanguage) getLanguage()).makeApply(func, args);
        }
        Expression part1 = args[0];
        Expression part2 = args[1];
        Symbol sym2 = namespaceResolve(part1, part2);
        if (sym2 != null) {
            return rewrite(sym2, function);
        }
        return CompileNamedPart.makeExp(part1, part2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: gnu.mapping.Namespace} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: gnu.mapping.Namespace} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: gnu.mapping.Namespace} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: gnu.mapping.Namespace} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.mapping.Namespace namespaceResolvePrefix(gnu.expr.Expression r11) {
        /*
            r10 = this;
            r7 = 0
            boolean r8 = r11 instanceof gnu.expr.ReferenceExp
            if (r8 == 0) goto L_0x0057
            r2 = r11
            gnu.expr.ReferenceExp r2 = (gnu.expr.ReferenceExp) r2
            gnu.expr.Declaration r0 = r2.getBinding()
            if (r0 == 0) goto L_0x0017
            r8 = 65536(0x10000, double:3.2379E-319)
            boolean r8 = r0.getFlag(r8)
            if (r8 == 0) goto L_0x004a
        L_0x0017:
            java.lang.Object r3 = r2.getSymbol()
            boolean r8 = r3 instanceof gnu.mapping.Symbol
            if (r8 == 0) goto L_0x003f
            gnu.mapping.Symbol r3 = (gnu.mapping.Symbol) r3
            r4 = r3
        L_0x0022:
            gnu.mapping.Environment r8 = r10.env
            java.lang.Object r6 = r8.get((gnu.mapping.EnvironmentKey) r4, (java.lang.Object) r7)
        L_0x0028:
            boolean r8 = r6 instanceof gnu.mapping.Namespace
            if (r8 == 0) goto L_0x0057
            r1 = r6
            gnu.mapping.Namespace r1 = (gnu.mapping.Namespace) r1
            java.lang.String r5 = r1.getName()
            if (r5 == 0) goto L_0x003e
            java.lang.String r8 = "class:"
            boolean r8 = r5.startsWith(r8)
            if (r8 == 0) goto L_0x003e
            r1 = r7
        L_0x003e:
            return r1
        L_0x003f:
            gnu.mapping.Environment r8 = r10.env
            java.lang.String r9 = r3.toString()
            gnu.mapping.Symbol r4 = r8.getSymbol(r9)
            goto L_0x0022
        L_0x004a:
            boolean r8 = r0.isNamespaceDecl()
            if (r8 == 0) goto L_0x0055
            java.lang.Object r6 = r0.getConstantValue()
            goto L_0x0028
        L_0x0055:
            r6 = 0
            goto L_0x0028
        L_0x0057:
            r1 = r7
            goto L_0x003e
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Translator.namespaceResolvePrefix(gnu.expr.Expression):gnu.mapping.Namespace");
    }

    public Symbol namespaceResolve(Namespace ns, Expression member) {
        if (ns == null || !(member instanceof QuoteExp)) {
            return null;
        }
        return ns.getSymbol(((QuoteExp) member).getValue().toString().intern());
    }

    public Symbol namespaceResolve(Expression context, Expression member) {
        return namespaceResolve(namespaceResolvePrefix(context), member);
    }

    public static Object stripSyntax(Object obj) {
        while (obj instanceof SyntaxForm) {
            obj = ((SyntaxForm) obj).getDatum();
        }
        return obj;
    }

    public static Object safeCar(Object obj) {
        while (obj instanceof SyntaxForm) {
            obj = ((SyntaxForm) obj).getDatum();
        }
        if (!(obj instanceof Pair)) {
            return null;
        }
        return stripSyntax(((Pair) obj).getCar());
    }

    public static Object safeCdr(Object obj) {
        while (obj instanceof SyntaxForm) {
            obj = ((SyntaxForm) obj).getDatum();
        }
        if (!(obj instanceof Pair)) {
            return null;
        }
        return stripSyntax(((Pair) obj).getCdr());
    }

    public static int listLength(Object obj) {
        int n = 0;
        Object slow = obj;
        Object fast = obj;
        while (true) {
            if (fast instanceof SyntaxForm) {
                fast = ((SyntaxForm) fast).getDatum();
            } else {
                while (slow instanceof SyntaxForm) {
                    slow = ((SyntaxForm) slow).getDatum();
                }
                if (fast == LList.Empty) {
                    return n;
                }
                if (!(fast instanceof Pair)) {
                    return -1 - n;
                }
                int n2 = n + 1;
                Object next = ((Pair) fast).getCdr();
                while (next instanceof SyntaxForm) {
                    next = ((SyntaxForm) next).getDatum();
                }
                if (next == LList.Empty) {
                    return n2;
                }
                if (!(next instanceof Pair)) {
                    return -1 - n2;
                }
                slow = ((Pair) slow).getCdr();
                fast = ((Pair) next).getCdr();
                n = n2 + 1;
                if (fast == slow) {
                    return Integer.MIN_VALUE;
                }
            }
        }
    }

    /* JADX INFO: finally extract failed */
    public void rewriteInBody(Object exp) {
        if (exp instanceof SyntaxForm) {
            SyntaxForm sf = (SyntaxForm) exp;
            ScopeExp save_scope = this.current_scope;
            try {
                setCurrentScope(sf.getScope());
                rewriteInBody(sf.getDatum());
                setCurrentScope(save_scope);
            } catch (Throwable th) {
                setCurrentScope(save_scope);
                throw th;
            }
        } else if (exp instanceof Values) {
            Object[] vals = ((Values) exp).getValues();
            for (Object rewriteInBody : vals) {
                rewriteInBody(rewriteInBody);
            }
        } else {
            this.formStack.add(rewrite(exp, false));
        }
    }

    public Expression rewrite(Object exp) {
        return rewrite(exp, false);
    }

    public Object namespaceResolve(Object name) {
        if (!(name instanceof SimpleSymbol) && (name instanceof Pair)) {
            Pair p = (Pair) name;
            if (safeCar(p) == LispLanguage.lookup_sym && (p.getCdr() instanceof Pair)) {
                Pair p2 = (Pair) p.getCdr();
                if (p2.getCdr() instanceof Pair) {
                    Expression part1 = rewrite(p2.getCar());
                    Expression part2 = rewrite(((Pair) p2.getCdr()).getCar());
                    Symbol sym = namespaceResolve(part1, part2);
                    if (sym != null) {
                        return sym;
                    }
                    String combinedName = CompileNamedPart.combineName(part1, part2);
                    if (combinedName != null) {
                        return Namespace.EmptyNamespace.getSymbol(combinedName);
                    }
                }
            }
        }
        return name;
    }

    /* JADX INFO: finally extract failed */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: gnu.expr.Special} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r27v1, resolved type: gnu.expr.LambdaExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r27v2, resolved type: gnu.expr.LambdaExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v68, resolved type: gnu.expr.LambdaExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v70, resolved type: gnu.expr.LambdaExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v72, resolved type: gnu.expr.LambdaExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r31v47, resolved type: gnu.expr.LambdaExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r27v7, resolved type: gnu.expr.LambdaExp} */
    /* JADX WARNING: type inference failed for: r23v1 */
    /* JADX WARNING: type inference failed for: r0v103, types: [gnu.expr.ThisExp] */
    /* JADX WARNING: type inference failed for: r0v104, types: [gnu.expr.ReferenceExp] */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x02c6, code lost:
        if ((r16 instanceof gnu.bytecode.ArrayClassLoader) == false) goto L_0x011c;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x011e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression rewrite(java.lang.Object r36, boolean r37) {
        /*
            r35 = this;
            r0 = r36
            boolean r0 = r0 instanceof kawa.lang.SyntaxForm
            r31 = r0
            if (r31 == 0) goto L_0x003c
            r29 = r36
            kawa.lang.SyntaxForm r29 = (kawa.lang.SyntaxForm) r29
            r0 = r35
            gnu.expr.ScopeExp r0 = r0.current_scope
            r26 = r0
            kawa.lang.TemplateScope r31 = r29.getScope()     // Catch:{ all -> 0x0033 }
            r0 = r35
            r1 = r31
            r0.setCurrentScope(r1)     // Catch:{ all -> 0x0033 }
            java.lang.Object r31 = r29.getDatum()     // Catch:{ all -> 0x0033 }
            r0 = r35
            r1 = r31
            r2 = r37
            gnu.expr.Expression r25 = r0.rewrite(r1, r2)     // Catch:{ all -> 0x0033 }
            r0 = r35
            r1 = r26
            r0.setCurrentScope(r1)
        L_0x0032:
            return r25
        L_0x0033:
            r31 = move-exception
            r0 = r35
            r1 = r26
            r0.setCurrentScope(r1)
            throw r31
        L_0x003c:
            r0 = r36
            boolean r0 = r0 instanceof gnu.lists.PairWithPosition
            r31 = r0
            if (r31 == 0) goto L_0x0055
            r31 = r36
            gnu.lists.PairWithPosition r31 = (gnu.lists.PairWithPosition) r31
            r0 = r35
            r1 = r36
            r2 = r37
            r3 = r31
            gnu.expr.Expression r25 = r0.rewrite_with_position(r1, r2, r3)
            goto L_0x0032
        L_0x0055:
            r0 = r36
            boolean r0 = r0 instanceof gnu.lists.Pair
            r31 = r0
            if (r31 == 0) goto L_0x0064
            gnu.lists.Pair r36 = (gnu.lists.Pair) r36
            gnu.expr.Expression r25 = r35.rewrite_pair(r36, r37)
            goto L_0x0032
        L_0x0064:
            r0 = r36
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r31 = r0
            if (r31 == 0) goto L_0x0381
            boolean r31 = r35.selfEvaluatingSymbol(r36)
            if (r31 != 0) goto L_0x0381
            r0 = r35
            gnu.expr.NameLookup r0 = r0.lexical
            r31 = r0
            r0 = r31
            r1 = r36
            r2 = r37
            gnu.expr.Declaration r10 = r0.lookup((java.lang.Object) r1, (boolean) r2)
            r5 = 0
            r0 = r35
            gnu.expr.ScopeExp r0 = r0.current_scope
            r27 = r0
            if (r10 != 0) goto L_0x0134
            r11 = -1
        L_0x008c:
            r0 = r36
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r31 = r0
            if (r31 == 0) goto L_0x013e
            r31 = r36
            gnu.mapping.Symbol r31 = (gnu.mapping.Symbol) r31
            boolean r31 = r31.hasEmptyNamespace()
            if (r31 == 0) goto L_0x013e
            java.lang.String r12 = r36.toString()
        L_0x00a2:
            if (r27 == 0) goto L_0x00d2
            r0 = r27
            boolean r0 = r0 instanceof gnu.expr.LambdaExp
            r31 = r0
            if (r31 == 0) goto L_0x018b
            r0 = r27
            gnu.expr.ScopeExp r0 = r0.outer
            r31 = r0
            r0 = r31
            boolean r0 = r0 instanceof gnu.expr.ClassExp
            r31 = r0
            if (r31 == 0) goto L_0x018b
            r31 = r27
            gnu.expr.LambdaExp r31 = (gnu.expr.LambdaExp) r31
            boolean r31 = r31.isClassMethod()
            if (r31 == 0) goto L_0x018b
            r0 = r27
            gnu.expr.ScopeExp r0 = r0.outer
            r31 = r0
            int r31 = gnu.expr.ScopeExp.nesting(r31)
            r0 = r31
            if (r11 < r0) goto L_0x0143
        L_0x00d2:
            if (r10 == 0) goto L_0x01cb
            java.lang.Object r21 = r10.getSymbol()
            r36 = 0
            gnu.expr.ReferenceExp r24 = getOriginalRef(r10)
            if (r24 == 0) goto L_0x00ec
            gnu.expr.Declaration r10 = r24.getBinding()
            if (r10 != 0) goto L_0x00ec
            java.lang.Object r36 = r24.getSymbol()
            r21 = r36
        L_0x00ec:
            r31 = r36
        L_0x00ee:
            r30 = r31
            gnu.mapping.Symbol r30 = (gnu.mapping.Symbol) r30
            gnu.expr.Language r32 = r35.getLanguage()
            boolean r28 = r32.hasSeparateFunctionNamespace()
            if (r10 == 0) goto L_0x0220
            r0 = r35
            gnu.expr.ScopeExp r0 = r0.current_scope
            r31 = r0
            r0 = r31
            boolean r0 = r0 instanceof kawa.lang.TemplateScope
            r31 = r0
            if (r31 == 0) goto L_0x01d1
            boolean r31 = r10.needsContext()
            if (r31 == 0) goto L_0x01d1
            r0 = r35
            gnu.expr.ScopeExp r0 = r0.current_scope
            r31 = r0
            kawa.lang.TemplateScope r31 = (kawa.lang.TemplateScope) r31
            r0 = r31
            gnu.expr.Declaration r5 = r0.macroContext
        L_0x011c:
            if (r10 == 0) goto L_0x035b
            if (r37 != 0) goto L_0x0328
            java.lang.Object r31 = r10.getConstantValue()
            r0 = r31
            boolean r0 = r0 instanceof kawa.standard.object
            r31 = r0
            if (r31 == 0) goto L_0x0328
            java.lang.Class<java.lang.Object> r31 = java.lang.Object.class
            gnu.expr.QuoteExp r25 = gnu.expr.QuoteExp.getInstance(r31)
            goto L_0x0032
        L_0x0134:
            gnu.expr.ScopeExp r0 = r10.context
            r31 = r0
            int r11 = gnu.expr.ScopeExp.nesting(r31)
            goto L_0x008c
        L_0x013e:
            r12 = 0
            r27 = 0
            goto L_0x00a2
        L_0x0143:
            r4 = r27
            gnu.expr.LambdaExp r4 = (gnu.expr.LambdaExp) r4
            r0 = r27
            gnu.expr.ScopeExp r7 = r0.outer
            gnu.expr.ClassExp r7 = (gnu.expr.ClassExp) r7
            gnu.bytecode.ClassType r9 = r7.getClassType()
            gnu.bytecode.Member r22 = gnu.kawa.reflect.SlotGet.lookupMember(r9, r12, r9)
            gnu.expr.LambdaExp r0 = r7.clinitMethod
            r31 = r0
            r0 = r31
            if (r4 == r0) goto L_0x016f
            gnu.expr.LambdaExp r0 = r7.initMethod
            r31 = r0
            r0 = r31
            if (r4 == r0) goto L_0x0193
            gnu.expr.Declaration r0 = r4.nameDecl
            r31 = r0
            boolean r31 = r31.isStatic()
            if (r31 == 0) goto L_0x0193
        L_0x016f:
            r8 = 1
        L_0x0170:
            if (r22 != 0) goto L_0x0198
            if (r8 == 0) goto L_0x0195
            r20 = 83
        L_0x0176:
            r0 = r35
            gnu.expr.Language r0 = r0.language
            r31 = r0
            r0 = r20
            r1 = r31
            gnu.expr.PrimProcedure[] r19 = gnu.kawa.reflect.ClassMethods.getMethods(r9, r12, r0, r9, r1)
            r0 = r19
            int r0 = r0.length
            r31 = r0
            if (r31 != 0) goto L_0x0198
        L_0x018b:
            r0 = r27
            gnu.expr.ScopeExp r0 = r0.outer
            r27 = r0
            goto L_0x00a2
        L_0x0193:
            r8 = 0
            goto L_0x0170
        L_0x0195:
            r20 = 86
            goto L_0x0176
        L_0x0198:
            if (r8 == 0) goto L_0x01bd
            gnu.expr.ReferenceExp r23 = new gnu.expr.ReferenceExp
            gnu.expr.ScopeExp r0 = r4.outer
            r31 = r0
            gnu.expr.ClassExp r31 = (gnu.expr.ClassExp) r31
            r0 = r31
            gnu.expr.Declaration r0 = r0.nameDecl
            r31 = r0
            r0 = r23
            r1 = r31
            r0.<init>((gnu.expr.Declaration) r1)
        L_0x01af:
            gnu.expr.QuoteExp r31 = gnu.expr.QuoteExp.getInstance(r12)
            r0 = r23
            r1 = r31
            gnu.expr.Expression r25 = gnu.kawa.functions.CompileNamedPart.makeExp((gnu.expr.Expression) r0, (gnu.expr.Expression) r1)
            goto L_0x0032
        L_0x01bd:
            gnu.expr.ThisExp r23 = new gnu.expr.ThisExp
            gnu.expr.Declaration r31 = r4.firstDecl()
            r0 = r23
            r1 = r31
            r0.<init>((gnu.expr.Declaration) r1)
            goto L_0x01af
        L_0x01cb:
            r21 = r36
            r31 = r36
            goto L_0x00ee
        L_0x01d1:
            r32 = 1048576(0x100000, double:5.180654E-318)
            r0 = r32
            boolean r31 = r10.getFlag(r0)
            if (r31 == 0) goto L_0x011c
            boolean r31 = r10.isStatic()
            if (r31 != 0) goto L_0x011c
            gnu.expr.ScopeExp r27 = r35.currentScope()
        L_0x01e6:
            if (r27 != 0) goto L_0x0203
            java.lang.Error r31 = new java.lang.Error
            java.lang.StringBuilder r32 = new java.lang.StringBuilder
            r32.<init>()
            java.lang.String r33 = "internal error: missing "
            java.lang.StringBuilder r32 = r32.append(r33)
            r0 = r32
            java.lang.StringBuilder r32 = r0.append(r10)
            java.lang.String r32 = r32.toString()
            r31.<init>(r32)
            throw r31
        L_0x0203:
            r0 = r27
            gnu.expr.ScopeExp r0 = r0.outer
            r31 = r0
            gnu.expr.ScopeExp r0 = r10.context
            r32 = r0
            r0 = r31
            r1 = r32
            if (r0 != r1) goto L_0x0219
            gnu.expr.Declaration r5 = r27.firstDecl()
            goto L_0x011c
        L_0x0219:
            r0 = r27
            gnu.expr.ScopeExp r0 = r0.outer
            r27 = r0
            goto L_0x01e6
        L_0x0220:
            r0 = r35
            gnu.mapping.Environment r0 = r0.env
            r33 = r0
            if (r37 == 0) goto L_0x027d
            if (r28 == 0) goto L_0x027d
            java.lang.Object r32 = gnu.mapping.EnvironmentKey.FUNCTION
        L_0x022c:
            r0 = r33
            r1 = r30
            r2 = r32
            gnu.mapping.Location r18 = r0.lookup(r1, r2)
            if (r18 == 0) goto L_0x023c
            gnu.mapping.Location r18 = r18.getBase()
        L_0x023c:
            r0 = r18
            boolean r0 = r0 instanceof gnu.kawa.reflect.FieldLocation
            r32 = r0
            if (r32 == 0) goto L_0x030a
            r17 = r18
            gnu.kawa.reflect.FieldLocation r17 = (gnu.kawa.reflect.FieldLocation) r17
            gnu.expr.Declaration r10 = r17.getDeclaration()     // Catch:{ Throwable -> 0x02ce }
            r32 = 0
            r0 = r35
            r1 = r32
            boolean r32 = r0.inlineOk((gnu.expr.Expression) r1)     // Catch:{ Throwable -> 0x02ce }
            if (r32 != 0) goto L_0x0280
            gnu.expr.Declaration r32 = getNamedPartDecl     // Catch:{ Throwable -> 0x02ce }
            r0 = r32
            if (r10 == r0) goto L_0x0280
            java.lang.String r32 = "objectSyntax"
            java.lang.String r33 = r17.getMemberName()     // Catch:{ Throwable -> 0x02ce }
            boolean r32 = r32.equals(r33)     // Catch:{ Throwable -> 0x02ce }
            if (r32 == 0) goto L_0x027a
            java.lang.String r32 = "kawa.standard.object"
            gnu.bytecode.ClassType r33 = r17.getDeclaringClass()     // Catch:{ Throwable -> 0x02ce }
            java.lang.String r33 = r33.getName()     // Catch:{ Throwable -> 0x02ce }
            boolean r32 = r32.equals(r33)     // Catch:{ Throwable -> 0x02ce }
            if (r32 != 0) goto L_0x0280
        L_0x027a:
            r10 = 0
            goto L_0x011c
        L_0x027d:
            r32 = 0
            goto L_0x022c
        L_0x0280:
            r0 = r35
            boolean r0 = r0.immediate     // Catch:{ Throwable -> 0x02ce }
            r32 = r0
            if (r32 == 0) goto L_0x02a8
            boolean r32 = r10.isStatic()     // Catch:{ Throwable -> 0x02ce }
            if (r32 != 0) goto L_0x011c
            gnu.expr.Declaration r6 = new gnu.expr.Declaration     // Catch:{ Throwable -> 0x02ce }
            java.lang.String r32 = "(module-instance)"
            r0 = r32
            r6.<init>((java.lang.Object) r0)     // Catch:{ Throwable -> 0x02ce }
            gnu.expr.QuoteExp r32 = new gnu.expr.QuoteExp     // Catch:{ Throwable -> 0x03c7 }
            java.lang.Object r33 = r17.getInstance()     // Catch:{ Throwable -> 0x03c7 }
            r32.<init>(r33)     // Catch:{ Throwable -> 0x03c7 }
            r0 = r32
            r6.setValue(r0)     // Catch:{ Throwable -> 0x03c7 }
            r5 = r6
            goto L_0x011c
        L_0x02a8:
            boolean r32 = r10.isStatic()     // Catch:{ Throwable -> 0x02ce }
            if (r32 == 0) goto L_0x02cb
            java.lang.Class r15 = r17.getRClass()     // Catch:{ Throwable -> 0x02ce }
            if (r15 == 0) goto L_0x02c8
            java.lang.ClassLoader r16 = r15.getClassLoader()     // Catch:{ Throwable -> 0x02ce }
            r0 = r16
            boolean r0 = r0 instanceof gnu.bytecode.ZipLoader     // Catch:{ Throwable -> 0x02ce }
            r32 = r0
            if (r32 != 0) goto L_0x02c8
            r0 = r16
            boolean r0 = r0 instanceof gnu.bytecode.ArrayClassLoader     // Catch:{ Throwable -> 0x02ce }
            r31 = r0
            if (r31 == 0) goto L_0x011c
        L_0x02c8:
            r10 = 0
            goto L_0x011c
        L_0x02cb:
            r10 = 0
            goto L_0x011c
        L_0x02ce:
            r14 = move-exception
        L_0x02cf:
            r32 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r33 = new java.lang.StringBuilder
            r33.<init>()
            java.lang.String r34 = "exception loading '"
            java.lang.StringBuilder r33 = r33.append(r34)
            r0 = r33
            r1 = r31
            java.lang.StringBuilder r31 = r0.append(r1)
            java.lang.String r33 = "' - "
            r0 = r31
            r1 = r33
            java.lang.StringBuilder r31 = r0.append(r1)
            java.lang.String r33 = r14.getMessage()
            r0 = r31
            r1 = r33
            java.lang.StringBuilder r31 = r0.append(r1)
            java.lang.String r31 = r31.toString()
            r0 = r35
            r1 = r32
            r2 = r31
            r0.error(r1, r2)
            r10 = 0
            goto L_0x011c
        L_0x030a:
            if (r18 == 0) goto L_0x0312
            boolean r31 = r18.isBound()
            if (r31 != 0) goto L_0x011c
        L_0x0312:
            gnu.expr.Language r31 = r35.getLanguage()
            gnu.kawa.lispexpr.LispLanguage r31 = (gnu.kawa.lispexpr.LispLanguage) r31
            r0 = r31
            r1 = r30
            r2 = r35
            gnu.expr.Expression r13 = r0.checkDefaultBinding(r1, r2)
            if (r13 == 0) goto L_0x011c
            r25 = r13
            goto L_0x0032
        L_0x0328:
            gnu.expr.ScopeExp r31 = r10.getContext()
            r0 = r31
            boolean r0 = r0 instanceof kawa.lang.PatternScope
            r31 = r0
            if (r31 == 0) goto L_0x035b
            java.lang.StringBuilder r31 = new java.lang.StringBuilder
            r31.<init>()
            java.lang.String r32 = "reference to pattern variable "
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r32 = r10.getName()
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r32 = " outside syntax template"
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r31 = r31.toString()
            r0 = r35
            r1 = r31
            gnu.expr.Expression r25 = r0.syntaxError(r1)
            goto L_0x0032
        L_0x035b:
            gnu.expr.ReferenceExp r24 = new gnu.expr.ReferenceExp
            r0 = r24
            r1 = r21
            r0.<init>(r1, r10)
            r0 = r24
            r0.setContextDecl(r5)
            r0 = r24
            r1 = r35
            r0.setLine((gnu.expr.Compilation) r1)
            if (r37 == 0) goto L_0x037d
            if (r28 == 0) goto L_0x037d
            r31 = 8
            r0 = r24
            r1 = r31
            r0.setFlag(r1)
        L_0x037d:
            r25 = r24
            goto L_0x0032
        L_0x0381:
            r0 = r36
            boolean r0 = r0 instanceof gnu.expr.LangExp
            r31 = r0
            if (r31 == 0) goto L_0x039b
            gnu.expr.LangExp r36 = (gnu.expr.LangExp) r36
            java.lang.Object r31 = r36.getLangValue()
            r0 = r35
            r1 = r31
            r2 = r37
            gnu.expr.Expression r25 = r0.rewrite(r1, r2)
            goto L_0x0032
        L_0x039b:
            r0 = r36
            boolean r0 = r0 instanceof gnu.expr.Expression
            r31 = r0
            if (r31 == 0) goto L_0x03a9
            gnu.expr.Expression r36 = (gnu.expr.Expression) r36
            r25 = r36
            goto L_0x0032
        L_0x03a9:
            gnu.expr.Special r31 = gnu.expr.Special.abstractSpecial
            r0 = r36
            r1 = r31
            if (r0 != r1) goto L_0x03b5
            gnu.expr.QuoteExp r25 = gnu.expr.QuoteExp.abstractExp
            goto L_0x0032
        L_0x03b5:
            r0 = r36
            r1 = r35
            java.lang.Object r31 = kawa.lang.Quote.quote(r0, r1)
            r0 = r31
            r1 = r35
            gnu.expr.QuoteExp r25 = gnu.expr.QuoteExp.getInstance(r0, r1)
            goto L_0x0032
        L_0x03c7:
            r14 = move-exception
            r5 = r6
            goto L_0x02cf
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Translator.rewrite(java.lang.Object, boolean):gnu.expr.Expression");
    }

    public static void setLine(Expression exp, Object location) {
        if (location instanceof SourceLocator) {
            exp.setLocation((SourceLocator) location);
        }
    }

    public static void setLine(Declaration decl, Object location) {
        if (location instanceof SourceLocator) {
            decl.setLocation((SourceLocator) location);
        }
    }

    public Object pushPositionOf(Object pair) {
        PairWithPosition saved;
        if (pair instanceof SyntaxForm) {
            pair = ((SyntaxForm) pair).getDatum();
        }
        if (!(pair instanceof PairWithPosition)) {
            return null;
        }
        PairWithPosition ppair = (PairWithPosition) pair;
        if (this.positionPair != null && this.positionPair.getFileName() == getFileName() && this.positionPair.getLineNumber() == getLineNumber() && this.positionPair.getColumnNumber() == getColumnNumber()) {
            saved = this.positionPair;
        } else {
            saved = new PairWithPosition(this, Special.eof, this.positionPair);
        }
        setLine(pair);
        this.positionPair = ppair;
        return saved;
    }

    public void popPositionOf(Object saved) {
        if (saved != null) {
            setLine(saved);
            this.positionPair = (PairWithPosition) saved;
            if (this.positionPair.getCar() == Special.eof) {
                this.positionPair = (PairWithPosition) this.positionPair.getCdr();
            }
        }
    }

    public void setLineOf(Expression exp) {
        if (!(exp instanceof QuoteExp)) {
            exp.setLocation(this);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: gnu.bytecode.Type} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.bytecode.Type exp2Type(gnu.lists.Pair r9) {
        /*
            r8 = this;
            java.lang.Object r1 = r8.pushPositionOf(r9)
            r5 = 0
            gnu.expr.Expression r3 = r8.rewrite_car((gnu.lists.Pair) r9, (boolean) r5)     // Catch:{ all -> 0x0071 }
            gnu.expr.Expression r3 = gnu.expr.InlineCalls.inlineCalls(r3, r8)     // Catch:{ all -> 0x0071 }
            boolean r5 = r3 instanceof gnu.expr.ErrorExp     // Catch:{ all -> 0x0071 }
            if (r5 == 0) goto L_0x0016
            r4 = 0
            r8.popPositionOf(r1)
        L_0x0015:
            return r4
        L_0x0016:
            gnu.expr.Language r5 = r8.getLanguage()     // Catch:{ all -> 0x0071 }
            gnu.bytecode.Type r4 = r5.getTypeFor((gnu.expr.Expression) r3)     // Catch:{ all -> 0x0071 }
            if (r4 != 0) goto L_0x0030
            gnu.mapping.Environment r5 = r8.env     // Catch:{ Throwable -> 0x007a }
            java.lang.Object r2 = r3.eval((gnu.mapping.Environment) r5)     // Catch:{ Throwable -> 0x007a }
            boolean r5 = r2 instanceof java.lang.Class     // Catch:{ Throwable -> 0x007a }
            if (r5 == 0) goto L_0x0060
            java.lang.Class r2 = (java.lang.Class) r2     // Catch:{ Throwable -> 0x007a }
            gnu.bytecode.Type r4 = gnu.bytecode.Type.make(r2)     // Catch:{ Throwable -> 0x007a }
        L_0x0030:
            if (r4 != 0) goto L_0x0076
            boolean r5 = r3 instanceof gnu.expr.ReferenceExp     // Catch:{ all -> 0x0071 }
            if (r5 == 0) goto L_0x0069
            r5 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0071 }
            r6.<init>()     // Catch:{ all -> 0x0071 }
            java.lang.String r7 = "unknown type name '"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0071 }
            gnu.expr.ReferenceExp r3 = (gnu.expr.ReferenceExp) r3     // Catch:{ all -> 0x0071 }
            java.lang.String r7 = r3.getName()     // Catch:{ all -> 0x0071 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0071 }
            r7 = 39
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x0071 }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x0071 }
            r8.error(r5, r6)     // Catch:{ all -> 0x0071 }
        L_0x005a:
            gnu.bytecode.ClassType r4 = gnu.bytecode.Type.pointer_type     // Catch:{ all -> 0x0071 }
            r8.popPositionOf(r1)
            goto L_0x0015
        L_0x0060:
            boolean r5 = r2 instanceof gnu.bytecode.Type     // Catch:{ Throwable -> 0x007a }
            if (r5 == 0) goto L_0x0030
            r0 = r2
            gnu.bytecode.Type r0 = (gnu.bytecode.Type) r0     // Catch:{ Throwable -> 0x007a }
            r4 = r0
            goto L_0x0030
        L_0x0069:
            r5 = 101(0x65, float:1.42E-43)
            java.lang.String r6 = "invalid type spec (must be \"type\" or 'type or <type>)"
            r8.error(r5, r6)     // Catch:{ all -> 0x0071 }
            goto L_0x005a
        L_0x0071:
            r5 = move-exception
            r8.popPositionOf(r1)
            throw r5
        L_0x0076:
            r8.popPositionOf(r1)
            goto L_0x0015
        L_0x007a:
            r5 = move-exception
            goto L_0x0030
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Translator.exp2Type(gnu.lists.Pair):gnu.bytecode.Type");
    }

    public Expression rewrite_with_position(Object exp, boolean function, PairWithPosition pair) {
        Expression result;
        Object saved = pushPositionOf(pair);
        if (exp == pair) {
            try {
                result = rewrite_pair(pair, function);
            } catch (Throwable th) {
                popPositionOf(saved);
                throw th;
            }
        } else {
            result = rewrite(exp, function);
        }
        setLineOf(result);
        popPositionOf(saved);
        return result;
    }

    public static Object wrapSyntax(Object form, SyntaxForm syntax) {
        return (syntax == null || (form instanceof Expression)) ? form : SyntaxForms.fromDatumIfNeeded(form, syntax);
    }

    public Object popForms(int first) {
        Object obj;
        int last = this.formStack.size();
        if (last == first) {
            return Values.empty;
        }
        if (last == first + 1) {
            obj = this.formStack.elementAt(first);
        } else {
            Values vals = new Values();
            for (int i = first; i < last; i++) {
                vals.writeObject(this.formStack.elementAt(i));
            }
            obj = vals;
        }
        this.formStack.setSize(first);
        return obj;
    }

    /* JADX INFO: finally extract failed */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v41, resolved type: kawa.lang.Syntax} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: gnu.mapping.Symbol} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v18, resolved type: gnu.mapping.Values} */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00c6, code lost:
        r10 = (gnu.lists.Pair) r9;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scanForm(java.lang.Object r29, gnu.expr.ScopeExp r30) {
        /*
            r28 = this;
            r0 = r29
            boolean r0 = r0 instanceof kawa.lang.SyntaxForm
            r26 = r0
            if (r26 == 0) goto L_0x005a
            r20 = r29
            kawa.lang.SyntaxForm r20 = (kawa.lang.SyntaxForm) r20
            gnu.expr.ScopeExp r17 = r28.currentScope()
            kawa.lang.TemplateScope r26 = r20.getScope()     // Catch:{ all -> 0x0051 }
            r0 = r28
            r1 = r26
            r0.setCurrentScope(r1)     // Catch:{ all -> 0x0051 }
            r0 = r28
            java.util.Stack r0 = r0.formStack     // Catch:{ all -> 0x0051 }
            r26 = r0
            int r6 = r26.size()     // Catch:{ all -> 0x0051 }
            java.lang.Object r26 = r20.getDatum()     // Catch:{ all -> 0x0051 }
            r0 = r28
            r1 = r26
            r2 = r30
            r0.scanForm(r1, r2)     // Catch:{ all -> 0x0051 }
            r0 = r28
            java.util.Stack r0 = r0.formStack     // Catch:{ all -> 0x0051 }
            r26 = r0
            r0 = r28
            java.lang.Object r27 = r0.popForms(r6)     // Catch:{ all -> 0x0051 }
            r0 = r27
            r1 = r20
            java.lang.Object r27 = wrapSyntax(r0, r1)     // Catch:{ all -> 0x0051 }
            r26.add(r27)     // Catch:{ all -> 0x0051 }
            r0 = r28
            r1 = r17
            r0.setCurrentScope(r1)
        L_0x0050:
            return
        L_0x0051:
            r26 = move-exception
            r0 = r28
            r1 = r17
            r0.setCurrentScope(r1)
            throw r26
        L_0x005a:
            r0 = r29
            boolean r0 = r0 instanceof gnu.mapping.Values
            r26 = r0
            if (r26 == 0) goto L_0x006c
            gnu.mapping.Values r26 = gnu.mapping.Values.empty
            r0 = r29
            r1 = r26
            if (r0 != r1) goto L_0x01ae
            gnu.expr.QuoteExp r29 = gnu.expr.QuoteExp.voidExp
        L_0x006c:
            r0 = r29
            boolean r0 = r0 instanceof gnu.lists.Pair
            r26 = r0
            if (r26 == 0) goto L_0x0225
            r21 = r29
            gnu.lists.Pair r21 = (gnu.lists.Pair) r21
            r0 = r28
            gnu.expr.Declaration r13 = r0.macroContext
            r22 = 0
            r0 = r28
            gnu.expr.ScopeExp r0 = r0.current_scope
            r19 = r0
            java.lang.Object r18 = r28.pushPositionOf(r29)
            r0 = r29
            boolean r0 = r0 instanceof gnu.text.SourceLocator
            r26 = r0
            if (r26 == 0) goto L_0x00a1
            int r26 = r30.getLineNumber()
            if (r26 >= 0) goto L_0x00a1
            r26 = r29
            gnu.text.SourceLocator r26 = (gnu.text.SourceLocator) r26
            r0 = r30
            r1 = r26
            r0.setLocation(r1)
        L_0x00a1:
            java.lang.Object r9 = r21.getCar()     // Catch:{ all -> 0x01fc }
            boolean r0 = r9 instanceof kawa.lang.SyntaxForm     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x00c0
            java.lang.Object r20 = r21.getCar()     // Catch:{ all -> 0x01fc }
            kawa.lang.SyntaxForm r20 = (kawa.lang.SyntaxForm) r20     // Catch:{ all -> 0x01fc }
            kawa.lang.TemplateScope r26 = r20.getScope()     // Catch:{ all -> 0x01fc }
            r0 = r28
            r1 = r26
            r0.setCurrentScope(r1)     // Catch:{ all -> 0x01fc }
            java.lang.Object r9 = r20.getDatum()     // Catch:{ all -> 0x01fc }
        L_0x00c0:
            boolean r0 = r9 instanceof gnu.lists.Pair     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x013b
            r0 = r9
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ all -> 0x01fc }
            r10 = r0
            java.lang.Object r26 = r10.getCar()     // Catch:{ all -> 0x01fc }
            gnu.mapping.Symbol r27 = gnu.kawa.lispexpr.LispLanguage.lookup_sym     // Catch:{ all -> 0x01fc }
            r0 = r26
            r1 = r27
            if (r0 != r1) goto L_0x013b
            java.lang.Object r26 = r10.getCdr()     // Catch:{ all -> 0x01fc }
            r0 = r26
            boolean r0 = r0 instanceof gnu.lists.Pair     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x013b
            java.lang.Object r10 = r10.getCdr()     // Catch:{ all -> 0x01fc }
            gnu.lists.Pair r10 = (gnu.lists.Pair) r10     // Catch:{ all -> 0x01fc }
            java.lang.Object r26 = r10.getCdr()     // Catch:{ all -> 0x01fc }
            r0 = r26
            boolean r0 = r0 instanceof gnu.lists.Pair     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x013b
            java.lang.Object r26 = r10.getCar()     // Catch:{ all -> 0x01fc }
            r0 = r28
            r1 = r26
            gnu.expr.Expression r11 = r0.rewrite(r1)     // Catch:{ all -> 0x01fc }
            java.lang.Object r26 = r10.getCdr()     // Catch:{ all -> 0x01fc }
            gnu.lists.Pair r26 = (gnu.lists.Pair) r26     // Catch:{ all -> 0x01fc }
            java.lang.Object r26 = r26.getCar()     // Catch:{ all -> 0x01fc }
            r0 = r28
            r1 = r26
            gnu.expr.Expression r12 = r0.rewrite(r1)     // Catch:{ all -> 0x01fc }
            java.lang.Object r24 = r11.valueIfConstant()     // Catch:{ all -> 0x01fc }
            java.lang.Object r25 = r12.valueIfConstant()     // Catch:{ all -> 0x01fc }
            r0 = r24
            boolean r0 = r0 instanceof java.lang.Class     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x01d0
            r0 = r25
            boolean r0 = r0 instanceof gnu.mapping.Symbol     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x01d0
            gnu.mapping.Symbol r25 = (gnu.mapping.Symbol) r25     // Catch:{ Throwable -> 0x01cc }
            java.lang.Object r9 = gnu.kawa.functions.GetNamedPart.getNamedPart(r24, r25)     // Catch:{ Throwable -> 0x01cc }
            boolean r0 = r9 instanceof kawa.lang.Syntax     // Catch:{ Throwable -> 0x01cc }
            r26 = r0
            if (r26 == 0) goto L_0x013b
            r0 = r9
            kawa.lang.Syntax r0 = (kawa.lang.Syntax) r0     // Catch:{ Throwable -> 0x01cc }
            r22 = r0
        L_0x013b:
            boolean r0 = r9 instanceof gnu.mapping.Symbol     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x01ef
            r0 = r28
            boolean r26 = r0.selfEvaluatingSymbol(r9)     // Catch:{ all -> 0x01fc }
            if (r26 != 0) goto L_0x01ef
            r26 = 1
            r0 = r28
            r1 = r26
            gnu.expr.Expression r7 = r0.rewrite(r9, r1)     // Catch:{ all -> 0x01fc }
            boolean r0 = r7 instanceof gnu.expr.ReferenceExp     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x0167
            gnu.expr.ReferenceExp r7 = (gnu.expr.ReferenceExp) r7     // Catch:{ all -> 0x01fc }
            gnu.expr.Declaration r4 = r7.getBinding()     // Catch:{ all -> 0x01fc }
            if (r4 == 0) goto L_0x01d8
            r0 = r28
            kawa.lang.Syntax r22 = r0.check_if_Syntax(r4)     // Catch:{ all -> 0x01fc }
        L_0x0167:
            r0 = r28
            gnu.expr.ScopeExp r0 = r0.current_scope
            r26 = r0
            r0 = r19
            r1 = r26
            if (r0 == r1) goto L_0x017a
            r0 = r28
            r1 = r19
            r0.setCurrentScope(r1)
        L_0x017a:
            r0 = r28
            r1 = r18
            r0.popPositionOf(r1)
            if (r22 == 0) goto L_0x0225
            java.lang.String r15 = r28.getFileName()
            int r16 = r28.getLineNumber()
            int r14 = r28.getColumnNumber()
            r0 = r28
            r1 = r21
            r0.setLine((java.lang.Object) r1)     // Catch:{ all -> 0x0218 }
            r0 = r22
            r1 = r21
            r2 = r30
            r3 = r28
            r0.scanForm(r1, r2, r3)     // Catch:{ all -> 0x0218 }
            r0 = r28
            r0.macroContext = r13
            r0 = r28
            r1 = r16
            r0.setLine(r15, r1, r14)
            goto L_0x0050
        L_0x01ae:
            gnu.mapping.Values r29 = (gnu.mapping.Values) r29
            java.lang.Object[] r23 = r29.getValues()
            r8 = 0
        L_0x01b5:
            r0 = r23
            int r0 = r0.length
            r26 = r0
            r0 = r26
            if (r8 >= r0) goto L_0x0050
            r26 = r23[r8]
            r0 = r28
            r1 = r26
            r2 = r30
            r0.scanForm(r1, r2)
            int r8 = r8 + 1
            goto L_0x01b5
        L_0x01cc:
            r5 = move-exception
            r9 = 0
            goto L_0x013b
        L_0x01d0:
            r0 = r28
            gnu.mapping.Symbol r9 = r0.namespaceResolve((gnu.expr.Expression) r11, (gnu.expr.Expression) r12)     // Catch:{ all -> 0x01fc }
            goto L_0x013b
        L_0x01d8:
            r26 = 1
            r0 = r28
            r1 = r26
            java.lang.Object r9 = r0.resolve(r9, r1)     // Catch:{ all -> 0x01fc }
            boolean r0 = r9 instanceof kawa.lang.Syntax     // Catch:{ all -> 0x01fc }
            r26 = r0
            if (r26 == 0) goto L_0x0167
            r0 = r9
            kawa.lang.Syntax r0 = (kawa.lang.Syntax) r0     // Catch:{ all -> 0x01fc }
            r22 = r0
            goto L_0x0167
        L_0x01ef:
            kawa.standard.begin r26 = kawa.standard.begin.begin     // Catch:{ all -> 0x01fc }
            r0 = r26
            if (r9 != r0) goto L_0x0167
            r0 = r9
            kawa.lang.Syntax r0 = (kawa.lang.Syntax) r0     // Catch:{ all -> 0x01fc }
            r22 = r0
            goto L_0x0167
        L_0x01fc:
            r26 = move-exception
            r0 = r28
            gnu.expr.ScopeExp r0 = r0.current_scope
            r27 = r0
            r0 = r19
            r1 = r27
            if (r0 == r1) goto L_0x0210
            r0 = r28
            r1 = r19
            r0.setCurrentScope(r1)
        L_0x0210:
            r0 = r28
            r1 = r18
            r0.popPositionOf(r1)
            throw r26
        L_0x0218:
            r26 = move-exception
            r0 = r28
            r0.macroContext = r13
            r0 = r28
            r1 = r16
            r0.setLine(r15, r1, r14)
            throw r26
        L_0x0225:
            r0 = r28
            java.util.Stack r0 = r0.formStack
            r26 = r0
            r0 = r26
            r1 = r29
            r0.add(r1)
            goto L_0x0050
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Translator.scanForm(java.lang.Object, gnu.expr.ScopeExp):void");
    }

    /* JADX INFO: finally extract failed */
    public LList scanBody(Object body, ScopeExp defs, boolean makeList) {
        LList list = makeList ? LList.Empty : null;
        Pair lastPair = null;
        while (body != LList.Empty) {
            if (body instanceof SyntaxForm) {
                SyntaxForm sf = (SyntaxForm) body;
                ScopeExp save_scope = this.current_scope;
                try {
                    setCurrentScope(sf.getScope());
                    int first = this.formStack.size();
                    LList f = scanBody(sf.getDatum(), defs, makeList);
                    if (makeList) {
                        LList f2 = (LList) SyntaxForms.fromDatumIfNeeded(f, sf);
                        if (lastPair == null) {
                            setCurrentScope(save_scope);
                            return f2;
                        }
                        lastPair.setCdrBackdoor(f2);
                        setCurrentScope(save_scope);
                        return list;
                    }
                    this.formStack.add(wrapSyntax(popForms(first), sf));
                    setCurrentScope(save_scope);
                    return null;
                } catch (Throwable th) {
                    setCurrentScope(save_scope);
                    throw th;
                }
            } else if (body instanceof Pair) {
                Pair pair = (Pair) body;
                int first2 = this.formStack.size();
                scanForm(pair.getCar(), defs);
                if (getState() == 2) {
                    if (pair.getCar() != this.pendingForm) {
                        pair = makePair(pair, this.pendingForm, pair.getCdr());
                    }
                    this.pendingForm = new Pair(begin.begin, pair);
                    return LList.Empty;
                }
                int fsize = this.formStack.size();
                if (makeList) {
                    for (int i = first2; i < fsize; i++) {
                        LList npair = makePair(pair, this.formStack.elementAt(i), LList.Empty);
                        if (lastPair == null) {
                            list = npair;
                        } else {
                            lastPair.setCdrBackdoor(npair);
                        }
                        lastPair = npair;
                    }
                    this.formStack.setSize(first2);
                }
                body = pair.getCdr();
            } else {
                this.formStack.add(syntaxError("body is not a proper list"));
                return list;
            }
        }
        return list;
    }

    public static Pair makePair(Pair pair, Object car, Object cdr) {
        if (pair instanceof PairWithPosition) {
            return new PairWithPosition((PairWithPosition) pair, car, cdr);
        }
        return new Pair(car, cdr);
    }

    public Expression rewrite_body(Object exp) {
        Object saved = pushPositionOf(exp);
        LetExp defs = new LetExp((Expression[]) null);
        int first = this.formStack.size();
        defs.outer = this.current_scope;
        this.current_scope = defs;
        try {
            LList list = scanBody(exp, defs, true);
            if (list.isEmpty()) {
                this.formStack.add(syntaxError("body with no expressions"));
            }
            int ndecls = defs.countNonDynamicDecls();
            if (ndecls != 0) {
                Expression[] inits = new Expression[ndecls];
                int i = ndecls;
                while (true) {
                    i--;
                    if (i < 0) {
                        break;
                    }
                    inits[i] = QuoteExp.undefined_exp;
                }
                defs.inits = inits;
            }
            rewriteBody(list);
            Expression body = makeBody(first, (ScopeExp) null);
            setLineOf(body);
            if (ndecls == 0) {
                return body;
            }
            defs.body = body;
            setLineOf(defs);
            pop(defs);
            popPositionOf(saved);
            return defs;
        } finally {
            pop(defs);
            popPositionOf(saved);
        }
    }

    /* JADX INFO: finally extract failed */
    private void rewriteBody(LList forms) {
        while (forms != LList.Empty) {
            Pair pair = (Pair) forms;
            Object saved = pushPositionOf(pair);
            try {
                rewriteInBody(pair.getCar());
                popPositionOf(saved);
                forms = (LList) pair.getCdr();
            } catch (Throwable th) {
                popPositionOf(saved);
                throw th;
            }
        }
    }

    private Expression makeBody(int first, ScopeExp scope) {
        int nforms = this.formStack.size() - first;
        if (nforms == 0) {
            return QuoteExp.voidExp;
        }
        if (nforms == 1) {
            return (Expression) this.formStack.pop();
        }
        Expression[] exps = new Expression[nforms];
        for (int i = 0; i < nforms; i++) {
            exps[i] = (Expression) this.formStack.elementAt(first + i);
        }
        this.formStack.setSize(first);
        if (scope instanceof ModuleExp) {
            return new ApplyExp((Procedure) AppendValues.appendValues, exps);
        }
        return ((LispLanguage) getLanguage()).makeBody(exps);
    }

    public void noteAccess(Object name, ScopeExp scope) {
        if (this.notedAccess == null) {
            this.notedAccess = new Vector();
        }
        this.notedAccess.addElement(name);
        this.notedAccess.addElement(scope);
    }

    public void processAccesses() {
        if (this.notedAccess != null) {
            int sz = this.notedAccess.size();
            ScopeExp saveScope = this.current_scope;
            for (int i = 0; i < sz; i += 2) {
                Object name = this.notedAccess.elementAt(i);
                ScopeExp scope = (ScopeExp) this.notedAccess.elementAt(i + 1);
                if (this.current_scope != scope) {
                    setCurrentScope(scope);
                }
                Declaration decl = this.lexical.lookup(name, -1);
                if (decl != null && !decl.getFlag(65536)) {
                    decl.getContext().currentLambda().capture(decl);
                    decl.setCanRead(true);
                    decl.setSimple(false);
                    decl.setFlag(524288);
                }
            }
            if (this.current_scope != saveScope) {
                setCurrentScope(saveScope);
            }
        }
    }

    public void finishModule(ModuleExp mexp) {
        String msg2;
        boolean moduleStatic = mexp.isStatic();
        for (Declaration decl = mexp.firstDecl(); decl != null; decl = decl.nextDecl()) {
            if (decl.getFlag(512)) {
                if (decl.getFlag(1024)) {
                    msg2 = "' exported but never defined";
                } else {
                    msg2 = decl.getFlag(2048) ? "' declared static but never defined" : "' declared but never defined";
                }
                error('e', decl, "'", msg2);
            }
            if (mexp.getFlag(16384) || (this.generateMain && !this.immediate)) {
                if (!decl.getFlag(1024)) {
                    decl.setPrivate(true);
                } else if (decl.isPrivate()) {
                    if (decl.getFlag(16777216)) {
                        error('e', decl, "'", "' is declared both private and exported");
                    }
                    decl.setPrivate(false);
                }
            }
            if (moduleStatic) {
                decl.setFlag(2048);
            } else if ((mexp.getFlag(65536) && !decl.getFlag(2048)) || Compilation.moduleStatic < 0 || mexp.getFlag(131072)) {
                decl.setFlag(4096);
            }
        }
    }

    static void vectorReverse(Vector vec, int start, int count) {
        int j = count / 2;
        int last = (start + count) - 1;
        for (int i = 0; i < j; i++) {
            Object tmp = vec.elementAt(start + i);
            vec.setElementAt(vec.elementAt(last - i), start + i);
            vec.setElementAt(tmp, last - i);
        }
    }

    public void resolveModule(ModuleExp mexp) {
        int numPending;
        if (this.pendingImports == null) {
            numPending = 0;
        } else {
            numPending = this.pendingImports.size();
        }
        int i = 0;
        while (i < numPending) {
            int i2 = i + 1;
            ModuleInfo info = (ModuleInfo) this.pendingImports.elementAt(i);
            int i3 = i2 + 1;
            ScopeExp defs = (ScopeExp) this.pendingImports.elementAt(i2);
            int i4 = i3 + 1;
            Expression posExp = (Expression) this.pendingImports.elementAt(i3);
            i = i4 + 1;
            Integer savedSize = (Integer) this.pendingImports.elementAt(i4);
            if (mexp == defs) {
                ReferenceExp referenceExp = new ReferenceExp((Object) null);
                referenceExp.setLine((Compilation) this);
                setLine(posExp);
                int beforeSize = this.formStack.size();
                require.importDefinitions((String) null, info, (Procedure) null, this.formStack, defs, this);
                int desiredPosition = savedSize.intValue();
                if (savedSize.intValue() != beforeSize) {
                    int curSize = this.formStack.size();
                    vectorReverse(this.formStack, desiredPosition, beforeSize - desiredPosition);
                    vectorReverse(this.formStack, beforeSize, curSize - beforeSize);
                    vectorReverse(this.formStack, desiredPosition, curSize - desiredPosition);
                }
                setLine((Expression) referenceExp);
            }
        }
        this.pendingImports = null;
        processAccesses();
        setModule(mexp);
        Compilation save_comp = Compilation.setSaveCurrent(this);
        try {
            rewriteInBody(popForms(this.firstForm));
            mexp.body = makeBody(this.firstForm, mexp);
            if (!this.immediate) {
                this.lexical.pop((ScopeExp) mexp);
            }
        } finally {
            Compilation.restoreCurrent(save_comp);
        }
    }

    public Declaration makeRenamedAlias(Declaration decl, ScopeExp templateScope) {
        return templateScope == null ? decl : makeRenamedAlias(decl.getSymbol(), decl, templateScope);
    }

    public Declaration makeRenamedAlias(Object name, Declaration decl, ScopeExp templateScope) {
        Declaration alias = new Declaration(name);
        alias.setAlias(true);
        alias.setPrivate(true);
        alias.context = templateScope;
        ReferenceExp ref = new ReferenceExp(decl);
        ref.setDontDereference(true);
        alias.noteValue(ref);
        return alias;
    }

    public void pushRenamedAlias(Declaration alias) {
        Declaration decl = getOriginalRef(alias).getBinding();
        ScopeExp templateScope = alias.context;
        decl.setSymbol((Object) null);
        Declaration old = templateScope.lookup(decl.getSymbol());
        if (old != null) {
            templateScope.remove(old);
        }
        templateScope.addDeclaration(alias);
        if (this.renamedAliasStack == null) {
            this.renamedAliasStack = new Stack();
        }
        this.renamedAliasStack.push(old);
        this.renamedAliasStack.push(alias);
        this.renamedAliasStack.push(templateScope);
    }

    public void popRenamedAlias(int count) {
        while (true) {
            count--;
            if (count >= 0) {
                ScopeExp templateScope = (ScopeExp) this.renamedAliasStack.pop();
                Declaration alias = (Declaration) this.renamedAliasStack.pop();
                getOriginalRef(alias).getBinding().setSymbol(alias.getSymbol());
                templateScope.remove(alias);
                Object old = this.renamedAliasStack.pop();
                if (old != null) {
                    templateScope.addDeclaration((Declaration) old);
                }
            } else {
                return;
            }
        }
    }

    public Declaration define(Object name, SyntaxForm nameSyntax, ScopeExp defs) {
        Object declName;
        boolean aliasNeeded = (nameSyntax == null || nameSyntax.getScope() == currentScope()) ? false : true;
        if (aliasNeeded) {
            declName = new String(name.toString());
        } else {
            declName = name;
        }
        Declaration decl = defs.getDefine(declName, 'w', this);
        if (aliasNeeded) {
            nameSyntax.getScope().addDeclaration(makeRenamedAlias(name, decl, nameSyntax.getScope()));
        }
        push(decl);
        return decl;
    }
}
