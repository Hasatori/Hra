package server.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

/**
 * Handles output from MapProtocol messages.
 */
public class MapProtocolOut {

    MapProtocolOut() {

    }

    public String playerHasLost() {
        return MapProtocol.MSG_PREFIX + ":YOU HAVE LOST";
    }

    public String playerHasWon() {
        return MapProtocol.MSG_PREFIX + ":YOU HAVE WON";
    }

    public String movePlayer(Direction direction) {
        return MapProtocol.MSG_PREFIX + ":MOVING " + direction.toString();
    }

    public String restartMap() {
        return MapProtocol.MSG_PREFIX + ":RESTART MAP";
    }

    public String agreed() {
        return MapProtocol.MSG_PREFIX + ":OK";
    }

    public String disagreed() {
        return MapProtocol.MSG_PREFIX + ":NO";
    }

    public String playerHasLeft() {
        return MapProtocol.MSG_PREFIX + ":PLAYER HAS LEFT";
    }

    public String youHaveLeft() {
        return MapProtocol.MSG_PREFIX + ":YOU HAVE LEFT THE GAME";
    }
}
