package root.server.protocol.map;

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
}
