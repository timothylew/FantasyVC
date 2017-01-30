package guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import client.Client;
import gameplay.GameFrame;
import gameplay.User;
import utility.AppearanceConstants;
import utility.AppearanceSettings;
import utility.Constants;
import utility.ImageLibrary;

/**
 * The {@code TopPanel} is a {@code JPanel} that borders the top of the screen 
 * @author alancoon
 *	TODO: Needs to port images from FTP server to the user Icon
 *	
 *	Danny's Changes
 *		- Cleaned up GUI Code
 *		- Added JButton as User Icon
 *		- Added addedAction listener functions 
 */
public class TopPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private JLabel title, fantasyVCLogo;
	private JLabel username, currentCapital;
	private JButton userIcon;
	private User user;
	private GameFrame gameFrame;
	public boolean activeIcon;
	private UserInfoGUI ugui;
		
	/**
	 * Multiplayer.
	 * @param client
	 */
	public TopPanel(GameFrame gameFrame, Client client) {
		this.user = client.getUser();
		this.gameFrame = gameFrame;
		activeIcon = true;
		initializeComponents(client.getUser());
		createGUI();
		addActionListeners();

		ugui = new UserInfoGUI(gameFrame);
	}
	
	/**
	 * Single player.
	 * @param guest
	 */
	public TopPanel(GameFrame gameFrame, User guest) {
		this.gameFrame = gameFrame;
		initializeComponents(guest);
		createGUI();
		addActionListeners();
		user = guest;

		ugui = new UserInfoGUI(gameFrame);
	}
	
	public JButton getIconButton() {
		return userIcon;
	}

	private void initializeComponents(User user) {
		
		/*	TODO
		 *  Attempt to grab the user's avatar filepath and create a BufferedImage using that.  Then use the Buffered Image
		 * to create an ImageIcon, which can be placed in a JLabel, which we can use in our GUI. */
		/*
		 * We'll do this later when we have the server setup
		try {
		    avatar = ImageIO.read(this.getClass().getResource(user.getImage()));
			avatarLabel = new JLabel(new ImageIcon(avatar));
		} catch (IOException ioe) {
			System.out.println("IOException in TopPanel.initializeComponents(): " + ioe.getLocalizedMessage());
			ioe.printStackTrace();
		}
		*/		
		//Logo here
		currentCapital = new JLabel();
		currentCapital.setForeground(AppearanceConstants.offWhite);
		currentCapital.setFont(AppearanceConstants.fontHeaderMoney);
		//AppearanceSettings.setCenterAlignment(currentCapital);
		
		fantasyVCLogo = new JLabel();
		fantasyVCLogo.setBorder(new EmptyBorder(0,5,0,0));
		Image logoIcon = ImageLibrary.getImage("http://jeffreychen.space/fantasyvc/FantasyVC.png");
		fantasyVCLogo.setIcon(new ImageIcon(logoIcon.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));

		//userIcon here
		userIcon = new JButton();
		userIcon.setOpaque(true);
		userIcon.setFocusable(false);
		AppearanceSettings.unSetBorderOnButtons(userIcon);
		userIcon.setBackground(AppearanceConstants.darkBlue);
		userIcon.setVerticalAlignment(SwingConstants.CENTER);
		userIcon.setIcon(new ImageIcon(gameFrame.user.getUserIcon().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
		
		//Username formatting
		username = new JLabel(user.getUsername());
		username.setBorder(new EmptyBorder(5,5,5,5));
		username.setFont(AppearanceConstants.fontHeaderName);
		//username.setHorizontalAlignment(SwingConstants.CENTER);
		username.setForeground(AppearanceConstants.offWhite);
		
		//Banner Formatting
		title = new JLabel("Fantasy VC");
		title.setFont(AppearanceConstants.fontHeader);
		title.setBorder(new EmptyBorder(5,5,5,5));
		title.setForeground(AppearanceConstants.offWhite);
		title.setVerticalAlignment(SwingConstants.CENTER);
	}

	private void createGUI() {
		
		//Looks good
		setPreferredSize(new Dimension(1280, 72));
		//Just have to upper bounded
		setMaximumSize(new Dimension(1280, 72));  // May cause problems
		setBackground(AppearanceConstants.darkBlue);
		setLayout(new BorderLayout());
		
		JPanel name = new JPanel();
		name.setBackground(AppearanceConstants.darkBlue);
		name.setLayout(new BorderLayout());
		name.add(username, BorderLayout.EAST);
		
		//Hold both Namae and Capital;
		JPanel nameAndCapital = new JPanel();
		nameAndCapital.setLayout(new BoxLayout(nameAndCapital, BoxLayout.PAGE_AXIS));
		nameAndCapital.setBackground(AppearanceConstants.darkBlue);
		nameAndCapital.add(name);
		nameAndCapital.add(currentCapital);
		
		//Sub pane just need flow layout
		JPanel rightPane = new JPanel();
		rightPane.setBackground(AppearanceConstants.darkBlue);
		rightPane.add(nameAndCapital);
		rightPane.add(userIcon);
		
		JPanel leftPane = new JPanel();
		leftPane.setBackground(AppearanceConstants.darkBlue);
		leftPane.add(fantasyVCLogo);
		leftPane.add(title);

		//Adding everything at once
		add(leftPane, BorderLayout.WEST);
		add(rightPane, BorderLayout.EAST);
	}
	
	private void addActionListeners(){
		userIcon.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!gameFrame.gameInProgress && activeIcon) {
					ugui.setVisible(true);
				}
			}
		});
	}
	
	public void updateCurrentCapital(){
		currentCapital.setText(Constants.currentCapital + String.format("%.2f", gameFrame.user.getCurrentCapital()) + Constants.million);
	}
	public void updateIcon(){
		userIcon.setIcon(new ImageIcon(user.getUserIcon().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
	}
	public void hideCurrentCapital(){
		currentCapital.setText("");
	}
}
