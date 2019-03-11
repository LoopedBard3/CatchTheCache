package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheListRequest {
	private double playerLongitude, playerLatitude;

	public CacheListRequest(double player_longitude, double player_latitude) {
		this.playerLongitude = player_longitude;
		this.playerLatitude = player_latitude;
	}

	public CacheListRequest() {
		this.playerLatitude = 42.02413;
		this.playerLongitude = -93.65069;
	}

	public double getPlayerLongitude() {
		return this.playerLongitude;
	}

	public double getPlayerLatitude() {
		return this.playerLatitude;
	}
}
