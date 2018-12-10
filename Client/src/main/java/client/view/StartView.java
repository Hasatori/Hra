package client.view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import client.controller.StartController;
import client.util.ResourceLoader;

/**
 * View class for a start window.
 */
@SuppressWarnings("restriction")
public class StartView extends View {
    private StartController controller;

    /**
     * @param startController start controller
     * @throws IOException error in constructing a StartController
     */
    public StartView(StartController startController) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/start.fxml")));
        Button singlePlayer = (Button) this.lookup("#singleplayerButton");
        Button multiplayerButton = (Button) this.lookup("#multiplayerButton");
        this.controller = startController;
        multiplayerButton.setOnAction(a -> startController.loadMultiplayer());
        singlePlayer.setOnAction(a -> startController.loadSingleplayer());
    }
}
