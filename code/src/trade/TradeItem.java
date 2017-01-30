package trade;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.Serializable;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gameplay.Company;
import utility.AppearanceConstants;

/**
 * The abstract parent of all items that can be traded ({@code Company} class
 * and simple cash transfers).
 * @author alancoon
 *
 */
public abstract class TradeItem extends JPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label;

	
	protected void createGUI() {
		label = new JLabel();
		
		setBackground(AppearanceConstants.mediumGray);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(75, 25));
		label.setForeground(AppearanceConstants.offWhite);
		label.setFont(AppearanceConstants.fontTradeItem);
	}
	
	protected void setLabel(double number) { 
		DecimalFormat df = new DecimalFormat("#.##");
		label.setFont(AppearanceConstants.fontTradeCapital);
		label.setForeground(AppearanceConstants.lightGreen);
		label.setText("$" + df.format(number) + "M");
		
		add(label, BorderLayout.EAST);
	}
	
	protected void setLabel(Company company) { 
		JLabel stats = new JLabel();
		
		String name = company.getName();
		int tierLevel = company.getTierLevel(); 
		double value = company.getCurrentWorth();
		DecimalFormat df = new DecimalFormat("#.##");
		
		label.setText(name);
		stats.setText("(Tier: " + tierLevel + ", $" + df.format(value) + "M)");
		
		label.setFont(AppearanceConstants.fontTradeItem);
		stats.setFont(AppearanceConstants.fontTradeCapital);
		
		stats.setForeground(AppearanceConstants.mediumGray);
		
		add(label, BorderLayout.WEST);
		add(label, BorderLayout.EAST);
	}
}
