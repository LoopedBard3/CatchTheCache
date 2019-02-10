package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

public class UserResetPassRequest {
	private String username, new_password, security_answer;

	public UserResetPassRequest(String u, String p, String a) {
		this.username = u;
		this.new_password = p;
		this.security_answer = a;
	}

	public String getUsername() {
		return this.username;
	}

	public String getAnswer() {
		return this.security_answer;
	}

	public String getNewPassword() {
		return this.new_password;
	}
}
