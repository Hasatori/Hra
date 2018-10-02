package root.client.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import root.client.controller.MultiplayerController;
import root.client.util.ResourceLoader;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class MultiplayerView extends View {

    private Button joinLobbyButton, createCustomLobbyButton, back;
    private ListView<String> openedLobbiesListView;
    private MultiplayerController controller;

    public MultiplayerView(MultiplayerController controller, List<String> lobbies) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/lobbies.fxml")), controller);
        this.controller = controller;
        joinLobbyButton = (Button) this.lookup("#joinLobbyButton");
        createCustomLobbyButton = (Button) this.lookup("#createCustomLobbyButton");
        openedLobbiesListView = (ListView) this.lookup("#openedLobbiesListView");

        back = (Button) this.lookup("#back");
        back.setOnAction(a -> {
            controller.back();
        });

        joinLobbyButton.setOnAction(a -> {
            ObservableList<String> selectedItems = openedLobbiesListView.getSelectionModel().getSelectedItems();
            if (selectedItems.size() > 1) {
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Joining lobby", "You can join only one lobby");
            } else if (selectedItems.size() == 0) {
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Joining lobby", "No lobby selected");
            } else {

            }
        });
        fillLobbies(lobbies);
        createCustomLobbyButton.setOnAction(createLobbyHandler);
    }

    private void fillLobbies(List<String> lobbies) {
        openedLobbiesListView.getItems().clear();
        lobbies.forEach(lobby -> {
            openedLobbiesListView.getItems().add(lobby);

        });
    }

    @Override
    public void reload() {

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
            this.controller.loadLobby(name);
        });
    };
}
