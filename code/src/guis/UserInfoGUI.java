package guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import gameplay.GameFrame;
import server.SQLDriver;
import utility.AppearanceConstants;
import utility.AppearanceSettings;
import utility.ImageLibrary;

public class UserInfoGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton userIcon, cancel, save;
	private JLabel username;
	private JTextArea userBio;
	private GameFrame gameFrame;
	private String imagePath;
	private Image image;
	
	public UserInfoGUI(GameFrame gameFrame){
		super("User Information");
		this.gameFrame = gameFrame;
		imagePath = gameFrame.user.getUserIconString();
		image = ImageLibrary.getImage(imagePath);
		intializeVariables();
		createGUI();
		addActionListeners();
		toFront();
		this.setResizable(false);
	}
	
	private void intializeVariables(){		
		//Commented out portion is the real code
		userIcon = new JButton();
		userIcon.setOpaque(true);
		userIcon.setIcon(new ImageIcon(gameFrame.user.getUserIcon().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		AppearanceSettings.unSetBorderOnButtons(userIcon);
		
		//testCode
		username = new JLabel(gameFrame.user.getUsername());
		userBio = new JTextArea(gameFrame.user.getUserBio());
		userBio.setWrapStyleWord(true);
		userBio.setLineWrap(true);
		
		cancel = new JButton("Cancel");
		cancel.setOpaque(true);

		save = new JButton("Save");
		save.setOpaque(true);

	}
	
	private void createGUI(){
		setSize(400,600);
		setLocationRelativeTo(null);
		
		JPanel framePanel = new JPanel();
		JPanel userBioTextPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel userNameLabel = new JLabel("Username");
		JLabel userBioLabel = new JLabel("Bio");

		//Layouts
		framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		userBioTextPanel.setLayout(new BorderLayout());

		//BorderPadding
		userBio.setBorder(new EmptyBorder(5,5,5,5));
		userNameLabel.setBorder(new EmptyBorder(5,5,5,5));
		userBioLabel.setBorder(new EmptyBorder(5,5,5,5));

		//Sizes
		AppearanceSettings.setSize(300, 150, userBioTextPanel);
		userBioTextPanel.setMaximumSize(new Dimension(300,150));

		//appearance settings
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, userBioTextPanel, buttonPanel, framePanel, userIcon);
		AppearanceSettings.setBackground(AppearanceConstants.offWhite, username, userBio);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, cancel, save);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, cancel, save, userNameLabel,
				userBioLabel, username);
		AppearanceSettings.setForeground(AppearanceConstants.darkBlue, userBio);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, userNameLabel,userBioLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, userBio);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, username);
		AppearanceSettings.setFont(AppearanceConstants.fontButtonBig, cancel, save);
		AppearanceSettings.unSetBorderOnButtons(userIcon, cancel, save);
		AppearanceSettings.setCenterAlignment(userNameLabel, userBioLabel, username, userIcon);
		
		userBioTextPanel.add(userBio);
		
		buttonPanel.add(cancel);
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(save);

		AppearanceSettings.addGlue(framePanel, BoxLayout.PAGE_AXIS, true,
				userIcon);
		framePanel.add(userNameLabel);
		AppearanceSettings.addGlue(framePanel, BoxLayout.PAGE_AXIS, false,
				username);		
		framePanel.add(userBioLabel);
		AppearanceSettings.addGlue(framePanel, BoxLayout.PAGE_AXIS, false,userBioTextPanel,
				buttonPanel);

		add(framePanel);
	}
	
	private void addActionListeners(){
		userIcon.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				JFileChooser fileChoose = new JFileChooser();
//				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images Files", 
//						"png","jpg","gif","bmp");
//				fileChoose.setFileFilter(filter);
//				int check = fileChoose.showOpenDialog(getParent());
//				if (check == JFileChooser.APPROVE_OPTION){
//					imageLocation = fileChoose.getSelectedFile().getAbsolutePath();
//					updateIcon(imageLocation);
//				}
				
				String input = JOptionPane.showInputDialog("Please enter the URL of your new profile picture.");
				if(input != null) {
					if(!input.isEmpty()) {
						Image testImage = ImageLibrary.getImage(input);
						if (testImage != null) {
							image = testImage;
							imagePath = input;
							updateIcon(image);
						}
						else {
							JOptionPane.showMessageDialog(new JFrame(), "Invalid image URL!", "Image Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					
				}
			}
			
		});
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				gameFrame.user.setUserBio(userBio.getText());
				if (imagePath != null){
					gameFrame.user.setUserIcon(imagePath);
					gameFrame.header.updateIcon();
				}
				//Need to write function that updates SQL server and and the panels
				SQLDriver sqlDriver = new SQLDriver();
				sqlDriver.connect();
				sqlDriver.updateUserInfo(gameFrame.user.getUserIconString(), username.getText(), userBio.getText());
				setVisible(false);
			}
			
		});
		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}
	
	private void updateIcon(Image img){
		userIcon.setIcon(new ImageIcon(img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)));
		
	}
}
