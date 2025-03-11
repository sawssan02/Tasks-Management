package Presentation.views;

import POJO.Projet;
import POJO.Seance;
import Presentation.models.ModelProjet;
import Presentation.models.ModelSeance;
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
import Service.CalendarQuickstart;
import org.bson.types.ObjectId;
import com.google.api.services.calendar.model.Event;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SeanceView {
    private BorderPane root;
    private ListView<String> SeanceListView;
    private List<Seance> seances;
    private ModelSeance modelSeance = new ModelSeance();
    private Projet projet;
    private ObjectId projetID;
    private ModelProjet modelProjet = new ModelProjet();

    public SeanceView(Projet projet, ObjectId projetID) {
        root = new BorderPane();
        this.projet = projet;
        this.projetID = projetID;
        seances = projet.getSeances();
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
        Label label = new Label("\u00A0\u00A0Seance");
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
        SeanceListView = new ListView<>();
        SeanceListView.setStyle("-fx-background-color: #B8B9BB;");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
        for (Seance seanc : seances) {
            String formattedDateDebut = dateFormat.format(seanc.getDateDebut());
            String formattedDateFin = dateFormat.format(seanc.getDateFin());
            String seanceInfo = seanc.getDescription() + "\n" + "\n" + formattedDateDebut + " - " + formattedDateFin + " \n " + seanc.getNote();
            SeanceListView.getItems().add(seanceInfo);
        }
        double totalHeight = SeanceListView.getItems().size() * 157;
        SeanceListView.setPrefHeight(totalHeight);
        String listViewStyle = "-fx-background-color: #F1F4FB;";
        SeanceListView.setStyle(listViewStyle);
        SeanceListView.setCellFactory(param -> new ListCell<String>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final Button passeButton = new Button("Documents");

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
                    buttonsBox.getChildren().addAll(editButton, deleteButton, passeButton);
                    BorderPane cellPane = new BorderPane();
                    cellPane.setLeft(new Label(item));
                    cellPane.setRight(buttonsBox);
                    setGraphic(cellPane);
                    SeanceListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                        int selectedIndex = newValue.intValue();
                        if (selectedIndex >= 0 && selectedIndex < seances.size()) {
                            Seance selectedDoc = seances.get(selectedIndex);
                            deleteButton.setOnAction(deleteEvent -> {
                                seances = modelSeance.readSeances();
                                getListView().getItems().remove(item);
                                double totalHeightUpdated = SeanceListView.getItems().size() * 150;
                                SeanceListView.setPrefHeight(totalHeightUpdated);
                                modelProjet.supprimerSeanceDeProjet(projetID, selectedIndex);
                                System.out.println("Supprimer: " + item);
                            });
                            editButton.setOnAction(editEvent -> {
                                String selectedItem = getItem();
                                if (selectedItem != null) {
                                    String description = selectedDoc.getDescription();
                                    String note = selectedDoc.getNote();
                                    Stage editStage = new Stage();
                                    editStage.setTitle("Modifier le Document");
                                    VBox editLayout = new VBox();
                                    editLayout.setSpacing(10);
                                    editLayout.setPadding(new Insets(20));
                                    TextArea editDescriptionField = new TextArea(description);
                                    DatePicker newDatePickerDebut = new DatePicker();
                                    DatePicker newDatePickerFin = new DatePicker();
                                    TextArea editNoteField = new TextArea(note);
                                    editDescriptionField.setPromptText("Description");
                                    Button editSubmitButton = new Button("Modifier");
                                    editSubmitButton.setOnAction(e -> {
                                        String newDescription = editDescriptionField.getText();
                                        LocalDate selectedDateDebut = newDatePickerDebut.getValue();
                                        Date newDateDebut = Date.from(selectedDateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        LocalDate selectedDateFin = newDatePickerFin.getValue();
                                        Date newDateFin = Date.from(selectedDateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
                                        String newNote = editNoteField.getText();
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
                                        String formatDateDebut = sdf.format(newDateDebut);
                                        String formatDateFin = sdf.format(newDateFin);
                                        String newSeanceInfo = newDescription + "\n" + "\n" + formatDateDebut + " - " + formatDateFin + " \n" + newNote;
                                        SeanceListView.getItems().set(getIndex(), newSeanceInfo);
                                        double totalHeightUpdated = SeanceListView.getItems().size() * 150;
                                        SeanceListView.setPrefHeight(totalHeightUpdated);
                                        modelProjet.modifierSeanceDeProjet(projetID, newDescription, newDateDebut, newDateFin, newNote, selectedIndex);
                                        editStage.close();
                                    });
                                    editLayout.getChildren().addAll(
                                            new Label("Description"), editDescriptionField,
                                            new Label("Date Debut"), newDatePickerDebut,
                                            new Label("Date Fin"), newDatePickerFin,
                                            new Label("Note"), editNoteField,
                                            editSubmitButton
                                    );

                                    editStage.setScene(new Scene(editLayout, 400, 200));
                                    editStage.show();
                                } else {
                                    System.out.println("Invalid document format: " + selectedItem);
                                }
                            });
                            passeButton.setOnAction(documentEvent -> {
                                DocumentSeanceView documentTaskView = new DocumentSeanceView(selectedDoc, selectedIndex, projetID);
                                Scene currentScene = SeanceListView.getScene();
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
        buttonBox.getChildren().addAll(addDocumentButton);
        contentBody.getChildren().addAll(SeanceListView);
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
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        DatePicker datePickerDebut = new DatePicker();
        DatePicker datePickerFin = new DatePicker();
        datePickerDebut.setPromptText("Date debut");
        datePickerFin.setPromptText("Date fin");
        TextArea noteField = new TextArea();
        noteField.setPromptText("Note");
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
            LocalDate selectedDateDebut = datePickerDebut.getValue();
            Date dateDebut = Date.from(selectedDateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            LocalDate selectedDateFin = datePickerFin.getValue();
            Date dateFin = Date.from(selectedDateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());
            String note = noteField.getText();
            Seance nouvelleSeance = new Seance();
            nouvelleSeance.setDescription(description);
            nouvelleSeance.setDateDebut(dateDebut);
            nouvelleSeance.setDateFin(dateFin);
            nouvelleSeance.setNote(note);
            nouvelleSeance.setDoc(null);
            modelProjet.ajouterSeanceAProjet(id, nouvelleSeance);
            seances.add(nouvelleSeance);
            String documentInfo = description + " \n " + " \n " + selectedDateDebut + " - " + selectedDateFin + " \n " + note;
            SeanceListView.getItems().add(documentInfo);
            double totalHeight = SeanceListView.getItems().size() * 150;
            SeanceListView.setPrefHeight(totalHeight);
            SeanceListView.refresh();
            formStage.close();
        });
        Button fetchEventsButton = new Button("Récupérer Événements");
        fetchEventsButton.setOnAction(event -> {
            try {
                List<Event> events = new CalendarQuickstart().getCalendarEvents();
                Events(events);
                formStage.close();
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        });
        formLayout.getChildren().addAll(
                new Label("Description"), descriptionField,
                new Label("Date Debut"), datePickerDebut,
                new Label("Date Fin"), datePickerFin,
                new Label("Note"), noteField,
                new Label("Importer Fichier"), fileButton,
                submitButton,
                fetchEventsButton
        );
        formStage.setScene(new Scene(formLayout, 400, 600));
        formStage.show();
    }
    private void Events(List<Event> events) {
        for (Event event : events) {
            Seance nouvelleSeance = new Seance();
            nouvelleSeance.setDescription(event.getSummary());
            Date dateDebut = new Date(event.getStart().getDateTime().getValue());
            Date dateFin = new Date(event.getEnd().getDateTime().getValue());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd   HH:mm");
            String formattedDateDebut = dateFormat.format(dateFin);
            String formattedDateFin = dateFormat.format(dateFin);
            nouvelleSeance.setDateDebut(dateDebut);
            nouvelleSeance.setDateFin(dateFin);
            nouvelleSeance.setNote("Ma note");
            nouvelleSeance.setDoc(null);
            modelProjet.ajouterSeanceAProjet(projet.getID(),nouvelleSeance);
            seances.add(nouvelleSeance);
            String documentInfo = event.getSummary() + " \n " + " \n " + formattedDateDebut  + " - " + formattedDateFin + " \n " + "Ma note";
            SeanceListView.getItems().add(documentInfo);
            double totalHeight = SeanceListView.getItems().size() * 150;
            SeanceListView.setPrefHeight(totalHeight);
            SeanceListView.refresh();
        }
    }

    public BorderPane getRoot() {
        return root;
    }
}