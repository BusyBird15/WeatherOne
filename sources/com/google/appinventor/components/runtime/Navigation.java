package com.google.appinventor.components.runtime;

import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.TransportMethod;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.GeoJSONUtil;
import com.google.appinventor.components.runtime.util.GeometryUtil;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.MapFactory;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.osmdroid.util.GeoPoint;

@DesignerComponent(category = ComponentCategory.MAPS, description = "Navigation", iconName = "images/navigation.png", nonVisible = true, version = 2)
@UsesLibraries({"osmdroid.jar"})
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class Navigation extends AndroidNonvisibleComponent implements Component {
    public static final String OPEN_ROUTE_SERVICE_URL = "https://api.openrouteservice.org/v2/directions/";
    private static final String TAG = "Navigation";
    private String apiKey = "";
    private GeoPoint endLocation = new GeoPoint(0.0d, 0.0d);
    private String language = "en";
    /* access modifiers changed from: private */
    public YailDictionary lastResponse = YailDictionary.makeDictionary();
    private TransportMethod method = TransportMethod.Foot;
    private String serviceUrl = OPEN_ROUTE_SERVICE_URL;
    private GeoPoint startLocation = new GeoPoint(0.0d, 0.0d);

    public Navigation(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Request directions from the routing service.")
    public void RequestDirections() {
        if (this.apiKey.equals("")) {
            this.form.dispatchErrorOccurredEvent(this, "Authorization", ErrorMessages.ERROR_INVALID_API_KEY, new Object[0]);
            return;
        }
        final GeoPoint startLocation2 = this.startLocation;
        final GeoPoint endLocation2 = this.endLocation;
        final TransportMethod method2 = this.method;
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                try {
                    Navigation.this.performRequest(startLocation2, endLocation2, method2);
                } catch (IOException e) {
                    Navigation.this.form.dispatchErrorOccurredEvent(Navigation.this, "RequestDirections", 0, new Object[0]);
                } catch (JSONException e2) {
                    Navigation.this.form.dispatchErrorOccurredEvent(Navigation.this, "RequestDirections", 0, new Object[0]);
                }
            }
        });
    }

    @SimpleProperty(userVisible = false)
    public void ServiceURL(String url) {
        this.serviceUrl = url;
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(description = "API Key for Open Route Service.")
    public void ApiKey(String key) {
        this.apiKey = key;
    }

    @DesignerProperty(defaultValue = "0.0", editorType = "latitude")
    @SimpleProperty
    public void StartLatitude(double latitude) {
        if (GeometryUtil.isValidLatitude(latitude)) {
            this.startLocation.setLatitude(latitude);
            return;
        }
        getDispatchDelegate().dispatchErrorOccurredEvent(this, "StartLatitude", ErrorMessages.ERROR_INVALID_LATITUDE, Double.valueOf(latitude));
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The latitude of the start location.")
    public double StartLatitude() {
        return this.startLocation.getLatitude();
    }

    @DesignerProperty(defaultValue = "0.0", editorType = "longitude")
    @SimpleProperty
    public void StartLongitude(double longitude) {
        if (GeometryUtil.isValidLongitude(longitude)) {
            this.startLocation.setLongitude(longitude);
            return;
        }
        getDispatchDelegate().dispatchErrorOccurredEvent(this, "StartLongitude", ErrorMessages.ERROR_INVALID_LONGITUDE, Double.valueOf(longitude));
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The longitude of the start location.")
    public double StartLongitude() {
        return this.startLocation.getLongitude();
    }

    @SimpleProperty(description = "Set the start location.")
    public void StartLocation(MapFactory.MapFeature feature) {
        GeoPoint point = feature.getCentroid();
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();
        if (!GeometryUtil.isValidLatitude(latitude)) {
            getDispatchDelegate().dispatchErrorOccurredEvent(this, "SetStartLocation", ErrorMessages.ERROR_INVALID_LATITUDE, Double.valueOf(latitude));
        } else if (!GeometryUtil.isValidLongitude(longitude)) {
            getDispatchDelegate().dispatchErrorOccurredEvent(this, "SetStartLocation", ErrorMessages.ERROR_INVALID_LONGITUDE, Double.valueOf(longitude));
        } else {
            this.startLocation.setCoords(latitude, longitude);
        }
    }

    @DesignerProperty(defaultValue = "0.0", editorType = "latitude")
    @SimpleProperty
    public void EndLatitude(double latitude) {
        if (GeometryUtil.isValidLatitude(latitude)) {
            this.endLocation.setLatitude(latitude);
            return;
        }
        getDispatchDelegate().dispatchErrorOccurredEvent(this, "EndLatitude", ErrorMessages.ERROR_INVALID_LATITUDE, Double.valueOf(latitude));
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The latitude of the end location.")
    public double EndLatitude() {
        return this.endLocation.getLatitude();
    }

    @DesignerProperty(defaultValue = "0.0", editorType = "longitude")
    @SimpleProperty
    public void EndLongitude(double longitude) {
        if (GeometryUtil.isValidLongitude(longitude)) {
            this.endLocation.setLongitude(longitude);
            return;
        }
        getDispatchDelegate().dispatchErrorOccurredEvent(this, "EndLongitude", ErrorMessages.ERROR_INVALID_LONGITUDE, Double.valueOf(longitude));
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The longitude of the end location.")
    public double EndLongitude() {
        return this.endLocation.getLongitude();
    }

    @Options(TransportMethod.class)
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String TransportationMethod() {
        return TransportationMethodAbstract().toUnderlyingValue();
    }

    public TransportMethod TransportationMethodAbstract() {
        return this.method;
    }

    public void TransportationMethodAbstract(TransportMethod method2) {
        this.method = method2;
    }

    @DesignerProperty(defaultValue = "foot-walking", editorType = "navigation_method")
    @SimpleProperty(description = "The transportation method used for determining the route.")
    public void TransportationMethod(@Options(TransportMethod.class) String method2) {
        TransportMethod t = TransportMethod.fromUnderlyingValue(method2);
        if (t != null) {
            TransportationMethodAbstract(t);
        }
    }

    @SimpleProperty(description = "Set the end location.")
    public void EndLocation(MapFactory.MapFeature feature) {
        GeoPoint point = feature.getCentroid();
        double latitude = point.getLatitude();
        double longitude = point.getLongitude();
        if (!GeometryUtil.isValidLatitude(latitude)) {
            getDispatchDelegate().dispatchErrorOccurredEvent(this, "SetEndLocation", ErrorMessages.ERROR_INVALID_LATITUDE, Double.valueOf(latitude));
        } else if (!GeometryUtil.isValidLongitude(longitude)) {
            getDispatchDelegate().dispatchErrorOccurredEvent(this, "SetEndLocation", ErrorMessages.ERROR_INVALID_LONGITUDE, Double.valueOf(longitude));
        } else {
            this.endLocation.setCoords(latitude, longitude);
        }
    }

    @DesignerProperty(defaultValue = "en")
    @SimpleProperty(description = "The language to use for textual directions.")
    public void Language(String language2) {
        this.language = language2;
    }

    @SimpleProperty
    public String Language() {
        return this.language;
    }

    @SimpleProperty(description = "Content of the last response as a dictionary.")
    public YailDictionary ResponseContent() {
        return this.lastResponse;
    }

    @SimpleEvent(description = "Event triggered when the Openrouteservice returns the directions.")
    public void GotDirections(YailList directions, YailList points, double distance, double duration) {
        Log.d(TAG, "GotDirections");
        EventDispatcher.dispatchEvent(this, "GotDirections", directions, points, Double.valueOf(distance), Double.valueOf(duration));
    }

    /* access modifiers changed from: private */
    public void performRequest(GeoPoint start, GeoPoint end, TransportMethod method2) throws IOException, JSONException {
        BufferedOutputStream bufferedOutputStream;
        HttpURLConnection connection = (HttpURLConnection) new URL(this.serviceUrl + method2.toUnderlyingValue() + "/geojson/").openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", this.apiKey);
        try {
            byte[] postData = ("{\"coordinates\": " + JsonUtil.getJsonRepresentation(getCoordinates(start, end)) + ", \"language\": \"" + this.language + "\"}").getBytes("UTF-8");
            connection.setFixedLengthStreamingMode(postData.length);
            bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            bufferedOutputStream.write(postData, 0, postData.length);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            if (connection.getResponseCode() != 200) {
                this.form.dispatchErrorOccurredEvent(this, "RequestDirections", ErrorMessages.ERROR_ROUTING_SERVICE_ERROR, Integer.valueOf(connection.getResponseCode()), connection.getResponseMessage());
                connection.disconnect();
                return;
            }
            String geoJson = getResponseContent(connection);
            Log.d(TAG, geoJson);
            final YailDictionary response = (YailDictionary) JsonUtil.getObjectFromJson(geoJson, true);
            YailList features = (YailList) response.get("features");
            if (features.size() > 0) {
                YailDictionary feature = (YailDictionary) features.getObject(0);
                YailDictionary yailDictionary = feature;
                YailDictionary summary = (YailDictionary) yailDictionary.getObjectAtKeyPath(Arrays.asList(new String[]{"properties", "summary"}));
                final double distance = ((Double) summary.get("distance")).doubleValue();
                final double duration = ((Double) summary.get("duration")).doubleValue();
                final YailList directions = YailList.makeList((List) getDirections(feature));
                final YailList coordinates = getLineStringCoords(feature);
                this.form.runOnUiThread(new Runnable() {
                    public void run() {
                        YailDictionary unused = Navigation.this.lastResponse = response;
                        Navigation.this.GotDirections(directions, coordinates, distance, duration);
                    }
                });
            } else {
                this.form.dispatchErrorOccurredEvent(this, "RequestDirections", ErrorMessages.ERROR_NO_ROUTE_FOUND, new Object[0]);
            }
            connection.disconnect();
        } catch (Exception e) {
            try {
                this.form.dispatchErrorOccurredEvent(this, "RequestDirections", ErrorMessages.ERROR_UNABLE_TO_REQUEST_DIRECTIONS, e.getMessage());
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
        } catch (Throwable th) {
            bufferedOutputStream.close();
            throw th;
        }
    }

    private static String getResponseContent(HttpURLConnection connection) throws IOException {
        String encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        Log.d(TAG, Integer.toString(connection.getResponseCode()));
        InputStreamReader reader = new InputStreamReader(connection.getInputStream(), encoding);
        try {
            int contentLength = connection.getContentLength();
            StringBuilder sb = contentLength != -1 ? new StringBuilder(contentLength) : new StringBuilder();
            char[] buf = new char[1024];
            while (true) {
                int read = reader.read(buf);
                if (read == -1) {
                    return sb.toString();
                }
                sb.append(buf, 0, read);
            }
        } finally {
            reader.close();
        }
    }

    private Double[][] getCoordinates(GeoPoint startLocation2, GeoPoint endLocation2) {
        Double[][] coords = (Double[][]) Array.newInstance(Double.class, new int[]{2, 2});
        coords[0][0] = Double.valueOf(startLocation2.getLongitude());
        coords[0][1] = Double.valueOf(startLocation2.getLatitude());
        coords[1][0] = Double.valueOf(endLocation2.getLongitude());
        coords[1][1] = Double.valueOf(endLocation2.getLatitude());
        return coords;
    }

    private YailList getLineStringCoords(YailDictionary feature) {
        return GeoJSONUtil.swapCoordinates((YailList) feature.getObjectAtKeyPath(Arrays.asList(new String[]{"geometry", "coordinates"})));
    }

    private List<?> getDirections(YailDictionary feature) {
        return YailDictionary.walkKeyPath(feature, Arrays.asList(new Object[]{"properties", "segments", YailDictionary.ALL, "steps", YailDictionary.ALL, "instruction"}));
    }
}
