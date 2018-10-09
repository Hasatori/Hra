package root.client.model.protocol;

public class Protocol {

public static String PREFIX_DELIMITER;
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


}
