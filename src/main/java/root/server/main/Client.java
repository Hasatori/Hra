package root.server.main;

public class Client {
    public final String IDENTIFIER;
    public final ClientConnection CLIENT_CONNETION;

    public Client(String identifier, ClientConnection clientConnection) {
        IDENTIFIER = identifier;
        CLIENT_CONNETION = clientConnection;
    }


}
