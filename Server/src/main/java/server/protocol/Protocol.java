package server.protocol;

/**
 * Base class for text protocol.
 */
public class Protocol {

    private static final String PREFIX_DELIM = ":";
    private final String prefix;
    public static final String DISCONNECTED = "DISCONNECTED";

    /**
     * @param prefix prefix of a protocol
     */
    public Protocol(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Determines if message is of the right type.
     *
     * @param message message
     * @return true=is right message type
     */
    protected boolean isRightMessageType(String message) {
        return message.split(PREFIX_DELIM)[0].equals(this.prefix);
    }

    /**
     * Strips message of prefix and returns the prefix.
     *
     * @param message message
     * @return prefix
     */
    protected String stripPrefix(String message) {
        return message.split(PREFIX_DELIM)[1];
    }

    /**
     * Returns a protocol type according to type of the message.
     *
     * @param message message
     * @return protocol type
     */
    public static ProtocolType getProtocolType(String message) {
        String prefix = message.split(PREFIX_DELIM)[0];
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
