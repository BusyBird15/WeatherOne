package com.google.appinventor.components.runtime.util;

import android.view.View;
import com.google.appinventor.components.common.MapType;
import com.google.appinventor.components.common.ScaleUnits;
import com.google.appinventor.components.runtime.LocationSensor;
import com.google.appinventor.components.runtime.util.MapFactory;
import org.osmdroid.util.BoundingBox;

class DummyMapController implements MapFactory.MapController {
    DummyMapController() {
    }

    public View getView() {
        throw new UnsupportedOperationException();
    }

    public double getLatitude() {
        throw new UnsupportedOperationException();
    }

    public double getLongitude() {
        throw new UnsupportedOperationException();
    }

    public void setCenter(double latitude, double longitude) {
        throw new UnsupportedOperationException();
    }

    public void setZoom(int zoom) {
        throw new UnsupportedOperationException();
    }

    public void setRotation(float rotation) {
        throw new UnsupportedOperationException();
    }

    public float getRotation() {
        throw new UnsupportedOperationException();
    }

    public int getZoom() {
        throw new UnsupportedOperationException();
    }

    public void setMapType(MapFactory.MapType type) {
        throw new UnsupportedOperationException();
    }

    public MapFactory.MapType getMapType() {
        throw new UnsupportedOperationException();
    }

    public void setMapTypeAbstract(MapType type) {
        throw new UnsupportedOperationException();
    }

    public MapType getMapTypeAbstract() {
        throw new UnsupportedOperationException();
    }

    public void setCompassEnabled(boolean enabled) {
        throw new UnsupportedOperationException();
    }

    public boolean isCompassEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setZoomEnabled(boolean enabled) {
        throw new UnsupportedOperationException();
    }

    public boolean isZoomEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setZoomControlEnabled(boolean enabled) {
        throw new UnsupportedOperationException();
    }

    public boolean isZoomControlEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setShowUserEnabled(boolean enable) {
        throw new UnsupportedOperationException();
    }

    public boolean isShowUserEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setRotationEnabled(boolean enable) {
        throw new UnsupportedOperationException();
    }

    public boolean isRotationEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setPanEnabled(boolean enable) {
        throw new UnsupportedOperationException();
    }

    public boolean isPanEnabled() {
        throw new UnsupportedOperationException();
    }

    public void panTo(double latitude, double longitude, int zoom, double seconds) {
        throw new UnsupportedOperationException();
    }

    public void addEventListener(MapFactory.MapEventListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addFeature(MapFactory.MapMarker marker) {
        throw new UnsupportedOperationException();
    }

    public void updateFeaturePosition(MapFactory.MapMarker marker) {
        throw new UnsupportedOperationException();
    }

    public void updateFeatureFill(MapFactory.HasFill marker) {
        throw new UnsupportedOperationException();
    }

    public void updateFeatureImage(MapFactory.MapMarker marker) {
        throw new UnsupportedOperationException();
    }

    public void updateFeatureText(MapFactory.MapFeature marker) {
        throw new UnsupportedOperationException();
    }

    public void updateFeatureDraggable(MapFactory.MapFeature marker) {
        throw new UnsupportedOperationException();
    }

    public BoundingBox getBoundingBox() {
        throw new UnsupportedOperationException();
    }

    public void setBoundingBox(BoundingBox bbox) {
        throw new UnsupportedOperationException();
    }

    public void addFeature(MapFactory.MapLineString polyline) {
        throw new UnsupportedOperationException();
    }

    public void addFeature(MapFactory.MapPolygon polygon) {
        throw new UnsupportedOperationException();
    }

    public void addFeature(MapFactory.MapCircle circle) {
        throw new UnsupportedOperationException();
    }

    public void addFeature(MapFactory.MapRectangle circle) {
        throw new UnsupportedOperationException();
    }

    public void removeFeature(MapFactory.MapFeature feature) {
        throw new UnsupportedOperationException();
    }

    public void showFeature(MapFactory.MapFeature feature) {
        throw new UnsupportedOperationException();
    }

    public void hideFeature(MapFactory.MapFeature feature) {
        throw new UnsupportedOperationException();
    }

    public boolean isFeatureVisible(MapFactory.MapFeature feature) {
        throw new UnsupportedOperationException();
    }

    public boolean isFeatureCollectionVisible(MapFactory.MapFeatureCollection collection) {
        throw new UnsupportedOperationException();
    }

    public void setFeatureCollectionVisible(MapFactory.MapFeatureCollection collection, boolean visible) {
        throw new UnsupportedOperationException();
    }

    public void showInfobox(MapFactory.MapFeature feature) {
        throw new UnsupportedOperationException();
    }

    public void hideInfobox(MapFactory.MapFeature feature) {
        throw new UnsupportedOperationException();
    }

    public boolean isInfoboxVisible(MapFactory.MapFeature feature) {
        throw new UnsupportedOperationException();
    }

    public void updateFeaturePosition(MapFactory.MapLineString polyline) {
        throw new UnsupportedOperationException();
    }

    public void updateFeaturePosition(MapFactory.MapPolygon polygon) {
        throw new UnsupportedOperationException();
    }

    public void updateFeatureHoles(MapFactory.MapPolygon polygon) {
        throw new UnsupportedOperationException();
    }

    public void updateFeaturePosition(MapFactory.MapCircle circle) {
        throw new UnsupportedOperationException();
    }

    public void updateFeaturePosition(MapFactory.MapRectangle rectangle) {
        throw new UnsupportedOperationException();
    }

    public void updateFeatureStroke(MapFactory.HasStroke marker) {
        throw new UnsupportedOperationException();
    }

    public void updateFeatureSize(MapFactory.MapMarker marker) {
        throw new UnsupportedOperationException();
    }

    public LocationSensor.LocationSensorListener getLocationListener() {
        throw new UnsupportedOperationException();
    }

    public int getOverlayCount() {
        throw new UnsupportedOperationException();
    }

    public void setScaleVisible(boolean show) {
        throw new UnsupportedOperationException();
    }

    public boolean isScaleVisible() {
        throw new UnsupportedOperationException();
    }

    public void setScaleUnits(MapFactory.MapScaleUnits units) {
        throw new UnsupportedOperationException();
    }

    public MapFactory.MapScaleUnits getScaleUnits() {
        throw new UnsupportedOperationException();
    }

    public void setScaleUnitsAbstract(ScaleUnits units) {
        throw new UnsupportedOperationException();
    }

    public ScaleUnits getScaleUnitsAbstract() {
        throw new UnsupportedOperationException();
    }
}
