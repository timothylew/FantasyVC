package gameplay;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;
import guis.AuctionTeamList;
import guis.ChatPanel;
import guis.IntroPanel;
import guis.LobbyPanel;
import guis.TopPanel;
import listeners.ExitWindowListener;
import messages.GameSet;

public class GameFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private Client client;
	public Boolean networked;
	public Game game;
	public JPanel currentPanel;
	public TopPanel header;
	public User user;
	public ChatPanel chatbox;
	public Boolean gameInProgress;

	
	/**
	 * Single player.
	 * @param guest A guest {@code User} instantiation.
	 */
	public GameFrame(User guest) {
		super("Fantasy VC | Guest Mode");
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this));
		setSize(1280, 720);
		
		networked = false;
		gameInProgress = false;
		
		game = new Game();
		user = guest;
		game.addUser(guest);
		
		header = new TopPanel(this, guest);
		chatbox = new ChatPanel(guest);
		
		header.setPreferredSize(new Dimension(1280,72));
		chatbox.setPreferredSize(new Dimension(1280,144));
		
		//LobbyPanel main = new LobbyPanel(this);
		//AuctionTeamList main = new AuctionTeamList(null, this);
		//AuctionBidScreen main = new AuctionBidScreen(this,game.getCompanies().get(5));
		AuctionTeamList main = new AuctionTeamList(null, this);
		//FinalGUI main = new FinalGUI(this);
		//TradeGUI main = new TradeGUI(null);
		
		
		add(header, BorderLayout.NORTH);
		add(chatbox, BorderLayout.SOUTH);
		add(main, BorderLayout.CENTER);
		
		currentPanel = main;
		setVisible(true);
	}
	
	public GameFrame(Client client, User user) {
		super("Fantasy VC Online | Welcome " + user.getUsername() + "!");
		this.setResizable(false);
		gameInProgress = false;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitWindowListener(this, client));
		setSize(1280,720);
		
		this.client = client;
		this.user = user;
		networked = true;
		header = new TopPanel(this, client);
		chatbox = new ChatPanel(client);

		header.setPreferredSize(new Dimension(1280,72));
		chatbox.setPreferredSize(new Dimension(0,144));
		
		IntroPanel main = new IntroPanel(this);

		currentPanel = main;
		
//		chatbox.setBackground(Color.GRAY);
		
		add(header, BorderLayout.NORTH);
		add(chatbox, BorderLayout.SOUTH);
		chatbox.setVisible(false);
		add(currentPanel, BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	public void chatVisible() {
		chatbox.setVisible(true);
	}
	
	public void setGame(Game game){
		this.game = game;
		this.game.initializeIcons();
		for(User user : game.getUsers()) {
			if(this.user.getUsername().equals(user.getUsername())){
				this.user = user;
				client.user = user;				
			}
		}
		client.sendMessage(new GameSet());
	}
	
	
	public void changePanel(JPanel panel) {
		if(panel instanceof LobbyPanel) {
			header.activeIcon = false;
		}
		remove(currentPanel);
		currentPanel = panel;
		add(currentPanel, BorderLayout.CENTER);
		// must repaint or the change won't show
		revalidate();
		repaint();
	}
	
	public JPanel getCurrentPanel() {
		return currentPanel;
	}
	
	public ChatPanel getChatPanel() {
		return chatbox;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Client getClient() {
		return client;
	}

}
