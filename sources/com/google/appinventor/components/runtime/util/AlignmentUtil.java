package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.common.HorizontalAlignment;
import com.google.appinventor.components.common.VerticalAlignment;
import com.google.appinventor.components.runtime.LinearLayout;

public class AlignmentUtil {
    LinearLayout viewLayout;

    public AlignmentUtil(LinearLayout viewLayout2) {
        this.viewLayout = viewLayout2;
    }

    public void setHorizontalAlignment(int alignment) throws IllegalArgumentException {
        switch (alignment) {
            case 1:
                this.viewLayout.setHorizontalGravity(3);
                return;
            case 2:
                this.viewLayout.setHorizontalGravity(5);
                return;
            case 3:
                this.viewLayout.setHorizontalGravity(1);
                return;
            default:
                throw new IllegalArgumentException("Bad value to setHorizontalAlignment: " + alignment);
        }
    }

    public void setHorizontalAlignment(HorizontalAlignment alignment) {
        switch (alignment) {
            case Left:
                this.viewLayout.setHorizontalGravity(3);
                return;
            case Center:
                this.viewLayout.setHorizontalGravity(1);
                return;
            case Right:
                this.viewLayout.setHorizontalGravity(5);
                return;
            default:
                throw new IllegalArgumentException("Bad value to setHorizontalAlignment: " + alignment);
        }
    }

    public void setVerticalAlignment(int alignment) throws IllegalArgumentException {
        switch (alignment) {
            case 1:
                this.viewLayout.setVerticalGravity(48);
                return;
            case 2:
                this.viewLayout.setVerticalGravity(16);
                return;
            case 3:
                this.viewLayout.setVerticalGravity(80);
                return;
            default:
                throw new IllegalArgumentException("Bad value to setVerticalAlignment: " + alignment);
        }
    }

    public void setVerticalAlignment(VerticalAlignment alignment) {
        switch (alignment) {
            case Top:
                this.viewLayout.setVerticalGravity(48);
                return;
            case Center:
                this.viewLayout.setVerticalGravity(16);
                return;
            case Bottom:
                this.viewLayout.setVerticalGravity(80);
                return;
            default:
                throw new IllegalArgumentException("Bad value to setVerticalAlignment: " + alignment);
        }
    }
}
