package gnu.mapping;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.Hashtable;

public class Namespace implements Externalizable, HasNamedParts {
    public static final Namespace EmptyNamespace = valueOf("");
    protected static final Hashtable nsTable = new Hashtable(50);
    int log2Size;
    private int mask;
    String name;
    int num_bindings;
    protected String prefix;
    protected SymbolRef[] table;

    public final String getName() {
        return this.name;
    }

    public final void setName(String name2) {
        this.name = name2;
    }

    public final String getPrefix() {
        return this.prefix;
    }

    protected Namespace() {
        this(64);
    }

    protected Namespace(int capacity) {
        this.prefix = "";
        this.log2Size = 4;
        while (capacity > (1 << this.log2Size)) {
            this.log2Size++;
        }
        int capacity2 = 1 << this.log2Size;
        this.table = new SymbolRef[capacity2];
        this.mask = capacity2 - 1;
    }

    public static Namespace create(int capacity) {
        return new Namespace(capacity);
    }

    public static Namespace create() {
        return new Namespace(64);
    }

    public static Namespace getDefault() {
        return EmptyNamespace;
    }

    public static Symbol getDefaultSymbol(String name2) {
        return EmptyNamespace.getSymbol(name2);
    }

    public static Namespace valueOf() {
        return EmptyNamespace;
    }

    public static Namespace valueOf(String name2) {
        if (name2 == null) {
            name2 = "";
        }
        synchronized (nsTable) {
            Namespace ns = (Namespace) nsTable.get(name2);
            if (ns != null) {
                return ns;
            }
            Namespace ns2 = new Namespace();
            ns2.setName(name2.intern());
            nsTable.put(name2, ns2);
            return ns2;
        }
    }

    public static Namespace valueOf(String uri, String prefix2) {
        if (prefix2 == null || prefix2.length() == 0) {
            return valueOf(uri);
        }
        String xname = prefix2 + " -> " + uri;
        synchronized (nsTable) {
            Object old = nsTable.get(xname);
            if (old instanceof Namespace) {
                Namespace namespace = (Namespace) old;
                return namespace;
            }
            Namespace ns = new Namespace();
            ns.setName(uri.intern());
            ns.prefix = prefix2.intern();
            nsTable.put(xname, ns);
            return ns;
        }
    }

    public static Namespace valueOf(String uri, SimpleSymbol prefix2) {
        return valueOf(uri, prefix2 == null ? null : prefix2.getName());
    }

    public static Namespace makeUnknownNamespace(String prefix2) {
        String uri;
        if (prefix2 == null || prefix2 == "") {
            uri = "";
        } else {
            uri = "http://kawa.gnu.org/unknown-namespace/" + prefix2;
        }
        return valueOf(uri, prefix2);
    }

    public Object get(String key) {
        return Environment.getCurrent().get(getSymbol(key));
    }

    public boolean isConstant(String key) {
        return false;
    }

    public Symbol getSymbol(String key) {
        return lookup(key, key.hashCode(), true);
    }

    public Symbol lookup(String key) {
        return lookup(key, key.hashCode(), false);
    }

    /* access modifiers changed from: protected */
    public final Symbol lookupInternal(String key, int hash) {
        int index = hash & this.mask;
        SymbolRef prev = null;
        SymbolRef ref = this.table[index];
        while (ref != null) {
            SymbolRef next = ref.next;
            Symbol sym = ref.getSymbol();
            if (sym == null) {
                if (prev == null) {
                    this.table[index] = next;
                } else {
                    prev.next = next;
                }
                this.num_bindings--;
            } else if (sym.getLocalPart().equals(key)) {
                return sym;
            } else {
                prev = ref;
            }
            ref = next;
        }
        return null;
    }

    public Symbol add(Symbol sym, int hash) {
        int index = hash & this.mask;
        SymbolRef ref = new SymbolRef(sym, this);
        sym.namespace = this;
        ref.next = this.table[index];
        this.table[index] = ref;
        this.num_bindings++;
        if (this.num_bindings >= this.table.length) {
            rehash();
        }
        return sym;
    }

    public Symbol lookup(String key, int hash, boolean create) {
        Symbol sym;
        synchronized (this) {
            Symbol sym2 = lookupInternal(key, hash);
            if (sym2 != null) {
                return sym2;
            }
            if (!create) {
                return null;
            }
            if (this == EmptyNamespace) {
                sym = new SimpleSymbol(key);
            } else {
                sym = new Symbol(this, key);
            }
            Symbol add = add(sym, hash);
            return add;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0038, code lost:
        r7 = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean remove(gnu.mapping.Symbol r9) {
        /*
            r8 = this;
            monitor-enter(r8)
            java.lang.String r2 = r9.getLocalPart()     // Catch:{ all -> 0x0032 }
            int r0 = r2.hashCode()     // Catch:{ all -> 0x0032 }
            int r7 = r8.mask     // Catch:{ all -> 0x0032 }
            r1 = r0 & r7
            r4 = 0
            gnu.mapping.SymbolRef[] r7 = r8.table     // Catch:{ all -> 0x0032 }
            r5 = r7[r1]     // Catch:{ all -> 0x0032 }
        L_0x0012:
            if (r5 == 0) goto L_0x0038
            gnu.mapping.SymbolRef r3 = r5.next     // Catch:{ all -> 0x0032 }
            gnu.mapping.Symbol r6 = r5.getSymbol()     // Catch:{ all -> 0x0032 }
            if (r6 == 0) goto L_0x001e
            if (r6 != r9) goto L_0x0035
        L_0x001e:
            if (r4 != 0) goto L_0x002f
            gnu.mapping.SymbolRef[] r7 = r8.table     // Catch:{ all -> 0x0032 }
            r7[r1] = r3     // Catch:{ all -> 0x0032 }
        L_0x0024:
            int r7 = r8.num_bindings     // Catch:{ all -> 0x0032 }
            int r7 = r7 + -1
            r8.num_bindings = r7     // Catch:{ all -> 0x0032 }
            if (r6 == 0) goto L_0x0036
            r7 = 1
            monitor-exit(r8)     // Catch:{ all -> 0x0032 }
        L_0x002e:
            return r7
        L_0x002f:
            r4.next = r3     // Catch:{ all -> 0x0032 }
            goto L_0x0024
        L_0x0032:
            r7 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0032 }
            throw r7
        L_0x0035:
            r4 = r5
        L_0x0036:
            r5 = r3
            goto L_0x0012
        L_0x0038:
            r7 = 0
            monitor-exit(r8)     // Catch:{ all -> 0x0032 }
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.mapping.Namespace.remove(gnu.mapping.Symbol):boolean");
    }

    /* access modifiers changed from: protected */
    public void rehash() {
        int oldCapacity = this.table.length;
        int newCapacity = oldCapacity * 2;
        int newMask = newCapacity - 1;
        int countInserted = 0;
        SymbolRef[] oldTable = this.table;
        SymbolRef[] newTable = new SymbolRef[newCapacity];
        int i = oldCapacity;
        while (true) {
            i--;
            if (i >= 0) {
                SymbolRef ref = oldTable[i];
                while (ref != null) {
                    SymbolRef next = ref.next;
                    Symbol sym = ref.getSymbol();
                    if (sym != null) {
                        int index = sym.getName().hashCode() & newMask;
                        countInserted++;
                        ref.next = newTable[index];
                        newTable[index] = ref;
                    }
                    ref = next;
                }
            } else {
                this.table = newTable;
                this.log2Size++;
                this.mask = newMask;
                this.num_bindings = countInserted;
                return;
            }
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getName());
        out.writeObject(this.prefix);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name = ((String) in.readObject()).intern();
        this.prefix = (String) in.readObject();
    }

    public Object readResolve() throws ObjectStreamException {
        String name2 = getName();
        if (name2 != null) {
            String xname = (this.prefix == null || this.prefix.length() == 0) ? name2 : this.prefix + " -> " + name2;
            Namespace ns = (Namespace) nsTable.get(xname);
            if (ns != null) {
                return ns;
            }
            nsTable.put(xname, this);
        }
        return this;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder("#,(namespace \"");
        sbuf.append(this.name);
        sbuf.append('\"');
        if (!(this.prefix == null || this.prefix == "")) {
            sbuf.append(' ');
            sbuf.append(this.prefix);
        }
        sbuf.append(')');
        return sbuf.toString();
    }
}
