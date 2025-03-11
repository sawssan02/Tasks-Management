package Presentation.views;

import POJO.Doc;
import POJO.Projet;
import POJO.Seance;
import POJO.Tache;
import Presentation.models.ModelProjet;
import Presentation.models.ModelTask;
import Service.CalendarQuickstart;
import com.google.api.services.calendar.model.Event;
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
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class TaskView {
    private BorderPane root;
    private ListView<String> TacheListView;
    private List<Tache> taches;
    private ModelTask model=new ModelTask();
    private Projet projet;
    private ObjectId projetID;
    private ModelProjet modelProject=new ModelProjet();
    private boolean isAscending = true;

    public TaskView(Projet projet,ObjectId projetID) {
        root = new BorderPane();
        this.projet=projet;
        this.projetID=projetID;
        taches=projet.getTaches();
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
        Label label= new Label("\u00A0\u00A0Tache");
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
        TacheListView = new ListView<>();
        TacheListView.setStyle("-fx-background-color: #B8B9BB;");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
        for (Tache tache : taches) {
            String formattedDateDebut = dateFormat.format(tache.getDateDebut());
            String formattedDateFin = dateFormat.format(tache.getDateFin());
            String tacheInfo = tache.getDescription()+"\n" + tache.getCategorie()+"\n"+formattedDateDebut+" - "+formattedDateFin ;
            TacheListView.getItems().add(tacheInfo);
        }
        double totalHeight = TacheListView.getItems().size() * 157;
        TacheListView.setPrefHeight(totalHeight);
        String listViewStyle = "-fx-background-color: #F1F4FB;";
        TacheListView.setStyle(listViewStyle);
        TacheListView.setCellFactory(param -> new ListCell<String>() {//cree des cellules pour afficher chaque élément de la liste et chaque element string
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final Button passeButton = new Button("Documents");
            {
                setStyle("-fx-background-color: #B3B9CA; -fx-padding: 40px;-fx-border-width: 0 0 1 0;-fx-border-width: 10px;-fx-border-color: #F1F4FB; -fx-background-radius: 40px; ");
                setPadding(new Insets(10, 0, 0, 0));
            }
            @Override
            protected void updateItem(String item, boolean empty) {// item l'element de la liste et e,pty pour assuree que l'element vide ou non
                super.updateItem(item, empty);//pour mettre à jour le contenu de la cellule
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);// une image, un bouton
                } else {
                    HBox buttonsBox = new HBox(10);
                    buttonsBox.getChildren().addAll(editButton, deleteButton,passeButton);
                    BorderPane cellPane = new BorderPane();
                    cellPane.setLeft(new Label(item));
                    cellPane.setRight(buttonsBox);
                    setGraphic(cellPane);
                    TacheListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        int selectedIndex = newValue.intValue();
                        if (selectedIndex >= 0 && selectedIndex < taches.size()) {
                            Tache selectedDoc = taches.get(selectedIndex);
                            deleteButton.setOnAction(event -> {
                                taches = model.readTaches();
                                getListView().getItems().remove(item);
                                double totalHeight = TacheListView.getItems().size() * 150;
                                TacheListView.setPrefHeight(totalHeight);
                                modelProject.supprimerTacheDeProjet(projetID, selectedIndex);
                            });
                            editButton.setOnAction(event -> {
                                String selectedItem = getItem();
                                if (selectedItem != null) {
                                    String description = selectedDoc.getDescription();
                                    String categorie = selectedDoc.getCategorie();
                                    Date datedebut=selectedDoc.getDateDebut();
                                    Date datefin=selectedDoc.getDateFin();
                                    List<Doc> docs=selectedDoc.getDoc();
                                    Stage editStage = new Stage();
                                    editStage.setTitle("Modifier le Document");
                                    VBox editLayout = new VBox();
                                    editLayout.setSpacing(10);
                                    editLayout.setPadding(new Insets(20));
                                    TextArea editDescriptionField = new TextArea(description);
                                    TextArea editCategorieField = new TextArea(categorie);
                                    DatePicker newdatePickerDebut=new DatePicker();
                                    DatePicker newdatePickerFin=new DatePicker();
                                    editDescriptionField.setPromptText("Description");
                                    Button editSubmitButton = new Button("Modifier");
                                    editSubmitButton.setOnAction(e -> {
                                        String newDescription = editDescriptionField.getText();
                                        String newCategorie = editCategorieField.getText();
                                        LocalDate selectedDateDebut = newdatePickerDebut.getValue();
                                        Date dateDebut = Date.from(selectedDateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        LocalDate selectedDateFin = newdatePickerFin.getValue();
                                        Date dateFin = Date.from(selectedDateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                                        String formattedDateDebut = sdf.format(dateDebut);
                                        String formattedDateFin = sdf.format(dateFin);
                                        String newDocumentInfo =newDescription+"\n"+newCategorie+"\n"+formattedDateDebut+" - "+formattedDateFin;
                                        TacheListView.getItems().set(getIndex(), newDocumentInfo);
                                        double totalHeight = TacheListView.getItems().size() * 150;
                                        TacheListView.setPrefHeight(totalHeight);
                                        modelProject.modifierTacheDeProjet(projetID,newDescription,datedebut,datefin,selectedIndex);
                                        editStage.close();
                                    });
                                    editLayout.getChildren().addAll(
                                            new Label("Description"), editDescriptionField,
                                            new Label("Categorie"), editCategorieField,
                                            new Label("Date Debut"), newdatePickerDebut,
                                            new Label("Date Fin"), newdatePickerFin,
                                            editSubmitButton
                                    );
                                    editStage.setScene(new Scene(editLayout, 400, 600));
                                    editStage.show();
                                }

                            });
                            passeButton.setOnAction(event -> {
                                DocumentTaskView documentTaskView = new DocumentTaskView(selectedDoc,selectedIndex,projetID);
                                Scene currentScene = TacheListView.getScene();
                                currentScene.setRoot(documentTaskView.getRoot());
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
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Tache> projetsFiltres = modelProject.searchTacheByDescription(newValue);
            TacheListView.getItems().clear();
            for (Tache projet : projetsFiltres) {
                String formattedDateDebut = dateFormat.format(projet.getDateDebut());
                String formattedDateFin = dateFormat.format(projet.getDateFin());
                String projetInfo = projet.getDescription() + "\n" + projet.getCategorie() + "\n" + formattedDateDebut + " - " + formattedDateFin;
                TacheListView.getItems().add(projetInfo);
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
        addDocumentButton.setOnAction(e -> {
            openAddDocumentForm(projet.getID());
        });
        Button sortButton = new Button("Trier par Date");
        sortButton.setFont(Font.font(14));
        sortButton.setOnAction(e -> {
            if (isAscending) {
                taches.sort((t1, t2) -> t1.getDateDebut().compareTo(t2.getDateDebut()));
            } else {
                taches.sort((t1, t2) -> t2.getDateDebut().compareTo(t1.getDateDebut()));
            }
            isAscending = !isAscending;
            TacheListView.getItems().clear();
            for (Tache projet : taches) {
                String formattedDateDebut = dateFormat.format(projet.getDateDebut());
                String formattedDateFin = dateFormat.format(projet.getDateFin());
                String projetInfo = projet.getDescription() + "\n" + projet.getCategorie() + "\n" + formattedDateDebut + " - " + formattedDateFin;
                TacheListView.getItems().add(projetInfo);
            }
        });
        ComboBox<String> categorieComboBox = new ComboBox<>();
        categorieComboBox.setPromptText("Catégorie");
        categorieComboBox.getItems().addAll("enseignant", "encadrant");
        categorieComboBox.setOnAction(e -> {
            String selectedType = categorieComboBox.getValue();
            List<Tache> projetsFiltres = modelProject.filtrerTachesParCategorie(selectedType);
            TacheListView.getItems().clear();
            for (Tache prj : projetsFiltres) {
                String formattedDateDebut = dateFormat.format(prj.getDateDebut());
                String formattedDateFin = dateFormat.format(prj.getDateFin());
                String projetInfo = prj.getDescription()+"\n" +prj.getCategorie()+"\n"+formattedDateDebut+" - "+formattedDateFin ;
                TacheListView.getItems().add(projetInfo);
            }
            TacheListView.setPrefHeight(totalHeight);
        });
        ComboBox<String> etat = new ComboBox<>();
        etat.setPromptText("etat");
        etat.getItems().addAll("fini", "pas fini");
        etat.setOnAction(e -> {
            String selectedType = etat.getValue();
            Boolean stat;
            if(selectedType=="fini"){
                stat=true;
            }else{
                stat=false;
            }
            List<Tache> projetsFiltres = modelProject.filtrerTachesParStatut(stat);
            TacheListView.getItems().clear();
            for (Tache prj : projetsFiltres) {
                String formattedDateDebut = dateFormat.format(prj.getDateDebut());
                String formattedDateFin = dateFormat.format(prj.getDateFin());
                String projetInfo = prj.getDescription()+"\n" +prj.getCategorie()+"\n"+formattedDateDebut+" - "+formattedDateFin ;
                TacheListView.getItems().add(projetInfo);
            }
            TacheListView.setPrefHeight(totalHeight);
        });
        HBox filter=new HBox();
        filter.getChildren().addAll(categorieComboBox,sortButton,etat);
        buttonBox.getChildren().addAll(addDocumentButton);
        contentBody.getChildren().addAll(filter,TacheListView);
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
        TextField nameField = new TextField();
        nameField.setPromptText("Nom du Document");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        TextArea categorieField = new TextArea();
        descriptionField.setPromptText("Categorie");
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
            String description = descriptionField.getText();
            String categorie = categorieField.getText();
            LocalDate selectedDateDebut = datePickerDebut.getValue();
            Date dateDebut = Date.from(selectedDateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            LocalDate selectedDateFin = datePickerFin.getValue();
            Date dateFin = Date.from(selectedDateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Tache nouvelleTache = new Tache();
            nouvelleTache.setDescription(description);
            nouvelleTache.setCategorie(categorie);
            nouvelleTache.setDateDebut(dateDebut);
            nouvelleTache.setDateFin(dateFin);
            nouvelleTache.setDoc(null);
            modelProject.ajouterTacheAProjet(id,nouvelleTache);
            taches.add(nouvelleTache);
            String documentInfo = description + " \n " + categorie + " \n " + selectedDateDebut + " - " + selectedDateFin;
            TacheListView.getItems().add(documentInfo);
            double totalHeight = TacheListView.getItems().size() * 140;
            TacheListView.setPrefHeight(totalHeight);
            TacheListView.refresh();
            formStage.close();
        });
        Button fetchEventsButton = new Button("Récupérer Événements");
        fetchEventsButton.setOnAction(event -> {
            try {
                List<Event> events = new CalendarQuickstart().getCalendarEvents();
                getEvents(events);
                formStage.close();
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        });
        formLayout.getChildren().addAll(
                new Label("Nom du Document"), nameField,
                new Label("Description"), descriptionField,
                new Label("Categorie"), categorieField,
                new Label("Date Debut"), datePickerDebut,
                new Label("Date Fin"), datePickerFin,
                new Label("Importer Fichier"), fileButton,
                submitButton,
                fetchEventsButton
        );
        formStage.setScene(new Scene(formLayout, 400, 600));
        formStage.show();
    }
    private void getEvents(List<Event> events) {
        for (Event event : events) {
            Tache nouvelleTache = new Tache();
            nouvelleTache.setDescription(event.getSummary());
            Date dateDebut = new Date(event.getStart().getDateTime().getValue());
            Date dateFin = new Date(event.getEnd().getDateTime().getValue());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
            String formattedDateDebut = dateFormat.format(dateFin);
            String formattedDateFin = dateFormat.format(dateFin);
            nouvelleTache.setDateDebut(dateDebut);
            nouvelleTache.setDateFin(dateFin);
            nouvelleTache.setDoc(null);
            modelProject.ajouterTacheAProjet(projet.getID(),nouvelleTache);
            taches.add(nouvelleTache);
            String documentInfo = event.getSummary() + " \n " + " \n " + formattedDateDebut  + " - " + formattedDateFin;
            TacheListView.getItems().add(documentInfo);
            double totalHeight = TacheListView.getItems().size() * 150;
            TacheListView.setPrefHeight(totalHeight);
            TacheListView.refresh();
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}
