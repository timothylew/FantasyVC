package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import gameplay.Lobby;
import messages.CreateGameMessage;
import utility.AppearanceConstants;
import utility.AppearanceSettings;

public class CreateGameGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel createLabel, lobbyLabel, sizeLabel, warningLabel;
	JButton cancelButton, createButton;
	JTextField lobbyName;
	JComboBox<Integer> size;
	JSlider slider;
	public IntroPanel ip;
	
	public CreateGameGUI(IntroPanel ip) {
		this.ip = ip;
		initializeComponents();
		createGUI();
		addEvents();
		this.setResizable(false);
	}
	
	private void initializeComponents() {
		createLabel = new JLabel("Create Game");
		lobbyLabel = new JLabel("Lobby Name");
		sizeLabel = new JLabel("Lobby Size");
		warningLabel = new JLabel("");
		
		cancelButton = new JButton("Cancel");
		createButton = new JButton("Create");
		
		lobbyName = new JTextField();
		
		Integer [] ints = {2, 3, 4};
		size = new JComboBox<Integer>(ints);
		slider = new JSlider(2, 4, 2);
		slider.setOpaque(false);
	}
	
	private void createGUI() {
		this.setSize(360, 480);
		this.getContentPane().setBackground(AppearanceConstants.lightBlue);
		
		createLabel.setFont(AppearanceConstants.fontLarge);
		createLabel.setForeground(AppearanceConstants.offWhite);
		createLabel.setHorizontalAlignment(JLabel.CENTER);
		createLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
		
		this.add(createLabel, BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2, 16, 16));
		buttonPanel.setBorder(new EmptyBorder(40, 16, 40, 16));
		makeButton(cancelButton, createButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(createButton);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		lobbyLabel.setFont(AppearanceConstants.fontMedium);
		lobbyLabel.setForeground(AppearanceConstants.offWhite);
		lobbyLabel.setHorizontalAlignment(JLabel.CENTER);
		northPanel.add(lobbyLabel, BorderLayout.NORTH);
		
		lobbyName.setFont(AppearanceConstants.fontMedium);
		lobbyName.setBorder(new EmptyBorder(10,4,10,4));
		northPanel.add(lobbyName);
		
		warningLabel.setFont(AppearanceConstants.fontSmall);
		warningLabel.setForeground(Color.red);
		warningLabel.setHorizontalAlignment(JLabel.CENTER);
		northPanel.add(warningLabel, BorderLayout.SOUTH);
		
		centerPanel.add(northPanel, BorderLayout.NORTH);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());

		sizeLabel.setFont(AppearanceConstants.fontMedium);
		sizeLabel.setForeground(Color.white);
		sizeLabel.setHorizontalAlignment(JLabel.CENTER);
		southPanel.add(sizeLabel, BorderLayout.NORTH);
		
		size.setFont(AppearanceConstants.fontSmall);
		size.setBackground(AppearanceConstants.offWhite);
		((JLabel) size.getRenderer()).setBorder(new EmptyBorder(0, 120, 0, 0));
		size.setFocusable(false);
		size.setPreferredSize(new Dimension(0, 20));
//		southPanel.add(new JPanel().add(size));
		
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(1);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setPaintTrack(true);
		AppearanceSettings.setForeground(AppearanceConstants.offWhite, slider);
		AppearanceSettings.setFont(AppearanceConstants.fontMedium, slider);
		southPanel.add(new JPanel().add(slider));
		
		centerPanel.add(southPanel, BorderLayout.SOUTH);
		
		((JLabel) size.getRenderer()).setBorder(new EmptyBorder(0, 120, 0, 0));

		AppearanceSettings.unSetBorderOnButtons(cancelButton, createButton);
		AppearanceSettings.setOpaque(createButton, cancelButton);
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, buttonPanel, northPanel, centerPanel, southPanel);
		cancelButton.setBackground(AppearanceConstants.red);
		createButton.setBackground(AppearanceConstants.green);
		
		this.add(centerPanel);
		
		
	}
	
	private void addEvents() {
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		/*
		lobbyName.getDocument().addDocumentListener(new DocumentListener() {
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
				
			}
 		}); */
		lobbyName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }

			@Override
			public void keyPressed(KeyEvent e) { }

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!lobbyName.getText().equals("")) {
						for(Lobby lobbies : ip.getLobbies()) {
							if (lobbyName.getText().trim().equals(lobbies.getLobbyName())){
								JOptionPane.showMessageDialog(new JFrame(), "Lobby name already taken.", "Lobby name taken", JOptionPane.WARNING_MESSAGE);
								return;
							}
						}
						
						CreateGameMessage cgm = new CreateGameMessage(lobbyName.getText(), slider.getValue(), ip.gameFrame.user);
//						CreateGameMessage cgm = new CreateGameMessage(lobbyName.getText(), size.getItemAt(size.getSelectedIndex()), ip.gameFrame.user);
						ip.gameFrame.getClient().sendMessage(cgm);
						dispose();
					}
					else {
						warningLabel.setText("Please enter a lobby name.");
					}
				}
			}
		});
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(!lobbyName.getText().equals("")) {
					for(Lobby lobbies : ip.getLobbies()) {
						if (lobbyName.getText().trim().equals(lobbies.getLobbyName())){
							JOptionPane.showMessageDialog(new JFrame(), "Lobby name already taken.", "Lobby name taken", JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
					CreateGameMessage cgm = new CreateGameMessage(lobbyName.getText(), slider.getValue(), ip.gameFrame.user);
//					CreateGameMessage cgm = new CreateGameMessage(lobbyName.getText(), size.getItemAt(size.getSelectedIndex()), ip.gameFrame.user);
					ip.gameFrame.getClient().sendMessage(cgm);
					dispose();
				}
				else {
					warningLabel.setText("Please enter a lobby name.");
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
	}
	
	public void makeButton(JButton... button) {
		for (JButton b : button) {
			b.setForeground(AppearanceConstants.offWhite);
			b.setFont(AppearanceConstants.fontMedium);
		}
	}
}
