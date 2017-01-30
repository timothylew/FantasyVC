package messages;

public class LobbyPlayerReadyMessage extends Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String teamName, username;
	

	public LobbyPlayerReadyMessage(String username, String teamName) {
		super(MessageType.lobbyPlayerReady);
		this.username = username;
		this.teamName = teamName;
	}

	public String getTeamName() {
		return teamName;
	}


	public String getUsername() {
		return username;
	}
}
