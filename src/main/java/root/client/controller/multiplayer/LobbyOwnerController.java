package root.client.controller.multiplayer;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import root.client.model.connection.InputReader;
import root.client.model.connection.OutputWritter;
import root.client.model.protocol.lobby.LobbyProtocol;
import root.client.model.protocol.lobby.LobbyProtocolIn;
import root.client.util.ResourceLoader;
import root.client.view.DialogFactory;
import root.client.view.LobbyOwnerView;

import java.io.IOException;

public class LobbyOwnerController extends ServerController {
    private final LobbyProtocol protocol;
    private LobbyOwnerView view;
    private String secondPlayerName;
    private boolean isFull = false;

    public LobbyOwnerController(Stage stage, String lobbyName, String playerName, InputReader incommingMessageProccessor, OutputWritter outgoingMessageProccessor) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        try {
            this.view = new LobbyOwnerView(this, ResourceLoader.getMultiplayerMaps(), playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.protocol = new LobbyProtocol();
    }

    @Override
    public void updateView() {

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
        LobbyProtocolIn in = protocol.get(incommingMessageProccessor.getMessage());
        if (in.playerConnected()) {
            this.secondPlayerName = in.getSecondPlayerName();
            DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby", "Player " + in.getSecondPlayerName() + " has connected");
            setSecondPlayerName(in.getSecondPlayerName());
            isFull = true;
        }
        if (in.playerHasLeft()) {
            DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Lobby", "Player " + in.getSecondPlayerName() + " has left");
            view.lobbyIsEmpty();
            isFull = false;
        }
    }

    private void setSecondPlayerName(String name) {
        view.setSecondPlayerName(name);
    }

    public void startGame(String mapName) {
        if (isFull) {
            outgoingMessageProccessor.sendMessage(protocol.send().startGame());
            new MultiplayerMapController(stage, mapName, 0, playerName, incommingMessageProccessor, outgoingMessageProccessor).loadView();
        }
    }

    public void kickOtherPlayer() {
        outgoingMessageProccessor.sendMessage(protocol.send().kickOtherPlayer());
        isFull = false;
        view.lobbyIsEmpty();
    }

}
