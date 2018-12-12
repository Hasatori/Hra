package client.controller.multiplayer;

import java.io.IOException;

import client.model.connection.ServerConnection;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import client.model.connection.InputReader;
import client.model.connection.OutputWriter;
import client.model.protocol.lobby.LobbyProtocol;
import client.model.protocol.lobby.LobbyProtocolIn;
import client.util.ResourceLoader;
import client.view.DialogFactory;
import client.view.LobbyOwnerView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for owner of lobby.
 */
@SuppressWarnings("restriction")
public class LobbyOwnerController extends ServerController {

    private final Logger LOGGER = LoggerFactory.getLogger(LobbyOwnerController.class);
    private final LobbyProtocol protocol;
    private LobbyOwnerView view;
    private String secondPlayerName;
    private boolean isFull = false;

    /**
     * @param stage            stage
     * @param playerName       owner name
     * @param serverConnection server connection
     */
    public LobbyOwnerController(Stage stage, String playerName, ServerConnection serverConnection) {
        super(stage, serverConnection, playerName);
        try {
            this.view = new LobbyOwnerView(this, ResourceLoader.getMultiplayerMaps(), playerName);
        } catch (IOException e) {
            LOGGER.error("Failed to create LobbyOwnerView", e);
        }
        this.protocol = new LobbyProtocol();
    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
        waitForMessage();
    }

    /**
     * Sets map of the game in lobby.
     *
     * @param mapName name of the map
     */
    public void setMap(String mapName) {
        outgoingMessageProcessor.sendMessage(protocol.send().setMap(mapName));
    }

    /**
     * Method for waiting for messages.
     */
    private void waitForMessage() {
        new Thread(() -> {
            String message = incomingMessageProcessor.getMessage();
            while (message != null) {
                LobbyProtocolIn in = protocol.get(message);
                if (in.playerConnected()) {
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby",
                                "Player " + in.getSecondPlayerName() + " has connected").showAndWait();
                        setSecondPlayerName(in.getSecondPlayerName());
                    });
                    isFull = true;
                }
                if (in.playerHasLeft()) {
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby",
                                "Player " + in.getSecondPlayerName() + " has left").showAndWait();
                        view.lobbyIsEmpty();
                    });
                    isFull = false;
                }
                if (in.messageSent()) {
                    view.receiveLobbyMessage(message.replaceFirst(LobbyProtocol.MSG_PREFIX + ":SENT MESSAGE-SECPLAYER", ""), false, true);
                }
                if (in.lobbyDeleted()) {
                    Platform.runLater(() ->
                            new MultiplayerController(stage, serverConnection, playerName).loadView());
                    break;
                }
                if (in.start()) {
                    break;
                }
                message = incomingMessageProcessor.getMessage();
            }
        }).start();
    }

    /**
     * Sets name of the second player.
     *
     * @param name second player name
     */
    public void setSecondPlayerName(String name) {
        view.setSecondPlayerName(name);
        secondPlayerName = name;
        isFull = true;
    }

    /**
     * Starts the map game if lobby is full.
     *
     * @param mapName map name
     */
    public void startGame(String mapName) {
        if (isFull) {
            outgoingMessageProcessor.sendMessage(protocol.send().startGame());
            new MultiplayerMapController(stage, mapName, 0, playerName,
                    secondPlayerName, 1, serverConnection, true).loadView();
        }
    }

    /**
     * Kicks second player out of the lobby.
     */
    public void kickOtherPlayer() {
        outgoingMessageProcessor.sendMessage(protocol.send().kickOtherPlayer());
        isFull = false;
        view.lobbyIsEmpty();
    }

    /**
     * Deletes the lobby, thus kicking everyone out of it.
     */
    public void deleteLobby() {
        outgoingMessageProcessor.sendMessage(protocol.send().destroyLobby());
    }

    /**
     * Sends lobby message to server.
     *
     * @param msg message to send
     */
    public void sendLobbyMessage(String msg) {
        outgoingMessageProcessor.sendMessage(protocol.send().sendLobbyMessage("-OWNER" + msg));
    }
}
