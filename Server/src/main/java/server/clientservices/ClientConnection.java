package server.clientservices;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.main.Server;
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
            getProcessor(line).processMessage(line);
            while ((line = reader.readLine()) != null) {
                processMessage(line);
            }
            socket.close();
            ClientManager.getInstance().remove(client);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                ClientManager.getInstance().remove(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String message) throws SocketException {
        LOGGER.info("Incoming message {}", message);
        try {
            getProcessor(message).processMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        LOGGER.info("Outgoing message {}", message);
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
