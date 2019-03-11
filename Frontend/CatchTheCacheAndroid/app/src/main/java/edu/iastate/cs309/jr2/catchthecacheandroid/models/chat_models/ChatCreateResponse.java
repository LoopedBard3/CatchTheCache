package edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models;

public class ChatCreateResponse {
	private boolean success;

	public ChatCreateResponse() {
		this.success = false;
	}

	public void setSuccess(boolean b) {
		this.success = b;
	}

	public boolean getSuccess() {
		return this.success;
	}
}
