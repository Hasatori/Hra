package client.controller.singleplayer;

import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import client.controller.Controller;
import client.controller.MapController;
import client.controller.StartController;
import client.model.map.*;
import client.view.DialogFactory;
import client.view.MapView;

import java.io.IOException;

import java.util.Optional;

public class SingleplayerMapController extends Controller implements MapController {

    private Map map;
    private MapView view;
    private String mapName;
    private String playerName;
    private final Logger LOGGER = LoggerFactory.getLogger(SingleplayerMapController.class);

    public SingleplayerMapController(Stage stage, String mapName, String playerName) {
        super(stage);
        this.mapName = mapName;
        this.playerName = playerName;
        createMap();
    }

    private void createMap() {
        this.map = new Map(mapName, false, 0, playerName);
        try {
            this.view = new MapView(this, this.map.getMapParts(), this.map.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        this.stage.setScene(this.view);
        this.stage.show();
    }

    public void reloadScene() {
    }

    @Override
    public void movePlayer(KeyCode keyCode) {
        try {
            Direction direction = Direction.valueOf(keyCode.toString());
            map.movePlayer(direction);
            stage.setScene(new MapView(this, map.getMapParts(), map.getName()));
            this.mapName = map.getName();
            if (map.checkWinCondition()) {
                LOGGER.info("Game ended.{} has won", playerName);
                DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Game ended", "You have won").showAndWait();
                new StartController(stage).loadView();
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restartMap() {
        createMap();
        loadView();
    }

    public void quitMap() {
        Optional<ButtonType> result = DialogFactory.getConfirmDialog("Quiting game", "Are you sure?", "You are about to quit the game.").showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                new StartController(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
