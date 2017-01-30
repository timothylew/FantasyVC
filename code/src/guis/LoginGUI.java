package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import client.Client;
import gameplay.GameFrame;
import gameplay.User;
import listeners.PasswordFocusListener;
import listeners.TextFieldFocusListener;
import server.SQLDriver;
import utility.AppearanceConstants;
import utility.AppearanceSettings;
import utility.ImageLibrary;

/**
 * The {@code LoginGUI} is the first graphical user interface 
 * that the player sees when starting the game. From here,
 * he or she can login using an existing account, create a new
 * account, or continue as a guest. We utilize MySQL databases
 * to store the user data.
 * 
 * @author alancoon
 *
 */
public class LoginGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton loginButton;
	private JButton createAccount;
	private JButton guestButton;
	private JTextField username;
	private JPasswordField passwordField;
	private JLabel alertLabel;
	private SQLDriver driver;


	public LoginGUI() {
		super("Fantasy Venture Capital");
		//initializeConnection();
		initializeComponents();
		createGUI();
		addListeners();
		driver = new SQLDriver();
		driver.connect();
	}
	
	private void initializeComponents() {

		loginButton = new JButton("Login");
		createAccount = new JButton("Create Account");
		guestButton = new JButton("Continue as Guest");
		username = new JTextField("Username");
		passwordField = new JPasswordField();
		alertLabel = new JLabel();
		alertLabel.setForeground(Color.red);
	}

	private void createGUI() {

		JPanel mainPanel = new JPanel();
		JPanel textFieldOnePanel = new JPanel();
		JPanel textFieldTwoPanel = new JPanel();
		JLabel welcome = new JLabel("Login or create an account to play.", JLabel.CENTER);
		JLabel fantasyVCLogo = new JLabel();
		JLabel ventureLabel = new JLabel("Fantasy VC", JLabel.CENTER);
		JPanel alertPanel = new JPanel();
		JPanel textFieldsPanel = new JPanel();
		JPanel buttonsPanel = new JPanel(new BoxLayout(this, BoxLayout.LINE_AXIS));
		JPanel welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.PAGE_AXIS));

		//add Logo image
		Image logoIcon = ImageLibrary.getImage("http://jeffreychen.space/fantasyvc/FantasyVC.png");
		fantasyVCLogo.setIcon(new ImageIcon(logoIcon.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)));

		
		// Set mass component appearances
		// TODO replace with generalized variables from AppearanceConstants
		Color buttonForeground = AppearanceConstants.offWhite;
		Color buttonBackground = AppearanceConstants.mediumGray; 
		Color primaryBackground = AppearanceConstants.lightBlue;

		AppearanceSettings.setForeground(buttonForeground, createAccount, loginButton, guestButton, passwordField, username);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, alertLabel, welcome, ventureLabel);
		AppearanceSettings.setSize(300, 60, passwordField, username);

		AppearanceSettings.setSize(200, 80, loginButton, createAccount);
		AppearanceSettings.setSize(200, 80, guestButton);
		AppearanceSettings.setBackground(buttonBackground, loginButton, createAccount, guestButton);

		AppearanceSettings.setOpaque(loginButton, createAccount, guestButton);
		AppearanceSettings.unSetBorderOnButtons(loginButton, createAccount, guestButton);

		AppearanceSettings.setTextAlignment(welcome, alertLabel, ventureLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontButtonMedium, passwordField, alertLabel, username, loginButton, createAccount, guestButton);

		AppearanceSettings.setBackground(primaryBackground, mainPanel, welcome, alertLabel, ventureLabel, alertPanel, textFieldsPanel, 
				buttonsPanel, welcomePanel, textFieldOnePanel, textFieldTwoPanel);
		// Other appearance settings
		welcome.setFont(AppearanceConstants.fontMedium);
		ventureLabel.setFont(AppearanceConstants.fontHeader);

		loginButton.setEnabled(false);
		createAccount.setEnabled(false);

		// Add components to containers
		AppearanceSettings.setCenterAlignment(fantasyVCLogo, ventureLabel, welcome);
		//Removed the welcome
		AppearanceSettings.addGlue(welcomePanel, BoxLayout.PAGE_AXIS, true, fantasyVCLogo, ventureLabel);

		alertPanel.add(alertLabel);
		textFieldOnePanel.add(username);
//		textFieldTwoPanel.add(password);
		textFieldTwoPanel.add(passwordField);

		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		// Adds the three buttons 
		buttonsPanel.add(new JPanel().add(loginButton));
		buttonsPanel.add(Box.createHorizontalStrut(10));
		buttonsPanel.add(new JPanel().add(createAccount));
		buttonsPanel.add(Box.createHorizontalStrut(10));
		buttonsPanel.add(new JPanel().add(guestButton));
		
		//AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, false, welcomePanel);
		// Don't want glue in between the following two panels, so they are not passed in to addGlue
		mainPanel.add(welcomePanel);
		mainPanel.add(alertPanel);
		//mainPanel.add(textFieldOnePanel);
		AppearanceSettings.addGlue(mainPanel, BoxLayout.PAGE_AXIS, true, textFieldOnePanel, textFieldTwoPanel);
		mainPanel.add(buttonsPanel);
		

		add(mainPanel, BorderLayout.CENTER);
		setSize(600, 600);
		setMinimumSize(new Dimension(490, 300));

	}

	/**
	 * Returns whether the buttons should be enabled.
	 * @return Whether the buttons should be enabled or not.
	 */
	
//	@SuppressWarnings("deprecation")
	private boolean canPressButtons() {
//		return (!username.getText().isEmpty() && !username.getText().equalsIgnoreCase("Username") && 
//				!password.getText().equalsIgnoreCase("Password") && !password.getText().isEmpty());
		return (!username.getText().isEmpty() && !username.getText().equalsIgnoreCase("Username") &&
				!String.valueOf(passwordField.getPassword()).equalsIgnoreCase("Password") && 
				!String.valueOf(passwordField.getPassword()).isEmpty());
		
	}

	/**
	 * Adds action listeners to the GUI components.
	 */
	private void addListeners() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// Focus listeners
		username.addFocusListener(new TextFieldFocusListener("Username", username));
		passwordField.addFocusListener(new PasswordFocusListener("Password", passwordField));
		// Document listeners
		username.getDocument().addDocumentListener(new LoginDocumentListener());
		passwordField.getDocument().addDocumentListener(new LoginDocumentListener());
		// Action listeners
		loginButton.addActionListener(new LoginActionListener());
		createAccount.addActionListener(new CreateActionListener());
		guestButton.addActionListener(new GuestActionListener());
		
		passwordField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!String.valueOf(passwordField.getPassword()).trim().equals("") && !username.getText().trim().equals("")) {
						if(!driver.userExists(username.getText().trim())) {
							alertLabel.setText("That username does not exist.");
						}
						else {
							if (driver.checkPassword(username.getText(), String.valueOf(passwordField.getPassword()))) {
								new Client(driver.getUser(username.getText().trim())).start();
								dispose();
							}
							else {
								alertLabel.setText("Incorrect password.");
							}
						}
					}
				}
			}
		});
	}

	/**
	 * The {@code LoginDocumentListener} implements the {@code DocumentListener} 
	 * to listen for input in the text fields to set the buttons on the GUI either
	 * enabled or disabled.
	 * @author alancoon
	 * @see javax.swing.event.DocumentListener
	 */
	private class LoginDocumentListener implements DocumentListener {

		@Override
		public void insertUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			createAccount.setEnabled(canPressButtons());
			loginButton.setEnabled(canPressButtons());
		}
	}

	/**
	 * The {@code LoginActionListener} is the subroutine that executes
	 * when the login button is pressed.
	 * @author alancoon
	 *
	 */
	private class LoginActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(!driver.userExists(username.getText().trim())) {
				alertLabel.setText("That username does not exist.");
			}
			else {
				if (driver.checkPassword(username.getText().trim(), String.valueOf(passwordField.getPassword()).trim())) {
					new Client(driver.getUser(username.getText().trim())).start();
					dispose();
				}
				else {
					alertLabel.setText("Incorrect password.");
				}
			}
		}
	}
	
	/**
	 * The {@code CreateActionListener} is the subroutine that
	 * executes when the create account button is pressed.
	 * @author alancoon
	 *
	 */
	private class CreateActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(driver.userExists(username.getText().trim())) {
				alertLabel.setText("Username already exists.");
			}
			else {
				driver.insertUser(username.getText().trim(), String.valueOf(passwordField.getPassword()).trim(), "Fill in biography here.");
				new Client(driver.getUser(username.getText().trim())).start();
				dispose();
			}
		}
	}
	
	private class GuestActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			/* Guest users will have an ID of -1. */
			User guest = new User(-1, "Guest User", "null", "Guest User", 0, 0, 0);
			guest.setCompanyName("Guest Team");
			new GameFrame(guest).setVisible(true);
			dispose();
		}
	}
}

