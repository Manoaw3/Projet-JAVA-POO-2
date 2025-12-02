package Model;

public class Etape {
    private int numero;
    private String nom;
    private String description;
    private int dureeEstimee; // en minutes
    private int quantite;
    private boolean estTerminee;

    // Constructeur Classe Etape
    public Etape(int numero, String nom, String description, int dureeEstimee) {
        this.numero = numero;
        this.nom = nom;
        this.description = description;
        this.dureeEstimee = dureeEstimee;
        this.estTerminee = false;
    }

    // Constructeur simplifié sans description si ya pas de description, (ya juste
    // pas l'objet attribute description)
    public Etape(int numero, String nom, int dureeEstimee, int quantite) {
        this.numero = numero;
        this.nom = nom;
        this.description = "";
        this.dureeEstimee = dureeEstimee;
        this.quantite = quantite;
        this.estTerminee = false;
    }

    // Recup les attributs de la classe
    public int getNumero() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getDureeEstimee() {
        return dureeEstimee;
    }

    public int getQuantite() {
        return quantite;
    }

    public boolean isEstTerminee() {
        return estTerminee;
    }

    public void setEstTerminee(boolean estTerminee) {
        this.estTerminee = estTerminee;
    }

    public void terminer() {
        this.estTerminee = true;
    }
}