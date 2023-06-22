package gnu.kawa.swingviews;

import gnu.kawa.models.Display;
import gnu.kawa.models.Model;
import gnu.kawa.models.ModelListener;
import gnu.kawa.models.Viewable;
import javax.swing.Box;

/* compiled from: SwingDisplay */
class SwingBox extends Box implements ModelListener {
    gnu.kawa.models.Box model;

    public SwingBox(gnu.kawa.models.Box model2, Display display) {
        super(model2.getAxis());
        model2.addListener((ModelListener) this);
        Viewable cellSpacing = model2.getCellSpacing();
        int n = model2.getComponentCount();
        for (int i = 0; i < n; i++) {
            if (i > 0 && cellSpacing != null) {
                cellSpacing.makeView(display, this);
            }
            model2.getComponent(i).makeView(display, this);
        }
    }

    public void modelUpdated(Model model2, Object key) {
    }
}
