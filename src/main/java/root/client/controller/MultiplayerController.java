package root.client.controller;

import javafx.stage.Stage;
import root.client.view.MultiplayerView;
import root.client.view.View;

import java.io.IOException;
import java.util.LinkedList;

public class MultiplayerController extends Controller {

    private View view;

    public MultiplayerController(Stage stage) throws IOException {
        super(stage);
        this.view = new MultiplayerView(this, new LinkedList<>());
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        this.stage.setScene(view);
        stage.show();
    }

    public void back() {
        try {
            new StartController(stage).loadView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadLobby(String lobbyName) {
        new LobbyController(stage,lobbyName).loadView();
    }

}
