package guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import gameplay.GameFrame;
import gameplay.User;
import listeners.TextFieldFocusListener;
import messages.LobbyPlayerReadyMessage;
import utility.AppearanceConstants;
import utility.AppearanceSettings;

public class LobbyPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel statusLabel, firmLabel;
	private JTextField firmField;
	private JButton readyButton;
	private Vector<LobbyUserPanel> lobbyUserLabels;
	public GameFrame gameFrame;
	private JPanel memberPanel;
	
	public LobbyPanel(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
		initializeComponents();
		createGUI();
		addEvents();
	}
	
	private void initializeComponents() {
		statusLabel = new JLabel("Waiting for 1 more player to join...");
		firmLabel = new JLabel("Firm Name");
		firmField = new JTextField();
		readyButton = new JButton("Ready");
		lobbyUserLabels = new Vector<LobbyUserPanel>();
		
	}
	private void createGUI() {
		this.setSize(new Dimension(1280, 648));
		this.setBackground(AppearanceConstants.lightBlue);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(AppearanceConstants.lightBlue);
		eastPanel.setLayout(new BorderLayout());
		eastPanel.setPreferredSize(new Dimension(400, 500));
		eastPanel.setMaximumSize(new Dimension(400, 500));
		
		JPanel buttonPanel = new JPanel();
		AppearanceSettings.setCenterAlignment(readyButton);
		buttonPanel.setOpaque(false);
		buttonPanel.setBorder(new EmptyBorder(30,60,30,60));
		buttonPanel.setLayout(new GridLayout(3, 1, 30, 30));
		makeButton(readyButton);
		buttonPanel.add(readyButton);
		buttonPanel.setBackground(AppearanceConstants.offWhite);
		
		eastPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		JPanel firmPanel = new JPanel();
		firmPanel.setBorder(new EmptyBorder(50, 50, 0, 50));
		firmPanel.setOpaque(false);
		firmPanel.setLayout(new GridLayout(2,1,10,10));
		
		firmLabel.setHorizontalAlignment(JLabel.CENTER);
		firmLabel.setFont(AppearanceConstants.fontLarge);
		firmLabel.setForeground(AppearanceConstants.offWhite);
		firmPanel.add(firmLabel);
		
		firmField.setFont(AppearanceConstants.fontLarge);
		firmField.setBorder(new EmptyBorder(10, 5, 10, 5));
		firmPanel.add(firmField);
		
		eastPanel.add(firmPanel, BorderLayout.NORTH);
		
		//TODO this.add(eastPanel);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setPreferredSize(new Dimension(700, 500));
		centerPanel.setMaximumSize(new Dimension(700, 500));
		centerPanel.setBackground(AppearanceConstants.lightBlue);
		
		memberPanel = new JPanel();
		memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.Y_AXIS));
		memberPanel.setOpaque(false);
		memberPanel.setBorder(new EmptyBorder(20,40,0,40));
		
//		for(int i = 0; i < 2; i++) {
//			lobbyUserLabels.add(new LobbyUserPanel("Jeffrey " + Integer.toString(i)));
//			lobbyUserLabels.get(i).setFirmName("JMoney Capital "+ Integer.toString(i));
//		}
		refreshMemberPanel();
		
		JScrollPane scrollPane = new JScrollPane(memberPanel);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		centerPanel.add(scrollPane, BorderLayout.NORTH);
		
		statusLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
		statusLabel.setForeground(AppearanceConstants.offWhite);
		statusLabel.setFont(AppearanceConstants.fontHeaderUser);
		centerPanel.add(statusLabel, BorderLayout.SOUTH);
		
		add(Box.createGlue());
		add(centerPanel);
		add(Box.createGlue());
		add(eastPanel);
		add(Box.createGlue());
		
		
		AppearanceSettings.setBackground(AppearanceConstants.green, readyButton);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, readyButton);
		readyButton.setEnabled(false);
	}
	private void addEvents() {
		firmField.addFocusListener(new TextFieldFocusListener("Enter a name...", firmField));
		firmField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				changed();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				changed();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				changed();
			}
			private void changed() {
				if (firmField.getText().trim().equals("Enter a name...") || firmField.getText().trim().isEmpty()) {
					readyButton.setEnabled(false);
				} else {
					readyButton.setEnabled(true);
				}
			}
		});
		firmField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(readyButton.isEnabled()) {
						System.out.println("username: " + gameFrame.user.getUsername());
						for(LobbyUserPanel lup : lobbyUserLabels) {
							System.out.println(firmField.getText().trim() + " " + lup.getFirmName());
							if(firmField.getText().trim().equals(lup.getFirmName())) {
								JOptionPane.showMessageDialog(new JFrame(),
									    "Sorry, that firm name is already taken.",
									    "Name already taken",
									    JOptionPane.WARNING_MESSAGE);
								return;
							}
						}
						LobbyPlayerReadyMessage lprm = new LobbyPlayerReadyMessage(gameFrame.user.getUsername(),firmField.getText().trim());
						gameFrame.getClient().sendMessage(lprm);
						gameFrame.getClient().getUser().setCompanyName(firmField.getText().trim());
						readyButton.setEnabled(false);
						firmField.setEnabled(false);
					}
				}
			}
		});
		readyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println(lobbyUserLabels.size());
				for(LobbyUserPanel lup : lobbyUserLabels) {
					System.out.println(firmField.getText().trim() + " " + lup.getFirmName());
					if(firmField.getText().trim().equals(lup.getFirmName())) {
						JOptionPane.showMessageDialog(new JFrame(),
							    "Sorry, that firm name is already taken.",
							    "Name already taken",
							    JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				System.out.println("username: " + gameFrame.user.getUsername());
				LobbyPlayerReadyMessage lprm = new LobbyPlayerReadyMessage(gameFrame.user.getUsername(),firmField.getText().trim());
				gameFrame.getClient().sendMessage(lprm);
				gameFrame.getClient().getUser().setCompanyName(firmField.getText().trim());
				((JButton)ae.getSource()).setEnabled(false);
				firmField.setEnabled(false);
			}
		});
					
		// Listen for changes in the text
		firmField.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    updateButton();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    updateButton();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    updateButton();
			  }
	
			  public void updateButton() {
//				  firmField.setForeground(Color.BLACK); // TODO
			     if (firmField.getText().length() > 0) {
			        readyButton.setEnabled(true);
			     }
			     else {
			        readyButton.setEnabled(false);
			     }
			  }
		});
				
	}
	
	public void makeButton(JButton... button) {
		for (JButton b : button) {
			b.setOpaque(true);
			b.setBackground(AppearanceConstants.darkBlue);
			b.setForeground(AppearanceConstants.offWhite);
			b.setFont(AppearanceConstants.fontButtonBig);
			b.setBorderPainted(false);
		}
	}
	
	public void refreshMemberPanel() {
		memberPanel.removeAll();
		for (int i = 0; i < lobbyUserLabels.size(); i++) {
			memberPanel.add(lobbyUserLabels.get(i));
			if (i != (lobbyUserLabels.size()-1) ) 
				memberPanel.add(Box.createVerticalStrut(5));
		}
	}
	
	public void addUser(String username){
		lobbyUserLabels.add(new LobbyUserPanel(username));
	}
	
	public void removeUser(String username){
		for(int i = 0; i < lobbyUserLabels.size(); i++){
			if(lobbyUserLabels.get(i).getUsername().equals(username))
				lobbyUserLabels.remove(i);
		}
	}
	
	public void setUsers(Vector<User> users) {
		for(User u : users) {
			removeUser(u.getUsername());
			addUser(u.getUsername());
			if(u.getReady()) {
				for (LobbyUserPanel lup : lobbyUserLabels) {
					if(lup.getUsername().equals(u.getUsername())) {
						lup.setReady();
						lup.setFirmName(u.getCompanyName());
					}
				}
			}
		}
		refreshMemberPanel();
		gameFrame.revalidate();
		gameFrame.repaint();
	}
	
	public Vector<LobbyUserPanel> getLobbyPanels() {
		return lobbyUserLabels;
	}
	
	public void setWaitingText(int waiting) {
		if (waiting > 1) statusLabel.setText("Waiting for " + waiting + " more players to join...");
		else if (waiting == 1) statusLabel.setText("Waiting for " + waiting + " more player to join...");
		else if (waiting == -1) statusLabel.setText("Game loading, please wait...");
		else statusLabel.setText("All players here");
	}

}
