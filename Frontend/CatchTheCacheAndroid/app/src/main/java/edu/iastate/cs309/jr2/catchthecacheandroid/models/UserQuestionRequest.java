package edu.iastate.cs309.jr2.catchthecacheandroid.models;

public class UserQuestionRequest {
	private String username;

	public UserQuestionRequest(String u) {
		this.username = u;
	}

	public String getUsername() {
		return this.username;
	}
}
