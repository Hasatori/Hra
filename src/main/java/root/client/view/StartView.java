package root.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import root.client.controller.Controller;
import root.client.model.FileLoader;


import java.io.IOException;

public class StartView extends Scene {
private Controller mapController;
    public StartView(Controller mapController) throws IOException {
        super(FXMLLoader.load(FileLoader.loadFileURL("fxml/start/start.fxml")));
        this.mapController = mapController;
        Button singlePlayer=(Button)this.lookup("#singleplayerButton");
        Button multiplayerButton=(Button)this.lookup("#multiplayerButton");

        singlePlayer.setOnAction(a->{
            this.mapController.loadMap("level1");
        });
       multiplayerButton.setOnAction(a->{
           try {
               this.mapController.loadScene(new MultiplayerLobbyView(mapController));
           } catch (IOException e) {
               e.printStackTrace();
           }
       });
    }
}
