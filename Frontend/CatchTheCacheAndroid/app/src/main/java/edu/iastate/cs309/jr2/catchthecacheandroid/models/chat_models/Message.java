package edu.iastate.cs309.jr2.catchthecacheandroid.models.chat_models;




public class Message {


	private Integer id;


	private Integer chatId;


	private String sender;


	private String text;

	public Message() {
	}

	public Message(String sender, String text) {
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

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
