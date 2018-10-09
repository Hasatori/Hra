package root.client.model.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

public class MapProtocolOut {

    MapProtocolOut() {

    }

    public String moving(Direction direction) {
        return MapProtocol.messagePrefix + ":MOVING " + direction;
    }
    public String won() {
        return MapProtocol.messagePrefix + ":WON";
    }
}
