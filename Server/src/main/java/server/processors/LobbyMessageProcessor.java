package server.processors;

import java.io.IOException;

import server.clientservices.Client;
import server.clientservices.ClientConnection;
import server.protocol.lobby.LobbyProtocol;
import server.protocol.lobby.LobbyProtocolIn;

public class LobbyMessageProcessor extends MessageProcessor {
    private final LobbyProtocol protocol;
    private static final String OWNER_SENT_MESSAGE = "LOBBY:SENT MESSAGE-OWNER";
    private static final String SECPLAYER_SENT_MESSAGE = "LOBBY:SENT MESSAGE-SECPLAYER";

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
            clientConnection.getClient().getLobby().removeOtherPlayer();
        }
        if (in.destroyLobby()) {
            Client otherPlayer = clientConnection.getClient().getLobby().getOtherPlayer();
            if (otherPlayer != null) {
                otherPlayer.getClientConnection().sendMessage(protocol.send().playerKicked());
                otherPlayer.deleteLobby();
                clientConnection.getClient().getLobby().removeOtherPlayer();
            }

            clientConnection.getClient().getLobby().getOwner().deleteLobby();
            clientConnection.sendMessage(protocol.send().lobbyDeleted());
        }
        if (in.startGame()) {
            clientConnection.getClient().getLobby().getOtherPlayer().getClientConnection().sendMessage(protocol.send().startGame());
            clientConnection.sendMessage(protocol.send().startGame());
        }
        if (in.sendLobbyMessage()) {
        	if (message.startsWith(OWNER_SENT_MESSAGE)) {
        		clientConnection.getClient().getLobby().getOtherPlayer().getClientConnection().sendMessage(protocol.send().sendLobbyMessage(message));
        	}
        	else if (message.startsWith(SECPLAYER_SENT_MESSAGE)) {
        		clientConnection.getClient().getLobby().getOwner().getClientConnection().sendMessage(protocol.send().sendLobbyMessage(message));
        	}
        }
    }
}
