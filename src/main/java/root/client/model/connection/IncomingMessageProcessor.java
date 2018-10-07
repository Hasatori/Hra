package root.client.model.connection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class IncomingMessageProcessor {

    private final Logger LOGGER = LoggerFactory.getLogger(IncomingMessageProcessor.class);
    private final BufferedReader reader;

    public IncomingMessageProcessor(BufferedReader reader) {
        this.reader = reader;
    }

    public Object processMessage() {
        try {
            String message = reader.readLine();
            LOGGER.info("Incomming message {}", message);
            if (message.split(":")[0].equals("LOBBY") || message.split(":")[0].equals("MAP")) {
                LOGGER.info("Incomming message {}", message);
                return message.split(":")[1];

            } else {
                Type type = new TypeToken<LinkedList<String>>() {
                }.getType();
                return new Gson().fromJson(message, type);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("null");
        return null;
    }
}
