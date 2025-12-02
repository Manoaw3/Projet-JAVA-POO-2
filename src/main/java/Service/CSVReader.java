package Service;

import Model.Poste;
import Model.Operateur;
import Model.Etape;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVReader {

    // Méthode pour lire le fichier CSV et créer les postes
    public static List<Poste> lirePostes(String cheminFichier) {
        List<Poste> postes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            boolean premiereLigne = true;
            int numeroPoste = 1;
            int idOperateur = 1;

            while ((ligne = br.readLine()) != null) {
                // Ignorer la première ligne (en-têtes)
                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }

                // Découper la ligne avec le séparateur point-virgule
                String[] colonnes = ligne.split(";");

                if (colonnes.length >= 3) {
                    // Colonne 1 : Nom du poste
                    String nomPoste = colonnes[0].trim();

                    // Colonne 2 : Opérateur (format "Prénom Nom")
                    String[] nomComplet = colonnes[1].trim().split(" ");
                    String prenom = nomComplet.length > 0 ? nomComplet[0] : "Inconnu";
                    String nom = nomComplet.length > 1 ? nomComplet[1] : "";

                    Operateur operateur = new Operateur(idOperateur++, nom, prenom);

                    // Colonne 3 : Étapes au format [Nom_Piece:nombre][Nom_Piece:nombre]
                    List<Etape> etapes = parseEtapes(colonnes[2]);

                    // Créer le poste avec l'opérateur
                    Poste poste = new Poste(numeroPoste++, nomPoste, operateur);
                    poste.setEtapes(etapes);

                    postes.add(poste);
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }

        return postes;
    }

    // Méthode pour mettre les les etps les étapes au format
    // [Nom_Piece:nombre][Nom_Piece:nombre]
    private static List<Etape> parseEtapes(String etapesStr) {
        List<Etape> etapes = new ArrayList<>();

        // Pattern pour capturer [Nom:nombre]
        Pattern pattern = Pattern.compile("\\[([^:]+):([0-9]+)\\]");
        Matcher matcher = pattern.matcher(etapesStr);

        int numeroEtape = 1;
        while (matcher.find()) {
            String nomPiece = matcher.group(1).trim();
            int quantite = Integer.parseInt(matcher.group(2));

            // Créer une étape (durée estimée = quantité * 5 minutes par exemple)
            Etape etape = new Etape(numeroEtape++, nomPiece, quantite * 5, quantite);
            etapes.add(etape);
        }

        return etapes;
    }

    public static void main(String[] args) {
        // Le chemin
        String cheminCSV = "C://Users/manoa/IdeaProjects/CesiBike/src/main/java/Data/poste.csv";

        List<Poste> postes = lirePostes(cheminCSV);

        System.out.println("Nombre de postes chargés : " + postes.size());
        for (Poste poste : postes) {
            System.out.println("Poste " + poste.getNumero() + " : " + poste.getNom());
            if (poste.getOperateur() != null) {
                System.out.println("  Opérateur : " + poste.getOperateur().getNomComplet());
            }
        }
    }
}