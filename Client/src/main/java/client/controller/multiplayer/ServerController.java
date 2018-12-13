package client.controller.multiplayer;

import client.controller.StartController;
import client.model.connection.ServerConnection;
import client.view.DialogFactory;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import client.controller.Controller;
import client.model.connection.InputReader;
import client.model.connection.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Controller class for server.
 */
public abstract class ServerController extends Controller {
    protected final InputReader incomingMessageProcessor;
    protected final OutputWriter outgoingMessageProcessor;
    protected final String playerName;
    protected final ServerConnection serverConnection;
    private final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

    /**
     * @param stage            stage
     * @param serverConnection server connection
     * @param playerName       name of the player
     */
    public ServerController(Stage stage, ServerConnection serverConnection, String playerName) {
        super(stage);
        this.serverConnection = serverConnection;
        this.incomingMessageProcessor = serverConnection.getIncomingMessageProcessor();
        this.outgoingMessageProcessor = serverConnection.getOutgoingMessageProcessor();
        this.playerName = playerName;
    }

    @Override
    public void loadView() {

    }

    /**
     * Action which is initiated when server terminates. It provides warning message and moves player to initial view.
     */
    protected void disconnected() {
        Platform.runLater(() -> {
            try {
                new StartController(stage).loadView();
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Connection lost",
                        "Connection to the server has been lost").showAndWait();

            } catch (IOException e) {
                LOGGER.error("Error while loading start controller {}", e);
            }
        });

    }
}
