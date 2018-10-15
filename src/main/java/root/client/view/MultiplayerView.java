package root.client.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import root.client.controller.multiplayer.MultiplayerController;
import root.client.util.ResourceLoader;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class MultiplayerView extends View {

    private Button joinLobbyButton, createCustomLobbyButton, back, refreshButton;
    private ListView<String> openedLobbiesListView;
    private MultiplayerController controller;
    private Label playerNameLabel;

    public MultiplayerView(MultiplayerController controller, List<String> lobbies, String playerName) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/lobbies.fxml")));
        this.controller = controller;
        joinLobbyButton = (Button) this.lookup("#joinLobbyButton");
        createCustomLobbyButton = (Button) this.lookup("#createCustomLobbyButton");
        openedLobbiesListView = (ListView) this.lookup("#openedLobbiesListView");
        refreshButton = (Button) this.lookup("#refreshButton");
        playerNameLabel = (Label) this.lookup("#playerNameLabel");

        playerNameLabel.setText(playerName);
        back = (Button) this.lookup("#back");
        back.setOnAction(a -> {
            controller.back();
        });

        joinLobbyButton.setOnAction(a -> {
            ObservableList<String> selectedItems = openedLobbiesListView.getSelectionModel().getSelectedItems();
            if (selectedItems.size() > 1) {
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Joining lobby", "You can join only one lobby").showAndWait();
            } else if (selectedItems.size() == 0) {
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Joining lobby", "No lobby selected").showAndWait();
            } else {
                controller.joinLobby(openedLobbiesListView.getSelectionModel().getSelectedItem());
            }
        });
        fillLobbies(lobbies);
        createCustomLobbyButton.setOnAction(createLobbyHandler);

        refreshButton.setOnAction((a) -> {
            fillLobbies(controller.loadLobbies());
        });
    }

    private void fillLobbies(List<String> lobbies) {
        openedLobbiesListView.getItems().clear();
        lobbies.forEach(lobby -> {
            openedLobbiesListView.getItems().add(lobby);
        });

    }


    private EventHandler<ActionEvent> createLobbyHandler = event -> {
        TextInputDialog dialog = DialogFactory.getTextInputDialog("", "Creating lobby", "Fill the lobby name please");
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (dialog.getEditor().getText().equals("")) {
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Creating lobby", "Name must be filled");
            }
        });
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            this.controller.createLobby(name);
        });
    };
}
