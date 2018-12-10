package server.clientservices;

/**
 * Entity class for a client.
 */
public class Client {

    private final String identifier;
    private final ClientConnection clientConnection;
    private Lobby lobby = null;

    /**
     * @param identifier identifier of a client
     * @param clientConnection client connection
     */
    public Client(String identifier, ClientConnection clientConnection) {
        this.identifier = identifier;
        this.clientConnection = clientConnection;
    }

    /**
     * Creates lobby if name doesn't exist yet.
     * @param name name of the lobby
     * @param mapName name of the map
     */
    public void createLobby(String name, String mapName) {
        if (this.lobby != null) {
            throw new IllegalStateException("Lobby was already created");
        } else {
            this.lobby = new Lobby(this, name, mapName);
        }
    }

    /**
     * Deletes created lobby.
     */
    public void deleteLobby() {
        this.lobby = null;
    }

    /**
     * Setter for lobby.
     * @param lobby lobby
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Returns lobby where this client is connected to.
     * @return lobby
     */
    public Lobby getLobby() {
        return this.lobby;
    }

    /**
     * Getter for client connection.
     * @return ClientConnection
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * Return unique identifier of Client.
     * @return id
     */
    public String getIdentifier() {
        return this.identifier;
    }
}
