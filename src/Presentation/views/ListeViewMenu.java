package Presentation.views;

import POJO.Liste;
import Presentation.models.ModelListe;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ListeViewMenu {
    private BorderPane root;
    private ListView<String> ListListView;
    private List<Liste> listes;
    private ModelListe model=new ModelListe();
    private final Button passeButton = new Button("Documents");

    public ListeViewMenu() {
        root = new BorderPane();
        View view = new View();
        VBox menu = view.createMenu();
        root.setLeft(menu);
        createContent();
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
        InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/"+"logo_tache.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Label label= new Label("\u00A0\u00A0Liste");
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
        contentBody.getChildren().add(anchorPane);
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setPadding(new Insets(30));
        HBox filter = new HBox();
        Button categorie=new Button("categorie");
        Button type=new Button("type");
        filter.getChildren().addAll(categorie,type);
        ListListView = new ListView<>();
        ListListView.setStyle("-fx-background-color: #B8B9BB;");
        listes = model.readListes();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
        for (Liste prj : listes) {
            String projetInfo = prj.getDescription();
            ListListView.getItems().add(projetInfo);
        }
        double totalHeight = ListListView.getItems().size() * 157;
        ListListView.setPrefHeight(totalHeight);
        String listViewStyle = "-fx-background-color: #F1F4FB;";
        ListListView.setStyle(listViewStyle);
        ListListView.setCellFactory(param -> new ListCell<String>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                setStyle("-fx-background-color: #B3B9CA; -fx-padding: 40px;-fx-border-width: 0 0 1 0;-fx-border-width: 10px;-fx-border-color: #F1F4FB; -fx-background-radius: 40px; ");
                setPadding(new Insets(10, 0, 0, 0));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(10);
                    buttonsBox.getChildren().addAll(editButton, deleteButton,passeButton);
                    BorderPane cellPane = new BorderPane();
                    cellPane.setLeft(new Label(item));
                    cellPane.setRight(buttonsBox);
                    setGraphic(cellPane);
                    ListListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        int selectedIndex = newValue.intValue();
                        if (selectedIndex >= 0 && selectedIndex < listes.size()) {
                            Liste selectedDoc = listes.get(selectedIndex);
                            ObjectId docId = selectedDoc.getID();
                            deleteButton.setOnAction(event -> {
                                listes = model.readListes();

                                getListView().getItems().remove(item);
                                double totalHeight = ListListView.getItems().size() * 150;
                                ListListView.setPrefHeight(totalHeight);
                                model.SupprimerListe(docId);

                                System.out.println("Supprimer: " + item);
                            });
                            editButton.setOnAction(event -> {
                                String selectedItem = getItem();
                                if (selectedItem != null) {
                                    String description = selectedDoc.getDescription();
                                    Stage editStage = new Stage();
                                    editStage.setTitle("Modifier le Document");
                                    VBox editLayout = new VBox();
                                    editLayout.setSpacing(10);
                                    editLayout.setPadding(new Insets(20));
                                    TextArea editDescriptionField = new TextArea(description);
                                    editDescriptionField.setPromptText("Description");
                                    Button editSubmitButton = new Button("Modifier");
                                    editSubmitButton.setOnAction(e -> {
                                        String newDescription = editDescriptionField.getText();
                                        String newDocumentInfo =newDescription;
                                        ListListView.getItems().set(getIndex(), newDocumentInfo);
                                        double totalHeight = ListListView.getItems().size() * 150; // 50 étant la hauteur d'une cellule
                                        ListListView.setPrefHeight(totalHeight);
                                        model.ModifierListe( newDescription,docId);
                                        editStage.close();
                                    });
                                    editLayout.getChildren().addAll(
                                            new Label("Description"), editDescriptionField,
                                            editSubmitButton
                                    );

                                    editStage.setScene(new Scene(editLayout, 400, 200));
                                    editStage.show();
                                } else {
                                    System.out.println("Invalid document format: " + selectedItem);
                                }

                            });
                            passeButton.setOnAction(event -> {
                                ListTacheView  projetViewMenu= new ListTacheView(selectedDoc,docId);
                                Scene currentScene = ListListView.getScene();
                                currentScene.setRoot(projetViewMenu.getRoot());
                            });
                        }
                    });
                    editButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
                    deleteButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
                    passeButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
                    {
                        selectedProperty().addListener((observable, wasSelected, isSelected) -> {
                            if (isSelected) {

                                setStyle("-fx-background-color: #ADB5CD; -fx-text-fill: white;-fx-padding :40px;-fx-border-width: 10px;-fx-border-color: #F1F4FB; -fx-background-radius: 40px;");

                            } else {
                                setStyle("-fx-background-color: #B3B9CA; -fx-text-fill: black;-fx-padding :40px;-fx-border-width: 10px;-fx-border-color: #F1F4FB; -fx-background-radius: 40px;");

                            }
                        });
                    }
                }
            }
        });
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        Button addDocumentButton = new Button("+");
        addDocumentButton.setStyle("-fx-background-color: #7192BC; -fx-text-fill: white; -fx-font-size: 1.5em;");
        addDocumentButton.setShape(new Circle(20));
        addDocumentButton.setMinSize(50, 50);
        addDocumentButton.setMaxSize(50, 50);
        addDocumentButton.setFont(Font.font(14));
        addDocumentButton.setOnAction(e -> openAddDocumentForm());
        buttonBox.getChildren().addAll(addDocumentButton);
        HBox filterBox = new HBox(10);
        contentBody.getChildren().addAll(filterBox, ListListView);
        content.setCenter(contentBody);
        content.setBottom(buttonBox);
        root.setCenter(content);
    }
    private void openAddDocumentForm() {
        Stage formStage = new Stage();
        formStage.setTitle("Ajouter un Document");
        VBox formLayout = new VBox();
        formLayout.setSpacing(10);
        formLayout.setPadding(new Insets(20));
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        Button fileButton = new Button("Importer Fichier");
        fileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un Fichier");
            fileChooser.getExtensionFilters().clear();
            File selectedFile = fileChooser.showOpenDialog(formStage);
            if (selectedFile != null) {
                System.out.println("Fichier sélectionné: " + selectedFile.getName());
            }
        });
        Button submitButton = new Button("Soumettre");
        submitButton.setOnAction(e -> {
            String description = descriptionField.getText();
            ObjectId tacheId = model.ajouterListe(description, null);
            System.out.println(tacheId);

            Liste nouvellListe = new Liste();
            nouvellListe.setDescription(description);

            listes.add(nouvellListe);
            String documentInfo = description;
            ListListView.getItems().add(documentInfo);
            double totalHeight = ListListView.getItems().size() * 140;
            ListListView.setPrefHeight(totalHeight);
            ListListView.refresh();
            formStage.close();
        });

        formLayout.getChildren().addAll(
                new Label("Description"), descriptionField,
                new Label("Importer Fichier"), fileButton,
                submitButton
        );
        formStage.setScene(new Scene(formLayout, 400, 600));
        formStage.show();
    }
    public BorderPane getRoot() {
        return root;
    }
    public Button getDocument(){
        return passeButton;
    }
}


