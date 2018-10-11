package root.server.protocol.lobby;

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
