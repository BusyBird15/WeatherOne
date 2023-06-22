package kawa.lang;

import gnu.bytecode.ArrayClassLoader;
import gnu.bytecode.ClassType;
import gnu.bytecode.Field;
import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.Inlineable;
import gnu.expr.Target;
import gnu.mapping.Procedure1;

public class GetFieldProc extends Procedure1 implements Inlineable {
    ClassType ctype;
    Field field;

    public GetFieldProc(Class clas, String fname) {
        this((ClassType) Type.make(clas), fname);
    }

    public GetFieldProc(ClassType ctype2, String fname) {
        this.ctype = ctype2;
        this.field = Field.searchField(ctype2.getFields(), fname);
    }

    public GetFieldProc(ClassType ctype2, String name, Type ftype, int flags) {
        this.ctype = ctype2;
        this.field = ctype2.getField(name);
        if (this.field == null) {
            this.field = ctype2.addField(name, ftype, flags);
        }
    }

    public Object apply1(Object arg1) {
        try {
            return this.field.getReflectField().get(arg1);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("no such field " + this.field.getSourceName() + " in " + this.ctype.getName());
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("illegal access for field " + this.field.getSourceName());
        }
    }

    private Field getField() {
        return this.field;
    }

    public void compile(ApplyExp exp, Compilation comp, Target target) {
        if (this.ctype.getReflectClass().getClassLoader() instanceof ArrayClassLoader) {
            ApplyExp.compile(exp, comp, target);
            return;
        }
        exp.getArgs()[0].compile(comp, (Type) this.ctype);
        comp.getCode().emitGetField(this.field);
        target.compileFromStack(comp, this.field.getType());
    }

    public Type getReturnType(Expression[] args) {
        return getField().getType();
    }
}
