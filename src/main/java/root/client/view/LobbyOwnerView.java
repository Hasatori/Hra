package root.client.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import root.client.controller.multiplayer.LobbyOwnerController;
import root.client.util.ResourceLoader;

import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;

public class LobbyOwnerView extends View {
    private final LobbyOwnerController controller;
    @FXML
    Button leaveLobbyButton, startGameButton;
    ComboBox<String> multiplayerMapsComboBox;
    Label ownerNameLabel, secondPlayerNameLabel;

    public LobbyOwnerView(LobbyOwnerController controller, List<String> maps, String ownerName) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/lobby.fxml")));
        this.controller = controller;
        this.multiplayerMapsComboBox = (ComboBox<String>) this.lookup("#multiplayerMapsComboBox");
        this.startGameButton = (Button) this.lookup("#startGameButton");
        this.leaveLobbyButton = (Button) this.lookup("#leaveLobbyButton");
        this.ownerNameLabel = (Label) this.lookup("#ownerNameLabel");
        this.secondPlayerNameLabel = (Label) this.lookup("#secondPlayerNameLabel");
        fillComboBox(maps);
        ownerNameLabel.setText(ownerName);

        startGameButton.setOnAction((a) -> {
            controller.startGame(multiplayerMapsComboBox.getSelectionModel().getSelectedItem());
        });
    }

    private void fillComboBox(List<String> maps) {
        this.multiplayerMapsComboBox.getItems().clear();
        this.multiplayerMapsComboBox.getItems().addAll(maps);
        this.multiplayerMapsComboBox.getSelectionModel().select(0);
        this.multiplayerMapsComboBox.setOnAction((a) -> {
            controller.setMap(multiplayerMapsComboBox.getSelectionModel().getSelectedItem());
        });
    }

    public void lobbyIsEmpty(){
        this.secondPlayerNameLabel.setText("");

    }
    public void setSecondPlayerName(String value) {
        this.secondPlayerNameLabel.setText(value);
    }
}
