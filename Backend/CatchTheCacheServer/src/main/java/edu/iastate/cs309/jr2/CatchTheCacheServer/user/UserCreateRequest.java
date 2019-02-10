package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

public class UserCreateRequest {
	private String username, password, security_question, security_answer;
	private int authority;

	public void updateRequest(String u, String p, String q, String a) {
		this.username = u;
		this.password = p;
		this.security_question = q;
		this.security_answer = a;
		this.authority = 0;
	}

	public void updateRequest(String u, String p, String q, String a, int auth) {
		this.username = u;
		this.password = p;
		this.security_question = q;
		this.security_answer = a;
		this.authority = auth;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getQuestion() {
		return this.security_question;
	}

	public String getAnswer() {
		return this.security_answer;
	}

	public int getAuthority() {
		return this.authority;
	}
}
