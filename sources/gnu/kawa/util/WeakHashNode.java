package gnu.kawa.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;

public class WeakHashNode<K, V> extends WeakReference<K> implements Map.Entry<K, V> {
    public int hash;
    public WeakHashNode<K, V> next;
    public V value;

    public WeakHashNode(K key, ReferenceQueue<K> q, int hash2) {
        super(key, q);
        this.hash = hash2;
    }

    public K getKey() {
        return get();
    }

    public V getValue() {
        return this.value;
    }

    public V setValue(V value2) {
        V old = this.value;
        this.value = value2;
        return old;
    }
}
