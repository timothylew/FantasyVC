package listeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import client.Client;
import gameplay.GameFrame;
import guis.FinalGUI;
import messages.ClientExitMessage;
import messages.LeaveFinal;


/**
 * Will allow a {@code Client} to gracefully exit the game by using the
 * {@code JFrame}'s red exit button from the operating system. Informs
 * the other clients in the same game that a person has left.
 * 
 * @author alancoon
 *
 */


//pop-up for when user clicks the red X on a frame
public class ExitWindowListener extends WindowAdapter{

	private GameFrame frame;
	private Client client;
	private final boolean isMultiplayer;
	
	/**
	 * We have overloaded constructors so if you pass in a {@code Client},
	 * the logic will know that we are currently in a multiplayer game.
	 * Otherwise we will assume that it is single player guest mode.
	 * @param frame The {@code JFrame} that you want to add the 
	 * {@code ExitWindowListener} to.
	 * @param client The {@code Client} that the {@code JFrame} belongs to.
	 */
	public ExitWindowListener(GameFrame frame, Client client) {
		this.frame = frame;
		this.client = client;
		this.isMultiplayer = true;
	}
	
	/**
	 * Single player guest mode.
	 * @param frame The frame that you want to add the {@code ExitWindowListener} to.
	 */
	public ExitWindowListener(GameFrame frame) {
		this.frame = frame;
		this.isMultiplayer = false;
	}
	
	 public void windowClosing(WindowEvent e) {
		 int answer = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
		 if (answer == JOptionPane.YES_OPTION) {
			 try {
				 if (isMultiplayer) {
					 if(frame.getCurrentPanel() instanceof FinalGUI) {
						 client.sendMessage(new LeaveFinal());
					 }
					 else{
						 String name = client.getUser().getUsername();
						 ClientExitMessage message = new ClientExitMessage(name);
						 System.out.println("sent");
						 client.sendMessage(message);
					 }
				 }
			 } catch(Exception exp) {
				 exp.printStackTrace();
			 } finally {
				 System.exit(0);				 
			 }
		 }
	 }
}
