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

	@Column(name = "user")
	@NotFound(action = NotFoundAction.IGNORE)
	private String user;

	@Column(name = "cacheId")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer cacheId;

	/**
	 * Default/empty constructor included to solve a bug
	 */
	public Chat() {

	}

	/**
	 * @param id      integer id for this chat object
	 * @param user    String list of user ids associated with this chat
	 * @param cacheId integer id of cache associated with this chat
	 */
	public Chat(int id, String user, int cacheId) {
		this.id = id;
		this.user = user;
		this.cacheId = cacheId;
	}

	/**
	 * Used to initialize chats
	 * 
	 * @param req ChatCreateRequest object to be used to update this chat
	 */
	public void updateChat(ChatCreateRequest req) {
		this.cacheId = req.getCacheId();
		this.user = req.getUser();
	}

	/**
	 * @return integer id for this chat
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return String of user ids (comma separated) associated with this chat
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param u String of user ids (comma separated) associated with this chat
	 */
	public void setUser(String u) {
		this.user = u;
	}

	/**
	 * @param id integer id for the cache associated with this chat
	 */
	public void setCacheId(int id) {
		this.cacheId = id;
	}

	/**
	 * @return integer id for the cache associated with this chat
	 */
	public Integer getCacheId() {
		return this.cacheId;
	}

	/**
	 * @return String representation for this chat object
	 */
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("chatId", this.getId()).append("user", this.getUser()).append("cacheId", this.getCacheId())
				.toString();
	}
}
