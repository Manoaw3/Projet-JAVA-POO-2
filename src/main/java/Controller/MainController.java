package Controller;

import Model.Poste;
import Service.CSVReader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MainController {

    @FXML
    private FlowPane stationsContainer;

    @FXML
    private ProgressBar globalProgressBar;

    @FXML
    private Button startButton;

    private List<Poste> postes;
    private Timeline simulationTimeline;
    private Random random = new Random();

    @FXML
    public void initialize() {
        // chargerPostes(); // Ne plus charger automatiquement
        startButton.setDisable(true); // Désactiver le bouton start au début
        configurerDependances();
        // Initialiser à 0 pour la simulation
        if (postes != null) {
            postes.forEach(p -> p.setProgression(0.0));
        }
        afficherPostes();
    }

    @FXML
    private void importerCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner le fichier CSV des postes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers CSV", "*.csv"));

        // Point de départ (optionnel, dossier du proj)
        File initialDir = new File("src/main/java/Data");
        if (initialDir.exists()) {
            fileChooser.setInitialDirectory(initialDir);
        }

        File selectedFile = fileChooser.showOpenDialog(stationsContainer.getScene().getWindow());

        if (selectedFile != null) {
            chargerPostes(selectedFile.getAbsolutePath());
            configurerDependances();
            // Initialiser à 0 pour la simulation
            if (postes != null) {
                postes.forEach(p -> p.setProgression(0.0));
            }
            afficherPostes();
            startButton.setDisable(false);
            startButton.setText("▶ Démarrer Production");
            startButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        }
    }

    private void chargerPostes(String cheminCSV) {
        postes = CSVReader.lirePostes(cheminCSV);
    }

    private void configurerDependances() {
        if (postes != null) {
            // Gestion des dependances du Poste 4
            if (postes.size() >= 4) {
                postes.get(3).addDependance(3); // ID 3
                postes.get(3).addDependance(2); // ID 2
            }

            // Gestion des dependances du Poste 5
            if (postes.size() >= 5) {
                postes.get(4).addDependance(2); // Dépend du Poste 2
                postes.get(4).addDependance(4); // Dépend du Poste 4
            }

            // Poste 6 depend on Poste 3 and Poste 5
            if (postes.size() >= 6) {
                postes.get(5).addDependance(3); // ID 3
                postes.get(5).addDependance(5); // ID 5
            }
        }
    }

    @FXML
    private void demarrerProduction() {
        if (simulationTimeline != null && simulationTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // Déjà en cours
        }

        startButton.setDisable(true);
        startButton.setText("Production en cours...");

        simulationTimeline = new Timeline(new KeyFrame(Duration.millis(500), e -> mettreAJourSimulation()));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationTimeline.play();
    }

    private void mettreAJourSimulation() {
        boolean toutFini = true;

        if (postes != null) {
            for (Poste poste : postes) {
                if (poste.getProgression() < 1.0) {
                    toutFini = false;

                    // Vérifier les dépendances
                    boolean locked = false;
                    for (int depId : poste.getDependances()) {
                        Poste depPoste = postes.stream()
                                .filter(p -> p.getNumero() == depId)
                                .findFirst()
                                .orElse(null);

                        if (depPoste != null && depPoste.getProgression() < 1.0) {
                            locked = true;
                            break;
                        }
                    }

                    if (!locked) {
                        // Avancer la progression
                        double increment = 0.05 + (random.nextDouble() * 0.1); // Entre 5% et 15%
                        double nouvelleProgression = Math.min(1.0, poste.getProgression() + increment);
                        poste.setProgression(nouvelleProgression);
                    }
                }
            }

            afficherPostes(); // Rafraîchir l'interface

            if (toutFini) {
                simulationTimeline.stop();
                startButton.setText("Production Terminée !");
                startButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
            }
        }
    }

    private void afficherPostes() {
        stationsContainer.getChildren().clear();

        if (postes != null) {
            for (Poste poste : postes) {
                VBox card = new VBox(10);
                card.getStyleClass().add("station-card");
                card.setPrefWidth(200);
                card.setStyle("-fx-alignment: center;");

                Button btn = new Button(poste.getNom());
                btn.setMaxWidth(Double.MAX_VALUE);

                // Vérifier si le poste est verrouillé
                boolean locked = false;
                for (int depId : poste.getDependances()) {
                    Poste depPoste = postes.stream().filter(p -> p.getNumero() == depId).findFirst().orElse(null);
                    if (depPoste != null && depPoste.getProgression() < 1.0) {
                        locked = true;
                        break;
                    }
                }

                if (locked) {
                    btn.setDisable(true);
                    btn.setTooltip(new Tooltip("En attente des postes précédents..."));
                } else {
                    btn.setOnAction(e -> ouvrirDetails(poste));
                }

                ProgressBar pb = new ProgressBar(poste.getProgression());
                pb.setMaxWidth(Double.MAX_VALUE);

                // change de couleur si fini
                if (poste.getProgression() >= 1.0) {
                    pb.setStyle("-fx-accent: #27ae60;"); // Green
                } else if (locked) {
                    pb.setStyle("-fx-accent: #7f8c8d;"); // Grey
                }

                Label progressLabel = new Label(String.format("%.0f%%", poste.getProgression() * 100));
                progressLabel
                        .setStyle("-fx-font-weight: bold; -fx-text-fill: " + (locked ? "#7f8c8d" : "#ecf0f1") + ";");

                card.getChildren().addAll(btn, pb, progressLabel);
                stationsContainer.getChildren().add(card);
            }

            // Update la progress barre (la moyenne des postes)
            double totalProgress = postes.stream().mapToDouble(Poste::getProgression).average().orElse(0.0);
            globalProgressBar.setProgress(totalProgress);
        }
    }

    private void ouvrirDetails(Poste poste) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cesibike/details.fxml"));
            Parent root = loader.load();

            // Apply CSS
            root.getStylesheets().add(getClass().getResource("/com/example/cesibike/style.css").toExternalForm());

            DetailController controller = loader.getController();
            controller.setPoste(poste);

            Stage stage = new Stage();
            stage.setTitle("Détails - " + poste.getNom());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}