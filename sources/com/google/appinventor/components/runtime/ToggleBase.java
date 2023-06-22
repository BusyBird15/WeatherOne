package com.google.appinventor.components.runtime;

import android.view.View;
import android.widget.CompoundButton;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.util.TextViewUtil;

@SimpleObject
public abstract class ToggleBase<T extends CompoundButton> extends AndroidViewComponent implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, AccessibleComponent {
    private int backgroundColor;
    private boolean bold;
    private String fontTypeface;
    private boolean isBigText = false;
    private boolean italic;
    private int textColor;
    protected T view;

    public ToggleBase(ComponentContainer container) {
        super(container);
    }

    /* access modifiers changed from: protected */
    public void initToggle() {
        this.view.setOnFocusChangeListener(this);
        this.view.setOnCheckedChangeListener(this);
        this.container.$add(this);
        BackgroundColor(16777215);
        Enabled(true);
        this.fontTypeface = Component.TYPEFACE_DEFAULT;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, this.bold, this.italic);
        FontSize(14.0f);
        Text("");
        TextColor(0);
    }

    public View getView() {
        return this.view;
    }

    public void setHighContrast(boolean isHighContrast) {
    }

    public boolean getHighContrast() {
        return false;
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

    @SimpleEvent(description = "User tapped and released the %type%.")
    public void Changed() {
        EventDispatcher.dispatchEvent(this, "Changed", new Object[0]);
    }

    @SimpleEvent(description = "%type% became the focused component.")
    public void GotFocus() {
        EventDispatcher.dispatchEvent(this, "GotFocus", new Object[0]);
    }

    @SimpleEvent(description = "%type% stopped being the focused component.")
    public void LostFocus() {
        EventDispatcher.dispatchEvent(this, "LostFocus", new Object[0]);
    }

    @DesignerProperty(defaultValue = "&H00FFFFFF", editorType = "color")
    @SimpleProperty(description = "The background color of the %type% as an alpha-red-green-blue integer.")
    public void BackgroundColor(int argb) {
        this.backgroundColor = argb;
        if (argb != 0) {
            TextViewUtil.setBackgroundColor(this.view, argb);
        } else {
            TextViewUtil.setBackgroundColor(this.view, 16777215);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public int BackgroundColor() {
        return this.backgroundColor;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(description = "True if the %type% is active and clickable.")
    public void Enabled(boolean enabled) {
        TextViewUtil.setEnabled(this.view, enabled);
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean Enabled() {
        return this.view.isEnabled();
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(description = "Set to true if the text of the %type% should be bold.", userVisible = false)
    public void FontBold(boolean bold2) {
        this.bold = bold2;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, bold2, this.italic);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public boolean FontBold() {
        return this.bold;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(description = "Set to true if the text of the %type% should be italic.", userVisible = false)
    public void FontItalic(boolean italic2) {
        this.italic = italic2;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, this.bold, italic2);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public boolean FontItalic() {
        return this.italic;
    }

    @DesignerProperty(defaultValue = "14.0", editorType = "non_negative_float")
    @SimpleProperty(description = "Specifies the text font size of the %type% in scale-independent pixels.")
    public void FontSize(float size) {
        if (((double) Math.abs(size - 14.0f)) >= 0.01d && ((double) Math.abs(size - 24.0f)) >= 0.01d) {
            TextViewUtil.setFontSize(this.view, size);
        } else if (this.isBigText || this.container.$form().BigDefaultText()) {
            TextViewUtil.setFontSize(this.view, 24.0f);
        } else {
            TextViewUtil.setFontSize(this.view, 14.0f);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public float FontSize() {
        return TextViewUtil.getFontSize(this.view, this.container.$context());
    }

    @DesignerProperty(defaultValue = "0", editorType = "typeface")
    @SimpleProperty(description = "Specifies the text font face of the %type%.", userVisible = false)
    public void FontTypeface(String typeface) {
        this.fontTypeface = typeface;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, this.bold, this.italic);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public String FontTypeface() {
        return this.fontTypeface;
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(description = "Specifies the text displayed by the %type%.")
    public void Text(String text) {
        TextViewUtil.setText(this.view, text);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public String Text() {
        return TextViewUtil.getText(this.view);
    }

    @DesignerProperty(defaultValue = "&HFF000000", editorType = "color")
    @SimpleProperty(description = "Specifies the text color of the %type% as an alpha-red-green-blue integer.")
    public void TextColor(int argb) {
        this.textColor = argb;
        if (argb != 0) {
            TextViewUtil.setTextColor(this.view, argb);
        } else {
            TextViewUtil.setTextColor(this.view, this.container.$form().isDarkTheme() ? -1 : -16777216);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public int TextColor() {
        return this.textColor;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Changed();
    }

    public void onFocusChange(View previouslyFocused, boolean gainFocus) {
        if (gainFocus) {
            GotFocus();
        } else {
            LostFocus();
        }
    }
}
