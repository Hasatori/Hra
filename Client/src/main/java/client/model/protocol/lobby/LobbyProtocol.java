package client.model.protocol.lobby;

import client.model.protocol.Protocol;

public class LobbyProtocol  extends Protocol {
    public static String messagePrefix = "LOBBY";

    public LobbyProtocol() {
        super(messagePrefix);
    }

    public LobbyProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new LobbyProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + messagePrefix + " message");
    }

    public LobbyProtocolOut send() {
        return new LobbyProtocolOut();
    }
}
