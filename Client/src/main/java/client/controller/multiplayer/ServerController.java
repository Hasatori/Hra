package client.controller.multiplayer;

import javafx.stage.Stage;
import client.controller.Controller;
import client.model.connection.InputReader;
import client.model.connection.OutputWritter;

public abstract class ServerController extends Controller {
    protected final InputReader incommingMessageProccessor;
    protected final OutputWritter outgoingMessageProccessor;
    protected final String playerName;

    public ServerController(Stage stage, InputReader incomingMessageProcessor, OutputWritter outgoingMessageProcessor, String playerName) {
        super(stage);
        this.incommingMessageProccessor = incomingMessageProcessor;
        this.outgoingMessageProccessor = outgoingMessageProcessor;
        this.playerName = playerName;
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {

    }

}
