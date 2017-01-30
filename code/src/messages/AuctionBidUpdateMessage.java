package messages;

/**
 * Send the {@code ClientExitMessage} to all {@code Client} classes 
 * connected to a game when one of the players in the game disconnects.
 * @author alancoon
 *
 */
public class AuctionBidUpdateMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String companyName;
	private double bidAmount;
	
	public AuctionBidUpdateMessage(String username, double amount) {
		super(MessageType.AuctionBidUpdate);
		this.companyName = username;
		bidAmount = amount;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public double getBidAmount(){
		return bidAmount;
	}
}
