package root.client.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class DialogView extends Alert {
   public DialogView(AlertType alertType, String title, String content) {
        super(alertType);
        this.setTitle(title);
        this.setContentText(content);
        this.showAndWait();
    }

   public DialogView(String title, String content,String header) {
        super(AlertType.CONFIRMATION);
        this.setTitle(title);
        this.setHeaderText(header);
        this.setContentText(content);

    }

}
