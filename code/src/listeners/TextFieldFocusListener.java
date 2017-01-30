package listeners;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class TextFieldFocusListener implements FocusListener {

	private String defaultText;
	private JTextField thisTextField;
	
	/**
	 * Adapted from Jeopardy solution code's TextFieldFocusListener.
	 * @param defaultText What the text field should show on default.
	 * @param thisTextField The text field this listener is assigned to.
	 */
	public TextFieldFocusListener(String defaultText, JTextField thisTextField) {
		this.defaultText = defaultText;
		this.thisTextField = thisTextField;
		// Set the text to default initially
		thisTextField.setText(defaultText);
		thisTextField.setForeground(Color.gray);
	}

	@Override
    public void focusGained(FocusEvent fe)
    {
		if (thisTextField.getText().equals(defaultText)) {
			thisTextField.setForeground(Color.black);
			thisTextField.setText("");
		}
    }

    @Override
    public void focusLost(FocusEvent fe)
    {
    	if (thisTextField.getText().equals("")) {
    		thisTextField.setForeground(Color.gray);
	    	thisTextField.setText(defaultText);
    	}
    }
}
