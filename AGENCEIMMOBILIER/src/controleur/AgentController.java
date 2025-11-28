package controleur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AgentController {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/agenceimmobiliere";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	public boolean ajouterAgent(String nom, String prenom, String email, String telephone, String adresse,
			String nbrAffaire, String salaire, String nomUtilisateur, String motDePasse) {

		String query = "INSERT INTO agent (nomE, prenomE, emailE, telephoneE, AdresseE, nbrAff, salaire, NomUtilisateur, mdp) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, nom);
			stmt.setString(2, prenom);
			stmt.setString(3, email);
			stmt.setString(4, telephone);
			stmt.setString(5, adresse);
			stmt.setInt(6, Integer.parseInt(nbrAffaire));
			stmt.setBigDecimal(7, new java.math.BigDecimal(salaire));
			stmt.setString(8, nomUtilisateur); 
			stmt.setString(9, motDePasse);


			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (NumberFormatException e) {
			System.err.println("Erreur : nbrAffaire doit être un nombre entier valide.");
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String[]> getAllAgents() {
		List<String[]> agents = new ArrayList<>();
		String query = "SELECT nomE, prenomE, emailE, telephoneE, AdresseE, nbrAff FROM agent";

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				String[] agent = new String[6];
				agent[0] = resultSet.getString("nomE"); 
				agent[1] = resultSet.getString("prenomE"); 
				agent[2] = resultSet.getString("emailE");
				agent[3] = resultSet.getString("telephoneE");
				agent[4] = resultSet.getString("AdresseE");
				agent[5] = resultSet.getString("nbrAff");
				agents.add(agent);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return agents;
	}
	
	public boolean modifierAgent(String nom, String prenom, String email, String telephone, String adresse, String nbrA) {
	    String query = "UPDATE agent SET nomE = ?, prenomE = ?, emailE = ?, telephoneE = ?, AdresseE = ?, nbrAff = ? WHERE emailE = ?";

	    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setString(1, nom);
	        preparedStatement.setString(2, prenom);
	        preparedStatement.setString(3, email);
	        preparedStatement.setString(4, telephone);
	        preparedStatement.setString(5, adresse);
	        preparedStatement.setString(6, nbrA);
	        preparedStatement.setString(7, email);

	        int rowsUpdated = preparedStatement.executeUpdate();
	        return rowsUpdated > 0; 
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	

}
