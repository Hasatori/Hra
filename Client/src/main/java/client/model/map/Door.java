package client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import client.util.ResourceLoader;

import java.io.IOException;

public class Door extends MapPart {

    private final String toMapName, fromMapName;

    public Door(Position position, String fromMapName, String toMapName) {
        super(position);
        this.fromMapName = fromMapName;
        this.toMapName = toMapName;
    }

    public String getToMapName() {
        return toMapName;
    }

    public String getFromMapName() {
        return fromMapName;
    }

    @Override
    public Node getSource() {
        try {
            return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/doorVertical.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
