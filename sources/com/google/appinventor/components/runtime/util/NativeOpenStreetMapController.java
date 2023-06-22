package com.google.appinventor.components.runtime.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import androidx.core.view.ViewCompat;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.appinventor.components.common.MapType;
import com.google.appinventor.components.common.ScaleUnits;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.LocationSensor;
import com.google.appinventor.components.runtime.util.MapFactory;
import com.google.appinventor.components.runtime.view.ZoomControlView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayWithIW;
import org.osmdroid.views.overlay.OverlayWithIWVisitor;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.IOrientationProvider;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.OverlayInfoWindow;
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer;
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

class NativeOpenStreetMapController implements MapFactory.MapController, MapListener {
    private static final float[] ANCHOR_HORIZONTAL = {Float.NaN, 0.0f, 1.0f, 0.5f};
    private static final float[] ANCHOR_VERTICAL = {Float.NaN, 0.0f, 0.5f, 1.0f};
    private static final long SPECIFIED_FILL = 1;
    private static final long SPECIFIED_FILL_OPACITY = 4;
    private static final long SPECIFIED_STROKE = 8;
    private static final long SPECIFIED_STROKE_OPACITY = 16;
    private static final long SPECIFIED_STROKE_WIDTH = 32;
    /* access modifiers changed from: private */
    public static final String TAG = NativeOpenStreetMapController.class.getSimpleName();
    private boolean caches;
    /* access modifiers changed from: private */
    public CompassOverlay compass = null;
    private RelativeLayout containerView;
    private OverlayInfoWindow defaultInfoWindow = null;
    private SVG defaultMarkerSVG = null;
    /* access modifiers changed from: private */
    public Set<MapFactory.MapEventListener> eventListeners = new HashSet();
    private Map<MapFactory.MapFeature, OverlayWithIW> featureOverlays = new HashMap();
    /* access modifiers changed from: private */
    public final Form form;
    private Set<MapFactory.MapFeatureCollection> hiddenFeatureCollections = new HashSet();
    private Set<MapFactory.MapFeature> hiddenFeatures = new HashSet();
    private float lastAzimuth = Float.NaN;
    private final AppInventorLocationSensorAdapter locationProvider;
    /* access modifiers changed from: private */
    public boolean ready = false;
    private RotationGestureOverlay rotation = null;
    private ScaleBarOverlay scaleBar;
    private MapType tileType;
    private TouchOverlay touch = null;
    private final MyLocationNewOverlay userLocation;
    /* access modifiers changed from: private */
    public MapView view;
    private boolean zoomControlEnabled;
    private ZoomControlView zoomControls = null;
    private boolean zoomEnabled;

    private static class AppInventorLocationSensorAdapter implements IMyLocationProvider, LocationSensor.LocationSensorListener {
        private IMyLocationConsumer consumer;
        private boolean enabled;
        private Location lastLocation;
        private LocationSensor source;

        private AppInventorLocationSensorAdapter() {
            this.enabled = false;
        }

        public void setSource(LocationSensor source2) {
            if (this.source != source2) {
                if (this.source != null) {
                    this.source.Enabled(false);
                }
                this.source = source2;
                if (this.source != null) {
                    this.source.Enabled(this.enabled);
                }
            }
        }

        public void onTimeIntervalChanged(int time) {
        }

        public void onDistanceIntervalChanged(int distance) {
        }

        public void onLocationChanged(Location location) {
            this.lastLocation = location;
            if (this.consumer != null) {
                this.consumer.onLocationChanged(location, this);
            }
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        public void onProviderEnabled(String s) {
        }

        public void onProviderDisabled(String s) {
        }

        public boolean startLocationProvider(IMyLocationConsumer consumer2) {
            this.consumer = consumer2;
            if (this.source != null) {
                this.source.Enabled(true);
                this.enabled = true;
            }
            return this.enabled;
        }

        public void stopLocationProvider() {
            if (this.source != null) {
                this.source.Enabled(false);
            }
            this.enabled = false;
        }

        public Location getLastKnownLocation() {
            return this.lastLocation;
        }

        public void destroy() {
            this.consumer = null;
        }
    }

    private class TouchOverlay extends Overlay {
        /* access modifiers changed from: private */
        public boolean scrollEnabled;

        private TouchOverlay() {
            this.scrollEnabled = true;
        }

        public void draw(Canvas arg0, MapView arg1, boolean arg2) {
        }

        public boolean onFling(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY, MapView mapView) {
            return !this.scrollEnabled;
        }

        public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY, MapView mapView) {
            return !this.scrollEnabled;
        }

        public boolean onLongPress(MotionEvent pEvent, MapView pMapView) {
            IGeoPoint p = pMapView.getProjection().fromPixels((int) pEvent.getX(), (int) pEvent.getY());
            double lat = p.getLatitude();
            double lng = p.getLongitude();
            for (MapFactory.MapEventListener l : NativeOpenStreetMapController.this.eventListeners) {
                l.onLongPress(lat, lng);
            }
            return false;
        }
    }

    private class MapReadyHandler extends Handler {
        private MapReadyHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!NativeOpenStreetMapController.this.ready && NativeOpenStreetMapController.this.form.canDispatchEvent((Component) null, "MapReady")) {
                        boolean unused = NativeOpenStreetMapController.this.ready = true;
                        NativeOpenStreetMapController.this.form.runOnUiThread(new Runnable() {
                            public void run() {
                                for (MapFactory.MapEventListener l : NativeOpenStreetMapController.this.eventListeners) {
                                    l.onReady(NativeOpenStreetMapController.this);
                                }
                            }
                        });
                    }
                    NativeOpenStreetMapController.this.view.invalidate();
                    return;
                default:
                    return;
            }
        }
    }

    private class CustomMapView extends MapView {
        public CustomMapView(Context context) {
            super(context, (MapTileProviderBase) null, new MapReadyHandler());
        }

        /* access modifiers changed from: protected */
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            scrollTo(getScrollX() + ((oldw - w) / 2), getScrollY() + ((oldh - h) / 2));
            NativeOpenStreetMapController.super.onSizeChanged(w, h, oldw, oldh);
        }

        public void onDetach() {
        }
    }

    NativeOpenStreetMapController(Form form2) {
        OpenStreetMapTileProviderConstants.setUserAgentValue(form2.getApplication().getPackageName());
        File osmdroid = new File(form2.getCacheDir(), "osmdroid");
        if (osmdroid.exists() || osmdroid.mkdirs()) {
            Configuration.getInstance().setOsmdroidBasePath(osmdroid);
            File osmdroidTiles = new File(osmdroid, "tiles");
            if (osmdroidTiles.exists() || osmdroidTiles.mkdirs()) {
                Configuration.getInstance().setOsmdroidTileCache(osmdroidTiles);
                this.caches = true;
            }
        }
        this.form = form2;
        this.touch = new TouchOverlay();
        this.view = new CustomMapView(form2.getApplicationContext());
        this.locationProvider = new AppInventorLocationSensorAdapter();
        this.defaultInfoWindow = new OverlayInfoWindow(this.view);
        this.view.setTilesScaledToDpi(true);
        this.view.setMapListener(this);
        this.view.getOverlayManager().add(new CopyrightOverlay(form2));
        this.view.getOverlayManager().add(this.touch);
        this.view.addOnTapListener(new MapView.OnTapListener() {
            public void onSingleTap(MapView view, double latitude, double longitude) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onSingleTap(latitude, longitude);
                }
            }

            public void onDoubleTap(MapView view, double latitude, double longitude) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onDoubleTap(latitude, longitude);
                }
            }
        });
        this.zoomControls = new ZoomControlView(this.view);
        this.userLocation = new MyLocationNewOverlay(this.locationProvider, this.view);
        this.scaleBar = new ScaleBarOverlay(this.view);
        this.scaleBar.setAlignBottom(true);
        this.scaleBar.setAlignRight(true);
        this.scaleBar.disableScaleBar();
        this.view.getOverlayManager().add(this.scaleBar);
        this.containerView = new RelativeLayout(form2);
        this.containerView.setClipChildren(true);
        this.containerView.addView(this.view, new RelativeLayout.LayoutParams(-1, -1));
        this.containerView.addView(this.zoomControls);
        this.zoomControls.setVisibility(8);
    }

    public View getView() {
        return this.containerView;
    }

    public double getLatitude() {
        return this.view.getMapCenter().getLatitude();
    }

    public double getLongitude() {
        return this.view.getMapCenter().getLongitude();
    }

    public void setCenter(double latitude, double longitude) {
        this.view.getController().setCenter(new GeoPoint(latitude, longitude));
    }

    public void setZoom(int zoom) {
        this.view.getController().setZoom((double) zoom);
        this.zoomControls.updateButtons();
    }

    public int getZoom() {
        return (int) this.view.getZoomLevel(true);
    }

    public void setZoomEnabled(boolean enable) {
        this.zoomEnabled = enable;
        this.view.setMultiTouchControls(enable);
    }

    public boolean isZoomEnabled() {
        return this.zoomEnabled;
    }

    public void setMapType(MapFactory.MapType type) {
        MapType mapType = MapType.fromUnderlyingValue(Integer.valueOf(type.ordinal()));
        if (mapType != null) {
            setMapTypeAbstract(mapType);
        }
    }

    public MapFactory.MapType getMapType() {
        return MapFactory.MapType.values()[this.tileType.toUnderlyingValue().intValue()];
    }

    public void setMapTypeAbstract(MapType type) {
        this.tileType = type;
        switch (type) {
            case Road:
                this.view.setTileSource(TileSourceFactory.MAPNIK);
                return;
            case Aerial:
                this.view.setTileSource(TileSourceFactory.USGS_SAT);
                return;
            case Terrain:
                this.view.setTileSource(TileSourceFactory.USGS_TOPO);
                return;
            default:
                return;
        }
    }

    public MapType getMapTypeAbstract() {
        return this.tileType;
    }

    public void setCompassEnabled(boolean enabled) {
        if (enabled && this.compass == null) {
            this.compass = new CompassOverlay(this.view.getContext(), this.view);
            this.view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    NativeOpenStreetMapController.this.compass.setCompassCenter((((float) NativeOpenStreetMapController.this.view.getMeasuredWidth()) / NativeOpenStreetMapController.this.view.getContext().getResources().getDisplayMetrics().density) - 35.0f, 35.0f);
                    return true;
                }
            });
            this.view.getOverlayManager().add(this.compass);
        }
        if (this.compass == null) {
            return;
        }
        if (enabled) {
            if (this.compass.getOrientationProvider() != null) {
                this.compass.enableCompass();
            } else {
                this.compass.enableCompass(new InternalCompassOrientationProvider(this.view.getContext()));
            }
            this.compass.onOrientationChanged(this.lastAzimuth, (IOrientationProvider) null);
            return;
        }
        this.lastAzimuth = this.compass.getOrientation();
        this.compass.disableCompass();
    }

    public boolean isCompassEnabled() {
        return this.compass != null && this.compass.isCompassEnabled();
    }

    public void setZoomControlEnabled(boolean enabled) {
        if (this.zoomControlEnabled != enabled) {
            this.zoomControls.setVisibility(enabled ? 0 : 8);
            this.zoomControlEnabled = enabled;
            this.containerView.invalidate();
        }
    }

    public boolean isZoomControlEnabled() {
        return this.zoomControlEnabled;
    }

    public void setShowUserEnabled(boolean enable) {
        this.userLocation.setEnabled(enable);
        if (enable) {
            this.userLocation.enableMyLocation();
            this.view.getOverlayManager().add(this.userLocation);
            return;
        }
        this.userLocation.disableMyLocation();
        this.view.getOverlayManager().remove(this.userLocation);
    }

    public boolean isShowUserEnabled() {
        return this.userLocation != null && this.userLocation.isEnabled();
    }

    public void setRotationEnabled(boolean enabled) {
        if (enabled && this.rotation == null) {
            this.rotation = new RotationGestureOverlay(this.view);
        }
        if (this.rotation != null) {
            this.rotation.setEnabled(enabled);
            if (enabled) {
                this.view.getOverlayManager().add(this.rotation);
            } else {
                this.view.getOverlayManager().remove(this.rotation);
            }
        }
    }

    public boolean isRotationEnabled() {
        return this.rotation != null && this.rotation.isEnabled();
    }

    public void setPanEnabled(boolean enable) {
        boolean unused = this.touch.scrollEnabled = enable;
    }

    public boolean isPanEnabled() {
        return this.touch.scrollEnabled;
    }

    public void panTo(double latitude, double longitude, int zoom, double seconds) {
        Animation animation;
        this.view.getController().animateTo(new GeoPoint(latitude, longitude));
        if (this.view.getController().zoomTo((double) zoom) && (animation = this.view.getAnimation()) != null) {
            animation.setDuration((long) (1000.0d * seconds));
        }
    }

    public void addEventListener(MapFactory.MapEventListener listener) {
        this.eventListeners.add(listener);
        if ((this.ready || ViewCompat.isAttachedToWindow(this.view)) && this.form.canDispatchEvent((Component) null, "MapReady")) {
            this.ready = true;
            listener.onReady(this);
        }
    }

    public void addFeature(final MapFactory.MapMarker aiMarker) {
        createNativeMarker(aiMarker, new AsyncCallbackPair<Marker>() {
            public void onFailure(String message) {
                Log.e(NativeOpenStreetMapController.TAG, "Unable to create marker: " + message);
            }

            public void onSuccess(Marker overlay) {
                overlay.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                            listener.onFeatureClick(aiMarker);
                        }
                        if (!aiMarker.EnableInfobox()) {
                            return false;
                        }
                        marker.showInfoWindow();
                        return false;
                    }

                    public boolean onMarkerLongPress(Marker marker, MapView mapView) {
                        for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                            listener.onFeatureLongPress(aiMarker);
                        }
                        return false;
                    }
                });
                overlay.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                    public void onMarkerDrag(Marker marker) {
                        for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                            listener.onFeatureDrag(aiMarker);
                        }
                    }

                    public void onMarkerDragEnd(Marker marker) {
                        IGeoPoint point = marker.getPosition();
                        aiMarker.updateLocation(point.getLatitude(), point.getLongitude());
                        for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                            listener.onFeatureStopDrag(aiMarker);
                        }
                    }

                    public void onMarkerDragStart(Marker marker) {
                        for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                            listener.onFeatureStartDrag(aiMarker);
                        }
                    }
                });
                if (aiMarker.Visible()) {
                    NativeOpenStreetMapController.this.showOverlay(overlay);
                } else {
                    NativeOpenStreetMapController.this.hideOverlay(overlay);
                }
            }
        });
    }

    public void addFeature(final MapFactory.MapLineString aiPolyline) {
        Polyline polyline = createNativePolyline(aiPolyline);
        this.featureOverlays.put(aiPolyline, polyline);
        polyline.setOnClickListener(new Polyline.OnClickListener() {
            public boolean onClick(Polyline arg0, MapView arg1, GeoPoint arg2) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureClick(aiPolyline);
                }
                if (!aiPolyline.EnableInfobox()) {
                    return true;
                }
                arg0.showInfoWindow(arg2);
                return true;
            }

            public boolean onLongClick(Polyline arg0, MapView arg1, GeoPoint arg2) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureLongPress(aiPolyline);
                }
                return true;
            }
        });
        polyline.setOnDragListener(new Polyline.OnDragListener() {
            public void onDragStart(Polyline polyline) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureStartDrag(aiPolyline);
                }
            }

            public void onDrag(Polyline polyline) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureDrag(aiPolyline);
                }
            }

            public void onDragEnd(Polyline polyline) {
                aiPolyline.updatePoints(polyline.getPoints());
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureStopDrag(aiPolyline);
                }
            }
        });
        if (aiPolyline.Visible()) {
            showOverlay(polyline);
        } else {
            hideOverlay(polyline);
        }
    }

    private void configurePolygon(final MapFactory.MapFeature component, Polygon polygon) {
        this.featureOverlays.put(component, polygon);
        polygon.setOnClickListener(new Polygon.OnClickListener() {
            public boolean onLongClick(Polygon arg0, MapView arg1, GeoPoint arg2) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureLongPress(component);
                }
                return true;
            }

            public boolean onClick(Polygon arg0, MapView arg1, GeoPoint arg2) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureClick(component);
                }
                if (!component.EnableInfobox()) {
                    return true;
                }
                arg0.showInfoWindow(arg2);
                return true;
            }
        });
        polygon.setOnDragListener(new Polygon.OnDragListener() {
            public void onDragStart(Polygon polygon) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureStartDrag(component);
                }
            }

            public void onDrag(Polygon polygon) {
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureDrag(component);
                }
            }

            public void onDragEnd(Polygon polygon) {
                if (component instanceof MapFactory.MapCircle) {
                    double latitude = 0.0d;
                    double longitude = 0.0d;
                    int count = polygon.getPoints().size();
                    for (GeoPoint p : polygon.getPoints()) {
                        latitude += p.getLatitude();
                        longitude += p.getLongitude();
                    }
                    if (count > 0) {
                        ((MapFactory.MapCircle) component).updateCenter(latitude / ((double) count), longitude / ((double) count));
                    } else {
                        ((MapFactory.MapCircle) component).updateCenter(0.0d, 0.0d);
                    }
                } else if (component instanceof MapFactory.MapRectangle) {
                    double north = -90.0d;
                    double east = -180.0d;
                    double west = 180.0d;
                    double south = 90.0d;
                    for (GeoPoint p2 : polygon.getPoints()) {
                        double lat = p2.getLatitude();
                        double lng = p2.getLongitude();
                        north = Math.max(north, lat);
                        south = Math.min(south, lat);
                        east = Math.max(east, lng);
                        west = Math.min(west, lng);
                    }
                    ((MapFactory.MapRectangle) component).updateBounds(north, west, south, east);
                } else {
                    ((MapFactory.MapPolygon) component).updatePoints(((MultiPolygon) polygon).getMultiPoints());
                    ((MapFactory.MapPolygon) component).updateHolePoints(((MultiPolygon) polygon).getMultiHoles());
                }
                for (MapFactory.MapEventListener listener : NativeOpenStreetMapController.this.eventListeners) {
                    listener.onFeatureStopDrag(component);
                }
            }
        });
        if (component.Visible()) {
            showOverlay(polygon);
        } else {
            hideOverlay(polygon);
        }
    }

    public void addFeature(MapFactory.MapPolygon aiPolygon) {
        configurePolygon(aiPolygon, createNativePolygon(aiPolygon));
    }

    public void addFeature(MapFactory.MapCircle aiCircle) {
        configurePolygon(aiCircle, createNativeCircle(aiCircle));
    }

    public void addFeature(MapFactory.MapRectangle aiRectangle) {
        configurePolygon(aiRectangle, createNativeRectangle(aiRectangle));
    }

    public void removeFeature(MapFactory.MapFeature aiFeature) {
        this.view.getOverlayManager().remove(this.featureOverlays.get(aiFeature));
        this.featureOverlays.remove(aiFeature);
    }

    public void updateFeaturePosition(MapFactory.MapMarker aiMarker) {
        Marker marker = this.featureOverlays.get(aiMarker);
        if (marker != null) {
            marker.setAnchor(ANCHOR_HORIZONTAL[aiMarker.AnchorHorizontal()], ANCHOR_VERTICAL[aiMarker.AnchorVertical()]);
            marker.setPosition(new GeoPoint(aiMarker.Latitude(), aiMarker.Longitude()));
            this.view.invalidate();
        }
    }

    public void updateFeaturePosition(MapFactory.MapLineString aiPolyline) {
        Polyline overlay = this.featureOverlays.get(aiPolyline);
        if (overlay != null) {
            overlay.setPoints(aiPolyline.getPoints());
            this.view.invalidate();
        }
    }

    public void updateFeaturePosition(MapFactory.MapPolygon aiPolygon) {
        MultiPolygon polygon = this.featureOverlays.get(aiPolygon);
        if (polygon != null) {
            polygon.setMultiPoints(aiPolygon.getPoints());
            this.view.invalidate();
        }
    }

    public void updateFeatureHoles(MapFactory.MapPolygon aiPolygon) {
        MultiPolygon polygon = this.featureOverlays.get(aiPolygon);
        if (polygon != null) {
            polygon.setMultiHoles(aiPolygon.getHolePoints());
            this.view.invalidate();
        }
    }

    public void updateFeaturePosition(MapFactory.MapCircle aiCircle) {
        GeoPoint center = new GeoPoint(aiCircle.Latitude(), aiCircle.Longitude());
        Polygon polygon = this.featureOverlays.get(aiCircle);
        if (polygon != null) {
            polygon.setPoints(Polygon.pointsAsCircle(center, aiCircle.Radius()));
            this.view.invalidate();
        }
    }

    public void updateFeaturePosition(MapFactory.MapRectangle aiRectangle) {
        Polygon polygon = this.featureOverlays.get(aiRectangle);
        if (polygon != null) {
            polygon.setPoints(Polygon.pointsAsRect(new BoundingBox(aiRectangle.NorthLatitude(), aiRectangle.EastLongitude(), aiRectangle.SouthLatitude(), aiRectangle.WestLongitude())));
            this.view.invalidate();
        }
    }

    public void updateFeatureFill(final MapFactory.HasFill aiFeature) {
        OverlayWithIW overlay = this.featureOverlays.get(aiFeature);
        if (overlay != null) {
            overlay.accept(new OverlayWithIWVisitor() {
                public void visit(final Marker marker) {
                    NativeOpenStreetMapController.this.getMarkerDrawable((MapFactory.MapMarker) aiFeature, new AsyncCallbackPair<Drawable>() {
                        public void onFailure(String message) {
                            Log.e(NativeOpenStreetMapController.TAG, "Unable to update fill color for marker: " + message);
                        }

                        public void onSuccess(Drawable result) {
                            marker.setIcon(result);
                            NativeOpenStreetMapController.this.view.invalidate();
                        }
                    });
                }

                public void visit(Polyline polyline) {
                }

                public void visit(Polygon polygon) {
                    polygon.setFillColor(aiFeature.FillColor());
                    NativeOpenStreetMapController.this.view.invalidate();
                }
            });
        }
    }

    public void updateFeatureStroke(final MapFactory.HasStroke aiFeature) {
        OverlayWithIW overlay = this.featureOverlays.get(aiFeature);
        if (overlay != null) {
            overlay.accept(new OverlayWithIWVisitor() {
                public void visit(final Marker marker) {
                    NativeOpenStreetMapController.this.getMarkerDrawable((MapFactory.MapMarker) aiFeature, new AsyncCallbackPair<Drawable>() {
                        public void onFailure(String message) {
                            Log.e(NativeOpenStreetMapController.TAG, "Unable to update stroke color for marker: " + message);
                        }

                        public void onSuccess(Drawable result) {
                            marker.setIcon(result);
                            NativeOpenStreetMapController.this.view.invalidate();
                        }
                    });
                }

                public void visit(Polyline polyline) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    NativeOpenStreetMapController.this.form.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    polyline.setColor(aiFeature.StrokeColor());
                    polyline.setWidth(((float) aiFeature.StrokeWidth()) * metrics.density);
                    NativeOpenStreetMapController.this.view.invalidate();
                }

                public void visit(Polygon polygon) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    NativeOpenStreetMapController.this.form.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    polygon.setStrokeColor(aiFeature.StrokeColor());
                    polygon.setStrokeWidth(((float) aiFeature.StrokeWidth()) * metrics.density);
                    NativeOpenStreetMapController.this.view.invalidate();
                }
            });
        }
    }

    public void updateFeatureText(MapFactory.MapFeature aiFeature) {
        OverlayWithIW overlay = this.featureOverlays.get(aiFeature);
        if (overlay != null) {
            overlay.setTitle(aiFeature.Title());
            overlay.setSnippet(aiFeature.Description());
        }
    }

    public void updateFeatureDraggable(MapFactory.MapFeature aiFeature) {
        OverlayWithIW overlay = this.featureOverlays.get(aiFeature);
        if (overlay != null) {
            overlay.setDraggable(aiFeature.Draggable());
        }
    }

    public void updateFeatureImage(MapFactory.MapMarker aiMarker) {
        final Marker marker = this.featureOverlays.get(aiMarker);
        if (marker != null) {
            getMarkerDrawable(aiMarker, new AsyncCallbackPair<Drawable>() {
                public void onFailure(String message) {
                    Log.e(NativeOpenStreetMapController.TAG, "Unable to update feature image: " + message);
                }

                public void onSuccess(Drawable result) {
                    marker.setIcon(result);
                    NativeOpenStreetMapController.this.view.invalidate();
                }
            });
        }
    }

    public void updateFeatureSize(MapFactory.MapMarker aiMarker) {
        final Marker marker = this.featureOverlays.get(aiMarker);
        if (marker != null) {
            getMarkerDrawable(aiMarker, new AsyncCallbackPair<Drawable>() {
                public void onFailure(String message) {
                    Log.wtf(NativeOpenStreetMapController.TAG, "Cannot find default marker");
                }

                public void onSuccess(Drawable result) {
                    marker.setIcon(result);
                    NativeOpenStreetMapController.this.view.invalidate();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void getMarkerDrawable(MapFactory.MapMarker aiMarker, AsyncCallbackPair<Drawable> callback) {
        String assetPath = aiMarker.ImageAsset();
        if (assetPath == null || assetPath.length() == 0 || assetPath.endsWith(".svg")) {
            getMarkerDrawableVector(aiMarker, callback);
        } else {
            getMarkerDrawableRaster(aiMarker, callback);
        }
    }

    private void getMarkerDrawableVector(MapFactory.MapMarker aiMarker, AsyncCallbackPair<Drawable> callback) {
        SVG markerSvg = null;
        if (this.defaultMarkerSVG == null) {
            try {
                this.defaultMarkerSVG = SVG.getFromAsset(this.view.getContext().getAssets(), "marker.svg");
            } catch (SVGParseException e) {
                Log.e(TAG, "Invalid SVG in Marker asset", e);
            } catch (IOException e2) {
                Log.e(TAG, "Unable to read Marker asset", e2);
            }
            if (this.defaultMarkerSVG == null || this.defaultMarkerSVG.getRootElement() == null) {
                throw new IllegalStateException("Unable to load SVG from assets");
            }
        }
        String markerAsset = aiMarker.ImageAsset();
        if (!(markerAsset == null || markerAsset.length() == 0)) {
            try {
                markerSvg = SVG.getFromAsset(this.view.getContext().getAssets(), markerAsset);
            } catch (SVGParseException e3) {
                Log.e(TAG, "Invalid SVG in Marker asset", e3);
            } catch (IOException e4) {
                Log.e(TAG, "Unable to read Marker asset", e4);
            }
            if (markerSvg == null) {
                InputStream is = null;
                try {
                    is = MediaUtil.openMedia(this.form, markerAsset);
                    markerSvg = SVG.getFromInputStream(is);
                } catch (SVGParseException e5) {
                    Log.e(TAG, "Invalid SVG in Marker asset", e5);
                } catch (IOException e6) {
                    Log.e(TAG, "Unable to read Marker asset", e6);
                } finally {
                    IOUtils.closeQuietly(TAG, is);
                }
            }
        }
        if (markerSvg == null) {
            markerSvg = this.defaultMarkerSVG;
        }
        try {
            callback.onSuccess(rasterizeSVG(aiMarker, markerSvg));
        } catch (Exception e7) {
            callback.onFailure(e7.getMessage());
        }
    }

    private void getMarkerDrawableRaster(final MapFactory.MapMarker aiMarker, final AsyncCallbackPair<Drawable> callback) {
        MediaUtil.getBitmapDrawableAsync(this.form, aiMarker.ImageAsset(), aiMarker.Width(), aiMarker.Height(), new AsyncCallbackPair<BitmapDrawable>() {
            public void onFailure(String message) {
                callback.onSuccess(NativeOpenStreetMapController.this.getDefaultMarkerDrawable(aiMarker));
            }

            public void onSuccess(BitmapDrawable result) {
                result.setAlpha(Math.round(aiMarker.FillOpacity() * 255.0f));
                callback.onSuccess(result);
            }
        });
    }

    /* access modifiers changed from: private */
    public Drawable getDefaultMarkerDrawable(MapFactory.MapMarker aiMarker) {
        return rasterizeSVG(aiMarker, this.defaultMarkerSVG);
    }

    private static float getBestGuessWidth(SVG.Svg svg) {
        if (svg.width != null) {
            return svg.width.floatValue();
        }
        if (svg.viewBox != null) {
            return svg.viewBox.width;
        }
        return 30.0f;
    }

    private static float getBestGuessHeight(SVG.Svg svg) {
        if (svg.height != null) {
            return svg.height.floatValue();
        }
        if (svg.viewBox != null) {
            return svg.viewBox.height;
        }
        return 50.0f;
    }

    private Drawable rasterizeSVG(MapFactory.MapMarker aiMarker, SVG markerSvg) {
        float width;
        SVG.Svg svg = markerSvg.getRootElement();
        float density = this.view.getContext().getResources().getDisplayMetrics().density;
        float height = aiMarker.Height() <= 0 ? getBestGuessHeight(svg) : (float) aiMarker.Height();
        if (aiMarker.Width() <= 0) {
            width = getBestGuessWidth(svg);
        } else {
            width = (float) aiMarker.Width();
        }
        float scaleH = height / getBestGuessHeight(svg);
        float scaleW = width / getBestGuessWidth(svg);
        float scale = (float) Math.sqrt((double) ((scaleH * scaleH) + (scaleW * scaleW)));
        Paint fillPaint = new Paint();
        Paint strokePaint = new Paint();
        PaintUtil.changePaint(fillPaint, aiMarker.FillColor());
        PaintUtil.changePaint(strokePaint, aiMarker.StrokeColor());
        SVG.Length length = new SVG.Length(((float) aiMarker.StrokeWidth()) / scale);
        for (SVG.SvgConditionalElement svgConditionalElement : svg.getChildren()) {
            if (svgConditionalElement instanceof SVG.SvgConditionalElement) {
                SVG.SvgConditionalElement path = svgConditionalElement;
                path.baseStyle.fill = new SVG.Colour(fillPaint.getColor());
                path.baseStyle.fillOpacity = Float.valueOf(((float) fillPaint.getAlpha()) / 255.0f);
                path.baseStyle.stroke = new SVG.Colour(strokePaint.getColor());
                path.baseStyle.strokeOpacity = Float.valueOf(((float) strokePaint.getAlpha()) / 255.0f);
                path.baseStyle.strokeWidth = length;
                path.baseStyle.specifiedFlags = 61;
                if (path.style != null) {
                    if ((path.style.specifiedFlags & SPECIFIED_FILL) == 0) {
                        path.style.fill = new SVG.Colour(fillPaint.getColor());
                        path.style.specifiedFlags |= SPECIFIED_FILL;
                    }
                    if ((path.style.specifiedFlags & SPECIFIED_FILL_OPACITY) == 0) {
                        path.style.fillOpacity = Float.valueOf(((float) fillPaint.getAlpha()) / 255.0f);
                        path.style.specifiedFlags |= SPECIFIED_FILL_OPACITY;
                    }
                    if ((path.style.specifiedFlags & SPECIFIED_STROKE) == 0) {
                        path.style.stroke = new SVG.Colour(strokePaint.getColor());
                        path.style.specifiedFlags |= SPECIFIED_STROKE;
                    }
                    if ((path.style.specifiedFlags & 16) == 0) {
                        path.style.strokeOpacity = Float.valueOf(((float) strokePaint.getAlpha()) / 255.0f);
                        path.style.specifiedFlags |= 16;
                    }
                    if ((path.style.specifiedFlags & SPECIFIED_STROKE_WIDTH) == 0) {
                        path.style.strokeWidth = length;
                        path.style.specifiedFlags |= SPECIFIED_STROKE_WIDTH;
                    }
                }
            }
        }
        Picture picture = markerSvg.renderToPicture();
        Picture scaledPicture = new Picture();
        Canvas canvas = scaledPicture.beginRecording((int) (((2.0f * ((float) aiMarker.StrokeWidth())) + width) * density), (int) (((2.0f * ((float) aiMarker.StrokeWidth())) + height) * density));
        canvas.scale(density * scaleW, density * scaleH);
        canvas.translate(length.floatValue(), length.floatValue());
        picture.draw(canvas);
        scaledPicture.endRecording();
        return new PictureDrawable(scaledPicture);
    }

    private void createNativeMarker(MapFactory.MapMarker aiMarker, AsyncCallbackPair<Marker> callback) {
        final Marker osmMarker = new Marker(this.view);
        this.featureOverlays.put(aiMarker, osmMarker);
        osmMarker.setDraggable(aiMarker.Draggable());
        osmMarker.setTitle(aiMarker.Title());
        osmMarker.setSnippet(aiMarker.Description());
        osmMarker.setPosition(new GeoPoint(aiMarker.Latitude(), aiMarker.Longitude()));
        osmMarker.setAnchor(0.5f, 1.0f);
        getMarkerDrawable(aiMarker, new AsyncCallbackFacade<Drawable, Marker>(callback) {
            public void onFailure(String message) {
                this.callback.onFailure(message);
            }

            public void onSuccess(Drawable result) {
                osmMarker.setIcon(result);
                this.callback.onSuccess(osmMarker);
            }
        });
    }

    private Polyline createNativePolyline(MapFactory.MapLineString aiLineString) {
        Polyline osmLine = new Polyline();
        osmLine.setDraggable(aiLineString.Draggable());
        osmLine.setTitle(aiLineString.Title());
        osmLine.setSnippet(aiLineString.Description());
        osmLine.setPoints(aiLineString.getPoints());
        osmLine.setColor(aiLineString.StrokeColor());
        osmLine.setWidth((float) aiLineString.StrokeWidth());
        osmLine.setInfoWindow(this.defaultInfoWindow);
        return osmLine;
    }

    private void createPolygon(Polygon osmPolygon, MapFactory.MapFeature aiFeature) {
        osmPolygon.setDraggable(aiFeature.Draggable());
        osmPolygon.setTitle(aiFeature.Title());
        osmPolygon.setSnippet(aiFeature.Description());
        osmPolygon.setStrokeColor(((MapFactory.HasStroke) aiFeature).StrokeColor());
        osmPolygon.setStrokeWidth((float) ((MapFactory.HasStroke) aiFeature).StrokeWidth());
        osmPolygon.setFillColor(((MapFactory.HasFill) aiFeature).FillColor());
        osmPolygon.setInfoWindow(this.defaultInfoWindow);
    }

    private MultiPolygon createNativePolygon(MapFactory.MapPolygon aiPolygon) {
        MultiPolygon osmPolygon = new MultiPolygon();
        createPolygon(osmPolygon, aiPolygon);
        osmPolygon.setMultiPoints(aiPolygon.getPoints());
        osmPolygon.setMultiHoles(aiPolygon.getHolePoints());
        return osmPolygon;
    }

    private Polygon createNativeCircle(MapFactory.MapCircle aiCircle) {
        Polygon osmPolygon = new Polygon();
        createPolygon(osmPolygon, aiCircle);
        osmPolygon.setPoints(Polygon.pointsAsCircle(new GeoPoint(aiCircle.Latitude(), aiCircle.Longitude()), aiCircle.Radius()));
        return osmPolygon;
    }

    private Polygon createNativeRectangle(MapFactory.MapRectangle aiRectangle) {
        BoundingBox bbox = new BoundingBox(aiRectangle.NorthLatitude(), aiRectangle.EastLongitude(), aiRectangle.SouthLatitude(), aiRectangle.WestLongitude());
        Polygon osmPolygon = new Polygon();
        createPolygon(osmPolygon, aiRectangle);
        osmPolygon.setPoints(new ArrayList(Polygon.pointsAsRect(bbox)));
        return osmPolygon;
    }

    public void showFeature(MapFactory.MapFeature feature) {
        if (!this.hiddenFeatures.contains(feature)) {
            showOverlay(this.featureOverlays.get(feature));
        }
    }

    /* access modifiers changed from: protected */
    public void showOverlay(OverlayWithIW overlay) {
        this.view.getOverlayManager().add(overlay);
        this.view.invalidate();
    }

    public void hideFeature(MapFactory.MapFeature feature) {
        hideOverlay(this.featureOverlays.get(feature));
    }

    /* access modifiers changed from: protected */
    public void hideOverlay(OverlayWithIW overlay) {
        this.view.getOverlayManager().remove(overlay);
        this.view.invalidate();
    }

    public boolean isFeatureVisible(MapFactory.MapFeature feature) {
        OverlayWithIW overlay = this.featureOverlays.get(feature);
        return overlay != null && this.view.getOverlayManager().contains(overlay);
    }

    public boolean isFeatureCollectionVisible(MapFactory.MapFeatureCollection collection) {
        return !this.hiddenFeatureCollections.contains(collection);
    }

    public void setFeatureCollectionVisible(MapFactory.MapFeatureCollection collection, boolean visible) {
        if (!visible && this.hiddenFeatureCollections.contains(collection)) {
            return;
        }
        if (visible && !this.hiddenFeatureCollections.contains(collection)) {
            return;
        }
        if (visible) {
            this.hiddenFeatureCollections.remove(collection);
            for (MapFactory.MapFeature feature : collection) {
                this.hiddenFeatures.remove(feature);
                if (feature.Visible()) {
                    showFeature(feature);
                }
            }
            return;
        }
        this.hiddenFeatureCollections.add(collection);
        for (MapFactory.MapFeature feature2 : collection) {
            this.hiddenFeatures.add(feature2);
            hideFeature(feature2);
        }
    }

    public void showInfobox(MapFactory.MapFeature feature) {
        Polyline polyOverlay = (OverlayWithIW) this.featureOverlays.get(feature);
        if (polyOverlay instanceof Marker) {
            polyOverlay.showInfoWindow();
        } else if (polyOverlay instanceof Polyline) {
            polyOverlay.showInfoWindow(feature.getCentroid());
        } else {
            ((Polygon) polyOverlay).showInfoWindow(feature.getCentroid());
        }
    }

    public void hideInfobox(MapFactory.MapFeature feature) {
        this.featureOverlays.get(feature).closeInfoWindow();
    }

    public boolean isInfoboxVisible(MapFactory.MapFeature feature) {
        OverlayWithIW overlay = this.featureOverlays.get(feature);
        return overlay != null && overlay.isInfoWindowOpen();
    }

    public BoundingBox getBoundingBox() {
        return this.view.getBoundingBox();
    }

    public void setBoundingBox(BoundingBox bbox) {
        this.view.getController().setCenter(bbox.getCenter());
        this.view.getController().zoomToSpan(bbox.getLatitudeSpan(), bbox.getLongitudeSpan());
    }

    public boolean onScroll(ScrollEvent event) {
        for (MapFactory.MapEventListener listener : this.eventListeners) {
            listener.onBoundsChanged();
        }
        return true;
    }

    public boolean onZoom(ZoomEvent event) {
        this.zoomControls.updateButtons();
        for (MapFactory.MapEventListener listener : this.eventListeners) {
            listener.onZoom();
        }
        return true;
    }

    public LocationSensor.LocationSensorListener getLocationListener() {
        return this.locationProvider;
    }

    public int getOverlayCount() {
        System.err.println(this.view.getOverlays());
        return this.view.getOverlays().size();
    }

    public void setRotation(float Rotation) {
        this.view.setMapOrientation(Rotation);
    }

    public float getRotation() {
        return this.view.getMapOrientation();
    }

    public void setScaleVisible(boolean show) {
        this.scaleBar.setEnabled(show);
        this.view.invalidate();
    }

    public boolean isScaleVisible() {
        return this.scaleBar.isEnabled();
    }

    public void setScaleUnits(MapFactory.MapScaleUnits units) {
        switch (units) {
            case METRIC:
                this.scaleBar.setUnitsOfMeasure(ScaleBarOverlay.UnitsOfMeasure.metric);
                break;
            case IMPERIAL:
                this.scaleBar.setUnitsOfMeasure(ScaleBarOverlay.UnitsOfMeasure.imperial);
                break;
            default:
                throw new IllegalArgumentException("Unallowable unit system: " + units);
        }
        this.view.invalidate();
    }

    public MapFactory.MapScaleUnits getScaleUnits() {
        switch (AnonymousClass14.$SwitchMap$org$osmdroid$views$overlay$ScaleBarOverlay$UnitsOfMeasure[this.scaleBar.getUnitsOfMeasure().ordinal()]) {
            case 1:
                return MapFactory.MapScaleUnits.IMPERIAL;
            case 2:
                return MapFactory.MapScaleUnits.METRIC;
            default:
                throw new IllegalStateException("Somehow we have an unallowed unit system");
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.util.NativeOpenStreetMapController$14  reason: invalid class name */
    static /* synthetic */ class AnonymousClass14 {
        static final /* synthetic */ int[] $SwitchMap$org$osmdroid$views$overlay$ScaleBarOverlay$UnitsOfMeasure = new int[ScaleBarOverlay.UnitsOfMeasure.values().length];

        static {
            $SwitchMap$com$google$appinventor$components$common$ScaleUnits = new int[ScaleUnits.values().length];
            try {
                $SwitchMap$com$google$appinventor$components$common$ScaleUnits[ScaleUnits.Metric.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$appinventor$components$common$ScaleUnits[ScaleUnits.Imperial.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$osmdroid$views$overlay$ScaleBarOverlay$UnitsOfMeasure[ScaleBarOverlay.UnitsOfMeasure.imperial.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$osmdroid$views$overlay$ScaleBarOverlay$UnitsOfMeasure[ScaleBarOverlay.UnitsOfMeasure.metric.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$com$google$appinventor$components$runtime$util$MapFactory$MapScaleUnits = new int[MapFactory.MapScaleUnits.values().length];
            try {
                $SwitchMap$com$google$appinventor$components$runtime$util$MapFactory$MapScaleUnits[MapFactory.MapScaleUnits.METRIC.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$google$appinventor$components$runtime$util$MapFactory$MapScaleUnits[MapFactory.MapScaleUnits.IMPERIAL.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$com$google$appinventor$components$common$MapType = new int[MapType.values().length];
            try {
                $SwitchMap$com$google$appinventor$components$common$MapType[MapType.Road.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$google$appinventor$components$common$MapType[MapType.Aerial.ordinal()] = 2;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$google$appinventor$components$common$MapType[MapType.Terrain.ordinal()] = 3;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    public void setScaleUnitsAbstract(ScaleUnits units) {
        switch (units) {
            case Metric:
                this.scaleBar.setUnitsOfMeasure(ScaleBarOverlay.UnitsOfMeasure.metric);
                break;
            case Imperial:
                this.scaleBar.setUnitsOfMeasure(ScaleBarOverlay.UnitsOfMeasure.imperial);
                break;
        }
        this.view.invalidate();
    }

    public ScaleUnits getScaleUnitsAbstract() {
        switch (AnonymousClass14.$SwitchMap$org$osmdroid$views$overlay$ScaleBarOverlay$UnitsOfMeasure[this.scaleBar.getUnitsOfMeasure().ordinal()]) {
            case 1:
                return ScaleUnits.Imperial;
            case 2:
                return ScaleUnits.Metric;
            default:
                throw new IllegalStateException("Somehow we have an unallowed unit system");
        }
    }

    static class MultiPolygon extends Polygon {
        private List<Polygon> children = new ArrayList();
        private Polygon.OnClickListener clickListener;
        private Polygon.OnDragListener dragListener;
        private boolean draggable;

        MultiPolygon() {
        }

        public void showInfoWindow() {
            if (this.children.size() > 0) {
                this.children.get(0).showInfoWindow();
            }
        }

        public void draw(Canvas canvas, MapView mapView, boolean b) {
            for (Polygon child : this.children) {
                child.draw(canvas, mapView, b);
            }
        }

        public List<List<GeoPoint>> getMultiPoints() {
            List<List<GeoPoint>> result = new ArrayList<>();
            for (Polygon p : this.children) {
                result.add(p.getPoints());
            }
            return result;
        }

        public void setMultiPoints(List<List<GeoPoint>> points) {
            Iterator<Polygon> polygonIterator = this.children.iterator();
            Iterator<List<GeoPoint>> pointIterator = points.iterator();
            while (polygonIterator.hasNext() && pointIterator.hasNext()) {
                polygonIterator.next().setPoints(pointIterator.next());
            }
            while (polygonIterator.hasNext()) {
                polygonIterator.next();
                polygonIterator.remove();
            }
            while (pointIterator.hasNext()) {
                Polygon p = new Polygon();
                p.setPoints(pointIterator.next());
                p.setStrokeColor(getStrokeColor());
                p.setFillColor(getFillColor());
                p.setStrokeWidth(getStrokeWidth());
                p.setInfoWindow(getInfoWindow());
                p.setDraggable(this.draggable);
                p.setOnClickListener(this.clickListener);
                p.setOnDragListener(this.dragListener);
                this.children.add(p);
            }
        }

        public List<List<List<GeoPoint>>> getMultiHoles() {
            List<List<List<GeoPoint>>> result = new ArrayList<>();
            for (Polygon p : this.children) {
                result.add(p.getHoles());
            }
            return result;
        }

        public void setMultiHoles(List<List<List<GeoPoint>>> holes) {
            if (holes == null || holes.isEmpty()) {
                for (Polygon child : this.children) {
                    child.setHoles(Collections.emptyList());
                }
            } else if (holes.size() != this.children.size()) {
                throw new IllegalArgumentException("Holes and points are not of the same arity.");
            } else {
                Iterator<Polygon> polygonIterator = this.children.iterator();
                Iterator<List<List<GeoPoint>>> holeIterator = holes.iterator();
                while (polygonIterator.hasNext() && holeIterator.hasNext()) {
                    polygonIterator.next().setHoles(holeIterator.next());
                }
            }
        }

        public void setDraggable(boolean draggable2) {
            NativeOpenStreetMapController.super.setDraggable(draggable2);
            this.draggable = draggable2;
            for (Polygon child : this.children) {
                child.setDraggable(draggable2);
            }
        }

        public void setOnClickListener(Polygon.OnClickListener listener) {
            NativeOpenStreetMapController.super.setOnClickListener(listener);
            this.clickListener = listener;
            for (Polygon child : this.children) {
                child.setOnClickListener(listener);
            }
        }

        public void setOnDragListener(Polygon.OnDragListener listener) {
            NativeOpenStreetMapController.super.setOnDragListener(listener);
            this.dragListener = listener;
            for (Polygon child : this.children) {
                child.setOnDragListener(listener);
            }
        }

        public void setStrokeWidth(float strokeWidth) {
            NativeOpenStreetMapController.super.setStrokeWidth(strokeWidth);
            for (Polygon child : this.children) {
                child.setStrokeWidth(strokeWidth);
            }
        }

        public void setStrokeColor(int strokeColor) {
            NativeOpenStreetMapController.super.setStrokeColor(strokeColor);
            for (Polygon child : this.children) {
                child.setStrokeColor(strokeColor);
            }
        }

        public void setFillColor(int fillColor) {
            NativeOpenStreetMapController.super.setFillColor(fillColor);
            for (Polygon child : this.children) {
                child.setFillColor(fillColor);
            }
        }

        public void setTitle(String title) {
            NativeOpenStreetMapController.super.setTitle(title);
            for (Polygon child : this.children) {
                child.setTitle(title);
            }
        }

        public void setSnippet(String snippet) {
            NativeOpenStreetMapController.super.setSnippet(snippet);
            for (Polygon child : this.children) {
                child.setSnippet(snippet);
            }
        }

        public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView) {
            for (Polygon child : this.children) {
                if (child.onSingleTapConfirmed(event, mapView)) {
                    return true;
                }
            }
            return false;
        }

        public boolean contains(MotionEvent event) {
            for (Polygon child : this.children) {
                if (child.contains(event)) {
                    return true;
                }
            }
            return false;
        }

        public boolean onLongPress(MotionEvent event, MapView mapView) {
            boolean touched = contains(event);
            if (touched) {
                if (this.mDraggable) {
                    this.mIsDragged = true;
                    closeInfoWindow();
                    this.mDragStartPoint = event;
                    if (this.mOnDragListener != null) {
                        this.mOnDragListener.onDragStart(this);
                    }
                    moveToEventPosition(event, this.mDragStartPoint, mapView);
                } else if (this.mOnClickListener != null) {
                    this.mOnClickListener.onLongClick(this, mapView, mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY()));
                }
            }
            return touched;
        }

        public void moveToEventPosition(MotionEvent event, MotionEvent start, MapView view) {
            for (Polygon child : this.children) {
                child.moveToEventPosition(event, start, view);
            }
        }

        public void finishMove(MotionEvent start, MotionEvent end, MapView view) {
            for (Polygon child : this.children) {
                child.finishMove(start, end, view);
            }
        }

        public boolean onTouchEvent(MotionEvent event, MapView mapView) {
            if (this.mDraggable && this.mIsDragged) {
                if (event.getAction() == 1) {
                    this.mIsDragged = false;
                    finishMove(this.mDragStartPoint, event, mapView);
                    if (this.mOnDragListener == null) {
                        return true;
                    }
                    this.mOnDragListener.onDragEnd(this);
                    return true;
                } else if (event.getAction() == 2) {
                    moveToEventPosition(event, this.mDragStartPoint, mapView);
                    if (this.mOnDragListener == null) {
                        return true;
                    }
                    this.mOnDragListener.onDrag(this);
                    return true;
                }
            }
            return false;
        }
    }
}
