package client.model.map;

import client.util.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MapFactory {
    private final Logger LOGGER = LoggerFactory.getLogger(MapFactory.class);
    private static final MapFactory INSTANCE = new MapFactory();

    private MapFactory() {

    }

    public static MapFactory getInstance() {
        return INSTANCE;
    }

    public Map getMap(String mapName, String playerName) {
        if (containsMap(mapName, false)) {
            return new SingleplayerMap(mapName, playerName);
        }
        throw new IllegalArgumentException("Map with the name " + mapName + " does not exist");
    }

    public Map getMap(String mapName, int currentPlayerNumber, int remotePlayerNumber, String currentPlayerName, String remotePlayerName) {
        if (containsMap(mapName, true)) {
            return new MultiplayerMap(mapName, currentPlayerNumber, remotePlayerNumber, currentPlayerName, remotePlayerName);
        }
        throw new IllegalArgumentException("Map with the name " + mapName + " does not exist");
    }

    private boolean containsMap(String mapName, boolean multiplayer) {
        try {
            List<String> mapNames;
            if (multiplayer) {
                mapNames = ResourceLoader.getMultiplayerMaps();
            } else {
                mapNames = ResourceLoader.getSingleplayerMaps();
            }
            if (mapNames.contains(mapName)) {
                return true;
            }
        } catch (IOException e) {
            LOGGER.error("Error while loading maps {}", e);
        }
        return false;
    }

}
