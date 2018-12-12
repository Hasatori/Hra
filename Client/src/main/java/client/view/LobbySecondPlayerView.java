package client.view;

import java.io.IOException;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import client.controller.multiplayer.LobbySecondPlayerController;
import client.util.ResourceLoader;

/**
 * View class for a lobby second player window.
 */
@SuppressWarnings({"restriction", "Duplicates"})
public class LobbySecondPlayerView extends View {
    private static final String CHAT_DELIM = ": ";
    private final LobbySecondPlayerController controller;
    private String ownerName;
    private String secondPlayerName;
    @FXML
    private Button leaveLobbyButton, lobbyChatSend;
    private ComboBox<String> multiplayerMapsComboBox;
    private Label ownerNameLabel, secondPlayerNameLabel, mapLabel;
    private TextArea lobbyChatInput;
    private ScrollPane lobbyChatPane;
    private TextFlow lobbyChat;

    /**
     * @param controller LobbySecondPlayerController
     * @param maps       maps to fill
     * @throws IOException error
     */
    public LobbySecondPlayerView(LobbySecondPlayerController controller, List<String> maps) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/lobbySecondPlayer.fxml")));
        this.controller = controller;
        this.secondPlayerName = controller.getPlayerName();
        this.multiplayerMapsComboBox = (ComboBox<String>) this.lookup("#multiplayerMapsComboBox");
        this.leaveLobbyButton = (Button) this.lookup("#leaveLobbyButton");
        this.ownerNameLabel = (Label) this.lookup("#ownerNameLabel");
        this.secondPlayerNameLabel = (Label) this.lookup("#secondPlayerNameLabel");
        this.mapLabel = (Label) this.lookup("#mapLabel");
        this.lobbyChatPane = (ScrollPane) this.lookup("#lobbyChatPane");
        this.lobbyChatInput = (TextArea) this.lookup("#lobbyChatInput");
        this.lobbyChatSend = (Button) this.lookup("#lobbyChatSend");
        fillMapComboBox(maps);

        this.lobbyChat = new TextFlow();
        lobbyChatPane.setContent(lobbyChat);
        lobbyChatPane.vvalueProperty().bind(lobbyChat.heightProperty());

        Text playerName = new Text(secondPlayerName);
        playerName.setFill(Color.RED);
        Text lobbyMessage = new Text(" has joined the lobby.");
        lobbyChat.getChildren().addAll(playerName, lobbyMessage);
        mapLabel.setText("Map");

        leaveLobbyButton.setOnAction((a) -> controller.leaveLobby());
        lobbyChatInput.setOnKeyPressed((k) -> {
            if (k.getCode() == KeyCode.ENTER) {
                k.consume();
                sendLobbyMessage();
            }
        });
        lobbyChatSend.setOnAction((a) -> sendLobbyMessage());
    }

    /**
     * Fills map combobox with available multiplayer maps.
     *
     * @param maps multiplayer maps
     */
    private void fillMapComboBox(List<String> maps) {
        this.multiplayerMapsComboBox.getItems().clear();
        this.multiplayerMapsComboBox.getItems().addAll(maps);
        this.multiplayerMapsComboBox.getSelectionModel().select(0);
        this.multiplayerMapsComboBox.setDisable(true);
    }

    /**
     * Sets owner name to Label.
     *
     * @param name owner name
     */
    public void setOwnerName(String name) {
        ownerName = name;
        ownerNameLabel.setText(name);
    }

    /**
     * Sets map in combobox.
     *
     * @param name map name.
     */
    public void setMap(String name) {
        multiplayerMapsComboBox.getSelectionModel().select(name);
    }

    /**
     * Sets second player name and fills the label.
     *
     * @param name second player name
     */
    public void setPlayerName(String name) {
        secondPlayerName = name;
        secondPlayerNameLabel.setText(name);
    }

    /**
     * Sends lobby message. Lobby message consists of text input in chat.
     */
    private void sendLobbyMessage() {
        if (!lobbyChatInput.getText().trim().isEmpty()) {
            String msg = lobbyChatInput.getText().trim();
            controller.sendLobbyMessage(msg);
            receiveLobbyMessage(msg, false);
            lobbyChatInput.setText("");
        }
    }

    /**
     * Receives lobby message based on ownership of the lobby.
     *
     * @param msg   message to receive
     * @param owner true=is owner
     */
    public void receiveLobbyMessage(String msg, boolean owner) {
        Text playerName = new Text();
        if (owner) {
            playerName.setText("\n" + ownerName);
            playerName.setFill(Color.BLUE);
        } else {
            playerName.setText("\n" + secondPlayerName);
            playerName.setFill(Color.RED);
        }
        Text lobbyMessage = new Text(CHAT_DELIM + msg);
        lobbyChat.setTextAlignment(TextAlignment.RIGHT);
        Platform.runLater(() -> lobbyChat.getChildren().addAll(playerName, lobbyMessage));
    }
}
