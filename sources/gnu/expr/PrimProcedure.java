package gnu.expr;

import gnu.bytecode.ArrayType;
import gnu.bytecode.ClassFileInput;
import gnu.bytecode.ClassType;
import gnu.bytecode.ClassTypeWriter;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Field;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.kawa.functions.MakeList;
import gnu.kawa.lispexpr.LangObjType;
import gnu.lists.Consumer;
import gnu.lists.ConsumerWriter;
import gnu.mapping.CallContext;
import gnu.mapping.MethodProc;
import gnu.mapping.Procedure;
import gnu.mapping.ProcedureN;
import gnu.mapping.WrongArguments;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.net.URL;

public class PrimProcedure extends MethodProc implements Inlineable {
    private static ClassLoader systemClassLoader = PrimProcedure.class.getClassLoader();
    Type[] argTypes;
    Member member;
    Method method;
    char mode;
    int op_code;
    Type retType;
    boolean sideEffectFree;
    LambdaExp source;

    public final int opcode() {
        return this.op_code;
    }

    public Type getReturnType() {
        return this.retType;
    }

    public void setReturnType(Type retType2) {
        this.retType = retType2;
    }

    public boolean isSpecial() {
        return this.mode == 'P';
    }

    public Type getReturnType(Expression[] args) {
        return this.retType;
    }

    public Method getMethod() {
        return this.method;
    }

    public boolean isSideEffectFree() {
        return this.sideEffectFree;
    }

    public void setSideEffectFree() {
        this.sideEffectFree = true;
    }

    public boolean takesVarArgs() {
        if (this.method == null) {
            return false;
        }
        if ((this.method.getModifiers() & 128) != 0) {
            return true;
        }
        String name = this.method.getName();
        if (name.endsWith("$V") || name.endsWith("$V$X")) {
            return true;
        }
        return false;
    }

    public boolean takesContext() {
        return this.method != null && takesContext(this.method);
    }

    public static boolean takesContext(Method method2) {
        return method2.getName().endsWith("$X");
    }

    public int isApplicable(Type[] argTypes2) {
        int app = super.isApplicable(argTypes2);
        int nargs = argTypes2.length;
        if (app != -1 || this.method == null || (this.method.getModifiers() & 128) == 0 || nargs <= 0 || !(argTypes2[nargs - 1] instanceof ArrayType)) {
            return app;
        }
        Type[] tmp = new Type[nargs];
        System.arraycopy(argTypes2, 0, tmp, 0, nargs - 1);
        tmp[nargs - 1] = argTypes2[nargs - 1].getComponentType();
        return super.isApplicable(tmp);
    }

    public final boolean isConstructor() {
        return opcode() == 183 && this.mode != 'P';
    }

    public boolean takesTarget() {
        return this.mode != 0;
    }

    public int numArgs() {
        int num = this.argTypes.length;
        if (takesTarget()) {
            num++;
        }
        if (takesContext()) {
            num--;
        }
        return takesVarArgs() ? (num - 1) - 4096 : (num << 12) + num;
    }

    public int match0(CallContext ctx) {
        return matchN(ProcedureN.noArgs, ctx);
    }

    public int match1(Object arg1, CallContext ctx) {
        return matchN(new Object[]{arg1}, ctx);
    }

    public int match2(Object arg1, Object arg2, CallContext ctx) {
        return matchN(new Object[]{arg1, arg2}, ctx);
    }

    public int match3(Object arg1, Object arg2, Object arg3, CallContext ctx) {
        return matchN(new Object[]{arg1, arg2, arg3}, ctx);
    }

    public int match4(Object arg1, Object arg2, Object arg3, Object arg4, CallContext ctx) {
        return matchN(new Object[]{arg1, arg2, arg3, arg4}, ctx);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v0, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int matchN(java.lang.Object[] r22, gnu.mapping.CallContext r23) {
        /*
            r21 = this;
            r0 = r22
            int r10 = r0.length
            boolean r17 = r21.takesVarArgs()
            int r8 = r21.minArgs()
            if (r10 >= r8) goto L_0x0012
            r19 = -983040(0xfffffffffff10000, float:NaN)
            r19 = r19 | r8
        L_0x0011:
            return r19
        L_0x0012:
            if (r17 != 0) goto L_0x001b
            if (r10 <= r8) goto L_0x001b
            r19 = -917504(0xfffffffffff20000, float:NaN)
            r19 = r19 | r8
            goto L_0x0011
        L_0x001b:
            r0 = r21
            gnu.bytecode.Type[] r0 = r0.argTypes
            r19 = r0
            r0 = r19
            int r11 = r0.length
            r4 = 0
            r13 = 0
            boolean r19 = r21.takesTarget()
            if (r19 != 0) goto L_0x0032
            boolean r19 = r21.isConstructor()
            if (r19 == 0) goto L_0x009d
        L_0x0032:
            r7 = 1
        L_0x0033:
            boolean r16 = r21.takesContext()
            java.lang.Object[] r12 = new java.lang.Object[r11]
            if (r16 == 0) goto L_0x003f
            int r11 = r11 + -1
            r12[r11] = r23
        L_0x003f:
            if (r17 == 0) goto L_0x0064
            r0 = r21
            gnu.bytecode.Type[] r0 = r0.argTypes
            r19 = r0
            int r20 = r11 + -1
            r15 = r19[r20]
            gnu.bytecode.ClassType r19 = gnu.expr.Compilation.scmListType
            r0 = r19
            if (r15 == r0) goto L_0x0057
            gnu.kawa.lispexpr.LangObjType r19 = gnu.kawa.lispexpr.LangObjType.listType
            r0 = r19
            if (r15 != r0) goto L_0x009f
        L_0x0057:
            int r19 = r11 + -1
            r0 = r22
            gnu.lists.LList r20 = gnu.lists.LList.makeList(r0, r8)
            r12[r19] = r20
            r10 = r8
            gnu.bytecode.ClassType r4 = gnu.bytecode.Type.objectType
        L_0x0064:
            boolean r19 = r21.isConstructor()
            if (r19 == 0) goto L_0x00bd
            r19 = 0
            r6 = r22[r19]
        L_0x006e:
            r9 = r7
        L_0x006f:
            r0 = r22
            int r0 = r0.length
            r19 = r0
            r0 = r19
            if (r9 >= r0) goto L_0x00ed
            r2 = r22[r9]
            if (r9 >= r8) goto L_0x00da
            r0 = r21
            gnu.bytecode.Type[] r0 = r0.argTypes
            r19 = r0
            int r20 = r9 - r7
            r18 = r19[r20]
        L_0x0086:
            gnu.bytecode.ClassType r19 = gnu.bytecode.Type.objectType
            r0 = r18
            r1 = r19
            if (r0 == r1) goto L_0x0094
            r0 = r18
            java.lang.Object r2 = r0.coerceFromObject(r2)     // Catch:{ ClassCastException -> 0x00dd }
        L_0x0094:
            if (r9 >= r8) goto L_0x00e6
            int r19 = r9 - r7
            r12[r19] = r2
        L_0x009a:
            int r9 = r9 + 1
            goto L_0x006f
        L_0x009d:
            r7 = 0
            goto L_0x0033
        L_0x009f:
            r14 = r15
            gnu.bytecode.ArrayType r14 = (gnu.bytecode.ArrayType) r14
            gnu.bytecode.Type r4 = r14.getComponentType()
            java.lang.Class r3 = r4.getReflectClass()
            int r19 = r10 - r8
            r0 = r19
            java.lang.Object r19 = java.lang.reflect.Array.newInstance(r3, r0)
            java.lang.Object[] r19 = (java.lang.Object[]) r19
            r13 = r19
            java.lang.Object[] r13 = (java.lang.Object[]) r13
            int r19 = r11 + -1
            r12[r19] = r13
            goto L_0x0064
        L_0x00bd:
            if (r7 == 0) goto L_0x00d8
            r0 = r21
            gnu.bytecode.Method r0 = r0.method     // Catch:{ ClassCastException -> 0x00d2 }
            r19 = r0
            gnu.bytecode.ClassType r19 = r19.getDeclaringClass()     // Catch:{ ClassCastException -> 0x00d2 }
            r20 = 0
            r20 = r22[r20]     // Catch:{ ClassCastException -> 0x00d2 }
            java.lang.Object r6 = r19.coerceFromObject(r20)     // Catch:{ ClassCastException -> 0x00d2 }
            goto L_0x006e
        L_0x00d2:
            r5 = move-exception
            r19 = -786431(0xfffffffffff40001, float:NaN)
            goto L_0x0011
        L_0x00d8:
            r6 = 0
            goto L_0x006e
        L_0x00da:
            r18 = r4
            goto L_0x0086
        L_0x00dd:
            r5 = move-exception
            r19 = -786432(0xfffffffffff40000, float:NaN)
            int r20 = r9 + 1
            r19 = r19 | r20
            goto L_0x0011
        L_0x00e6:
            if (r13 == 0) goto L_0x009a
            int r19 = r9 - r8
            r13[r19] = r2
            goto L_0x009a
        L_0x00ed:
            r0 = r23
            r0.value1 = r6
            r0 = r23
            r0.values = r12
            r0 = r21
            r1 = r23
            r1.proc = r0
            r19 = 0
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.PrimProcedure.matchN(java.lang.Object[], gnu.mapping.CallContext):int");
    }

    public void apply(CallContext ctx) throws Throwable {
        Object result;
        int i;
        int arg_count = this.argTypes.length;
        boolean is_constructor = isConstructor();
        boolean slink = is_constructor && this.method.getDeclaringClass().hasOuterLink();
        try {
            if (this.member == null) {
                Class clas = this.method.getDeclaringClass().getReflectClass();
                Class[] paramTypes = new Class[((slink ? 1 : 0) + arg_count)];
                int i2 = arg_count;
                while (true) {
                    i2--;
                    if (i2 < 0) {
                        break;
                    }
                    if (slink) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    paramTypes[i + i2] = this.argTypes[i2].getReflectClass();
                }
                if (slink) {
                    paramTypes[0] = this.method.getDeclaringClass().getOuterLinkType().getReflectClass();
                }
                if (is_constructor) {
                    this.member = clas.getConstructor(paramTypes);
                } else if (this.method != Type.clone_method) {
                    this.member = clas.getMethod(this.method.getName(), paramTypes);
                }
            }
            if (is_constructor) {
                Object[] args = ctx.values;
                if (slink) {
                    int nargs = args.length + 1;
                    Object[] xargs = new Object[nargs];
                    System.arraycopy(args, 0, xargs, 1, nargs - 1);
                    xargs[0] = ((PairClassType) ctx.value1).staticLink;
                    args = xargs;
                }
                result = ((Constructor) this.member).newInstance(args);
            } else if (this.method == Type.clone_method) {
                Object arr = ctx.value1;
                Class elClass = arr.getClass().getComponentType();
                int n = Array.getLength(arr);
                result = Array.newInstance(elClass, n);
                System.arraycopy(arr, 0, result, 0, n);
            } else {
                result = this.retType.coerceToObject(((java.lang.reflect.Method) this.member).invoke(ctx.value1, ctx.values));
            }
            if (!takesContext()) {
                ctx.consumer.writeObject(result);
            }
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }

    public PrimProcedure(String className, String methodName, int numArgs) {
        this(ClassType.make(className).getDeclaredMethod(methodName, numArgs));
    }

    public PrimProcedure(java.lang.reflect.Method method2, Language language) {
        this(((ClassType) language.getTypeFor((Class) method2.getDeclaringClass())).getMethod(method2), language);
    }

    public PrimProcedure(Method method2) {
        init(method2);
        this.retType = method2.getName().endsWith("$X") ? Type.objectType : method2.getReturnType();
    }

    public PrimProcedure(Method method2, Language language) {
        this(method2, 0, language);
    }

    public PrimProcedure(Method method2, char mode2, Language language) {
        this.mode = mode2;
        init(method2);
        Type[] pTypes = this.argTypes;
        int nTypes = pTypes.length;
        this.argTypes = null;
        int i = nTypes;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            Type javaType = pTypes[i];
            Type langType = language.getLangTypeFor(javaType);
            if (javaType != langType) {
                if (this.argTypes == null) {
                    this.argTypes = new Type[nTypes];
                    System.arraycopy(pTypes, 0, this.argTypes, 0, nTypes);
                }
                this.argTypes[i] = langType;
            }
        }
        if (this.argTypes == null) {
            this.argTypes = pTypes;
        }
        if (isConstructor()) {
            this.retType = method2.getDeclaringClass();
        } else if (method2.getName().endsWith("$X")) {
            this.retType = Type.objectType;
        } else {
            this.retType = language.getLangTypeFor(method2.getReturnType());
            if (this.retType == Type.toStringType) {
                this.retType = Type.javalangStringType;
            }
        }
    }

    private void init(Method method2) {
        this.method = method2;
        if ((method2.getModifiers() & 8) != 0) {
            this.op_code = 184;
        } else {
            ClassType mclass = method2.getDeclaringClass();
            if (this.mode == 'P') {
                this.op_code = 183;
            } else {
                this.mode = 'V';
                if ("<init>".equals(method2.getName())) {
                    this.op_code = 183;
                } else if ((mclass.getModifiers() & 512) != 0) {
                    this.op_code = 185;
                } else {
                    this.op_code = 182;
                }
            }
        }
        Type[] mtypes = method2.getParameterTypes();
        if (isConstructor() && method2.getDeclaringClass().hasOuterLink()) {
            int len = mtypes.length - 1;
            Type[] types = new Type[len];
            System.arraycopy(mtypes, 1, types, 0, len);
            mtypes = types;
        }
        this.argTypes = mtypes;
    }

    public PrimProcedure(Method method2, LambdaExp source2) {
        this(method2);
        this.retType = source2.getReturnType();
        this.source = source2;
    }

    public PrimProcedure(int opcode, Type retType2, Type[] argTypes2) {
        this.op_code = opcode;
        this.retType = retType2;
        this.argTypes = argTypes2;
    }

    public static PrimProcedure makeBuiltinUnary(int opcode, Type type) {
        return new PrimProcedure(opcode, type, new Type[]{type});
    }

    public static PrimProcedure makeBuiltinBinary(int opcode, Type type) {
        return new PrimProcedure(opcode, type, new Type[]{type, type});
    }

    public PrimProcedure(int op_code2, ClassType classtype, String name, Type retType2, Type[] argTypes2) {
        char c = 0;
        this.op_code = op_code2;
        this.method = classtype.addMethod(name, op_code2 == 184 ? 8 : 0, argTypes2, retType2);
        this.retType = retType2;
        this.argTypes = argTypes2;
        this.mode = op_code2 != 184 ? 'V' : c;
    }

    public final boolean getStaticFlag() {
        return this.method == null || this.method.getStaticFlag() || isConstructor();
    }

    public final Type[] getParameterTypes() {
        return this.argTypes;
    }

    private void compileArgs(Expression[] args, int startArg, Type thisType, Compilation comp) {
        int fix_arg_count;
        Declaration argDecl;
        Type argTypeForTarget;
        boolean variable = takesVarArgs();
        String name = getName();
        Type arg_type = null;
        CodeAttr code = comp.getCode();
        int skipArg = thisType == Type.voidType ? 1 : 0;
        int arg_count = this.argTypes.length - skipArg;
        if (takesContext()) {
            arg_count--;
        }
        int nargs = args.length - startArg;
        boolean is_static = thisType == null || skipArg != 0;
        boolean createVarargsArrayIfNeeded = false;
        if (variable && (this.method.getModifiers() & 128) != 0 && nargs > 0 && this.argTypes.length > 0) {
            if (nargs == (is_static ? 0 : 1) + arg_count) {
                Type lastType = args[args.length - 1].getType();
                Type lastParam = this.argTypes[this.argTypes.length - 1];
                if ((lastType instanceof ObjectType) && (lastParam instanceof ArrayType) && !(((ArrayType) lastParam).getComponentType() instanceof ArrayType)) {
                    if (!(lastType instanceof ArrayType)) {
                        createVarargsArrayIfNeeded = true;
                    }
                    variable = false;
                }
            }
        }
        if (variable) {
            fix_arg_count = arg_count - (is_static ? 1 : 0);
        } else {
            fix_arg_count = args.length - startArg;
        }
        if (this.source == null) {
            argDecl = null;
        } else {
            argDecl = this.source.firstDecl();
        }
        if (argDecl != null && argDecl.isThisParameter()) {
            argDecl = argDecl.nextDecl();
        }
        int i = 0;
        while (true) {
            if (variable && i == fix_arg_count) {
                Type arg_type2 = this.argTypes[(arg_count - 1) + skipArg];
                if (arg_type2 == Compilation.scmListType || arg_type2 == LangObjType.listType) {
                    MakeList.compile(args, startArg + i, comp);
                } else {
                    code.emitPushInt((args.length - startArg) - fix_arg_count);
                    arg_type = ((ArrayType) arg_type2).getComponentType();
                    code.emitNewArray(arg_type);
                }
            }
            if (i < nargs) {
                boolean createVarargsNow = createVarargsArrayIfNeeded && i + 1 == nargs;
                if (i >= fix_arg_count) {
                    code.emitDup(1);
                    code.emitPushInt(i - fix_arg_count);
                } else {
                    arg_type = (argDecl == null || (!is_static && i <= 0)) ? is_static ? this.argTypes[i + skipArg] : i == 0 ? thisType : this.argTypes[i - 1] : argDecl.getType();
                }
                comp.usedClass(arg_type);
                if (createVarargsNow) {
                    argTypeForTarget = Type.objectType;
                } else {
                    argTypeForTarget = arg_type;
                }
                args[startArg + i].compileNotePosition(comp, this.source == null ? CheckedTarget.getInstance(argTypeForTarget, name, i + 1) : CheckedTarget.getInstance(argTypeForTarget, this.source, i), args[startArg + i]);
                if (createVarargsNow) {
                    Type eltype = ((ArrayType) arg_type).getComponentType();
                    code.emitDup();
                    code.emitInstanceof(arg_type);
                    code.emitIfIntNotZero();
                    code.emitCheckcast(arg_type);
                    code.emitElse();
                    code.emitPushInt(1);
                    code.emitNewArray(eltype);
                    code.emitDupX();
                    code.emitSwap();
                    code.emitPushInt(0);
                    code.emitSwap();
                    eltype.emitCoerceFromObject(code);
                    code.emitArrayStore(arg_type);
                    code.emitFi();
                }
                if (i >= fix_arg_count) {
                    code.emitArrayStore(arg_type);
                }
                if (argDecl != null && (is_static || i > 0)) {
                    argDecl = argDecl.nextDecl();
                }
                i++;
            } else {
                return;
            }
        }
        MakeList.compile(args, startArg + i, comp);
    }

    public void compile(ApplyExp exp, Compilation comp, Target target) {
        ClassType mclass;
        CodeAttr code = comp.getCode();
        if (this.method == null) {
            mclass = null;
        } else {
            mclass = this.method.getDeclaringClass();
        }
        Expression[] args = exp.getArgs();
        if (isConstructor()) {
            if (exp.getFlag(8)) {
                int nargs = args.length;
                comp.letStart();
                Expression[] xargs = new Expression[nargs];
                xargs[0] = args[0];
                for (int i = 1; i < nargs; i++) {
                    Expression argi = args[i];
                    Declaration d = comp.letVariable((Object) null, argi.getType(), argi);
                    d.setCanRead(true);
                    xargs[i] = new ReferenceExp(d);
                }
                comp.letEnter();
                comp.letDone(new ApplyExp(exp.func, xargs)).compile(comp, target);
                return;
            }
            code.emitNew(mclass);
            code.emitDup((Type) mclass);
        }
        String arg_error = WrongArguments.checkArgCount(this, args.length);
        if (arg_error != null) {
            comp.error('e', arg_error);
        }
        if (getStaticFlag()) {
            mclass = null;
        }
        compile(mclass, exp, comp, target);
    }

    /* access modifiers changed from: package-private */
    public void compile(Type thisType, ApplyExp exp, Compilation comp, Target target) {
        ClassType mclass = null;
        Expression[] args = exp.getArgs();
        CodeAttr code = comp.getCode();
        Type stackType = this.retType;
        int startArg = 0;
        if (isConstructor()) {
            if (this.method != null) {
                mclass = this.method.getDeclaringClass();
            }
            if (mclass.hasOuterLink()) {
                ClassExp.loadSuperStaticLink(args[0], mclass, comp);
            }
            thisType = null;
            startArg = 1;
        } else if (opcode() == 183 && this.mode == 'P' && "<init>".equals(this.method.getName())) {
            if (this.method != null) {
                mclass = this.method.getDeclaringClass();
            }
            if (mclass.hasOuterLink()) {
                code.emitPushThis();
                code.emitLoad(code.getCurrentScope().getVariable(1));
                thisType = null;
                startArg = 1;
            }
        } else if (takesTarget() && this.method.getStaticFlag()) {
            startArg = 1;
        }
        compileArgs(args, startArg, thisType, comp);
        if (this.method == null) {
            code.emitPrimop(opcode(), args.length, this.retType);
            target.compileFromStack(comp, stackType);
            return;
        }
        compileInvoke(comp, this.method, target, exp.isTailCall(), this.op_code, stackType);
    }

    public static void compileInvoke(Compilation comp, Method method2, Target target, boolean isTailCall, int op_code2, Type stackType) {
        CodeAttr code = comp.getCode();
        comp.usedClass(method2.getDeclaringClass());
        comp.usedClass(method2.getReturnType());
        if (!takesContext(method2)) {
            code.emitInvokeMethod(method2, op_code2);
        } else if ((target instanceof IgnoreTarget) || ((target instanceof ConsumerTarget) && ((ConsumerTarget) target).isContextTarget())) {
            Field consumerFld = null;
            Variable saveCallContext = null;
            comp.loadCallContext();
            if (target instanceof IgnoreTarget) {
                ClassType typeCallContext = Compilation.typeCallContext;
                consumerFld = typeCallContext.getDeclaredField("consumer");
                code.pushScope();
                saveCallContext = code.addLocal(typeCallContext);
                code.emitDup();
                code.emitGetField(consumerFld);
                code.emitStore(saveCallContext);
                code.emitDup();
                code.emitGetStatic(ClassType.make("gnu.lists.VoidConsumer").getDeclaredField("instance"));
                code.emitPutField(consumerFld);
            }
            code.emitInvokeMethod(method2, op_code2);
            if (isTailCall) {
                comp.loadCallContext();
                code.emitInvoke(Compilation.typeCallContext.getDeclaredMethod("runUntilDone", 0));
            }
            if (target instanceof IgnoreTarget) {
                comp.loadCallContext();
                code.emitLoad(saveCallContext);
                code.emitPutField(consumerFld);
                code.popScope();
                return;
            }
            return;
        } else {
            comp.loadCallContext();
            stackType = Type.objectType;
            code.pushScope();
            Variable saveIndex = code.addLocal(Type.intType);
            comp.loadCallContext();
            code.emitInvokeVirtual(Compilation.typeCallContext.getDeclaredMethod("startFromContext", 0));
            code.emitStore(saveIndex);
            code.emitWithCleanupStart();
            code.emitInvokeMethod(method2, op_code2);
            code.emitWithCleanupCatch((Variable) null);
            comp.loadCallContext();
            code.emitLoad(saveIndex);
            code.emitInvokeVirtual(Compilation.typeCallContext.getDeclaredMethod("cleanupFromContext", 1));
            code.emitWithCleanupDone();
            comp.loadCallContext();
            code.emitLoad(saveIndex);
            code.emitInvokeVirtual(Compilation.typeCallContext.getDeclaredMethod("getFromContext", 1));
            code.popScope();
        }
        target.compileFromStack(comp, stackType);
    }

    public Type getParameterType(int index) {
        if (takesTarget()) {
            if (index != 0) {
                index--;
            } else if (isConstructor()) {
                return Type.objectType;
            } else {
                return this.method.getDeclaringClass();
            }
        }
        int lenTypes = this.argTypes.length;
        if (index < lenTypes - 1) {
            return this.argTypes[index];
        }
        boolean varArgs = takesVarArgs();
        if (index < lenTypes && !varArgs) {
            return this.argTypes[index];
        }
        Type restType = this.argTypes[lenTypes - 1];
        if (restType instanceof ArrayType) {
            return ((ArrayType) restType).getComponentType();
        }
        return Type.objectType;
    }

    public static PrimProcedure getMethodFor(Procedure pproc, Expression[] args) {
        return getMethodFor(pproc, (Declaration) null, args, Language.getDefaultLanguage());
    }

    public static PrimProcedure getMethodFor(Procedure pproc, Declaration decl, Expression[] args, Language language) {
        int nargs = args.length;
        Type[] atypes = new Type[nargs];
        int i = nargs;
        while (true) {
            i--;
            if (i < 0) {
                return getMethodFor(pproc, decl, atypes, language);
            }
            atypes[i] = args[i].getType();
        }
    }

    public static PrimProcedure getMethodFor(Procedure pproc, Declaration decl, Type[] atypes, Language language) {
        Procedure pproc2;
        boolean z = pproc instanceof GenericProc;
        Procedure pproc3 = pproc;
        if (z) {
            GenericProc gproc = (GenericProc) pproc;
            Procedure[] methods = gproc.methods;
            Procedure pproc4 = null;
            int i = gproc.count;
            while (true) {
                i--;
                if (i >= 0) {
                    if (methods[i].isApplicable(atypes) < 0) {
                        pproc2 = pproc4;
                    } else if (pproc4 != null) {
                        return null;
                    } else {
                        pproc2 = methods[i];
                    }
                    pproc4 = pproc2;
                } else {
                    pproc3 = pproc4;
                    if (pproc4 == null) {
                        return null;
                    }
                }
            }
        }
        if (pproc3 instanceof PrimProcedure) {
            PrimProcedure prproc = (PrimProcedure) pproc3;
            if (prproc.isApplicable(atypes) >= 0) {
                return prproc;
            }
        }
        Class pclass = getProcedureClass(pproc3);
        if (pclass == null) {
            return null;
        }
        return getMethodFor((ClassType) Type.make(pclass), pproc3.getName(), decl, atypes, language);
    }

    public static void disassemble$X(Procedure pproc, CallContext ctx) throws Exception {
        Consumer cons = ctx.consumer;
        disassemble(pproc, cons instanceof Writer ? (Writer) cons : new ConsumerWriter(cons));
    }

    public static void disassemble(Procedure proc, Writer out) throws Exception {
        disassemble(proc, new ClassTypeWriter((ClassType) null, out, 0));
    }

    public static void disassemble(Procedure proc, ClassTypeWriter cwriter) throws Exception {
        Method pmethod;
        if (proc instanceof GenericProc) {
            GenericProc gproc = (GenericProc) proc;
            int n = gproc.getMethodCount();
            cwriter.print("Generic procedure with ");
            cwriter.print(n);
            cwriter.println(n == 1 ? " method." : "methods.");
            for (int i = 0; i < n; i++) {
                Procedure mproc = gproc.getMethod(i);
                if (mproc != null) {
                    cwriter.println();
                    disassemble(mproc, cwriter);
                }
            }
            return;
        }
        String pname = null;
        Class cl = proc.getClass();
        if (proc instanceof ModuleMethod) {
            cl = ((ModuleMethod) proc).module.getClass();
        } else if ((proc instanceof PrimProcedure) && (pmethod = ((PrimProcedure) proc).method) != null) {
            cl = pmethod.getDeclaringClass().getReflectClass();
            pname = pmethod.getName();
        }
        ClassLoader loader = cl.getClassLoader();
        String cname = cl.getName();
        String rname = cname.replace('.', '/') + ".class";
        ClassType ctype = new ClassType();
        InputStream rin = loader.getResourceAsStream(rname);
        if (rin == null) {
            throw new RuntimeException("missing resource " + rname);
        }
        new ClassFileInput(ctype, rin);
        cwriter.setClass(ctype);
        URL resource = loader.getResource(rname);
        cwriter.print("In class ");
        cwriter.print(cname);
        if (resource != null) {
            cwriter.print(" at ");
            cwriter.print(resource);
        }
        cwriter.println();
        if (pname == null) {
            String pname2 = proc.getName();
            if (pname2 == null) {
                cwriter.println("Anonymous function - unknown method.");
                return;
            }
            pname = Compilation.mangleName(pname2);
        }
        for (Method method2 = ctype.getMethods(); method2 != null; method2 = method2.getNext()) {
            if (method2.getName().equals(pname)) {
                cwriter.printMethod(method2);
            }
        }
        cwriter.flush();
    }

    public static Class getProcedureClass(Object pproc) {
        Class procClass;
        if (pproc instanceof ModuleMethod) {
            procClass = ((ModuleMethod) pproc).module.getClass();
        } else {
            procClass = pproc.getClass();
        }
        try {
            if (procClass.getClassLoader() == systemClassLoader) {
                return procClass;
            }
            return null;
        } catch (SecurityException e) {
        }
    }

    public static PrimProcedure getMethodFor(Class procClass, String name, Declaration decl, Expression[] args, Language language) {
        return getMethodFor((ClassType) Type.make(procClass), name, decl, args, language);
    }

    public static PrimProcedure getMethodFor(ClassType procClass, String name, Declaration decl, Expression[] args, Language language) {
        int nargs = args.length;
        Type[] atypes = new Type[nargs];
        int i = nargs;
        while (true) {
            i--;
            if (i < 0) {
                return getMethodFor(procClass, name, decl, atypes, language);
            }
            atypes[i] = args[i].getType();
        }
    }

    /* JADX WARNING: type inference failed for: r15v13, types: [gnu.mapping.MethodProc] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static gnu.expr.PrimProcedure getMethodFor(gnu.bytecode.ClassType r17, java.lang.String r18, gnu.expr.Declaration r19, gnu.bytecode.Type[] r20, gnu.expr.Language r21) {
        /*
            r2 = 0
            r3 = -1
            r4 = 0
            if (r18 != 0) goto L_0x0007
            r15 = 0
        L_0x0006:
            return r15
        L_0x0007:
            java.lang.String r7 = gnu.expr.Compilation.mangleName(r18)     // Catch:{ SecurityException -> 0x00ca }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ SecurityException -> 0x00ca }
            r15.<init>()     // Catch:{ SecurityException -> 0x00ca }
            java.lang.StringBuilder r15 = r15.append(r7)     // Catch:{ SecurityException -> 0x00ca }
            java.lang.String r16 = "$V"
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ SecurityException -> 0x00ca }
            java.lang.String r8 = r15.toString()     // Catch:{ SecurityException -> 0x00ca }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ SecurityException -> 0x00ca }
            r15.<init>()     // Catch:{ SecurityException -> 0x00ca }
            java.lang.StringBuilder r15 = r15.append(r7)     // Catch:{ SecurityException -> 0x00ca }
            java.lang.String r16 = "$V$X"
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ SecurityException -> 0x00ca }
            java.lang.String r9 = r15.toString()     // Catch:{ SecurityException -> 0x00ca }
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ SecurityException -> 0x00ca }
            r15.<init>()     // Catch:{ SecurityException -> 0x00ca }
            java.lang.StringBuilder r15 = r15.append(r7)     // Catch:{ SecurityException -> 0x00ca }
            java.lang.String r16 = "$X"
            java.lang.StringBuilder r15 = r15.append(r16)     // Catch:{ SecurityException -> 0x00ca }
            java.lang.String r10 = r15.toString()     // Catch:{ SecurityException -> 0x00ca }
            r1 = 1
            gnu.bytecode.Method r11 = r17.getDeclaredMethods()     // Catch:{ SecurityException -> 0x00ca }
        L_0x0049:
            if (r11 == 0) goto L_0x00cb
            int r13 = r11.getModifiers()     // Catch:{ SecurityException -> 0x00ca }
            r15 = r13 & 9
            r16 = 9
            r0 = r16
            if (r15 == r0) goto L_0x0064
            if (r19 == 0) goto L_0x005f
            r0 = r19
            gnu.expr.Declaration r15 = r0.base     // Catch:{ SecurityException -> 0x00ca }
            if (r15 != 0) goto L_0x0064
        L_0x005f:
            gnu.bytecode.Method r11 = r11.getNext()     // Catch:{ SecurityException -> 0x00ca }
            goto L_0x0049
        L_0x0064:
            java.lang.String r12 = r11.getName()     // Catch:{ SecurityException -> 0x00ca }
            boolean r15 = r12.equals(r7)     // Catch:{ SecurityException -> 0x00ca }
            if (r15 != 0) goto L_0x0080
            boolean r15 = r12.equals(r8)     // Catch:{ SecurityException -> 0x00ca }
            if (r15 != 0) goto L_0x0080
            boolean r15 = r12.equals(r10)     // Catch:{ SecurityException -> 0x00ca }
            if (r15 != 0) goto L_0x0080
            boolean r15 = r12.equals(r9)     // Catch:{ SecurityException -> 0x00ca }
            if (r15 == 0) goto L_0x00a5
        L_0x0080:
            r6 = 0
        L_0x0081:
            if (r6 != 0) goto L_0x0089
            r1 = 0
            if (r4 == 0) goto L_0x0089
            r2 = 0
            r3 = -1
            r4 = 0
        L_0x0089:
            gnu.expr.PrimProcedure r14 = new gnu.expr.PrimProcedure     // Catch:{ SecurityException -> 0x00ca }
            r0 = r21
            r14.<init>((gnu.bytecode.Method) r11, (gnu.expr.Language) r0)     // Catch:{ SecurityException -> 0x00ca }
            r0 = r18
            r14.setName(r0)     // Catch:{ SecurityException -> 0x00ca }
            r0 = r20
            int r5 = r14.isApplicable(r0)     // Catch:{ SecurityException -> 0x00ca }
            if (r5 < 0) goto L_0x005f
            if (r5 < r3) goto L_0x005f
            if (r5 <= r3) goto L_0x00b9
            r2 = r14
        L_0x00a2:
            r3 = r5
            r4 = r6
            goto L_0x005f
        L_0x00a5:
            if (r1 == 0) goto L_0x005f
            java.lang.String r15 = "apply"
            boolean r15 = r12.equals(r15)     // Catch:{ SecurityException -> 0x00ca }
            if (r15 != 0) goto L_0x00b7
            java.lang.String r15 = "apply$V"
            boolean r15 = r12.equals(r15)     // Catch:{ SecurityException -> 0x00ca }
            if (r15 == 0) goto L_0x005f
        L_0x00b7:
            r6 = 1
            goto L_0x0081
        L_0x00b9:
            if (r2 == 0) goto L_0x00a2
            gnu.mapping.MethodProc r15 = gnu.mapping.MethodProc.mostSpecific((gnu.mapping.MethodProc) r2, (gnu.mapping.MethodProc) r14)     // Catch:{ SecurityException -> 0x00ca }
            r0 = r15
            gnu.expr.PrimProcedure r0 = (gnu.expr.PrimProcedure) r0     // Catch:{ SecurityException -> 0x00ca }
            r2 = r0
            if (r2 != 0) goto L_0x00a2
            if (r3 <= 0) goto L_0x00a2
            r15 = 0
            goto L_0x0006
        L_0x00ca:
            r15 = move-exception
        L_0x00cb:
            r15 = r2
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.PrimProcedure.getMethodFor(gnu.bytecode.ClassType, java.lang.String, gnu.expr.Declaration, gnu.bytecode.Type[], gnu.expr.Language):gnu.expr.PrimProcedure");
    }

    public String getName() {
        String name = super.getName();
        if (name != null) {
            return name;
        }
        String name2 = getVerboseName();
        setName(name2);
        return name2;
    }

    public String getVerboseName() {
        StringBuffer buf = new StringBuffer(100);
        if (this.method == null) {
            buf.append("<op ");
            buf.append(this.op_code);
            buf.append('>');
        } else {
            buf.append(this.method.getDeclaringClass().getName());
            buf.append('.');
            buf.append(this.method.getName());
        }
        buf.append('(');
        for (int i = 0; i < this.argTypes.length; i++) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append(this.argTypes[i].getName());
        }
        buf.append(')');
        return buf.toString();
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(100);
        buf.append(this.retType == null ? "<unknown>" : this.retType.getName());
        buf.append(' ');
        buf.append(getVerboseName());
        return buf.toString();
    }

    public void print(PrintWriter ps) {
        ps.print("#<primitive procedure ");
        ps.print(toString());
        ps.print('>');
    }
}
