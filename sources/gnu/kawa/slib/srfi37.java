package gnu.kawa.slib;

import androidx.fragment.app.FragmentTransaction;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.AddOp;
import gnu.kawa.functions.Apply;
import gnu.kawa.functions.IsEqual;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.mapping.CallContext;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import gnu.text.Char;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.strings;
import kawa.standard.Scheme;
import kawa.standard.call_with_values;

/* compiled from: srfi37.scm */
public class srfi37 extends ModuleBody {
    public static final srfi37 $instance = new srfi37();
    static final IntNum Lit0 = IntNum.make(1);
    static final Char Lit1 = Char.make(45);
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("option-processor").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("args-fold").readResolve());
    static final Char Lit2 = Char.make(61);
    static final IntNum Lit3 = IntNum.make(3);
    static final IntNum Lit4 = IntNum.make(0);
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("option?").readResolve());
    static final SimpleSymbol Lit6 = ((SimpleSymbol) new SimpleSymbol("option").readResolve());
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("option-names").readResolve());
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("option-required-arg?").readResolve());
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("option-optional-arg?").readResolve());
    public static final ModuleMethod args$Mnfold;
    public static final ModuleMethod option;
    public static final ModuleMethod option$Mnnames;
    public static final ModuleMethod option$Mnoptional$Mnarg$Qu;
    public static final ModuleMethod option$Mnprocessor;
    public static final ModuleMethod option$Mnrequired$Mnarg$Qu;
    static final Class option$Mntype = option$Mntype.class;
    public static final ModuleMethod option$Qu;

    static {
        srfi37 srfi37 = $instance;
        option$Qu = new ModuleMethod(srfi37, 25, Lit5, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        option = new ModuleMethod(srfi37, 26, Lit6, 16388);
        option$Mnnames = new ModuleMethod(srfi37, 27, Lit7, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        option$Mnrequired$Mnarg$Qu = new ModuleMethod(srfi37, 28, Lit8, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        option$Mnoptional$Mnarg$Qu = new ModuleMethod(srfi37, 29, Lit9, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        option$Mnprocessor = new ModuleMethod(srfi37, 30, Lit10, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        args$Mnfold = new ModuleMethod(srfi37, 31, Lit11, -4092);
        $instance.run();
    }

    public srfi37() {
        ModuleInfo.register(this);
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
    }

    public static option$Mntype option(Object names, Object required$Mnarg$Qu, Object optional$Mnarg$Qu, Object processor) {
        option$Mntype tmp = new option$Mntype();
        tmp.names = names;
        tmp.required$Mnarg$Qu = required$Mnarg$Qu;
        tmp.optional$Mnarg$Qu = optional$Mnarg$Qu;
        tmp.processor = processor;
        return tmp;
    }

    public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
        return moduleMethod.selector == 26 ? option(obj, obj2, obj3, obj4) : super.apply4(moduleMethod, obj, obj2, obj3, obj4);
    }

    public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
        if (moduleMethod.selector != 26) {
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

    public static boolean isOption(Object obj) {
        return obj instanceof option$Mntype;
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 25:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 27:
                if (!(obj instanceof option$Mntype)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 28:
                if (!(obj instanceof option$Mntype)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 29:
                if (!(obj instanceof option$Mntype)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 30:
                if (!(obj instanceof option$Mntype)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public static Object optionNames(option$Mntype obj) {
        return obj.names;
    }

    public static Object isOptionRequiredArg(option$Mntype obj) {
        return obj.required$Mnarg$Qu;
    }

    public static Object isOptionOptionalArg(option$Mntype obj) {
        return obj.optional$Mnarg$Qu;
    }

    public static Object optionProcessor(option$Mntype obj) {
        return obj.processor;
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 25:
                return isOption(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 27:
                try {
                    return optionNames((option$Mntype) obj);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "option-names", 1, obj);
                }
            case 28:
                try {
                    return isOptionRequiredArg((option$Mntype) obj);
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "option-required-arg?", 1, obj);
                }
            case 29:
                try {
                    return isOptionOptionalArg((option$Mntype) obj);
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "option-optional-arg?", 1, obj);
                }
            case 30:
                try {
                    return optionProcessor((option$Mntype) obj);
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "option-processor", 1, obj);
                }
            default:
                return super.apply1(moduleMethod, obj);
        }
    }

    public static Object argsFold$V(Object args, Object options, Object unrecognizedOptionProc, Object operandProc, Object[] argsArray) {
        frame frame7 = new frame();
        frame7.options = options;
        frame7.unrecognized$Mnoption$Mnproc = unrecognizedOptionProc;
        frame7.operand$Mnproc = operandProc;
        return frame7.lambda5scanArgs(args, LList.makeList(argsArray, 0));
    }

    public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
        if (moduleMethod.selector != 31) {
            return super.applyN(moduleMethod, objArr);
        }
        Object obj = objArr[0];
        Object obj2 = objArr[1];
        Object obj3 = objArr[2];
        Object obj4 = objArr[3];
        int length = objArr.length - 4;
        Object[] objArr2 = new Object[length];
        while (true) {
            length--;
            if (length < 0) {
                return argsFold$V(obj, obj2, obj3, obj4, objArr2);
            }
            objArr2[length] = objArr[length + 4];
        }
    }

    public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
        if (moduleMethod.selector != 31) {
            return super.matchN(moduleMethod, objArr, callContext);
        }
        callContext.values = objArr;
        callContext.proc = moduleMethod;
        callContext.pc = 5;
        return 0;
    }

    /* compiled from: srfi37.scm */
    public class frame extends ModuleBody {
        Object operand$Mnproc;
        Object options;
        Object unrecognized$Mnoption$Mnproc;

        public static Object lambda1find(Object l, Object $Qu) {
            if (lists.isNull(l)) {
                return Boolean.FALSE;
            }
            if (Scheme.applyToArgs.apply2($Qu, lists.car.apply1(l)) != Boolean.FALSE) {
                return lists.car.apply1(l);
            }
            return lambda1find(lists.cdr.apply1(l), $Qu);
        }

        public Object lambda2findOption(Object name) {
            frame0 frame0 = new frame0();
            frame0.staticLink = this;
            frame0.name = name;
            return lambda1find(this.options, frame0.lambda$Fn1);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:27:0x008a, code lost:
            if (r1 != java.lang.Boolean.FALSE) goto L_0x008c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x00b4, code lost:
            if (gnu.kawa.slib.srfi37.isOptionOptionalArg((gnu.kawa.slib.option$Mntype) r2) == java.lang.Boolean.FALSE) goto L_0x00b6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x00b6, code lost:
            r2 = r5.option;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ba, code lost:
            r1 = gnu.kawa.slib.srfi37.isOptionRequiredArg((gnu.kawa.slib.option$Mntype) r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c0, code lost:
            if (r1 == java.lang.Boolean.FALSE) goto L_0x00d7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c8, code lost:
            if (kawa.lib.lists.isPair(r5.args) == false) goto L_0x00db;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x00d4, code lost:
            if (r1 != false) goto L_0x008c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x00d9, code lost:
            if (r1 != java.lang.Boolean.FALSE) goto L_0x00ca;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x0126, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:0x012e, code lost:
            throw new gnu.mapping.WrongType(r3, "option-required-arg?", 0, r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
            return kawa.standard.call_with_values.callWithValues(r5.lambda$Fn5, r5.lambda$Fn6);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:?, code lost:
            return kawa.standard.call_with_values.callWithValues(r5.lambda$Fn7, r5.lambda$Fn8);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object lambda3scanShortOptions(java.lang.Object r10, java.lang.Object r11, java.lang.Object r12, java.lang.Object r13) {
            /*
                r9 = this;
                r8 = 1
                r7 = 0
                gnu.kawa.slib.srfi37$frame1 r5 = new gnu.kawa.slib.srfi37$frame1
                r5.<init>()
                r5.staticLink = r9
                r5.index = r10
                r5.shorts = r11
                r5.args = r12
                r5.seeds = r13
                gnu.kawa.functions.NumberCompare r3 = kawa.standard.Scheme.numEqu
                java.lang.Object r4 = r5.index
                java.lang.Object r2 = r5.shorts
                java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ ClassCastException -> 0x00e5 }
                int r2 = kawa.lib.strings.stringLength(r2)
                java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
                java.lang.Object r2 = r3.apply2(r4, r2)
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                if (r2 == r3) goto L_0x0032
                java.lang.Object r2 = r5.args
                java.lang.Object r3 = r5.seeds
                java.lang.Object r2 = r9.lambda5scanArgs(r2, r3)
            L_0x0031:
                return r2
            L_0x0032:
                java.lang.Object r2 = r5.shorts
                java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ ClassCastException -> 0x00ee }
                java.lang.Object r4 = r5.index
                r0 = r4
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00f7 }
                r3 = r0
                int r3 = r3.intValue()     // Catch:{ ClassCastException -> 0x00f7 }
                char r2 = kawa.lib.strings.stringRef(r2, r3)
                r5.name = r2
                char r2 = r5.name
                gnu.text.Char r2 = gnu.text.Char.make(r2)
                java.lang.Object r1 = r9.lambda2findOption(r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0095
            L_0x0054:
                r5.option = r1
                gnu.kawa.functions.NumberCompare r3 = kawa.standard.Scheme.numLss
                gnu.kawa.functions.AddOp r2 = gnu.kawa.functions.AddOp.$Pl
                java.lang.Object r4 = r5.index
                gnu.math.IntNum r6 = gnu.kawa.slib.srfi37.Lit0
                java.lang.Object r4 = r2.apply2(r4, r6)
                java.lang.Object r2 = r5.shorts
                java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ ClassCastException -> 0x0101 }
                int r2 = kawa.lib.strings.stringLength(r2)
                java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
                java.lang.Object r3 = r3.apply2(r4, r2)
                r0 = r3
                java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x010a }
                r2 = r0
                boolean r1 = r2.booleanValue()     // Catch:{ ClassCastException -> 0x010a }
                if (r1 == 0) goto L_0x00d4
                java.lang.Object r2 = r5.option
                gnu.kawa.slib.option$Mntype r2 = (gnu.kawa.slib.option$Mntype) r2     // Catch:{ ClassCastException -> 0x0114 }
                java.lang.Object r1 = gnu.kawa.slib.srfi37.isOptionRequiredArg(r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x00aa
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x00b6
            L_0x008c:
                gnu.expr.ModuleMethod r2 = r5.lambda$Fn3
                gnu.expr.ModuleMethod r3 = r5.lambda$Fn4
                java.lang.Object r2 = kawa.standard.call_with_values.callWithValues(r2, r3)
                goto L_0x0031
            L_0x0095:
                char r2 = r5.name
                gnu.text.Char r2 = gnu.text.Char.make(r2)
                gnu.lists.Pair r2 = gnu.lists.LList.list1(r2)
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                java.lang.Boolean r4 = java.lang.Boolean.FALSE
                java.lang.Object r6 = r9.unrecognized$Mnoption$Mnproc
                gnu.kawa.slib.option$Mntype r1 = gnu.kawa.slib.srfi37.option(r2, r3, r4, r6)
                goto L_0x0054
            L_0x00aa:
                java.lang.Object r2 = r5.option
                gnu.kawa.slib.option$Mntype r2 = (gnu.kawa.slib.option$Mntype) r2     // Catch:{ ClassCastException -> 0x011d }
                java.lang.Object r2 = gnu.kawa.slib.srfi37.isOptionOptionalArg(r2)
                java.lang.Boolean r3 = java.lang.Boolean.FALSE
                if (r2 != r3) goto L_0x008c
            L_0x00b6:
                java.lang.Object r2 = r5.option
                gnu.kawa.slib.option$Mntype r2 = (gnu.kawa.slib.option$Mntype) r2     // Catch:{ ClassCastException -> 0x0126 }
                java.lang.Object r1 = gnu.kawa.slib.srfi37.isOptionRequiredArg(r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x00d7
                java.lang.Object r2 = r5.args
                boolean r2 = kawa.lib.lists.isPair(r2)
                if (r2 == 0) goto L_0x00db
            L_0x00ca:
                gnu.expr.ModuleMethod r2 = r5.lambda$Fn5
                gnu.expr.ModuleMethod r3 = r5.lambda$Fn6
                java.lang.Object r2 = kawa.standard.call_with_values.callWithValues(r2, r3)
                goto L_0x0031
            L_0x00d4:
                if (r1 == 0) goto L_0x00b6
                goto L_0x008c
            L_0x00d7:
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 != r2) goto L_0x00ca
            L_0x00db:
                gnu.expr.ModuleMethod r2 = r5.lambda$Fn7
                gnu.expr.ModuleMethod r3 = r5.lambda$Fn8
                java.lang.Object r2 = kawa.standard.call_with_values.callWithValues(r2, r3)
                goto L_0x0031
            L_0x00e5:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "string-length"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r8, (java.lang.Object) r2)
                throw r4
            L_0x00ee:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "string-ref"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r8, (java.lang.Object) r2)
                throw r4
            L_0x00f7:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r5 = "string-ref"
                r6 = 2
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r5, (int) r6, (java.lang.Object) r4)
                throw r3
            L_0x0101:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "string-length"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r8, (java.lang.Object) r2)
                throw r4
            L_0x010a:
                r2 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "x"
                r6 = -2
                r4.<init>((java.lang.ClassCastException) r2, (java.lang.String) r5, (int) r6, (java.lang.Object) r3)
                throw r4
            L_0x0114:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "option-required-arg?"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r7, (java.lang.Object) r2)
                throw r4
            L_0x011d:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "option-optional-arg?"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r7, (java.lang.Object) r2)
                throw r4
            L_0x0126:
                r3 = move-exception
                gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
                java.lang.String r5 = "option-required-arg?"
                r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r7, (java.lang.Object) r2)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi37.frame.lambda3scanShortOptions(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
        }

        public Object lambda4scanOperands(Object operands, Object seeds) {
            frame2 frame2 = new frame2();
            frame2.staticLink = this;
            frame2.operands = operands;
            frame2.seeds = seeds;
            if (lists.isNull(frame2.operands)) {
                return Scheme.apply.apply2(misc.values, frame2.seeds);
            }
            return call_with_values.callWithValues(frame2.lambda$Fn9, frame2.lambda$Fn10);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:100:0x0181, code lost:
            r3 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:101:0x0182, code lost:
            if (r3 == false) goto L_0x01d8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:102:0x0184, code lost:
            r5 = gnu.kawa.slib.srfi37.Lit1;
            r4 = r8.arg;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:106:0x0196, code lost:
            if (kawa.lib.characters.isChar$Eq(r5, gnu.text.Char.make(kawa.lib.strings.stringRef((java.lang.CharSequence) r4, 0))) == false) goto L_0x01da;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:107:0x0198, code lost:
            r4 = r8.arg;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:109:?, code lost:
            r4 = (java.lang.CharSequence) r4;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:110:0x019c, code lost:
            r5 = r8.arg;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:114:0x01b4, code lost:
            if (r3 != false) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:119:0x01d6, code lost:
            r3 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:120:0x01d8, code lost:
            if (r3 != false) goto L_0x0198;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:161:0x0259, code lost:
            r5 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:163:0x0261, code lost:
            throw new gnu.mapping.WrongType(r5, "string-length", 1, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:164:0x0262, code lost:
            r5 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:166:0x026a, code lost:
            throw new gnu.mapping.WrongType(r5, "string-ref", 1, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:167:0x026b, code lost:
            r5 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:169:0x0273, code lost:
            throw new gnu.mapping.WrongType(r5, "substring", 1, r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:170:0x0274, code lost:
            r4 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:172:0x027c, code lost:
            throw new gnu.mapping.WrongType(r4, "string-length", 1, r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:179:?, code lost:
            return lambda3scanShortOptions(gnu.kawa.slib.srfi37.Lit4, kawa.lib.strings.substring(r4, 1, kawa.lib.strings.stringLength((java.lang.CharSequence) r5)), r8.args, r8.seeds);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:181:?, code lost:
            return kawa.standard.call_with_values.callWithValues(r8.lambda$Fn23, r8.lambda$Fn24);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x0135, code lost:
            if (kawa.lib.characters.isChar$Eq(r5, gnu.text.Char.make(kawa.lib.strings.stringRef((java.lang.CharSequence) r4, 1))) != false) goto L_0x0137;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x0175, code lost:
            if (r3 == false) goto L_0x0177;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:95:0x0177, code lost:
            r4 = r8.arg;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x017f, code lost:
            if (kawa.lib.strings.stringLength((java.lang.CharSequence) r4) <= 1) goto L_0x01d6;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object lambda5scanArgs(java.lang.Object r12, java.lang.Object r13) {
            /*
                r11 = this;
                r10 = 2
                r7 = 0
                r6 = 1
                gnu.kawa.slib.srfi37$frame3 r8 = new gnu.kawa.slib.srfi37$frame3
                r8.<init>()
                r8.staticLink = r11
                r8.seeds = r13
                boolean r4 = kawa.lib.lists.isNull(r12)
                if (r4 == 0) goto L_0x001d
                gnu.kawa.functions.Apply r4 = kawa.standard.Scheme.apply
                gnu.expr.ModuleMethod r5 = kawa.lib.misc.values
                java.lang.Object r6 = r8.seeds
                java.lang.Object r4 = r4.apply2(r5, r6)
            L_0x001c:
                return r4
            L_0x001d:
                gnu.expr.GenericProc r4 = kawa.lib.lists.car
                java.lang.Object r4 = r4.apply1(r12)
                gnu.expr.GenericProc r5 = kawa.lib.lists.cdr
                java.lang.Object r5 = r5.apply1(r12)
                r8.args = r5
                r8.arg = r4
                java.lang.String r4 = "--"
                java.lang.Object r5 = r8.arg
                boolean r4 = kawa.lib.strings.isString$Eq(r4, r5)
                if (r4 == 0) goto L_0x0040
                java.lang.Object r4 = r8.args
                java.lang.Object r5 = r8.seeds
                java.lang.Object r4 = r11.lambda4scanOperands(r4, r5)
                goto L_0x001c
            L_0x0040:
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x01e4 }
                int r4 = kawa.lib.strings.stringLength(r4)
                r5 = 4
                if (r4 <= r5) goto L_0x00ba
                r3 = r6
            L_0x004c:
                if (r3 == 0) goto L_0x00f9
                gnu.text.Char r5 = gnu.kawa.slib.srfi37.Lit1
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x01ed }
                char r4 = kawa.lib.strings.stringRef(r4, r7)
                gnu.text.Char r4 = gnu.text.Char.make(r4)
                boolean r3 = kawa.lib.characters.isChar$Eq(r5, r4)
                if (r3 == 0) goto L_0x00f1
                gnu.text.Char r5 = gnu.kawa.slib.srfi37.Lit1
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x01f6 }
                char r4 = kawa.lib.strings.stringRef(r4, r6)
                gnu.text.Char r4 = gnu.text.Char.make(r4)
                boolean r3 = kawa.lib.characters.isChar$Eq(r5, r4)
                if (r3 == 0) goto L_0x00e9
                gnu.text.Char r5 = gnu.kawa.slib.srfi37.Lit2
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x01ff }
                char r4 = kawa.lib.strings.stringRef(r4, r10)
                gnu.text.Char r4 = gnu.text.Char.make(r4)
                boolean r4 = kawa.lib.characters.isChar$Eq(r5, r4)
                int r4 = r4 + 1
                r3 = r4 & 1
                if (r3 == 0) goto L_0x00e1
                gnu.math.IntNum r1 = gnu.kawa.slib.srfi37.Lit3
            L_0x0090:
                gnu.kawa.functions.NumberCompare r5 = kawa.standard.Scheme.numEqu
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x0208 }
                int r4 = kawa.lib.strings.stringLength(r4)
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
                java.lang.Object r4 = r5.apply2(r1, r4)
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x00bc
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
            L_0x00a8:
                r8.temp = r1
                java.lang.Object r4 = r8.temp
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                if (r4 == r5) goto L_0x0101
                gnu.expr.ModuleMethod r4 = r8.lambda$Fn11
                gnu.expr.ModuleMethod r5 = r8.lambda$Fn12
                java.lang.Object r4 = kawa.standard.call_with_values.callWithValues(r4, r5)
                goto L_0x001c
            L_0x00ba:
                r3 = r7
                goto L_0x004c
            L_0x00bc:
                gnu.text.Char r9 = gnu.kawa.slib.srfi37.Lit2
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x0211 }
                r0 = r1
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x021a }
                r5 = r0
                int r5 = r5.intValue()     // Catch:{ ClassCastException -> 0x021a }
                char r4 = kawa.lib.strings.stringRef(r4, r5)
                gnu.text.Char r4 = gnu.text.Char.make(r4)
                boolean r4 = kawa.lib.characters.isChar$Eq(r9, r4)
                if (r4 != 0) goto L_0x00a8
                gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
                gnu.math.IntNum r5 = gnu.kawa.slib.srfi37.Lit0
                java.lang.Object r1 = r4.apply2(r5, r1)
                goto L_0x0090
            L_0x00e1:
                if (r3 == 0) goto L_0x00e6
                java.lang.Boolean r1 = java.lang.Boolean.TRUE
                goto L_0x00a8
            L_0x00e6:
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                goto L_0x00a8
            L_0x00e9:
                if (r3 == 0) goto L_0x00ee
                java.lang.Boolean r1 = java.lang.Boolean.TRUE
                goto L_0x00a8
            L_0x00ee:
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                goto L_0x00a8
            L_0x00f1:
                if (r3 == 0) goto L_0x00f6
                java.lang.Boolean r1 = java.lang.Boolean.TRUE
                goto L_0x00a8
            L_0x00f6:
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                goto L_0x00a8
            L_0x00f9:
                if (r3 == 0) goto L_0x00fe
                java.lang.Boolean r1 = java.lang.Boolean.TRUE
                goto L_0x00a8
            L_0x00fe:
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                goto L_0x00a8
            L_0x0101:
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x0223 }
                int r4 = kawa.lib.strings.stringLength(r4)
                r5 = 3
                if (r4 <= r5) goto L_0x0173
                r3 = r6
            L_0x010d:
                if (r3 == 0) goto L_0x01b4
                gnu.text.Char r5 = gnu.kawa.slib.srfi37.Lit1
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x022c }
                char r4 = kawa.lib.strings.stringRef(r4, r7)
                gnu.text.Char r4 = gnu.text.Char.make(r4)
                boolean r3 = kawa.lib.characters.isChar$Eq(r5, r4)
                if (r3 == 0) goto L_0x0175
                gnu.text.Char r5 = gnu.kawa.slib.srfi37.Lit1
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x0235 }
                char r4 = kawa.lib.strings.stringRef(r4, r6)
                gnu.text.Char r4 = gnu.text.Char.make(r4)
                boolean r4 = kawa.lib.characters.isChar$Eq(r5, r4)
                if (r4 == 0) goto L_0x0177
            L_0x0137:
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x023e }
                java.lang.Object r5 = r8.arg
                java.lang.CharSequence r5 = (java.lang.CharSequence) r5     // Catch:{ ClassCastException -> 0x0247 }
                int r5 = kawa.lib.strings.stringLength(r5)
                java.lang.CharSequence r4 = kawa.lib.strings.substring(r4, r10, r5)
                r8.name = r4
                java.lang.CharSequence r4 = r8.name
                java.lang.Object r3 = r11.lambda2findOption(r4)
                java.lang.Boolean r4 = java.lang.Boolean.FALSE
                if (r3 == r4) goto L_0x01b7
            L_0x0153:
                r8.option = r3
                java.lang.Object r4 = r8.option
                gnu.kawa.slib.option$Mntype r4 = (gnu.kawa.slib.option$Mntype) r4     // Catch:{ ClassCastException -> 0x0250 }
                java.lang.Object r3 = gnu.kawa.slib.srfi37.isOptionRequiredArg(r4)
                java.lang.Boolean r4 = java.lang.Boolean.FALSE
                if (r3 == r4) goto L_0x01c8
                java.lang.Object r4 = r8.args
                boolean r4 = kawa.lib.lists.isPair(r4)
                if (r4 == 0) goto L_0x01cc
            L_0x0169:
                gnu.expr.ModuleMethod r4 = r8.lambda$Fn19
                gnu.expr.ModuleMethod r5 = r8.lambda$Fn20
                java.lang.Object r4 = kawa.standard.call_with_values.callWithValues(r4, r5)
                goto L_0x001c
            L_0x0173:
                r3 = r7
                goto L_0x010d
            L_0x0175:
                if (r3 != 0) goto L_0x0137
            L_0x0177:
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x0259 }
                int r4 = kawa.lib.strings.stringLength(r4)
                if (r4 <= r6) goto L_0x01d6
                r3 = r6
            L_0x0182:
                if (r3 == 0) goto L_0x01d8
                gnu.text.Char r5 = gnu.kawa.slib.srfi37.Lit1
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x0262 }
                char r4 = kawa.lib.strings.stringRef(r4, r7)
                gnu.text.Char r4 = gnu.text.Char.make(r4)
                boolean r4 = kawa.lib.characters.isChar$Eq(r5, r4)
                if (r4 == 0) goto L_0x01da
            L_0x0198:
                java.lang.Object r4 = r8.arg
                java.lang.CharSequence r4 = (java.lang.CharSequence) r4     // Catch:{ ClassCastException -> 0x026b }
                java.lang.Object r5 = r8.arg
                java.lang.CharSequence r5 = (java.lang.CharSequence) r5     // Catch:{ ClassCastException -> 0x0274 }
                int r5 = kawa.lib.strings.stringLength(r5)
                java.lang.CharSequence r2 = kawa.lib.strings.substring(r4, r6, r5)
                gnu.math.IntNum r4 = gnu.kawa.slib.srfi37.Lit4
                java.lang.Object r5 = r8.args
                java.lang.Object r6 = r8.seeds
                java.lang.Object r4 = r11.lambda3scanShortOptions(r4, r2, r5, r6)
                goto L_0x001c
            L_0x01b4:
                if (r3 == 0) goto L_0x0177
                goto L_0x0137
            L_0x01b7:
                java.lang.CharSequence r4 = r8.name
                gnu.lists.Pair r4 = gnu.lists.LList.list1(r4)
                java.lang.Boolean r5 = java.lang.Boolean.FALSE
                java.lang.Boolean r6 = java.lang.Boolean.FALSE
                java.lang.Object r9 = r11.unrecognized$Mnoption$Mnproc
                gnu.kawa.slib.option$Mntype r3 = gnu.kawa.slib.srfi37.option(r4, r5, r6, r9)
                goto L_0x0153
            L_0x01c8:
                java.lang.Boolean r4 = java.lang.Boolean.FALSE
                if (r3 != r4) goto L_0x0169
            L_0x01cc:
                gnu.expr.ModuleMethod r4 = r8.lambda$Fn21
                gnu.expr.ModuleMethod r5 = r8.lambda$Fn22
                java.lang.Object r4 = kawa.standard.call_with_values.callWithValues(r4, r5)
                goto L_0x001c
            L_0x01d6:
                r3 = r7
                goto L_0x0182
            L_0x01d8:
                if (r3 != 0) goto L_0x0198
            L_0x01da:
                gnu.expr.ModuleMethod r4 = r8.lambda$Fn23
                gnu.expr.ModuleMethod r5 = r8.lambda$Fn24
                java.lang.Object r4 = kawa.standard.call_with_values.callWithValues(r4, r5)
                goto L_0x001c
            L_0x01e4:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-length"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x01ed:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-ref"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x01f6:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-ref"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x01ff:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-ref"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x0208:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-length"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x0211:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-ref"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x021a:
                r4 = move-exception
                gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
                java.lang.String r6 = "string-ref"
                r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r10, (java.lang.Object) r1)
                throw r5
            L_0x0223:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-length"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x022c:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-ref"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x0235:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-ref"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x023e:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "substring"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x0247:
                r4 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-length"
                r7.<init>((java.lang.ClassCastException) r4, (java.lang.String) r8, (int) r6, (java.lang.Object) r5)
                throw r7
            L_0x0250:
                r5 = move-exception
                gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
                java.lang.String r8 = "option-required-arg?"
                r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r7, (java.lang.Object) r4)
                throw r6
            L_0x0259:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-length"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x0262:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-ref"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x026b:
                r5 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "substring"
                r7.<init>((java.lang.ClassCastException) r5, (java.lang.String) r8, (int) r6, (java.lang.Object) r4)
                throw r7
            L_0x0274:
                r4 = move-exception
                gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
                java.lang.String r8 = "string-length"
                r7.<init>((java.lang.ClassCastException) r4, (java.lang.String) r8, (int) r6, (java.lang.Object) r5)
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi37.frame.lambda5scanArgs(java.lang.Object, java.lang.Object):java.lang.Object");
        }
    }

    /* compiled from: srfi37.scm */
    public class frame0 extends ModuleBody {
        final ModuleMethod lambda$Fn1;
        final ModuleMethod lambda$Fn2;
        Object name;
        frame staticLink;

        public frame0() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 1, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi37.scm:75");
            this.lambda$Fn2 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 2, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi37.scm:72");
            this.lambda$Fn1 = moduleMethod2;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 1:
                    return lambda7(obj) ? Boolean.TRUE : Boolean.FALSE;
                case 2:
                    return lambda6(obj);
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda6(Object option) {
            try {
                return frame.lambda1find(srfi37.optionNames((option$Mntype) option), this.lambda$Fn2);
            } catch (ClassCastException e) {
                throw new WrongType(e, "option-names", 0, option);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean lambda7(Object test$Mnname) {
            return IsEqual.apply(this.name, test$Mnname);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 1:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 2:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }
    }

    /* compiled from: srfi37.scm */
    public class frame1 extends ModuleBody {
        Object args;
        Object index;
        final ModuleMethod lambda$Fn3 = new ModuleMethod(this, 3, (Object) null, 0);
        final ModuleMethod lambda$Fn4 = new ModuleMethod(this, 4, (Object) null, -4096);
        final ModuleMethod lambda$Fn5 = new ModuleMethod(this, 5, (Object) null, 0);
        final ModuleMethod lambda$Fn6 = new ModuleMethod(this, 6, (Object) null, -4096);
        final ModuleMethod lambda$Fn7 = new ModuleMethod(this, 7, (Object) null, 0);
        final ModuleMethod lambda$Fn8 = new ModuleMethod(this, 8, (Object) null, -4096);
        char name;
        Object option;
        Object seeds;
        Object shorts;
        frame staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 3:
                    return lambda8();
                case 5:
                    return lambda10();
                case 7:
                    return lambda12();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
            switch (moduleMethod.selector) {
                case 4:
                    return lambda9$V(objArr);
                case 6:
                    return lambda11$V(objArr);
                case 8:
                    return lambda13$V(objArr);
                default:
                    return super.applyN(moduleMethod, objArr);
            }
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 3:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 5:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 7:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }

        public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 4:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                case 6:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                case 8:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                default:
                    return super.matchN(moduleMethod, objArr, callContext);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda9$V(Object[] argsArray) {
            return this.staticLink.lambda5scanArgs(this.args, LList.makeList(argsArray, 0));
        }

        /* access modifiers changed from: package-private */
        public Object lambda8() {
            Apply apply = Scheme.apply;
            Object[] objArr = new Object[5];
            Object obj = this.option;
            try {
                objArr[0] = srfi37.optionProcessor((option$Mntype) obj);
                objArr[1] = this.option;
                objArr[2] = Char.make(this.name);
                Object obj2 = this.shorts;
                try {
                    CharSequence charSequence = (CharSequence) obj2;
                    Object apply2 = AddOp.$Pl.apply2(this.index, srfi37.Lit0);
                    try {
                        int intValue = ((Number) apply2).intValue();
                        Object obj3 = this.shorts;
                        try {
                            objArr[3] = strings.substring(charSequence, intValue, strings.stringLength((CharSequence) obj3));
                            objArr[4] = this.seeds;
                            return apply.applyN(objArr);
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-length", 1, obj3);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "substring", 2, apply2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "substring", 1, obj2);
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "option-processor", 0, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda11$V(Object[] argsArray) {
            return this.staticLink.lambda5scanArgs(lists.cdr.apply1(this.args), LList.makeList(argsArray, 0));
        }

        /* access modifiers changed from: package-private */
        public Object lambda10() {
            Apply apply = Scheme.apply;
            Object[] objArr = new Object[5];
            Object obj = this.option;
            try {
                objArr[0] = srfi37.optionProcessor((option$Mntype) obj);
                objArr[1] = this.option;
                objArr[2] = Char.make(this.name);
                objArr[3] = lists.car.apply1(this.args);
                objArr[4] = this.seeds;
                return apply.applyN(objArr);
            } catch (ClassCastException e) {
                throw new WrongType(e, "option-processor", 0, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda13$V(Object[] argsArray) {
            return this.staticLink.lambda3scanShortOptions(AddOp.$Pl.apply2(this.index, srfi37.Lit0), this.shorts, this.args, LList.makeList(argsArray, 0));
        }

        /* access modifiers changed from: package-private */
        public Object lambda12() {
            Apply apply = Scheme.apply;
            Object[] objArr = new Object[5];
            Object obj = this.option;
            try {
                objArr[0] = srfi37.optionProcessor((option$Mntype) obj);
                objArr[1] = this.option;
                objArr[2] = Char.make(this.name);
                objArr[3] = Boolean.FALSE;
                objArr[4] = this.seeds;
                return apply.applyN(objArr);
            } catch (ClassCastException e) {
                throw new WrongType(e, "option-processor", 0, obj);
            }
        }
    }

    /* compiled from: srfi37.scm */
    public class frame2 extends ModuleBody {
        final ModuleMethod lambda$Fn10 = new ModuleMethod(this, 10, (Object) null, -4096);
        final ModuleMethod lambda$Fn9 = new ModuleMethod(this, 9, (Object) null, 0);
        Object operands;
        Object seeds;
        frame staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 9 ? lambda14() : super.apply0(moduleMethod);
        }

        public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
            return moduleMethod.selector == 10 ? lambda15$V(objArr) : super.applyN(moduleMethod, objArr);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 9) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
            if (moduleMethod.selector != 10) {
                return super.matchN(moduleMethod, objArr, callContext);
            }
            callContext.values = objArr;
            callContext.proc = moduleMethod;
            callContext.pc = 5;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda14() {
            return Scheme.apply.apply3(this.staticLink.operand$Mnproc, lists.car.apply1(this.operands), this.seeds);
        }

        /* access modifiers changed from: package-private */
        public Object lambda15$V(Object[] argsArray) {
            return this.staticLink.lambda4scanOperands(lists.cdr.apply1(this.operands), LList.makeList(argsArray, 0));
        }
    }

    /* compiled from: srfi37.scm */
    public class frame3 extends ModuleBody {
        Object arg;
        Object args;
        final ModuleMethod lambda$Fn11 = new ModuleMethod(this, 17, (Object) null, 0);
        final ModuleMethod lambda$Fn12 = new ModuleMethod(this, 18, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        final ModuleMethod lambda$Fn19 = new ModuleMethod(this, 19, (Object) null, 0);
        final ModuleMethod lambda$Fn20 = new ModuleMethod(this, 20, (Object) null, -4096);
        final ModuleMethod lambda$Fn21 = new ModuleMethod(this, 21, (Object) null, 0);
        final ModuleMethod lambda$Fn22 = new ModuleMethod(this, 22, (Object) null, -4096);
        final ModuleMethod lambda$Fn23 = new ModuleMethod(this, 23, (Object) null, 0);
        final ModuleMethod lambda$Fn24 = new ModuleMethod(this, 24, (Object) null, -4096);
        CharSequence name;
        Object option;
        Object seeds;
        frame staticLink;
        Object temp;

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 17:
                    return lambda16();
                case 19:
                    return lambda24();
                case 21:
                    return lambda26();
                case 23:
                    return lambda28();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 18 ? lambda17(obj) : super.apply1(moduleMethod, obj);
        }

        public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
            switch (moduleMethod.selector) {
                case 20:
                    return lambda25$V(objArr);
                case 22:
                    return lambda27$V(objArr);
                case 24:
                    return lambda29$V(objArr);
                default:
                    return super.applyN(moduleMethod, objArr);
            }
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 17:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 19:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 21:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 23:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 18) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }

        public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 20:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                case 22:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                case 24:
                    callContext.values = objArr;
                    callContext.proc = moduleMethod;
                    callContext.pc = 5;
                    return 0;
                default:
                    return super.matchN(moduleMethod, objArr, callContext);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda17(Object x) {
            frame4 frame4 = new frame4();
            frame4.staticLink = this;
            frame4.x = x;
            return call_with_values.callWithValues(frame4.lambda$Fn13, frame4.lambda$Fn14);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda16() {
            Object obj = this.arg;
            try {
                CharSequence charSequence = (CharSequence) obj;
                Object obj2 = this.temp;
                try {
                    return strings.substring(charSequence, 2, ((Number) obj2).intValue());
                } catch (ClassCastException e) {
                    throw new WrongType(e, "substring", 3, obj2);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "substring", 1, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda25$V(Object[] argsArray) {
            return this.staticLink.lambda5scanArgs(lists.cdr.apply1(this.args), LList.makeList(argsArray, 0));
        }

        /* access modifiers changed from: package-private */
        public Object lambda24() {
            Apply apply = Scheme.apply;
            Object[] objArr = new Object[5];
            Object obj = this.option;
            try {
                objArr[0] = srfi37.optionProcessor((option$Mntype) obj);
                objArr[1] = this.option;
                objArr[2] = this.name;
                objArr[3] = lists.car.apply1(this.args);
                objArr[4] = this.seeds;
                return apply.applyN(objArr);
            } catch (ClassCastException e) {
                throw new WrongType(e, "option-processor", 0, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda27$V(Object[] argsArray) {
            return this.staticLink.lambda5scanArgs(this.args, LList.makeList(argsArray, 0));
        }

        /* access modifiers changed from: package-private */
        public Object lambda26() {
            Apply apply = Scheme.apply;
            Object[] objArr = new Object[5];
            Object obj = this.option;
            try {
                objArr[0] = srfi37.optionProcessor((option$Mntype) obj);
                objArr[1] = this.option;
                objArr[2] = this.name;
                objArr[3] = Boolean.FALSE;
                objArr[4] = this.seeds;
                return apply.applyN(objArr);
            } catch (ClassCastException e) {
                throw new WrongType(e, "option-processor", 0, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda28() {
            return Scheme.apply.apply3(this.staticLink.operand$Mnproc, this.arg, this.seeds);
        }

        /* access modifiers changed from: package-private */
        public Object lambda29$V(Object[] argsArray) {
            return this.staticLink.lambda5scanArgs(this.args, LList.makeList(argsArray, 0));
        }
    }

    /* compiled from: srfi37.scm */
    public class frame4 extends ModuleBody {
        final ModuleMethod lambda$Fn13 = new ModuleMethod(this, 15, (Object) null, 0);
        final ModuleMethod lambda$Fn14 = new ModuleMethod(this, 16, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        frame3 staticLink;
        Object x;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 15 ? lambda18() : super.apply0(moduleMethod);
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 16 ? lambda19(obj) : super.apply1(moduleMethod, obj);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 15) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 16) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda19(Object x2) {
            frame5 frame5 = new frame5();
            frame5.staticLink = this;
            frame5.x = x2;
            return call_with_values.callWithValues(frame5.lambda$Fn15, frame5.lambda$Fn16);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda18() {
            Object obj = this.staticLink.arg;
            try {
                CharSequence charSequence = (CharSequence) obj;
                Object apply2 = AddOp.$Pl.apply2(this.staticLink.temp, srfi37.Lit0);
                try {
                    int intValue = ((Number) apply2).intValue();
                    Object obj2 = this.staticLink.arg;
                    try {
                        return strings.substring(charSequence, intValue, strings.stringLength((CharSequence) obj2));
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string-length", 1, obj2);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "substring", 2, apply2);
                }
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "substring", 1, obj);
            }
        }
    }

    /* compiled from: srfi37.scm */
    public class frame5 extends ModuleBody {
        final ModuleMethod lambda$Fn15 = new ModuleMethod(this, 13, (Object) null, 0);
        final ModuleMethod lambda$Fn16 = new ModuleMethod(this, 14, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        frame4 staticLink;
        Object x;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 13 ? lambda20() : super.apply0(moduleMethod);
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 14 ? lambda21(obj) : super.apply1(moduleMethod, obj);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 13) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 14) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda21(Object x2) {
            frame6 frame6 = new frame6();
            frame6.staticLink = this;
            frame6.x = x2;
            return call_with_values.callWithValues(frame6.lambda$Fn17, frame6.lambda$Fn18);
        }

        /* access modifiers changed from: package-private */
        public Object lambda20() {
            Object x2 = this.staticLink.staticLink.staticLink.lambda2findOption(this.staticLink.x);
            return x2 != Boolean.FALSE ? x2 : srfi37.option(LList.list1(this.staticLink.x), Boolean.TRUE, Boolean.FALSE, this.staticLink.staticLink.staticLink.unrecognized$Mnoption$Mnproc);
        }
    }

    /* compiled from: srfi37.scm */
    public class frame6 extends ModuleBody {
        final ModuleMethod lambda$Fn17 = new ModuleMethod(this, 11, (Object) null, 0);
        final ModuleMethod lambda$Fn18 = new ModuleMethod(this, 12, (Object) null, -4096);
        frame5 staticLink;
        Object x;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 11 ? lambda22() : super.apply0(moduleMethod);
        }

        public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
            return moduleMethod.selector == 12 ? lambda23$V(objArr) : super.applyN(moduleMethod, objArr);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 11) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
            if (moduleMethod.selector != 12) {
                return super.matchN(moduleMethod, objArr, callContext);
            }
            callContext.values = objArr;
            callContext.proc = moduleMethod;
            callContext.pc = 5;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda23$V(Object[] argsArray) {
            return this.staticLink.staticLink.staticLink.staticLink.lambda5scanArgs(this.staticLink.staticLink.staticLink.args, LList.makeList(argsArray, 0));
        }

        /* access modifiers changed from: package-private */
        public Object lambda22() {
            Apply apply = Scheme.apply;
            Object[] objArr = new Object[5];
            Object obj = this.x;
            try {
                objArr[0] = srfi37.optionProcessor((option$Mntype) obj);
                objArr[1] = this.x;
                objArr[2] = this.staticLink.staticLink.x;
                objArr[3] = this.staticLink.x;
                objArr[4] = this.staticLink.staticLink.staticLink.seeds;
                return apply.applyN(objArr);
            } catch (ClassCastException e) {
                throw new WrongType(e, "option-processor", 0, obj);
            }
        }
    }
}
