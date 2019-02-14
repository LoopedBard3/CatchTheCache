package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

public class Cache {
	private String name;
	private double longitude, latitude;

	public Cache(String name, double longitude, double latitude) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getName() {
		return this.name;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}
}
