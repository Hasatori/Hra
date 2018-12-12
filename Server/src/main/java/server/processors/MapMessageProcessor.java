package server.processors;

import server.clientservices.Client;
import server.clientservices.ClientConnection;
import server.protocol.map.MapProtocol;
import server.protocol.map.MapProtocolIn;

/**
 * Message processor of map messages.
 */
public class MapMessageProcessor extends MessageProcessor {
    private final MapProtocol protocol;

    /**
     * @param connection client connection
     */
    public MapMessageProcessor(ClientConnection connection) {
        super(connection);
        this.protocol = new MapProtocol();
    }

    @Override
    public void processMessage(String message) {
        MapProtocolIn in = protocol.get(message);
        Client owner = clientConnection.getClient().getLobby().getOwner();
        Client otherPlayer = clientConnection.getClient().getLobby().getOtherPlayer();
        if (in.moveNexPlayer()) {
            if (clientConnection.getClient() == owner) {
                otherPlayer.getClientConnection().sendMessage(protocol.send().movePlayer(in.getDirectionToMove()));
            } else {
                owner.getClientConnection().sendMessage(protocol.send().movePlayer(in.getDirectionToMove()));
            }
        }
        if (in.won()) {
            if (clientConnection.getClient() == owner) {
                otherPlayer.getClientConnection().sendMessage(protocol.send().playerHasLost());
                owner.getClientConnection().sendMessage(protocol.send().playerHasWon());
            } else {
                owner.getClientConnection().sendMessage(protocol.send().playerHasLost());
                otherPlayer.getClientConnection().sendMessage(protocol.send().playerHasWon());
            }
        }
        if (in.restartMapRequest()) {
            if (clientConnection.getClient() == owner) {
                otherPlayer.getClientConnection().sendMessage(protocol.send().restartMap());
            } else {
                owner.getClientConnection().sendMessage(protocol.send().restartMap());
            }
        }
        if (in.agreed()) {
            otherPlayer.getClientConnection().sendMessage(protocol.send().agreed());
            owner.getClientConnection().sendMessage(protocol.send().agreed());
        }
        if (in.disagreed()) {
            otherPlayer.getClientConnection().sendMessage(protocol.send().disagreed());
            owner.getClientConnection().sendMessage(protocol.send().disagreed());
        }
        if (in.quitMap()) {
            if (clientConnection.getClient() == owner) {
                otherPlayer.getClientConnection().sendMessage(protocol.send().playerHasLeft());
            } else {
                owner.getClientConnection().sendMessage(protocol.send().playerHasLeft());
            }
            otherPlayer.deleteLobby();
            owner.deleteLobby();
            clientConnection.sendMessage(protocol.send().youHaveLeft());
        }
    }
}
