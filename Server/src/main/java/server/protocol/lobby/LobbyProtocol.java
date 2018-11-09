package server.protocol.lobby;

import server.protocol.Protocol;

public class LobbyProtocol extends Protocol {
    public static final String MSG_PREFIX = "LOBBY";

    public LobbyProtocol() {
        super(MSG_PREFIX);
    }

    public LobbyProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new LobbyProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + MSG_PREFIX + " message");
    }
    public LobbyProtocolOut send() {
        return new LobbyProtocolOut();
    }
}
