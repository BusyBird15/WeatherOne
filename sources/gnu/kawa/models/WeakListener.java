package gnu.kawa.models;

import java.lang.ref.WeakReference;

public class WeakListener extends WeakReference {
    WeakListener next;

    public WeakListener(Object referent) {
        super(referent);
    }

    public WeakListener(Object referent, WeakListener next2) {
        super(referent);
        this.next = next2;
    }

    public void update(Object view, Model model, Object key) {
        ((ModelListener) view).modelUpdated(model, key);
    }
}
