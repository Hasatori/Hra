package root.client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.util.ResourceLoader;

import java.io.IOException;

 class Target extends MapPart implements Overlaid {
    public Target(Position position) {
        super(position);
    }

    @Override
    public Node getSource() {
        try {
            return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/target.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
