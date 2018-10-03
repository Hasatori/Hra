package root.client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.util.ResourceLoader;

import java.io.IOException;

public class Floor extends MapPart implements Overlaid {
    private Movable movable;

    public Floor(Position position) {
        super(position);
    }

    @Override
    public Node getSource() {
        try {
            return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/floor.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isCovered() {
        return false;
    }

    @Override
    public void setMovable(Movable movable) {
        this.movable=movable;
    }

}
