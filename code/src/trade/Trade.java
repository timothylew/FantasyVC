package trade;

import java.io.Serializable;
import java.util.Vector;

public class Trade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Vector<Object> playerOneInventory, playerOneTrade, playerTwoInventory, playerTwoTrade;
	
	public Trade(Vector<Object> p1i, Vector<Object> p1t, Vector<Object> p2i, Vector<Object> p2t) {
		playerOneInventory = p1i;
		playerOneTrade = p1t;
		playerTwoInventory = p2i;
		playerTwoTrade = p2t;
	}

	public Vector<Object> getPlayerOneInventory() {
		return playerOneInventory;
	}

	public Vector<Object> getPlayerOneTrade() {
		return playerOneTrade;
	}

	public Vector<Object> getPlayerTwoInventory() {
		return playerTwoInventory;
	}

	public Vector<Object> getPlayerTwoTrade() {
		return playerTwoTrade;
	}
}
