package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import gameplay.Company;
import gameplay.Game;
import gameplay.GameFrame;
import gameplay.User;
import guis.AuctionBidScreen;
import guis.AuctionTeamList;
import guis.FinalGUI;
import guis.IntroPanel;
import guis.LobbyPanel;
import guis.LobbyUserPanel;
import guis.PlayerTab;
import guis.QuarterlyGUI;
import guis.TimelapsePanel;
import messages.AuctionBidUpdateMessage;
import messages.BeginAuctionBidMessage;
import messages.BuyMessage;
import messages.ChatMessage;
import messages.ClientExitMessage;
import messages.CompanyUpdateMessage;
import messages.FinalMessage;
import messages.FinalRequestMessage;
import messages.LeaveFinal;
import messages.LoadingGame;
import messages.LobbyListMessage;
import messages.LobbyPlayerReadyMessage;
import messages.ReadyGameMessage;
import messages.ReturnToIntro;
import messages.SellMessage;
import messages.StartTimerMessage;
import messages.SwitchPanelMessage;
import messages.TimerDone;
import messages.TimerTickMessage;
import messages.UserListMessage;
import messages.UserUpdate;
import server.SQLDriver;

/**
 * The {@code Client} class is a {@code Thread} that represents a 
 * player in a game.
 * @author alancoon
 *
 */
public class Client extends Thread {
	public User user;
	public Vector<User> users;
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket s;
	private GameFrame gameFrame;
	//Running boolean used to determine when to break the run while loop.
	private boolean running;
	private AuctionTeamList atl;
	private QuarterlyGUI qgui;
	
	public Client(User user) { 
		running = true;
		this.s = null;
		this.user = user;
		try {
			s = new Socket("jeffreychen.space", 8008);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		} catch (IOException ioe) { 
			ioe.printStackTrace();
		}
		gameFrame = new GameFrame(this, user);
		gameFrame.setVisible(true);
		System.out.println(gameFrame.getCurrentPanel().getClass());		
//		AudioInputStream is;
//		try { 
//			is = AudioSystem.getAudioInputStream(new URL("http://jeffreychen.space/fantasyvc/resources/chatSound.wav"));
////			clip = AudioSystem.getClip();
//		    clip.open(is);
//		    clip.start();
//		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);
//	    Clip clip = AudioSystem.getClip();
//	    clip.open(audioInputStream);
//	    clip.start();
	}
	
	public Vector<User> getUsers() {
		return users;
	}
	
	public void playSound() {
		Clip clip = null;
	      try {
	        clip = AudioSystem.getClip();
	        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new URL("http://jeffreychen.space/fantasyvc/resources/chatSound.wav"));
	        clip.open(inputStream);
	      } catch (Exception e) {
	        System.err.println(e.getMessage());
	      }
	      clip.start();
	    }

		
	
	public void returnToIntro(boolean showPane) {
		running = false;
		SQLDriver driver = new SQLDriver();
		if(showPane) JOptionPane.showMessageDialog(new JFrame(), "Somebody disconnected from the lobby! Returning you to the home screen...", "Disconnect", JOptionPane.WARNING_MESSAGE);
		driver.connect();
		new Client(driver.getUser(user.getUsername())).start();
		driver.stop();
	}
	
	public void run() {
		try{
			while(running) {
				Object m = ois.readObject();
				if (m instanceof ReturnToIntro) {
					ReturnToIntro rti = (ReturnToIntro)m;
					System.out.println("return to intro");
					returnToIntro(rti.getShowPane());
				}
				else if (m instanceof LeaveFinal) {
					System.out.println("leave final");
					System.exit(0);
				}
				else if (m instanceof UserUpdate) {
					if(gameFrame.getCurrentPanel() instanceof QuarterlyGUI) {
						UserUpdate uu = (UserUpdate)m;
						qgui.sendUpdate(uu.getUser().getCompanyName() + " is ready for the next quarter.");
					}
				}
				else if (m instanceof LobbyListMessage) {
					if(gameFrame.getCurrentPanel() instanceof IntroPanel) {
						LobbyListMessage llm = (LobbyListMessage)m;
						((IntroPanel)gameFrame.getCurrentPanel()).setLobbies(llm.lobbies);
					}
				}
				else if(m instanceof UserListMessage) {
					UserListMessage ulm = (UserListMessage)m;
					this.users = ulm.user;
					if (gameFrame.getCurrentPanel() instanceof LobbyPanel) {
						((LobbyPanel)gameFrame.getCurrentPanel()).setUsers(ulm.user);
						((LobbyPanel)gameFrame.getCurrentPanel()).setWaitingText(ulm.waitingOn);
					}
					else if (gameFrame.getCurrentPanel() instanceof IntroPanel) {
						((IntroPanel)gameFrame.getCurrentPanel()).switchToLobby(ulm.waitingOn, ulm.user);
					}
				}
				else if(m instanceof ChatMessage) {
					if(!(gameFrame.getCurrentPanel() instanceof IntroPanel)) {
						ChatMessage cm = (ChatMessage)m;
						try{
							if(!cm.getUsername().equals(gameFrame.user.getUsername())){
								//System.out.println(cm.getUsername() + gameFrame.user.getUsername());
//								File f = new File("resources/chatSound.wav");
//								AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
//								    Clip clip = AudioSystem.getClip();
//								    clip.open(audioInputStream);
//								    clip.start();
								playSound();
							}
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							gameFrame.getChatPanel().addChat(cm.getUsername(), cm.getMessage());
						}
					}
				}
				else if (m instanceof LobbyPlayerReadyMessage) {
					if(gameFrame.getCurrentPanel() instanceof LobbyPanel){
						System.out.println("lprm");
						LobbyPlayerReadyMessage lprm = (LobbyPlayerReadyMessage)m;
						Vector<LobbyUserPanel> lup = ((LobbyPanel)gameFrame.getCurrentPanel()).getLobbyPanels();
						for (LobbyUserPanel user : lup){
							if (user.getUsername().equals(lprm.getUsername())){
								System.out.println("set user");
								user.setFirmName(lprm.getTeamName());
								user.setReady();
							}
						}
						for (User user : users) {
							if(user.getUsername().equals(lprm.getUsername())) {
								user.setCompanyName(lprm.getTeamName());
							}
							user.createIcon();
						}
					}
				}
				else if (m instanceof ReadyGameMessage) {
					if(gameFrame.getCurrentPanel() instanceof LobbyPanel){
						System.out.println("ready game");
						atl = new AuctionTeamList(this, gameFrame); 
						gameFrame.changePanel(atl);
						System.out.println(user.getUsername() + " " + users.get(0).getUsername());
						if(user.getUsername().equals(users.get(0).getUsername())) {
							System.out.println("sending message");
							sendMessage(new StartTimerMessage());
						}
					}
				}
				else if (m instanceof Game) {
					System.out.println("new game " + ((Game)m).getCurrentQuarter());
					gameFrame.setGame((Game)m);
				}
				else if (m instanceof LoadingGame) {
					((LobbyPanel) gameFrame.getCurrentPanel()).setWaitingText(-1);
				}
				else if (m instanceof ClientExitMessage) {
					System.out.println("exit");
					ClientExitMessage cem = (ClientExitMessage)m;
					System.out.println(cem.getUsername());
					System.out.println(user.getUsername());
					if(user.getUsername().equals(cem.getUsername())) {
						System.exit(0);
					}
					if(gameFrame.getCurrentPanel() instanceof LobbyPanel) {
						((LobbyPanel)gameFrame.getCurrentPanel()).removeUser(cem.getUsername());
					}
				}
				else if (m instanceof BeginAuctionBidMessage) { 
					if(gameFrame.getCurrentPanel() instanceof AuctionTeamList){
						System.out.println("bidding now");
						BeginAuctionBidMessage babm = (BeginAuctionBidMessage) m;
						Company company = babm.getCompany();
						company.createIcon();
						AuctionBidScreen abs = new AuctionBidScreen(gameFrame, company);
						abs.updateBet(babm.getCompanyName(), company.getStartingPrice());
						gameFrame.changePanel(abs);
						atl.removeRow(babm.getSelected());
						atl.nextPlayer();
					}
				}
				else if (m instanceof AuctionBidUpdateMessage) {
					if (gameFrame.getCurrentPanel() instanceof AuctionBidScreen){
						AuctionBidUpdateMessage abum = (AuctionBidUpdateMessage) m;
						((AuctionBidScreen)gameFrame.getCurrentPanel()).updateBet(abum.getCompanyName(), abum.getBidAmount());
					}
				}
				else if (m instanceof TimerDone) {
					if (gameFrame.getCurrentPanel() instanceof AuctionBidScreen) {
						System.out.println("ABS " + System.currentTimeMillis());
						AuctionBidScreen auctionBidScreen = (AuctionBidScreen) gameFrame.getCurrentPanel();
						auctionBidScreen.company.setCurrentWorth(auctionBidScreen.bidMin);
						for(User u : users) {
							if(u.getCompanyName().equals(auctionBidScreen.currentBidder)) {
								u.addCompany(auctionBidScreen.company);
								if (u.getCompanyName().equals(user.getCompanyName())) {
									user = u;
									gameFrame.user = u;
									gameFrame.header.updateCurrentCapital();
								}
							}
						}
						atl.setDraftOrder();
						atl.updateCapital();
						if(atl.getCurrent() == null) {
							// TODO: send message to the server lobby that everybody is done
							System.out.println(user.getCompanies());
							System.out.println(user.getCurrentCapital());
							sendMessage(new UserUpdate(user));
						}
						else{
							atl.updateDisplayedFirmAssets();
							gameFrame.changePanel(atl);
							if(user.getUsername().equals(users.get(0).getUsername())) sendMessage(new StartTimerMessage());
						}
					}
					else if (gameFrame.getCurrentPanel() instanceof AuctionTeamList){
						System.out.println("ATL " + System.currentTimeMillis());
						if (user.getUsername().equals(atl.getCurrent())) {
							atl.networkBidButtonAction();
						}
					}
					else if (gameFrame.getCurrentPanel() instanceof QuarterlyGUI){
						System.out.println("QGUI " + System.currentTimeMillis());
						qgui.networkReadyFunctionality();
					}
				}
				else if (m instanceof TimerTickMessage) { 
					TimerTickMessage ttm = (TimerTickMessage) m;
					if (gameFrame.getCurrentPanel() instanceof AuctionBidScreen) {
						AuctionBidScreen auctionBidScreen = (AuctionBidScreen) gameFrame.getCurrentPanel();
						auctionBidScreen.updateTimer(ttm.getDisplay());
					}
					else if (gameFrame.getCurrentPanel() instanceof AuctionTeamList) {
						atl.updateTimer(ttm.getDisplay());
					}
					else if(gameFrame.getCurrentPanel() instanceof QuarterlyGUI) {
						qgui.updateTimer(ttm.getDisplay());
					}
					
					if(ttm.getDisplay().equals("00:00")) {
						if(user.getUsername().equals(users.get(0).getUsername())){
							sendMessage(new TimerDone());							
						}
					}
				}
				else if (m instanceof SwitchPanelMessage) {
					if(gameFrame.getCurrentPanel() instanceof AuctionBidScreen){
						gameFrame.game.incrementQuarter();
						gameFrame.changePanel(new TimelapsePanel(this, gameFrame));
					}
					else if(gameFrame.getCurrentPanel() instanceof TimelapsePanel) {
						System.out.println("increment" + gameFrame.game.getCurrentQuarter());
						qgui = new QuarterlyGUI(gameFrame, this);
						gameFrame.changePanel(qgui);
					}
					else if(gameFrame.getCurrentPanel() instanceof QuarterlyGUI) {
						gameFrame.game.incrementQuarter();
						gameFrame.changePanel(new TimelapsePanel(this, gameFrame));
					}
				}
				else if (m instanceof CompanyUpdateMessage) {
					if(gameFrame.getCurrentPanel() instanceof TimelapsePanel){
						CompanyUpdateMessage cum = (CompanyUpdateMessage)m; // hehe, cum...
						((TimelapsePanel)gameFrame.getCurrentPanel()).appendNotification(cum.getMessage());
					}
				}
				else if (m instanceof BuyMessage) {
					System.out.println("buy message");
					BuyMessage bm = (BuyMessage)m;
					qgui.userBuy(bm.getUsername(), bm.getCompany(), bm.getRowSelected());
				}				
				else if (m instanceof SellMessage) {
					System.out.println("sell message");
					SellMessage sm = (SellMessage)m;
					for(User user : qgui.getUsers()) {
						if(user.getUsername().equals(sm.getUsername())) {
							PlayerTab pt = qgui.getUserToTab().get(user);
							pt.userSell(sm.getUsername(), sm.getCompany(), sm.getRowSelected());
						}
					}
				}
//				else if (m instanceof InitiateTradeMessage) {
//					System.out.println("Initiating trade");
//					InitiateTradeMessage itm = (InitiateTradeMessage) m;
//					User initiator = itm.getInitiator();
//					User target = itm.getTarget();
//					System.out.println("user " + user.getUsername() + user.hashCode());
//					System.out.println("initiator " + initiator.getUsername() + initiator.hashCode());
//					System.out.println("target " + target.getUsername() + target.hashCode());
//				
//					if (user.getID() == initiator.getID()) {
//						gameFrame.changePanel(new TradeGUI(this, (QuarterlyGUI) gameFrame.getCurrentPanel(), initiator, target));
//					} else if (user.getID() == target.getID()) {
//						gameFrame.changePanel(new TradeGUI(this, (QuarterlyGUI) gameFrame.getCurrentPanel(), initiator, target));
//					} else {
//						if (gameFrame.getCurrentPanel() instanceof QuarterlyGUI) {
//							String text = initiator.getCompanyName() + " and " + target.getCompanyName() + " are considering a trade deal.";
//							((QuarterlyGUI) gameFrame.getCurrentPanel()).sendUpdate(text);
//						} else {
//							/* This really shouldn't be happening.  Let's print something so we know this 
//							 * occurs, if it does.
//							 */
//							System.out.println("Warning in Client.java under InitiateTradeMessage");
//						}
//					}
//				}
				else if(m instanceof FinalRequestMessage) {
					sendMessage(new UserUpdate(user));
				}
				else if(m instanceof FinalMessage) {
					System.out.println("final message");
					gameFrame.setGame(((FinalMessage) m).getGame());
					for(User user : gameFrame.game.getUsers())
					System.out.println(user.getUsername() + " " + user.getTotalProfit());
					gameFrame.changePanel(new FinalGUI(gameFrame, this));
				}
			}

		} catch (IOException ioe){
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		} finally {
			try {
				if (oos != null){
					oos.close();
				}
				if (ois != null){
					ois.close();
				}
				if (s != null){
					s.close();
				}
			} catch(IOException ioe) {
				System.out.println("Connection already closed");
			}
		}
		System.out.println("closed");
		gameFrame.dispose();
	}
		
	public User getUser() {
		return user;
	}

	public void sendMessage(Object message) {
		try { 
			System.out.println("Client sending message");
			oos.writeObject(message);
			System.out.println("flushing");
			oos.flush();
//			System.out.println("reset");
			oos.reset();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}


}