package messages;

import java.io.Serializable;

/**
 * For tracking the current minute and second a {@code Timer} is on.  
 * The {@code ServerLobby} will send this {@code Message} every second
 * from the {@code Timer}'s {@code run} method.
 * @author alancoon
 *
 */
public class TimerTickMessage implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String display;
	
	public TimerTickMessage(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}
}
