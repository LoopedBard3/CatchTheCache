package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

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
@Table(name = "caches")
public class Cache {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer id;

	@Column(name = "name")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private String name;

	@Column(name = "chat_id")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private Integer chatId;

	@Column(name = "description")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private String description;

	@Column(name = "longitude")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private double longitude;

	@Column(name = "latitude")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private double latitude;

	
	/**
	 * @param req CacheAddRequest object with the information we need to update the cache
	 */
	public void updateCache(CacheAddRequest req) {
		this.name = req.getName();
		this.longitude = req.getLongitude();
		this.latitude = req.getLatitude();
		this.description = req.getDescription();
	}

	/**
	 * @return integer id for this cache
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return String name of this cache
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return integer id for the chat associated with this cache
	 */
	public Integer getChatId() {
		return this.chatId;
	}

	/**
	 * @param chatId integer id for the chat associated with this cache
	 */
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	/**
	 * @return String description for this cache
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return double longitude value for this cache
	 */
	public double getLongitude() {
		return this.longitude;
	}

	/**
	 * @return double latitude value for this cache
	 */
	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * @return String representation for this cache object
	 */
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("name", this.getName()).append("longitude", this.getLongitude())
				.append("latitude", this.getLatitude()).append("description", this.getDescription()).toString();
	}
}
