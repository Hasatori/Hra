package client.model.protocol;

public class Protocol {

    private static final String PREFIX_DELIM = ":";
    private final String prefix;

    public Protocol(String prefix) {
        this.prefix = prefix;
    }

    protected boolean isRightMessageType(String message) {
        return message.split(PREFIX_DELIM)[0].equals(this.prefix);
    }

    protected String stripPrefix(String message) {
        return message.split(PREFIX_DELIM)[1];
    }
}
