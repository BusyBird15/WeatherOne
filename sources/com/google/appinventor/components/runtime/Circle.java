package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.MapFeature;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.GeometryUtil;
import com.google.appinventor.components.runtime.util.MapFactory;
import org.locationtech.jts.geom.Geometry;
import org.osmdroid.util.GeoPoint;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MAPS, description = "Circle", version = 2)
public class Circle extends PolygonBase implements MapFactory.MapCircle {
    private static final MapFactory.MapFeatureVisitor<Double> distanceComputation = new MapFactory.MapFeatureVisitor<Double>() {
        public Double visit(MapFactory.MapMarker marker, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(marker, (MapFactory.MapCircle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(marker, (MapFactory.MapCircle) arguments[0]));
        }

        public Double visit(MapFactory.MapLineString lineString, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(lineString, (MapFactory.MapCircle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(lineString, (MapFactory.MapCircle) arguments[0]));
        }

        public Double visit(MapFactory.MapPolygon polygon, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(polygon, (MapFactory.MapCircle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(polygon, (MapFactory.MapCircle) arguments[0]));
        }

        public Double visit(MapFactory.MapCircle circle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(circle, (MapFactory.MapCircle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(circle, (MapFactory.MapCircle) arguments[0]));
        }

        public Double visit(MapFactory.MapRectangle rectangle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapCircle) arguments[0], rectangle));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapCircle) arguments[0], rectangle));
        }
    };
    private GeoPoint center = new GeoPoint(0.0d, 0.0d);
    private double latitude;
    private double longitude;
    private double radius;

    public Circle(MapFactory.MapFeatureContainer container) {
        super(container, distanceComputation);
        container.addFeature(this);
    }

    @Options(MapFeature.class)
    @SimpleProperty(description = "Returns the type of the feature. For Circles, this returns MapFeature.Circle (\"Circle\").")
    public String Type() {
        return TypeAbstract().toUnderlyingValue();
    }

    public MapFeature TypeAbstract() {
        return MapFeature.Circle;
    }

    @DesignerProperty(defaultValue = "0", editorType = "non_negative_float")
    @SimpleProperty
    public void Radius(double radius2) {
        this.radius = radius2;
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapCircle) this);
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The radius of the circle in meters.")
    public double Radius() {
        return this.radius;
    }

    @DesignerProperty(defaultValue = "0", editorType = "latitude")
    @SimpleProperty
    public void Latitude(double latitude2) {
        if (GeometryUtil.isValidLatitude(latitude2)) {
            this.latitude = latitude2;
            this.center.setLatitude(latitude2);
            clearGeometry();
            this.map.getController().updateFeaturePosition((MapFactory.MapCircle) this);
            return;
        }
        getDispatchDelegate().dispatchErrorOccurredEvent(this, "Latitude", ErrorMessages.ERROR_INVALID_LATITUDE, Double.valueOf(latitude2));
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The latitude of the center of the circle.")
    public double Latitude() {
        return this.latitude;
    }

    @DesignerProperty(defaultValue = "0", editorType = "longitude")
    @SimpleProperty
    public void Longitude(double longitude2) {
        if (GeometryUtil.isValidLongitude(longitude2)) {
            this.longitude = longitude2;
            this.center.setLongitude(longitude2);
            clearGeometry();
            this.map.getController().updateFeaturePosition((MapFactory.MapCircle) this);
            return;
        }
        getDispatchDelegate().dispatchErrorOccurredEvent(this, "Longitude", ErrorMessages.ERROR_INVALID_LONGITUDE, Double.valueOf(longitude2));
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The longitude of the center of the circle.")
    public double Longitude() {
        return this.longitude;
    }

    @SimpleFunction(description = "Set the center of the Circle.")
    public void SetLocation(double latitude2, double longitude2) {
        if (!GeometryUtil.isValidLatitude(latitude2)) {
            getDispatchDelegate().dispatchErrorOccurredEvent(this, "SetLocation", ErrorMessages.ERROR_INVALID_LATITUDE, Double.valueOf(latitude2));
        } else if (!GeometryUtil.isValidLongitude(longitude2)) {
            getDispatchDelegate().dispatchErrorOccurredEvent(this, "SetLocation", ErrorMessages.ERROR_INVALID_LONGITUDE, Double.valueOf(longitude2));
        } else {
            this.latitude = latitude2;
            this.longitude = longitude2;
            this.center.setLatitude(latitude2);
            this.center.setLongitude(longitude2);
            clearGeometry();
            this.map.getController().updateFeaturePosition((MapFactory.MapCircle) this);
        }
    }

    public <T> T accept(MapFactory.MapFeatureVisitor<T> visitor, Object... arguments) {
        return visitor.visit((MapFactory.MapCircle) this, arguments);
    }

    /* access modifiers changed from: protected */
    public Geometry computeGeometry() {
        return GeometryUtil.createGeometry(this.center);
    }

    public void updateCenter(double latitude2, double longitude2) {
        this.latitude = latitude2;
        this.longitude = longitude2;
        clearGeometry();
    }
}
