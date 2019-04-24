package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class ChatRemoveUserResponse {
	private boolean success;

	public ChatRemoveUserResponse() {
		success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
