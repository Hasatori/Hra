package server.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

public class MapProtocolIn {
    private final String message;

    MapProtocolIn(String message) {
        this.message = message;
    }

    public boolean won() {
        return message.equals("WON");
    }

    public boolean moveNexPlayer() {
        return message.matches("MOVING \\w+");
    }

    public Direction getDirectionToMove() {
        return Direction.valueOf(message.replace("MOVING ", ""));
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
    public boolean quitMap(){
        return message.equals("QUIT MAP");
    }
}
