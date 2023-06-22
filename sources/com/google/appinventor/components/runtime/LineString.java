package com.google.appinventor.components.runtime;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.MapFeature;
import com.google.appinventor.components.runtime.errors.DispatchableError;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.GeometryUtil;
import com.google.appinventor.components.runtime.util.MapFactory;
import com.google.appinventor.components.runtime.util.YailList;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.locationtech.jts.geom.Geometry;
import org.osmdroid.util.GeoPoint;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MAPS, description = "LineString", version = 2)
public class LineString extends MapFeatureBase implements MapFactory.MapLineString {
    private static final String TAG = LineString.class.getSimpleName();
    private static final MapFactory.MapFeatureVisitor<Double> distanceComputation = new MapFactory.MapFeatureVisitor<Double>() {
        public Double visit(MapFactory.MapMarker marker, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(marker, (MapFactory.MapLineString) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(marker, (MapFactory.MapLineString) arguments[0]));
        }

        public Double visit(MapFactory.MapLineString lineString, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids(lineString, (MapFactory.MapLineString) arguments[0]));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges(lineString, (MapFactory.MapLineString) arguments[0]));
        }

        public Double visit(MapFactory.MapPolygon polygon, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapLineString) arguments[0], polygon));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapLineString) arguments[0], polygon));
        }

        public Double visit(MapFactory.MapCircle circle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapLineString) arguments[0], circle));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapLineString) arguments[0], circle));
        }

        public Double visit(MapFactory.MapRectangle rectangle, Object... arguments) {
            if (arguments[1].booleanValue()) {
                return Double.valueOf(GeometryUtil.distanceBetweenCentroids((MapFactory.MapLineString) arguments[0], rectangle));
            }
            return Double.valueOf(GeometryUtil.distanceBetweenEdges((MapFactory.MapLineString) arguments[0], rectangle));
        }
    };
    private List<GeoPoint> points = new ArrayList();

    public LineString(MapFactory.MapFeatureContainer container) {
        super(container, distanceComputation);
        StrokeWidth(3);
        container.addFeature(this);
    }

    @Options(MapFeature.class)
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the type of the map feature. For LineString, this returns MapFeature.LineString (\"LineString\").")
    public String Type() {
        return TypeAbstract().toUnderlyingValue();
    }

    public MapFeature TypeAbstract() {
        return MapFeature.LineString;
    }

    @SimpleProperty(category = PropertyCategory.APPEARANCE, description = "A list of latitude and longitude pairs that represent the line segments of the polyline.")
    public YailList Points() {
        return GeometryUtil.pointsListToYailList(this.points);
    }

    @SimpleProperty
    public void Points(@NonNull YailList points2) {
        if (points2.size() < 2) {
            this.container.$form().dispatchErrorOccurredEvent(this, "Points", ErrorMessages.ERROR_LINESTRING_TOO_FEW_POINTS, Integer.valueOf(points2.length() - 1));
            return;
        }
        try {
            this.points = GeometryUtil.pointsFromYailList(points2);
            clearGeometry();
            this.map.getController().updateFeaturePosition((MapFactory.MapLineString) this);
        } catch (DispatchableError e) {
            this.container.$form().dispatchErrorOccurredEvent(this, "Points", e.getErrorCode(), e.getArguments());
        }
    }

    @DesignerProperty(editorType = "textArea")
    @SimpleProperty
    public void PointsFromString(String points2) {
        try {
            List<GeoPoint> geopoints = new ArrayList<>();
            JSONArray array = new JSONArray(points2);
            if (array.length() < 2) {
                throw new DispatchableError(ErrorMessages.ERROR_LINESTRING_TOO_FEW_POINTS, Integer.valueOf(array.length()));
            }
            int length = array.length();
            int i = 0;
            while (i < length) {
                JSONArray point = array.optJSONArray(i);
                if (point == null) {
                    throw new DispatchableError(ErrorMessages.ERROR_EXPECTED_ARRAY_AT_INDEX, Integer.valueOf(i), array.get(i).toString());
                } else if (point.length() < 2) {
                    throw new DispatchableError(ErrorMessages.ERROR_LINESTRING_TOO_FEW_FIELDS, Integer.valueOf(i), Integer.valueOf(points2.length()));
                } else {
                    double latitude = point.optDouble(0, Double.NaN);
                    double longitude = point.optDouble(1, Double.NaN);
                    if (!GeometryUtil.isValidLatitude(latitude)) {
                        throw new DispatchableError(ErrorMessages.ERROR_INVALID_LATITUDE_IN_POINT_AT_INDEX, Integer.valueOf(i), array.get(0).toString());
                    } else if (!GeometryUtil.isValidLongitude(longitude)) {
                        throw new DispatchableError(ErrorMessages.ERROR_INVALID_LONGITUDE_IN_POINT_AT_INDEX, Integer.valueOf(i), array.get(1).toString());
                    } else {
                        geopoints.add(new GeoPoint(latitude, longitude));
                        i++;
                    }
                }
            }
            this.points = geopoints;
            clearGeometry();
            this.map.getController().updateFeaturePosition((MapFactory.MapLineString) this);
        } catch (JSONException e) {
            Log.e(TAG, "Malformed string to LineString.PointsFromString", e);
            this.container.$form().dispatchErrorOccurredEvent(this, "PointsFromString", ErrorMessages.ERROR_LINESTRING_PARSE_ERROR, e.getMessage());
        } catch (DispatchableError e2) {
            this.container.$form().dispatchErrorOccurredEvent(this, "PointsFromString", e2.getErrorCode(), e2.getArguments());
        }
    }

    @DesignerProperty(defaultValue = "3")
    @SimpleProperty
    public void StrokeWidth(int width) {
        super.StrokeWidth(width);
    }

    public List<GeoPoint> getPoints() {
        return this.points;
    }

    public <T> T accept(MapFactory.MapFeatureVisitor<T> visitor, Object... arguments) {
        return visitor.visit((MapFactory.MapLineString) this, arguments);
    }

    /* access modifiers changed from: protected */
    public Geometry computeGeometry() {
        return GeometryUtil.createGeometry(this.points);
    }

    public void updatePoints(List<GeoPoint> points2) {
        this.points = points2;
        clearGeometry();
    }
}
