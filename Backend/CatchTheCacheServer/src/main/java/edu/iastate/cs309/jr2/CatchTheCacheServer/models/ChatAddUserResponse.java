package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class ChatAddUserResponse {
	private boolean success;
	
	public ChatAddUserResponse() {
		this.success = false;
	}

	public void setSuccess(boolean b) {
		this.success = b;
	}

	public boolean getSuccess() {
		return this.success;
	}
}
