package client.model.map;

import com.sun.javafx.scene.traversal.Direction;

class SingleplayerMap extends Map {

    private Player player;

    public SingleplayerMap(String name, String playerName) {
        super(name, "plans/singleplayer/");
        setPlayerAndTargets();
        this.player.setName(playerName);
    }


    private void setPlayerAndTargets() {
        for (int row = 0; row < mapParts.length; row++) {
            for (int column = 0; column < mapParts[row].length; column++) {
                if (mapParts[row][column] instanceof Player) {
                    this.player = (Player) mapParts[row][column];
                }
                if (mapParts[row][column] instanceof Target) {
                    targets.add((Target) mapParts[row][column]);
                }
            }
        }
    }

    @Override
    public void movePlayer(Direction direction, String name) {
        player.setDirection(direction);
        this.movePart(direction, player);
    }

    @Override
    public boolean checkWinCondition() {
        for (Target target : targets) {
            if (!target.isCovered()) {
                return false;
            }
        }
        return true;
    }
}
