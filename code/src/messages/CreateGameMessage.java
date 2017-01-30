package messages;

import gameplay.User;

public class CreateGameMessage extends Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String gamename;
	public User hostUser;
	public int numUsers;
	public CreateGameMessage(String gamename, int numUsers, User user) {
		super(MessageType.createGame);
		this.gamename = gamename;
		this.numUsers = numUsers;
		this.hostUser = user;
	}
}
