package client.model.connection;


import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Output writer class for sending messages.
 */
public class OutputWriter {

    private final PrintWriter writer;
    private final Logger LOGGER = LoggerFactory.getLogger(OutputWriter.class);

    /**
     * @param writer writer for sending messages
     */
    public OutputWriter(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * Sends message of type String.
     * @param message message to send
     */
    public void sendMessage(String message) {
        LOGGER.info("Outgoing message: {}", message);
        writer.println(message);
    }
}
