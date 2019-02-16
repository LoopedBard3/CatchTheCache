package chat.repository;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.core.style.ToStringCreator;


@Data
@Entity
@Table(name = "chat") // this tells the complier that this class is mapped to the table called "user" in the database
public class Chat {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	
	@Column(name = "chatId")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer chatId;
	
	@Column(name = "user")
	@NotFound(action = NotFoundAction.IGNORE)
    private String user;
	
	@Column(name = "cacheId")
	@NotFound(action = NotFoundAction.IGNORE)
    private String cacheId;
	
    public Chat () {
    	
    }
    public Chat (Integer chatId, String user, String cacheId) {
    	this.chatId=chatId;
    	this.user=user;
    	this.cacheId=cacheId;
    }
	public Integer getChatId() {
		return chatId;
	}
	public void setChatid(Integer chatId) {
		this.chatId = chatId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCacheId() {
		return cacheId;
	}
	public void setCacheID(String cacheId) {
		this.cacheId = cacheId;
	}
	public void updateChat(ChatCreateRequest req) {
			this.chatId = req.getChatId();
			this.user = req.getUser();
			this.cacheId = req.getCacheId();
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("chatId", this.getChatId()).append("user", this.getUser())
				.append("cacheId", this.getCacheId()).toString();
	}

}
