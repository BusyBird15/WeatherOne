package gnu.bytecode;

public class UninitializedType extends ObjectType {
    ClassType ctype;
    Label label;

    UninitializedType(ClassType ctype2) {
        super(ctype2.getName());
        setSignature(ctype2.getSignature());
        this.ctype = ctype2;
    }

    UninitializedType(ClassType ctype2, Label label2) {
        this(ctype2);
        this.label = label2;
    }

    static UninitializedType uninitializedThis(ClassType ctype2) {
        return new UninitializedType(ctype2);
    }

    public Type getImplementationType() {
        return this.ctype;
    }

    public String toString() {
        return "Uninitialized<" + this.ctype.getName() + '>';
    }
}
