package root.client.model.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

import java.util.Arrays;

public class MapProtocolIn {

    private final String message;

    MapProtocolIn(String message) {
        this.message = message;
    }

    public boolean youHaveLost() {
        return message.equals("YOU HAVE LOST") ? true : false;
    }

    public boolean moveNexPlayer() {
        return Arrays.asList(Direction.values()).contains(Direction.valueOf(message));
    }

    public Direction getDirectionToMoveOtherPlayer() {
        return Direction.valueOf(message);
    }
}
