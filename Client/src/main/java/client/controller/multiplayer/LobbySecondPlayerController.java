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
import client.view.LobbySecondPlayerView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for second player in lobby.
 */
@SuppressWarnings("restriction")
public class LobbySecondPlayerController extends ServerController {

    private final Logger LOGGER = LoggerFactory.getLogger(LobbySecondPlayerController.class);
    private final LobbyProtocol protocol;
    private final String ownerName;
    private LobbySecondPlayerView view;
    private Stage stage;
    private String mapName;

    /**
     * @param stage stage
     * @param ownerName name of the lobby owner
     * @param playerName name of the second player
     * @param selectedMap name of the selected map
     * @param serverConnection server connection
     */
    public LobbySecondPlayerController(Stage stage, String ownerName, String playerName, String selectedMap, ServerConnection serverConnection) {
        super(stage, serverConnection, playerName);
        this.stage = stage;
        this.ownerName=ownerName;
        try {
            this.view = new LobbySecondPlayerView(this, ResourceLoader.getMultiplayerMaps());
        } catch (IOException e) {
            LOGGER.error("Failed to create LobbySecondPlayerView", e);
        }
        view.setOwnerName(ownerName);
        this.mapName = selectedMap;
        view.setMap(selectedMap);
        view.setPlayerName(playerName);
        this.protocol = new LobbyProtocol();
    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
        waitForMessages();
    }

    /**
     * Method for waiting for messages.
     */
    private void waitForMessages() {
        new Thread(() -> {
            String message = incomingMessageProcessor.getMessage();
            while (message != null) {
                if (protocol.disconnected(message)) {
                    disconnected();
                    break;
                }
                LobbyProtocolIn in = protocol.get(message);
                if (in.kicked()) {
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, serverConnection, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.WARNING, "Lobby", "You were kicked out of the lobby").showAndWait();
                    });
                    break;
                }
                if (in.setMap()) {
                    this.mapName = in.getMap();
                    Platform.runLater(() -> view.setMap(in.getMap()));
                }
                if (in.messageSent()) {
                	view.receiveLobbyMessage(message.replaceFirst(LobbyProtocol.MSG_PREFIX + ":SENT MESSAGE-OWNER", ""), true);
                }
                if (in.start()) {
                    Platform.runLater(() -> new MultiplayerMapController(stage, mapName, 1, playerName, ownerName,0, serverConnection,false).loadView());
                    break;
                }
                if (in.playerHasLeft()) {
                    Platform.runLater(() -> new MultiplayerController(stage, serverConnection, playerName).loadView());
                    break;
                }

                message = incomingMessageProcessor.getMessage();
            }
        }).start();
    }

    /**
     * Leaves the lobby as second player.
     */
    public void leaveLobby() {
        outgoingMessageProcessor.sendMessage(protocol.send().leaveLobby());
    }

    /**
     * Sends lobby message to server.
     * @param msg message to send
     */
    public void sendLobbyMessage(String msg) {
        outgoingMessageProcessor.sendMessage(protocol.send().sendLobbyMessage("-SECPLAYER" + msg));
    }

    /**
     * Getter for second player name.
     * @return name of the player
     */
    public String getPlayerName() {
    	return playerName;
    }
}
