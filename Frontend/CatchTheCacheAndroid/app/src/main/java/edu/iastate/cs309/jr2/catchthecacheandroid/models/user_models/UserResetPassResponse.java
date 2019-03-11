package edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models;

public class UserResetPassResponse {
	private boolean validAnswer, validPassword;
	private String message;

	public UserResetPassResponse() {
		this.validAnswer = false;
		this.validPassword = false;
	}

	public void setValidAnswer(boolean b) {
		this.validAnswer = b;
	}

	public void setValidPassword(boolean b) {
		this.validPassword = b;
	}

	public void setMessage(String m) {
		this.message = m;
	}

	public boolean getValidAnswer() {
		return this.validAnswer;
	}

	public boolean getValidPass() {
		return this.validPassword;
	}

	public String getMessage() {
		return this.message;
	}

	public boolean getSuccess() {
		return this.validAnswer && this.validPassword;
	}
}
