package Model;

public class Poste {
    private int numero;
    private String nom;
    private double progression;
    private Operateur operateur;
    private Etape etapeEnCours;
    private java.util.List<Etape> etapes;
    private java.util.List<Integer> dependances;
    private String imagePath;

    // Constructeur complettttt
    public Poste(int numero, String nom) {
        this.numero = numero;
        this.nom = nom;
        this.progression = 0.0;
        this.operateur = null;
        this.etapeEnCours = null;
    }

    // Constructeur
    public Poste(int numero, String nom, Operateur operateur) {
        this.numero = numero;
        this.nom = nom;
        this.progression = 0.0;
        this.operateur = operateur;
        this.etapeEnCours = null;
        this.etapes = new java.util.ArrayList<>();
        this.dependances = new java.util.ArrayList<>();
        this.imagePath = "poste" + numero + ".png"; // img
    }

    // recup attributs clsse
    public int getNumero() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public double getProgression() {
        return progression;
    }

    public void setProgression(double progression) {
        this.progression = progression;
    }

    public Operateur getOperateur() {
        return operateur;
    }

    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }

    public Etape getEtapeEnCours() {
        return etapeEnCours;
    }

    public void setEtapeEnCours(Etape etapeEnCours) {
        this.etapeEnCours = etapeEnCours;
    }

    public java.util.List<Etape> getEtapes() {
        return etapes;
    }

    public void setEtapes(java.util.List<Etape> etapes) {
        this.etapes = etapes;
    }

    public java.util.List<Integer> getDependances() {
        return dependances;
    }

    public void addDependance(int posteId) {
        this.dependances.add(posteId);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
