package root.server.clientservices;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import root.server.main.Server;
import root.server.processors.GeneralMessageProcessor;
import root.server.processors.LobbyMessageProcessor;
import root.server.processors.MapMessageProcessor;
import root.server.processors.MessageProcessor;
import root.server.protocol.Protocol;
import root.server.protocol.ProtocolType;


public class ClientConnection implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(ClientConnection.class);
    private final MessageProcessor generalMessageProcessor, lobbyMessageProcessor, mapMessageProcessor;


    private Socket socket;

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    private Client client;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientConnection(Socket socket) {
        this.socket = socket;
        this.generalMessageProcessor = new GeneralMessageProcessor(this);
        this.lobbyMessageProcessor = new LobbyMessageProcessor(this);
        this.mapMessageProcessor = new MapMessageProcessor(this);
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Server.ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Server.ENCODING));
            this.reader = reader;
            this.writer = writer;
            String line = reader.readLine();
            line.toString();
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
      /* System.out.println("Incomming message " + message);
        String[] parts = message.split(":");
        if (parts[0].equals("LOBBY")) {
            String messageBody = parts[1];
            if (messageBody.equals("GET LOBBIES")) {
                Type type = new TypeToken<LinkedList<String>>() {
                }.getType();
                writer.println(new Gson().toJson(ClientManager.getInstance().getLobbies(), type));
            }
            if (messageBody.split(" ")[0].equals("CREATE")) {
                String lobbyName = messageBody.split(" ")[1];
                String mapName = messageBody.split(" ")[2];
                client.createLobby(lobbyName, mapName);
                System.out.println(client.getLobby().getName());
            }
            if (messageBody.split(" ")[0].equals("SETMAP")) {
                String mapName = messageBody.split(" ")[1];
                client.getLobby().setMapName(mapName);
            }
            if (messageBody.split(" ")[0].equals("JOIN")) {
                String lobbyName = messageBody.split(" ")[1];
                String response = ClientManager.getInstance().addPlayerToLobby(lobbyName, client) ? "CONNECTED TO " + client.getLobby().getName() + " " + client.IDENTIFIER + " " + client.getLobby().getMapName() : "FULL";
                writer.println("LOBBY:" + response);
            }
            if (messageBody.split(" ")[0].equals("START")) {
                String mapName = messageBody.split(" ")[1];
                client.getLobby().setMapName(mapName);
                client.getLobby().start();
            }
        }
        if (parts[0].equals("MAP")) {
            String messageBody = parts[1];
            System.out.println(messageBody);
            try {
                if (messageBody.equals("WON")) {
                    if (client == client.getLobby().getOtherPlayer()) {
                        client.getLobby().getOwner().getClientConnection().sendMessage("MAP:LOST");
                    } else {
                        client.getLobby().getOtherPlayer().getClientConnection().sendMessage("MAP:LOST");
                    }
                } else if (Arrays.asList(Direction.values()).contains(Direction.valueOf(messageBody))) {

                    if (client == client.getLobby().getOtherPlayer()) {
                        client.getLobby().getOwner().getClientConnection().sendMessage("MAP:" + messageBody);
                    } else {
                        client.getLobby().getOtherPlayer().getClientConnection().sendMessage("MAP:" + messageBody);
                    }

                }*/


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
