package gnu.mapping;

public class KeyPair implements EnvironmentKey {
    Symbol name;
    Object property;

    public KeyPair(Symbol name2, Object property2) {
        this.name = name2;
        this.property = property2;
    }

    public Symbol getKeySymbol() {
        return this.name;
    }

    public Object getKeyProperty() {
        return this.property;
    }

    public final boolean matches(EnvironmentKey key) {
        return Symbol.equals(key.getKeySymbol(), this.name) && key.getKeyProperty() == this.property;
    }

    public final boolean matches(Symbol symbol, Object property2) {
        return Symbol.equals(symbol, this.name) && property2 == this.property;
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0017 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r1 = 0
            boolean r2 = r5 instanceof gnu.mapping.KeyPair
            if (r2 != 0) goto L_0x0006
        L_0x0005:
            return r1
        L_0x0006:
            r0 = r5
            gnu.mapping.KeyPair r0 = (gnu.mapping.KeyPair) r0
            java.lang.Object r2 = r4.property
            java.lang.Object r3 = r0.property
            if (r2 != r3) goto L_0x0005
            gnu.mapping.Symbol r2 = r4.name
            if (r2 != 0) goto L_0x0019
            gnu.mapping.Symbol r2 = r0.name
            if (r2 != 0) goto L_0x0005
        L_0x0017:
            r1 = 1
            goto L_0x0005
        L_0x0019:
            gnu.mapping.Symbol r2 = r4.name
            gnu.mapping.Symbol r3 = r0.name
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0005
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.mapping.KeyPair.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return this.name.hashCode() ^ System.identityHashCode(this.property);
    }

    public String toString() {
        return "KeyPair[sym:" + this.name + " prop:" + this.property + "]";
    }
}
