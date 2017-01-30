package utility;

import javax.swing.JButton;

import gameplay.User;

public class FinalUserButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
	
	public FinalUserButton(User user){
		//Has to be replaced with firm name;
		super(user.getCompanyName());
		this.user = user;
		setOpaque(true);
		setBorderPainted(false);
		setBackground(AppearanceConstants.darkBlue);
		setForeground(AppearanceConstants.offWhite);
		setFont(AppearanceConstants.fontButtonBig);
	}
	
	public User getUser(){
		return user;
	}
}
