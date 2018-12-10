package client.model.protocol.lobby;

import client.model.protocol.Protocol;

/**
 * Class for Lobby Protocol.
 * This class takes care of Lobby messages like chat and such..
 */
public class LobbyProtocol  extends Protocol {
    public static final String MSG_PREFIX = "LOBBY";

    public LobbyProtocol() {
        super(MSG_PREFIX);
    }

    /**
     * Receive the message and creating LobbyProtocolIn, which reads the message.
     * @param message message to read
     * @return LobbyProtocolIn
     */
    public LobbyProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new LobbyProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + MSG_PREFIX + " message");
    }

    /**
     * Sends a general protocol message.
     * @return LobbyProtocolOut
     */
    public LobbyProtocolOut send() {
        return new LobbyProtocolOut();
    }
}
