package root.client.controller;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import root.client.util.ResourceLoader;
import root.client.view.DialogFactory;
import root.client.view.SingleplayerView;

import java.io.IOException;

public class SingleplayerController extends Controller {
    private final Stage stage;
    private SingleplayerView view;

    public SingleplayerController(Stage stage) {
        super(stage);
        this.stage = stage;
    }

    @Override
    public void updateView() {

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
