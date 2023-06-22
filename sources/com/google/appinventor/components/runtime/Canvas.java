package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.collect.Sets;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.errors.StopBlocksExecution;
import com.google.appinventor.components.runtime.util.BoundingBox;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.FileWriteOperation;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.PaintUtil;
import com.google.appinventor.components.runtime.util.ScopedFile;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.Synchronizer;
import com.google.appinventor.components.runtime.util.YailList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@DesignerComponent(category = ComponentCategory.ANIMATION, description = "<p>A two-dimensional touch-sensitive rectangular panel on which drawing can be done and sprites can be moved.</p> <p>The <code>BackgroundColor</code>, <code>PaintColor</code>, <code>BackgroundImage</code>, <code>Width</code>, and <code>Height</code> of the Canvas can be set in either the Designer or in the Blocks Editor.  The <code>Width</code> and <code>Height</code> are measured in pixels and must be positive.</p><p>Any location on the Canvas can be specified as a pair of (X, Y) values, where <ul> <li>X is the number of pixels away from the left edge of the Canvas</li><li>Y is the number of pixels away from the top edge of the Canvas</li></ul>.</p> <p>There are events to tell when and where a Canvas has been touched or a <code>Sprite</code> (<code>ImageSprite</code> or <code>Ball</code>) has been dragged.  There are also methods for drawing points, lines, and circles.</p>", version = 15)
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public final class Canvas extends AndroidViewComponent implements ComponentContainer {
    private static final int DEFAULT_BACKGROUND_COLOR = -1;
    private static final float DEFAULT_LINE_WIDTH = 2.0f;
    private static final int DEFAULT_PAINT_COLOR = -16777216;
    private static final int DEFAULT_TAP_THRESHOLD = 15;
    private static final int DEFAULT_TEXTALIGNMENT = 1;
    private static final int FLING_INTERVAL = 1000;
    private static final String LOG_TAG = "Canvas";
    private static final int MIN_WIDTH_HEIGHT = 1;
    /* access modifiers changed from: private */
    public int backgroundColor;
    /* access modifiers changed from: private */
    public String backgroundImagePath = "";
    private final Activity context;
    /* access modifiers changed from: private */
    public boolean drawn;
    /* access modifiers changed from: private */
    public boolean extendMovesOutsideCanvas = false;
    /* access modifiers changed from: private */
    public final Set<ExtensionGestureDetector> extensionGestureDetectors = Sets.newHashSet();
    private Form form = $form();
    /* access modifiers changed from: private */
    public boolean havePermission = false;
    /* access modifiers changed from: private */
    public final GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    public final MotionEventParser motionEventParser;
    /* access modifiers changed from: private */
    public final Paint paint;
    private int paintColor;
    /* access modifiers changed from: private */
    public final List<Sprite> sprites;
    /* access modifiers changed from: private */
    public int tapThreshold = 15;
    private int textAlignment;
    /* access modifiers changed from: private */
    public final CanvasView view;

    public interface ExtensionGestureDetector {
        boolean onTouchEvent(MotionEvent motionEvent);
    }

    class MotionEventParser {
        public static final int FINGER_HEIGHT = 24;
        public static final int FINGER_WIDTH = 24;
        private static final int HALF_FINGER_HEIGHT = 12;
        private static final int HALF_FINGER_WIDTH = 12;
        private static final int UNSET = -1;
        private boolean drag = false;
        private final List<Sprite> draggedSprites = new ArrayList();
        private boolean isDrag = false;
        private float lastX = -1.0f;
        private float lastY = -1.0f;
        private float startX = -1.0f;
        private float startY = -1.0f;

        MotionEventParser() {
        }

        /* access modifiers changed from: package-private */
        public void parse(MotionEvent event) {
            int width = Canvas.this.Width();
            int height = Canvas.this.Height();
            float x = Math.max(0.0f, ((float) ((int) event.getX())) / Canvas.this.$form().deviceDensity());
            float y = Math.max(0.0f, ((float) ((int) event.getY())) / Canvas.this.$form().deviceDensity());
            BoundingBox rect = new BoundingBox((double) Math.max(0, ((int) x) - 12), (double) Math.max(0, ((int) y) - 12), (double) Math.min(width - 1, ((int) x) + 12), (double) Math.min(height - 1, ((int) y) + 12));
            switch (event.getAction()) {
                case 0:
                    this.draggedSprites.clear();
                    this.startX = x;
                    this.startY = y;
                    this.lastX = x;
                    this.lastY = y;
                    this.drag = false;
                    this.isDrag = false;
                    for (Sprite sprite : Canvas.this.sprites) {
                        if (sprite.Enabled() && sprite.Visible() && sprite.intersectsWith(rect)) {
                            this.draggedSprites.add(sprite);
                            sprite.TouchDown(this.startX, this.startY);
                        }
                    }
                    Canvas.this.TouchDown(this.startX, this.startY);
                    return;
                case 1:
                    if (!this.drag) {
                        boolean handled = false;
                        for (Sprite sprite2 : this.draggedSprites) {
                            if (sprite2.Enabled() && sprite2.Visible()) {
                                sprite2.Touched(x, y);
                                sprite2.TouchUp(x, y);
                                handled = true;
                            }
                        }
                        Canvas.this.Touched(x, y, handled);
                    } else {
                        for (Sprite sprite3 : this.draggedSprites) {
                            if (sprite3.Enabled() && sprite3.Visible()) {
                                sprite3.Touched(x, y);
                                sprite3.TouchUp(x, y);
                            }
                        }
                    }
                    Canvas.this.TouchUp(x, y);
                    this.drag = false;
                    this.startX = -1.0f;
                    this.startY = -1.0f;
                    this.lastX = -1.0f;
                    this.lastY = -1.0f;
                    return;
                case 2:
                    if (this.startX == -1.0f || this.startY == -1.0f || this.lastX == -1.0f || this.lastY == -1.0f) {
                        Log.w(Canvas.LOG_TAG, "In Canvas.MotionEventParser.parse(), an ACTION_MOVE was passed without a preceding ACTION_DOWN: " + event);
                    }
                    if (this.isDrag || Math.abs(x - this.startX) >= ((float) Canvas.this.tapThreshold) || Math.abs(y - this.startY) >= ((float) Canvas.this.tapThreshold)) {
                        this.isDrag = true;
                        this.drag = true;
                        if ((x > 0.0f && x <= ((float) width) && y > 0.0f && y <= ((float) height)) || Canvas.this.extendMovesOutsideCanvas) {
                            for (Sprite sprite4 : Canvas.this.sprites) {
                                if (!this.draggedSprites.contains(sprite4) && sprite4.Enabled() && sprite4.Visible() && sprite4.intersectsWith(rect)) {
                                    this.draggedSprites.add(sprite4);
                                }
                            }
                            boolean handled2 = false;
                            for (Sprite sprite5 : this.draggedSprites) {
                                if (sprite5.Enabled() && sprite5.Visible()) {
                                    sprite5.Dragged(this.startX, this.startY, this.lastX, this.lastY, x, y);
                                    handled2 = true;
                                }
                            }
                            Canvas.this.Dragged(this.startX, this.startY, this.lastX, this.lastY, x, y, handled2);
                            this.lastX = x;
                            this.lastY = y;
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private final class CanvasView extends View {
        private BitmapDrawable backgroundDrawable;
        private Bitmap bitmap = Bitmap.createBitmap(32, 48, Bitmap.Config.ARGB_8888);
        /* access modifiers changed from: private */
        public android.graphics.Canvas canvas = new android.graphics.Canvas(this.bitmap);
        /* access modifiers changed from: private */
        public Bitmap completeCache;
        private Bitmap scaledBackgroundBitmap;

        public CanvasView(Context context) {
            super(context);
        }

        /* access modifiers changed from: private */
        public Bitmap buildCache() {
            setDrawingCacheEnabled(true);
            destroyDrawingCache();
            Bitmap cache = getDrawingCache();
            if (cache != null) {
                return cache;
            }
            int width = getWidth();
            int height = getHeight();
            Bitmap cache2 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            android.graphics.Canvas c = new android.graphics.Canvas(cache2);
            layout(0, 0, width, height);
            draw(c);
            return cache2;
        }

        public void onDraw(android.graphics.Canvas canvas0) {
            this.completeCache = null;
            super.onDraw(canvas0);
            canvas0.drawBitmap(this.bitmap, 0.0f, 0.0f, (Paint) null);
            for (Sprite sprite : Canvas.this.sprites) {
                sprite.onDraw(canvas0);
            }
            boolean unused = Canvas.this.drawn = true;
        }

        /* access modifiers changed from: protected */
        public void onSizeChanged(int w, int h, int oldW, int oldH) {
            int oldBitmapWidth = this.bitmap.getWidth();
            int oldBitmapHeight = this.bitmap.getHeight();
            if (w != oldBitmapWidth || h != oldBitmapHeight) {
                Bitmap oldBitmap = this.bitmap;
                try {
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(oldBitmap, w, h, false);
                    if (scaledBitmap.isMutable()) {
                        this.bitmap = scaledBitmap;
                        this.canvas = new android.graphics.Canvas(this.bitmap);
                    } else {
                        this.bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                        this.canvas = new android.graphics.Canvas(this.bitmap);
                        this.canvas.drawBitmap(oldBitmap, new Rect(0, 0, oldBitmapWidth, oldBitmapHeight), new RectF(0.0f, 0.0f, (float) w, (float) h), (Paint) null);
                    }
                } catch (IllegalArgumentException e) {
                    Log.e(Canvas.LOG_TAG, "Bad values to createScaledBimap w = " + w + ", h = " + h);
                }
                this.scaledBackgroundBitmap = null;
            }
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int preferredWidth;
            int preferredHeight;
            if (this.backgroundDrawable != null) {
                Bitmap bitmap2 = this.backgroundDrawable.getBitmap();
                preferredWidth = bitmap2.getWidth();
                preferredHeight = bitmap2.getHeight();
            } else {
                preferredWidth = 32;
                preferredHeight = 48;
            }
            setMeasuredDimension(getSize(widthMeasureSpec, preferredWidth), getSize(heightMeasureSpec, preferredHeight));
        }

        private int getSize(int measureSpec, int preferredSize) {
            int specMode = View.MeasureSpec.getMode(measureSpec);
            int specSize = View.MeasureSpec.getSize(measureSpec);
            if (specMode == 1073741824) {
                return specSize;
            }
            int result = preferredSize;
            if (specMode == Integer.MIN_VALUE) {
                return Math.min(result, specSize);
            }
            return result;
        }

        public boolean onTouchEvent(MotionEvent event) {
            Canvas.this.container.$form().dontGrabTouchEventsForComponent();
            Canvas.this.motionEventParser.parse(event);
            Canvas.this.mGestureDetector.onTouchEvent(event);
            for (ExtensionGestureDetector g : Canvas.this.extensionGestureDetectors) {
                g.onTouchEvent(event);
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public void setBackgroundImage(String path) {
            Canvas canvas2 = Canvas.this;
            if (path == null) {
                path = "";
            }
            String unused = canvas2.backgroundImagePath = path;
            this.backgroundDrawable = null;
            this.scaledBackgroundBitmap = null;
            if (!TextUtils.isEmpty(Canvas.this.backgroundImagePath)) {
                try {
                    this.backgroundDrawable = MediaUtil.getBitmapDrawable(Canvas.this.container.$form(), Canvas.this.backgroundImagePath);
                } catch (IOException e) {
                    Log.e(Canvas.LOG_TAG, "Unable to load " + Canvas.this.backgroundImagePath);
                }
            }
            setBackground();
            clearDrawingLayer();
        }

        /* access modifiers changed from: package-private */
        @RequiresApi(api = 8)
        public void setBackgroundImageBase64(String imageUrl) {
            Canvas canvas2 = Canvas.this;
            if (imageUrl == null) {
                imageUrl = "";
            }
            String unused = canvas2.backgroundImagePath = imageUrl;
            this.backgroundDrawable = null;
            this.scaledBackgroundBitmap = null;
            if (!TextUtils.isEmpty(Canvas.this.backgroundImagePath)) {
                byte[] decodedString = Base64.decode(Canvas.this.backgroundImagePath, 0);
                this.backgroundDrawable = new BitmapDrawable(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            }
            setBackground();
            clearDrawingLayer();
        }

        private void setBackground() {
            Drawable setDraw;
            int i = -1;
            BitmapDrawable bitmapDrawable = this.backgroundDrawable;
            if (Canvas.this.backgroundImagePath == "" || this.backgroundDrawable == null) {
                if (Canvas.this.backgroundColor != 0) {
                    i = Canvas.this.backgroundColor;
                }
                setDraw = new ColorDrawable(i);
            } else {
                setDraw = this.backgroundDrawable.getConstantState().newDrawable();
                if (Canvas.this.backgroundColor != 0) {
                    i = Canvas.this.backgroundColor;
                }
                setDraw.setColorFilter(i, PorterDuff.Mode.DST_OVER);
            }
            setBackgroundDrawable(setDraw);
        }

        /* access modifiers changed from: private */
        public void clearDrawingLayer() {
            this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            invalidate();
        }

        public void setBackgroundColor(int color) {
            int unused = Canvas.this.backgroundColor = color;
            setBackground();
            clearDrawingLayer();
        }

        /* access modifiers changed from: private */
        public void drawTextAtAngle(String text, int x, int y, float angle) {
            this.canvas.save();
            this.canvas.rotate(-angle, (float) x, (float) y);
            this.canvas.drawText(text, (float) x, (float) y, Canvas.this.paint);
            this.canvas.restore();
            invalidate();
        }

        /* access modifiers changed from: private */
        public int getBackgroundPixelColor(int x, int y) {
            if (x < 0 || x >= this.bitmap.getWidth() || y < 0 || y >= this.bitmap.getHeight()) {
                return 16777215;
            }
            try {
                int color = this.bitmap.getPixel(x, y);
                if (color != 0) {
                    return color;
                }
                if (this.backgroundDrawable != null) {
                    if (this.scaledBackgroundBitmap == null) {
                        this.scaledBackgroundBitmap = Bitmap.createScaledBitmap(this.backgroundDrawable.getBitmap(), this.bitmap.getWidth(), this.bitmap.getHeight(), false);
                    }
                    return this.scaledBackgroundBitmap.getPixel(x, y);
                } else if (Color.alpha(Canvas.this.backgroundColor) != 0) {
                    return Canvas.this.backgroundColor;
                } else {
                    return 16777215;
                }
            } catch (IllegalArgumentException e) {
                Log.e(Canvas.LOG_TAG, String.format("Returning COLOR_NONE (exception) from getBackgroundPixelColor.", new Object[0]));
                return 16777215;
            }
        }

        /* access modifiers changed from: private */
        public int getPixelColor(int x, int y) {
            if (x < 0 || x >= this.bitmap.getWidth() || y < 0 || y >= this.bitmap.getHeight()) {
                return 16777215;
            }
            if (this.completeCache == null) {
                boolean anySpritesVisible = false;
                Iterator it = Canvas.this.sprites.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (((Sprite) it.next()).Visible()) {
                            anySpritesVisible = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (!anySpritesVisible) {
                    return getBackgroundPixelColor(x, y);
                }
                this.completeCache = buildCache();
            }
            try {
                return this.completeCache.getPixel(x, y);
            } catch (IllegalArgumentException e) {
                Log.e(Canvas.LOG_TAG, String.format("Returning COLOR_NONE (exception) from getPixelColor.", new Object[0]));
                return 16777215;
            }
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Canvas(ComponentContainer container) {
        super(container);
        boolean z = true;
        this.context = container.$context();
        this.view = new CanvasView(this.context);
        container.$add(this);
        this.paint = new Paint();
        this.paint.setFlags(1);
        LineWidth(DEFAULT_LINE_WIDTH);
        PaintColor(-16777216);
        BackgroundColor(-1);
        TextAlignment(1);
        FontSize(14.0f);
        TapThreshold(15);
        this.sprites = new LinkedList();
        this.motionEventParser = new MotionEventParser();
        this.mGestureDetector = new GestureDetector(this.context, new FlingGestureListener());
        if (FileUtil.needsWritePermission(this.form.DefaultFileScope())) {
            this.havePermission = this.form.isDeniedPermission("android.permission.WRITE_EXTERNAL_STORAGE") ? false : z;
        } else {
            this.havePermission = true;
        }
    }

    public View getView() {
        return this.view;
    }

    public Bitmap getBitmap() {
        return this.view.buildCache();
    }

    public Activity getContext() {
        return this.context;
    }

    public void registerCustomGestureDetector(ExtensionGestureDetector detector) {
        this.extensionGestureDetectors.add(detector);
    }

    public void removeCustomGestureDetector(Object detector) {
        this.extensionGestureDetectors.remove(detector);
    }

    public boolean ready() {
        return this.drawn;
    }

    /* access modifiers changed from: package-private */
    public void addSprite(Sprite sprite) {
        for (int i = 0; i < this.sprites.size(); i++) {
            if (this.sprites.get(i).Z() > sprite.Z()) {
                this.sprites.add(i, sprite);
                return;
            }
        }
        this.sprites.add(sprite);
    }

    /* access modifiers changed from: package-private */
    public void removeSprite(Sprite sprite) {
        this.sprites.remove(sprite);
    }

    /* access modifiers changed from: package-private */
    public void changeSpriteLayer(Sprite sprite) {
        removeSprite(sprite);
        addSprite(sprite);
        this.view.invalidate();
    }

    public Activity $context() {
        return this.context;
    }

    public Form $form() {
        return this.container.$form();
    }

    public void $add(AndroidViewComponent component) {
        throw new UnsupportedOperationException("Canvas.$add() called");
    }

    public List<? extends Component> getChildren() {
        return this.sprites;
    }

    public void setChildWidth(AndroidViewComponent component, int width) {
        throw new UnsupportedOperationException("Canvas.setChildWidth() called");
    }

    public void setChildHeight(AndroidViewComponent component, int height) {
        throw new UnsupportedOperationException("Canvas.setChildHeight() called");
    }

    /* access modifiers changed from: package-private */
    public void registerChange(Sprite sprite) {
        this.view.invalidate();
        findSpriteCollisions(sprite);
    }

    /* access modifiers changed from: protected */
    public void findSpriteCollisions(Sprite movedSprite) {
        for (Sprite sprite : this.sprites) {
            if (sprite != movedSprite) {
                if (movedSprite.CollidingWith(sprite)) {
                    if (!movedSprite.Visible() || !movedSprite.Enabled() || !sprite.Visible() || !sprite.Enabled() || !Sprite.colliding(sprite, movedSprite)) {
                        movedSprite.NoLongerCollidingWith(sprite);
                        sprite.NoLongerCollidingWith(movedSprite);
                    }
                } else if (movedSprite.Visible() && movedSprite.Enabled() && sprite.Visible() && sprite.Enabled() && Sprite.colliding(sprite, movedSprite)) {
                    movedSprite.CollidedWith(sprite);
                    sprite.CollidedWith(movedSprite);
                }
            }
        }
    }

    @SimpleProperty
    public void Width(int width) {
        if (width > 0 || width == -2 || width == -1 || width <= -1000) {
            super.Width(width);
        } else {
            this.container.$form().dispatchErrorOccurredEvent(this, "Width", 1002, new Object[0]);
        }
    }

    @SimpleProperty
    public void Height(int height) {
        if (height > 0 || height == -2 || height == -1 || height <= -1000) {
            super.Height(height);
        } else {
            this.container.$form().dispatchErrorOccurredEvent(this, "Height", 1003, new Object[0]);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The color of the canvas background.")
    public int BackgroundColor() {
        return this.backgroundColor;
    }

    @DesignerProperty(defaultValue = "&HFFFFFFFF", editorType = "color")
    @SimpleProperty
    public void BackgroundColor(int argb) {
        this.view.setBackgroundColor(argb);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The name of a file containing the background image for the canvas")
    public String BackgroundImage() {
        return this.backgroundImagePath;
    }

    @DesignerProperty(defaultValue = "", editorType = "asset")
    @SimpleProperty
    public void BackgroundImage(@Asset String path) {
        this.view.setBackgroundImage(path);
    }

    @RequiresApi(api = 8)
    @SimpleProperty(description = "Set the background image in Base64 format. This requires API level >= 8. For devices with API level less than 8, setting this will end up with an empty background.")
    public void BackgroundImageinBase64(String imageUrl) {
        if (SdkLevel.getLevel() >= 8) {
            this.view.setBackgroundImageBase64(imageUrl);
        } else {
            this.view.setBackgroundImageBase64("");
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set the number of pixels right, left, up or down, a sequence of drags mustmove from the starting point to be considered a drag (instead of a touch).")
    public int TapThreshold() {
        return this.tapThreshold;
    }

    @DesignerProperty(defaultValue = "15", editorType = "integer")
    @SimpleProperty
    public void TapThreshold(int threshold) {
        this.tapThreshold = threshold;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The color in which lines are drawn")
    public int PaintColor() {
        return this.paintColor;
    }

    @DesignerProperty(defaultValue = "&HFF000000", editorType = "color")
    @SimpleProperty
    public void PaintColor(int argb) {
        this.paintColor = argb;
        changePaint(this.paint, argb);
    }

    private void changePaint(Paint paint2, int argb) {
        if (argb == 0) {
            PaintUtil.changePaint(paint2, -16777216);
        } else if (argb == 16777215) {
            PaintUtil.changePaintTransparent(paint2);
        } else {
            PaintUtil.changePaint(paint2, argb);
        }
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The font size of text drawn on the canvas.")
    public float FontSize() {
        return this.paint.getTextSize() / $form().deviceDensity();
    }

    @DesignerProperty(defaultValue = "14.0", editorType = "non_negative_float")
    @SimpleProperty
    public void FontSize(float size) {
        this.paint.setTextSize(size * $form().deviceDensity());
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The width of lines drawn on the canvas.")
    public float LineWidth() {
        return this.paint.getStrokeWidth() / $form().deviceDensity();
    }

    @DesignerProperty(defaultValue = "2.0", editorType = "non_negative_float")
    @SimpleProperty
    public void LineWidth(float width) {
        this.paint.setStrokeWidth($form().deviceDensity() * width);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "Determines the alignment of the text drawn by DrawText() or DrawAngle() with respect to the point specified by that command: point at the left of the text, point at the center of the text, or point at the right of the text.", userVisible = true)
    public int TextAlignment() {
        return this.textAlignment;
    }

    @DesignerProperty(defaultValue = "1", editorType = "textalignment")
    @SimpleProperty(userVisible = true)
    public void TextAlignment(int alignment) {
        this.textAlignment = alignment;
        switch (alignment) {
            case 0:
                this.paint.setTextAlign(Paint.Align.LEFT);
                return;
            case 1:
                this.paint.setTextAlign(Paint.Align.CENTER);
                return;
            case 2:
                this.paint.setTextAlign(Paint.Align.RIGHT);
                return;
            default:
                return;
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Determines whether moves can extend beyond the canvas borders.   Default is false. This should normally be false, and the property is provided for backwards compatibility.", userVisible = true)
    public boolean ExtendMovesOutsideCanvas() {
        return this.extendMovesOutsideCanvas;
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(userVisible = true)
    public void ExtendMovesOutsideCanvas(boolean extend) {
        this.extendMovesOutsideCanvas = extend;
    }

    @SimpleEvent
    public void Touched(float x, float y, boolean touchedAnySprite) {
        EventDispatcher.dispatchEvent(this, "Touched", Float.valueOf(x), Float.valueOf(y), Boolean.valueOf(touchedAnySprite));
    }

    @SimpleEvent
    public void TouchDown(float x, float y) {
        EventDispatcher.dispatchEvent(this, "TouchDown", Float.valueOf(x), Float.valueOf(y));
    }

    @SimpleEvent
    public void TouchUp(float x, float y) {
        EventDispatcher.dispatchEvent(this, "TouchUp", Float.valueOf(x), Float.valueOf(y));
    }

    @SimpleEvent
    public void Flung(float x, float y, float speed, float heading, float xvel, float yvel, boolean flungSprite) {
        EventDispatcher.dispatchEvent(this, "Flung", Float.valueOf(x), Float.valueOf(y), Float.valueOf(speed), Float.valueOf(heading), Float.valueOf(xvel), Float.valueOf(yvel), Boolean.valueOf(flungSprite));
    }

    @SimpleEvent
    public void Dragged(float startX, float startY, float prevX, float prevY, float currentX, float currentY, boolean draggedAnySprite) {
        EventDispatcher.dispatchEvent(this, "Dragged", Float.valueOf(startX), Float.valueOf(startY), Float.valueOf(prevX), Float.valueOf(prevY), Float.valueOf(currentX), Float.valueOf(currentY), Boolean.valueOf(draggedAnySprite));
    }

    @SimpleFunction(description = "Clears anything drawn on this Canvas but not any background color or image.")
    public void Clear() {
        this.view.clearDrawingLayer();
    }

    @SimpleFunction
    public void DrawPoint(int x, int y) {
        this.view.canvas.drawPoint(((float) x) * $form().deviceDensity(), ((float) y) * $form().deviceDensity(), this.paint);
        this.view.invalidate();
    }

    @SimpleFunction
    public void DrawCircle(int centerX, int centerY, float radius, boolean fill) {
        float correctedX = ((float) centerX) * $form().deviceDensity();
        float correctedY = ((float) centerY) * $form().deviceDensity();
        float correctedR = radius * $form().deviceDensity();
        Paint p = new Paint(this.paint);
        p.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
        this.view.canvas.drawCircle(correctedX, correctedY, correctedR, p);
        this.view.invalidate();
    }

    @SimpleFunction
    public void DrawLine(int x1, int y1, int x2, int y2) {
        this.view.canvas.drawLine(((float) x1) * $form().deviceDensity(), ((float) y1) * $form().deviceDensity(), ((float) x2) * $form().deviceDensity(), ((float) y2) * $form().deviceDensity(), this.paint);
        this.view.invalidate();
    }

    @SimpleFunction(description = "Draws a shape on the canvas. pointList should be a list contains sub-lists with two number which represents a coordinate. The first point and last point does not need to be the same. e.g. ((x1 y1) (x2 y2) (x3 y3)) When fill is true, the shape will be filled.")
    public void DrawShape(YailList pointList, boolean fill) {
        try {
            Path path = parsePath(parsePointList(pointList));
            path.close();
            Paint p = new Paint(this.paint);
            p.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
            this.view.canvas.drawPath(path, p);
            this.view.invalidate();
        } catch (IllegalArgumentException e) {
            $form().dispatchErrorOccurredEvent(this, "DrawShape", 1004, new Object[0]);
        }
    }

    private Path parsePath(float[][] points) throws IllegalArgumentException {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        }
        float scalingFactor = $form().deviceDensity();
        Path path = new Path();
        path.moveTo(points[0][0] * scalingFactor, points[0][1] * scalingFactor);
        for (int i = 1; i < points.length; i++) {
            path.lineTo(points[i][0] * scalingFactor, points[i][1] * scalingFactor);
        }
        return path;
    }

    private float[][] parsePointList(YailList pointList) throws IllegalArgumentException {
        int i = 0;
        if (pointList == null || pointList.size() == 0) {
            throw new IllegalArgumentException();
        }
        float[][] points = (float[][]) Array.newInstance(Float.TYPE, new int[]{pointList.size(), 2});
        int index = 0;
        Object[] array = pointList.toArray();
        int length = array.length;
        while (i < length) {
            Object pointObject = array[i];
            if (pointObject instanceof YailList) {
                YailList pointYailList = (YailList) pointObject;
                if (pointYailList.size() == 2) {
                    try {
                        points[index][0] = Float.parseFloat(pointYailList.getString(0));
                        points[index][1] = Float.parseFloat(pointYailList.getString(1));
                        index++;
                        i++;
                    } catch (NullPointerException e) {
                        throw new IllegalArgumentException(e.fillInStackTrace());
                    } catch (NumberFormatException e2) {
                        throw new IllegalArgumentException(e2.fillInStackTrace());
                    }
                } else {
                    throw new IllegalArgumentException("length of item YailList(" + index + ") is not 2");
                }
            } else {
                throw new IllegalArgumentException("item(" + index + ") in YailList is not a YailList");
            }
        }
        return points;
    }

    @SimpleFunction(description = "Draw an arc on Canvas, by drawing an arc from a specified oval (specified by left, top, right & bottom). Start angle is 0 when heading to the right, and increase when rotate clockwise. When useCenter is true, a sector will be drawed instead of an arc. When fill is true, a filled arc (or sector) will be drawed instead of just an outline.")
    public void DrawArc(int left, int top, int right, int bottom, float startAngle, float sweepAngle, boolean useCenter, boolean fill) {
        float scalingFactor = $form().deviceDensity();
        Paint p = new Paint(this.paint);
        p.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
        this.view.canvas.drawArc(new RectF(((float) left) * scalingFactor, ((float) top) * scalingFactor, ((float) right) * scalingFactor, ((float) bottom) * scalingFactor), startAngle, sweepAngle, useCenter, p);
        this.view.invalidate();
    }

    @SimpleFunction(description = "Draws the specified text relative to the specified coordinates using the values of the FontSize and TextAlignment properties.")
    public void DrawText(String text, int x, int y) {
        float fontScalingFactor = $form().deviceDensity();
        this.view.canvas.drawText(text, ((float) x) * fontScalingFactor, ((float) y) * fontScalingFactor, this.paint);
        this.view.invalidate();
    }

    @SimpleFunction(description = "Draws the specified text starting at the specified coordinates at the specified angle using the values of the FontSize and TextAlignment properties.")
    public void DrawTextAtAngle(String text, int x, int y, float angle) {
        this.view.drawTextAtAngle(text, (int) (((float) x) * $form().deviceDensity()), (int) (((float) y) * $form().deviceDensity()), angle);
    }

    @SimpleFunction(description = "Gets the color of the specified point. This includes the background and any drawn points, lines, or circles but not sprites.")
    public int GetBackgroundPixelColor(int x, int y) {
        return this.view.getBackgroundPixelColor((int) (((float) x) * $form().deviceDensity()), (int) (((float) y) * $form().deviceDensity()));
    }

    @SimpleFunction(description = "Sets the color of the specified point. This differs from DrawPoint by having an argument for color.")
    public void SetBackgroundPixelColor(int x, int y, int color) {
        Paint pixelPaint = new Paint();
        PaintUtil.changePaint(pixelPaint, color);
        this.view.canvas.drawPoint((float) ((int) (((float) x) * $form().deviceDensity())), (float) ((int) (((float) y) * $form().deviceDensity())), pixelPaint);
        this.view.invalidate();
    }

    @SimpleFunction(description = "Gets the color of the specified point.")
    public int GetPixelColor(int x, int y) {
        return this.view.getPixelColor((int) (((float) x) * $form().deviceDensity()), (int) (((float) y) * $form().deviceDensity()));
    }

    @UsesPermissions({"android.permission.WRITE_EXTERNAL_STORAGE"})
    @SimpleFunction(description = "Saves a picture of this Canvas to the device's external storage. If an error occurs, the Screen's ErrorOccurred event will be called.")
    public String Save() {
        return saveFile(FileUtil.getScopedPictureFile($form(), "png"), Bitmap.CompressFormat.PNG, "Save");
    }

    @UsesPermissions({"android.permission.WRITE_EXTERNAL_STORAGE"})
    @SimpleFunction(description = "Saves a picture of this Canvas to the device's external storage in the file named fileName. fileName must end with one of .jpg, .jpeg, or .png, which determines the file type.")
    public String SaveAs(String fileName) {
        Bitmap.CompressFormat format;
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            format = Bitmap.CompressFormat.JPEG;
        } else if (fileName.endsWith(".png")) {
            format = Bitmap.CompressFormat.PNG;
        } else if (!fileName.contains(".")) {
            fileName = fileName + ".png";
            format = Bitmap.CompressFormat.PNG;
        } else {
            this.container.$form().dispatchErrorOccurredEvent(this, "SaveAs", ErrorMessages.ERROR_MEDIA_IMAGE_FILE_FORMAT, new Object[0]);
            return "";
        }
        return saveFile(new ScopedFile(this.form.DefaultFileScope(), fileName), format, "SaveAs");
    }

    private String saveFile(ScopedFile scopedFile, Bitmap.CompressFormat format, String method) {
        if (this.havePermission || !FileUtil.needsWritePermission(scopedFile)) {
            final Synchronizer<Boolean> result = new Synchronizer<>();
            final Bitmap.CompressFormat compressFormat = format;
            new FileWriteOperation(this.form, this, method, scopedFile, false, false) {
                /* access modifiers changed from: protected */
                public boolean process(OutputStream stream) {
                    result.wakeup(Boolean.valueOf((Canvas.this.view.completeCache == null ? Canvas.this.view.buildCache() : Canvas.this.view.completeCache).compress(compressFormat, 100, stream)));
                    return true;
                }
            }.run();
            if (result.getThrowable() instanceof FileNotFoundException) {
                this.container.$form().dispatchErrorOccurredEvent(this, method, ErrorMessages.ERROR_MEDIA_CANNOT_OPEN, FileUtil.resolveFileName(this.form, scopedFile));
            } else if (result.getThrowable() instanceof IOException) {
                this.container.$form().dispatchErrorOccurredEvent(this, method, ErrorMessages.ERROR_MEDIA_FILE_ERROR, result.getThrowable().getMessage());
            } else if (result.getThrowable() instanceof PermissionException) {
                this.container.$form().dispatchPermissionDeniedEvent((Component) this, method, (PermissionException) result.getThrowable());
            } else if (result.getThrowable() instanceof FileUtil.FileException) {
                this.container.$form().dispatchErrorOccurredEvent(this, method, ((FileUtil.FileException) result.getThrowable()).getErrorMessageNumber(), new Object[0]);
            } else if (result.getResult().booleanValue()) {
                return FileUtil.resolveFileName(this.form, scopedFile);
            }
            return "";
        }
        this.form.askPermission("android.permission.WRITE_EXTERNAL_STORAGE", new PermissionResultHandler() {
            public void HandlePermissionResponse(String permission, boolean granted) {
                boolean unused = Canvas.this.havePermission = granted;
            }
        });
        throw new StopBlocksExecution();
    }

    class FlingGestureListener extends GestureDetector.SimpleOnGestureListener {
        FlingGestureListener() {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = (float) Math.max(0, (int) (e1.getX() / Canvas.this.$form().deviceDensity()));
            float y = (float) Math.max(0, (int) (e1.getY() / Canvas.this.$form().deviceDensity()));
            float vx = velocityX / 1000.0f;
            float vy = velocityY / 1000.0f;
            float speed = (float) Math.sqrt((double) ((vx * vx) + (vy * vy)));
            float heading = (float) (-Math.toDegrees(Math.atan2((double) vy, (double) vx)));
            BoundingBox rect = new BoundingBox((double) Math.max(0, ((int) x) - 12), (double) Math.max(0, ((int) y) - 12), (double) Math.min(Canvas.this.Width() - 1, ((int) x) + 12), (double) Math.min(Canvas.this.Height() - 1, ((int) y) + 12));
            boolean spriteHandledFling = false;
            for (Sprite sprite : Canvas.this.sprites) {
                if (sprite.Enabled() && sprite.Visible() && sprite.intersectsWith(rect)) {
                    sprite.Flung(x, y, speed, heading, vx, vy);
                    spriteHandledFling = true;
                }
            }
            Canvas.this.Flung(x, y, speed, heading, vx, vy, spriteHandledFling);
            return true;
        }
    }
}
