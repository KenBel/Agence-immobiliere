package controleur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BienController {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/agenceimmobiliere";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	public int getIdBienByAdresse(String adresse) {
		int idBien = -1;
		String query = "SELECT idBien FROM bienimmobilier WHERE localisationB = ?";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement stmt = connection.prepareStatement(query)) {

			stmt.setString(1, adresse);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				idBien = rs.getInt("idBien");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idBien;
	}

	private Integer getProprietaireId(String nomProprietaire) {
		String query = "SELECT idClient FROM client WHERE nomC = ?";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, nomProprietaire);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt("idClient");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean ajouterBien(String type, String etat, String adresse, String surface, String pieces, String prix,
			String statut, String nomProprietaire) {
		Integer proprietaireId = getProprietaireId(nomProprietaire);

		if (proprietaireId == null) {
			System.err.println("Propriétaire introuvable : " + nomProprietaire);
			return false;
		}

		String query = "INSERT INTO bienImmobilier (operation, typeB, localisationB, superficieB, nbrPieces, prixE, etatE, idClient) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, statut);
			preparedStatement.setString(2, type);
			preparedStatement.setString(3, adresse);
			preparedStatement.setDouble(4, Double.parseDouble(surface));
			preparedStatement.setInt(5, Integer.parseInt(pieces));
			preparedStatement.setDouble(6, Double.parseDouble(prix));
			preparedStatement.setString(7, etat);
			preparedStatement.setInt(8, proprietaireId);

			int rowsInserted = preparedStatement.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String[]> getAllBiens() {
		List<String[]> biens = new ArrayList<>();
		String query = "SELECT b.idBien, b.typeB, b.localisationB, b.superficieB, b.prixE, b.etatE, b.operation, c.nomC "
				+ "FROM bienImmobilier b " + "JOIN client c ON b.idClient = c.idClient";

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				String[] bien = new String[8];
				bien[0] = String.valueOf(resultSet.getInt("idBien"));
				bien[1] = resultSet.getString("typeB");
				bien[2] = resultSet.getString("localisationB");
				bien[3] = String.valueOf(resultSet.getDouble("superficieB"));
				bien[4] = String.valueOf(resultSet.getDouble("prixE"));
				bien[5] = resultSet.getString("etatE");
				bien[6] = resultSet.getString("operation");
				bien[7] = resultSet.getString("nomC");
				biens.add(bien);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return biens;
	}

	public boolean supprimerBien(String type, String adresse) {
		String query = "DELETE FROM bienImmobilier WHERE typeB = ? AND localisationB = ?";

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, type);
			preparedStatement.setString(2, adresse);

			int rowsDeleted = preparedStatement.executeUpdate();
			return rowsDeleted > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean modifierBien(
		    String ancienType, 
		    String ancienneAdresse, 
		    String nouveauType, 
		    String nouvelleAdresse,
		    double nouvelleSurface, 
		    double nouveauPrix, 
		    String nouveauStatut, 
		    String nouvelleDisponibilite) {

		    // Validation des entrées
		    if (nouveauType == null || nouvelleAdresse == null || nouveauStatut == null || nouvelleDisponibilite == null) {
		        System.err.println("Les champs obligatoires sont manquants.");
		        return false;
		    }

		    String query = "UPDATE bienImmobilier " +
		                   "SET typeB = ?, localisationB = ?, superficieB = ?, prixE = ?, etatE = ?, operation = ? " +
		                   "WHERE typeB = ? AND localisationB = ?";

		    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
		         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

		        preparedStatement.setString(1, nouveauType);
		        preparedStatement.setString(2, nouvelleAdresse);
		        preparedStatement.setDouble(3, nouvelleSurface);
		        preparedStatement.setDouble(4, nouveauPrix);
		        preparedStatement.setString(5, nouveauStatut);
		        preparedStatement.setString(6, nouvelleDisponibilite);

		        preparedStatement.setString(7, ancienType);
		        preparedStatement.setString(8, ancienneAdresse);

		        int rowsUpdated = preparedStatement.executeUpdate();

		        if (rowsUpdated > 0) {
		            System.out.println("Le bien a été modifié avec succès.");
		            return true;
		        } else {
		            System.err.println("Aucune ligne mise à jour. Vérifiez les critères de recherche.");
		            return false;
		        }

		    } catch (SQLException e) {
		        System.err.println("Erreur lors de la modification du bien : " + e.getMessage());
		        return false;
		    }
		}

}
