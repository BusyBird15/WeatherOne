package gnu.kawa.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class AbstractHashTable<Entry extends Map.Entry<K, V>, K, V> extends AbstractMap<K, V> {
    public static final int DEFAULT_INITIAL_SIZE = 64;
    protected int mask;
    protected int num_bindings;
    protected Entry[] table;

    /* access modifiers changed from: protected */
    public abstract Entry[] allocEntries(int i);

    /* access modifiers changed from: protected */
    public abstract int getEntryHashCode(Entry entry);

    /* access modifiers changed from: protected */
    public abstract Entry getEntryNext(Entry entry);

    /* access modifiers changed from: protected */
    public abstract Entry makeEntry(K k, int i, V v);

    /* access modifiers changed from: protected */
    public abstract void setEntryNext(Entry entry, Entry entry2);

    public AbstractHashTable() {
        this(64);
    }

    public AbstractHashTable(int capacity) {
        int log2Size = 4;
        while (capacity > (1 << log2Size)) {
            log2Size++;
        }
        int capacity2 = 1 << log2Size;
        this.table = allocEntries(capacity2);
        this.mask = capacity2 - 1;
    }

    public int hash(Object key) {
        if (key == null) {
            return 0;
        }
        return key.hashCode();
    }

    /* access modifiers changed from: protected */
    public int hashToIndex(int hash) {
        return this.mask & (hash ^ (hash >>> 15));
    }

    /* access modifiers changed from: protected */
    public boolean matches(Object key, int hash, Entry node) {
        return getEntryHashCode(node) == hash && matches(node.getKey(), key);
    }

    /* access modifiers changed from: protected */
    public boolean matches(K key1, Object key2) {
        return key1 == key2 || (key1 != null && key1.equals(key2));
    }

    public V get(Object key) {
        return get(key, (Object) null);
    }

    public Entry getNode(Object key) {
        int hash = hash(key);
        Entry node = this.table[hashToIndex(hash)];
        while (node != null) {
            if (matches(key, hash, node)) {
                return node;
            }
            node = getEntryNext(node);
        }
        return null;
    }

    public V get(Object key, V defaultValue) {
        Entry node = getNode(key);
        return node == null ? defaultValue : node.getValue();
    }

    /* access modifiers changed from: protected */
    public void rehash() {
        Entry[] oldTable = this.table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity * 2;
        Entry[] newTable = allocEntries(newCapacity);
        this.table = newTable;
        this.mask = newCapacity - 1;
        int i = oldCapacity;
        while (true) {
            i--;
            if (i >= 0) {
                Entry chain = oldTable[i];
                if (!(chain == null || getEntryNext(chain) == null)) {
                    Entry prev = null;
                    do {
                        Entry node = chain;
                        chain = getEntryNext(node);
                        setEntryNext(node, prev);
                        prev = node;
                    } while (chain != null);
                    chain = prev;
                }
                Entry element = chain;
                while (element != null) {
                    Entry next = getEntryNext(element);
                    int j = hashToIndex(getEntryHashCode(element));
                    setEntryNext(element, newTable[j]);
                    newTable[j] = element;
                    element = next;
                }
            } else {
                return;
            }
        }
    }

    public V put(K key, V value) {
        return put(key, hash(key), value);
    }

    public V put(K key, int hash, V value) {
        int index = hashToIndex(hash);
        Entry first = this.table[index];
        Entry node = first;
        while (node != null) {
            if (matches(key, hash, node)) {
                V value2 = node.getValue();
                node.setValue(value);
                return value2;
            }
            node = getEntryNext(node);
        }
        int i = this.num_bindings + 1;
        this.num_bindings = i;
        if (i >= this.table.length) {
            rehash();
            index = hashToIndex(hash);
            first = this.table[index];
        }
        Entry node2 = makeEntry(key, hash, value);
        setEntryNext(node2, first);
        this.table[index] = node2;
        return null;
    }

    public V remove(Object key) {
        int hash = hash(key);
        int index = hashToIndex(hash);
        Entry prev = null;
        Entry node = this.table[index];
        while (node != null) {
            Entry next = getEntryNext(node);
            if (matches(key, hash, node)) {
                if (prev == null) {
                    this.table[index] = next;
                } else {
                    setEntryNext(prev, next);
                }
                this.num_bindings--;
                return node.getValue();
            }
            prev = node;
            node = next;
        }
        return null;
    }

    public void clear() {
        Entry[] t = this.table;
        int i = t.length;
        while (true) {
            i--;
            if (i >= 0) {
                Entry e = t[i];
                while (e != null) {
                    Entry next = getEntryNext(e);
                    setEntryNext(e, (Entry) null);
                    e = next;
                }
                t[i] = null;
            } else {
                this.num_bindings = 0;
                return;
            }
        }
    }

    public int size() {
        return this.num_bindings;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return new AbstractEntrySet(this);
    }

    static class AbstractEntrySet<Entry extends Map.Entry<K, V>, K, V> extends AbstractSet<Entry> {
        AbstractHashTable<Entry, K, V> htable;

        public AbstractEntrySet(AbstractHashTable<Entry, K, V> htable2) {
            this.htable = htable2;
        }

        public int size() {
            return this.htable.size();
        }

        public Iterator<Entry> iterator() {
            return new Iterator<Entry>() {
                int curIndex = -1;
                Entry currentEntry;
                Entry nextEntry;
                int nextIndex;
                Entry previousEntry;

                public boolean hasNext() {
                    if (this.curIndex < 0) {
                        this.nextIndex = AbstractEntrySet.this.htable.table.length;
                        this.curIndex = this.nextIndex;
                        advance();
                    }
                    return this.nextEntry != null;
                }

                private void advance() {
                    while (this.nextEntry == null) {
                        int i = this.nextIndex - 1;
                        this.nextIndex = i;
                        if (i >= 0) {
                            this.nextEntry = AbstractEntrySet.this.htable.table[this.nextIndex];
                        } else {
                            return;
                        }
                    }
                }

                public Entry next() {
                    if (this.nextEntry == null) {
                        throw new NoSuchElementException();
                    }
                    this.previousEntry = this.currentEntry;
                    this.currentEntry = this.nextEntry;
                    this.curIndex = this.nextIndex;
                    this.nextEntry = AbstractEntrySet.this.htable.getEntryNext(this.currentEntry);
                    advance();
                    return this.currentEntry;
                }

                public void remove() {
                    if (this.previousEntry == this.currentEntry) {
                        throw new IllegalStateException();
                    }
                    if (this.previousEntry == null) {
                        AbstractEntrySet.this.htable.table[this.curIndex] = this.nextEntry;
                    } else {
                        AbstractEntrySet.this.htable.setEntryNext(this.previousEntry, this.nextEntry);
                    }
                    AbstractHashTable<Entry, K, V> abstractHashTable = AbstractEntrySet.this.htable;
                    abstractHashTable.num_bindings--;
                    this.previousEntry = this.currentEntry;
                }
            };
        }
    }
}
