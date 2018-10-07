package root.server.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.scene.traversal.Direction;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;

public class ClientConnection implements Runnable {
    private Socket socket;
    private Client client;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientConnection(Socket socket) {
        this.socket = socket;
        this.client = new Client("Oldřich", this);
        ClientManager.getInstance().add(client);
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Server.ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Server.ENCODING));
            this.reader = reader;
            this.writer = writer;

            String line;
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
        System.out.println("Incomming message " + message);
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
                String response = ClientManager.getInstance().tryAddPlayerToLobby(lobbyName, client) ? "CONNECTED TO " + client.getLobby().getName() + " " + client.IDENTIFIER + " " + client.getLobby().getMapName() : "FULL";
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

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        System.out.println(message + " " + this.client.IDENTIFIER);
        writer.println(message);

    }
}
