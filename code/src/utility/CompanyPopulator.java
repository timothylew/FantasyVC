package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.PreparedStatement;

import gameplay.Company;

/**
 * Interfaces with the database to get information about our companies and populate
 * the {@code Game} class with that data.
 * @author alancoon
 *
 */
public class CompanyPopulator {
	
	private Connection conn = null;
	private ResultSet rs = null;
	private static final String queryStatement = "SELECT * FROM Venture.Companies WHERE companyID = ?";

	public CompanyPopulator() {
		initializeConnection();
	}
	
	private void initializeConnection() { 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Venture?user=root&password=Fuck you MySQL.&useSSL=false");
		} catch (SQLException sqle) { 
			System.out.println("SQLException in CompanyPopulator: " + sqle.getLocalizedMessage());
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) { 
			System.out.println("ClassNotFoundException in CompanyPopulator: " + cnfe.getLocalizedMessage());
			cnfe.printStackTrace();
		}
	}
	
	public Vector<Company> populate() { 
		Vector<Company> vec = new Vector<Company>();
		for (int i = 0; i < Constants.COMPANIES_IN_DATABASE; i++) {
			try {
				PreparedStatement ps = conn.prepareStatement(queryStatement);
				ps.setInt(1, i);
				rs = ps.executeQuery();
				if (rs.next()) {
					
					String imagePath = rs.getString(2);
					String companyName = rs.getString(3);
					String description = rs.getString(4);
					long startingPrice = rs.getLong(5);
					int tierLevel = rs.getInt(6);
					
					Company company = new Company(imagePath, companyName, description, startingPrice, tierLevel);
					vec.add(company);
				}
			} catch (SQLException sqle) {
				System.out.println("SQLException in CompanyPopulator::populate(): " + sqle.getLocalizedMessage());
				sqle.printStackTrace();
			}
			
		}
		
		// Alert will tell us if the number of companies processed equals the number of companies that are
		// supposedly in the database.
		String alert = (vec.size() == Constants.COMPANIES_IN_DATABASE) ? "CompanyPopulator seems to have worked." : "There seems to be an error with CompanyPopulator.";
		System.out.println(alert);
		return vec;
	}
}
