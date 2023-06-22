package com.google.appinventor.components.runtime.util.theme;

import android.text.Html;
import androidx.appcompat.app.ActionBar;
import com.google.appinventor.components.runtime.AppInventorCompatActivity;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.ImageViewUtil;

public class IceCreamSandwichThemeHelper implements ThemeHelper {
    private final AppInventorCompatActivity activity;

    public IceCreamSandwichThemeHelper(AppInventorCompatActivity activity2) {
        this.activity = activity2;
    }

    public void requestActionBar() {
        this.activity.getWindow().requestFeature(8);
    }

    public boolean setActionBarVisible(boolean visible) {
        ActionBar actionBar = this.activity.getSupportActionBar();
        if (actionBar == null) {
            if (this.activity instanceof Form) {
                ((Form) this.activity).dispatchErrorOccurredEvent((Form) this.activity, "ActionBar", ErrorMessages.ERROR_ACTIONBAR_NOT_SUPPORTED, new Object[0]);
            }
            return false;
        }
        if (visible) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
        return true;
    }

    public boolean hasActionBar() {
        return this.activity.getSupportActionBar() != null;
    }

    public void setTitle(String title) {
        ActionBar actionBar = this.activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle((CharSequence) title);
        }
    }

    public void setActionBarAnimation(boolean enabled) {
        ActionBar actionBar = this.activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(enabled);
        }
    }

    public void setTitle(String title, boolean black) {
        ActionBar actionBar = this.activity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        if (black) {
            actionBar.setTitle((CharSequence) Html.fromHtml("<font color=\"black\">" + title + "</font>"));
            ImageViewUtil.setMenuButtonColor(this.activity, -16777216);
            return;
        }
        actionBar.setTitle((CharSequence) title);
        ImageViewUtil.setMenuButtonColor(this.activity, -1);
    }
}
