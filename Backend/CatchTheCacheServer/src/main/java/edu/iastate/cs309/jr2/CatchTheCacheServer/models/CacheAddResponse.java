package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class CacheAddResponse {
	private boolean authorized, success;

	public CacheAddResponse(boolean authorized, boolean success) {
		this.authorized = authorized;
		this.success = success;
	}

	public CacheAddResponse() {
		this.authorized = false;
		this.success = false;
	}

	public boolean getAuthorized() {
		return this.authorized;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
