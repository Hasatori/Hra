package server.clientservices;

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
    	client.getLobby().removeOtherPlayer();
        clients.remove(client);
        System.out.println(this.toString());
    }

    public synchronized List<String> getLobbies() {
        List<String> lobbies = new LinkedList<String>();
        clients.forEach(client -> {
        	Lobby lobby = client.getLobby();
            if (lobby != null) {
                String lobbyInfo = lobby.getName() + "|" + lobby.getOwner().IDENTIFIER + "|" + lobby.getPlayerCount();
                if (!lobbies.contains(lobbyInfo)) {
                    lobbies.add(lobbyInfo);
                }

            }
        });
        return lobbies;
    }

    public synchronized boolean isUserNameUnique(String name) {
        for (Client client : clients) {
            if (client.IDENTIFIER.equals(name)) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean isLobbyNameUnique(String lobbyName) {
        for (Client client : clients) {
            if (client.getLobby() != null && client.getLobby().getName().equals(lobbyName)) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean isLobbyFull(String lobbyName) {
        for (Client client : clients) {
            Lobby lobby = client.getLobby();
            if (lobby != null && lobby.getName().equals(lobbyName) && !lobby.isFull()) {
                return false;
            }
        }
        return true;
    }

    public synchronized Lobby getLobby(String lobbyName) {
        for (Client client : clients) {
            Lobby lobby = client.getLobby();
            if (lobby != null && lobby.getName().equals(lobbyName)) {
                return lobby;
            }
        }
        return null;
    }

    public synchronized void addPlayerToLobby(String name, Client client) {
        for (Client client1 : clients) {
            Lobby lobby = client1.getLobby();
            if (lobby != null && lobby.getName().equals(name) && !lobby.isFull()) {
                lobby.addPlayer(client);
            }
        }
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
