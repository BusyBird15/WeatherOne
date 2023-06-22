package com.google.appinventor.components.runtime;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.HorizontalAlignment;
import com.google.appinventor.components.common.MapFeature;
import com.google.appinventor.components.common.VerticalAlignment;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.GeometryUtil;
import com.google.appinventor.components.runtime.util.MapFactory;
import org.locationtech.jts.geom.Geometry;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MAPS, description = "<p>An icon positioned at a point to indicate information on a map. Markers can be used to provide an info window, custom fill and stroke colors, and custom images to convey information to the user.</p>", version = 4)
@UsesLibraries(libraries = "osmdroid.aar, androidsvg.jar")
public class Marker extends MapFeatureBaseWithFill implements MapFactory.MapMarker {
    private static final String TAG = Marker.class.getSimpleName();
    private static final MapFactory.MapFeatureVisitor<Double> bearingComputation = new MapFactory.MapFeatureVisitor<Double>() {
        public Double visit(MapFactory.MapMarker marker, Object... arguments) {
            return Double.valueOf(GeometryUtil.bearingTo(arguments[0], marker));
        }

        public Double visit(MapFactory.MapLineString lineString, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.bearingToCentroid(arguments[0], lineString));
            }
            return Double.valueOf(GeometryUtil.bearingToEdge(arguments[0], lineString));
        }

        public Double visit(MapFactory.MapPolygon polygon, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.bearingToCentroid(arguments[0], polygon));
            }
            return Double.valueOf(GeometryUtil.bearingToEdge(arguments[0], polygon));
        }

        public Double visit(MapFactory.MapCircle circle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.bearingToCentroid(arguments[0], circle));
            }
            return Double.valueOf(GeometryUtil.bearingToEdge(arguments[0], circle));
        }

        public Double visit(MapFactory.MapRectangle rectangle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.bearingToCentroid(arguments[0], rectangle));
            }
            return Double.valueOf(GeometryUtil.bearingToEdge(arguments[0], rectangle));
        }
    };
    private static final MapFactory.MapFeatureVisitor<Double> distanceComputation = new MapFactory.MapFeatureVisitor<Double>() {
        public Double visit(MapFactory.MapMarker marker, Object... arguments) {
            return Double.valueOf(GeometryUtil.distanceBetween((MapFactory.MapMarker) arguments[0], marker));
        }

        public Double visit(MapFactory.MapLineString lineString, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapMarker) arguments[0], lineString));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapMarker) arguments[0], lineString));
        }

        public Double visit(MapFactory.MapPolygon polygon, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapMarker) arguments[0], polygon));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapMarker) arguments[0], polygon));
        }

        public Double visit(MapFactory.MapCircle circle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapMarker) arguments[0], circle));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapMarker) arguments[0], circle));
        }

        public Double visit(MapFactory.MapRectangle rectangle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapMarker) arguments[0], rectangle));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapMarker) arguments[0], rectangle));
        }
    };
    private HorizontalAlignment anchorHAlign = HorizontalAlignment.Center;
    private VerticalAlignment anchorVAlign = VerticalAlignment.Bottom;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int height = -1;
    private String imagePath = "";
    private GeoPoint location = new GeoPoint(0.0d, 0.0d);
    /* access modifiers changed from: private */
    public volatile boolean pendingUpdate = false;
    private int width = -1;

    public Marker(MapFactory.MapFeatureContainer container) {
        super(container, distanceComputation);
        container.addFeature(this);
        ShowShadow(false);
        AnchorHorizontal(3);
        AnchorVertical(3);
        ImageAsset("");
        Width(-1);
        Height(-1);
        Latitude(0.0d);
        Longitude(0.0d);
    }

    @Options(MapFeature.class)
    @SimpleProperty(description = "Returns the type of the feature. For Markers, this returns MapFeature.Marker (\"Marker\").")
    public String Type() {
        return TypeAbstract().toUnderlyingValue();
    }

    public MapFeature TypeAbstract() {
        return MapFeature.Marker;
    }

    @DesignerProperty(defaultValue = "0", editorType = "latitude")
    @SimpleProperty
    public void Latitude(double latitude) {
        Log.d(TAG, "Latitude");
        if (latitude < -90.0d || latitude > 90.0d) {
            this.container.$form().dispatchErrorOccurredEvent(this, "Latitude", ErrorMessages.ERROR_INVALID_LATITUDE, Double.valueOf(latitude));
            return;
        }
        this.location.setLatitude(latitude);
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapMarker) this);
    }

    @SimpleProperty
    public double Latitude() {
        return this.location.getLatitude();
    }

    @DesignerProperty(defaultValue = "0", editorType = "longitude")
    @SimpleProperty
    public void Longitude(double longitude) {
        Log.d(TAG, "Longitude");
        if (longitude < -180.0d || longitude > 180.0d) {
            this.container.$form().dispatchErrorOccurredEvent(this, "Longitude", ErrorMessages.ERROR_INVALID_LONGITUDE, Double.valueOf(longitude));
            return;
        }
        this.location.setLongitude(longitude);
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapMarker) this);
    }

    @SimpleProperty
    public double Longitude() {
        return this.location.getLongitude();
    }

    @DesignerProperty(editorType = "asset")
    @SimpleProperty
    public void ImageAsset(@Asset String path) {
        Log.d(TAG, "ImageAsset");
        this.imagePath = path;
        setNeedsUpdate();
    }

    @SimpleProperty(description = "The ImageAsset property is used to provide an alternative image for the Marker.")
    public String ImageAsset() {
        return this.imagePath;
    }

    @SimpleProperty
    public void StrokeColor(int argb) {
        super.StrokeColor(argb);
        this.map.getController().updateFeatureStroke(this);
    }

    @DesignerProperty(defaultValue = "3", editorType = "horizontal_alignment")
    @SimpleProperty
    public void AnchorHorizontal(@Options(HorizontalAlignment.class) int horizontal) {
        HorizontalAlignment alignment = HorizontalAlignment.fromUnderlyingValue(Integer.valueOf(horizontal));
        if (alignment == null) {
            this.container.$form().dispatchErrorOccurredEvent(this, "AnchorHorizontal", ErrorMessages.ERROR_INVALID_ANCHOR_HORIZONTAL, Integer.valueOf(horizontal));
            return;
        }
        AnchorHorizontalAbstract(alignment);
    }

    @Options(HorizontalAlignment.class)
    @SimpleProperty(description = "The horizontal alignment property controls where the Marker's anchor is located relative to its width. The choices are: 1 = left aligned, 3 = horizontally centered, 2 = right aligned.")
    public int AnchorHorizontal() {
        return AnchorHorizontalAbstract().toUnderlyingValue().intValue();
    }

    public HorizontalAlignment AnchorHorizontalAbstract() {
        return this.anchorHAlign;
    }

    public void AnchorHorizontalAbstract(HorizontalAlignment alignment) {
        if (alignment != this.anchorHAlign) {
            this.anchorHAlign = alignment;
            this.map.getController().updateFeaturePosition((MapFactory.MapMarker) this);
        }
    }

    @DesignerProperty(defaultValue = "3", editorType = "vertical_alignment")
    @SimpleProperty
    public void AnchorVertical(@Options(VerticalAlignment.class) int vertical) {
        VerticalAlignment alignment = VerticalAlignment.fromUnderlyingValue(Integer.valueOf(vertical));
        if (alignment == null) {
            this.container.$form().dispatchErrorOccurredEvent(this, "AnchorVertical", ErrorMessages.ERROR_INVALID_ANCHOR_VERTICAL, Integer.valueOf(vertical));
            return;
        }
        AnchorVerticalAbstract(alignment);
    }

    @Options(VerticalAlignment.class)
    @SimpleProperty(description = "The vertical alignment property controls where the Marker's anchor is located relative to its height. The choices are: 1 = aligned at the top, 2 = vertically centered, 3 = aligned at the bottom.")
    public int AnchorVertical() {
        return AnchorVerticalAbstract().toUnderlyingValue().intValue();
    }

    public VerticalAlignment AnchorVerticalAbstract() {
        return this.anchorVAlign;
    }

    public void AnchorVerticalAbstract(VerticalAlignment alignment) {
        if (alignment != null) {
            this.anchorVAlign = alignment;
            this.map.getController().updateFeaturePosition((MapFactory.MapMarker) this);
        }
    }

    @SimpleProperty(userVisible = false)
    public void ShowShadow(boolean show) {
    }

    @SimpleProperty(description = "Gets whether or not the shadow of the Marker is shown.")
    public boolean ShowShadow() {
        return false;
    }

    @SimpleProperty
    public void Width(int width2) {
        this.width = width2;
        setNeedsUpdate();
    }

    @SimpleProperty
    public int Width() {
        if (this.width == -2) {
            return this.map.getView().getWidth();
        }
        if (this.width < -1000) {
            return (int) (((((double) (-this.width)) - 0.00408935546875d) / 100.0d) * ((double) this.map.getView().getWidth()));
        }
        return this.width;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public void WidthPercent(int pCent) {
        this.width = -1000 - pCent;
        setNeedsUpdate();
    }

    @SimpleProperty
    public void Height(int height2) {
        this.height = height2;
        setNeedsUpdate();
    }

    @SimpleProperty
    public int Height() {
        if (this.height == -2) {
            return this.map.getView().getHeight();
        }
        if (this.height < -1000) {
            return (int) (((((double) (-this.height)) - 0.00408935546875d) / 100.0d) * ((double) this.map.getView().getHeight()));
        }
        return this.height;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE)
    public void HeightPercent(int pCent) {
        this.height = -1000 - pCent;
        setNeedsUpdate();
    }

    @SimpleFunction(description = "Set the location of the marker.")
    public void SetLocation(double latitude, double longitude) {
        Log.d(TAG, "SetLocation");
        this.location.setCoords(latitude, longitude);
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapMarker) this);
    }

    public double DistanceToPoint(double latitude, double longitude, boolean centroid) {
        return DistanceToPoint(latitude, longitude);
    }

    @SimpleFunction(description = "Compute the distance, in meters, between a Marker and a latitude, longitude point.")
    public double DistanceToPoint(double latitude, double longitude) {
        return GeometryUtil.distanceBetween((MapFactory.MapMarker) this, new GeoPoint(latitude, longitude));
    }

    @SimpleFunction(description = "Returns the bearing from the Marker to the given latitude and longitude, in degrees from due north.")
    public double BearingToPoint(double latitude, double longitude) {
        return this.location.bearingTo(new GeoPoint(latitude, longitude));
    }

    @SimpleFunction(description = "Returns the bearing from the Marker to the given map feature, in degrees from due north. If the centroids parameter is true, the bearing will be to the center of the map feature. Otherwise, the bearing will be computed to the point in the feature nearest the Marker.")
    public double BearingToFeature(MapFactory.MapFeature mapFeature, boolean centroids) {
        if (mapFeature == null) {
            return -1.0d;
        }
        return ((Double) mapFeature.accept(bearingComputation, this, Boolean.valueOf(centroids))).doubleValue();
    }

    public IGeoPoint getLocation() {
        return this.location;
    }

    public void updateLocation(double latitude, double longitude) {
        this.location = new GeoPoint(latitude, longitude);
        clearGeometry();
    }

    public <T> T accept(MapFactory.MapFeatureVisitor<T> visitor, Object... arguments) {
        return visitor.visit((MapFactory.MapMarker) this, arguments);
    }

    /* access modifiers changed from: protected */
    public Geometry computeGeometry() {
        return GeometryUtil.createGeometry(this.location);
    }

    private void setNeedsUpdate() {
        synchronized (this) {
            if (!this.pendingUpdate) {
                this.pendingUpdate = true;
                this.handler.postDelayed(new Runnable() {
                    public void run() {
                        Marker.this.map.getController().updateFeatureImage(Marker.this);
                        synchronized (Marker.this) {
                            boolean unused = Marker.this.pendingUpdate = false;
                        }
                    }
                }, 1);
            }
        }
    }
}
