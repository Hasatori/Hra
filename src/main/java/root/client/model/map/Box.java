package root.client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.model.FileLoader;

import java.io.IOException;

 public class Box extends MapPart {
    public Box(Position position) {
        super(position);
    }

    @Override
    public Node getSource() {
        try {
            return FXMLLoader.load(FileLoader.loadFileURL("fxml/parts/box.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
