package controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import server.clientservices.ClientManager;
import server.Server;
import view.MainView;

import java.io.IOException;

public class MainController extends Controller {
    private MainView view;

    public MainController(Stage stage) {
        super(stage);
        try {
            this.view = new MainView(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientManager.getInstance().setController(this);
    }

    @Override
    public void loadView() {
        stage.setScene(view);
        stage.show();
    }

    public void startServer(Integer port) {
        Server.getInstance().setPort(port);
        new Thread(Server.getInstance()).start();
    }

    public void stopServer() {
        Server.getInstance().stopServer();
        ClientManager.getInstance().removeAllClients();
        updateMessages("Server stopped");
        updateClients();
    }

    public void updateClients() {
        view.fillClientsTable(ClientManager.getInstance().getClients());
    }
    public void updateMessages(String message){
       Platform.runLater(()-> view.message(message));
    }
}
