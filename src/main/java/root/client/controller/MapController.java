package root.client.controller;

import javafx.scene.input.KeyCode;

import javax.swing.*;

public interface MapController {

    public void movePlayer(KeyCode keyCode);

    public void quitMap();

    public void restartMap();
}
