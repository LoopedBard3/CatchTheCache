package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheIndividual {
    public String name;
    private double longitude, latitude;

    public CacheIndividual(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
