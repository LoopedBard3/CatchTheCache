package edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models;

public class MessageRequest {
	private String sender;
	private String message;

	public MessageRequest(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}

	public MessageRequest() {
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}

}
