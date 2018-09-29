package root.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;
import root.client.controller.Controller;
import root.client.model.FileLoader;
import root.client.model.map.MapPart;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MapView extends Scene {
    private Controller mapController;
    private GridPane gridPane;
    private static Pane pane;

    public MapView(Controller mapController, List<List<MapPart>> mapParts, String mapName) throws IOException {
        super((pane = new Pane()));
        pane.getChildren().clear();
        this.mapController = mapController;
        this.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> mapController.movePlayer(event.getCode()));
        VBox vBox = new VBox();
        vBox.getChildren().add(getMenu());

        Label heading = (Label) FXMLLoader.load(FileLoader.loadFileURL("fxml/parts/mapHeading.fxml"));
        heading.setText(mapName);
        vBox.getChildren().add(heading);
        vBox.getChildren().add(setMap(mapParts));
        pane.getChildren().add(vBox);

    }

    private GridPane setMap(List<List<MapPart>> mapParts) {
        gridPane = new GridPane();
        for (int row = 0; row < mapParts.size(); row++) {
            for (int column = 0; column < mapParts.get(row).size(); column++) {
                // System.out.println("Column: "+column+" Rows: "+row);
                System.out.println(mapParts.get(row).get(column).getClass().getName());
                try {
                    gridPane.add(mapParts.get(row).get(column).getSource(), column, row);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    private MenuBar getMenu() {
        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu maps = new Menu("Maps");

        File[] mapFiles = FileLoader.loadFile("plans").listFiles();
        Arrays.stream(mapFiles).forEach(file -> {
            String mapName = FilenameUtils.removeExtension(file.getName());

            MenuItem menuItem = new MenuItem(mapName);
            menuItem.setOnAction(a -> {
                System.out.println("Trying to load map " + mapName);
                mapController.switchToMap(mapName);
            });
            maps.getItems().add(menuItem);
        });
        Menu general = new Menu("General");
        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(a -> {
            Optional<ButtonType> result = new DialogView("Quiting game", "Are you sure?", "You are about to quit the game.").showAndWait();
            if (result.get() == ButtonType.OK) {
                try {
                    mapController.loadScene(new StartView(mapController));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // ... user chose CANCEL or closed the dialog
            }

        });
        MenuItem restart = new MenuItem("Restart");
        restart.setOnAction(a -> {

            mapController.restartMap();

        });

        general.getItems().addAll(quit, restart);
        menuBar.getMenus().addAll(maps, general);
        return menuBar;
    }
}
