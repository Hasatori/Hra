package client.model.protocol.map;


import client.model.protocol.Protocol;

/**
 * Class for Map Protocol.
 * This class takes care of Map messages like player moving on a map and such..
 */
public class MapProtocol  extends Protocol {
    public static final String MSG_PREFIX = "MAP";

    public MapProtocol() {
        super(MSG_PREFIX);
    }

    /**
     * Receive the message and creating MapProtocolIn, which reads the message.
     * @param message message to read
     * @return MapProtocolIn
     */
    public MapProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new MapProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + MSG_PREFIX + " message");
    }

    /**
     * Sends a general protocol message.
     * @return MapProtocolOut
     */
    public MapProtocolOut send() {
        return new MapProtocolOut();
    }
}
