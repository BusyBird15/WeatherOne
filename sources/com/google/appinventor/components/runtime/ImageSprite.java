package com.google.appinventor.components.runtime;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.MediaUtil;
import java.io.IOException;

@SimpleObject
@DesignerComponent(category = ComponentCategory.ANIMATION, description = "<p>A 'sprite' that can be placed on a <code>Canvas</code>, where it can react to touches and drags, interact with other sprites (<code>Ball</code>s and other <code>ImageSprite</code>s) and the edge of the Canvas, and move according to its property values.  Its appearance is that of the image specified in its <code>Picture</code> property (unless its <code>Visible</code> property is <code>False</code>).</p> <p>To have an <code>ImageSprite</code> move 10 pixels to the left every 1000 milliseconds (one second), for example, you would set the <code>Speed</code> property to 10 [pixels], the <code>Interval</code> property to 1000 [milliseconds], the <code>Heading</code> property to 180 [degrees], and the <code>Enabled</code> property to <code>True</code>.  A sprite whose <code>Rotates</code> property is <code>True</code> will rotate its image as the sprite's <code>Heading</code> changes.  Checking for collisions with a rotated sprite currently checks the sprite's unrotated position so that collision checking will be inaccurate for tall narrow or short wide sprites that are rotated.  Any of the sprite properties can be changed at any time under program control.</p> ", version = 8)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class ImageSprite extends Sprite {
    private BitmapDrawable drawable;
    private final Form form;
    private int heightHint = -1;
    private String picturePath = "";
    private boolean rotates;
    private int widthHint = -1;

    public ImageSprite(ComponentContainer container) {
        super(container);
        this.form = container.$form();
        this.rotates = true;
    }

    public void onDraw(Canvas canvas) {
        if (this.drawable != null && this.visible) {
            int xinit = (int) (((float) Math.round(this.xLeft)) * this.form.deviceDensity());
            int yinit = (int) (((float) Math.round(this.yTop)) * this.form.deviceDensity());
            int w = (int) (((float) Width()) * this.form.deviceDensity());
            int h = (int) (((float) Height()) * this.form.deviceDensity());
            this.drawable.setBounds(xinit, yinit, xinit + w, yinit + h);
            if (!this.rotates) {
                this.drawable.draw(canvas);
                return;
            }
            canvas.save();
            canvas.rotate((float) (-Heading()), (float) ((w / 2) + xinit), (float) ((h / 2) + yinit));
            this.drawable.draw(canvas);
            canvas.restore();
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The picture that determines the ImageSprite's appearance.")
    public String Picture() {
        return this.picturePath;
    }

    @DesignerProperty(defaultValue = "", editorType = "asset")
    @SimpleProperty
    public void Picture(@Asset String path) {
        if (path == null) {
            path = "";
        }
        this.picturePath = path;
        try {
            this.drawable = MediaUtil.getBitmapDrawable(this.form, this.picturePath);
        } catch (IOException e) {
            Log.e("ImageSprite", "Unable to load " + this.picturePath);
            this.drawable = null;
        }
        registerChange();
    }

    @SimpleProperty(description = "The height of the ImageSprite in pixels.")
    public int Height() {
        if (this.heightHint != -1 && this.heightHint != -2 && this.heightHint > -1000) {
            return this.heightHint;
        }
        if (this.drawable == null) {
            return 0;
        }
        return (int) (((float) this.drawable.getBitmap().getHeight()) / this.form.deviceDensity());
    }

    @SimpleProperty
    public void Height(int height) {
        this.heightHint = height;
        registerChange();
    }

    public void HeightPercent(int pCent) {
    }

    @SimpleProperty(description = "The width of the ImageSprite in pixels.")
    public int Width() {
        if (this.widthHint != -1 && this.widthHint != -2 && this.widthHint > -1000) {
            return this.widthHint;
        }
        if (this.drawable == null) {
            return 0;
        }
        return (int) (((float) this.drawable.getBitmap().getWidth()) / this.form.deviceDensity());
    }

    @SimpleProperty
    public void Width(int width) {
        this.widthHint = width;
        registerChange();
    }

    public void WidthPercent(int pCent) {
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the image should rotate to match the ImageSprite's heading. The sprite rotates around its centerpoint.")
    public boolean Rotates() {
        return this.rotates;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void Rotates(boolean rotates2) {
        this.rotates = rotates2;
        registerChange();
    }

    @SimpleProperty(description = "The horizontal coordinate of the left edge of the ImageSprite, increasing as the ImageSprite moves right.")
    public double X() {
        return super.X();
    }

    @SimpleProperty(description = "The vertical coordinate of the top edge of the ImageSprite, increasing as the ImageSprite moves down.")
    public double Y() {
        return super.Y();
    }

    @SimpleFunction(description = "Moves the ImageSprite so that its left top corner is at the specified x and y coordinates.")
    public void MoveTo(double x, double y) {
        super.MoveTo(x, y);
    }
}
