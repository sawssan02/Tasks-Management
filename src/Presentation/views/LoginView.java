package Presentation.views;

import Presentation.models.LoginModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.InputStream;

public class LoginView {
    private BorderPane root;
    private Label resultLabel;
    private Button loginButton;
    private LoginModel model=new LoginModel();
    private String emailText;

    public LoginView(){
        root = new BorderPane();
        content();
    }
    public void content() {
        BorderPane content = new BorderPane();
        content.setStyle("-fx-background-color: #ffff;");
        VBox contentBody = new VBox();
        contentBody.setAlignment(Pos.CENTER);
        contentBody.setSpacing(10);
        contentBody.setStyle("-fx-background-color: #ffff; ");
        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        InputStream inputStream = getClass().getResourceAsStream("/Presentation/images/"+"login.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(root.widthProperty().multiply(0.5));
        imageView.fitHeightProperty().bind(root.heightProperty().multiply(1.4));
        imageBox.getChildren().add(imageView);
        Label label = new Label("Welcome to login");
        label.setStyle("-fx-font-size:30px;");
        VBox.setMargin(label, new Insets(0, 0, 0, 25));
        TextField email = new TextField();
        email.setPromptText("Email");
        email.setPadding(new Insets(8));
        email.setFont(Font.font(14));
        email.prefWidthProperty().bind(root.widthProperty().multiply(0.3));
        loginButton = new Button("Login");
        loginButton.prefWidthProperty().bind(root.widthProperty().multiply(0.35));
        loginButton.setStyle("-fx-background-color:#4453B9; -fx-padding:6px; -fx-font-size:20px; -fx-text-fill:white;");
        resultLabel = new Label();
        loginButton.setOnAction(event -> {
            emailText = email.getText();
            System.out.println("Button clicked. Email entered: " + emailText);
            resultLabel.setText("Checking email...");
            model.handleLogin(emailText);
        });
        VBox loginElements = new VBox();
        loginElements.setAlignment(Pos.CENTER_LEFT);
        loginElements.setSpacing(60);
        loginElements.setPadding(new Insets(0, 0, 0, 70));
        loginElements.getChildren().addAll(resultLabel, label, email, loginButton);
        HBox loginBox = new HBox();
        loginBox.setAlignment(Pos.CENTER_LEFT);
        loginBox.setStyle("-fx-background-color: #ffff;");
        loginBox.setPadding(new Insets(0, 0, 350, 0));
        loginBox.getChildren().addAll(loginElements);
        content.setLeft(loginBox);
        content.setRight(imageBox);
        root.setCenter(content);
    }
    public BorderPane getRoot() {
        return root;
    }
    public void updateStatus(String message) {
        resultLabel.setText(message);
    }
    public Button getLoginButton(){
        return loginButton;
    }
}
