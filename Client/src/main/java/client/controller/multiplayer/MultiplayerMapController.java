package client.controller.multiplayer;

import client.model.map.Map;
import client.model.map.MapFactory;
import com.sun.javafx.scene.traversal.Direction;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import client.controller.MapController;
import client.controller.StartController;
import client.model.connection.InputReader;
import client.model.connection.OutputWriter;
import client.model.protocol.map.MapProtocol;
import client.model.protocol.map.MapProtocolIn;
import client.view.DialogFactory;
import client.view.MapView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class MultiplayerMapController extends ServerController implements MapController {

    private final Logger LOGGER = LoggerFactory.getLogger(MultiplayerMapController.class);
    private Map map;
    private final MapProtocol protocol;
    private final String remotePlayerName;
    private final int playerNumber, remotePlayerNumber;
    private final String mapName;
    private MapView view;
    private boolean isOwner;

    public MultiplayerMapController(Stage stage, String mapName, int playerNumber, String playerName, String remotePlayerName, int remotePlayerNumber, InputReader incommingMessageProccessor, OutputWriter outgoingMessageProccessor,boolean isOwner) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        this.remotePlayerName = remotePlayerName;
        this.map = MapFactory.getInstance().getMap(mapName, playerNumber, remotePlayerNumber, playerName, remotePlayerName);
        this.isOwner=isOwner;
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
                DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", "You have won").showAndWait();
                if (isOwner){
                    new LobbyOwnerController(stage, playerName, incomingMessageProcessor, outgoingMessageProcessor).loadView();
                }else{
                    new LobbySecondPlayerController(stage, playerName, remotePlayerName, mapName,
                            incomingMessageProcessor, outgoingMessageProcessor).loadView();
                }

            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Moving player failed", e);
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

    public void waitForCommands() {

        new Thread(() -> {
            String message = incomingMessageProcessor.getMessage();
            while (message != null) {
                MapProtocolIn in = protocol.get(message);
                if (in.youHaveLost()) {
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", this.playerName + " has lost").showAndWait();
                        if (isOwner){
                            new LobbyOwnerController(stage, playerName, incomingMessageProcessor, outgoingMessageProcessor).loadView();
                        }else{
                            new LobbySecondPlayerController(stage, playerName, remotePlayerName, mapName,
                                    incomingMessageProcessor, outgoingMessageProcessor).loadView();
                        }
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
                        new MultiplayerController(stage, incomingMessageProcessor, outgoingMessageProcessor, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Map", "Other player has left").showAndWait();
                    });
                    break;
                }
                if (in.youHaveLeft()) {
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, incomingMessageProcessor, outgoingMessageProcessor, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game info", "You have left the game").showAndWait();
                    });
                    break;
                }
                message = incomingMessageProcessor.getMessage();
            }
        }).start();
    }
}
