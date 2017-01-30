package gameplay;

import java.io.Serializable;
import java.util.Vector;

public class Lobby implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String lobbyName, hostName;
	private int gameSize;
	private Vector<User> users;
	
	public Lobby(String lobbyName, String hostName, int gameSize, Vector<User> users) {
		this.lobbyName = lobbyName;
		this.hostName = hostName;
		this.gameSize = gameSize;
		this.users = users;
	}

	public String getLobbyName() {
		return lobbyName;
	}

	public void setLobbyName(String lobbyName) {
		this.lobbyName = lobbyName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getGameSize() {
		return gameSize;
	}

	public void setGameSize(int gameSize) {
		this.gameSize = gameSize;
	}

	public Vector<User> getUsers() {
		return users;
	}
	

}
