package edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models;

public class UserResetPassRequest {
	private String username, new_password, answer;

	public UserResetPassRequest(String u, String p, String a) {
		this.username = u;
		this.new_password = p;
		this.answer = a;
	}

	public String getUsername() {
		return this.username;
	}

	public String getAnswer() {
		return this.answer;
	}

	public String getNewPassword() {
		return this.new_password;
	}
}
