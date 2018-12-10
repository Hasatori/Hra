package client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import client.controller.*;
import client.model.map.Player;
import client.util.ResourceLoader;
import client.model.map.MapPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * View class for a map window.
 */
public class MapView extends View {

    private final Logger LOGGER = LoggerFactory.getLogger(MapView.class);
    private MapController mapController;
    private GridPane gridPane;
    private static Pane pane;
    private List<List<MapPart>> mapParts;
    private VBox vBox;

    /**
     * @param mapController MapController
     * @param mapParts map parts to be drawn
     * @param mapName name of the map
     * @throws IOException error
     */
    public MapView(MapController mapController, List<List<MapPart>> mapParts, String mapName) throws IOException {
        super((pane = new Pane()));
        pane.getChildren().clear();
        this.mapController = mapController;
        this.mapParts = mapParts;
        this.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> mapController.movePlayer(event.getCode()));
        this.vBox = new VBox();
        vBox.getChildren().add(getMenu());

        Label heading = FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/mapHeading.fxml"));
        heading.setText(mapName);
        vBox.getChildren().add(heading);
        vBox.getChildren().add(createMap(this.mapParts));
        pane.getChildren().add(vBox);
    }

    /**
     * Draw map from given map parts.
     * @param mapParts map parts
     * @return GridPane with complete map layout
     */
    private GridPane createMap(List<List<MapPart>> mapParts) {
        gridPane = new GridPane();
        for (int row = 0; row < mapParts.size(); row++) {
            for (int column = 0; column < mapParts.get(row).size(); column++) {
                try {
                    MapPart part = mapParts.get(row).get(column);
                    Node node = part.getSource();
                    gridPane.add(node, column, row);
                    if (mapParts.get(row).get(column) instanceof Player) {
                        MapPart player = part;
                        System.out.println("Setting name " + ((Player) player).getName());
                        ((Label) node.lookup(".playerName")).setText(((Player) player).getName());
                        System.out.println(((Label) node.lookup(".playerName")).getText());
                    }
                } catch (IOException e) {
                    LOGGER.error("Failed to create map", e);
                }
            }
        }
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    /**
     * Create menubar and it's components.
     * @return menubar
     */
    private MenuBar getMenu() {
        MenuBar menuBar = new MenuBar();
        Menu general = new Menu("General");
        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(a -> mapController.quitMap());
        MenuItem restart = new MenuItem("Restart");
        restart.setOnAction(a -> mapController.restartMap());
        general.getItems().addAll(quit, restart);
        menuBar.getMenus().addAll(general);
        return menuBar;
    }

    /**
     * Reloads the entire map.
     * @param mapParts new map parts
     */
    public void reload(List<List<MapPart>> mapParts) {
        vBox.getChildren().remove(gridPane);
        vBox.getChildren().add(createMap(mapParts));
    }
}
