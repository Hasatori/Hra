package server.clientservices;

public class Client {
    private final String identifier;
    private final ClientConnection clientConnection;
    private Lobby lobby = null;

    public Client(String identifier, ClientConnection clientConnection) {
        this.identifier = identifier;
        this.clientConnection = clientConnection;
    }

    public void createLobby(String name, String mapName) {
        if (this.lobby != null) {
            throw new IllegalStateException("Lobby was already created");
        } else {
            this.lobby = new Lobby(this, name, mapName);
        }
    }
    
    public void deleteLobby() {
        this.lobby = null;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
    
    public Lobby getLobby() {
        return this.lobby;
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
