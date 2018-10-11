package root.server.main.protocol.lobby;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import root.server.main.protocol.general.GeneralProtocol;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class LobbyProtocolOut {
    LobbyProtocolOut() {
    }

    public String playerHasLeft() {
        return LobbyProtocol.messagePrefix + ":PLAYER HAS LEFT";
    }

    public String playerKicked() {
        return LobbyProtocol.messagePrefix + ":YOU WERE KICKED OUT OF THE LOBBY";
    }

    public String setMap(String mapName) {
        return LobbyProtocol.messagePrefix + ":SET MAP " + mapName;
    }

    public String startGame() {
        return LobbyProtocol.messagePrefix + ":START";
    }

    public String connectedSecondPlayer(String secondPlayerName) {
        return LobbyProtocol.messagePrefix + ":CONNECTED PLAYER " + secondPlayerName;
    }

    public String lobbyDeleted() {
        return LobbyProtocol.messagePrefix + ":LOBBY DELETED";
    }
}
