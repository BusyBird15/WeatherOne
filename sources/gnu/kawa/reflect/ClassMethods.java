package gnu.kawa.reflect;

import gnu.bytecode.ClassType;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.GenericProc;
import gnu.expr.Language;
import gnu.expr.PrimProcedure;
import gnu.expr.QuoteExp;
import gnu.lists.FString;
import gnu.mapping.MethodProc;
import gnu.mapping.Procedure;
import gnu.mapping.Procedure2;
import gnu.mapping.Symbol;
import gnu.mapping.WrongType;
import java.util.Vector;

public class ClassMethods extends Procedure2 {
    public static final ClassMethods classMethods = new ClassMethods();

    static {
        classMethods.setName("class-methods");
    }

    public Object apply2(Object arg0, Object arg1) {
        return apply(this, arg0, arg1);
    }

    public static MethodProc apply(Procedure thisProc, Object arg0, Object arg1) {
        ClassType dtype;
        if (arg0 instanceof Class) {
            arg0 = Type.make((Class) arg0);
        }
        if (arg0 instanceof ClassType) {
            dtype = (ClassType) arg0;
        } else if ((arg0 instanceof String) || (arg0 instanceof FString) || (arg0 instanceof Symbol)) {
            dtype = ClassType.make(arg0.toString());
        } else {
            throw new WrongType(thisProc, 0, (ClassCastException) null);
        }
        if ((arg1 instanceof String) || (arg1 instanceof FString) || (arg1 instanceof Symbol)) {
            String mname = arg1.toString();
            if (!"<init>".equals(mname)) {
                mname = Compilation.mangleName(mname);
            }
            MethodProc result = apply(dtype, mname, 0, Language.getDefaultLanguage());
            if (result != null) {
                return result;
            }
            throw new RuntimeException("no applicable method named `" + mname + "' in " + dtype.getName());
        }
        throw new WrongType(thisProc, 1, (ClassCastException) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0036 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0026 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int removeRedundantMethods(java.util.Vector r12) {
        /*
            int r6 = r12.size()
            r1 = 1
        L_0x0005:
            if (r1 >= r6) goto L_0x0052
            java.lang.Object r4 = r12.elementAt(r1)
            gnu.bytecode.Method r4 = (gnu.bytecode.Method) r4
            gnu.bytecode.ClassType r0 = r4.getDeclaringClass()
            gnu.bytecode.Type[] r8 = r4.getParameterTypes()
            int r7 = r8.length
            r2 = 0
        L_0x0017:
            if (r2 >= r1) goto L_0x004f
            java.lang.Object r5 = r12.elementAt(r2)
            gnu.bytecode.Method r5 = (gnu.bytecode.Method) r5
            gnu.bytecode.Type[] r9 = r5.getParameterTypes()
            int r10 = r9.length
            if (r7 == r10) goto L_0x0029
        L_0x0026:
            int r2 = r2 + 1
            goto L_0x0017
        L_0x0029:
            r3 = r7
        L_0x002a:
            int r3 = r3 + -1
            if (r3 < 0) goto L_0x0034
            r10 = r8[r3]
            r11 = r9[r3]
            if (r10 == r11) goto L_0x002a
        L_0x0034:
            if (r3 >= 0) goto L_0x0026
            gnu.bytecode.ClassType r10 = r5.getDeclaringClass()
            boolean r10 = r0.isSubtype(r10)
            if (r10 == 0) goto L_0x0043
            r12.setElementAt(r4, r2)
        L_0x0043:
            int r10 = r6 + -1
            java.lang.Object r10 = r12.elementAt(r10)
            r12.setElementAt(r10, r1)
            int r6 = r6 + -1
            goto L_0x0005
        L_0x004f:
            int r1 = r1 + 1
            goto L_0x0005
        L_0x0052:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.reflect.ClassMethods.removeRedundantMethods(java.util.Vector):int");
    }

    public static PrimProcedure[] getMethods(ObjectType dtype, String mname, char mode, ClassType caller, Language language) {
        ObjectType objectType;
        if (dtype == Type.tostring_type) {
            dtype = Type.string_type;
        }
        if (mode == 'P') {
            objectType = null;
        } else {
            objectType = dtype;
        }
        MethodFilter filter = new MethodFilter(mname, 0, 0, caller, objectType);
        boolean named_class_only = mode == 'P' || "<init>".equals(mname);
        Vector methods = new Vector();
        dtype.getMethods(filter, named_class_only ? 0 : 2, methods);
        if (!named_class_only && (!(dtype instanceof ClassType) || ((ClassType) dtype).isInterface())) {
            Type.pointer_type.getMethods(filter, 0, methods);
        }
        int mlength = named_class_only ? methods.size() : removeRedundantMethods(methods);
        PrimProcedure[] result = new PrimProcedure[mlength];
        int count = 0;
        int i = mlength;
        while (true) {
            int count2 = count;
            i--;
            if (i < 0) {
                return result;
            }
            Method method = (Method) methods.elementAt(i);
            if (!named_class_only && method.getDeclaringClass() != dtype) {
                Type itype = dtype.getImplementationType();
                if (itype instanceof ClassType) {
                    method = new Method(method, (ClassType) itype);
                }
            }
            count = count2 + 1;
            result[count2] = new PrimProcedure(method, mode, language);
        }
    }

    public static long selectApplicable(PrimProcedure[] methods, Type[] atypes) {
        int limit = methods.length;
        int numDefApplicable = 0;
        int numPosApplicable = 0;
        int i = 0;
        while (i < limit) {
            int code = methods[i].isApplicable(atypes);
            if (code < 0) {
                PrimProcedure tmp = methods[limit - 1];
                methods[limit - 1] = methods[i];
                methods[i] = tmp;
                limit--;
            } else if (code > 0) {
                PrimProcedure tmp2 = methods[numDefApplicable];
                methods[numDefApplicable] = methods[i];
                methods[i] = tmp2;
                numDefApplicable++;
                i++;
            } else {
                numPosApplicable++;
                i++;
            }
        }
        return (((long) numDefApplicable) << 32) + ((long) numPosApplicable);
    }

    public static int selectApplicable(PrimProcedure[] methods, int numArgs) {
        int limit = methods.length;
        int numTooManyArgs = 0;
        int numTooFewArgs = 0;
        int numOk = 0;
        int i = 0;
        while (i < limit) {
            int num = methods[i].numArgs();
            int min = Procedure.minArgs(num);
            int max = Procedure.maxArgs(num);
            boolean ok = false;
            if (numArgs < min) {
                numTooFewArgs++;
            } else if (numArgs <= max || max < 0) {
                ok = true;
            } else {
                numTooManyArgs++;
            }
            if (ok) {
                numOk++;
                i++;
            } else {
                PrimProcedure tmp = methods[limit - 1];
                methods[limit - 1] = methods[i];
                methods[i] = tmp;
                limit--;
            }
        }
        if (numOk > 0) {
            return numOk;
        }
        if (numTooFewArgs > 0) {
            return MethodProc.NO_MATCH_TOO_FEW_ARGS;
        }
        if (numTooManyArgs > 0) {
            return MethodProc.NO_MATCH_TOO_MANY_ARGS;
        }
        return 0;
    }

    public static MethodProc apply(ObjectType dtype, String mname, char mode, Language language) {
        PrimProcedure[] methods = getMethods(dtype, mname, mode, (ClassType) null, language);
        GenericProc gproc = null;
        PrimProcedure pproc = null;
        for (PrimProcedure cur : methods) {
            if (pproc != null && gproc == null) {
                gproc = new GenericProc();
                gproc.add((MethodProc) pproc);
            }
            pproc = cur;
            if (gproc != null) {
                gproc.add((MethodProc) pproc);
            }
        }
        if (gproc == null) {
            return pproc;
        }
        gproc.setName(dtype.getName() + "." + mname);
        return gproc;
    }

    static String checkName(Expression exp, boolean reversible) {
        String nam;
        if (!(exp instanceof QuoteExp)) {
            return null;
        }
        Object name = ((QuoteExp) exp).getValue();
        if ((name instanceof FString) || (name instanceof String)) {
            nam = name.toString();
        } else if (!(name instanceof Symbol)) {
            return null;
        } else {
            nam = ((Symbol) name).getName();
        }
        if (Compilation.isValidJavaName(nam)) {
            return nam;
        }
        return Compilation.mangleName(nam, reversible);
    }

    static String checkName(Expression exp) {
        if (!(exp instanceof QuoteExp)) {
            return null;
        }
        Object name = ((QuoteExp) exp).getValue();
        if ((name instanceof FString) || (name instanceof String)) {
            return name.toString();
        }
        if (name instanceof Symbol) {
            return ((Symbol) name).getName();
        }
        return null;
    }
}
