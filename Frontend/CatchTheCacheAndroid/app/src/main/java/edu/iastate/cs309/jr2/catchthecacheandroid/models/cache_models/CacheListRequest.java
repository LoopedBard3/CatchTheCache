package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheListRequest {
    private double playerLongitude, playerLatitude;

    public CacheListRequest(double playerLongitude, double playerLatitude) {
        this.playerLongitude = playerLongitude;
        this.playerLatitude = playerLatitude;
    }

    public CacheListRequest() {
        this.playerLatitude = 42.02413;
        this.playerLongitude = -93.65069;
    }
}
