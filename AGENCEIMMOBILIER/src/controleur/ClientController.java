package controleur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClientController {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/agenceimmobiliere";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	public boolean ajouterClient(String nom, String prenom, String email, String telephone, String adresse,
			String statut) {
		String query = "INSERT INTO client (nomC, prenomC,AdresseC, telephoneC, emailC, statutC) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, prenom);
			preparedStatement.setString(5, adresse);
			preparedStatement.setString(4, telephone);
			preparedStatement.setString(3, email);
			preparedStatement.setString(6, statut);

			int rowsInserted = preparedStatement.executeUpdate();
			return rowsInserted > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String[]> getAllClients() {
	    List<String[]> clients = new ArrayList<>();
	    String query = "SELECT idClient, nomC, prenomC, emailC, telephoneC, AdresseC, statutC FROM client";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement preparedStatement = connection.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            String[] client = new String[7];
	            client[0] = String.valueOf(resultSet.getInt("idClient"));
	            client[1] = resultSet.getString("nomC");
	            client[2] = resultSet.getString("prenomC");
	            client[3] = resultSet.getString("emailC");
	            client[4] = resultSet.getString("telephoneC");
	            client[5] = resultSet.getString("AdresseC");
	            client[6] = resultSet.getString("statutC");
	            clients.add(client);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return clients;
	}

	
	public boolean modifierClient(int idClient, String nom, String prenom, String email, String telephone, String adresse, String statut) {
	    String query = "UPDATE client SET nomC = ?, prenomC = ?, emailC = ?, telephoneC = ?, AdresseC = ?, statutC = ? WHERE idClient = ?";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setString(1, nom);
	        preparedStatement.setString(2, prenom);
	        preparedStatement.setString(3, email);
	        preparedStatement.setString(4, telephone);
	        preparedStatement.setString(5, adresse);
	        preparedStatement.setString(6, statut);
	        preparedStatement.setInt(7, idClient);

	        int rowsUpdated = preparedStatement.executeUpdate();
	        return rowsUpdated > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}



}
