package edu.iastate.cs309.jr2.catchthecacheandroid.models;

public class UserQuestionResponse {
	private String security_question;

	public UserQuestionResponse(String q) {
		this.security_question = q;
	}

	public String getQuestion() {
		return this.security_question;
	}
}
