package messages;

/**
 * 
 * @author alancoon
 * 
 */
public class ChatMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String message;

	public ChatMessage(String username, String message) {
		super(MessageType.chatMessage);
		this.username = username;
		this.message = message;
	}
	
	public String getUsername() { 
		return username;
	}
	
	public String getMessage() { 
		return message;
	}

}
