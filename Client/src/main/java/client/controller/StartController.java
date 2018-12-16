package client.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
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
    private void connectionNotRunning() {
        DialogFactory.getAlert(Alert.AlertType.ERROR, "Server connection", "Connection with the server can not be established. Please try it later.").showAndWait();
    }

    /**
     * Shows dialog for player to set their name.
     * After setting the player's name, this method will try to connect to server on port 8002.
     */
    public void loadMultiplayer() {

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Server connection");

        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField port = new TextField();
        port.setPromptText("Port");

        gridPane.add(new Label("Name:"), 0, 0);
        gridPane.add(name, 1, 0);
        gridPane.add(new Label(" Server port:"), 2, 0);
        gridPane.add(port, 3, 0);

        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(() -> name.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(name.getText(), port.getText());
            }
            return null;
        });

        final Button okButton = (Button) dialog.getDialogPane().lookupButton(loginButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            //ae.consume();
            Pair<String, String> pair = dialog.getResultConverter().call(loginButtonType);
            String filledName = pair.getKey().trim();
            String filledPort = pair.getValue().trim();
            if (filledName.equals("") || filledPort.equals("")) {
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting up server connection", "Mandatory data must be filled").showAndWait();
            } else if (filledName.contains("|")) {
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting up server connection", "Name cannot contain |").showAndWait();
            } else if (!filledPort.matches("\\d{4,5}")) {
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Setting up server connection", "Port must be a number of length 4 or 5").showAndWait();
            } else {
                this.serverConnection = new ServerConnection(filledName, Integer.valueOf(filledPort));
                if (!this.serverConnection.isConnected()) {
                    ae.consume(); //not valid
                    this.connectionNotRunning();
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

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            String playerName = pair.getKey();
            LOGGER.info("Player name is:{}", playerName);
            new MultiplayerController(this.stage, serverConnection, playerName).loadView();
        });
    }
}
