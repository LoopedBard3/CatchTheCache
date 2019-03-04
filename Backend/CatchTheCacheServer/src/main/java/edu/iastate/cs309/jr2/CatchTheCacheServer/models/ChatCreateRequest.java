package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

import java.util.List;

import edu.iastate.cs309.jr2.CatchTheCacheServer.user.User;

public class ChatCreateRequest {
	private String users;
	private String  cacheId;

	public void updateRequest(String u, String c) {
		this.users = u;
		this.cacheId = c;
	}

	public String getUsers() {
		return this.users;
	}

	public String getCacheId() {
		return this.cacheId;
	}

}
