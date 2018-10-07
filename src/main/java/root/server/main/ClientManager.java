package root.server.main;

import com.sun.xml.internal.ws.api.model.MEP;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ClientManager {
    private List<Client> clients = new LinkedList<>();

    private static ClientManager INSTANCE = new ClientManager();

    private ClientManager() {
    }

    public synchronized void add(Client client) {
        System.out.println("Client " + client.IDENTIFIER + " came");
        clients.add(client);
        System.out.println(this.toString());
    }

    public synchronized void remove(Client client) {
        clients.remove(client);
        System.out.println(this.toString());
    }

    public synchronized List<String> getLobbies() {
        List<String> lobbies = new LinkedList<>();
        clients.forEach(client -> {
            Lobby lobby = client.getLobby();
            if (lobby != null) {
                lobbies.add(client.getLobby().getName());
            }
        });
        return lobbies;
    }

    public synchronized boolean tryAddPlayerToLobby(String name, Client client) {
        for (Client client1 : clients) {
            Lobby lobby = client1.getLobby();
            if (lobby != null && lobby.getName().equals(name) && !lobby.isFull()) {
                lobby.addPlayer(client);
                return true;
            }
        }
        return false;
    }
    public synchronized static ClientManager getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized String toString() {
        return "\n Number of client: " + clients.size();
    }

    public void sendMessageToAll(String message) {
        if (clients.size() > 1) {
            clients.forEach(client -> {
                try {
                    if (client.IDENTIFIER.equals("OLDRICH")) {

                        client.CLIENT_CONNETION.sendMessage(message);

                    } else {
                        client.CLIENT_CONNETION.sendMessage("ok");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
