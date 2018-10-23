package root.server.processors;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import root.server.clientservices.Client;
import root.server.clientservices.ClientConnection;
import root.server.clientservices.ClientManager;
import root.server.clientservices.Lobby;
import root.server.protocol.general.GeneralProtocol;
import root.server.protocol.general.GeneralProtocolIn;
import root.server.protocol.lobby.LobbyProtocol;

public class GeneralMessageProcessor extends MessageProcessor {
    private final GeneralProtocol protocol;
    private final Logger LOGGER = LoggerFactory.getLogger(GeneralMessageProcessor.class);

    public GeneralMessageProcessor(ClientConnection connection) {
        super(connection);
        this.protocol = new GeneralProtocol();

    }

    @Override
    public void processMessage(String message) throws IOException {
        GeneralProtocolIn in = this.protocol.get(message);
        if (in.newUser()) {
            if (!ClientManager.getInstance().isUserNameUnique(in.getNewUser())) {
                clientConnection.sendMessage(protocol.send().duplicateUserName());
            } else {
                Client client = new Client(in.getNewUser(), clientConnection);
                clientConnection.setClient(client);
                ClientManager.getInstance().add(client);
                clientConnection.sendMessage(protocol.send().loginOk());
            }
        }
        if (in.wantLobbies()) {
            this.clientConnection.sendMessage(protocol.send().sendLobbies(ClientManager.getInstance().getLobbies()));
        }
        if (in.wannaCreateLobby()) {
        	String[] lobbyInfo = in.getLobbyAndMapName();
            String lobbyName = lobbyInfo[0];
            String mapName = lobbyInfo[1];
            if (ClientManager.getInstance().isLobbyNameUnique(lobbyName)) {
                this.clientConnection.getClient().createLobby(lobbyName, mapName);
                clientConnection.sendMessage(protocol.send().lobbyCreated());
            } else {
                clientConnection.sendMessage(protocol.send().duplicateLobbyName());
            }
        }
        if (in.wannaJoinLobby()) {
            String lobbyName = in.getLobbyName();
            LOGGER.debug("Lobby name:{}", lobbyName);
            if (ClientManager.getInstance().isLobbyFull(lobbyName)) {
                clientConnection.sendMessage(protocol.send().lobbyIsFull());
            } else {
                ClientManager.getInstance().addPlayerToLobby(lobbyName, clientConnection.getClient());
                Lobby lobby = ClientManager.getInstance().getLobby(lobbyName);
                clientConnection.sendMessage(protocol.send().connectedToLobby(lobbyName, lobby.getOwner().IDENTIFIER, lobby.getMapName()));
                lobby.getOwner().getClientConnection().sendMessage(new LobbyProtocol().send().connectedSecondPlayer(clientConnection.getClient().IDENTIFIER));
            }
        }

    }
}
