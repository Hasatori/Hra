package root.client.controller;

import javafx.stage.Stage;
import root.client.view.StartView;

import java.io.IOException;

public class StartController extends Controller {

    private StartView view;

    public StartController(Stage stage) throws IOException {
        super(stage);
        this.view = new StartView(this);
        this.loadView();
    }

    public void loadMultiplayer() {
        try {
            new MultiplayerController(this.stage).loadView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(){
            new SingleplayerMapController(this.stage,"level1","Oldřich").loadView();
    }
    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        this.stage.setScene(this.view);
        this.stage.show();
    }
}
