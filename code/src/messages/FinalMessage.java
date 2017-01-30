package messages;

import java.io.Serializable;

import gameplay.Game;

public class FinalMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Game game;
	
	public FinalMessage(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}

}
