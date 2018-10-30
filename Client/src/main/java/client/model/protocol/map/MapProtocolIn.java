package client.model.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

public class MapProtocolIn {

    private final String message;

    MapProtocolIn(String message) {
        this.message = message;
    }

    public boolean youHaveLost() {
        return message.equals("YOU HAVE LOST");
    }

    public boolean moveNexPlayer() {
        return message.matches("MOVING \\w+");
    }

    public Direction getDirectionToMoveOtherPlayer() {
        return Direction.valueOf(message.replace("MOVING ", ""));
    }

    public boolean playerHasLeft() {
        return message.matches("PLAYER HAS LEFT");
    }
public boolean youHaveLeft(){
        return message.equals("YOU HAVE LEFT THE GAME");
}
    public boolean restartMapRequest() {
        return message.matches("RESTART MAP");
    }

    public boolean agreed() {
        return message.matches("OK");
    }

    public boolean disagreed() {
        return message.matches("NO");
    }
}
