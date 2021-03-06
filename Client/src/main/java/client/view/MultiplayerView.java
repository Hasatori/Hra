package client.view;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import client.controller.multiplayer.MultiplayerController;
import client.util.ResourceLoader;
import client.model.map.CreatedLobby;
import javafx.scene.image.ImageView;

/**
 * View class for a multiplayer window (choosing lobbies).
 */
@SuppressWarnings("restriction")
public class MultiplayerView extends View {

    private Button joinLobbyButton, createCustomLobbyButton, back;
    private ImageView refreshButton;
    private TableView<CreatedLobby> openedLobbiesTable;
    private TableColumn columnLobbyName, columnLobbyOwner ,columnLobbyCapacity;
    private MultiplayerController controller;
    private Label playerNameLabel;

    /**
     * @param controller MultiplayerController
     * @param lobbies list of lobbies
     * @param playerName name of the player
     * @throws IOException error
     */
    public MultiplayerView(MultiplayerController controller, List<CreatedLobby> lobbies, String playerName) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/lobbies.fxml")));

        this.controller = controller;
        joinLobbyButton = (Button) this.lookup("#joinLobbyButton");
        createCustomLobbyButton = (Button) this.lookup("#createCustomLobbyButton");
        openedLobbiesTable = (TableView) this.lookup("#openedLobbiesTable");
        refreshButton = (ImageView) this.lookup("#refreshButton");
        playerNameLabel = (Label) this.lookup("#playerNameLabel");

        columnLobbyName = new TableColumn("Lobby name");
        columnLobbyOwner = new TableColumn("Owner");
        columnLobbyCapacity = new TableColumn("Capacity");
        columnLobbyName.setCellValueFactory(new PropertyValueFactory<CreatedLobby, String>("name"));
        columnLobbyOwner.setCellValueFactory(new PropertyValueFactory<CreatedLobby, String>("owner"));
        columnLobbyCapacity.setCellValueFactory(new PropertyValueFactory<CreatedLobby, String>("status"));
        openedLobbiesTable.getColumns().addAll(columnLobbyName, columnLobbyOwner, columnLobbyCapacity);

        playerNameLabel.setText(playerName);
        back = (Button) this.lookup("#disconnect");
        back.setOnAction(a -> controller.disconnect());

        joinLobbyButton.setOnAction(a -> {
            ObservableList<CreatedLobby> selectedItems = openedLobbiesTable.getSelectionModel().getSelectedItems();
            if (selectedItems.size() > 1) {
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Joining lobby", "You can join only one lobby").showAndWait();
            } else if (selectedItems.size() == 0) {
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Joining lobby", "No lobby selected").showAndWait();
            } else {
                controller.joinLobby(openedLobbiesTable.getSelectionModel().getSelectedItem().getName());
            }
        });

        fillLobbies(lobbies);
        createCustomLobbyButton.setOnAction(createLobbyHandler);

        refreshButton.setOnMouseClicked((a) -> fillLobbies(controller.loadLobbies()));
    }

    /**
     * Fill table with existing lobbies that player can connect to.
     * @param lobbies lobbies to list
     */
    private void fillLobbies(List<CreatedLobby> lobbies) {
    	ObservableList<CreatedLobby> lobbiesList = FXCollections.observableArrayList(lobbies);
    	openedLobbiesTable.setItems(lobbiesList);
    }

    private EventHandler<ActionEvent> createLobbyHandler = event -> {
        TextInputDialog dialog = DialogFactory.getTextInputDialog("", "Creating lobby", "Fill the lobby name please");
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, ae -> {
            if (dialog.getEditor().getText().equals("")) {
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Creating lobby", "Name must be filled");
            }else if (dialog.getEditor().getText().contains("|")){
                ae.consume(); //not valid
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Creating lobby", "Name cannot contain |").showAndWait();
            }
        });
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> this.controller.createLobby(name));
    };
}
