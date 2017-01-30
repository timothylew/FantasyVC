package guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;

import client.Client;
import gameplay.User;
import messages.ChatMessage;
import utility.AppearanceConstants;
import utility.AppearanceSettings;

/**
 * The {@code ChatPanel} is an extension of {@code JPanel} 
 * that can be passed along between graphical interfaces
 * for the user to communicate with other players.
 * @author alancoon
 *
 */
public class ChatPanel extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;
	private Client client;
	private JTextArea chat;
	private JTextField input;
	private JButton submit;
	private JScrollPane scroll;
	
	private final boolean networked;
	
	/**
	 * Singleplayer ChatPanel.
	 * @param user
	 */
	public ChatPanel(User user) { 
		this.user = user;
		this.networked = false;
		initializeComponents();
		createGUI();
		addEvents();
		colorizeComponents();
	}
	
	/**
	 * Multiplayer ChatPanel.
	 * @param client The player's client.
	 * @param user The player's user.
	 */
	public ChatPanel(Client client) { 
		this.client = client;
		this.user = client.getUser();
		this.networked = true;
		initializeComponents();
		createGUI();
		addEvents();
		colorizeComponents();
	}


	/**
	 * Initialize the data member GUI components we need for this panel.
	 */
	private void initializeComponents() {
		input = new JTextField(30);
		input.setBorder(null);
		input.setFont(AppearanceConstants.fontSmallest);
		input.setText("Chat...");

		submit = new JButton("Send");
		submit.setFont(AppearanceConstants.fontButtonSmall);
		submit.setEnabled(false);
		
		chat = new JTextArea();
		chat.setWrapStyleWord(true);
		chat.setLineWrap(true);
		chat.setEditable(false);
		chat.setFont(AppearanceConstants.fontSmallest);
		DefaultCaret caret = ((DefaultCaret)chat.getCaret());
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		//Testing purposes

	}
	
	/**
	 * Just to color the data member GUI components.
	 */
	private void colorizeComponents() {
		// TODO change once we get the AppearanceSettings/Constants up and running
		
		// Pimp my button
		AppearanceSettings.unSetBorderOnButtons(submit);
		AppearanceSettings.setOpaque(submit);
		submit.setBackground(AppearanceConstants.lightBlue);
		
		input.setBackground(AppearanceConstants.darkBlue);
		
		chat.setBackground(AppearanceConstants.darkBlue);
		
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, submit, chat, input);
		input.setCaretColor(AppearanceConstants.offWhite);
	}
	
	/**
	 * Assembles the GUI.  Mostly generated.
	 */
	private void createGUI() {
		
		// Look here
		setPreferredSize(new Dimension(1280, 144));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(AppearanceConstants.darkBlue);

		
		scroll = new JScrollPane(chat);
		scroll.setMaximumSize(new Dimension(1280, 100));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setFocusable(false);
		scroll.setBorder(new EmptyBorder(5,5,5,5));
		AppearanceSettings.setSize(1280, 100, scroll);
		
		JPanel textFieldPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		textFieldPanel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new BorderLayout());
		
		AppearanceSettings.setBackground(AppearanceConstants.darkBlue, scroll, textFieldPanel, buttonPanel,
				bottomPanel);
		AppearanceSettings.setSize(1000, 40, textFieldPanel);
		textFieldPanel.setMaximumSize(new Dimension(1000, 40));
		AppearanceSettings.setSize(100, 40, buttonPanel);
		buttonPanel.setMaximumSize(new Dimension(1000, 40));
		
		//Do borders
		buttonPanel.setBorder(new EmptyBorder(5,5,5,5));
		input.setBorder(new EmptyBorder(0,5,0,5));

		buttonPanel.add(submit);
		textFieldPanel.add(input);
		bottomPanel.add(textFieldPanel);
		bottomPanel.add(buttonPanel);
		
		add(scroll);
		add(new JSeparator(JSeparator.HORIZONTAL));
		add(bottomPanel);
				
	}

	/**
	 * Add listeners to the components.
	 */
	private void addEvents() {
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (networked) {
					if (!input.getText().isEmpty() && !input.getText().equals("Chat...")) {
						ChatMessage message = new ChatMessage(client.getUser().getUsername(), input.getText());
						System.out.println(message.getUsername() + " says: " + message.getMessage());
						input.setText("Chat...");
						submit.setEnabled(false);
						client.sendMessage(message);
					}
				} else {
					addChat(user.getUsername(), input.getText());
					input.setText("Chat...");
					submit.setEnabled(false);
				}
			} 
		});
		
		//Wrote custom one because it's a bit different than Emma's
		input.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
		    	input.setText("");
			}

			@Override
			public void focusLost(FocusEvent e) {
		    	if (input.getText().equals("")) {
		    		input.setText("Chat...");
		    		submit.setEnabled(false);
		    	}
			}
			
		});
		input.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!input.getText().trim().equals("") && !input.getText().trim().equals("Chat...")) {
						if (networked) {
							ChatMessage message = new ChatMessage(client.getUser().getUsername(), input.getText());
							input.setText("");
							client.sendMessage(message);
							submit.setEnabled(false);
						} else {
							addChat(user.getUsername(), input.getText());
							input.setText("");
							submit.setEnabled(false);
						}
					}
				}
			}
		});
		
		//Document listener for input
		input.getDocument().addDocumentListener(new DocumentListener() {
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
			
			/**
			 * Enable the submit button if the field's foreground color is black,
			 * which means that it isn't empty.
			 */
			private void changed() {
				submit.setEnabled(!input.getText().equals(""));
			}
		});
	}
	
	public void addChat(String username, String text) {
		chat.append('\n' + username + ": " + text);
		JScrollBar sb = scroll.getVerticalScrollBar();
		sb.setValue(sb.getMaximum());
	}
}
