package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.SdkLevel;

@SimpleObject
@DesignerComponent(category = ComponentCategory.USERINTERFACE, description = "Toggle switch that raises an event when the user clicks on it. There are many properties affecting its appearance that can be set in the Designer or Blocks Editor.", version = 1)
public final class Switch extends ToggleBase<CompoundButton> {
    private final Activity activity;
    private final SwitchCompat switchView;
    private int thumbColorActive;
    private int thumbColorInactive;
    private int trackColorActive;
    private int trackColorInactive;

    public Switch(ComponentContainer container) {
        super(container);
        this.activity = container.$context();
        if (SdkLevel.getLevel() < 14) {
            this.switchView = null;
            this.view = new CheckBox(this.activity);
        } else {
            this.switchView = new SwitchCompat(this.activity);
            this.view = this.switchView;
        }
        On(false);
        ThumbColorActive(-1);
        ThumbColorInactive(Component.COLOR_LTGRAY);
        TrackColorActive(Component.COLOR_GREEN);
        TrackColorInactive(Component.COLOR_GRAY);
        initToggle();
    }

    private ColorStateList createSwitchColors(int active_color, int inactive_color) {
        return new ColorStateList(new int[][]{new int[]{16842912}, new int[0]}, new int[]{active_color, inactive_color});
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public int ThumbColorActive() {
        return this.thumbColorActive;
    }

    @DesignerProperty(defaultValue = "&HFFFFFFFF", editorType = "color")
    @SimpleProperty
    public void ThumbColorActive(int argb) {
        this.thumbColorActive = argb;
        if (this.switchView != null) {
            DrawableCompat.setTintList(this.switchView.getThumbDrawable(), createSwitchColors(argb, this.thumbColorInactive));
            this.view.invalidate();
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
    public int ThumbColorInactive() {
        return this.thumbColorInactive;
    }

    @DesignerProperty(defaultValue = "&HFFCCCCCC", editorType = "color")
    @SimpleProperty
    public void ThumbColorInactive(int argb) {
        this.thumbColorInactive = argb;
        if (this.switchView != null) {
            DrawableCompat.setTintList(this.switchView.getThumbDrawable(), createSwitchColors(this.thumbColorActive, argb));
            this.view.invalidate();
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
    public int TrackColorActive() {
        return this.trackColorActive;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = true)
    public int TrackColorInactive() {
        return this.trackColorInactive;
    }

    @DesignerProperty(defaultValue = "&HFF00FF00", editorType = "color")
    @SimpleProperty(description = "Color of the toggle track when switched on", userVisible = true)
    public void TrackColorActive(int argb) {
        this.trackColorActive = argb;
        if (this.switchView != null) {
            DrawableCompat.setTintList(this.switchView.getTrackDrawable(), createSwitchColors(argb, this.trackColorInactive));
            this.view.invalidate();
        }
    }

    @DesignerProperty(defaultValue = "&HFF444444", editorType = "color")
    @SimpleProperty(description = "Color of the toggle track when switched off", userVisible = true)
    public void TrackColorInactive(int argb) {
        this.trackColorInactive = argb;
        if (this.switchView != null) {
            DrawableCompat.setTintList(this.switchView.getTrackDrawable(), createSwitchColors(this.trackColorActive, argb));
            this.view.invalidate();
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean On() {
        return this.view.isChecked();
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty
    public void On(boolean value) {
        this.view.setChecked(value);
        this.view.invalidate();
    }

    @SimpleEvent(description = "User change the state of the `Switch` from On to Off or back.")
    public void Changed() {
        super.Changed();
    }
}
