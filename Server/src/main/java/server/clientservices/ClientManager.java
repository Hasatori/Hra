package server.clientservices;

import controller.MainController;
import server.protocol.Protocol;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Singleton class for managing all active clients.
 */
public class ClientManager {

    private static final String LOBBY_DELIM = "|";
    private static ClientManager INSTANCE = new ClientManager();
    private MainController controller;
    private List<Client> clients = new LinkedList<>();

    private ClientManager() {

    }

    /**
     * Getter for main controller.
     *
     * @return MainController
     */
    public MainController getController() {
        return controller;
    }

    /**
     * Returns list of active clients
     *
     * @return List of Client entities
     */
    public List<Client> getClients() {
        return clients;
    }

    /**
     * Sets main controller.
     *
     * @param controller main controller
     */
    public void setController(MainController controller) {
        this.controller = controller;
    }

    /**
     * Adds a client to an existing list of active clients.
     *
     * @param client new client
     */
    public synchronized void add(Client client) {
        System.out.println("Client " + client.getIdentifier() + " came");
        clients.add(client);
        System.out.println(this.toString());
        controller.updateClients();
    }

    /**
     * Removes a client from an existing list of active clients.
     *
     * @param client client to remove
     */
    public synchronized void remove(Client client) {
        Lobby lobby = client.getLobby();
        if (lobby != null && lobby.isFull()) {
            lobby.removeOtherPlayer();
        }
        clients.remove(client);
        System.out.println(this.toString());
    }

    /**
     * Removes all active clients at once.
     */
    public void removeAllClients() {
        clients.forEach(client -> {
            client.getClientConnection().sendMessage(Protocol.DISCONNECTED);
            client.getClientConnection().interrupt();
        });
        this.clients.clear();
    }

    /**
     * Lists all the active lobbies.
     *
     * @return List of lobby information
     */
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

    /**
     * Checks if user has an unique name.
     *
     * @param name user name to check
     * @return true=name does not exist yet
     */
    public synchronized boolean isUserNameUnique(String name) {
        for (Client client : clients) {
            if (client.getIdentifier().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if lobby name is unique.
     *
     * @param lobbyName lobby name to check
     * @return true=name does not exist yet
     */
    public synchronized boolean isLobbyNameUnique(String lobbyName) {
        for (Client client : clients) {
            if (client.getLobby() != null && client.getLobby().getName().equals(lobbyName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether lobby is full.
     *
     * @param lobbyName lobby name
     * @return true=lobby is full
     */
    public synchronized boolean isLobbyFull(String lobbyName) {
        for (Client client : clients) {
            Lobby lobby = client.getLobby();
            if (lobby != null && lobby.getName().equals(lobbyName) && !lobby.isFull()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get lobby by name of the lobby.
     *
     * @param lobbyName lobby name
     * @return found lobby
     */
    public synchronized Lobby getLobby(String lobbyName) {
        for (Client client : clients) {
            Lobby lobby = client.getLobby();
            if (lobby != null && lobby.getName().equals(lobbyName)) {
                return lobby;
            }
        }
        return null;
    }

    /**
     * Adds player to an existing lobby if lobby is not full.
     *
     * @param name   lobby name
     * @param client client to add to lobby
     */
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

    /**
     * Sends message to all clients.
     *
     * @param message message to send
     */
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
