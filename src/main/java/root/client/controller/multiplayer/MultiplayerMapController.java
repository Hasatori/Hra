package root.client.controller.multiplayer;

import com.sun.javafx.scene.traversal.Direction;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import root.client.controller.MapController;
import root.client.controller.StartController;
import root.client.model.connection.InputReader;
import root.client.model.connection.OutputWritter;
import root.client.model.map.Map;
import root.client.model.protocol.map.MapProtocol;
import root.client.model.protocol.map.MapProtocolIn;
import root.client.view.DialogFactory;
import root.client.view.MapView;

import java.io.IOException;
import java.util.Optional;

public class MultiplayerMapController extends ServerController implements MapController {


    private Map map;
    private final MapProtocol protocol;
    private final String secondPlayerName;
    private final int playerNumber;
    private final String mapName;
    private MapView view;

    public MultiplayerMapController(Stage stage, String mapName, int playerNumber, String playerName, String secondPlayerName, InputReader incommingMessageProccessor, OutputWritter outgoingMessageProccessor) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        this.secondPlayerName = secondPlayerName;
        this.map = new Map(mapName, true, playerNumber, this.playerName, this.secondPlayerName);
        try {
            this.view = new MapView(this, map.getMapParts(), mapName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.protocol = new MapProtocol();
        this.mapName = mapName;
        this.playerNumber = playerNumber;
    }

    @Override
    public void updateView() {

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
            outgoingMessageProccessor.sendMessage(protocol.send().moving(direction));
            map.movePlayer(direction);
            view.reload(map.getMapParts());
            if (map.checkWinCondition()) {
                outgoingMessageProccessor.sendMessage(protocol.send().won());
                DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", "You have won").showAndWait();
                new StartController(stage).loadView();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quitMap() {
        outgoingMessageProccessor.sendMessage(protocol.send().quitMap());
    }

    @Override
    public void restartMap() {
        outgoingMessageProccessor.sendMessage(protocol.send().restartMap());
    }

    public void waitForCommands() {

        new Thread(() -> {
            String message = incommingMessageProccessor.getMessage();
            while (message != null) {
                MapProtocolIn in = protocol.get(message);
                if (in.youHaveLost()) {
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", this.playerName + " has lost").showAndWait();
                        try {
                            new StartController(stage).loadView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                }
                if (in.moveNexPlayer()) {
                    map.moverOtherPlayer(in.getDirectionToMoveOtherPlayer());
                    Platform.runLater(() -> view.reload(map.getMapParts()));
                }
                if (in.restartMapRequest()) {
                    Platform.runLater(() -> {
                        Optional<ButtonType> result = DialogFactory.getConfirmDialog("Restarting map", "Other player wants to restart the map.", "Do you agree?").showAndWait();
                        if (result.get() == ButtonType.OK) {
                            outgoingMessageProccessor.sendMessage(protocol.send().agreed());
                        } else {
                            outgoingMessageProccessor.sendMessage(protocol.send().disagreed());
                        }
                    });
                }
                if (in.agreed()) {
                    this.map = new Map(mapName, true, playerNumber, this.playerName, this.secondPlayerName);
                    Platform.runLater(() -> view.reload(map.getMapParts()));
                }
                if (in.disagreed()) {
                    Platform.runLater(() -> DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Restarting map", "Other player refused to restart the map.").showAndWait());
                }
                if (in.playerHasLeft()) {
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Map", "Other player has left").showAndWait();
                    });
                    break;
                }
                if (in.youHaveLeft()) {
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game info", "You have left the game").showAndWait();
                    });
                    break;
                }
                message = (String) incommingMessageProccessor.getMessage();
            }
        }).start();
    }
}
