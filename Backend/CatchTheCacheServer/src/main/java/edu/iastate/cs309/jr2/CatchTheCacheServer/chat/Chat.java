package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.List;

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
import edu.iastate.cs309.jr2.CatchTheCacheServer.user.User;

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
	private List<User> users;

	//Implementing permissions as Integer right now, could be changed
	@Column(name = "permissions")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<Integer> permissions;
	
	@Column(name = "bannedUsers")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<User> bannedUsers;
	
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

	public List<User> getUsers() {
		return this.users;
	}

	public void setUser(List<User> u) {
		this.users = u;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Integer> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Integer> permissions) {
		this.permissions = permissions;
	}

	public List<User> getBannedUsers() {
		return bannedUsers;
	}

	public void setBannedUsers(List<User> bannedUsers) {
		this.bannedUsers = bannedUsers;
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

				.append("chatId", this.getId()).append(", users", this.getUsers().toString()).append(", permissions",getPermissions()).append(", bannedUsers", getBannedUsers()).append(", cacheId", this.getCacheId())
				.toString();
	}
}
