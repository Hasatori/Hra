package server.protocol.general;

import server.protocol.Protocol;

/**
 * Class for General Protocol.
 * This class takes care of General messages like registering a player and such..
 */
public class GeneralProtocol extends Protocol {

    public static final String MSG_PREFIX = "GENERAL";

    public GeneralProtocol() {
        super(MSG_PREFIX);
    }

    /**
     * Receive the message and create GeneralProtocolIn, which reads the message.
     * @param message message to read
     * @return GeneralProtocolIn
     */
    public GeneralProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new GeneralProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + MSG_PREFIX + " message");
    }

    /**
     * Sends a general protocol message.
     * @return GeneralProtocolOut
     */
    public GeneralProtocolOut send() {
        return new GeneralProtocolOut();
    }
}
