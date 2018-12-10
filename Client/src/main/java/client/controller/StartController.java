package client.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import client.controller.multiplayer.MultiplayerController;
import client.controller.singleplayer.SingleplayerController;
import client.model.connection.ServerConnection;
import client.model.protocol.general.GeneralProtocol;
import client.view.DialogFactory;
import client.view.StartView;

/**
 * Controller class for the start screen.
 * Handles player's choices, either to play singleplayer or multiplayer games.
 */
@SuppressWarnings("restriction")
public class StartController extends Controller {

    private final Logger LOGGER = LoggerFactory.getLogger(StartController.class);
    private final GeneralProtocol protocol;
    private StartView view;
    private ServerConnection serverConnection;

    /**
     * @param stage stage
     * @throws IOException error in constructing StartController
     */
    public StartController(Stage stage) throws IOException {
        super(stage);
        this.view = new StartView(this);
        this.protocol = new GeneralProtocol();
    }

    /**
     * Redirects player to offline mode games.
     */
    public void loadSingleplayer() {
        new SingleplayerController(stage).loadView();
    }

    @Override
    public void loadView() {
        this.stage.setScene(this.view);
        this.stage.show();
    }

    /**
     * Shows alert, that server is not available.
     */
    private void serverDisconnected() {
        DialogFactory.getAlert(Alert.AlertType.ERROR, "Server connection", "Connection with the server can not be established. Please try it later.").showAndWait();
    }

    /**
     * Shows dialog for player to set their name.
     * After setting the player's name, this method will try to connect to server on port 8002.
     */
    public void loadMultiplayer() {
        TextInputDialog dialog = DialogFactory.getTextInputDialog("", "Setting name", "Fill your name please");
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (dialog.getEditor().getText().equals("")) {
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting name", "Name must be filled").showAndWait();
            } else if (dialog.getEditor().getText().contains("|")){
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting name", "Name cannot contain |").showAndWait();
            } else {
                String filledName = dialog.getEditor().getText();
                this.serverConnection = new ServerConnection(filledName,8002);
                if (!this.serverConnection.isConnected()) {
                    ae.consume(); //not valid
                    this.serverDisconnected();
                } else {
                    String message = serverConnection.getIncomingMessageProcessor().getMessage();
                    if (protocol.get(message).duplicateUserName()) {
                        ae.consume(); //not valid
                        serverConnection.disconnect();
                        DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting name", "This name is already used by someone. Please try to fill a different name.").showAndWait();
                    } else if (protocol.get(message).wasLoginOk()) {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Connecting", "Connection successful").showAndWait();
                    }
                }
            }
        });
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            LOGGER.info("Player name is:{}", name);
            new MultiplayerController(this.stage, serverConnection, name).loadView();
        });
    }
}
