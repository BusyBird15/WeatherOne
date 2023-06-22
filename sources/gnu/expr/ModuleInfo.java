package gnu.expr;

import gnu.bytecode.ClassType;
import gnu.bytecode.Field;
import gnu.bytecode.Type;
import gnu.kawa.reflect.FieldLocation;
import gnu.kawa.util.AbstractWeakHashTable;
import gnu.mapping.Location;
import gnu.mapping.WrappedException;
import gnu.text.Path;
import java.io.IOException;
import java.net.URL;

public class ModuleInfo {
    static ClassToInfoMap mapClassToInfo = new ClassToInfoMap();
    private String className;
    Compilation comp;
    ModuleInfo[] dependencies;
    ModuleExp exp;
    public long lastCheckedTime;
    public long lastModifiedTime;
    Class moduleClass;
    int numDependencies;
    Path sourceAbsPath;
    String sourceAbsPathname;
    public String sourcePath;
    String uri;

    public String getNamespaceUri() {
        return this.uri;
    }

    public void setNamespaceUri(String uri2) {
        this.uri = uri2;
    }

    public Compilation getCompilation() {
        return this.comp;
    }

    public void setCompilation(Compilation comp2) {
        comp2.minfo = this;
        this.comp = comp2;
        ModuleExp mod = comp2.mainLambda;
        this.exp = mod;
        if (mod != null) {
            String fileName = mod.getFileName();
            this.sourcePath = fileName;
            this.sourceAbsPath = absPath(fileName);
        }
    }

    public void cleanupAfterCompilation() {
        if (this.comp != null) {
            this.comp.cleanupAfterCompilation();
        }
    }

    public static Path absPath(String path) {
        return Path.valueOf(path).getCanonical();
    }

    public Path getSourceAbsPath() {
        return this.sourceAbsPath;
    }

    public void setSourceAbsPath(Path path) {
        this.sourceAbsPath = path;
        this.sourceAbsPathname = null;
    }

    public String getSourceAbsPathname() {
        String str = this.sourceAbsPathname;
        if (str != null || this.sourceAbsPath == null) {
            return str;
        }
        String str2 = this.sourceAbsPath.toString();
        this.sourceAbsPathname = str2;
        return str2;
    }

    public synchronized void addDependency(ModuleInfo dep) {
        if (this.dependencies == null) {
            this.dependencies = new ModuleInfo[8];
        } else if (this.numDependencies == this.dependencies.length) {
            ModuleInfo[] deps = new ModuleInfo[(this.numDependencies * 2)];
            System.arraycopy(this.dependencies, 0, deps, 0, this.numDependencies);
            this.dependencies = deps;
        }
        ModuleInfo[] moduleInfoArr = this.dependencies;
        int i = this.numDependencies;
        this.numDependencies = i + 1;
        moduleInfoArr[i] = dep;
    }

    public synchronized ClassType getClassType() {
        ClassType make;
        if (this.moduleClass != null) {
            make = (ClassType) Type.make(this.moduleClass);
        } else if (this.comp == null || this.comp.mainClass == null) {
            make = ClassType.make(this.className);
        } else {
            make = this.comp.mainClass;
        }
        return make;
    }

    public synchronized String getClassName() {
        if (this.className == null) {
            if (this.moduleClass != null) {
                this.className = this.moduleClass.getName();
            } else if (!(this.comp == null || this.comp.mainClass == null)) {
                this.className = this.comp.mainClass.getName();
            }
        }
        return this.className;
    }

    public void setClassName(String name) {
        this.className = name;
    }

    public synchronized ModuleExp getModuleExp() {
        ModuleExp moduleExp;
        ModuleExp m = this.exp;
        if (m == null) {
            if (this.comp != null) {
                moduleExp = this.comp.mainLambda;
            } else {
                ClassType ctype = ClassType.make(this.className);
                m = new ModuleExp();
                m.type = ctype;
                m.setName(ctype.getName());
                m.flags |= 524288;
                m.info = this;
                this.exp = m;
            }
        }
        moduleExp = m;
        return moduleExp;
    }

    public synchronized ModuleExp setupModuleExp() {
        ModuleExp mod;
        ClassType type;
        Class rclass;
        mod = getModuleExp();
        if ((mod.flags & 524288) != 0) {
            mod.setFlag(false, 524288);
            if (this.moduleClass != null) {
                rclass = this.moduleClass;
                type = (ClassType) Type.make(rclass);
            } else {
                type = ClassType.make(this.className);
                rclass = type.getReflectClass();
            }
            Object instance = null;
            Language language = Language.getDefaultLanguage();
            for (Field fld = type.getFields(); fld != null; fld = fld.getNext()) {
                int flags = fld.getFlags();
                if ((flags & 1) != 0) {
                    if ((flags & 8) == 0 && instance == null) {
                        try {
                            instance = getInstance();
                        } catch (Exception ex) {
                            throw new WrappedException((Throwable) ex);
                        }
                    }
                    Object fvalue = rclass.getField(fld.getName()).get(instance);
                    Declaration fdecl = language.declFromField(mod, fvalue, fld);
                    if ((flags & 16) == 0 || ((fvalue instanceof Location) && !(fvalue instanceof FieldLocation))) {
                        fdecl.noteValue((Expression) null);
                    } else {
                        fdecl.noteValue(new QuoteExp(fvalue));
                    }
                }
            }
            for (Declaration fdecl2 = mod.firstDecl(); fdecl2 != null; fdecl2 = fdecl2.nextDecl()) {
                makeDeclInModule2(mod, fdecl2);
            }
        }
        return mod;
    }

    public synchronized Class getModuleClass() throws ClassNotFoundException {
        Class mclass;
        Class mclass2 = this.moduleClass;
        if (mclass2 != null) {
            mclass = mclass2;
        } else {
            Class mclass3 = ClassType.getContextClass(this.className);
            this.moduleClass = mclass3;
            mclass = mclass3;
        }
        return mclass;
    }

    public Class getModuleClassRaw() {
        return this.moduleClass;
    }

    public void setModuleClass(Class clas) {
        this.moduleClass = clas;
        this.className = clas.getName();
        mapClassToInfo.put(clas, this);
    }

    public static ModuleInfo findFromInstance(Object instance) {
        return ModuleContext.getContext().findFromInstance(instance);
    }

    public static ModuleInfo find(ClassType type) {
        if (type.isExisting()) {
            try {
                return ModuleManager.findWithClass(type.getReflectClass());
            } catch (Exception e) {
            }
        }
        return ModuleManager.getInstance().findWithClassName(type.getName());
    }

    public static void register(Object instance) {
        ModuleContext.getContext().setInstance(instance);
    }

    public Object getInstance() {
        return ModuleContext.getContext().findInstance(this);
    }

    public Object getRunInstance() {
        Object inst = getInstance();
        if (inst instanceof Runnable) {
            ((Runnable) inst).run();
        }
        return inst;
    }

    static void makeDeclInModule2(ModuleExp mod, Declaration fdecl) {
        Object fvalue = fdecl.getConstantValue();
        if (fvalue instanceof FieldLocation) {
            FieldLocation floc = (FieldLocation) fvalue;
            Declaration vdecl = floc.getDeclaration();
            ReferenceExp fref = new ReferenceExp(vdecl);
            fdecl.setAlias(true);
            fref.setDontDereference(true);
            fdecl.setValue(fref);
            if (vdecl.isProcedureDecl()) {
                fdecl.setProcedureDecl(true);
            }
            if (vdecl.getFlag(32768)) {
                fdecl.setSyntax();
            }
            if (!fdecl.getFlag(2048)) {
                String vname = floc.getDeclaringClass().getName();
                Declaration xdecl = mod.firstDecl();
                while (xdecl != null) {
                    if (!vname.equals(xdecl.getType().getName()) || !xdecl.getFlag(1073741824)) {
                        xdecl = xdecl.nextDecl();
                    } else {
                        fref.setContextDecl(xdecl);
                        return;
                    }
                }
            }
        }
    }

    public int getState() {
        if (this.comp == null) {
            return 14;
        }
        return this.comp.getState();
    }

    public void loadByStages(int wantedState) {
        if (getState() + 1 < wantedState) {
            loadByStages(wantedState - 2);
            int state = getState();
            if (state < wantedState) {
                this.comp.setState(state + 1);
                int ndeps = this.numDependencies;
                for (int idep = 0; idep < ndeps; idep++) {
                    this.dependencies[idep].loadByStages(wantedState);
                }
                int state2 = getState();
                if (state2 < wantedState) {
                    this.comp.setState(state2 & -2);
                    this.comp.process(wantedState);
                }
            }
        }
    }

    public boolean loadEager(int wantedState) {
        boolean z = true;
        if (this.comp == null && this.className != null) {
            return false;
        }
        int state = getState();
        if (state >= wantedState) {
            return true;
        }
        if ((state & 1) != 0) {
            return false;
        }
        this.comp.setState(state + 1);
        int ndeps = this.numDependencies;
        int idep = 0;
        while (idep < ndeps) {
            if (this.dependencies[idep].loadEager(wantedState)) {
                idep++;
            } else if (getState() != state + 1) {
                return false;
            } else {
                this.comp.setState(state);
                return false;
            }
        }
        if (getState() == state + 1) {
            this.comp.setState(state);
        }
        this.comp.process(wantedState);
        if (getState() != wantedState) {
            z = false;
        }
        return z;
    }

    public void clearClass() {
        this.moduleClass = null;
        this.numDependencies = 0;
        this.dependencies = null;
    }

    public boolean checkCurrent(ModuleManager manager, long now) {
        if (this.sourceAbsPath == null) {
            return true;
        }
        if (this.lastCheckedTime + manager.lastModifiedCacheTime >= now) {
            return this.moduleClass != null;
        }
        long lastModifiedTime2 = this.sourceAbsPath.getLastModified();
        long oldModifiedTime = this.lastModifiedTime;
        this.lastModifiedTime = lastModifiedTime2;
        this.lastCheckedTime = now;
        if (this.className == null) {
            return false;
        }
        if (this.moduleClass == null) {
            try {
                this.moduleClass = ClassType.getContextClass(this.className);
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        if (oldModifiedTime == 0 && this.moduleClass != null) {
            String classFilename = this.className;
            int dot = classFilename.lastIndexOf(46);
            if (dot >= 0) {
                classFilename = classFilename.substring(dot + 1);
            }
            URL resource = this.moduleClass.getResource(classFilename + ".class");
            if (resource != null) {
                try {
                    oldModifiedTime = resource.openConnection().getLastModified();
                } catch (IOException e2) {
                    resource = null;
                }
            }
            if (resource == null) {
                return true;
            }
        }
        if (lastModifiedTime2 > oldModifiedTime) {
            this.moduleClass = null;
            return false;
        }
        int i = this.numDependencies;
        while (true) {
            i--;
            if (i < 0) {
                return true;
            }
            ModuleInfo dep = this.dependencies[i];
            if (dep.comp == null && !dep.checkCurrent(manager, now)) {
                this.moduleClass = null;
                return false;
            }
        }
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("ModuleInfo[");
        if (this.moduleClass != null) {
            sbuf.append("class: ");
            sbuf.append(this.moduleClass);
        } else if (this.className != null) {
            sbuf.append("class-name: ");
            sbuf.append(this.className);
        }
        sbuf.append(']');
        return sbuf.toString();
    }

    static class ClassToInfoMap extends AbstractWeakHashTable<Class, ModuleInfo> {
        ClassToInfoMap() {
        }

        /* access modifiers changed from: protected */
        public Class getKeyFromValue(ModuleInfo minfo) {
            return minfo.moduleClass;
        }

        /* access modifiers changed from: protected */
        public boolean matches(Class oldValue, Class newValue) {
            return oldValue == newValue;
        }
    }
}
