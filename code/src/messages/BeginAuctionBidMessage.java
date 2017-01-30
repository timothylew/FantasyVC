package messages;

import gameplay.Company;

/**
 * 
 * @author alancoon
 * Updated Friday (11/18) 11:39 PM changed to Company type for input
 * parameter from String.
 */
public class BeginAuctionBidMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Company company;
	private String companyname;
	private int selected;
	
	public BeginAuctionBidMessage(Company company, String companyname, int selected) {
		super(MessageType.beginBid);
		this.company = company;
		this.companyname = companyname;
		this.selected = selected;
	} 
	
	public Company getCompany() {
		return company;
	}
	
	public String getCompanyName() {
		return companyname;
	}
	
	public int getSelected() {
		return selected;
	}
}
