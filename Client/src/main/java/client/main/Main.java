package client.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import client.controller.StartController;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
        Platform.exit();

    }
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setX(100);
        primaryStage.setY(100);
        new StartController(primaryStage).loadView();
    }
}

