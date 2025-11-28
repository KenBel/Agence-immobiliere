package modele;

import java.time.LocalDate;

public class ContratModel {

    private int idContrat;
    private int idBien;

    private String proprietaire;  // Nom du propriétaire
    private String preneur;       // Nom du client acheteur/locataire

    private String typeContrat;   // Vente ou Location
    private float prix;
    private LocalDate date;

    // ===============================
    // Constructeurs
    // ===============================
    public ContratModel() {}

    public ContratModel(int idContrat, int idBien, String proprietaire, String preneur,
                        String typeContrat, float prix, LocalDate date) {

        this.idContrat = idContrat;
        this.idBien = idBien;
        this.proprietaire = proprietaire;
        this.preneur = preneur;
        this.typeContrat = typeContrat;
        this.prix = prix;
        this.date = date;
    }

    // ===============================
    // Getters
    // ===============================
    public int getIdContrat() {
        return idContrat;
    }

    public int getIdBien() {
        return idBien;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public String getPreneur() {
        return preneur;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public float getPrix() {
        return prix;
    }

    public LocalDate getDate() {
        return date;
    }

    // ===============================
    // Setters
    // ===============================
    public void setIdContrat(int idContrat) {
        this.idContrat = idContrat;
    }

    public void setIdBien(int idBien) {
        this.idBien = idBien;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public void setPreneur(String preneur) {
        this.preneur = preneur;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // ===============================
    // toString (utile)
    // ===============================
    @Override
    public String toString() {
        return "Contrat #" + idContrat + " (" + typeContrat + ") - " + prix + "€";
    }
}
