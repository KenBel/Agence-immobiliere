package modele;

public class AgentModel {

    private int idAgent;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private int nbrAffaires;
    private double salaire;
    private String nomUtilisateur;
    private String motDePasse;

    // ============================
    // Constructeurs
    // ============================
    public AgentModel() {}

    public AgentModel(int idAgent, String nom, String prenom, String email, String telephone,
                      String adresse, int nbrAffaires, double salaire,
                      String nomUtilisateur, String motDePasse) {

        this.idAgent = idAgent;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.nbrAffaires = nbrAffaires;
        this.salaire = salaire;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
    }

    // ============================
    // Getters
    // ============================
    public int getIdAgent() {
        return idAgent;
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

    public int getNbrAffaires() {
        return nbrAffaires;
    }

    public double getSalaire() {
        return salaire;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    // ============================
    // Setters
    // ============================
    public void setIdAgent(int idAgent) {
        this.idAgent = idAgent;
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

    public void setNbrAffaires(int nbrAffaires) {
        this.nbrAffaires = nbrAffaires;
    }

    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    // ============================
    // toString (utile)
    // ============================
    @Override
    public String toString() {
        return nom + " " + prenom + " (" + email + ")";
    }
}
