package messages;

import java.io.Serializable;

public class ReturnToIntro implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean showPane;
	
	public ReturnToIntro(boolean showPane) {
		this.showPane = showPane;
	}

	public boolean getShowPane() {
		return showPane;
	}
}
