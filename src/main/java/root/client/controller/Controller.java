package root.client.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.UnmodifiableArrayList;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import root.client.model.map.*;
import root.client.view.DialogView;
import root.client.view.MapView;
import root.client.view.StartView;
import root.server.main.Server;

import java.io.IOException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Map map;
    private ServerConnection serverConnection;


    public Controller(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(a -> {
            a.consume();
            Optional<ButtonType> result = new DialogView("Quiting application", "Do you really want to close the application?", "").showAndWait();
            if (result.get() == ButtonType.OK) {
                Platform.exit();
            } else {
            }
        });
        openConnection();
    }

    public void loadScene(Scene newScene) {
        this.scene = scene;
        this.stage.setScene(newScene);
        stage.setTitle("");
        stage.show();
    }

    public void loadMap(String name) {
        if (map == null) {
            this.map = new Map(name);
        }
        try {
            this.scene = new MapView(this, map.getMapParts(), map.getName());
            this.stage.setScene(this.scene);
            stage.setTitle(map.getName() + " " + ServerConnection.IDENTIFIER);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToMap(String name) {
        map = null;
        loadMap(name);
    }

    public void reloadScene() {
    }

    public void movePlayer(KeyCode keyCode) {
        scene = new Scene(new Pane());
        List<List<MapPart>> mapParts = map.getMapParts();
        map.movePlayer(keyCode);
        loadMap(map.getName());

    }

    public void joinLobby(String name) {
        map = null;
        System.out.println("Joining lobby " + name);
        sendMessage("Wanna join lobby " + name);
        loadMap("level1M");
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

    public void sendMap() {
    }
}
