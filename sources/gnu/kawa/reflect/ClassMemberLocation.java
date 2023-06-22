package gnu.kawa.reflect;

import gnu.bytecode.ClassType;
import gnu.bytecode.Type;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Language;
import gnu.mapping.Environment;
import gnu.mapping.Location;
import gnu.mapping.Named;
import gnu.mapping.Symbol;
import gnu.mapping.UnboundLocationException;
import gnu.mapping.WrappedException;
import java.lang.reflect.Field;

public abstract class ClassMemberLocation extends Location {
    Object instance;
    String mname;
    Field rfield;
    ClassType type;

    public final Object getInstance() {
        return this.instance;
    }

    public final void setInstance(Object obj) {
        this.instance = obj;
    }

    public ClassMemberLocation(Object instance2, ClassType type2, String mname2) {
        this.instance = instance2;
        this.type = type2;
        this.mname = mname2;
    }

    public ClassMemberLocation(Object instance2, Class clas, String mname2) {
        this.instance = instance2;
        this.type = (ClassType) Type.make(clas);
        this.mname = mname2;
    }

    public ClassMemberLocation(Object instance2, Field field) {
        this.instance = instance2;
        this.rfield = field;
        this.mname = field.getName();
    }

    public String getMemberName() {
        return this.mname;
    }

    public ClassType getDeclaringClass() {
        return this.type;
    }

    /* access modifiers changed from: package-private */
    public void setup() {
        if (this.rfield == null) {
            try {
                try {
                    this.rfield = this.type.getReflectClass().getField(this.mname);
                } catch (NoSuchFieldException ex) {
                    RuntimeException uex = new UnboundLocationException((Object) null, "Unbound location  - no field " + this.mname + " in " + this.type.getName());
                    uex.initCause(ex);
                    throw uex;
                }
            } catch (RuntimeException ex2) {
                RuntimeException uex2 = new UnboundLocationException((Object) null, "Unbound location - " + ex2.toString());
                uex2.initCause(ex2);
                throw uex2;
            }
        }
    }

    public Field getRField() {
        Field rfld = this.rfield;
        if (rfld == null) {
            try {
                rfld = this.type.getReflectClass().getField(this.mname);
                this.rfield = rfld;
            } catch (Exception e) {
                return null;
            }
        }
        return rfld;
    }

    public Class getRClass() {
        Field rfld = this.rfield;
        if (rfld != null) {
            return rfld.getDeclaringClass();
        }
        try {
            return this.type.getReflectClass();
        } catch (Exception e) {
            return null;
        }
    }

    public Object get(Object defaultValue) {
        Field rfld = getRField();
        if (rfld == null) {
            return defaultValue;
        }
        try {
            return rfld.get(this.instance);
        } catch (IllegalAccessException ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    public boolean isConstant() {
        return (getRField() == null || (this.rfield.getModifiers() & 16) == 0) ? false : true;
    }

    public boolean isBound() {
        return getRField() != null;
    }

    public void set(Object value) {
        setup();
        try {
            this.rfield.set(this.instance, value);
        } catch (IllegalAccessException ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    public static void define(Object instance2, Field rfield2, String uri, Language language, Environment env) throws IllegalAccessException {
        Symbol sym;
        Location loc;
        Object fvalue = rfield2.get(instance2);
        Type ftype = Type.make(rfield2.getType());
        boolean isAlias = ftype.isSubtype(Compilation.typeLocation);
        boolean isSubtype = ftype.isSubtype(Compilation.typeProcedure);
        int rModifiers = rfield2.getModifiers();
        boolean isFinal = (rModifiers & 16) != 0;
        Object fdname = (!isFinal || !(fvalue instanceof Named) || isAlias) ? Compilation.demangleName(rfield2.getName(), true) : ((Named) fvalue).getSymbol();
        if (fdname instanceof Symbol) {
            sym = (Symbol) fdname;
        } else {
            if (uri == null) {
                uri = "";
            }
            sym = Symbol.make(uri, fdname.toString().intern());
        }
        Object property = null;
        if (!isAlias || !isFinal) {
            if (isFinal) {
                property = language.getEnvPropertyFor(rfield2, fvalue);
            }
            if ((rModifiers & 8) != 0) {
                loc = new StaticFieldLocation(rfield2);
            } else {
                loc = new FieldLocation(instance2, rfield2);
            }
        } else {
            loc = (Location) fvalue;
        }
        env.addLocation(sym, property, loc);
    }

    public static void defineAll(Object instance2, Language language, Environment env) throws IllegalAccessException {
        Field[] fields = instance2.getClass().getFields();
        int i = fields.length;
        while (true) {
            i--;
            if (i >= 0) {
                Field field = fields[i];
                String fname = field.getName();
                if (!fname.startsWith(Declaration.PRIVATE_PREFIX) && !fname.endsWith("$instance")) {
                    define(instance2, field, (String) null, language, env);
                }
            } else {
                return;
            }
        }
    }
}
