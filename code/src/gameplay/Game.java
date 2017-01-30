
package gameplay;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import messages.CompanyUpdateMessage;
import server.SQLDriver;
import server.ServerLobby;

/**
 * Maintains all of the current players inside of the instance of the game
 * Has a list of all the companies that are still available.
 * Also keeps track of the game state.
 * 
 * We decided to use the {@code Serializable} interface so we can 
 * transmit the {@code Game} over object streams from the server to 
 * the player clients.  This way we can do computations that affect 
 * the {@code Game} state and then package and ship it to every 
 * player so a uniform state is available for all of the players.
 *
 * @author arschroc
 * @author alancoon
 */
public class Game extends Thread implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Vector<User> users;
	private Vector<Company> companies;
	private transient ServerLobby sl;
	public int currentQuarter;
	
	//Constructor for not networked game
	public Game() {
		currentQuarter = -1;
		users = new Vector<User>();
		companies = new Vector<Company>();
		initializeCompanies();
	}
	
	//constructor for networked game
	public Game(Vector<User> users, ServerLobby sl) {
		currentQuarter = -1;
		this.users = users;
		this.sl = sl;
		companies = new Vector<Company>();
		initializeCompanies();
	}
	
	/**
	 * To be done only on the SERVER side at the beginning
	 * of the game, initializes all of the companies from the server
	 */
	public void initializeCompanies() {
		//CompanyPopulator compPop = new CompanyPopulator();
		//companies = compPop.populate();
		

		SQLDriver driver = new SQLDriver();
		driver.connect();
		companies = driver.getCompanies();
		driver.stop();
	}
	
	public void seed(Vector<User> users, ServerLobby sl) {
		this.users = users;
		this.sl = sl;
	}
	
	//formally updateNonNetworkedCompanies
	public LinkedList<String> updateCompanies() {
		LinkedList<String> output = new LinkedList<String>();
		for(Company company : companies) {
			//update every company
			String updateText = company.updateCurrentWorth();
			
			
			//update a user's version of the company (if owned)
			for(User user : users) {
				for(Company userCompany : user.getCompanies()) {
					if(userCompany.getName().equals(company.getName())) {
						userCompany = company;
					}
				}
			}
			
			if(!updateText.equals("")) {
				output.add(company.getName() + updateText);
			}
		}
		
		return output;
	}
	
	
	/**
	 * To be done ONLY IN SERVER
	 * updates all of the companies and sends a message to 
	 * the clients with the new companies
	 */
	
	public void updateCompanies(int overload) {
		Random rand = new Random();
		for(Company company : companies) {
			//update every company
			String updateText = company.updateCurrentWorth();
			
			
			//update a user's version of the company (if owned)
			for(User user : users) {
				for(Company userCompany : user.getCompanies()) {
					if(userCompany.getName().equals(company.getName())) {
						System.out.println("updating" + company.getName() + " to " + company.getCurrentWorth());
						userCompany.setCurrentWorth(company.getCurrentWorth());
					}
				}
			}
			
			if(!updateText.isEmpty()) {
				//TODO create and send a message to all clients 
				//telling them to display the updateText on the TimeLapseGUI
				sl.sendToAll(new CompanyUpdateMessage(company.getName() + updateText));				
				try {
					Thread.sleep(rand.nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

//		System.out.println(this.getUsers().get(0).getCompanies().get(0).getName() + this.getUsers().get(0).getCompanies().get(0).getCurrentWorth());
		this.incrementQuarter();
		//TODO send message to all clients containing the new game and User updates
	}
	
	
	public Vector<Company> getCompanies() {
		return companies;
	}
	
	/**
	 * 
	 * To be done FOR ALL CLIENTS
	 * updates all of the companies by making them
	 * the same as the server's already updated companies
	 */
	public void updateCompanies(Vector<Company> companies, Vector<User> users) {
		this.companies = companies;
		this.users = users;
	}
	
	public void addUser(User user) {
		users.add(user);
	}
	
	public void updateUser(User user) {
		for(User u : users) {
			if(u.getUsername().equals(user.getUsername())) {
				u.setCompanies(user.getCompanies());
				u.setCurrentCapital(user.getCurrentCapital());
			}
		}
	}
	
	//returns a list of winning teams
	public Vector<User> getWinners() {
		Vector<User> finalists = users;
		for(User user : finalists) {
			double value = user.getCurrentCapital();
			for(Company company : user.getCompanies()) {
				value += company.getCurrentWorth();
			}
			user.setCurrentCapital(value);
		}
		
		Vector<User> winners = new Vector<User>();
		
		//sorts the finalists in order of their total profit
		Collections.sort(finalists, User.getComparator());
		User definiteWinner = finalists.get(finalists.size() - 1);
		double max = definiteWinner.getTotalProfit();
		
		winners.add(definiteWinner);
		
		//check to see if there are other winners
		if(finalists.size() > 1) {
			
			for(int i = finalists.size() - 2; i > -1; i--) {
				//if this team has the same score as the definite winner then their is a tie
				if(finalists.get(i).getCurrentCapital() == max) {
					winners.add(finalists.get(i));
				}
			}
		}
		
		return winners;
	}
	public Vector<User> getUsers(){
		return users;
	}
	
	public User returnUser(String userCompanyName){
		for(User user: users){
			if (user.getCompanyName().equals(userCompanyName))
				return user;
		}
		return null;
	}
	
	public Company returnCompany(String companyName){
		for(Company company: companies){
			if (company.getName().equals(companyName))
				return company;
		}
		return null;
	}
	
	public void incrementQuarter() {
		currentQuarter++;
	}
	
	public void decrementQuarter() {
		currentQuarter--;
	}
	
	public int getCurrentQuarter() { 
		return currentQuarter;
	}
	
	public int getQuarter() {
		return (currentQuarter % 4) + 1;
	}

	public int getYear() { 
		return (currentQuarter / 4) + 2016;
	}
	
	public Vector<Company> getFreeAgents() {
		Vector<Company> freeAgents = new Vector<Company>();
		
		for(Company company : companies) {
			boolean isntOwned = true;
			
			userloop:
			for(User user : users) {
				for(Company ownedCompany: user.getCompanies()) {
					if(company.getName().equals(ownedCompany.getName())) {
						isntOwned = false;
						break userloop;
					}
				}
			}
			
			if(isntOwned) {
				freeAgents.add(company);
			}
		}
		
		return freeAgents;
	}
	
	public void initializeIcons() {
		for(Company c : companies) {
			c.createIcon();
		}
		for(User u : users) {
			u.createIcon();
		}
	}
}