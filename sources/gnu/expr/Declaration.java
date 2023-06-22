package gnu.expr;

import com.google.appinventor.components.runtime.repackaged.org.json.zip.JSONzip;
import gnu.bytecode.Access;
import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Field;
import gnu.bytecode.Method;
import gnu.bytecode.PrimType;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.mapping.Location;
import gnu.mapping.Named;
import gnu.mapping.Namespace;
import gnu.mapping.OutPort;
import gnu.mapping.Symbol;
import gnu.mapping.WrappedException;
import gnu.math.IntNum;
import gnu.text.Char;
import gnu.text.SourceLocator;

public class Declaration implements SourceLocator {
    static final int CAN_CALL = 4;
    static final int CAN_READ = 2;
    static final int CAN_WRITE = 8;
    public static final long CLASS_ACCESS_FLAGS = 25820135424L;
    public static final int EARLY_INIT = 536870912;
    public static final long ENUM_ACCESS = 8589934592L;
    public static final int EXPORT_SPECIFIED = 1024;
    public static final int EXTERNAL_ACCESS = 524288;
    public static final long FIELD_ACCESS_FLAGS = 32463912960L;
    public static final int FIELD_OR_METHOD = 1048576;
    public static final long FINAL_ACCESS = 17179869184L;
    static final int INDIRECT_BINDING = 1;
    public static final int IS_ALIAS = 256;
    public static final int IS_CONSTANT = 16384;
    public static final int IS_DYNAMIC = 268435456;
    static final int IS_FLUID = 16;
    public static final int IS_IMPORTED = 131072;
    public static final int IS_NAMESPACE_PREFIX = 2097152;
    static final int IS_SIMPLE = 64;
    public static final int IS_SINGLE_VALUE = 262144;
    public static final int IS_SYNTAX = 32768;
    public static final int IS_UNKNOWN = 65536;
    public static final long METHOD_ACCESS_FLAGS = 17431527424L;
    public static final int MODULE_REFERENCE = 1073741824;
    public static final int NONSTATIC_SPECIFIED = 4096;
    public static final int NOT_DEFINING = 512;
    public static final int PACKAGE_ACCESS = 134217728;
    static final int PRIVATE = 32;
    public static final int PRIVATE_ACCESS = 16777216;
    public static final String PRIVATE_PREFIX = "$Prvt$";
    public static final int PRIVATE_SPECIFIED = 16777216;
    static final int PROCEDURE = 128;
    public static final int PROTECTED_ACCESS = 33554432;
    public static final int PUBLIC_ACCESS = 67108864;
    public static final int STATIC_SPECIFIED = 2048;
    public static final long TRANSIENT_ACCESS = 4294967296L;
    public static final int TYPE_SPECIFIED = 8192;
    static final String UNKNOWN_PREFIX = "loc$";
    public static final long VOLATILE_ACCESS = 2147483648L;
    static int counter;
    public Declaration base;
    public ScopeExp context;
    int evalIndex;
    public Field field;
    String filename;
    public ApplyExp firstCall;
    protected long flags;
    protected int id;
    Method makeLocationMethod;
    Declaration next;
    Declaration nextCapturedVar;
    int position;
    Object symbol;
    protected Type type;
    protected Expression typeExp;
    protected Expression value;
    Variable var;

    public void setCode(int code) {
        if (code >= 0) {
            throw new Error("code must be negative");
        }
        this.id = code;
    }

    public int getCode() {
        return this.id;
    }

    public final Expression getTypeExp() {
        if (this.typeExp == null) {
            setType(Type.objectType);
        }
        return this.typeExp;
    }

    public final Type getType() {
        if (this.type == null) {
            setType(Type.objectType);
        }
        return this.type;
    }

    public final void setType(Type type2) {
        this.type = type2;
        if (this.var != null) {
            this.var.setType(type2);
        }
        this.typeExp = QuoteExp.getInstance(type2);
    }

    public final void setTypeExp(Expression typeExp2) {
        Type t;
        this.typeExp = typeExp2;
        if (typeExp2 instanceof TypeValue) {
            t = ((TypeValue) typeExp2).getImplementationType();
        } else {
            t = Language.getDefaultLanguage().getTypeFor(typeExp2, false);
        }
        if (t == null) {
            t = Type.pointer_type;
        }
        this.type = t;
        if (this.var != null) {
            this.var.setType(t);
        }
    }

    public final String getName() {
        if (this.symbol == null) {
            return null;
        }
        return this.symbol instanceof Symbol ? ((Symbol) this.symbol).getName() : this.symbol.toString();
    }

    public final void setName(Object symbol2) {
        this.symbol = symbol2;
    }

    public final Object getSymbol() {
        return this.symbol;
    }

    public final void setSymbol(Object symbol2) {
        this.symbol = symbol2;
    }

    public final Declaration nextDecl() {
        return this.next;
    }

    public final void setNext(Declaration next2) {
        this.next = next2;
    }

    public Variable getVariable() {
        return this.var;
    }

    public final boolean isSimple() {
        return (this.flags & 64) != 0;
    }

    public final void setSimple(boolean b) {
        setFlag(b, 64);
        if (this.var != null && !this.var.isParameter()) {
            this.var.setSimple(b);
        }
    }

    public final void setSyntax() {
        setSimple(false);
        setFlag(536920064);
    }

    public final ScopeExp getContext() {
        return this.context;
    }

    /* access modifiers changed from: package-private */
    public void loadOwningObject(Declaration owner, Compilation comp) {
        if (owner == null) {
            owner = this.base;
        }
        if (owner != null) {
            owner.load((AccessExp) null, 0, comp, Target.pushObject);
        } else {
            getContext().currentLambda().loadHeapFrame(comp);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: gnu.expr.QuoteExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v46, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r32v34, resolved type: gnu.mapping.Symbol} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void load(gnu.expr.AccessExp r36, int r37, gnu.expr.Compilation r38, gnu.expr.Target r39) {
        /*
            r35 = this;
            r0 = r39
            boolean r0 = r0 instanceof gnu.expr.IgnoreTarget
            r32 = r0
            if (r32 == 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            if (r36 != 0) goto L_0x0051
            r23 = 0
        L_0x000d:
            boolean r32 = r35.isAlias()
            if (r32 == 0) goto L_0x0056
            r0 = r35
            gnu.expr.Expression r0 = r0.value
            r32 = r0
            r0 = r32
            boolean r0 = r0 instanceof gnu.expr.ReferenceExp
            r32 = r0
            if (r32 == 0) goto L_0x0056
            r0 = r35
            gnu.expr.Expression r0 = r0.value
            r25 = r0
            gnu.expr.ReferenceExp r25 = (gnu.expr.ReferenceExp) r25
            r0 = r25
            gnu.expr.Declaration r0 = r0.binding
            r22 = r0
            if (r22 == 0) goto L_0x0056
            r32 = r37 & 2
            if (r32 == 0) goto L_0x003b
            boolean r32 = r22.isIndirectBinding()
            if (r32 == 0) goto L_0x0056
        L_0x003b:
            if (r23 == 0) goto L_0x0043
            boolean r32 = r22.needsContext()
            if (r32 != 0) goto L_0x0056
        L_0x0043:
            r0 = r22
            r1 = r25
            r2 = r37
            r3 = r38
            r4 = r39
            r0.load(r1, r2, r3, r4)
            goto L_0x0008
        L_0x0051:
            gnu.expr.Declaration r23 = r36.contextDecl()
            goto L_0x000d
        L_0x0056:
            boolean r32 = r35.isFluid()
            if (r32 == 0) goto L_0x007e
            r0 = r35
            gnu.expr.ScopeExp r0 = r0.context
            r32 = r0
            r0 = r32
            boolean r0 = r0 instanceof gnu.expr.FluidLetExp
            r32 = r0
            if (r32 == 0) goto L_0x007e
            r0 = r35
            gnu.expr.Declaration r0 = r0.base
            r32 = r0
            r0 = r32
            r1 = r36
            r2 = r37
            r3 = r38
            r4 = r39
            r0.load(r1, r2, r3, r4)
            goto L_0x0008
        L_0x007e:
            gnu.bytecode.CodeAttr r7 = r38.getCode()
            gnu.bytecode.Type r26 = r35.getType()
            boolean r32 = r35.isIndirectBinding()
            if (r32 != 0) goto L_0x0145
            r32 = r37 & 2
            if (r32 == 0) goto L_0x0145
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            if (r32 != 0) goto L_0x00b5
            java.lang.Error r32 = new java.lang.Error
            java.lang.StringBuilder r33 = new java.lang.StringBuilder
            r33.<init>()
            java.lang.String r34 = "internal error: cannot take location of "
            java.lang.StringBuilder r33 = r33.append(r34)
            r0 = r33
            r1 = r35
            java.lang.StringBuilder r33 = r0.append(r1)
            java.lang.String r33 = r33.toString()
            r32.<init>(r33)
            throw r32
        L_0x00b5:
            r0 = r38
            boolean r0 = r0.immediate
            r16 = r0
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            boolean r32 = r32.getStaticFlag()
            if (r32 == 0) goto L_0x00fb
            java.lang.String r32 = "gnu.kawa.reflect.StaticFieldLocation"
            gnu.bytecode.ClassType r20 = gnu.bytecode.ClassType.make(r32)
            java.lang.String r33 = "make"
            if (r16 == 0) goto L_0x00f8
            r32 = 1
        L_0x00d3:
            r0 = r20
            r1 = r33
            r2 = r32
            gnu.bytecode.Method r21 = r0.getDeclaredMethod((java.lang.String) r1, (int) r2)
        L_0x00dd:
            if (r16 == 0) goto L_0x011e
            r0 = r38
            r1 = r35
            r0.compileConstant(r1)
        L_0x00e6:
            r0 = r21
            r7.emitInvokeStatic(r0)
            r26 = r20
        L_0x00ed:
            r0 = r39
            r1 = r38
            r2 = r26
            r0.compileFromStack(r1, r2)
            goto L_0x0008
        L_0x00f8:
            r32 = 2
            goto L_0x00d3
        L_0x00fb:
            java.lang.String r32 = "gnu.kawa.reflect.FieldLocation"
            gnu.bytecode.ClassType r20 = gnu.bytecode.ClassType.make(r32)
            java.lang.String r33 = "make"
            if (r16 == 0) goto L_0x011b
            r32 = 2
        L_0x0107:
            r0 = r20
            r1 = r33
            r2 = r32
            gnu.bytecode.Method r21 = r0.getDeclaredMethod((java.lang.String) r1, (int) r2)
            r0 = r35
            r1 = r23
            r2 = r38
            r0.loadOwningObject(r1, r2)
            goto L_0x00dd
        L_0x011b:
            r32 = 3
            goto L_0x0107
        L_0x011e:
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            gnu.bytecode.ClassType r32 = r32.getDeclaringClass()
            java.lang.String r32 = r32.getName()
            r0 = r38
            r1 = r32
            r0.compileConstant(r1)
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            java.lang.String r32 = r32.getName()
            r0 = r38
            r1 = r32
            r0.compileConstant(r1)
            goto L_0x00e6
        L_0x0145:
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            if (r32 == 0) goto L_0x0227
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            gnu.bytecode.ClassType r32 = r32.getDeclaringClass()
            r0 = r38
            r1 = r32
            r0.usedClass(r1)
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            gnu.bytecode.Type r32 = r32.getType()
            r0 = r38
            r1 = r32
            r0.usedClass(r1)
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            boolean r32 = r32.getStaticFlag()
            if (r32 != 0) goto L_0x021a
            r0 = r35
            r1 = r23
            r2 = r38
            r0.loadOwningObject(r1, r2)
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            r0 = r32
            r7.emitGetField(r0)
        L_0x018f:
            boolean r32 = r35.isIndirectBinding()
            if (r32 == 0) goto L_0x00ed
            r32 = r37 & 2
            if (r32 != 0) goto L_0x00ed
            if (r36 == 0) goto L_0x035e
            java.lang.String r12 = r36.getFileName()
            if (r12 == 0) goto L_0x035e
            int r18 = r36.getLineNumber()
            if (r18 <= 0) goto L_0x035e
            java.lang.String r32 = "gnu.mapping.UnboundLocationException"
            gnu.bytecode.ClassType r29 = gnu.bytecode.ClassType.make(r32)
            boolean r17 = r7.isInTry()
            int r8 = r36.getColumnNumber()
            gnu.bytecode.Label r27 = new gnu.bytecode.Label
            r0 = r27
            r0.<init>((gnu.bytecode.CodeAttr) r7)
            r0 = r27
            r0.define(r7)
            gnu.bytecode.Method r32 = gnu.expr.Compilation.getLocationMethod
            r0 = r32
            r7.emitInvokeVirtual(r0)
            gnu.bytecode.Label r10 = new gnu.bytecode.Label
            r10.<init>((gnu.bytecode.CodeAttr) r7)
            r10.define(r7)
            gnu.bytecode.Label r9 = new gnu.bytecode.Label
            r9.<init>((gnu.bytecode.CodeAttr) r7)
            r9.setTypes((gnu.bytecode.CodeAttr) r7)
            if (r17 == 0) goto L_0x0354
            r7.emitGoto(r9)
        L_0x01dd:
            r13 = 0
            if (r17 != 0) goto L_0x01e4
            int r13 = r7.beginFragment(r9)
        L_0x01e4:
            r0 = r27
            r1 = r29
            r7.addHandler(r0, r10, r1)
            r0 = r29
            r7.emitDup((gnu.bytecode.Type) r0)
            r7.emitPushString(r12)
            r0 = r18
            r7.emitPushInt(r0)
            r7.emitPushInt(r8)
            java.lang.String r32 = "setLine"
            r33 = 3
            r0 = r29
            r1 = r32
            r2 = r33
            gnu.bytecode.Method r32 = r0.getDeclaredMethod((java.lang.String) r1, (int) r2)
            r0 = r32
            r7.emitInvokeVirtual(r0)
            r7.emitThrow()
            if (r17 == 0) goto L_0x0359
            r9.define(r7)
        L_0x0216:
            gnu.bytecode.ClassType r26 = gnu.bytecode.Type.pointer_type
            goto L_0x00ed
        L_0x021a:
            r0 = r35
            gnu.bytecode.Field r0 = r0.field
            r32 = r0
            r0 = r32
            r7.emitGetStatic(r0)
            goto L_0x018f
        L_0x0227:
            boolean r32 = r35.isIndirectBinding()
            if (r32 == 0) goto L_0x0295
            r0 = r38
            boolean r0 = r0.immediate
            r32 = r0
            if (r32 == 0) goto L_0x0295
            gnu.bytecode.Variable r32 = r35.getVariable()
            if (r32 != 0) goto L_0x0295
            gnu.mapping.Environment r11 = gnu.mapping.Environment.getCurrent()
            r0 = r35
            java.lang.Object r0 = r0.symbol
            r32 = r0
            r0 = r32
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r32 = r0
            if (r32 == 0) goto L_0x0284
            r0 = r35
            java.lang.Object r0 = r0.symbol
            r32 = r0
            gnu.mapping.Symbol r32 = (gnu.mapping.Symbol) r32
            r28 = r32
        L_0x0257:
            r24 = 0
            boolean r32 = r35.isProcedureDecl()
            if (r32 == 0) goto L_0x026b
            gnu.expr.Language r32 = r38.getLanguage()
            boolean r32 = r32.hasSeparateFunctionNamespace()
            if (r32 == 0) goto L_0x026b
            java.lang.Object r24 = gnu.mapping.EnvironmentKey.FUNCTION
        L_0x026b:
            r0 = r28
            r1 = r24
            gnu.mapping.Location r19 = r11.getLocation((gnu.mapping.Symbol) r0, (java.lang.Object) r1)
            gnu.bytecode.ClassType r32 = gnu.expr.Compilation.typeLocation
            gnu.expr.Target r32 = gnu.expr.Target.pushValue(r32)
            r0 = r38
            r1 = r19
            r2 = r32
            r0.compileConstant(r1, r2)
            goto L_0x018f
        L_0x0284:
            r0 = r35
            java.lang.Object r0 = r0.symbol
            r32 = r0
            java.lang.String r32 = r32.toString()
            r0 = r32
            gnu.mapping.Symbol r28 = r11.getSymbol(r0)
            goto L_0x0257
        L_0x0295:
            r0 = r38
            boolean r0 = r0.immediate
            r32 = r0
            if (r32 == 0) goto L_0x02ae
            java.lang.Object r30 = r35.getConstantValue()
            if (r30 == 0) goto L_0x02ae
            r0 = r38
            r1 = r30
            r2 = r39
            r0.compileConstant(r1, r2)
            goto L_0x0008
        L_0x02ae:
            r0 = r35
            gnu.expr.Expression r0 = r0.value
            r32 = r0
            gnu.expr.QuoteExp r33 = gnu.expr.QuoteExp.undefined_exp
            r0 = r32
            r1 = r33
            if (r0 == r1) goto L_0x02f7
            boolean r32 = r35.ignorable()
            if (r32 == 0) goto L_0x02f7
            r0 = r35
            gnu.expr.Expression r0 = r0.value
            r32 = r0
            r0 = r32
            boolean r0 = r0 instanceof gnu.expr.LambdaExp
            r32 = r0
            if (r32 == 0) goto L_0x02e6
            r0 = r35
            gnu.expr.Expression r0 = r0.value
            r32 = r0
            gnu.expr.LambdaExp r32 = (gnu.expr.LambdaExp) r32
            r0 = r32
            gnu.expr.ScopeExp r0 = r0.outer
            r32 = r0
            r0 = r32
            boolean r0 = r0 instanceof gnu.expr.ModuleExp
            r32 = r0
            if (r32 != 0) goto L_0x02f7
        L_0x02e6:
            r0 = r35
            gnu.expr.Expression r0 = r0.value
            r32 = r0
            r0 = r32
            r1 = r38
            r2 = r39
            r0.compile((gnu.expr.Compilation) r1, (gnu.expr.Target) r2)
            goto L_0x0008
        L_0x02f7:
            gnu.bytecode.Variable r31 = r35.getVariable()
            r0 = r35
            gnu.expr.ScopeExp r0 = r0.context
            r32 = r0
            r0 = r32
            boolean r0 = r0 instanceof gnu.expr.ClassExp
            r32 = r0
            if (r32 == 0) goto L_0x0345
            if (r31 != 0) goto L_0x0345
            r32 = 128(0x80, double:6.32E-322)
            r0 = r35
            r1 = r32
            boolean r32 = r0.getFlag(r1)
            if (r32 != 0) goto L_0x0345
            r0 = r35
            gnu.expr.ScopeExp r6 = r0.context
            gnu.expr.ClassExp r6 = (gnu.expr.ClassExp) r6
            boolean r32 = r6.isMakingClassPair()
            if (r32 == 0) goto L_0x0345
            java.lang.String r32 = "get"
            java.lang.String r33 = r35.getName()
            java.lang.String r14 = gnu.expr.ClassExp.slotToMethodName(r32, r33)
            gnu.bytecode.ClassType r0 = r6.type
            r32 = r0
            r33 = 0
            r0 = r32
            r1 = r33
            gnu.bytecode.Method r15 = r0.getDeclaredMethod((java.lang.String) r14, (int) r1)
            r0 = r38
            r6.loadHeapFrame(r0)
            r7.emitInvoke(r15)
            goto L_0x018f
        L_0x0345:
            if (r31 != 0) goto L_0x034d
            r0 = r35
            gnu.bytecode.Variable r31 = r0.allocateVariable(r7)
        L_0x034d:
            r0 = r31
            r7.emitLoad(r0)
            goto L_0x018f
        L_0x0354:
            r7.setUnreachable()
            goto L_0x01dd
        L_0x0359:
            r7.endFragment(r13)
            goto L_0x0216
        L_0x035e:
            gnu.bytecode.Method r32 = gnu.expr.Compilation.getLocationMethod
            r0 = r32
            r7.emitInvokeVirtual(r0)
            goto L_0x0216
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.Declaration.load(gnu.expr.AccessExp, int, gnu.expr.Compilation, gnu.expr.Target):void");
    }

    public void compileStore(Compilation comp) {
        CodeAttr code = comp.getCode();
        if (isSimple()) {
            code.emitStore(getVariable());
        } else if (!this.field.getStaticFlag()) {
            loadOwningObject((Declaration) null, comp);
            code.emitSwap();
            code.emitPutField(this.field);
        } else {
            code.emitPutStatic(this.field);
        }
    }

    public final Expression getValue() {
        if (this.value == QuoteExp.undefined_exp) {
            if (this.field != null && (this.field.getModifiers() & 24) == 24 && !isIndirectBinding()) {
                try {
                    this.value = new QuoteExp(this.field.getReflectField().get((Object) null));
                } catch (Throwable th) {
                }
            }
        } else if ((this.value instanceof QuoteExp) && getFlag(8192) && this.value.getType() != this.type) {
            try {
                Object val = ((QuoteExp) this.value).getValue();
                Type t = getType();
                this.value = new QuoteExp(t.coerceFromObject(val), t);
            } catch (Throwable th2) {
            }
        }
        return this.value;
    }

    public final void setValue(Expression value2) {
        this.value = value2;
    }

    public final Object getConstantValue() {
        Expression v = getValue();
        if (!(v instanceof QuoteExp) || v == QuoteExp.undefined_exp) {
            return null;
        }
        return ((QuoteExp) v).getValue();
    }

    public final boolean hasConstantValue() {
        Expression v = getValue();
        return (v instanceof QuoteExp) && v != QuoteExp.undefined_exp;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldEarlyInit() {
        return getFlag(536870912) || isCompiletimeConstant();
    }

    public boolean isCompiletimeConstant() {
        return getFlag(JSONzip.int14) && hasConstantValue();
    }

    public final boolean needsExternalAccess() {
        return (this.flags & 524320) == 524320 || (this.flags & 2097184) == 2097184;
    }

    public final boolean needsContext() {
        return this.base == null && this.field != null && !this.field.getStaticFlag();
    }

    public final boolean getFlag(long flag) {
        return (this.flags & flag) != 0;
    }

    public final void setFlag(boolean setting, long flag) {
        if (setting) {
            this.flags |= flag;
        } else {
            this.flags &= -1 ^ flag;
        }
    }

    public final void setFlag(long flag) {
        this.flags |= flag;
    }

    public final boolean isPublic() {
        return (this.context instanceof ModuleExp) && (this.flags & 32) == 0;
    }

    public final boolean isPrivate() {
        return (this.flags & 32) != 0;
    }

    public final void setPrivate(boolean isPrivate) {
        setFlag(isPrivate, 32);
    }

    public short getAccessFlags(short defaultFlags) {
        short flags2;
        if (getFlag(251658240)) {
            flags2 = 0;
            if (getFlag(16777216)) {
                flags2 = (short) 2;
            }
            if (getFlag(33554432)) {
                flags2 = (short) (flags2 | 4);
            }
            if (getFlag(67108864)) {
                flags2 = (short) (flags2 | 1);
            }
        } else {
            flags2 = defaultFlags;
        }
        if (getFlag(VOLATILE_ACCESS)) {
            flags2 = (short) (flags2 | 64);
        }
        if (getFlag(TRANSIENT_ACCESS)) {
            flags2 = (short) (flags2 | 128);
        }
        if (getFlag(ENUM_ACCESS)) {
            flags2 = (short) (flags2 | Access.ENUM);
        }
        if (getFlag(FINAL_ACCESS)) {
            return (short) (flags2 | 16);
        }
        return flags2;
    }

    public final boolean isAlias() {
        return (this.flags & 256) != 0;
    }

    public final void setAlias(boolean flag) {
        setFlag(flag, 256);
    }

    public final boolean isFluid() {
        return (this.flags & 16) != 0;
    }

    public final void setFluid(boolean fluid) {
        setFlag(fluid, 16);
    }

    public final boolean isProcedureDecl() {
        return (this.flags & 128) != 0;
    }

    public final void setProcedureDecl(boolean val) {
        setFlag(val, 128);
    }

    public final boolean isNamespaceDecl() {
        return (this.flags & 2097152) != 0;
    }

    public final boolean isIndirectBinding() {
        return (this.flags & 1) != 0;
    }

    public final void setIndirectBinding(boolean indirectBinding) {
        setFlag(indirectBinding, 1);
    }

    public void maybeIndirectBinding(Compilation comp) {
        if ((isLexical() && !(this.context instanceof ModuleExp)) || this.context == comp.mainLambda) {
            setIndirectBinding(true);
        }
    }

    public final boolean getCanRead() {
        return (this.flags & 2) != 0;
    }

    public final void setCanRead(boolean read) {
        setFlag(read, 2);
    }

    public final void setCanRead() {
        setFlag(true, 2);
        if (this.base != null) {
            this.base.setCanRead();
        }
    }

    public final boolean getCanCall() {
        return (this.flags & 4) != 0;
    }

    public final void setCanCall(boolean called) {
        setFlag(called, 4);
    }

    public final void setCanCall() {
        setFlag(true, 4);
        if (this.base != null) {
            this.base.setCanRead();
        }
    }

    public final boolean getCanWrite() {
        return (this.flags & 8) != 0;
    }

    public final void setCanWrite(boolean written) {
        if (written) {
            this.flags |= 8;
        } else {
            this.flags &= -9;
        }
    }

    public final void setCanWrite() {
        this.flags |= 8;
        if (this.base != null) {
            this.base.setCanRead();
        }
    }

    public final boolean isThisParameter() {
        return this.symbol == ThisExp.THIS_NAME;
    }

    public boolean ignorable() {
        if (getCanRead() || isPublic()) {
            return false;
        }
        if (getCanWrite() && getFlag(65536)) {
            return false;
        }
        if (!getCanCall()) {
            return true;
        }
        Expression value2 = getValue();
        if (value2 == null || !(value2 instanceof LambdaExp)) {
            return false;
        }
        LambdaExp lexp = (LambdaExp) value2;
        if (!lexp.isHandlingTailCalls() || lexp.getInlineOnly()) {
            return true;
        }
        return false;
    }

    public boolean needsInit() {
        return !ignorable() && (this.value != QuoteExp.nullExp || this.base == null);
    }

    public boolean isStatic() {
        if (this.field != null) {
            return this.field.getStaticFlag();
        }
        if (getFlag(2048) || isCompiletimeConstant()) {
            return true;
        }
        if (getFlag(4096)) {
            return false;
        }
        LambdaExp lambda = this.context.currentLambda();
        if (!(lambda instanceof ModuleExp) || !((ModuleExp) lambda).isStatic()) {
            return false;
        }
        return true;
    }

    public final boolean isLexical() {
        return (this.flags & 268501008) == 0;
    }

    public static final boolean isUnknown(Declaration decl) {
        return decl == null || decl.getFlag(65536);
    }

    public void noteValue(Expression value2) {
        if (this.value == QuoteExp.undefined_exp) {
            if (value2 instanceof LambdaExp) {
                ((LambdaExp) value2).nameDecl = this;
            }
            this.value = value2;
        } else if (this.value != value2) {
            if (this.value instanceof LambdaExp) {
                ((LambdaExp) this.value).nameDecl = null;
            }
            this.value = null;
        }
    }

    protected Declaration() {
        int i = counter + 1;
        counter = i;
        this.id = i;
        this.value = QuoteExp.undefined_exp;
        this.flags = 64;
        this.makeLocationMethod = null;
    }

    public Declaration(Variable var2) {
        this((Object) var2.getName(), var2.getType());
        this.var = var2;
    }

    public Declaration(Object name) {
        int i = counter + 1;
        counter = i;
        this.id = i;
        this.value = QuoteExp.undefined_exp;
        this.flags = 64;
        this.makeLocationMethod = null;
        setName(name);
    }

    public Declaration(Object name, Type type2) {
        int i = counter + 1;
        counter = i;
        this.id = i;
        this.value = QuoteExp.undefined_exp;
        this.flags = 64;
        this.makeLocationMethod = null;
        setName(name);
        setType(type2);
    }

    public Declaration(Object name, Field field2) {
        this(name, field2.getType());
        this.field = field2;
        setSimple(false);
    }

    public void pushIndirectBinding(Compilation comp) {
        CodeAttr code = comp.getCode();
        code.emitPushString(getName());
        if (this.makeLocationMethod == null) {
            this.makeLocationMethod = Compilation.typeLocation.addMethod("make", new Type[]{Type.pointer_type, Type.string_type}, (Type) Compilation.typeLocation, 9);
        }
        code.emitInvokeStatic(this.makeLocationMethod);
    }

    public final Variable allocateVariable(CodeAttr code) {
        if (!isSimple() || this.var == null) {
            String vname = null;
            if (this.symbol != null) {
                vname = Compilation.mangleNameIfNeeded(getName());
            }
            if (!isAlias() || !(getValue() instanceof ReferenceExp)) {
                this.var = this.context.getVarScope().addVariable(code, isIndirectBinding() ? Compilation.typeLocation : getType().getImplementationType(), vname);
            } else {
                Declaration base2 = followAliases(this);
                this.var = base2 == null ? null : base2.var;
            }
        }
        return this.var;
    }

    public final void setLocation(SourceLocator location) {
        this.filename = location.getFileName();
        setLine(location.getLineNumber(), location.getColumnNumber());
    }

    public final void setFile(String filename2) {
        this.filename = filename2;
    }

    public final void setLine(int lineno, int colno) {
        if (lineno < 0) {
            lineno = 0;
        }
        if (colno < 0) {
            colno = 0;
        }
        this.position = (lineno << 12) + colno;
    }

    public final void setLine(int lineno) {
        setLine(lineno, 0);
    }

    public final String getFileName() {
        return this.filename;
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return this.filename;
    }

    public final int getLineNumber() {
        int line = this.position >> 12;
        if (line == 0) {
            return -1;
        }
        return line;
    }

    public final int getColumnNumber() {
        int column = this.position & 4095;
        if (column == 0) {
            return -1;
        }
        return column;
    }

    public boolean isStableSourceLocation() {
        return true;
    }

    public void printInfo(OutPort out) {
        StringBuffer sbuf = new StringBuffer();
        printInfo(sbuf);
        out.print(sbuf.toString());
    }

    public void printInfo(StringBuffer sbuf) {
        sbuf.append(this.symbol);
        sbuf.append('/');
        sbuf.append(this.id);
        sbuf.append("/fl:");
        sbuf.append(Long.toHexString(this.flags));
        if (ignorable()) {
            sbuf.append("(ignorable)");
        }
        Expression tx = this.typeExp;
        Type t = getType();
        if (tx != null && !(tx instanceof QuoteExp)) {
            sbuf.append("::");
            sbuf.append(tx);
        } else if (!(this.type == null || t == Type.pointer_type)) {
            sbuf.append("::");
            sbuf.append(t.getName());
        }
        if (this.base != null) {
            sbuf.append("(base:#");
            sbuf.append(this.base.id);
            sbuf.append(')');
        }
    }

    public String toString() {
        return "Declaration[" + this.symbol + '/' + this.id + ']';
    }

    public static Declaration followAliases(Declaration decl) {
        Declaration orig;
        while (decl != null && decl.isAlias()) {
            Expression declValue = decl.getValue();
            if (!(declValue instanceof ReferenceExp) || (orig = ((ReferenceExp) declValue).binding) == null) {
                break;
            }
            decl = orig;
        }
        return decl;
    }

    public void makeField(Compilation comp, Expression value2) {
        setSimple(false);
        makeField(comp.mainClass, comp, value2);
    }

    public void makeField(ClassType frameType, Compilation comp, Expression value2) {
        String fname;
        int nlength;
        boolean external_access = needsExternalAccess();
        int fflags = 0;
        boolean isConstant = getFlag(JSONzip.int14);
        boolean typeSpecified = getFlag(8192);
        if (comp.immediate && (this.context instanceof ModuleExp) && !isConstant && !typeSpecified) {
            setIndirectBinding(true);
        }
        if (isPublic() || external_access || comp.immediate) {
            fflags = 0 | 1;
        }
        if (isStatic() || ((getFlag(268501008) && isIndirectBinding() && !isAlias()) || ((value2 instanceof ClassExp) && !((LambdaExp) value2).getNeedsClosureEnv()))) {
            fflags |= 8;
        }
        if ((isIndirectBinding() || (isConstant && (shouldEarlyInit() || ((this.context instanceof ModuleExp) && ((ModuleExp) this.context).staticInitRun())))) && ((this.context instanceof ClassExp) || (this.context instanceof ModuleExp))) {
            fflags |= 16;
        }
        Type ftype = getType().getImplementationType();
        if (isIndirectBinding() && !ftype.isSubtype(Compilation.typeLocation)) {
            ftype = Compilation.typeLocation;
        }
        if (!ignorable()) {
            String fname2 = getName();
            if (fname2 == null) {
                fname = "$unnamed$0";
                nlength = fname.length() - 2;
            } else {
                fname = Compilation.mangleNameIfNeeded(fname2);
                if (getFlag(65536)) {
                    fname = UNKNOWN_PREFIX + fname;
                }
                if (external_access && !getFlag(1073741824)) {
                    fname = PRIVATE_PREFIX + fname;
                }
                nlength = fname.length();
            }
            int counter2 = 0;
            while (frameType.getDeclaredField(fname) != null) {
                counter2++;
                fname = fname.substring(0, nlength) + '$' + counter2;
            }
            this.field = frameType.addField(fname, ftype, fflags);
            if (value2 instanceof QuoteExp) {
                Object val = ((QuoteExp) value2).getValue();
                if (this.field.getStaticFlag() && val.getClass().getName().equals(ftype.getName())) {
                    Literal literal = comp.litTable.findLiteral(val);
                    if (literal.field == null) {
                        literal.assign(this.field, comp.litTable);
                    }
                } else if ((ftype instanceof PrimType) || "java.lang.String".equals(ftype.getName())) {
                    if (val instanceof Char) {
                        val = IntNum.make(((Char) val).intValue());
                    }
                    this.field.setConstantValue(val, frameType);
                    return;
                }
            }
        }
        if (shouldEarlyInit()) {
            return;
        }
        if (isIndirectBinding() || (value2 != null && !(value2 instanceof ClassExp))) {
            BindingInitializer.create(this, value2, comp);
        }
    }

    /* access modifiers changed from: package-private */
    public Location makeIndirectLocationFor() {
        return Location.make(this.symbol instanceof Symbol ? (Symbol) this.symbol : Namespace.EmptyNamespace.getSymbol(this.symbol.toString().intern()));
    }

    public static Declaration getDeclarationFromStatic(String cname, String fname) {
        Declaration decl = new Declaration((Object) fname, ClassType.make(cname).getDeclaredField(fname));
        decl.setFlag(18432);
        return decl;
    }

    public static Declaration getDeclarationValueFromStatic(String className, String fieldName, String name) {
        try {
            Object value2 = Class.forName(className).getDeclaredField(fieldName).get((Object) null);
            Declaration decl = new Declaration((Object) name, ClassType.make(className).getDeclaredField(fieldName));
            decl.noteValue(new QuoteExp(value2));
            decl.setFlag(18432);
            return decl;
        } catch (Exception ex) {
            throw new WrappedException((Throwable) ex);
        }
    }

    public static Declaration getDeclaration(Named proc) {
        return getDeclaration(proc, proc.getName());
    }

    public static Declaration getDeclaration(Object proc, String name) {
        Class procClass;
        Field procField = null;
        if (!(name == null || (procClass = PrimProcedure.getProcedureClass(proc)) == null)) {
            procField = ((ClassType) Type.make(procClass)).getDeclaredField(Compilation.mangleNameIfNeeded(name));
        }
        if (procField != null) {
            int fflags = procField.getModifiers();
            if ((fflags & 8) != 0) {
                Declaration decl = new Declaration((Object) name, procField);
                decl.noteValue(new QuoteExp(proc));
                if ((fflags & 16) == 0) {
                    return decl;
                }
                decl.setFlag(JSONzip.int14);
                return decl;
            }
        }
        return null;
    }
}
