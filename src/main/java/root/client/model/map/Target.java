package root.client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.util.ResourceLoader;

import java.io.IOException;

class Target extends MapPart {

    private boolean covered;
    private final Position initialPosition;
    private Node coveredResource = FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/floor.fxml"));
    private Node uncoveredResource = FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/target.fxml"));
    private Node resource = uncoveredResource;

    public Target(Position position) throws IOException {
        super(position);
        this.initialPosition = position;
        this.covered = false;
    }

    @Override
    public Node getSource() {
        return resource;
    }

    public boolean isCovered() {
        return this.covered;
    }

    public void setCovered(MapPart coveringObject) {
        if (coveringObject instanceof Box) {
            this.covered = true;
        }
        this.resource = coveredResource;
    }

    public void setUncovered() {
        this.covered = false;
        this.resource = uncoveredResource;
    }

    public Position getInitialPosition() {
        return this.initialPosition;
    }
}
