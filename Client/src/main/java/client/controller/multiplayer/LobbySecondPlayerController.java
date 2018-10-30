package client.controller.multiplayer;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import client.model.connection.InputReader;
import client.model.connection.OutputWritter;
import client.model.protocol.lobby.LobbyProtocol;
import client.model.protocol.lobby.LobbyProtocolIn;
import client.util.ResourceLoader;
import client.view.DialogFactory;
import client.view.LobbySecondPlayerView;

@SuppressWarnings("restriction")
public class LobbySecondPlayerController extends ServerController {
    private final LobbyProtocol protocol;
    private final String ownerName;
    private final String secondPlayerName;
    private LobbySecondPlayerView view;
    private Stage stage;
    private String mapName;

    public LobbySecondPlayerController(Stage stage, String lobbyName, String ownerName, String playerName, String selectedMap, InputReader incommingMessageProccessor, OutputWritter outgoingMessageProccessor) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        this.stage = stage;
        this.ownerName = ownerName;
        this.secondPlayerName = playerName;
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
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName).loadView();
                        DialogFactory.getAlert(Alert.AlertType.WARNING, "Lobby", "You were kicked out of the lobby").showAndWait();
                    });
                    break;
                }
                if (in.setMap()) {
                    this.mapName = in.getMap();
                    Platform.runLater(() -> view.setMap(in.getMap()));
                }
                if (in.messageSent()) {
                	view.receiveLobbyMessage(message.replaceFirst(LobbyProtocol.messagePrefix + ":SENT MESSAGE-OWNER", ""), true);
                }
                if (in.start()) {
                    Platform.runLater(() -> new MultiplayerMapController(stage, mapName, 1, playerName, secondPlayerName, this.incommingMessageProccessor, outgoingMessageProccessor).loadView());
                    break;
                }
                if (in.playerHasLeft()) {
                    Platform.runLater(() -> {
                        new MultiplayerController(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName).loadView();
                    });
                    break;
                }
                message = incommingMessageProccessor.getMessage();
            }
        }).start();
    }

    public void leaveLobby() {
        outgoingMessageProccessor.sendMessage(protocol.send().leaveLobby());
    }
    
    public void sendLobbyMessage(String msg) {
        outgoingMessageProccessor.sendMessage(protocol.send().sendLobbyMessage("-SECPLAYER" + msg));
    }
    
    public String getPlayerName() {
    	return secondPlayerName;
    }
}
