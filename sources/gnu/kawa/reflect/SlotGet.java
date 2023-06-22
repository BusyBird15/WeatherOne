package gnu.kawa.reflect;

import com.google.appinventor.components.common.PropertyTypeConstants;
import gnu.bytecode.ArrayType;
import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Field;
import gnu.bytecode.Member;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.ClassExp;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.Inlineable;
import gnu.expr.Language;
import gnu.expr.QuoteExp;
import gnu.expr.Target;
import gnu.kawa.lispexpr.LangPrimType;
import gnu.mapping.HasSetter;
import gnu.mapping.Procedure;
import gnu.mapping.Procedure2;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.WrongArguments;
import gnu.mapping.WrongType;

public class SlotGet extends Procedure2 implements HasSetter, Inlineable {
    public static final SlotGet field = new SlotGet("field", false, SlotSet.set$Mnfield$Ex);
    static Class[] noClasses = new Class[0];
    public static final SlotGet slotRef = new SlotGet("slot-ref", false, SlotSet.set$Mnfield$Ex);
    public static final SlotGet staticField = new SlotGet("static-field", true, SlotSet.set$Mnstatic$Mnfield$Ex);
    boolean isStatic;
    Procedure setter;

    public SlotGet(String name, boolean isStatic2) {
        super(name);
        this.isStatic = isStatic2;
        setProperty(Procedure.validateApplyKey, "gnu.kawa.reflect.CompileReflect:validateApplySlotGet");
    }

    public SlotGet(String name, boolean isStatic2, Procedure setter2) {
        this(name, isStatic2);
        this.setter = setter2;
    }

    public static Object field(Object obj, String fname) {
        return field.apply2(obj, fname);
    }

    public static Object staticField(Object obj, String fname) {
        return staticField.apply2(obj, fname);
    }

    public Object apply2(Object arg1, Object arg2) {
        String name;
        String fname;
        String getName = null;
        String isName = null;
        if (arg2 instanceof Field) {
            fname = ((Field) arg2).getName();
            name = Compilation.demangleName(fname, true);
        } else if (arg2 instanceof Method) {
            String mname = ((Method) arg2).getName();
            name = Compilation.demangleName(mname, false);
            if (mname.startsWith("get")) {
                getName = mname;
            } else if (mname.startsWith("is")) {
                isName = mname;
            }
            fname = null;
        } else if ((arg2 instanceof SimpleSymbol) || (arg2 instanceof CharSequence)) {
            name = arg2.toString();
            fname = Compilation.mangleNameIfNeeded(name);
        } else {
            throw new WrongType((Procedure) this, 2, arg2, PropertyTypeConstants.PROPERTY_TYPE_STRING);
        }
        if ("class".equals(fname)) {
            fname = "class";
        } else if (PropertyTypeConstants.PROPERTY_TYPE_LENGTH.equals(fname)) {
            fname = PropertyTypeConstants.PROPERTY_TYPE_LENGTH;
        }
        return getSlotValue(this.isStatic, arg1, name, fname, getName, isName, Language.getDefaultLanguage());
    }

    /* JADX WARNING: Removed duplicated region for block: B:57:0x00e5  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0100  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object getSlotValue(boolean r13, java.lang.Object r14, java.lang.String r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, gnu.expr.Language r19) {
        /*
            if (r13 == 0) goto L_0x001b
            java.lang.Class r1 = coerceToClass(r14)
        L_0x0006:
            java.lang.String r10 = "length"
            r0 = r16
            if (r0 != r10) goto L_0x0020
            boolean r10 = r1.isArray()
            if (r10 == 0) goto L_0x0020
            int r7 = java.lang.reflect.Array.getLength(r14)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r7)
        L_0x001a:
            return r1
        L_0x001b:
            java.lang.Class r1 = r14.getClass()
            goto L_0x0006
        L_0x0020:
            java.lang.String r10 = "class"
            r0 = r16
            if (r0 == r10) goto L_0x001a
            r6 = 0
            if (r16 == 0) goto L_0x0070
            r0 = r16
            java.lang.reflect.Field r3 = r1.getField(r0)     // Catch:{ Exception -> 0x005c }
        L_0x002f:
            if (r3 == 0) goto L_0x0070
            if (r13 == 0) goto L_0x005f
            int r10 = r3.getModifiers()
            r10 = r10 & 8
            if (r10 != 0) goto L_0x005f
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "cannot access non-static field '"
            java.lang.StringBuilder r11 = r11.append(r12)
            r0 = r16
            java.lang.StringBuilder r11 = r11.append(r0)
            r12 = 39
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        L_0x005c:
            r2 = move-exception
            r3 = 0
            goto L_0x002f
        L_0x005f:
            java.lang.Class r10 = r3.getType()     // Catch:{ IllegalAccessException -> 0x006e, Exception -> 0x00af }
            java.lang.Object r11 = r3.get(r14)     // Catch:{ IllegalAccessException -> 0x006e, Exception -> 0x00af }
            r0 = r19
            java.lang.Object r1 = r0.coerceToObject(r10, r11)     // Catch:{ IllegalAccessException -> 0x006e, Exception -> 0x00af }
            goto L_0x001a
        L_0x006e:
            r2 = move-exception
            r6 = 1
        L_0x0070:
            r8 = 0
            r5 = 0
            if (r17 == 0) goto L_0x00b4
            r8 = r17
        L_0x0076:
            java.lang.Class[] r10 = noClasses     // Catch:{ Exception -> 0x00bb }
            java.lang.reflect.Method r5 = r1.getMethod(r8, r10)     // Catch:{ Exception -> 0x00bb }
        L_0x007c:
            if (r13 == 0) goto L_0x00ce
            int r10 = r5.getModifiers()     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            r10 = r10 & 8
            if (r10 != 0) goto L_0x00ce
            java.lang.RuntimeException r10 = new java.lang.RuntimeException     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            r11.<init>()     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            java.lang.String r12 = "cannot call non-static getter method '"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            java.lang.StringBuilder r11 = r11.append(r8)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            r12 = 39
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            java.lang.String r11 = r11.toString()     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            r10.<init>(r11)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            throw r10     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
        L_0x00a5:
            r2 = move-exception
            java.lang.Throwable r10 = r2.getTargetException()
            java.lang.RuntimeException r10 = gnu.mapping.WrappedException.wrapIfNeeded(r10)
            throw r10
        L_0x00af:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0070
        L_0x00b4:
            java.lang.String r10 = "get"
            java.lang.String r8 = gnu.expr.ClassExp.slotToMethodName(r10, r15)     // Catch:{ Exception -> 0x00bb }
            goto L_0x0076
        L_0x00bb:
            r4 = move-exception
            if (r18 == 0) goto L_0x00c7
            r8 = r18
        L_0x00c0:
            java.lang.Class[] r10 = noClasses     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            java.lang.reflect.Method r5 = r1.getMethod(r8, r10)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            goto L_0x007c
        L_0x00c7:
            java.lang.String r10 = "is"
            java.lang.String r8 = gnu.expr.ClassExp.slotToMethodName(r10, r15)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            goto L_0x00c0
        L_0x00ce:
            java.lang.Object[] r10 = gnu.mapping.Values.noArgs     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            java.lang.Object r9 = r5.invoke(r14, r10)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            java.lang.Class r10 = r5.getReturnType()     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            r0 = r19
            java.lang.Object r9 = r0.coerceToObject(r10, r9)     // Catch:{ InvocationTargetException -> 0x00a5, IllegalAccessException -> 0x00e1, NoSuchMethodException -> 0x0129 }
            r1 = r9
            goto L_0x001a
        L_0x00e1:
            r2 = move-exception
            r6 = 1
        L_0x00e3:
            if (r6 == 0) goto L_0x0100
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "illegal access for field "
            java.lang.StringBuilder r11 = r11.append(r12)
            r0 = r16
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        L_0x0100:
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "no such field "
            java.lang.StringBuilder r11 = r11.append(r12)
            r0 = r16
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r12 = " in "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = r1.getName()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        L_0x0129:
            r10 = move-exception
            goto L_0x00e3
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.reflect.SlotGet.getSlotValue(boolean, java.lang.Object, java.lang.String, java.lang.String, java.lang.String, java.lang.String, gnu.expr.Language):java.lang.Object");
    }

    static Class coerceToClass(Object obj) {
        if (obj instanceof Class) {
            return (Class) obj;
        }
        if (obj instanceof Type) {
            return ((Type) obj).getReflectClass();
        }
        throw new RuntimeException("argument is neither Class nor Type");
    }

    public void setN(Object[] args) {
        int nargs = args.length;
        if (nargs != 3) {
            throw new WrongArguments(getSetter(), nargs);
        }
        set2(args[0], args[1], args[2]);
    }

    public void set2(Object obj, Object name, Object value) {
        SlotSet.apply(this.isStatic, obj, (String) name, value);
    }

    public static Member lookupMember(ObjectType clas, String name, ClassType caller) {
        Field field2 = clas.getField(Compilation.mangleNameIfNeeded(name), -1);
        if (field2 != null) {
            if (caller == null) {
                caller = Type.pointer_type;
            }
            if (caller.isAccessible(field2, clas)) {
                return field2;
            }
        }
        Method method = clas.getMethod(ClassExp.slotToMethodName("get", name), Type.typeArray0);
        if (method != null) {
            return method;
        }
        return field2;
    }

    public void compile(ApplyExp exp, Compilation comp, Target target) {
        Expression[] args = exp.getArgs();
        Expression arg0 = args[0];
        Expression arg1 = args[1];
        Language language = comp.getLanguage();
        Type type = this.isStatic ? language.getTypeFor(arg0) : arg0.getType();
        CodeAttr code = comp.getCode();
        if ((type instanceof ObjectType) && (arg1 instanceof QuoteExp)) {
            ObjectType ctype = (ObjectType) type;
            Object part = ((QuoteExp) arg1).getValue();
            if (part instanceof Field) {
                Field field2 = (Field) part;
                boolean isStaticField = (field2.getModifiers() & 8) != 0;
                args[0].compile(comp, isStaticField ? Target.Ignore : Target.pushValue(ctype));
                if (!isStaticField) {
                    code.emitGetField(field2);
                } else if (0 == 0) {
                    code.emitGetStatic(field2);
                }
                target.compileFromStack(comp, language.getLangTypeFor(field2.getType()));
                return;
            } else if (part instanceof Method) {
                Method method = (Method) part;
                int modifiers = method.getModifiers();
                boolean isStaticMethod = method.getStaticFlag();
                args[0].compile(comp, isStaticMethod ? Target.Ignore : Target.pushValue(ctype));
                if (isStaticMethod) {
                    code.emitInvokeStatic(method);
                } else {
                    code.emitInvoke(method);
                }
                target.compileFromStack(comp, method.getReturnType());
                return;
            }
        }
        String name = ClassMethods.checkName(arg1);
        if (!(type instanceof ArrayType) || !PropertyTypeConstants.PROPERTY_TYPE_LENGTH.equals(name) || this.isStatic) {
            ApplyExp.compile(exp, comp, target);
            return;
        }
        args[0].compile(comp, Target.pushValue(type));
        code.emitArrayLength();
        target.compileFromStack(comp, LangPrimType.intType);
    }

    public Type getReturnType(Expression[] args) {
        if (args.length == 2) {
            Expression arg0 = args[0];
            QuoteExp quoteExp = args[1];
            if (quoteExp instanceof QuoteExp) {
                Object part = quoteExp.getValue();
                if (part instanceof Field) {
                    return ((Field) part).getType();
                }
                if (part instanceof Method) {
                    return ((Method) part).getReturnType();
                }
                if (!this.isStatic && (arg0.getType() instanceof ArrayType) && PropertyTypeConstants.PROPERTY_TYPE_LENGTH.equals(ClassMethods.checkName(quoteExp, true))) {
                    return LangPrimType.intType;
                }
            }
        }
        return Type.pointer_type;
    }

    public Procedure getSetter() {
        return this.setter == null ? super.getSetter() : this.setter;
    }

    public static ApplyExp makeGetField(Expression value, String fieldName) {
        return new ApplyExp((Procedure) field, value, new QuoteExp(fieldName));
    }
}
