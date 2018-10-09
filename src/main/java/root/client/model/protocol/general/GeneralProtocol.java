package root.client.model.protocol.general;

import root.client.model.protocol.Protocol;

public class GeneralProtocol extends Protocol {
    static String messagePrefix = "GENERAL";

    public GeneralProtocol() {
        super(messagePrefix);
    }

    public GeneralProtocolIn get(String message) {
        if (isRightMessageType(message)) {
            return new GeneralProtocolIn(stripPrefix(message));
        }
        throw new IllegalArgumentException("Wrong message type for " + messagePrefix + " message");
    }

    public GeneralProtocolOut send() {
            return new GeneralProtocolOut();
    }
}
