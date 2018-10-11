package root.client.model.connection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class InputReader {

    private final Logger LOGGER = LoggerFactory.getLogger(InputReader.class);
    private final BufferedReader reader;

    public InputReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String getMessage() {
        try {
            String message = reader.readLine();
            LOGGER.info("Incoming message {}",message);
            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return null;
    }
}
