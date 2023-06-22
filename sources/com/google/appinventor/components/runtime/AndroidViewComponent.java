package com.google.appinventor.components.runtime;

import android.view.View;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.SimplePropertyCopier;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.util.ErrorMessages;

@SimpleObject
public abstract class AndroidViewComponent extends VisibleComponent {
    private int column = -1;
    protected final ComponentContainer container;
    private int lastSetHeight = -3;
    private int lastSetWidth = -3;
    private int percentHeightHolder = -3;
    private int percentWidthHolder = -3;
    private int row = -1;

    public abstract View getView();

    protected AndroidViewComponent(ComponentContainer container2) {
        this.container = container2;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Specifies whether the %type% should be visible on the screen. Value is true if the %type% is showing and false if hidden.")
    public boolean Visible() {
        return getView().getVisibility() == 0;
    }

    @DesignerProperty(defaultValue = "True", editorType = "visibility")
    @SimpleProperty
    public void Visible(boolean visibility) {
        getView().setVisibility(visibility ? 0 : 8);
    }

    @SimpleProperty
    public int Width() {
        return (int) (((float) getView().getWidth()) / this.container.$form().deviceDensity());
    }

    @SimpleProperty(description = "Specifies the horizontal width of the %type%, measured in pixels.")
    public void Width(int width) {
        this.container.setChildWidth(this, width);
        this.lastSetWidth = width;
        if (width <= -1000) {
            this.container.$form().registerPercentLength(this, width, Form.PercentStorageRecord.Dim.WIDTH);
        } else {
            this.container.$form().unregisterPercentLength(this, Form.PercentStorageRecord.Dim.WIDTH);
        }
    }

    @SimpleProperty(description = "Specifies the horizontal width of the %type% as a percentage of the width of the Screen.")
    public void WidthPercent(int pCent) {
        if (pCent < 0 || pCent > 100) {
            this.container.$form().dispatchErrorOccurredEvent(this, "WidthPercent", ErrorMessages.ERROR_BAD_PERCENT, Integer.valueOf(pCent));
            return;
        }
        Width((-pCent) - 1000);
    }

    public void setLastWidth(int width) {
        this.percentWidthHolder = width;
    }

    public int getSetWidth() {
        if (this.percentWidthHolder == -3) {
            return Width();
        }
        return this.percentWidthHolder;
    }

    public void setLastHeight(int height) {
        this.percentHeightHolder = height;
    }

    public int getSetHeight() {
        if (this.percentHeightHolder == -3) {
            return Height();
        }
        return this.percentHeightHolder;
    }

    @SimplePropertyCopier
    public void CopyWidth(AndroidViewComponent sourceComponent) {
        Width(sourceComponent.lastSetWidth);
    }

    @SimpleProperty
    public int Height() {
        return (int) (((float) getView().getHeight()) / this.container.$form().deviceDensity());
    }

    @SimpleProperty(description = "Specifies the vertical height of the %type%, measured in pixels.")
    public void Height(int height) {
        this.container.setChildHeight(this, height);
        this.lastSetHeight = height;
        if (height <= -1000) {
            this.container.$form().registerPercentLength(this, height, Form.PercentStorageRecord.Dim.HEIGHT);
        } else {
            this.container.$form().unregisterPercentLength(this, Form.PercentStorageRecord.Dim.HEIGHT);
        }
    }

    @SimpleProperty(description = "Specifies the vertical height of the %type% as a percentage of the height of the Screen.")
    public void HeightPercent(int pCent) {
        if (pCent < 0 || pCent > 100) {
            this.container.$form().dispatchErrorOccurredEvent(this, "HeightPercent", ErrorMessages.ERROR_BAD_PERCENT, Integer.valueOf(pCent));
            return;
        }
        Height((-pCent) - 1000);
    }

    @SimplePropertyCopier
    public void CopyHeight(AndroidViewComponent sourceComponent) {
        Height(sourceComponent.lastSetHeight);
    }

    @SimpleProperty(userVisible = false)
    public int Column() {
        return this.column;
    }

    @SimpleProperty(userVisible = false)
    public void Column(int column2) {
        this.column = column2;
    }

    @SimpleProperty(userVisible = false)
    public int Row() {
        return this.row;
    }

    @SimpleProperty(userVisible = false)
    public void Row(int row2) {
        this.row = row2;
    }

    public HandlesEventDispatching getDispatchDelegate() {
        return this.container.$form();
    }
}
