package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import gameplay.Lobby;
import gameplay.User;
import messages.LobbyListMessage;
import messages.Message;

public class Server extends Thread{
	private ServerSocket ss;
	private Vector<ServerClientCommunicator> sccVector;
	private Map<String, ServerLobby> lobbies;
	
	public Server() {
		try {
			ss = new ServerSocket(8008);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		sccVector = new Vector<ServerClientCommunicator>();
		lobbies = new TreeMap<String, ServerLobby>();
		this.start();
	}
	
	public void removeServerClientCommunicator(ServerClientCommunicator scc) {
		sccVector.remove(scc);
	}
	
	public void removeServerLobby(ServerLobby sl) {
		lobbies.remove(sl.getLobbyName());
		System.out.println("Lobby empty " + lobbies.isEmpty());
		// TODO: Send updated listing of available lobbies
		sendLobbies();
	}
	
	public void run() {
		try {
			while(true) {
				Socket s = ss.accept();
				System.out.println("accepted");
				ServerClientCommunicator scc = new ServerClientCommunicator(s, this);
				scc.start();
				sccVector.addElement(scc);
				sendLobbies();
			}
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (ss != null) {
				try {
					ss.close();							
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}
	
	public void sendToAll(Message msg) {
		for(ServerClientCommunicator scc : sccVector) {
			scc.sendMessage(msg);
		}
	}
	
	public synchronized void createLobby(ServerClientCommunicator scc, String lobbyName, User host, int numPlayers) {
		if (lobbies.containsKey(lobbyName)) {
			// TODO: Send message that lobby name is already taken
			return;
		}
		Vector<ServerClientCommunicator> sccVector2 = new Vector<ServerClientCommunicator>();
		sccVector2.add(scc);
		sccVector.remove(scc);
		
		ServerLobby sl = new ServerLobby(sccVector2, this, lobbyName, host, numPlayers);

		scc.setLobby(sl);
		
		lobbies.put(lobbyName, sl);
		sendLobbies();
		System.out.println("Lobby created");
		// TODO: Send updated listing of all the lobbies
	}
	
	public synchronized void addToLobby(ServerClientCommunicator scc, String lobbyName, User user) {
		if(!lobbies.containsKey(lobbyName)) {
			// TODO: Send message that lobby does not exist
			System.out.println("not found");
		}
		else {
			ServerLobby sl = lobbies.get(lobbyName);
			sl.addToLobby(scc, user);
			sccVector.remove(scc);
			
			
			scc.setLobby(sl);
			sendLobbies();
			System.out.println("Lobby joined");
		}
		
		// TODO: Send updated listing of all the lobbies (since they have more players available now)
	}
	
	public synchronized void sendLobbies() {
		Vector<Lobby> lobbies = new Vector<Lobby>();
		if (!this.lobbies.isEmpty()) {
			for(ServerLobby sl : this.lobbies.values()) {
				Lobby lobby = new Lobby(sl.getLobbyName(), sl.getHostName(), sl.getGameSize(), sl.getUsers());
				
				lobbies.add(lobby);
			}
		}
		LobbyListMessage llm = new LobbyListMessage(lobbies);
		sendToAll(llm);
	}
	
	public static void main(String [] args) {
		new Server();
	}
}