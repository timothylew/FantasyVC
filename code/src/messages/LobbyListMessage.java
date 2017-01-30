package messages;

import java.util.Vector;

import gameplay.Lobby;

public class LobbyListMessage extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vector<Lobby> lobbies;
	
	public LobbyListMessage(Vector<Lobby> lobbies) {
		super(MessageType.LobbyList);
		this.lobbies = lobbies;
	}

}
