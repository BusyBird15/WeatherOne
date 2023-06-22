package com.google.appinventor.components.runtime.util;

import android.text.TextUtils;
import android.util.Log;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.LineString;
import com.google.appinventor.components.runtime.Marker;
import com.google.appinventor.components.runtime.Polygon;
import com.google.appinventor.components.runtime.util.MapFactory;
import com.google.common.annotations.VisibleForTesting;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

public final class GeoJSONUtil {
    private static final int ERROR_CODE_MALFORMED_GEOJSON = -3;
    private static final String ERROR_MALFORMED_GEOJSON = "Malformed GeoJSON response. Expected FeatureCollection as root element.";
    private static final String ERROR_UNKNOWN_TYPE = "Unrecognized/invalid type in JSON object";
    private static final String GEOJSON_COORDINATES = "coordinates";
    private static final String GEOJSON_FEATURE = "Feature";
    private static final String GEOJSON_FEATURECOLLECTION = "FeatureCollection";
    private static final String GEOJSON_FEATURES = "features";
    private static final String GEOJSON_GEOMETRY = "geometry";
    private static final String GEOJSON_GEOMETRYCOLLECTION = "GeometryCollection";
    private static final String GEOJSON_PROPERTIES = "properties";
    private static final String GEOJSON_TYPE = "type";
    private static final int KEY = 1;
    private static final int LATITUDE = 2;
    private static final int LONGITUDE = 1;
    private static final String PROPERTY_ANCHOR_HORIZONTAL = "anchorHorizontal";
    private static final String PROPERTY_ANCHOR_VERTICAL = "anchorVertical";
    private static final String PROPERTY_DESCRIPTION = "description";
    private static final String PROPERTY_DRAGGABLE = "draggable";
    private static final String PROPERTY_FILL = "fill";
    private static final String PROPERTY_FILL_OPACITY = "fill-opacity";
    private static final String PROPERTY_HEIGHT = "height";
    private static final String PROPERTY_IMAGE = "image";
    private static final String PROPERTY_INFOBOX = "infobox";
    private static final String PROPERTY_STROKE = "stroke";
    private static final String PROPERTY_STROKE_OPACITY = "stroke-opacity";
    private static final String PROPERTY_STROKE_WIDTH = "stroke-width";
    private static final String PROPERTY_TITLE = "title";
    private static final String PROPERTY_VISIBLE = "visible";
    private static final String PROPERTY_WIDTH = "width";
    private static final Map<String, PropertyApplication> SUPPORTED_PROPERTIES = new HashMap();
    private static final int VALUE = 2;
    private static final Map<String, Integer> colors = new HashMap();

    private interface PropertyApplication {
        void apply(MapFactory.MapFeature mapFeature, Object obj);
    }

    static {
        colors.put("black", -16777216);
        colors.put("blue", Integer.valueOf(Component.COLOR_BLUE));
        colors.put("cyan", Integer.valueOf(Component.COLOR_CYAN));
        colors.put("darkgray", Integer.valueOf(Component.COLOR_DKGRAY));
        colors.put("gray", Integer.valueOf(Component.COLOR_GRAY));
        colors.put("green", Integer.valueOf(Component.COLOR_GREEN));
        colors.put("lightgray", Integer.valueOf(Component.COLOR_LTGRAY));
        colors.put("magenta", Integer.valueOf(Component.COLOR_MAGENTA));
        colors.put("orange", Integer.valueOf(Component.COLOR_ORANGE));
        colors.put("pink", Integer.valueOf(Component.COLOR_PINK));
        colors.put("red", -65536);
        colors.put("white", -1);
        colors.put("yellow", -256);
        SUPPORTED_PROPERTIES.put(PROPERTY_ANCHOR_HORIZONTAL.toLowerCase(), new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.MapMarker) {
                    ((MapFactory.MapMarker) feature).AnchorHorizontal(GeoJSONUtil.parseIntegerOrString(value));
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_ANCHOR_VERTICAL.toLowerCase(), new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.MapMarker) {
                    ((MapFactory.MapMarker) feature).AnchorHorizontal();
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_DESCRIPTION, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                feature.Description(value.toString());
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_DRAGGABLE, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                feature.Draggable(GeoJSONUtil.parseBooleanOrString(value));
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_FILL, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                int parseColor;
                if (feature instanceof MapFactory.HasFill) {
                    MapFactory.HasFill hasFill = (MapFactory.HasFill) feature;
                    if (value instanceof Number) {
                        parseColor = ((Number) value).intValue();
                    } else {
                        parseColor = GeoJSONUtil.parseColor(value.toString());
                    }
                    hasFill.FillColor(parseColor);
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_FILL_OPACITY, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.HasFill) {
                    ((MapFactory.HasFill) feature).FillOpacity(GeoJSONUtil.parseFloatOrString(value));
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_HEIGHT, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.MapMarker) {
                    ((MapFactory.MapMarker) feature).Height(GeoJSONUtil.parseIntegerOrString(value));
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_IMAGE, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.MapMarker) {
                    ((MapFactory.MapMarker) feature).ImageAsset(value.toString());
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_INFOBOX, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                feature.EnableInfobox(GeoJSONUtil.parseBooleanOrString(value));
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_STROKE, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                int parseColor;
                if (feature instanceof MapFactory.HasStroke) {
                    MapFactory.HasStroke hasStroke = (MapFactory.HasStroke) feature;
                    if (value instanceof Number) {
                        parseColor = ((Number) value).intValue();
                    } else {
                        parseColor = GeoJSONUtil.parseColor(value.toString());
                    }
                    hasStroke.StrokeColor(parseColor);
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_STROKE_OPACITY, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.HasStroke) {
                    ((MapFactory.HasStroke) feature).StrokeOpacity(GeoJSONUtil.parseFloatOrString(value));
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_STROKE_WIDTH, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.HasStroke) {
                    ((MapFactory.HasStroke) feature).StrokeWidth(GeoJSONUtil.parseIntegerOrString(value));
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_TITLE, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                feature.Title(value.toString());
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_WIDTH, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                if (feature instanceof MapFactory.MapMarker) {
                    ((MapFactory.MapMarker) feature).Width(GeoJSONUtil.parseIntegerOrString(value));
                }
            }
        });
        SUPPORTED_PROPERTIES.put(PROPERTY_VISIBLE, new PropertyApplication() {
            public void apply(MapFactory.MapFeature feature, Object value) {
                feature.Visible(GeoJSONUtil.parseBooleanOrString(value));
            }
        });
    }

    private GeoJSONUtil() {
    }

    @VisibleForTesting
    static int parseColor(String value) {
        String lcValue = value.toLowerCase();
        Integer result = colors.get(lcValue);
        if (result != null) {
            return result.intValue();
        }
        if (lcValue.startsWith("#")) {
            return parseColorHex(lcValue.substring(1));
        }
        if (lcValue.startsWith("&h")) {
            return parseColorHex(lcValue.substring(2));
        }
        return -65536;
    }

    @VisibleForTesting
    static int parseColorHex(String value) {
        int argb;
        int argb2 = 0;
        if (value.length() == 3) {
            argb = -16777216;
            for (int i = 0; i < value.length(); i++) {
                int hex = charToHex(value.charAt(i));
                argb |= ((hex << 4) | hex) << ((2 - i) * 8);
            }
        } else if (value.length() == 6) {
            int argb3 = -16777216;
            for (int i2 = 0; i2 < 3; i2++) {
                argb3 = argb | (((charToHex(value.charAt(i2 * 2)) << 4) | charToHex(value.charAt((i2 * 2) + 1))) << ((2 - i2) * 8));
            }
        } else if (value.length() == 8) {
            for (int i3 = 0; i3 < 4; i3++) {
                argb2 = argb | (((charToHex(value.charAt(i3 * 2)) << 4) | charToHex(value.charAt((i3 * 2) + 1))) << ((3 - i3) * 8));
            }
        } else {
            throw new IllegalArgumentException();
        }
        return argb;
    }

    @VisibleForTesting
    static int charToHex(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        if ('a' <= c && c <= 'f') {
            return (c - 'a') + 10;
        }
        if ('A' <= c && c <= 'F') {
            return (c - 'A') + 10;
        }
        throw new IllegalArgumentException("Invalid hex character. Expected [0-9A-Fa-f].");
    }

    public static MapFactory.MapFeature processGeoJSONFeature(String logTag, MapFactory.MapFeatureContainer container, YailList descriptions) {
        String type = null;
        YailList geometry = null;
        YailList properties = null;
        Iterator it = ((LList) descriptions.getCdr()).iterator();
        while (it.hasNext()) {
            YailList keyvalue = (YailList) it.next();
            String key = keyvalue.getString(0);
            Object value = keyvalue.getObject(1);
            if (GEOJSON_TYPE.equals(key)) {
                type = (String) value;
            } else if (GEOJSON_GEOMETRY.equals(key)) {
                geometry = (YailList) value;
            } else if (GEOJSON_PROPERTIES.equals(key)) {
                properties = (YailList) value;
            } else {
                Log.w(logTag, String.format("Unsupported field \"%s\" in JSON format", new Object[]{key}));
            }
        }
        if (!GEOJSON_FEATURE.equals(type)) {
            throw new IllegalArgumentException(String.format("Unknown type \"%s\"", new Object[]{type}));
        } else if (geometry == null) {
            return null;
        } else {
            MapFactory.MapFeature feature = processGeometry(logTag, container, geometry);
            if (properties == null) {
                return feature;
            }
            processProperties(logTag, feature, properties);
            return feature;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: com.google.appinventor.components.runtime.util.YailList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.google.appinventor.components.runtime.util.MapFactory.MapFeature processGeometry(java.lang.String r11, com.google.appinventor.components.runtime.util.MapFactory.MapFeatureContainer r12, com.google.appinventor.components.runtime.util.YailList r13) {
        /*
            r10 = 1
            r9 = 0
            r4 = 0
            r0 = 0
            java.lang.Object r6 = r13.getCdr()
            gnu.lists.LList r6 = (gnu.lists.LList) r6
            java.util.Iterator r6 = r6.iterator()
        L_0x000e:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L_0x0049
            java.lang.Object r3 = r6.next()
            r2 = r3
            com.google.appinventor.components.runtime.util.YailList r2 = (com.google.appinventor.components.runtime.util.YailList) r2
            java.lang.String r1 = r2.getString(r9)
            java.lang.Object r5 = r2.getObject(r10)
            java.lang.String r7 = "type"
            boolean r7 = r7.equals(r1)
            if (r7 == 0) goto L_0x002f
            r4 = r5
            java.lang.String r4 = (java.lang.String) r4
            goto L_0x000e
        L_0x002f:
            java.lang.String r7 = "coordinates"
            boolean r7 = r7.equals(r1)
            if (r7 == 0) goto L_0x003b
            r0 = r5
            com.google.appinventor.components.runtime.util.YailList r0 = (com.google.appinventor.components.runtime.util.YailList) r0
            goto L_0x000e
        L_0x003b:
            java.lang.String r7 = "Unsupported field \"%s\" in JSON format"
            java.lang.Object[] r8 = new java.lang.Object[r10]
            r8[r9] = r1
            java.lang.String r7 = java.lang.String.format(r7, r8)
            android.util.Log.w(r11, r7)
            goto L_0x000e
        L_0x0049:
            if (r0 != 0) goto L_0x0053
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = "No coordinates found in GeoJSON Feature"
            r6.<init>(r7)
            throw r6
        L_0x0053:
            com.google.appinventor.components.runtime.util.MapFactory$MapFeature r6 = processCoordinates(r12, r4, r0)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.GeoJSONUtil.processGeometry(java.lang.String, com.google.appinventor.components.runtime.util.MapFactory$MapFeatureContainer, com.google.appinventor.components.runtime.util.YailList):com.google.appinventor.components.runtime.util.MapFactory$MapFeature");
    }

    private static MapFactory.MapFeature processCoordinates(MapFactory.MapFeatureContainer container, String type, YailList coordinates) {
        if (MapFactory.MapFeatureType.TYPE_POINT.equals(type)) {
            return markerFromGeoJSON(container, coordinates);
        }
        if (MapFactory.MapFeatureType.TYPE_LINESTRING.equals(type)) {
            return lineStringFromGeoJSON(container, coordinates);
        }
        if (MapFactory.MapFeatureType.TYPE_POLYGON.equals(type)) {
            return polygonFromGeoJSON(container, coordinates);
        }
        if (MapFactory.MapFeatureType.TYPE_MULTIPOLYGON.equals(type)) {
            return multipolygonFromGeoJSON(container, coordinates);
        }
        throw new IllegalArgumentException();
    }

    private static MapFactory.MapMarker markerFromGeoJSON(MapFactory.MapFeatureContainer container, YailList coordinates) {
        if (coordinates.length() != 3) {
            throw new IllegalArgumentException("Invalid coordinate supplied in GeoJSON");
        }
        Marker marker = new Marker(container);
        marker.Latitude(((Number) coordinates.get(2)).doubleValue());
        marker.Longitude(((Number) coordinates.get(1)).doubleValue());
        return marker;
    }

    private static MapFactory.MapLineString lineStringFromGeoJSON(MapFactory.MapFeatureContainer container, YailList coordinates) {
        if (coordinates.size() < 2) {
            throw new IllegalArgumentException("Too few coordinates supplied in GeoJSON");
        }
        LineString lineString = new LineString(container);
        lineString.Points(swapCoordinates(coordinates));
        return lineString;
    }

    private static MapFactory.MapPolygon polygonFromGeoJSON(MapFactory.MapFeatureContainer container, YailList coordinates) {
        Polygon polygon = new Polygon(container);
        Iterator i = coordinates.iterator();
        i.next();
        polygon.Points(swapCoordinates((YailList) i.next()));
        if (i.hasNext()) {
            polygon.HolePoints(YailList.makeList((List) swapNestedCoordinates((LList) ((Pair) coordinates.getCdr()).getCdr())));
        }
        polygon.Initialize();
        return polygon;
    }

    private static MapFactory.MapPolygon multipolygonFromGeoJSON(MapFactory.MapFeatureContainer container, YailList coordinates) {
        Polygon polygon = new Polygon(container);
        List<YailList> points = new ArrayList<>();
        List<YailList> holePoints = new ArrayList<>();
        Iterator i = coordinates.iterator();
        i.next();
        while (i.hasNext()) {
            YailList list = (YailList) i.next();
            points.add(swapCoordinates((YailList) list.get(1)));
            holePoints.add(YailList.makeList((List) swapNestedCoordinates((LList) ((Pair) list.getCdr()).getCdr())));
        }
        polygon.Points(YailList.makeList((List) points));
        polygon.HolePoints(YailList.makeList((List) holePoints));
        polygon.Initialize();
        return polygon;
    }

    private static void processProperties(String logTag, MapFactory.MapFeature feature, YailList properties) {
        Iterator it = properties.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof YailList) {
                YailList pair = (YailList) o;
                String key = pair.get(1).toString();
                PropertyApplication application = SUPPORTED_PROPERTIES.get(key.toLowerCase());
                if (application != null) {
                    application.apply(feature, pair.get(2));
                } else {
                    Log.i(logTag, String.format("Ignoring GeoJSON property \"%s\"", new Object[]{key}));
                }
            }
        }
    }

    @VisibleForTesting
    static boolean parseBooleanOrString(Object value) {
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        if (value instanceof String) {
            return !"false".equalsIgnoreCase((String) value) && ((String) value).length() != 0;
        }
        if (value instanceof FString) {
            return parseBooleanOrString(value.toString());
        }
        throw new IllegalArgumentException();
    }

    @VisibleForTesting
    static int parseIntegerOrString(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        if (value instanceof FString) {
            return Integer.parseInt(value.toString());
        }
        throw new IllegalArgumentException();
    }

    @VisibleForTesting
    static float parseFloatOrString(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            return Float.parseFloat((String) value);
        }
        if (value instanceof FString) {
            return Float.parseFloat(value.toString());
        }
        throw new IllegalArgumentException();
    }

    public static List<YailList> getGeoJSONFeatures(String logTag, String content) throws JSONException {
        JSONArray features = new JSONObject(stripBOM(content)).getJSONArray(GEOJSON_FEATURES);
        List<YailList> yailFeatures = new ArrayList<>();
        for (int i = 0; i < features.length(); i++) {
            yailFeatures.add(jsonObjectToYail(logTag, features.getJSONObject(i)));
        }
        return yailFeatures;
    }

    public static String getGeoJSONType(String content, String geojsonType) throws JSONException {
        return new JSONObject(stripBOM(content)).optString(geojsonType);
    }

    private static YailList jsonObjectToYail(String logTag, JSONObject object) throws JSONException {
        List<YailList> pairs = new ArrayList<>();
        Iterator<String> j = object.keys();
        while (j.hasNext()) {
            String key = j.next();
            Object value = object.get(key);
            if ((value instanceof Boolean) || (value instanceof Integer) || (value instanceof Long) || (value instanceof Double) || (value instanceof String)) {
                pairs.add(YailList.makeList(new Object[]{key, value}));
            } else if (value instanceof JSONArray) {
                pairs.add(YailList.makeList(new Object[]{key, jsonArrayToYail(logTag, (JSONArray) value)}));
            } else if (value instanceof JSONObject) {
                pairs.add(YailList.makeList(new Object[]{key, jsonObjectToYail(logTag, (JSONObject) value)}));
            } else if (!JSONObject.NULL.equals(value)) {
                Log.wtf(logTag, "Unrecognized/invalid type in JSON object: " + value.getClass());
                throw new IllegalArgumentException(ERROR_UNKNOWN_TYPE);
            }
        }
        return YailList.makeList((List) pairs);
    }

    private static YailList jsonArrayToYail(String logTag, JSONArray array) throws JSONException {
        List<Object> items = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if ((value instanceof Boolean) || (value instanceof Integer) || (value instanceof Long) || (value instanceof Double) || (value instanceof String)) {
                items.add(value);
            } else if (value instanceof JSONArray) {
                items.add(jsonArrayToYail(logTag, (JSONArray) value));
            } else if (value instanceof JSONObject) {
                items.add(jsonObjectToYail(logTag, (JSONObject) value));
            } else if (!JSONObject.NULL.equals(value)) {
                Log.wtf(logTag, "Unrecognized/invalid type in JSON object: " + value.getClass());
                throw new IllegalArgumentException(ERROR_UNKNOWN_TYPE);
            }
        }
        return YailList.makeList((List) items);
    }

    private static String stripBOM(String content) {
        if (content.charAt(0) == 65279) {
            return content.substring(1);
        }
        return content;
    }

    private static final class FeatureWriter implements MapFactory.MapFeatureVisitor<Void> {
        private final PrintStream out;

        private FeatureWriter(PrintStream out2) {
            this.out = out2;
        }

        private void writeType(String type) {
            this.out.print("\"type\":\"");
            this.out.print(type);
            this.out.print("\"");
        }

        private void writeProperty(String property, Object value) {
            try {
                String result = JsonUtil.getJsonRepresentation(value);
                this.out.print(",\"");
                this.out.print(property);
                this.out.print("\":");
                this.out.print(result);
            } catch (JSONException e) {
                Log.w("GeoJSONUtil", "Unable to serialize the value of \"" + property + "\" as JSON", e);
            }
        }

        private void writeProperty(String property, String value) {
            if (value != null && !TextUtils.isEmpty(value)) {
                writeProperty(property, (Object) value);
            }
        }

        private void writeColorProperty(String property, int color) {
            this.out.print(",\"");
            this.out.print(property);
            this.out.print("\":\"&H");
            String unpadded = Integer.toHexString(color);
            for (int i = 8; i > unpadded.length(); i--) {
                this.out.print(Component.TYPEFACE_DEFAULT);
            }
            this.out.print(unpadded);
            this.out.print("\"");
        }

        private void writePointGeometry(GeoPoint point) {
            this.out.print("\"geometry\":{\"type\":\"Point\",\"coordinates\":[");
            this.out.print(point.getLongitude());
            this.out.print(",");
            this.out.print(point.getLatitude());
            if (hasAltitude(point)) {
                this.out.print(",");
                this.out.print(point.getAltitude());
            }
            this.out.print("]}");
        }

        private void writePropertiesHeader(String runtimeType) {
            this.out.print(",\"properties\":{\"$Type\":\"" + runtimeType + "\"");
        }

        private void writeProperties(MapFactory.MapFeature feature) {
            writeProperty(GeoJSONUtil.PROPERTY_DESCRIPTION, feature.Description());
            writeProperty(GeoJSONUtil.PROPERTY_DRAGGABLE, (Object) Boolean.valueOf(feature.Draggable()));
            writeProperty(GeoJSONUtil.PROPERTY_INFOBOX, (Object) Boolean.valueOf(feature.EnableInfobox()));
            writeProperty(GeoJSONUtil.PROPERTY_TITLE, feature.Title());
            writeProperty(GeoJSONUtil.PROPERTY_VISIBLE, (Object) Boolean.valueOf(feature.Visible()));
        }

        private void writeProperties(MapFactory.HasStroke feature) {
            writeColorProperty(GeoJSONUtil.PROPERTY_STROKE, feature.StrokeColor());
            writeProperty(GeoJSONUtil.PROPERTY_STROKE_OPACITY, (Object) Float.valueOf(feature.StrokeOpacity()));
            writeProperty(GeoJSONUtil.PROPERTY_STROKE_WIDTH, (Object) Integer.valueOf(feature.StrokeWidth()));
        }

        private void writeProperties(MapFactory.HasFill feature) {
            writeColorProperty(GeoJSONUtil.PROPERTY_FILL, feature.FillColor());
            writeProperty(GeoJSONUtil.PROPERTY_FILL_OPACITY, (Object) Float.valueOf(feature.FillOpacity()));
        }

        private void writePoints(List<GeoPoint> points) {
            boolean first = true;
            for (GeoPoint p : points) {
                if (!first) {
                    this.out.print(',');
                }
                this.out.print("[");
                this.out.print(p.getLongitude());
                this.out.print(",");
                this.out.print(p.getLatitude());
                if (hasAltitude(p)) {
                    this.out.print(",");
                    this.out.print(p.getAltitude());
                }
                this.out.print("]");
                first = false;
            }
        }

        private void writeLineGeometry(MapFactory.MapLineString lineString) {
            this.out.print("\"geometry\":{\"type\":\"LineString\",\"coordinates\":[");
            writePoints(lineString.getPoints());
            this.out.print("]}");
        }

        private void writeMultipolygonGeometryNoHoles(MapFactory.MapPolygon polygon) {
            this.out.print("\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[");
            Iterator<List<List<GeoPoint>>> holePointIterator = polygon.getHolePoints().iterator();
            boolean first = true;
            for (List<GeoPoint> writePoints : polygon.getPoints()) {
                if (!first) {
                    this.out.print(",");
                }
                this.out.print("[");
                writePoints(writePoints);
                if (holePointIterator.hasNext()) {
                    for (List<GeoPoint> holePoints : holePointIterator.next()) {
                        this.out.print(",");
                        writePoints(holePoints);
                    }
                }
                this.out.print("]");
                first = false;
            }
            this.out.print("]}");
        }

        private void writePolygonGeometryNoHoles(MapFactory.MapPolygon polygon) {
            this.out.print("\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[");
            writePoints(polygon.getPoints().get(0));
            if (!polygon.getHolePoints().isEmpty()) {
                for (List<GeoPoint> points : polygon.getHolePoints().get(0)) {
                    this.out.print(",");
                    writePoints(points);
                }
            }
            this.out.print("]}");
        }

        private void writePolygonGeometry(MapFactory.MapPolygon polygon) {
            if (polygon.getPoints().size() > 1) {
                writeMultipolygonGeometryNoHoles(polygon);
            } else {
                writePolygonGeometryNoHoles(polygon);
            }
        }

        public Void visit(MapFactory.MapMarker marker, Object... arguments) {
            this.out.print("{");
            writeType(GeoJSONUtil.GEOJSON_FEATURE);
            this.out.print(',');
            writePointGeometry(marker.getCentroid());
            writePropertiesHeader(marker.getClass().getName());
            writeProperties((MapFactory.MapFeature) marker);
            writeProperties((MapFactory.HasStroke) marker);
            writeProperties((MapFactory.HasFill) marker);
            writeProperty(GeoJSONUtil.PROPERTY_ANCHOR_HORIZONTAL, (Object) Integer.valueOf(marker.AnchorHorizontal()));
            writeProperty(GeoJSONUtil.PROPERTY_ANCHOR_VERTICAL, (Object) Integer.valueOf(marker.AnchorVertical()));
            writeProperty(GeoJSONUtil.PROPERTY_HEIGHT, (Object) Integer.valueOf(marker.Height()));
            writeProperty(GeoJSONUtil.PROPERTY_IMAGE, marker.ImageAsset());
            writeProperty(GeoJSONUtil.PROPERTY_WIDTH, (Object) Integer.valueOf(marker.Width()));
            this.out.print("}}");
            return null;
        }

        public Void visit(MapFactory.MapLineString lineString, Object... arguments) {
            this.out.print("{");
            writeType(GeoJSONUtil.GEOJSON_FEATURE);
            this.out.print(',');
            writeLineGeometry(lineString);
            writePropertiesHeader(lineString.getClass().getName());
            writeProperties((MapFactory.MapFeature) lineString);
            writeProperties((MapFactory.HasStroke) lineString);
            this.out.print("}}");
            return null;
        }

        public Void visit(MapFactory.MapPolygon polygon, Object... arguments) {
            this.out.print("{");
            writeType(GeoJSONUtil.GEOJSON_FEATURE);
            this.out.print(',');
            writePolygonGeometry(polygon);
            writePropertiesHeader(polygon.getClass().getName());
            writeProperties((MapFactory.MapFeature) polygon);
            writeProperties((MapFactory.HasStroke) polygon);
            writeProperties((MapFactory.HasFill) polygon);
            this.out.print("}}");
            return null;
        }

        public Void visit(MapFactory.MapCircle circle, Object... arguments) {
            this.out.print("{");
            writeType(GeoJSONUtil.GEOJSON_FEATURE);
            this.out.print(',');
            writePointGeometry(circle.getCentroid());
            writePropertiesHeader(circle.getClass().getName());
            writeProperties((MapFactory.MapFeature) circle);
            writeProperties((MapFactory.HasStroke) circle);
            writeProperties((MapFactory.HasFill) circle);
            this.out.print("}}");
            return null;
        }

        public Void visit(MapFactory.MapRectangle rectangle, Object... arguments) {
            this.out.print("{");
            writeType(GeoJSONUtil.GEOJSON_FEATURE);
            this.out.print(",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[");
            this.out.print("[" + rectangle.WestLongitude() + "," + rectangle.NorthLatitude() + "],");
            this.out.print("[" + rectangle.WestLongitude() + "," + rectangle.SouthLatitude() + "],");
            this.out.print("[" + rectangle.EastLongitude() + "," + rectangle.SouthLatitude() + "],");
            this.out.print("[" + rectangle.EastLongitude() + "," + rectangle.NorthLatitude() + "],");
            this.out.print("[" + rectangle.WestLongitude() + "," + rectangle.NorthLatitude() + "]]}");
            writePropertiesHeader(rectangle.getClass().getName());
            writeProperties((MapFactory.MapFeature) rectangle);
            writeProperties((MapFactory.HasStroke) rectangle);
            writeProperties((MapFactory.HasFill) rectangle);
            writeProperty("NorthLatitude", (Object) Double.valueOf(rectangle.NorthLatitude()));
            writeProperty("WestLongitude", (Object) Double.valueOf(rectangle.WestLongitude()));
            writeProperty("SouthLatitude", (Object) Double.valueOf(rectangle.SouthLatitude()));
            writeProperty("EastLongitude", (Object) Double.valueOf(rectangle.EastLongitude()));
            this.out.print("}}");
            return null;
        }

        private static boolean hasAltitude(GeoPoint point) {
            return Double.compare(0.0d, point.getAltitude()) != 0;
        }
    }

    public static void writeFeaturesAsGeoJSON(List<MapFactory.MapFeature> featuresToSave, String path) throws IOException {
        PrintStream out = null;
        try {
            PrintStream out2 = new PrintStream(new FileOutputStream(path));
            try {
                FeatureWriter writer = new FeatureWriter(out2);
                out2.print("{\"type\": \"FeatureCollection\", \"features\":[");
                Iterator<MapFactory.MapFeature> it = featuresToSave.iterator();
                if (it.hasNext()) {
                    it.next().accept(writer, new Object[0]);
                    while (it.hasNext()) {
                        out2.print(',');
                        it.next().accept(writer, new Object[0]);
                    }
                }
                out2.print("]}");
                IOUtils.closeQuietly("GeoJSONUtil", out2);
            } catch (Throwable th) {
                th = th;
                out = out2;
                IOUtils.closeQuietly("GeoJSONUtil", out);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            IOUtils.closeQuietly("GeoJSONUtil", out);
            throw th;
        }
    }

    public static YailList swapCoordinates(YailList coordinates) {
        Iterator i = coordinates.iterator();
        i.next();
        while (i.hasNext()) {
            YailList coordinate = (YailList) i.next();
            Object temp = coordinate.get(1);
            Pair p = (Pair) coordinate.getCdr();
            p.setCar(coordinate.get(2));
            ((Pair) p.getCdr()).setCar(temp);
        }
        return coordinates;
    }

    public static <E> List<List<E>> swapCoordinates2(List<List<E>> coordinates) {
        for (List<E> point : coordinates) {
            E temp = point.get(0);
            point.set(0, point.get(1));
            point.set(1, temp);
        }
        return coordinates;
    }

    public static LList swapNestedCoordinates(LList coordinates) {
        for (LList it = coordinates; !it.isEmpty(); it = (LList) ((Pair) it).getCdr()) {
            swapCoordinates((YailList) it.get(0));
        }
        return coordinates;
    }
}
