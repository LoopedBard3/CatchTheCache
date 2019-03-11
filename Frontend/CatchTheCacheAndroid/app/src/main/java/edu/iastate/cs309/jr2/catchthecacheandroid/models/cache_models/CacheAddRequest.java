package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheAddRequest {
	private String creator, description, name;
	private double longitude, latitude;

	public CacheAddRequest(String name, double lon, double lat, String creator, String description) {
		this.name = name;
		this.longitude = lon;
		this.latitude = lat;
		this.creator = creator;
		this.description = description;
	}

	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public String getCreator() {
		return this.creator;
	}

}
