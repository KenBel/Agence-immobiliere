package modele;

public class ClientModel {

    private int idClient;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String statut;

    // ============================
    // Constructeurs
    // ============================
    public ClientModel() {}

    public ClientModel(int idClient, String nom, String prenom, String email, String telephone, String adresse, String statut) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.statut = statut;
    }

    // ============================
    // Getters
    // ============================
    public int getIdClient() {
        return idClient;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getStatut() {
        return statut;
    }

    // ============================
    // Setters
    // ============================
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + statut + ")";
    }
}
