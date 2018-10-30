package server.processors;

import server.clientservices.ClientConnection;

import java.io.IOException;

public abstract class MessageProcessor {

    protected final ClientConnection clientConnection;

    public MessageProcessor(ClientConnection connection){
    this.clientConnection=connection;

    }
   public abstract void processMessage(String message) throws IOException;
}
