package client.model.protocol.map;

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
    public String quitMap(){
        return MapProtocol.messagePrefix+":QUIT MAP";
    }
    public String restartMap(){
        return MapProtocol.messagePrefix+":RESTART MAP";
    }

    public String agreed(){
        return MapProtocol.messagePrefix+":OK";
    }
    public String disagreed(){
        return MapProtocol.messagePrefix+":NO";
    }

}
