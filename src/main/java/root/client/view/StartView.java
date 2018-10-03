package root.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import root.client.controller.MapController;
import root.client.controller.StartController;
import root.client.util.ResourceLoader;


import java.io.IOException;

public class StartView extends Scene {
    public StartView(StartController startController) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/start.fxml")));
        Button singlePlayer = (Button) this.lookup("#singleplayerButton");
        Button multiplayerButton = (Button) this.lookup("#multiplayerButton");

        multiplayerButton.setOnAction((a) -> {
            startController.loadMultiplayer();
        });
        singlePlayer.setOnAction((a)->{
            startController.loadMap();
        });
    }
}
