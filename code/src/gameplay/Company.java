
package gameplay;


import java.awt.Image;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Random;

import utility.Constants;
import utility.ImageLibrary;

/**
 * The {@code Company} class stores information about a company.
 * Each company is matched with one of the trends form the MySQL database
 * The Company will either belong to the Game or a specific user
 *
 * @author arschroc
 * 
 */

public class Company implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String image, name, description;
	private double startingPrice, askingPrice, currentWorth;
	private int delta, tierLevel;
	private transient Image companyLogo;
	private double imageAspectRatio;
	
	public Company(String imagePath, String companyName, String description, double startingPrice, int tierLevel) {
		this.image = imagePath;
		this.name = companyName;
		this.description = description;
		this.startingPrice = startingPrice;
		this.tierLevel = tierLevel;
		askingPrice = startingPrice; //asking price = startingPrice at beginning of auction
		currentWorth = startingPrice; //the current worth starts at startingPrice
		createIcon();
		
		switch (tierLevel) { 
			case 1:
				this.delta = 25;
				break;
			case 2:
				this.delta = 50;
				break;
			case 3:
				this.delta = 100;
				break;
		}
	}



	/* Getters. */
	public String getName() {
		return name;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getStartingPrice() {
		return startingPrice;
	}
	
	public double getAskingPrice() {
		return askingPrice;
	}
	
	public double getCurrentWorth() {
		return currentWorth;
	}
	
	public void setCurrentWorth(double worth) {
		currentWorth = worth;
	}
	
	public int getTierLevel() {
		return tierLevel;
	}
	
	public Image getCompanyLogo(){
		return companyLogo;
	}
	
	
	/**
	 * 
	 * @return A {@code String} of text that we can add to the notifications
	 * box on every {@code Client}'s {@code TimelapseGUI}.  A random
	 * outcome will determine if a special event occurs.  If there is 
	 * no special event, the returned text will be an empty {@code String}.
	 * Otherwise it will describe a good or bad event, also determined
	 * by random outcome.
	 */
	public synchronized String updateCurrentWorth() {
		Random rand = new Random();
		int intChange = Math.abs(rand.nextInt(delta));
		double change = intChange/100.0;
		boolean positive = false;
		int random = rand.nextInt(10) + 1; //creates random number between 1 and 10
		switch (tierLevel) { 
		case 1:
			if(random <= 5) positive = true;
			break;
		case 2:
			if(random <=75) positive = true;
			break;
		case 3:
			if(random <= 90) positive = true;
			break;
		}
		
		
		
		boolean specialEvent = rand.nextBoolean();
		String text = "";
		
		if(currentWorth!= 0) {
			// First we check if a special event has occurred. 
			if (specialEvent) { 
				// We will either boost or decrease this turn's delta by
				// a factor of between 3 and 6.
				int modifier = Math.abs(rand.nextInt(3)) + (4 - tierLevel);
				int index;
				if (positive) { 
					change *= modifier;  // Boost the change by the modifier.
					index = rand.nextInt(Constants.positiveEvents.length - 1); // Randomly pick an event text.
					text = Constants.positiveEvents[index];  // Grab the event text.
				} else {
					change *= -modifier;
					if (rand.nextBoolean()) change /= 2;
					index = rand.nextInt(Constants.negativeEvents.length - 1);
					text = Constants.negativeEvents[index];
				}
			} else {
				//if tier 3 roll for another special event
				if(tierLevel == 3 && positive == false) {
					specialEvent = rand.nextBoolean();
					if(specialEvent) {
						int modifier = Math.abs(rand.nextInt(3)) + (4 - tierLevel);
						int index;
						change *= -modifier;
						if (rand.nextBoolean()) change /= 2;
						index = rand.nextInt(Constants.negativeEvents.length - 1);
						text = Constants.negativeEvents[index];	
					}
				}
				
				if(!specialEvent) {
					// If no special event has occurred, we just modify the 
					// change based on whether positive came out true or not.
					change = positive ? change : -change;
				}

			}
			
			// At the end we just add the change to the currentWorth,
			// and we return the text.  
			currentWorth += change;
			//announce that the company became bankrupt
			if(currentWorth < 0) {
				if(text.equals("")) {
					text = " became bankrupt!";
				}
				else {
					text += " They are now bankrupt!";
				}
				currentWorth = 0;
			}
		}

		return text;
	}
	
	public void updateAskingPrice(int askingPrice) {
		System.out.println("updated");
		this.askingPrice = askingPrice;
	}
	
	
	/* Setters. */
	public void setName(String name) {
		this.name = name;
	}
	
	public void setImage(String image) {
		this.image = image;
		createIcon();
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setStartingPrice(int startingPrice) {
		this.startingPrice = startingPrice;
	}
	
	public void setTier(int tierLevel) {
		this.tierLevel = tierLevel;
	}
	
	public double getAspectRatio(){
		return imageAspectRatio;
	}
	
	public void createIcon(){
		companyLogo = ImageLibrary.getImage(image);
		imageAspectRatio = (double)companyLogo.getWidth(null) / companyLogo.getHeight(null);
	}
	
	/**
	 * Comparator class for sorting companies
	 * @author arschroc
	 *
	 */
	//comparator for sorting companies
	//it is passed into the sort method from the Java Collections class as a custom comparator
	//this will sort the users in order of their total profit
	private static class CompanyComparator implements Comparator<Company>{

		@Override
		public int compare(Company companyOne, Company companyTwo) {
			return Double.compare(companyOne.getCurrentWorth(), companyTwo.getCurrentWorth());
		}
		
	}
	
	public static CompanyComparator getComparator(){
		return new CompanyComparator();
	}
}
