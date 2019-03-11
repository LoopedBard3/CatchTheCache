package edu.iastate.cs309.jr2.CatchTheCacheServer.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "messages")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer id;

	@Column(name = "chatId")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer chatId;

	@Column(name = "sender")
	@NotFound(action = NotFoundAction.IGNORE)
	private String sender;

	@Column(name = "text")
	@NotFound(action = NotFoundAction.IGNORE)
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
