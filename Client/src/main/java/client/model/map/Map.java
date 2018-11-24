package client.model.map;

import com.sun.javafx.scene.traversal.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class Map {
    private final String mapPath;
    private final Logger LOGGER = LoggerFactory.getLogger(Map.class);
    protected final List<Target> targets = new LinkedList<>();
    private final List<Player> players = new LinkedList<>();
    private String name;
    public MapPart[][] mapParts;

    public Map(String name, String mapPath) {
        this.mapPath = mapPath;
        this.mapParts = new LevelLoader().load(mapPath + name + ".txt");
        loadNeighbours();
        this.name = name;

    }
    public String getName() {
        return name;
    }

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



    public abstract void movePlayer(Direction direction, String name);

   public  abstract boolean checkWinCondition();

    protected void movePart(Direction direction, MapPart mapPart) {
        MapPart neighbour = mapPart.getNeighbour(direction);
        if (neighbour instanceof Box && !(mapPart instanceof Box)) {
            movePart(direction, neighbour);
        }
        neighbour = mapPart.getNeighbour(direction);
        if (neighbour instanceof Floor || neighbour instanceof Target) {
            switch (direction) {
                case DOWN:
                    mapParts[mapPart.getPosition().getRow() + 1][mapPart.getPosition().getColumn()] = mapPart;
                    break;
                case UP:
                    mapParts[mapPart.getPosition().getRow() - 1][mapPart.getPosition().getColumn()] = mapPart;
                    break;
                case LEFT:
                    mapParts[mapPart.getPosition().getRow()][mapPart.getPosition().getColumn() - 1] = mapPart;
                    break;
                case RIGHT:
                    mapParts[mapPart.getPosition().getRow()][mapPart.getPosition().getColumn() + 1] = mapPart;
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

    private void switchPositions(MapPart firstPart, MapPart secondPart) {
        Position firstPartPosition = firstPart.getPosition();
        Position secondPartPosition = secondPart.getPosition();
        firstPart.setPosition(secondPartPosition);
        secondPart.setPosition(firstPartPosition);
    }

    private void switchParts(MapPart firstPart, MapPart secondPart) {
        mapParts[firstPart.getPosition().getRow()][firstPart.getPosition().getColumn()] = secondPart;
        mapParts[secondPart.getPosition().getRow()][secondPart.getPosition().getColumn()] = firstPart;
        switchPositions(firstPart, secondPart);
    }

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
