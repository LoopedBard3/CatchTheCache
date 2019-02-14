package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

import java.util.ArrayList;

public class CacheListResponse {
    ArrayList<CacheIndividual> cacheList;

    public CacheListResponse(ArrayList<CacheIndividual> cacheList) {
        this.cacheList = cacheList;
    }

    public ArrayList<CacheIndividual> getCacheList() {
        return cacheList;
    }

    public void setCacheList(ArrayList<CacheIndividual> cacheList) {
        this.cacheList = cacheList;
    }
}
