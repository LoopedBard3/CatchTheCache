package edu.iastate.cs309.jr2.catchthecacheandroid.models;

public class UserCreateResponse {
	private boolean validUsername, validPassword;
	private String message;
	boolean success;

	public UserCreateResponse(boolean validUser, boolean validPass, String message) {
		this.validUsername = validUser;
		this.validPassword = validPass;
		this.message = message;
		this.success = false;
	}

	public UserCreateResponse() {
		this.validUsername = false;
		this.validPassword = false;
		this.success = false;
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

	public void setSuccess(boolean b) {
		this.success = b;
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
