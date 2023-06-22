package com.google.appinventor.components.runtime;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.TextViewUtil;

@SimpleObject
@DesignerComponent(category = ComponentCategory.USERINTERFACE, description = "A Label displays a piece of text, which is specified through the <code>Text</code> property.  Other properties, all of which can be set in the Designer or Blocks Editor, control the appearance and placement of the text.", version = 5)
public final class Label extends AndroidViewComponent implements AccessibleComponent {
    private static final int DEFAULT_LABEL_MARGIN = 2;
    private int backgroundColor;
    private boolean bold;
    private int defaultLabelMarginInDp = 0;
    private String fontTypeface;
    private boolean hasMargins;
    private String htmlContent;
    private boolean htmlFormat;
    private boolean isBigText = false;
    private boolean italic;
    private final LinearLayout.LayoutParams linearLayoutParams;
    private int textAlignment;
    private int textColor;
    private final TextView view;

    public Label(ComponentContainer container) {
        super(container);
        this.view = new TextView(container.$context());
        container.$add(this);
        ViewGroup.LayoutParams lp = this.view.getLayoutParams();
        if (lp instanceof LinearLayout.LayoutParams) {
            this.linearLayoutParams = (LinearLayout.LayoutParams) lp;
            this.defaultLabelMarginInDp = dpToPx(this.view, 2);
        } else {
            this.defaultLabelMarginInDp = 0;
            this.linearLayoutParams = null;
            Log.e("Label", "Error: The label's view does not have linear layout parameters");
            new RuntimeException().printStackTrace();
        }
        TextAlignment(0);
        BackgroundColor(16777215);
        this.fontTypeface = Component.TYPEFACE_DEFAULT;
        TextViewUtil.setFontTypeface(container.$form(), this.view, this.fontTypeface, this.bold, this.italic);
        FontSize(14.0f);
        Text("");
        TextColor(0);
        HTMLFormat(false);
        HasMargins(true);
    }

    private static int dpToPx(View view2, int dp) {
        return Math.round(((float) dp) * view2.getContext().getResources().getDisplayMetrics().density);
    }

    public View getView() {
        return this.view;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public int TextAlignment() {
        return this.textAlignment;
    }

    @DesignerProperty(defaultValue = "0", editorType = "textalignment")
    @SimpleProperty(userVisible = false)
    public void TextAlignment(int alignment) {
        this.textAlignment = alignment;
        TextViewUtil.setAlignment(this.view, alignment, false);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public int BackgroundColor() {
        return this.backgroundColor;
    }

    @DesignerProperty(defaultValue = "&H00FFFFFF", editorType = "color")
    @SimpleProperty
    public void BackgroundColor(int argb) {
        this.backgroundColor = argb;
        if (argb != 0) {
            TextViewUtil.setBackgroundColor(this.view, argb);
        } else {
            TextViewUtil.setBackgroundColor(this.view, 16777215);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public boolean FontBold() {
        return this.bold;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(userVisible = false)
    public void FontBold(boolean bold2) {
        this.bold = bold2;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, bold2, this.italic);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public boolean FontItalic() {
        return this.italic;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(userVisible = false)
    public void FontItalic(boolean italic2) {
        this.italic = italic2;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, this.bold, italic2);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Reports whether or not the label appears with margins.  All four margins (left, right, top, bottom) are the same.  This property has no effect in the designer, where labels are always shown with margins.", userVisible = true)
    public boolean HasMargins() {
        return this.hasMargins;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(userVisible = true)
    public void HasMargins(boolean hasMargins2) {
        this.hasMargins = hasMargins2;
        setLabelMargins(hasMargins2);
    }

    private void setLabelMargins(boolean hasMargins2) {
        int m = hasMargins2 ? this.defaultLabelMarginInDp : 0;
        this.linearLayoutParams.setMargins(m, m, m, m);
        this.view.invalidate();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public float FontSize() {
        return TextViewUtil.getFontSize(this.view, this.container.$context());
    }

    @DesignerProperty(defaultValue = "14.0", editorType = "non_negative_float")
    @SimpleProperty
    public void FontSize(float size) {
        if (size != 14.0f || (!this.isBigText && !this.container.$form().BigDefaultText())) {
            TextViewUtil.setFontSize(this.view, size);
        } else {
            TextViewUtil.setFontSize(this.view, 24.0f);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, userVisible = false)
    public String FontTypeface() {
        return this.fontTypeface;
    }

    @DesignerProperty(defaultValue = "0", editorType = "typeface")
    @SimpleProperty(userVisible = false)
    public void FontTypeface(String typeface) {
        this.fontTypeface = typeface;
        TextViewUtil.setFontTypeface(this.container.$form(), this.view, this.fontTypeface, this.bold, this.italic);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public String Text() {
        return TextViewUtil.getText(this.view);
    }

    @DesignerProperty(defaultValue = "", editorType = "textArea")
    @SimpleProperty
    public void Text(String text) {
        if (this.htmlFormat) {
            TextViewUtil.setTextHTML(this.view, text);
        } else {
            TextViewUtil.setText(this.view, text);
        }
        this.htmlContent = text;
    }

    @SimpleProperty
    public String HTMLContent() {
        if (this.htmlFormat) {
            return this.htmlContent;
        }
        return TextViewUtil.getText(this.view);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "If true, then this label will show html text else it will show plain text. Note: Not all HTML is supported.")
    public boolean HTMLFormat() {
        return this.htmlFormat;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(userVisible = false)
    public void HTMLFormat(boolean fmt) {
        this.htmlFormat = fmt;
        if (this.htmlFormat) {
            TextViewUtil.setTextHTML(this.view, TextViewUtil.getText(this.view));
            return;
        }
        TextViewUtil.setText(this.view, TextViewUtil.getText(this.view));
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public int TextColor() {
        return this.textColor;
    }

    @DesignerProperty(defaultValue = "&HFF000000", editorType = "color")
    @SimpleProperty
    public void TextColor(int argb) {
        this.textColor = argb;
        if (argb != 0) {
            TextViewUtil.setTextColor(this.view, argb);
        } else {
            TextViewUtil.setTextColor(this.view, this.container.$form().isDarkTheme() ? -1 : -16777216);
        }
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
}
