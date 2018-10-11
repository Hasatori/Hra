package root.server.processors;


import root.server.clientservices.ClientConnection;
import root.server.protocol.map.MapProtocol;
import root.server.protocol.map.MapProtocolIn;

import java.io.IOException;

public class MapMessageProcessor extends MessageProcessor {
    private final MapProtocol protocol;

    public MapMessageProcessor(ClientConnection connection) {
        super(connection);
        this.protocol = new MapProtocol();
    }

    @Override
    public void processMessage(String message) throws IOException {
        MapProtocolIn in = protocol.get(message);

        if (in.moveNexPlayer()) {
            if (clientConnection.getClient() == clientConnection.getClient().getLobby().getOwner()) {
                clientConnection.getClient().getLobby().getOtherPlayer().getClientConnection().sendMessage(protocol.send().movePlayer(in.getDirectionToMove()));
            } else {
                clientConnection.getClient().getLobby().getOwner().getClientConnection().sendMessage(protocol.send().movePlayer(in.getDirectionToMove()));
            }

        }
        if (in.won()) {
            clientConnection.getClient().getLobby().getOtherPlayer().getClientConnection().sendMessage(protocol.send().playerHasLost());
        }
    }
}
