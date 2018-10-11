package root.client.model.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

import java.util.Arrays;

public class MapProtocolIn {

    private final String message;

    MapProtocolIn(String message) {
        this.message = message;
    }

    public boolean youHaveLost() {
        return message.equals("YOU HAVE LOST") ;
    }

    public boolean moveNexPlayer() {
        return message.matches("MOVING \\w+");
    }

    public Direction getDirectionToMoveOtherPlayer() {
        return Direction.valueOf(message.replace("MOVING ",""));
    }
}
