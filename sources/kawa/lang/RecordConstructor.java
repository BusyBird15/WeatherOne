package kawa.lang;

import gnu.bytecode.ClassType;
import gnu.bytecode.Field;
import gnu.bytecode.Type;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.ProcedureN;
import gnu.mapping.WrappedException;
import gnu.mapping.WrongArguments;

public class RecordConstructor extends ProcedureN {
    Field[] fields;
    ClassType type;

    public RecordConstructor(ClassType type2, Field[] fields2) {
        this.type = type2;
        this.fields = fields2;
    }

    public RecordConstructor(Class clas, Field[] fields2) {
        this((ClassType) Type.make(clas), fields2);
    }

    public RecordConstructor(Class clas) {
        init((ClassType) Type.make(clas));
    }

    public RecordConstructor(ClassType type2) {
        init(type2);
    }

    private void init(ClassType type2) {
        int i;
        this.type = type2;
        Field list = type2.getFields();
        int count = 0;
        for (Field fld = list; fld != null; fld = fld.getNext()) {
            if ((fld.getModifiers() & 9) == 1) {
                count++;
            }
        }
        this.fields = new Field[count];
        Field fld2 = list;
        int i2 = 0;
        while (fld2 != null) {
            if ((fld2.getModifiers() & 9) == 1) {
                i = i2 + 1;
                this.fields[i2] = fld2;
            } else {
                i = i2;
            }
            fld2 = fld2.getNext();
            i2 = i;
        }
    }

    public RecordConstructor(Class clas, Object fieldsList) {
        this((ClassType) Type.make(clas), fieldsList);
    }

    public RecordConstructor(ClassType type2, Object fieldsList) {
        this.type = type2;
        if (fieldsList == null) {
            init(type2);
            return;
        }
        int nfields = LList.listLength(fieldsList, false);
        this.fields = new Field[nfields];
        Field list = type2.getFields();
        int i = 0;
        while (i < nfields) {
            Pair pair = (Pair) fieldsList;
            String fname = pair.getCar().toString();
            Field fld = list;
            while (fld != null) {
                if (fld.getSourceName() == fname) {
                    this.fields[i] = fld;
                    fieldsList = pair.getCdr();
                    i++;
                } else {
                    fld = fld.getNext();
                }
            }
            throw new RuntimeException("no such field " + fname + " in " + type2.getName());
        }
    }

    public int numArgs() {
        int nargs = this.fields.length;
        return (nargs << 12) | nargs;
    }

    public String getName() {
        return this.type.getName() + " constructor";
    }

    public Object applyN(Object[] args) {
        try {
            Object obj = this.type.getReflectClass().newInstance();
            if (args.length != this.fields.length) {
                throw new WrongArguments(this, args.length);
            }
            int i = 0;
            while (i < args.length) {
                Field fld = this.fields[i];
                try {
                    fld.getReflectField().set(obj, args[i]);
                    i++;
                } catch (Exception ex) {
                    throw new WrappedException("illegal access for field " + fld.getName(), ex);
                }
            }
            return obj;
        } catch (InstantiationException ex2) {
            throw new GenericError(ex2.toString());
        } catch (IllegalAccessException ex3) {
            throw new GenericError(ex3.toString());
        }
    }
}
