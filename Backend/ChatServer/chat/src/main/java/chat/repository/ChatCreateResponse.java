package chat.repository;

public class ChatCreateResponse {
	private boolean success, validChatId, validCacheId;
	private String message;

	public ChatCreateResponse() {
		this.validChatId = false;
		this.validCacheId = false;
	}

	public void setSuccess(boolean b) {
		this.success = b;
	}

	public boolean isValidChatId() {
		return validChatId;
	}

	public void setValidChatId(boolean validChatId) {
		this.validChatId = validChatId;
	}

	public boolean isValidCacheId() {
		return validCacheId;
	}
	
	public void setValidCacheId(boolean validCacheId) {
		this.validCacheId = validCacheId;
	}

	public void setMessage(String m) {
		this.message = m;
	}

	public boolean getValidChatId() {
		return this.validChatId;
	}

	public boolean getValidCacheId() {
		return this.validCacheId;
	}

	public String getMessage() {
		return this.message;
	}

	public boolean getSuccess() {
		return this.success;
	}

	

	

}
