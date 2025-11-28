package controleur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransacController {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/agenceimmobiliere";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

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

	public boolean ajouterTransaction(String dateT, String typeT, float montantT, int idClient, int idAgent,
			int idBien) {
		String query = "INSERT INTO transaction (datT, typeT, montantT, idClient, idAgent, idBien) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(query)) {

			stmt.setDate(1, Date.valueOf(dateT));
			stmt.setString(2, typeT);
			stmt.setFloat(3, montantT);
			stmt.setInt(4, idClient);
			stmt.setInt(5, idAgent);
			stmt.setInt(6, idBien);

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String[]> getAllTransactions() {
	    List<String[]> transactions = new ArrayList<>();

	    String query = "SELECT t.idTransac, " +
	                   "       t.idBien, " + 
	                   "       cProprietaire.nomC AS proprietaire, " +
	                   "       cPreneur.nomC AS preneur, " +
	                   "       t.typeT, t.montantT, t.datT, " +
	                   "       a.nomE AS agent " +
	                   "FROM transaction t " +
	                   "JOIN client cPreneur ON t.idClient = cPreneur.idClient " +
	                   "JOIN bienimmobilier b ON t.idBien = b.idBien " +
	                   "JOIN client cProprietaire ON b.idClient = cProprietaire.idClient " +
	                   "JOIN agent a ON t.idAgent = a.idAgent";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            transactions.add(new String[] {
	                String.valueOf(rs.getInt("idTransac")),
	                String.valueOf(rs.getInt("idBien")),
	                rs.getString("proprietaire"),
	                rs.getString("preneur"),
	                rs.getString("typeT"),
	                String.valueOf(rs.getFloat("montantT")),
	                rs.getString("datT"),
	                rs.getString("agent")
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return transactions;
	}


	public boolean modifierTransac(int idTransac, String type, float montant, String date) {
	    String query = "UPDATE transaction SET typeT = ?, montantT = ?, datT = ? WHERE idTransac = ?";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setString(1, type);
	        preparedStatement.setFloat(2, montant);
	        preparedStatement.setDate(3, Date.valueOf(date)); // Assurez-vous que la date est au format "yyyy-MM-dd"
	        preparedStatement.setInt(4, idTransac);

	        int rowsUpdated = preparedStatement.executeUpdate();
	        return rowsUpdated > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


}
