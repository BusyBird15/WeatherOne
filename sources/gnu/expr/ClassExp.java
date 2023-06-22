package gnu.expr;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Field;
import gnu.bytecode.Member;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.mapping.OutPort;
import java.util.Hashtable;
import java.util.Vector;

public class ClassExp extends LambdaExp {
    public static final int CLASS_SPECIFIED = 65536;
    public static final int HAS_SUBCLASS = 131072;
    public static final int INTERFACE_SPECIFIED = 32768;
    public static final int IS_ABSTRACT = 16384;
    public String classNameSpecifier;
    public LambdaExp clinitMethod;
    boolean explicitInit;
    public LambdaExp initMethod;
    ClassType instanceType;
    boolean partsDeclared;
    boolean simple;
    public int superClassIndex = -1;
    public Expression[] supers;

    public boolean isSimple() {
        return this.simple;
    }

    public void setSimple(boolean value) {
        this.simple = value;
    }

    public final boolean isAbstract() {
        return getFlag(16384);
    }

    public boolean isMakingClassPair() {
        return this.type != this.instanceType;
    }

    public Type getType() {
        return this.simple ? Compilation.typeClass : Compilation.typeClassType;
    }

    public ClassType getClassType() {
        return this.type;
    }

    public ClassExp() {
    }

    public ClassExp(boolean simple2) {
        this.simple = simple2;
        ClassType classType = new ClassType();
        this.type = classType;
        this.instanceType = classType;
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        return true;
    }

    public void compile(Compilation comp, Target target) {
        if (!(target instanceof IgnoreTarget)) {
            compileMembers(comp);
            compilePushClass(comp, target);
        }
    }

    public void compilePushClass(Compilation comp, Target target) {
        ClassType typeType;
        int nargs;
        ClassType new_class = this.type;
        CodeAttr code = comp.getCode();
        comp.loadClassRef(new_class);
        boolean needsLink = getNeedsClosureEnv();
        if (!isSimple() || needsLink) {
            if (isMakingClassPair() || needsLink) {
                if (new_class == this.instanceType) {
                    code.emitDup((Type) this.instanceType);
                } else {
                    comp.loadClassRef(this.instanceType);
                }
                typeType = ClassType.make("gnu.expr.PairClassType");
                nargs = needsLink ? 3 : 2;
            } else {
                typeType = ClassType.make("gnu.bytecode.Type");
                nargs = 1;
            }
            Type[] argsClass = new Type[nargs];
            if (needsLink) {
                getOwningLambda().loadHeapFrame(comp);
                nargs--;
                argsClass[nargs] = Type.pointer_type;
            }
            ClassType typeClass = ClassType.make("java.lang.Class");
            while (true) {
                nargs--;
                if (nargs >= 0) {
                    argsClass[nargs] = typeClass;
                } else {
                    code.emitInvokeStatic(typeType.addMethod("make", argsClass, (Type) typeType, 9));
                    target.compileFromStack(comp, typeType);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public ClassType getCompiledClassType(Compilation comp) {
        return this.type;
    }

    public void setTypes(Compilation comp) {
        ClassType[] interfaces;
        String name;
        int nlen;
        String name2;
        int modifiers;
        int j;
        int nsupers = this.supers == null ? 0 : this.supers.length;
        ClassType[] superTypes = new ClassType[nsupers];
        ClassType superType = null;
        int i = 0;
        int j2 = 0;
        while (i < nsupers) {
            Type st = Language.getDefaultLanguage().getTypeFor(this.supers[i]);
            if (!(st instanceof ClassType)) {
                comp.setLine(this.supers[i]);
                comp.error('e', "invalid super type");
                j = j2;
            } else {
                ClassType t = (ClassType) st;
                try {
                    modifiers = t.getModifiers();
                } catch (RuntimeException e) {
                    modifiers = 0;
                    if (comp != null) {
                        comp.error('e', "unknown super-type " + t.getName());
                    }
                }
                if ((modifiers & 512) == 0) {
                    if (j2 < i) {
                        comp.error('e', "duplicate superclass for " + this);
                    }
                    superType = t;
                    this.superClassIndex = i;
                    j = j2;
                } else {
                    j = j2 + 1;
                    superTypes[j2] = t;
                }
            }
            i++;
            j2 = j;
        }
        if (!(superType == null || (this.flags & 32768) == 0)) {
            comp.error('e', "cannot be interface since has superclass");
        }
        if (!this.simple && superType == null && (this.flags & 65536) == 0 && (getFlag(131072) || (this.nameDecl != null && this.nameDecl.isPublic()))) {
            PairClassType ptype = new PairClassType();
            this.type = ptype;
            ptype.setInterface(true);
            ptype.instanceType = this.instanceType;
            ClassType[] interfaces2 = {this.type};
            this.instanceType.setSuper(Type.pointer_type);
            this.instanceType.setInterfaces(interfaces2);
        } else if (getFlag(32768)) {
            this.instanceType.setInterface(true);
        }
        ClassType classType = this.type;
        if (superType == null) {
            superType = Type.pointer_type;
        }
        classType.setSuper(superType);
        if (j2 == nsupers) {
            interfaces = superTypes;
        } else {
            interfaces = new ClassType[j2];
            System.arraycopy(superTypes, 0, interfaces, 0, j2);
        }
        this.type.setInterfaces(interfaces);
        if (this.type.getName() == null) {
            if (this.classNameSpecifier != null) {
                name = this.classNameSpecifier;
            } else {
                name = getName();
                if (name != null && (nlen = name.length()) > 2 && name.charAt(0) == '<' && name.charAt(nlen - 1) == '>') {
                    name = name.substring(1, nlen - 1);
                }
            }
            if (name == null) {
                StringBuffer nbuf = new StringBuffer(100);
                comp.getModule().classFor(comp);
                nbuf.append(comp.mainClass.getName());
                nbuf.append('$');
                int len = nbuf.length();
                int i2 = 0;
                while (true) {
                    nbuf.append(i2);
                    name2 = nbuf.toString();
                    if (comp.findNamedClass(name2) == null) {
                        break;
                    }
                    nbuf.setLength(len);
                    i2++;
                }
            } else if (!isSimple() || (this instanceof ObjectExp)) {
                name2 = comp.generateClassName(name);
            } else {
                int start = 0;
                StringBuffer nbuf2 = new StringBuffer(100);
                while (true) {
                    int dot = name.indexOf(46, start);
                    if (dot < 0) {
                        break;
                    }
                    nbuf2.append(Compilation.mangleNameIfNeeded(name.substring(start, dot)));
                    start = dot + 1;
                    if (start < name.length()) {
                        nbuf2.append('.');
                    }
                }
                if (start == 0) {
                    String mainName = comp.mainClass == null ? null : comp.mainClass.getName();
                    int dot2 = mainName == null ? -1 : mainName.lastIndexOf(46);
                    if (dot2 > 0) {
                        nbuf2.append(mainName.substring(0, dot2 + 1));
                    } else if (comp.classPrefix != null) {
                        nbuf2.append(comp.classPrefix);
                    }
                } else if (start == 1 && start < name.length()) {
                    nbuf2.setLength(0);
                    nbuf2.append(comp.mainClass.getName());
                    nbuf2.append('$');
                }
                if (start < name.length()) {
                    nbuf2.append(Compilation.mangleNameIfNeeded(name.substring(start)));
                }
                name2 = nbuf2.toString();
            }
            this.type.setName(name2);
            comp.addClass(this.type);
            if (isMakingClassPair()) {
                this.instanceType.setName(this.type.getName() + "$class");
                comp.addClass(this.instanceType);
            }
        }
    }

    public void declareParts(Compilation comp) {
        if (!this.partsDeclared) {
            this.partsDeclared = true;
            Hashtable<String, Declaration> seenFields = new Hashtable<>();
            for (Declaration decl = firstDecl(); decl != null; decl = decl.nextDecl()) {
                if (decl.getCanRead()) {
                    int flags = decl.getAccessFlags(1);
                    if (decl.getFlag(2048)) {
                        flags |= 8;
                    }
                    if (isMakingClassPair()) {
                        int flags2 = flags | 1024;
                        Type ftype = decl.getType().getImplementationType();
                        this.type.addMethod(slotToMethodName("get", decl.getName()), flags2, Type.typeArray0, ftype);
                        this.type.addMethod(slotToMethodName("set", decl.getName()), flags2, new Type[]{ftype}, (Type) Type.voidType);
                    } else {
                        String fname = Compilation.mangleNameIfNeeded(decl.getName());
                        decl.field = this.instanceType.addField(fname, decl.getType(), flags);
                        decl.setSimple(false);
                        Declaration old = seenFields.get(fname);
                        if (old != null) {
                            duplicateDeclarationError(old, decl, comp);
                        }
                        seenFields.put(fname, decl);
                    }
                }
            }
            for (LambdaExp child = this.firstChild; child != null; child = child.nextSibling) {
                if (child.isAbstract()) {
                    setFlag(16384);
                }
                if ("*init*".equals(child.getName())) {
                    this.explicitInit = true;
                    if (child.isAbstract()) {
                        comp.error('e', "*init* method cannot be abstract", child);
                    }
                    if (this.type instanceof PairClassType) {
                        comp.error('e', "'*init*' methods only supported for simple classes");
                    }
                }
                child.outer = this;
                if (!(child == this.initMethod || child == this.clinitMethod || child.nameDecl == null || child.nameDecl.getFlag(2048)) || !isMakingClassPair()) {
                    child.addMethodFor(this.type, comp, (ObjectType) null);
                }
                if (isMakingClassPair()) {
                    child.addMethodFor(this.instanceType, comp, this.type);
                }
            }
            if (!this.explicitInit && !this.instanceType.isInterface()) {
                Compilation.getConstructor(this.instanceType, this);
            }
            if (isAbstract()) {
                this.instanceType.setModifiers(this.instanceType.getModifiers() | 1024);
            }
            if (this.nameDecl != null) {
                this.instanceType.setModifiers((this.instanceType.getModifiers() & -2) | this.nameDecl.getAccessFlags(1));
            }
        }
    }

    static void getImplMethods(ClassType interfaceType, String mname, Type[] paramTypes, Vector vec) {
        ClassType implType;
        if (interfaceType instanceof PairClassType) {
            implType = ((PairClassType) interfaceType).instanceType;
        } else if (interfaceType.isInterface()) {
            try {
                Class reflectClass = interfaceType.getReflectClass();
                if (reflectClass != null) {
                    implType = (ClassType) Type.make(Class.forName(interfaceType.getName() + "$class", false, reflectClass.getClassLoader()));
                } else {
                    return;
                }
            } catch (Throwable th) {
                return;
            }
        } else {
            return;
        }
        Type[] itypes = new Type[(paramTypes.length + 1)];
        itypes[0] = interfaceType;
        System.arraycopy(paramTypes, 0, itypes, 1, paramTypes.length);
        Method implMethod = implType.getDeclaredMethod(mname, itypes);
        if (implMethod != null) {
            int count = vec.size();
            if (count != 0) {
                if (vec.elementAt(count - 1).equals(implMethod)) {
                    return;
                }
            }
            vec.addElement(implMethod);
            return;
        }
        ClassType[] superInterfaces = interfaceType.getInterfaces();
        for (ClassType implMethods : superInterfaces) {
            getImplMethods(implMethods, mname, paramTypes, vec);
        }
    }

    private static void usedSuperClasses(ClassType clas, Compilation comp) {
        comp.usedClass(clas.getSuperclass());
        ClassType[] interfaces = clas.getInterfaces();
        if (interfaces != null) {
            int i = interfaces.length;
            while (true) {
                i--;
                if (i >= 0) {
                    comp.usedClass(interfaces[i]);
                } else {
                    return;
                }
            }
        }
    }

    public ClassType compileMembers(Compilation comp) {
        Method[] methods;
        int nmethods;
        char ch;
        Type ftype;
        ClassType saveClass = comp.curClass;
        Method saveMethod = comp.method;
        try {
            ClassType new_class = getCompiledClassType(comp);
            comp.curClass = new_class;
            LambdaExp outer = outerLambda();
            Member enclosing = null;
            if (outer instanceof ClassExp) {
                enclosing = outer.type;
            } else if (outer != null && !(outer instanceof ModuleExp)) {
                enclosing = saveMethod;
            } else if ((outer instanceof ModuleExp) && this.type.getName().indexOf(36) > 0) {
                enclosing = outer.type;
            }
            if (enclosing != null) {
                new_class.setEnclosingMember(enclosing);
                if (enclosing instanceof ClassType) {
                    ((ClassType) enclosing).addMemberClass(new_class);
                }
            }
            if (this.instanceType != new_class) {
                this.instanceType.setEnclosingMember(this.type);
                this.type.addMemberClass(this.instanceType);
            }
            usedSuperClasses(this.type, comp);
            if (this.type != this.instanceType) {
                usedSuperClasses(this.instanceType, comp);
            }
            String filename = getFileName();
            if (filename != null) {
                new_class.setSourceFile(filename);
            }
            LambdaExp saveLambda = comp.curLambda;
            comp.curLambda = this;
            allocFrame(comp);
            for (LambdaExp child = this.firstChild; child != null; child = child.nextSibling) {
                if (!child.isAbstract()) {
                    Method save_method = comp.method;
                    LambdaExp save_lambda = comp.curLambda;
                    String saveFilename = comp.getFileName();
                    int saveLine = comp.getLineNumber();
                    int saveColumn = comp.getColumnNumber();
                    comp.setLine((Expression) child);
                    comp.method = child.getMainMethod();
                    Declaration childDecl = child.nameDecl;
                    if (childDecl == null || !childDecl.getFlag(2048)) {
                        child.declareThis(comp.curClass);
                    }
                    comp.curClass = this.instanceType;
                    comp.curLambda = child;
                    comp.method.initCode();
                    child.allocChildClasses(comp);
                    child.allocParameters(comp);
                    if ("*init*".equals(child.getName())) {
                        CodeAttr code = comp.getCode();
                        if (this.staticLinkField != null) {
                            code.emitPushThis();
                            code.emitLoad(code.getCurrentScope().getVariable(1));
                            code.emitPutField(this.staticLinkField);
                        }
                        Expression bodyFirst = child.body;
                        while (bodyFirst instanceof BeginExp) {
                            BeginExp bbody = (BeginExp) bodyFirst;
                            if (bbody.length == 0) {
                                bodyFirst = null;
                            } else {
                                bodyFirst = bbody.exps[0];
                            }
                        }
                        ClassType calledInit = null;
                        if (bodyFirst instanceof ApplyExp) {
                            Expression exp = ((ApplyExp) bodyFirst).func;
                            if (exp instanceof QuoteExp) {
                                Object value = ((QuoteExp) exp).getValue();
                                if (value instanceof PrimProcedure) {
                                    PrimProcedure pproc = (PrimProcedure) value;
                                    if (pproc.isSpecial() && "<init>".equals(pproc.method.getName())) {
                                        calledInit = pproc.method.getDeclaringClass();
                                    }
                                }
                            }
                        }
                        ClassType superClass = this.instanceType.getSuperclass();
                        if (calledInit != null) {
                            bodyFirst.compileWithPosition(comp, Target.Ignore);
                            if (!(calledInit == this.instanceType || calledInit == superClass)) {
                                comp.error('e', "call to <init> for not this or super class");
                            }
                        } else if (superClass != null) {
                            invokeDefaultSuperConstructor(superClass, comp, this);
                        }
                        child.enterFunction(comp);
                        if (calledInit != this.instanceType) {
                            comp.callInitMethods(getCompiledClassType(comp), new Vector(10));
                        }
                        if (calledInit != null) {
                            Expression.compileButFirst(child.body, comp);
                        } else {
                            child.compileBody(comp);
                        }
                    } else {
                        child.enterFunction(comp);
                        child.compileBody(comp);
                    }
                    child.compileEnd(comp);
                    child.generateApplyMethods(comp);
                    comp.method = save_method;
                    comp.curClass = new_class;
                    comp.curLambda = save_lambda;
                    comp.setLine(saveFilename, saveLine, saveColumn);
                }
            }
            if (!this.explicitInit && !this.instanceType.isInterface()) {
                comp.generateConstructor(this.instanceType, this);
            } else if (this.initChain != null) {
                this.initChain.reportError("unimplemented: explicit constructor cannot initialize ", comp);
            }
            if (isAbstract()) {
                methods = null;
                nmethods = 0;
            } else {
                methods = this.type.getAbstractMethods();
                nmethods = methods.length;
            }
            for (int i = 0; i < nmethods; i++) {
                Method meth = methods[i];
                String mname = meth.getName();
                Type[] ptypes = meth.getParameterTypes();
                Type rtype = meth.getReturnType();
                Method mimpl = this.instanceType.getMethod(mname, ptypes);
                if (mimpl == null || mimpl.isAbstract()) {
                    if (mname.length() > 3 && mname.charAt(2) == 't' && mname.charAt(1) == 'e' && ((ch = mname.charAt(0)) == 'g' || ch == 's')) {
                        if (ch == 's' && rtype.isVoid() && ptypes.length == 1) {
                            ftype = ptypes[0];
                        } else if (ch == 'g' && ptypes.length == 0) {
                            ftype = rtype;
                        }
                        String fname = Character.toLowerCase(mname.charAt(3)) + mname.substring(4);
                        Field fld = this.instanceType.getField(fname);
                        if (fld == null) {
                            fld = this.instanceType.addField(fname, ftype, 1);
                        }
                        CodeAttr code2 = this.instanceType.addMethod(mname, 1, ptypes, rtype).startCode();
                        code2.emitPushThis();
                        if (ch == 'g') {
                            code2.emitGetField(fld);
                        } else {
                            code2.emitLoad(code2.getArg(1));
                            code2.emitPutField(fld);
                        }
                        code2.emitReturn();
                    } else {
                        Vector vec = new Vector();
                        getImplMethods(this.type, mname, ptypes, vec);
                        if (vec.size() != 1) {
                            comp.error('e', (vec.size() == 0 ? "missing implementation for " : "ambiguous implementation for ") + meth);
                        } else {
                            CodeAttr code3 = this.instanceType.addMethod(mname, 1, ptypes, rtype).startCode();
                            for (Variable var = code3.getCurrentScope().firstVar(); var != null; var = var.nextVar()) {
                                code3.emitLoad(var);
                            }
                            code3.emitInvokeStatic((Method) vec.elementAt(0));
                            code3.emitReturn();
                        }
                    }
                }
            }
            generateApplyMethods(comp);
            comp.curLambda = saveLambda;
            return new_class;
        } finally {
            comp.curClass = saveClass;
            comp.method = saveMethod;
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        Compilation comp = visitor.getCompilation();
        if (comp == null) {
            return visitor.visitClassExp(this, d);
        }
        ClassType saveClass = comp.curClass;
        try {
            comp.curClass = this.type;
            R visitClassExp = visitor.visitClassExp(this, d);
            comp.curClass = saveClass;
            return visitClassExp;
        } catch (Throwable th) {
            comp.curClass = saveClass;
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public <R, D> void visitChildren(ExpVisitor<R, D> visitor, D d) {
        Declaration firstParam;
        LambdaExp save = visitor.currentLambda;
        visitor.currentLambda = this;
        this.supers = visitor.visitExps(this.supers, this.supers.length, d);
        try {
            for (LambdaExp child = this.firstChild; child != null && visitor.exitValue == null; child = child.nextSibling) {
                if (!(this.instanceType == null || (firstParam = child.firstDecl()) == null || !firstParam.isThisParameter())) {
                    firstParam.setType(this.type);
                }
                visitor.visitLambdaExp(child, d);
            }
        } finally {
            visitor.currentLambda = save;
        }
    }

    static void loadSuperStaticLink(Expression superExp, ClassType superClass, Compilation comp) {
        CodeAttr code = comp.getCode();
        superExp.compile(comp, Target.pushValue(Compilation.typeClassType));
        code.emitInvokeStatic(ClassType.make("gnu.expr.PairClassType").getDeclaredMethod("extractStaticLink", 1));
        code.emitCheckcast(superClass.getOuterLinkType());
    }

    static void invokeDefaultSuperConstructor(ClassType superClass, Compilation comp, LambdaExp lexp) {
        CodeAttr code = comp.getCode();
        Method superConstructor = superClass.getDeclaredMethod("<init>", 0);
        if (superConstructor == null) {
            comp.error('e', "super class does not have a default constructor");
            return;
        }
        code.emitPushThis();
        if (superClass.hasOuterLink() && (lexp instanceof ClassExp)) {
            ClassExp clExp = (ClassExp) lexp;
            loadSuperStaticLink(clExp.supers[clExp.superClassIndex], superClass, comp);
        }
        code.emitInvokeSpecial(superConstructor);
    }

    public void print(OutPort out) {
        out.startLogicalBlock("(" + getExpClassName() + "/", ")", 2);
        Object name = getSymbol();
        if (name != null) {
            out.print(name);
            out.print('/');
        }
        out.print(this.id);
        out.print("/fl:");
        out.print(Integer.toHexString(this.flags));
        if (this.supers.length > 0) {
            out.writeSpaceFill();
            out.startLogicalBlock("supers:", "", 2);
            for (Expression print : this.supers) {
                print.print(out);
                out.writeSpaceFill();
            }
            out.endLogicalBlock("");
        }
        out.print('(');
        int i = 0;
        if (this.keywords != null) {
            int length = this.keywords.length;
        }
        for (Declaration decl = firstDecl(); decl != null; decl = decl.nextDecl()) {
            if (i > 0) {
                out.print(' ');
            }
            decl.printInfo(out);
            i++;
        }
        out.print(") ");
        for (LambdaExp child = this.firstChild; child != null; child = child.nextSibling) {
            out.writeBreakLinear();
            child.print(out);
        }
        if (this.body != null) {
            out.writeBreakLinear();
            this.body.print(out);
        }
        out.endLogicalBlock(")");
    }

    public Field compileSetField(Compilation comp) {
        return new ClassInitializer(this, comp).field;
    }

    public static String slotToMethodName(String prefix, String sname) {
        if (!Compilation.isValidJavaName(sname)) {
            sname = Compilation.mangleName(sname, false);
        }
        int slen = sname.length();
        StringBuffer sbuf = new StringBuffer(slen + 3);
        sbuf.append(prefix);
        if (slen > 0) {
            sbuf.append(Character.toTitleCase(sname.charAt(0)));
            sbuf.append(sname.substring(1));
        }
        return sbuf.toString();
    }

    public Declaration addMethod(LambdaExp lexp, Object mname) {
        Declaration mdecl = addDeclaration(mname, Compilation.typeProcedure);
        lexp.outer = this;
        lexp.setClassMethod(true);
        mdecl.noteValue(lexp);
        mdecl.setFlag(1048576);
        mdecl.setProcedureDecl(true);
        lexp.setSymbol(mname);
        return mdecl;
    }
}
