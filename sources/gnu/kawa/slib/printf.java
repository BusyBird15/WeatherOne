package gnu.kawa.slib;

import androidx.fragment.app.FragmentTransaction;
import com.google.appinventor.components.runtime.Component;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.expr.Special;
import gnu.kawa.functions.AddOp;
import gnu.kawa.functions.ApplyToArgs;
import gnu.kawa.functions.DivideOp;
import gnu.kawa.functions.IsEqual;
import gnu.kawa.functions.IsEqv;
import gnu.kawa.functions.MultiplyOp;
import gnu.kawa.lispexpr.LangObjType;
import gnu.lists.Consumer;
import gnu.lists.FString;
import gnu.lists.FVector;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.Procedure;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.Complex;
import gnu.math.IntNum;
import gnu.text.Char;
import kawa.lib.characters;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.numbers;
import kawa.lib.ports;
import kawa.lib.rnrs.unicode;
import kawa.lib.strings;
import kawa.lib.vectors;
import kawa.standard.Scheme;
import kawa.standard.append;

/* compiled from: printf.scm */
public class printf extends ModuleBody {
    public static final printf $instance = new printf();
    static final IntNum Lit0 = IntNum.make(-15);
    static final IntNum Lit1 = IntNum.make(0);
    static final PairWithPosition Lit10 = PairWithPosition.make(Lit13, PairWithPosition.make(Lit37, PairWithPosition.make(Lit25, PairWithPosition.make(Lit12, PairWithPosition.make(Lit30, PairWithPosition.make(Lit54, PairWithPosition.make(Lit38, PairWithPosition.make(Lit26, PairWithPosition.make(Lit41, PairWithPosition.make(Lit31, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266284), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266280), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266276), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266272), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266268), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266264), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266260), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266256), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266252), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 266247);
    static final Char Lit11 = Char.make(46);
    static final Char Lit12;
    static final Char Lit13;
    static final IntNum Lit14 = IntNum.make(2);
    static final IntNum Lit15 = IntNum.make(5);
    static final IntNum Lit16 = IntNum.make(9);
    static final IntNum Lit17 = IntNum.make(-1);
    static final Char Lit18 = Char.make(92);
    static final Char Lit19 = Char.make(110);
    static final PairWithPosition Lit2 = PairWithPosition.make(Lit6, PairWithPosition.make(Lit5, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 446503), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 446498);
    static final Char Lit20 = Char.make(78);
    static final Char Lit21 = Char.make(10);
    static final Char Lit22 = Char.make(116);
    static final Char Lit23 = Char.make(84);
    static final Char Lit24 = Char.make(9);
    static final Char Lit25;
    static final Char Lit26 = Char.make(70);
    static final Char Lit27 = Char.make(12);
    static final Char Lit28 = Char.make(37);
    static final Char Lit29 = Char.make(32);
    static final Char Lit3;
    static final Char Lit30 = Char.make(108);
    static final Char Lit31 = Char.make(76);
    static final Char Lit32 = Char.make(104);
    static final PairWithPosition Lit33;
    static final SimpleSymbol Lit34 = ((SimpleSymbol) new SimpleSymbol("printf").readResolve());
    static final Char Lit35 = Char.make(99);
    static final Char Lit36 = Char.make(67);
    static final Char Lit37 = Char.make(115);
    static final Char Lit38 = Char.make(83);
    static final Char Lit39 = Char.make(97);
    static final Char Lit4 = Char.make(64);
    static final Char Lit40 = Char.make(65);
    static final Char Lit41 = Char.make(68);
    static final Char Lit42 = Char.make(73);
    static final Char Lit43 = Char.make(117);
    static final Char Lit44 = Char.make(85);
    static final IntNum Lit45 = IntNum.make(10);
    static final Char Lit46 = Char.make(111);
    static final Char Lit47 = Char.make(79);
    static final IntNum Lit48 = IntNum.make(8);
    static final Char Lit49 = Char.make(120);
    static final Char Lit5 = Char.make(45);
    static final IntNum Lit50 = IntNum.make(16);
    static final Char Lit51 = Char.make(88);
    static final Char Lit52 = Char.make(98);
    static final Char Lit53 = Char.make(66);
    static final Char Lit54 = Char.make(69);
    static final Char Lit55 = Char.make(103);
    static final Char Lit56 = Char.make(71);
    static final Char Lit57 = Char.make(107);
    static final Char Lit58 = Char.make(75);
    static final IntNum Lit59 = IntNum.make(6);
    static final Char Lit6 = Char.make(43);
    static final IntNum Lit60 = IntNum.make(-10);
    static final IntNum Lit61 = IntNum.make(3);
    static final FVector Lit62 = FVector.make("y", "z", "a", "f", "p", "n", "u", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y");
    static final PairWithPosition Lit63 = PairWithPosition.make("i", LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1634315);
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("format-real").readResolve());
    static final Char Lit65 = Char.make(63);
    static final Char Lit66 = Char.make(42);
    static final SimpleSymbol Lit67 = ((SimpleSymbol) new SimpleSymbol("pad").readResolve());
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("sprintf").readResolve());
    static final SimpleSymbol Lit69 = ((SimpleSymbol) new SimpleSymbol("stdio:parse-float").readResolve());
    static final IntNum Lit7 = IntNum.make(1);
    static final SimpleSymbol Lit70 = ((SimpleSymbol) new SimpleSymbol("stdio:round-string").readResolve());
    static final SimpleSymbol Lit71 = ((SimpleSymbol) new SimpleSymbol("stdio:iprintf").readResolve());
    static final SimpleSymbol Lit72 = ((SimpleSymbol) new SimpleSymbol("fprintf").readResolve());
    static final Char Lit8 = Char.make(35);
    static final Char Lit9 = Char.make(48);
    public static final ModuleMethod fprintf;
    public static final ModuleMethod printf;
    public static final ModuleMethod sprintf;
    public static final boolean stdio$Clhex$Mnupper$Mncase$Qu = false;
    public static final ModuleMethod stdio$Cliprintf;
    public static final ModuleMethod stdio$Clparse$Mnfloat;
    public static final ModuleMethod stdio$Clround$Mnstring;

    static {
        Char charR = Lit35;
        Char charR2 = Lit37;
        Char charR3 = Lit39;
        Char make = Char.make(100);
        Lit12 = make;
        Char make2 = Char.make(105);
        Lit3 = make2;
        Char charR4 = Lit43;
        Char charR5 = Lit46;
        Char charR6 = Lit49;
        Char charR7 = Lit52;
        Char make3 = Char.make(102);
        Lit25 = make3;
        Char make4 = Char.make(101);
        Lit13 = make4;
        Lit33 = PairWithPosition.make(charR, PairWithPosition.make(charR2, PairWithPosition.make(charR3, PairWithPosition.make(make, PairWithPosition.make(make2, PairWithPosition.make(charR4, PairWithPosition.make(charR5, PairWithPosition.make(charR6, PairWithPosition.make(charR7, PairWithPosition.make(make3, PairWithPosition.make(make4, PairWithPosition.make(Lit55, PairWithPosition.make(Lit57, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1781780), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1781776), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1781772), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1781768), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777704), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777700), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777696), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777692), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777688), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777684), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777680), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777676), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm", 1777671);
        printf printf2 = $instance;
        stdio$Clparse$Mnfloat = new ModuleMethod(printf2, 22, Lit69, 8194);
        stdio$Clround$Mnstring = new ModuleMethod(printf2, 23, Lit70, 12291);
        stdio$Cliprintf = new ModuleMethod(printf2, 24, Lit71, -4094);
        fprintf = new ModuleMethod(printf2, 25, Lit72, -4094);
        printf = new ModuleMethod(printf2, 26, Lit34, -4095);
        sprintf = new ModuleMethod(printf2, 27, Lit68, -4094);
        $instance.run();
    }

    public printf() {
        ModuleInfo.register(this);
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
        stdio$Clhex$Mnupper$Mncase$Qu = strings.isString$Eq("-F", numbers.number$To$String(Lit0, 16));
    }

    public static Object stdio$ClParseFloat(Object str, Object proc) {
        frame frame14 = new frame();
        frame14.str = str;
        frame14.proc = proc;
        Object obj = frame14.str;
        try {
            frame14.n = strings.stringLength((CharSequence) obj);
            return frame14.lambda4real(Lit1, frame14.lambda$Fn1);
        } catch (ClassCastException e) {
            throw new WrongType(e, "string-length", 1, obj);
        }
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        return moduleMethod.selector == 22 ? stdio$ClParseFloat(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        if (moduleMethod.selector != 22) {
            return super.match2(moduleMethod, obj, obj2, callContext);
        }
        callContext.value1 = obj;
        callContext.value2 = obj2;
        callContext.proc = moduleMethod;
        callContext.pc = 2;
        return 0;
    }

    /* compiled from: printf.scm */
    public class frame extends ModuleBody {
        final ModuleMethod lambda$Fn1;
        int n;
        Object proc;
        Object str;

        public frame() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 12, (Object) null, 16388);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:106");
            this.lambda$Fn1 = moduleMethod;
        }

        public static Boolean lambda1parseError() {
            return Boolean.FALSE;
        }

        public Object lambda2sign(Object i, Object cont) {
            if (Scheme.numLss.apply2(i, Integer.valueOf(this.n)) == Boolean.FALSE) {
                return Values.empty;
            }
            Object obj = this.str;
            try {
                try {
                    char c = strings.stringRef((CharSequence) obj, ((Number) i).intValue());
                    Object x = Scheme.isEqv.apply2(Char.make(c), printf.Lit5);
                    if (x == Boolean.FALSE ? Scheme.isEqv.apply2(Char.make(c), printf.Lit6) != Boolean.FALSE : x != Boolean.FALSE) {
                        return Scheme.applyToArgs.apply3(cont, AddOp.$Pl.apply2(i, printf.Lit7), Char.make(c));
                    }
                    return Scheme.applyToArgs.apply3(cont, i, printf.Lit6);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-ref", 2, i);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-ref", 1, obj);
            }
        }

        public Object lambda3digits(Object i, Object cont) {
            Object substring;
            Object j = i;
            while (true) {
                Object apply2 = Scheme.numGEq.apply2(j, Integer.valueOf(this.n));
                try {
                    boolean x = ((Boolean) apply2).booleanValue();
                    if (x) {
                        if (x) {
                            break;
                        }
                    } else {
                        Object obj = this.str;
                        try {
                            try {
                                boolean x2 = unicode.isCharNumeric(Char.make(strings.stringRef((CharSequence) obj, ((Number) j).intValue())));
                                if (!x2) {
                                    Char charR = printf.Lit8;
                                    Object obj2 = this.str;
                                    try {
                                        try {
                                            if (!characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) j).intValue())))) {
                                                break;
                                            }
                                        } catch (ClassCastException e) {
                                            throw new WrongType(e, "string-ref", 2, j);
                                        }
                                    } catch (ClassCastException e2) {
                                        throw new WrongType(e2, "string-ref", 1, obj2);
                                    }
                                } else if (!x2) {
                                    break;
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "string-ref", 2, j);
                            }
                        } catch (ClassCastException e4) {
                            throw new WrongType(e4, "string-ref", 1, obj);
                        }
                    }
                    j = AddOp.$Pl.apply2(j, printf.Lit7);
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "x", -2, apply2);
                }
            }
            ApplyToArgs applyToArgs = Scheme.applyToArgs;
            if (Scheme.numEqu.apply2(i, j) != Boolean.FALSE) {
                substring = Component.TYPEFACE_DEFAULT;
            } else {
                Object obj3 = this.str;
                try {
                    try {
                        try {
                            substring = strings.substring((CharSequence) obj3, ((Number) i).intValue(), ((Number) j).intValue());
                        } catch (ClassCastException e6) {
                            throw new WrongType(e6, "substring", 3, j);
                        }
                    } catch (ClassCastException e7) {
                        throw new WrongType(e7, "substring", 2, i);
                    }
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "substring", 1, obj3);
                }
            }
            return applyToArgs.apply3(cont, j, substring);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a1, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(gnu.text.Char.make(r1), gnu.kawa.slib.printf.Lit11) == java.lang.Boolean.FALSE) goto L_0x00bb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r11, r10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            return lambda1parseError();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object lambda4real(java.lang.Object r10, java.lang.Object r11) {
            /*
                r9 = this;
                r8 = 2
                r7 = 1
                gnu.kawa.slib.printf$frame2 r3 = new gnu.kawa.slib.printf$frame2
                r3.<init>()
                r3.staticLink = r9
                r3.cont = r11
                gnu.expr.ModuleMethod r11 = r3.lambda$Fn5
            L_0x000d:
                gnu.kawa.functions.NumberCompare r3 = kawa.standard.Scheme.numLss
                int r4 = r9.n
                int r4 = r4 + -1
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                java.lang.Object r4 = r3.apply2(r10, r4)
                r0 = r4
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x00c0 }
                r3 = r0
                boolean r2 = r3.booleanValue()     // Catch:{ ClassCastException -> 0x00c0 }
                if (r2 == 0) goto L_0x0076
                gnu.text.Char r5 = gnu.kawa.slib.printf.Lit8
                java.lang.Object r3 = r9.str
                java.lang.CharSequence r3 = (java.lang.CharSequence) r3     // Catch:{ ClassCastException -> 0x00ca }
                r0 = r10
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00d3 }
                r4 = r0
                int r4 = r4.intValue()     // Catch:{ ClassCastException -> 0x00d3 }
                char r3 = kawa.lib.strings.stringRef(r3, r4)
                gnu.text.Char r3 = gnu.text.Char.make(r3)
                boolean r3 = kawa.lib.characters.isChar$Eq(r5, r3)
                if (r3 == 0) goto L_0x0078
            L_0x0041:
                java.lang.Object r3 = r9.str
                java.lang.CharSequence r3 = (java.lang.CharSequence) r3     // Catch:{ ClassCastException -> 0x00dc }
                gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
                gnu.math.IntNum r5 = gnu.kawa.slib.printf.Lit7
                java.lang.Object r5 = r4.apply2(r10, r5)
                r0 = r5
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00e5 }
                r4 = r0
                int r4 = r4.intValue()     // Catch:{ ClassCastException -> 0x00e5 }
                char r1 = kawa.lib.strings.stringRef(r3, r4)
                gnu.kawa.functions.IsEqv r3 = kawa.standard.Scheme.isEqv
                gnu.text.Char r4 = gnu.text.Char.make(r1)
                gnu.text.Char r5 = gnu.kawa.slib.printf.Lit12
                java.lang.Object r2 = r3.apply2(r4, r5)
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                if (r2 == r3) goto L_0x007f
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                if (r2 == r3) goto L_0x0093
            L_0x006d:
                gnu.kawa.functions.AddOp r3 = gnu.kawa.functions.AddOp.$Pl
                gnu.math.IntNum r4 = gnu.kawa.slib.printf.Lit14
                java.lang.Object r10 = r3.apply2(r10, r4)
                goto L_0x000d
            L_0x0076:
                if (r2 != 0) goto L_0x0041
            L_0x0078:
                gnu.kawa.functions.ApplyToArgs r3 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r3 = r3.apply2(r11, r10)
            L_0x007e:
                return r3
            L_0x007f:
                gnu.kawa.functions.IsEqv r3 = kawa.standard.Scheme.isEqv
                gnu.text.Char r4 = gnu.text.Char.make(r1)
                gnu.text.Char r5 = gnu.kawa.slib.printf.Lit3
                java.lang.Object r2 = r3.apply2(r4, r5)
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                if (r2 == r3) goto L_0x00aa
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                if (r2 != r3) goto L_0x006d
            L_0x0093:
                gnu.kawa.functions.IsEqv r3 = kawa.standard.Scheme.isEqv
                gnu.text.Char r4 = gnu.text.Char.make(r1)
                gnu.text.Char r5 = gnu.kawa.slib.printf.Lit11
                java.lang.Object r3 = r3.apply2(r4, r5)
                java.lang.Boolean r4 = java.lang.Boolean.FALSE
                if (r3 == r4) goto L_0x00bb
                gnu.kawa.functions.ApplyToArgs r3 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r3 = r3.apply2(r11, r10)
                goto L_0x007e
            L_0x00aa:
                gnu.kawa.functions.IsEqv r3 = kawa.standard.Scheme.isEqv
                gnu.text.Char r4 = gnu.text.Char.make(r1)
                gnu.text.Char r5 = gnu.kawa.slib.printf.Lit13
                java.lang.Object r3 = r3.apply2(r4, r5)
                java.lang.Boolean r4 = java.lang.Boolean.FALSE
                if (r3 == r4) goto L_0x0093
                goto L_0x006d
            L_0x00bb:
                java.lang.Boolean r3 = lambda1parseError()
                goto L_0x007e
            L_0x00c0:
                r3 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "x"
                r7 = -2
                r5.<init>((java.lang.ClassCastException) r3, (java.lang.String) r6, (int) r7, (java.lang.Object) r4)
                throw r5
            L_0x00ca:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-ref"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r7, (java.lang.Object) r3)
                throw r5
            L_0x00d3:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "string-ref"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r8, (java.lang.Object) r10)
                throw r4
            L_0x00dc:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-ref"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r7, (java.lang.Object) r3)
                throw r5
            L_0x00e5:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-ref"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r6, (int) r8, (java.lang.Object) r5)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.printf.frame.lambda4real(java.lang.Object, java.lang.Object):java.lang.Object");
        }

        public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
            return moduleMethod.selector == 12 ? lambda5(obj, obj2, obj3, obj4) : super.apply4(moduleMethod, obj, obj2, obj3, obj4);
        }

        /* access modifiers changed from: package-private */
        public Object lambda5(Object i, Object sgn, Object digs, Object ex) {
            frame0 frame0 = new frame0();
            frame0.staticLink = this;
            frame0.sgn = sgn;
            frame0.digs = digs;
            frame0.ex = ex;
            if (Scheme.numEqu.apply2(i, Integer.valueOf(this.n)) != Boolean.FALSE) {
                return Scheme.applyToArgs.apply4(this.proc, frame0.sgn, frame0.digs, frame0.ex);
            }
            Object obj = this.str;
            try {
                try {
                    if (lists.memv(Char.make(strings.stringRef((CharSequence) obj, ((Number) i).intValue())), printf.Lit2) != Boolean.FALSE) {
                        return lambda4real(i, frame0.lambda$Fn2);
                    }
                    IsEqv isEqv = Scheme.isEqv;
                    Object obj2 = this.str;
                    try {
                        try {
                            if (isEqv.apply2(Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())), printf.Lit4) == Boolean.FALSE) {
                                return Boolean.FALSE;
                            }
                            Object obj3 = this.str;
                            try {
                                frame0.num = numbers.string$To$Number((CharSequence) obj3);
                                if (frame0.num == Boolean.FALSE) {
                                    return lambda1parseError();
                                }
                                Object obj4 = frame0.num;
                                try {
                                    return printf.stdio$ClParseFloat(numbers.number$To$String(numbers.realPart((Complex) obj4)), frame0.lambda$Fn3);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "real-part", 1, obj4);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string->number", 1, obj3);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "string-ref", 2, i);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "string-ref", 1, obj2);
                    }
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "string-ref", 2, i);
                }
            } catch (ClassCastException e6) {
                throw new WrongType(e6, "string-ref", 1, obj);
            }
        }

        public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
            if (moduleMethod.selector != 12) {
                return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.value4 = obj4;
            callContext.proc = moduleMethod;
            callContext.pc = 4;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame6 extends ModuleBody {
        Object cont;
        final ModuleMethod lambda$Fn11;
        frame staticLink;

        public frame6() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 5, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:67");
            this.lambda$Fn11 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 5 ? lambda15(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda15(Object i, Object sgn) {
            frame7 frame7 = new frame7();
            frame7.staticLink = this;
            frame7.sgn = sgn;
            return this.staticLink.lambda3digits(i, frame7.lambda$Fn12);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 5) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame7 extends ModuleBody {
        final ModuleMethod lambda$Fn12;
        Object sgn;
        frame6 staticLink;

        public frame7() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 4, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:69");
            this.lambda$Fn12 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 4 ? lambda16(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda16(Object i, Object digs) {
            Object string$To$Number;
            ApplyToArgs applyToArgs = Scheme.applyToArgs;
            Object obj = this.staticLink.cont;
            Char charR = printf.Lit5;
            Object obj2 = this.sgn;
            try {
                if (characters.isChar$Eq(charR, (Char) obj2)) {
                    try {
                        string$To$Number = AddOp.$Mn.apply1(numbers.string$To$Number((CharSequence) digs));
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string->number", 1, digs);
                    }
                } else {
                    try {
                        string$To$Number = numbers.string$To$Number((CharSequence) digs);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string->number", 1, digs);
                    }
                }
                return applyToArgs.apply3(obj, i, string$To$Number);
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "char=?", 2, obj2);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 4) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame2 extends ModuleBody {
        Object cont;
        final ModuleMethod lambda$Fn5;
        final ModuleMethod lambda$Fn6;
        frame staticLink;

        public frame2() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 10, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:81");
            this.lambda$Fn6 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 11, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:78");
            this.lambda$Fn5 = moduleMethod2;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 11 ? lambda9(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda9(Object i) {
            return this.staticLink.lambda2sign(i, this.lambda$Fn6);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 11) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 10 ? lambda10(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda10(Object i, Object sgn) {
            frame3 frame3 = new frame3();
            frame3.staticLink = this;
            frame3.sgn = sgn;
            return this.staticLink.lambda3digits(i, frame3.lambda$Fn7);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 10) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame3 extends ModuleBody {
        final ModuleMethod lambda$Fn7;
        Object sgn;
        frame2 staticLink;

        public frame3() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 9, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:84");
            this.lambda$Fn7 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 9 ? lambda11(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0043, code lost:
            if (kawa.lib.characters.isChar$Eq(r5, gnu.text.Char.make(kawa.lib.strings.stringRef((java.lang.CharSequence) r2, ((java.lang.Number) r8).intValue()))) != false) goto L_0x0045;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0054, code lost:
            if (r1 == false) goto L_0x0056;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r4, r8);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object lambda11(java.lang.Object r8, java.lang.Object r9) {
            /*
                r7 = this;
                gnu.kawa.slib.printf$frame4 r2 = new gnu.kawa.slib.printf$frame4
                r2.<init>()
                r2.staticLink = r7
                r2.idigs = r9
                gnu.expr.ModuleMethod r4 = r2.lambda$Fn8
                gnu.kawa.functions.NumberCompare r2 = kawa.standard.Scheme.numLss
                gnu.kawa.slib.printf$frame2 r3 = r7.staticLink
                gnu.kawa.slib.printf$frame r3 = r3.staticLink
                int r3 = r3.n
                java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
                java.lang.Object r3 = r2.apply2(r8, r3)
                r0 = r3
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x005d }
                r2 = r0
                boolean r1 = r2.booleanValue()     // Catch:{ ClassCastException -> 0x005d }
                if (r1 == 0) goto L_0x0054
                gnu.text.Char r5 = gnu.kawa.slib.printf.Lit11
                gnu.kawa.slib.printf$frame2 r2 = r7.staticLink
                gnu.kawa.slib.printf$frame r2 = r2.staticLink
                java.lang.Object r2 = r2.str
                java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ ClassCastException -> 0x0067 }
                r0 = r8
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0071 }
                r3 = r0
                int r3 = r3.intValue()     // Catch:{ ClassCastException -> 0x0071 }
                char r2 = kawa.lib.strings.stringRef(r2, r3)
                gnu.text.Char r2 = gnu.text.Char.make(r2)
                boolean r2 = kawa.lib.characters.isChar$Eq(r5, r2)
                if (r2 == 0) goto L_0x0056
            L_0x0045:
                gnu.kawa.functions.ApplyToArgs r2 = kawa.standard.Scheme.applyToArgs
                gnu.kawa.functions.AddOp r3 = gnu.kawa.functions.AddOp.$Pl
                gnu.math.IntNum r5 = gnu.kawa.slib.printf.Lit7
                java.lang.Object r3 = r3.apply2(r8, r5)
                java.lang.Object r2 = r2.apply2(r4, r3)
            L_0x0053:
                return r2
            L_0x0054:
                if (r1 != 0) goto L_0x0045
            L_0x0056:
                gnu.kawa.functions.ApplyToArgs r2 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r2.apply2(r4, r8)
                goto L_0x0053
            L_0x005d:
                r2 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "x"
                r6 = -2
                r4.<init>((java.lang.ClassCastException) r2, (java.lang.String) r5, (int) r6, (java.lang.Object) r3)
                throw r4
            L_0x0067:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "string-ref"
                r6 = 1
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r6, (java.lang.Object) r2)
                throw r4
            L_0x0071:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r5 = 2
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r8)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.printf.frame3.lambda11(java.lang.Object, java.lang.Object):java.lang.Object");
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 9) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame4 extends ModuleBody {
        Object idigs;
        final ModuleMethod lambda$Fn8;
        final ModuleMethod lambda$Fn9;
        frame3 staticLink;

        public frame4() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 7, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:90");
            this.lambda$Fn9 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 8, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:87");
            this.lambda$Fn8 = moduleMethod2;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 8 ? lambda12(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda12(Object i) {
            return this.staticLink.staticLink.staticLink.lambda3digits(i, this.lambda$Fn9);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 8) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 7 ? lambda13(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda13(Object i, Object fdigs) {
            frame5 frame5 = new frame5();
            frame5.staticLink = this;
            frame5.fdigs = fdigs;
            ModuleMethod moduleMethod = frame5.lambda$Fn10;
            frame closureEnv = this.staticLink.staticLink.staticLink;
            frame6 frame6 = new frame6();
            frame6.staticLink = closureEnv;
            frame6.cont = moduleMethod;
            if (Scheme.numGEq.apply2(i, Integer.valueOf(this.staticLink.staticLink.staticLink.n)) != Boolean.FALSE) {
                return Scheme.applyToArgs.apply3(frame6.cont, i, printf.Lit1);
            }
            Object obj = this.staticLink.staticLink.staticLink.str;
            try {
                try {
                    if (lists.memv(Char.make(strings.stringRef((CharSequence) obj, ((Number) i).intValue())), printf.Lit10) != Boolean.FALSE) {
                        return this.staticLink.staticLink.staticLink.lambda2sign(AddOp.$Pl.apply2(i, printf.Lit7), frame6.lambda$Fn11);
                    }
                    return Scheme.applyToArgs.apply3(frame6.cont, i, printf.Lit1);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-ref", 2, i);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-ref", 1, obj);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 7) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame5 extends ModuleBody {
        Object fdigs;
        final ModuleMethod lambda$Fn10;
        frame4 staticLink;

        public frame5() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 6, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:92");
            this.lambda$Fn10 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 6 ? lambda14(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda14(Object i, Object ex) {
            FString digs = strings.stringAppend(Component.TYPEFACE_DEFAULT, this.staticLink.idigs, this.fdigs);
            int ndigs = strings.stringLength(digs);
            Object obj = printf.Lit7;
            AddOp addOp = AddOp.$Pl;
            Object obj2 = this.staticLink.idigs;
            try {
                Object ex2 = addOp.apply2(ex, Integer.valueOf(strings.stringLength((CharSequence) obj2)));
                while (Scheme.numGEq.apply2(obj, Integer.valueOf(ndigs)) == Boolean.FALSE) {
                    try {
                        if (characters.isChar$Eq(printf.Lit9, Char.make(strings.stringRef(digs, ((Number) obj).intValue())))) {
                            obj = AddOp.$Pl.apply2(obj, printf.Lit7);
                            ex2 = AddOp.$Mn.apply2(ex2, printf.Lit7);
                        } else {
                            ApplyToArgs applyToArgs = Scheme.applyToArgs;
                            Object[] objArr = new Object[5];
                            objArr[0] = this.staticLink.staticLink.staticLink.cont;
                            objArr[1] = i;
                            objArr[2] = this.staticLink.staticLink.sgn;
                            Object apply2 = AddOp.$Mn.apply2(obj, printf.Lit7);
                            try {
                                objArr[3] = strings.substring(digs, ((Number) apply2).intValue(), ndigs);
                                objArr[4] = ex2;
                                return applyToArgs.applyN(objArr);
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "substring", 2, apply2);
                            }
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-ref", 2, obj);
                    }
                }
                return Scheme.applyToArgs.applyN(new Object[]{this.staticLink.staticLink.staticLink.cont, i, this.staticLink.staticLink.sgn, Component.TYPEFACE_DEFAULT, printf.Lit7});
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "string-length", 1, obj2);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 6) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame0 extends ModuleBody {
        Object digs;
        Object ex;
        final ModuleMethod lambda$Fn2;
        final ModuleMethod lambda$Fn3;
        Object num;
        Object sgn;
        frame staticLink;

        public frame0() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 2, (Object) null, 16388);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:111");
            this.lambda$Fn2 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 3, (Object) null, 12291);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:123");
            this.lambda$Fn3 = moduleMethod2;
        }

        public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
            return moduleMethod.selector == 2 ? lambda6(obj, obj2, obj3, obj4) : super.apply4(moduleMethod, obj, obj2, obj3, obj4);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0038, code lost:
            if (kawa.lib.rnrs.unicode.isCharCi$Eq(r4, gnu.text.Char.make(kawa.lib.strings.stringRef((java.lang.CharSequence) r2, ((java.lang.Number) r9).intValue()))) != false) goto L_0x003a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0061, code lost:
            if (r1 == false) goto L_0x0063;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            return gnu.kawa.slib.printf.frame.lambda1parseError();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object lambda6(java.lang.Object r9, java.lang.Object r10, java.lang.Object r11, java.lang.Object r12) {
            /*
                r8 = this;
                r7 = 2
                r6 = 1
                gnu.kawa.functions.NumberCompare r2 = kawa.standard.Scheme.numEqu
                gnu.kawa.slib.printf$frame r3 = r8.staticLink
                int r3 = r3.n
                int r3 = r3 + -1
                java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
                java.lang.Object r3 = r2.apply2(r9, r3)
                r0 = r3
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x0068 }
                r2 = r0
                boolean r1 = r2.booleanValue()     // Catch:{ ClassCastException -> 0x0068 }
                if (r1 == 0) goto L_0x0061
                gnu.text.Char r4 = gnu.kawa.slib.printf.Lit3
                gnu.kawa.slib.printf$frame r2 = r8.staticLink
                java.lang.Object r2 = r2.str
                java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ ClassCastException -> 0x0072 }
                r0 = r9
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x007b }
                r3 = r0
                int r3 = r3.intValue()     // Catch:{ ClassCastException -> 0x007b }
                char r2 = kawa.lib.strings.stringRef(r2, r3)
                gnu.text.Char r2 = gnu.text.Char.make(r2)
                boolean r2 = kawa.lib.rnrs.unicode.isCharCi$Eq(r4, r2)
                if (r2 == 0) goto L_0x0063
            L_0x003a:
                gnu.kawa.functions.ApplyToArgs r2 = kawa.standard.Scheme.applyToArgs
                r3 = 7
                java.lang.Object[] r3 = new java.lang.Object[r3]
                r4 = 0
                gnu.kawa.slib.printf$frame r5 = r8.staticLink
                java.lang.Object r5 = r5.proc
                r3[r4] = r5
                java.lang.Object r4 = r8.sgn
                r3[r6] = r4
                java.lang.Object r4 = r8.digs
                r3[r7] = r4
                r4 = 3
                java.lang.Object r5 = r8.ex
                r3[r4] = r5
                r4 = 4
                r3[r4] = r10
                r4 = 5
                r3[r4] = r11
                r4 = 6
                r3[r4] = r12
                java.lang.Object r2 = r2.applyN(r3)
            L_0x0060:
                return r2
            L_0x0061:
                if (r1 != 0) goto L_0x003a
            L_0x0063:
                java.lang.Boolean r2 = gnu.kawa.slib.printf.frame.lambda1parseError()
                goto L_0x0060
            L_0x0068:
                r2 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "x"
                r6 = -2
                r4.<init>((java.lang.ClassCastException) r2, (java.lang.String) r5, (int) r6, (java.lang.Object) r3)
                throw r4
            L_0x0072:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "string-ref"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r6, (java.lang.Object) r2)
                throw r4
            L_0x007b:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r7, (java.lang.Object) r9)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.printf.frame0.lambda6(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
        }

        public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
            if (moduleMethod.selector != 2) {
                return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.value4 = obj4;
            callContext.proc = moduleMethod;
            callContext.pc = 4;
            return 0;
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 3 ? lambda7(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        /* access modifiers changed from: package-private */
        public Object lambda7(Object sgn2, Object digs2, Object ex2) {
            frame1 frame1 = new frame1();
            frame1.staticLink = this;
            frame1.sgn = sgn2;
            frame1.digs = digs2;
            frame1.ex = ex2;
            Object obj = this.num;
            try {
                return printf.stdio$ClParseFloat(numbers.number$To$String(numbers.imagPart((Complex) obj)), frame1.lambda$Fn4);
            } catch (ClassCastException e) {
                throw new WrongType(e, "imag-part", 1, obj);
            }
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 3) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }
    }

    /* compiled from: printf.scm */
    public class frame1 extends ModuleBody {
        Object digs;
        Object ex;
        final ModuleMethod lambda$Fn4;
        Object sgn;
        frame0 staticLink;

        public frame1() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 1, (Object) null, 12291);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:126");
            this.lambda$Fn4 = moduleMethod;
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 1 ? lambda8(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        /* access modifiers changed from: package-private */
        public Object lambda8(Object im$Mnsgn, Object im$Mndigs, Object im$Mnex) {
            return Scheme.applyToArgs.applyN(new Object[]{this.staticLink.staticLink.proc, this.sgn, this.digs, this.ex, im$Mnsgn, im$Mndigs, im$Mnex});
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 1) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0112, code lost:
        if (r8 != false) goto L_0x0114;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x017d, code lost:
        if ((((java.lang.Number) r1.lambda17dig(r15)).intValue() & 1) != 0) goto L_0x0114;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0195, code lost:
        if (r8 != false) goto L_0x0114;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object stdio$ClRoundString(java.lang.CharSequence r14, java.lang.Object r15, java.lang.Object r16) {
        /*
            gnu.kawa.slib.printf$frame8 r1 = new gnu.kawa.slib.printf$frame8
            r1.<init>()
            r1.str = r14
            java.lang.CharSequence r9 = r1.str
            int r9 = kawa.lib.strings.stringLength(r9)
            int r5 = r9 + -1
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numLss
            gnu.math.IntNum r10 = Lit1
            java.lang.Object r9 = r9.apply2(r15, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x005d
            java.lang.String r7 = ""
        L_0x001d:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            r0 = r16
            if (r0 == r9) goto L_0x005c
            r0 = r7
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0248 }
            r9 = r0
            int r9 = kawa.lib.strings.stringLength(r9)
            int r9 = r9 + -1
            java.lang.Integer r3 = java.lang.Integer.valueOf(r9)
        L_0x0031:
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numLEq
            r0 = r16
            java.lang.Object r10 = r9.apply2(r3, r0)
            r0 = r10
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x0252 }
            r9 = r0
            boolean r8 = r9.booleanValue()     // Catch:{ ClassCastException -> 0x0252 }
            if (r8 == 0) goto L_0x01b4
            if (r8 == 0) goto L_0x01d0
        L_0x0045:
            java.lang.CharSequence r7 = (java.lang.CharSequence) r7     // Catch:{ ClassCastException -> 0x0270 }
            r11 = 0
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit7
            java.lang.Object r10 = r9.apply2(r3, r10)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x027a }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x027a }
            java.lang.CharSequence r7 = kawa.lib.strings.substring(r7, r11, r9)
        L_0x005c:
            return r7
        L_0x005d:
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numEqu
            java.lang.Integer r10 = java.lang.Integer.valueOf(r5)
            java.lang.Object r9 = r9.apply2(r10, r15)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x006e
            java.lang.CharSequence r7 = r1.str
            goto L_0x001d
        L_0x006e:
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numLss
            java.lang.Integer r10 = java.lang.Integer.valueOf(r5)
            java.lang.Object r9 = r9.apply2(r10, r15)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x00dd
            r9 = 2
            java.lang.Object[] r9 = new java.lang.Object[r9]
            r10 = 0
            gnu.math.IntNum r11 = Lit1
            r9[r10] = r11
            r10 = 1
            gnu.kawa.functions.AddOp r11 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Boolean r12 = java.lang.Boolean.FALSE
            r0 = r16
            if (r0 == r12) goto L_0x008f
            r15 = r16
        L_0x008f:
            java.lang.Integer r12 = java.lang.Integer.valueOf(r5)
            java.lang.Object r11 = r11.apply2(r15, r12)
            r9[r10] = r11
            java.lang.Object r6 = kawa.lib.numbers.max(r9)
            r0 = r6
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01da }
            r9 = r0
            boolean r9 = kawa.lib.numbers.isZero(r9)
            if (r9 == 0) goto L_0x00ac
            java.lang.CharSequence r9 = r1.str
        L_0x00a9:
            r7 = r9
            goto L_0x001d
        L_0x00ac:
            r9 = 2
            java.lang.Object[] r10 = new java.lang.Object[r9]
            r9 = 0
            java.lang.CharSequence r11 = r1.str
            r10[r9] = r11
            r11 = 1
            r0 = r6
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01e4 }
            r9 = r0
            int r12 = r9.intValue()     // Catch:{ ClassCastException -> 0x01e4 }
            java.lang.CharSequence r9 = r1.str
            char r9 = kawa.lib.strings.stringRef(r9, r5)
            gnu.text.Char r9 = gnu.text.Char.make(r9)
            boolean r9 = kawa.lib.rnrs.unicode.isCharNumeric(r9)
            if (r9 == 0) goto L_0x00da
            gnu.text.Char r9 = Lit9
        L_0x00cf:
            java.lang.CharSequence r9 = kawa.lib.strings.makeString(r12, r9)
            r10[r11] = r9
            gnu.lists.FString r9 = kawa.lib.strings.stringAppend(r10)
            goto L_0x00a9
        L_0x00da:
            gnu.text.Char r9 = Lit8
            goto L_0x00cf
        L_0x00dd:
            java.lang.CharSequence r11 = r1.str
            r12 = 0
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit7
            java.lang.Object r10 = r9.apply2(r15, r10)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01ee }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x01ee }
            java.lang.CharSequence r7 = kawa.lib.strings.substring(r11, r12, r9)
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit7
            java.lang.Object r9 = r9.apply2(r10, r15)
            java.lang.Object r4 = r1.lambda17dig(r9)
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numGrt
            gnu.math.IntNum r10 = Lit15
            java.lang.Object r10 = r9.apply2(r4, r10)
            r0 = r10
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x01f8 }
            r9 = r0
            boolean r8 = r9.booleanValue()     // Catch:{ ClassCastException -> 0x01f8 }
            if (r8 == 0) goto L_0x0149
            if (r8 == 0) goto L_0x001d
        L_0x0114:
            r3 = r15
        L_0x0115:
            java.lang.Object r2 = r1.lambda17dig(r3)
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numLss
            gnu.math.IntNum r10 = Lit16
            java.lang.Object r9 = r9.apply2(r2, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0199
            r0 = r7
            gnu.lists.CharSeq r0 = (gnu.lists.CharSeq) r0     // Catch:{ ClassCastException -> 0x0216 }
            r9 = r0
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0220 }
            r10 = r0
            int r11 = r10.intValue()     // Catch:{ ClassCastException -> 0x0220 }
            gnu.kawa.functions.AddOp r10 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r12 = Lit7
            java.lang.Object r10 = r10.apply2(r2, r12)
            java.lang.Number r10 = (java.lang.Number) r10     // Catch:{ ClassCastException -> 0x022a }
            java.lang.CharSequence r10 = kawa.lib.numbers.number$To$String(r10)
            r12 = 0
            char r10 = kawa.lib.strings.stringRef(r10, r12)
            kawa.lib.strings.stringSet$Ex(r9, r11, r10)
            goto L_0x001d
        L_0x0149:
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numEqu
            gnu.math.IntNum r10 = Lit15
            java.lang.Object r10 = r9.apply2(r4, r10)
            r0 = r10
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x0202 }
            r9 = r0
            boolean r8 = r9.booleanValue()     // Catch:{ ClassCastException -> 0x0202 }
            if (r8 == 0) goto L_0x0195
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit14
            java.lang.Object r3 = r9.apply2(r10, r15)
        L_0x0163:
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numGrt
            java.lang.Integer r10 = java.lang.Integer.valueOf(r5)
            java.lang.Object r9 = r9.apply2(r3, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0180
            java.lang.Object r9 = r1.lambda17dig(r15)
            java.lang.Number r9 = (java.lang.Number) r9
            int r9 = r9.intValue()
            r9 = r9 & 1
            if (r9 == 0) goto L_0x001d
            goto L_0x0114
        L_0x0180:
            java.lang.Object r9 = r1.lambda17dig(r3)
            java.lang.Number r9 = (java.lang.Number) r9     // Catch:{ ClassCastException -> 0x020c }
            boolean r9 = kawa.lib.numbers.isZero(r9)
            if (r9 == 0) goto L_0x0114
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit7
            java.lang.Object r3 = r9.apply2(r3, r10)
            goto L_0x0163
        L_0x0195:
            if (r8 == 0) goto L_0x001d
            goto L_0x0114
        L_0x0199:
            r0 = r7
            gnu.lists.CharSeq r0 = (gnu.lists.CharSeq) r0     // Catch:{ ClassCastException -> 0x0234 }
            r9 = r0
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x023e }
            r10 = r0
            int r10 = r10.intValue()     // Catch:{ ClassCastException -> 0x023e }
            r11 = 48
            kawa.lib.strings.stringSet$Ex(r9, r10, r11)
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r10 = Lit7
            java.lang.Object r3 = r9.apply2(r3, r10)
            goto L_0x0115
        L_0x01b4:
            gnu.text.Char r11 = Lit9
            r0 = r7
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x025c }
            r9 = r0
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0266 }
            r10 = r0
            int r10 = r10.intValue()     // Catch:{ ClassCastException -> 0x0266 }
            char r9 = kawa.lib.strings.stringRef(r9, r10)
            gnu.text.Char r9 = gnu.text.Char.make(r9)
            boolean r9 = kawa.lib.characters.isChar$Eq(r11, r9)
            if (r9 == 0) goto L_0x0045
        L_0x01d0:
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r10 = Lit7
            java.lang.Object r3 = r9.apply2(r3, r10)
            goto L_0x0031
        L_0x01da:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "zero?"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r6)
            throw r10
        L_0x01e4:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "make-string"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r6)
            throw r10
        L_0x01ee:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "substring"
            r13 = 3
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x01f8:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "x"
            r13 = -2
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x0202:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "x"
            r13 = -2
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x020c:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "zero?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0216:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-set!"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r7)
            throw r10
        L_0x0220:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-set!"
            r12 = 2
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r3)
            throw r10
        L_0x022a:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "number->string"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x0234:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-set!"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r7)
            throw r10
        L_0x023e:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-set!"
            r12 = 2
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r3)
            throw r10
        L_0x0248:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r7)
            throw r10
        L_0x0252:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "x"
            r13 = -2
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x025c:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-ref"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r7)
            throw r10
        L_0x0266:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-ref"
            r12 = 2
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r3)
            throw r10
        L_0x0270:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "substring"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r7)
            throw r10
        L_0x027a:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "substring"
            r13 = 3
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.printf.stdio$ClRoundString(java.lang.CharSequence, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
        if (moduleMethod.selector != 23) {
            return super.apply3(moduleMethod, obj, obj2, obj3);
        }
        try {
            return stdio$ClRoundString((CharSequence) obj, obj2, obj3);
        } catch (ClassCastException e) {
            throw new WrongType(e, "stdio:round-string", 1, obj);
        }
    }

    public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
        if (moduleMethod.selector != 23) {
            return super.match3(moduleMethod, obj, obj2, obj3, callContext);
        }
        if (!(obj instanceof CharSequence)) {
            return -786431;
        }
        callContext.value1 = obj;
        callContext.value2 = obj2;
        callContext.value3 = obj3;
        callContext.proc = moduleMethod;
        callContext.pc = 3;
        return 0;
    }

    /* compiled from: printf.scm */
    public class frame8 extends ModuleBody {
        CharSequence str;

        public Object lambda17dig(Object i) {
            try {
                char c = strings.stringRef(this.str, ((Number) i).intValue());
                if (!unicode.isCharNumeric(Char.make(c))) {
                    return printf.Lit1;
                }
                return numbers.string$To$Number(strings.$make$string$(Char.make(c)));
            } catch (ClassCastException e) {
                throw new WrongType(e, "string-ref", 2, i);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:136:0x0302, code lost:
        if (r7 == false) goto L_0x0304;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:?, code lost:
        r4 = (java.lang.CharSequence) r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x0306, code lost:
        r10 = r12.precision;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0310, code lost:
        r4 = kawa.lib.strings.substring(r4, 0, ((java.lang.Number) r10).intValue());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:146:0x031d, code lost:
        r9 = kawa.standard.Scheme.numLEq.apply2(r12.width, java.lang.Integer.valueOf(kawa.lib.strings.stringLength((java.lang.CharSequence) r4)));
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x032b, code lost:
        if (r9 == java.lang.Boolean.FALSE) goto L_0x0416;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x032d, code lost:
        r7 = r2.lambda21out$St(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x0333, code lost:
        if (r7 == java.lang.Boolean.FALSE) goto L_0x0075;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x0335, code lost:
        r1 = kawa.lib.lists.cdr.apply1(r12.args);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:167:0x038e, code lost:
        if (kawa.lib.numbers.isNegative(gnu.kawa.lispexpr.LangObjType.coerceRealNum(r10)) != false) goto L_0x0390;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x03a9, code lost:
        if (kawa.lib.numbers.isNegative(gnu.kawa.lispexpr.LangObjType.coerceRealNum(r9)) != false) goto L_0x03ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x0404, code lost:
        r9 = kawa.standard.Scheme.numGEq.apply2(r12.precision, java.lang.Integer.valueOf(kawa.lib.strings.stringLength((java.lang.CharSequence) r4)));
        r4 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:191:0x0412, code lost:
        if (r9 != java.lang.Boolean.FALSE) goto L_0x0315;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:193:0x041a, code lost:
        if (r12.left$Mnadjust == java.lang.Boolean.FALSE) goto L_0x0444;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:0x0424, code lost:
        r10 = gnu.kawa.functions.AddOp.$Mn.apply2(r12.width, java.lang.Integer.valueOf(kawa.lib.strings.stringLength((java.lang.CharSequence) r4)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x0437, code lost:
        r4 = gnu.lists.LList.list2(r4, kawa.lib.strings.makeString(((java.lang.Number) r10).intValue(), Lit29));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:204:0x044c, code lost:
        r10 = gnu.kawa.functions.AddOp.$Mn.apply2(r12.width, java.lang.Integer.valueOf(kawa.lib.strings.stringLength((java.lang.CharSequence) r4)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:?, code lost:
        r10 = ((java.lang.Number) r10).intValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:208:0x0464, code lost:
        if (r12.leading$Mn0s == java.lang.Boolean.FALSE) goto L_0x0472;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:209:0x0466, code lost:
        r9 = Lit9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:210:0x0468, code lost:
        r4 = gnu.lists.LList.list2(kawa.lib.strings.makeString(r10, r9), r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:211:0x0472, code lost:
        r9 = Lit29;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:223:0x04b9, code lost:
        if (r7 == java.lang.Boolean.FALSE) goto L_0x04bb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:225:0x04bf, code lost:
        if (r12.left$Mnadjust == java.lang.Boolean.FALSE) goto L_0x04c5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:226:0x04c1, code lost:
        r10 = r12.lambda$Fn14;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:227:0x04c5, code lost:
        r10 = r12.pr;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:231:0x04cf, code lost:
        if (kawa.lib.numbers.isNegative(gnu.kawa.lispexpr.LangObjType.coerceRealNum(r10)) == false) goto L_0x04d9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:232:0x04d1, code lost:
        r12.pr = r12.width;
        r10 = r12.lambda$Fn15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:233:0x04d9, code lost:
        r10 = r12.lambda$Fn16;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:235:0x04df, code lost:
        if (r7 == java.lang.Boolean.FALSE) goto L_0x04e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:237:0x04e5, code lost:
        if (r12.left$Mnadjust == java.lang.Boolean.FALSE) goto L_0x0528;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:239:0x04fb, code lost:
        if (kawa.standard.Scheme.numGrt.apply2(r12.width, gnu.kawa.functions.AddOp.$Mn.apply2(r12.precision, r12.pr)) == java.lang.Boolean.FALSE) goto L_0x03d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x04fd, code lost:
        r11 = kawa.standard.Scheme.applyToArgs;
        r13 = r2.out;
        r10 = gnu.kawa.functions.AddOp.$Mn.apply2(r12.width, gnu.kawa.functions.AddOp.$Mn.apply2(r12.precision, r12.pr));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:243:0x051c, code lost:
        r11.apply2(r13, kawa.lib.strings.makeString(((java.lang.Number) r10).intValue(), Lit29));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:244:0x0528, code lost:
        r9 = r12.os;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:247:0x052c, code lost:
        if (r9 == java.lang.Boolean.FALSE) goto L_0x0558;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:248:0x052e, code lost:
        r9 = 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:250:0x0533, code lost:
        if (((r9 + 1) & true) != false) goto L_0x03d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:251:0x0535, code lost:
        r10 = kawa.standard.Scheme.numLEq;
        r11 = r12.width;
        r9 = r12.os;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:0x054b, code lost:
        if (r10.apply2(r11, java.lang.Integer.valueOf(kawa.lib.strings.stringLength((java.lang.CharSequence) r9))) == java.lang.Boolean.FALSE) goto L_0x055a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:256:0x054d, code lost:
        kawa.standard.Scheme.applyToArgs.apply2(r2.out, r12.os);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:257:0x0558, code lost:
        r9 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:258:0x055a, code lost:
        r11 = kawa.standard.Scheme.applyToArgs;
        r13 = r2.out;
        r10 = gnu.kawa.functions.AddOp.$Mn;
        r14 = r12.width;
        r9 = r12.os;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:261:0x0566, code lost:
        r10 = r10.apply2(r14, java.lang.Integer.valueOf(kawa.lib.strings.stringLength((java.lang.CharSequence) r9)));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:265:0x0586, code lost:
        if (r11.apply2(r13, kawa.lib.strings.makeString(((java.lang.Number) r10).intValue(), Lit29)) == java.lang.Boolean.FALSE) goto L_0x03d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:266:0x0588, code lost:
        kawa.standard.Scheme.applyToArgs.apply2(r2.out, r12.os);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:384:0x07e5, code lost:
        if (kawa.lib.rnrs.unicode.isCharCi$Eq((gnu.text.Char) r9, Lit55) != false) goto L_0x07e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:386:0x07ed, code lost:
        if (r7 != false) goto L_0x07e7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:432:0x08aa, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:434:0x08b3, code lost:
        throw new gnu.mapping.WrongType(r9, "string-length", 1, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:435:0x08b4, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:437:0x08bd, code lost:
        throw new gnu.mapping.WrongType(r9, "substring", 1, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:438:0x08be, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:440:0x08c7, code lost:
        throw new gnu.mapping.WrongType(r9, "substring", 3, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:441:0x08c8, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:443:0x08d1, code lost:
        throw new gnu.mapping.WrongType(r9, "string-length", 1, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:444:0x08d2, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:446:0x08db, code lost:
        throw new gnu.mapping.WrongType(r9, "string-length", 1, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:447:0x08dc, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:449:0x08e5, code lost:
        throw new gnu.mapping.WrongType(r9, "make-string", 1, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:450:0x08e6, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:452:0x08ef, code lost:
        throw new gnu.mapping.WrongType(r9, "string-length", 1, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:453:0x08f0, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:455:0x08f9, code lost:
        throw new gnu.mapping.WrongType(r9, "make-string", 1, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:459:0x0904, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:461:0x090d, code lost:
        throw new gnu.mapping.WrongType(r9, "negative?", 1, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:468:0x0922, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:470:0x092b, code lost:
        throw new gnu.mapping.WrongType(r9, "make-string", 1, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:471:0x092c, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:473:0x0935, code lost:
        throw new gnu.mapping.WrongType(r10, "x", -2, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:474:0x0936, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:476:0x093f, code lost:
        throw new gnu.mapping.WrongType(r10, "string-length", 1, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:477:0x0940, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:479:0x0949, code lost:
        throw new gnu.mapping.WrongType(r10, "string-length", 1, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:480:0x094a, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:482:0x0953, code lost:
        throw new gnu.mapping.WrongType(r9, "make-string", 1, r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:549:?, code lost:
        return r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object stdio$ClIprintf$V(java.lang.Object r17, java.lang.Object r18, java.lang.Object[] r19) {
        /*
            gnu.kawa.slib.printf$frame9 r2 = new gnu.kawa.slib.printf$frame9
            r2.<init>()
            r0 = r17
            r2.out = r0
            r0 = r18
            r2.format$Mnstring = r0
            r9 = 0
            r0 = r19
            gnu.lists.LList r9 = gnu.lists.LList.makeList(r0, r9)
            r2.args = r9
            java.lang.String r9 = ""
            java.lang.Object r10 = r2.format$Mnstring
            boolean r9 = gnu.kawa.functions.IsEqual.apply(r9, r10)
            if (r9 != 0) goto L_0x0856
            gnu.math.IntNum r10 = Lit17
            java.lang.Object r9 = r2.format$Mnstring
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x085a }
            int r11 = kawa.lib.strings.stringLength(r9)
            java.lang.Object r9 = r2.format$Mnstring
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x0864 }
            r12 = 0
            char r9 = kawa.lib.strings.stringRef(r9, r12)
            gnu.text.Char r9 = gnu.text.Char.make(r9)
            r2.fc = r9
            r2.fl = r11
            r2.pos = r10
            gnu.lists.LList r1 = r2.args
        L_0x003f:
            gnu.kawa.slib.printf$frame10 r12 = new gnu.kawa.slib.printf$frame10
            r12.<init>()
            r12.staticLink = r2
            r12.args = r1
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit7
            java.lang.Object r11 = r2.pos
            java.lang.Object r9 = r9.apply2(r10, r11)
            r2.pos = r9
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numGEq
            java.lang.Object r10 = r2.pos
            int r11 = r2.fl
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)
            java.lang.Object r9 = r9.apply2(r10, r11)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0076
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            r2.fc = r9
        L_0x006a:
            boolean r7 = r2.lambda19isEndOfFormat()
            if (r7 == 0) goto L_0x0092
            if (r7 == 0) goto L_0x008f
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
        L_0x0074:
            r7 = r9
        L_0x0075:
            return r7
        L_0x0076:
            java.lang.Object r9 = r2.format$Mnstring
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x086e }
            java.lang.Object r11 = r2.pos
            r0 = r11
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0878 }
            r10 = r0
            int r10 = r10.intValue()     // Catch:{ ClassCastException -> 0x0878 }
            char r9 = kawa.lib.strings.stringRef(r9, r10)
            gnu.text.Char r9 = gnu.text.Char.make(r9)
            r2.fc = r9
            goto L_0x006a
        L_0x008f:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            goto L_0x0074
        L_0x0092:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit18
            java.lang.Object r11 = r2.fc
            java.lang.Object r9 = r9.apply2(r10, r11)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x013e
            r2.lambda18mustAdvance()
            java.lang.Object r6 = r2.fc
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit19
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x00c7
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x00d3
        L_0x00b5:
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            gnu.text.Char r11 = Lit21
            java.lang.Object r8 = r9.apply2(r10, r11)
        L_0x00bf:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x013b
            java.lang.Object r1 = r12.args
            goto L_0x003f
        L_0x00c7:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit20
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x00b5
        L_0x00d3:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit22
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x00ee
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x00fa
        L_0x00e3:
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            gnu.text.Char r11 = Lit24
            java.lang.Object r8 = r9.apply2(r10, r11)
            goto L_0x00bf
        L_0x00ee:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit23
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x00e3
        L_0x00fa:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit25
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0115
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0121
        L_0x010a:
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            gnu.text.Char r11 = Lit27
            java.lang.Object r8 = r9.apply2(r10, r11)
            goto L_0x00bf
        L_0x0115:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit26
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x010a
        L_0x0121:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit21
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0130
            java.lang.Boolean r8 = java.lang.Boolean.TRUE
            goto L_0x00bf
        L_0x0130:
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            java.lang.Object r11 = r2.fc
            java.lang.Object r8 = r9.apply2(r10, r11)
            goto L_0x00bf
        L_0x013b:
            r7 = r8
            goto L_0x0075
        L_0x013e:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit28
            java.lang.Object r11 = r2.fc
            java.lang.Object r9 = r9.apply2(r10, r11)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0844
            r2.lambda18mustAdvance()
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            java.lang.Boolean r11 = java.lang.Boolean.FALSE
            java.lang.Boolean r13 = java.lang.Boolean.FALSE
            java.lang.Boolean r14 = java.lang.Boolean.FALSE
            gnu.math.IntNum r15 = Lit1
            gnu.math.IntNum r16 = Lit17
            r0 = r16
            r12.precision = r0
            r12.width = r15
            r12.leading$Mn0s = r14
            r12.alternate$Mnform = r13
            r12.blank = r11
            r12.signed = r10
            r12.left$Mnadjust = r9
            gnu.mapping.Procedure r9 = r12.pad
            r12.pad = r9
        L_0x0171:
            java.lang.Object r6 = r2.fc
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit5
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0187
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            r12.left$Mnadjust = r9
        L_0x0183:
            r2.lambda18mustAdvance()
            goto L_0x0171
        L_0x0187:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit6
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0198
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            r12.signed = r9
            goto L_0x0183
        L_0x0198:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit29
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x01a9
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            r12.blank = r9
            goto L_0x0183
        L_0x01a9:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit8
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x01ba
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            r12.alternate$Mnform = r9
            goto L_0x0183
        L_0x01ba:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit9
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x01cb
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            r12.leading$Mn0s = r9
            goto L_0x0183
        L_0x01cb:
            java.lang.Object r9 = r12.left$Mnadjust
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x01d5
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            r12.leading$Mn0s = r9
        L_0x01d5:
            java.lang.Object r9 = r12.signed
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x01df
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            r12.blank = r9
        L_0x01df:
            java.lang.Object r9 = r12.lambda22readFormatNumber()
            r12.width = r9
            java.lang.Object r9 = r12.width
            gnu.math.RealNum r9 = gnu.kawa.lispexpr.LangObjType.coerceRealNum(r9)     // Catch:{ ClassCastException -> 0x0882 }
            boolean r9 = kawa.lib.numbers.isNegative(r9)
            if (r9 == 0) goto L_0x01ff
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            r12.left$Mnadjust = r9
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r10 = r12.width
            java.lang.Object r9 = r9.apply1(r10)
            r12.width = r9
        L_0x01ff:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit11
            java.lang.Object r11 = r2.fc
            java.lang.Object r9 = r9.apply2(r10, r11)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0216
            r2.lambda18mustAdvance()
            java.lang.Object r9 = r12.lambda22readFormatNumber()
            r12.precision = r9
        L_0x0216:
            java.lang.Object r6 = r2.fc
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit30
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x029e
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x022b
        L_0x0228:
            r2.lambda18mustAdvance()
        L_0x022b:
            java.lang.Object r9 = r12.args
            boolean r9 = kawa.lib.lists.isNull(r9)
            if (r9 == 0) goto L_0x0264
            java.lang.Object r9 = r2.fc
            gnu.text.Char r9 = (gnu.text.Char) r9     // Catch:{ ClassCastException -> 0x088c }
            gnu.text.Char r9 = kawa.lib.rnrs.unicode.charDowncase(r9)
            gnu.lists.PairWithPosition r10 = Lit33
            java.lang.Object r9 = kawa.lib.lists.memv(r9, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0264
            gnu.mapping.SimpleSymbol r9 = Lit34
            r10 = 3
            java.lang.Object[] r10 = new java.lang.Object[r10]
            r11 = 0
            java.lang.String r13 = "wrong number of arguments"
            r10[r11] = r13
            r11 = 1
            gnu.lists.LList r13 = r2.args
            int r13 = kawa.lib.lists.length(r13)
            java.lang.Integer r13 = java.lang.Integer.valueOf(r13)
            r10[r11] = r13
            r11 = 2
            java.lang.Object r13 = r2.format$Mnstring
            r10[r11] = r13
            kawa.lib.misc.error$V(r9, r10)
        L_0x0264:
            java.lang.Object r6 = r2.fc
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit35
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x02be
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x02ca
        L_0x0276:
            gnu.kawa.functions.ApplyToArgs r11 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r13 = r2.out
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r9 = r9.apply1(r10)
            boolean r10 = r9 instanceof java.lang.Object[]
            if (r10 == 0) goto L_0x033f
            java.lang.Object[] r9 = (java.lang.Object[]) r9
        L_0x0288:
            java.lang.CharSequence r9 = kawa.lib.strings.$make$string$(r9)
            java.lang.Object r7 = r11.apply2(r13, r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x029e:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit31
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x02b0
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x022b
            goto L_0x0228
        L_0x02b0:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit32
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x022b
            goto L_0x0228
        L_0x02be:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit36
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x0276
        L_0x02ca:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit37
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0348
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0354
        L_0x02da:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r9 = r9.apply1(r10)
            boolean r9 = kawa.lib.misc.isSymbol(r9)
            if (r9 == 0) goto L_0x03e2
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r9 = r9.apply1(r10)
            gnu.mapping.Symbol r9 = (gnu.mapping.Symbol) r9     // Catch:{ ClassCastException -> 0x0896 }
            java.lang.String r4 = kawa.lib.misc.symbol$To$String(r9)
        L_0x02f6:
            java.lang.Object r9 = r12.precision
            gnu.math.RealNum r9 = gnu.kawa.lispexpr.LangObjType.coerceRealNum(r9)     // Catch:{ ClassCastException -> 0x08a0 }
            boolean r7 = kawa.lib.numbers.isNegative(r9)
            if (r7 == 0) goto L_0x03fc
            if (r7 != 0) goto L_0x0315
        L_0x0304:
            java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x08b4 }
            r11 = 0
            java.lang.Object r10 = r12.precision
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x08be }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x08be }
            java.lang.CharSequence r4 = kawa.lib.strings.substring(r4, r11, r9)
        L_0x0315:
            gnu.kawa.functions.NumberCompare r10 = kawa.standard.Scheme.numLEq
            java.lang.Object r11 = r12.width
            r0 = r4
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x08c8 }
            r9 = r0
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r9 = r10.apply2(r11, r9)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0416
        L_0x032d:
            java.lang.Object r7 = r2.lambda21out$St(r4)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x033f:
            r10 = 1
            java.lang.Object[] r10 = new java.lang.Object[r10]
            r14 = 0
            r10[r14] = r9
            r9 = r10
            goto L_0x0288
        L_0x0348:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit38
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x02da
        L_0x0354:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit39
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0475
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0481
        L_0x0364:
            java.lang.String r9 = ""
            java.lang.Object r10 = r12.precision
            r12.pr = r10
            r12.os = r9
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r11 = r9.apply1(r10)
            java.lang.Object r9 = r12.alternate$Mnform
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x04b3
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
        L_0x037c:
            java.lang.Boolean r13 = java.lang.Boolean.FALSE
            java.lang.Object r7 = r12.left$Mnadjust
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r7 == r10) goto L_0x04b7
            java.lang.Object r10 = r12.pr
            gnu.math.RealNum r10 = gnu.kawa.lispexpr.LangObjType.coerceRealNum(r10)     // Catch:{ ClassCastException -> 0x08fa }
            boolean r10 = kawa.lib.numbers.isNegative(r10)
            if (r10 == 0) goto L_0x04bb
        L_0x0390:
            gnu.math.IntNum r10 = Lit1
            r12.pr = r10
            gnu.expr.ModuleMethod r10 = r12.lambda$Fn13
        L_0x0396:
            gnu.kawa.slib.genwrite.genericWrite(r11, r9, r13, r10)
            java.lang.Object r7 = r12.left$Mnadjust
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x04dd
            java.lang.Object r9 = r12.precision
            gnu.math.RealNum r9 = gnu.kawa.lispexpr.LangObjType.coerceRealNum(r9)     // Catch:{ ClassCastException -> 0x090e }
            boolean r9 = kawa.lib.numbers.isNegative(r9)
            if (r9 == 0) goto L_0x04e1
        L_0x03ab:
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numGrt
            java.lang.Object r10 = r12.width
            java.lang.Object r11 = r12.pr
            java.lang.Object r9 = r9.apply2(r10, r11)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x03d8
            gnu.kawa.functions.ApplyToArgs r11 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r13 = r2.out
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r10 = r12.width
            java.lang.Object r14 = r12.pr
            java.lang.Object r10 = r9.apply2(r10, r14)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0918 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x0918 }
            gnu.text.Char r10 = Lit29
            java.lang.CharSequence r9 = kawa.lib.strings.makeString(r9, r10)
            r11.apply2(r13, r9)
        L_0x03d8:
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x03e2:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r9 = r9.apply1(r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x03f2
            java.lang.String r4 = "(NULL)"
            goto L_0x02f6
        L_0x03f2:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r4 = r9.apply1(r10)
            goto L_0x02f6
        L_0x03fc:
            gnu.kawa.functions.NumberCompare r10 = kawa.standard.Scheme.numGEq
            java.lang.Object r11 = r12.precision
            r0 = r4
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x08aa }
            r9 = r0
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r9 = r10.apply2(r11, r9)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x0315
            goto L_0x0304
        L_0x0416:
            java.lang.Object r9 = r12.left$Mnadjust
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0444
            gnu.kawa.functions.AddOp r10 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r11 = r12.width
            r0 = r4
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x08d2 }
            r9 = r0
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r10 = r10.apply2(r11, r9)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x08dc }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x08dc }
            gnu.text.Char r10 = Lit29
            java.lang.CharSequence r9 = kawa.lib.strings.makeString(r9, r10)
            gnu.lists.Pair r4 = gnu.lists.LList.list2(r4, r9)
            goto L_0x032d
        L_0x0444:
            gnu.kawa.functions.AddOp r10 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r11 = r12.width
            r0 = r4
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x08e6 }
            r9 = r0
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r10 = r10.apply2(r11, r9)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x08f0 }
            r9 = r0
            int r10 = r9.intValue()     // Catch:{ ClassCastException -> 0x08f0 }
            java.lang.Object r9 = r12.leading$Mn0s
            java.lang.Boolean r11 = java.lang.Boolean.FALSE
            if (r9 == r11) goto L_0x0472
            gnu.text.Char r9 = Lit9
        L_0x0468:
            java.lang.CharSequence r9 = kawa.lib.strings.makeString(r10, r9)
            gnu.lists.Pair r4 = gnu.lists.LList.list2(r9, r4)
            goto L_0x032d
        L_0x0472:
            gnu.text.Char r9 = Lit29
            goto L_0x0468
        L_0x0475:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit40
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x0364
        L_0x0481:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit12
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0593
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x05a3
        L_0x0491:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r9 = r9.apply1(r10)
            gnu.math.IntNum r10 = Lit45
            java.lang.Boolean r11 = java.lang.Boolean.FALSE
            java.lang.Object r9 = r12.lambda24integerConvert(r9, r10, r11)
            java.lang.Object r7 = r2.lambda21out$St(r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x04b3:
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            goto L_0x037c
        L_0x04b7:
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r7 != r10) goto L_0x0390
        L_0x04bb:
            java.lang.Object r10 = r12.left$Mnadjust
            java.lang.Boolean r14 = java.lang.Boolean.FALSE
            if (r10 == r14) goto L_0x04c5
            gnu.expr.ModuleMethod r10 = r12.lambda$Fn14
            goto L_0x0396
        L_0x04c5:
            java.lang.Object r10 = r12.pr
            gnu.math.RealNum r10 = gnu.kawa.lispexpr.LangObjType.coerceRealNum(r10)     // Catch:{ ClassCastException -> 0x0904 }
            boolean r10 = kawa.lib.numbers.isNegative(r10)
            if (r10 == 0) goto L_0x04d9
            java.lang.Object r10 = r12.width
            r12.pr = r10
            gnu.expr.ModuleMethod r10 = r12.lambda$Fn15
            goto L_0x0396
        L_0x04d9:
            gnu.expr.ModuleMethod r10 = r12.lambda$Fn16
            goto L_0x0396
        L_0x04dd:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 != r9) goto L_0x03ab
        L_0x04e1:
            java.lang.Object r9 = r12.left$Mnadjust
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0528
            gnu.kawa.functions.NumberCompare r9 = kawa.standard.Scheme.numGrt
            java.lang.Object r10 = r12.width
            gnu.kawa.functions.AddOp r11 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r13 = r12.precision
            java.lang.Object r14 = r12.pr
            java.lang.Object r11 = r11.apply2(r13, r14)
            java.lang.Object r9 = r9.apply2(r10, r11)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x03d8
            gnu.kawa.functions.ApplyToArgs r11 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r13 = r2.out
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r10 = r12.width
            gnu.kawa.functions.AddOp r14 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r15 = r12.precision
            java.lang.Object r0 = r12.pr
            r16 = r0
            java.lang.Object r14 = r14.apply2(r15, r16)
            java.lang.Object r10 = r9.apply2(r10, r14)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0922 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x0922 }
            gnu.text.Char r10 = Lit29
            java.lang.CharSequence r9 = kawa.lib.strings.makeString(r9, r10)
            r11.apply2(r13, r9)
            goto L_0x03d8
        L_0x0528:
            java.lang.Object r9 = r12.os
            java.lang.Boolean r10 = java.lang.Boolean.FALSE     // Catch:{ ClassCastException -> 0x092c }
            if (r9 == r10) goto L_0x0558
            r9 = 1
        L_0x052f:
            int r9 = r9 + 1
            r7 = r9 & 1
            if (r7 != 0) goto L_0x03d8
            gnu.kawa.functions.NumberCompare r10 = kawa.standard.Scheme.numLEq
            java.lang.Object r11 = r12.width
            java.lang.Object r9 = r12.os
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x0936 }
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r9 = r10.apply2(r11, r9)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x055a
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            java.lang.Object r11 = r12.os
            r9.apply2(r10, r11)
            goto L_0x03d8
        L_0x0558:
            r9 = 0
            goto L_0x052f
        L_0x055a:
            gnu.kawa.functions.ApplyToArgs r11 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r13 = r2.out
            gnu.kawa.functions.AddOp r10 = gnu.kawa.functions.AddOp.$Mn
            java.lang.Object r14 = r12.width
            java.lang.Object r9 = r12.os
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x0940 }
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r10 = r10.apply2(r14, r9)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x094a }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x094a }
            gnu.text.Char r10 = Lit29
            java.lang.CharSequence r9 = kawa.lib.strings.makeString(r9, r10)
            java.lang.Object r7 = r11.apply2(r13, r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x03d8
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            java.lang.Object r11 = r12.os
            r9.apply2(r10, r11)
            goto L_0x03d8
        L_0x0593:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit41
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x05d5
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 != r9) goto L_0x0491
        L_0x05a3:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit46
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0619
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0625
        L_0x05b3:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r9 = r9.apply1(r10)
            gnu.math.IntNum r10 = Lit48
            java.lang.Boolean r11 = java.lang.Boolean.FALSE
            java.lang.Object r9 = r12.lambda24integerConvert(r9, r10, r11)
            java.lang.Object r7 = r2.lambda21out$St(r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x05d5:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit3
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x05e7
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x05a3
            goto L_0x0491
        L_0x05e7:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit42
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x05f9
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x05a3
            goto L_0x0491
        L_0x05f9:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit43
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x060b
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x05a3
            goto L_0x0491
        L_0x060b:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit44
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x05a3
            goto L_0x0491
        L_0x0619:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit47
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x05b3
        L_0x0625:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit49
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x065a
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r10 = r9.apply1(r10)
            gnu.math.IntNum r11 = Lit50
            boolean r9 = stdio$Clhex$Mnupper$Mncase$Qu
            if (r9 == 0) goto L_0x0657
            gnu.expr.ModuleMethod r9 = kawa.lib.rnrs.unicode.string$Mndowncase
        L_0x0641:
            java.lang.Object r9 = r12.lambda24integerConvert(r10, r11, r9)
            java.lang.Object r7 = r2.lambda21out$St(r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x0657:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            goto L_0x0641
        L_0x065a:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit51
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x068f
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r10 = r9.apply1(r10)
            gnu.math.IntNum r11 = Lit50
            boolean r9 = stdio$Clhex$Mnupper$Mncase$Qu
            if (r9 == 0) goto L_0x068c
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
        L_0x0676:
            java.lang.Object r9 = r12.lambda24integerConvert(r10, r11, r9)
            java.lang.Object r7 = r2.lambda21out$St(r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x068c:
            gnu.expr.ModuleMethod r9 = kawa.lib.rnrs.unicode.string$Mnupcase
            goto L_0x0676
        L_0x068f:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit52
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x06c1
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x06cd
        L_0x069f:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r9 = r9.apply1(r10)
            gnu.math.IntNum r10 = Lit14
            java.lang.Boolean r11 = java.lang.Boolean.FALSE
            java.lang.Object r9 = r12.lambda24integerConvert(r9, r10, r11)
            java.lang.Object r7 = r2.lambda21out$St(r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x06c1:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit53
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 != r10) goto L_0x069f
        L_0x06cd:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit28
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x06eb
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            gnu.text.Char r11 = Lit28
            java.lang.Object r7 = r9.apply2(r10, r11)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            java.lang.Object r1 = r12.args
            goto L_0x003f
        L_0x06eb:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit25
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x074e
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x075e
        L_0x06fb:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r10 = r12.args
            java.lang.Object r3 = r9.apply1(r10)
            java.lang.Object r9 = r2.fc
            gnu.kawa.slib.printf$frame11 r10 = new gnu.kawa.slib.printf$frame11
            r10.<init>()
            r10.staticLink = r12
            r10.fc = r9
            java.lang.Object r9 = r12.precision
            gnu.math.RealNum r9 = gnu.kawa.lispexpr.LangObjType.coerceRealNum(r9)     // Catch:{ ClassCastException -> 0x0954 }
            boolean r9 = kawa.lib.numbers.isNegative(r9)
            if (r9 == 0) goto L_0x07d1
            gnu.math.IntNum r9 = Lit59
            r12.precision = r9
        L_0x071e:
            boolean r9 = kawa.lib.numbers.isNumber(r3)
            if (r9 == 0) goto L_0x07f0
            java.lang.Number r3 = (java.lang.Number) r3     // Catch:{ ClassCastException -> 0x0972 }
            java.lang.Number r9 = kawa.lib.numbers.exact$To$Inexact(r3)
            java.lang.CharSequence r5 = kawa.lib.numbers.number$To$String(r9)
        L_0x072e:
            gnu.mapping.Procedure r9 = r10.format$Mnreal
            r10.format$Mnreal = r9
            gnu.expr.ModuleMethod r9 = r10.lambda$Fn17
            java.lang.Object r7 = stdio$ClParseFloat(r5, r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x080b
        L_0x073c:
            java.lang.Object r7 = r2.lambda21out$St(r7)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r10 = r12.args
            java.lang.Object r1 = r9.apply1(r10)
            goto L_0x003f
        L_0x074e:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit26
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x076a
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 != r9) goto L_0x06fb
        L_0x075e:
            boolean r9 = r2.lambda19isEndOfFormat()
            if (r9 == 0) goto L_0x0816
            java.lang.Object r7 = r2.lambda20incomplete()
            goto L_0x0075
        L_0x076a:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit13
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x077b
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x075e
            goto L_0x06fb
        L_0x077b:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit54
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x078d
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x075e
            goto L_0x06fb
        L_0x078d:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit55
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x079f
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x075e
            goto L_0x06fb
        L_0x079f:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit56
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x07b1
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x075e
            goto L_0x06fb
        L_0x07b1:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit57
            java.lang.Object r7 = r9.apply2(r6, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x07c3
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x075e
            goto L_0x06fb
        L_0x07c3:
            gnu.kawa.functions.IsEqv r9 = kawa.standard.Scheme.isEqv
            gnu.text.Char r10 = Lit58
            java.lang.Object r9 = r9.apply2(r6, r10)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x075e
            goto L_0x06fb
        L_0x07d1:
            java.lang.Object r9 = r12.precision
            java.lang.Number r9 = (java.lang.Number) r9     // Catch:{ ClassCastException -> 0x095e }
            boolean r7 = kawa.lib.numbers.isZero(r9)
            if (r7 == 0) goto L_0x07ed
            java.lang.Object r9 = r10.fc
            gnu.text.Char r9 = (gnu.text.Char) r9     // Catch:{ ClassCastException -> 0x0968 }
            gnu.text.Char r11 = Lit55
            boolean r9 = kawa.lib.rnrs.unicode.isCharCi$Eq(r9, r11)
            if (r9 == 0) goto L_0x071e
        L_0x07e7:
            gnu.math.IntNum r9 = Lit7
            r12.precision = r9
            goto L_0x071e
        L_0x07ed:
            if (r7 == 0) goto L_0x071e
            goto L_0x07e7
        L_0x07f0:
            boolean r9 = kawa.lib.strings.isString(r3)
            if (r9 == 0) goto L_0x07f9
            r5 = r3
            goto L_0x072e
        L_0x07f9:
            boolean r9 = kawa.lib.misc.isSymbol(r3)
            if (r9 == 0) goto L_0x0807
            gnu.mapping.Symbol r3 = (gnu.mapping.Symbol) r3     // Catch:{ ClassCastException -> 0x097c }
            java.lang.String r5 = kawa.lib.misc.symbol$To$String(r3)
            goto L_0x072e
        L_0x0807:
            java.lang.String r5 = "???"
            goto L_0x072e
        L_0x080b:
            java.lang.String r9 = "???"
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.Object r7 = r12.lambda23pad$V(r9, r10)
            goto L_0x073c
        L_0x0816:
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            gnu.text.Char r11 = Lit28
            java.lang.Object r7 = r9.apply2(r10, r11)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            java.lang.Object r11 = r2.fc
            java.lang.Object r7 = r9.apply2(r10, r11)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            gnu.text.Char r11 = Lit65
            java.lang.Object r7 = r9.apply2(r10, r11)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            java.lang.Object r1 = r12.args
            goto L_0x003f
        L_0x0844:
            gnu.kawa.functions.ApplyToArgs r9 = kawa.standard.Scheme.applyToArgs
            java.lang.Object r10 = r2.out
            java.lang.Object r11 = r2.fc
            java.lang.Object r7 = r9.apply2(r10, r11)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r7 == r9) goto L_0x0075
            java.lang.Object r1 = r12.args
            goto L_0x003f
        L_0x0856:
            gnu.mapping.Values r7 = gnu.mapping.Values.empty
            goto L_0x0075
        L_0x085a:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0864:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-ref"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x086e:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-ref"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0878:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-ref"
            r13 = 2
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r11)
            throw r10
        L_0x0882:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "negative?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x088c:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "char-downcase"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0896:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "symbol->string"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x08a0:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "negative?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x08aa:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r4)
            throw r10
        L_0x08b4:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "substring"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r4)
            throw r10
        L_0x08be:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "substring"
            r13 = 3
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x08c8:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r4)
            throw r10
        L_0x08d2:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r4)
            throw r10
        L_0x08dc:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "make-string"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x08e6:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r4)
            throw r10
        L_0x08f0:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "make-string"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x08fa:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "negative?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x0904:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "negative?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x090e:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "negative?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0918:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "make-string"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x0922:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "make-string"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x092c:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "x"
            r13 = -2
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0936:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0940:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x094a:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "make-string"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x0954:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "negative?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x095e:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "zero?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0968:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "char-ci=?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0972:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "exact->inexact"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r3)
            throw r10
        L_0x097c:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "symbol->string"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r3)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.printf.stdio$ClIprintf$V(java.lang.Object, java.lang.Object, java.lang.Object[]):java.lang.Object");
    }

    public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 24:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 25:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 26:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 27:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            default:
                return super.matchN(moduleMethod, objArr, callContext);
        }
    }

    /* compiled from: printf.scm */
    public class frame9 extends ModuleBody {
        LList args;
        Object fc;
        int fl;
        Object format$Mnstring;
        Object out;
        Object pos;

        public Object lambda18mustAdvance() {
            this.pos = AddOp.$Pl.apply2(printf.Lit7, this.pos);
            if (Scheme.numGEq.apply2(this.pos, Integer.valueOf(this.fl)) != Boolean.FALSE) {
                return lambda20incomplete();
            }
            Object obj = this.format$Mnstring;
            try {
                CharSequence charSequence = (CharSequence) obj;
                Object obj2 = this.pos;
                try {
                    this.fc = Char.make(strings.stringRef(charSequence, ((Number) obj2).intValue()));
                    return Values.empty;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-ref", 2, obj2);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-ref", 1, obj);
            }
        }

        public boolean lambda19isEndOfFormat() {
            return ((Boolean) Scheme.numGEq.apply2(this.pos, Integer.valueOf(this.fl))).booleanValue();
        }

        public Object lambda20incomplete() {
            return misc.error$V(printf.Lit34, new Object[]{"conversion specification incomplete", this.format$Mnstring});
        }

        public Object lambda21out$St(Object strs) {
            if (strings.isString(strs)) {
                return Scheme.applyToArgs.apply2(this.out, strs);
            }
            while (true) {
                boolean x = lists.isNull(strs);
                if (x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                Object x2 = Scheme.applyToArgs.apply2(this.out, lists.car.apply1(strs));
                if (x2 == Boolean.FALSE) {
                    return x2;
                }
                strs = lists.cdr.apply1(strs);
            }
        }
    }

    /* compiled from: printf.scm */
    public class frame10 extends ModuleBody {
        Object alternate$Mnform;
        Object args;
        Object blank;
        final ModuleMethod lambda$Fn13;
        final ModuleMethod lambda$Fn14;
        final ModuleMethod lambda$Fn15;
        final ModuleMethod lambda$Fn16;
        Object leading$Mn0s;
        Object left$Mnadjust;
        Object os;
        Procedure pad = new ModuleMethod(this, 15, printf.Lit67, -4095);
        Object pr;
        Object precision;
        Object signed;
        frame9 staticLink;
        Object width;

        public frame10() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 16, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:472");
            this.lambda$Fn13 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 17, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:476");
            this.lambda$Fn14 = moduleMethod2;
            ModuleMethod moduleMethod3 = new ModuleMethod(this, 18, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod3.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:484");
            this.lambda$Fn15 = moduleMethod3;
            ModuleMethod moduleMethod4 = new ModuleMethod(this, 19, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod4.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:494");
            this.lambda$Fn16 = moduleMethod4;
        }

        public Object lambda22readFormatNumber() {
            Object[] objArr;
            if (Scheme.isEqv.apply2(printf.Lit66, this.staticLink.fc) != Boolean.FALSE) {
                this.staticLink.lambda18mustAdvance();
                Object ans = lists.car.apply1(this.args);
                this.args = lists.cdr.apply1(this.args);
                return ans;
            }
            Object c = this.staticLink.fc;
            Object ans2 = printf.Lit1;
            while (true) {
                Object obj = this.staticLink.fc;
                try {
                    if (!unicode.isCharNumeric((Char) obj)) {
                        return ans2;
                    }
                    this.staticLink.lambda18mustAdvance();
                    Object c2 = this.staticLink.fc;
                    AddOp addOp = AddOp.$Pl;
                    Object apply2 = MultiplyOp.$St.apply2(ans2, printf.Lit45);
                    if (c instanceof Object[]) {
                        objArr = (Object[]) c;
                    } else {
                        objArr = new Object[]{c};
                    }
                    ans2 = addOp.apply2(apply2, numbers.string$To$Number(strings.$make$string$(objArr)));
                    c = c2;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "char-numeric?", 1, obj);
                }
            }
        }

        public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
            if (moduleMethod.selector != 15) {
                return super.applyN(moduleMethod, objArr);
            }
            Object obj = objArr[0];
            int length = objArr.length - 1;
            Object[] objArr2 = new Object[length];
            while (true) {
                length--;
                if (length < 0) {
                    return lambda23pad$V(obj, objArr2);
                }
                objArr2[length] = objArr[length + 1];
            }
        }

        public Object lambda23pad$V(Object pre, Object[] argsArray) {
            LList strs = LList.makeList(argsArray, 0);
            try {
                Object valueOf = Integer.valueOf(strings.stringLength((CharSequence) pre));
                Object obj = strs;
                while (Scheme.numGEq.apply2(valueOf, this.width) == Boolean.FALSE) {
                    if (!lists.isNull(obj)) {
                        AddOp addOp = AddOp.$Pl;
                        Object apply1 = lists.car.apply1(obj);
                        try {
                            valueOf = addOp.apply2(valueOf, Integer.valueOf(strings.stringLength((CharSequence) apply1)));
                            obj = lists.cdr.apply1(obj);
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-length", 1, apply1);
                        }
                    } else if (this.left$Mnadjust != Boolean.FALSE) {
                        Object[] objArr = new Object[2];
                        objArr[0] = strs;
                        Object apply2 = AddOp.$Mn.apply2(this.width, valueOf);
                        try {
                            objArr[1] = LList.list1(strings.makeString(((Number) apply2).intValue(), printf.Lit29));
                            return lists.cons(pre, append.append$V(objArr));
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "make-string", 1, apply2);
                        }
                    } else if (this.leading$Mn0s != Boolean.FALSE) {
                        Object apply22 = AddOp.$Mn.apply2(this.width, valueOf);
                        try {
                            return lists.cons(pre, lists.cons(strings.makeString(((Number) apply22).intValue(), printf.Lit9), strs));
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "make-string", 1, apply22);
                        }
                    } else {
                        Object apply23 = AddOp.$Mn.apply2(this.width, valueOf);
                        try {
                            return lists.cons(strings.makeString(((Number) apply23).intValue(), printf.Lit29), lists.cons(pre, strs));
                        } catch (ClassCastException e4) {
                            throw new WrongType(e4, "make-string", 1, apply23);
                        }
                    }
                }
                return lists.cons(pre, strs);
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "string-length", 1, pre);
            }
        }

        public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
            if (moduleMethod.selector != 15) {
                return super.matchN(moduleMethod, objArr, callContext);
            }
            callContext.values = objArr;
            callContext.proc = moduleMethod;
            callContext.pc = 5;
            return 0;
        }

        public Object lambda24integerConvert(Object s, Object radix, Object fixcase) {
            Object obj;
            Object s2;
            String pre;
            String str;
            Object obj2;
            Object obj3 = this.precision;
            try {
                if (!numbers.isNegative(LangObjType.coerceRealNum(obj3))) {
                    this.leading$Mn0s = Boolean.FALSE;
                    Object obj4 = this.precision;
                    try {
                        boolean x = numbers.isZero((Number) obj4);
                        if (!x ? x : Scheme.isEqv.apply2(printf.Lit1, s) != Boolean.FALSE) {
                            s = "";
                        }
                        obj = s;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "zero?", 1, obj4);
                    }
                } else {
                    obj = s;
                }
                if (misc.isSymbol(obj)) {
                    try {
                        s2 = misc.symbol$To$String((Symbol) obj);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "symbol->string", 1, obj);
                    }
                } else if (numbers.isNumber(obj)) {
                    try {
                        try {
                            s2 = numbers.number$To$String((Number) obj, ((Number) radix).intValue());
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "number->string", 2, radix);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "number->string", 1, obj);
                    }
                } else {
                    try {
                        boolean x2 = ((obj != Boolean.FALSE ? 1 : 0) + 1) & true;
                        s2 = (!x2 ? lists.isNull(obj) : x2) ? Component.TYPEFACE_DEFAULT : strings.isString(obj) ? obj : Component.TYPEFACE_SANSSERIF;
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "x", -2, obj);
                    }
                }
                if (fixcase != Boolean.FALSE) {
                    s2 = Scheme.applyToArgs.apply2(fixcase, s2);
                }
                if (IsEqual.apply("", s2)) {
                    pre = "";
                } else {
                    try {
                        if (Scheme.isEqv.apply2(printf.Lit5, Char.make(strings.stringRef((CharSequence) s2, 0))) != Boolean.FALSE) {
                            try {
                                try {
                                    s2 = strings.substring((CharSequence) s2, 1, strings.stringLength((CharSequence) s2));
                                    pre = "-";
                                } catch (ClassCastException e6) {
                                    throw new WrongType(e6, "string-length", 1, s2);
                                }
                            } catch (ClassCastException e7) {
                                throw new WrongType(e7, "substring", 1, s2);
                            }
                        } else if (this.signed != Boolean.FALSE) {
                            pre = "+";
                        } else if (this.blank != Boolean.FALSE) {
                            pre = " ";
                        } else if (this.alternate$Mnform != Boolean.FALSE) {
                            if (Scheme.isEqv.apply2(radix, printf.Lit48) != Boolean.FALSE) {
                                str = Component.TYPEFACE_DEFAULT;
                            } else if (Scheme.isEqv.apply2(radix, printf.Lit50) != Boolean.FALSE) {
                                str = "0x";
                            } else {
                                str = "";
                            }
                            pre = str;
                        } else {
                            pre = "";
                        }
                    } catch (ClassCastException e8) {
                        throw new WrongType(e8, "string-ref", 1, s2);
                    }
                }
                Object[] objArr = new Object[2];
                try {
                    if (Scheme.numLss.apply2(Integer.valueOf(strings.stringLength((CharSequence) s2)), this.precision) != Boolean.FALSE) {
                        try {
                            Object apply2 = AddOp.$Mn.apply2(this.precision, Integer.valueOf(strings.stringLength((CharSequence) s2)));
                            try {
                                obj2 = strings.makeString(((Number) apply2).intValue(), printf.Lit9);
                            } catch (ClassCastException e9) {
                                throw new WrongType(e9, "make-string", 1, apply2);
                            }
                        } catch (ClassCastException e10) {
                            throw new WrongType(e10, "string-length", 1, s2);
                        }
                    } else {
                        obj2 = "";
                    }
                    objArr[0] = obj2;
                    objArr[1] = s2;
                    return lambda23pad$V(pre, objArr);
                } catch (ClassCastException e11) {
                    throw new WrongType(e11, "string-length", 1, s2);
                }
            } catch (ClassCastException e12) {
                throw new WrongType(e12, "negative?", 1, obj3);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda25(Object s) {
            try {
                this.pr = AddOp.$Pl.apply2(this.pr, Integer.valueOf(strings.stringLength((CharSequence) s)));
                return Scheme.applyToArgs.apply2(this.staticLink.out, s);
            } catch (ClassCastException e) {
                throw new WrongType(e, "string-length", 1, s);
            }
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 16:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 17:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 18:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 19:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean lambda26(Object s) {
            Object obj;
            Special special = Special.undefined;
            try {
                Object sl = AddOp.$Mn.apply2(this.pr, Integer.valueOf(strings.stringLength((CharSequence) s)));
                try {
                    if (numbers.isNegative(LangObjType.coerceRealNum(sl))) {
                        ApplyToArgs applyToArgs = Scheme.applyToArgs;
                        Object obj2 = this.staticLink.out;
                        try {
                            CharSequence charSequence = (CharSequence) s;
                            Object obj3 = this.pr;
                            try {
                                applyToArgs.apply2(obj2, strings.substring(charSequence, 0, ((Number) obj3).intValue()));
                                obj = printf.Lit1;
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "substring", 3, obj3);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "substring", 1, s);
                        }
                    } else {
                        Scheme.applyToArgs.apply2(this.staticLink.out, s);
                        obj = sl;
                    }
                    this.pr = obj;
                    try {
                        return numbers.isPositive(LangObjType.coerceRealNum(sl));
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "positive?", 1, sl);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "negative?", 1, sl);
                }
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "string-length", 1, s);
            }
        }

        /* access modifiers changed from: package-private */
        public Boolean lambda27(Object s) {
            try {
                this.pr = AddOp.$Mn.apply2(this.pr, Integer.valueOf(strings.stringLength((CharSequence) s)));
                if (this.os == Boolean.FALSE) {
                    Scheme.applyToArgs.apply2(this.staticLink.out, s);
                } else {
                    Object obj = this.pr;
                    try {
                        if (numbers.isNegative(LangObjType.coerceRealNum(obj))) {
                            Scheme.applyToArgs.apply2(this.staticLink.out, this.os);
                            this.os = Boolean.FALSE;
                            Scheme.applyToArgs.apply2(this.staticLink.out, s);
                        } else {
                            this.os = strings.stringAppend(this.os, s);
                        }
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "negative?", 1, obj);
                    }
                }
                return Boolean.TRUE;
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-length", 1, s);
            }
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 16:
                    return lambda25(obj);
                case 17:
                    return lambda26(obj) ? Boolean.TRUE : Boolean.FALSE;
                case 18:
                    return lambda27(obj);
                case 19:
                    return lambda28(obj) ? Boolean.TRUE : Boolean.FALSE;
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean lambda28(Object s) {
            Special special = Special.undefined;
            try {
                Object sl = AddOp.$Mn.apply2(this.pr, Integer.valueOf(strings.stringLength((CharSequence) s)));
                try {
                    if (numbers.isNegative(LangObjType.coerceRealNum(sl))) {
                        Object[] objArr = new Object[2];
                        objArr[0] = this.os;
                        try {
                            CharSequence charSequence = (CharSequence) s;
                            Object obj = this.pr;
                            try {
                                objArr[1] = strings.substring(charSequence, 0, ((Number) obj).intValue());
                                this.os = strings.stringAppend(objArr);
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "substring", 3, obj);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "substring", 1, s);
                        }
                    } else {
                        this.os = strings.stringAppend(this.os, s);
                    }
                    this.pr = sl;
                    try {
                        return numbers.isPositive(LangObjType.coerceRealNum(sl));
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "positive?", 1, sl);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "negative?", 1, sl);
                }
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "string-length", 1, s);
            }
        }
    }

    /* compiled from: printf.scm */
    public class frame11 extends ModuleBody {
        Object fc;
        Procedure format$Mnreal = new ModuleMethod(this, 13, printf.Lit64, -4092);
        final ModuleMethod lambda$Fn17;
        frame10 staticLink;

        public frame11() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 14, (Object) null, -4093);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:401");
            this.lambda$Fn17 = moduleMethod;
        }

        public Object lambda29f(Object digs, Object exp, Object strip$Mn0s) {
            Object x;
            Object obj;
            IntNum i0;
            try {
                Object digs2 = printf.stdio$ClRoundString((CharSequence) digs, AddOp.$Pl.apply2(exp, this.staticLink.precision), strip$Mn0s != Boolean.FALSE ? exp : strip$Mn0s);
                if (Scheme.numGEq.apply2(exp, printf.Lit1) != Boolean.FALSE) {
                    try {
                        if (numbers.isZero((Number) exp)) {
                            i0 = printf.Lit1;
                        } else {
                            try {
                                if (characters.isChar$Eq(printf.Lit9, Char.make(strings.stringRef((CharSequence) digs2, 0)))) {
                                    i0 = printf.Lit7;
                                } else {
                                    i0 = printf.Lit1;
                                }
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "string-ref", 1, digs2);
                            }
                        }
                        Object i1 = numbers.max(printf.Lit7, AddOp.$Pl.apply2(printf.Lit7, exp));
                        try {
                            try {
                                try {
                                    CharSequence idigs = strings.substring((CharSequence) digs2, i0.intValue(), ((Number) i1).intValue());
                                    try {
                                        try {
                                            try {
                                                CharSequence fdigs = strings.substring((CharSequence) digs2, ((Number) i1).intValue(), strings.stringLength((CharSequence) digs2));
                                                boolean x2 = strings.isString$Eq(fdigs, "");
                                                return lists.cons(idigs, (!x2 ? x2 : this.staticLink.alternate$Mnform == Boolean.FALSE) ? LList.Empty : LList.list2(".", fdigs));
                                            } catch (ClassCastException e2) {
                                                throw new WrongType(e2, "string-length", 1, digs2);
                                            }
                                        } catch (ClassCastException e3) {
                                            throw new WrongType(e3, "substring", 2, i1);
                                        }
                                    } catch (ClassCastException e4) {
                                        throw new WrongType(e4, "substring", 1, digs2);
                                    }
                                } catch (ClassCastException e5) {
                                    throw new WrongType(e5, "substring", 3, i1);
                                }
                            } catch (ClassCastException e6) {
                                throw new WrongType(e6, "substring", 2, (Object) i0);
                            }
                        } catch (ClassCastException e7) {
                            throw new WrongType(e7, "substring", 1, digs2);
                        }
                    } catch (ClassCastException e8) {
                        throw new WrongType(e8, "zero?", 1, exp);
                    }
                } else {
                    Object obj2 = this.staticLink.precision;
                    try {
                        if (numbers.isZero((Number) obj2)) {
                            return LList.list1(this.staticLink.alternate$Mnform != Boolean.FALSE ? "0." : Component.TYPEFACE_DEFAULT);
                        }
                        if (strip$Mn0s != Boolean.FALSE) {
                            boolean x3 = strings.isString$Eq(digs2, "");
                            if (x3) {
                                obj = LList.list1(Component.TYPEFACE_DEFAULT);
                            } else {
                                obj = x3 ? Boolean.TRUE : Boolean.FALSE;
                            }
                            x = obj;
                        } else {
                            x = strip$Mn0s;
                        }
                        if (x != Boolean.FALSE) {
                            return x;
                        }
                        Object min = numbers.min(this.staticLink.precision, AddOp.$Mn.apply2(printf.Lit17, exp));
                        try {
                            return LList.list3("0.", strings.makeString(((Number) min).intValue(), printf.Lit9), digs2);
                        } catch (ClassCastException e9) {
                            throw new WrongType(e9, "make-string", 1, min);
                        }
                    } catch (ClassCastException e10) {
                        throw new WrongType(e10, "zero?", 1, obj2);
                    }
                }
            } catch (ClassCastException e11) {
                throw new WrongType(e11, "stdio:round-string", 0, digs);
            }
        }

        public Object lambda30formatReal$V(Object signed$Qu, Object sgn, Object digs, Object exp, Object[] argsArray) {
            String str;
            Object obj;
            Object obj2;
            Object i;
            Object uind;
            Object obj3;
            Boolean bool;
            LList rest = LList.makeList(argsArray, 0);
            if (lists.isNull(rest)) {
                try {
                    if (characters.isChar$Eq(printf.Lit5, (Char) sgn)) {
                        str = "-";
                    } else {
                        str = signed$Qu != Boolean.FALSE ? "+" : this.staticLink.blank != Boolean.FALSE ? " " : "";
                    }
                    Object x = Scheme.isEqv.apply2(this.fc, printf.Lit13);
                    if (x == Boolean.FALSE ? Scheme.isEqv.apply2(this.fc, printf.Lit54) != Boolean.FALSE : x != Boolean.FALSE) {
                        obj3 = Boolean.FALSE;
                    } else {
                        Object x2 = Scheme.isEqv.apply2(this.fc, printf.Lit25);
                        if (x2 == Boolean.FALSE ? Scheme.isEqv.apply2(this.fc, printf.Lit26) != Boolean.FALSE : x2 != Boolean.FALSE) {
                            obj = lambda29f(digs, exp, Boolean.FALSE);
                            return lists.cons(str, obj);
                        }
                        Object x3 = Scheme.isEqv.apply2(this.fc, printf.Lit55);
                        if (x3 == Boolean.FALSE ? Scheme.isEqv.apply2(this.fc, printf.Lit56) == Boolean.FALSE : x3 == Boolean.FALSE) {
                            if (Scheme.isEqv.apply2(this.fc, printf.Lit57) != Boolean.FALSE) {
                                obj2 = "";
                            } else if (Scheme.isEqv.apply2(this.fc, printf.Lit58) != Boolean.FALSE) {
                                obj2 = " ";
                            } else {
                                obj = Values.empty;
                                return lists.cons(str, obj);
                            }
                            try {
                                if (numbers.isNegative(LangObjType.coerceRealNum(exp))) {
                                    i = DivideOp.quotient.apply2(AddOp.$Mn.apply2(exp, printf.Lit61), printf.Lit61);
                                } else {
                                    i = DivideOp.quotient.apply2(AddOp.$Mn.apply2(exp, printf.Lit7), printf.Lit61);
                                }
                                Object apply3 = Scheme.numLss.apply3(printf.Lit17, AddOp.$Pl.apply2(i, printf.Lit48), Integer.valueOf(vectors.vectorLength(printf.Lit62)));
                                try {
                                    boolean x4 = ((Boolean) apply3).booleanValue();
                                    if (x4) {
                                        uind = i;
                                    } else {
                                        uind = x4 ? Boolean.TRUE : Boolean.FALSE;
                                    }
                                    if (uind != Boolean.FALSE) {
                                        Object exp2 = AddOp.$Mn.apply2(exp, MultiplyOp.$St.apply2(printf.Lit61, uind));
                                        this.staticLink.precision = numbers.max(printf.Lit1, AddOp.$Mn.apply2(this.staticLink.precision, exp2));
                                        Object[] objArr = new Object[2];
                                        objArr[0] = lambda29f(digs, exp2, Boolean.FALSE);
                                        FVector fVector = printf.Lit62;
                                        Object apply2 = AddOp.$Pl.apply2(uind, printf.Lit48);
                                        try {
                                            objArr[1] = LList.list2(obj2, vectors.vectorRef(fVector, ((Number) apply2).intValue()));
                                            obj = append.append$V(objArr);
                                            return lists.cons(str, obj);
                                        } catch (ClassCastException e) {
                                            throw new WrongType(e, "vector-ref", 2, apply2);
                                        }
                                    }
                                } catch (ClassCastException e2) {
                                    throw new WrongType(e2, "x", -2, apply3);
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "negative?", 1, exp);
                            }
                        }
                        Object obj4 = this.staticLink.alternate$Mnform;
                        try {
                            boolean strip$Mn0s = ((obj4 != Boolean.FALSE ? 1 : 0) + 1) & true;
                            this.staticLink.alternate$Mnform = Boolean.FALSE;
                            if (Scheme.numLEq.apply3(AddOp.$Mn.apply2(printf.Lit7, this.staticLink.precision), exp, this.staticLink.precision) != Boolean.FALSE) {
                                this.staticLink.precision = AddOp.$Mn.apply2(this.staticLink.precision, exp);
                                if (strip$Mn0s) {
                                    bool = Boolean.TRUE;
                                } else {
                                    bool = Boolean.FALSE;
                                }
                                obj = lambda29f(digs, exp, bool);
                                return lists.cons(str, obj);
                            }
                            this.staticLink.precision = AddOp.$Mn.apply2(this.staticLink.precision, printf.Lit7);
                            obj3 = strip$Mn0s ? Boolean.TRUE : Boolean.FALSE;
                        } catch (ClassCastException e4) {
                            throw new WrongType(e4, "strip-0s", -2, obj4);
                        }
                    }
                    try {
                        CharSequence charSequence = (CharSequence) digs;
                        Object apply22 = AddOp.$Pl.apply2(printf.Lit7, this.staticLink.precision);
                        if (obj3 != Boolean.FALSE) {
                            obj3 = printf.Lit1;
                        }
                        Object digs2 = printf.stdio$ClRoundString(charSequence, apply22, obj3);
                        try {
                            IntNum istrt = characters.isChar$Eq(printf.Lit9, Char.make(strings.stringRef((CharSequence) digs2, 0))) ? printf.Lit7 : printf.Lit1;
                            try {
                                try {
                                    CharSequence fdigs = strings.substring((CharSequence) digs2, istrt.intValue() + 1, strings.stringLength((CharSequence) digs2));
                                    if (!numbers.isZero(istrt)) {
                                        exp = AddOp.$Mn.apply2(exp, printf.Lit7);
                                    }
                                    try {
                                        try {
                                            Pair list1 = LList.list1(strings.substring((CharSequence) digs2, istrt.intValue(), istrt.intValue() + 1));
                                            boolean x5 = strings.isString$Eq(fdigs, "");
                                            String str2 = (!x5 ? x5 : this.staticLink.alternate$Mnform == Boolean.FALSE) ? "" : ".";
                                            Object obj5 = this.fc;
                                            try {
                                                try {
                                                    try {
                                                        LList.chain1(LList.chain1(LList.chain4(list1, str2, fdigs, unicode.isCharUpperCase((Char) obj5) ? "E" : "e", numbers.isNegative(LangObjType.coerceRealNum(exp)) ? "-" : "+"), Scheme.numLss.apply3(printf.Lit60, exp, printf.Lit45) != Boolean.FALSE ? Component.TYPEFACE_DEFAULT : ""), numbers.number$To$String(numbers.abs((Number) exp)));
                                                        obj = list1;
                                                        return lists.cons(str, obj);
                                                    } catch (ClassCastException e5) {
                                                        throw new WrongType(e5, "abs", 1, exp);
                                                    }
                                                } catch (ClassCastException e6) {
                                                    throw new WrongType(e6, "negative?", 1, exp);
                                                }
                                            } catch (ClassCastException e7) {
                                                throw new WrongType(e7, "char-upper-case?", 1, obj5);
                                            }
                                        } catch (ClassCastException e8) {
                                            throw new WrongType(e8, "substring", 2, (Object) istrt);
                                        }
                                    } catch (ClassCastException e9) {
                                        throw new WrongType(e9, "substring", 1, digs2);
                                    }
                                } catch (ClassCastException e10) {
                                    throw new WrongType(e10, "string-length", 1, digs2);
                                }
                            } catch (ClassCastException e11) {
                                throw new WrongType(e11, "substring", 1, digs2);
                            }
                        } catch (ClassCastException e12) {
                            throw new WrongType(e12, "string-ref", 1, digs2);
                        }
                    } catch (ClassCastException e13) {
                        throw new WrongType(e13, "stdio:round-string", 0, digs);
                    }
                } catch (ClassCastException e14) {
                    throw new WrongType(e14, "char=?", 2, sgn);
                }
            } else {
                return append.append$V(new Object[]{lambda30formatReal$V(signed$Qu, sgn, digs, exp, new Object[0]), Scheme.apply.apply3(this.format$Mnreal, Boolean.TRUE, rest), printf.Lit63});
            }
        }

        public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 13:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                case 14:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                default:
                    return super.matchN(moduleMethod, objArr, callContext);
            }
        }

        public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
            switch (moduleMethod.selector) {
                case 13:
                    Object obj = objArr[0];
                    Object obj2 = objArr[1];
                    Object obj3 = objArr[2];
                    Object obj4 = objArr[3];
                    int length = objArr.length - 4;
                    Object[] objArr2 = new Object[length];
                    while (true) {
                        length--;
                        if (length < 0) {
                            return lambda30formatReal$V(obj, obj2, obj3, obj4, objArr2);
                        }
                        objArr2[length] = objArr[length + 4];
                    }
                case 14:
                    Object obj5 = objArr[0];
                    Object obj6 = objArr[1];
                    Object obj7 = objArr[2];
                    int length2 = objArr.length - 3;
                    Object[] objArr3 = new Object[length2];
                    while (true) {
                        length2--;
                        if (length2 < 0) {
                            return lambda31$V(obj5, obj6, obj7, objArr3);
                        }
                        objArr3[length2] = objArr[length2 + 3];
                    }
                default:
                    return super.applyN(moduleMethod, objArr);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda31$V(Object sgn, Object digs, Object expon, Object[] argsArray) {
            LList imag = LList.makeList(argsArray, 0);
            return Scheme.apply.apply2(this.staticLink.pad, Scheme.apply.applyN(new Object[]{this.format$Mnreal, this.staticLink.signed, sgn, digs, expon, imag}));
        }
    }

    public static Object fprintf$V(Object port, Object format, Object[] argsArray) {
        frame12 frame122 = new frame12();
        frame122.port = port;
        LList args = LList.makeList(argsArray, 0);
        frame122.cnt = Lit1;
        Scheme.apply.apply4(stdio$Cliprintf, frame122.lambda$Fn18, format, args);
        return frame122.cnt;
    }

    /* compiled from: printf.scm */
    public class frame12 extends ModuleBody {
        Object cnt;
        final ModuleMethod lambda$Fn18;
        Object port;

        public frame12() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 20, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:546");
            this.lambda$Fn18 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 20 ? lambda32(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Boolean lambda32(Object x) {
            if (strings.isString(x)) {
                try {
                    this.cnt = AddOp.$Pl.apply2(Integer.valueOf(strings.stringLength((CharSequence) x)), this.cnt);
                    ports.display(x, this.port);
                    return Boolean.TRUE;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-length", 1, x);
                }
            } else {
                this.cnt = AddOp.$Pl.apply2(printf.Lit7, this.cnt);
                ports.display(x, this.port);
                return Boolean.TRUE;
            }
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 20) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object printf$V(Object format, Object[] argsArray) {
        return Scheme.apply.apply4(fprintf, ports.current$Mnoutput$Mnport.apply0(), format, LList.makeList(argsArray, 0));
    }

    public static Object sprintf$V(Object str, Object format, Object[] argsArray) {
        Object error$V;
        frame13 frame132 = new frame13();
        frame132.str = str;
        LList args = LList.makeList(argsArray, 0);
        frame132.cnt = Lit1;
        if (strings.isString(frame132.str)) {
            error$V = frame132.str;
        } else if (numbers.isNumber(frame132.str)) {
            Object obj = frame132.str;
            try {
                error$V = strings.makeString(((Number) obj).intValue());
            } catch (ClassCastException e) {
                throw new WrongType(e, "make-string", 1, obj);
            }
        } else if (frame132.str == Boolean.FALSE) {
            error$V = strings.makeString(100);
        } else {
            error$V = misc.error$V(Lit68, new Object[]{"first argument not understood", frame132.str});
        }
        frame132.s = error$V;
        Object obj2 = frame132.s;
        try {
            frame132.end = Integer.valueOf(strings.stringLength((CharSequence) obj2));
            Scheme.apply.apply4(stdio$Cliprintf, frame132.lambda$Fn19, format, args);
            if (strings.isString(frame132.str)) {
                return frame132.cnt;
            }
            if (Scheme.isEqv.apply2(frame132.end, frame132.cnt) != Boolean.FALSE) {
                return frame132.s;
            }
            Object obj3 = frame132.s;
            try {
                CharSequence charSequence = (CharSequence) obj3;
                Object obj4 = frame132.cnt;
                try {
                    return strings.substring(charSequence, 0, ((Number) obj4).intValue());
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "substring", 3, obj4);
                }
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "substring", 1, obj3);
            }
        } catch (ClassCastException e4) {
            throw new WrongType(e4, "string-length", 1, obj2);
        }
    }

    public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
        switch (moduleMethod.selector) {
            case 24:
                Object obj = objArr[0];
                Object obj2 = objArr[1];
                int length = objArr.length - 2;
                Object[] objArr2 = new Object[length];
                while (true) {
                    length--;
                    if (length < 0) {
                        return stdio$ClIprintf$V(obj, obj2, objArr2);
                    }
                    objArr2[length] = objArr[length + 2];
                }
            case 25:
                Object obj3 = objArr[0];
                Object obj4 = objArr[1];
                int length2 = objArr.length - 2;
                Object[] objArr3 = new Object[length2];
                while (true) {
                    length2--;
                    if (length2 < 0) {
                        return fprintf$V(obj3, obj4, objArr3);
                    }
                    objArr3[length2] = objArr[length2 + 2];
                }
            case 26:
                Object obj5 = objArr[0];
                int length3 = objArr.length - 1;
                Object[] objArr4 = new Object[length3];
                while (true) {
                    length3--;
                    if (length3 < 0) {
                        return printf$V(obj5, objArr4);
                    }
                    objArr4[length3] = objArr[length3 + 1];
                }
            case 27:
                Object obj6 = objArr[0];
                Object obj7 = objArr[1];
                int length4 = objArr.length - 2;
                Object[] objArr5 = new Object[length4];
                while (true) {
                    length4--;
                    if (length4 < 0) {
                        return sprintf$V(obj6, obj7, objArr5);
                    }
                    objArr5[length4] = objArr[length4 + 2];
                }
            default:
                return super.applyN(moduleMethod, objArr);
        }
    }

    /* compiled from: printf.scm */
    public class frame13 extends ModuleBody {
        Object cnt;
        Object end;
        final ModuleMethod lambda$Fn19;
        Object s;
        Object str;

        public frame13() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 21, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/printf.scm:564");
            this.lambda$Fn19 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 21) {
                return lambda33(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: gnu.math.IntNum} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: gnu.math.IntNum} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v9, resolved type: gnu.math.IntNum} */
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean lambda33(java.lang.Object r13) {
            /*
                r12 = this;
                r10 = 3
                r11 = 2
                r9 = 0
                r8 = 1
                boolean r4 = kawa.lib.strings.isString(r13)
                if (r4 == 0) goto L_0x00dd
                java.lang.Object r4 = r12.str
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 != r5) goto L_0x0030
                gnu.kawa.functions.NumberCompare r5 = kawa.standard.Scheme.numGEq
                gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Mn
                java.lang.Object r6 = r12.end
                java.lang.Object r7 = r12.cnt
                java.lang.Object r6 = r4.apply2(r6, r7)
                r0 = r13
                java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0175 }
                r4 = r0
                int r4 = kawa.lib.strings.stringLength(r4)
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                java.lang.Object r4 = r5.apply2(r6, r4)
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x0093
            L_0x0030:
                java.lang.Object[] r5 = new java.lang.Object[r11]
                r0 = r13
                java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x017e }
                r4 = r0
                int r4 = kawa.lib.strings.stringLength(r4)
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                r5[r9] = r4
                gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Mn
                java.lang.Object r6 = r12.end
                java.lang.Object r7 = r12.cnt
                java.lang.Object r4 = r4.apply2(r6, r7)
                r5[r8] = r4
                java.lang.Object r2 = kawa.lib.numbers.min(r5)
                gnu.math.IntNum r7 = gnu.kawa.slib.printf.Lit1
            L_0x0052:
                gnu.kawa.functions.NumberCompare r4 = kawa.standard.Scheme.numGEq
                java.lang.Object r4 = r4.apply2(r7, r2)
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 != r5) goto L_0x00c3
                java.lang.Object r4 = r12.s
                gnu.lists.CharSeq r4 = (gnu.lists.CharSeq) r4     // Catch:{ ClassCastException -> 0x0187 }
                java.lang.Object r6 = r12.cnt
                r0 = r6
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0190 }
                r5 = r0
                int r10 = r5.intValue()     // Catch:{ ClassCastException -> 0x0190 }
                r0 = r13
                java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0199 }
                r5 = r0
                r0 = r7
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01a2 }
                r6 = r0
                int r6 = r6.intValue()     // Catch:{ ClassCastException -> 0x01a2 }
                char r5 = kawa.lib.strings.stringRef(r5, r6)
                kawa.lib.strings.stringSet$Ex(r4, r10, r5)
                gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
                java.lang.Object r5 = r12.cnt
                gnu.math.IntNum r6 = gnu.kawa.slib.printf.Lit7
                java.lang.Object r4 = r4.apply2(r5, r6)
                r12.cnt = r4
                gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
                gnu.math.IntNum r5 = gnu.kawa.slib.printf.Lit7
                java.lang.Object r1 = r4.apply2(r7, r5)
                r7 = r1
                goto L_0x0052
            L_0x0093:
                java.lang.Object[] r7 = new java.lang.Object[r11]
                java.lang.Object r4 = r12.s
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x01ab }
                java.lang.Object r6 = r12.cnt
                r0 = r6
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01b4 }
                r5 = r0
                int r5 = r5.intValue()     // Catch:{ ClassCastException -> 0x01b4 }
                java.lang.CharSequence r4 = kawa.lib.strings.substring(r4, r9, r5)
                r7[r9] = r4
                r7[r8] = r13
                gnu.lists.FString r4 = kawa.lib.strings.stringAppend(r7)
                r12.s = r4
                java.lang.Object r4 = r12.s
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x01bd }
                int r4 = kawa.lib.strings.stringLength(r4)
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                r12.cnt = r4
                java.lang.Object r4 = r12.cnt
                r12.end = r4
            L_0x00c3:
                java.lang.Object r4 = r12.str
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x0169
                gnu.kawa.functions.NumberCompare r4 = kawa.standard.Scheme.numGEq
                java.lang.Object r5 = r12.cnt
                java.lang.Object r6 = r12.end
                java.lang.Object r4 = r4.apply2(r5, r6)
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x0166
                r4 = r8
            L_0x00d8:
                int r4 = r4 + 1
                r4 = r4 & 1
                return r4
            L_0x00dd:
                java.lang.Object r4 = r12.str
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x015b
                gnu.kawa.functions.NumberCompare r4 = kawa.standard.Scheme.numGEq
                java.lang.Object r5 = r12.cnt
                java.lang.Object r6 = r12.end
                java.lang.Object r3 = r4.apply2(r5, r6)
            L_0x00ed:
                java.lang.Boolean r4 = java.lang.Boolean.FALSE
                if (r3 != r4) goto L_0x00c3
                java.lang.Object r4 = r12.str
                java.lang.Boolean r5 = java.lang.Boolean.FALSE     // Catch:{ ClassCastException -> 0x01c6 }
                if (r4 == r5) goto L_0x015e
                r4 = r8
            L_0x00f8:
                int r4 = r4 + 1
                r3 = r4 & 1
                if (r3 == 0) goto L_0x0160
                gnu.kawa.functions.NumberCompare r4 = kawa.standard.Scheme.numGEq
                java.lang.Object r5 = r12.cnt
                java.lang.Object r6 = r12.end
                java.lang.Object r4 = r4.apply2(r5, r6)
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x012e
            L_0x010c:
                java.lang.Object[] r4 = new java.lang.Object[r11]
                java.lang.Object r5 = r12.s
                r4[r9] = r5
                r5 = 100
                java.lang.CharSequence r5 = kawa.lib.strings.makeString(r5)
                r4[r8] = r5
                gnu.lists.FString r4 = kawa.lib.strings.stringAppend(r4)
                r12.s = r4
                java.lang.Object r4 = r12.s
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x01d0 }
                int r4 = kawa.lib.strings.stringLength(r4)
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                r12.end = r4
            L_0x012e:
                java.lang.Object r4 = r12.s
                gnu.lists.CharSeq r4 = (gnu.lists.CharSeq) r4     // Catch:{ ClassCastException -> 0x01d9 }
                java.lang.Object r6 = r12.cnt
                r0 = r6
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01e2 }
                r5 = r0
                int r6 = r5.intValue()     // Catch:{ ClassCastException -> 0x01e2 }
                boolean r5 = kawa.lib.characters.isChar(r13)
                if (r5 == 0) goto L_0x0163
                r0 = r13
                gnu.text.Char r0 = (gnu.text.Char) r0     // Catch:{ ClassCastException -> 0x01eb }
                r5 = r0
                char r5 = r5.charValue()     // Catch:{ ClassCastException -> 0x01eb }
            L_0x014a:
                kawa.lib.strings.stringSet$Ex(r4, r6, r5)
                gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
                java.lang.Object r5 = r12.cnt
                gnu.math.IntNum r6 = gnu.kawa.slib.printf.Lit7
                java.lang.Object r4 = r4.apply2(r5, r6)
                r12.cnt = r4
                goto L_0x00c3
            L_0x015b:
                java.lang.Object r3 = r12.str
                goto L_0x00ed
            L_0x015e:
                r4 = r9
                goto L_0x00f8
            L_0x0160:
                if (r3 == 0) goto L_0x012e
                goto L_0x010c
            L_0x0163:
                r5 = 63
                goto L_0x014a
            L_0x0166:
                r4 = r9
                goto L_0x00d8
            L_0x0169:
                java.lang.Object r4 = r12.str
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x0172
                r4 = r8
                goto L_0x00d8
            L_0x0172:
                r4 = r9
                goto L_0x00d8
            L_0x0175:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-length"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r8, (java.lang.Object) r13)
                throw r5
            L_0x017e:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-length"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r8, (java.lang.Object) r13)
                throw r5
            L_0x0187:
                r5 = move-exception
                gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
                java.lang.String r7 = "string-set!"
                r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r8, (java.lang.Object) r4)
                throw r6
            L_0x0190:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r7 = "string-set!"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r7, (int) r11, (java.lang.Object) r6)
                throw r5
            L_0x0199:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-ref"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r8, (java.lang.Object) r13)
                throw r5
            L_0x01a2:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-ref"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r11, (java.lang.Object) r7)
                throw r5
            L_0x01ab:
                r5 = move-exception
                gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
                java.lang.String r7 = "substring"
                r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r8, (java.lang.Object) r4)
                throw r6
            L_0x01b4:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r7 = "substring"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r7, (int) r10, (java.lang.Object) r6)
                throw r5
            L_0x01bd:
                r5 = move-exception
                gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
                java.lang.String r7 = "string-length"
                r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r8, (java.lang.Object) r4)
                throw r6
            L_0x01c6:
                r5 = move-exception
                gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
                java.lang.String r7 = "x"
                r8 = -2
                r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r8, (java.lang.Object) r4)
                throw r6
            L_0x01d0:
                r5 = move-exception
                gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
                java.lang.String r7 = "string-length"
                r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r8, (java.lang.Object) r4)
                throw r6
            L_0x01d9:
                r5 = move-exception
                gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
                java.lang.String r7 = "string-set!"
                r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r8, (java.lang.Object) r4)
                throw r6
            L_0x01e2:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r7 = "string-set!"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r7, (int) r11, (java.lang.Object) r6)
                throw r5
            L_0x01eb:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-set!"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r10, (java.lang.Object) r13)
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.printf.frame13.lambda33(java.lang.Object):boolean");
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 21) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }
}
