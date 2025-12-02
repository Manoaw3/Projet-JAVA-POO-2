package Model;

public class Operateur {
    private int id;
    private String nom;
    private String prenom;
    private String specialite;

    // Constructeur complet
    public Operateur(int id, String nom, String prenom, String specialite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
    }

    // Constructeur simplifié sans spécialité
    public Operateur(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = "BSI/IA";
    }

    // recup attributs clsse

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}