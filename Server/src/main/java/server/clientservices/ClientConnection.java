package server.clientservices;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import controller.MainController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.Server;
import server.processors.GeneralMessageProcessor;
import server.processors.LobbyMessageProcessor;
import server.processors.MapMessageProcessor;
import server.processors.MessageProcessor;
import server.protocol.Protocol;
import server.protocol.ProtocolType;


public class ClientConnection implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);
    private final MessageProcessor generalMessageProcessor, lobbyMessageProcessor, mapMessageProcessor;

    private Socket socket;
    private Client client;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientConnection(Socket socket) {
        this.socket = socket;
        this.generalMessageProcessor = new GeneralMessageProcessor(this);
        this.lobbyMessageProcessor = new LobbyMessageProcessor(this);
        this.mapMessageProcessor = new MapMessageProcessor(this);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void run() {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Server.ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Server.ENCODING));
            this.reader = reader;
            this.writer = writer;
            String line = reader.readLine();
            processMessage(line);
            while ((line = reader.readLine()) != null) {
                processMessage(line);
            }
            socket.close();
            ClientManager.getInstance().remove(client);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                socket.close();
                String prefix;
                if (client != null) {
                    prefix = client.getIdentifier() + " | ";
                    ClientManager.getInstance().getController().updateMessages(prefix + " has left");
                    ClientManager.getInstance().remove(client);
                    ClientManager.getInstance().getController().updateClients();
                } else {
                    ClientManager.getInstance().getController().updateMessages("Connection lost");
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private void processMessage(String message) {
        LOGGER.info("Incoming message {}", message);
        String prefix = "";
        if (client != null) {
            prefix = client.getIdentifier() + " | ";
        }
        ClientManager.getInstance().getController().updateMessages(prefix + " Incoming message - " + message);
        try {
            getProcessor(message).processMessage(message);
        } catch (IOException e) {
            LOGGER.error("Failed to process message", e);
        }
    }

    public void sendMessage(String message) {
        LOGGER.info("Outgoing message {}", message);
        String prefix = "";
        if (client != null) {
            prefix = client.getIdentifier() + " | ";
        }

        ClientManager.getInstance().getController().updateMessages(prefix + " Outgoing message - " + message);
        writer.println(message);
    }

    private MessageProcessor getProcessor(String message) {
        ProtocolType protocolType = Protocol.getProtocolType(message);
        switch (protocolType) {
            case GENERAL:
                return generalMessageProcessor;
            case MAP:
                return mapMessageProcessor;
            case LOBBY:
                return lobbyMessageProcessor;
            default:
                throw new IllegalArgumentException("Unknown message type");
        }
    }
}
