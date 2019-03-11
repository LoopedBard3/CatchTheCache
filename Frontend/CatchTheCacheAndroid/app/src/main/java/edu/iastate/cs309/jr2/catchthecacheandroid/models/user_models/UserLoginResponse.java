package edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models;

public class UserLoginResponse {
	private String message;
	private boolean success;
	private int authority;

	public UserLoginResponse(boolean s, String m, int a) {
		this.message = m;
		this.success = s;
		this.authority = a;
	}

	public UserLoginResponse() {
		this.success = false;
		this.authority = 0;
	}

	public int getAuthority() {
		return this.authority;
	}

	public void setAuthority(int a) {
		this.authority = a;
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
