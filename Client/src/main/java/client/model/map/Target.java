package client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import client.util.ResourceLoader;

import java.io.IOException;

/**
 * Map part class - Target
 */
class Target extends MapPart {

    private boolean covered;
    private final Position initialPosition;
    private Node coveredResource = FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/floor.fxml"));
    private Node uncoveredResource = FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/target.fxml"));
    private Node resource = uncoveredResource;

    /**
     * @param position position of target
     * @throws IOException error
     */
    public Target(Position position) throws IOException {
        super(position);
        this.initialPosition = position;
        this.covered = false;
    }

    @Override
    public Node getSource() {
        return resource;
    }

    /**
     * Returns true if target is covered.
     * @return true=target is covered
     */
    public boolean isCovered() {
        return this.covered;
    }

    /**
     * Sets target to covered.
     * @param coveringObject covering object
     */
    public void setCovered(MapPart coveringObject) {
        if (coveringObject instanceof Box) {
            this.covered = true;
        }
        this.resource = coveredResource;
    }

    /**
     * Sets target to uncovered.
     */
    public void setUncovered() {
        this.covered = false;
        this.resource = uncoveredResource;
    }

    /**
     * Getter for initial position.
     * @return initial position
     */
    public Position getInitialPosition() {
        return this.initialPosition;
    }
}
