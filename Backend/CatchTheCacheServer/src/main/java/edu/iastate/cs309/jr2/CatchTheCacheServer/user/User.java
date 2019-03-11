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
	private String securityQuestion;

	@Column(name = "security_answer")
	@NotFound(action = NotFoundAction.EXCEPTION)
	private String securityAnswer;

	public void updateUser(UserCreateRequest req) {
		this.username = req.getUsername();
		this.password = req.getPassword();
		this.securityQuestion = req.getQuestion();
		this.securityAnswer = req.getAnswer();
		this.authority = req.getAuthority();
	}

	public Integer getId() {
		return this.id;
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
		return this.securityQuestion;
	}

	public void setSecurityQuestion(String question) {
		this.securityQuestion = question;
	}

	public String getSecurityAnswer() {
		return this.securityAnswer;
	}

	public void setSecurityAnswer(String answer) {
		this.securityAnswer = answer;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("username", this.getUsername())
				.append("authority", this.getAuthority()).toString();
	}
}
