package root.client.controller.multiplayer;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import root.client.model.connection.InputReader;
import root.client.model.connection.OutputWritter;
import root.client.model.protocol.lobby.LobbyProtocol;
import root.client.model.protocol.lobby.LobbyProtocolIn;
import root.client.util.ResourceLoader;
import root.client.view.DialogFactory;
import root.client.view.LobbySecondPlayerView;


import java.io.IOException;

public class LobbySecondPlayerController extends ServerController {
    private final LobbyProtocol protocol;
    private LobbySecondPlayerView view;
    private Stage stage;
    private InputReader incommingMessageProccessor;
    private OutputWritter outgoingMessageProccessor;
    private String mapName;

    public LobbySecondPlayerController(Stage stage, String lobbyName, String ownerName, String playerName, String selectedMap, InputReader incommingMessageProccessor, OutputWritter outgoingMessageProccessor) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        this.stage = stage;
        try {
            this.view = new LobbySecondPlayerView(this, ResourceLoader.getMultiplayerMaps());
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setOwnerName(ownerName);
        this.mapName = selectedMap;
        view.setMap(selectedMap);
        view.setPlayerName(playerName);
        this.protocol = new LobbyProtocol();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
        waitForMessages();
    }

    private void waitForMessages() {
        new Thread(() -> {
            String message = incommingMessageProccessor.getMessage();

            while (message != null) {
                LobbyProtocolIn in = protocol.get(message);
                if (in.kicked()) {
                    new MultiplayerController(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName).loadView();
                    DialogFactory.getAlert(Alert.AlertType.WARNING, "Lobby", "You were kicked of the lobby");
                }
                if (in.setMap()) {
                    this.mapName = in.getMap();
                    Platform.runLater(() -> view.setMap(in.getMap()));
                }
                if (in.start()) {
                    Platform.runLater(() -> new MultiplayerMapController(stage, mapName, 1, playerName, incommingMessageProccessor, outgoingMessageProccessor).loadView());
                    break;
                }
            }
        }).start();
    }
}
