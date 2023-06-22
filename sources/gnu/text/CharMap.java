package gnu.text;

import gnu.kawa.util.AbstractWeakHashTable;

/* compiled from: Char */
class CharMap extends AbstractWeakHashTable<Char, Char> {
    CharMap() {
    }

    public Char get(int key) {
        cleanup();
        for (AbstractWeakHashTable.WEntry<Char, Char> node = ((AbstractWeakHashTable.WEntry[]) this.table)[hashToIndex(key)]; node != null; node = node.next) {
            Char val = node.getValue();
            if (val != null && val.intValue() == key) {
                return val;
            }
        }
        Char val2 = new Char(key);
        super.put(val2, val2);
        return val2;
    }

    /* access modifiers changed from: protected */
    public Char getKeyFromValue(Char ch) {
        return ch;
    }

    /* access modifiers changed from: protected */
    public boolean matches(Char oldValue, Char newValue) {
        return oldValue.intValue() == newValue.intValue();
    }
}
