package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;

public class CacheMessageRequest {
	private String sender;
	private String message;
	
	public CacheMessageRequest(String sender, String message) {
		this.sender=sender;
		this.message=message;
	}
	public CacheMessageRequest() {
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}
	

}
