package guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import gameplay.Company;
import gameplay.GameFrame;
import gameplay.User;
import listeners.TableModel;
import messages.SellMessage;
import utility.AppearanceConstants;
import utility.AppearanceSettings;
import utility.Constants;
import utility.ImageLibrary;

public class PlayerTab extends JPanel {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	public JButton trade;
	public JButton sell;
	public String playerName;
	public ImageIcon playerIcon;
	public JPanel playerInfo;
	public Vector<Company> assets;
	public QuarterlyGUI qg;
	public GameFrame gameFrame;
	
	
	private JTable portfolio;
	private User user;
	
//	private PlayerTab currentTab = this;
	
//	public PlayerTab(String playerName, ImageIcon playerIcon, Vector<Company> assets, QuarterlyGUI qg) {
//		this.playerName = playerName;
//		this.playerIcon = playerIcon;
//		this.assets = assets;
//		this.qg = qg;
//		this.gameFrame = qg.gameFrame;
//		initializeComponents();
//		createGUI();
//		addActionListeners();
//	}
	
	public PlayerTab(User user, QuarterlyGUI qg) {
		this.user = user;
//		this.playerName = user.getUsername();
//		this.playerIcon = new ImageIcon(user.getUserIcon());
//		this.assets = user.getCompanies();
		this.qg = qg;
		this.gameFrame = this.qg.gameFrame;
		initializeComponents();
		createGUI();
		addActionListeners();
	}
	

	private void initializeComponents(){
//		trade = new JButton("Trade with this player.");
//		if (user.getID() == -1) {
//			trade.setEnabled(false);
//		} else if (!gameFrame.user.equals(user)) {
//			trade.setEnabled(false);
//		}
		sell = new JButton("Sell selected company.");
		playerInfo = new JPanel();
	}
	
	private void createGUI(){
//		JScrollPane updatesScrollPane = new JScrollPane(portfolio, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		String[] columnNames = {"Name", "Tier Level", "Price (Millions)", "Net Growth"};
		TableModel dtm = new TableModel();
		dtm.setColumnIdentifiers(columnNames);
		Vector<Company> companies = user.getCompanies();
		
		for(int i = 0; i < companies.size(); i++) {
			double percentChange = (companies.get(i).getCurrentWorth() - companies.get(i).getStartingPrice())/
					 companies.get(i).getStartingPrice() * 100;
			DecimalFormat df = new DecimalFormat ("#.##");
			System.out.println(df.format(percentChange));
			System.out.println(df.format(percentChange) + "%");

			if(companies.get(i).getCurrentWorth() == 0) {
				dtm.addRow(new Object[]{companies.get(i).getName(), 
						Integer.toString(companies.get(i).getTierLevel()),
						df.format(companies.get(i).getCurrentWorth()), 
						"Bankrupt"});
			} else {
				dtm.addRow(new Object[]{companies.get(i).getName(),
						Integer.toString(companies.get(i).getTierLevel()),
						df.format(companies.get(i).getCurrentWorth()), 
						df.format(percentChange) + "%"});
			}
		}
		
		
		portfolio = new JTable(dtm);
		portfolio.getTableHeader().setReorderingAllowed(false);
		portfolio.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane portfolioScrollPane = new JScrollPane(portfolio);

		setLayout(new BorderLayout());
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(2, 1));
		
		JLabel playerPicture = new JLabel();
		Image image = ImageLibrary.getImage(user.getUserIconString());
		playerPicture.setIcon(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
		playerPicture.setBorder(new EmptyBorder(10, 10, 10, 10));
		playerPicture.setHorizontalAlignment(SwingConstants.CENTER);
//		ImageIcon ii = new ImageIcon(user.getUserIcon());
//		playerPicture.setIcon(ii);
		JLabel companyName = new JLabel(user.getCompanyName(), SwingConstants.CENTER);
		double userValue = user.getCurrentCapital();
		for (int i = 0; i < user.getCompanies().size(); i++){
			userValue += user.getCompanies().get(i).getCurrentWorth();
		}
		JLabel userValueLabel = new JLabel("Quarterly Value: " + String.format("%.2f", userValue) + Constants.million, SwingConstants.CENTER);
		//user.setUserBio("This is the User's bio. It's less than 144 characters.");
		user.setUserBio(user.getUserBio());
		JTextArea playerBio = new JTextArea(user.getUserBio());
		playerBio.setEditable(false);
		playerBio.setLineWrap(true);
		playerBio.setWrapStyleWord(true);
		playerBio.setBorder(new EmptyBorder(5,5,5,5));
		
		AppearanceSettings.setFont(AppearanceConstants.fontHeaderUser, companyName);
		AppearanceSettings.setFont(AppearanceConstants.fontFirmName, userValueLabel);
		AppearanceSettings.setFont(AppearanceConstants.fontSmall, playerBio);
		AppearanceSettings.setCenterAlignment(playerPicture, companyName, playerBio,
				userValueLabel);
		
		JPanel wordsPanel = new JPanel();
		wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.PAGE_AXIS));
		wordsPanel.add(companyName);
		wordsPanel.add(userValueLabel);
		wordsPanel.add(playerBio);
		
		westPanel.add(playerPicture);
		westPanel.add(wordsPanel);
		
		JPanel centerPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.add(portfolioScrollPane);
//		buttonPanel.add(trade);
		buttonPanel.add(sell);
		centerPanel.add(buttonPanel);
		
		westPanel.setPreferredSize(new Dimension(350, 0));
		add(westPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		
		/* Modify Appearances! */
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, wordsPanel, westPanel, playerInfo, playerBio, buttonPanel, this);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, centerPanel, portfolioScrollPane);
		AppearanceSettings.setBackground(AppearanceConstants.offWhite, portfolio);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, companyName, playerBio, sell, userValueLabel);
		AppearanceSettings.setBackground(AppearanceConstants.mediumGray,  sell);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, sell);
		AppearanceSettings.unSetBorderOnButtons(sell);
		AppearanceSettings.setOpaque(sell);
		
		if(user.getUsername().equals(gameFrame.user.getUsername())) {
//			trade.setEnabled(false);
			sell.setEnabled(true);
		} else {
//			trade.setEnabled(true);
			sell.setEnabled(false);
		}
	}
	
	private void addActionListeners() {
		
		sell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the selected company
				int selectedRow = portfolio.getSelectedRow();
				
				if(!gameFrame.networked) {
					if (selectedRow != -1) {
			        	TableModel dtm = (TableModel) portfolio.getModel();
						Company selectedCompany = gameFrame.game.returnCompany((String) dtm.getValueAt(selectedRow, 0));
						
						// Sell the company
						gameFrame.user.deleteCompany(selectedCompany);
						
						// Update our GUI to reflect current capital
						gameFrame.header.updateCurrentCapital();
						
						// Remove from table
						dtm.removeRow(selectedRow);
						
						// Make the stuff needed to insert
						double percentChange = (selectedCompany.getCurrentWorth() - selectedCompany.getStartingPrice())/selectedCompany.getStartingPrice() * 100;
						DecimalFormat df = new DecimalFormat("#.##");
						
						// Get the free agents table
						JTable freeAgentTable = qg.getFreeAgentTable();
			        	TableModel freeAgentDtm = (TableModel) freeAgentTable.getModel();
			        	
						if(selectedCompany.getCurrentWorth() != 0) {
				        	freeAgentDtm.addRow(new Object[]{selectedCompany.getName(), 
									Integer.toString(selectedCompany.getTierLevel()),
									Double.toString(selectedCompany.getCurrentWorth()),
									df.format(percentChange) + "%" });
						}
			        	
						//update the notifications
						String update;
						if (selectedCompany.getCurrentWorth() == 0) {
							update = gameFrame.user.getCompanyName() + " liquidated " + selectedCompany.getName() + ".";
						} else {
							update = gameFrame.user.getCompanyName() + " sold " + selectedCompany.getName() + ".";
						}
						qg.sendUpdate(update);
					}
				}
				//TODO networked game version of when sell button pressed
				else {
					//if there is a row selected and it is the user's actual player page
					if (selectedRow != -1 && gameFrame.user.equals(user)) {
			        	TableModel dtm = (TableModel) portfolio.getModel();
						Company selectedCompany = gameFrame.game.returnCompany((String) dtm.getValueAt(selectedRow, 0));
						gameFrame.getClient().sendMessage(new SellMessage(gameFrame.user.getUsername(), selectedCompany, selectedRow));
						// Sell the company
//						gameFrame.user.deleteCompany(selectedCompany);
						
						// Update our GUI to reflect current capital
//						gameFrame.header.updateCurrentCapital();
						
						//TODO
						/**
						 * Send a message with either selectedCompany or selectedRow and the user
						 * to all clients. When the clients recieve this message they should 
						 * remove the selected row from the correct playerTab and then add the row to free agents
						 * Base these changes in every client off the commented code below:
						 */
						
						//create message with User, selectedCompany, and selectedRow
						//call removeFromPlayerTab(User user, Company selectedCompany, int selectedRow)
						
						
					}
				}
				
			}
		});
		
//		trade.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent ae) {
//				//TODO handle the trade in networked game
//				//send every user to the tradeGUI
//				if (gameFrame.networked) {
//					// Determine the initiator and target of the trade (both Users)
//					User initiator = gameFrame.user;
//					User target = qg.getTabToUser().get(currentTab);
//					InitiateTradeMessage itm = new InitiateTradeMessage(initiator, target);
//					gameFrame.getClient().sendMessage(itm);
//				}
//			}
//		});
	}

	public JTable getTable() {
		return portfolio;
	}
	
	public void userSell(String username, Company company, int selectedRow) {
		removeFromPlayerTab(user, company, selectedRow);
		user.deleteCompany(company);
		if(username.equals(gameFrame.user.getUsername())) {
			gameFrame.user = user;
			gameFrame.header.updateCurrentCapital();
		}
	}
	
	public void removeFromPlayerTab(User user, Company selectedCompany, int selectedRow) {
		
		// Remove from table
		PlayerTab pt = qg.getUserToTab().get(user);
		JTable userTable = pt.getTable();
		TableModel userDtm = (TableModel) userTable.getModel();
		userDtm.removeRow(selectedRow);
		
		// Make the stuff needed to insert
		double percentChange = (selectedCompany.getCurrentWorth() - selectedCompany.getStartingPrice())/selectedCompany.getStartingPrice() * 100;
		DecimalFormat df = new DecimalFormat("#.##");
		
		// Get the free agents table
		JTable freeAgentTable = qg.getFreeAgentTable();
    	TableModel freeAgentDtm = (TableModel) freeAgentTable.getModel();
    	
		if(selectedCompany.getCurrentWorth() != 0) {
        	freeAgentDtm.addRow(new Object[]{selectedCompany.getName(), 
					Integer.toString(selectedCompany.getTierLevel()),
					df.format(selectedCompany.getCurrentWorth()),
					df.format(percentChange) + "%" });
		}
    	
		//update the notifications
		String update;
		if (selectedCompany.getCurrentWorth() == 0) {
			update = user.getCompanyName() + " liquidated " + selectedCompany.getName() + ".";
		} else {
			update = user.getCompanyName() + " sold " + selectedCompany.getName() + ".";
		}
		qg.sendUpdate(update);
		
	}


	public JButton getSellButton() {
		return sell;
	}
} 
