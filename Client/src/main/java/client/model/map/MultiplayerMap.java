package client.model.map;

import com.sun.javafx.scene.traversal.Direction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class MultiplayerMap extends Map {
    private HashMap<String, Player> players = new HashMap<>();
    private List<Target> conditionTargets = new LinkedList<>();
    private Player currentPlayer, remotePlayer;

    public MultiplayerMap(String mapName, int currentPlayerNumber, int remotePlayerNumber, String currentPlayerName, String remotePlayerName) {
        super(mapName, "plans/multiplayer/");
        setPlayerAndTargets(currentPlayerNumber, currentPlayerName, remotePlayerName);
        System.out.println("Current player number : " + currentPlayerNumber);
        System.out.println("Current player name : " + currentPlayerName + "=" + currentPlayer.getName());
        System.out.println("Remote player number : " + remotePlayerNumber);
        System.out.println("Remote player name : " + remotePlayer.getName());

        conditionTargets.forEach(target -> {
            System.out.println("Contiditon target: " + target.getInitialPosition().toString());
        });
    }

    private void setPlayerAndTargets(int currentPlayerNumber, String currentPlayerName, String remotePlayerName) {
        int targetCount = 0;
        int playerCount = 0;
        for (int row = 0; row < mapParts.length; row++) {
            for (int column = 0; column < mapParts[row].length; column++) {
                if (mapParts[row][column] instanceof Player) {
                    if (playerCount == currentPlayerNumber) {
                        this.currentPlayer = (Player) mapParts[row][column];
                        this.currentPlayer.setName(currentPlayerName);
                        players.put(currentPlayerName, currentPlayer);
                    } else {
                        this.remotePlayer = (Player) mapParts[row][column];
                        this.remotePlayer.setName(remotePlayerName);
                        players.put(remotePlayerName, remotePlayer);
                    }
                    playerCount++;
                }
                if (mapParts[row][column] instanceof Target) {
                    ++targetCount;
                    if (currentPlayerNumber == 1 &&column>(mapParts[row].length/2)) {
                        conditionTargets.add((Target) mapParts[row][column]);
                    }
                    if (currentPlayerNumber == 0 &&column<(mapParts[row].length/2)) {
                        conditionTargets.add((Target) mapParts[row][column]);
                    }
                    targets.add((Target) mapParts[row][column]);
                }
            }
        }
    }

    @Override
    public void movePlayer(Direction direction, String name) {
        players.get(name).setDirection(direction);
        movePart(direction, players.get(name));
    }

    @Override
    public boolean checkWinCondition() {
        for (Target target : conditionTargets) {
            if (!target.isCovered()) {
                return false;
            }
        }
        return true;
    }

}
