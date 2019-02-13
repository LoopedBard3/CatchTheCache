package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class UserCreateRequest {
	private String username, password, question, answer;
	private int authority;

	public void updateRequest(String u, String p, String q, String a) {
		this.username = u;
		this.password = p;
		this.question = q;
		this.answer = a;
		this.authority = 0;
	}

	public void updateRequest(String u, String p, String q, String a, int auth) {
		this.username = u;
		this.password = p;
		this.question = q;
		this.answer = a;
		this.authority = auth;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getQuestion() {
		return this.question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public int getAuthority() {
		return this.authority;
	}
}
