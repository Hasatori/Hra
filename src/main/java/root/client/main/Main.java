package root.client.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import root.client.controller.Controller;
import root.client.controller.MapTypeAdapter;
import root.client.model.map.Box;
import root.client.model.map.Floor;
import root.client.model.map.MapPart;
import root.client.model.map.Position;
import root.client.view.StartView;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch();
        Platform.exit();

    }
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        primaryStage.setX(100);
        primaryStage.setY(100);
        Controller mapController = new Controller(primaryStage);
        mapController.loadScene(new StartView(mapController));

    }
}
