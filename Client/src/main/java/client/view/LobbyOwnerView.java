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

@SuppressWarnings("restriction")
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
    
	public LobbyOwnerView(LobbyOwnerController controller, List<String> maps, String ownerName) throws IOException {
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
        
        fillComboBox(maps);
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

    private void fillComboBox(List<String> maps) {
        this.multiplayerMapsComboBox.getItems().clear();
        this.multiplayerMapsComboBox.getItems().addAll(maps);
        this.multiplayerMapsComboBox.getSelectionModel().select(0);
        this.multiplayerMapsComboBox.setOnAction((a) -> controller.setMap(multiplayerMapsComboBox.getSelectionModel().getSelectedItem()));
    }

    public void lobbyIsEmpty() {
        this.secondPlayerNameLabel.setText("");
        this.startGameButton.setDisable(true);
        receiveLobbyMessage(" has left the lobby", false, false);
    }

    public void setSecondPlayerName(String value) {
    	secondPlayerName = value;
        this.secondPlayerNameLabel.setText(value);
        this.startGameButton.setDisable(false);
        receiveLobbyMessage(" has joined the lobby", false, false);
    }
    
    private void sendLobbyMessage () {
    	if (!lobbyChatInput.getText().trim().isEmpty()) {
    		String msg = lobbyChatInput.getText().trim();
        	controller.sendLobbyMessage(msg);
        	receiveLobbyMessage(msg, true, true);
        	lobbyChatInput.setText("");
        }
    }
    
    public void receiveLobbyMessage(String msg, boolean owner, boolean prefix) {
    	Text playerName = new Text();
    	if (owner) {
    		playerName.setText("\n" + ownerName);
    		playerName.setFill(Color.BLUE);
    	}
    	else {
    		playerName.setText("\n" + secondPlayerName);
    		playerName.setFill(Color.RED);
    	}
    	String pre = prefix ? CHAT_DELIM : "";
    	Text lobbyMessage = new Text(pre + msg);
    	
    	Platform.runLater(() -> lobbyChat.getChildren().addAll(playerName, lobbyMessage));
    }
}
