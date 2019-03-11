package chat.repository;

import org.springframework.core.style.ToStringCreator;

public class ChatCreateRequest {
	private String user, cacheId;

	public void updateRequest(String user, String cacheId) {

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
	
	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("user", this.getUser())
				.append("cacheId", this.getCacheId()).toString();
	}

}
