package client.model.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Input reader class for reading messages.
 */
public class InputReader {

    private final Logger LOGGER = LoggerFactory.getLogger(InputReader.class);
    private final BufferedReader reader;

    /**
     * @param reader reader for reading messages
     */
    public InputReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Reads message from input line.
     * @return message
     */
    public String getMessage() {
        try {
            String message = reader.readLine();
            LOGGER.info("Incoming message {}",message);
            return message;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        System.out.println("null");
        return null;
    }
}
