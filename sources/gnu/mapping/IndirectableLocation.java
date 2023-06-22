package gnu.mapping;

public abstract class IndirectableLocation extends Location {
    protected static final Object DIRECT_ON_SET = new String("(direct-on-set)");
    protected static final Object INDIRECT_FLUIDS = new String("(indirect-fluids)");
    protected Location base;
    protected Object value;

    public Symbol getKeySymbol() {
        if (this.base != null) {
            return this.base.getKeySymbol();
        }
        return null;
    }

    public Object getKeyProperty() {
        if (this.base != null) {
            return this.base.getKeyProperty();
        }
        return null;
    }

    public boolean isConstant() {
        return this.base != null && this.base.isConstant();
    }

    /* Debug info: failed to restart local var, previous not found, register: 1 */
    public Location getBase() {
        return this.base == null ? this : this.base.getBase();
    }

    public Location getBaseForce() {
        if (this.base == null) {
            return new PlainLocation(getKeySymbol(), getKeyProperty(), this.value);
        }
        return this.base;
    }

    public void setBase(Location base2) {
        this.base = base2;
        this.value = null;
    }

    public void setAlias(Location base2) {
        this.base = base2;
        this.value = INDIRECT_FLUIDS;
    }

    public void undefine() {
        this.base = null;
        this.value = UNBOUND;
    }

    public Environment getEnvironment() {
        if (this.base instanceof NamedLocation) {
            return ((NamedLocation) this.base).getEnvironment();
        }
        return null;
    }
}
