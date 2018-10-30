package client.model.connection;


import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutputWritter {

    private final PrintWriter writer;
    private final Logger LOGGER = LoggerFactory.getLogger(OutputWritter.class);

    public OutputWritter(PrintWriter writer) {
        this.writer = writer;
    }

    public void sendMessage(String message) {
        LOGGER.info("Outgoing message: {}", message);
        writer.println(message);
    }
}
