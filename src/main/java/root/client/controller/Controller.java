package root.client.controller;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import root.client.view.DialogFactory;

import java.util.Optional;

public abstract class Controller {

    protected final Stage stage;

    public Controller(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(a -> {
            a.consume();
            Optional<ButtonType> result =DialogFactory.getConfirmDialog("Quiting application", "Do you really want to close the application?", "").showAndWait();
            if (result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public abstract void updateView();

    public abstract void loadView();
}
