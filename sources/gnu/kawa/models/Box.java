package gnu.kawa.models;

import gnu.math.IntNum;
import java.awt.Dimension;
import java.io.Serializable;

public abstract class Box extends Model implements Viewable, Serializable {
    Viewable cellSpacing;
    Viewable[] components;
    int numComponents;

    public abstract int getAxis();

    public Viewable getCellSpacing() {
        return this.cellSpacing;
    }

    public void setCellSpacing(Object cellSpacing2) {
        Dimension dim;
        if ((cellSpacing2 instanceof IntNum) || (cellSpacing2 instanceof Integer)) {
            int size = ((Number) cellSpacing2).intValue();
            if (getAxis() == 0) {
                dim = new Dimension(size, 0);
            } else {
                dim = new Dimension(0, size);
            }
            this.cellSpacing = Spacer.rigidArea(dim);
            return;
        }
        this.cellSpacing = (Viewable) cellSpacing2;
    }

    public final int getComponentCount() {
        return this.numComponents;
    }

    public final Viewable getComponent(int i) {
        return this.components[i];
    }

    public void add(Viewable component) {
        Viewable[] arr = this.components;
        int n = this.numComponents;
        if (n == 0) {
            this.components = new Viewable[4];
        } else if (arr.length <= n) {
            this.components = new Viewable[(n * 2)];
            System.arraycopy(arr, 0, this.components, 0, n);
            Viewable[] arr2 = this.components;
        }
        this.components[n] = component;
        this.numComponents = n + 1;
    }

    public void makeView(Display display, Object where) {
        display.addBox(this, where);
    }
}
