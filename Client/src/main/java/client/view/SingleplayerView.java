package client.view;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import client.controller.singleplayer.SingleplayerController;
import client.util.ResourceLoader;

/**
 * View class for a singleplayer window.
 */
public class SingleplayerView extends View {
    @FXML
    private Button startSingleplayerButton;
    private TextField nameTextField;
    private ComboBox<String> singleplayerMapsComboBox;

    /**
     * @param controller SingleplayerController
     * @param maps maps to show
     * @throws IOException error
     */
    public SingleplayerView(SingleplayerController controller, List<String> maps) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/singleplayer.fxml")));
        this.startSingleplayerButton = (Button) this.lookup("#startSingleplayerButton");
        this.nameTextField = (TextField) this.lookup("#nameTextField");
        this.singleplayerMapsComboBox = (ComboBox<String>) this.lookup("#singleplayerMapsComboBox");
        fillMapComboBox(maps);

        startSingleplayerButton.setOnAction(a -> {
            if (nameTextField.getText().equals("") || nameTextField.getText() == null){
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Starting game", "Name must be filled").showAndWait();
            }
            controller.startGame(singleplayerMapsComboBox.getSelectionModel().getSelectedItem(), nameTextField.getText());
        });
    }

    /**
     * Fills map combobox with available singleplayer maps.
     * @param maps singleplayer maps
     */
    private void fillMapComboBox(List<String> maps) {
        this.singleplayerMapsComboBox.getItems().clear();
        this.singleplayerMapsComboBox.getItems().addAll(maps);
        this.singleplayerMapsComboBox.getSelectionModel().select(0);
    }
}
