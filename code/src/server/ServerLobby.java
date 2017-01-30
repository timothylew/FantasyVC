package server;

import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import gameplay.Game;
import gameplay.User;
import messages.FinalMessage;
import messages.LoadingGame;
import messages.ReadyGameMessage;
import messages.SwitchPanelMessage;
import messages.UserListMessage;
import threads.Timer;

public class ServerLobby extends Thread{
	protected Vector<ServerClientCommunicator> sccVector;
	private Vector<User> users;
	private Server server;
	private String lobbyName, hostName;
	public Lock lock;
	public Condition condition;
	public Semaphore semaphore;
	private int numPlayers;
	private Timer timer;
	private Game seedGame;
	private int quarters = 8;
	private double currentMax;
	
	public ServerLobby(Vector<ServerClientCommunicator> sccVector, Server server, String lobbyName, User host, int numPlayers) {
		this.sccVector = sccVector;
		this.server = server;
		this.lobbyName = lobbyName;
		this.hostName = host.getUsername();
		this.numPlayers = numPlayers;
		this.lock = new ReentrantLock();
		this.condition = lock.newCondition();
		this.semaphore = new Semaphore(this.numPlayers);
		timer = null;
		users = new Vector<User>();
		users.add(host);
		this.start();
		sendToAll(new UserListMessage(users, numPlayers - users.size()));
	}
	
	@SuppressWarnings("deprecation")
	public void removeServerClientCommunicator(ServerClientCommunicator scc) {
		sccVector.remove(scc);
		if (sccVector.isEmpty()) {
			server.removeServerLobby(this);
			this.stop();
			if(timer != null) {
				timer.kill();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeAll() {
		if(!sccVector.isEmpty()){
			sccVector.clear();
			server.removeServerLobby(this);
			this.stop();
			if(timer != null) {
				timer.kill();
			}
		}
	}
	
	public String getLobbyName() {
		return lobbyName;
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public int getGameSize() {
		return numPlayers;
	}
	
	public Vector<User> getUsers() {
		return users;
	}
	
	public void setUserCompanies(User user) {
		seedGame.updateUser(user);
		for(User u : users) {
			if(u.getUsername().equals(user.getUsername())) {
				u.setReady();
				System.out.println("Unready " + u.getUsername());
			}
		}
		System.out.println("set user lock");
		semaphore.release();
	}
	
	public synchronized void sendToAll(Object msg) {
		for (ServerClientCommunicator scc : sccVector) {
			System.out.println(msg.getClass() + " " + System.currentTimeMillis());
			scc.sendMessage(msg);
		}
	}
	
	public void addToLobby(ServerClientCommunicator scc, User user) {
		System.out.println("add");
		sccVector.add(scc);
		users.add(user);
		System.out.println(users.get(0).getReady());
		// TODO: send to people in the lobby how many to wait on		
		sendToAll(new UserListMessage(users, numPlayers - users.size()));
	}
	
	public boolean checkReady(){
		for (User user : users) {
			if (!user.getReady()) {
				return false;
			}
		}
		return true;
	}
	
	public void gameReceived() {
		semaphore.release();
	}
	
	public void resetReady() {
		for(User user : users) {
			user.unReady();
		}
	}
	
	public void setReady(String username, String teamname) {
		for (User user : users) {
			if(user.getUsername().equals(username)) {
				user.setReady();
				user.setCompanyName(teamname);
			}
		}
		System.out.println("set ready lock");
		semaphore.release();
	}
	
	private synchronized void initializeGame() { 
		seedGame = new Game(users, this);
	}
	
	public void beginAuction(double newMax) {
		currentMax = newMax;
	}
	
	public synchronized boolean checkBid(double bid) {
		if (bid > currentMax) {
			currentMax = bid;
			return true;
		}
		else return false;
	}
	
	public void run() {
		try{
			while (sccVector.size() < numPlayers);
			server.removeServerLobby(this);
			System.out.println("full");
			
			semaphore.acquire(this.numPlayers);
			while(!checkReady());
			
			System.out.println("semaphore passed");
			this.sendToAll(new LoadingGame());
			initializeGame();		
			this.sendToAll(seedGame);
			semaphore.acquire(this.numPlayers);
			this.sendToAll(new ReadyGameMessage());
			
			for (int i = 0; i < quarters; i++) {
				resetReady();
				semaphore.acquire(this.numPlayers);
				while(!checkReady());
				if (timer != null) timer.kill();
				System.out.println("Send timelapse");
				
				sendToAll(new SwitchPanelMessage());
					
				seedGame.updateCompanies(1);
				sendToAll(seedGame);
				semaphore.acquire(this.numPlayers);
				sendToAll(new SwitchPanelMessage());
				startTimer(60);
			}
			System.out.println("done");
			
			resetReady();
			semaphore.acquire(this.numPlayers);
			System.out.println("requesting final stuff");
			while(!checkReady());			
			if(timer != null) timer.kill();
			sendToAll(new SwitchPanelMessage());
			
			seedGame.updateCompanies(1);
			sendToAll(seedGame);
			
			sendToAll(new FinalMessage(seedGame));
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
	
	public void startTimer(int time) {
		if(timer != null) {
			timer.kill();
		}
		timer = new Timer(this, time);
	}
	
	public void increaseTime(int seconds) { 
		timer.increaseTime(seconds);
	}
	
	public void nullifyTimer() {
		timer = null;
	}
}
