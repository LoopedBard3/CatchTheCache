package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

public class UserQuestionResponse {
	private String question;

	public UserQuestionResponse(String q) {
		this.question = q;
	}

	public String getQuestion() {
		return this.question;
	}
}
