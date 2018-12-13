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
import javafx.scene.text.TextFlow;
import client.controller.multiplayer.LobbyOwnerController;
import client.util.ResourceLoader;

/**
 * View class for a lobby owner window.
 */
@SuppressWarnings({"restriction", "Duplicates"})
public class LobbyOwnerView extends View {
    private static final String CHAT_DELIM = ": ";
    private final LobbyOwnerController controller;
    private final String ownerName;
    private String secondPlayerName;
    @FXML
    private Button deleteLobbyButton, startGameButton, lobbyChatSend;
    private ComboBox<String> multiplayerMapsComboBox;
    private Label ownerNameLabel, secondPlayerNameLabel;
    private TextArea lobbyChatInput;
    private ScrollPane lobbyChatPane;
    private TextFlow lobbyChat;

    /**
     * @param controller LobbyOwnerController
     * @param maps       maps to offer
     * @param ownerName  name of the owner
     * @throws IOException error
     */
    public LobbyOwnerView(LobbyOwnerController controller, List<String> maps, String map, String ownerName) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/lobbyOwner.fxml")));
        this.controller = controller;
        this.ownerName = ownerName;
        this.multiplayerMapsComboBox = (ComboBox<String>) this.lookup("#multiplayerMapsComboBox");
        this.startGameButton = (Button) this.lookup("#startGameButton");
        this.deleteLobbyButton = (Button) this.lookup("#deleteLobbyButton");
        this.ownerNameLabel = (Label) this.lookup("#ownerNameLabel");
        this.secondPlayerNameLabel = (Label) this.lookup("#secondPlayerNameLabel");
        this.lobbyChatPane = (ScrollPane) this.lookup("#lobbyChatPane");
        this.lobbyChatInput = (TextArea) this.lookup("#lobbyChatInput");
        this.lobbyChatSend = (Button) this.lookup("#lobbyChatSend");

        this.lobbyChat = new TextFlow();
        lobbyChatPane.setContent(lobbyChat);
        lobbyChatPane.vvalueProperty().bind(lobbyChat.heightProperty());

        fillMapComboBox(maps, map);
        ownerNameLabel.setText(ownerName);
        Text playerName = new Text(ownerName);
        playerName.setFill(Color.BLUE);
        Text lobbyMessage = new Text(" has joined the lobby.");
        lobbyChat.getChildren().addAll(playerName, lobbyMessage);
        startGameButton.setDisable(true);

        startGameButton.setOnAction(a -> controller.startGame(multiplayerMapsComboBox.getSelectionModel().getSelectedItem()));
        deleteLobbyButton.setOnAction(a -> controller.deleteLobby());
        lobbyChatInput.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER) {
                k.consume();
                sendLobbyMessage();
            }
        });
        lobbyChatSend.setOnAction(a -> sendLobbyMessage());
    }

    /**
     * Fills map combobox with available multiplayer maps and selects given map.
     *
     * @param maps multiplayer maps
     */
    private void fillMapComboBox(List<String> maps, String map) {
        this.multiplayerMapsComboBox.getItems().clear();
        this.multiplayerMapsComboBox.getItems().addAll(maps);
        this.multiplayerMapsComboBox.getSelectionModel().select(map);
        this.multiplayerMapsComboBox.setOnAction((a) -> controller.setMap(multiplayerMapsComboBox.getSelectionModel().getSelectedItem()));
    }

    /**
     * Method called after second player leaving the lobby.
     */
    public void lobbyIsEmpty() {
        this.secondPlayerNameLabel.setText("");
        this.startGameButton.setDisable(true);
        receiveLobbyMessage(" has left the lobby", false, false);
    }

    /**
     * Sets second player name to label.
     *
     * @param secondPlayerName second player name
     */
    public void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
        this.secondPlayerNameLabel.setText(secondPlayerName);
        this.startGameButton.setDisable(false);
        receiveLobbyMessage(" has joined the lobby", false, false);
    }

    /**
     * Sends lobby message. Lobby message consists of text input in chat.
     */
    private void sendLobbyMessage() {
        if (!lobbyChatInput.getText().trim().isEmpty()) {
            String msg = lobbyChatInput.getText().trim();
            controller.sendLobbyMessage(msg);
            receiveLobbyMessage(msg, true, true);
            lobbyChatInput.setText("");
        }
    }

    /**
     * Receives lobby message based on ownership of the lobby.
     *
     * @param msg    message to receive
     * @param owner  true=is owner
     * @param prefix true=needs prefix to match (User1: Hello world!)
     */
    public void receiveLobbyMessage(String msg, boolean owner, boolean prefix) {
        Text playerName = new Text();
        if (owner) {
            playerName.setText("\n" + ownerName);
            playerName.setFill(Color.BLUE);
        } else {
            playerName.setText("\n" + secondPlayerName);
            playerName.setFill(Color.RED);
        }
        String pre = prefix ? CHAT_DELIM : "";
        Text lobbyMessage = new Text(pre + msg);

        Platform.runLater(() -> lobbyChat.getChildren().addAll(playerName, lobbyMessage));
    }
}
