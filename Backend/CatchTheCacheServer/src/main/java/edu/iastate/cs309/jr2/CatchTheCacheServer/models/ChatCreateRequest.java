package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class ChatCreateRequest {
	private String user;
	private int cacheId;

	public void updateRequest(String u, int c) {
		this.user = u;
		this.cacheId = c;
	}

	public String getUser() {
		return this.user;
	}

	public Integer getCacheId() {
		return this.cacheId;
	}

}
