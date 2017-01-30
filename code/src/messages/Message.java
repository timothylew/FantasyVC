package messages;

import java.io.Serializable;

/**
 * 
 * @author alancoon
 *
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static enum MessageType {addUser, startGame, beginAuction, endGame, clientExit,
		beginQuarterly, beginTimelapse, chatMessage, quarterlyReady, initiateTrade, login, createGame, userInfo, beginBid, AuctionDetailsUpdateUser,
		AuctionDetailsUpdateCompany, acceptTrade, declineTrade, lobbyPlayerReady, leaveLobby, hostGame, joinGame, createAccount, AuctionBidUpdate, LobbyList, UserList};

	
	private MessageType type;
	
	public Message(MessageType type){
		this.type = type;
	}
	
	public MessageType getType(){
		return type;
	}
	
}
