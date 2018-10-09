package root.server.main.processors;

import root.server.main.ClientConnection;
import root.server.main.ClientManager;
import root.server.main.protocol.lobby.LobbyProtocol;

import java.io.IOException;

public class LobbyMessageProcessor extends MessageProcessor{
    private final LobbyProtocol protocol;

    public LobbyMessageProcessor(ClientConnection connection) {
        super(connection);
        this.protocol=new LobbyProtocol();
    }

    @Override
   public void processMessage(String message) throws IOException {

    }
}
