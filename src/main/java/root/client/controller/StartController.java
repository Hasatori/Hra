package root.client.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import root.client.controller.multiplayer.MultiplayerController;
import root.client.controller.singleplayer.SingleplayerController;
import root.client.model.connection.ServerConnection;
import root.client.model.protocol.general.GeneralProtocol;
import root.client.view.DialogFactory;
import root.client.view.StartView;

import java.io.IOException;
import java.util.Optional;

public class StartController extends Controller {
    private final Logger LOGGER = LoggerFactory.getLogger(StartController.class);
    private final GeneralProtocol protocol;
    private StartView view;
    private ServerConnection serverConnection;
    private String playerName;

    public StartController(Stage stage) throws IOException {
        super(stage);
        this.view = new StartView(this);
        this.protocol = new GeneralProtocol();
    }

    public void loadMultiplayer() {
        setNameAndTryToConnect();
    }

    public void loadSingleplayer() {
        new SingleplayerController(stage).loadView();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void loadView() {
        this.stage.setScene(this.view);
        this.stage.show();
    }

    private void serverDisconnected() {
        DialogFactory.getAlert(Alert.AlertType.ERROR, "Server connection", "Connection with the server can not be established. Please try it later.").showAndWait();
    }

    private void setNameAndTryToConnect() {
        TextInputDialog dialog = DialogFactory.getTextInputDialog("", "Setting name", "Fill your name please");
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (dialog.getEditor().getText().equals("")) {
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting name", "Name must be filled").showAndWait();
            } else {
                String filledName = dialog.getEditor().getText();
                this.serverConnection = new ServerConnection(filledName);
                if (!this.serverConnection.isConnected()) {
                    ae.consume(); //not valid
                    this.serverDisconnected();
                } else {
                    String message = serverConnection.getIncommingMessageProccessor().getMessage();
                    if (protocol.get(message).duplicateUserName()) {
                        ae.consume(); //not valid
                        serverConnection.disconnect();
                        DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting name", "This name is already used by someone. Please try to fill a different name.").showAndWait();
                    } else if (protocol.get(message).wasLoginOk()) {
                        DialogFactory.getAlert(Alert.AlertType.INFORMATION, "Conneting", "Connection sucessfull").showAndWait();
                    }
                }

            }

        });
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            LOGGER.info("Player name is:{}", name);
            new MultiplayerController(this.stage, serverConnection.getIncommingMessageProccessor(), serverConnection.getOutgoingMessageProccessor(), name).loadView();
        });
    }
}
