package gnu.bytecode;

public class Location {
    protected String name;
    int name_index;
    int signature_index;
    protected Type type;

    public final String getName() {
        return this.name;
    }

    public final void setName(String name2) {
        this.name = name2;
    }

    public final void setName(int name_index2, ConstantPool constants) {
        if (name_index2 <= 0) {
            this.name = null;
        } else {
            this.name = ((CpoolUtf8) constants.getForced(name_index2, 1)).string;
        }
        this.name_index = name_index2;
    }

    public final Type getType() {
        return this.type;
    }

    public final void setType(Type type2) {
        this.type = type2;
    }

    public final String getSignature() {
        return this.type.getSignature();
    }

    public void setSignature(int signature_index2, ConstantPool constants) {
        this.signature_index = signature_index2;
        this.type = Type.signatureToType(((CpoolUtf8) constants.getForced(signature_index2, 1)).string);
    }
}
