package com.sunny.CornerRadius;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

@SimpleObject(external = true)
@DesignerComponent(category = ComponentCategory.EXTENSION, description = "An utils extension for HTML <br> Developed by Sunny Gupta ", iconName = "https://res.cloudinary.com/andromedaviewflyvipul/image/upload/c_scale,w_20/v1571472765/ktvu4bapylsvnykoyhdm.png", nonVisible = true, version = 1)
public class CornerRadius extends AndroidNonvisibleComponent {
    public CornerRadius(ComponentContainer componentContainer) {
        super(componentContainer.$form());
    }

    @SimpleFunction
    public void SetCornerRadius(AndroidViewComponent androidViewComponent, int i, int i2, int i3, int i4, int i5) {
        View view = androidViewComponent.getView();
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(i);
        gradientDrawable.setCornerRadii(new float[]{(float) p2d(i2), (float) p2d(i2), (float) p2d(i3), (float) p2d(i3), (float) p2d(i5), (float) p2d(i5), (float) p2d(i4), (float) p2d(i4)});
        view.setBackgroundDrawable(gradientDrawable);
    }

    public int p2d(int i) {
        return Math.round(((float) i) * this.form.deviceDensity());
    }
}
