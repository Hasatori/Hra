package client.controller.singleplayer;

import client.controller.StartController;
import javafx.stage.Stage;
import client.controller.Controller;
import client.util.ResourceLoader;
import client.view.SingleplayerView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SingleplayerController extends Controller {

    private final Logger LOGGER = LoggerFactory.getLogger(SingleplayerController.class);
    private final Stage stage;
    private SingleplayerView view;

    public SingleplayerController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @Override
    public void loadView() {
        try {
            this.view = new SingleplayerView(this, ResourceLoader.getSingleplayerMaps());
            stage.setScene(view);
            stage.show();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void startGame(String mapName, String playerName) {
        new SingleplayerMapController(stage, mapName, playerName).loadView();
    }

    public void backToMenu() {
        try {
            new StartController(stage).loadView();
        } catch (IOException e) {
            LOGGER.error("Error while loading view {}", e);
        }
    }
}
