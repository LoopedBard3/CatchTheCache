package edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models;

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
