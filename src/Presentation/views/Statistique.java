package Presentation.views;
import POJO.Projet;
import Presentation.models.ModelProjet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistique {
    private ModelProjet model = new ModelProjet();
    private BorderPane root;
    public Statistique() {
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
        InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/"+"logo_statistique.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        Label label = new Label("\u00A0\u00A0Statistique");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        HBox hbox1 = new HBox(imageView, label);
        anchorPane.getChildren().addAll(hbox1);
        AnchorPane.setTopAnchor(hbox1, 20.0);
        AnchorPane.setLeftAnchor(hbox1, 20.0);
        List<Projet> projects = model.readProjets();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Nombre d'heures de travail par projet");
        xAxis.setLabel("Projet");
        yAxis.setLabel("Nombre d'heures");
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        for (Projet project : projects) {
            Duration duration = calculateProjectDuration(project);
            long hours = duration.toHours();
            dataSeries.getData().add(new XYChart.Data<>(project.getDescription(), hours));
        }
        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();
        barChartData.add(dataSeries);
        barChart.setData(barChartData);
        CategoryAxis docXAxis = new CategoryAxis();
        NumberAxis docYAxis = new NumberAxis();
        BarChart<String, Number> docBarChart = new BarChart<>(docXAxis, docYAxis);
        docBarChart.setTitle("Nombre de documents par projet");
        docXAxis.setLabel("Projet");
        docYAxis.setLabel("Nombre de documents");
        XYChart.Series<String, Number> docDataSeries = new XYChart.Series<>();
        for (Projet project : projects) {
            int numDocuments = calculDocuments(project);
            docDataSeries.getData().add(new XYChart.Data<>(project.getDescription(), numDocuments));
        }
        ObservableList<XYChart.Series<String, Number>> docBarChartData = FXCollections.observableArrayList();
        docBarChartData.add(docDataSeries);
        docBarChart.setData(docBarChartData);
        CategoryAxis xAxisTime = new CategoryAxis();
        NumberAxis yAxisTime = new NumberAxis();
        BarChart<String, Number> barChartTime = new BarChart<>(xAxisTime, yAxisTime);
        barChartTime.setTitle("Heures de travail par période");
        xAxisTime.setLabel("Période");
        yAxisTime.setLabel("Heures de travail");
        XYChart.Series<String, Number> dataSeriesWeek = new XYChart.Series<>();
        dataSeriesWeek.setName("Semaine");
        XYChart.Series<String, Number> dataSeriesMonth = new XYChart.Series<>();
        dataSeriesMonth.setName("Mois");
        XYChart.Series<String, Number> dataSeriesYear = new XYChart.Series<>();
        dataSeriesYear.setName("Année");
        long workHoursThisWeek = calculateWorkHours(projects, ChronoUnit.WEEKS, 1);
        dataSeriesWeek.getData().add(new XYChart.Data<>("Semaine en cours", workHoursThisWeek));
        long workHoursThisMonth = calculateWorkHours(projects, ChronoUnit.MONTHS, 1);
        dataSeriesMonth.getData().add(new XYChart.Data<>("Mois en cours", workHoursThisMonth));
        long workHoursThisYear = calculateWorkHours(projects, ChronoUnit.YEARS, 1);
        dataSeriesYear.getData().add(new XYChart.Data<>("Année en cours", workHoursThisYear));
        ObservableList<XYChart.Series<String, Number>> barChartDataTime = FXCollections.observableArrayList();
        barChartDataTime.addAll(dataSeriesWeek, dataSeriesMonth, dataSeriesYear);
        barChartTime.setData(barChartDataTime);
        PieChart pieChartWeek = createPieChartByCategory(projects, ChronoUnit.WEEKS, "Pourcentage d'heures de travail par catégorie (Semaine)");
        PieChart pieChartMonth = createPieChartByCategory(projects, ChronoUnit.MONTHS, "Pourcentage d'heures de travail par catégorie (Mois)");
        PieChart pieChartYear = createPieChartByCategory(projects, ChronoUnit.YEARS, "Pourcentage d'heures de travail par catégorie (Année)");
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(barChart, 0, 0);
        gridPane.add(docBarChart, 1, 0);
        gridPane.add(barChartTime, 0, 1);
        gridPane.add(pieChartWeek, 1, 1);
        gridPane.add(pieChartMonth, 0, 2);
        gridPane.add(pieChartYear, 1, 2);
        contentBody.getChildren().addAll(anchorPane, gridPane);
        content.setCenter(contentBody);
        root.setCenter(content);
    }
    private Duration calculateProjectDuration(Projet project) {
        Date startDate = project.getDateDebut();
        Date endDate = project.getDateFin();
        Instant startInstant = startDate.toInstant();
        Instant endInstant = endDate.toInstant();
        return Duration.between(startInstant, endInstant);
    }
    private int calculDocuments(Projet projet) {
        return projet.getDoc().size();
    }
    private long calculateWorkHours(List<Projet> projects, TemporalUnit unit, int amount) {
        long totalHours = 0;
        for (Projet project : projects) {
            Duration duration = calculateProjectDuration(project);
            if (unit == ChronoUnit.WEEKS && duration.toDays() < 7) {
                continue;
            }
            if (duration.toHours() > 0 && duration.toHours() <= amount * unit.getDuration().toHours()) {
                totalHours += duration.toHours();
            }
        }
        return totalHours;
    }

    private PieChart createPieChartByCategory(List<Projet> projects, TemporalUnit unit, String title) {
        PieChart pieChart = new PieChart();
        pieChart.setTitle(title);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        long totalHours = calculateWorkHours(projects, unit, 1);
        Map<String, Long> categoryHoursMap = new HashMap<>();
        for (Projet project : projects) {
            String category = project.getCategorie();
            Duration duration = calculateProjectDuration(project);
            long hours = duration.toHours();
            categoryHoursMap.put(category, categoryHoursMap.getOrDefault(category, 0L) + hours);
        }
        for (Map.Entry<String, Long> entry : categoryHoursMap.entrySet()) {
            String category = entry.getKey();
            long hours = entry.getValue();
            double percentage = (double) hours / totalHours * 100;
            pieChartData.add(new PieChart.Data(category, percentage));
        }
        pieChart.setData(pieChartData);
        return pieChart;
    }
    public BorderPane getRoot() {
        return root;
    }
}