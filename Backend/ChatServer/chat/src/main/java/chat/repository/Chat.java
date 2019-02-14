package chat.repository;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "chat") // this tells the complier that this class is mapped to the table called "user" in the database
public class Chat {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer chatid;
    private String user;
    private String cacheID;
    public Chat (Integer id, String user, String cacheID) {
    	this.chatid=id;
    	this.user=user;
    	this.cacheID=cacheID;
    }
	public Integer getChatid() {
		return chatid;
	}
	public void setChatid(Integer chatid) {
		this.chatid = chatid;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCacheID() {
		return cacheID;
	}
	public void setCacheID(String cacheID) {
		this.cacheID = cacheID;
	}

}
