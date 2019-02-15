package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

import java.util.ArrayList;

public class CacheListResponse {
    ArrayList<Cache> cacheList;

    public CacheListResponse(ArrayList<Cache> cacheList) {
        this.cacheList = cacheList;
    }

    public ArrayList<Cache> getCacheList() {
        return cacheList;
    }

    public void setCacheList(ArrayList<Cache> cacheList) {
        this.cacheList = cacheList;
    }
}
