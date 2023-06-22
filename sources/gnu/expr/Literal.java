package gnu.expr;

import gnu.bytecode.Field;
import gnu.bytecode.Type;

public class Literal {
    static final int CYCLIC = 4;
    static final int EMITTED = 8;
    static final int WRITING = 1;
    static final int WRITTEN = 2;
    public static final Literal nullLiteral = new Literal((Object) null, (Type) Type.nullType);
    Type[] argTypes;
    Object[] argValues;
    public Field field;
    public int flags;
    int index;
    Literal next;
    public Type type;
    Object value;

    public final Object getValue() {
        return this.value;
    }

    /* access modifiers changed from: package-private */
    public void assign(LitTable litTable) {
        assign((String) null, litTable);
    }

    /* access modifiers changed from: package-private */
    public void assign(String name, LitTable litTable) {
        int flags2 = litTable.comp.immediate ? 9 : 24;
        if (name == null) {
            int i = litTable.literalsCount;
            litTable.literalsCount = i + 1;
            this.index = i;
            name = "Lit" + this.index;
        } else {
            flags2 |= 1;
        }
        assign(litTable.mainClass.addField(name, this.type, flags2), litTable);
    }

    /* access modifiers changed from: package-private */
    public void assign(Field field2, LitTable litTable) {
        this.next = litTable.literalsChain;
        litTable.literalsChain = this;
        this.field = field2;
    }

    public Literal(Object value2, LitTable litTable) {
        this(value2, (String) null, litTable);
    }

    public Literal(Object value2, String name, LitTable litTable) {
        this.value = value2;
        litTable.literalTable.put(value2, this);
        this.type = Type.make(value2.getClass());
        assign(name, litTable);
    }

    public Literal(Object value2, Field field2, LitTable litTable) {
        this.value = value2;
        litTable.literalTable.put(value2, this);
        this.field = field2;
        this.type = field2.getType();
        this.flags = 10;
    }

    public Literal(Object value2, Type type2, LitTable litTable) {
        this.value = value2;
        litTable.literalTable.put(value2, this);
        this.type = type2;
    }

    Literal(Object value2, Type type2) {
        this.value = value2;
        this.type = type2;
    }
}
