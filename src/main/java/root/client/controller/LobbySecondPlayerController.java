package root.client.controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import root.client.model.connection.IncomingMessageProcessor;
import root.client.model.connection.OutgoingMessageProcessor;
import root.client.util.ResourceLoader;
import root.client.view.LobbySecondPlayerView;
import root.client.view.View;

import java.io.IOException;

public class LobbySecondPlayerController extends Controller {
    private final String playerName;
    private LobbySecondPlayerView view;
    private Stage stage;
    private IncomingMessageProcessor incommingMessageProccessor;
    private OutgoingMessageProcessor outgoingMessageProccessor;

    public LobbySecondPlayerController(Stage stage, String lobbyName, String ownerName, String playerName, String selectedMap, IncomingMessageProcessor incommingMessageProccessor, OutgoingMessageProcessor outgoingMessageProccessor) {
        super(stage);
        this.incommingMessageProccessor = incommingMessageProccessor;
        this.outgoingMessageProccessor = outgoingMessageProccessor;
        this.stage = stage;
        try {
            this.view = new LobbySecondPlayerView(this, ResourceLoader.getMultiplayerMaps());
        } catch (IOException e) {
            e.printStackTrace();
        }
        view.setOwnerName(ownerName);
        view.setMap(selectedMap);
        this.playerName = playerName;
        view.setPlayerName(playerName);
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
            String message;
            while ((message = (String) incommingMessageProccessor.processMessage()) != null) {
                if (message.contains("SETMAP")) {
                    final String mapName = message.split(" ")[1];
                    Platform.runLater(() -> view.setMap(mapName));
                }
                if (message.contains("START")) {
                    final String mapName = message.split(" ")[1];
                    Platform.runLater(() -> new MultiplayerMapController(stage, mapName, 1, playerName, incommingMessageProccessor, outgoingMessageProccessor).loadView());
                    break;
                }
            }
        }).start();
    }
}
