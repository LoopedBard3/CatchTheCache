package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheListRequest {
    private double player_longitude, player_latitude;

    public CacheListRequest(double player_longitude, double player_latitude) {
        this.player_longitude = player_longitude;
        this.player_latitude = player_latitude;
    }

    public CacheListRequest() {
        this.player_latitude = 42.02413;
        this.player_longitude = -93.65069;
    }
}
