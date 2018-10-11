package root.client.controller.multiplayer;

import com.sun.javafx.scene.traversal.Direction;
import javafx.application.Platform;
import javafx.scene.control.Alert;
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

public class MultiplayerMapController extends ServerController implements MapController {


    private final Map map;
    private final MapProtocol protocol;
    private final String secondPlayerName;
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

    }

    @Override
    public void restartMap() {

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
                } else if (in.moveNexPlayer()) {
                    map.moverOtherPlayer(in.getDirectionToMoveOtherPlayer());
                    Platform.runLater(() -> view.reload(map.getMapParts()));
                }
                message = (String) incommingMessageProccessor.getMessage();
            }
        }).start();
    }
}
