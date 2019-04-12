package edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models;


import edu.iastate.cs309.jr2.catchthecacheandroid.models.user_models.User;

public class Message {


	private Integer id;


	private Integer chatId;


	private User sender;


	private String text;

	public Message() {
	}

	public Message(User sender, String text) {
		this.sender = sender;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public void setChatId(int id) {
		this.chatId = id;
	}

	public Integer getChatId() {
		return chatId;
	}

	public User getSender() {
		return this.sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
