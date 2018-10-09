package root.client.controller.multiplayer;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import root.client.controller.StartController;
import root.client.model.connection.InputReader;
import root.client.model.connection.OutputWritter;
import root.client.model.protocol.general.GeneralProtocol;
import root.client.model.protocol.general.GeneralProtocolIn;
import root.client.model.protocol.lobby.LobbyProtocol;
import root.client.util.ResourceLoader;
import root.client.view.DialogFactory;
import root.client.view.MultiplayerView;
import root.client.view.View;


import java.io.IOException;
import java.util.List;

public class MultiplayerController extends ServerController {
    private final Logger LOGGER = LoggerFactory.getLogger(MultiplayerController.class);
    private final GeneralProtocol protocol;
    private View view;

    public MultiplayerController(Stage stage, InputReader incommingMessageProccessor, OutputWritter outgoingMessageProccessor, String playerName) {
        super(stage, incommingMessageProccessor, outgoingMessageProccessor, playerName);
        this.protocol = new GeneralProtocol();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        try {
            this.view = new MultiplayerView(this, loadLobbies());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stage.setScene(view);
        stage.show();
    }

    public List<String> loadLobbies() {
        outgoingMessageProccessor.sendMessage(protocol.send().getLobbies());
        List<String> result = this.protocol.get(incommingMessageProccessor.getMessage()).getLobbies();
        return result;
    }

    public void back() {
        try {
            new StartController(stage).loadView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createLobby(String lobbyName) {
        outgoingMessageProccessor.sendMessage(protocol.send().createLobby(lobbyName));
        new LobbyOwnerController(stage, lobbyName, playerName, incommingMessageProccessor, outgoingMessageProccessor).loadView();
    }

    public void joinLobby(String lobbyName) {
        outgoingMessageProccessor.sendMessage(protocol.send().joinLobby(lobbyName));
        GeneralProtocolIn in = protocol.get(incommingMessageProccessor.getMessage());
        if (in.lobbyFull()) {
            DialogFactory.getAlert(Alert.AlertType.ERROR, "Connecting lobby", "Lobby is full.").showAndWait();
        } else {
            String[] parts = in.connectedToLobby();
            new LobbySecondPlayerController(stage, parts[0], parts[1], playerName, parts[2], incommingMessageProccessor, outgoingMessageProccessor).loadView();
        }
    }


}
