package root.server.main.protocol.lobby;

import root.server.main.Lobby;
import root.server.main.protocol.Protocol;
import root.server.main.protocol.general.GeneralProtocolIn;
import root.server.main.protocol.general.GeneralProtocolOut;

public class LobbyProtocol extends Protocol {
    static String messagePrefix="LOBBY";

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
