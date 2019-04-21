package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.Scanner;

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

	@Column(name = "user")
	@NotFound(action = NotFoundAction.IGNORE)
	private String user;

	@Column(name = "cacheId")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer cacheId;
	public Chat() {
		
	}
	public Chat(int id, String user,int cacheId) {
		this.id=id;
		this.user=user;
		this.cacheId=cacheId;
	}
	public void updateChat(ChatCreateRequest req) {
		this.cacheId = req.getCacheId();
		this.user = req.getUser();
	}

	public Integer getId() {
		return id;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String u) {
		this.user = u;
	}

	public void setCacheId(int id) {
		this.cacheId = id;
	}

	public Integer getCacheId() {
		return this.cacheId;
	}
	/**
	 *  * Function to add new user to chat
	 * @param newUser
	 * @return True if success, false if fail
	 */
	 
	public boolean addUser(User newUser) {
		if(!hasUser(newUser)) {
			this.user= this.user +", " + newUser.getId();
			return true;
		}
		else
			return false;
	}

	/**
	 * Function to remove user from chat
	 * @param user
	 */
	public boolean removeUser(User user) {
		if(!hasUser(user)) {
			this.user= this.user +", " + user.getId();
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Function to check whether user exists in this chat
	 * @param user
	 * @return true if user is in this chat, false if not
	 */
	public boolean hasUser(User user) {
		Scanner s = new Scanner(this.user);
		int id = user.getId();
		while(s.hasNextInt())
		{
			int foundId = s.nextInt();
			if(foundId==id)
				{
					s.close();
					return true;
				}
		}
		s.close();
		return false;
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("chatId", this.getId()).append("user", this.getUser()).append("cacheId", this.getCacheId())
				.toString();
	}
}
