package root.client.model.protocol.map;

import root.server.protocol.Protocol;

public class MapProtocol  extends Protocol {
    static String messagePrefix="MAP";

    public MapProtocol() {
        super(messagePrefix);
    }

    public MapProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new MapProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + messagePrefix + " message");
    }

    public MapProtocolOut send() {
        return new MapProtocolOut();
    }

}
