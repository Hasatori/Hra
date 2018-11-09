package client.controller.singleplayer;

import javafx.stage.Stage;
import client.controller.Controller;
import client.util.ResourceLoader;
import client.view.SingleplayerView;

import java.io.IOException;

public class SingleplayerController extends Controller {
    private final Stage stage;
    private SingleplayerView view;

    public SingleplayerController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @Override
    public void loadView() {
        try {
            this.view = new SingleplayerView(this,ResourceLoader.getSingleplayerMaps());
            stage.setScene(view);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame(String mapName, String playerName) {
            new SingleplayerMapController(stage, mapName, playerName).loadView();
    }
}
