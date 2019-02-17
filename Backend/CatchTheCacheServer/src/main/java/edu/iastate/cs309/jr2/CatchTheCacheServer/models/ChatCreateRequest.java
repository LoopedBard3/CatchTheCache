package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class ChatCreateRequest {
	private String user, cacheId;

	public void updateRequest(String u, String c) {
		this.user = u;
		this.cacheId = c;
	}

	public String getUser() {
		return this.user;
	}

	public String getCacheId() {
		return this.cacheId;
	}

}
