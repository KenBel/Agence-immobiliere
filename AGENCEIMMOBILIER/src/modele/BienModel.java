package modele;

public class BienModel {

    private int idBien;
    private String type;
    private String adresse;
    private double superficie;
    private double prix;
    private String statut;
    private String disponibilite;
    private int idClient; // Propriétaire

    // ============================
    // Constructeurs
    // ============================
    public BienModel() {}

    public BienModel(int idBien, String type, String adresse, double superficie, double prix,
                     String statut, String disponibilite, int idClient) {
        this.idBien = idBien;
        this.type = type;
        this.adresse = adresse;
        this.superficie = superficie;
        this.prix = prix;
        this.statut = statut;
        this.disponibilite = disponibilite;
        this.idClient = idClient;
    }

    // ============================
    // Getters
    // ============================
    public int getIdBien() {
        return idBien;
    }

    public String getType() {
        return type;
    }

    public String getAdresse() {
        return adresse;
    }

    public double getSuperficie() {
        return superficie;
    }

    public double getPrix() {
        return prix;
    }

    public String getStatut() {
        return statut;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public int getIdClient() {
        return idClient;
    }

    // ============================
    // Setters
    // ============================
    public void setIdBien(int idBien) {
        this.idBien = idBien;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    // ============================
    // ToString (utile)
    // ============================
    @Override
    public String toString() {
        return type + " - " + adresse + " (" + statut + ")";
    }
}
