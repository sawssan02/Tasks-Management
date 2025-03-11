package Presentation.controllers;

import Presentation.views.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller extends Application {
    private View view;
    private LoginView loginView;
    @Override
    public void start(Stage primaryStage) throws IOException {
        view=new View();
        loginView = new LoginView();
        primaryStage.setScene(new Scene(loginView.getRoot(), 800, 600));
        primaryStage.setTitle("Page des Notes");
        primaryStage.show();
        loginView.getLoginButton().setOnAction(event -> {
            primaryStage.getScene().setRoot(view.getRoot());
        });
    }
}
