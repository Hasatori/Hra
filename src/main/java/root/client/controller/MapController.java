package root.client.controller;

import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import root.client.model.map.*;
import root.client.view.DialogFactory;
import root.client.view.MapView;

import java.io.IOException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MapController extends Controller {

    private Map map;
    private MapView view;

    private ServerConnection serverConnection;
    private final Logger LOGGER = LoggerFactory.getLogger(MapController.class);

    public MapController(Stage stage) {
        super(stage);
        this.map = new Map("level1");
        try {
            this.view = new MapView(this, this.map.getMapParts(), this.map.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // openConnection();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        this.stage.setScene(this.view);
        this.stage.show();
    }


    public void switchToMap(String name) {
        map = new Map(name);
        loadView();

    }

    public void reloadScene() {
    }

    public void movePlayer(KeyCode keyCode) {
        try {
            Direction direction = Direction.valueOf(keyCode.toString());
            map.movePlayer(direction);
            view.reload();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void joinLobby(String name) {
        map = null;
        System.out.println("Joining lobby " + name);
        sendMessage("Wanna join lobby " + name);

    }

    public void restartMap() {
        System.out.println("Restarting map " + map.getName());
        switchToMap(map.getName());
    }

    private void openConnection() {

        new Thread(this.serverConnection = new ServerConnection(this)).start();
    }

    public void sendMessage(String sendMessage) {
        serverConnection.sendMessage(sendMessage);
    }

    public void processMessage(String json) throws IOException {
    }


    public List<String> getOpenedLobbies() {
        String reply = serverConnection.sendMessage("Get me opened lobbies");
        List<String> lobbies = new LinkedList<>();
        lobbies.add(reply.split(",")[0]);
        lobbies.add(reply.split(",")[1]);
        return lobbies;
    }

    public void quitMap() {
        Optional<ButtonType> result = DialogFactory.getConfirmDialog("Quiting game", "Are you sure?", "You are about to quit the game.").showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                new StartController(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
