package client.controller.multiplayer;

import client.model.connection.ServerConnection;
import javafx.stage.Stage;
import client.controller.Controller;
import client.model.connection.InputReader;
import client.model.connection.OutputWriter;

public abstract class ServerController extends Controller {
    protected final InputReader incommingMessageProccessor;
    protected final OutputWriter outgoingMessageProccessor;
    protected final String playerName;
    protected final ServerConnection serverConnection;


    public ServerController(Stage stage, ServerConnection serverConnection, String playerName) {
        super(stage);
        this.serverConnection=serverConnection;
        this.incommingMessageProccessor = serverConnection.getIncommingMessageProccessor();
        this.outgoingMessageProccessor = serverConnection.getOutgoingMessageProccessor();
        this.playerName = playerName;

    }

    @Override
    public void loadView() {

    }
}
