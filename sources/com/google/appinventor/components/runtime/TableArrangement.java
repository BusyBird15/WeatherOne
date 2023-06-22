package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.view.View;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.ViewUtil;
import java.util.ArrayList;
import java.util.List;

@SimpleObject
@DesignerComponent(category = ComponentCategory.LAYOUT, description = "<p>A formatting element in which to place components that should be displayed in tabular form.</p>", version = 1)
public class TableArrangement extends AndroidViewComponent implements Component, ComponentContainer {
    private List<Component> allChildren = new ArrayList();
    private final Activity context;
    private final TableLayout viewLayout;

    public TableArrangement(ComponentContainer container) {
        super(container);
        this.context = container.$context();
        this.viewLayout = new TableLayout(this.context, 2, 2);
        container.$add(this);
    }

    @SimpleProperty(userVisible = false)
    public int Columns() {
        return this.viewLayout.getNumColumns();
    }

    @DesignerProperty(defaultValue = "2", editorType = "non_negative_integer")
    @SimpleProperty(userVisible = false)
    public void Columns(int numColumns) {
        this.viewLayout.setNumColumns(numColumns);
    }

    @SimpleProperty(userVisible = false)
    public int Rows() {
        return this.viewLayout.getNumRows();
    }

    @DesignerProperty(defaultValue = "2", editorType = "non_negative_integer")
    @SimpleProperty(userVisible = false)
    public void Rows(int numRows) {
        this.viewLayout.setNumRows(numRows);
    }

    public Activity $context() {
        return this.context;
    }

    public Form $form() {
        return this.container.$form();
    }

    public void $add(AndroidViewComponent component) {
        this.viewLayout.add(component);
        this.allChildren.add(component);
    }

    public List<? extends Component> getChildren() {
        return this.allChildren;
    }

    public void setChildWidth(AndroidViewComponent component, int width) {
        System.err.println("TableArrangment.setChildWidth: width = " + width + " component = " + component);
        if (width <= -1000) {
            int cWidth = this.container.$form().Width();
            if (cWidth <= -1000 || cWidth > 0) {
                System.err.println("%%TableArrangement.setChildWidth(): width = " + width + " parent Width = " + cWidth + " child = " + component);
                width = ((-(width + 1000)) * cWidth) / 100;
            } else {
                width = -1;
            }
        }
        component.setLastWidth(width);
        ViewUtil.setChildWidthForTableLayout(component.getView(), width);
    }

    public void setChildHeight(AndroidViewComponent component, int height) {
        if (height <= -1000) {
            int cHeight = this.container.$form().Height();
            if (cHeight <= -1000 || cHeight > 0) {
                height = ((-(height + 1000)) * cHeight) / 100;
            } else {
                height = -1;
            }
        }
        component.setLastHeight(height);
        ViewUtil.setChildHeightForTableLayout(component.getView(), height);
    }

    public View getView() {
        return this.viewLayout.getLayoutManager();
    }
}
