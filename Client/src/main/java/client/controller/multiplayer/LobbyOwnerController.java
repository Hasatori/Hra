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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class LobbyOwnerController extends ServerController {

    private final Logger LOGGER = LoggerFactory.getLogger(LobbyOwnerController.class);
    private final LobbyProtocol protocol;
    private LobbyOwnerView view;
    private String secondPlayerName;
    private boolean isFull = false;

    public LobbyOwnerController(Stage stage, String playerName, InputReader incomingMessageProcessor, OutputWriter outgoingMessageProcessor) {
        super(stage, incomingMessageProcessor, outgoingMessageProcessor, playerName);
        try {
            this.view = new LobbyOwnerView(this, ResourceLoader.getMultiplayerMaps(), playerName);
        } catch (IOException e) {
            LOGGER.error("Failed to create LobbyOwnerView", e);
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
        outgoingMessageProcessor.sendMessage(protocol.send().setMap(mapName));
    }

    private void waitForMessage() {
        new Thread(() -> {
            String message = incomingMessageProcessor.getMessage();
            while (message != null) {
                LobbyProtocolIn in = protocol.get(message);
                if (in.playerConnected()) {
                    this.secondPlayerName = in.getSecondPlayerName();
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby",
                                "Player " + in.getSecondPlayerName() + " has connected").showAndWait();
                        setSecondPlayerName(in.getSecondPlayerName());
                    });
                    isFull = true;
                }
                if (in.playerHasLeft()) {
                    Platform.runLater(() -> {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby",
                                "Player " + in.getSecondPlayerName() + " has left").showAndWait();
                        view.lobbyIsEmpty();
                    });
                    isFull = false;
                }
                if (in.messageSent()) {
                    view.receiveLobbyMessage(message.replaceFirst(LobbyProtocol.MSG_PREFIX + ":SENT MESSAGE-SECPLAYER", ""), false, true);
                }
                if (in.lobbyDeleted()) {
                    Platform.runLater(() ->
                            new MultiplayerController(stage, incomingMessageProcessor, outgoingMessageProcessor, playerName).loadView());
                    break;
                }
                if (in.start()) {
                    break;
                }
                message = incomingMessageProcessor.getMessage();
            }
        }).start();
    }

    private void setSecondPlayerName(String name) {
        view.setSecondPlayerName(name);
    }

    public void startGame(String mapName) {
        if (isFull) {
            outgoingMessageProcessor.sendMessage(protocol.send().startGame());
            new MultiplayerMapController(stage, mapName, 0, playerName,
                    secondPlayerName, 1, incomingMessageProcessor, outgoingMessageProcessor,true).loadView();
        }
    }

    public void kickOtherPlayer() {
        outgoingMessageProcessor.sendMessage(protocol.send().kickOtherPlayer());
        isFull = false;
        view.lobbyIsEmpty();
    }

    public void deleteLobby() {
        outgoingMessageProcessor.sendMessage(protocol.send().destroyLobby());
    }

    public void sendLobbyMessage(String msg) {
        outgoingMessageProcessor.sendMessage(protocol.send().sendLobbyMessage("-OWNER" + msg));
    }
}
