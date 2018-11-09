package client.model.protocol.map;


import client.model.protocol.Protocol;

public class MapProtocol  extends Protocol {
    public static final String MSG_PREFIX = "MAP";

    public MapProtocol() {
        super(MSG_PREFIX);
    }

    public MapProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new MapProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + MSG_PREFIX + " message");
    }

    public MapProtocolOut send() {
        return new MapProtocolOut();
    }
}
