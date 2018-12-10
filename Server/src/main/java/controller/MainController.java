package controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.clientservices.ClientManager;
import server.Server;
import view.MainView;

import java.io.IOException;

/**
 * Controller class for main view for server.
 */
public class MainController extends Controller {

    private final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private MainView view;

    public MainController(Stage stage) {
        super(stage);
        try {
            this.view = new MainView(this);
        } catch (IOException e) {
            LOGGER.error("Failed to create MainView", e);
        }
        ClientManager.getInstance().setController(this);
    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
    }

    /**
     * Starts server at given given port number.
     * @param port port number
     */
    public void startServer(Integer port) {
        Server.getInstance().setPort(port);
        new Thread(Server.getInstance()).start();
    }

    /**
     * Stops a running server.
     */
    public void stopServer() {
        Server.getInstance().stopServer();
        ClientManager.getInstance().removeAllClients();
        updateMessages("Server stopped");
        updateClients();
    }

    /**
     * Updates list of active clients.
     */
    public void updateClients() {
        view.fillClientsTable(ClientManager.getInstance().getClients());
    }

    /**
     * Updates a list of sent messages.
     * @param message message to show
     */
    public void updateMessages(String message){
        Platform.runLater(()-> view.message(message));
    }
}
