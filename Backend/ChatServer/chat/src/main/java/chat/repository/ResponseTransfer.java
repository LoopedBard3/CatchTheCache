package chat.repository;

public class ResponseTransfer {
	
	    private String text;

	   public ResponseTransfer() { 
		   
	   }
	   public ResponseTransfer(String s) {
		   this.text=s;
	   }
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		} 
	   
	
}
