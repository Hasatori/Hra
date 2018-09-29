package root.client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.model.FileLoader;

import java.io.IOException;

public class Floor extends MapPart {
    public Floor(Position position) {
        super(position);
    }

    @Override
    public Node getSource() {
        try {
            return FXMLLoader.load(FileLoader.loadFileURL("fxml/parts/floor.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
