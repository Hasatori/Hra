package root.server.processors;

import root.server.clientservices.Client;
import root.server.clientservices.ClientConnection;
import root.server.protocol.lobby.LobbyProtocol;
import root.server.protocol.lobby.LobbyProtocolIn;

import java.io.IOException;

public class LobbyMessageProcessor extends MessageProcessor {
    private final LobbyProtocol protocol;

    public LobbyMessageProcessor(ClientConnection connection) {
        super(connection);
        this.protocol = new LobbyProtocol();
    }

    @Override
    public void processMessage(String message) throws IOException {
        LobbyProtocolIn in = protocol.get(message);
        if (in.setMap()) {
            if (clientConnection.getClient().getLobby().isFull()) {
                clientConnection.getClient().getLobby().getOtherPlayer().getClientConnection().sendMessage(protocol.send().setMap(in.getMapName()));
            }

        }
        if (in.leaveLobby()) {
            clientConnection.getClient().getLobby().getOwner().getClientConnection().sendMessage(protocol.send().playerHasLeft());
            clientConnection.getClient().getLobby().getOtherPlayer().getClientConnection().sendMessage(protocol.send().playerHasLeft());
            clientConnection.getClient().getLobby().kickOtherPlayer();
        }
        if (in.destroyLobby()) {
            Client otherPlayer = clientConnection.getClient().getLobby().getOtherPlayer();
            if (otherPlayer != null) {
                otherPlayer.getClientConnection().sendMessage(protocol.send().playerKicked());
                otherPlayer.deleteLobby();
                clientConnection.getClient().getLobby().kickOtherPlayer();
            }

            clientConnection.getClient().getLobby().getOwner().deleteLobby();
            clientConnection.sendMessage(protocol.send().lobbyDeleted());
        }
        if (in.startGame()) {
            clientConnection.getClient().getLobby().getOtherPlayer().getClientConnection().sendMessage(protocol.send().startGame());
            clientConnection.sendMessage(protocol.send().startGame());
        }
    }
}
