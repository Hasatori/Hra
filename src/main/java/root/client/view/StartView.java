package root.client.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import root.client.controller.StartController;
import root.client.util.ResourceLoader;


import java.io.IOException;
import java.util.Optional;

public class StartView extends View {
    private StartController controller;

    public StartView(StartController startController) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/start.fxml")));
        Button singlePlayer = (Button) this.lookup("#singleplayerButton");
        Button multiplayerButton = (Button) this.lookup("#multiplayerButton");
        this.controller = startController;
        multiplayerButton.setOnAction((a)->{
            startController.loadMultiplayer();
        });
        singlePlayer.setOnAction((a) -> {
            startController.loadSingleplayer();
        });
    }

}
