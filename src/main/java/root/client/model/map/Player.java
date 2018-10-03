package root.client.model.map;

import com.sun.javafx.scene.traversal.Direction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.util.ResourceLoader;

import java.io.IOException;

class Player extends MapPart implements Movable {

    private Overlaid overlaid;
    private Direction directionn;

    private Player(Position position, Overlaid overlaid) {
        super(position);
        this.directionn = Direction.DOWN;
        this.overlaid = overlaid;
    }

    @Override
    public Node getSource() throws IOException {
        switch (this.directionn) {
            case UP:
                return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/playerUp.fxml"));
            case DOWN:
                return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/playerDown.fxml"));
            case LEFT:
                return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/playerLeft.fxml"));
            case RIGHT:
                return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/playerRight.fxml"));
            default:
                throw new IllegalStateException("Wrong direction set");
        }

    }

    public void setDirection(Direction directionn) {
        this.directionn = directionn;
    }

    @Override
    public boolean tryMoveLeft() {
        if (this.getLeft() instanceof Box) {
            if(((Movable) this.getLeft()).tryMoveLeft()){

            };
        }
        if (this.getLeft() instanceof Overlaid) {
            if(!((Overlaid)this.getLeft()).isCovered()){
                
            }
        }
        return false;
    }

    @Override
    public boolean tryMoveRight() {
        return false;
    }

    @Override
    public boolean tryMoveup() {
        return false;
    }

    @Override
    public boolean tryMoveDown() {
        return false;
    }

}
