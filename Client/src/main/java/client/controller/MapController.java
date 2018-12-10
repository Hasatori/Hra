package client.controller;

import javafx.scene.input.KeyCode;

/**
 * Controller class for playing a map.
 */
public interface MapController {

    /**
     * Method for moving a player on a map in a direction the player desired.
     * @param keyCode pressed button
     */
    void movePlayer(KeyCode keyCode);

    /**
     * Quits the map and ends the game.
     */
    void quitMap();

    /**
     * Quits the map and starts a new one with the same players.
     */
    void restartMap();
}
