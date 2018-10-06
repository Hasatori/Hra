package root.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;
import root.client.controller.SingleplayerMapController;
import root.client.util.ResourceLoader;
import root.client.model.map.MapPart;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapView extends View {
    private SingleplayerMapController mapController;
    private GridPane gridPane;
    private static Pane pane;
    private List<List<MapPart>> mapParts;
    private VBox vBox;

    public MapView(SingleplayerMapController mapController, List<List<MapPart>> mapParts, String mapName) throws IOException {
        super((pane = new Pane()), mapController);
        pane.getChildren().clear();
        this.mapController = mapController;
        this.mapParts = mapParts;
        this.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> mapController.movePlayer(event.getCode()));
       this.vBox = new VBox();
        vBox.getChildren().add(getMenu());

        Label heading = (Label) FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/mapHeading.fxml"));
        heading.setText(mapName);
        vBox.getChildren().add(heading);
        vBox.getChildren().add(createMap(this.mapParts));

        pane.getChildren().add(vBox);

    }

    private GridPane createMap(List<List<MapPart>> mapParts) {
        gridPane = new GridPane();
        for (int row = 0; row < mapParts.size(); row++) {
            for (int column = 0; column < mapParts.get(row).size(); column++) {
                // System.out.println("Column: "+column+" Rows: "+row);
                //System.out.println(mapParts.get(row).get(column).getClass().getName());
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

        Menu general = new Menu("General");
        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(a -> {
            mapController.quitMap();
        });
        MenuItem restart = new MenuItem("Restart");
        restart.setOnAction(a -> {

            mapController.restartMap();

        });
        general.getItems().addAll(quit, restart);
        menuBar.getMenus().addAll(general);
        return menuBar;
    }


    public void reload(List<List<MapPart>> mapParts) {
        vBox.getChildren().remove(gridPane);
        vBox.getChildren().add(createMap(mapParts));
    }
}
