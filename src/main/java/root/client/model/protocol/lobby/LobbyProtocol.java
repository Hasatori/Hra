package root.client.model.protocol.lobby;

import root.client.model.protocol.general.GeneralProtocolIn;
import root.client.model.protocol.general.GeneralProtocolOut;
import root.server.main.protocol.Protocol;

public class LobbyProtocol  extends Protocol {
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
