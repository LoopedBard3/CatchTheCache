package edu.iastate.cs309.jr2.catchthecacheandroid.models.cache_models;



import android.os.Message;

import java.util.List;

//import edu.iastate.cs309.jr2.catchthecacheandroid.models.Message;
//TODO change import statement back to edu.models

public class CacheChatResponse {
	List<Message> messageList;

	public CacheChatResponse(List<Message> messageList) {
		this.messageList = messageList;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

}
