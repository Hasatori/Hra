package client.controller.multiplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.model.connection.ServerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import client.controller.StartController;
import client.model.connection.InputReader;
import client.model.connection.OutputWriter;
import client.model.protocol.general.GeneralProtocol;
import client.model.protocol.general.GeneralProtocolIn;
import client.util.ResourceLoader;
import client.view.DialogFactory;
import client.view.MultiplayerView;
import client.view.View;
import client.model.map.CreatedLobby;

/**
 * Controller class for server.
 */
public class MultiplayerController extends ServerController {
    private final Logger LOGGER = LoggerFactory.getLogger(MultiplayerController.class);
    private final static String LOBBY_SPLIT_DELIM = "\\|";
    private final GeneralProtocol protocol;
    private View view;

    /**
     * @param stage stage
     * @param serverConnection server connection
     * @param playerName name of the player
     */
    public MultiplayerController(Stage stage, ServerConnection serverConnection, String playerName) {
        super(stage,serverConnection, playerName);
        this.protocol = new GeneralProtocol();

    }

    @Override
    public void loadView() {
        try {
            this.view = new MultiplayerView(this, loadLobbies(), playerName);
        } catch (IOException e) {
            LOGGER.error("Failed to create MultiplayerView", e);
        }
        this.stage.setScene(view);
        stage.show();
    }

    /**
     * Loads active lobbies and passes them.
     * @return List of entity CreatedLobbies
     */
    public List<CreatedLobby> loadLobbies() {
        outgoingMessageProcessor.sendMessage(protocol.send().getLobbies());
        List<String> result = this.protocol.get(incomingMessageProcessor.getMessage()).getLobbies();
        List<CreatedLobby> lobbies = new ArrayList<>();
        for (String lobby : result) {
            String[] data = lobby.split(LOBBY_SPLIT_DELIM);
            lobbies.add(new CreatedLobby(data[0], data[1], data[2]));
        }
        return lobbies;
    }

    /**
     * Disconnect and redirect back to StartController.
     */
    public void disconnect() {
        try {
            new StartController(stage).loadView();
            serverConnection.disconnect();
        } catch (IOException e) {
            LOGGER.error("Failed to disconnect to StartController", e);
        }
    }

    /**
     * Method for creating a public lobby.
     * @param lobbyName name of the new lobby
     */
    public void createLobby(String lobbyName) {
        try {
            outgoingMessageProcessor.sendMessage(protocol.send().createLobby(lobbyName, ResourceLoader.getMultiplayerMaps().get(0)));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        GeneralProtocolIn in = protocol.get(incomingMessageProcessor.getMessage());
        if (in.duplicateLobbyName()) {
            DialogFactory.getAlert(Alert.AlertType.WARNING, "Creating lobby", "Lobby with this name already exists.").showAndWait();
        } else if (in.lobbyCreated()) {
            new LobbyOwnerController(stage, playerName, serverConnection).loadView();
        }
    }

    /**
     * Method for joining an existing lobby
     * @param name name of the lobby to join
     */
    public void joinLobby(String name) {
        outgoingMessageProcessor.sendMessage(protocol.send().joinLobby(name));
        GeneralProtocolIn in = protocol.get(incomingMessageProcessor.getMessage());
        if (in.lobbyFull()) {
            DialogFactory.getAlert(Alert.AlertType.WARNING, "Connecting lobby", "Lobby is full.").showAndWait();
        } else if(in.lobbyDoesNotExist()){
            DialogFactory.getAlert(Alert.AlertType.WARNING, "Connecting lobby", "Lobby is does not exist.").showAndWait();
        }else if (in.connectedToLobby()) {
            String[] parts = in.getLobbyCredentials();
            new LobbySecondPlayerController(stage, parts[1], playerName, parts[2], serverConnection).loadView();
        }
    }
}