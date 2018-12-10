package client.model.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

/**
 * Handles input from MapProtocol messages.
 */
public class MapProtocolIn {

    private static final String MAP_YOU_LOST = "YOU HAVE LOST";
    private static final String MAP_MOVING = "MOVING ";
    private static final String MAP_PLAYER_LEFT = "PLAYER HAS LEFT";
    private static final String MAP_YOU_LEFT = "YOU HAVE LEFT THE GAME";
    private static final String MAP_RESTART = "RESTART MAP";
    private static final String MAP_OK = "OK";
    private static final String MAP_NO = "NO";
    private final String message;

    MapProtocolIn(String message) {
        this.message = message;
    }

    public boolean youHaveLost() {
        return message.equals(MAP_YOU_LOST);
    }

    public boolean moveNexPlayer() {
        return message.matches(MAP_MOVING + "\\w+");
    }

    public Direction getDirectionToMoveOtherPlayer() {
        return Direction.valueOf(message.replace(MAP_MOVING, ""));
    }

    public boolean playerHasLeft() {
        return message.matches(MAP_PLAYER_LEFT);
    }

    public boolean youHaveLeft(){
        return message.equals(MAP_YOU_LEFT);
    }

    public boolean restartMapRequest() {
        return message.matches(MAP_RESTART);
    }

    public boolean agreed() {
        return message.matches(MAP_OK);
    }

    public boolean disagreed() {
        return message.matches(MAP_NO);
    }
}
