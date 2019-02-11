package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

public class UserCreateResponse {
	private boolean success, validUsername, validPassword;
	private String message;

	public UserCreateResponse() {
		this.validUsername = false;
		this.validPassword = false;
	}

	public void setSuccess(boolean b) {
		this.success = b;
	}

	public void setValidUser(boolean b) {
		this.validUsername = b;
	}

	public void setValidPass(boolean b) {
		this.validPassword = b;
	}

	public void setMessage(String m) {
		this.message = m;
	}

	public boolean getValidUser() {
		return this.validUsername;
	}

	public boolean getValidPass() {
		return this.validPassword;
	}

	public String getMessage() {
		return this.message;
	}

	public boolean getSuccess() {
		return this.success;
	}
}
