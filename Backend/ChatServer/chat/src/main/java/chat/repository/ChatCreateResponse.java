package chat.repository;

public class ChatCreateResponse {
	private boolean success, validId, validCacheId;
	private String message;

	public ChatCreateResponse() {
		this.validId = false;
		this.validCacheId = false;
	}

	public void setSuccess(boolean b) {
		this.success = b;
	}

	public boolean isValidId() {
		return validId;
	}

	public void setValidId(boolean validId) {
		this.validId = validId;
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

	public String getMessage() {
		return this.message;
	}

	public boolean getSuccess() {
		return this.success;
	}

	

	

}
