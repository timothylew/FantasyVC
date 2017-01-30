package threads;

import messages.TimerTickMessage;
import server.ServerLobby;

/**
 * A simple timer from Jeopardy project.  You can enter an {@code int}
 * in the constructor and the {@code Timer} class will give you
 * a label that contains a digital clock view of a countdown from
 * the given {@code int}.
 * @author alancoon
 *
 */
public class Timer extends Thread {

	private int current;
	private final int length;
	private ServerLobby serverLobby;
	
	public Timer (ServerLobby serverLobby, int length) {
		this.serverLobby = serverLobby;
		this.length = length;
		this.current = 0;
		this.start();
	}

	
	@Override
	public void run() {
		while (current <= length) {
			try {
				int diff = length - current;
				Integer minutes = diff / 60;
				Integer seconds = diff % 60;
				String minDisplay = (minutes >= 10) ? minutes.toString() : "0" + minutes;
				String secDisplay = (seconds >= 10) ? seconds.toString() : "0" + seconds;
				String display = minDisplay + ":" + secDisplay;
				System.out.println("display");
				serverLobby.sendToAll(new TimerTickMessage(display));
				Thread.sleep(1000);
				current++;
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		serverLobby.nullifyTimer();
	}
	
	public void increaseTime(int seconds) { 
		current += seconds;
	}
	
	@SuppressWarnings("deprecation")
	public void kill() {
		this.stop();
	}
}
