package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.widget.TextView;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ReplForm;
import java.io.File;

public class TextViewUtil {
    private TextViewUtil() {
    }

    public static void setAlignment(TextView textview, int alignment, boolean centerVertically) {
        int horizontalGravity;
        switch (alignment) {
            case 0:
                horizontalGravity = 3;
                break;
            case 1:
                horizontalGravity = 1;
                break;
            case 2:
                horizontalGravity = 5;
                break;
            default:
                throw new IllegalArgumentException();
        }
        textview.setGravity(horizontalGravity | (centerVertically ? 16 : 48));
        textview.invalidate();
    }

    public static void setBackgroundColor(TextView textview, int argb) {
        textview.setBackgroundColor(argb);
        textview.invalidate();
    }

    public static boolean isEnabled(TextView textview) {
        return textview.isEnabled();
    }

    public static void setEnabled(TextView textview, boolean enabled) {
        textview.setEnabled(enabled);
        textview.invalidate();
    }

    public static float getFontSize(TextView textview, Context context) {
        return textview.getTextSize() / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static void setFontSize(TextView textview, float size) {
        textview.setTextSize(size);
        textview.requestLayout();
    }

    public static void setFontTypeface(Form form, TextView textview, String typeface, boolean bold, boolean italic) {
        Typeface tf;
        if (typeface.equals(Component.TYPEFACE_DEFAULT)) {
            tf = Typeface.DEFAULT;
        } else if (typeface.equals(Component.TYPEFACE_SANSSERIF)) {
            tf = Typeface.SANS_SERIF;
        } else if (typeface.equals(Component.TYPEFACE_SERIF)) {
            tf = Typeface.SERIF;
        } else if (typeface.equals(Component.TYPEFACE_MONOSPACE)) {
            tf = Typeface.MONOSPACE;
        } else {
            tf = getTypeFace(form, typeface);
        }
        int style = 0;
        if (bold) {
            style = 0 | 1;
        }
        if (italic) {
            style |= 2;
        }
        textview.setTypeface(Typeface.create(tf, style));
        textview.requestLayout();
    }

    public static Typeface getTypeFace(Form form, String fontFile) {
        if (fontFile == null || fontFile.isEmpty()) {
            return null;
        }
        if (fontFile.contains("/")) {
            return Typeface.createFromFile(new File(fontFile));
        }
        if (!(form instanceof ReplForm)) {
            return Typeface.createFromAsset(form.$context().getAssets(), fontFile);
        }
        if (Build.VERSION.SDK_INT > 28) {
            return Typeface.createFromFile(new File("/storage/emulated/0/Android/data/edu.mit.appinventor.aicompanion3/files/assets/" + fontFile));
        }
        return Typeface.createFromFile(new File("/storage/emulated/0/AppInventor/assets/" + fontFile));
    }

    public static String getText(TextView textview) {
        return textview.getText().toString();
    }

    public static void setTextHTML(TextView textview, String text) {
        textview.setText(Html.fromHtml(text));
        textview.requestLayout();
    }

    public static void setText(TextView textview, String text) {
        textview.setText(text);
        textview.requestLayout();
    }

    public static void setPadding(TextView textview, int padding) {
        textview.setPadding(padding, padding, 0, 0);
        textview.requestLayout();
    }

    public static void setTextColor(TextView textview, int argb) {
        textview.setTextColor(argb);
        textview.invalidate();
    }

    public static void setTextColors(TextView textview, ColorStateList colorStateList) {
        textview.setTextColor(colorStateList);
    }

    public static void setMinWidth(TextView textview, int minWidth) {
        textview.setMinWidth(minWidth);
        textview.setMinimumWidth(minWidth);
    }

    public static void setMinHeight(TextView textview, int minHeight) {
        textview.setMinHeight(minHeight);
        textview.setMinimumHeight(minHeight);
    }

    public static void setMinSize(TextView textview, int minWidth, int minHeight) {
        setMinWidth(textview, minWidth);
        setMinHeight(textview, minHeight);
    }
}
