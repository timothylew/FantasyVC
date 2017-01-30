package guis;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.Client;
import gameplay.Company;
import gameplay.GameFrame;
import gameplay.User;
import listeners.DisabledItemSelectionModel;
import listeners.TableModel;
import messages.BeginAuctionBidMessage;
import utility.AppearanceConstants;
import utility.AppearanceSettings;
import utility.Constants;

/**
 * @author dannypan
 */
public class AuctionTeamList extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//variables used here
	private JLabel timer, middleFirmPicture, firmCurrentMoney, middleFirmName,
	purchasedFirmsLabel, detailsFirmPicture, detailsFirmCurrentMoney, detailsFirmName,
	detailsCompanyPicture,detailsPurchasedLabel ,detailsCompanyName;
	private JTextArea detailsCompanyBio;
	private Vector<String> firms, purchasedFirms;
	private JList<String> firmList, purchasedCompanysList, detailsFirmPurchasedList;
	private JTable firmData, detailsCompanyInfo;
	private JButton bidButton;
	private JPanel companyDetailsPanel;
	
	private Client client;
	private GameFrame gameFrame;
	private Vector<Company> companyVect;
	private Vector<User> order;
	private HashMap<String, Vector<String>> companyLists;
	private int turnsPerPerson = 5;
	
	
	public AuctionTeamList(Client client, GameFrame gameFrame) {
		this.gameFrame = gameFrame;
		this.client = client;
		//initialize companyLists
		companyLists = new HashMap<String, Vector<String>>();
		for(User user : gameFrame.game.getUsers()) {
			companyLists.put(user.getUsername(), new Vector<String>());
		}
		
		intializeVariables();
		createGUI();
		addActionListeners();
		gameFrame.header.updateCurrentCapital();
		firmData.getTableHeader().setReorderingAllowed(false);
		detailsCompanyInfo.getTableHeader().setReorderingAllowed(false);
	}
	
	public void updateTimer(String display) {
		timer.setText(display);
		this.revalidate();
		this.repaint();
	}
	
	public void nextPlayer() {
		System.out.println(order + " " + order.size());
		order.remove(0);
		if(!order.isEmpty()) {
			updateMiddleFirmName(order.get(0).getCompanyName());
			middleFirmPicture.setIcon(new ImageIcon(order.get(0).getUserIcon().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		}
	}
	
	public String getCurrent() {
		if(order.isEmpty()) {
			return null;
		}
		return order.get(0).getUsername();
	}
	
	public void updateCapital() {
		if(!order.isEmpty()){
			for (User user : client.getUsers()) {
				if(user.getUsername().equals(order.get(0).getUsername())) {
					firmCurrentMoney.setText(Constants.currentCapital + String.format("%.2f", user.getCurrentCapital()) +
							 Constants.million);
				}
			}
		}
		this.revalidate();
		this.repaint();
	}
		
	private void intializeVariables(){
		
		//Variables for left panel
		timer = new JLabel("GUEST", SwingConstants.CENTER);
		firms = new Vector<String>();
		middleFirmName = new JLabel(gameFrame.user.getCompanyName());
		
		if (!gameFrame.networked) {
			bidButton = new JButton("BUY"); 
			firms.add(" ");
			firms.add("Buy 8 companies to start playing!");
			
		} else {
			bidButton = new JButton("BID");
			order = new Vector<User>();
			//TODO changed form 1 to 5
			for (int i = 0; i < turnsPerPerson; i++) {
				for(User user : client.getUsers()) {
					order.add(user);
				}
			}
			setDraftOrder();
			middleFirmName = new JLabel(client.getUsers().get(0).getUsername());
			updateMiddleFirmName(order.get(0).getCompanyName());
			timer.setText("0:45");
		}
		
		
		firmList = new JList<String>(firms);
		
		if (!gameFrame.networked) { 
			DefaultListCellRenderer renderer = (DefaultListCellRenderer)firmList.getCellRenderer();  
			renderer.setHorizontalAlignment(JLabel.LEFT);
			firmList.setFocusable(false);
			AppearanceSettings.setFont(AppearanceConstants.fontSmallest, firmList);

		} else {
			//Set List to center text alignment
			DefaultListCellRenderer renderer = (DefaultListCellRenderer)firmList.getCellRenderer();  
			renderer.setHorizontalAlignment(JLabel.CENTER);
			AppearanceSettings.setFont(AppearanceConstants.fontMedium, firmList);

		}
		//Variables for middle panel
		DecimalFormat df = new DecimalFormat("#.##");
		middleFirmPicture = new JLabel();
		middleFirmPicture.setPreferredSize(new Dimension(100,100));
		firmCurrentMoney = new JLabel(Constants.currentCapital + String.format("%.2f", gameFrame.user.getCurrentCapital()) +
				 Constants.million);
		purchasedFirmsLabel = new JLabel("Purchased Firms", SwingConstants.CENTER);
		purchasedFirms = new Vector<String>();
		
		String[] columnNames = {"Name", "Tier Level", "Asking Price (Millions)"};
		TableModel dtm = new TableModel();
		dtm.setColumnIdentifiers(columnNames);
		companyVect = gameFrame.getGame().getCompanies();
		System.out.println("Abstergo: " + gameFrame.getGame().getCompanies().get(0).getStartingPrice());
		for(int i = 0; i < companyVect.size(); i++){
			dtm.addRow(new Object[]{companyVect.get(i).getName(), Integer.toString(companyVect.get(i).getTierLevel()),
					df.format(companyVect.get(i).getStartingPrice())});
		}
		firmData = new JTable(dtm);
		firmData.setBackground(AppearanceConstants.darkBlue);
		firmData.setForeground(AppearanceConstants.darkBlue);
		firmData.setFont(AppearanceConstants.fontSmallest);
		
		//Placed down here for testing purposes
		purchasedCompanysList = new JList<String>();

		
		//Variables for firm details
		detailsFirmPicture = new JLabel();
		detailsFirmPicture.setPreferredSize(new Dimension(100,100));
		detailsFirmCurrentMoney = new JLabel("Current Capital: ", SwingConstants.CENTER);
		detailsFirmName = new JLabel("Guestbros", SwingConstants.CENTER);
		detailsFirmName.setFont(AppearanceConstants.fontFirmName);
		detailsPurchasedLabel = new JLabel("Purchased Firms", SwingConstants.CENTER);
		detailsCompanyPicture = new JLabel();
		detailsCompanyPicture.setPreferredSize(new Dimension(100,100));
		detailsCompanyName = new JLabel();
		detailsCompanyBio = new JTextArea();
		detailsCompanyBio.setLineWrap(true);
		detailsCompanyBio.setEditable(false);
		detailsCompanyBio.setWrapStyleWord(true);
		detailsCompanyInfo = new JTable();
		detailsCompanyInfo.setForeground(AppearanceConstants.darkBlue);
		detailsCompanyInfo.setBackground(AppearanceConstants.offWhite);
		detailsCompanyInfo.setFont(AppearanceConstants.fontSmallest);
		

		
		//Initialized here to purchased firms for testing purposes.
		detailsFirmPurchasedList = new JList<String>(purchasedFirms);

//		firmData.getSelectionModel().addSelectionInterval(0,0);
		
		// TODO: Set a default table selection so that if the timer runs out, it just picks the first option
		
		intializePictures();

	}
	
	public void updateMiddleFirmName(String username) {
		System.out.println(username);
		middleFirmName.setText(username);
		if (gameFrame.user.getCompanyName().equalsIgnoreCase(username)) {
			bidButton.setEnabled(true);
		}
		else {
			bidButton.setEnabled(false);
		}
	}
	
	//All of this just has to be updated with user images from company and user objects.
	private void intializePictures() {
		if (!gameFrame.networked) middleFirmPicture.setIcon(new ImageIcon(gameFrame.user.getUserIcon().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));	
		else middleFirmPicture.setIcon(new ImageIcon(order.get(0).getUserIcon().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}
	
	//Function that fills in the random test team names
//	private void testDraftOrder() {
//		for (int i = 0; i < 20; i++){
//			firms.add("Team " + Integer.toString((i%4) + 1));
//		}
//	}
	
	public void setDraftOrder() {
		firms.removeAllElements();
		for(User user : order) {
			firms.add(user.getCompanyName());
		}
		this.revalidate();
		this.repaint();
	}
	
	private void createGUI() {
		//Size accounts of chatbox and header
		setPreferredSize(new Dimension(1280,504));
		setBackground(AppearanceConstants.lightBlue);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		//Code is designed so each panel has it's own create function to be modular
		JPanel draftOrderPanel = createDraftOrderPanel();
		JPanel companyListPanel = createCompanyListPanel();
		companyDetailsPanel = createCompanyDetailsPanel();
		
		//Adding Subpanels to manually b/c appearance settings function doesn't like
		//inherited panels
		add(Box.createHorizontalGlue());
		add(draftOrderPanel);
		add(Box.createHorizontalGlue());
		add(companyListPanel);
		add(Box.createHorizontalGlue());
		add(companyDetailsPanel);
		add(Box.createHorizontalGlue());
	}
	
	//Creates and returns the Draft Order Panel;
	private JPanel createDraftOrderPanel(){
		JPanel draftOrderPanel = new JPanel();
		JPanel timePanel = new JPanel();
		JScrollPane listPanel = new JScrollPane(firmList);
		
		listPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		//Appearance Adjustments
		timePanel.setLayout(new BorderLayout());
		draftOrderPanel.setLayout(new BorderLayout());
		
		AppearanceSettings.setSize(250, 450, draftOrderPanel);
		draftOrderPanel.setMaximumSize(new Dimension(250,450));
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, draftOrderPanel, listPanel, timePanel, firmList);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, timer, firmList);
		timer.setFont(AppearanceConstants.fontTimerLarge);
		
  
		listPanel.setFocusable(false);
		listPanel.setBorder(null);
		
		//Adding individual components to respective panels
		timePanel.add(timer, BorderLayout.CENTER);
		timePanel.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.SOUTH);
				
		//Adding everything to main panel
		draftOrderPanel.add(timePanel, BorderLayout.NORTH);
		draftOrderPanel.add(listPanel, BorderLayout.CENTER);
		
		return draftOrderPanel;
	}
	
	//Creates and return the comapny list Panel
	private JPanel createCompanyListPanel(){
		JPanel companyListPanel = new JPanel();
		//Top spacing is used to hold spacing and a JSeparator
		JPanel topSpacingPanel = new JPanel();
		JPanel firmDetailsPanel = new JPanel();
		JPanel firmStatsPanel = new JPanel();
		JPanel purchasedListPanel = new JPanel();
		JScrollPane purchasedScrollPane = new JScrollPane(purchasedCompanysList);
		JScrollPane companyListPane = new JScrollPane(firmData);
		
		//Tring to remove borders from the scroll paes
		purchasedScrollPane.setFocusable(false);
		purchasedScrollPane.setBorder(null);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer)purchasedCompanysList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);  
		
		//Remove borders
		companyListPane.setFocusable(false);
		companyListPane.setBorder(BorderFactory.createEmptyBorder());
		companyListPane.getViewport().setBackground(AppearanceConstants.darkBlue);
		
		//Add padding
		purchasedCompanysList.setBorder(new EmptyBorder(5,5,5,5));
		purchasedFirmsLabel.setBorder(new EmptyBorder(5,5,5,5));

		
		//Layouts are set
		AppearanceSettings.setBoxLayout(BoxLayout.PAGE_AXIS, topSpacingPanel);
		AppearanceSettings.setBoxLayout(BoxLayout.LINE_AXIS, firmDetailsPanel);
		companyListPanel.setLayout(new BorderLayout());
		purchasedListPanel.setLayout(new BorderLayout());
		firmStatsPanel.setLayout(new GridLayout(2,1,5,5));
		
		//Appearance Settings are changed
		AppearanceSettings.setSize(650, 450, companyListPanel);
		companyListPanel.setMaximumSize(new Dimension(650,450));
		AppearanceSettings.setSize(650, 100, firmDetailsPanel);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, companyListPanel, firmDetailsPanel,
				firmStatsPanel, purchasedScrollPane, purchasedCompanysList, topSpacingPanel,companyListPane,
				purchasedListPanel);
		AppearanceSettings.setBackground(AppearanceConstants.offWhite, firmData);

		AppearanceSettings.setForeground(AppearanceConstants.offWhite, firmCurrentMoney, middleFirmName,
				purchasedCompanysList, purchasedFirmsLabel, companyListPane);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, purchasedCompanysList, firmCurrentMoney,
				firmCurrentMoney, middleFirmName, purchasedFirmsLabel);

		//firmstats adding in sequence
		firmStatsPanel.add(middleFirmName);
		//firmStatsPanel.add(purchasedFirmsLabel);
		firmStatsPanel.add(firmCurrentMoney);
		//firmStatsPanel.add(purchasedScrollPane);
		
		purchasedListPanel.add(purchasedFirmsLabel, BorderLayout.NORTH);
		purchasedListPanel.add(purchasedScrollPane, BorderLayout.CENTER);

		
		//Add firm details panel in sequence
		firmDetailsPanel.add(Box.createHorizontalStrut(10));
		firmDetailsPanel.add(middleFirmPicture);
		firmDetailsPanel.add(Box.createHorizontalStrut(20));
		firmDetailsPanel.add(firmStatsPanel);
		firmDetailsPanel.add(Box.createHorizontalStrut(10));
		firmDetailsPanel.add(purchasedListPanel);
		
		//Add everything to top spacing panel so we can have a divider
		topSpacingPanel.add(Box.createVerticalStrut(5));
		topSpacingPanel.add(firmDetailsPanel);
		topSpacingPanel.add(new JSeparator(JSeparator.HORIZONTAL));

		
		companyListPanel.add(topSpacingPanel, BorderLayout.NORTH);
		companyListPanel.add(companyListPane, BorderLayout.CENTER);
		
		return companyListPanel;
	}
	
	//Creates and return the Company Details Panel
	private JPanel createCompanyDetailsPanel(){
		JPanel cardSwapPanel = new JPanel();
		JPanel companyDetailsPanel = new JPanel();
		JPanel companyInfoPanel = new JPanel();
		JScrollPane companyTablePane = new JScrollPane(detailsCompanyInfo);
		JScrollPane companyBioPane = new JScrollPane(detailsCompanyBio);
		JPanel firmDetailsPanel = new JPanel();
		JPanel firmInfoPanel = new JPanel();
		JScrollPane firmListPane = new JScrollPane(detailsFirmPurchasedList);
		JPanel bidButtonPanel = new JPanel();
		
		//Tring to remove borders from the panes
		companyTablePane.setFocusable(false);
		companyTablePane.setBorder(BorderFactory.createEmptyBorder());
		companyTablePane.getViewport().setBackground(AppearanceConstants.darkBlue);
		firmListPane.setFocusable(false);
		firmListPane.setBorder(null);
		companyBioPane.setFocusable(false);
		companyBioPane.setBorder(null);
		
		//Set purchased list text to be centered
		DefaultListCellRenderer renderer = (DefaultListCellRenderer)detailsFirmPurchasedList.getCellRenderer();  
		renderer.setHorizontalAlignment(JLabel.CENTER);  
		
		//Set Layouts
		cardSwapPanel.setLayout(new CardLayout());
		companyDetailsPanel.setLayout(new BorderLayout());
		firmDetailsPanel.setLayout(new BorderLayout());
		AppearanceSettings.setBoxLayout(BoxLayout.PAGE_AXIS, firmInfoPanel,companyInfoPanel);
	
		//Appearance Settings
		AppearanceSettings.setSize(250, 450, cardSwapPanel, firmDetailsPanel, companyDetailsPanel);
		cardSwapPanel.setMaximumSize(new Dimension(250,450));
		firmDetailsPanel.setMaximumSize(new Dimension(250,450));
		companyDetailsPanel.setMaximumSize(new Dimension(250,450));
		AppearanceSettings.setSize(250,150, companyBioPane);
		AppearanceSettings.setSize(100,40, bidButton);
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, cardSwapPanel, firmDetailsPanel,
				companyDetailsPanel,companyInfoPanel,firmInfoPanel,detailsFirmCurrentMoney, detailsFirmName,
				detailsCompanyName, detailsCompanyBio, detailsFirmPurchasedList,
				detailsFirmPicture, detailsCompanyPicture, purchasedCompanysList, companyTablePane,
				firmListPane,detailsPurchasedLabel, companyBioPane, bidButtonPanel);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, detailsFirmCurrentMoney, detailsFirmName,
				detailsCompanyName, detailsCompanyBio, detailsFirmPurchasedList,
				purchasedCompanysList, detailsPurchasedLabel, bidButton);
		AppearanceSettings.setFont(AppearanceConstants.fontSmallest, detailsFirmCurrentMoney, detailsFirmName,
				detailsCompanyName, detailsFirmPurchasedList, purchasedCompanysList, detailsPurchasedLabel);
		detailsCompanyBio.setFont(AppearanceConstants.fontSmallBidButton);
		detailsCompanyBio.setBorder(new EmptyBorder(5,5,5,5));
		//Bid Button Appearance Settings
		bidButton.setOpaque(true);
		bidButton.setFont(AppearanceConstants.fontLargeBidButton);
		bidButton.setBackground(AppearanceConstants.green);
		bidButton.setBorderPainted(false);
		//Function to Align all Labels to the center of the BoxLayout.
		//Needs to be added to Appearance Settings.
		AppearanceSettings.setCenterAlignment(detailsFirmPicture, detailsFirmName, detailsFirmCurrentMoney,
				detailsPurchasedLabel, detailsCompanyPicture, detailsCompanyName);
		
		//Adding Firm Info panel objects
		firmInfoPanel.add(Box.createVerticalStrut(20));
		firmInfoPanel.add(detailsFirmPicture);
		firmInfoPanel.add(Box.createVerticalStrut(10));
		firmInfoPanel.add(detailsFirmName);
		firmInfoPanel.add(Box.createVerticalStrut(10));
		firmInfoPanel.add(detailsFirmCurrentMoney);
		firmInfoPanel.add(Box.createVerticalStrut(10));
		firmInfoPanel.add(detailsPurchasedLabel);
		firmInfoPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		//Finishing firm details Panel
		firmDetailsPanel.add(firmInfoPanel, BorderLayout.NORTH);
		firmDetailsPanel.add(firmListPane, BorderLayout.CENTER);
		
		//Adding Company Info panel objects
		companyInfoPanel.add(Box.createVerticalStrut(20));
		companyInfoPanel.add(detailsCompanyPicture);
		companyInfoPanel.add(Box.createVerticalStrut(10));
		companyInfoPanel.add(detailsCompanyName);
		companyInfoPanel.add(Box.createVerticalStrut(10));
		companyInfoPanel.add(companyBioPane);
		companyInfoPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		bidButtonPanel.add(bidButton);
		
		//Adding everything to company details Panel
		companyDetailsPanel.add(companyInfoPanel, BorderLayout.NORTH);
		companyDetailsPanel.add(companyTablePane, BorderLayout.CENTER);
		companyDetailsPanel.add(bidButtonPanel, BorderLayout.SOUTH);
		
		//Add both layouts into the card layout.
		cardSwapPanel.add(companyDetailsPanel, "Company");
		cardSwapPanel.add(firmDetailsPanel, "Firm");
		
		return cardSwapPanel;
	}
	
	private void addActionListeners(){
		//List selection listener for draft order list
		firmList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
	            //TODO Add some sort of function to update detail panel
				User selectedUser = gameFrame.game.returnUser(firmList.getSelectedValue());
				if(gameFrame.networked) {
					for(User user : client.users) {
						if(user.getUsername().equals(selectedUser.getUsername())) {
							selectedUser = user;
						}
					}
				}
				
				System.out.println(selectedUser.getUsername());
				
				detailsFirmPicture.setIcon(new ImageIcon(selectedUser.getUserIcon().getScaledInstance(100, 100,Image.SCALE_SMOOTH)));
				detailsFirmName.setText(selectedUser.getCompanyName());
				detailsFirmCurrentMoney.setText("" + new DecimalFormat("#.##").format(selectedUser.getCurrentCapital()) + Constants.million);
				//TODO
				// Populate temporary string vector to insert into the list
				Vector<String> companyNames = new Vector<String>();
				for(Company company: selectedUser.getCompanies()){
					companyNames.add(company.getName());
				}	
				detailsFirmPurchasedList.setListData(companyNames);
				
				CardLayout cardLayout = (CardLayout) companyDetailsPanel.getLayout();
				cardLayout.show(companyDetailsPanel, "Firm");
				

			}
			
		});
		
		//Listener for the purschased firms list at the top
		purchasedCompanysList.addListSelectionListener(new ListSelectionListener(){

			@Override 
			public void valueChanged(ListSelectionEvent e) {
				if(purchasedCompanysList.getSelectedValue() != null){
		        	Company selectedCompany = gameFrame.game.returnCompany(purchasedCompanysList.getSelectedValue());
		        	detailsCompanyName.setText(selectedCompany.getName());
	
		        	detailsCompanyPicture.setIcon(new ImageIcon(selectedCompany.getCompanyLogo().getScaledInstance((int)(100*selectedCompany.getAspectRatio()), 100,  java.awt.Image.SCALE_SMOOTH)));
		        	detailsCompanyBio.setText(selectedCompany.getDescription());
		        	
		        	//detailsCompanyInfo
		        	Object[][] companyData = {
		        			{"Name", selectedCompany.getName()},
		        			{"Tier", selectedCompany.getTierLevel()},
		        			{"Asking Price", selectedCompany.getAskingPrice()},
		        			{"Current Worth", selectedCompany.getCurrentWorth()},
		        	};
		        	String[] columnNames = {"",""};
		        	TableModel dtm = new TableModel();
		        	dtm.setDataVector(companyData, columnNames);
		   	       	detailsCompanyInfo.setModel(dtm);
					
					CardLayout cardLayout = (CardLayout) companyDetailsPanel.getLayout();
					cardLayout.show(companyDetailsPanel, "Company");
				}
			}
			
		});
		
		//Maybe we use a mouse listener? We'll see in the future
		firmData.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		firmData.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	
	        	int selectedRow = firmData.getSelectedRow();
	        	TableModel dtm = (TableModel)firmData.getModel();
	        	
				Company selectedCompany = gameFrame.game.returnCompany((String)dtm.getValueAt(selectedRow, 0));
	        	detailsCompanyName.setText(selectedCompany.getName());

	        	detailsCompanyPicture.setIcon(new ImageIcon(selectedCompany.getCompanyLogo().getScaledInstance((int)(100*companyVect.get(selectedRow).getAspectRatio()), 100, Image.SCALE_SMOOTH)));
	        	detailsCompanyBio.setText(selectedCompany.getDescription());
	        	
	        	//detailsCompanyInfo
	        	Object[][] companyData = {
	        			{"Name", selectedCompany.getName()},
	        			{"Tier", selectedCompany.getTierLevel()},
	        			{"Asking Price", selectedCompany.getAskingPrice()}
	        	};
	        	String[] columnNames = {"",""};
	        	dtm = new TableModel();
	        	dtm.setDataVector(companyData, columnNames);
	   	       	detailsCompanyInfo.setModel(dtm);
	   	       		        	
				CardLayout cardLayout = (CardLayout)companyDetailsPanel.getLayout();
				cardLayout.show(companyDetailsPanel, "Company");
	        }
		});
				
		//Action Listener to make purchased firms list read only
		detailsFirmPurchasedList.setSelectionModel(new DisabledItemSelectionModel());
		detailsFirmPurchasedList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		
		bidButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Bid button fired");
				if (gameFrame.networked) {
					networkBidButtonAction();
				} else {	
					int selectedRow = firmData.getSelectedRow();
					firmData.getSelectionModel().addSelectionInterval((selectedRow+1)%firmData.getRowCount(),
							(selectedRow+1)%firmData.getRowCount());
					TableModel dtm = (TableModel) firmData.getModel();
					Company selectedCompany = gameFrame.game.returnCompany((String)dtm.getValueAt(selectedRow, 0));
					
					purchasedFirms.add(selectedCompany.getName());
					purchasedCompanysList.setListData(purchasedFirms);
					gameFrame.user.addCompany(selectedCompany);
					
					firmCurrentMoney.setText(Constants.currentCapital + String.format("%.2f", gameFrame.user.getCurrentCapital()) + Constants.million);
					gameFrame.header.updateCurrentCapital();
					dtm.removeRow(selectedRow);
					firmData.revalidate();
					firmData.repaint();
					
					if(gameFrame.user.getCompanies().size() == 8) {
						gameFrame.changePanel(new TimelapsePanel(client, gameFrame));
					}
				}
			}
			
		});
		
		//We might have to add empty action listeners to the tables to prevent
		//editing of data
	}
	
	public void networkBidButtonAction() {
		int selectedRow = firmData.getSelectedRow();
		if(selectedRow == -1) {
			selectedRow = 0;
		}
		TableModel dtm = (TableModel) firmData.getModel();
		Company selectedCompany = gameFrame.game.returnCompany((String)dtm.getValueAt(selectedRow, 0));
		System.out.println(selectedCompany.getName());
		BeginAuctionBidMessage message = new BeginAuctionBidMessage(selectedCompany, gameFrame.getClient().getUser().getCompanyName(), selectedRow);
		System.out.println("attempting to bid upon " + message.getCompany().getName());
		client.sendMessage(message);
	}
	
	public void removeRow(int selectedRow) {
		firmData.getSelectionModel().addSelectionInterval((selectedRow+1)%firmData.getRowCount(),
				(selectedRow+1)%firmData.getRowCount());
		TableModel dtm = (TableModel) firmData.getModel();
		dtm.removeRow(selectedRow);
		firmData.revalidate();
		firmData.repaint();
	}
	
	public void updateFirmAssets(String username, String companyName) {
		companyLists.get(username).add(companyName);
	}
	
	public void updateDisplayedFirmAssets() {
		
		//fill the new companies
		for(User user : client.users) {
			if(user.getUsername().equals(getCurrent())) {
				Vector<String> companyNames = new Vector<String>();
				for(Company company: user.getCompanies()){
					companyNames.add(company.getName());
				}	
				purchasedCompanysList.setListData(companyNames);
			}
		}
	}
}