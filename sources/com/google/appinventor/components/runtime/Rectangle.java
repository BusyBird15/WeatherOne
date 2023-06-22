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
import com.google.appinventor.components.runtime.util.YailList;
import org.locationtech.jts.geom.Geometry;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MAPS, description = "Rectangle", version = 2)
public class Rectangle extends PolygonBase implements MapFactory.MapRectangle {
    private static final MapFactory.MapFeatureVisitor<Double> distanceComputation = new MapFactory.MapFeatureVisitor<Double>() {
        public Double visit(MapFactory.MapMarker marker, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(marker, (MapFactory.MapRectangle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(marker, (MapFactory.MapRectangle) arguments[0]));
        }

        public Double visit(MapFactory.MapLineString lineString, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(lineString, (MapFactory.MapRectangle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(lineString, (MapFactory.MapRectangle) arguments[0]));
        }

        public Double visit(MapFactory.MapPolygon polygon, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(polygon, (MapFactory.MapRectangle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(polygon, (MapFactory.MapRectangle) arguments[0]));
        }

        public Double visit(MapFactory.MapCircle circle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(circle, (MapFactory.MapRectangle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(circle, (MapFactory.MapRectangle) arguments[0]));
        }

        public Double visit(MapFactory.MapRectangle rectangle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(rectangle, (MapFactory.MapRectangle) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(rectangle, (MapFactory.MapRectangle) arguments[0]));
        }
    };
    private double east = 0.0d;
    private double north = 0.0d;
    private double south = 0.0d;
    private double west = 0.0d;

    public Rectangle(MapFactory.MapFeatureContainer container) {
        super(container, distanceComputation);
        container.addFeature(this);
    }

    @Options(MapFeature.class)
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the type of the feature. For rectangles, this returns MapFeature.Rectangle (\"Rectangle\").")
    public String Type() {
        return TypeAbstract().toUnderlyingValue();
    }

    public MapFeature TypeAbstract() {
        return MapFeature.Rectangle;
    }

    @DesignerProperty(defaultValue = "0", editorType = "float")
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The east edge of the rectangle, in decimal degrees east of the prime meridian.")
    public void EastLongitude(double east2) {
        this.east = east2;
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapRectangle) this);
    }

    @SimpleProperty
    public double EastLongitude() {
        return this.east;
    }

    @DesignerProperty(defaultValue = "0", editorType = "float")
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The north edge of the rectangle, in decimal degrees north of the equator.")
    public void NorthLatitude(double north2) {
        this.north = north2;
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapRectangle) this);
    }

    @SimpleProperty
    public double NorthLatitude() {
        return this.north;
    }

    @DesignerProperty(defaultValue = "0", editorType = "float")
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The south edge of the rectangle, in decimal degrees north of the equator.")
    public void SouthLatitude(double south2) {
        this.south = south2;
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapRectangle) this);
    }

    @SimpleProperty
    public double SouthLatitude() {
        return this.south;
    }

    @DesignerProperty(defaultValue = "0", editorType = "float")
    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "The west edge of the rectangle, in decimal degrees east of the equator.")
    public void WestLongitude(double west2) {
        this.west = west2;
        clearGeometry();
        this.map.getController().updateFeaturePosition((MapFactory.MapRectangle) this);
    }

    @SimpleProperty
    public double WestLongitude() {
        return this.west;
    }

    @SimpleFunction(description = "Returns the center of the Rectangle as a list of the form (Latitude Longitude).")
    public YailList Center() {
        return GeometryUtil.asYailList(getCentroid());
    }

    @SimpleFunction(description = "Returns the bounding box of the Rectangle in the format ((North West) (South East)).")
    public YailList Bounds() {
        return YailList.makeList((Object[]) new YailList[]{YailList.makeList((Object[]) new Double[]{Double.valueOf(this.north), Double.valueOf(this.west)}), YailList.makeList((Object[]) new Double[]{Double.valueOf(this.south), Double.valueOf(this.east)})});
    }

    @SimpleFunction(description = "Moves the Rectangle so that it is centered on the given latitude and longitude while attempting to maintain the width and height of the Rectangle as measured from the center to the edges.")
    public void SetCenter(double latitude, double longitude) {
        if (latitude < -90.0d || latitude > 90.0d) {
            this.container.$form().dispatchErrorOccurredEvent(this, "SetCenter", ErrorMessages.ERROR_INVALID_POINT, Double.valueOf(latitude), Double.valueOf(longitude));
        } else if (longitude < -180.0d || longitude > 180.0d) {
            this.container.$form().dispatchErrorOccurredEvent(this, "SetCenter", ErrorMessages.ERROR_INVALID_POINT, Double.valueOf(latitude), Double.valueOf(longitude));
        } else {
            GeoPoint currentCenter = getCentroid();
            GeoPoint northPoint = new GeoPoint(this.north, currentCenter.getLongitude());
            GeoPoint southPoint = new GeoPoint(this.south, currentCenter.getLongitude());
            GeoPoint eastPoint = new GeoPoint(currentCenter.getLatitude(), this.east);
            GeoPoint westPoint = new GeoPoint(currentCenter.getLatitude(), this.west);
            double latExtent2 = GeometryUtil.distanceBetween((IGeoPoint) northPoint, (IGeoPoint) southPoint) / 2.0d;
            double longExtent2 = GeometryUtil.distanceBetween((IGeoPoint) eastPoint, (IGeoPoint) westPoint) / 2.0d;
            currentCenter.setCoords(latitude, longitude);
            this.north = currentCenter.destinationPoint(latExtent2, 0.0f).getLatitude();
            this.south = currentCenter.destinationPoint(latExtent2, 180.0f).getLatitude();
            this.east = currentCenter.destinationPoint(longExtent2, 90.0f).getLongitude();
            this.west = currentCenter.destinationPoint(longExtent2, 270.0f).getLongitude();
            clearGeometry();
            this.map.getController().updateFeaturePosition((MapFactory.MapRectangle) this);
        }
    }

    public <T> T accept(MapFactory.MapFeatureVisitor<T> visitor, Object... arguments) {
        return visitor.visit((MapFactory.MapRectangle) this, arguments);
    }

    /* access modifiers changed from: protected */
    public Geometry computeGeometry() {
        return GeometryUtil.createGeometry(this.north, this.east, this.south, this.west);
    }

    public void updateBounds(double north2, double west2, double south2, double east2) {
        this.north = north2;
        this.west = west2;
        this.south = south2;
        this.east = east2;
        clearGeometry();
    }
}
