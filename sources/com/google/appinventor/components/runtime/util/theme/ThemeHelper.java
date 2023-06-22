package com.google.appinventor.components.runtime.util.theme;

public interface ThemeHelper {
    boolean hasActionBar();

    void requestActionBar();

    void setActionBarAnimation(boolean z);

    boolean setActionBarVisible(boolean z);

    void setTitle(String str);

    void setTitle(String str, boolean z);
}
