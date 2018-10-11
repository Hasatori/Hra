package root.client.controller.multiplayer;

import javafx.stage.Stage;
import root.client.controller.Controller;
import root.client.model.connection.InputReader;
import root.client.model.connection.OutputWritter;

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
