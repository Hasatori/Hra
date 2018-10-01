package root.client.model.map;

import com.sun.javafx.scene.traversal.Direction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.util.ResourceLoader;

import java.io.IOException;

class Player extends MapPart {

    private Direction directionn;



    Player(Position position) {
        super(position);
        this.directionn = Direction.DOWN;
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
                return  FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/playerRight.fxml"));
            default:
                throw new IllegalStateException("Wrong direction set");
        }

    }

    public void setDirectionn(Direction directionn) {
        this.directionn = directionn;
    }
}
