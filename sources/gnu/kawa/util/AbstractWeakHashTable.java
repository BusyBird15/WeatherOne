package gnu.kawa.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;

public abstract class AbstractWeakHashTable<K, V> extends AbstractHashTable<WEntry<K, V>, K, V> {
    ReferenceQueue<V> rqueue = new ReferenceQueue<>();

    /* access modifiers changed from: protected */
    public abstract K getKeyFromValue(V v);

    public AbstractWeakHashTable() {
        super(64);
    }

    public AbstractWeakHashTable(int capacity) {
        super(capacity);
    }

    /* access modifiers changed from: protected */
    public int getEntryHashCode(WEntry<K, V> entry) {
        return entry.hash;
    }

    /* access modifiers changed from: protected */
    public WEntry<K, V> getEntryNext(WEntry<K, V> entry) {
        return entry.next;
    }

    /* access modifiers changed from: protected */
    public void setEntryNext(WEntry<K, V> entry, WEntry<K, V> next) {
        entry.next = next;
    }

    /* access modifiers changed from: protected */
    public WEntry<K, V>[] allocEntries(int n) {
        return (WEntry[]) new WEntry[n];
    }

    /* access modifiers changed from: protected */
    public V getValueIfMatching(WEntry<K, V> node, Object key) {
        V val = node.getValue();
        if (val == null || !matches(getKeyFromValue(val), key)) {
            return null;
        }
        return val;
    }

    public V get(Object key, V defaultValue) {
        cleanup();
        return super.get(key, defaultValue);
    }

    public int hash(Object key) {
        return System.identityHashCode(key);
    }

    /* access modifiers changed from: protected */
    public boolean valuesEqual(V oldValue, V newValue) {
        return oldValue == newValue;
    }

    /* access modifiers changed from: protected */
    public WEntry<K, V> makeEntry(K k, int hash, V value) {
        return new WEntry<>(value, this, hash);
    }

    public V put(K key, V value) {
        cleanup();
        int hash = hash(key);
        int index = hashToIndex(hash);
        WEntry<K, V> first = ((WEntry[]) this.table)[index];
        WEntry<K, V> node = first;
        WEntry<K, V> prev = null;
        V v = null;
        while (node != null) {
            V curValue = node.getValue();
            if (curValue == value) {
                return curValue;
            }
            WEntry<K, V> next = node.next;
            if (curValue == null || !valuesEqual(curValue, value)) {
                prev = node;
            } else {
                if (prev == null) {
                    ((WEntry[]) this.table)[index] = next;
                } else {
                    prev.next = next;
                }
                v = curValue;
            }
            node = next;
        }
        int i = this.num_bindings + 1;
        this.num_bindings = i;
        if (i >= ((WEntry[]) this.table).length) {
            rehash();
            index = hashToIndex(hash);
            first = ((WEntry[]) this.table)[index];
        }
        WEntry<K, V> node2 = makeEntry((Object) null, hash, value);
        node2.next = first;
        ((WEntry[]) this.table)[index] = node2;
        return v;
    }

    /* access modifiers changed from: protected */
    public void cleanup() {
        cleanup(this, this.rqueue);
    }

    static <Entry extends Map.Entry<K, V>, K, V> void cleanup(AbstractHashTable<Entry, ?, ?> map, ReferenceQueue<?> rqueue2) {
        while (true) {
            Entry oldref = (Map.Entry) rqueue2.poll();
            if (oldref != null) {
                int index = map.hashToIndex(map.getEntryHashCode(oldref));
                Entry prev = null;
                Entry node = map.table[index];
                while (true) {
                    if (node == null) {
                        break;
                    }
                    Entry next = map.getEntryNext(node);
                    if (node != oldref) {
                        prev = node;
                        node = next;
                    } else if (prev == null) {
                        map.table[index] = next;
                    } else {
                        map.setEntryNext(prev, next);
                    }
                }
                map.num_bindings--;
            } else {
                return;
            }
        }
    }

    public static class WEntry<K, V> extends WeakReference<V> implements Map.Entry<K, V> {
        public int hash;
        AbstractWeakHashTable<K, V> htable;
        public WEntry next;

        public WEntry(V value, AbstractWeakHashTable<K, V> htable2, int hash2) {
            super(value, htable2.rqueue);
            this.htable = htable2;
            this.hash = hash2;
        }

        public K getKey() {
            V v = get();
            if (v == null) {
                return null;
            }
            return this.htable.getKeyFromValue(v);
        }

        public V getValue() {
            return get();
        }

        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }
    }
}
