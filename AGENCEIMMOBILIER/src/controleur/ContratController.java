package controleur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratController {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/agenceimmobiliere";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

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

	public boolean ajouterContrat(String dateC, String typeContrat, float prixC, int idClient, int idBien) {
		String query = "INSERT INTO contrat (dateC, typeContrat, prixC, idClient, idBien) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(query)) {

			System.out.println("ID Client : " + idClient);
			System.out.println("ID Bien : " + idBien);
			System.out.println("Date : " + dateC);
			System.out.println("Type Contrat : " + typeContrat);
			System.out.println("Prix : " + prixC);

			stmt.setDate(1, Date.valueOf(dateC));
			stmt.setString(2, typeContrat);
			stmt.setFloat(3, prixC);
			stmt.setInt(4, idClient);
			stmt.setInt(5, idBien);

			int rowsAffected = stmt.executeUpdate();
			System.out.println("Lignes affectées : " + rowsAffected);
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String[]> getAllContrats() {
	    List<String[]> contrats = new ArrayList<>();

	    String query = "SELECT t.idContrat, " +
	                   "       t.idBien, " +
	                   "       cProprietaire.nomC AS proprietaire, " +
	                   "       cPreneur.nomC AS preneur, " +
	                   "       t.typeContrat, t.prixC, t.dateC " +
	                   "FROM contrat t " +
	                   "JOIN client cPreneur ON t.idClient = cPreneur.idClient " +
	                   "JOIN bienimmobilier b ON t.idBien = b.idBien " +
	                   "JOIN client cProprietaire ON b.idClient = cProprietaire.idClient";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         Statement stmt = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        while (rs.next()) {
	            contrats.add(new String[] {
	                String.valueOf(rs.getInt("idContrat")),
	                String.valueOf(rs.getInt("idBien")),
	                rs.getString("proprietaire"),
	                rs.getString("preneur"), 
	                rs.getString("typeContrat"),
	                String.valueOf(rs.getFloat("prixC")),
	                rs.getString("dateC") 
	            });
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return contrats;
	}

	
	public boolean modifierContrat(int idContrat, String type, float prix, String date) {
	    String query = "UPDATE contrat SET typeContrat = ?, prixC = ?, dateC = ? WHERE idContrat = ?";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setString(1, type);
	        preparedStatement.setFloat(2, prix);
	        preparedStatement.setDate(3, Date.valueOf(date)); // Assurez-vous que la date est au format "yyyy-MM-dd"
	        preparedStatement.setInt(4, idContrat);

	        int rowsUpdated = preparedStatement.executeUpdate();
	        return rowsUpdated > 0; // Retourne true si la mise à jour a réussi
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Retourne false en cas d'échec
	    }
	}


}
