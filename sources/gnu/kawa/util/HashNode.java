package gnu.kawa.util;

import java.util.Map;

public class HashNode<K, V> implements Map.Entry<K, V> {
    int hash;
    K key;
    public HashNode<K, V> next;
    V value;

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public V setValue(V value2) {
        V old = this.value;
        this.value = value2;
        return old;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.key == null ? 0 : this.key.hashCode();
        if (this.value != null) {
            i = this.value.hashCode();
        }
        return hashCode ^ i;
    }

    public HashNode(K key2, V value2) {
        this.key = key2;
        this.value = value2;
    }

    public V get(V v) {
        return getValue();
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0019 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r1 = 0
            boolean r2 = r5 instanceof gnu.kawa.util.HashNode
            if (r2 != 0) goto L_0x0006
        L_0x0005:
            return r1
        L_0x0006:
            r0 = r5
            gnu.kawa.util.HashNode r0 = (gnu.kawa.util.HashNode) r0
            K r2 = r4.key
            if (r2 != 0) goto L_0x001b
            K r2 = r0.key
            if (r2 != 0) goto L_0x0005
        L_0x0011:
            V r2 = r4.value
            if (r2 != 0) goto L_0x0026
            V r2 = r0.value
            if (r2 != 0) goto L_0x0005
        L_0x0019:
            r1 = 1
            goto L_0x0005
        L_0x001b:
            K r2 = r4.key
            K r3 = r0.key
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0005
            goto L_0x0011
        L_0x0026:
            V r2 = r4.value
            V r3 = r0.value
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0005
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.util.HashNode.equals(java.lang.Object):boolean");
    }
}
