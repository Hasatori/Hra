package root.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import root.client.controller.Controller;
import root.client.controller.LobbyController;
import root.client.util.ResourceLoader;

import java.io.IOException;

public class LobbyView extends View {
    public LobbyView( LobbyController controller) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/lobby.fxml")), controller);
    }

}
