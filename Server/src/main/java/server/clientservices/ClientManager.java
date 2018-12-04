package server.clientservices;

import controller.MainController;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ClientManager {

    private static final String LOBBY_DELIM = "|";
    private static ClientManager INSTANCE = new ClientManager();
    private MainController controller;
    private List<Client> clients = new LinkedList<>();

    private ClientManager() {

    }

    public MainController getController() {
        return controller;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public synchronized void add(Client client) {
        System.out.println("Client " + client.getIdentifier() + " came");
        clients.add(client);
        System.out.println(this.toString());
        controller.updateClients();
    }

    public synchronized void remove(Client client) {
        Lobby lobby = client.getLobby();
        if (lobby != null && lobby.isFull()) {
            lobby.removeOtherPlayer();
        }
        clients.remove(client);
        System.out.println(this.toString());
    }

    public synchronized List<String> getLobbies() {
        List<String> lobbies = new LinkedList<>();
        clients.forEach(client -> {
            Lobby lobby = client.getLobby();
            if (lobby != null) {
                String lobbyInfo = lobby.getName() + LOBBY_DELIM + lobby.getOwner().getIdentifier() + LOBBY_DELIM + lobby.getPlayerCount();
                if (!lobbies.contains(lobbyInfo)) {
                    lobbies.add(lobbyInfo);
                }
            }
        });
        return lobbies;
    }

    public synchronized boolean isUserNameUnique(String name) {
        for (Client client : clients) {
            if (client.getIdentifier().equals(name)) {
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
                if (client.getIdentifier().equals("OLDRICH")) {
                    client.getClientConnection().sendMessage(message);
                } else {
                    client.getClientConnection().sendMessage("ok");
                }
            });
        }
    }
}
