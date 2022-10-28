package practice.data.filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Ariline {
	static PreparedStatement ps = null;

	public static void main(String[] args) {

		ResourceBundle rb = ResourceBundle.getBundle("practice.data.filter.AirlineRecord");
		try {
			Class.forName(rb.getString("driver"));
			Connection con = DriverManager.getConnection(rb.getString("url"), rb.getString("uid"), rb.getString("pwd"));
			BufferedReader br = new BufferedReader(new FileReader(rb.getString("path") + "airline.txt"));

			String s = br.readLine();
			while (s != null) {
				if (s.equalsIgnoreCase("@data")) {
					s = br.readLine();
					break;
				}
				s = br.readLine();
			}
			// Airline data
			while (s != null) {
				StringTokenizer st = new StringTokenizer(s, ",");
				ps = con.prepareStatement("insert into airline values(?,?)");
				ps.setString(1, st.nextToken());
				ps.setString(2, st.nextToken());
				ps.executeUpdate();
				s=br.readLine();
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loaded Successfully");
	}
}
