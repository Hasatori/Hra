package client.model.protocol.map;

import com.sun.javafx.scene.traversal.Direction;

public class MapProtocolOut {

    MapProtocolOut() {

    }

    public String moving(Direction direction) {
        return MapProtocol.MSG_PREFIX + ":MOVING " + direction;
    }
    public String won() {
        return MapProtocol.MSG_PREFIX + ":WON";
    }
    public String quitMap(){
        return MapProtocol.MSG_PREFIX + ":QUIT MAP";
    }
    public String restartMap(){
        return MapProtocol.MSG_PREFIX + ":RESTART MAP";
    }

    public String agreed(){
        return MapProtocol.MSG_PREFIX + ":OK";
    }
    public String disagreed(){
        return MapProtocol.MSG_PREFIX + ":NO";
    }
}
