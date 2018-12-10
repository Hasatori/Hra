package client.model.map;

import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.Node;

import java.io.IOException;

/**
 * Core class for map construction.
 */
public abstract class MapPart {

    private MapPart left, right, top, bottom = null;
    private Position position;

    /**
     * @param position of map part
     */
    public MapPart(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public MapPart getLeft() {
        return left;
    }

    public MapPart getRight() {
        return right;
    }

    public MapPart getTop() {
        return top;
    }

    public MapPart getBottom() {
        return bottom;
    }

    /**
     * @return Node source
     * @throws IOException error
     */
    public abstract Node getSource() throws IOException;

    void setLeft(MapPart mapPart) {
        this.left = mapPart;
    }

    void setRight(MapPart mapPart) {
        this.right = mapPart;
    }

    void setTop(MapPart mapPart) {
        this.top = mapPart;
    }

    void setBottom(MapPart mapPart) {
        this.bottom = mapPart;
    }

    /**
     * Returns neighbour map part based on the position
     * @param direction direction in which we ask for the neighbour (UP, DOWN, LEFT, RIGHT)
     * @return map part
     */
    MapPart getNeighbour(Direction direction) {
        switch (direction) {
            case UP:
                return this.top;
            case DOWN:
                return this.bottom;
            case LEFT:
                return this.left;
            case RIGHT:
                return this.right;
        }
        return null;
    }
}
