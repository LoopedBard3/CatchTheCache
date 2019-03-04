package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.core.style.ToStringCreator;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;

@Entity
@Table(name = "chats")
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer id;

	//List of users participating in this specific chat
	@Column(name = "users")
	@NotFound(action = NotFoundAction.IGNORE)
	private String users;

	
	@Column(name = "cacheId")
	@NotFound(action = NotFoundAction.IGNORE)
	private String cacheId;

	public void updateChat(ChatCreateRequest req) {
		this.cacheId = req.getCacheId();
		this.users = req.getUsers();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsers() {
		return this.users;
	}

	public void setUser(String u) {
		this.users = u;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	

	public void setCacheId(String id) {
		this.cacheId = id;
	}

	public String getCacheId() {
		return this.cacheId;
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("chatId", this.getId()).append(", users", this.getUsers().toString()).append(", cacheId", this.getCacheId())
				.toString();
	}
}
