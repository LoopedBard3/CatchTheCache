package chat.repository;

public class ChatCreateRequest {
	private String user, cacheId;
	private int id;

	public void updateRequest(int id, String user, String cacheId) {
		this.id = id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
