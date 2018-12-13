package client.controller.multiplayer;

import client.model.connection.ServerConnection;
import client.model.map.Map;
import client.model.map.MapFactory;
import com.sun.javafx.scene.traversal.Direction;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import client.controller.MapController;
import client.model.protocol.map.MapProtocol;
import client.model.protocol.map.MapProtocolIn;
import client.view.DialogFactory;
import client.view.MapView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller class for multiplayer while playing a map.
 */
public class MultiplayerMapController extends ServerController implements MapController {

    private final Logger LOGGER = LoggerFactory.getLogger(MultiplayerMapController.class);
    private Map map;
    private final MapProtocol protocol;
    private final String remotePlayerName;
    private final int playerNumber, remotePlayerNumber;
    private final String mapName;
    private MapView view;
    private boolean isOwner;
    private Thread waitingForCommandsThread;

    /**
     * @param stage              stage
     * @param mapName            name of the current map
     * @param playerNumber       number of a player
     * @param playerName         name of a player
     * @param remotePlayerName   name of a remote player
     * @param remotePlayerNumber number of a remote player
     * @param serverConnection   server connection
     * @param isOwner            true=player is owner
     */
    public MultiplayerMapController(Stage stage, String mapName, int playerNumber, String playerName, String remotePlayerName, int remotePlayerNumber, ServerConnection serverConnection, boolean isOwner) {
        super(stage, serverConnection, playerName);
        this.remotePlayerName = remotePlayerName;
        this.map = MapFactory.getInstance().getMap(mapName, playerNumber, remotePlayerNumber, playerName, remotePlayerName);
        this.isOwner = isOwner;
        try {
            this.view = new MapView(this, map.getMapParts(), mapName);
        } catch (IOException e) {
            LOGGER.error("Failed to load MapView", e);
        }
        this.protocol = new MapProtocol();
        this.mapName = mapName;
        this.playerNumber = playerNumber;
        this.remotePlayerNumber = remotePlayerNumber;
    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
        waitForCommands();
    }

    @Override
    public void movePlayer(KeyCode keyCode) {
        try {
            Direction direction = Direction.valueOf(keyCode.toString());
            outgoingMessageProcessor.sendMessage(protocol.send().moving(direction));
            map.movePlayer(direction, playerName);
            view.reload(map.getMapParts());
            if (map.checkWinCondition()) {
                outgoingMessageProcessor.sendMessage(protocol.send().won());
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Moving player failed", e);
        }
    }

    /**
     * Method for sending a win message.
     */
    private void won() {
        if (isOwner) {
            LobbyOwnerController lobbyOwnerController = new LobbyOwnerController(stage, playerName, serverConnection, mapName);
            lobbyOwnerController.loadView();
            lobbyOwnerController.setMap(mapName);
            lobbyOwnerController.setSecondPlayerName(remotePlayerName);
        } else {
            new LobbySecondPlayerController(stage, remotePlayerName, playerName, mapName,
                    serverConnection).loadView();
        }
    }

    @Override
    public void quitMap() {
        outgoingMessageProcessor.sendMessage(protocol.send().quitMap());
    }

    @Override
    public void restartMap() {
        outgoingMessageProcessor.sendMessage(protocol.send().restartMap());
    }

    /**
     * Method for waiting for messages.
     */
    public void waitForCommands() {
        waitingForCommandsThread = new Thread(() -> {
            String message = incomingMessageProcessor.getMessage();
            while (message != null || !Thread.currentThread().isInterrupted()) {
                if (protocol.disconnected(message)) {
                    disconnected();
                    break;
                }
                MapProtocolIn in = protocol.get(message);
                if (in.youHaveLost()) {
                    Platform.runLater(() -> {
                        won();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", "You have lost").showAndWait();
                    });
                    break;
                }
                if (in.youHaveWon()) {
                    Platform.runLater(() -> {
                        won();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", "You have won").showAndWait();
                    });
                    break;
                }
                if (in.moveNexPlayer()) {
                    map.movePlayer(in.getDirectionToMoveOtherPlayer(), remotePlayerName);
                    Platform.runLater(() -> view.reload(map.getMapParts()));
                }
                if (in.restartMapRequest()) {
                    Platform.runLater(() -> {
                        Optional<ButtonType> result = DialogFactory.getConfirmDialog("Restarting map",
                                "Other player wants to restart the map.", "Do you agree?").showAndWait();
                        if (result.get() == ButtonType.OK) {
                            outgoingMessageProcessor.sendMessage(protocol.send().agreed());
                        } else {
                            outgoingMessageProcessor.sendMessage(protocol.send().disagreed());
                        }
                    });
                }
                if (in.agreed()) {
                    this.map = MapFactory.getInstance().getMap(mapName, playerNumber, remotePlayerNumber, playerName, remotePlayerName);
                    Platform.runLater(() -> view.reload(map.getMapParts()));
                }
                if (in.disagreed()) {
                    Platform.runLater(() -> DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Restarting map", "Other player refused to restart the map.").showAndWait());
                }
                if (in.playerHasLeft()) {
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, serverConnection, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Map", "Other player has left").showAndWait();
                    });
                    break;
                }
                if (in.youHaveLeft()) {
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, serverConnection, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game info", "You have left the game").showAndWait();
                    });
                    break;
                }

                message = incomingMessageProcessor.getMessage();
            }
        });
        waitingForCommandsThread.start();
    }
}
