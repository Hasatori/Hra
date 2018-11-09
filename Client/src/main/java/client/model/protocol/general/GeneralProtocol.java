package client.model.protocol.general;

import client.model.protocol.Protocol;

public class GeneralProtocol extends Protocol {
    public static final String MSG_PREFIX = "GENERAL";

    public GeneralProtocol() {
        super(MSG_PREFIX);
    }

    public GeneralProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new GeneralProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + MSG_PREFIX + " message");
    }

    public GeneralProtocolOut send() {
            return new GeneralProtocolOut();
    }
}
