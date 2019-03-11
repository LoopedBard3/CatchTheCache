package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class MessageResponse {
	boolean success;

	public MessageResponse() {
		this.success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
