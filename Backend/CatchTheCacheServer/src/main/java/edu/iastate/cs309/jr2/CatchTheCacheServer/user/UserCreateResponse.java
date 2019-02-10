package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

public class UserCreateResponse {
	private boolean validUsername, validPassword;
	private String message;

	public UserCreateResponse(boolean validUser, boolean validPass, String message) {
		this.validUsername = validUser;
		this.validPassword = validPass;
		this.message = message;
	}

	public UserCreateResponse() {
		this.validUsername = false;
		this.validPassword = false;
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
}
