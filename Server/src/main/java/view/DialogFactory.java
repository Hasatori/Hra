package view;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class DialogFactory {

    private DialogFactory(){

    }

    public static Alert getAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }

    public static Alert getConfirmDialog(String title, String content, String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    public static TextInputDialog getTextInputDialog(String title,String headerText,String contextText){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contextText);
        return dialog;
    }
}
