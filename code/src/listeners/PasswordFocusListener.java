package listeners;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class PasswordFocusListener implements FocusListener {

	private String defaultText;
	private JPasswordField thisTextField;
	
	/**
	 * 
	 * @author alancoon
	 * @param defaultText
	 * @param thisTextField
	 */
	public PasswordFocusListener(String defaultText, JPasswordField thisTextField) {
		this.defaultText = defaultText;
		this.thisTextField = thisTextField;
		// Set the text to default initially
		thisTextField.setText(defaultText);
		thisTextField.setEchoChar((char) 0);
		thisTextField.setForeground(Color.gray);
	}

	@Override
    public void focusGained(FocusEvent fe)
    {
		if (String.valueOf(thisTextField.getPassword()).equals(defaultText)) {
			thisTextField.setForeground(Color.black);
			thisTextField.setText("");
			thisTextField.setEchoChar('\u25CF');
		}
    }

    @Override
    public void focusLost(FocusEvent fe)
    {
    	if (String.valueOf(thisTextField.getPassword()).equals("")) {
    		thisTextField.setEchoChar((char) 0);

    		thisTextField.setForeground(Color.gray);
	    	thisTextField.setText(defaultText);
    	}
    }
}
