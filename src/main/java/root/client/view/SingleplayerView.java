package root.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import root.client.controller.Controller;
import root.client.util.ResourceLoader;

import java.io.IOException;

public class SingleplayerView extends View {
    public SingleplayerView(Parent parent, Controller controller) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/singleplayer.fxml")), controller);

    }

}
