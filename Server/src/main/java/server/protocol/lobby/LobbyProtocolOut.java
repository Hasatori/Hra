package server.protocol.lobby;

/**
 * Handles output from LobbyProtocol messages.
 */
public class LobbyProtocolOut {

    LobbyProtocolOut() {}

    public String playerHasLeft() {
        return LobbyProtocol.MSG_PREFIX + ":PLAYER HAS LEFT";
    }

    public String playerKicked() {
        return LobbyProtocol.MSG_PREFIX + ":YOU WERE KICKED OUT OF THE LOBBY";
    }

    public String setMap(String mapName) {
        return LobbyProtocol.MSG_PREFIX + ":SET MAP " + mapName;
    }

    public String startGame() {
        return LobbyProtocol.MSG_PREFIX + ":START";
    }

    public String connectedSecondPlayer(String secondPlayerName) {
        return LobbyProtocol.MSG_PREFIX + ":CONNECTED PLAYER " + secondPlayerName;
    }

    public String lobbyDeleted() {
        return LobbyProtocol.MSG_PREFIX + ":LOBBY DELETED";
    }
    
    public String sendLobbyMessage(String msg) {
    	return msg;
    }
}
