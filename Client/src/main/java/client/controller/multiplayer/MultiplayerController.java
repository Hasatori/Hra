package client.controller.multiplayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class MultiplayerController extends ServerController {
    private final static String LOBBY_SPLIT_DELIM = "\\|";
    private final Logger LOGGER = LoggerFactory.getLogger(MultiplayerController.class);
    private final GeneralProtocol protocol;
    private View view;

    public MultiplayerController(Stage stage, InputReader incommingMessageProccessor, OutputWriter outgoingMessageProccessor, String playerName) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        this.protocol = new GeneralProtocol();
    }

    @Override
    public void loadView() {
        try {
            this.view = new MultiplayerView(this, loadLobbies(),playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage.setScene(view);
        stage.show();
    }

    public List<CreatedLobby> loadLobbies() {
        outgoingMessageProccessor.sendMessage(protocol.send().getLobbies());
        List<String> result = this.protocol.get(incommingMessageProccessor.getMessage()).getLobbies();
        List<CreatedLobby> lobbies = new ArrayList<>();
        for (String lobby : result) {
        	String[] data = lobby.split(LOBBY_SPLIT_DELIM);
        	lobbies.add(new CreatedLobby(data[0], data[1] , data[2]));
        }
        return lobbies;
    }

    public void back() {
        try {
            new StartController(stage).loadView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLobby(String lobbyName) {
        try {
            outgoingMessageProccessor.sendMessage(protocol.send().createLobby(lobbyName, ResourceLoader.getMultiplayerMaps().get(0)));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        GeneralProtocolIn in = protocol.get(incommingMessageProccessor.getMessage());
        if (in.duplicateLobbyName()) {
            DialogFactory.getAlert(Alert.AlertType.WARNING, "Creating lobby", "Lobby with this name already exists.").showAndWait();
        } else if (in.lobbyCreated()) {
            new LobbyOwnerController(stage, playerName, incommingMessageProccessor, outgoingMessageProccessor).loadView();
        }

    }

    public void joinLobby(String name) {
        outgoingMessageProccessor.sendMessage(protocol.send().joinLobby(name));
        GeneralProtocolIn in = protocol.get(incommingMessageProccessor.getMessage());
        if (in.lobbyFull()) {
            DialogFactory.getAlert(Alert.AlertType.WARNING, "Connecting lobby", "Lobby is full.").showAndWait();
        } else if (in.connectedToLobby()) {
            String[] parts = in.getLobbyCredentials();
            new LobbySecondPlayerController(stage, parts[1], playerName, parts[2], incommingMessageProccessor, outgoingMessageProccessor).loadView();
        }
    }
}
