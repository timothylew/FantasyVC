package messages;

import java.util.Vector;

import gameplay.User;

public class UserListMessage extends Message{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vector<User> user;
	public int waitingOn;

	public UserListMessage(Vector<User> user, int waitingOn) {
		super(MessageType.UserList); 
		this.user = user;
		this.waitingOn = waitingOn;
	}
}
