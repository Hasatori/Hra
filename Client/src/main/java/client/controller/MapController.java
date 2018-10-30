package client.controller;

import javafx.scene.input.KeyCode;

public interface MapController {

    public void movePlayer(KeyCode keyCode);

    public void quitMap();

    public void restartMap();
}
