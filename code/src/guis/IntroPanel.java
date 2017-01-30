package guis;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import gameplay.GameFrame;
import gameplay.Lobby;
import gameplay.User;
import messages.JoinGameMessage;
import utility.AppearanceConstants;
import utility.AppearanceSettings;

public class IntroPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel lobbyLabel, hostLabel, sizeLabel, playerLabel;
	JLabel playerList;
	JLabel lobbyDetails;
	JButton hostButton, joinButton;
	JPanel eastPanel, centerPanel, playerPanel;
	public GameFrame gameFrame;
	Vector<JButton> lobbyButton;
	Vector<Lobby> lobbies;
	IntroPanel ip;
	Lobby activeLobby;
	CreateGameGUI cgui;
	
	public IntroPanel(GameFrame gameFrame) {
		this.ip = this;
		this.gameFrame = gameFrame;
		initializeComponents();
		createGUI();
		addEvents();
		centerPanel.removeAll();
		lobbyButton = new Vector<JButton>();
		joinButton.setEnabled(false);
		cgui = new CreateGameGUI(this);
	}
	
	private void initializeComponents() {
		lobbyLabel = new JLabel("Lobby Name");
		hostLabel = new JLabel("Host: Host Name");
		sizeLabel = new JLabel("Game Size: 3");
		playerLabel = new JLabel("Players:");
		playerList = new JLabel("test");
		hostButton = new JButton("Host");
		joinButton = new JButton("Join");
		lobbyDetails = new JLabel("Lobby Details", SwingConstants.CENTER);
		AppearanceSettings.setCenterAlignment(lobbyDetails);
		lobbyDetails.setFont(AppearanceConstants.fontLobby);
		lobbyDetails.setBorder(new EmptyBorder(10,10,30,10));
		lobbyDetails.setForeground(AppearanceConstants.offWhite);
	}
	
	private void createGUI() {
		this.setBackground(AppearanceConstants.lightBlue);
		this.setLayout(new BorderLayout());
		
		JPanel lobbyDetailsPanel = new JPanel();
		lobbyDetailsPanel.setBackground(AppearanceConstants.mediumGray);
		lobbyDetailsPanel.setLayout(new BorderLayout());
		
		eastPanel = new JPanel();
		eastPanel.setBackground(AppearanceConstants.mediumGray);
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		eastPanel.setPreferredSize(new Dimension(400,0));
		eastPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
		
		JScrollPane infoPane = new JScrollPane(eastPanel);
		infoPane.getViewport().setOpaque(false);
		infoPane.setOpaque(false);
		infoPane.setBorder(null);
		infoPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		lobbyDetailsPanel.add(lobbyDetails, BorderLayout.NORTH);
		lobbyDetailsPanel.add(infoPane, BorderLayout.CENTER);
		
		this.add(lobbyDetailsPanel, BorderLayout.EAST);
		
		JPanel southPanel = new JPanel();
		southPanel.setBackground(AppearanceConstants.darkBlue);
		southPanel.setLayout(new GridLayout(1, 2, 90, 90));
		southPanel.setBorder(new EmptyBorder(20, 90, 20, 90));
		southPanel.setPreferredSize(new Dimension(0, 90));
		
		makeButton(hostButton, joinButton);
		southPanel.add(hostButton);
		southPanel.add(joinButton);
		

		this.add(southPanel, BorderLayout.SOUTH);
		
		centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

		JScrollPane lobbyPane = new JScrollPane(centerPanel);
		lobbyPane.getViewport().setOpaque(false);
		lobbyPane.setOpaque(false);
		lobbyPane.setBorder(null);
		lobbyPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		
		this.add(lobbyPane);
		
	}
	
	public void switchToLobby(int numWaiting, Vector<User> users) {
		LobbyPanel lp = new LobbyPanel(gameFrame);
		lp.setUsers(users);
		lp.setWaitingText(numWaiting);
		gameFrame.chatVisible();
		gameFrame.changePanel(lp);
		cgui.setVisible(false);
		cgui.dispose();
	}
	
	private void addEvents() {
		hostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cgui.setVisible(true);
			}
		});
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Vector<User> lobbyUsers = activeLobby.getUsers();
				for (User user : lobbyUsers) {
					if (user.getUsername().equalsIgnoreCase(gameFrame.user.getUsername())) {
						JOptionPane.showMessageDialog(null, "Somebody else with the same username is already in that lobby!", "Duplicate Username", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				JoinGameMessage jgm = new JoinGameMessage(gameFrame.user, activeLobby.getLobbyName());
				gameFrame.getClient().sendMessage(jgm);
			}
		});
	}
	
	public void makeButton(JButton... button) {
		for (JButton b : button) {
			b.setOpaque(true);
			b.setBackground(AppearanceConstants.green);
			b.setForeground(AppearanceConstants.offWhite);
			AppearanceSettings.unSetBorderOnButtons(b);
			b.setFont(AppearanceConstants.fontMedium);
//			b.setBorder(null);
		}
	}
	
	public void addToInfo(JComponent jc) {
		//jc.setFont(new Font("Arial", Font.BOLD, 32));
		jc.setFont(AppearanceConstants.fontLobby);
		jc.setForeground(AppearanceConstants.offWhite);
		jc.setBorder(new EmptyBorder(10, 0, 10, 0));
		eastPanel.add(jc);
	}

	public void clearPlayerPanel() {
		for(Component c : eastPanel.getComponents()) {
			eastPanel.remove(c);
		}
	}
	
	public void addLobby(Lobby lobby) {
		JPanel lobbyPanel = new JPanel();
		lobbyPanel.setLayout(new BoxLayout(lobbyPanel, BoxLayout.X_AXIS));
//		lobbyPanel.setBorder(new EmptyBorder(15,40,15,40));
		lobbyPanel.setBorder(new LineBorder(AppearanceConstants.mediumGray,	15));
//		lobbyPanel.setOpaque(false);
		lobbyPanel.setBackground(AppearanceConstants.mediumGray);
		
		JLabel lobbyName = new JLabel(lobby.getLobbyName());
		//lobbyName.setFont(new Font("Arial", Font.BOLD, 32));
		lobbyName.setFont(AppearanceConstants.fontLobby);
		lobbyName.setForeground(AppearanceConstants.offWhite);
		
		lobbyPanel.add(lobbyName);
		lobbyPanel.add(Box.createHorizontalGlue());
		
		JButton selectButton = new JButton("Select");
		selectButton.setForeground(AppearanceConstants.offWhite);
		selectButton.setBackground(AppearanceConstants.green);
		AppearanceSettings.unSetBorderOnButtons(selectButton);
		selectButton.setOpaque(true);
//		selectButton.setBorder(new EmptyBorder(10,40,10,40));
		//selectButton.setFont(new Font("Arial", Font.BOLD, 28));
		selectButton.setFont(AppearanceConstants.fontMedium);
		selectButton.putClientProperty("lobbyName", lobby);
		selectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				joinButton.setEnabled(true);
				clearPlayerPanel();
				JButton source = (JButton) ae.getSource();
				activeLobby = (Lobby)source.getClientProperty("lobbyName");
				lobbyLabel.setText("Lobby: " + lobby.getLobbyName());
				hostLabel.setText("Host: " + lobby.getHostName());
				sizeLabel.setText("Game Size: " + lobby.getGameSize());
				addToInfo(lobbyLabel);
				addToInfo(hostLabel);
				addToInfo(sizeLabel);
				addToInfo(playerLabel);
				for(User p : lobby.getUsers()) {
					addToInfo(new JLabel(p.getUsername()));
				}
				gameFrame.revalidate();
				gameFrame.repaint();
			}
			
		});
		lobbyButton.add(selectButton);
		
		lobbyPanel.add(selectButton);
		
		JPanel line = new JPanel();
		line.setBackground(AppearanceConstants.lightBlue);
		line.setLayout(new BoxLayout(line, BoxLayout.PAGE_AXIS));
		line.add(Box.createVerticalStrut(5));
		line.add(lobbyPanel);
		
		centerPanel.add(line);
		
		
	}
	
	public void setLobbies(Vector<Lobby> lobbies) {
		this.lobbies = lobbies;
		lobbyButton.clear();
		joinButton.setEnabled(false);
		clearCenterPanel();
		eastPanel.removeAll();
		for(Lobby lobby : lobbies) {
			System.out.println("Lobby");
			
			addLobby(lobby);
		}
		
		gameFrame.revalidate();
		gameFrame.repaint();
	}

	public void clearCenterPanel() {
		for(Component c : centerPanel.getComponents()) {
			centerPanel.remove(c);
		}
	}
	
	public Vector<Lobby> getLobbies() {
		return lobbies;
	}
}
