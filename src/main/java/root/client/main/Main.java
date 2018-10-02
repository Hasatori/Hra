package root.client.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import root.client.controller.MapController;
import root.client.controller.StartController;
import root.client.view.StartView;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
        Platform.exit();

    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setX(100);
        primaryStage.setY(100);
        new StartController(primaryStage);
    }
}
