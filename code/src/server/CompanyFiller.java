package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import gameplay.Company;

public class CompanyFiller {
	
	public static void main(String [] args) {
		SQLDriver driver = new SQLDriver();
		driver.connect();
		try {
			FileReader file = new FileReader("resources/PopulateCompanies");
			Scanner scanner = new Scanner(file);
			
			scanner.nextLine(); // Clear the description line
			
			while(scanner.hasNextLine()) {
				StringTokenizer st = new StringTokenizer(scanner.nextLine(), "||");
				String companyName = st.nextToken();
				String imagePath = st.nextToken();
				try {
					InputStream is = new URL(imagePath).openStream();
					@SuppressWarnings("unused")
					ImageInputStream iis = ImageIO.createImageInputStream(is);
				} catch (Exception e) {
					System.out.println(companyName);
				}
				String description = st.nextToken();
				String startingPrice = st.nextToken();
				String tier = st.nextToken();
				driver.insertCompany(imagePath, companyName, description, Double.parseDouble(startingPrice), Integer.parseInt(tier));
			}
			
			file.close();
			scanner.close();
			
			driver.getCompanies();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//driver.stop();
		}
		
		Vector<Company> companies = driver.getCompanies();
		for(Company c : companies) {
			System.out.println(c.getName());
		}
		
		driver.insertUser("Timmy Lewd", "passcode", "intern");
		System.out.println(driver.userExists("Timmy Lewd"));
		System.out.println(driver.checkPassword("Timmy Lewd", "passcode"));
		
		
//		User user = driver.getUser("Timmy Lewd");
		
//		
//		user = driver.getUser("New Name");
//		System.out.println(user.getUserBio());
		
//		System.out.println(driver.getUserBio("asdf"));
		
		driver.stop();
	}
}


