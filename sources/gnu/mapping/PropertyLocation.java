package gnu.mapping;

import gnu.lists.LList;
import gnu.lists.Pair;

public class PropertyLocation extends Location {
    Pair pair;

    public final Object get(Object defaultValue) {
        return this.pair.getCar();
    }

    public boolean isBound() {
        return true;
    }

    public final void set(Object newValue) {
        this.pair.setCar(newValue);
    }

    public static Object getPropertyList(Object symbol, Environment env) {
        return env.get(Symbol.PLIST, symbol, LList.Empty);
    }

    public static Object getPropertyList(Object symbol) {
        return Environment.getCurrent().get(Symbol.PLIST, symbol, LList.Empty);
    }

    /* JADX WARNING: type inference failed for: r2v1, types: [gnu.mapping.Location] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setPropertyList(java.lang.Object r11, java.lang.Object r12, gnu.mapping.Environment r13) {
        /*
            monitor-enter(r13)
            gnu.mapping.Symbol r10 = gnu.mapping.Symbol.PLIST     // Catch:{ all -> 0x0071 }
            gnu.mapping.Location r1 = r13.lookup(r10, r11)     // Catch:{ all -> 0x0071 }
            boolean r10 = r11 instanceof gnu.mapping.Symbol     // Catch:{ all -> 0x0071 }
            if (r10 == 0) goto L_0x001f
            r0 = r11
            gnu.mapping.Symbol r0 = (gnu.mapping.Symbol) r0     // Catch:{ all -> 0x0071 }
            r8 = r0
            gnu.lists.LList r10 = gnu.lists.LList.Empty     // Catch:{ all -> 0x0071 }
            java.lang.Object r3 = r1.get(r10)     // Catch:{ all -> 0x0071 }
            r4 = r3
        L_0x0016:
            boolean r10 = r4 instanceof gnu.lists.Pair     // Catch:{ all -> 0x0071 }
            if (r10 != 0) goto L_0x0024
            r4 = r12
        L_0x001b:
            boolean r10 = r4 instanceof gnu.lists.Pair     // Catch:{ all -> 0x0071 }
            if (r10 != 0) goto L_0x0041
        L_0x001f:
            r1.set(r12)     // Catch:{ all -> 0x0071 }
            monitor-exit(r13)     // Catch:{ all -> 0x0071 }
            return
        L_0x0024:
            r0 = r4
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ all -> 0x0071 }
            r5 = r0
            java.lang.Object r7 = r5.getCar()     // Catch:{ all -> 0x0071 }
            r10 = 0
            java.lang.Object r10 = plistGet(r12, r7, r10)     // Catch:{ all -> 0x0071 }
            if (r10 == 0) goto L_0x0036
            r13.remove(r8, r7)     // Catch:{ all -> 0x0071 }
        L_0x0036:
            java.lang.Object r10 = r5.getCdr()     // Catch:{ all -> 0x0071 }
            gnu.lists.Pair r10 = (gnu.lists.Pair) r10     // Catch:{ all -> 0x0071 }
            java.lang.Object r4 = r10.getCdr()     // Catch:{ all -> 0x0071 }
            goto L_0x0016
        L_0x0041:
            r0 = r4
            gnu.lists.Pair r0 = (gnu.lists.Pair) r0     // Catch:{ all -> 0x0071 }
            r5 = r0
            java.lang.Object r7 = r5.getCar()     // Catch:{ all -> 0x0071 }
            gnu.mapping.Location r2 = r13.lookup(r8, r7)     // Catch:{ all -> 0x0071 }
            if (r2 == 0) goto L_0x0068
            gnu.mapping.Location r2 = r2.getBase()     // Catch:{ all -> 0x0071 }
            boolean r10 = r2 instanceof gnu.mapping.PropertyLocation     // Catch:{ all -> 0x0071 }
            if (r10 == 0) goto L_0x0068
            r0 = r2
            gnu.mapping.PropertyLocation r0 = (gnu.mapping.PropertyLocation) r0     // Catch:{ all -> 0x0071 }
            r6 = r0
        L_0x005b:
            java.lang.Object r9 = r5.getCdr()     // Catch:{ all -> 0x0071 }
            gnu.lists.Pair r9 = (gnu.lists.Pair) r9     // Catch:{ all -> 0x0071 }
            r6.pair = r9     // Catch:{ all -> 0x0071 }
            java.lang.Object r4 = r9.getCdr()     // Catch:{ all -> 0x0071 }
            goto L_0x001b
        L_0x0068:
            gnu.mapping.PropertyLocation r6 = new gnu.mapping.PropertyLocation     // Catch:{ all -> 0x0071 }
            r6.<init>()     // Catch:{ all -> 0x0071 }
            r13.addLocation(r8, r7, r6)     // Catch:{ all -> 0x0071 }
            goto L_0x005b
        L_0x0071:
            r10 = move-exception
            monitor-exit(r13)     // Catch:{ all -> 0x0071 }
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.mapping.PropertyLocation.setPropertyList(java.lang.Object, java.lang.Object, gnu.mapping.Environment):void");
    }

    public static void setPropertyList(Object symbol, Object plist) {
        setPropertyList(symbol, plist, Environment.getCurrent());
    }

    public static Object getProperty(Object symbol, Object property, Object defaultValue, Environment env) {
        if (!(symbol instanceof Symbol)) {
            if (!(symbol instanceof String)) {
                return plistGet(env.get(Symbol.PLIST, symbol, LList.Empty), property, defaultValue);
            }
            symbol = Namespace.getDefaultSymbol((String) symbol);
        }
        return env.get((Symbol) symbol, property, defaultValue);
    }

    public static Object getProperty(Object symbol, Object property, Object defaultValue) {
        return getProperty(symbol, property, defaultValue, Environment.getCurrent());
    }

    public static void putProperty(Object symbol, Object property, Object newValue, Environment env) {
        if (!(symbol instanceof Symbol)) {
            if (symbol instanceof String) {
                symbol = Namespace.getDefaultSymbol((String) symbol);
            } else {
                Location lloc = env.getLocation(Symbol.PLIST, symbol);
                lloc.set(plistPut(lloc.get(LList.Empty), property, newValue));
                return;
            }
        }
        Location loc = env.lookup((Symbol) symbol, property);
        if (loc != null) {
            Location loc2 = loc.getBase();
            if (loc2 instanceof PropertyLocation) {
                ((PropertyLocation) loc2).set(newValue);
                return;
            }
        }
        Location lloc2 = env.getLocation(Symbol.PLIST, symbol);
        Pair pair2 = new Pair(newValue, lloc2.get(LList.Empty));
        lloc2.set(new Pair(property, pair2));
        PropertyLocation ploc = new PropertyLocation();
        ploc.pair = pair2;
        env.addLocation((Symbol) symbol, property, ploc);
    }

    public static void putProperty(Object symbol, Object property, Object newValue) {
        putProperty(symbol, property, newValue, Environment.getCurrent());
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: gnu.lists.Pair} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean removeProperty(java.lang.Object r8, java.lang.Object r9, gnu.mapping.Environment r10) {
        /*
            r6 = 0
            gnu.mapping.Symbol r7 = gnu.mapping.Symbol.PLIST
            gnu.mapping.Location r3 = r10.lookup(r7, r8)
            if (r3 != 0) goto L_0x000a
        L_0x0009:
            return r6
        L_0x000a:
            gnu.lists.LList r7 = gnu.lists.LList.Empty
            java.lang.Object r2 = r3.get(r7)
            boolean r7 = r2 instanceof gnu.lists.Pair
            if (r7 == 0) goto L_0x0009
            r1 = r2
            gnu.lists.Pair r1 = (gnu.lists.Pair) r1
            r4 = 0
        L_0x0018:
            java.lang.Object r7 = r1.getCar()
            if (r7 != r9) goto L_0x0039
            java.lang.Object r6 = r1.getCdr()
            gnu.lists.Pair r6 = (gnu.lists.Pair) r6
            java.lang.Object r5 = r6.getCdr()
            if (r4 != 0) goto L_0x0046
            r2 = r5
            r3.set(r2)
        L_0x002e:
            boolean r6 = r8 instanceof gnu.mapping.Symbol
            if (r6 == 0) goto L_0x0037
            gnu.mapping.Symbol r8 = (gnu.mapping.Symbol) r8
            r10.remove(r8, r9)
        L_0x0037:
            r6 = 1
            goto L_0x0009
        L_0x0039:
            java.lang.Object r0 = r1.getCdr()
            boolean r7 = r0 instanceof gnu.lists.Pair
            if (r7 == 0) goto L_0x0009
            r4 = r1
            r1 = r0
            gnu.lists.Pair r1 = (gnu.lists.Pair) r1
            goto L_0x0018
        L_0x0046:
            r4.setCdr(r5)
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.mapping.PropertyLocation.removeProperty(java.lang.Object, java.lang.Object, gnu.mapping.Environment):boolean");
    }

    public static boolean removeProperty(Object symbol, Object property) {
        return removeProperty(symbol, property, Environment.getCurrent());
    }

    public static Object plistGet(Object plist, Object prop, Object dfault) {
        while (plist instanceof Pair) {
            Pair pair2 = (Pair) plist;
            if (pair2.getCar() == prop) {
                return ((Pair) pair2.getCdr()).getCar();
            }
        }
        return dfault;
    }

    public static Object plistPut(Object plist, Object prop, Object value) {
        Object p = plist;
        while (p instanceof Pair) {
            Pair pair2 = (Pair) p;
            Pair next = (Pair) pair2.getCdr();
            if (pair2.getCar() == prop) {
                next.setCar(value);
                return plist;
            }
            p = next.getCdr();
        }
        return new Pair(prop, new Pair(value, plist));
    }

    public static Object plistRemove(Object plist, Object prop) {
        Pair prev = null;
        Object p = plist;
        while (p instanceof Pair) {
            Pair pair2 = (Pair) p;
            Pair next = (Pair) pair2.getCdr();
            p = next.getCdr();
            if (pair2.getCar() != prop) {
                prev = next;
            } else if (prev == null) {
                return p;
            } else {
                prev.setCdr(p);
                return plist;
            }
        }
        return plist;
    }
}
