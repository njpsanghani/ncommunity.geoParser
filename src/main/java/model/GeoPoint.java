package model;

public class GeoPoint {
    private int id;
    private Double latitude;
    private Double longitude;

    public GeoPoint(Double latitude, Double longitude) {
        this.id = 0;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeoPoint(int id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
