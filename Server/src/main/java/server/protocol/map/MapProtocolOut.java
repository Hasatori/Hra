package server.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

public class MapProtocolOut {

    MapProtocolOut() {

    }

    public String playerHasLost() {
        return MapProtocol.MSG_PREFIX + ":YOU HAVE LOST";
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
