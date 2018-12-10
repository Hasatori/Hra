package client.model.map;

import com.sun.javafx.scene.traversal.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Core class for the map game.
 * This class handles construction the map, movement of map parts and players.
 * It also checks the win conditions.
 */
public abstract class Map {

    protected final List<Target> targets = new LinkedList<>();
    private String name;
    public MapPart[][] mapParts;

    /**
     * @param name name of the map
     */
    public Map(String name, String mapPath) {
        this.name = name;
        this.mapParts = new LevelLoader().load(mapPath + name + ".txt");
        loadNeighbours();
    }

    /**
     * Getter for map name.
     * @return map name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns list of the map parts that map consists of.
     * @return List of map parts
     */
    public synchronized List<List<MapPart>> getMapParts() {
        List<List<MapPart>> arrayListMapParts = new LinkedList<>();

        for (int i = 0; i < mapParts.length; i++) {
            List<MapPart> row = new LinkedList<>();
            for (int column = 0; column < mapParts[i].length; column++) {
                row.add(mapParts[i][column]);
            }
            arrayListMapParts.add(row);
        }
        return Collections.unmodifiableList(arrayListMapParts);
    }

    /**
     * Moves player in a direction on a map.
     * @param direction direction to move player
     * @param name name of the player
     */
    public abstract void movePlayer(Direction direction, String name);

    /**
     * Checks win condition, if condition agrees return true.
     * @return true=condition checks
     */
    public  abstract boolean checkWinCondition();

    /**
     * Moves map part depending on a direction.
     * @param direction direction to move
     * @param mapPart map part to move
     */
    protected void movePart(Direction direction, MapPart mapPart) {
        MapPart neighbour = mapPart.getNeighbour(direction);
        if (neighbour instanceof Box && !(mapPart instanceof Box)) {
            movePart(direction, neighbour);
        }
        neighbour = mapPart.getNeighbour(direction);
        if (neighbour instanceof Floor || neighbour instanceof Target) {
            Position position = mapPart.getPosition();

            switch (direction) {
                case DOWN:
                    mapParts[position.getRow() + 1][position.getColumn()] = mapPart;
                    break;
                case UP:
                    mapParts[position.getRow() - 1][position.getColumn()] = mapPart;
                    break;
                case LEFT:
                    mapParts[position.getRow()][position.getColumn() - 1] = mapPart;
                    break;
                case RIGHT:
                    mapParts[position.getRow()][position.getColumn() + 1] = mapPart;
                    break;
            }
            mapParts[mapPart.getPosition().getRow()][mapPart.getPosition().getColumn()] = neighbour;
            switchPositions(mapPart, neighbour);
            if (neighbour instanceof Target) {
                ((Target) neighbour).setCovered(mapPart);
            }
            for (Target target : targets) {
                if (neighbour.getPosition().equals(target.getInitialPosition())) {
                    switchParts(neighbour, target);
                    target.setUncovered();
                    break;
                }
            }
        }
        loadNeighbours();
    }

    /**
     * Switches position of 2 parts with each other.
     * @param firstPart first part to switch
     * @param secondPart second part to switch
     */
    private void switchPositions(MapPart firstPart, MapPart secondPart) {
        Position firstPartPosition = firstPart.getPosition();
        Position secondPartPosition = secondPart.getPosition();
        firstPart.setPosition(secondPartPosition);
        secondPart.setPosition(firstPartPosition);
    }

    /**
     * Switches 2 parts with each other.
     * @param firstPart first part to switch
     * @param secondPart second part to switch
     */
    private void switchParts(MapPart firstPart, MapPart secondPart) {
        mapParts[firstPart.getPosition().getRow()][firstPart.getPosition().getColumn()] = secondPart;
        mapParts[secondPart.getPosition().getRow()][secondPart.getPosition().getColumn()] = firstPart;
        switchPositions(firstPart, secondPart);
    }

    /**
     * Loads map part neighbours.
     */
    private void loadNeighbours() {
        for (int rowNum = 0; rowNum < mapParts.length; rowNum++) {
            for (int column = 0; column < mapParts[rowNum].length; column++) {
                if (rowNum != 0) {
                    mapParts[rowNum][column].setTop(mapParts[rowNum - 1][column]);
                }
                if (rowNum != mapParts.length - 1) {
                    mapParts[rowNum][column].setBottom(mapParts[rowNum + 1][column]);
                }
                if (column != 0) {
                    mapParts[rowNum][column].setLeft(mapParts[rowNum][column - 1]);
                }
                if (column != mapParts[rowNum].length - 1) {
                    mapParts[rowNum][column].setRight(mapParts[rowNum][column + 1]);
                }
            }
        }
    }
}
