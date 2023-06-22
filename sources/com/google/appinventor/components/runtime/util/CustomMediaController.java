package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.MediaController;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

public class CustomMediaController extends MediaController implements View.OnTouchListener {
    private View mAnchorView;
    private int mShowTime = PathInterpolatorCompat.MAX_NUM_POINTS;

    public CustomMediaController(Context context) {
        super(context);
    }

    public void show(int timeout) {
        setVisibility(0);
        super.show(timeout);
    }

    public void show() {
        setVisibility(0);
        super.show();
    }

    public boolean addTo(ViewGroup parent, ViewGroup.LayoutParams params) {
        ViewParent mParent = getParent();
        if (mParent == null || !(mParent instanceof ViewGroup)) {
            Log.e("CustomMediaController.addTo", "MediaController not available in fullscreen.");
            return false;
        }
        ((ViewGroup) mParent).removeView(this);
        parent.addView(this, params);
        return true;
    }

    public void setAnchorView(View anchorView) {
        this.mAnchorView = anchorView;
        this.mAnchorView.setOnTouchListener(this);
        super.setAnchorView(anchorView);
    }

    public void hide() {
        super.hide();
        setVisibility(4);
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (v != this.mAnchorView) {
            return false;
        }
        show(this.mShowTime);
        return false;
    }
}
