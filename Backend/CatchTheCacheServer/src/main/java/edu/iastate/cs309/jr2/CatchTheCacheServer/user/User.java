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

	/**
	 * Initialize user object based on creation request
	 * 
	 * @param req UserCreateRequest object to be used to initialize this user object
	 */
	public void updateUser(UserCreateRequest req) {
		this.username = req.getUsername();
		this.password = req.getPassword();
		this.securityQuestion = req.getQuestion();
		this.securityAnswer = req.getAnswer();
		this.authority = req.getAuthority();
	}

	/**
	 * @return integer id for this user object
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return String username for this user
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param username String username for this user
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return String password for this user
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password String password for this user
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return integer authority level for this user
	 */
	public int getAuthority() {
		return this.authority;
	}

	/**
	 * @param authority integer authority level for this user
	 */
	public void setAuthority(int authority) {
		this.authority = authority;
	}

	/**
	 * @return String security question for this user
	 */
	public String getSecurityQuestion() {
		return this.securityQuestion;
	}

	/**
	 * @param question String security question for this user
	 */
	public void setSecurityQuestion(String question) {
		this.securityQuestion = question;
	}

	/**
	 * @return String security answer for this user
	 */
	public String getSecurityAnswer() {
		return this.securityAnswer;
	}

	/**
	 * @param answer String security answer for this user
	 */
	public void setSecurityAnswer(String answer) {
		this.securityAnswer = answer;
	}

	/**
	 * @return String representation for this user object
	 */
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("username", this.getUsername())
				.append("authority", this.getAuthority()).toString();
	}
}
