package client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import client.util.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Floor extends MapPart {

    private final Logger LOGGER = LoggerFactory.getLogger(Floor.class);

    public Floor(Position position) {
        super(position);
    }

    @Override
    public Node getSource() {
        try {
            return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/floor.fxml"));
        } catch (IOException e) {
            LOGGER.error("Could not load file", e);
        }
        return null;
    }
}
