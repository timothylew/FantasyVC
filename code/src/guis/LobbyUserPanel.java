package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import utility.AppearanceConstants;
import utility.AppearanceSettings;

public class LobbyUserPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private String firmName;

	private JLabel usernameLabel, firmNameLabel, ready;
	
	public LobbyUserPanel(String username){
		super();
		this.username = username;
		this.firmName = "";
		initializeVariables();
		createGUI();
	}
	
	public void initializeVariables(){
		usernameLabel = new JLabel(username);
		firmNameLabel = new JLabel("", SwingConstants.CENTER);
		ready = new JLabel("Not Ready");
	}
	
	public void createGUI(){
		this.setSize(new Dimension(700,60));
		this.setLayout(new BorderLayout());
		this.setBackground(AppearanceConstants.darkBlue);
		
		JPanel line = new JPanel();
		line.setBackground(AppearanceConstants.mediumGray);
		line.setLayout(new BorderLayout());
		AppearanceSettings.setFont(AppearanceConstants.fontLobby, usernameLabel, firmNameLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontLobby2, ready);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, usernameLabel, firmNameLabel);
		ready.setForeground(Color.red);
		setBorder(new LineBorder(AppearanceConstants.mediumGray, 15));
		
		add(Box.createVerticalStrut(5));
		line.add(usernameLabel, BorderLayout.WEST);
		line.add(firmNameLabel, BorderLayout.CENTER);
		line.add(ready, BorderLayout.EAST);
		add(line);
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
		firmNameLabel.setText(firmName);
	}
	
	public void setReady(){
		ready.setText("Ready");
		ready.setForeground(AppearanceConstants.lightGreen);
	}
	
	public void unReady(){
		ready.setText("Not Ready");
		ready.setForeground(AppearanceConstants.red);
	}
	
	public String getUsername() {
		return username;
	}
}
