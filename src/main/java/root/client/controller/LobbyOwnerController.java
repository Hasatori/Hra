package root.client.controller;

import javafx.stage.Stage;
import root.client.model.connection.IncomingMessageProcessor;
import root.client.model.connection.OutgoingMessageProcessor;
import root.client.util.ResourceLoader;
import root.client.view.LobbyOwnerView;

import java.io.IOException;

public class LobbyOwnerController extends Controller {
    private final String playerName;
    private LobbyOwnerView view;
    private IncomingMessageProcessor incommingMessageProccessor;
    private OutgoingMessageProcessor outgoingMessageProccessor;
    private boolean isSecondPlayer;

    public LobbyOwnerController(Stage stage, String lobbyName, String playerName, IncomingMessageProcessor incommingMessageProccessor, OutgoingMessageProcessor outgoingMessageProccessor) {
        super(stage);
        this.incommingMessageProccessor = incommingMessageProccessor;
        this.outgoingMessageProccessor = outgoingMessageProccessor;
        try {
            this.view = new LobbyOwnerView(this, ResourceLoader.getMultiplayerMaps(), playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.playerName = playerName;
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
        waitForPlayer();
    }

    public void setMap(String mapName) {
        outgoingMessageProccessor.sendMessage("LOBBY:SETMAP " + mapName);
    }

    private void waitForPlayer() {
        String message = (String) incommingMessageProccessor.processMessage();
        setSecondPlayerName(message.replace("CONNECTED PLAYER", ""));
    }

    private void setSecondPlayerName(String name) {
        view.setSecondPlayerName(name);
    }

    public void startGame(String mapName) {
        outgoingMessageProccessor.sendMessage("LOBBY:START " + mapName);
        new MultiplayerMapController(stage, mapName, 0,playerName,incommingMessageProccessor,outgoingMessageProccessor).loadView();
    }
}
