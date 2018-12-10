package client.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import client.controller.StartController;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 100;
    private static final int WINDOW_HEIGHT = 100;

    public static void main(String[] args) {
        Application.launch();
        Platform.exit();
    }

    /**
     * Sets primary stage stats and starts the starting window
     * @param primaryStage primary stage
     * @throws Exception error
     */
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setX(WINDOW_WIDTH);
        primaryStage.setY(WINDOW_HEIGHT);
        new StartController(primaryStage).loadView();
    }
}
