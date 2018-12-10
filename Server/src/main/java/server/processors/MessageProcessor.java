package server.processors;

import server.clientservices.ClientConnection;

import java.io.IOException;

/**
 * Abstract class message processor.
 * This class handles all incoming messages.
 */
public abstract class MessageProcessor {

    protected final ClientConnection clientConnection;

    /**
     * @param connection client connection
     */
    public MessageProcessor(ClientConnection connection){
        this.clientConnection = connection;
    }

    /**
     * Processes message.
     * Handles messages based on the their type.
     * @param message message to process
     */
    public abstract void processMessage(String message);
}
