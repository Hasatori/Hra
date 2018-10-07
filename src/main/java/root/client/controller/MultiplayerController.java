package root.client.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import root.client.model.connection.IncomingMessageProcessor;
import root.client.model.connection.OutgoingMessageProcessor;
import root.client.model.connection.ServerConnection;
import root.client.model.map.Box;
import root.client.util.ResourceLoader;
import root.client.view.DialogFactory;
import root.client.view.MultiplayerView;
import root.client.view.View;

import java.io.IOException;
import java.util.List;

public class MultiplayerController extends Controller {
    private final Logger LOGGER = LoggerFactory.getLogger(MultiplayerController.class);
    private View view;
    private ServerConnection serverConnection;
    private IncomingMessageProcessor incommingMessageProccessor;
    private OutgoingMessageProcessor outgoingMessageProccessor;

    public MultiplayerController(Stage stage) {
        super(stage);
        this.serverConnection = new ServerConnection();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        if (!this.serverConnection.isConnected()) {
            serverDisconnected();
        } else {

            try {
                this.view = new MultiplayerView(this, loadLobbies());
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            this.stage.setScene(view);
            stage.show();
        }

    }

    public List<String> loadLobbies() {
        this.incommingMessageProccessor = serverConnection.getIncommingMessageProccessor();
        this.outgoingMessageProccessor = serverConnection.getOutgoingMessageProccessor();
        outgoingMessageProccessor.sendMessage("LOBBY:GET LOBBIES");
        List<String> result = (List<String>) incommingMessageProccessor.processMessage();
        if (!this.serverConnection.isConnected()) {
            serverDisconnected();
        }
        return result;
    }

    public void back() {
        try {
            serverConnection.disconnect();
            new StartController(stage).loadView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLobby(String lobbyName) {
        outgoingMessageProccessor.sendMessage("LOBBY:CREATE " + lobbyName + " " + ResourceLoader.getSingleplayerMaps().get(0));
        new LobbyOwnerController(stage, lobbyName, "Oldřich", incommingMessageProccessor, outgoingMessageProccessor).loadView();
    }

    public void joinLobby(String lobbyName) {
        outgoingMessageProccessor.sendMessage("LOBBY:JOIN " + lobbyName);
        String response = (String) incommingMessageProccessor.processMessage();
        if (response.contains("FULL")) {
            DialogFactory.getAlert(Alert.AlertType.ERROR, "Connecting lobby", "Lobby is full.").showAndWait();

        } else if (response.contains("CONNECTED TO ")) {
            String[] parts = response.replace("CONNECTED TO ", "").split(" ");
            new LobbySecondPlayerController(stage, parts[0], parts[1], "Petr", parts[2], incommingMessageProccessor, outgoingMessageProccessor).loadView();
        }
    }

    public void serverDisconnected() {
        DialogFactory.getAlert(Alert.AlertType.ERROR, "Server connection", "Connection with the server can not be established. Please try it later.").showAndWait();
        try {
            new StartController(stage).loadView();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
