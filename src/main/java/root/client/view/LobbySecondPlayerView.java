package root.client.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import root.client.controller.multiplayer.LobbySecondPlayerController;
import root.client.util.ResourceLoader;

import java.io.IOException;
import java.util.List;

public class LobbySecondPlayerView extends View {
    private final LobbySecondPlayerController controller;
    @FXML
    Button leaveLobbyButton, startGameButton;
    ComboBox<String> multiplayerMapsComboBox;
    Label ownerNameLabel, secondPlayerNameLabel;


    public LobbySecondPlayerView(LobbySecondPlayerController controller, List<String> maps) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/lobbySecondPlayer.fxml")));
        this.controller = controller;
        this.multiplayerMapsComboBox = (ComboBox<String>) this.lookup("#multiplayerMapsComboBox");
        this.leaveLobbyButton = (Button) this.lookup("#leaveLobbyButton");
        this.ownerNameLabel = (Label) this.lookup("#ownerNameLabel");
        this.secondPlayerNameLabel = (Label) this.lookup("#secondPlayerNameLabel");
        fillComboBox(maps);

        leaveLobbyButton.setOnAction((a) -> {
            controller.leaveLobby();
        });

    }

    private void fillComboBox(List<String> maps) {
        this.multiplayerMapsComboBox.getItems().clear();
        this.multiplayerMapsComboBox.getItems().addAll(maps);
        this.multiplayerMapsComboBox.getSelectionModel().select(0);
        this.multiplayerMapsComboBox.setDisable(true);
    }

    public void setOwnerName(String name) {
        ownerNameLabel.setText(name);
    }

    public void setMap(String name) {
        multiplayerMapsComboBox.getSelectionModel().select(name);
    }

    public void setPlayerName(String name) {
        secondPlayerNameLabel.setText(name);
    }
}
