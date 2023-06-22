package com.google.appinventor.components.runtime;

import android.graphics.Canvas;
import android.os.Handler;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.Direction;
import com.google.appinventor.components.runtime.errors.IllegalArgumentError;
import com.google.appinventor.components.runtime.util.BoundingBox;
import com.google.appinventor.components.runtime.util.TimerInternal;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.HashSet;
import java.util.Set;

@SimpleObject
public abstract class Sprite extends VisibleComponent implements AlarmHandler, OnDestroyListener, Deleteable {
    private static final boolean DEFAULT_ENABLED = true;
    private static final int DEFAULT_HEADING = 0;
    private static final int DEFAULT_INTERVAL = 100;
    protected static final boolean DEFAULT_ORIGIN_AT_CENTER = false;
    private static final float DEFAULT_SPEED = 0.0f;
    private static final boolean DEFAULT_VISIBLE = true;
    private static final double DEFAULT_Z = 1.0d;
    private static final int DIRECTION_NONE = 0;
    private static final String LOG_TAG = "Sprite";
    private final Handler androidUIHandler;
    protected final Canvas canvas;
    protected Form form;
    protected double heading;
    protected double headingCos;
    protected double headingRadians;
    protected double headingSin;
    protected boolean initialized;
    protected int interval;
    protected boolean originAtCenter;
    private final Set<Sprite> registeredCollisions;
    protected float speed;
    private final TimerInternal timerInternal;
    protected double userHeading;
    protected boolean visible;
    protected double xCenter;
    protected double xLeft;
    protected double yCenter;
    protected double yTop;
    protected double zLayer;

    /* access modifiers changed from: protected */
    public abstract void onDraw(Canvas canvas2);

    protected Sprite(ComponentContainer container, Handler handler) {
        this.initialized = false;
        this.visible = true;
        this.androidUIHandler = handler;
        if (!(container instanceof Canvas)) {
            throw new IllegalArgumentError("Sprite constructor called with container " + container);
        }
        this.canvas = (Canvas) container;
        this.canvas.addSprite(this);
        this.registeredCollisions = new HashSet();
        this.timerInternal = new TimerInternal(this, true, 100, handler);
        this.form = container.$form();
        OriginAtCenter(false);
        Heading(0.0d);
        Enabled(true);
        Interval(100);
        Speed(0.0f);
        Visible(true);
        Z(DEFAULT_Z);
        container.$form().registerForOnDestroy(this);
    }

    protected Sprite(ComponentContainer container) {
        this(container, new Handler());
    }

    public void Initialize() {
        this.initialized = true;
        this.canvas.registerChange(this);
    }

    @SimpleProperty(description = "Controls whether the %type% moves and can be interacted with through collisions, dragging, touching, and flinging.")
    public boolean Enabled() {
        return this.timerInternal.Enabled();
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void Enabled(boolean enabled) {
        this.timerInternal.Enabled(enabled);
    }

    @SimpleProperty(description = "Returns the %type%'s heading in degrees above the positive x-axis.  Zero degrees is toward the right of the screen; 90 degrees is toward the top of the screen.")
    public double Heading() {
        return this.userHeading;
    }

    @DesignerProperty(defaultValue = "0", editorType = "float")
    @SimpleProperty
    public void Heading(double userHeading2) {
        this.userHeading = userHeading2;
        this.heading = -userHeading2;
        this.headingRadians = Math.toRadians(this.heading);
        this.headingCos = Math.cos(this.headingRadians);
        this.headingSin = Math.sin(this.headingRadians);
        registerChange();
    }

    @SimpleProperty(description = "The interval in milliseconds at which the %type%'s position is updated.  For example, if the interval is 50 and the speed is 10, then every 50 milliseconds the sprite will move 10 pixels in the heading direction.")
    public int Interval() {
        return this.timerInternal.Interval();
    }

    @DesignerProperty(defaultValue = "100", editorType = "non_negative_integer")
    @SimpleProperty
    public void Interval(int interval2) {
        this.timerInternal.Interval(interval2);
    }

    @DesignerProperty(defaultValue = "0.0", editorType = "float")
    @SimpleProperty(description = "The number of pixels that the %type% should move every interval, if enabled.")
    public void Speed(float speed2) {
        this.speed = speed2;
    }

    @SimpleProperty(description = "The speed at which the %type% moves. The %type% moves this many pixels every interval if enabled.")
    public float Speed() {
        return this.speed;
    }

    @SimpleProperty(description = "Whether the %type% is visible.")
    public boolean Visible() {
        return this.visible;
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty
    public void Visible(boolean visible2) {
        this.visible = visible2;
        registerChange();
    }

    public double X() {
        return this.originAtCenter ? this.xCenter : this.xLeft;
    }

    private double xLeftToCenter(double xLeft2) {
        return ((double) (Width() / 2)) + xLeft2;
    }

    private double xCenterToLeft(double xCenter2) {
        return xCenter2 - ((double) (Width() / 2));
    }

    private void updateX(double x) {
        if (this.originAtCenter) {
            this.xCenter = x;
            this.xLeft = xCenterToLeft(x);
            return;
        }
        this.xLeft = x;
        this.xCenter = xLeftToCenter(x);
    }

    @DesignerProperty(defaultValue = "0.0", editorType = "float")
    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public void X(double x) {
        updateX(x);
        registerChange();
    }

    private double yTopToCenter(double yTop2) {
        return ((double) (Width() / 2)) + yTop2;
    }

    private double yCenterToTop(double yCenter2) {
        return yCenter2 - ((double) (Width() / 2));
    }

    private void updateY(double y) {
        if (this.originAtCenter) {
            this.yCenter = y;
            this.yTop = yCenterToTop(y);
            return;
        }
        this.yTop = y;
        this.yCenter = yTopToCenter(y);
    }

    @DesignerProperty(defaultValue = "0.0", editorType = "float")
    @SimpleProperty
    public void Y(double y) {
        updateY(y);
        registerChange();
    }

    public double Y() {
        return this.originAtCenter ? this.yCenter : this.yTop;
    }

    @DesignerProperty(defaultValue = "1.0", editorType = "float")
    @SimpleProperty
    public void Z(double layer) {
        this.zLayer = layer;
        this.canvas.changeSpriteLayer(this);
    }

    @SimpleProperty(description = "How the %type% should be layered relative to other Balls and ImageSprites, with higher-numbered layers in front of lower-numbered layers.")
    public double Z() {
        return this.zLayer;
    }

    /* access modifiers changed from: protected */
    public void OriginAtCenter(boolean b) {
        this.originAtCenter = b;
    }

    /* access modifiers changed from: protected */
    public void postEvent(final Sprite sprite, final String eventName, final Object... args) {
        this.androidUIHandler.post(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(sprite, eventName, args);
            }
        });
    }

    @SimpleEvent
    public void CollidedWith(Sprite other) {
        if (!this.registeredCollisions.contains(other)) {
            this.registeredCollisions.add(other);
            postEvent(this, "CollidedWith", other);
        }
    }

    @SimpleEvent(description = "Event handler called when a %type% is dragged. On all calls, the starting coordinates are where the screen was first touched, and the \"current\" coordinates describe the endpoint of the current line segment. On the first call within a given drag, the \"previous\" coordinates are the same as the starting coordinates; subsequently, they are the \"current\" coordinates from the prior call. Note that the %type% won't actually move anywhere in response to the Dragged event unless MoveTo is explicitly called. For smooth movement, each of its coordinates should be set to the sum of its initial value and the difference between its current and previous values.")
    public void Dragged(float startX, float startY, float prevX, float prevY, float currentX, float currentY) {
        postEvent(this, "Dragged", Float.valueOf(startX), Float.valueOf(startY), Float.valueOf(prevX), Float.valueOf(prevY), Float.valueOf(currentX), Float.valueOf(currentY));
    }

    @SimpleEvent(description = "Event handler called when the %type% reaches an edge of the screen. If Bounce is then called with that edge, the %type% will appear to bounce off of the edge it reached. Edge here is represented as an integer that indicates one of eight directions north (1), northeast (2), east (3), southeast (4), south (-1), southwest (-2), west (-3), and northwest (-4).")
    public void EdgeReached(@Options(Direction.class) int edge) {
        Direction dir = Direction.fromUnderlyingValue(Integer.valueOf(edge));
        if (dir != null) {
            EdgeReachedAbstract(dir);
        }
    }

    public void EdgeReachedAbstract(Direction edge) {
        postEvent(this, "EdgeReached", edge.toUnderlyingValue());
    }

    @SimpleEvent(description = "Event handler called when a pair of sprites (Balls and ImageSprites) are no longer colliding.")
    public void NoLongerCollidingWith(Sprite other) {
        this.registeredCollisions.remove(other);
        postEvent(this, "NoLongerCollidingWith", other);
    }

    @SimpleEvent(description = "Event handler called when the user touches an enabled %type% and then immediately lifts their finger. The provided x and y coordinates are relative to the upper left of the canvas.")
    public void Touched(float x, float y) {
        postEvent(this, "Touched", Float.valueOf(x), Float.valueOf(y));
    }

    @SimpleEvent(description = "Event handler called when a fling gesture (quick swipe) is made on an enabled %type%. This provides the x and y coordinates of the start of the fling (relative to the upper left of the canvas), the speed (pixels per millisecond), the heading (0-360 degrees), and the x and y velocity components of the fling's vector.")
    public void Flung(float x, float y, float speed2, float heading2, float xvel, float yvel) {
        postEvent(this, "Flung", Float.valueOf(x), Float.valueOf(y), Float.valueOf(speed2), Float.valueOf(heading2), Float.valueOf(xvel), Float.valueOf(yvel));
    }

    @SimpleEvent(description = "Event handler called when the user stops touching an enabled %type% (lifting their finger after a TouchDown event). This provides the x and y coordinates of the touch, relative to the upper left of the canvas.")
    public void TouchUp(float x, float y) {
        postEvent(this, "TouchUp", Float.valueOf(x), Float.valueOf(y));
    }

    @SimpleEvent(description = "Event handler called when the user begins touching an enabled %type% (placing their finger on a %type% and leaving it there). This provides the x and y coordinates of the touch, relative to the upper left of the canvas.")
    public void TouchDown(float x, float y) {
        postEvent(this, "TouchDown", Float.valueOf(x), Float.valueOf(y));
    }

    @SimpleFunction(description = "Makes the %type% bounce, as if off a wall. For normal bouncing, the edge argument should be the one returned by EdgeReached.")
    public void Bounce(@Options(Direction.class) int edge) {
        Direction dir = Direction.fromUnderlyingValue(Integer.valueOf(edge));
        if (dir != null) {
            BounceAbstract(dir);
        }
    }

    public void BounceAbstract(Direction edge) {
        MoveIntoBounds();
        double normalizedAngle = this.userHeading % 360.0d;
        if (normalizedAngle < 0.0d) {
            normalizedAngle += 360.0d;
        }
        if ((edge == Direction.East && (normalizedAngle < 90.0d || normalizedAngle > 270.0d)) || (edge == Direction.West && normalizedAngle > 90.0d && normalizedAngle < 270.0d)) {
            Heading(180.0d - normalizedAngle);
        } else if ((edge == Direction.North && normalizedAngle > 0.0d && normalizedAngle < 180.0d) || (edge == Direction.South && normalizedAngle > 180.0d)) {
            Heading(360.0d - normalizedAngle);
        } else if ((edge == Direction.Northeast && normalizedAngle > 0.0d && normalizedAngle < 90.0d) || ((edge == Direction.Northwest && normalizedAngle > 90.0d && normalizedAngle < 180.0d) || ((edge == Direction.Southwest && normalizedAngle > 180.0d && normalizedAngle < 270.0d) || (edge == Direction.Southeast && normalizedAngle > 270.0d)))) {
            Heading(180.0d + normalizedAngle);
        }
    }

    @SimpleFunction(description = "Indicates whether a collision has been registered between this %type% and the passed sprite (Ball or ImageSprite).")
    public boolean CollidingWith(Sprite other) {
        return this.registeredCollisions.contains(other);
    }

    @SimpleFunction(description = "Moves the %type% back in bounds if part of it extends out of bounds, having no effect otherwise. If the %type% is too wide to fit on the canvas, this aligns the left side of the %type% with the left side of the canvas. If the %type% is too tall to fit on the canvas, this aligns the top side of the %type% with the top side of the canvas.")
    public void MoveIntoBounds() {
        moveIntoBounds(this.canvas.Width(), this.canvas.Height());
    }

    public void MoveTo(double x, double y) {
        updateX(x);
        updateY(y);
        registerChange();
    }

    @SimpleFunction(description = "Moves the origin of %type% to the position of the cooordinates given  by the list formatted as [x-coordinate, y-coordinate].")
    public void MoveToPoint(YailList coordinates) {
        MoveTo(coerceToDouble(coordinates.getObject(0)), coerceToDouble(coordinates.getObject(1)));
    }

    protected static double coerceToDouble(Object o) {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        try {
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    @SimpleFunction(description = "Turns the %type% to point towards a designated target sprite (Ball or ImageSprite). The new heading will be parallel to the line joining the centerpoints of the two sprites.")
    public void PointTowards(Sprite target) {
        Heading(-Math.toDegrees(Math.atan2(target.yCenter - this.yCenter, target.xCenter - this.xCenter)));
    }

    @SimpleFunction(description = "Sets the heading of the %type% toward the point with the coordinates (x, y).")
    public void PointInDirection(double x, double y) {
        Heading(-Math.toDegrees(Math.atan2(y - this.yCenter, x - this.xCenter)));
    }

    /* access modifiers changed from: protected */
    public void registerChange() {
        if (!this.initialized) {
            this.canvas.getView().invalidate();
            return;
        }
        Direction edge = hitEdgeAbstract();
        if (edge != null) {
            EdgeReachedAbstract(edge);
        }
        this.canvas.registerChange(this);
    }

    /* access modifiers changed from: protected */
    public int hitEdge() {
        Direction edge = hitEdgeAbstract();
        if (edge == null) {
            return 0;
        }
        return edge.toUnderlyingValue().intValue();
    }

    /* access modifiers changed from: protected */
    public int hitEdge(int canvasWidth, int canvasHeight) {
        Direction edge = hitEdgeAbstract(canvasWidth, canvasHeight);
        if (edge == null) {
            return 0;
        }
        return edge.toUnderlyingValue().intValue();
    }

    /* access modifiers changed from: protected */
    public Direction hitEdgeAbstract() {
        if (!this.canvas.ready()) {
            return null;
        }
        return hitEdgeAbstract(this.canvas.Width(), this.canvas.Height());
    }

    /* access modifiers changed from: protected */
    public Direction hitEdgeAbstract(int canvasWidth, int canvasHeight) {
        boolean west = overWestEdge();
        boolean north = overNorthEdge();
        boolean east = overEastEdge(canvasWidth);
        boolean south = overSouthEdge(canvasHeight);
        if (!north && !south && !east && !west) {
            return null;
        }
        MoveIntoBounds();
        if (west) {
            if (north) {
                return Direction.Northwest;
            }
            if (south) {
                return Direction.Southwest;
            }
            return Direction.West;
        } else if (east) {
            if (north) {
                return Direction.Northeast;
            }
            if (south) {
                return Direction.Southeast;
            }
            return Direction.East;
        } else if (north) {
            return Direction.North;
        } else {
            return Direction.South;
        }
    }

    /* access modifiers changed from: protected */
    public final void moveIntoBounds(int canvasWidth, int canvasHeight) {
        boolean moved = false;
        if (Width() > canvasWidth) {
            if (this.xLeft != 0.0d) {
                this.xLeft = 0.0d;
                this.xCenter = xLeftToCenter(this.xLeft);
                moved = true;
            }
        } else if (overWestEdge()) {
            this.xLeft = 0.0d;
            this.xCenter = xLeftToCenter(this.xLeft);
            moved = true;
        } else if (overEastEdge(canvasWidth)) {
            this.xLeft = (double) (canvasWidth - Width());
            this.xCenter = xLeftToCenter(this.xLeft);
            moved = true;
        }
        if (Height() > canvasHeight) {
            if (this.yTop != 0.0d) {
                this.yTop = 0.0d;
                this.yCenter = yTopToCenter(this.yTop);
                moved = true;
            }
        } else if (overNorthEdge()) {
            this.yTop = 0.0d;
            this.yCenter = yTopToCenter(this.yTop);
            moved = true;
        } else if (overSouthEdge(canvasHeight)) {
            this.yTop = (double) (canvasHeight - Height());
            this.yCenter = yTopToCenter(this.yTop);
            moved = true;
        }
        if (moved) {
            registerChange();
        }
    }

    /* access modifiers changed from: protected */
    public void updateCoordinates() {
        this.xLeft += ((double) this.speed) * this.headingCos;
        this.xCenter = xLeftToCenter(this.xLeft);
        this.yTop += ((double) this.speed) * this.headingSin;
        this.yCenter = yTopToCenter(this.yTop);
    }

    private final boolean overWestEdge() {
        return this.xLeft < 0.0d;
    }

    private final boolean overEastEdge(int canvasWidth) {
        return this.xLeft + ((double) Width()) > ((double) canvasWidth);
    }

    private final boolean overNorthEdge() {
        return this.yTop < 0.0d;
    }

    private final boolean overSouthEdge(int canvasHeight) {
        return this.yTop + ((double) Height()) > ((double) canvasHeight);
    }

    public BoundingBox getBoundingBox(int border) {
        return new BoundingBox(this.xLeft - ((double) border), this.yTop - ((double) border), ((this.xLeft + ((double) Width())) - DEFAULT_Z) + ((double) border), ((this.yTop + ((double) Height())) - DEFAULT_Z) + ((double) border));
    }

    public static boolean colliding(Sprite sprite1, Sprite sprite2) {
        BoundingBox rect1 = sprite1.getBoundingBox(1);
        if (!rect1.intersectDestructively(sprite2.getBoundingBox(1))) {
            return false;
        }
        for (double x = rect1.getLeft(); x <= rect1.getRight(); x += DEFAULT_Z) {
            for (double y = rect1.getTop(); y <= rect1.getBottom(); y += DEFAULT_Z) {
                if (sprite1.containsPoint(x, y) && sprite2.containsPoint(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean intersectsWith(BoundingBox rect) {
        BoundingBox rect1 = getBoundingBox(0);
        if (!rect1.intersectDestructively(rect)) {
            return false;
        }
        for (double x = rect1.getLeft(); x < rect1.getRight(); x += DEFAULT_Z) {
            for (double y = rect1.getTop(); y < rect1.getBottom(); y += DEFAULT_Z) {
                if (containsPoint(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsPoint(double qx, double qy) {
        return qx >= this.xLeft && qx < this.xLeft + ((double) Width()) && qy >= this.yTop && qy < this.yTop + ((double) Height());
    }

    public void alarm() {
        if (this.initialized && this.speed != 0.0f) {
            updateCoordinates();
            registerChange();
        }
    }

    public HandlesEventDispatching getDispatchDelegate() {
        return this.canvas.$form();
    }

    public void onDestroy() {
        this.timerInternal.Enabled(false);
    }

    public void onDelete() {
        this.timerInternal.Enabled(false);
        this.canvas.removeSprite(this);
    }
}
