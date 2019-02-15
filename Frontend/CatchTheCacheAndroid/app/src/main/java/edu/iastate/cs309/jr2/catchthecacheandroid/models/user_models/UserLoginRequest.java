package edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models;

public class UserLoginRequest {
	private String username, password;

	public UserLoginRequest(String u, String p) {
		this.username = u;
		this.password = p;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}
}
