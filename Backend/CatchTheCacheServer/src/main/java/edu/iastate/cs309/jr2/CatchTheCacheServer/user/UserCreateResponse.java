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

	protected void setValidUser(boolean b) {
		this.validUsername = b;
	}

	protected void setValidPass(boolean b) {
		this.validPassword = b;
	}

	protected void setMessage(String m) {
		this.message = m;
	}
	
	protected boolean getValidUser() {
		return this.validUsername;
	}

	protected boolean getValidPass() {
		return this.validPassword;
	}

	protected String getMessage() {
		return this.message;
	}
}
