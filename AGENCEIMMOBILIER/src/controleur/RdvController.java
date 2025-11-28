package controleur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class RdvController {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/agenceimmobiliere";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";
	
	public int getIdBienByAdresse(String adresse) {
	    int idBien = -1;
	    String query = "SELECT idBien FROM bienimmobilier WHERE localisationB = ?";

	    System.out.println("Recherche de l'idBien pour l'adresse : " + adresse);

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement stmt = connection.prepareStatement(query)) {

	        stmt.setString(1, adresse.trim());
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            idBien = rs.getInt("idBien");
	            System.out.println("idBien trouvé : " + idBien);
	        } else {
	            System.out.println("Aucun idBien trouvé pour l'adresse : " + adresse);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return idBien;
	}



	public int getIdAgentByNom(String nom) {
		int idAgent = -1;
		String query = "SELECT idAgent FROM agent WHERE nomE = ?";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(query)) {

			stmt.setString(1, nom);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				idAgent = rs.getInt("idAgent");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idAgent;
	}

	public int getIdClientByNom(String nom) {
		int idClient = -1;
		String query = "SELECT idClient FROM client WHERE nomC = ?";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(query)) {

			stmt.setString(1, nom);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				idClient = rs.getInt("idClient");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idClient;
	}

	public boolean ajouterRdv(String dateV, String heureV, int idClient, int idAgent, int idBien) {
	    String query = "INSERT INTO visite (dateV, heureV, idClient, idAgent, idBien) VALUES (?, ?, ?, ?, ?)";
	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement stmt = connection.prepareStatement(query)) {

	        stmt.setDate(1, Date.valueOf(dateV));
	        if (!heureV.matches("\\d{2}:\\d{2}:\\d{2}")) {
	            heureV = heureV + ":00";
	        }
	        stmt.setTime(2, Time.valueOf(heureV));
	        stmt.setInt(3, idClient);
	        stmt.setInt(4, idAgent);
	        stmt.setInt(5, idBien);

	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}



	public List<String[]> getAllRdvs() {
	    List<String[]> rdvs = new ArrayList<>();
	    
	    String query = "SELECT v.idV, v.idBien, cProprietaire.nomC AS proprietaire, " +
	                   "cPreneur.nomC AS preneur, v.dateV, v.heureV, a.nomE AS agent " +
	                   "FROM visite v " +
	                   "JOIN client cPreneur ON v.idClient = cPreneur.idClient " +
	                   "JOIN bienimmobilier b ON v.idBien = b.idBien " +
	                   "JOIN client cProprietaire ON b.idClient = cProprietaire.idClient " +
	                   "JOIN agent a ON v.idAgent = a.idAgent";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            String[] row = new String[] {
	                String.valueOf(rs.getInt("idV")),
	                String.valueOf(rs.getInt("idBien")),
	                rs.getString("proprietaire"),
	                rs.getString("preneur"),
	                rs.getString("dateV"),
	                rs.getString("heureV"),
	                rs.getString("agent")
	            };

	            System.out.println("Row: " + Arrays.toString(row));
	            rdvs.add(row);
	        }

	        System.out.println("Total rows fetched: " + rdvs.size());

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rdvs;
	}





	public boolean modifierRdv(int idV, String dateV, String heureV, int idClient, int idAgent, int idBien) {
	    String query = "UPDATE visite SET dateV = ?, heureV = ?, idClient = ?, idAgent = ?, idBien = ? WHERE idV = ?";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement stmt = connection.prepareStatement(query)) {

	        stmt.setDate(1, Date.valueOf(dateV));
	        stmt.setTime(2, Time.valueOf(heureV + ":00"));
	        stmt.setInt(3, idClient);
	        stmt.setInt(4, idAgent);
	        stmt.setInt(5, idBien);
	        stmt.setInt(6, idV);

	        return stmt.executeUpdate() > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


}
