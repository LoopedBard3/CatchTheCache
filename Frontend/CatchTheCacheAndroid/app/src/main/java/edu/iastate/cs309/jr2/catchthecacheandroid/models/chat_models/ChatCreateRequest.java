package edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models;

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
