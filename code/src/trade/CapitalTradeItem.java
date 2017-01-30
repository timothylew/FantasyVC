package trade;

/**
 * Used to represent capital being traded.
 * @author alancoon
 *
 */
public class CapitalTradeItem extends TradeItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CapitalTradeItem(double number) {
		super.createGUI();
		super.setLabel(number);
	}
	
}
