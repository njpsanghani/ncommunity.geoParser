package model;


import com.sun.istack.internal.NotNull;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class GeoPolygon {

    private int id;
    private String polygonName;
    private Area polyArea;
    private List<GeoPoint> geoPointList;

    public GeoPolygon(){
        geoPointList = new ArrayList<>();
    }

    public GeoPolygon(int id, String polygonName, List<GeoPoint> geoPointList) {
        this.id = id;
        this.polygonName = polygonName;
        this.geoPointList = geoPointList;
        this.polyArea = buildArea(geoPointList);
    }

    public GeoPolygon(List<GeoPoint> geoPointList) {
        this.id = 0;
        this.polygonName = "";
        this.geoPointList = geoPointList;
        this.polyArea = buildArea(geoPointList);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPolygonName() {
        return polygonName;
    }

    public void setPolygonName(String polygonName) {
        this.polygonName = polygonName;
    }

    public Area getPolyArea() {
        return polyArea;
    }

    public void setPolyArea(Area polyArea) {
        this.polyArea = polyArea;
    }

    public List<GeoPoint> getGeoPointList() {
        return geoPointList;
    }

    public void setGeoPointList(List<GeoPoint> geoPointList) {
        this.geoPointList = geoPointList;
    }

    public Area buildArea(@NotNull List<GeoPoint> geoPoints) {
        Path2D boundary = new Path2D.Double();
        for (int i = 0; i < geoPoints.size(); i++) {
            if (i == 0) {
                boundary.moveTo(geoPoints.get(i).getLatitude(), geoPoints.get(i).getLongitude());
            } else {
                boundary.lineTo(geoPoints.get(i).getLatitude(), geoPoints.get(i).getLongitude());
            }
        }

        Area area = new Area(boundary);
        if (!area.isPolygonal()) {
            boundary.closePath();
            return new Area(boundary);
        } else {
            return area;
        }
    }

    public Area buildArea() {
        if (this.geoPointList!=null && this.geoPointList.size()>2) {
            Path2D boundary = new Path2D.Double();
            for (int i = 0; i < geoPointList.size(); i++) {
                if (i == 0) {
                    boundary.moveTo(geoPointList.get(i).getLatitude(), geoPointList.get(i).getLongitude());
                } else {
                    boundary.lineTo(geoPointList.get(i).getLatitude(), geoPointList.get(i).getLongitude());
                }
            }

            Area area = new Area(boundary);
            if (!area.isPolygonal()) {
                boundary.closePath();
                return new Area(boundary);
            } else {
                return area;
            }
        }
        else return null;
    }

}
