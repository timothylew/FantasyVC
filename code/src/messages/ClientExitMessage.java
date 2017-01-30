package messages;

/**
 * Send the {@code ClientExitMessage} to all {@code Client} classes 
 * connected to a game when one of the players in the game disconnects.
 * @author alancoon
 *
 */
public class ClientExitMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	
	public ClientExitMessage(String username) {
		super(MessageType.clientExit);
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
}
