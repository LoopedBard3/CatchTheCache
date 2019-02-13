package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class UserLoginResponse {
	private String message;
	private boolean success;

	public UserLoginResponse(boolean s, String m) {
		this.message = m;
		this.success = s;
	}

	public UserLoginResponse() {
		this.success = false;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String m) {
		this.message = m;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(boolean s) {
		this.success = s;
	}
}
