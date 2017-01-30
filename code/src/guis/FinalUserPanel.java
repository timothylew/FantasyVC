package guis;

import java.awt.BorderLayout;
import java.awt.Image;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gameplay.Company;
import gameplay.User;
import utility.AppearanceConstants;
import utility.AppearanceSettings;
import utility.ImageLibrary;

/**
 * A listing for each {@code User} to be shown on the
 * {@code FinalGUI} panel.
 * @author alancoon
 *
 */
public class FinalUserPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private double delta;
	
	private JLabel avatarLabel;
	private JLabel usernameLabel;
	private JLabel firmnameLabel;
	private JLabel profitLabel;
	private JLabel deltaLabel;
	private JLabel bioLabel;

	public FinalUserPanel(User user) {
		this.user = user;
		initializeComponents();
		createGUI();
	}


	private void createGUI() {
		setSize(600, 200);
		setBackground(AppearanceConstants.mediumGray);
		setLayout(new BorderLayout());
		JPanel northPanel = new JPanel(new BorderLayout());
		JPanel centerPanel = new JPanel(new BorderLayout());
		JPanel southPanel = new JPanel(new BorderLayout());
		
		northPanel.add(avatarLabel, BorderLayout.WEST);
		northPanel.add(usernameLabel, BorderLayout.EAST);
		JPanel centerEastPanel = new JPanel(new BorderLayout());
		JLabel endingCapital = new JLabel("Ending Capital: ");
		centerEastPanel.add(endingCapital, BorderLayout.WEST);
		centerEastPanel.add(profitLabel, BorderLayout.CENTER);
		centerEastPanel.add(deltaLabel, BorderLayout.EAST);
		
		centerPanel.add(firmnameLabel, BorderLayout.WEST);
		centerPanel.add(centerEastPanel, BorderLayout.EAST);
		
		JLabel bio = new JLabel("User Bio");
		southPanel.add(bio, BorderLayout.NORTH);
		southPanel.add(bioLabel, BorderLayout.CENTER);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		AppearanceSettings.setFont(AppearanceConstants.fontHeaderUser, usernameLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontFirmName, firmnameLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, endingCapital, profitLabel, deltaLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, bio, bioLabel);
		AppearanceSettings.setBackground(AppearanceConstants.mediumGray, northPanel, centerEastPanel, centerPanel, southPanel);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, endingCapital, bio, usernameLabel, firmnameLabel, bioLabel);
		
		/* If the person ends up with a positive profit, make the numbers green, else make it red. */
		DecimalFormat df = new DecimalFormat("#.##");
		System.out.println("final game delta " + df.format(delta) +  "     "  + delta);
		if (delta >= 0.00) {
			AppearanceSettings.setForeground(AppearanceConstants.lightGreen, profitLabel, deltaLabel);
		} else {
			AppearanceSettings.setForeground(AppearanceConstants.red, profitLabel, deltaLabel);
		}
	}


	private void initializeComponents() {
		Image image = ImageLibrary.getImage(user.getUserIconString());
		avatarLabel = new JLabel(new ImageIcon(image.getScaledInstance(125, 125, Image.SCALE_SMOOTH)));
		avatarLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		usernameLabel = new JLabel(user.getUsername());
		firmnameLabel = new JLabel(user.getCompanyName());
		
		DecimalFormat df = new DecimalFormat("#.##");
		double currentCapital = user.getCurrentCapital();
		for (Company c : user.getCompanies()) {
			currentCapital += c.getCurrentWorth();
		}
		
		delta = 100 * (user.getCurrentCapital() - user.getStartingCapital()) / user.getStartingCapital();
		profitLabel = new JLabel("$" + df.format(currentCapital) + "M");
		deltaLabel = new JLabel("(" + df.format(delta) + "% from start.)");
		bioLabel = new JLabel(user.getUserBio());
	}
}
