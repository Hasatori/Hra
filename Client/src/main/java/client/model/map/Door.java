package client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import client.util.ResourceLoader;

import java.io.IOException;

public class Door extends MapPart {

    public String getToMapName() {
        return toMapName;
    }

    public String getFromMapName() {
        return fromMapName;
    }

    private final String toMapName, fromMapName;

    public Door(Position position, String fromMapName, String toMapName) {
        super(position);
        this.fromMapName = fromMapName;
        this.toMapName = toMapName;
    }

    @Override
    public Node getSource() throws IOException {
        try {
            return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/doorVertical.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
