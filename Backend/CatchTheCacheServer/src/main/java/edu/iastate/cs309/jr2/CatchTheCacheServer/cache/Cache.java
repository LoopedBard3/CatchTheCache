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

	@Column(name = "longitude")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private double longitude;

	@Column(name = "latitude")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private double latitude;

	public void updateCache(CacheAddRequest req) {
		this.name = req.getName();
		this.longitude = req.getLongitude();
		this.latitude = req.getLatitude();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("name", this.getName()).append("longitude", this.getLongitude())
				.append("latitude", this.getLatitude()).toString();
	}
}
