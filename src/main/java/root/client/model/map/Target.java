package root.client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.util.ResourceLoader;

import java.io.IOException;

class Target extends MapPart {

    private boolean covered;

    public Target(Position position) {
        super(position);
        this.covered = false;
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

    public boolean isCovered() {
        return this.covered;
    }

    public void setCovered() {
        this.covered = true;
    }
}
