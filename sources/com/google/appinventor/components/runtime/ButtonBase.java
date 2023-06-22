package com.google.appinventor.components.runtime;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.runtime.util.IceCreamSandwichUtil;
import com.google.appinventor.components.runtime.util.KitkatUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.TextViewUtil;
import com.google.appinventor.components.runtime.util.ViewUtil;
import java.io.IOException;

@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public abstract class ButtonBase extends AndroidViewComponent implements View.OnClickListener, View.OnFocusChangeListener, View.OnLongClickListener, View.OnTouchListener, AccessibleComponent {
    private static final String LOG_TAG = "ButtonBase";
    private static final float[] ROUNDED_CORNERS_ARRAY = {10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f};
    private static final float ROUNDED_CORNERS_RADIUS = 10.0f;
    private static final int SHAPED_DEFAULT_BACKGROUND_COLOR = -3355444;
    private static int defaultButtonMinHeight = 0;
    private static int defaultButtonMinWidth = 0;
    private int backgroundColor;
    private Drawable backgroundImageDrawable;
    private boolean bold;
    private Drawable defaultButtonDrawable;
    private ColorStateList defaultColorStateList;
    private String fontTypeface;
    private String imagePath = "";
    private boolean isBigText = false;
    private boolean isHighContrast = false;
    private boolean italic;
    private Drawable myBackgroundDrawable = null;
    private int shape;
    private boolean showFeedback = true;
    private int textAlignment;
    private int textColor;
    private final Button view;

    public abstract void click();

    public ButtonBase(ComponentContainer container) {
        super(container);
        this.view = new Button(container.$context());
        this.defaultButtonDrawable = this.view.getBackground();
        this.defaultColorStateList = this.view.getTextColors();
        defaultButtonMinWidth = KitkatUtil.getMinWidth(this.view);
        defaultButtonMinHeight = KitkatUtil.getMinHeight(this.view);
        container.$add(this);
        this.view.setOnClickListener(this);
        this.view.setOnFocusChangeListener(this);
        this.view.setOnLongClickListener(this);
        this.view.setOnTouchListener(this);
        IceCreamSandwichUtil.setAllCaps(this.view, false);
        TextAlignment(1);
        BackgroundColor(0);
        Image("");
        Enabled(true);
        this.fontTypeface = Component.TYPEFACE_DEFAULT;
        TextViewUtil.setFontTypeface(container.$form(), this.view, this.fontTypeface, this.bold, this.italic);
        FontSize(14.0f);
        Text("");
        TextColor(0);
        Shape(0);
    }

    public void Initialize() {
        updateAppearance();
    }

    public boolean onTouch(View view2, MotionEvent me) {
        if (me.getAction() == 0) {
            if (ShowFeedback() && (AppInventorCompatActivity.isClassicMode() || Build.VERSION.SDK_INT < 21)) {
                view2.getBackground().setAlpha(70);
                view2.invalidate();
            }
            TouchDown();
            return false;
        } else if (me.getAction() != 1 && me.getAction() != 3) {
            return false;
        } else {
            if (ShowFeedback() && (AppInventorCompatActivity.isClassicMode() || Build.VERSION.SDK_INT < 21)) {
                view2.getBackground().setAlpha(255);
                view2.invalidate();
            }
            TouchUp();
            return false;
        }
    }

    public View getView() {
        return this.view;
    }

    @SimpleEvent(description = "Indicates that the %type% was pressed down.")
    public void TouchDown() {
        EventDispatcher.dispatchEvent(this, "TouchDown", new Object[0]);
    }

    @SimpleEvent(description = "Indicates that the %type% has been released.")
    public void TouchUp() {
        EventDispatcher.dispatchEvent(this, "TouchUp", new Object[0]);
    }

    @SimpleEvent(description = "Indicates the cursor moved over the %type% so it is now possible to click it.")
    public void GotFocus() {
        EventDispatcher.dispatchEvent(this, "GotFocus", new Object[0]);
    }

    @SimpleEvent(description = "Indicates the cursor moved away from the %type% so it is now no longer possible to click it.")
    public void LostFocus() {
        EventDispatcher.dispatchEvent(this, "LostFocus", new Object[0]);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Left, center, or right.", userVisible = false)
    public int TextAlignment() {
        return this.textAlignment;
    }

    @DesignerProperty(defaultValue = "1", editorType = "textalignment")
    @SimpleProperty(userVisible = false)
    public void TextAlignment(int alignment) {
        this.textAlignment = alignment;
        TextViewUtil.setAlignment(this.view, alignment, true);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public int Shape() {
        return this.shape;
    }

    @DesignerProperty(defaultValue = "0", editorType = "button_shape")
    @SimpleProperty(description = "Specifies the shape of the %type% (default, rounded, rectangular, oval). The shape will not be visible if an Image is being displayed.", userVisible = false)
    public void Shape(int shape2) {
        this.shape = shape2;
        updateAppearance();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Image to display on button.")
    public String Image() {
        return this.imagePath;
    }

    @DesignerProperty(defaultValue = "", editorType = "asset")
    @SimpleProperty(description = "Specifies the path of the image of the %type%.  If there is both an Image and a BackgroundColor, only the Image will be visible.")
    public void Image(@Asset String path) {
        if (!path.equals(this.imagePath) || this.backgroundImageDrawable == null) {
            if (path == null) {
                path = "";
            }
            this.imagePath = path;
            this.backgroundImageDrawable = null;
            if (this.imagePath.length() > 0) {
                try {
                    this.backgroundImageDrawable = MediaUtil.getBitmapDrawable(this.container.$form(), this.imagePath);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Unable to load " + this.imagePath);
                }
            }
            updateAppearance();
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the button's background color")
    public int BackgroundColor() {
        return this.backgroundColor;
    }

    @DesignerProperty(defaultValue = "&H00000000", editorType = "color")
    @SimpleProperty(description = "Specifies the background color of the %type%. The background color will not be visible if an Image is being displayed.")
    public void BackgroundColor(int argb) {
        this.backgroundColor = argb;
        updateAppearance();
    }

    private void updateAppearance() {
        if (this.backgroundImageDrawable == null) {
            if (this.shape != 0) {
                setShape();
            } else if (this.backgroundColor == 0) {
                if (this.isHighContrast || this.container.$form().HighContrast()) {
                    ViewUtil.setBackgroundDrawable(this.view, (Drawable) null);
                    ViewUtil.setBackgroundDrawable(this.view, getSafeBackgroundDrawable());
                    this.view.getBackground().setColorFilter(-16777216, PorterDuff.Mode.SRC_ATOP);
                } else {
                    ViewUtil.setBackgroundDrawable(this.view, this.defaultButtonDrawable);
                }
            } else if (this.backgroundColor == 16777215) {
                ViewUtil.setBackgroundDrawable(this.view, (Drawable) null);
                ViewUtil.setBackgroundDrawable(this.view, getSafeBackgroundDrawable());
                this.view.getBackground().setColorFilter(this.backgroundColor, PorterDuff.Mode.CLEAR);
            } else {
                ViewUtil.setBackgroundDrawable(this.view, (Drawable) null);
                ViewUtil.setBackgroundDrawable(this.view, getSafeBackgroundDrawable());
                this.view.getBackground().setColorFilter(this.backgroundColor, PorterDuff.Mode.SRC_ATOP);
            }
            TextViewUtil.setMinSize(this.view, defaultButtonMinWidth, defaultButtonMinHeight);
            return;
        }
        ViewUtil.setBackgroundImage(this.view, this.backgroundImageDrawable);
        TextViewUtil.setMinSize(this.view, 0, 0);
    }

    private Drawable getSafeBackgroundDrawable() {
        if (this.myBackgroundDrawable == null) {
            Drawable.ConstantState state = this.defaultButtonDrawable.getConstantState();
            if (state == null || Build.VERSION.SDK_INT < 10) {
                this.myBackgroundDrawable = this.defaultButtonDrawable;
            } else {
                try {
                    this.myBackgroundDrawable = state.newDrawable().mutate();
                } catch (NullPointerException e) {
                    Log.e(LOG_TAG, "Unable to clone button drawable", e);
                    this.myBackgroundDrawable = this.defaultButtonDrawable;
                }
            }
        }
        return this.myBackgroundDrawable;
    }

    private ColorStateList createRippleState() {
        int[][] states = {new int[]{16842910}};
        int enabled_color = this.defaultColorStateList.getColorForState(this.view.getDrawableState(), 16842910);
        return new ColorStateList(states, new int[]{Color.argb(70, Color.red(enabled_color), Color.green(enabled_color), Color.blue(enabled_color))});
    }

    private void setShape() {
        ShapeDrawable drawable = new ShapeDrawable();
        switch (this.shape) {
            case 1:
                drawable.setShape(new RoundRectShape(ROUNDED_CORNERS_ARRAY, (RectF) null, (float[]) null));
                break;
            case 2:
                drawable.setShape(new RectShape());
                break;
            case 3:
                drawable.setShape(new OvalShape());
                break;
            default:
                throw new IllegalArgumentException();
        }
        if (AppInventorCompatActivity.isClassicMode() || Build.VERSION.SDK_INT < 21) {
            ViewUtil.setBackgroundDrawable(this.view, drawable);
        } else {
            ViewUtil.setBackgroundDrawable(this.view, new RippleDrawable(createRippleState(), drawable, drawable));
        }
        if (this.backgroundColor == 16777215) {
            this.view.getBackground().setColorFilter(this.backgroundColor, PorterDuff.Mode.CLEAR);
        } else if (this.backgroundColor != 0) {
            this.view.getBackground().setColorFilter(this.backgroundColor, PorterDuff.Mode.SRC_ATOP);
        } else if (this.isHighContrast || this.container.$form().HighContrast()) {
            this.view.getBackground().setColorFilter(-16777216, PorterDuff.Mode.SRC_ATOP);
        } else {
            this.view.getBackground().setColorFilter(-3355444, PorterDuff.Mode.SRC_ATOP);
        }
        this.view.invalidate();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "If set, user can tap %type% to cause action.")
    public boolean Enabled() {
        return TextViewUtil.isEnabled(this.view);
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void Enabled(boolean enabled) {
        TextViewUtil.setEnabled(this.view, enabled);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "If set, %type% text is displayed in bold.")
    public boolean FontBold() {
        return this.bold;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public void FontBold(boolean bold2) {
        this.bold = bold2;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, bold2, this.italic);
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(description = "Specifies if a visual feedback should be shown  for a %type% that has an image as background.")
    public void ShowFeedback(boolean showFeedback2) {
        this.showFeedback = showFeedback2;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the visual feedback state of the %type%")
    public boolean ShowFeedback() {
        return this.showFeedback;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "If set, %type% text is displayed in italics.")
    public boolean FontItalic() {
        return this.italic;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public void FontItalic(boolean italic2) {
        this.italic = italic2;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, this.bold, italic2);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Point size for %type% text.")
    public float FontSize() {
        return TextViewUtil.getFontSize(this.view, this.container.$context());
    }

    @DesignerProperty(defaultValue = "14.0", editorType = "non_negative_float")
    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public void FontSize(float size) {
        if (size != 14.0f || !this.container.$form().BigDefaultText()) {
            TextViewUtil.setFontSize(this.view, size);
        } else {
            TextViewUtil.setFontSize(this.view, 24.0f);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Font family for %type% text.", userVisible = false)
    public String FontTypeface() {
        return this.fontTypeface;
    }

    @DesignerProperty(defaultValue = "0", editorType = "typeface")
    @SimpleProperty(userVisible = false)
    public void FontTypeface(String typeface) {
        this.fontTypeface = typeface;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, this.bold, this.italic);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Text to display on %type%.")
    public String Text() {
        return TextViewUtil.getText(this.view);
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void Text(String text) {
        TextViewUtil.setText(this.view, text);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Color for button text.")
    public int TextColor() {
        return this.textColor;
    }

    @DesignerProperty(defaultValue = "&H00000000", editorType = "color")
    @SimpleProperty
    public void TextColor(int argb) {
        this.textColor = argb;
        if (argb != 0) {
            TextViewUtil.setTextColor(this.view, argb);
        } else if (this.isHighContrast || this.container.$form().HighContrast()) {
            TextViewUtil.setTextColor(this.view, -1);
        } else {
            TextViewUtil.setTextColors(this.view, this.defaultColorStateList);
        }
    }

    public boolean longClick() {
        return false;
    }

    public void onClick(View view2) {
        click();
    }

    public void onFocusChange(View previouslyFocused, boolean gainFocus) {
        if (gainFocus) {
            GotFocus();
        } else {
            LostFocus();
        }
    }

    public boolean onLongClick(View view2) {
        return longClick();
    }

    public void setHighContrast(boolean isHighContrast2) {
        if (this.backgroundImageDrawable == null && this.shape == 0 && this.backgroundColor == 0) {
            if (isHighContrast2) {
                ViewUtil.setBackgroundDrawable(this.view, (Drawable) null);
                ViewUtil.setBackgroundDrawable(this.view, getSafeBackgroundDrawable());
                this.view.getBackground().setColorFilter(-16777216, PorterDuff.Mode.SRC_ATOP);
            } else {
                ViewUtil.setBackgroundDrawable(this.view, this.defaultButtonDrawable);
            }
        }
        if (this.textColor != 0) {
            return;
        }
        if (isHighContrast2) {
            TextViewUtil.setTextColor(this.view, -1);
        } else {
            TextViewUtil.setTextColors(this.view, this.defaultColorStateList);
        }
    }

    public boolean getHighContrast() {
        return this.isHighContrast;
    }

    public void setLargeFont(boolean isLargeFont) {
        if (((double) TextViewUtil.getFontSize(this.view, this.container.$context())) != 24.0d && TextViewUtil.getFontSize(this.view, this.container.$context()) != 14.0f) {
            return;
        }
        if (isLargeFont) {
            TextViewUtil.setFontSize(this.view, 24.0f);
        } else {
            TextViewUtil.setFontSize(this.view, 14.0f);
        }
    }

    public boolean getLargeFont() {
        return this.isBigText;
    }
}
