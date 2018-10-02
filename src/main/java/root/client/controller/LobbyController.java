package root.client.controller;

import javafx.stage.Stage;
import root.client.view.LobbyView;
import root.client.view.View;


import java.io.IOException;

public class LobbyController extends Controller {
    private LobbyView view;

    public LobbyController(Stage stage, String lobbyName) {
        super(stage);
        try {
            this.view = new LobbyView(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
    }


}
