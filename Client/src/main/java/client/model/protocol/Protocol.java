package client.model.protocol;

/**
 * Base class for text protocol.
 */
public class Protocol {

    private static final String PREFIX_DELIM = ":";
    private final String prefix;

    /**
     * @param prefix prefix of a protocol
     */
    public Protocol(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Determines if message is of the right type.
     * @param message
     * @return true=is right message type
     */
    protected boolean isRightMessageType(String message) {
        return message.split(PREFIX_DELIM)[0].equals(this.prefix);
    }

    /**
     * Strips message of prefix and returns the prefix.
     * @param message message
     * @return prefix
     */
    protected String stripPrefix(String message) {
        return message.split(PREFIX_DELIM)[1];
    }
}
