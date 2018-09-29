package root.client.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import jdk.nashorn.internal.scripts.JO;
import root.client.controller.Controller;
import root.client.model.FileLoader;

import java.io.IOException;
import java.util.List;


public class MultiplayerLobbyView extends Scene {
    private Controller lobbyController;
    private Button joinLobbyButton, createCustomLobbyButton, back;
    private ListView<String> openedLobbiesListView;

    public MultiplayerLobbyView(Controller lobbyController) throws IOException {
        super(FXMLLoader.load(FileLoader.loadFileURL("fxml/start/lobbies.fxml")));
        this.lobbyController = lobbyController;

        joinLobbyButton = (Button) this.lookup("#joinLobbyButton");
        createCustomLobbyButton = (Button) this.lookup("#createCustomLobbyButton");
        openedLobbiesListView = (ListView) this.lookup("#openedLobbiesListView");

        back = (Button) this.lookup("#back");

        back.setOnAction(a -> {
            try {
                this.lobbyController.loadScene(new StartView(lobbyController));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        joinLobbyButton.setOnAction(a -> {
            ObservableList<String> selectedItems = openedLobbiesListView.getSelectionModel().getSelectedItems();
            if (selectedItems.size() > 1) {
                new DialogView(Alert.AlertType.WARNING, "Joining lobby", "You can join only one lobby");
            } else if (selectedItems.size() == 0) {
                new DialogView(Alert.AlertType.WARNING, "Joining lobby", "No lobby selected");
            } else {
                lobbyController.joinLobby(selectedItems.get(0));
            }
        });
      loadLobbies(lobbyController.getOpenedLobbies());
    }

    public void loadLobbies(List<String> lobbies) {
        openedLobbiesListView.getItems().clear();
        lobbies.forEach(lobby -> {
            openedLobbiesListView.getItems().add(lobby);

        });
    }

}
