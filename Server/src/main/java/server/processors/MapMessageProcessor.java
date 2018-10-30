package server.processors;


import server.clientservices.Client;
import server.clientservices.ClientConnection;
import server.protocol.map.MapProtocol;
import server.protocol.map.MapProtocolIn;

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
            otherPlayer.getClientConnection().sendMessage(protocol.send().playerHasLost());
        }
        if (in.restartMapRequest()) {
            if (clientConnection.getClient() == owner) {
                otherPlayer.CLIENT_CONNETION.sendMessage(protocol.send().restartMap());
            } else {
                owner.CLIENT_CONNETION.sendMessage(protocol.send().restartMap());
            }
        }
        if (in.agreed()) {
            otherPlayer.CLIENT_CONNETION.sendMessage(protocol.send().agreed());
            owner.CLIENT_CONNETION.sendMessage(protocol.send().agreed());
        }
        if (in.disagreed()) {
            otherPlayer.CLIENT_CONNETION.sendMessage(protocol.send().disagreed());
            owner.CLIENT_CONNETION.sendMessage(protocol.send().disagreed());
        }
        if (in.quitMap()) {
            if (clientConnection.getClient() == owner) {
                otherPlayer.CLIENT_CONNETION.sendMessage(protocol.send().playerHasLeft());
            } else {
                owner.CLIENT_CONNETION.sendMessage(protocol.send().playerHasLeft());
            }
            otherPlayer.deleteLobby();
            owner.deleteLobby();
            clientConnection.sendMessage(protocol.send().youHaveLeft());
        }
    }
}
