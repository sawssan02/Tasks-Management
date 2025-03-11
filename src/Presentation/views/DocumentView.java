package Presentation.views;

import POJO.Doc;
import POJO.Projet;
import Presentation.models.ModelDocument;
import Presentation.models.ModelProjet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DocumentView {
    private static BorderPane root;
    private ListView<String> documentListView;
    private TextArea descriptionField = new TextArea();
    private Button submitButton;
    private Button addDocumentButton;
    private ModelDocument model=new ModelDocument();
    private List<Doc> documents;
    private Projet projet;
    private ObjectId projetID;
    private ModelProjet modelProjet=new ModelProjet();

    public DocumentView(Projet projet,ObjectId projetID) {
        root = new BorderPane();
        View view = new View();
        this.projet=projet;
        this.projetID=projetID;
        documents=projet.getDoc();
        VBox menu = view.createMenu();
        submitButton = new Button("Soumettre");
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
        contentBody.setStyle("-fx-background-color: #F1F4FB; ");
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.prefWidthProperty().bind(root.widthProperty().subtract(300));
        InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/"+"document.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Label label= new Label("\u00A0\u00A0Document");
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
        documentListView = new ListView<>();
        documentListView.setStyle("-fx-background-color: #B8B9BB;");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
        for (Doc doc : documents) {
            String formattedDate = dateFormat.format(doc.getDateAjout());
            String documentInfo = doc.getDescription() + "\n\n" + formattedDate;
            documentListView.getItems().add(documentInfo);
        }
        double totalHeight = documentListView.getItems().size() * 155;
        documentListView.setPrefHeight(totalHeight);


        String listViewStyle = "-fx-background-color: #F1F4FB;";
        documentListView.setStyle(listViewStyle);
        documentListView.setCellFactory(param -> new ListCell<String>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            {
                setStyle("-fx-background-color: #B3B9CA; -fx-padding: 40px;-fx-border-width: 0 0 1 0;-fx-border-width: 10px;-fx-border-color: #F1F4FB; -fx-background-radius: 40px; ");
                setPadding(new Insets(10, 0, 0, 0)); // Ajoute une marge en bas de 10px
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(10);
                    buttonsBox.getChildren().addAll(editButton, deleteButton);
                    BorderPane cellPane = new BorderPane();
                    cellPane.setLeft(new Label(item));
                    cellPane.setRight(buttonsBox);
                    setGraphic(cellPane);
                    documentListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        int selectedIndex = newValue.intValue();
                        if (selectedIndex >= 0 && selectedIndex < documents.size()) {
                            Doc selectedDoc = documents.get(selectedIndex);
                            deleteButton.setOnAction(event -> {

                                getListView().getItems().remove(item);
                                double totalHeight = documentListView.getItems().size() * 155;
                                documentListView.setPrefHeight(totalHeight);
                                modelProjet.supprimerDocumentDeProjet(projetID,selectedIndex);
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
                                            documentListView.getItems().set(getIndex(), newDocumentInfo);
                                            System.out.println("Description: " + newDescription);
                                            modelProjet.ModifierDocumentDeProjet(projetID,newDescription, new Date(),selectedIndex);
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

                        }
                    });
                    editButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
                    deleteButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
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
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Doc> projetsFiltres = modelProjet.chercherDocParDescription(newValue);
            documentListView.getItems().clear();
            for (Doc projet : projetsFiltres) {
                String projetInfo = projet.getDescription() +  "\n" + projet.getDateAjout();
                documentListView.getItems().add(projetInfo);
            }
        });
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        addDocumentButton = new Button("+");
        addDocumentButton.setStyle("-fx-background-color: #7192BC; -fx-text-fill: white; -fx-font-size: 1.5em;");
        addDocumentButton.setShape(new Circle(20));
        addDocumentButton.setMinSize(50, 50);
        addDocumentButton.setMaxSize(50, 50);
        addDocumentButton.setFont(Font.font(14));
        addDocumentButton.setOnAction(e -> openAddDocumentForm(projetID));
        buttonBox.getChildren().addAll(addDocumentButton);
        contentBody.getChildren().addAll(documentListView);
        content.setCenter(contentBody);
        content.setBottom(buttonBox);
        root.setCenter(content);
    }
    private void openAddDocumentForm(ObjectId id) {
        Stage formStage = new Stage();
        formStage.setTitle("Ajouter un Document");
        VBox formLayout = new VBox();
        formLayout.setSpacing(10);
        formLayout.setPadding(new Insets(20));
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
        submitButton.setOnAction(e -> {
            String description = descriptionField.getText();
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
            String formattedDate = dateFormat.format(currentDate);
            String documentInfo = description + "\n\n" + formattedDate;
            Doc newDoc = new Doc(description, currentDate);
            modelProjet.ajouterDocumentAProjet(id,newDoc);
            documentListView.getItems().add(documentInfo);
            double totalHeight = documentListView.getItems().size() * 155;
            documentListView.setPrefHeight(totalHeight);
            documents = model.readDocuments();
            System.out.println("Description: " + description);
            formStage.close();
        });
        formLayout.getChildren().addAll(
                new Label("Description"), descriptionField,
                new Label("Importer Fichier"), fileButton,
                submitButton
        );
        formStage.setScene(new Scene(formLayout, 400, 500));
        formStage.show();
    }

    public BorderPane getRoot() {
        return root;
    }
}
