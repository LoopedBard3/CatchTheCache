package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheAddResponse {
  private boolean authorized, success;

    public CacheAddResponse(boolean authorized, boolean success) {
        this.authorized = authorized;
        this.success = success;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
