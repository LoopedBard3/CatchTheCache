package edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models;

public class UserQuestionResponse {
	private String question;

	public UserQuestionResponse(String q) {
		this.question = q;
	}

	public String getQuestion() {
		return this.question;
	}
}
