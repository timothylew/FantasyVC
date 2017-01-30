package messages;

import java.io.Serializable;

import gameplay.Company;

public class BuyMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private Company company;
	private int rowSelected;
	
	public BuyMessage(String username, Company company, int rowSelected) {
		this.username = username;
		this.company = company;
		this.rowSelected = rowSelected;
	}

	public String getUsername() {
		return username;
	}

	public Company getCompany() {
		return company;
	}

	public int getRowSelected() {
		return rowSelected;
	}

}
