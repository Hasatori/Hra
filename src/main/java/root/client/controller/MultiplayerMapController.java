package root.client.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.scene.traversal.Direction;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import root.client.model.connection.IncomingMessageProcessor;
import root.client.model.connection.OutgoingMessageProcessor;
import root.client.model.map.Map;
import root.client.model.map.MapPart;
import root.client.view.DialogFactory;
import root.client.view.MapView;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MultiplayerMapController extends Controller implements MapController {


    private final Map map;
    private final String playerName;
    private MapView view;
    private IncomingMessageProcessor incommingMessageProccessor;
    private OutgoingMessageProcessor outgoingMessageProccessor;

    public MultiplayerMapController(Stage stage, String mapName, int playerNumber, String playerName, IncomingMessageProcessor incommingMessageProccessor, OutgoingMessageProcessor outgoingMessageProccessor) {
        super(stage);
        this.incommingMessageProccessor = incommingMessageProccessor;
        this.outgoingMessageProccessor = outgoingMessageProccessor;
        this.map = new Map(mapName, true, playerNumber, playerName);
        try {
            this.view = new MapView(this, map.getMapParts(), mapName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.playerName = playerName;
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
            outgoingMessageProccessor.sendMessage("MAP:" + direction);
            map.movePlayer(direction);

            view.reload(map.getMapParts());
            if (map.checkWinCondition()) {
                // LOGGER.info("Game ended.{} has won", playerName);
                outgoingMessageProccessor.sendMessage("MAP:WON");
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
            String message = (String) incommingMessageProccessor.processMessage();
            while (message != null) {
                System.out.println(message);
                if (message.equals("LOST")) {
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", "You have lost").showAndWait();
                        try {
                            new StartController(stage).loadView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                } else if (Arrays.asList(Direction.values()).contains(Direction.valueOf(message))) {
                    map.moverOtherPlayer(Direction.valueOf(message));
                    Platform.runLater(() -> view.reload(map.getMapParts()));
                }
                message = (String) incommingMessageProccessor.processMessage();
            }
        }).start();
    }
}
