package gnu.kawa.reflect;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Field;
import gnu.bytecode.Member;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.expr.CheckedTarget;
import gnu.expr.ClassExp;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.Inlineable;
import gnu.expr.Language;
import gnu.mapping.Procedure;
import gnu.mapping.Procedure3;
import gnu.mapping.Values;

public class SlotSet extends Procedure3 implements Inlineable {
    public static final SlotSet set$Mnfield$Ex = new SlotSet("set-field!", false);
    public static final SlotSet set$Mnstatic$Mnfield$Ex = new SlotSet("set-static-field!", true);
    public static final SlotSet setFieldReturnObject = new SlotSet("set-field-return-object!", false);
    static final Type[] type1Array = new Type[1];
    boolean isStatic;
    boolean returnSelf;

    static {
        setFieldReturnObject.returnSelf = true;
    }

    public SlotSet(String name, boolean isStatic2) {
        super(name);
        this.isStatic = isStatic2;
        setProperty(Procedure.validateApplyKey, "gnu.kawa.reflect.CompileReflect:validateApplySlotSet");
    }

    public static void setField(Object obj, String name, Object value) {
        apply(false, obj, name, value);
    }

    public static void setStaticField(Object obj, String name, Object value) {
        apply(true, obj, name, value);
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x012a  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0145  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void apply(boolean r21, java.lang.Object r22, java.lang.Object r23, java.lang.Object r24) {
        /*
            gnu.expr.Language r13 = gnu.expr.Language.getDefaultLanguage()
            r12 = 0
            r0 = r23
            boolean r0 = r0 instanceof java.lang.String
            r18 = r0
            if (r18 != 0) goto L_0x001d
            r0 = r23
            boolean r0 = r0 instanceof gnu.lists.FString
            r18 = r0
            if (r18 != 0) goto L_0x001d
            r0 = r23
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r18 = r0
            if (r18 == 0) goto L_0x0054
        L_0x001d:
            java.lang.String r14 = r23.toString()
            java.lang.String r6 = gnu.expr.Compilation.mangleNameIfNeeded(r14)
            if (r21 == 0) goto L_0x004f
            java.lang.Class r3 = gnu.kawa.reflect.SlotGet.coerceToClass(r22)
        L_0x002b:
            r0 = r23
            boolean r0 = r0 instanceof gnu.bytecode.Field     // Catch:{ NoSuchFieldException -> 0x0170, IllegalAccessException -> 0x0064 }
            r18 = r0
            if (r18 == 0) goto L_0x005f
            r0 = r23
            gnu.bytecode.Field r0 = (gnu.bytecode.Field) r0     // Catch:{ NoSuchFieldException -> 0x0170, IllegalAccessException -> 0x0064 }
            r18 = r0
            java.lang.reflect.Field r5 = r18.getReflectField()     // Catch:{ NoSuchFieldException -> 0x0170, IllegalAccessException -> 0x0064 }
        L_0x003d:
            java.lang.Class r7 = r5.getType()     // Catch:{ NoSuchFieldException -> 0x0170, IllegalAccessException -> 0x0064 }
            r0 = r24
            java.lang.Object r18 = r13.coerceFromObject(r7, r0)     // Catch:{ NoSuchFieldException -> 0x0170, IllegalAccessException -> 0x0064 }
            r0 = r22
            r1 = r18
            r5.set(r0, r1)     // Catch:{ NoSuchFieldException -> 0x0170, IllegalAccessException -> 0x0064 }
        L_0x004e:
            return
        L_0x004f:
            java.lang.Class r3 = r22.getClass()
            goto L_0x002b
        L_0x0054:
            r18 = r23
            gnu.bytecode.Member r18 = (gnu.bytecode.Member) r18
            java.lang.String r14 = r18.getName()
            r6 = r14
            r3 = 0
            goto L_0x002b
        L_0x005f:
            java.lang.reflect.Field r5 = r3.getField(r6)     // Catch:{ NoSuchFieldException -> 0x0170, IllegalAccessException -> 0x0064 }
            goto L_0x003d
        L_0x0064:
            r4 = move-exception
            r12 = 1
        L_0x0066:
            r10 = 0
            r0 = r23
            boolean r11 = r0 instanceof gnu.bytecode.Method     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            if (r11 == 0) goto L_0x00e2
            r16 = r6
        L_0x006f:
            if (r11 == 0) goto L_0x007e
            java.lang.String r18 = "set"
            r0 = r16
            r1 = r18
            boolean r18 = r0.startsWith(r1)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            if (r18 != 0) goto L_0x007e
            r11 = 0
        L_0x007e:
            if (r11 == 0) goto L_0x00eb
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f4 }
            r18.<init>()     // Catch:{ Exception -> 0x00f4 }
            java.lang.String r19 = "get"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x00f4 }
            r19 = 3
            r0 = r16
            r1 = r19
            java.lang.String r19 = r0.substring(r1)     // Catch:{ Exception -> 0x00f4 }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ Exception -> 0x00f4 }
            java.lang.String r9 = r18.toString()     // Catch:{ Exception -> 0x00f4 }
        L_0x009d:
            java.lang.Class[] r18 = gnu.kawa.reflect.SlotGet.noClasses     // Catch:{ Exception -> 0x00f4 }
            r0 = r18
            java.lang.reflect.Method r10 = r3.getMethod(r9, r0)     // Catch:{ Exception -> 0x00f4 }
        L_0x00a5:
            r18 = 1
            r0 = r18
            java.lang.Class[] r15 = new java.lang.Class[r0]     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r18 = 0
            java.lang.Class r19 = r10.getReturnType()     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r15[r18] = r19     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r0 = r16
            java.lang.reflect.Method r17 = r3.getMethod(r0, r15)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r18 = 1
            r0 = r18
            java.lang.Object[] r2 = new java.lang.Object[r0]     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r18 = 0
            r19 = 0
            r19 = r15[r19]     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r0 = r19
            r1 = r24
            java.lang.Object r19 = r13.coerceFromObject(r0, r1)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r2[r18] = r19     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r0 = r17
            r1 = r22
            r0.invoke(r1, r2)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            goto L_0x004e
        L_0x00d8:
            r4 = move-exception
            java.lang.Throwable r18 = r4.getTargetException()
            java.lang.RuntimeException r18 = gnu.mapping.WrappedException.wrapIfNeeded(r18)
            throw r18
        L_0x00e2:
            java.lang.String r18 = "set"
            r0 = r18
            java.lang.String r16 = gnu.expr.ClassExp.slotToMethodName(r0, r14)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            goto L_0x006f
        L_0x00eb:
            java.lang.String r18 = "get"
            r0 = r18
            java.lang.String r9 = gnu.expr.ClassExp.slotToMethodName(r0, r14)     // Catch:{ Exception -> 0x00f4 }
            goto L_0x009d
        L_0x00f4:
            r8 = move-exception
            if (r11 == 0) goto L_0x011d
            java.lang.StringBuilder r18 = new java.lang.StringBuilder     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r18.<init>()     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            java.lang.String r19 = "is"
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r19 = 3
            r0 = r16
            r1 = r19
            java.lang.String r19 = r0.substring(r1)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            java.lang.StringBuilder r18 = r18.append(r19)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            java.lang.String r9 = r18.toString()     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
        L_0x0114:
            java.lang.Class[] r18 = gnu.kawa.reflect.SlotGet.noClasses     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            r0 = r18
            java.lang.reflect.Method r10 = r3.getMethod(r9, r0)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            goto L_0x00a5
        L_0x011d:
            java.lang.String r18 = "is"
            r0 = r18
            java.lang.String r9 = gnu.expr.ClassExp.slotToMethodName(r0, r14)     // Catch:{ InvocationTargetException -> 0x00d8, IllegalAccessException -> 0x0126, NoSuchMethodException -> 0x016e }
            goto L_0x0114
        L_0x0126:
            r4 = move-exception
            r12 = 1
        L_0x0128:
            if (r12 == 0) goto L_0x0145
            java.lang.RuntimeException r18 = new java.lang.RuntimeException
            java.lang.StringBuilder r19 = new java.lang.StringBuilder
            r19.<init>()
            java.lang.String r20 = "illegal access for field "
            java.lang.StringBuilder r19 = r19.append(r20)
            r0 = r19
            java.lang.StringBuilder r19 = r0.append(r14)
            java.lang.String r19 = r19.toString()
            r18.<init>(r19)
            throw r18
        L_0x0145:
            java.lang.RuntimeException r18 = new java.lang.RuntimeException
            java.lang.StringBuilder r19 = new java.lang.StringBuilder
            r19.<init>()
            java.lang.String r20 = "no such field "
            java.lang.StringBuilder r19 = r19.append(r20)
            r0 = r19
            java.lang.StringBuilder r19 = r0.append(r14)
            java.lang.String r20 = " in "
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r20 = r3.getName()
            java.lang.StringBuilder r19 = r19.append(r20)
            java.lang.String r19 = r19.toString()
            r18.<init>(r19)
            throw r18
        L_0x016e:
            r18 = move-exception
            goto L_0x0128
        L_0x0170:
            r18 = move-exception
            goto L_0x0066
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.reflect.SlotSet.apply(boolean, java.lang.Object, java.lang.Object, java.lang.Object):void");
    }

    public Object apply3(Object obj, Object fname, Object value) {
        apply(this.isStatic, obj, fname, value);
        return this.returnSelf ? obj : Values.empty;
    }

    public static Member lookupMember(ObjectType clas, String name, ClassType caller) {
        Field field = clas.getField(Compilation.mangleNameIfNeeded(name), -1);
        if (field != null) {
            if (caller == null) {
                caller = Type.pointer_type;
            }
            if (caller.isAccessible(field, clas)) {
                return field;
            }
        }
        Method method = clas.getMethod(ClassExp.slotToMethodName("set", name), type1Array);
        if (method != null) {
            return method;
        }
        return field;
    }

    static void compileSet(Procedure thisProc, ObjectType ctype, Expression valArg, Object part, Compilation comp) {
        CodeAttr code = comp.getCode();
        Language language = comp.getLanguage();
        boolean isStatic2 = (thisProc instanceof SlotSet) && ((SlotSet) thisProc).isStatic;
        if (part instanceof Field) {
            Field field = (Field) part;
            boolean isStaticField = field.getStaticFlag();
            Type ftype = language.getLangTypeFor(field.getType());
            if (isStatic2 && !isStaticField) {
                comp.error('e', "cannot access non-static field `" + field.getName() + "' using `" + thisProc.getName() + '\'');
            }
            valArg.compile(comp, CheckedTarget.getInstance(ftype));
            if (isStaticField) {
                code.emitPutStatic(field);
            } else {
                code.emitPutField(field);
            }
        } else if (part instanceof Method) {
            Method method = (Method) part;
            boolean isStaticMethod = method.getStaticFlag();
            if (isStatic2 && !isStaticMethod) {
                comp.error('e', "cannot call non-static getter method `" + method.getName() + "' using `" + thisProc.getName() + '\'');
            }
            valArg.compile(comp, CheckedTarget.getInstance(language.getLangTypeFor(method.getParameterTypes()[0])));
            if (isStaticMethod) {
                code.emitInvokeStatic(method);
            } else {
                code.emitInvoke(method);
            }
            if (!method.getReturnType().isVoid()) {
                code.emitPop(1);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v3, resolved type: gnu.bytecode.Member} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void compile(gnu.expr.ApplyExp r21, gnu.expr.Compilation r22, gnu.expr.Target r23) {
        /*
            r20 = this;
            gnu.expr.Expression[] r5 = r21.getArgs()
            int r12 = r5.length
            r17 = 3
            r0 = r17
            if (r12 == r0) goto L_0x0050
            r17 = 3
            r0 = r17
            if (r12 >= r0) goto L_0x004d
            java.lang.String r10 = "too few"
        L_0x0013:
            r17 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r10)
            java.lang.String r19 = " arguments to `"
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r19 = r20.getName()
            java.lang.StringBuilder r18 = r18.append(r19)
            r19 = 39
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r18 = r18.toString()
            r0 = r22
            r1 = r17
            r2 = r18
            r0.error(r1, r2)
            r17 = 0
            r0 = r22
            r1 = r17
            r2 = r23
            r0.compileConstant(r1, r2)
        L_0x004c:
            return
        L_0x004d:
            java.lang.String r10 = "too many"
            goto L_0x0013
        L_0x0050:
            r17 = 0
            r3 = r5[r17]
            r17 = 1
            r4 = r5[r17]
            r17 = 2
            r16 = r5[r17]
            r0 = r20
            boolean r0 = r0.isStatic
            r17 = r0
            if (r17 == 0) goto L_0x016d
            gnu.bytecode.Type r14 = kawa.standard.Scheme.exp2Type(r3)
        L_0x0068:
            r13 = 0
            boolean r0 = r14 instanceof gnu.bytecode.ObjectType
            r17 = r0
            if (r17 == 0) goto L_0x01a0
            boolean r0 = r4 instanceof gnu.expr.QuoteExp
            r17 = r0
            if (r17 == 0) goto L_0x01a0
            gnu.expr.QuoteExp r4 = (gnu.expr.QuoteExp) r4
            java.lang.Object r15 = r4.getValue()
            r7 = r14
            gnu.bytecode.ObjectType r7 = (gnu.bytecode.ObjectType) r7
            r0 = r22
            gnu.bytecode.ClassType r0 = r0.curClass
            r17 = r0
            if (r17 == 0) goto L_0x0173
            r0 = r22
            gnu.bytecode.ClassType r6 = r0.curClass
        L_0x008a:
            boolean r0 = r15 instanceof java.lang.String
            r17 = r0
            if (r17 != 0) goto L_0x009c
            boolean r0 = r15 instanceof gnu.lists.FString
            r17 = r0
            if (r17 != 0) goto L_0x009c
            boolean r0 = r15 instanceof gnu.mapping.Symbol
            r17 = r0
            if (r17 == 0) goto L_0x0179
        L_0x009c:
            java.lang.String r11 = r15.toString()
            gnu.bytecode.Member r13 = lookupMember(r7, r11, r6)
            if (r13 != 0) goto L_0x00e0
            gnu.bytecode.ClassType r17 = gnu.bytecode.Type.pointer_type
            r0 = r17
            if (r14 == r0) goto L_0x00e0
            boolean r17 = r22.warnUnknownMember()
            if (r17 == 0) goto L_0x00e0
            r17 = 119(0x77, float:1.67E-43)
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "no slot `"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r11)
            java.lang.String r19 = "' in "
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r19 = r7.getName()
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r18 = r18.toString()
            r0 = r22
            r1 = r17
            r2 = r18
            r0.error(r1, r2)
        L_0x00e0:
            if (r13 == 0) goto L_0x01a0
            int r9 = r13.getModifiers()
            r17 = r9 & 8
            if (r17 == 0) goto L_0x018b
            r8 = 1
        L_0x00eb:
            if (r6 == 0) goto L_0x012b
            boolean r17 = r6.isAccessible(r13, r7)
            if (r17 != 0) goto L_0x012b
            r17 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r18 = new java.lang.StringBuilder
            r18.<init>()
            java.lang.String r19 = "slot '"
            java.lang.StringBuilder r18 = r18.append(r19)
            r0 = r18
            java.lang.StringBuilder r18 = r0.append(r11)
            java.lang.String r19 = "' in "
            java.lang.StringBuilder r18 = r18.append(r19)
            gnu.bytecode.ClassType r19 = r13.getDeclaringClass()
            java.lang.String r19 = r19.getName()
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r19 = " not accessible here"
            java.lang.StringBuilder r18 = r18.append(r19)
            java.lang.String r18 = r18.toString()
            r0 = r22
            r1 = r17
            r2 = r18
            r0.error(r1, r2)
        L_0x012b:
            r17 = 0
            r18 = r5[r17]
            if (r8 == 0) goto L_0x018e
            gnu.expr.Target r17 = gnu.expr.Target.Ignore
        L_0x0133:
            r0 = r18
            r1 = r22
            r2 = r17
            r0.compile((gnu.expr.Compilation) r1, (gnu.expr.Target) r2)
            r0 = r20
            boolean r0 = r0.returnSelf
            r17 = r0
            if (r17 == 0) goto L_0x014f
            gnu.bytecode.CodeAttr r17 = r22.getCode()
            gnu.bytecode.Type r18 = r7.getImplementationType()
            r17.emitDup((gnu.bytecode.Type) r18)
        L_0x014f:
            r17 = 2
            r17 = r5[r17]
            r0 = r20
            r1 = r17
            r2 = r22
            compileSet(r0, r7, r1, r13, r2)
            r0 = r20
            boolean r0 = r0.returnSelf
            r17 = r0
            if (r17 == 0) goto L_0x0193
            r0 = r23
            r1 = r22
            r0.compileFromStack(r1, r7)
            goto L_0x004c
        L_0x016d:
            gnu.bytecode.Type r14 = r3.getType()
            goto L_0x0068
        L_0x0173:
            r0 = r22
            gnu.bytecode.ClassType r6 = r0.mainClass
            goto L_0x008a
        L_0x0179:
            boolean r0 = r15 instanceof gnu.bytecode.Member
            r17 = r0
            if (r17 == 0) goto L_0x0188
            r13 = r15
            gnu.bytecode.Member r13 = (gnu.bytecode.Member) r13
            java.lang.String r11 = r13.getName()
            goto L_0x00e0
        L_0x0188:
            r11 = 0
            goto L_0x00e0
        L_0x018b:
            r8 = 0
            goto L_0x00eb
        L_0x018e:
            gnu.expr.Target r17 = gnu.expr.Target.pushValue(r7)
            goto L_0x0133
        L_0x0193:
            gnu.mapping.Values r17 = gnu.mapping.Values.empty
            r0 = r22
            r1 = r17
            r2 = r23
            r0.compileConstant(r1, r2)
            goto L_0x004c
        L_0x01a0:
            gnu.expr.ApplyExp.compile(r21, r22, r23)
            goto L_0x004c
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.reflect.SlotSet.compile(gnu.expr.ApplyExp, gnu.expr.Compilation, gnu.expr.Target):void");
    }
}
