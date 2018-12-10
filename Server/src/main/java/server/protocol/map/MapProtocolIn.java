package server.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

/**
 * Handles input from MapProtocol messages.
 */
public class MapProtocolIn {

    private static final String VICTORY = "WON";
    private static final String MOVING = "MOVING ";
    private static final String RESTART_MAP = "RESTART MAP";
    private static final String OK = "OK";
    private static final String NO = "NO";
    private static final String QUIT_MAP = "QUIT MAP";
    private final String message;

    MapProtocolIn(String message) {
        this.message = message;
    }

    public boolean won() {
        return message.equals(VICTORY);
    }

    public boolean moveNexPlayer() {
        return message.matches(MOVING + "\\w+");
    }

    public Direction getDirectionToMove() {
        return Direction.valueOf(message.replace(MOVING, ""));
    }

    public boolean restartMapRequest() {
        return message.matches(RESTART_MAP);
    }

    public boolean agreed() {
        return message.matches(OK);
    }

    public boolean disagreed() {
        return message.matches(NO);
    }

    public boolean quitMap(){
        return message.equals(QUIT_MAP);
    }
}
