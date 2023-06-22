package gnu.mapping;

public class InheritingEnvironment extends SimpleEnvironment {
    int baseTimestamp;
    Environment[] inherited;
    Namespace[] namespaceMap;
    int numInherited;
    Object[] propertyMap;

    public InheritingEnvironment(String name, Environment parent) {
        super(name);
        addParent(parent);
        if (parent instanceof SimpleEnvironment) {
            SimpleEnvironment simpleEnvironment = (SimpleEnvironment) parent;
            int timestamp = simpleEnvironment.currentTimestamp + 1;
            simpleEnvironment.currentTimestamp = timestamp;
            this.baseTimestamp = timestamp;
            this.currentTimestamp = timestamp;
        }
    }

    public final int getNumParents() {
        return this.numInherited;
    }

    public final Environment getParent(int index) {
        return this.inherited[index];
    }

    public void addParent(Environment env) {
        if (this.numInherited == 0) {
            this.inherited = new Environment[4];
        } else if (this.numInherited <= this.inherited.length) {
            Environment[] newInherited = new Environment[(this.numInherited * 2)];
            System.arraycopy(this.inherited, 0, newInherited, 0, this.numInherited);
            this.inherited = newInherited;
        }
        this.inherited[this.numInherited] = env;
        this.numInherited++;
    }

    public NamedLocation lookupInherited(Symbol name, Object property, int hash) {
        for (int i = 0; i < this.numInherited; i++) {
            Symbol sym = name;
            Object prop = property;
            if (this.namespaceMap != null && this.namespaceMap.length > i * 2) {
                Namespace srcNamespace = this.namespaceMap[i * 2];
                Namespace dstNamespace = this.namespaceMap[(i * 2) + 1];
                if (!(srcNamespace == null && dstNamespace == null)) {
                    if (name.getNamespace() != dstNamespace) {
                        continue;
                    } else {
                        sym = Symbol.make(srcNamespace, name.getName());
                    }
                }
            }
            if (this.propertyMap != null && this.propertyMap.length > i * 2) {
                Object srcProperty = this.propertyMap[i * 2];
                Object dstProperty = this.propertyMap[(i * 2) + 1];
                if (!(srcProperty == null && dstProperty == null)) {
                    if (property == dstProperty) {
                        prop = srcProperty;
                    } else {
                        continue;
                    }
                }
            }
            NamedLocation loc = this.inherited[i].lookup(sym, prop, hash);
            if (loc != null && loc.isBound() && (!(loc instanceof SharedLocation) || ((SharedLocation) loc).timestamp < this.baseTimestamp)) {
                return loc;
            }
        }
        return null;
    }

    public NamedLocation lookup(Symbol name, Object property, int hash) {
        NamedLocation loc = super.lookup(name, property, hash);
        return (loc == null || !loc.isBound()) ? lookupInherited(name, property, hash) : loc;
    }

    public synchronized NamedLocation getLocation(Symbol name, Object property, int hash, boolean create) {
        NamedLocation loc;
        NamedLocation xloc;
        NamedLocation namedLocation = null;
        synchronized (this) {
            NamedLocation loc2 = lookupDirect(name, property, hash);
            if (loc2 == null || (!create && !loc2.isBound())) {
                if ((this.flags & 32) == 0 || !create) {
                    loc = lookupInherited(name, property, hash);
                } else {
                    loc = this.inherited[0].getLocation(name, property, hash, true);
                }
                if (loc == null) {
                    if (create) {
                        namedLocation = addUnboundLocation(name, property, hash);
                    }
                    xloc = namedLocation;
                } else if (create) {
                    xloc = addUnboundLocation(name, property, hash);
                    if ((this.flags & 1) == 0 && loc.isBound()) {
                        redefineError(name, property, xloc);
                    }
                    xloc.base = loc;
                    if (loc.value == IndirectableLocation.INDIRECT_FLUIDS) {
                        xloc.value = loc.value;
                    } else if ((this.flags & 16) != 0) {
                        xloc.value = IndirectableLocation.DIRECT_ON_SET;
                    } else {
                        xloc.value = null;
                    }
                    if (xloc instanceof SharedLocation) {
                        ((SharedLocation) xloc).timestamp = this.baseTimestamp;
                    }
                } else {
                    xloc = loc;
                }
            } else {
                xloc = loc2;
            }
        }
        return xloc;
    }

    public LocationEnumeration enumerateAllLocations() {
        LocationEnumeration it = new LocationEnumeration(this.table, 1 << this.log2Size);
        it.env = this;
        if (this.inherited != null && this.inherited.length > 0) {
            it.inherited = this.inherited[0].enumerateAllLocations();
            it.index = 0;
        }
        return it;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0014, code lost:
        r6.prevLoc = null;
        r6.nextLoc = r6.inherited.nextLoc;
        r2 = r6.index + 1;
        r6.index = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0024, code lost:
        if (r2 != r5.numInherited) goto L_0x004a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0026, code lost:
        r6.inherited = null;
        r6.bindings = r5.table;
        r6.index = 1 << r5.log2Size;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hasMoreElements(gnu.mapping.LocationEnumeration r6) {
        /*
            r5 = this;
            r4 = 0
            r1 = 1
            gnu.mapping.LocationEnumeration r2 = r6.inherited
            if (r2 == 0) goto L_0x0031
        L_0x0006:
            gnu.mapping.NamedLocation r0 = r6.nextLoc
        L_0x0008:
            gnu.mapping.LocationEnumeration r2 = r6.inherited
            r2.nextLoc = r0
            gnu.mapping.LocationEnumeration r2 = r6.inherited
            boolean r2 = r2.hasMoreElements()
            if (r2 != 0) goto L_0x0036
            r6.prevLoc = r4
            gnu.mapping.LocationEnumeration r2 = r6.inherited
            gnu.mapping.NamedLocation r2 = r2.nextLoc
            r6.nextLoc = r2
            int r2 = r6.index
            int r2 = r2 + 1
            r6.index = r2
            int r3 = r5.numInherited
            if (r2 != r3) goto L_0x004a
            r6.inherited = r4
            gnu.mapping.NamedLocation[] r2 = r5.table
            r6.bindings = r2
            int r2 = r5.log2Size
            int r1 = r1 << r2
            r6.index = r1
        L_0x0031:
            boolean r1 = super.hasMoreElements(r6)
        L_0x0035:
            return r1
        L_0x0036:
            gnu.mapping.LocationEnumeration r2 = r6.inherited
            gnu.mapping.NamedLocation r0 = r2.nextLoc
            gnu.mapping.Symbol r2 = r0.name
            java.lang.Object r3 = r0.property
            gnu.mapping.Location r2 = r5.lookup(r2, r3)
            if (r2 != r0) goto L_0x0047
            r6.nextLoc = r0
            goto L_0x0035
        L_0x0047:
            gnu.mapping.NamedLocation r0 = r0.next
            goto L_0x0008
        L_0x004a:
            gnu.mapping.Environment[] r2 = r5.inherited
            int r3 = r6.index
            r2 = r2[r3]
            gnu.mapping.LocationEnumeration r2 = r2.enumerateAllLocations()
            r6.inherited = r2
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.mapping.InheritingEnvironment.hasMoreElements(gnu.mapping.LocationEnumeration):boolean");
    }

    /* access modifiers changed from: protected */
    public void toStringBase(StringBuffer sbuf) {
        sbuf.append(" baseTs:");
        sbuf.append(this.baseTimestamp);
        for (int i = 0; i < this.numInherited; i++) {
            sbuf.append(" base:");
            sbuf.append(this.inherited[i].toStringVerbose());
        }
    }
}
