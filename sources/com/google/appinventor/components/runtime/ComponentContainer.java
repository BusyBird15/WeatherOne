package com.google.appinventor.components.runtime;

import android.app.Activity;
import java.util.List;

public interface ComponentContainer {
    void $add(AndroidViewComponent androidViewComponent);

    Activity $context();

    Form $form();

    int Height();

    int Width();

    List<? extends Component> getChildren();

    void setChildHeight(AndroidViewComponent androidViewComponent, int i);

    void setChildWidth(AndroidViewComponent androidViewComponent, int i);
}
