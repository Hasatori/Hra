package client.model.map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import client.util.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class LevelLoader {
    private final Logger LOGGER = LoggerFactory.getLogger(LevelLoader.class);
    private int numberOfWalls, numberOfPlayers, numberOfBoxes, numberOfTargets, numberOFFloors = 0;
    private static final char WALL_SIGN = 'x';
    private static final char FLOOR_SIGN = '*';
    private static final char PLAYER_SIGN = '-';
    private static final char BOX_SIGN = '+';
    private static final char TARGET_SIGN = '/';
    private static final char DOOR_SIGN = '^';
    private int columns, rows;
    private MapPart[][] mapParts;
    private String levelName;
    private String toMap;

    LevelLoader() {

    }

    MapPart[][] load(String path) {
        try {
            LOGGER.info("Loading map {}", path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceLoader.getResourceAsInputStream(path)));
            String line;
            String size = reader.readLine().split("=")[1];
            columns = Integer.valueOf(size.split("x")[0]);
            rows = Integer.valueOf(size.split("x")[1]);
            mapParts = new MapPart[rows][columns];

            line = reader.readLine();
            String[] parts = line.split("-");
            if (isMultilevel(line)) {
                toMap = parts[1];
            }
            levelName = parts[0];
            int rowNum = 0;
            while ((line = reader.readLine()) != null) {
                this.createMatrices(line, rowNum);
                ++rowNum;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.toString());
        return mapParts;
    }

    private boolean isMultilevel(String line) {
        return line.split("-").length == 2;
    }

    private char getSign(String value) {
        return value.split("=")[1].toCharArray()[0];
    }

    private void createMatrices(String line, int rowNum) {
        char[] signs = line.toCharArray();
        for (int i = 0; i < signs.length; i++) {
            Position position = new Position(rowNum, i);
            if (signs[i] == BOX_SIGN) {
                ++numberOfBoxes;
                mapParts[rowNum][i] = new Box(position);
            } else if (signs[i] == WALL_SIGN) {
                ++numberOfWalls;
                mapParts[rowNum][i] = new Wall(position);

            } else if (signs[i] == FLOOR_SIGN) {
                ++numberOFFloors;
                mapParts[rowNum][i] = new Floor(position);
            } else if (signs[i] == PLAYER_SIGN) {
                ++numberOfPlayers;
                mapParts[rowNum][i] = new Player(position);
            } else if (signs[i] == TARGET_SIGN) {
                try {
                    mapParts[rowNum][i] = new Target(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ++numberOfTargets;
            } else if (signs[i] == DOOR_SIGN) {
                mapParts[rowNum][i] = new Door(position, this.levelName, this.toMap);
            }
        }
    }

    @Override
    public String toString() {
        return "\nLevel name:" + this.levelName + "\n" +
                "Number of players: " + numberOfPlayers + "\n" +
                "Number of boxes: " + numberOfBoxes + "\n" +
                "Number of walls: " + numberOfWalls + "\n" +
                "Number of floors:" + numberOFFloors + "\n" +
                "Number of targets: " + numberOfTargets + "\n";
    }
}
