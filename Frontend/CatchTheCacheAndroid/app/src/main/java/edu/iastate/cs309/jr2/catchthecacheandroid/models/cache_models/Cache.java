package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

import java.io.Serializable;

public class Cache implements Serializable{
    public String name, description;
    private double longitude, latitude;
    private Integer chatId, id;

    public Cache(String name, double longitude, double latitude)
    {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        description = null;
    }

    public Cache(String name, double longitude, double latitude, String desc) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        description = desc;
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

    public Integer getChatId() {
        return chatId;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription(){
        return description;
    }
}
