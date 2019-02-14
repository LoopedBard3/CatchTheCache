package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheAddRequest {
    private String name;
    private double longitude, latitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public CacheAddRequest(String name, double lon, double lat){
        this.name = name;
        longitude = lon;
        latitude = lat;
    }
}
