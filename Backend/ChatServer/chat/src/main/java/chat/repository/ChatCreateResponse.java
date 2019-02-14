package chat.repository;

public class ChatCreateResponse {
	private String  user, cacheId;
	private int id;

	public void updateRequest(int id, String user, String cacheId) {
		this.id = id;
		this.user = user;
		this.cacheId = cacheId;
	}

}
