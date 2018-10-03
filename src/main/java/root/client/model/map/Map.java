package root.client.model.map;

import com.sun.javafx.UnmodifiableArrayList;
import com.sun.javafx.scene.traversal.Direction;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Map {
    public String getName() {
        return name;
    }

    private final String name;

    public List<List<MapPart>> getMapParts() {
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

    private final MapPart[][] mapParts;
    private Player player;

    public Map(String name) {
        this.mapParts = new LevelLoader().load("plans/" + name + ".txt");
        this.name = name;
        this.player = (Player) mapParts[getPlayerPosition().row][getPlayerPosition().column];
    }

    private Position getPlayerPosition() {
        for (int row = 0; row < mapParts.length; row++) {
            for (int column = 0; column < mapParts[row].length; column++) {
                if (mapParts[row][column] instanceof Player) {
                    return mapParts[row][column].getPosition();
                }
            }
        }
        throw new IllegalStateException("Player is not defined on the map");
    }

    public void movePlayer(KeyCode keyCode) {
        this.movePart(keyCode, player);
        player.setDirection(Direction.valueOf(keyCode.toString()));
       /* System.out.println("Position by player is " + player.getPosition().toString());
        System.out.println("Position by map is " + getPlayerPosition().toString());
        System.out.println("Map part is" + player.getClass().getSimpleName());
        System.out.println("By map tryMoveLeft neighbour is " + mapParts[getPlayerPosition().row][getPlayerPosition().column - 1].getClass().getSimpleName());
        System.out.println("By map tryMoveRight neighbour is " + mapParts[getPlayerPosition().row][getPlayerPosition().column + 1].getClass().getSimpleName());
        System.out.println("By player tryMoveLeft neighbour is " + player.getLeft().getClass().getSimpleName());
        System.out.println("By player tryMoveRight neighbour is " + player.getRight().getClass().getSimpleName());*/

    }

    private void movePart(KeyCode keyCode, MapPart mapPart) {
        switch (keyCode) {
            case DOWN:
                MapPart bottomPart = mapPart.getBottom();
                if (bottomPart instanceof Box) {
                    movePart(keyCode, bottomPart);
                }
                bottomPart = mapPart.getBottom();
                if (bottomPart instanceof Floor) {
                    mapParts[mapPart.getPosition().row + 1][mapPart.getPosition().column] = mapPart;
                    mapParts[mapPart.getPosition().row][mapPart.getPosition().column] = bottomPart;
                    switchPositions(mapPart, bottomPart);
                }
                break;
            case UP:
                MapPart topPart = mapPart.getTop();
                if (topPart instanceof Box) {
                    movePart(keyCode, topPart);
                }
                topPart = mapPart.getTop();
                if (mapPart.getTop() instanceof Floor) {
                    mapParts[mapPart.getPosition().row - 1][mapPart.getPosition().column] = mapPart;
                    mapParts[mapPart.getPosition().row][mapPart.getPosition().column] = topPart;
                    switchPositions(mapPart, topPart);
                }

                break;
            case LEFT:
                MapPart leftPart = mapPart.getLeft();

                if (leftPart instanceof Box) {
                    movePart(keyCode, leftPart);
                }
                leftPart = mapPart.getLeft();
                if (mapPart.getLeft() instanceof Floor) {
                    mapParts[mapPart.getPosition().row][mapPart.getPosition().column - 1] = mapPart;
                    mapParts[mapPart.getPosition().row][mapPart.getPosition().column] = leftPart;
                    switchPositions(mapPart, leftPart);
                }
                break;
            case RIGHT:
                MapPart rightPart = mapPart.getRight();

                if (rightPart instanceof Box) {
                    movePart(keyCode, rightPart);
                }
                rightPart = mapPart.getRight();
                if (mapPart.getRight() instanceof Floor) {

                    mapParts[mapPart.getPosition().row][mapPart.getPosition().column + 1] = mapPart;
                    mapParts[mapPart.getPosition().row][mapPart.getPosition().column] = rightPart;
                    switchPositions(mapPart, rightPart);
                }
                break;
        }
        reloadNeighbours();
    }

    private void switchPositions(MapPart firstPart, MapPart secondPart) {
        Position firstPartPosition = firstPart.getPosition();
        Position secondPartPosition = secondPart.getPosition();
        firstPart.setPosition(secondPartPosition);
        secondPart.setPosition(firstPartPosition);
    }

    private void reloadNeighbours() {
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
