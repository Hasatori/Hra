package view;

import com.sun.org.apache.bcel.internal.generic.FADD;
import controller.MainController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import server.clientservices.Client;
import util.ResourceLoader;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainView extends Scene {

    private TextField port;
    private Button start, stop;
    private TableView<Client> clientsTable;
    private TableColumn identifierColumn;
    private TextFlow messagesTextFlow;
    private ScrollPane scrollPane;

    public MainView(MainController mainController) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("main.fxml")));

        start = (Button) this.lookup("#startButton");
        stop = (Button) this.lookup("#stopButton");
        port = (TextField) this.lookup("#portTextField");
        scrollPane = (ScrollPane) this.lookup("#textFlowScrollPane");
        messagesTextFlow = new TextFlow();
        scrollPane.setContent(messagesTextFlow);

        clientsTable = (TableView) this.lookup("#clientTable");
        identifierColumn = new TableColumn("IDENTIFIER");
        identifierColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("identifier"));
        clientsTable.getColumns().addAll(identifierColumn);
        identifierColumn.prefWidthProperty().bind(clientsTable.widthProperty());

        start.setOnAction(a -> {

            start.setDisable(true);
            stop.setDisable(true);

            if (port.getText().equals("")) {
                DialogFactory.getAlert(Alert.AlertType.ERROR, "Port", "Port must be filled").showAndWait();
                start.setDisable(false);
            } else {
                try {
                    Integer portNumber = Integer.valueOf(port.getText());
                    mainController.startServer(portNumber);
                } catch (NumberFormatException e) {
                    DialogFactory.getAlert(Alert.AlertType.ERROR, "Port", "Port must an integer").showAndWait();
                    start.setDisable(false);
                }
            }
            stop.setDisable(false);
        });

        stop.setOnAction(a -> {
            mainController.stopServer();
            start.setDisable(false);
        });

    }

    public void fillClientsTable(List<Client> clients) {
        clientsTable.getItems().clear();
        clientsTable.setItems(FXCollections.observableArrayList(clients));
    }

    public void message(String message) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date();
        String prefix=dateFormat.format(date)+" - ";
        messagesTextFlow.getChildren().addAll(new Text(prefix+message + "\n"));
    }
}
