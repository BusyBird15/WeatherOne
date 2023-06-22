package gnu.kawa.slib;

import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.BeginExp;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.IfExp;
import gnu.expr.Keyword;
import gnu.expr.LambdaExp;
import gnu.expr.Language;
import gnu.expr.LetExp;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.expr.NameLookup;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.SetExp;
import gnu.expr.Special;
import gnu.kawa.functions.Convert;
import gnu.kawa.functions.Format;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.lispexpr.LangObjType;
import gnu.kawa.lispexpr.LispLanguage;
import gnu.kawa.reflect.SlotGet;
import gnu.lists.Consumer;
import gnu.lists.EofClass;
import gnu.lists.LList;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import gnu.math.Numeric;
import gnu.text.Char;
import gnu.text.SourceMessages;
import kawa.lang.Macro;
import kawa.lang.Quote;
import kawa.lang.SyntaxPattern;
import kawa.lang.SyntaxRule;
import kawa.lang.SyntaxRules;
import kawa.lang.Translator;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.standard.Scheme;

/* compiled from: syntaxutils.scm */
public class syntaxutils extends ModuleBody {
    public static final Macro $Prvt$$Ex = Macro.make(Lit15, Lit16, $instance);
    public static final Macro $Prvt$typecase$Pc = Macro.make(Lit13, Lit14, $instance);
    public static final syntaxutils $instance = new syntaxutils();
    static final Keyword Lit0 = Keyword.make("env");
    static final PairWithPosition Lit1 = PairWithPosition.make(Lit21, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 278557);
    static final PairWithPosition Lit10 = PairWithPosition.make(Lit19, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 552972);
    static final PairWithPosition Lit11 = PairWithPosition.make(Lit25, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 626704);
    static final PairWithPosition Lit12 = PairWithPosition.make((SimpleSymbol) new SimpleSymbol(":").readResolve(), LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 634896);
    static final SimpleSymbol Lit13;
    static final SyntaxRules Lit14;
    static final SimpleSymbol Lit15;
    static final SyntaxRules Lit16;
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("expand").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("eql").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol(LispLanguage.quote_sym).readResolve());
    static final Keyword Lit2 = Keyword.make("lang");
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("or").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("begin").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("cond").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("let").readResolve());
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("else").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("as").readResolve());
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("lambda").readResolve());
    static final PairWithPosition Lit3 = PairWithPosition.make((SimpleSymbol) new SimpleSymbol("set").readResolve(), LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 368647);
    static final PairWithPosition Lit4 = PairWithPosition.make(Lit26, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 376839);
    static final PairWithPosition Lit5 = PairWithPosition.make(Lit21, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 409627);
    static final PairWithPosition Lit6 = PairWithPosition.make((SimpleSymbol) new SimpleSymbol("if").readResolve(), LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 417799);
    static final IntNum Lit7 = IntNum.make(0);
    static final IntNum Lit8 = IntNum.make(1);
    static final PairWithPosition Lit9 = PairWithPosition.make(Lit23, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/syntaxutils.scm", 479236);
    public static final ModuleMethod expand = new ModuleMethod($instance, 1, Lit17, -4095);

    /* compiled from: syntaxutils.scm */
    public class frame extends ModuleBody {
        LList pack;
    }

    /* compiled from: syntaxutils.scm */
    public class frame0 extends ModuleBody {
        LList pack;
    }

    /* compiled from: syntaxutils.scm */
    public class frame1 extends ModuleBody {
        LList pack;
    }

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol("!").readResolve();
        Lit15 = simpleSymbol;
        Lit16 = new SyntaxRules(new Object[]{simpleSymbol}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\f\u000f\r\u0017\u0010\b\b", new Object[0], 3), "\u0001\u0001\u0003", "\u0011\u0018\u0004\t\u000b)\u0011\u0018\f\b\u0003\b\u0015\u0013", new Object[]{(SimpleSymbol) new SimpleSymbol("invoke").readResolve(), Lit19}, 1)}, 3);
        SimpleSymbol simpleSymbol2 = (SimpleSymbol) new SimpleSymbol("typecase%").readResolve();
        Lit13 = simpleSymbol2;
        Lit14 = new SyntaxRules(new Object[]{simpleSymbol2, Lit18, Lit20}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007<\f\u0002\r\u000f\b\b\b\r\u0017\u0010\b\b", new Object[]{Boolean.TRUE}, 3), "\u0001\u0003\u0003", "\u0011\u0018\u0004\b\r\u000b", new Object[]{Lit21}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\\,\f\u0002\f\u000f\b\r\u0017\u0010\b\b\r\u001f\u0018\b\b", new Object[]{Lit18}, 4), "\u0001\u0001\u0003\u0003", "\u0011\u0018\u0004yY\u0011\u0018\f\t\u0003\b\u0011\u0018\u0014\b\u000b\b\u0015\u0013\b\u0011\u0018\u001c\b\u0011\u0018$\t\u0003\b\u001d\u001b", new Object[]{Lit22, (SimpleSymbol) new SimpleSymbol("eqv?").readResolve(), Lit19, Lit24, Lit13}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\\,\f\u0002\f\u000f\b\r\u0017\u0010\b\b\r\u001f\u0018\b\b", new Object[]{Lit20}, 4), "\u0001\u0001\u0003\u0003", "\u0011\u0018\u0004\t\u0003)\t\u000b\b\u0015\u0013\b\u001d\u001b", new Object[]{Lit13}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007l<\f\u0002\r\u000f\b\b\b\r\u0017\u0010\b\b\r\u001f\u0018\b\b", new Object[]{Lit20}, 4), "\u0001\u0003\u0003\u0003", "\u0011\u0018\u0004\b\u0011\u0018\f\b\u0011\u0018\u0014\u0011\b\u0003\b\u0011\u0018\u001c\b\u0015\u0013\b\u0011\u0018$\t\u0003I\r\t\u000b\b\u0011\u0018\f\b\u0003\b\u0011\u0018,\b\u0011\u0018$\t\u0003\b\u001d\u001b", new Object[]{Lit23, (SimpleSymbol) new SimpleSymbol("f").readResolve(), Lit26, Lit21, Lit13, Boolean.TRUE}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007<\f\u000f\r\u0017\u0010\b\b\r\u001f\u0018\b\b", new Object[0], 4), "\u0001\u0001\u0003\u0003", "\u0011\u0018\u0004ñ9\u0011\u0018\f\t\u0003\b\u000b\b\u0011\u0018\u0014Q\b\t\u0003\u0011\u0018\u001c\t\u000b\b\u0003\b\u0011\u0018$\b\u0015\u0013\b\u0011\u0018,\b\u0011\u00184\t\u0003\b\u001d\u001b", new Object[]{Lit22, (SimpleSymbol) new SimpleSymbol(GetNamedPart.INSTANCEOF_METHOD_NAME).readResolve(), Lit23, (SimpleSymbol) new SimpleSymbol("::").readResolve(), Lit21, Lit24, Lit13}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018\f\u0007\b", new Object[0], 1), "\u0001", "\u0011\u0018\u0004\u0011\u0018\f\t\u0003\b\u0011\u0018\u0014\u0011\u0018\u001c\b\u0011\u0018$\u0011\u0018,\b\u0003", new Object[]{(SimpleSymbol) new SimpleSymbol("error").readResolve(), "typecase% failed", Lit15, (SimpleSymbol) new SimpleSymbol("getClass").readResolve(), Lit25, (SimpleSymbol) new SimpleSymbol("<object>").readResolve()}, 0)}, 4);
        $instance.run();
    }

    public syntaxutils() {
        ModuleInfo.register(this);
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
    }

    public static Object expand$V(Object sexp, Object[] argsArray) {
        Object env = Keyword.searchForKeyword(argsArray, 0, Lit0);
        if (env == Special.dfault) {
            env = misc.interactionEnvironment();
        }
        return unrewrite(rewriteForm$V(Quote.append$V(new Object[]{Lit1, Quote.consX$V(new Object[]{sexp, LList.Empty})}), new Object[]{Lit0, env}));
    }

    public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
        if (moduleMethod.selector != 1) {
            return super.applyN(moduleMethod, objArr);
        }
        Object obj = objArr[0];
        int length = objArr.length - 1;
        Object[] objArr2 = new Object[length];
        while (true) {
            length--;
            if (length < 0) {
                return expand$V(obj, objArr2);
            }
            objArr2[length] = objArr[length + 1];
        }
    }

    public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
        if (moduleMethod.selector != 1) {
            return super.matchN(moduleMethod, objArr, callContext);
        }
        callContext.values = objArr;
        callContext.proc = moduleMethod;
        callContext.pc = 5;
        return 0;
    }

    static Expression rewriteForm$V(Object exp, Object[] argsArray) {
        Object lang = Keyword.searchForKeyword(argsArray, 0, Lit2);
        if (lang == Special.dfault) {
            lang = Language.getDefaultLanguage();
        }
        Object env = Keyword.searchForKeyword(argsArray, 0, Lit0);
        if (env == Special.dfault) {
            env = misc.interactionEnvironment();
        }
        try {
            try {
                try {
                    Translator translator = new Translator((Language) lang, new SourceMessages(), NameLookup.getInstance((Environment) env, (Language) lang));
                    translator.pushNewModule((String) null);
                    Compilation saved$Mncomp = Compilation.setSaveCurrent(translator);
                    try {
                        return translator.rewrite(exp);
                    } finally {
                        Compilation.restoreCurrent(saved$Mncomp);
                    }
                } catch (ClassCastException e) {
                    throw new WrongType(e, "kawa.lang.Translator.<init>(gnu.expr.Language,gnu.text.SourceMessages,gnu.expr.NameLookup)", 1, lang);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "gnu.expr.NameLookup.getInstance(gnu.mapping.Environment,gnu.expr.Language)", 2, lang);
            }
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "gnu.expr.NameLookup.getInstance(gnu.mapping.Environment,gnu.expr.Language)", 1, env);
        }
    }

    static Object unrewrite(Expression exp) {
        frame closureEnv = new frame();
        if (exp instanceof LetExp) {
            try {
                return unrewriteLet((LetExp) exp);
            } catch (ClassCastException e) {
                throw new WrongType(e, "exp", -2, (Object) exp);
            }
        } else if (exp instanceof QuoteExp) {
            try {
                return unrewriteQuote((QuoteExp) exp);
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "exp", -2, (Object) exp);
            }
        } else if (exp instanceof SetExp) {
            try {
                SetExp exp2 = (SetExp) exp;
                return Quote.append$V(new Object[]{Lit3, Quote.consX$V(new Object[]{exp2.getSymbol(), Quote.consX$V(new Object[]{unrewrite(exp2.getNewValue()), LList.Empty})})});
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "exp", -2, (Object) exp);
            }
        } else if (exp instanceof LambdaExp) {
            try {
                LambdaExp exp3 = (LambdaExp) exp;
                Object[] objArr = new Object[2];
                objArr[0] = Lit4;
                Object[] objArr2 = new Object[2];
                closureEnv.pack = LList.Empty;
                for (Declaration decl = exp3.firstDecl(); decl != null; decl = decl.nextDecl()) {
                    closureEnv.pack = lists.cons(decl.getSymbol(), closureEnv.pack);
                }
                objArr2[0] = lists.reverse$Ex(closureEnv.pack);
                objArr2[1] = Quote.consX$V(new Object[]{unrewrite(exp3.body), LList.Empty});
                objArr[1] = Quote.consX$V(objArr2);
                return Quote.append$V(objArr);
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "exp", -2, (Object) exp);
            }
        } else if (exp instanceof ReferenceExp) {
            try {
                return ((ReferenceExp) exp).getSymbol();
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "exp", -2, (Object) exp);
            }
        } else if (exp instanceof ApplyExp) {
            try {
                return unrewriteApply((ApplyExp) exp);
            } catch (ClassCastException e6) {
                throw new WrongType(e6, "exp", -2, (Object) exp);
            }
        } else if (exp instanceof BeginExp) {
            try {
                return Quote.append$V(new Object[]{Lit5, unrewrite$St(((BeginExp) exp).getExpressions())});
            } catch (ClassCastException e7) {
                throw new WrongType(e7, "exp", -2, (Object) exp);
            }
        } else if (!(exp instanceof IfExp)) {
            return exp;
        } else {
            try {
                IfExp exp4 = (IfExp) exp;
                Object[] objArr3 = new Object[2];
                objArr3[0] = Lit6;
                Object[] objArr4 = new Object[2];
                objArr4[0] = unrewrite(exp4.getTest());
                Object[] objArr5 = new Object[2];
                objArr5[0] = unrewrite(exp4.getThenClause());
                Object[] objArr6 = new Object[2];
                Expression eclause = exp4.getElseClause();
                objArr6[0] = eclause == null ? LList.Empty : LList.list1(unrewrite(eclause));
                objArr6[1] = LList.Empty;
                objArr5[1] = Quote.append$V(objArr6);
                objArr4[1] = Quote.consX$V(objArr5);
                objArr3[1] = Quote.consX$V(objArr4);
                return Quote.append$V(objArr3);
            } catch (ClassCastException e8) {
                throw new WrongType(e8, "exp", -2, (Object) exp);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Object unrewrite$St(gnu.expr.Expression[] r7) {
        /*
            gnu.kawa.slib.syntaxutils$frame0 r0 = new gnu.kawa.slib.syntaxutils$frame0
            r0.<init>()
            gnu.lists.LList r4 = gnu.lists.LList.Empty
            r0.pack = r4
            int r4 = r7.length
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)
            gnu.math.IntNum r5 = Lit7
        L_0x0010:
            gnu.kawa.functions.NumberCompare r4 = kawa.standard.Scheme.numEqu
            java.lang.Object r4 = r4.apply2(r5, r2)
            java.lang.Boolean r6 = java.lang.Boolean.FALSE
            if (r4 != r6) goto L_0x0039
            r4 = r5
            java.lang.Number r4 = (java.lang.Number) r4
            int r4 = r4.intValue()
            r4 = r7[r4]
            java.lang.Object r3 = unrewrite(r4)
            gnu.lists.LList r4 = r0.pack
            gnu.lists.Pair r4 = kawa.lib.lists.cons(r3, r4)
            r0.pack = r4
            gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r6 = Lit8
            java.lang.Object r1 = r4.apply2(r5, r6)
            r5 = r1
            goto L_0x0010
        L_0x0039:
            gnu.lists.LList r4 = r0.pack
            gnu.lists.LList r4 = kawa.lib.lists.reverse$Ex(r4)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.syntaxutils.unrewrite$St(gnu.expr.Expression[]):java.lang.Object");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Object unrewriteLet(gnu.expr.LetExp r13) {
        /*
            r12 = 2
            r11 = 1
            r10 = 0
            gnu.kawa.slib.syntaxutils$frame1 r0 = new gnu.kawa.slib.syntaxutils$frame1
            r0.<init>()
            java.lang.Object[] r6 = new java.lang.Object[r12]
            gnu.lists.PairWithPosition r4 = Lit9
            r6[r10] = r4
            java.lang.Object[] r7 = new java.lang.Object[r12]
            gnu.lists.LList r4 = gnu.lists.LList.Empty
            r0.pack = r4
            gnu.expr.Declaration r1 = r13.firstDecl()
            gnu.math.IntNum r5 = Lit7
        L_0x001a:
            if (r1 == 0) goto L_0x0049
            java.lang.Object r8 = r1.getSymbol()
            gnu.expr.Expression[] r9 = r13.inits
            r4 = r5
            java.lang.Number r4 = (java.lang.Number) r4
            int r4 = r4.intValue()
            r4 = r9[r4]
            java.lang.Object r4 = unrewrite(r4)
            gnu.lists.Pair r3 = gnu.lists.LList.list2(r8, r4)
            gnu.lists.LList r4 = r0.pack
            gnu.lists.Pair r4 = kawa.lib.lists.cons(r3, r4)
            r0.pack = r4
            gnu.expr.Declaration r1 = r1.nextDecl()
            gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r2 = r4.apply2(r5, r8)
            r5 = r2
            goto L_0x001a
        L_0x0049:
            gnu.lists.LList r4 = r0.pack
            gnu.lists.LList r4 = kawa.lib.lists.reverse$Ex(r4)
            r7[r10] = r4
            java.lang.Object[] r4 = new java.lang.Object[r12]
            gnu.expr.Expression r5 = r13.body
            java.lang.Object r5 = unrewrite(r5)
            r4[r10] = r5
            gnu.lists.LList r5 = gnu.lists.LList.Empty
            r4[r11] = r5
            java.lang.Object r4 = kawa.lang.Quote.consX$V(r4)
            r7[r11] = r4
            java.lang.Object r4 = kawa.lang.Quote.consX$V(r7)
            r6[r11] = r4
            java.lang.Object r4 = kawa.lang.Quote.append$V(r6)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.syntaxutils.unrewriteLet(gnu.expr.LetExp):java.lang.Object");
    }

    static Object unrewriteQuote(QuoteExp exp) {
        String name;
        Object val = exp.getValue();
        if (Numeric.asNumericOrNull(val) != null) {
            try {
                return LangObjType.coerceNumeric(val);
            } catch (ClassCastException e) {
                throw new WrongType(e, "val", -2, val);
            }
        } else if (val instanceof Boolean) {
            try {
                return (val != Boolean.FALSE ? 1 : null) != null ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "val", -2, val);
            }
        } else if (val instanceof Char) {
            try {
                return (Char) val;
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "val", -2, val);
            }
        } else if (val instanceof Keyword) {
            try {
                return (Keyword) val;
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "val", -2, val);
            }
        } else if (val instanceof CharSequence) {
            try {
                return (CharSequence) val;
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "val", -2, val);
            }
        } else if (val == Special.undefined || val == EofClass.eofValue) {
            return val;
        } else {
            if (val instanceof Type) {
                try {
                    name = ((Type) val).getName();
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "val", -2, val);
                }
            } else if (val instanceof Class) {
                try {
                    name = ((Class) val).getName();
                } catch (ClassCastException e7) {
                    throw new WrongType(e7, "val", -2, val);
                }
            } else {
                return Quote.append$V(new Object[]{Lit10, Quote.consX$V(new Object[]{val, LList.Empty})});
            }
            return misc.string$To$Symbol(Format.formatToString(0, "<~a>", name));
        }
    }

    static Object unrewriteApply(ApplyExp exp) {
        Declaration fbinding;
        int i;
        Object x;
        int i2;
        Expression fun = exp.getFunction();
        Object args = unrewrite$St(exp.getArgs());
        if (fun instanceof ReferenceExp) {
            try {
                fbinding = ((ReferenceExp) fun).getBinding();
            } catch (ClassCastException e) {
                throw new WrongType(e, "fun", -2, (Object) fun);
            }
        } else {
            fbinding = null;
        }
        Declaration apply$Mnto$Mnargs = Declaration.getDeclarationFromStatic("kawa.standard.Scheme", "applyToArgs");
        Object fval = exp.getFunctionValue();
        if (fbinding == null) {
            i = 1;
        } else {
            i = 0;
        }
        boolean x2 = (i + 1) & true;
        if (x2) {
            if (apply$Mnto$Mnargs == null) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            boolean x3 = (i2 + 1) & true;
            if (x3) {
                if (SlotGet.getSlotValue(false, fbinding, "field", "field", "getField", "isField", Scheme.instance) == apply$Mnto$Mnargs.field) {
                    return args;
                }
            } else if (x3) {
                return args;
            }
        } else if (x2) {
            return args;
        }
        if (fval instanceof Convert) {
            x = Quote.append$V(new Object[]{Lit11, args});
        } else if (fval instanceof GetNamedPart) {
            x = Quote.append$V(new Object[]{Lit12, args});
        } else {
            x = Boolean.FALSE;
        }
        if (x != Boolean.FALSE) {
            return x;
        }
        return Quote.consX$V(new Object[]{unrewrite(fun), args});
    }
}
