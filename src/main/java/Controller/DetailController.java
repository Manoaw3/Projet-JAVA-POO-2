package Controller;

import Model.Etape;
import Model.Poste;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.InputStream;

public class DetailController {

    @FXML
    private Label posteTitle;

    @FXML
    private ImageView posteImage;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TableView<Etape> piecesTable;

    @FXML
    private TableColumn<Etape, String> pieceNameColumn;

    @FXML
    private TableColumn<Etape, Integer> pieceQuantityColumn;

    @FXML
    private Button closeButton;

    private Poste poste;

    @FXML
    public void initialize() {
        // Initialiser la tableau des pieces + qtt
        pieceNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        pieceQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
        updateUI();
    }

    private void updateUI() {
        if (poste != null) {
            posteTitle.setText(poste.getNom());

            // Description
            if (poste.getOperateur() != null) {
                descriptionLabel.setText("Opérateur: " + poste.getOperateur().getNomComplet());
            } else {
                descriptionLabel.setText("Aucun opérateur assigné.");
            }

            // Image mm si y'en a pas
            try {
                String imagePath = "/images/" + poste.getImagePath();
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is != null) {
                    posteImage.setImage(new Image(is));
                } else {
                    // Fallback or placeholder
                    // System.out.println("Image not found: " + imagePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Tableau des etapes
            if (poste.getEtapes() != null) {
                ObservableList<Etape> data = FXCollections.observableArrayList(poste.getEtapes());
                piecesTable.setItems(data);
            }
        }
    }
}