package client.controller.multiplayer;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import client.model.connection.InputReader;
import client.model.connection.OutputWriter;
import client.model.protocol.lobby.LobbyProtocol;
import client.model.protocol.lobby.LobbyProtocolIn;
import client.util.ResourceLoader;
import client.view.DialogFactory;
import client.view.LobbyOwnerView;

@SuppressWarnings("restriction")
public class LobbyOwnerController extends ServerController {
    private final LobbyProtocol protocol;
    private LobbyOwnerView view;
    private String secondPlayerName;
    private boolean isFull = false;

    public LobbyOwnerController(Stage stage, String playerName, InputReader incommingMessageProccessor, OutputWriter outgoingMessageProccessor) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        try {
            this.view = new LobbyOwnerView(this, ResourceLoader.getMultiplayerMaps(), playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.protocol = new LobbyProtocol();
    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
        waitForMessage();
    }

    public void setMap(String mapName) {
        outgoingMessageProccessor.sendMessage(protocol.send().setMap(mapName));
    }

    private void waitForMessage() {
        new Thread(() -> {
            String message = incommingMessageProccessor.getMessage();
            while (message != null) {
                LobbyProtocolIn in = protocol.get(message);
                if (in.playerConnected()) {
                    this.secondPlayerName = in.getSecondPlayerName();
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby", "Player " + in.getSecondPlayerName() + " has connected").showAndWait();
                        setSecondPlayerName(in.getSecondPlayerName());
                    });
                    isFull = true;
                }
                if (in.playerHasLeft()) {
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby", "Player " + in.getSecondPlayerName() + " has left").showAndWait();
                        view.lobbyIsEmpty();
                    });
                    isFull = false;
                }
                if (in.messageSent()) {
                    view.receiveLobbyMessage(message.replaceFirst(LobbyProtocol.MSG_PREFIX + ":SENT MESSAGE-SECPLAYER", ""), false, true);
                }
                if (in.lobbyDeleted()) {
                    Platform.runLater(() ->
                            new MultiplayerController(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName).loadView());
                    break;
                }
                if (in.start()) {
                    break;
                }
                message = incommingMessageProccessor.getMessage();
            }
        }).start();
    }

    private void setSecondPlayerName(String name) {
        view.setSecondPlayerName(name);
    }

    public void startGame(String mapName) {
        if (isFull) {
            outgoingMessageProccessor.sendMessage(protocol.send().startGame());
            new MultiplayerMapController(stage, mapName, 0, playerName, secondPlayerName, 1, incommingMessageProccessor, outgoingMessageProccessor,true).loadView();
        }
    }

    public void kickOtherPlayer() {
        outgoingMessageProccessor.sendMessage(protocol.send().kickOtherPlayer());
        isFull = false;
        view.lobbyIsEmpty();
    }

    public void deleteLobby() {
        outgoingMessageProccessor.sendMessage(protocol.send().destroyLobby());
    }

    public void sendLobbyMessage(String msg) {
        outgoingMessageProccessor.sendMessage(protocol.send().sendLobbyMessage("-OWNER" + msg));
    }
}
