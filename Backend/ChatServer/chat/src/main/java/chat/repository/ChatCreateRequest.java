package chat.repository;

public class ChatCreateRequest {
	private String user, cacheId;
	private int chatId;

	public void updateRequest(int chatId, String user, String cacheId) {
		this.chatId = chatId;
		this.user = user;
		this.cacheId = cacheId;
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

	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}

	public int getChatId() {
		return chatId;
	}

	public void setId(int chatId) {
		this.chatId = chatId;
	}

}
