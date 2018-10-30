package server.clientservices;

public class Client {
    public final String IDENTIFIER;
    public final ClientConnection CLIENT_CONNETION;
    private Lobby lobby = null;

    public Client(String identifier, ClientConnection clientConnection) {
        IDENTIFIER = identifier;
        CLIENT_CONNETION = clientConnection;
    }
    
    public ClientConnection getClientConnection() {
        return CLIENT_CONNETION;
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
}
