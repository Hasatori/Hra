package client.model.protocol.lobby;

/**
 * Handles input from LobbyProtocol messages.
 */
public class LobbyProtocolIn {
    private final String message;
    private static final String SENT_MESSAGE = "SENT MESSAGE";
    private static final String LOBBY_PLAYER_LEFT = "PLAYER HAS LEFT";
    private static final String LOBBY_KICKED = "YOU WERE KICKED OUT OF THE LOBBY";
    private static final String LOBBY_SET_MAP = "SET MAP ";
    private static final String LOBBY_START = "START";
    private static final String LOBBY_PLAYER_CONNECTED = "CONNECTED PLAYER ";
    private static final String LOBBY_DELETED = "LOBBY DELETED";

    LobbyProtocolIn(String message) {
        this.message = message;
    }

    public boolean playerHasLeft() {
        return message.equals(LOBBY_PLAYER_LEFT);
    }

    public boolean kicked() {
        return message.equals(LOBBY_KICKED);
    }

    public boolean setMap() {
        return message.matches(LOBBY_SET_MAP + "\\w+");
    }

    public String getMap() {
        return message.replace(LOBBY_SET_MAP, "");
    }

    public boolean start() {
        return message.equals(LOBBY_START);
    }

    public boolean playerConnected() {
        return message.contains(LOBBY_PLAYER_CONNECTED);
    }

    public String getSecondPlayerName() {
        return message.replace(LOBBY_PLAYER_CONNECTED, "");
    }

    public boolean lobbyDeleted() {
        return message.equals(LOBBY_DELETED);
    }
    
    public boolean messageSent() {
        return message.startsWith(SENT_MESSAGE);
    }
}
