package gnu.mapping;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

public class Symbol implements EnvironmentKey, Comparable, Externalizable {
    public static final Symbol FUNCTION = makeUninterned("(function)");
    public static final Symbol PLIST = makeUninterned("(property-list)");
    protected String name;
    Namespace namespace;

    public final Symbol getKeySymbol() {
        return this;
    }

    public final Object getKeyProperty() {
        return null;
    }

    public boolean matches(EnvironmentKey key) {
        return equals(key.getKeySymbol(), this) && key.getKeyProperty() == null;
    }

    public boolean matches(Symbol symbol, Object property) {
        return equals(symbol, this) && property == null;
    }

    public final String getNamespaceURI() {
        Namespace ns = getNamespace();
        if (ns == null) {
            return null;
        }
        return ns.getName();
    }

    public final String getLocalPart() {
        return this.name;
    }

    public final String getPrefix() {
        Namespace ns = this.namespace;
        return ns == null ? "" : ns.prefix;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r0.getName();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean hasEmptyNamespace() {
        /*
            r3 = this;
            gnu.mapping.Namespace r0 = r3.getNamespace()
            if (r0 == 0) goto L_0x0012
            java.lang.String r1 = r0.getName()
            if (r1 == 0) goto L_0x0012
            int r2 = r1.length()
            if (r2 != 0) goto L_0x0014
        L_0x0012:
            r2 = 1
        L_0x0013:
            return r2
        L_0x0014:
            r2 = 0
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.mapping.Symbol.hasEmptyNamespace():boolean");
    }

    public final String getLocalName() {
        return this.name;
    }

    public final String getName() {
        return this.name;
    }

    public static Symbol make(String uri, String name2, String prefix) {
        return Namespace.valueOf(uri, prefix).getSymbol(name2.intern());
    }

    public static Symbol make(Object namespace2, String name2) {
        Namespace ns = namespace2 instanceof String ? Namespace.valueOf((String) namespace2) : (Namespace) namespace2;
        if (ns == null || name2 == null) {
            return makeUninterned(name2);
        }
        return ns.getSymbol(name2.intern());
    }

    public static SimpleSymbol valueOf(String name2) {
        return (SimpleSymbol) Namespace.EmptyNamespace.getSymbol(name2.intern());
    }

    public static Symbol valueOf(String name2, Object spec) {
        Namespace ns;
        if (spec == null || spec == Boolean.FALSE) {
            return makeUninterned(name2);
        }
        if (spec instanceof Namespace) {
            ns = (Namespace) spec;
        } else if (spec == Boolean.TRUE) {
            ns = Namespace.EmptyNamespace;
        } else {
            ns = Namespace.valueOf(((CharSequence) spec).toString());
        }
        return ns.getSymbol(name2.intern());
    }

    public static Symbol valueOf(String name2, String namespace2, String prefix) {
        return Namespace.valueOf(namespace2, prefix).getSymbol(name2.intern());
    }

    public static Symbol parse(String symbol) {
        int slen = symbol.length();
        int lbr = -1;
        int rbr = -1;
        int braceCount = 0;
        int mainStart = 0;
        int prefixEnd = 0;
        int i = 0;
        while (true) {
            if (i < slen) {
                char ch = symbol.charAt(i);
                if (ch == ':' && braceCount == 0) {
                    prefixEnd = i;
                    mainStart = i + 1;
                    break;
                }
                if (ch == '{') {
                    if (lbr < 0) {
                        prefixEnd = i;
                        lbr = i;
                    }
                    braceCount++;
                }
                if (ch == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        rbr = i;
                        mainStart = (i >= slen || symbol.charAt(i + 1) != ':') ? i + 1 : i + 2;
                    } else if (braceCount < 0) {
                        mainStart = prefixEnd;
                        break;
                    }
                }
                i++;
            } else {
                break;
            }
        }
        if (lbr >= 0 && rbr > 0) {
            return valueOf(symbol.substring(mainStart), symbol.substring(lbr + 1, rbr), prefixEnd > 0 ? symbol.substring(0, prefixEnd) : null);
        } else if (prefixEnd > 0) {
            return makeWithUnknownNamespace(symbol.substring(mainStart), symbol.substring(0, prefixEnd));
        } else {
            return valueOf(symbol);
        }
    }

    public static Symbol makeWithUnknownNamespace(String local, String prefix) {
        return Namespace.makeUnknownNamespace(prefix).getSymbol(local.intern());
    }

    public Symbol() {
    }

    public static Symbol makeUninterned(String name2) {
        return new Symbol((Namespace) null, name2);
    }

    public Symbol(Namespace ns, String name2) {
        this.name = name2;
        this.namespace = ns;
    }

    public int compareTo(Object o) {
        Symbol other = (Symbol) o;
        if (getNamespaceURI() == other.getNamespaceURI()) {
            return getLocalName().compareTo(other.getLocalName());
        }
        throw new IllegalArgumentException("comparing Symbols in different namespaces");
    }

    public static boolean equals(Symbol sym1, Symbol sym2) {
        if (sym1 == sym2) {
            return true;
        }
        if (sym1 == null || sym2 == null) {
            return false;
        }
        if (sym1.name == sym2.name) {
            Namespace namespace1 = sym1.namespace;
            Namespace namespace2 = sym2.namespace;
            if (!(namespace1 == null || namespace2 == null)) {
                if (namespace1.name != namespace2.name) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public final boolean equals(Object o) {
        return (o instanceof Symbol) && equals(this, (Symbol) o);
    }

    public int hashCode() {
        if (this.name == null) {
            return 0;
        }
        return this.name.hashCode();
    }

    public final Namespace getNamespace() {
        return this.namespace;
    }

    public final void setNamespace(Namespace ns) {
        this.namespace = ns;
    }

    public String toString() {
        return toString('P');
    }

    public String toString(char style) {
        boolean hasUri;
        boolean hasPrefix = true;
        String uri = getNamespaceURI();
        String prefix = getPrefix();
        if (uri == null || uri.length() <= 0) {
            hasUri = false;
        } else {
            hasUri = true;
        }
        if (prefix == null || prefix.length() <= 0) {
            hasPrefix = false;
        }
        String name2 = getName();
        if (!hasUri && !hasPrefix) {
            return name2;
        }
        StringBuilder sbuf = new StringBuilder();
        if (hasPrefix && (style != 'U' || !hasUri)) {
            sbuf.append(prefix);
        }
        if (hasUri && (style != 'P' || !hasPrefix)) {
            sbuf.append('{');
            sbuf.append(getNamespaceURI());
            sbuf.append('}');
        }
        sbuf.append(':');
        sbuf.append(name2);
        return sbuf.toString();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getNamespace());
        out.writeObject(getName());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.namespace = (Namespace) in.readObject();
        this.name = (String) in.readObject();
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public Object readResolve() throws ObjectStreamException {
        return this.namespace == null ? this : make(this.namespace, getName());
    }
}
