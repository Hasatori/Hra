package root.client.model.map;

import javafx.scene.Node;

import java.io.IOException;

public abstract class MapPart {

    public Position getPosition() {
        return position;
    }

     void setPosition(Position position) {
        this.position = position;
    }

    private Position position;

    public MapPart(Position position) {
        this.position = position;
    }

     MapPart getLeft() {
        return left;
    }

     MapPart getRight() {
        return right;
    }

     MapPart getTop() {
        return top;
    }

     MapPart getBottom() {
        return bottom;
    }

    private MapPart left, right, top, bottom = null;

   public  abstract Node getSource() throws IOException;

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


}
