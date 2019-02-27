package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class UserResetPassRequest {
	private String username, newPassword, answer;

	public UserResetPassRequest(String u, String p, String a) {
		this.username = u;
		this.newPassword = p;
		this.answer = a;
	}

	public String getUsername() {
		return this.username;
	}

	public String getAnswer() {
		return this.answer;
	}

	public String getNewPassword() {
		return this.newPassword;
	}
}
