package edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models;

public class MessageResponse {
	boolean success;

	public MessageResponse() {
		this.success = false;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
