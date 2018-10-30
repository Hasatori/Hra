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
import client.controller.multiplayer.LobbySecondPlayerController;
import client.util.ResourceLoader;

@SuppressWarnings("restriction")
public class LobbySecondPlayerView extends View {
    private final LobbySecondPlayerController controller;
    private String ownerName;
    private String secondPlayerName;
    @FXML
    Button leaveLobbyButton, startGameButton, lobbyChatSend;
    ComboBox<String> multiplayerMapsComboBox;
    Label ownerNameLabel, secondPlayerNameLabel, mapLabel;
    TextArea lobbyChatInput;
    ScrollPane lobbyChatPane;
    
    TextFlow lobbyChat;

    public LobbySecondPlayerView(LobbySecondPlayerController controller, List<String> maps) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/lobby.fxml")));
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
        fillComboBox(maps);
        
        this.lobbyChat = new TextFlow();
        lobbyChatPane.setContent(lobbyChat);
        lobbyChatPane.vvalueProperty().bind(lobbyChat.heightProperty());

        leaveLobbyButton.setText("Leave Lobby");
        Text playerName = new Text(secondPlayerName);
        playerName.setFill(Color.RED);
        Text lobbyMessage = new Text(" has joined the lobby.");
        lobbyChat.getChildren().addAll(playerName, lobbyMessage);
        mapLabel.setText("Map");
        
        leaveLobbyButton.setOnAction((a) -> {
            controller.leaveLobby();
        });

        lobbyChatInput.setOnKeyPressed((k) -> {
        	if (k.getCode() == KeyCode.ENTER) {
        		k.consume();
        		sendLobbyMessage();
        	}
        });
        
        lobbyChatSend.setOnAction((a) -> {
        	sendLobbyMessage();
        });
    }

    private void fillComboBox(List<String> maps) {
        this.multiplayerMapsComboBox.getItems().clear();
        this.multiplayerMapsComboBox.getItems().addAll(maps);
        this.multiplayerMapsComboBox.getSelectionModel().select(0);
        this.multiplayerMapsComboBox.setDisable(true);
    }

    public void setOwnerName(String name) {
    	ownerName = name;
        ownerNameLabel.setText(name);
    }

    public void setMap(String name) {
        multiplayerMapsComboBox.getSelectionModel().select(name);
    }

    public void setPlayerName(String name) {
    	secondPlayerName = name;
        secondPlayerNameLabel.setText(name);
    }
    
    public void sendLobbyMessage () {
    	if (!lobbyChatInput.getText().trim().isEmpty()) {
    		String msg = lobbyChatInput.getText().trim();
        	controller.sendLobbyMessage(msg);
        	receiveLobbyMessage(msg, false);
        	lobbyChatInput.setText("");
        }
    }
    
    public void receiveLobbyMessage(String msg, boolean owner) {
    	Text playerName = new Text();
    	if (owner) {
    		playerName.setText("\n" + ownerName);
    		playerName.setFill(Color.BLUE);
    	}
    	else {
    		playerName.setText("\n" + secondPlayerName);
    		playerName.setFill(Color.RED);
    	}
    	Text lobbyMessage = new Text(": " + msg);
    	
    	Platform.runLater(new Runnable(){
			@Override
			public void run() {
				lobbyChat.getChildren().addAll(playerName, lobbyMessage);
			}
    	});
    }
}
