package client.controller.multiplayer;

import client.model.connection.ServerConnection;
import javafx.stage.Stage;
import client.controller.Controller;
import client.model.connection.InputReader;
import client.model.connection.OutputWriter;

public abstract class ServerController extends Controller {
    protected final InputReader incomingMessageProcessor;
    protected final OutputWriter outgoingMessageProcessor;
    protected final String playerName;
    protected final ServerConnection serverConnection;

    public ServerController(Stage stage, ServerConnection serverConnection, String playerName) {
        super(stage);
        this.serverConnection = serverConnection;
        this.incomingMessageProcessor = serverConnection.getIncommingMessageProccessor();
        this.outgoingMessageProcessor = serverConnection.getOutgoingMessageProccessor();
        this.playerName = playerName;

    }

    @Override
    public void loadView() {

    }
}
