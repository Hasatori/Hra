package client.controller;

import javafx.scene.input.KeyCode;

public interface MapController {

    void movePlayer(KeyCode keyCode);

    void quitMap();

    void restartMap();
}
