package model;

import java.util.ArrayList;
import java.util.List;

public class GeoData {

    private List<GeoPoint> geoPoints;
    private List<GeoPolygon> geoPolygons;

    public GeoData() {
        geoPoints = new ArrayList<>();
        geoPolygons = new ArrayList<>();
    }

    public List<GeoPoint> getGeoPoints() {
        return geoPoints;
    }

    public void setGeoPoints(List<GeoPoint> geoPoints) {
        this.geoPoints = geoPoints;
    }

    public List<GeoPolygon> getGeoPolygons() {
        return geoPolygons;
    }

    public void setGeoPolygons(List<GeoPolygon> geoPolygons) {
        this.geoPolygons = geoPolygons;
    }
}
