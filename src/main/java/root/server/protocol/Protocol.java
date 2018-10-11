package root.server.protocol;

public class Protocol {

    private final String prefix;

    public Protocol(String prefix) {
        this.prefix = prefix;
    }

    protected boolean isRightMessageType(String message) {
        return message.split(":")[0].equals(this.prefix) ? true : false;
    }

    protected String stripPrefix(String message) {
        return message.split(":")[1];
    }

    public static ProtocolType getProtocolType(String message) {
        String prefix = message.split(":")[0];
        switch (prefix) {
            case "GENERAL":
                return ProtocolType.GENERAL;
            case "MAP":
                return ProtocolType.MAP;
            case "LOBBY":
                return ProtocolType.LOBBY;
            default:
                throw new IllegalArgumentException("Unknown message type");
        }
    }
}
