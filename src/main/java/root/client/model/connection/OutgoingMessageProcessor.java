package root.client.model.connection;

import java.io.PrintWriter;

public class OutgoingMessageProcessor {

    private final PrintWriter writer;

    public OutgoingMessageProcessor(PrintWriter writer) {
        this.writer = writer;
    }


    public void sendMessage(String message) {
        writer.println(message);
    }
}
