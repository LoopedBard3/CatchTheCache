package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

public class UserQuestionRequest {
	private String username;

	public UserQuestionRequest(String u) {
		this.username = u;
	}

	public String getUsername() {
		return this.username;
	}
}
