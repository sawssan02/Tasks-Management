package Presentation.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;

public class View {
    private BorderPane root;
    private  Button buttonAccueil;
    private  Button buttonTache ;
    private  Button buttonProjet;
    private  Button buttonStatistique;

    public View() {
        root = new BorderPane();
        createMenu();
        createContent();
    }
    public VBox createMenu() {
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER_LEFT);
        menu.setMinWidth(300);
        menu.setSpacing(30);
        menu.setPadding(new Insets(40));
        buttonAccueil =createMenuItem("Accueil", "acceuil.png");
        buttonTache = createMenuItem("Liste", "tache.png");
        buttonProjet = createMenuItem("Projet", "projet.png");
        buttonStatistique =createMenuItem("Statistique", "statistique.png");
        menu.getChildren().addAll(buttonAccueil,buttonTache ,buttonProjet ,buttonStatistique  );
        menu.setStyle("-fx-background-color: #7192BC");
        root.setLeft(menu);
        buttonStatistique.setOnAction(event -> {
            Statistique statistique = new Statistique();
            Scene scene = new Scene(statistique.getRoot(), 800, 600);
            Stage stage = (Stage) buttonStatistique.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
        buttonAccueil.setOnAction(event -> {
            View acceuil = new View();
            Scene scene = new Scene(acceuil.getRoot(), 800, 600);
            Stage stage = (Stage) buttonAccueil.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
        buttonTache.setOnAction(event -> {
            ListeViewMenu task = new ListeViewMenu();
            Scene scene = new Scene(task.getRoot(), 800, 600);
            Stage stage = (Stage) buttonTache.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });
        buttonProjet.setOnAction(event -> {
            ProjetView projet = new ProjetView();
            Scene scene = new Scene(projet.getRoot(), 800, 600);
            Stage stage = (Stage) buttonProjet.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });

        return menu;
    }
    private Button createMenuItem(String text, String imageName) {
        Button button = new Button();
        button.setText("\u00A0\u00A0\u00A0"+text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 1.5em;");
        button.setAlignment(Pos.BASELINE_LEFT);
        try {
            InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/" + imageName);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                button.setGraphic(imageView);
            } else {
                System.err.println("Image file not found: " + imageName);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        return button;
    }
    private void createContent() {
        BorderPane content = new BorderPane();
        content.setStyle("-fx-background-color: #F1F4FB;");
        content.prefWidthProperty().bind(root.widthProperty().subtract(300));
        VBox contentBody = new VBox();
        contentBody.setAlignment(Pos.TOP_LEFT);
        contentBody.setSpacing(20);
        contentBody.setStyle("-fx-background-color: #F1F4FB; -fx-padding: 10px;");
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefWidthProperty().bind(root.widthProperty().subtract(300));
        InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/"+"logo_acceuil.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Label label= new Label("\u00A0\u00A0Acceuil");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setFont(Font.font(14));
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-padding: 5px;");
        HBox hbox1 = new HBox(imageView, label);
        HBox hbox2 = new HBox(searchField);
        anchorPane.getChildren().addAll(hbox1, hbox2);
        AnchorPane.setTopAnchor(hbox1, 20.0);
        AnchorPane.setLeftAnchor(hbox1, 20.0);
        AnchorPane.setTopAnchor(hbox2, 20.0);
        AnchorPane.setRightAnchor(hbox2, 20.0);
        VBox message = new VBox();
        Label labelhaut = new Label("Hi");
        Label labelbas = new Label("Ready yourself for success by effectively organizing your tasks");
        labelhaut.setStyle("-fx-font-size:30px; -fx-text-fill:white;");
        labelbas.setStyle("-fx-font-size:15px; ");
        labelbas.setWrapText(true);
        message.getChildren().addAll(labelhaut, labelbas);
        InputStream inputStreammessage = getClass().getResourceAsStream("/Presentation/images/"+"logo_accueil_message.png");
        Image imageMessage = new Image(inputStreammessage);
        ImageView imageViewmessage = new ImageView(imageMessage);
        imageViewmessage.setFitWidth(140);
        imageViewmessage.setFitHeight(160);
        HBox hboxleft = new HBox(message);
        VBox vboxright = new VBox(imageViewmessage);
        vboxright.setPrefHeight(80);
        AnchorPane anchorPanemessage = new AnchorPane();
        labelbas.maxWidthProperty().bind(anchorPanemessage.widthProperty().multiply(0.6));
        anchorPanemessage.getChildren().addAll(hboxleft, vboxright);
        anchorPanemessage.setStyle("-fx-background-color:#B3B9CA;");
        AnchorPane.setTopAnchor(hboxleft, 20.0);
        AnchorPane.setLeftAnchor(hboxleft, 20.0);
        AnchorPane.setTopAnchor(vboxright, -20.0);
        AnchorPane.setRightAnchor(vboxright, -5.0);
        AnchorPane.setBottomAnchor(vboxright, 0.0);
        anchorPanemessage.setMaxHeight(100);
        anchorPanemessage.setMinHeight(130);
        anchorPanemessage.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            Insets insets = new Insets(40, newBounds.getWidth() * 0.1, 0, newBounds.getWidth() * 0.1);
            VBox.setMargin(anchorPanemessage, insets);
        });
        contentBody.getChildren().addAll(anchorPane,anchorPanemessage);
        content.setCenter(contentBody);
        root.setCenter(content);
    }
    public BorderPane getRoot() {
        return root;
    }
}
