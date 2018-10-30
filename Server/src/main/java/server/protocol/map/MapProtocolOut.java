package server.protocol.map;

import com.sun.javafx.scene.traversal.Direction;


public class MapProtocolOut {


    MapProtocolOut() {

    }

    public String playerHasLost() {
        return MapProtocol.messagePrefix + ":YOU HAVE LOST";
    }

    public String movePlayer(Direction direction) {
        return MapProtocol.messagePrefix + ":MOVING " + direction.toString();
    }

    public String restartMap() {
        return MapProtocol.messagePrefix + ":RESTART MAP";
    }

    public String agreed() {
        return MapProtocol.messagePrefix + ":OK";
    }

    public String disagreed() {
        return MapProtocol.messagePrefix + ":NO";
    }

    public String playerHasLeft() {
        return MapProtocol.messagePrefix + ":PLAYER HAS LEFT";
    }

    public String youHaveLeft() {
        return MapProtocol.messagePrefix + ":YOU HAVE LEFT THE GAME";
    }
}
