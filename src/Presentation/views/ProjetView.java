package Presentation.views;

import POJO.Doc;
import POJO.Projet;
import Presentation.models.ModelProjet;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ProjetView {
    private BorderPane root;
    private ListView<String> ProjetListView;
    private List<Projet> projets;
    private ModelProjet model=new ModelProjet();
    private final Button passeButton = new Button("Documents");
    private final Button cloneButton = new Button("Cloner");
    public ProjetView() {
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
        InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/"+"logo_projet.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);imageView.setFitWidth(30);
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
        contentBody.getChildren().add(anchorPane);
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setPadding(new Insets(30));
        HBox filter = new HBox();
        Button categorie=new Button("categorie");
        Button type=new Button("type");
        filter.getChildren().addAll(categorie,type);
        ProjetListView = new ListView<>();
        ProjetListView.setStyle("-fx-background-color: #B8B9BB;");
        projets = model.readProjets();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
        for (Projet prj : projets) {
            String formattedDateDebut = dateFormat.format(prj.getDateDebut());
            String formattedDateFin = dateFormat.format(prj.getDateFin());
            String dateNow = dateFormat.format(new Date());
            String statut;
            if(formattedDateFin.compareTo(dateNow) <= 0){
                statut="Cloture";
            }else {
                statut="en cours";
            }
            String projetInfo = prj.getDescription()+"\n" + prj.getType()+"\n"+prj.getCategorie()+"\n"+formattedDateDebut+" - "+formattedDateFin+"\n" +statut;
            ProjetListView.getItems().add(projetInfo);
        }
        double totalHeight = ProjetListView.getItems().size() * 157;
        ProjetListView.setPrefHeight(totalHeight);
        String listViewStyle = "-fx-background-color: #F1F4FB;";
        ProjetListView.setStyle(listViewStyle);
        ProjetListView.setCellFactory(param -> new ListCell<String>() {
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
                    buttonsBox.getChildren().addAll(editButton, deleteButton,cloneButton,passeButton);
                    BorderPane cellPane = new BorderPane();
                    cellPane.setLeft(new Label(item));
                    cellPane.setRight(buttonsBox);
                    setGraphic(cellPane);
                    ProjetListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        int selectedIndex = newValue.intValue();
                        if (selectedIndex >= 0 && selectedIndex < projets.size()) {
                            Projet selectedDoc = projets.get(selectedIndex);
                            ObjectId docId = selectedDoc.getID();
                            deleteButton.setOnAction(event -> {
                                projets = model.readProjets();
                                getListView().getItems().remove(item);
                                double totalHeight = ProjetListView.getItems().size() * 150;
                                ProjetListView.setPrefHeight(totalHeight);
                                model.SupprimerProjet(docId);
                                System.out.println("Supprimer: " + item);
                            });
                            editButton.setOnAction(event -> {
                                String selectedItem = getItem();
                                if (selectedItem != null) {
                                    String categorie = selectedDoc.getCategorie();
                                    String type = selectedDoc.getType();
                                    String description = selectedDoc.getDescription();
                                    Date datedebut=selectedDoc.getDateDebut();
                                    Date datefin=selectedDoc.getDateFin();
                                    List<Doc> docs=selectedDoc.getDoc();
                                    Stage editStage = new Stage();
                                    editStage.setTitle("Modifier le Document");
                                    VBox editLayout = new VBox();
                                    editLayout.setSpacing(10);
                                    editLayout.setPadding(new Insets(20));
                                    TextArea editCategorieField = new TextArea(categorie);
                                    TextArea editTypeField = new TextArea(type);
                                    TextArea editDescriptionField = new TextArea(description);
                                    DatePicker newdatePickerDebut=new DatePicker();
                                    DatePicker newdatePickerFin=new DatePicker();
                                    editCategorieField.setPromptText("Categorie");
                                    editTypeField.setPromptText("Type");
                                    editDescriptionField.setPromptText("Description");
                                    Button editSubmitButton = new Button("Modifier");
                                    editSubmitButton.setOnAction(e -> {
                                        String newDescription = editDescriptionField.getText();
                                        String newCategorie = editCategorieField.getText();
                                        String newType=editTypeField.getText();
                                        LocalDate selectedDateDebut = newdatePickerDebut.getValue();
                                        Date dateDebut = Date.from(selectedDateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        LocalDate selectedDateFin = newdatePickerFin.getValue();
                                        Date dateFin = Date.from(selectedDateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                                        String formattedDateDebut = sdf.format(dateDebut);
                                        String formattedDateFin = sdf.format(dateFin);
                                        String newDocumentInfo =newDescription+"\n" + newType+"\n"+newCategorie+"\n"+formattedDateDebut+" - "+formattedDateFin ;
                                        ProjetListView.getItems().set(getIndex(), newDocumentInfo);
                                        double totalHeight = ProjetListView.getItems().size() * 150; // 50 étant la hauteur d'une cellule
                                        ProjetListView.setPrefHeight(totalHeight);
                                        model.ModifierProjet(newCategorie,newType ,newDescription, datedebut,datefin,docId);
                                        editStage.close();
                                    });
                                    editLayout.getChildren().addAll(
                                            new Label("Description"), editDescriptionField,
                                            new Label("Categorie"), editCategorieField,
                                            new Label("Date Debut"), newdatePickerDebut,
                                            new Label("Date Fin"), newdatePickerFin,
                                            editSubmitButton
                                    );

                                    editStage.setScene(new Scene(editLayout, 400, 200));
                                    editStage.show();
                                } else {
                                    System.out.println("Invalid document format: " + selectedItem);
                                }

                            });
                            passeButton.setOnAction(event -> {
                                ProjetViewMenu  projetViewMenu= new ProjetViewMenu(selectedDoc,docId);
                                Scene currentScene = ProjetListView.getScene();
                                currentScene.setRoot(projetViewMenu.getRoot());
                            });
                            cloneButton.setOnAction(event -> {
                                ObjectId newProjetId = model.clonerProjet(docId);
                                if (newProjetId != null) {
                                    projets = model.readProjets();
                                    Projet newProjet = projets.stream().filter(prj -> prj.getID().equals(newProjetId)).findFirst().orElse(null);
                                    if (newProjet != null) {
                                        String formattedDateDebut = dateFormat.format(newProjet.getDateDebut());
                                        String formattedDateFin = dateFormat.format(newProjet.getDateFin());
                                        String newProjetInfo = newProjet.getDescription() + "\n" + newProjet.getCategorie() + "\n" + formattedDateDebut + " - " + formattedDateFin;
                                        ProjetListView.getItems().add(newProjetInfo);
                                        double totalHeight = ProjetListView.getItems().size() * 150;
                                        ProjetListView.setPrefHeight(totalHeight);
                                    }
                                }
                            });
                        }
                    });
                    editButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
                    deleteButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
                    cloneButton.setStyle("-fx-background-color: #3C465F; -fx-text-fill: white; -fx-padding :3px;");
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
        ComboBox<String> categorieComboBox = new ComboBox<>();
        categorieComboBox.setPromptText("Catégorie");
        categorieComboBox.getItems().addAll("enseignant", "encadrant");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.setPromptText("Type");
        typeComboBox.getItems().addAll("Thèse", "PFE", "PFA", "Cours", "Examen");
        categorieComboBox.setOnAction(e -> {
            String selectedType = categorieComboBox.getValue();
            List<Projet> projetsFiltres = model.filtrerParCategorie(selectedType);
            ProjetListView.getItems().clear();
            for (Projet prj : projetsFiltres) {
                String formattedDateDebut = dateFormat.format(prj.getDateDebut());
                String formattedDateFin = dateFormat.format(prj.getDateFin());
                String projetInfo = prj.getDescription()+"\n" + prj.getType()+"\n"+prj.getCategorie()+"\n"+formattedDateDebut+" - "+formattedDateFin ;
                ProjetListView.getItems().add(projetInfo);
            }
            ProjetListView.setPrefHeight(totalHeight);
        });
        typeComboBox.setOnAction(e -> {
            String selectedType = typeComboBox.getValue();
            List<Projet> projetsFiltres = model.filtrerProjetType(selectedType);
            ProjetListView.getItems().clear();
            for (Projet prj : projetsFiltres) {
                String formattedDateDebut = dateFormat.format(prj.getDateDebut());
                String formattedDateFin = dateFormat.format(prj.getDateFin());
                String projetInfo = prj.getDescription()+"\n" + prj.getType()+"\n"+prj.getCategorie()+"\n"+formattedDateDebut+" - "+formattedDateFin ;
                ProjetListView.getItems().add(projetInfo);
            }
            ProjetListView.setPrefHeight(totalHeight);
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Projet> projetsFiltres = model.searchByDescription(newValue);
            ProjetListView.getItems().clear();
            for (Projet projet : projetsFiltres) {
                String formattedDateDebut = dateFormat.format(projet.getDateDebut());
                String formattedDateFin = dateFormat.format(projet.getDateFin());
                String projetInfo = projet.getDescription() + "\n" + projet.getCategorie() + "\n" + formattedDateDebut + " - " + formattedDateFin;
                ProjetListView.getItems().add(projetInfo);
            }
        });

        HBox filterBox = new HBox(10);
        filterBox.getChildren().addAll(categorieComboBox, typeComboBox);
        contentBody.getChildren().addAll(filterBox, ProjetListView);
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
        TextField nameField = new TextField();
        nameField.setPromptText("Nom du Document");
        TextArea categorieField = new TextArea();
        categorieField.setPromptText("Categorie");
        TextArea TypeField = new TextArea();
        TypeField.setPromptText("Type");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        DatePicker datePickerDebut = new DatePicker();
        DatePicker datePickerFin = new DatePicker();
        datePickerDebut.setPromptText("Date debut");
        datePickerFin.setPromptText("Date fin");
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
            String categorie = categorieField.getText();
            String Type = TypeField.getText();
            String description = descriptionField.getText();
            LocalDate selectedDateDebut = datePickerDebut.getValue();
            Date dateDebut = Date.from(selectedDateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            LocalDate selectedDateFin = datePickerFin.getValue();
            Date dateFin = Date.from(selectedDateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

            ObjectId tacheId = model.ajouterProjet( categorie,Type,description, dateDebut, dateFin, null,null,null);
            System.out.println(tacheId);

            Projet nouvellProjet = new Projet();
            nouvellProjet.setCategorie(categorie);
            nouvellProjet.setCategorie(Type);
            nouvellProjet.setDescription(description);
            nouvellProjet.setDateDebut(dateDebut);
            nouvellProjet.setDateFin(dateFin);

            projets.add(nouvellProjet);
            String documentInfo = description + " \n " + categorie + " \n " + selectedDateDebut + " - " + selectedDateFin;
            ProjetListView.getItems().add(documentInfo);
            double totalHeight = ProjetListView.getItems().size() * 140;
            ProjetListView.setPrefHeight(totalHeight);
            ProjetListView.refresh();
            formStage.close();
        });

        formLayout.getChildren().addAll(
                new Label("Nom du Document"), nameField,
                new Label("Description"), descriptionField,
                new Label("Categorie"), categorieField,
                new Label("type"), TypeField,
                new Label("Date Debut"), datePickerDebut,
                new Label("Date Fin"), datePickerFin,
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

