package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.HorizontalAlignment;
import com.google.appinventor.components.common.VerticalAlignment;
import com.google.appinventor.components.runtime.util.AlignmentUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.ViewUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SimpleObject
public class HVArrangement extends AndroidViewComponent implements Component, ComponentContainer {
    private static final String LOG_TAG = "HVArrangement";
    private AlignmentUtil alignmentSetter;
    private List<Component> allChildren = new ArrayList();
    private final Handler androidUIHandler = new Handler();
    private int backgroundColor;
    private Drawable backgroundImageDrawable;
    private final Activity context;
    private Drawable defaultButtonDrawable;
    private ViewGroup frameContainer;
    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.Left;
    private String imagePath = "";
    private final int orientation;
    private boolean scrollable = false;
    private VerticalAlignment verticalAlignment = VerticalAlignment.Top;
    private final LinearLayout viewLayout;

    public HVArrangement(ComponentContainer container, int orientation2, boolean scrollable2) {
        super(container);
        this.context = container.$context();
        this.orientation = orientation2;
        this.scrollable = scrollable2;
        this.viewLayout = new LinearLayout(this.context, orientation2, 100, 100);
        this.viewLayout.setBaselineAligned(false);
        this.alignmentSetter = new AlignmentUtil(this.viewLayout);
        this.alignmentSetter.setHorizontalAlignment(this.horizontalAlignment);
        this.alignmentSetter.setVerticalAlignment(this.verticalAlignment);
        if (scrollable2) {
            switch (orientation2) {
                case 0:
                    Log.d(LOG_TAG, "Setting up frameContainer = ScrollView()");
                    this.frameContainer = new ScrollView(this.context);
                    break;
                case 1:
                    Log.d(LOG_TAG, "Setting up frameContainer = HorizontalScrollView()");
                    this.frameContainer = new HorizontalScrollView(this.context);
                    break;
            }
        } else {
            Log.d(LOG_TAG, "Setting up frameContainer = FrameLayout()");
            this.frameContainer = new FrameLayout(this.context);
        }
        this.frameContainer.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        this.frameContainer.addView(this.viewLayout.getLayoutManager(), new ViewGroup.LayoutParams(-1, -1));
        this.defaultButtonDrawable = getView().getBackground();
        container.$add(this);
        BackgroundColor(0);
    }

    public Activity $context() {
        return this.context;
    }

    public Form $form() {
        return this.container.$form();
    }

    public void $add(AndroidViewComponent component) {
        this.viewLayout.add(component);
        this.allChildren.add(component);
    }

    public List<? extends Component> getChildren() {
        return this.allChildren;
    }

    public void setChildWidth(AndroidViewComponent component, int width) {
        setChildWidth(component, width, 0);
    }

    public void setChildWidth(final AndroidViewComponent component, int width, final int trycount) {
        int cWidth = this.container.$form().Width();
        if (cWidth == 0 && trycount < 2) {
            final int fWidth = width;
            this.androidUIHandler.postDelayed(new Runnable() {
                public void run() {
                    Log.d(HVArrangement.LOG_TAG, "(HVArrangement)Width not stable yet... trying again");
                    HVArrangement.this.setChildWidth(component, fWidth, trycount + 1);
                }
            }, 100);
        }
        if (width <= -1000) {
            Log.d(LOG_TAG, "HVArrangement.setChildWidth(): width = " + width + " parent Width = " + cWidth + " child = " + component);
            width = ((-(width + 1000)) * cWidth) / 100;
        }
        component.setLastWidth(width);
        if (this.orientation == 1) {
            ViewUtil.setChildWidthForHorizontalLayout(component.getView(), width);
        } else {
            ViewUtil.setChildWidthForVerticalLayout(component.getView(), width);
        }
    }

    public void setChildHeight(final AndroidViewComponent component, int height) {
        int cHeight = this.container.$form().Height();
        if (cHeight == 0) {
            final int fHeight = height;
            this.androidUIHandler.postDelayed(new Runnable() {
                public void run() {
                    Log.d(HVArrangement.LOG_TAG, "(HVArrangement)Height not stable yet... trying again");
                    HVArrangement.this.setChildHeight(component, fHeight);
                }
            }, 100);
        }
        if (height <= -1000) {
            height = ((-(height + 1000)) * cHeight) / 100;
        }
        component.setLastHeight(height);
        if (this.orientation == 1) {
            ViewUtil.setChildHeightForHorizontalLayout(component.getView(), height);
        } else {
            ViewUtil.setChildHeightForVerticalLayout(component.getView(), height);
        }
    }

    public View getView() {
        return this.frameContainer;
    }

    @Options(HorizontalAlignment.class)
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "A number that encodes how contents of the %type% are aligned  horizontally. The choices are: 1 = left aligned, 2 = right aligned,  3 = horizontally centered.  Alignment has no effect if the arrangement's width is automatic.")
    public int AlignHorizontal() {
        return AlignHorizontalAbstract().toUnderlyingValue().intValue();
    }

    public HorizontalAlignment AlignHorizontalAbstract() {
        return this.horizontalAlignment;
    }

    public void AlignHorizontalAbstract(HorizontalAlignment alignment) {
        this.alignmentSetter.setHorizontalAlignment(alignment);
        this.horizontalAlignment = alignment;
    }

    @DesignerProperty(defaultValue = "1", editorType = "horizontal_alignment")
    @SimpleProperty
    public void AlignHorizontal(@Options(HorizontalAlignment.class) int alignment) {
        HorizontalAlignment align = HorizontalAlignment.fromUnderlyingValue(Integer.valueOf(alignment));
        if (align == null) {
            this.container.$form().dispatchErrorOccurredEvent(this, "HorizontalAlignment", ErrorMessages.ERROR_BAD_VALUE_FOR_HORIZONTAL_ALIGNMENT, Integer.valueOf(alignment));
            return;
        }
        AlignHorizontalAbstract(align);
    }

    @Options(VerticalAlignment.class)
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "A number that encodes how the contents of the %type% are aligned  vertically. The choices are: 1 = aligned at the top, 2 = vertically centered, 3 = aligned at the bottom.  Alignment has no effect if the arrangement's height is automatic.")
    public int AlignVertical() {
        return AlignVerticalAbstract().toUnderlyingValue().intValue();
    }

    public VerticalAlignment AlignVerticalAbstract() {
        return this.verticalAlignment;
    }

    public void AlignVerticalAbstract(VerticalAlignment alignment) {
        this.alignmentSetter.setVerticalAlignment(alignment);
        this.verticalAlignment = alignment;
    }

    @DesignerProperty(defaultValue = "1", editorType = "vertical_alignment")
    @SimpleProperty
    public void AlignVertical(@Options(VerticalAlignment.class) int alignment) {
        VerticalAlignment align = VerticalAlignment.fromUnderlyingValue(Integer.valueOf(alignment));
        if (align == null) {
            this.container.$form().dispatchErrorOccurredEvent(this, "VerticalAlignment", ErrorMessages.ERROR_BAD_VALUE_FOR_VERTICAL_ALIGNMENT, Integer.valueOf(alignment));
            return;
        }
        AlignVerticalAbstract(align);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Returns the background color of the %type%")
    public int BackgroundColor() {
        return this.backgroundColor;
    }

    @DesignerProperty(defaultValue = "&H00000000", editorType = "color")
    @SimpleProperty(description = "Specifies the background color of the %type%. The background color will not be visible if an Image is being displayed.")
    public void BackgroundColor(int argb) {
        this.backgroundColor = argb;
        updateAppearance();
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public String Image() {
        return this.imagePath;
    }

    @DesignerProperty(defaultValue = "", editorType = "asset")
    @SimpleProperty(description = "Specifies the path of the background image for the %type%.  If there is both an Image and a BackgroundColor, only the Image will be visible.")
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
                }
            }
            updateAppearance();
        }
    }

    private void updateAppearance() {
        if (this.backgroundImageDrawable != null) {
            ViewUtil.setBackgroundImage(this.viewLayout.getLayoutManager(), this.backgroundImageDrawable);
        } else if (this.backgroundColor == 0) {
            ViewUtil.setBackgroundDrawable(this.viewLayout.getLayoutManager(), this.defaultButtonDrawable);
        } else {
            ViewUtil.setBackgroundDrawable(this.viewLayout.getLayoutManager(), (Drawable) null);
            this.viewLayout.getLayoutManager().setBackgroundColor(this.backgroundColor);
        }
    }
}
