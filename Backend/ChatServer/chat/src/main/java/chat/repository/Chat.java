package chat.repository;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Data
@Entity
public class Chat {
	@Id
    @GeneratedValue
    protected Integer chatid;
    protected String user;
    protected String cacheID;
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
