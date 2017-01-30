package messages;

import gameplay.User;

public class JoinGameMessage extends Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String lobbyName;
	public User user;
	
	public JoinGameMessage(User user, String lobbyName) {
		super(MessageType.joinGame);
		this.user = user;
		this.lobbyName = lobbyName;
	}

}
