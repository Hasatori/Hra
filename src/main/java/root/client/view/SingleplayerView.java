package root.client.view;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import root.client.controller.singleplayer.SingleplayerController;
import root.client.util.ResourceLoader;

public class SingleplayerView extends View {
    @FXML
    private Button startSingleplayerButton;
    private TextField nameTextField;
    private ComboBox<String> singleplayerMapsComboBox;

    public SingleplayerView(SingleplayerController controller, List<String> maps) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/start/singleplayer.fxml")));
        this.startSingleplayerButton = (Button) this.lookup("#startSingleplayerButton");
        this.nameTextField = (TextField) this.lookup("#nameTextField");
        this.singleplayerMapsComboBox = (ComboBox<String>) this.lookup("#singleplayerMapsComboBox");
        fillComboBox(maps);

        startSingleplayerButton.setOnAction((a) -> {
            if (nameTextField.getText().equals("")||nameTextField.getText()==null){
                DialogFactory.getAlert(Alert.AlertType.WARNING, "Starting game", "Name must be filled").showAndWait();
            }
            controller.startGame(singleplayerMapsComboBox.getSelectionModel().getSelectedItem(), nameTextField.getText());
        });
    }

    private void fillComboBox(List<String> maps) {
        this.singleplayerMapsComboBox.getItems().clear();
        this.singleplayerMapsComboBox.getItems().addAll(maps);
        this.singleplayerMapsComboBox.getSelectionModel().select(0);
    }
}
