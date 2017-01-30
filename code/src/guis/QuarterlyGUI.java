package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import client.Client;
import gameplay.Company;
import gameplay.GameFrame;
import gameplay.User;
import listeners.TableModel;
import messages.BuyMessage;
import messages.UserUpdate;
import utility.AppearanceConstants;
import utility.AppearanceSettings;

public class QuarterlyGUI extends JPanel{
	public JPanel chatPanel;
	public JPanel notificationsAndReadyPanel;
	private JLabel timer;
	public JPanel titlePanel;
	public JTextArea updatesTextArea;
	public JButton ready;
	public JTabbedPane tabbedPane;
	public PlayerTab panel1, panel2, panel3, panel4;
	public JPanel freeAgents;
	public GameFrame gameFrame;
	private Client client;

	private Vector<User> users;
	private Vector<PlayerTab> tabs;

	private JTable freeAgentTable;
	private JButton buy;
	private HashMap<User, PlayerTab> userToTab;
	private HashMap<PlayerTab, User> tabToUser;
	private JScrollBar scrollBar;

	/** Used https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QuarterlyGUI(GameFrame gameFrame, Client client) {
		this.gameFrame = gameFrame;
		this.client = client;
		this.gameFrame.header.updateCurrentCapital();
		initializeComponents();
		createGUI();
		addActionListeners();
	}
	
	public void updateTimer(String time) {
		timer.setText(time);
		revalidate();
		repaint();
	}
	
	public Vector<User> getUsers() {
		return users;
	}

	private void initializeComponents() {
		setSize(1280, 504);
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		updatesTextArea = new JTextArea();
		updatesTextArea.setWrapStyleWord(true);
		notificationsAndReadyPanel = new JPanel();
		ready = new JButton("Ready for Next Quarter");
		timer = new JLabel("01:00", SwingConstants.CENTER);

		users = gameFrame.game.getUsers();
		tabs = new Vector<PlayerTab>();
		buy = new JButton("Buy selected company.");
		userToTab = new HashMap<User, PlayerTab>();
		tabToUser = new HashMap<PlayerTab, User>();
	}

	private void createGUI() {
		JScrollPane updatesScrollPane = new JScrollPane(updatesTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		updatesTextArea.setEditable(false);
		updatesTextArea.setForeground(Color.WHITE);

		//TODO temporary icon
		Vector<String> assets = new Vector<String>();
		assets.addElement("Portfolio Contents:");
		
		//String companyName1 = gameFrame.user.getCompanyName();
		//Image image = gameFrame.user.getUserIcon();
		PlayerTab pt1;
		if(gameFrame.networked) pt1 = new PlayerTab(client.getUser(), this);
		else pt1 = new PlayerTab(gameFrame.user, this);
		userToTab.put(gameFrame.user, pt1);
		tabToUser.put(pt1, gameFrame.user);
		tabs.add(pt1);
		tabbedPane.add(gameFrame.user.getCompanyName(), pt1);
		
		

		for (User user : users) {
			if(!user.getUsername().equals(gameFrame.user.getUsername())) {
				//String companyName = user.getCompanyName();
				//			ImageIcon imageIcon = new ImageIcon(user.getUserIcon());
				//ImageIcon imageIcon = new ImageIcon("http://jeffreychen.space/fantasyvc/users/guestuser.png");
				//user.setUserIcon("http://jeffreychen.space/fantasyvc/users/guestuser.png");
	
				PlayerTab pt = new PlayerTab(user, this);
				userToTab.put(user, pt);
				tabToUser.put(pt, user);
				tabs.add(pt);
				tabbedPane.add(user.getCompanyName(), pt);
			}
		}

		freeAgents = new JPanel();
		tabbedPane.add("Available Companies", freeAgents);

		// Create freeAgents 
		String[] columnNames = {"Name", "Tier Level", "Price (Millions)", "Net Growth"};
		TableModel dtm = new TableModel();
		dtm.setColumnIdentifiers(columnNames);
		Vector<Company> companies = gameFrame.game.getFreeAgents();

		for(int i = 0; i < companies.size(); i++) {
			double percentChange = (companies.get(i).getCurrentWorth() - companies.get(i).getStartingPrice())/
					companies.get(i).getStartingPrice() * 100;
			DecimalFormat df = new DecimalFormat ("#.##");
			
			if(companies.get(i).getCurrentWorth() != 0) {
				dtm.addRow(new Object[] {
						companies.get(i).getName(), 
						Integer.toString(companies.get(i).getTierLevel()),
						df.format(companies.get(i).getCurrentWorth()), 
						df.format(percentChange) + "%"});
			}
		}

		freeAgentTable = new JTable(dtm);
		freeAgentTable.getTableHeader().setReorderingAllowed(false);
		freeAgentTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

		JScrollPane freeAgentScrollPane = new JScrollPane(freeAgentTable);
		freeAgents.setLayout(new BorderLayout());
		freeAgents.add(freeAgentScrollPane, BorderLayout.CENTER);
		JPanel buyPanel = new JPanel();
		buyPanel.add(Box.createHorizontalStrut(5));
		buyPanel.add(buy);

		freeAgents.add(buyPanel, BorderLayout.SOUTH);

		AppearanceSettings.setBackground(AppearanceConstants.offWhite, freeAgentTable);

		// Create notificationsAndReadyPanel
		
		JPanel timerReadyPanel = new JPanel(new GridLayout(2, 1));
		timerReadyPanel.add(timer);
		timerReadyPanel.add(ready);
		
		updatesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		updatesScrollPane.getViewport().setBackground(AppearanceConstants.darkBlue);

		notificationsAndReadyPanel.setLayout(new BorderLayout());
		notificationsAndReadyPanel.add(timerReadyPanel, BorderLayout.NORTH);
		notificationsAndReadyPanel.add(updatesScrollPane, BorderLayout.CENTER);
		
//		notificationsAndReadyPanel.add(updatesPanel, BorderLayout.SOUTH);
		
//		notificationsAndReadyPanel.setLayout(new GridLayout(3, 1));
//		notificationsAndReadyPanel.add(timer);
//		notificationsAndReadyPanel.add(ready);
//		notificationsAndReadyPanel.add(updatesPanel);

		
		updatesTextArea.setFont(AppearanceConstants.fontSmallest);
		updatesTextArea.setLineWrap(true);
		
		scrollBar = updatesScrollPane.getVerticalScrollBar();
		scrollBar.setBackground(AppearanceConstants.darkBlue);

		int quarter = gameFrame.game.getQuarter();
		int year = gameFrame.game.getYear();
		
		sendUpdate("End of Q" + quarter + ", " + year + ".");
		sendUpdate("Here is your quarterly report!");

		notificationsAndReadyPanel.setPreferredSize(new Dimension(300, 1));
		updatesScrollPane.setPreferredSize(new Dimension(210, 400));
		updatesScrollPane.setBorder(null);
		
		add(tabbedPane, BorderLayout.CENTER);
		add(notificationsAndReadyPanel, BorderLayout.EAST);

		AppearanceSettings.setFont(AppearanceConstants.fontMedium, timer);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, timerReadyPanel, updatesTextArea, notificationsAndReadyPanel);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, freeAgentScrollPane);

		AppearanceSettings.setBackground(AppearanceConstants.green, ready);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, timer, ready, buy);
		AppearanceSettings.setOpaque(ready, buy);
		AppearanceSettings.unSetBorderOnButtons(ready, buy);
		AppearanceSettings.setFont(AppearanceConstants.fontButtonMedium, ready);
		AppearanceSettings.setFont(AppearanceConstants.fontButtonMedium, buy);
		AppearanceSettings.setBackground(AppearanceConstants.mediumGray, buy);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, freeAgents, buyPanel, this);
		if(!gameFrame.networked) timer.setVisible(false);
	}

	private void addActionListeners() {
		buy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the selected company
				int selectedRow = freeAgentTable.getSelectedRow();
				
				if(!gameFrame.networked) {
					if (selectedRow != -1) {
						TableModel dtm = (TableModel) freeAgentTable.getModel();
						Company selectedCompany = gameFrame.game.returnCompany((String) dtm.getValueAt(selectedRow, 0));
						double price = selectedCompany.getCurrentWorth();
						double currentCapital = gameFrame.user.getCurrentCapital();
						if (price > currentCapital) {
							JOptionPane.showConfirmDialog(null, "You can't afford that!", "Venture Capital", JOptionPane.WARNING_MESSAGE);
						} else {
							// Buy the company
							gameFrame.user.addCompany(selectedCompany);

							// Update our GUI to reflect current capital
							gameFrame.header.updateCurrentCapital();

							// Remove from table
							dtm.removeRow(selectedRow);

							// Make the stuff needed to insert
							double percentChange = (selectedCompany.getCurrentWorth() - selectedCompany.getStartingPrice())/selectedCompany.getStartingPrice() * 100;
							DecimalFormat df = new DecimalFormat("#.##");

							// Get the PlayerTab of the user
							PlayerTab pt = userToTab.get(gameFrame.user);
							JTable userTable = pt.getTable();
							userTable.getTableHeader().setReorderingAllowed(false);
							TableModel userDtm = (TableModel) userTable.getModel();
							
							
							userDtm.addRow(new Object[]{selectedCompany.getName(), 
									Integer.toString(selectedCompany.getTierLevel()),
									df.format(selectedCompany.getCurrentWorth()),
									df.format(percentChange) + "%" });
							
							//update the notifications
							String update = gameFrame.user.getCompanyName() + " bought " + selectedCompany.getName() + ".";
							sendUpdate(update);
						}
					}
				}
				//TODO networked game buy button pressed:
				else {
					if (selectedRow != -1) {
						TableModel dtm = (TableModel) freeAgentTable.getModel();
						Company selectedCompany = gameFrame.game.returnCompany((String) dtm.getValueAt(selectedRow, 0));
						double price = selectedCompany.getCurrentWorth();
						double currentCapital = gameFrame.user.getCurrentCapital();
						if (price > currentCapital) {
							JOptionPane.showConfirmDialog(null, "You can't afford that!", "Venture Capital", JOptionPane.WARNING_MESSAGE);
						} else {
							client.sendMessage(new BuyMessage(gameFrame.user.getUsername(), selectedCompany, selectedRow));
							// Buy the company
//							gameFrame.user.addCompany(selectedCompany);
							// Update our GUI to reflect current capital
//							gameFrame.header.updateCurrentCapital();
							
							//TODO
							/**
							 * Send a message with either selectedCompany or selectedRow and the user
							 * to all clients. When the clients recieve this message they should 
							 * remove the selected row then add the row to the player tab of the user who bought it
							 * Base these changes in every client off the commented code below:
							 */
							
							//create a message that contains: User, selectedCompany, and selectedRow
							//call moveToFreeAgents(User user, Company selectedCompany, int selectedRow)
						}
					}
				}
				
			}
		});
		ready.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				if(!gameFrame.networked) {
					if (gameFrame.game.currentQuarter == 20) {
						gameFrame.changePanel(new FinalGUI(gameFrame, null));
					} else {
						gameFrame.changePanel(new TimelapsePanel(null, gameFrame));
					}
				}
				else {
					networkReadyFunctionality();
					ready.setEnabled(false);
					buy.setEnabled(false);
					for (PlayerTab pt : tabs) {
						pt.getSellButton().setEnabled(false);
					}
					//TODO networked game
					//QuarterlyReadyMessage qrm = new QuarterlyReadyMessage(); //maybe this message?
				}

				
			}
		});
	}
	
	public void networkReadyFunctionality() {
		client.sendMessage(new UserUpdate(gameFrame.user));
	}

	public void sendUpdate(String message) {
		updatesTextArea.append("\n" + message);
		scrollBar.setValue(scrollBar.getMaximum());
	}

	public JTable getFreeAgentTable() {
		return freeAgentTable;
	}
	
	public HashMap<User, PlayerTab> getUserToTab() {
		return userToTab;
	}
	
	public HashMap<PlayerTab, User> getTabToUser() { 
		return tabToUser;
	}
	
	public void userBuy(String username, Company company, int selectedRow) {
		for(User user : users) {
			if(user.getUsername().equals(username)) {
				if(!removeFromFreeAgents(user, company, selectedRow)) {
					return;
				}
				user.addCompany(company);
				if(user.getUsername().equals(gameFrame.user.getUsername())) {
					gameFrame.user = user;
					gameFrame.header.updateCurrentCapital();
				}
				
			}
		}
	}
	
	public boolean removeFromFreeAgents(User user, Company selectedCompany, int selectedRow) {
		TableModel dtm = (TableModel) freeAgentTable.getModel();
		// Remove from table
		if(!dtm.getValueAt(selectedRow, 0).equals(selectedCompany.getName())) {
			return false;
		}
		dtm.removeRow(selectedRow);

		// Make the stuff needed to insert
		double percentChange = (selectedCompany.getCurrentWorth() - selectedCompany.getStartingPrice())/selectedCompany.getStartingPrice() * 100;
		DecimalFormat df = new DecimalFormat("#.##");

		// Get the PlayerTab of the user
		PlayerTab pt = userToTab.get(user);
		JTable userTable = pt.getTable();
		TableModel userDtm = (TableModel) userTable.getModel();
		
		
		userDtm.addRow(new Object[]{selectedCompany.getName(), 
				Integer.toString(selectedCompany.getTierLevel()),
				df.format(selectedCompany.getCurrentWorth()),
				df.format(percentChange) + "%" });
		
		//update the notifications
		String update = user.getCompanyName() + " bought " + selectedCompany.getName() + ".";
		sendUpdate(update);
		
		return true;
		/*
		// Remove from table
		dtm.removeRow(selectedRow);

		// Make the stuff needed to insert
		double percentChange = (selectedCompany.getCurrentWorth() - selectedCompany.getStartingPrice())/selectedCompany.getStartingPrice() * 100;
		DecimalFormat df = new DecimalFormat("#.##");

		// Get the PlayerTab of the user
		PlayerTab pt = userToTab.get(gameFrame.user);
		JTable userTable = pt.getTable();
		TableModel userDtm = (TableModel) userTable.getModel();
		
		
		userDtm.addRow(new Object[]{selectedCompany.getName(), 
				Integer.toString(selectedCompany.getTierLevel()),
				df.format(selectedCompany.getCurrentWorth()),
				df.format(percentChange) + "%" });
		
		//update the notifications
		String update = gameFrame.user.getCompanyName() + " bought " + selectedCompany.getName() + ".";
		sendUpdate(update);
		*/
	}
}