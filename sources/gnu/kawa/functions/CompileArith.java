package gnu.kawa.functions;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Method;
import gnu.bytecode.PrimType;
import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.IgnoreTarget;
import gnu.expr.InlineCalls;
import gnu.expr.Inlineable;
import gnu.expr.PrimProcedure;
import gnu.expr.QuoteExp;
import gnu.expr.StackTarget;
import gnu.expr.Target;
import gnu.kawa.lispexpr.LangObjType;
import gnu.kawa.lispexpr.LangPrimType;
import gnu.mapping.Procedure;
import gnu.math.IntNum;

public class CompileArith implements Inlineable {
    public static CompileArith $Mn = new CompileArith(AddOp.$Mn, 2);
    public static CompileArith $Pl = new CompileArith(AddOp.$Pl, 1);
    int op;
    Procedure proc;

    CompileArith(Object proc2, int op2) {
        this.proc = (Procedure) proc2;
        this.op = op2;
    }

    public static CompileArith forMul(Object proc2) {
        return new CompileArith(proc2, 3);
    }

    public static CompileArith forDiv(Object proc2) {
        return new CompileArith(proc2, ((DivideOp) proc2).op);
    }

    public static CompileArith forBitwise(Object proc2) {
        return new CompileArith(proc2, ((BitwiseOp) proc2).op);
    }

    public static boolean appropriateIntConstant(Expression[] args, int iarg, InlineCalls visitor) {
        Expression exp = visitor.fixIntValue(args[iarg]);
        if (exp == null) {
            return false;
        }
        args[iarg] = exp;
        return true;
    }

    public static boolean appropriateLongConstant(Expression[] args, int iarg, InlineCalls visitor) {
        Expression exp = visitor.fixLongValue(args[iarg]);
        if (exp == null) {
            return false;
        }
        args[iarg] = exp;
        return true;
    }

    public static Expression validateApplyArithOp(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc2) {
        int rkind;
        int op2 = ((ArithOp) proc2).op;
        exp.visitArgs(visitor);
        Expression[] args = exp.getArgs();
        if (args.length > 2) {
            return pairwise(proc2, exp.getFunction(), args, visitor);
        }
        Expression folded = exp.inlineIfConstant(proc2, visitor);
        if (folded != exp) {
            return folded;
        }
        int rkind2 = 0;
        if (args.length == 2 || args.length == 1) {
            int kind1 = Arithmetic.classifyType(args[0].getType());
            if (args.length != 2 || (op2 >= 9 && op2 <= 12)) {
                rkind = kind1;
            } else {
                int kind2 = Arithmetic.classifyType(args[1].getType());
                rkind = getReturnKind(kind1, kind2, op2);
                if (rkind == 4) {
                    if (kind1 == 1 && appropriateIntConstant(args, 1, visitor)) {
                        rkind = 1;
                    } else if (kind2 == 1 && appropriateIntConstant(args, 0, visitor)) {
                        rkind = 1;
                    } else if (kind1 == 2 && appropriateLongConstant(args, 1, visitor)) {
                        rkind = 2;
                    } else if (kind2 == 2 && appropriateLongConstant(args, 0, visitor)) {
                        rkind = 2;
                    }
                }
            }
            rkind2 = adjustReturnKind(rkind, op2);
            exp.setType(Arithmetic.kindType(rkind2));
        }
        if (!visitor.getCompilation().mustCompile) {
            return exp;
        }
        switch (op2) {
            case 1:
            case 2:
                return validateApplyAdd((AddOp) proc2, exp, visitor);
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return validateApplyDiv((DivideOp) proc2, exp, visitor);
            case 16:
                if (rkind2 > 0) {
                    return validateApplyNot(exp, rkind2, visitor);
                }
                return exp;
            default:
                return exp;
        }
    }

    public void compile(ApplyExp exp, Compilation comp, Target target) {
        Type wtype;
        Type wtype2;
        Method meth;
        Expression[] args = exp.getArgs();
        int len = args.length;
        if (len == 0) {
            comp.compileConstant(((ArithOp) this.proc).defaultResult(), target);
        } else if (len == 1 || (target instanceof IgnoreTarget)) {
            ApplyExp.compile(exp, comp, target);
        } else {
            int kind1 = Arithmetic.classifyType(args[0].getType());
            int kind2 = Arithmetic.classifyType(args[1].getType());
            int kind = getReturnKind(kind1, kind2, this.op);
            Type type = Arithmetic.kindType(kind);
            if (kind == 0 || len != 2) {
                ApplyExp.compile(exp, comp, target);
                return;
            }
            int tkind = Arithmetic.classifyType(target.getType());
            if ((tkind == 1 || tkind == 2) && kind >= 1 && kind <= 4) {
                kind = tkind;
                wtype = tkind == 1 ? LangPrimType.intType : LangPrimType.longType;
            } else if ((tkind == 8 || tkind == 7) && kind > 2 && kind <= 10) {
                kind = tkind;
                wtype = tkind == 7 ? LangPrimType.floatType : LangPrimType.doubleType;
            } else if (kind == 7) {
                wtype = LangPrimType.floatType;
            } else if (kind == 8 || kind == 9) {
                kind = 8;
                wtype = LangPrimType.doubleType;
            } else {
                wtype = type;
            }
            if (this.op >= 4 && this.op <= 8) {
                DivideOp dproc = (DivideOp) this.proc;
                if (dproc.op != 4 || (kind > 4 && kind < 6 && kind > 9)) {
                    if ((dproc.op == 5 && kind <= 10 && kind != 7) || (dproc.op == 4 && kind == 10)) {
                        kind = 8;
                    } else if (((dproc.op != 7 && (dproc.op != 6 || kind > 4)) || !(dproc.getRoundingMode() == 3 || kind == 4 || kind == 7 || kind == 8)) && !(dproc.op == 8 && (dproc.getRoundingMode() == 3 || kind == 4))) {
                        ApplyExp.compile(exp, comp, target);
                        return;
                    }
                }
            }
            if (this.op == 4 && kind <= 10 && kind != 8 && kind != 7) {
                if (kind == 6 || kind > 4) {
                    LangObjType ctype = kind == 6 ? Arithmetic.typeRatNum : Arithmetic.typeRealNum;
                    wtype2 = ctype;
                    meth = ctype.getDeclaredMethod("divide", 2);
                } else {
                    wtype2 = Arithmetic.typeIntNum;
                    meth = Arithmetic.typeRatNum.getDeclaredMethod("make", 2);
                }
                Target wtarget = StackTarget.getInstance(wtype2);
                args[0].compile(comp, wtarget);
                args[1].compile(comp, wtarget);
                comp.getCode().emitInvokeStatic(meth);
            } else if (kind == 4 && (this.op == 1 || this.op == 3 || this.op == 2 || this.op == 13 || this.op == 14 || this.op == 15 || this.op == 7 || this.op == 8 || (this.op >= 9 && this.op <= 11))) {
                compileIntNum(args[0], args[1], kind1, kind2, comp);
            } else if (kind == 1 || kind == 2 || ((kind == 7 || kind == 8) && (this.op <= 8 || this.op >= 13))) {
                Target wtarget2 = StackTarget.getInstance(wtype2);
                CodeAttr code = comp.getCode();
                for (int i = 0; i < len; i++) {
                    if (i == 1 && this.op >= 9 && this.op <= 12) {
                        wtarget2 = StackTarget.getInstance(Type.intType);
                    }
                    args[i].compile(comp, wtarget2);
                    if (i != 0) {
                        switch (kind) {
                            case 1:
                            case 2:
                            case 7:
                            case 8:
                                if (this.op != 9) {
                                    code.emitBinop(primitiveOpcode(), (Type) (PrimType) wtype2.getImplementationType());
                                    break;
                                } else {
                                    code.emitInvokeStatic(ClassType.make("gnu.math.IntNum").getDeclaredMethod("shift", new Type[]{wtype2, Type.intType}));
                                    break;
                                }
                        }
                    }
                }
            } else {
                ApplyExp.compile(exp, comp, target);
                return;
            }
            target.compileFromStack(comp, wtype2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:65:0x011f, code lost:
        if (r11 != null) goto L_0x012a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0121, code lost:
        r11 = new gnu.bytecode.Type[]{r22, r23};
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x012a, code lost:
        r12.emitInvokeStatic(r15.getMethod(r19, r11));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0140, code lost:
        if (r19 != null) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0142, code lost:
        r19 = "ior";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x0144, code lost:
        if (r19 != null) goto L_0x0148;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0146, code lost:
        r19 = "xor";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0148, code lost:
        r15 = gnu.bytecode.ClassType.make("gnu.math.BitOps");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:?, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean compileIntNum(gnu.expr.Expression r26, gnu.expr.Expression r27, int r28, int r29, gnu.expr.Compilation r30) {
        /*
            r25 = this;
            r0 = r25
            int r4 = r0.op
            r5 = 2
            if (r4 != r5) goto L_0x0069
            r0 = r27
            boolean r4 = r0 instanceof gnu.expr.QuoteExp
            if (r4 == 0) goto L_0x0069
            java.lang.Object r24 = r27.valueIfConstant()
            r4 = 2
            r0 = r29
            if (r0 > r4) goto L_0x004b
            java.lang.Number r24 = (java.lang.Number) r24
            long r16 = r24.longValue()
            r4 = -2147483648(0xffffffff80000000, double:NaN)
            int r4 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x0048
            r4 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r4 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r4 > 0) goto L_0x0048
            r20 = 1
        L_0x002c:
            if (r20 == 0) goto L_0x0069
            gnu.kawa.functions.CompileArith r4 = $Pl
            r0 = r16
            long r6 = -r0
            int r5 = (int) r6
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            gnu.expr.QuoteExp r6 = gnu.expr.QuoteExp.getInstance(r5)
            r8 = 1
            r5 = r26
            r7 = r28
            r9 = r30
            boolean r4 = r4.compileIntNum(r5, r6, r7, r8, r9)
        L_0x0047:
            return r4
        L_0x0048:
            r20 = 0
            goto L_0x002c
        L_0x004b:
            r0 = r24
            boolean r4 = r0 instanceof gnu.math.IntNum
            if (r4 == 0) goto L_0x0064
            r14 = r24
            gnu.math.IntNum r14 = (gnu.math.IntNum) r14
            long r16 = r14.longValue()
            r4 = -2147483647(0xffffffff80000001, double:NaN)
            r6 = 2147483647(0x7fffffff, double:1.060997895E-314)
            boolean r20 = r14.inRange(r4, r6)
            goto L_0x002c
        L_0x0064:
            r20 = 0
            r16 = 0
            goto L_0x002c
        L_0x0069:
            r0 = r25
            int r4 = r0.op
            r5 = 1
            if (r4 == r5) goto L_0x0077
            r0 = r25
            int r4 = r0.op
            r5 = 3
            if (r4 != r5) goto L_0x00b5
        L_0x0077:
            r10 = 1
        L_0x0078:
            if (r10 == 0) goto L_0x00ff
            java.lang.Integer r4 = gnu.expr.InlineCalls.checkIntValue(r26)
            if (r4 == 0) goto L_0x0082
            r28 = 1
        L_0x0082:
            java.lang.Integer r4 = gnu.expr.InlineCalls.checkIntValue(r27)
            if (r4 == 0) goto L_0x008a
            r29 = 1
        L_0x008a:
            r4 = 1
            r0 = r28
            if (r0 != r4) goto L_0x00b7
            r4 = 1
            r0 = r29
            if (r0 == r4) goto L_0x00b7
            r21 = 1
        L_0x0096:
            if (r21 == 0) goto L_0x00ba
            boolean r4 = r26.side_effects()
            if (r4 == 0) goto L_0x00a4
            boolean r4 = r27.side_effects()
            if (r4 != 0) goto L_0x00ba
        L_0x00a4:
            r4 = r25
            r5 = r27
            r6 = r26
            r7 = r29
            r8 = r28
            r9 = r30
            boolean r4 = r4.compileIntNum(r5, r6, r7, r8, r9)
            goto L_0x0047
        L_0x00b5:
            r10 = 0
            goto L_0x0078
        L_0x00b7:
            r21 = 0
            goto L_0x0096
        L_0x00ba:
            r4 = 1
            r0 = r28
            if (r0 != r4) goto L_0x00f9
            gnu.bytecode.PrimType r22 = gnu.bytecode.Type.intType
        L_0x00c1:
            r4 = 1
            r0 = r29
            if (r0 != r4) goto L_0x00fc
            gnu.bytecode.PrimType r23 = gnu.bytecode.Type.intType
        L_0x00c8:
            r0 = r26
            r1 = r30
            r2 = r22
            r0.compile((gnu.expr.Compilation) r1, (gnu.bytecode.Type) r2)
            r0 = r27
            r1 = r30
            r2 = r23
            r0.compile((gnu.expr.Compilation) r1, (gnu.bytecode.Type) r2)
            gnu.bytecode.CodeAttr r12 = r30.getCode()
            if (r21 == 0) goto L_0x00e7
            r12.emitSwap()
            gnu.kawa.lispexpr.LangObjType r22 = gnu.kawa.functions.Arithmetic.typeIntNum
            gnu.bytecode.PrimType r23 = gnu.kawa.lispexpr.LangPrimType.intType
        L_0x00e7:
            r19 = 0
            r11 = 0
            gnu.kawa.lispexpr.LangObjType r15 = gnu.kawa.functions.Arithmetic.typeIntNum
            r0 = r25
            int r4 = r0.op
            switch(r4) {
                case 1: goto L_0x011d;
                case 2: goto L_0x0138;
                case 3: goto L_0x013b;
                case 4: goto L_0x014f;
                case 5: goto L_0x014f;
                case 6: goto L_0x014f;
                case 7: goto L_0x014f;
                case 8: goto L_0x014f;
                case 9: goto L_0x019f;
                case 10: goto L_0x018b;
                case 11: goto L_0x018b;
                case 12: goto L_0x00f3;
                case 13: goto L_0x013e;
                case 14: goto L_0x0140;
                case 15: goto L_0x0144;
                default: goto L_0x00f3;
            }
        L_0x00f3:
            java.lang.Error r4 = new java.lang.Error
            r4.<init>()
            throw r4
        L_0x00f9:
            gnu.kawa.lispexpr.LangObjType r22 = gnu.kawa.functions.Arithmetic.typeIntNum
            goto L_0x00c1
        L_0x00fc:
            gnu.kawa.lispexpr.LangObjType r23 = gnu.kawa.functions.Arithmetic.typeIntNum
            goto L_0x00c8
        L_0x00ff:
            r0 = r25
            int r4 = r0.op
            r5 = 9
            if (r4 < r5) goto L_0x0116
            r0 = r25
            int r4 = r0.op
            r5 = 12
            if (r4 > r5) goto L_0x0116
            gnu.kawa.lispexpr.LangObjType r22 = gnu.kawa.functions.Arithmetic.typeIntNum
            gnu.bytecode.PrimType r23 = gnu.bytecode.Type.intType
            r21 = 0
            goto L_0x00c8
        L_0x0116:
            gnu.kawa.lispexpr.LangObjType r23 = gnu.kawa.functions.Arithmetic.typeIntNum
            r22 = r23
            r21 = 0
            goto L_0x00c8
        L_0x011d:
            java.lang.String r19 = "add"
        L_0x011f:
            if (r11 != 0) goto L_0x012a
            r4 = 2
            gnu.bytecode.Type[] r11 = new gnu.bytecode.Type[r4]
            r4 = 0
            r11[r4] = r22
            r4 = 1
            r11[r4] = r23
        L_0x012a:
            r0 = r19
            gnu.bytecode.Method r18 = r15.getMethod(r0, r11)
            r0 = r18
            r12.emitInvokeStatic(r0)
            r4 = 1
            goto L_0x0047
        L_0x0138:
            java.lang.String r19 = "sub"
            goto L_0x011f
        L_0x013b:
            java.lang.String r19 = "times"
            goto L_0x011f
        L_0x013e:
            java.lang.String r19 = "and"
        L_0x0140:
            if (r19 != 0) goto L_0x0144
            java.lang.String r19 = "ior"
        L_0x0144:
            if (r19 != 0) goto L_0x0148
            java.lang.String r19 = "xor"
        L_0x0148:
            java.lang.String r4 = "gnu.math.BitOps"
            gnu.bytecode.ClassType r15 = gnu.bytecode.ClassType.make(r4)
            goto L_0x011f
        L_0x014f:
            r0 = r25
            int r4 = r0.op
            r5 = 8
            if (r4 != r5) goto L_0x016f
            java.lang.String r19 = "remainder"
        L_0x0159:
            r0 = r25
            gnu.mapping.Procedure r13 = r0.proc
            gnu.kawa.functions.DivideOp r13 = (gnu.kawa.functions.DivideOp) r13
            r0 = r25
            int r4 = r0.op
            r5 = 8
            if (r4 != r5) goto L_0x0172
            int r4 = r13.rounding_mode
            r5 = 1
            if (r4 != r5) goto L_0x0172
            java.lang.String r19 = "modulo"
            goto L_0x011f
        L_0x016f:
            java.lang.String r19 = "quotient"
            goto L_0x0159
        L_0x0172:
            int r4 = r13.rounding_mode
            r5 = 3
            if (r4 == r5) goto L_0x011f
            int r4 = r13.rounding_mode
            r12.emitPushInt(r4)
            r4 = 3
            gnu.bytecode.Type[] r11 = new gnu.bytecode.Type[r4]
            r4 = 0
            r11[r4] = r22
            r4 = 1
            r11[r4] = r23
            r4 = 2
            gnu.bytecode.PrimType r5 = gnu.bytecode.Type.intType
            r11[r4] = r5
            goto L_0x011f
        L_0x018b:
            r0 = r25
            int r4 = r0.op
            r5 = 10
            if (r4 != r5) goto L_0x019c
            java.lang.String r19 = "shiftLeft"
        L_0x0195:
            java.lang.String r4 = "gnu.kawa.functions.BitwiseOp"
            gnu.bytecode.ClassType r15 = gnu.bytecode.ClassType.make(r4)
            goto L_0x011f
        L_0x019c:
            java.lang.String r19 = "shiftRight"
            goto L_0x0195
        L_0x019f:
            java.lang.String r19 = "shift"
            goto L_0x011f
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.functions.CompileArith.compileIntNum(gnu.expr.Expression, gnu.expr.Expression, int, int, gnu.expr.Compilation):boolean");
    }

    public static int getReturnKind(int kind1, int kind2, int op2) {
        if (op2 >= 9 && op2 <= 12) {
            return kind1;
        }
        if (kind1 <= 0 || (kind1 > kind2 && kind2 > 0)) {
            kind2 = kind1;
        }
        return kind2;
    }

    public int getReturnKind(Expression[] args) {
        int len = args.length;
        if (len == 0) {
            return 4;
        }
        ClassType classType = Type.pointer_type;
        int kindr = 0;
        for (int i = 0; i < len; i++) {
            int kind = Arithmetic.classifyType(args[i].getType());
            if (i == 0 || kind == 0 || kind > kindr) {
                kindr = kind;
            }
        }
        return kindr;
    }

    public Type getReturnType(Expression[] args) {
        return Arithmetic.kindType(adjustReturnKind(getReturnKind(args), this.op));
    }

    static int adjustReturnKind(int rkind, int op2) {
        if (op2 < 4 || op2 > 7 || rkind <= 0) {
            return rkind;
        }
        switch (op2) {
            case 4:
                if (rkind <= 4) {
                    return 6;
                }
                return rkind;
            case 5:
                if (rkind > 10 || rkind == 7) {
                    return rkind;
                }
                return 8;
            case 7:
                if (rkind <= 10) {
                    return 4;
                }
                return rkind;
            default:
                return rkind;
        }
    }

    public static Expression validateApplyAdd(AddOp proc2, ApplyExp exp, InlineCalls visitor) {
        Expression[] args = exp.getArgs();
        if (args.length != 1 || proc2.plusOrMinus >= 0) {
            return exp;
        }
        Type type0 = args[0].getType();
        if (!(type0 instanceof PrimType)) {
            return exp;
        }
        char sig0 = type0.getSignature().charAt(0);
        Type type = null;
        int opcode = 0;
        if (!(sig0 == 'V' || sig0 == 'Z' || sig0 == 'C')) {
            if (sig0 == 'D') {
                opcode = 119;
                type = LangPrimType.doubleType;
            } else if (sig0 == 'F') {
                opcode = 118;
                type = LangPrimType.floatType;
            } else if (sig0 == 'J') {
                opcode = 117;
                type = LangPrimType.longType;
            } else {
                opcode = 116;
                type = LangPrimType.intType;
            }
        }
        if (type != null) {
            return new ApplyExp((Procedure) PrimProcedure.makeBuiltinUnary(opcode, type), args);
        }
        return exp;
    }

    public static Expression validateApplyDiv(DivideOp proc2, ApplyExp exp, InlineCalls visitor) {
        Expression[] args = exp.getArgs();
        if (args.length != 1) {
            return exp;
        }
        Expression[] args2 = {QuoteExp.getInstance(IntNum.one()), args[0]};
        Expression[] expressionArr = args2;
        return new ApplyExp(exp.getFunction(), args2);
    }

    public static Expression validateApplyNot(ApplyExp exp, int kind, InlineCalls visitor) {
        String cname;
        if (exp.getArgCount() != 1) {
            return exp;
        }
        Expression arg = exp.getArg(0);
        if (kind == 1 || kind == 2) {
            return visitor.visitApplyOnly(new ApplyExp((Procedure) BitwiseOp.xor, arg, QuoteExp.getInstance(IntNum.minusOne())), (Type) null);
        }
        if (kind == 4) {
            cname = "gnu.math.BitOps";
        } else if (kind == 3) {
            cname = "java.meth.BigInteger";
        } else {
            cname = null;
        }
        if (cname != null) {
            return new ApplyExp(ClassType.make(cname).getDeclaredMethod("not", 1), exp.getArgs());
        }
        return exp;
    }

    public static Expression validateApplyNumberCompare(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc2) {
        exp.visitArgs(visitor);
        Expression folded = exp.inlineIfConstant(proc2, visitor);
        return folded != exp ? folded : exp;
    }

    public int primitiveOpcode() {
        switch (this.op) {
            case 1:
                return 96;
            case 2:
                return 100;
            case 3:
                return 104;
            case 4:
            case 5:
            case 6:
            case 7:
                return 108;
            case 8:
                return 112;
            case 10:
                return 120;
            case 11:
                return 122;
            case 12:
                return 124;
            case 13:
                return 126;
            case 14:
                return 128;
            case 15:
                return 130;
            default:
                return -1;
        }
    }

    public static Expression pairwise(Procedure proc2, Expression rproc, Expression[] args, InlineCalls visitor) {
        int len = args.length;
        Expression prev = args[0];
        for (int i = 1; i < len; i++) {
            ApplyExp next = new ApplyExp(rproc, prev, args[i]);
            Expression inlined = visitor.maybeInline(next, (Type) null, proc2);
            prev = inlined != null ? inlined : next;
        }
        return prev;
    }

    public static Expression validateApplyNumberPredicate(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc2) {
        int i = ((NumberPredicate) proc2).op;
        Expression[] args = exp.getArgs();
        args[0] = visitor.visit(args[0], (Type) LangObjType.integerType);
        exp.setType(Type.booleanType);
        return exp;
    }
}
