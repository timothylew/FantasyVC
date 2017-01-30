package utility;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class AppearanceConstants {
	
	//colors, fonts, ect that can be statically referenced by other classes	
	public static final Color darkBlue = new Color(49,59,71);
	public static final Color lightBlue = new Color(49,71,112);
	public static final Color offWhite = new Color(221, 221, 221);
	public static final Color mediumGray = new Color(100, 100, 100);
	public static final Color darkGray = new Color(31,31,31);
	public static final Color green = new Color(51, 102, 0);
	public static final Color red = new Color(178, 34, 34);
	public static final Color lightGreen = new Color(115, 220, 105);

	public static final Font fontSmall = new Font("Century Gothic", Font.PLAIN,18);
	public static final Font fontHeaderName = new Font("Century Gothic", Font.ITALIC,20);
	public static final Font fontHeaderMoney = new Font("Century Gothic", Font.ITALIC,16);
	public static final Font fontSmallest = new Font("Century Gothic", Font.PLAIN,14);
	public static final Font fontMedium = new Font("Century Gothic", Font.PLAIN, 22);
	public static final Font fontLarge = new Font("Century Gothic", Font.PLAIN, 30);
	public static final Font fontHeader = new Font("Century Gothic", Font.ITALIC, 40);
	public static final Font fontHeaderUser = new Font("Century Gothic", Font.ITALIC, 28);
	public static final Font fontButtonSmall = new Font("Century Gothic", Font.PLAIN, 14);
	public static final Font fontButtonMedium = new Font("Century Gothic", Font.PLAIN, 18);
	public static final Font fontButtonBig = new Font("Century Gothic", Font.PLAIN, 24);
	public static final Font fontSmallBidButton = new Font("Century Gothic", Font.PLAIN,12);
	public static final Font fontLargeBidButton = new Font("Century Gothic", Font.PLAIN,24);
	public static final Font fontBidAmount = new Font("Century Gothic", Font.PLAIN, 18);
	public static final Font fontFirmName = new Font("Century Gothic", Font.PLAIN, 20);
	public static final Font fontTimerMedium = new Font("Century Gothic", Font.PLAIN, 48);
	public static final Font fontTimerLarge = new Font("Century Gothic", Font.PLAIN, 60);
	public static final Font fontLobby = new Font("Century Gothic", Font.ITALIC | Font.BOLD, 24);
	public static final Font fontLobby2 = new Font("Century Gothic", Font.BOLD, 24);
	public static final Font fontTradeItem = new Font("Century Gothic", Font.BOLD, 22);
	public static final Font fontTradeCapital = new Font("Century Gothic", Font.ITALIC, 22);
	
	//added a blue border variable used in StartWindowGUI
	public static final Border blueLineBorder = BorderFactory.createLineBorder(darkBlue);
}
