package trade;

import gameplay.Company;

/**
 * Used to represent a {@code Company} being
 * traded.
 * @author alancoon
 *
 */
public class CompanyTradeItem extends TradeItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public CompanyTradeItem(Company company) { 
		super.createGUI();
		super.setLabel(company);
	}
}
