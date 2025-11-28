package modele;

import java.time.LocalDate;
import java.time.LocalTime;

public class RdvModel {

    private int idRdv;
    private int idBien;

    private String proprietaire;   // Nom du propriétaire du bien
    private String preneur;        // Nom du client intéressé
    private String agent;          // Nom de l’agent

    private LocalDate date;
    private LocalTime heure;

    // ====================================================
    // Constructeurs
    // ====================================================
    public RdvModel() {}

    public RdvModel(int idRdv, int idBien, String proprietaire, String preneur,
                    LocalDate date, LocalTime heure, String agent) {

        this.idRdv = idRdv;
        this.idBien = idBien;
        this.proprietaire = proprietaire;
        this.preneur = preneur;
        this.date = date;
        this.heure = heure;
        this.agent = agent;
    }

    // ====================================================
    // Getters
    // ====================================================
    public int getIdRdv() {
        return idRdv;
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

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public String getAgent() {
        return agent;
    }

    // ====================================================
    // Setters
    // ====================================================
    public void setIdRdv(int idRdv) {
        this.idRdv = idRdv;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    // ====================================================
    // toString (utile)
    // ====================================================
    @Override
    public String toString() {
        return "RDV #" + idRdv + " - " + proprietaire + " / " + preneur + " (" + date + " " + heure + ")";
    }
}
