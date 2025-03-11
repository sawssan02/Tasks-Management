package Presentation.views;

import POJO.Projet;
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
import org.bson.types.ObjectId;

import java.io.InputStream;


public class ProjetViewMenu {
    private BorderPane root;
    private static Button buttonTask;
    private static Button buttonSeance;
    private static Button buttonDocument;
    private ObjectId projetID;
    private Projet projet;

    public ProjetViewMenu(Projet projet, ObjectId projetID) {
        root = new BorderPane();
        this.projetID=projetID;
        this.projet=projet;
        createMenu();
        createContent();
    }
    public VBox createMenu() {
        View view = new View();
        VBox menu = view.createMenu();
        root.setLeft(menu);
        return menu;
    }
    private void createContent() {
        VBox content = new VBox();
        VBox.setVgrow(content, Priority.ALWAYS);
        content.setPrefWidth(800);
        content.setStyle("-fx-background-color: #ffffff;");
        content.prefWidthProperty().bind(root.widthProperty().subtract(300));
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #ffffff;");
        anchorPane.prefWidthProperty().bind(root.widthProperty().subtract(300));
        InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/"+"logo_projet.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Label label= new Label("\u00A0\u00A0Projet");
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
        content.getChildren().add(anchorPane);
        VBox buttonBox = new VBox();
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.setSpacing(30);
        buttonTask = new Button("Les Taches");
        buttonSeance = new Button("Les seances");
        buttonDocument = new Button("Les documents");
        buttonBox.setPadding(new Insets(90, 30, 0, 30));
        buttonTask.setAlignment(Pos.CENTER_LEFT);
        buttonTask.setPadding(new Insets(0, 0, 0, 45));
        buttonSeance.setAlignment(Pos.CENTER_LEFT);
        buttonSeance.setPadding(new Insets(0, 0, 0, 45));
        buttonDocument.setAlignment(Pos.CENTER_LEFT);
        buttonDocument.setPadding(new Insets(0, 0, 0, 45));
        buttonTask.prefWidthProperty().bind(content.widthProperty().subtract(60));
        buttonSeance.prefWidthProperty().bind(content.widthProperty().subtract(60));
        buttonDocument.prefWidthProperty().bind(content.widthProperty().subtract(60));
        buttonTask.setPrefHeight(60);
        buttonSeance.setPrefHeight(60);
        buttonDocument.setPrefHeight(60);
        buttonTask.setStyle("-fx-background-color: #B3B9CA; -fx-text-fill: white; -fx-font-size: 1.5em;");
        buttonSeance.setStyle("-fx-background-color: #B3B9CA; -fx-text-fill: white; -fx-font-size: 1.5em;");
        buttonDocument.setStyle("-fx-background-color: #B3B9CA; -fx-text-fill: white; -fx-font-size: 1.5em;");
        buttonBox.getChildren().addAll(buttonTask, buttonSeance, buttonDocument);
        content.getChildren().add(buttonBox);
        root.setRight(content);
        buttonTask.setOnAction(event -> {
                TaskView task = new TaskView(projet,projetID);
                Scene currentScene = root.getScene();
                currentScene.setRoot(task.getRoot());
        });
        buttonSeance.setOnAction(event -> {
            SeanceView task = new SeanceView(projet,projetID);
            Scene currentScene = root.getScene();
            currentScene.setRoot(task.getRoot());
        });
        buttonDocument.setOnAction(event -> {
            DocumentView task = new DocumentView(projet,projetID);
            Scene currentScene = root.getScene();
            currentScene.setRoot(task.getRoot());
        });
    }
    public BorderPane getRoot() {
        return root;
    }

}
