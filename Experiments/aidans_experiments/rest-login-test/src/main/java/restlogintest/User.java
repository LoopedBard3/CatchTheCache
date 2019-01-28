package restlogintest;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
class User {

	private @Id @GeneratedValue Long id;
	private String username;
	private String passwd;

	User(String username, String passwd) {
		this.username = username;
		this.passwd = passwd;
	}
	
	User(){
		this.username = "admin";
		this.passwd = "admin";
	}
}