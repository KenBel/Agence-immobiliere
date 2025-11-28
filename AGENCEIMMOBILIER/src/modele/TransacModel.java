package modele;

import java.time.LocalDate;

public class TransacModel {

    private int idTransac;
    private int idBien;

    private String proprietaire;  // Nom du vendeur
    private String acheteur;      // Nom de l'acheteur

    private String type;          // Vente / Location
    private float montant;        // Montant total
    private LocalDate date;       // Date de transaction

    // ====================================================
    // Constructeurs
    // ====================================================
    public TransacModel() {}

    public TransacModel(int idTransac, int idBien, String proprietaire, String acheteur,
                        String type, float montant, LocalDate date) {

        this.idTransac = idTransac;
        this.idBien = idBien;
        this.proprietaire = proprietaire;
        this.acheteur = acheteur;
        this.type = type;
        this.montant = montant;
        this.date = date;
    }

    // ====================================================
    // Getters
    // ====================================================
    public int getIdTransac() {
        return idTransac;
    }

    public int getIdBien() {
        return idBien;
    }

    public String getProprietaire() {
        return proprietaire;
    }

    public String getAcheteur() {
        return acheteur;
    }

    public String getType() {
        return type;
    }

    public float getMontant() {
        return montant;
    }

    public LocalDate getDate() {
        return date;
    }

    // ====================================================
    // Setters
    // ====================================================
    public void setIdTransac(int idTransac) {
        this.idTransac = idTransac;
    }

    public void setIdBien(int idBien) {
        this.idBien = idBien;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public void setAcheteur(String acheteur) {
        this.acheteur = acheteur;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // ====================================================
    // toString (utile)
    // ====================================================
    @Override
    public String toString() {
        return "Transaction #" + idTransac + " [" + type + "] - " + montant + "€";
    }
}
