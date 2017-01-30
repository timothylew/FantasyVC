package messages;

import java.io.Serializable;

public class CompanyUpdateMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public CompanyUpdateMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
