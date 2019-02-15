package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

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
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer id;

	@Column(name = "username")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private String username;

	@Column(name = "password")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private String password;

	@Column(name = "authority")
	@NotFound(action = NotFoundAction.IGNORE)
	private int authority;

	@Column(name = "security_question")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private String security_question;

	@Column(name = "security_answer")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private String security_answer;

	public void updateUser(UserCreateRequest req) {
		this.username = req.getUsername();
		this.password = req.getPassword();
		this.security_question = req.getQuestion();
		this.security_answer = req.getAnswer();
		this.authority = req.getAuthority();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAuthority() {
		return this.authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	public String getSecurityQuestion() {
		return this.security_question;
	}

	public void setSecurityQuestion(String question) {
		this.security_question = question;
	}

	public String getSecurityAnswer() {
		return this.security_answer;
	}

	public void setSecurityAnswer(String answer) {
		this.security_answer = answer;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("username", this.getUsername())
				.append("authority", this.getAuthority()).toString();
	}
}
