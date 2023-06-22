package gnu.expr;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Field;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.lists.LList;
import gnu.mapping.CallContext;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure;
import gnu.mapping.PropertySet;
import gnu.mapping.Values;
import gnu.mapping.WrappedException;
import gnu.mapping.WrongArguments;
import java.util.Set;
import java.util.Vector;

public class LambdaExp extends ScopeExp {
    public static final int ATTEMPT_INLINE = 4096;
    static final int CANNOT_INLINE = 32;
    static final int CAN_CALL = 4;
    static final int CAN_READ = 2;
    static final int CLASS_METHOD = 64;
    static final int DEFAULT_CAPTURES_ARG = 512;
    static final int IMPORTS_LEX_VARS = 8;
    static final int INLINE_ONLY = 8192;
    static final int METHODS_COMPILED = 128;
    static final int NEEDS_STATIC_LINK = 16;
    protected static final int NEXT_AVAIL_FLAG = 16384;
    public static final int NO_FIELD = 256;
    public static final int OVERLOADABLE_FIELD = 2048;
    public static final int SEQUENCE_RESULT = 1024;
    static Method searchForKeywordMethod3;
    static Method searchForKeywordMethod4;
    static final ApplyExp unknownContinuation = new ApplyExp((Expression) null, (Expression[]) null);
    Vector applyMethods;
    Variable argsArray;
    public Expression body;
    Declaration capturedVars;
    Variable closureEnv;
    public Field closureEnvField;
    public Expression[] defaultArgs;
    private Declaration firstArgsArrayArg;
    public LambdaExp firstChild;
    Variable heapFrame;
    Initializer initChain;
    public LambdaExp inlineHome;
    public Keyword[] keywords;
    public int max_args;
    public int min_args;
    public Declaration nameDecl;
    public LambdaExp nextSibling;
    Method[] primBodyMethods;
    Method[] primMethods;
    Object[] properties;
    public Expression returnContinuation;
    public Type returnType;
    int selectorValue;
    public Field staticLinkField;
    Set<LambdaExp> tailCallers;
    Procedure thisValue;
    Variable thisVariable;
    Expression[] throwsSpecification;
    ClassType type = Compilation.typeProcedure;

    public void capture(Declaration decl) {
        if (decl.isSimple()) {
            if (this.capturedVars == null && !decl.isStatic() && !(this instanceof ModuleExp) && !(this instanceof ClassExp)) {
                this.heapFrame = new Variable("heapFrame");
            }
            decl.setSimple(false);
            if (!decl.isPublic()) {
                decl.nextCapturedVar = this.capturedVars;
                this.capturedVars = decl;
            }
        }
    }

    public void setExceptions(Expression[] exceptions) {
        this.throwsSpecification = exceptions;
    }

    public final boolean getInlineOnly() {
        return (this.flags & 8192) != 0;
    }

    public final void setInlineOnly(boolean inlineOnly) {
        setFlag(inlineOnly, 8192);
    }

    public final boolean getNeedsClosureEnv() {
        return (this.flags & 24) != 0;
    }

    public final boolean getNeedsStaticLink() {
        return (this.flags & 16) != 0;
    }

    public final void setNeedsStaticLink(boolean needsStaticLink) {
        if (needsStaticLink) {
            this.flags |= 16;
        } else {
            this.flags &= -17;
        }
    }

    public final boolean getImportsLexVars() {
        return (this.flags & 8) != 0;
    }

    public final void setImportsLexVars(boolean importsLexVars) {
        if (importsLexVars) {
            this.flags |= 8;
        } else {
            this.flags &= -9;
        }
    }

    public final void setImportsLexVars() {
        int old = this.flags;
        this.flags |= 8;
        if ((old & 8) == 0 && this.nameDecl != null) {
            setCallersNeedStaticLink();
        }
    }

    public final void setNeedsStaticLink() {
        int old = this.flags;
        this.flags |= 16;
        if ((old & 16) == 0 && this.nameDecl != null) {
            setCallersNeedStaticLink();
        }
    }

    /* access modifiers changed from: package-private */
    public void setCallersNeedStaticLink() {
        LambdaExp outer = outerLambda();
        for (ApplyExp app = this.nameDecl.firstCall; app != null; app = app.nextCall) {
            LambdaExp caller = app.context;
            while (caller != outer && !(caller instanceof ModuleExp)) {
                caller.setNeedsStaticLink();
                caller = caller.outerLambda();
            }
        }
    }

    public final boolean getCanRead() {
        return (this.flags & 2) != 0;
    }

    public final void setCanRead(boolean read) {
        if (read) {
            this.flags |= 2;
        } else {
            this.flags &= -3;
        }
    }

    public final boolean getCanCall() {
        return (this.flags & 4) != 0;
    }

    public final void setCanCall(boolean called) {
        if (called) {
            this.flags |= 4;
        } else {
            this.flags &= -5;
        }
    }

    public final boolean isClassMethod() {
        return (this.flags & 64) != 0;
    }

    public final void setClassMethod(boolean isMethod) {
        if (isMethod) {
            this.flags |= 64;
        } else {
            this.flags &= -65;
        }
    }

    public final boolean isModuleBody() {
        return this instanceof ModuleExp;
    }

    public final boolean isClassGenerated() {
        return isModuleBody() || (this instanceof ClassExp);
    }

    public boolean isAbstract() {
        return this.body == QuoteExp.abstractExp;
    }

    public int getCallConvention() {
        if (isModuleBody()) {
            if (Compilation.defaultCallConvention >= 2) {
                return Compilation.defaultCallConvention;
            }
            return 2;
        } else if (isClassMethod()) {
            return 1;
        } else {
            if (Compilation.defaultCallConvention != 0) {
                return Compilation.defaultCallConvention;
            }
            return 1;
        }
    }

    public final boolean isHandlingTailCalls() {
        return isModuleBody() || (Compilation.defaultCallConvention >= 3 && !isClassMethod());
    }

    public final boolean variable_args() {
        return this.max_args < 0;
    }

    /* access modifiers changed from: protected */
    public ClassType getCompiledClassType(Compilation comp) {
        if (this.type != Compilation.typeProcedure) {
            return this.type;
        }
        throw new Error("internal error: getCompiledClassType");
    }

    public Type getType() {
        return this.type;
    }

    public ClassType getClassType() {
        return this.type;
    }

    public void setType(ClassType type2) {
        this.type = type2;
    }

    public int incomingArgs() {
        if (this.min_args != this.max_args || this.max_args > 4 || this.max_args <= 0) {
            return 1;
        }
        return this.max_args;
    }

    /* access modifiers changed from: package-private */
    public int getSelectorValue(Compilation comp) {
        int s = this.selectorValue;
        if (s != 0) {
            return s;
        }
        int s2 = comp.maxSelectorValue;
        comp.maxSelectorValue = this.primMethods.length + s2;
        int s3 = s2 + 1;
        this.selectorValue = s3;
        return s3;
    }

    public final Method getMethod(int argCount) {
        int index;
        if (this.primMethods == null) {
            return null;
        }
        if ((this.max_args >= 0 && argCount > this.max_args) || (index = argCount - this.min_args) < 0) {
            return null;
        }
        int length = this.primMethods.length;
        Method[] methodArr = this.primMethods;
        if (index >= length) {
            index = length - 1;
        }
        return methodArr[index];
    }

    public final Method getMainMethod() {
        Method[] methods = this.primBodyMethods;
        if (methods == null) {
            return null;
        }
        return methods[methods.length - 1];
    }

    public final Type restArgType() {
        if (this.min_args == this.max_args) {
            return null;
        }
        if (this.primMethods == null) {
            throw new Error("internal error - restArgType");
        }
        Method[] methods = this.primMethods;
        if (this.max_args >= 0 && methods.length > this.max_args - this.min_args) {
            return null;
        }
        Method method = methods[methods.length - 1];
        Type[] types = method.getParameterTypes();
        int ilast = types.length - 1;
        if (method.getName().endsWith("$X")) {
            ilast--;
        }
        return types[ilast];
    }

    public LambdaExp outerLambda() {
        if (this.outer == null) {
            return null;
        }
        return this.outer.currentLambda();
    }

    public LambdaExp outerLambdaNotInline() {
        ScopeExp exp = this;
        while (true) {
            exp = exp.outer;
            if (exp == null) {
                return null;
            }
            if (exp instanceof LambdaExp) {
                LambdaExp result = (LambdaExp) exp;
                if (!result.getInlineOnly()) {
                    return result;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean inlinedIn(LambdaExp outer) {
        for (LambdaExp exp = this; exp.getInlineOnly(); exp = exp.getCaller()) {
            if (exp == outer) {
                return true;
            }
        }
        return false;
    }

    public LambdaExp getCaller() {
        return this.inlineHome;
    }

    public Variable declareThis(ClassType clas) {
        if (this.thisVariable == null) {
            this.thisVariable = new Variable("this");
            getVarScope().addVariableAfter((Variable) null, this.thisVariable);
            this.thisVariable.setParameter(true);
        }
        if (this.thisVariable.getType() == null) {
            this.thisVariable.setType(clas);
        }
        if (this.decls != null && this.decls.isThisParameter()) {
            this.decls.var = this.thisVariable;
        }
        return this.thisVariable;
    }

    public Variable declareClosureEnv() {
        Variable prev;
        if (this.closureEnv == null && getNeedsClosureEnv()) {
            LambdaExp parent = outerLambda();
            if (parent instanceof ClassExp) {
                parent = parent.outerLambda();
            }
            Variable parentFrame = parent.heapFrame != null ? parent.heapFrame : parent.closureEnv;
            if (isClassMethod() && !"*init*".equals(getName())) {
                this.closureEnv = declareThis(this.type);
            } else if (parent.heapFrame == null && !parent.getNeedsStaticLink() && !(parent instanceof ModuleExp)) {
                this.closureEnv = null;
            } else if (!isClassGenerated() && !getInlineOnly()) {
                Method primMethod = getMainMethod();
                boolean isInit = "*init*".equals(getName());
                if (primMethod.getStaticFlag() || isInit) {
                    this.closureEnv = new Variable("closureEnv", primMethod.getParameterTypes()[0]);
                    if (isInit) {
                        prev = declareThis(primMethod.getDeclaringClass());
                    } else {
                        prev = null;
                    }
                    getVarScope().addVariableAfter(prev, this.closureEnv);
                    this.closureEnv.setParameter(true);
                } else {
                    this.closureEnv = declareThis(primMethod.getDeclaringClass());
                }
            } else if (inlinedIn(parent)) {
                this.closureEnv = parentFrame;
            } else {
                this.closureEnv = new Variable("closureEnv", parentFrame.getType());
                getVarScope().addVariable(this.closureEnv);
            }
        }
        return this.closureEnv;
    }

    public LambdaExp() {
    }

    public LambdaExp(int args) {
        this.min_args = args;
        this.max_args = args;
    }

    public LambdaExp(Expression body2) {
        this.body = body2;
    }

    public void loadHeapFrame(Compilation comp) {
        ClassType curType;
        LambdaExp curLambda = comp.curLambda;
        while (curLambda != this && curLambda.getInlineOnly()) {
            curLambda = curLambda.getCaller();
        }
        CodeAttr code = comp.getCode();
        if (curLambda.heapFrame == null || this != curLambda) {
            if (curLambda.closureEnv != null) {
                code.emitLoad(curLambda.closureEnv);
                curType = (ClassType) curLambda.closureEnv.getType();
            } else {
                code.emitPushThis();
                curType = comp.curClass;
            }
            while (curLambda != this) {
                Field link = curLambda.staticLinkField;
                if (link != null && link.getDeclaringClass() == curType) {
                    code.emitGetField(link);
                    curType = (ClassType) link.getType();
                }
                curLambda = curLambda.outerLambda();
            }
            return;
        }
        code.emitLoad(curLambda.heapFrame);
    }

    /* access modifiers changed from: package-private */
    public Declaration getArg(int i) {
        for (Declaration var = firstDecl(); var != null; var = var.nextDecl()) {
            if (i == 0) {
                return var;
            }
            i--;
        }
        throw new Error("internal error - getArg");
    }

    public void compileEnd(Compilation comp) {
        CodeAttr code = comp.getCode();
        if (!getInlineOnly()) {
            if (comp.method.reachableHere() && (Compilation.defaultCallConvention < 3 || isModuleBody() || isClassMethod() || isHandlingTailCalls())) {
                code.emitReturn();
            }
            popScope(code);
            code.popScope();
        }
        for (LambdaExp child = this.firstChild; child != null; child = child.nextSibling) {
            if (!child.getCanRead() && !child.getInlineOnly()) {
                child.compileAsMethod(comp);
            }
        }
        if (this.heapFrame != null) {
            comp.generateConstructor(this);
        }
    }

    public void generateApplyMethods(Compilation comp) {
        comp.generateMatchMethods(this);
        if (Compilation.defaultCallConvention >= 2) {
            comp.generateApplyMethodsWithContext(this);
        } else {
            comp.generateApplyMethodsWithoutContext(this);
        }
    }

    /* access modifiers changed from: package-private */
    public Field allocFieldFor(Compilation comp) {
        if (this.nameDecl != null && this.nameDecl.field != null) {
            return this.nameDecl.field;
        }
        boolean needsClosure = getNeedsClosureEnv();
        ClassType frameType = needsClosure ? getOwningLambda().getHeapFrameType() : comp.mainClass;
        String name = getName();
        String fname = name == null ? "lambda" : Compilation.mangleNameIfNeeded(name);
        int fflags = 16;
        if (this.nameDecl == null || !(this.nameDecl.context instanceof ModuleExp)) {
            StringBuilder append = new StringBuilder().append(fname).append("$Fn");
            int i = comp.localFieldIndex + 1;
            comp.localFieldIndex = i;
            fname = append.append(i).toString();
            if (!needsClosure) {
                fflags = 16 | 8;
            }
        } else {
            boolean external_access = this.nameDecl.needsExternalAccess();
            if (external_access) {
                fname = Declaration.PRIVATE_PREFIX + fname;
            }
            if (this.nameDecl.getFlag(2048)) {
                fflags = 16 | 8;
                if (!((ModuleExp) this.nameDecl.context).isStatic()) {
                    fflags &= -17;
                }
            }
            if (!this.nameDecl.isPrivate() || external_access || comp.immediate) {
                fflags |= 1;
            }
            if ((this.flags & 2048) != 0) {
                String fname0 = fname;
                int suffix = this.min_args == this.max_args ? this.min_args : 1;
                while (true) {
                    int suffix2 = suffix + 1;
                    fname = fname0 + '$' + suffix;
                    if (frameType.getDeclaredField(fname) == null) {
                        break;
                    }
                    suffix = suffix2;
                }
            }
        }
        Field field = frameType.addField(fname, Compilation.typeModuleMethod, fflags);
        if (this.nameDecl == null) {
            return field;
        }
        this.nameDecl.field = field;
        return field;
    }

    /* access modifiers changed from: package-private */
    public final void addApplyMethod(Compilation comp, Field field) {
        LambdaExp owner = this;
        if (field == null || !field.getStaticFlag()) {
            do {
                owner = owner.outerLambda();
                if ((owner instanceof ModuleExp) || owner.heapFrame != null) {
                }
                owner = owner.outerLambda();
                break;
            } while (owner.heapFrame != null);
            if (!owner.getHeapFrameType().getSuperclass().isSubtype(Compilation.typeModuleBody)) {
                owner = comp.getModule();
            }
        } else {
            owner = comp.getModule();
        }
        if (owner.applyMethods == null) {
            owner.applyMethods = new Vector();
        }
        owner.applyMethods.addElement(this);
    }

    public Field compileSetField(Compilation comp) {
        if (this.primMethods == null) {
            allocMethod(outerLambda(), comp);
        }
        Field field = allocFieldFor(comp);
        if (comp.usingCPStyle()) {
            compile(comp, (Type) Type.objectType);
        } else {
            compileAsMethod(comp);
            addApplyMethod(comp, field);
        }
        return new ProcInitializer(this, comp, field).field;
    }

    public void compile(Compilation comp, Target target) {
        if (!(target instanceof IgnoreTarget)) {
            CodeAttr code = comp.getCode();
            LambdaExp outer = outerLambda();
            Type rtype = Compilation.typeModuleMethod;
            if ((this.flags & 256) != 0 || (comp.immediate && (outer instanceof ModuleExp))) {
                if (this.primMethods == null) {
                    allocMethod(outerLambda(), comp);
                }
                compileAsMethod(comp);
                addApplyMethod(comp, (Field) null);
                ProcInitializer.emitLoadModuleMethod(this, comp);
            } else {
                Field field = compileSetField(comp);
                if (field.getStaticFlag()) {
                    code.emitGetStatic(field);
                } else {
                    LambdaExp parent = comp.curLambda;
                    code.emitLoad(parent.heapFrame != null ? parent.heapFrame : parent.closureEnv);
                    code.emitGetField(field);
                }
            }
            target.compileFromStack(comp, rtype);
        }
    }

    public ClassType getHeapFrameType() {
        if ((this instanceof ModuleExp) || (this instanceof ClassExp)) {
            return (ClassType) getType();
        }
        return (ClassType) this.heapFrame.getType();
    }

    public LambdaExp getOwningLambda() {
        for (ScopeExp exp = this.outer; exp != null; exp = exp.outer) {
            if ((exp instanceof ModuleExp) || (((exp instanceof ClassExp) && getNeedsClosureEnv()) || ((exp instanceof LambdaExp) && ((LambdaExp) exp).heapFrame != null))) {
                return (LambdaExp) exp;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void addMethodFor(Compilation comp, ObjectType closureEnvType) {
        ClassType ctype;
        ScopeExp sc = this;
        while (sc != null && !(sc instanceof ClassExp)) {
            sc = sc.outer;
        }
        if (sc != null) {
            ctype = ((ClassExp) sc).instanceType;
        } else {
            ctype = getOwningLambda().getHeapFrameType();
        }
        addMethodFor(ctype, comp, closureEnvType);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v29, resolved type: gnu.bytecode.ClassType} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v120, resolved type: gnu.bytecode.ClassType} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:158:0x0322  */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x0344  */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x0362 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addMethodFor(gnu.bytecode.ClassType r59, gnu.expr.Compilation r60, gnu.bytecode.ObjectType r61) {
        /*
            r58 = this;
            java.lang.String r35 = r58.getName()
            gnu.expr.LambdaExp r41 = r58.outerLambda()
            r0 = r58
            gnu.expr.Keyword[] r0 = r0.keywords
            r55 = r0
            if (r55 != 0) goto L_0x01ec
            r24 = 0
        L_0x0012:
            r0 = r58
            gnu.expr.Expression[] r0 = r0.defaultArgs
            r55 = r0
            if (r55 != 0) goto L_0x01f9
            r40 = 0
        L_0x001c:
            r0 = r58
            int r0 = r0.flags
            r55 = r0
            r0 = r55
            r0 = r0 & 512(0x200, float:7.175E-43)
            r55 = r0
            if (r55 == 0) goto L_0x0208
            r39 = 0
        L_0x002c:
            r0 = r58
            int r0 = r0.max_args
            r55 = r0
            if (r55 < 0) goto L_0x0048
            r0 = r58
            int r0 = r0.min_args
            r55 = r0
            int r55 = r55 + r39
            r0 = r58
            int r0 = r0.max_args
            r56 = r0
            r0 = r55
            r1 = r56
            if (r0 >= r1) goto L_0x020c
        L_0x0048:
            r53 = 1
        L_0x004a:
            int r55 = r39 + 1
            r0 = r55
            gnu.bytecode.Method[] r0 = new gnu.bytecode.Method[r0]
            r30 = r0
            r0 = r30
            r1 = r58
            r1.primBodyMethods = r0
            r0 = r58
            gnu.bytecode.Method[] r0 = r0.primMethods
            r55 = r0
            if (r55 != 0) goto L_0x0066
            r0 = r30
            r1 = r58
            r1.primMethods = r0
        L_0x0066:
            r19 = 0
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            if (r55 == 0) goto L_0x0210
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            r56 = 4096(0x1000, double:2.0237E-320)
            boolean r55 = r55.getFlag(r56)
            if (r55 == 0) goto L_0x0210
            r20 = 0
        L_0x0080:
            java.lang.StringBuffer r37 = new java.lang.StringBuffer
            r55 = 60
            r0 = r37
            r1 = r55
            r0.<init>(r1)
            if (r20 == 0) goto L_0x02be
            r32 = 8
        L_0x008f:
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            if (r55 == 0) goto L_0x00a5
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            boolean r55 = r55.needsExternalAccess()
            if (r55 == 0) goto L_0x02c2
            r32 = r32 | 1
        L_0x00a5:
            boolean r55 = r41.isModuleBody()
            if (r55 != 0) goto L_0x00b3
            r0 = r41
            boolean r0 = r0 instanceof gnu.expr.ClassExp
            r55 = r0
            if (r55 == 0) goto L_0x00b5
        L_0x00b3:
            if (r35 != 0) goto L_0x00d3
        L_0x00b5:
            java.lang.String r55 = "lambda"
            r0 = r37
            r1 = r55
            r0.append(r1)
            r0 = r60
            int r0 = r0.method_counter
            r55 = r0
            int r55 = r55 + 1
            r0 = r55
            r1 = r60
            r1.method_counter = r0
            r0 = r37
            r1 = r55
            r0.append(r1)
        L_0x00d3:
            r55 = 67
            r0 = r19
            r1 = r55
            if (r0 != r1) goto L_0x02e7
            java.lang.String r55 = "<clinit>"
            r0 = r37
            r1 = r55
            r0.append(r1)
        L_0x00e4:
            r55 = 1024(0x400, float:1.435E-42)
            r0 = r58
            r1 = r55
            boolean r55 = r0.getFlag(r1)
            if (r55 == 0) goto L_0x00f9
            java.lang.String r55 = "$C"
            r0 = r37
            r1 = r55
            r0.append(r1)
        L_0x00f9:
            int r55 = r58.getCallConvention()
            r56 = 2
            r0 = r55
            r1 = r56
            if (r0 < r1) goto L_0x02fa
            if (r19 != 0) goto L_0x02fa
            r54 = 1
        L_0x0109:
            if (r19 == 0) goto L_0x0111
            if (r20 == 0) goto L_0x02fe
            r55 = r32 & -3
            int r32 = r55 + 1
        L_0x0111:
            boolean r55 = r59.isInterface()
            if (r55 != 0) goto L_0x011d
            boolean r55 = r58.isAbstract()
            if (r55 == 0) goto L_0x0123
        L_0x011d:
            r0 = r32
            r0 = r0 | 1024(0x400, float:1.435E-42)
            r32 = r0
        L_0x0123:
            boolean r55 = r58.isClassMethod()
            if (r55 == 0) goto L_0x0155
            r0 = r41
            boolean r0 = r0 instanceof gnu.expr.ClassExp
            r55 = r0
            if (r55 == 0) goto L_0x0155
            r0 = r58
            int r0 = r0.min_args
            r55 = r0
            r0 = r58
            int r0 = r0.max_args
            r56 = r0
            r0 = r55
            r1 = r56
            if (r0 != r1) goto L_0x0155
            r18 = 0
            r17 = 0
            gnu.expr.Declaration r42 = r58.firstDecl()
        L_0x014b:
            if (r42 != 0) goto L_0x0304
            r0 = r58
            gnu.bytecode.Type r0 = r0.returnType
            r55 = r0
            if (r55 == 0) goto L_0x0320
        L_0x0155:
            r55 = 1024(0x400, float:1.435E-42)
            r0 = r58
            r1 = r55
            boolean r55 = r0.getFlag(r1)
            if (r55 != 0) goto L_0x016d
            int r55 = r58.getCallConvention()
            r56 = 2
            r0 = r55
            r1 = r56
            if (r0 < r1) goto L_0x0379
        L_0x016d:
            gnu.bytecode.PrimType r46 = gnu.bytecode.Type.voidType
        L_0x016f:
            if (r61 == 0) goto L_0x0383
            r0 = r61
            r1 = r59
            if (r0 == r1) goto L_0x0383
            r14 = 1
        L_0x0178:
            r8 = 0
            int r55 = r58.getCallConvention()
            r56 = 2
            r0 = r55
            r1 = r56
            if (r0 < r1) goto L_0x0188
            if (r19 != 0) goto L_0x0188
            r8 = 1
        L_0x0188:
            int r36 = r37.length()
            r16 = 0
        L_0x018e:
            r0 = r16
            r1 = r39
            if (r0 > r1) goto L_0x0590
            r0 = r37
            r1 = r36
            r0.setLength(r1)
            r0 = r58
            int r0 = r0.min_args
            r55 = r0
            int r43 = r55 + r16
            r38 = r43
            r0 = r16
            r1 = r39
            if (r0 != r1) goto L_0x01af
            if (r53 == 0) goto L_0x01af
            int r38 = r38 + 1
        L_0x01af:
            int r55 = r14 + r38
            int r55 = r55 + r8
            r0 = r55
            gnu.bytecode.Type[] r5 = new gnu.bytecode.Type[r0]
            if (r14 <= 0) goto L_0x01bd
            r55 = 0
            r5[r55] = r61
        L_0x01bd:
            gnu.expr.Declaration r52 = r58.firstDecl()
            if (r52 == 0) goto L_0x01cd
            boolean r55 = r52.isThisParameter()
            if (r55 == 0) goto L_0x01cd
            gnu.expr.Declaration r52 = r52.nextDecl()
        L_0x01cd:
            r21 = 0
            r22 = r21
        L_0x01d1:
            r0 = r22
            r1 = r43
            if (r0 >= r1) goto L_0x0386
            int r21 = r22 + 1
            int r55 = r14 + r22
            gnu.bytecode.Type r56 = r52.getType()
            gnu.bytecode.Type r56 = r56.getImplementationType()
            r5[r55] = r56
            gnu.expr.Declaration r52 = r52.nextDecl()
            r22 = r21
            goto L_0x01d1
        L_0x01ec:
            r0 = r58
            gnu.expr.Keyword[] r0 = r0.keywords
            r55 = r0
            r0 = r55
            int r0 = r0.length
            r24 = r0
            goto L_0x0012
        L_0x01f9:
            r0 = r58
            gnu.expr.Expression[] r0 = r0.defaultArgs
            r55 = r0
            r0 = r55
            int r0 = r0.length
            r55 = r0
            int r40 = r55 - r24
            goto L_0x001c
        L_0x0208:
            r39 = r40
            goto L_0x002c
        L_0x020c:
            r53 = 0
            goto L_0x004a
        L_0x0210:
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            if (r55 == 0) goto L_0x022a
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            r56 = 2048(0x800, double:1.0118E-320)
            boolean r55 = r55.getFlag(r56)
            if (r55 == 0) goto L_0x022a
            r20 = 1
            goto L_0x0080
        L_0x022a:
            boolean r55 = r58.isClassMethod()
            if (r55 == 0) goto L_0x026b
            r0 = r41
            boolean r0 = r0 instanceof gnu.expr.ClassExp
            r55 = r0
            if (r55 == 0) goto L_0x0267
            r6 = r41
            gnu.expr.ClassExp r6 = (gnu.expr.ClassExp) r6
            boolean r55 = r6.isMakingClassPair()
            if (r55 == 0) goto L_0x0254
            if (r61 == 0) goto L_0x0254
            r20 = 1
        L_0x0246:
            gnu.expr.LambdaExp r0 = r6.initMethod
            r55 = r0
            r0 = r58
            r1 = r55
            if (r0 != r1) goto L_0x0257
            r19 = 73
            goto L_0x0080
        L_0x0254:
            r20 = 0
            goto L_0x0246
        L_0x0257:
            gnu.expr.LambdaExp r0 = r6.clinitMethod
            r55 = r0
            r0 = r58
            r1 = r55
            if (r0 != r1) goto L_0x0080
            r19 = 67
            r20 = 1
            goto L_0x0080
        L_0x0267:
            r20 = 0
            goto L_0x0080
        L_0x026b:
            r0 = r58
            gnu.bytecode.Variable r0 = r0.thisVariable
            r55 = r0
            if (r55 != 0) goto L_0x0279
            r0 = r61
            r1 = r59
            if (r0 != r1) goto L_0x027d
        L_0x0279:
            r20 = 0
            goto L_0x0080
        L_0x027d:
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            if (r55 == 0) goto L_0x02ba
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            r0 = r55
            gnu.expr.ScopeExp r0 = r0.context
            r55 = r0
            r0 = r55
            boolean r0 = r0 instanceof gnu.expr.ModuleExp
            r55 = r0
            if (r55 == 0) goto L_0x02ba
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            r0 = r55
            gnu.expr.ScopeExp r0 = r0.context
            r31 = r0
            gnu.expr.ModuleExp r31 = (gnu.expr.ModuleExp) r31
            gnu.bytecode.ClassType r55 = r31.getSuperType()
            if (r55 != 0) goto L_0x02b7
            gnu.bytecode.ClassType[] r55 = r31.getInterfaces()
            if (r55 != 0) goto L_0x02b7
            r20 = 1
        L_0x02b5:
            goto L_0x0080
        L_0x02b7:
            r20 = 0
            goto L_0x02b5
        L_0x02ba:
            r20 = 1
            goto L_0x0080
        L_0x02be:
            r32 = 0
            goto L_0x008f
        L_0x02c2:
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            boolean r55 = r55.isPrivate()
            if (r55 == 0) goto L_0x02e5
            r11 = 0
        L_0x02cf:
            boolean r55 = r58.isClassMethod()
            if (r55 == 0) goto L_0x02e1
            r0 = r58
            gnu.expr.Declaration r0 = r0.nameDecl
            r55 = r0
            r0 = r55
            short r11 = r0.getAccessFlags(r11)
        L_0x02e1:
            r32 = r32 | r11
            goto L_0x00a5
        L_0x02e5:
            r11 = 1
            goto L_0x02cf
        L_0x02e7:
            java.lang.Object r55 = r58.getSymbol()
            if (r55 == 0) goto L_0x00e4
            java.lang.String r55 = gnu.expr.Compilation.mangleName(r35)
            r0 = r37
            r1 = r55
            r0.append(r1)
            goto L_0x00e4
        L_0x02fa:
            r54 = 0
            goto L_0x0109
        L_0x02fe:
            r55 = r32 & 2
            int r32 = r55 + 2
            goto L_0x0111
        L_0x0304:
            boolean r55 = r42.isThisParameter()
            if (r55 == 0) goto L_0x0314
            int r17 = r17 + -1
        L_0x030c:
            gnu.expr.Declaration r42 = r42.nextDecl()
            int r17 = r17 + 1
            goto L_0x014b
        L_0x0314:
            r56 = 8192(0x2000, double:4.0474E-320)
            r0 = r42
            r1 = r56
            boolean r55 = r0.getFlag(r1)
            if (r55 != 0) goto L_0x030c
        L_0x0320:
            if (r18 != 0) goto L_0x0339
            java.lang.String r28 = r37.toString()
            gnu.expr.LambdaExp$1 r15 = new gnu.expr.LambdaExp$1
            r0 = r58
            r1 = r28
            r15.<init>(r1)
            r55 = 2
            r0 = r59
            r1 = r55
            gnu.bytecode.Method[] r18 = r0.getMethods((gnu.bytecode.Filter) r15, (int) r1)
        L_0x0339:
            r50 = 0
            r0 = r18
            int r0 = r0.length
            r16 = r0
        L_0x0340:
            int r16 = r16 + -1
            if (r16 < 0) goto L_0x0362
            r29 = r18[r16]
            if (r42 != 0) goto L_0x0351
            gnu.bytecode.Type r44 = r29.getReturnType()
        L_0x034c:
            if (r50 != 0) goto L_0x0358
            r50 = r44
            goto L_0x0340
        L_0x0351:
            gnu.bytecode.Type[] r55 = r29.getParameterTypes()
            r44 = r55[r17]
            goto L_0x034c
        L_0x0358:
            r0 = r44
            r1 = r50
            if (r0 == r1) goto L_0x0340
            if (r42 != 0) goto L_0x030c
            goto L_0x0155
        L_0x0362:
            if (r50 == 0) goto L_0x036d
            if (r42 == 0) goto L_0x0371
            r0 = r42
            r1 = r50
            r0.setType(r1)
        L_0x036d:
            if (r42 != 0) goto L_0x030c
            goto L_0x0155
        L_0x0371:
            r0 = r58
            r1 = r50
            r0.setCoercedReturnType(r1)
            goto L_0x036d
        L_0x0379:
            gnu.bytecode.Type r55 = r58.getReturnType()
            gnu.bytecode.Type r46 = r55.getImplementationType()
            goto L_0x016f
        L_0x0383:
            r14 = 0
            goto L_0x0178
        L_0x0386:
            if (r8 == 0) goto L_0x0391
            int r0 = r5.length
            r55 = r0
            int r55 = r55 + -1
            gnu.bytecode.ClassType r56 = gnu.expr.Compilation.typeCallContext
            r5[r55] = r56
        L_0x0391:
            r0 = r43
            r1 = r38
            if (r0 >= r1) goto L_0x0402
            gnu.bytecode.Type r25 = r52.getType()
            java.lang.String r26 = r25.getName()
            int r55 = r59.getClassfileVersion()
            r56 = 3211264(0x310000, float:4.49994E-39)
            r0 = r55
            r1 = r56
            if (r0 < r1) goto L_0x0460
            r0 = r25
            boolean r0 = r0 instanceof gnu.bytecode.ArrayType
            r55 = r0
            if (r55 == 0) goto L_0x0460
            r0 = r32
            r0 = r0 | 128(0x80, float:1.794E-43)
            r32 = r0
        L_0x03b9:
            if (r24 > 0) goto L_0x03d5
            r0 = r39
            r1 = r40
            if (r0 < r1) goto L_0x03d5
            java.lang.String r55 = "gnu.lists.LList"
            r0 = r55
            r1 = r26
            boolean r55 = r0.equals(r1)
            if (r55 != 0) goto L_0x03f1
            r0 = r25
            boolean r0 = r0 instanceof gnu.bytecode.ArrayType
            r55 = r0
            if (r55 != 0) goto L_0x03f1
        L_0x03d5:
            gnu.bytecode.ArrayType r25 = gnu.expr.Compilation.objArrayType
            gnu.bytecode.Variable r55 = new gnu.bytecode.Variable
            java.lang.String r56 = "argsArray"
            gnu.bytecode.ArrayType r57 = gnu.expr.Compilation.objArrayType
            r55.<init>(r56, r57)
            r0 = r55
            r1 = r58
            r1.argsArray = r0
            r0 = r58
            gnu.bytecode.Variable r0 = r0.argsArray
            r55 = r0
            r56 = 1
            r55.setParameter(r56)
        L_0x03f1:
            r0 = r52
            r1 = r58
            r1.firstArgsArrayArg = r0
            int r0 = r5.length
            r56 = r0
            if (r54 == 0) goto L_0x046b
            r55 = 2
        L_0x03fe:
            int r55 = r56 - r55
            r5[r55] = r25
        L_0x0402:
            if (r54 == 0) goto L_0x040d
            java.lang.String r55 = "$X"
            r0 = r37
            r1 = r55
            r0.append(r1)
        L_0x040d:
            r0 = r41
            boolean r0 = r0 instanceof gnu.expr.ClassExp
            r55 = r0
            if (r55 != 0) goto L_0x0429
            r0 = r41
            boolean r0 = r0 instanceof gnu.expr.ModuleExp
            r55 = r0
            if (r55 == 0) goto L_0x046e
            r55 = r41
            gnu.expr.ModuleExp r55 = (gnu.expr.ModuleExp) r55
            r56 = 131072(0x20000, float:1.83671E-40)
            boolean r55 = r55.getFlag(r56)
            if (r55 == 0) goto L_0x046e
        L_0x0429:
            r7 = 1
        L_0x042a:
            java.lang.String r35 = r37.toString()
            r45 = 0
            int r27 = r37.length()
        L_0x0434:
            r47 = r59
        L_0x0436:
            if (r47 == 0) goto L_0x0472
            r0 = r47
            r1 = r35
            gnu.bytecode.Method r55 = r0.getDeclaredMethod((java.lang.String) r1, (gnu.bytecode.Type[]) r5)
            if (r55 == 0) goto L_0x0470
            r0 = r37
            r1 = r27
            r0.setLength(r1)
            r55 = 36
            r0 = r37
            r1 = r55
            r0.append(r1)
            int r45 = r45 + 1
            r0 = r37
            r1 = r45
            r0.append(r1)
            java.lang.String r35 = r37.toString()
            goto L_0x0434
        L_0x0460:
            java.lang.String r55 = "$V"
            r0 = r37
            r1 = r55
            r0.append(r1)
            goto L_0x03b9
        L_0x046b:
            r55 = 1
            goto L_0x03fe
        L_0x046e:
            r7 = 0
            goto L_0x042a
        L_0x0470:
            if (r7 == 0) goto L_0x04f7
        L_0x0472:
            r0 = r59
            r1 = r35
            r2 = r46
            r3 = r32
            gnu.bytecode.Method r29 = r0.addMethod((java.lang.String) r1, (gnu.bytecode.Type[]) r5, (gnu.bytecode.Type) r2, (int) r3)
            r30[r16] = r29
            r0 = r58
            gnu.expr.Expression[] r0 = r0.throwsSpecification
            r55 = r0
            if (r55 == 0) goto L_0x058c
            r0 = r58
            gnu.expr.Expression[] r0 = r0.throwsSpecification
            r55 = r0
            r0 = r55
            int r0 = r0.length
            r55 = r0
            if (r55 <= 0) goto L_0x058c
            r0 = r58
            gnu.expr.Expression[] r0 = r0.throwsSpecification
            r55 = r0
            r0 = r55
            int r0 = r0.length
            r34 = r0
            r0 = r34
            gnu.bytecode.ClassType[] r13 = new gnu.bytecode.ClassType[r0]
            r23 = 0
        L_0x04a6:
            r0 = r23
            r1 = r34
            if (r0 >= r1) goto L_0x0582
            r12 = 0
            r0 = r58
            gnu.expr.Expression[] r0 = r0.throwsSpecification
            r55 = r0
            r48 = r55[r23]
            r33 = 0
            r0 = r48
            boolean r0 = r0 instanceof gnu.expr.ReferenceExp
            r55 = r0
            if (r55 == 0) goto L_0x0533
            r49 = r48
            gnu.expr.ReferenceExp r49 = (gnu.expr.ReferenceExp) r49
            gnu.expr.Declaration r9 = r49.getBinding()
            if (r9 == 0) goto L_0x051b
            gnu.expr.Expression r10 = r9.getValue()
            boolean r0 = r10 instanceof gnu.expr.ClassExp
            r55 = r0
            if (r55 == 0) goto L_0x04fd
            gnu.expr.ClassExp r10 = (gnu.expr.ClassExp) r10
            r0 = r60
            gnu.bytecode.ClassType r12 = r10.getCompiledClassType(r0)
        L_0x04db:
            if (r12 != 0) goto L_0x04e1
            if (r33 != 0) goto L_0x04e1
            java.lang.String r33 = "invalid throws specification"
        L_0x04e1:
            if (r33 == 0) goto L_0x04f2
            r55 = 101(0x65, float:1.42E-43)
            r0 = r60
            r1 = r55
            r2 = r33
            r3 = r48
            r0.error(r1, r2, r3)
            gnu.bytecode.ClassType r12 = gnu.bytecode.Type.javalangThrowableType
        L_0x04f2:
            r13[r23] = r12
            int r23 = r23 + 1
            goto L_0x04a6
        L_0x04f7:
            gnu.bytecode.ClassType r47 = r47.getSuperclass()
            goto L_0x0436
        L_0x04fd:
            java.lang.StringBuilder r55 = new java.lang.StringBuilder
            r55.<init>()
            java.lang.String r56 = "throws specification "
            java.lang.StringBuilder r55 = r55.append(r56)
            java.lang.String r56 = r9.getName()
            java.lang.StringBuilder r55 = r55.append(r56)
            java.lang.String r56 = " has non-class lexical binding"
            java.lang.StringBuilder r55 = r55.append(r56)
            java.lang.String r33 = r55.toString()
            goto L_0x04db
        L_0x051b:
            java.lang.StringBuilder r55 = new java.lang.StringBuilder
            r55.<init>()
            java.lang.String r56 = "unknown class "
            java.lang.StringBuilder r55 = r55.append(r56)
            java.lang.String r56 = r49.getName()
            java.lang.StringBuilder r55 = r55.append(r56)
            java.lang.String r33 = r55.toString()
            goto L_0x04db
        L_0x0533:
            r0 = r48
            boolean r0 = r0 instanceof gnu.expr.QuoteExp
            r55 = r0
            if (r55 == 0) goto L_0x04db
            r55 = r48
            gnu.expr.QuoteExp r55 = (gnu.expr.QuoteExp) r55
            java.lang.Object r51 = r55.getValue()
            r0 = r51
            boolean r0 = r0 instanceof java.lang.Class
            r55 = r0
            if (r55 == 0) goto L_0x0551
            java.lang.Class r51 = (java.lang.Class) r51
            gnu.bytecode.Type r51 = gnu.bytecode.Type.make(r51)
        L_0x0551:
            r0 = r51
            boolean r0 = r0 instanceof gnu.bytecode.ClassType
            r55 = r0
            if (r55 == 0) goto L_0x055d
            r12 = r51
            gnu.bytecode.ClassType r12 = (gnu.bytecode.ClassType) r12
        L_0x055d:
            if (r12 == 0) goto L_0x04db
            gnu.bytecode.ClassType r55 = gnu.bytecode.Type.javalangThrowableType
            r0 = r55
            boolean r55 = r12.isSubtype(r0)
            if (r55 != 0) goto L_0x04db
            java.lang.StringBuilder r55 = new java.lang.StringBuilder
            r55.<init>()
            java.lang.String r56 = r12.getName()
            java.lang.StringBuilder r55 = r55.append(r56)
            java.lang.String r56 = " does not extend Throwable"
            java.lang.StringBuilder r55 = r55.append(r56)
            java.lang.String r33 = r55.toString()
            goto L_0x04db
        L_0x0582:
            gnu.bytecode.ExceptionsAttr r4 = new gnu.bytecode.ExceptionsAttr
            r0 = r29
            r4.<init>(r0)
            r4.setExceptions(r13)
        L_0x058c:
            int r16 = r16 + 1
            goto L_0x018e
        L_0x0590:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.LambdaExp.addMethodFor(gnu.bytecode.ClassType, gnu.expr.Compilation, gnu.bytecode.ObjectType):void");
    }

    public void allocChildClasses(Compilation comp) {
        Method main = getMainMethod();
        if (main != null && !main.getStaticFlag()) {
            declareThis(main.getDeclaringClass());
        }
        Declaration decl = firstDecl();
        while (true) {
            if (decl == this.firstArgsArrayArg && this.argsArray != null) {
                getVarScope().addVariable(this.argsArray);
            }
            if (!getInlineOnly() && getCallConvention() >= 2 && (this.firstArgsArrayArg != null ? !(this.argsArray == null ? decl != this.firstArgsArrayArg.nextDecl() : decl != this.firstArgsArrayArg) : decl == null)) {
                getVarScope().addVariable((CodeAttr) null, Compilation.typeCallContext, "$ctx").setParameter(true);
            }
            if (decl == null) {
                declareClosureEnv();
                allocFrame(comp);
                allocChildMethods(comp);
                return;
            }
            if (decl.var == null && (!getInlineOnly() || !decl.ignorable())) {
                if (!decl.isSimple() || decl.isIndirectBinding()) {
                    String vname = Compilation.mangleName(decl.getName()).intern();
                    Variable var = getVarScope().addVariable((CodeAttr) null, decl.getType().getImplementationType(), vname);
                    decl.var = var;
                    var.setParameter(true);
                } else {
                    Variable var2 = decl.allocateVariable((CodeAttr) null);
                }
            }
            decl = decl.nextDecl();
        }
    }

    /* access modifiers changed from: package-private */
    public void allocMethod(LambdaExp outer, Compilation comp) {
        ObjectType closureEnvType;
        if (!getNeedsClosureEnv()) {
            closureEnvType = null;
        } else if ((outer instanceof ClassExp) || (outer instanceof ModuleExp)) {
            closureEnvType = outer.getCompiledClassType(comp);
        } else {
            LambdaExp owner = outer;
            while (owner.heapFrame == null) {
                owner = owner.outerLambda();
            }
            closureEnvType = (ClassType) owner.heapFrame.getType();
        }
        addMethodFor(comp, closureEnvType);
    }

    /* access modifiers changed from: package-private */
    public void allocChildMethods(Compilation comp) {
        ClassType parentFrameType;
        for (LambdaExp child = this.firstChild; child != null; child = child.nextSibling) {
            if (!child.isClassGenerated() && !child.getInlineOnly() && child.nameDecl != null) {
                child.allocMethod(this, comp);
            }
            if (child instanceof ClassExp) {
                ClassExp cl = (ClassExp) child;
                if (cl.getNeedsClosureEnv()) {
                    if ((this instanceof ModuleExp) || (this instanceof ClassExp)) {
                        parentFrameType = (ClassType) getType();
                    } else {
                        parentFrameType = (ClassType) (this.heapFrame != null ? this.heapFrame : this.closureEnv).getType();
                    }
                    Field outerLink = cl.instanceType.setOuterLink(parentFrameType);
                    cl.staticLinkField = outerLink;
                    cl.closureEnvField = outerLink;
                }
            }
        }
    }

    public void allocFrame(Compilation comp) {
        ClassType frameType;
        if (this.heapFrame != null) {
            if ((this instanceof ModuleExp) || (this instanceof ClassExp)) {
                frameType = getCompiledClassType(comp);
            } else {
                frameType = new ClassType(comp.generateClassName("frame"));
                frameType.setSuper(comp.getModuleType());
                comp.addClass(frameType);
            }
            this.heapFrame.setType(frameType);
        }
    }

    /* access modifiers changed from: package-private */
    public void allocParameters(Compilation comp) {
        CodeAttr code = comp.getCode();
        code.locals.enterScope(getVarScope());
        int line = getLineNumber();
        if (line > 0) {
            code.putLineNumber(getFileName(), line);
        }
        if (this.heapFrame != null) {
            this.heapFrame.allocateLocal(code);
        }
    }

    /* access modifiers changed from: package-private */
    public void enterFunction(Compilation comp) {
        Variable lookup;
        int opt_i;
        int key_i;
        Type stackType;
        CodeAttr code = comp.getCode();
        getVarScope().noteStartFunction(code);
        if (this.closureEnv != null && !this.closureEnv.isParameter() && !comp.usingCPStyle()) {
            if (!getInlineOnly()) {
                code.emitPushThis();
                Field field = this.closureEnvField;
                if (field == null) {
                    field = outerLambda().closureEnvField;
                }
                code.emitGetField(field);
                code.emitStore(this.closureEnv);
            } else if (!inlinedIn(outerLambda())) {
                outerLambda().loadHeapFrame(comp);
                code.emitStore(this.closureEnv);
            }
        }
        if (!comp.usingCPStyle()) {
            ClassType frameType = this.heapFrame == null ? currentModule().getCompiledClassType(comp) : (ClassType) this.heapFrame.getType();
            for (Declaration decl = this.capturedVars; decl != null; decl = decl.nextCapturedVar) {
                if (decl.field == null) {
                    decl.makeField(frameType, comp, (Expression) null);
                }
            }
        }
        if (this.heapFrame != null && !comp.usingCPStyle()) {
            ClassType frameType2 = (ClassType) this.heapFrame.getType();
            if (this.closureEnv != null && !(this instanceof ModuleExp)) {
                this.staticLinkField = frameType2.addField("staticLink", this.closureEnv.getType());
            }
            if (!(this instanceof ModuleExp) && !(this instanceof ClassExp)) {
                frameType2.setEnclosingMember(comp.method);
                code.emitNew(frameType2);
                code.emitDup((Type) frameType2);
                code.emitInvokeSpecial(Compilation.getConstructor(frameType2, this));
                if (this.staticLinkField != null) {
                    code.emitDup((Type) frameType2);
                    code.emitLoad(this.closureEnv);
                    code.emitPutField(this.staticLinkField);
                }
                code.emitStore(this.heapFrame);
            }
        }
        Variable argsArray2 = this.argsArray;
        if (this.min_args == this.max_args && this.primMethods == null && getCallConvention() < 2) {
            argsArray2 = null;
        }
        int i = 0;
        int opt_args = this.defaultArgs == null ? 0 : this.defaultArgs.length - (this.keywords == null ? 0 : this.keywords.length);
        if (!(this instanceof ModuleExp)) {
            int plainArgs = -1;
            int defaultStart = 0;
            Method mainMethod = getMainMethod();
            Variable callContextSave = comp.callContextVar;
            Declaration param = firstDecl();
            int key_i2 = 0;
            int opt_i2 = 0;
            while (param != null) {
                if (getCallConvention() < 2) {
                    lookup = null;
                } else {
                    lookup = getVarScope().lookup("$ctx");
                }
                comp.callContextVar = lookup;
                if (param == this.firstArgsArrayArg && argsArray2 != null) {
                    if (this.primMethods != null) {
                        plainArgs = i;
                        defaultStart = plainArgs - this.min_args;
                    } else {
                        plainArgs = 0;
                        defaultStart = 0;
                    }
                }
                if (plainArgs >= 0 || !param.isSimple() || param.isIndirectBinding()) {
                    Type paramType = param.getType();
                    if (plainArgs >= 0) {
                        stackType = Type.objectType;
                    } else {
                        stackType = paramType;
                    }
                    if (!param.isSimple()) {
                        param.loadOwningObject((Declaration) null, comp);
                    }
                    if (plainArgs < 0) {
                        code.emitLoad(param.getVariable());
                        key_i = key_i2;
                        opt_i = opt_i2;
                    } else if (i < this.min_args) {
                        code.emitLoad(argsArray2);
                        code.emitPushInt(i);
                        code.emitArrayLoad(Type.objectType);
                        key_i = key_i2;
                        opt_i = opt_i2;
                    } else if (i < this.min_args + opt_args) {
                        code.emitPushInt(i - plainArgs);
                        code.emitLoad(argsArray2);
                        code.emitArrayLength();
                        code.emitIfIntLt();
                        code.emitLoad(argsArray2);
                        code.emitPushInt(i - plainArgs);
                        code.emitArrayLoad();
                        code.emitElse();
                        opt_i = opt_i2 + 1;
                        this.defaultArgs[defaultStart + opt_i2].compile(comp, paramType);
                        code.emitFi();
                        key_i = key_i2;
                    } else if (this.max_args >= 0 || i != this.min_args + opt_args) {
                        code.emitLoad(argsArray2);
                        code.emitPushInt((this.min_args + opt_args) - plainArgs);
                        key_i = key_i2 + 1;
                        comp.compileConstant(this.keywords[key_i2]);
                        opt_i = opt_i2 + 1;
                        Expression defaultArg = this.defaultArgs[defaultStart + opt_i2];
                        if (defaultArg instanceof QuoteExp) {
                            if (searchForKeywordMethod4 == null) {
                                searchForKeywordMethod4 = Compilation.scmKeywordType.addMethod("searchForKeyword", new Type[]{Compilation.objArrayType, Type.intType, Type.objectType, Type.objectType}, (Type) Type.objectType, 9);
                            }
                            defaultArg.compile(comp, paramType);
                            code.emitInvokeStatic(searchForKeywordMethod4);
                        } else {
                            if (searchForKeywordMethod3 == null) {
                                searchForKeywordMethod3 = Compilation.scmKeywordType.addMethod("searchForKeyword", new Type[]{Compilation.objArrayType, Type.intType, Type.objectType}, (Type) Type.objectType, 9);
                            }
                            code.emitInvokeStatic(searchForKeywordMethod3);
                            code.emitDup(1);
                            comp.compileConstant(Special.dfault);
                            code.emitIfEq();
                            code.emitPop(1);
                            defaultArg.compile(comp, paramType);
                            code.emitFi();
                        }
                    } else {
                        code.emitLoad(argsArray2);
                        code.emitPushInt(i - plainArgs);
                        code.emitInvokeStatic(Compilation.makeListMethod);
                        stackType = Compilation.scmListType;
                        key_i = key_i2;
                        opt_i = opt_i2;
                    }
                    if (paramType != stackType) {
                        CheckedTarget.emitCheckedCoerce(comp, this, i + 1, paramType);
                    }
                    if (param.isIndirectBinding()) {
                        param.pushIndirectBinding(comp);
                    }
                    if (param.isSimple()) {
                        Variable var = param.getVariable();
                        if (param.isIndirectBinding()) {
                            var.setType(Compilation.typeLocation);
                        }
                        code.emitStore(var);
                    } else {
                        code.emitPutField(param.field);
                    }
                } else {
                    key_i = key_i2;
                    opt_i = opt_i2;
                }
                i++;
                param = param.nextDecl();
                key_i2 = key_i;
                opt_i2 = opt_i;
            }
            comp.callContextVar = callContextSave;
            int i2 = key_i2;
            int i3 = opt_i2;
        }
    }

    /* access modifiers changed from: package-private */
    public void compileAsMethod(Compilation comp) {
        Expression arg;
        if ((this.flags & 128) == 0 && !isAbstract()) {
            this.flags |= 128;
            if (this.primMethods != null) {
                Method save_method = comp.method;
                LambdaExp save_lambda = comp.curLambda;
                comp.curLambda = this;
                boolean isStatic = this.primMethods[0].getStaticFlag();
                int numStubs = this.primMethods.length - 1;
                Type restArgType = restArgType();
                long[] saveDeclFlags = null;
                if (numStubs > 0) {
                    saveDeclFlags = new long[(this.min_args + numStubs)];
                    Declaration decl = firstDecl();
                    for (int k = 0; k < this.min_args + numStubs; k++) {
                        saveDeclFlags[k] = decl.flags;
                        decl = decl.nextDecl();
                    }
                }
                boolean ctxArg = getCallConvention() >= 2;
                for (int i = 0; i <= numStubs; i++) {
                    comp.method = this.primMethods[i];
                    if (i < numStubs) {
                        CodeAttr code = comp.method.startCode();
                        int toCall = i + 1;
                        while (toCall < numStubs && (this.defaultArgs[toCall] instanceof QuoteExp)) {
                            toCall++;
                        }
                        boolean varArgs = toCall == numStubs && restArgType != null;
                        Variable callContextSave = comp.callContextVar;
                        Variable var = code.getArg(0);
                        if (!isStatic) {
                            code.emitPushThis();
                            if (getNeedsClosureEnv()) {
                                this.closureEnv = var;
                            }
                            var = code.getArg(1);
                        }
                        Declaration decl2 = firstDecl();
                        int j = 0;
                        while (j < this.min_args + i) {
                            decl2.flags |= 64;
                            decl2.var = var;
                            code.emitLoad(var);
                            var = var.nextVar();
                            j++;
                            decl2 = decl2.nextDecl();
                        }
                        comp.callContextVar = ctxArg ? var : null;
                        int j2 = i;
                        while (j2 < toCall) {
                            this.defaultArgs[j2].compile(comp, StackTarget.getInstance(decl2.getType()));
                            j2++;
                            decl2 = decl2.nextDecl();
                        }
                        if (varArgs) {
                            String lastTypeName = restArgType.getName();
                            if ("gnu.lists.LList".equals(lastTypeName)) {
                                arg = new QuoteExp(LList.Empty);
                            } else if ("java.lang.Object[]".equals(lastTypeName)) {
                                arg = new QuoteExp(Values.noArgs);
                            } else {
                                throw new Error("unimplemented #!rest type " + lastTypeName);
                            }
                            arg.compile(comp, restArgType);
                        }
                        if (ctxArg) {
                            code.emitLoad(var);
                        }
                        if (isStatic) {
                            code.emitInvokeStatic(this.primMethods[toCall]);
                        } else {
                            code.emitInvokeVirtual(this.primMethods[toCall]);
                        }
                        code.emitReturn();
                        this.closureEnv = null;
                        comp.callContextVar = callContextSave;
                    } else {
                        if (saveDeclFlags != null) {
                            Declaration decl3 = firstDecl();
                            for (int k2 = 0; k2 < this.min_args + numStubs; k2++) {
                                decl3.flags = saveDeclFlags[k2];
                                decl3.var = null;
                                decl3 = decl3.nextDecl();
                            }
                        }
                        comp.method.initCode();
                        allocChildClasses(comp);
                        allocParameters(comp);
                        enterFunction(comp);
                        compileBody(comp);
                        compileEnd(comp);
                        generateApplyMethods(comp);
                    }
                }
                comp.method = save_method;
                comp.curLambda = save_lambda;
            }
        }
    }

    /* Debug info: failed to restart local var, previous not found, register: 5 */
    public void compileBody(Compilation comp) {
        Target target;
        Variable callContextSave = comp.callContextVar;
        comp.callContextVar = null;
        if (getCallConvention() >= 2) {
            Variable var = getVarScope().lookup("$ctx");
            if (var != null && var.getType() == Compilation.typeCallContext) {
                comp.callContextVar = var;
            }
            target = ConsumerTarget.makeContextTarget(comp);
        } else {
            target = Target.pushValue(getReturnType());
        }
        Expression expression = this.body;
        int lineNumber = this.body.getLineNumber();
        this = this;
        if (lineNumber > 0) {
            this = this.body;
        }
        expression.compileWithPosition(comp, target, this);
        comp.callContextVar = callContextSave;
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        LambdaExp saveLambda;
        Compilation comp = visitor.getCompilation();
        if (comp == null) {
            saveLambda = null;
        } else {
            saveLambda = comp.curLambda;
            comp.curLambda = this;
        }
        try {
            return visitor.visitLambdaExp(this, d);
        } finally {
            if (comp != null) {
                comp.curLambda = saveLambda;
            }
        }
    }

    /* access modifiers changed from: protected */
    public <R, D> void visitChildren(ExpVisitor<R, D> visitor, D d) {
        visitChildrenOnly(visitor, d);
        visitProperties(visitor, d);
    }

    /* access modifiers changed from: protected */
    public final <R, D> void visitChildrenOnly(ExpVisitor<R, D> visitor, D d) {
        LambdaExp save = visitor.currentLambda;
        visitor.currentLambda = this;
        try {
            this.throwsSpecification = visitor.visitExps(this.throwsSpecification, d);
            visitor.visitDefaultArgs(this, d);
            if (visitor.exitValue == null && this.body != null) {
                this.body = visitor.update(this.body, visitor.visit(this.body, d));
            }
        } finally {
            visitor.currentLambda = save;
        }
    }

    /* access modifiers changed from: protected */
    public final <R, D> void visitProperties(ExpVisitor<R, D> visitor, D d) {
        if (this.properties != null) {
            int len = this.properties.length;
            for (int i = 1; i < len; i += 2) {
                Object val = this.properties[i];
                if (val instanceof Expression) {
                    this.properties[i] = visitor.visitAndUpdate((Expression) val, d);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        if (this.keywords != null && this.keywords.length > 0) {
            return true;
        }
        if (this.defaultArgs != null) {
            int i = this.defaultArgs.length;
            while (true) {
                i--;
                if (i < 0) {
                    break;
                }
                Expression def = this.defaultArgs[i];
                if (def != null && !(def instanceof QuoteExp)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void apply(CallContext ctx) throws Throwable {
        setIndexes();
        ctx.writeValue(new Closure(this, ctx));
    }

    /* access modifiers changed from: package-private */
    public Object evalDefaultArg(int index, CallContext ctx) {
        try {
            return this.defaultArgs[index].eval(ctx);
        } catch (Throwable ex) {
            throw new WrappedException("error evaluating default argument", ex);
        }
    }

    public Expression validateApply(ApplyExp exp, InlineCalls visitor, Type required, Declaration decl) {
        Method method;
        Expression[] margs;
        Expression inlined;
        Expression[] args = exp.getArgs();
        if ((this.flags & 4096) != 0 && (inlined = InlineCalls.inlineCall(this, args, true)) != null) {
            return visitor.visit(inlined, required);
        }
        exp.visitArgs(visitor);
        int args_length = exp.args.length;
        String msg = WrongArguments.checkArgCount(getName(), this.min_args, this.max_args, args_length);
        if (msg != null) {
            return visitor.noteError(msg);
        }
        int conv = getCallConvention();
        if (!visitor.getCompilation().inlineOk((Expression) this) || !isClassMethod()) {
            return exp;
        }
        if ((conv > 2 && conv != 3) || (method = getMethod(args_length)) == null) {
            return exp;
        }
        boolean isStatic = this.nameDecl.isStatic();
        if (isStatic || !(this.outer instanceof ClassExp) || ((ClassExp) this.outer).isMakingClassPair()) {
        }
        PrimProcedure mproc = new PrimProcedure(method, this);
        if (isStatic) {
            margs = exp.args;
        } else {
            LambdaExp curLambda = visitor.getCurrentLambda();
            while (curLambda != null) {
                if (curLambda.outer == this.outer) {
                    Declaration d = curLambda.firstDecl();
                    if (d == null || !d.isThisParameter()) {
                        return visitor.noteError("calling non-static method " + getName() + " from static method " + curLambda.getName());
                    }
                    int nargs = exp.getArgCount();
                    margs = new Expression[(nargs + 1)];
                    System.arraycopy(exp.getArgs(), 0, margs, 1, nargs);
                    margs[0] = new ThisExp(d);
                } else {
                    curLambda = curLambda.outerLambda();
                }
            }
            return visitor.noteError("internal error: missing " + this);
        }
        return new ApplyExp((Procedure) mproc, margs).setLine((Expression) exp);
    }

    public void print(OutPort out) {
        int opt_i;
        Special mode;
        int opt_i2;
        out.startLogicalBlock("(Lambda/", ")", 2);
        Object sym = getSymbol();
        if (sym != null) {
            out.print(sym);
            out.print('/');
        }
        out.print(this.id);
        out.print('/');
        out.print("fl:");
        out.print(Integer.toHexString(this.flags));
        out.writeSpaceFill();
        printLineColumn(out);
        out.startLogicalBlock("(", false, ")");
        Special prevMode = null;
        int i = 0;
        int opt_args = this.defaultArgs == null ? 0 : this.defaultArgs.length - (this.keywords == null ? 0 : this.keywords.length);
        Declaration decl = firstDecl();
        if (decl == null || !decl.isThisParameter()) {
            opt_i = 0;
        } else {
            i = -1;
            opt_i = 0;
        }
        while (decl != null) {
            if (i < this.min_args) {
                mode = null;
            } else if (i < this.min_args + opt_args) {
                mode = Special.optional;
            } else if (this.max_args >= 0 || i != this.min_args + opt_args) {
                mode = Special.key;
            } else {
                mode = Special.rest;
            }
            if (decl != firstDecl()) {
                out.writeSpaceFill();
            }
            if (mode != prevMode) {
                out.print((Object) mode);
                out.writeSpaceFill();
            }
            Expression defaultArg = null;
            if (mode == Special.optional || mode == Special.key) {
                opt_i2 = opt_i + 1;
                defaultArg = this.defaultArgs[opt_i];
            } else {
                opt_i2 = opt_i;
            }
            if (defaultArg != null) {
                out.print('(');
            }
            decl.printInfo(out);
            if (!(defaultArg == null || defaultArg == QuoteExp.falseExp)) {
                out.print(' ');
                defaultArg.print(out);
                out.print(')');
            }
            i++;
            prevMode = mode;
            decl = decl.nextDecl();
            opt_i = opt_i2;
        }
        out.endLogicalBlock(")");
        out.writeSpaceLinear();
        if (this.body == null) {
            out.print("<null body>");
        } else {
            this.body.print(out);
        }
        out.endLogicalBlock(")");
    }

    /* access modifiers changed from: protected */
    public final String getExpClassName() {
        String cname = getClass().getName();
        int index = cname.lastIndexOf(46);
        if (index >= 0) {
            return cname.substring(index + 1);
        }
        return cname;
    }

    public boolean side_effects() {
        return false;
    }

    public String toString() {
        String str = getExpClassName() + ':' + getSymbol() + '/' + this.id + '/';
        int l = getLineNumber();
        if (l <= 0 && this.body != null) {
            l = this.body.getLineNumber();
        }
        if (l > 0) {
            return str + "l:" + l;
        }
        return str;
    }

    public Object getProperty(Object key, Object defaultValue) {
        if (this.properties == null) {
            return defaultValue;
        }
        int i = this.properties.length;
        do {
            i -= 2;
            if (i < 0) {
                return defaultValue;
            }
        } while (this.properties[i] != key);
        return this.properties[i + 1];
    }

    public synchronized void setProperty(Object key, Object value) {
        this.properties = PropertySet.setProperty(this.properties, key, value);
    }

    public final Type getReturnType() {
        if (this.returnType == null) {
            this.returnType = Type.objectType;
            if (this.body != null && !isAbstract()) {
                this.returnType = this.body.getType();
            }
        }
        return this.returnType;
    }

    public final void setReturnType(Type returnType2) {
        this.returnType = returnType2;
    }

    public final void setCoercedReturnType(Type returnType2) {
        this.returnType = returnType2;
        if (returnType2 != null && returnType2 != Type.objectType && returnType2 != Type.voidType && this.body != QuoteExp.abstractExp) {
            Expression value = this.body;
            this.body = Compilation.makeCoercion(value, returnType2);
            this.body.setLine(value);
        }
    }

    public final void setCoercedReturnValue(Expression type2, Language language) {
        if (!isAbstract()) {
            Expression value = this.body;
            this.body = Compilation.makeCoercion(value, type2);
            this.body.setLine(value);
        }
        Type rtype = language.getTypeFor(type2);
        if (rtype != null) {
            setReturnType(rtype);
        }
    }
}
