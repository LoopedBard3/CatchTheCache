package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

import java.util.List;

public class MessageListResponse {
	List<Message> messageList;

	public MessageListResponse(List<Message> messageList) {
		this.messageList = messageList;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

}
