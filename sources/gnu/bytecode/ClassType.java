package gnu.bytecode;

import androidx.core.internal.view.SupportMenu;
import com.google.appinventor.components.runtime.util.FullScreenVideoUtil;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ClassType extends ObjectType implements AttrContainer, Externalizable, Member {
    public static final int JDK_1_1_VERSION = 2949123;
    public static final int JDK_1_2_VERSION = 3014656;
    public static final int JDK_1_3_VERSION = 3080192;
    public static final int JDK_1_4_VERSION = 3145728;
    public static final int JDK_1_5_VERSION = 3211264;
    public static final int JDK_1_6_VERSION = 3276800;
    public static final int JDK_1_7_VERSION = 3342336;
    public static final ClassType[] noClasses = new ClassType[0];
    int Code_name_index;
    int ConstantValue_name_index;
    int LineNumberTable_name_index;
    int LocalVariableTable_name_index;
    int access_flags;
    Attribute attributes;
    int classfileFormatVersion = JDK_1_1_VERSION;
    ConstantPool constants;
    public Method constructor;
    boolean emitDebugInfo = true;
    Member enclosingMember;
    Field fields;
    int fields_count;
    ClassType firstInnerClass;
    int[] interfaceIndexes;
    ClassType[] interfaces;
    Field last_field;
    Method last_method;
    Method methods;
    int methods_count;
    ClassType nextInnerClass;
    SourceDebugExtAttr sourceDbgExt;
    ClassType superClass;
    int superClassIndex = -1;
    int thisClassIndex;

    public short getClassfileMajorVersion() {
        return (short) (this.classfileFormatVersion >> 16);
    }

    public short getClassfileMinorVersion() {
        return (short) (this.classfileFormatVersion & SupportMenu.USER_MASK);
    }

    public void setClassfileVersion(int major, int minor) {
        this.classfileFormatVersion = ((major & SupportMenu.USER_MASK) * 65536) + (minor * SupportMenu.USER_MASK);
    }

    public void setClassfileVersion(int code) {
        this.classfileFormatVersion = code;
    }

    public int getClassfileVersion() {
        return this.classfileFormatVersion;
    }

    public void setClassfileVersionJava5() {
        setClassfileVersion(JDK_1_5_VERSION);
    }

    public static ClassType make(String name) {
        return (ClassType) Type.getType(name);
    }

    public static ClassType make(String name, ClassType superClass2) {
        ClassType type = make(name);
        if (type.superClass == null) {
            type.setSuper(superClass2);
        }
        return type;
    }

    public final Attribute getAttributes() {
        return this.attributes;
    }

    public final void setAttributes(Attribute attributes2) {
        this.attributes = attributes2;
    }

    public final ConstantPool getConstants() {
        return this.constants;
    }

    public final CpoolEntry getConstant(int i) {
        if (this.constants == null || this.constants.pool == null || i > this.constants.count) {
            return null;
        }
        return this.constants.pool[i];
    }

    public final synchronized int getModifiers() {
        if (!(this.access_flags != 0 || (this.flags & 16) == 0 || getReflectClass() == null)) {
            this.access_flags = this.reflectClass.getModifiers();
        }
        return this.access_flags;
    }

    public final boolean getStaticFlag() {
        return (getModifiers() & 8) != 0;
    }

    public final void setModifiers(int flags) {
        this.access_flags = flags;
    }

    public final void addModifiers(int flags) {
        this.access_flags |= flags;
    }

    public synchronized String getSimpleName() {
        String name;
        if (!((this.flags & 16) == 0 || getReflectClass() == null)) {
            try {
                name = this.reflectClass.getSimpleName();
            } catch (Throwable th) {
            }
        }
        name = getName();
        int dot = name.lastIndexOf(46);
        if (dot > 0) {
            name = name.substring(dot + 1);
        }
        int dollar = name.lastIndexOf(36);
        if (dollar >= 0) {
            int len = name.length();
            int start = dollar + 1;
            while (start < len) {
                char ch = name.charAt(start);
                if (ch < '0' || ch > '9') {
                    break;
                }
                start++;
            }
            name = name.substring(start);
        }
        return name;
    }

    public void addMemberClass(ClassType member) {
        ClassType prev = null;
        ClassType entry = this.firstInnerClass;
        while (entry != null) {
            if (entry != member) {
                prev = entry;
                entry = entry.nextInnerClass;
            } else {
                return;
            }
        }
        if (prev == null) {
            this.firstInnerClass = member;
        } else {
            prev.nextInnerClass = member;
        }
    }

    public ClassType getDeclaredClass(String simpleName) {
        addMemberClasses();
        for (ClassType member = this.firstInnerClass; member != null; member = member.nextInnerClass) {
            if (simpleName.equals(member.getSimpleName())) {
                return member;
            }
        }
        return null;
    }

    public ClassType getDeclaringClass() {
        addEnclosingMember();
        if (this.enclosingMember instanceof ClassType) {
            return (ClassType) this.enclosingMember;
        }
        return null;
    }

    public Member getEnclosingMember() {
        addEnclosingMember();
        return this.enclosingMember;
    }

    public void setEnclosingMember(Member member) {
        this.enclosingMember = member;
    }

    /* access modifiers changed from: package-private */
    public synchronized void addEnclosingMember() {
        if ((this.flags & 24) == 16) {
            Class clas = getReflectClass();
            this.flags |= 8;
            Class dclas = clas.getEnclosingClass();
            if (dclas != null) {
                if (!clas.isMemberClass()) {
                    Method rmeth = clas.getEnclosingMethod();
                    if (rmeth != null) {
                        this.enclosingMember = addMethod(rmeth);
                    } else {
                        Constructor rcons = clas.getEnclosingConstructor();
                        if (rcons != null) {
                            this.enclosingMember = addMethod(rcons);
                        }
                    }
                }
                this.enclosingMember = (ClassType) Type.make(dclas);
            }
        }
    }

    public synchronized void addMemberClasses() {
        if ((this.flags & 20) == 16) {
            Class clas = getReflectClass();
            this.flags |= 4;
            if (numMembers > 0) {
                for (Class make : clas.getClasses()) {
                    addMemberClass((ClassType) Type.make(make));
                }
            }
        }
    }

    public final boolean hasOuterLink() {
        getFields();
        return (this.flags & 32) != 0;
    }

    public ClassType getOuterLinkType() {
        if (!hasOuterLink()) {
            return null;
        }
        return (ClassType) getDeclaredField("this$0").getType();
    }

    public final Field setOuterLink(ClassType outer) {
        if ((this.flags & 16) != 0) {
            throw new Error("setOuterLink called for existing class " + getName());
        }
        Field field = getDeclaredField("this$0");
        if (field == null) {
            field = addField("this$0", outer);
            this.flags |= 32;
            for (Method meth = this.methods; meth != null; meth = meth.getNext()) {
                if ("<init>".equals(meth.getName())) {
                    if (meth.code != null) {
                        throw new Error("setOuterLink called when " + meth + " has code");
                    }
                    Type[] arg_types = meth.arg_types;
                    Type[] new_types = new Type[(arg_types.length + 1)];
                    System.arraycopy(arg_types, 0, new_types, 1, arg_types.length);
                    new_types[0] = outer;
                    meth.arg_types = new_types;
                    meth.signature = null;
                }
            }
        } else if (!outer.equals(field.getType())) {
            throw new Error("inconsistent setOuterLink call for " + getName());
        }
        return field;
    }

    public boolean isAccessible(Member member, ObjectType receiver) {
        if (member.getStaticFlag()) {
            receiver = null;
        }
        return isAccessible(member.getDeclaringClass(), receiver, member.getModifiers());
    }

    public boolean isAccessible(ClassType declaring, ObjectType receiver, int modifiers) {
        int cmods = declaring.getModifiers();
        if ((modifiers & 1) != 0 && (cmods & 1) != 0) {
            return true;
        }
        String callerName = getName();
        String className = declaring.getName();
        if (callerName.equals(className)) {
            return true;
        }
        if ((modifiers & 2) != 0) {
            return false;
        }
        int dot = callerName.lastIndexOf(46);
        String callerPackage = dot >= 0 ? callerName.substring(0, dot) : "";
        int dot2 = className.lastIndexOf(46);
        if (callerPackage.equals(dot2 >= 0 ? className.substring(0, dot2) : "")) {
            return true;
        }
        if ((cmods & 1) == 0) {
            return false;
        }
        if ((modifiers & 4) == 0 || !isSubclass(declaring) || ((receiver instanceof ClassType) && !((ClassType) receiver).isSubclass(this))) {
            return false;
        }
        return true;
    }

    public void setName(String name) {
        this.this_name = name;
        setSignature("L" + name.replace('.', '/') + ";");
    }

    public void setStratum(String stratum) {
        if (this.sourceDbgExt == null) {
            this.sourceDbgExt = new SourceDebugExtAttr(this);
        }
        this.sourceDbgExt.addStratum(stratum);
    }

    public void setSourceFile(String name) {
        if (this.sourceDbgExt != null) {
            this.sourceDbgExt.addFile(name);
            if (this.sourceDbgExt.fileCount > 1) {
                return;
            }
        }
        String name2 = SourceFileAttr.fixSourceFile(name);
        int slash = name2.lastIndexOf(47);
        if (slash >= 0) {
            name2 = name2.substring(slash + 1);
        }
        SourceFileAttr.setSourceFile(this, name2);
    }

    public void setSuper(String name) {
        setSuper(name == null ? Type.pointer_type : make(name));
    }

    public void setSuper(ClassType superClass2) {
        this.superClass = superClass2;
    }

    public synchronized ClassType getSuperclass() {
        if (this.superClass == null && !isInterface() && !"java.lang.Object".equals(getName()) && (this.flags & 16) != 0 && getReflectClass() != null) {
            this.superClass = (ClassType) make(this.reflectClass.getSuperclass());
        }
        return this.superClass;
    }

    public String getPackageName() {
        String name = getName();
        int index = name.lastIndexOf(46);
        return index < 0 ? "" : name.substring(0, index);
    }

    public synchronized ClassType[] getInterfaces() {
        if (!(this.interfaces != null || (this.flags & 16) == 0 || getReflectClass() == null)) {
            Class[] reflectInterfaces = this.reflectClass.getInterfaces();
            int numInterfaces = reflectInterfaces.length;
            this.interfaces = numInterfaces == 0 ? noClasses : new ClassType[numInterfaces];
            for (int i = 0; i < numInterfaces; i++) {
                this.interfaces[i] = (ClassType) Type.make(reflectInterfaces[i]);
            }
        }
        return this.interfaces;
    }

    public void setInterfaces(ClassType[] interfaces2) {
        this.interfaces = interfaces2;
    }

    public void addInterface(ClassType newInterface) {
        int oldCount;
        if (this.interfaces == null || this.interfaces.length == 0) {
            oldCount = 0;
            this.interfaces = new ClassType[1];
        } else {
            oldCount = this.interfaces.length;
            int i = oldCount;
            do {
                i--;
                if (i < 0) {
                    ClassType[] newInterfaces = new ClassType[(oldCount + 1)];
                    System.arraycopy(this.interfaces, 0, newInterfaces, 0, oldCount);
                    this.interfaces = newInterfaces;
                }
            } while (this.interfaces[i] != newInterface);
            return;
        }
        this.interfaces[oldCount] = newInterface;
    }

    public final boolean isInterface() {
        return (getModifiers() & 512) != 0;
    }

    public final void setInterface(boolean val) {
        if (val) {
            this.access_flags |= 1536;
        } else {
            this.access_flags &= -513;
        }
    }

    public ClassType() {
    }

    public ClassType(String class_name) {
        setName(class_name);
    }

    public final synchronized Field getFields() {
        if ((this.flags & 17) == 16) {
            addFields();
        }
        return this.fields;
    }

    public final int getFieldCount() {
        return this.fields_count;
    }

    public Field getDeclaredField(String name) {
        for (Field field = getFields(); field != null; field = field.next) {
            if (name.equals(field.name)) {
                return field;
            }
        }
        return null;
    }

    public synchronized Field getField(String name, int mask) {
        Field field;
        Field field2;
        ClassType cl = this;
        loop0:
        while (true) {
            field = cl.getDeclaredField(name);
            if (field == null || (mask != -1 && (field.getModifiers() & mask) == 0)) {
                ClassType[] interfaces2 = cl.getInterfaces();
                if (interfaces2 != null) {
                    int i = 0;
                    while (true) {
                        if (i >= interfaces2.length) {
                            break;
                        }
                        Field field3 = interfaces2[i].getField(name, mask);
                        if (field3 != null) {
                            field2 = field3;
                            break loop0;
                        }
                        i++;
                    }
                }
                cl = cl.getSuperclass();
                if (cl == null) {
                    field2 = null;
                    break;
                }
            }
        }
        field2 = field;
        return field2;
    }

    public Field getField(String name) {
        return getField(name, 1);
    }

    public Field addField() {
        return new Field(this);
    }

    public Field addField(String name) {
        Field field = new Field(this);
        field.setName(name);
        return field;
    }

    public final Field addField(String name, Type type) {
        Field field = new Field(this);
        field.setName(name);
        field.setType(type);
        return field;
    }

    public final Field addField(String name, Type type, int flags) {
        Field field = addField(name, type);
        field.flags = flags;
        return field;
    }

    public synchronized void addFields() {
        Field[] fields2;
        Class clas = getReflectClass();
        try {
            fields2 = clas.getDeclaredFields();
        } catch (SecurityException e) {
            fields2 = clas.getFields();
        }
        for (Field field : fields2) {
            if ("this$0".equals(field.getName())) {
                this.flags |= 32;
            }
            addField(field.getName(), Type.make(field.getType()), field.getModifiers());
        }
        this.flags |= 1;
    }

    public final Method getMethods() {
        return this.methods;
    }

    public final int getMethodCount() {
        return this.methods_count;
    }

    /* access modifiers changed from: package-private */
    public Method addMethod() {
        return new Method(this, 0);
    }

    public Method addMethod(String name) {
        return addMethod(name, 0);
    }

    public Method addMethod(String name, int flags) {
        Method method = new Method(this, flags);
        method.setName(name);
        return method;
    }

    public Method addMethod(String name, Type[] arg_types, Type return_type, int flags) {
        return addMethod(name, flags, arg_types, return_type);
    }

    public synchronized Method addMethod(String name, int flags, Type[] arg_types, Type return_type) {
        Method method;
        Method method2 = getDeclaredMethod(name, arg_types);
        if (method2 == null || !return_type.equals(method2.getReturnType()) || (method2.access_flags & flags) != flags) {
            Method method3 = addMethod(name, flags);
            method3.arg_types = arg_types;
            method3.return_type = return_type;
            method = method3;
        } else {
            method = method2;
        }
        return method;
    }

    public Method addMethod(Method method) {
        int modifiers = method.getModifiers();
        Class[] paramTypes = method.getParameterTypes();
        int j = paramTypes.length;
        Type[] args = new Type[j];
        while (true) {
            j--;
            if (j >= 0) {
                args[j] = Type.make(paramTypes[j]);
            } else {
                return addMethod(method.getName(), modifiers, args, Type.make(method.getReturnType()));
            }
        }
    }

    public Method addMethod(Constructor method) {
        Class[] paramTypes = method.getParameterTypes();
        int modifiers = method.getModifiers();
        int j = paramTypes.length;
        Type[] args = new Type[j];
        while (true) {
            j--;
            if (j < 0) {
                return addMethod("<init>", modifiers, args, (Type) Type.voidType);
            }
            args[j] = Type.make(paramTypes[j]);
        }
    }

    public Method addMethod(String name, String signature, int flags) {
        Method meth = addMethod(name, flags);
        meth.setSignature(signature);
        return meth;
    }

    public Method getMethod(Method method) {
        String name = method.getName();
        Class[] parameterClasses = method.getParameterTypes();
        Type[] parameterTypes = new Type[parameterClasses.length];
        int i = parameterClasses.length;
        while (true) {
            i--;
            if (i < 0) {
                return addMethod(name, method.getModifiers(), parameterTypes, Type.make(method.getReturnType()));
            }
            parameterTypes[i] = Type.make(parameterClasses[i]);
        }
    }

    public final synchronized Method getDeclaredMethods() {
        if ((this.flags & 18) == 16) {
            addMethods(getReflectClass());
        }
        return this.methods;
    }

    public final int countMethods(Filter filter, int searchSupers) {
        Vector vec = new Vector();
        getMethods(filter, searchSupers, vec);
        return vec.size();
    }

    public Method[] getMethods(Filter filter, boolean searchSupers) {
        return getMethods(filter, searchSupers ? 1 : 0);
    }

    public Method[] getMethods(Filter filter, int searchSupers) {
        Vector<Method> vec = new Vector<>();
        getMethods(filter, searchSupers, vec);
        int count = vec.size();
        Method[] result = new Method[count];
        for (int i = 0; i < count; i++) {
            result[i] = vec.elementAt(i);
        }
        return result;
    }

    public int getMethods(Filter filter, int searchSupers, Method[] result, int offset) {
        Vector<Method> vec = new Vector<>();
        getMethods(filter, searchSupers, vec);
        int count = vec.size();
        for (int i = 0; i < count; i++) {
            result[offset + i] = vec.elementAt(i);
        }
        return count;
    }

    public int getMethods(Filter filter, int searchSupers, List<Method> result) {
        ClassType[] interfaces2;
        int count = 0;
        String inheritingPackage = null;
        for (ClassType ctype = this; ctype != null; ctype = ctype.getSuperclass()) {
            String curPackage = ctype.getPackageName();
            for (Method meth = ctype.getDeclaredMethods(); meth != null; meth = meth.getNext()) {
                if (ctype != this) {
                    int mmods = meth.getModifiers();
                    if ((mmods & 2) == 0) {
                        if ((mmods & 5) == 0 && !curPackage.equals(inheritingPackage)) {
                        }
                    }
                }
                if (filter.select(meth)) {
                    if (result != null) {
                        result.add(meth);
                    }
                    count++;
                }
            }
            inheritingPackage = curPackage;
            if (searchSupers == 0) {
                break;
            }
            if (searchSupers > 1 && (interfaces2 = ctype.getInterfaces()) != null) {
                for (ClassType methods2 : interfaces2) {
                    count += methods2.getMethods(filter, searchSupers, result);
                }
            }
        }
        return count;
    }

    static class AbstractMethodFilter implements Filter {
        public static final AbstractMethodFilter instance = new AbstractMethodFilter();

        AbstractMethodFilter() {
        }

        public boolean select(Object value) {
            return ((Method) value).isAbstract();
        }
    }

    public Method[] getAbstractMethods() {
        return getMethods((Filter) AbstractMethodFilter.instance, 2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x002e A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x001f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.bytecode.Method getDeclaredMethod(java.lang.String r10, gnu.bytecode.Type[] r11) {
        /*
            r9 = this;
            java.lang.String r8 = "<init>"
            boolean r8 = r8.equals(r10)
            if (r8 == 0) goto L_0x0022
            boolean r8 = r9.hasOuterLink()
            if (r8 == 0) goto L_0x0022
            r5 = 1
        L_0x000f:
            gnu.bytecode.Method r3 = r9.getDeclaredMethods()
        L_0x0013:
            if (r3 == 0) goto L_0x0053
            java.lang.String r8 = r3.getName()
            boolean r8 = r10.equals(r8)
            if (r8 != 0) goto L_0x0024
        L_0x001f:
            gnu.bytecode.Method r3 = r3.next
            goto L_0x0013
        L_0x0022:
            r5 = 0
            goto L_0x000f
        L_0x0024:
            gnu.bytecode.Type[] r4 = r3.getParameterTypes()
            if (r11 == 0) goto L_0x002e
            if (r11 != r4) goto L_0x002f
            if (r5 != 0) goto L_0x002f
        L_0x002e:
            return r3
        L_0x002f:
            int r0 = r11.length
            int r8 = r4.length
            int r8 = r8 - r5
            if (r0 != r8) goto L_0x001f
        L_0x0034:
            int r0 = r0 + -1
            if (r0 < 0) goto L_0x0050
            int r8 = r0 + r5
            r2 = r4[r8]
            r7 = r11[r0]
            if (r2 == r7) goto L_0x0034
            if (r7 == 0) goto L_0x0034
            java.lang.String r1 = r2.getSignature()
            java.lang.String r6 = r7.getSignature()
            boolean r8 = r1.equals(r6)
            if (r8 != 0) goto L_0x0034
        L_0x0050:
            if (r0 >= 0) goto L_0x001f
            goto L_0x002e
        L_0x0053:
            r3 = 0
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.bytecode.ClassType.getDeclaredMethod(java.lang.String, gnu.bytecode.Type[]):gnu.bytecode.Method");
    }

    public synchronized Method getDeclaredMethod(String name, int argCount) {
        Method result;
        result = null;
        int needOuterLinkArg = (!"<init>".equals(name) || !hasOuterLink()) ? 0 : 1;
        for (Method method = getDeclaredMethods(); method != null; method = method.next) {
            if (name.equals(method.getName()) && argCount + needOuterLinkArg == method.getParameterTypes().length) {
                if (result != null) {
                    throw new Error("ambiguous call to getDeclaredMethod(\"" + name + "\", " + argCount + ")\n - " + result + "\n - " + method);
                }
                result = method;
            }
        }
        return result;
    }

    public synchronized Method getMethod(String name, Type[] arg_types) {
        Method method;
        ClassType cl = this;
        while (true) {
            method = cl.getDeclaredMethod(name, arg_types);
            if (method == null) {
                cl = cl.getSuperclass();
                if (cl == null) {
                    ClassType cl2 = this;
                    loop1:
                    while (true) {
                        ClassType[] interfaces2 = cl2.getInterfaces();
                        if (interfaces2 != null) {
                            int i = 0;
                            while (true) {
                                if (i >= interfaces2.length) {
                                    break;
                                }
                                method = interfaces2[i].getDeclaredMethod(name, arg_types);
                                if (method != null) {
                                    break loop1;
                                }
                                i++;
                            }
                        }
                        cl2 = cl2.getSuperclass();
                        if (cl2 == null) {
                            method = null;
                            break;
                        }
                    }
                }
            } else {
                break;
            }
        }
        return method;
    }

    public synchronized void addMethods(Class clas) {
        Method[] methods2;
        Constructor[] cmethods;
        this.flags |= 2;
        try {
            methods2 = clas.getDeclaredMethods();
        } catch (SecurityException e) {
            methods2 = clas.getMethods();
        }
        for (Method method : methods2) {
            if (method.getDeclaringClass().equals(clas)) {
                addMethod(method);
            }
        }
        try {
            cmethods = clas.getDeclaredConstructors();
        } catch (SecurityException e2) {
            cmethods = clas.getConstructors();
        }
        for (Constructor method2 : cmethods) {
            if (method2.getDeclaringClass().equals(clas)) {
                addMethod(method2);
            }
        }
    }

    public Method[] getMatchingMethods(String name, Type[] paramTypes, int flags) {
        int nMatches = 0;
        Vector matches = new Vector(10);
        for (Method method = this.methods; method != null; method = method.getNext()) {
            if (name.equals(method.getName()) && (flags & 8) == (method.access_flags & 8) && (flags & 1) <= (method.access_flags & 1) && method.arg_types.length == paramTypes.length) {
                nMatches++;
                matches.addElement(method);
            }
        }
        Method[] result = new Method[nMatches];
        matches.copyInto(result);
        return result;
    }

    public void doFixups() {
        if (this.constants == null) {
            this.constants = new ConstantPool();
        }
        if (this.thisClassIndex == 0) {
            this.thisClassIndex = this.constants.addClass((ObjectType) this).index;
        }
        if (this.superClass == this) {
            setSuper((ClassType) null);
        }
        if (this.superClassIndex < 0) {
            this.superClassIndex = this.superClass == null ? 0 : this.constants.addClass((ObjectType) this.superClass).index;
        }
        if (this.interfaces != null && this.interfaceIndexes == null) {
            int n = this.interfaces.length;
            this.interfaceIndexes = new int[n];
            for (int i = 0; i < n; i++) {
                this.interfaceIndexes[i] = this.constants.addClass((ObjectType) this.interfaces[i]).index;
            }
        }
        for (Field field = this.fields; field != null; field = field.next) {
            field.assign_constants(this);
        }
        for (Method method = this.methods; method != null; method = method.next) {
            method.assignConstants();
        }
        if (this.enclosingMember instanceof Method) {
            EnclosingMethodAttr attr = EnclosingMethodAttr.getFirstEnclosingMethod(getAttributes());
            if (attr == null) {
                attr = new EnclosingMethodAttr(this);
            }
            attr.method = (Method) this.enclosingMember;
        } else if (this.enclosingMember instanceof ClassType) {
            this.constants.addClass((ObjectType) (ClassType) this.enclosingMember);
        }
        for (ClassType member = this.firstInnerClass; member != null; member = member.nextInnerClass) {
            this.constants.addClass((ObjectType) member);
        }
        InnerClassesAttr innerAttr = InnerClassesAttr.getFirstInnerClasses(getAttributes());
        if (innerAttr != null) {
            innerAttr.setSkipped(true);
        }
        Attribute.assignConstants(this, this);
        for (int i2 = 1; i2 <= this.constants.count; i2++) {
            CpoolEntry entry = this.constants.pool[i2];
            if (entry instanceof CpoolClass) {
                CpoolClass centry = (CpoolClass) entry;
                if ((centry.clas instanceof ClassType) && ((ClassType) centry.clas).getEnclosingMember() != null) {
                    if (innerAttr == null) {
                        innerAttr = new InnerClassesAttr(this);
                    }
                    innerAttr.addClass(centry, this);
                }
            }
        }
        if (innerAttr != null) {
            innerAttr.setSkipped(false);
            innerAttr.assignConstants(this);
        }
    }

    public void writeToStream(OutputStream stream) throws IOException {
        DataOutputStream dstr = new DataOutputStream(stream);
        doFixups();
        dstr.writeInt(-889275714);
        dstr.writeShort(getClassfileMinorVersion());
        dstr.writeShort(getClassfileMajorVersion());
        if (this.constants == null) {
            dstr.writeShort(1);
        } else {
            this.constants.write(dstr);
        }
        dstr.writeShort(this.access_flags);
        dstr.writeShort(this.thisClassIndex);
        dstr.writeShort(this.superClassIndex);
        if (this.interfaceIndexes == null) {
            dstr.writeShort(0);
        } else {
            dstr.writeShort(interfaces_count);
            for (int writeShort : this.interfaceIndexes) {
                dstr.writeShort(writeShort);
            }
        }
        dstr.writeShort(this.fields_count);
        for (Field field = this.fields; field != null; field = field.next) {
            field.write(dstr, this);
        }
        dstr.writeShort(this.methods_count);
        for (Method method = this.methods; method != null; method = method.next) {
            method.write(dstr, this);
        }
        Attribute.writeAll(this, dstr);
        this.flags |= 3;
    }

    public void writeToFile(String filename) throws IOException {
        OutputStream stream = new BufferedOutputStream(new FileOutputStream(filename));
        writeToStream(stream);
        stream.close();
    }

    public void writeToFile() throws IOException {
        writeToFile(this.this_name.replace('.', File.separatorChar) + ".class");
    }

    public byte[] writeToArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(500);
        try {
            writeToStream(stream);
            return stream.toByteArray();
        } catch (IOException ex) {
            throw new InternalError(ex.toString());
        }
    }

    public static byte[] to_utf8(String str) {
        int j;
        if (str == null) {
            return null;
        }
        int str_len = str.length();
        int utf_len = 0;
        for (int i = 0; i < str_len; i++) {
            int c = str.charAt(i);
            if (c > 0 && c <= 127) {
                utf_len++;
            } else if (c <= 2047) {
                utf_len += 2;
            } else {
                utf_len += 3;
            }
        }
        byte[] buffer = new byte[utf_len];
        int i2 = 0;
        int j2 = 0;
        while (i2 < str_len) {
            int c2 = str.charAt(i2);
            if (c2 > 0 && c2 <= 127) {
                j = j2 + 1;
                buffer[j2] = (byte) c2;
            } else if (c2 <= 2047) {
                int j3 = j2 + 1;
                buffer[j2] = (byte) (((c2 >> 6) & 31) | FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE);
                buffer[j3] = (byte) (((c2 >> 0) & 63) | 128);
                j = j3 + 1;
            } else {
                int j4 = j2 + 1;
                buffer[j2] = (byte) (((c2 >> 12) & 15) | 224);
                int j5 = j4 + 1;
                buffer[j4] = (byte) (((c2 >> 6) & 63) | 128);
                j = j5 + 1;
                buffer[j5] = (byte) (((c2 >> 0) & 63) | 128);
            }
            i2++;
            j2 = j;
        }
        return buffer;
    }

    public final boolean implementsInterface(ClassType iface) {
        if (this == iface) {
            return true;
        }
        ClassType baseClass = getSuperclass();
        if (baseClass != null && baseClass.implementsInterface(iface)) {
            return true;
        }
        ClassType[] interfaces2 = getInterfaces();
        if (interfaces2 != null) {
            int i = interfaces2.length;
            do {
                i--;
                if (i >= 0) {
                }
            } while (!interfaces2[i].implementsInterface(iface));
            return true;
        }
        return false;
    }

    public final boolean isSubclass(String cname) {
        ClassType ctype = this;
        while (!cname.equals(ctype.getName())) {
            ctype = ctype.getSuperclass();
            if (ctype == null) {
                return false;
            }
        }
        return true;
    }

    public final boolean isSubclass(ClassType other) {
        if (other.isInterface()) {
            return implementsInterface(other);
        }
        if (this == toStringType && other == javalangStringType) {
            return true;
        }
        if (this == javalangStringType && other == toStringType) {
            return true;
        }
        for (ClassType baseClass = this; baseClass != null; baseClass = baseClass.getSuperclass()) {
            if (baseClass == other) {
                return true;
            }
        }
        return false;
    }

    public int compare(Type other) {
        int i = -1;
        if (other == nullType) {
            return 1;
        }
        if (!(other instanceof ClassType)) {
            return swappedCompareResult(other.compare(this));
        }
        String name = getName();
        if (name != null && name.equals(other.getName())) {
            return 0;
        }
        ClassType cother = (ClassType) other;
        if (isSubclass(cother)) {
            return -1;
        }
        if (cother.isSubclass(this)) {
            return 1;
        }
        if (this == toStringType) {
            if (cother != Type.javalangObjectType) {
                i = 1;
            }
            return i;
        } else if (cother == toStringType) {
            if (this != Type.javalangObjectType) {
                return -1;
            }
            return 1;
        } else if (isInterface()) {
            if (cother != Type.javalangObjectType) {
                i = -2;
            }
            return i;
        } else if (!cother.isInterface()) {
            return -3;
        } else {
            if (this != Type.javalangObjectType) {
                return -2;
            }
            return 1;
        }
    }

    public String toString() {
        return "ClassType " + getName();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(getName());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        setName(in.readUTF());
        this.flags |= 16;
    }

    public Object readResolve() throws ObjectStreamException {
        String name = getName();
        HashMap<String, Type> map = mapNameToType;
        synchronized (map) {
            Type found = map.get(name);
            if (found != null) {
                return found;
            }
            map.put(name, this);
            return this;
        }
    }

    public void cleanupAfterCompilation() {
        for (Method meth = this.methods; meth != null; meth = meth.getNext()) {
            meth.cleanupAfterCompilation();
        }
        this.constants = null;
        this.attributes = null;
        this.sourceDbgExt = null;
    }

    public Method checkSingleAbstractMethod() {
        Method result = null;
        for (Method meth : getAbstractMethods()) {
            Method mimpl = getMethod(meth.getName(), meth.getParameterTypes());
            if (mimpl == null || mimpl.isAbstract()) {
                if (result != null) {
                    return null;
                }
                result = meth;
            }
        }
        return result;
    }
}
