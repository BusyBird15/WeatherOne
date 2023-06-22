package com.sunny.CustomWebView;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

public class WView extends WebView {
    GestureDetector gd;

    public interface SwipeCallback {
        void onSwipe(int i, int i2);
    }

    public WView(final int i, Context context, final SwipeCallback swipeCallback) {
        super(context);
        this.gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent motionEvent) {
                return super.onDoubleTap(motionEvent);
            }

            public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                return super.onDoubleTapEvent(motionEvent);
            }

            public boolean onDown(MotionEvent motionEvent) {
                return super.onDown(motionEvent);
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                float abs = Math.abs(motionEvent.getRawX() - motionEvent2.getRawX());
                float abs2 = Math.abs(motionEvent.getRawY() - motionEvent2.getRawY());
                float abs3 = Math.abs(f);
                if (abs > 200.0f && abs2 < 90.0f && abs3 > 350.0f) {
                    if (motionEvent.getRawX() > motionEvent2.getRawX()) {
                        swipeCallback.onSwipe(i, 2);
                    } else if (motionEvent.getRawX() < motionEvent2.getRawX()) {
                        swipeCallback.onSwipe(i, 1);
                    }
                }
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }

            public void onLongPress(MotionEvent motionEvent) {
                super.onLongPress(motionEvent);
            }

            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                return super.onScroll(motionEvent, motionEvent2, f, f2);
            }

            public void onShowPress(MotionEvent motionEvent) {
                super.onShowPress(motionEvent);
            }

            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                return super.onSingleTapConfirmed(motionEvent);
            }

            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return super.onSingleTapUp(motionEvent);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.gd.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
    }
}
