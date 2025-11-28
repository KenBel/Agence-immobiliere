package controleur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;


public class AccueilController {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/agenceimmobiliere";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";
	
	public Map<String, Integer> getMonthlyStats() {
	    Map<String, Integer> stats = new HashMap<>();

	    String queryBiens = "SELECT COUNT(*) AS total FROM bienimmobilier";
	    String queryVentes = "SELECT COUNT(*) AS total FROM transaction WHERE MONTH(datT) = MONTH(CURRENT_DATE())";
	    String queryClients = "SELECT COUNT(*) AS total FROM client";
	    String queryContrats = "SELECT COUNT(*) AS total FROM contrat WHERE MONTH(dateC) = MONTH(CURRENT_DATE())";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	        try (PreparedStatement stmt = connection.prepareStatement(queryBiens);
	             ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                stats.put("Biens", rs.getInt("total"));
	            }
	        }
	        try (PreparedStatement stmt = connection.prepareStatement(queryVentes);
	             ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                stats.put("Ventes", rs.getInt("total"));
	            }
	        }
	        try (PreparedStatement stmt = connection.prepareStatement(queryClients);
	             ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                stats.put("Clients", rs.getInt("total"));
	            }
	        }
	        try (PreparedStatement stmt = connection.prepareStatement(queryContrats);
	             ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                stats.put("Contrats", rs.getInt("total"));
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return stats;
	}

	public List<String[]> getUpcomingAppointments() {
	    List<String[]> appointments = new ArrayList<>();
	    String query = "SELECT v.dateV, v.heureV, c.nomC AS client, b.localisationB AS bien, a.nomE AS agent " +
	                   "FROM visite v " +
	                   "JOIN client c ON v.idClient = c.idClient " +
	                   "JOIN bienimmobilier b ON v.idBien = b.idBien " +
	                   "JOIN agent a ON v.idAgent = a.idAgent " +
	                   "WHERE v.dateV >= CURDATE() " +
	                   "ORDER BY v.dateV ASC, v.heureV ASC";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement stmt = connection.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            String[] appointment = new String[] {
	                rs.getString("dateV") + " " + rs.getString("heureV"),
	                rs.getString("client"),
	                rs.getString("bien"),
	                rs.getString("agent")
	            };
	            System.out.println("Appointment: " + Arrays.toString(appointment));
	            appointments.add(appointment);
	        }

	        System.out.println("Total appointments: " + appointments.size());

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return appointments;
	}
}
